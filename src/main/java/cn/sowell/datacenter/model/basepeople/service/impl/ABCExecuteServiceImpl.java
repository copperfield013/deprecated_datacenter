package cn.sowell.datacenter.model.basepeople.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.excel.poi.PoiCellReader;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.people.status.ImportStatus;

import com.abc.application.DataSource;
import com.abc.application.PeopleFusion;
import com.abc.application.PeopleRelationFusion;
import com.abc.application.PeopleRemoveFusion;
import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.SocialEntity;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.people.PeopleRelation;
import com.abc.people.RelationShip;
import com.abc.query.querypeople.Criteria;
import com.abc.query.querypeople.impl.SortedPagedQuery;

@Service
public class ABCExecuteServiceImpl implements ABCExecuteService{

	private String mapperName = "baseinfoImport";
	private Logger logger = Logger.getLogger(ABCExecuteService.class);
	@Resource
	private PeopleFusion peopleFusion;
	
	@Resource
	MappingNodeAnalysis analysis;
	
	@Resource
	PeopleRelationFusion relationFusion;
	
	@Deprecated
	private ABCNode abcNode;
	@Deprecated
	private ABCNode importNode;
	private String writeMappingName = "baseinfoImport";
	
	@Resource
	private PeopleRemoveFusion peopleRemoveFusion;
	
	private ABCNode getABCNode() throws RuntimeException{
		if(abcNode == null){
			ClassPathResource resouce = new ClassPathResource("mapping/baseinfoImport.xml");
			try {
				abcNode = analysis.analysis(resouce.getInputStream());
				abcNode.selectNameAsTitle();
			} catch (Exception e) {
				logger.error("解析abcNode配置文件时发生异常", e);
				throw new RuntimeException(e);
			}
		}
		return abcNode;
	}
	
	private ABCNode getImportNode(){
		if(importNode == null){
			ClassPathResource resouce = new ClassPathResource("mapping/baseinfoImport.xml");
			try {
				importNode = analysis.analysis(resouce.getInputStream());
				importNode.selectAliasAsTitle();
			} catch (Exception e) {
				logger.error("解析abcNode配置文件时发生异常", e);
				throw new RuntimeException(e);
			}
		}
		return importNode;
	}
	
	
	@Override
	public SocialEntity createSocialEntity(Map<String, String> data) {
		Assert.notNull(data);
		SocialEntity entity = new SocialEntity(mapperName);
		for (Entry<String, String> entry : data.entrySet()) {
			entity.setValue(entry.getKey(), entry.getValue());
		}
		return entity;
	}
	
	@Override
	public People mergePeople(Map<String, String> data) throws IOException {
		SocialEntity socialEntity = createSocialEntity(data);
		People people = createPeople(getABCNode(), socialEntity);
		people = peopleFusion.fuseStrange(people, writeMappingName, DataSource.SOURCE_EDIT);
		logger.debug(people.getJson(getABCNode().getTitle()));
		return people;
	}
	
	private People createPeople(ABCNode abcNode, SocialEntity socialEntity) {
		People people = new People();
		people.addMapping(abcNode);
		people.addEntity(socialEntity);
		logger.debug(people.getJson(mapperName));
		return people;
	}
	
	@Override
	public List<People> queryPeopleList(List<Criteria> criterias, PageInfo pageInfo){
		SortedPagedQuery sortedPagedQuery = new SortedPagedQuery(criterias, getABCNode(), null);
		sortedPagedQuery.setPageSize(pageInfo.getPageSize());
		pageInfo.setCount(sortedPagedQuery.getAllCount());
		return sortedPagedQuery.visit(pageInfo.getPageNo());
	}
	
	@Override
	public List<People> queryPeopleList(Function<ABCNode, List<Criteria>> handler, PageInfo pageInfo){
		return queryPeopleList(handler.apply(getABCNode()), pageInfo);
	}
	
	
	@Override
	public People getPeople(String peopleCode) {
		SortedPagedQuery sortedPagedQuery = new SortedPagedQuery(null, getABCNode(), null);
		People people = sortedPagedQuery.visit(peopleCode);
		people.addMapping(getABCNode());
		return people;
	}
	
	@Override
	public void importPeople(Sheet sheet) {
		try {
			importPeople(sheet, new ImportStatus());
		} catch (ImportBreakException e) {
		}
	}
	
	@Override
	public void importPeople(Sheet sheet, ImportStatus importStatus) throws ImportBreakException{
		Row headerRow = sheet.getRow(1);
		execute(sheet, headerRow, getImportNode(), importStatus);
	}
	
	private NumberFormat numberFormat = new DecimalFormat("0.000");
	
	
	protected void execute(Sheet sheet, Row headerRow, ABCNode abcNode, ImportStatus importStatus) throws ImportBreakException {
		importStatus.appendMessage("正在计算总行数");
		importStatus.setTotal(colculateRowCount(sheet));
		int rownum = 2;
		importStatus.appendMessage("开始导入");
		while(true){
			if(importStatus.breaked()){
				throw new ImportBreakException();
			}
			Row row = sheet.getRow(rownum++);
			if(row == null || row.getCell(0) == null || !TextUtils.hasText(row.getCell(0).getStringCellValue())){
				break;
			}
			importStatus.setCurrent(rownum - 2);
			importStatus.appendMessage("导入第" + importStatus.getCurrent() + "条数据");
			SocialEntity socialEntity = createSocialEntity(abcNode, headerRow, row);
			People people = createPeople(sheet, headerRow, abcNode, socialEntity);
			List<PeopleRelation> peopleRelations = createPeopleRelation(abcNode, people, socialEntity);
			people = peopleFusion.fuseStrange(people, writeMappingName, DataSource.SOURCE_POLIC);
			logger.debug(people.getPeopleCode() + " : " + people.getJson(abcNode.getTitle()));
			people = relationFusion.fuse(people,peopleRelations, writeMappingName, DataSource.SOURCE_POLIC);
			if(people != null && people.getRelationShip() != null){
				Collection<RelationShip> ships = people.getRelationShip();
				for (RelationShip ship : ships) {
					logger.debug(ship);
				}
			}
			importStatus.appendMessage("第" + importStatus.getCurrent() + "条数据导入完成，用时" + numberFormat.format(importStatus.lastInterval()));
		}
		importStatus.appendMessage("导入完成");
		importStatus.setEnded();
	}

	private Integer colculateRowCount(Sheet sheet) {
		int rownum = 2;
		Row row;
		do {
			row = sheet.getRow(rownum++);
		} while (row != null && row.getCell(0) != null && TextUtils.hasText(row.getCell(0).getStringCellValue()));
		return rownum - 3;
	}

	protected  People createPeople(Sheet sheet, Row headerRow, ABCNode abcNode,
			SocialEntity socialEntity) {
		People people = new People();
		people.addMapping(abcNode);
		people.addEntity(socialEntity);
		logger.debug(people.getJson(mapperName));
		return people;
	}

	protected  List<PeopleRelation> createPeopleRelation(ABCNode abcNode,
			People people, SocialEntity socialEntity) {
		List<PeopleRelation> peopleRelations = new ArrayList<PeopleRelation>();
		for (String key : socialEntity.getRelationKeys()) {
			List<Entity> entitys = socialEntity.getRelations(key);
			PeopleRelation peopleRelation = new PeopleRelation(
					abcNode.getRelation(key));
			peopleRelation.setHost(people);
			for (Entity entity : entitys) {
				peopleRelation.addRelated(entity);
			}
			peopleRelations.add(peopleRelation);
		}
		for (PeopleRelation relation : peopleRelations) {
			logger.debug(relation.getJson());
		}
		return peopleRelations;
	}

	protected  SocialEntity createSocialEntity(ABCNode abcNode, Row headerRow,
			Row row) {
		SocialEntity entity = new SocialEntity(mapperName);
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			PoiCellReader reader = new PoiCellReader(cell);
			String value = reader.getString();
			if (headerRow.getCell(i).getStringCellValue().equals("家庭医生")) {
				if (TextUtils.hasText(value)) {
					Entity relationentity = new Entity("familydoctor");
					relationentity.setValue(headerRow.getCell(i)
							.getStringCellValue(), value);
					entity.addRelation("家庭医生", relationentity);
				}
			} else {
				if(cell.getCellTypeEnum() == CellType.ERROR) {
					logger.warn("ERROR Type row number:" + row.getRowNum()
							+ " ; cell number:" + i + ";");
				}else{
					entity.setValue(headerRow.getCell(i).getStringCellValue(), value);
				}
			}
		}
		return entity;
	}
	
	@Override
	public void deletePeople(String peopleCode) {
		if(!peopleRemoveFusion.remove(peopleCode, null, "test")){
			throw new RuntimeException("删除失败");
		}
	}
	
	
}

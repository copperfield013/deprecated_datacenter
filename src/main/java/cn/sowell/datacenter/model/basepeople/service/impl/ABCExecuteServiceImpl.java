package cn.sowell.datacenter.model.basepeople.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.excel.CellTypeUtils;
import cn.sowell.copframe.utils.excel.poi.PoiCellReader;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.peopledata.status.ImportStatus;

import com.abc.application.ComplexFusion;
import com.abc.application.DataSource;
import com.abc.application.PeopleRelationFusion;
import com.abc.application.PeopleRemoveFusion;
import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RecordEntity;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.people.PeopleRelation;
import com.abc.people.PeopleTracker;
import com.abc.query.criteria.Criteria;
import com.abc.query.criteria.SortedCriteria;
import com.abc.query.people.impl.PeopleSortedPagedQuery;

@Service
public class ABCExecuteServiceImpl implements ABCExecuteService{

	private String mapperName = "baseinfoImport";
	private Logger logger = Logger.getLogger(ABCExecuteService.class);
	@Resource
	private ComplexFusion cFusion;
	
	@Resource
	MappingNodeAnalysis analysis;
	
	@Resource
	PeopleRelationFusion relationFusion;
	private DateFormat defaultDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
	
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
	public Entity createSocialEntity(Map<String, String> data) {
		Assert.notNull(data);
		Entity entity = new Entity(mapperName);
		for (Entry<String, String> entry : data.entrySet()) {
			entity.putValue(entry.getKey(), entry.getValue());
		}
		return entity;
	}
	
	@Override
	public People mergePeople(Map<String, String> data) throws IOException {
		Entity socialEntity = createSocialEntity(data);
		People people = createPeople(getABCNode(), socialEntity);
		
		List<PeopleRelation> peopleRelations = createPeopleRelation(getABCNode(), people, socialEntity);
		people = cFusion.fuse(people,writeMappingName, DataSource.SOURCE_POLIC);
		logger.debug(people.getPeopleCode() + " : " + people.getJson(getABCNode().getTitle()));
		people = relationFusion.fuse(people, peopleRelations, writeMappingName, DataSource.SOURCE_POLIC);
		return people;
	}
	
	private People createPeople(ABCNode abcNode, Entity socialEntity) {
		People people = new People();
		people.addMapping(abcNode);
		people.addEntity(socialEntity);
		logger.debug(people.getJson(mapperName));
		return people;
	}
	
	@Override
	public List<People> queryPeopleList(List<Criteria> criterias, PageInfo pageInfo){
		PeopleSortedPagedQuery sortedPagedQuery = new PeopleSortedPagedQuery(criterias, getABCNode(), "编辑时间", SortedCriteria.TYPE_DESC);
		sortedPagedQuery.setPageSize(pageInfo.getPageSize());
		pageInfo.setCount(sortedPagedQuery.getAllCount());
		List<People> peoples = sortedPagedQuery.visit(pageInfo.getPageNo());
		return peoples;
	}
	
	@Override
	public List<People> queryPeopleList(Function<ABCNode, List<Criteria>> handler, PageInfo pageInfo){
		return queryPeopleList(handler.apply(getABCNode()), pageInfo);
	}
	
	
	@Override
	public People getPeople(String peopleCode) {
		PeopleSortedPagedQuery sortedPagedQuery = new PeopleSortedPagedQuery(null, getABCNode(), null);
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
			if(row == null || row.getCell(0) == null || !TextUtils.hasText(getStringWithBlank(row.getCell(0)))){
				break;
			}
			importStatus.setCurrent(rownum - 2);
			importStatus.startItemTimer().appendMessage("导入第" + importStatus.getCurrent() + "条数据");
			try {
				Entity socialEntity = createSocialEntity(abcNode, headerRow, row);
				People people = createPeople(sheet, headerRow, abcNode, socialEntity);
				List<PeopleRelation> peopleRelations = createPeopleRelation(abcNode, people, socialEntity);
				people = cFusion.fuse(people, writeMappingName, DataSource.SOURCE_POLIC);
				logger.debug(people.getPeopleCode() + " : " + people.getJson(abcNode.getTitle()));
				people = relationFusion.fuse(people,peopleRelations, writeMappingName, DataSource.SOURCE_POLIC);
				importStatus.endItemTimer().appendMessage("第" + importStatus.getCurrent() + "条数据导入完成，用时" + numberFormat.format(importStatus.lastInterval()));
			} catch (Exception e) {
				logger.error("导入第" + rownum + "行时发生异常", e);
				importStatus.endItemTimer().appendMessage("第" + importStatus.getCurrent() + "条数据导入异常，用时" + numberFormat.format(importStatus.lastInterval()));
			}
		}
		importStatus.appendMessage("导入完成");
		importStatus.setEnded();
	}

	private Integer colculateRowCount(Sheet sheet) {
		int rownum = 2;
		Row row;
		do {
			row = sheet.getRow(rownum++);
		} while (row != null && row.getCell(0) != null && TextUtils.hasText(getStringWithBlank(row.getCell(0))));
		return rownum - 3;
	}

	protected  People createPeople(Sheet sheet, Row headerRow, ABCNode abcNode,
			Entity socialEntity) {
		People people = new People();
		people.addMapping(abcNode);
		people.addEntity(socialEntity);
		logger.debug(people.getJson(mapperName));
		return people;
	}

	protected  List<PeopleRelation> createPeopleRelation(ABCNode abcNode,
			People people, Entity socialEntity) {
		List<PeopleRelation> peopleRelations = new ArrayList<PeopleRelation>();
		for (String key : socialEntity.getRelationNames()) {
			List<RecordEntity> entitys = socialEntity.getRelations(key);
			PeopleRelation peopleRelation = new PeopleRelation(
					abcNode.getRelation(key));
			peopleRelation.setHost(people);
			for (RecordEntity entity : entitys) {
				peopleRelation.addRelated(entity.getEntity());
			}
			peopleRelations.add(peopleRelation);
		}
		for (PeopleRelation relation : peopleRelations) {
			logger.debug(relation.getJson());
		}
		return peopleRelations;
	}

	protected  Entity createSocialEntity(ABCNode abcNode, Row headerRow,
			Row row) {
		Entity entity = new Entity(mapperName);
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			PoiCellReader reader = new PoiCellReader(cell);
			String value = reader.getString();
			if (getStringWithBlank(headerRow.getCell(i)).equals("家庭医生")) {
				if (TextUtils.hasText(value)) {
					Entity relationentity = new Entity("familydoctor");
					relationentity.putValue(getStringWithBlank(headerRow.getCell(i)), value);
					entity.putRelationEntity("家庭医生信息", "家庭医生", relationentity);
				}
			} else {
				if(cell.getCellTypeEnum() == CellType.ERROR) {
					logger.warn("ERROR Type row number:" + row.getRowNum()
							+ " ; cell number:" + i + ";");
				}else{
					entity.putValue(getStringWithBlank(headerRow.getCell(i)), value);
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
	
	@Override
	public People getHistoryPeople(String peopleCode, Date date) {
		PeopleTracker peopleTracker=new PeopleTracker(peopleCode,date);
		People people=peopleTracker.getPeople();
		if(people != null){
			people.addMapping(getABCNode());
		}
		return people;
	}
	
	private String getStringWithBlank(Cell cell){
		//如果有覆盖至
		if(cell == null){
			return null;
		}
		CellType cellType = cell.getCellTypeEnum();
		if(cellType == CellType.STRING){
			return cell.getStringCellValue();
		}else if(cellType == CellType.NUMERIC){
			if(CellTypeUtils.isCellDateFormatted(cell)){
				return defaultDateFormat.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
			}
			return FormatUtils.toString(FormatUtils.toLong(cell.getNumericCellValue()));
		}else if(cellType == CellType.FORMULA){
			FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			CellValue cellValue = evaluator.evaluate(cell);
			CellType cellValueType = cellValue.getCellTypeEnum();
			if(cellValueType == CellType.STRING){
				return cellValue.getStringValue();
			}else if(cellValueType == CellType.NUMERIC){
				return FormatUtils.toString(FormatUtils.toLong(cellValue.getNumberValue()));
			}
			return null;
		}
		return null;
	}
	
}

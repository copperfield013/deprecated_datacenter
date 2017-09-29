package cn.sowell.datacenter.model.basepeople.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.excel.CellTypeUtils;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.peopledata.status.ImportStatus;

import com.abc.application.ApplicationInfo;
import com.abc.mapping.entity.Entity;
import com.abc.panel.Discoverer;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;
import com.abc.query.criteria.Criteria;
import com.abc.query.entity.impl.EntitySortedPagedQuery;
import com.abc.record.HistoryTracker;

@Service
public class ABCExecuteServiceImpl implements ABCExecuteService{

	private static final String IMPORT_NODE_NAME = "baseinfoImport";
	private static final String BASE_NODE_NAME = "baseinfoImport";
	private Logger logger = Logger.getLogger(ABCExecuteService.class);
	
	private DateFormat defaultDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
	
	
	
	
	@Override
	public Entity createEntity(Map<String, String> data) {
		Assert.notNull(data);
		Entity entity = new Entity(BASE_NODE_NAME);
		for (Entry<String, String> entry : data.entrySet()) {
			entity.putValue(entry.getKey(), entry.getValue());
		}
		return entity;
	}
	
	@Override
	public Entity mergePeople(Map<String, String> data) throws IOException {
		Entity entity = createEntity(data);
		
		ApplicationInfo appInfo=new ApplicationInfo();
		appInfo.setWriteMappingName(BASE_NODE_NAME);
		appInfo.setSource(ApplicationInfo.SOURCE_COMMON);
		Integration integration=PanelFactory.getIntegration();
		integration.integrate(entity, appInfo);
		return entity;
		
	}
	
	@Override
	public List<Entity> queryPeopleList(List<Criteria> criterias, PageInfo pageInfo){
		Discoverer discoverer=PanelFactory.getDiscoverer(BASE_NODE_NAME);
		
		EntitySortedPagedQuery sortedPagedQuery = discoverer.discover(criterias, null);
		
		sortedPagedQuery.setPageSize(pageInfo.getPageSize());
		pageInfo.setCount(sortedPagedQuery.getAllCount());
		List<Entity> peoples = sortedPagedQuery.visit(pageInfo.getPageNo());
		return peoples;
	}
	
	@Override
	public List<Entity> queryPeopleList(Function<String, List<Criteria>> handler, PageInfo pageInfo){
		return queryPeopleList(handler.apply(BASE_NODE_NAME), pageInfo);
	}
	
	
	@Override
	public Entity getPeople(String peopleCode) {
		Discoverer discoverer = PanelFactory.getDiscoverer(BASE_NODE_NAME);
		Entity result=discoverer.discover(peopleCode);
		return result;
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
		execute(sheet, headerRow, IMPORT_NODE_NAME, importStatus);
	}
	
	private NumberFormat numberFormat = new DecimalFormat("0.000");
	
	
	protected void execute(Sheet sheet, Row headerRow, String mapperName, ImportStatus importStatus) throws ImportBreakException {
		importStatus.appendMessage("正在计算总行数");
		importStatus.setTotal(colculateRowCount(sheet));
		int rownum = 2;
		importStatus.appendMessage("开始导入");
		Integration integration=PanelFactory.getIntegration();
		ApplicationInfo appInfo=new ApplicationInfo();
		appInfo.setWriteMappingName(BASE_NODE_NAME);
		appInfo.setSource(ApplicationInfo.SOURCE_COMMON);
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
				Entity entity = createEntity(mapperName, headerRow, row);
				integration.integrate(entity, appInfo);
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


	protected String familyDoctorMapper = "familydoctor";
	private Entity createEntity(String mappingName, Row headerRow,
			Row row) {
		Entity entity = new Entity(mappingName);
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			if (headerRow.getCell(i).getStringCellValue().equals("家庭医生")) {
				if (cell.getStringCellValue() != null
						&& !cell.getStringCellValue().equals("")) {
					Entity relationentity = new Entity(familyDoctorMapper);
					relationentity.putValue("姓名", cell.getStringCellValue());
					entity.putRelationEntity("医疗信息","家庭医生", relationentity);
				}
			} else {
				if (cell.getCellTypeEnum() == CellType.NUMERIC) {
					entity.putValue(headerRow.getCell(i).getStringCellValue(),
							String.valueOf(cell.getNumericCellValue()));
				} else if (cell.getCellTypeEnum() == CellType.ERROR) {
					logger.warn("ERROR Type row number:" + row.getRowNum()
							+ " ; cell number:" + i + ";");
				} else {
					entity.putValue(headerRow.getCell(i).getStringCellValue(),
							cell.getStringCellValue());
				}
			}
		}
		
		
		return entity;
	}
	
	@Override
	public void deletePeople(String peopleCode) {
		ApplicationInfo appInfo=new ApplicationInfo("a526bd2fa93b4375a5b76506b8651a33", null, "test");
		if(!PanelFactory.getIntegration().remove(appInfo)){
			throw new RuntimeException("删除失败");
		}
	}
	
	@Override
	public Entity getHistoryPeople(String peopleCode, Date date) {
		Discoverer discoverer=PanelFactory.getDiscoverer(BASE_NODE_NAME);
		
		HistoryTracker tracker = discoverer.track(peopleCode, date);
		return tracker.getEntity();
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

package cn.sowell.datacenter.model.basepeople.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.stereotype.Service;

import com.abc.application.ApplicationInfo;
import com.abc.dto.ErrorInfomation;
import com.abc.mapping.entity.Entity;
import com.abc.panel.Discoverer;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;
import com.abc.query.criteria.Criteria;
import com.abc.query.entity.impl.EntitySortedPagedQuery;
import com.abc.record.HistoryTracker;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.excel.CellTypeUtils;
import cn.sowell.datacenter.model.basepeople.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.pojo.ExcelModel;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleItemEntity;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.impl.EntityTransfer;
import cn.sowell.datacenter.model.peopledata.status.ImportStatus;

@Service
public class ABCExecuteServiceImpl implements ABCExecuteService{

	private static final String IMPORT_NODE_NAME = "baseinfoImport";
	private static final String BASE_NODE_NAME = "baseinfoImport";
	private Logger logger = Logger.getLogger(ABCExecuteService.class);
	
	private DateFormat defaultDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
	
	EntityTransfer eTransfer = new EntityTransfer();
	
	
	/*@Override
	public Entity createEntity(Map<String, String> data) {
		Assert.notNull(data);
		Entity entity = new Entity(BASE_NODE_NAME);
		for (Entry<String, String> entry : data.entrySet()) {
			entity.putValue(entry.getKey(), entry.getValue());
		}
		Entity workExperience = new Entity("workExperience");
		workExperience.putValue("companyName", data.get("companyName"));
		
		entity.putRecordEntity("workExperience", "工作经历", workExperience);
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
		
	}*/
	
	@Override
	public List<Entity> queryPeopleList(List<Criteria> criterias, PageInfo pageInfo){
		Discoverer discoverer=PanelFactory.getDiscoverer(BASE_NODE_NAME);
		
		EntitySortedPagedQuery sortedPagedQuery = discoverer.discover(criterias, "编辑时间");
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
	public void importPeople(Sheet sheet, String dataType) {
		try {
			importPeople(sheet, new ImportStatus(), dataType);
		} catch (ImportBreakException e) {
		}
	}
	
	
	@SuppressWarnings("serial")
	Map<String, String> dataTypeMap = new HashMap<String, String>(){
		{
			put("base", "importBase");
			put("handicapped", "importHandicapped");
			put("lowincome", "importLowincome");
			put("familyPlanning", "importFamilyPlanning");
		}
	};
	
	@Override
	public void importPeople(Sheet sheet, ImportStatus importStatus, String dataType) throws ImportBreakException{
		Row headerRow = sheet.getRow(1);
		String writeMapperName = dataTypeMap.get(dataType);
		if(writeMapperName != null){
			execute(sheet, headerRow, IMPORT_NODE_NAME, writeMapperName, importStatus);
		}
	}
	
	private NumberFormat numberFormat = new DecimalFormat("0.000");
	
	
	protected void execute(Sheet sheet, Row headerRow, String mapperName, String writeMapperName, ImportStatus importStatus) throws ImportBreakException {
		importStatus.appendMessage("正在计算总行数");
		importStatus.setTotal(colculateRowCount(sheet));
		int rownum = 2;
		importStatus.appendMessage("开始导入");
		Integration integration=PanelFactory.getIntegration();
		ApplicationInfo appInfo=new ApplicationInfo();
		appInfo.setWriteMappingName(writeMapperName);
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
				Entity entity = createImportEntity(mapperName, headerRow, row);
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


	private Entity createImportEntity(String mappingName, Row headerRow,
			Row row) {
		Entity entity = new Entity(mappingName, true);
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			entity.putValue(getStringWithBlank(headerRow.getCell(i)), getStringWithBlank(cell));
		}
		
		return entity;
	}
	
	@Override
	public void deletePeople(String peopleCode) {
		ApplicationInfo appInfo=new ApplicationInfo(peopleCode, null, "list-delete" );
		if(!PanelFactory.getIntegration().remove(appInfo)){
			throw new RuntimeException("删除失败");
		}
	}
	
	@Override
	public Entity getHistoryPeople(String peopleCode, Date date, List<ErrorInfomation> errors) {
		Discoverer discoverer=PanelFactory.getDiscoverer(BASE_NODE_NAME);
		
		HistoryTracker tracker = discoverer.track(peopleCode, date);
		if(errors != null){
			errors.addAll(tracker.getErrorInfomations());
		}
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
	
	@Override
	public Entity savePeople(PeopleData people) {
		Entity entity = new Entity(BASE_NODE_NAME);
		eTransfer.bind(people, entity);
		
		ApplicationInfo appInfo=new ApplicationInfo();
		appInfo.setWriteMappingName(BASE_NODE_NAME);
		appInfo.setSource(ApplicationInfo.SOURCE_COMMON);
		Integration integration=PanelFactory.getIntegration();
		integration.integrate(entity, appInfo);
		return entity;
	}
	
	@Override
	public Workbook downloadPeople(List<Map<String, Object>> listmap, List<TBasePeopleDictionaryEntity> keys,
			List<String[]> columnLists, ExcelModel model){		
		try {   
	          
	        FileInputStream fis = new FileInputStream("D:/demo.xls");  
			System.out.println("导入模板");
			
			// 创建excel工作簿
	        Workbook wb = new HSSFWorkbook(fis);
	        Sheet sheet = wb.getSheetAt(0);
            wb.setSheetName(0, model.getTitleName());
            //获得model样式
	        CellStyle titleStytle = sheet.getRow(0).getCell(0).getCellStyle();
	        CellStyle columnStytle = sheet.getRow(1).getCell(0).getCellStyle();
	        CellStyle listStytle = sheet.getRow(2).getCell(0).getCellStyle();
			//创建标题
            Cell title = sheet.createRow(0).createCell(0);
            title.setCellValue(model.getModelName());
            title.setCellStyle(titleStytle);
            //创建隐藏行
            Row row1 = sheet.createRow(1);
		    for(int i=0;i<keys.size();i++){
		    	sheet.setColumnWidth(i, 10*2*256);//设定宽度，一个数字256，一个汉字512
		    	Cell cell = row1.createCell(i);
		        cell.setCellValue(keys.get(i).getcCnEnglish());
		        }
		    row1.setZeroHeight(true);
			//创建列名
            Row row2 = sheet.createRow(2);
		    for(int i=0;i<keys.size();i++){
		    	Cell cell = row2.createCell(i);
		        cell.setCellValue(keys.get(i).getcCnName());
		        cell.setCellStyle(columnStytle);
		        }
			
		    //创建列表
			for (int i = 0; i < listmap.size(); i++) {
				Row row3 = sheet.createRow(i+3);
				for(int j=0;j<keys.size();j++){
					String key = keys.get(j).getcCnEnglish();
	                Cell cell = row3.createCell(j);
	                cell.setCellValue(listmap.get(i).get(key) == null?" ": listmap.get(i).get(key).toString());
	                cell.setCellStyle(listStytle);
	            }
			}
			for(int i = 0; i < columnLists.size(); i++){
				String[] columnList = columnLists.get(i);
				if(columnList.length!=0)
					sheet = setValidation(sheet,columnList, 2, listmap.size()+2, i, i);//设置下拉列表
			}
			
			//sheet = setPrompt(sheet, "姓名", "这是姓名",1, listmap.size(), 0, 0);//设置提示
			return wb;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
				
	}
	

	/** 
     * 设置单元格上提示 
     * @param sheet  要设置的sheet. 
     * @param promptTitle 标题 
     * @param promptContent 内容 
     * @param firstRow 开始行 
     * @param endRow  结束行 
     * @param firstCol  开始列 
     * @param endCol  结束列 
     * @return 设置好的sheet. 
     */  
    public Sheet setPrompt(Sheet sheet, String promptTitle,  
            String promptContent, int firstRow, int endRow ,int firstCol,int endCol)  {
    	// 构造constraint对象
    	DataValidationConstraint constraint = DVConstraint.createFormulaListConstraint("E1");
    	// 设置数据有效性加载在哪个单元格上
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);
		DataValidationHelper help = new HSSFDataValidationHelper((HSSFSheet) sheet);
		DataValidation validation = help.createValidation(constraint, regions);  
        validation.createPromptBox(promptTitle, promptContent);  
        sheet.addValidationData(validation);  
        return sheet;  
	}

	/** 
     * 设置某些列的值只能输入预制的数据,显示下拉框. 
     * @param sheet 要设置的sheet. 
     * @param list 下拉框显示的内容 
     * @param firstRow 开始行 
     * @param endRow 结束行 
     * @param firstCol 开始列 
     * @param endCol  结束列 
     * @return 设置好的sheet. 
     */  
	private Sheet setValidation(Sheet sheet, String[] list, int firstRow, int endRow, int firstCol, int endCol) {
		// 构造constraint对象
		DataValidationConstraint constraint = DVConstraint.createExplicitListConstraint(list);
		// 设置数据有效性加载在哪个单元格上
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);
		DataValidationHelper help = new HSSFDataValidationHelper((HSSFSheet) sheet);		
		DataValidation validation = help.createValidation(constraint, regions);
		validation.createErrorBox("输入值有误", "请从下拉框中选择");
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
        return sheet;  
	}

}

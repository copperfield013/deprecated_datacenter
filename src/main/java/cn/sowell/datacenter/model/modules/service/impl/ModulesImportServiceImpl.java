package cn.sowell.datacenter.model.modules.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.abc.application.BizFusionContext;
import com.abc.application.FusionContext;
import com.abc.mapping.entity.Entity;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;

import cn.sowell.copframe.dao.utils.NormalOperateDao;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.excel.CellTypeUtils;
import cn.sowell.datacenter.entityResolver.FusionContextConfig;
import cn.sowell.datacenter.entityResolver.FusionContextConfigFactory;
import cn.sowell.datacenter.entityResolver.FusionContextConfigResolver;
import cn.sowell.datacenter.entityResolver.ImportCompositeField;
import cn.sowell.datacenter.entityResolver.config.ImportComposite;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.dao.ModulesImportDao;
import cn.sowell.datacenter.model.modules.pojo.ImportStatus;
import cn.sowell.datacenter.model.modules.pojo.ImportTemplateCriteria;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplate;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplateField;
import cn.sowell.datacenter.model.modules.service.ModulesImportService;

@Service
public class ModulesImportServiceImpl implements ModulesImportService {

	@Resource
	FusionContextConfigFactory fFactory;

	@Resource
	NormalOperateDao nDao;
	
	@Resource
	ModulesImportDao impDao;
	
	Logger logger = Logger.getLogger(ModulesImportServiceImpl.class);
	
	private DateFormat defaultDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
	private NumberFormat numberFormat = new DecimalFormat("0.000");
	
	/*@SuppressWarnings("serial")
	Map<String, Map<String, ImportComposite>> importConfigKeyMap = new HashMap<String, Map<String, ImportComposite>>(){
		{
			ImportComposite[] composites = new ImportComposite[] {
				new ImportComposite("base", "基本数据", DataCenterConstants.MODULE_KEY_PEOPLE, FusionContextFactoryDC.KEY_IMPORT_BASE),
				new ImportComposite("lowincome", "低保数据", DataCenterConstants.MODULE_KEY_PEOPLE, FusionContextFactoryDC.KEY_IMPORT_LOWINCOME),
				new ImportComposite("handicapped", "残疾人数据", DataCenterConstants.MODULE_KEY_PEOPLE, FusionContextFactoryDC.KEY_IMPORT_HANDICAPPED),
				new ImportComposite("familyPlanning", "计生数据", DataCenterConstants.MODULE_KEY_PEOPLE, FusionContextFactoryDC.KEY_IMPORT_FAMILYPLANNING),
				new ImportComposite("addressBase", "地址基本数据", DataCenterConstants.MODULE_KEY_ADDRESS, FusionContextFactoryDC.KEY_ADDRESS_BASE),
				new ImportComposite("studentpartyBase", "学生基本数据", DataCenterConstants.MODULE_KEY_STUDENT, FusionContextFactoryDC.KEY_STUDENT_BASE),
				new ImportComposite("disabledpeople", "残助数据", DataCenterConstants.MODULE_KEY_DISABLEDPEOPLE, FusionContextFactoryDC.KEY_DISABLEDPEOPLE_BASE),
				new ImportComposite("hspeople", "党员管理", DataCenterConstants.MODULE_KEY_HSPEOPLE, FusionContextFactoryDC.KEY_HSPEOPLE_BASE)
			};
			for(ImportComposite c :composites) {
				if(!this.containsKey(c.getModuleKey())) {
					this.put(c.getModuleKey(), new LinkedHashMap<>());
				}
				this.get(c.getModuleKey()).put(c.getName(), c);
			}
		}
	};*/
	
	
	private String getConfigKey(String module, String compositeName) {
		return getImportCompositeMap(module).get(compositeName).getConfigId();
	}
	
	@Override
	public Map<String, ImportComposite> getImportCompositeMap(String module) {
		return fFactory.getModuleImportMap(module);
	}
	
	@Override
	public void importData(Sheet sheet, ImportStatus importStatus, String module, String compositeName)
			throws ImportBreakException {
		Row headerRow = sheet.getRow(1);
		String configKey = getConfigKey(module, compositeName);
		if(configKey != null){
			FusionContextConfig config = fFactory.getConfig(configKey);
			if(config != null) {
				execute(sheet, headerRow, config, importStatus);
			}
		}
	}
	
	private void execute(Sheet sheet, Row headerRow, FusionContextConfig config, ImportStatus importStatus) throws ImportBreakException {
		importStatus.appendMessage("正在计算总行数");
		importStatus.setTotal(colculateRowCount(sheet));
		int rownum = 2;
		importStatus.appendMessage("开始导入");
		Integration integration = PanelFactory.getIntegration();
		BizFusionContext context = config.createContext();
		context.setSource(FusionContext.SOURCE_COMMON);
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
				Entity entity = createImportEntity(config.getConfigResolver(), headerRow, row);
				String code = integration.integrate(entity, context);
				logger.debug("修改记录" + code);
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
	
	private String trim(String str) {
		if(str != null) {
			str = str.replaceAll("^\\u00a0+", "").replaceAll("\\u00a0+$", "");
			return str.trim();
		}else {
			return null;
		}
	}
	
	private String getStringWithBlank(Cell cell){
		//如果有覆盖至
		if(cell == null){
			return null;
		}
		CellType cellType = cell.getCellTypeEnum();
		if(cellType == CellType.STRING){
			return trim(cell.getStringCellValue());
		}else if(cellType == CellType.NUMERIC){
			if(CellTypeUtils.isCellDateFormatted(cell)){
				return trim(defaultDateFormat.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())));
			}
			return trim(FormatUtils.toString(FormatUtils.toLong(cell.getNumericCellValue())));
		}else if(cellType == CellType.FORMULA){
			FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			CellValue cellValue = evaluator.evaluate(cell);
			CellType cellValueType = cellValue.getCellTypeEnum();
			if(cellValueType == CellType.STRING){
				return trim(cellValue.getStringValue());
			}else if(cellValueType == CellType.NUMERIC){
				return trim(FormatUtils.toString(FormatUtils.toLong(cellValue.getNumberValue())));
			}
			return null;
		}
		return null;
	}
	
	private Entity createImportEntity(FusionContextConfigResolver fusionContextConfigResolver, Row headerRow,
			Row row) {
		Map<String, Object> map = new HashMap<String, Object>();
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			map.put(getStringWithBlank(headerRow.getCell(i)), getStringWithBlank(cell));
		}
		Entity entity = fusionContextConfigResolver.createEntityIgnoreUnsupportedElement(map);
		return entity;
	}
	
	@Override
	public byte[] createImportTempalteBytes(ModuleImportTemplate tmpl) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet sheet = workbook.createSheet("导入数据");
			sheet.createRow(0);
			XSSFRow headerRow = sheet.createRow(1);
			XSSFRow firstDataRow = sheet.createRow(2);
			
			CellStyle titleStyle = getTitleStyle(workbook);
			CellStyle dataStyle = getDataStyle(workbook);
			
			sheet.setDefaultColumnStyle(0, dataStyle);
			XSSFCell numberTitleCell = headerRow.createCell(0);
			numberTitleCell.setCellValue("序号");
			numberTitleCell.setCellStyle(titleStyle);
			XSSFCell valueCell = firstDataRow.createCell(0);
			valueCell.setCellValue(1);
			valueCell.setCellStyle(dataStyle);
			
			List<ModuleImportTemplateField> fields = tmpl.getFields();
			if(fields != null) {
				int columnIndex = 1;
				for (ModuleImportTemplateField field : fields) {
					sheet.setDefaultColumnStyle(columnIndex, dataStyle);
					XSSFCell titleCell = headerRow.createCell(columnIndex);
					titleCell.setCellValue(field.getFieldName());
					titleCell.setCellStyle(titleStyle);
					XSSFCell dataCell = firstDataRow.createCell(columnIndex);
					dataCell.setCellType(CellType.STRING);
					dataCell.setCellStyle(dataStyle);
					sheet.autoSizeColumn(columnIndex);
					columnIndex++;
				}
				
			}
			
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				workbook.write(os);
				return os.toByteArray();
			} catch (IOException e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}finally{
			try {
				workbook.close();
			} catch (IOException e) {
			}
		}
	}

	

	private CellStyle getTitleStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(new XSSFColor( new Color(146, 208, 80)));
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontName("宋体");
		style.setFont(font);
		return style;
	}
	
	private CellStyle getDataStyle(XSSFWorkbook workbook) {
		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		style.setFont(font);
		return style;
	}
	
	@Override
	public Long saveTemplate(ModuleImportTemplate tmpl) {
		Date now = new Date();
		if(tmpl.getId() == null) {
			tmpl.setCreateTime(now);
		}
		tmpl.setUpdateTime(now);
		Long tmplId = nDao.save(tmpl);
		if(tmpl.getFields() != null) {
			for (int i = 0; i < tmpl.getFields().size(); i++) {
				ModuleImportTemplateField field = tmpl.getFields().get(i);
				field.setOrder(i);
				field.setTemplateId(tmplId);
				field.setUpdateTime(now);
				nDao.save(field);
			}
		}
		return tmplId;
	}
	
	@Override
	public Set<ImportCompositeField> getImportCompositeFields(String module, String compositeName) {
		Map<String, ImportComposite> impMap = fFactory.getModuleImportMap(module);
		ImportComposite composite = impMap.get(compositeName);
		FusionContextConfig config = fFactory.getConfig(composite.getConfigId());
		return config.getAllImportFields();
	}
	
	@Override
	public List<ModuleImportTemplate> getImportTemplates(ImportTemplateCriteria criteria) {
		return impDao.getImportTemplates(criteria);
	}
	
	@Override
	public ModuleImportTemplate getImportTempalte(Long tmplId) {
		ModuleImportTemplate tmpl = nDao.get(ModuleImportTemplate.class, tmplId);
		List<ModuleImportTemplateField> fields = impDao.getTemplateFields(tmpl.getId());
		tmpl.setFields(fields);
		return tmpl;
	}
	
	
	
}

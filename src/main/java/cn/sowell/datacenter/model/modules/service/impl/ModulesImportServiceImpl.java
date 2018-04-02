package cn.sowell.datacenter.model.modules.service.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.abc.application.BizFusionContext;
import com.abc.application.FusionContext;
import com.abc.mapping.entity.Entity;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;

import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.excel.CellTypeUtils;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfig;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfigResolver;
import cn.sowell.datacenter.model.abc.resolver.FusionContextFactoryDC;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.bean.ImportComposite;
import cn.sowell.datacenter.model.modules.pojo.ImportStatus;
import cn.sowell.datacenter.model.modules.service.ModulesImportService;

@Service
public class ModulesImportServiceImpl implements ModulesImportService {

	@Resource
	FusionContextFactoryDC fFactory;

	Logger logger = Logger.getLogger(ModulesImportServiceImpl.class);
	
	private DateFormat defaultDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
	private NumberFormat numberFormat = new DecimalFormat("0.000");
	
	@SuppressWarnings("serial")
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
	};
	
	
	@Override
	public Map<String, ImportComposite> getImportCompositeMap(String module) {
		return importConfigKeyMap.get(module);
	}
	
	public String getConfigKey(String module, String compositeName) {
		try {
			return getImportCompositeMap(module).get(compositeName).getConfigKey();
		} catch (NullPointerException e) {
			return null;
		}
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
		BizFusionContext context = fFactory.getContext(config.getMappingName());
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

}

package cn.sowell.datacenter.model.modules.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;

import cn.sowell.datacenter.entityResolver.ImportCompositeField;
import cn.sowell.datacenter.entityResolver.config.ImportComposite;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.pojo.ImportStatus;
import cn.sowell.datacenter.model.modules.pojo.ImportTemplateCriteria;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplate;

public interface ModulesImportService {

	void importData(Sheet sheet, ImportStatus importStatus, String module, String dataType) throws ImportBreakException;

	Map<String, ImportComposite> getImportCompositeMap(String module);

	byte[] createImportTempalteBytes(ModuleImportTemplate tmpl) throws Exception;

	Long saveTemplate(ModuleImportTemplate tmpl);
	
	/**
	 * 根据模块名和composite获得所有的字段
	 * @param module
	 * @param composite
	 * @return
	 */
	Set<ImportCompositeField> getImportCompositeFields(String module, String composite);

	List<ModuleImportTemplate> getImportTemplates(ImportTemplateCriteria criteria);

	ModuleImportTemplate getImportTempalte(Long tmplId);

}

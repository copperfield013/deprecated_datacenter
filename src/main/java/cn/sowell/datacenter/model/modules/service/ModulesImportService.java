package cn.sowell.datacenter.model.modules.service;

import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;

import cn.sowell.datacenter.entityResolver.ImportCompositeField;
import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.pojo.ImportStatus;
import cn.sowell.datacenter.model.modules.pojo.ImportTemplateCriteria;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplate;

public interface ModulesImportService {

	void importData(Sheet sheet, ImportStatus importStatus, String module) throws ImportBreakException;

	byte[] createImportTempalteBytes(ModuleImportTemplate tmpl) throws Exception;

	Long saveTemplate(ModuleImportTemplate tmpl);
	
	List<ModuleImportTemplate> getImportTemplates(ImportTemplateCriteria criteria);

	ModuleImportTemplate getImportTempalte(Long tmplId);

	Set<ImportCompositeField> getImportCompositeFields(String module);

}

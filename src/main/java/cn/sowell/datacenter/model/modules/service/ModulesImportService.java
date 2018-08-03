package cn.sowell.datacenter.model.modules.service;

import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.datacenter.entityResolver.ImportCompositeField;
import cn.sowell.datacenter.model.modules.exception.ImportBreakException;
import cn.sowell.datacenter.model.modules.pojo.ImportTemplateCriteria;
import cn.sowell.datacenter.model.modules.pojo.ModuleImportTemplate;

public interface ModulesImportService {

	byte[] createImportTempalteBytes(ModuleImportTemplate tmpl) throws Exception;

	Long saveTemplate(ModuleImportTemplate tmpl);
	
	List<ModuleImportTemplate> getImportTemplates(ImportTemplateCriteria criteria);

	ModuleImportTemplate getImportTempalte(Long tmplId);

	Set<ImportCompositeField> getImportCompositeFields(String module);

	void importData(Sheet sheet, WorkProgress progress, String module, UserIdentifier user)
			throws ImportBreakException;

}

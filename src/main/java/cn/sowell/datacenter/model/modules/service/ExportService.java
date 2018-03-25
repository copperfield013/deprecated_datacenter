package cn.sowell.datacenter.model.modules.service;

import java.util.Set;

import org.springframework.core.io.AbstractResource;

import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.modules.bean.ExportDataPageInfo;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;

public interface ExportService {

	ExportStatus getExportStatus(String uuid);
	void startExport(String uuid, TemplateListTempalte ltmpl, Set<NormalCriteria> criteria, ExportDataPageInfo ePageInfo);
	void clearExportCache();
	AbstractResource getDownloadResource(String uuid);
	
}

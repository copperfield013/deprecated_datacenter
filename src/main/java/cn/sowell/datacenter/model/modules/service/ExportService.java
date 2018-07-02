package cn.sowell.datacenter.model.modules.service;

import java.util.Set;

import org.springframework.core.io.AbstractResource;

import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.dataserver.model.modules.bean.ExportDataPageInfo;
import cn.sowell.dataserver.model.modules.pojo.criteria.NormalCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;

public interface ExportService {

	ExportStatus getExportStatus(String uuid);
	void startExport(String uuid, TemplateListTemplate ltmpl, Set<NormalCriteria> criteria, ExportDataPageInfo ePageInfo);
	void clearExportCache();
	AbstractResource getDownloadResource(String uuid);
	
}

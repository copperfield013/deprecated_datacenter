package cn.sowell.datacenter.model.modules.service;

import java.util.Set;

import org.springframework.core.io.AbstractResource;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.dataserver.model.modules.bean.ExportDataPageInfo;
import cn.sowell.dataserver.model.modules.pojo.criteria.NormalCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;

public interface ExportService {

	void clearExportCache();
	AbstractResource getDownloadResource(String uuid);
	void startExport(WorkProgress progress, TemplateListTemplate ltmpl, Set<NormalCriteria> criteria,
			ExportDataPageInfo ePageInfo, UserIdentifier user);
	WorkProgress getExportProgress(String uuid);
	void stopExport(String uuid);
	void removeExport(String uuid);
	
}

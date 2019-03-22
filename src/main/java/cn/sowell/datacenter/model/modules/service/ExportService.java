package cn.sowell.datacenter.model.modules.service;

import java.util.List;

import org.springframework.core.io.AbstractResource;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.dataserver.model.modules.bean.ExportDataPageInfo;
import cn.sowell.dataserver.model.modules.pojo.criteria.NormalCriteria;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;

public interface ExportService {

	void clearExportCache();
	AbstractResource getDownloadResource(String uuid);
	WorkProgress getExportProgress(String uuid);
	void stopExport(String uuid);
	void removeExport(String uuid);
	
	void startWholeExport(WorkProgress progress, TemplateGroup tmplGroup, boolean withDetail,
			List<NormalCriteria> criteria, ExportDataPageInfo ePageInfo, UserIdentifier user);
	String exportDetailExcel(ModuleEntityPropertyParser parser, TemplateDetailTemplate dtmpl) throws Exception;
}

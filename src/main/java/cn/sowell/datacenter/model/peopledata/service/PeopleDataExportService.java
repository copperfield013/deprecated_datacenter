package cn.sowell.datacenter.model.peopledata.service;

import java.util.Set;

import org.springframework.core.io.AbstractResource;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.people.ExportDataPageInfo;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;

public interface PeopleDataExportService {

	ExportStatus getExportStatus(String uuid);

	void startExport(String uuid, TemplateListTmpl ltmpl, Set<NormalCriteria> criteria, ExportDataPageInfo ePageInfo);
	
	AbstractResource getDownloadResource(String uuid);

	void clearExportCache();

}

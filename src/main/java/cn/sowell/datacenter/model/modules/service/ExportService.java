package cn.sowell.datacenter.model.modules.service;

import cn.sowell.datacenter.model.admin.pojo.ExportStatus;

public interface ExportService {

	ExportStatus getExportStatus(String uuid);

}

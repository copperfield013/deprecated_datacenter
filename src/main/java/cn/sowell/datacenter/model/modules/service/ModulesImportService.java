package cn.sowell.datacenter.model.modules.service;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import cn.sowell.datacenter.model.basepeople.service.impl.ImportBreakException;
import cn.sowell.datacenter.model.modules.bean.ImportComposite;
import cn.sowell.datacenter.model.modules.pojo.ImportStatus;

public interface ModulesImportService {

	void importData(Sheet sheet, ImportStatus importStatus, String module, String dataType) throws ImportBreakException;

	Map<String, ImportComposite> getImportCompositeMap(String module);

}

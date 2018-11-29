package cn.sowell.datacenter.test.abc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.modules.bean.EntityExportWriter;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.dataserver.model.tmpl.service.TemplateService;

@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestExport {
	@Resource
	TemplateService tService;
	
	@Resource
	ModulesService mService;
	

	@Test
	public void test() throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		TemplateDetailTemplate dtmpl = tService.getDetailTemplate(42);
		String code = "b2ac0226cbd04bff9b0dfc2735721ddb";
		ModuleEntityPropertyParser parser = mService.getEntity(dtmpl.getModule(), code, null, TestEntityBinder.getUser());
		EntityExportWriter writer = new EntityExportWriter();
		writer.writeDetail(parser, dtmpl, sheet);
		File file = new File("d:/export.xlsx");
		if(!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fo = new FileOutputStream(file);
		workbook.write(fo);
		workbook.close();
		fo.close();
	}
}

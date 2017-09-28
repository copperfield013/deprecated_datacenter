package com.abc.application.seven;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.application.ApplicationInfo;
import com.abc.mapping.entity.Entity;
import com.abc.panel.Discoverer;
import com.abc.panel.Integration;
import com.abc.panel.PanelFactory;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordRelatedTest {
	private static Logger logger = Logger.getLogger(RecordRelatedTest.class);

	protected String mapperName = "example";
	protected String familyDoctorMapper = "familydoctor";
	protected String educationhistoryMapper = "educationhistory";
	protected String familyInfomationMapper = "familyInfomation";
	protected String familyMemberMapper = "familyMember";
	protected String filename = "E:\\数据\\test艮山门all.xlsx";
	protected String sheetName = "2";
	protected String excelExtName = "xlsx";
	protected String writeMappingName = "example";


	@Test
	public void readData() {
		long startTime = System.currentTimeMillis();
		Workbook wb = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filename);
			if ("xlsx".equalsIgnoreCase(excelExtName)) {
				wb = new XSSFWorkbook(fis);
			} else {
				wb = new HSSFWorkbook(fis);
			}
			Sheet sheet = wb.getSheet(sheetName);
			Row headerRow = sheet.getRow(1);

			execute(sheet, headerRow, mapperName,1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis!=null){
					fis.close();  
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		long endTime = System.currentTimeMillis();// 记录结束时间
		logger.debug((float) (endTime - startTime) / 1000);
	}

	protected void execute(Sheet sheet, Row headerRow, String mapperName,
			int number) {
		if (number < 1) {
			number = 1;
		}
		Row row = sheet.getRow(2 + number);
		Entity entity = createEntity(mapperName, headerRow, row);
		
		// 添加家庭信息
		Entity relatedFamilyentity = new Entity(familyInfomationMapper);
		relatedFamilyentity.putValue("家庭人数", 5);
		relatedFamilyentity.putValue("家庭类别", "户籍家庭");
		relatedFamilyentity.putValue("家庭地址", "宝善公寓1幢1单元303室");
		entity.putRecordEntity("家庭信息", "家庭信息", relatedFamilyentity);
		//跟家庭信息添加成员
		Entity memberEntity ;
		row = sheet.getRow(2 + number);
		memberEntity = createEntity("member", headerRow, row);
		relatedFamilyentity.putRecordEntity("家庭成员", "户主", memberEntity);

		// 添加家庭人员
		Entity relationentity ;
		row = sheet.getRow(2 + number+1);
		relationentity = createEntity(familyMemberMapper, headerRow, row);
		entity.putRelationEntity("家庭关系", "夫妻", relationentity);
		row = sheet.getRow(2 + number+2);
		relationentity = createEntity(familyMemberMapper, headerRow, row);
		entity.putRelationEntity("家庭关系", "子女", relationentity);
		
		logger.debug(entity.toJson());
		
		ApplicationInfo appInfo=new ApplicationInfo();
		appInfo.setWriteMappingName(writeMappingName);
		appInfo.setSource(ApplicationInfo.SOURCE_COMMON);
		Integration integration=PanelFactory.getIntegration();
		String code=integration.integrate(entity, appInfo);
		
		logger.debug(code);
		Discoverer discoverer=PanelFactory.getDiscoverer(mapperName);
		Entity result=discoverer.discover(code);
		logger.debug(code + " : "+ result.toJson());

	}

	private Entity createEntity(String mappingName, Row headerRow,
			Row row) {
		Entity entity = new Entity(mappingName);
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			if (headerRow.getCell(i).getStringCellValue().equals("家庭医生")) {
				if (cell.getStringCellValue() != null
						&& !cell.getStringCellValue().equals("")) {
					Entity relationentity = new Entity(familyDoctorMapper);
					relationentity.putValue("姓名", cell.getStringCellValue());
					entity.putRelationEntity("医疗信息","家庭医生", relationentity);
				}
			} else {
				if (cell.getCellTypeEnum() == CellType.NUMERIC) {
					entity.putValue(headerRow.getCell(i).getStringCellValue(),
							String.valueOf(cell.getNumericCellValue()));
				} else if (cell.getCellTypeEnum() == CellType.ERROR) {
					logger.warn("ERROR Type row number:" + row.getRowNum()
							+ " ; cell number:" + i + ";");
				} else {
					entity.putValue(headerRow.getCell(i).getStringCellValue(),
							cell.getStringCellValue());
				}
			}
		}
		
		
		return entity;
	}

}

package com.abc.applicationtest;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.application.PeopleFusion;
import com.abc.application.PeopleRelationFusion;
import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.node.ABCNode;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PerformanceStrangePeopleTest extends StrangePeopleTest{
	private static Logger logger = Logger.getLogger(PerformanceStrangePeopleTest.class);

	@Resource
	MappingNodeAnalysis analysis;
	@Resource
	PeopleFusion peopleFusion;
	@Resource
	PeopleRelationFusion relationFusion;

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
			ABCNode abcNode = analysis.analysis(mappingfilepath);

			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
			execute(sheet, headerRow, abcNode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

}

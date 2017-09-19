package com.abc.applicationtest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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

import com.abc.application.DataSource;
import com.abc.application.PeopleFusion;
import com.abc.application.PeopleRelationFusion;
import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RecordEntity;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.people.PeopleRelation;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StrangePeopleTest {
	private static Logger logger = Logger.getLogger(StrangePeopleTest.class);

	@Resource(name="MappingNodeAnalysis")
	MappingNodeAnalysis analysis;
	@Resource(name="PeopleFusion")
	PeopleFusion peopleFusion;
	@Resource(name="PeopleRelationFusion")
	PeopleRelationFusion relationFusion;
	
	protected String mapperName = "baseinfoImport";
	protected String familyDoctorMapper = "familydoctor";
	protected String filename = "E:\\数据\\test艮山门all.xlsx";
	protected String sheetName = "2";
	protected String excelExtName = "xlsx";
	protected String mappingfilepath = getClass().getResource("/").getFile()
			+ "../classes/mapping/baseinfoImport.xml";
	protected String writeMappingName = "goodnode_polic";


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

			execute(sheet, headerRow, abcNode,1);

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

	protected void execute(Sheet sheet, Row headerRow, ABCNode abcNode,int number) {
		if(number<1){
			number=1;
		}
		Row row = sheet.getRow(2+number);
		Entity entity = createSocialEntity(abcNode, headerRow, row);
		People people = createPeople(sheet, headerRow, abcNode, entity);
		List<PeopleRelation> peopleRelations = createPeopleRelation(abcNode,
				people, entity);
		people = peopleFusion.fuseStrange(people,writeMappingName,DataSource.SOURCE_POLIC);
		logger.debug(people.getPeopleCode()+" : "+people.getJson(abcNode.getTitle()));
		people = relationFusion.fuse(people,peopleRelations,writeMappingName,DataSource.SOURCE_POLIC);
	}

	private People createPeople(Sheet sheet, Row headerRow, ABCNode abcNode,
			Entity entity) {
		People people = new People();
		people.addMapping(abcNode);
		people.addEntity(entity);
		logger.debug(people.getJson(mapperName));
		return people;
	}

	private List<PeopleRelation> createPeopleRelation(ABCNode abcNode,
			People people, Entity entity) {
		List<PeopleRelation> peopleRelations = new ArrayList<PeopleRelation>();
		for (String name : entity.getRelationNames()) {
			List<RecordEntity> entitys = entity.getRelations(name);
			PeopleRelation peopleRelation = new PeopleRelation(
					abcNode.getRelation(name));
			peopleRelation.setHost(people);
			for (RecordEntity reocrdentity : entitys) {
				peopleRelation.addRelated(reocrdentity.getEntity());
			}
			peopleRelations.add(peopleRelation);
		}
		for (PeopleRelation relation : peopleRelations) {
			logger.debug(relation.getJson());
		}
		return peopleRelations;
	}

	private Entity createSocialEntity(ABCNode abcNode, Row headerRow,
			Row row) {
		Entity entity = new Entity(mapperName);
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			if (headerRow.getCell(i).getStringCellValue().equals("家庭医生")) {
				if (cell.getStringCellValue() != null
						&& !cell.getStringCellValue().equals("")) {
					Entity relationentity = new Entity(familyDoctorMapper);
					relationentity.putValue("姓名", cell.getStringCellValue());
					entity.putRelationEntity("家庭医生信息","家庭医生", relationentity);
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

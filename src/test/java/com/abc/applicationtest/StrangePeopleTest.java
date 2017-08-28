package com.abc.applicationtest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

import com.abc.fuse.rule.algorithm.DataSource;
import com.abc.mapping.MappingNodeAnalysis;
import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.SocialEntity;
import com.abc.mapping.node.ABCNode;
import com.abc.people.People;
import com.abc.people.PeopleRelation;
import com.abc.people.RelationShip;
import com.abc.application.PeopleFusion;
import com.abc.application.PeopleRelationFusion;

@ContextConfiguration(locations = "classpath*:spring-core.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class StrangePeopleTest {
	private static Logger logger = Logger.getLogger(StrangePeopleTest.class);

	@Resource
	MappingNodeAnalysis analysis;
	@Resource
	PeopleFusion peopleFusion;
	@Resource
	PeopleRelationFusion relationFusion;
	
	protected String mapperName = "baseinfoImport";
	protected  String familyDoctorMapper = "familydoctor";
	protected  String filename = "D:\\test艮山门.xlsx";
	protected  String sheetName = "2";
	protected  String excelExtName = "xlsx";
	protected  String mappingfilepath = getClass().getResource("/").getFile()
			+ "../classes/mapping/baseinfoImport.xml";

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

	protected  void execute(Sheet sheet, Row headerRow, ABCNode abcNode) {
		Row row = sheet.getRow(3);
		SocialEntity socialEntity = createSocialEntity(abcNode, headerRow, row);
		People people = createPeople(sheet, headerRow, abcNode, socialEntity);
		List<PeopleRelation> peopleRelations = createPeopleRelation(abcNode,
				people, socialEntity);
		people = peopleFusion.fuseStrange(people,DataSource.SOURCE_POLIC);
		logger.debug(people.getPeopleCode()+" : "+people.getJson(abcNode.getName()));
		people = relationFusion.fuse(people,peopleRelations,DataSource.SOURCE_POLIC);
		if(people!=null&& people.getRelationShip()!=null){
			Collection<RelationShip> ships = people.getRelationShip();
			for (RelationShip ship : ships) {
				logger.debug(ship);
			}
		}
	}

	protected  People createPeople(Sheet sheet, Row headerRow, ABCNode abcNode,
			SocialEntity socialEntity) {
		People people = new People();
		people.addMapping(abcNode);
		people.addEntity(socialEntity);
		logger.debug(people.getJson(mapperName));
		return people;
	}

	protected  List<PeopleRelation> createPeopleRelation(ABCNode abcNode,
			People people, SocialEntity socialEntity) {
		List<PeopleRelation> peopleRelations = new ArrayList<PeopleRelation>();
		for (String key : socialEntity.getRelationKeys()) {
			List<Entity> entitys = socialEntity.getRelations(key);
			PeopleRelation peopleRelation = new PeopleRelation(
					abcNode.getRelation(key));
			peopleRelation.setHost(people);
			for (Entity entity : entitys) {
				peopleRelation.addRelated(entity);
			}
			peopleRelations.add(peopleRelation);
		}
		for (PeopleRelation relation : peopleRelations) {
			logger.debug(relation.getJson());
		}
		return peopleRelations;
	}

	protected  SocialEntity createSocialEntity(ABCNode abcNode, Row headerRow,
			Row row) {
		SocialEntity entity = new SocialEntity(mapperName);
		int length = headerRow.getPhysicalNumberOfCells();
		for (int i = 0; i < length; i++) {
			Cell cell = row.getCell(i);
			if (headerRow.getCell(i).getStringCellValue().equals("家庭医生")) {
				if (cell.getStringCellValue() != null
						&& !cell.getStringCellValue().equals("")) {
					Entity relationentity = new Entity(familyDoctorMapper);
					relationentity.setValue(headerRow.getCell(i)
							.getStringCellValue(), cell.getStringCellValue());
					entity.addRelation("家庭医生", relationentity);
				}
			} else {
				if (cell.getCellTypeEnum() == CellType.NUMERIC) {
					entity.setValue(headerRow.getCell(i).getStringCellValue(),
							String.valueOf(cell.getNumericCellValue()));
				} else if (cell.getCellTypeEnum() == CellType.ERROR) {
					logger.warn("ERROR Type row number:" + row.getRowNum()
							+ " ; cell number:" + i + ";");
				} else {
					entity.setValue(headerRow.getCell(i).getStringCellValue(),
							cell.getStringCellValue());
				}
			}
		}
		return entity;
	}


}

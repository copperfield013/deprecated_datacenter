package cn.sowell.datacenter.test.abc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abc.mapping.entity.Entity;
import com.abc.mapping.entity.RecordEntity;

import cn.sowell.datacenter.model.peopledata.pojo.FamilyInfo;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.WorkExperience;
import cn.sowell.datacenter.model.peopledata.service.impl.EntityTransfer;


@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestEntityBinder {
	EntityTransfer transfer = new EntityTransfer();
	
	@Test
	public void test() {
		PeopleData data = new PeopleData();
		data.setName("张荣波");
		
		FamilyInfo family = new FamilyInfo();
		family.setFamilyAddress("福建省");
		data.setFamilyInfo(family);
		
		List<WorkExperience> workExperiences = new ArrayList<>();
		WorkExperience work = new WorkExperience();
		work.setCompanyName("杭州设维");
		workExperiences.add(work);
		work = new WorkExperience();
		work.setCompanyName("未知");
		workExperiences.add(work);
		data.setWorkExperiences(workExperiences);
		
		Entity target = new Entity("baseinfoImport");
		transfer.bind(data, target);
		System.out.println(target.getStringValue("name"));
		
		
		List<RecordEntity> fRecord = target.getRecords("家庭信息");
		System.out.println(fRecord);
		System.out.println(fRecord.size());
		System.out.println(fRecord.get(0).getEntity().getStringValue("家庭地址"));
		
		
		List<RecordEntity> wRecord = target.getRecords("workExperience");
		wRecord.forEach(wr->{
			System.out.println(wr.getEntity().getStringValue("companyName"));
		});
		
	}
}

package cn.sowell.datacenter.test.abc;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.sowell.copframe.jedica.JedicaFactory;
import cn.sowell.datacenter.model.redis.service.RedisPeopleDataService;

import com.abc.people.People;


@ContextConfiguration(locations = "classpath*:spring-config/spring-junit.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JedicaTest{
	
	@Resource
	RedisPeopleDataService rpService;
	
	@Resource
	JedicaFactory jFactory;
	
	@Test
	public void getData(){
		People people = rpService.getPeople("96e3f645220c4f8980a56d978819d693");
		System.out.println(people);
	}
	
	@Test
	public void testRedis() throws Exception{
		/*JedicaReadonly<TestEntry> jedica = jFactory.getJedica((key)->{
			JedicaValue value = new JedicaValue();
			value.setArray(new long[]{0, 2, 3});
			value.setList(Arrays.asList(1, 10));
			Map<String, String> map = new HashMap<String, String>();
			map.put("x", "X");
			value.setMap(map);
			InnerValue inner = new InnerValue();
			Map<Integer, String> innerMap = new HashMap<Integer, String>();
			innerMap.put(10, "P");
			inner.setMap(innerMap);
			inner.setVal("inner");
			value.setInner(inner);
			TestEntry entry = new TestEntry(key, value);
			return entry;
		});
		TestEntry entry = jedica.get("xxxx");
		System.out.println(entry.getValue());*/
	}
	
	
	
}

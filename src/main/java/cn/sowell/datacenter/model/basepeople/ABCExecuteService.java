package cn.sowell.datacenter.model.basepeople;

import java.io.IOException;
import java.util.Map;

import com.abc.mapping.entity.SocialEntity;
import com.abc.people.People;

public interface ABCExecuteService {
	/**
	 * 
	 * @param data
	 * @return
	 */
	SocialEntity createSocialEntity(Map<String, String> data);
	
	People editPeople(Map<String, String> data) throws IOException;
	
}

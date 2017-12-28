package cn.sowell.datacenter.model.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.sowell.copframe.common.UserIdentifier;

public class UserUtils {
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentUser(Class<T> userClass){
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		if(authen != null){
			Object user = authen.getPrincipal();
			return (T) user;
		}
		return null;
	}
	
	
	public static UserIdentifier getCurrentUser(){
		return getCurrentUser(UserIdentifier.class);
	}
}

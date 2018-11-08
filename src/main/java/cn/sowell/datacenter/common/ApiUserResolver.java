package cn.sowell.datacenter.common;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.admin.service.AdminUserService;
import cn.sowell.datacenter.model.admin.service.impl.AdminUserServiceImpl.Token;
import cn.sowell.datacenter.model.config.service.NonAuthorityException;

public class ApiUserResolver implements HandlerMethodArgumentResolver{

	@Resource
	AdminUserService uService;
	
	static Logger logger = Logger.getLogger(ApiUserResolver.class);
	
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramClass = parameter.getParameterType();
		if(ApiUser.class.isAssignableFrom(paramClass)){
			return true;
		}
		return false;
	}

	@Override
	public ApiUser resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String tokenCode = webRequest.getHeader("datamobile-token");
		if(TextUtils.hasText(tokenCode)) {
			try {
				Token token = uService.validateToken(tokenCode);
				token.refreshDeadline();
				return token.getUser();
			} catch (Exception e) {
				logger.error("验证用户token时发生异常", e);
			}
		}
		throw new NonAuthorityException("没有权限");
		
	}

}

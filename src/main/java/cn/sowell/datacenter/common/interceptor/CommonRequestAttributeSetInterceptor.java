package cn.sowell.datacenter.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import cn.sowell.copframe.common.property.PropertyPlaceholder;

public class CommonRequestAttributeSetInterceptor implements WebRequestInterceptor{
	
	@Override
	public void preHandle(WebRequest request) throws Exception {
		if(request instanceof NativeWebRequest){
			HttpServletRequest req = ((NativeWebRequest) request).getNativeRequest(HttpServletRequest.class);
			String basePath = PropertyPlaceholder.getProperty("project_url");
			long resStamp = System.currentTimeMillis();
			req.setAttribute("RES_STAMP", resStamp);
			req.setAttribute("basePath", basePath);
		}
		
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}
}

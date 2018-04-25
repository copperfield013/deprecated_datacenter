package cn.sowell.datacenter.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestParameterMapCompositeResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramClass = parameter.getParameterType();
		if(RequestParameterMapComposite.class.isAssignableFrom(paramClass)){
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		RequestParameterMapComposite composite = new RequestParameterMapComposite();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> pMap = webRequest.getParameterMap();
		if(pMap != null) {
			pMap.forEach((name, val)->{
				if(val != null) {
					if(val.length == 1) {
						map.put(name, val[0]);
					}else if(val.length > 1) {
						map.put(name, val);
					}else {
						map.put(name, null);
					}
				}else {
					map.put(name, null);
				}
			});
		}
		composite.setMap(map);
		return composite;
	}

}

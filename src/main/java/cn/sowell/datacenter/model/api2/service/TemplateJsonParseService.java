package cn.sowell.datacenter.model.api2.service;

import com.alibaba.fastjson.JSONObject;

import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailFieldGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateGroup;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;

public interface TemplateJsonParseService {

	JSONObject toListTemplateJson(TemplateListTemplate ltmpl);

	JSONObject toTemplateGroupJson(TemplateGroup tmplGroup);

	JSONObject toSelectConfig(TemplateDetailFieldGroup fieldGroup);

	JSONObject toDetailTemplateConfig(TemplateGroup tmplGroup);

}

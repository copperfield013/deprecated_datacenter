package cn.sowell.datacenter.admin.controller.people;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.spring.propTranslator.PropertyParser;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.PeopleButtService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataExportService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;
import cn.sowell.datacenter.model.peopledata.service.PojoService;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.param.ListTemplateParameter;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateDetailTemplate;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;
import cn.sowell.datacenter.model.tmpl.service.TemplateService;

@Controller
@RequestMapping(AdminConstants.URI_PEOPLEDATA + "/tmpl")
public class AdminPeopleDataTmplController {
	
	@Resource
	PeopleDictionaryService dictService;
	
	@Resource
	ListTemplateService ltmplService;
	
	@Resource
	PojoService pojoService;
	
	@Resource
    PeopleDataService peopleService;

	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	PeopleDataExportService eService;
	
	@Resource
	TemplateService tService;
	
	@Resource
    PeopleButtService buttService;
	
	Logger logger = Logger.getLogger(AdminPeopleDataTmplController.class);
	
	@RequestMapping("/list")
	public String list(Long tmplId, PageInfo pageInfo, Model model, HttpServletRequest request, HttpSession session){
		/*ListTemplateParameter param = ltmplService.exractTemplateParameter(tmplId, DataCenterConstants.MODULE_KEY_PEOPLE, request);
		if(param.getListTemplate() != null){
			List<PeopleData> srcList = ltmplService.queryPeopleList(new HashSet<NormalCriteria>(param.getNormalCriteriaMap().values()), pageInfo);
			List<PropertyParser> parserList = new ArrayList<PropertyParser>();
			srcList.forEach(src->parserList.add(pojoService.createPropertyParser(src)));
			model.addAttribute("parserList", parserList);
			
			String uuid = (String) session.getAttribute(AdminConstants.EXPORT_PEOPLE_STATUS_UUID);
			if(uuid != null){
				ExportStatus exportStatus = eService.getExportStatus(uuid);
				if(exportStatus != null && !exportStatus.isBreaked() && !exportStatus.isCompleted()){
					model.addAttribute("exportStatus", exportStatus);
				}
			}
			if(param.getListTemplate().getCriterias() != null){
				model.addAttribute("criteriaOptionsMap", dictService.getOptionsMap(CollectionUtils.toSet(param.getListTemplate().getCriterias(), criteria->criteria.getFieldId())));
			}
		}
		model.addAttribute("vCriteriaMap", param.getNormalCriteriaMap());
		model.addAttribute("ltmpl", param.getListTemplate());
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("ltmplList", ltmplService.queryLtmplList("people", param.getUser()));
		model.addAttribute("module", DataCenterConstants.MODULE_KEY_PEOPLE);
		return AdminConstants.JSP_PEOPLEDATA_TMPL + "/peopledata_list_tmpl.jsp";*/
		return null;
	}
	
	@RequestMapping("/add")
	public String add(Long tmplId, Model model) {
		TemplateDetailTemplate template = null;
    	UserIdentifier user = UserUtils.getCurrentUser();
    	if(tmplId == null){
			template = tService.getDefaultDetailTemplate(user, DataCenterConstants.MODULE_KEY_PEOPLE);
    	}else{
    		template = tService.getDetailTemplate(tmplId);
    	}
    	List<TemplateDetailTemplate> tmplList = tService.getAllDetailTemplateList("people", user, null, false);
		model.addAttribute("tmpl", template);
		model.addAttribute("tmplList", tmplList);
		model.addAttribute("module", DataCenterConstants.MODULE_KEY_PEOPLE);
		return AdminConstants.JSP_PEOPLEDATA_TMPL + "/peopledata_update_tmpl.jsp";
	}


	@RequestMapping("/detail/{peopleCode}")
	public String detailTmpl(
    		@PathVariable String peopleCode, 
    		Long tmplId, 
    		String datetime, 
    		Long timestamp, 
    		Model model){
    	 Date date = null;
         date = dateFormat.parse(datetime);
         if(timestamp != null){
         	date = new Date(timestamp);
         }
         UserIdentifier user = UserUtils.getCurrentUser();
         TemplateDetailTemplate template = null;
         if(tmplId == null){
        	 template = tService.getDefaultDetailTemplate(user, DataCenterConstants.MODULE_KEY_PEOPLE);
         }else{
        	 template = tService.getDetailTemplate(tmplId);
         }
         List<TemplateDetailTemplate> tmplList = tService.getAllDetailTemplateList("people", user, null, false);
         PeopleData people = peopleService.getHistoryPeople(peopleCode, date);
         PropertyParser parser = pojoService.createPropertyParser(people);
         model.addAttribute("parser", parser);
         model.addAttribute("people", people);
         model.addAttribute("datetime", datetime);
         model.addAttribute("peopleCode", peopleCode);
         model.addAttribute("tmpl", template);	
         model.addAttribute("date", date == null? new Date() : date);
         model.addAttribute("tmplList", tmplList);
         model.addAttribute("timestamp", timestamp);
         model.addAttribute("module", DataCenterConstants.MODULE_KEY_PEOPLE);
         return AdminConstants.JSP_PEOPLEDATA_TMPL + "/peopledata_detail_tmpl.jsp";
    }
	
	@RequestMapping("/update/{peopleCode}")
    public String update(@PathVariable String peopleCode, Long tmplId, Model model){
    	TemplateDetailTemplate template = null;
    	UserIdentifier user = UserUtils.getCurrentUser();
    	if(tmplId == null){
			template = tService.getDefaultDetailTemplate(user, DataCenterConstants.MODULE_KEY_PEOPLE);
    	}else{
    		template = tService.getDetailTemplate(tmplId);
    	}
    	List<TemplateDetailTemplate> tmplList = tService.getAllDetailTemplateList("people", user, null, false);
    	PeopleData people = peopleService.getPeople(peopleCode);
        PropertyParser parser = pojoService.createPropertyParser(people);
        model.addAttribute("tmplList", tmplList);
        model.addAttribute("parser", parser);
        model.addAttribute("people", people);
        model.addAttribute("tmpl", template);
        model.addAttribute("peopleCode", peopleCode);
        model.addAttribute("module", DataCenterConstants.MODULE_KEY_PEOPLE);
    	return AdminConstants.JSP_PEOPLEDATA_TMPL + "/peopledata_update_tmpl.jsp";
    }
	
	@ResponseBody
    @RequestMapping("/save")
    public AjaxPageResponse save(String peopleCode, @RequestParam Map<String, String> map){
    	 try {
             buttService.mergePeople(peopleCode, map);
             return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("保存成功", "peopledata_list");
         } catch (Exception e) {
             logger.error("保存时发生错误", e);
             return AjaxPageResponse.FAILD("保存失败");
         }
    }
	
}

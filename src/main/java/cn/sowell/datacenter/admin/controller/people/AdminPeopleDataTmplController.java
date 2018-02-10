package cn.sowell.datacenter.admin.controller.people;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.spring.propTranslator.PropertyParser;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleTemplateData;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataExportService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDictionaryService;
import cn.sowell.datacenter.model.peopledata.service.PojoService;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTmpl;
import cn.sowell.datacenter.model.tmpl.service.ListTemplateService;

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
	
	@RequestMapping("/list")
	public String list(Long tmplId, PageInfo pageInfo, Model model, HttpServletRequest request, HttpSession session){
		TemplateListTmpl ltmpl = null;
		UserIdentifier user = UserUtils.getCurrentUser();
		if(tmplId == null){
			ltmpl = ltmplService.getDefaultListTemplate(user);
		}else{
			ltmpl = ltmplService.getListTemplate(tmplId);
		}
		Map<Long, NormalCriteria> vCriteriaMap = ltmplService.getCriteriasFromRequest(
				new ServletRequestParameterPropertyValues(request, "criteria", "_"), 
				CollectionUtils.toMap(ltmpl.getCriterias(), c->c.getId())); 
		if(ltmpl != null){
			List<PeopleData> srcList = ltmplService.queryPeopleList(new HashSet<NormalCriteria>(vCriteriaMap.values()), pageInfo);
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
		}
		if(ltmpl.getCriterias() != null){
			model.addAttribute("criteriaOptionsMap", dictService.getOptionsMap(CollectionUtils.toSet(ltmpl.getCriterias(), criteria->criteria.getFieldId())));
		}
		model.addAttribute("vCriteriaMap", vCriteriaMap);
		model.addAttribute("ltmpl", ltmpl);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("ltmplList", ltmplService.queryLtmplList(user));
		return AdminConstants.JSP_PEOPLEDATA_TMPL + "/peopledata_list_tmpl.jsp";
	}
	
	


	@RequestMapping("/detail_tmpl/{peopleCode}")
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
	         PeopleTemplateData template = null;
	         if(tmplId == null){
	        	 template = dictService.getDefaultTemplate(user);
	         }else{
	        	 template = dictService.getTemplate(tmplId);
	         }
	         List<PeopleTemplateData> tmplList = dictService.getAllTemplateList(user, null, false);
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
	         return AdminConstants.JSP_PEOPLEDATA + "/peopledata_detail_tmpl.jsp";
	    }
	
}

package cn.sowell.datacenter.admin.controller.address;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.dao.utils.UserUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.spring.propTranslator.PropertyParser;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.DataCenterConstants;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.address.pojo.AddressData;
import cn.sowell.datacenter.model.address.service.TemplateAddressService;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.dict.service.DictionaryService;
import cn.sowell.datacenter.model.modules.service.ModulesService;
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
@RequestMapping(AdminConstants.URI_BASE + "/address/tmpl")
public class AdminTemplateAddressController {
	@Resource
	DictionaryService dictService;
	
	@Resource
	ListTemplateService ltmplService;
	
	@Resource
	PojoService pojoService;
	
	@Resource
    PeopleDataService peopleService;

	@Resource
	TemplateAddressService addressService;
	
	@Resource
	FrameDateFormat dateFormat;
	
	@Resource
	PeopleDataExportService eService;
	
	@Resource
	TemplateService tService;
	
	@Resource
	ModulesService mService;
	
	@RequestMapping("/list")
	public String list(Long tmplId, PageInfo pageInfo, Model model, HttpServletRequest request, HttpSession session){
		ListTemplateParameter param = mService.exractTemplateParameter(tmplId, DataCenterConstants.MODULE_KEY_ADDRESS, request);
		if(param.getListTemplate() != null){
			List<AddressData> srcList = addressService.queryAddressList(new HashSet<NormalCriteria>(param.getNormalCriteriaMap().values()), pageInfo);
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
		model.addAttribute("ltmplList", ltmplService.queryLtmplList("address", param.getUser()));
		return AdminConstants.JSP_ADDRESS + "/tmpl/address_list_tmpl.jsp";
	}
	
	
	@RequestMapping("/detail/{code}")
	public String detail(
	    		@PathVariable String code,
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
        	 template = tService.getDefaultDetailTemplate(user, DataCenterConstants.MODULE_KEY_ADDRESS);
         }else{
        	 template = tService.getDetailTemplate(tmplId);
         }
         List<TemplateDetailTemplate> tmplList = tService.getAllDetailTemplateList("address", user, null, false);
         AddressData address = addressService.getHistoryAddress(code, date);
         PropertyParser parser = pojoService.createPropertyParser(address);
         model.addAttribute("parser", parser);
         model.addAttribute("address", address);
         model.addAttribute("code", code);
         model.addAttribute("datetime", datetime);
         model.addAttribute("tmpl", template);	
         model.addAttribute("date", date == null? new Date() : date);
         model.addAttribute("tmplList", tmplList);
         model.addAttribute("timestamp", timestamp);
         return AdminConstants.JSP_ADDRESS + "/tmpl/address_detail_tmpl.jsp";
    }
	
	
	
}

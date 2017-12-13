package cn.sowell.datacenter.admin.controller.specialposition;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abc.extface.dto.SpecialPosition;
import com.abc.position.SpecialPositionIndex;
import com.abc.position.constant.PositionLevel;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.specialposition.pojo.criteria.SpecialPositionCriteria;
import cn.sowell.datacenter.model.specialposition.service.SpecialPositionService;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/special_position")
public class AdminSpecialPositionController {
	@Resource
	SpecialPositionService specialPositionService;
	
	Logger logger = Logger.getLogger(AdminSpecialPositionController.class);

	@RequestMapping("/special_position_list")
	public String list(SpecialPositionCriteria specialPositionCriteria, PageInfo pageInfo, Model model) {
		List<SpecialPosition> list = specialPositionService.getSpecialPositionList(specialPositionCriteria, pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("criteria", specialPositionCriteria);
		model.addAttribute("pageInfo", pageInfo);
		return AdminConstants.JSP_SPECIAL_POSITION + "/special_position_list.jsp";
	}
	
	@RequestMapping("/special_position_detail/{id}")
	public String detail(@PathVariable Long id, Model model) {
		SpecialPosition specialPosition = specialPositionService.getSpecialPosition(id);
		model.addAttribute("specialPosition", specialPosition);
		return AdminConstants.JSP_SPECIAL_POSITION + "/special_position_detail.jsp";
	}
	
	@RequestMapping("/special_position_add")
	public String add(Model model) {
		model.addAttribute("levelNameMap", PositionLevel.LEVEL_NAME_MAP);
		return AdminConstants.JSP_SPECIAL_POSITION + "/special_position_add.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/doAdd")
	public AjaxPageResponse doAdd(SpecialPosition specialPosition) {
		try {
			specialPositionService.add(specialPosition);
			SpecialPositionIndex.addToMemory(specialPosition);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功！", "special_position_list");
		}catch (Exception e) {
			logger.error("添加失败！", e);
			return AjaxPageResponse.FAILD("添加失败！");
		}
	}
	
	@RequestMapping("/special_position_edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		SpecialPosition specialPosition = specialPositionService.getSpecialPosition(id);
		model.addAttribute("specialPosition", specialPosition);
		model.addAttribute("levelNameMap", PositionLevel.LEVEL_NAME_MAP);
		return AdminConstants.JSP_SPECIAL_POSITION + "/special_position_edit.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/doEdit")
	public AjaxPageResponse doEdit(SpecialPosition specialPosition) {
		try {
			specialPositionService.update(specialPosition);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功！", "special_position_list");
		}catch (Exception e) {
			logger.error("修改失败", e);
			return AjaxPageResponse.FAILD("修改失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/special_position_delete/{id}")
	public AjaxPageResponse delete(@PathVariable Long id) {
		try {
			SpecialPosition specialPosition = specialPositionService.getSpecialPosition(id);
			specialPositionService.delete(id);
			SpecialPositionIndex.removeFromMemory(specialPosition);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功！");
		}catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败！");
		}
	}
	
}

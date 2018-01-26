package cn.sowell.datacenter.admin.controller.position;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.sowell.datacenter.admin.controller.AdminConstants;


@Controller
@RequestMapping(AdminConstants.URI_BASE + "/position")
public class AdminPositionController {
	/*
	@Resource
	PositionService positionService;
	
	Logger logger = Logger.getLogger(AdminPositionController.class);
	
	@RequestMapping("/position_list")
	public String list(PositionCriteria positionCriteria, PageInfo pageInfo, Model model) {
		List<Position> positionList = positionService.getPositionList(positionCriteria, pageInfo);
		model.addAttribute("list", positionList);
		model.addAttribute("criteria", positionCriteria);
		model.addAttribute("pageInfo", pageInfo);
		return AdminConstants.JSP_POSITION + "/position_list.jsp";
	}
	
	@RequestMapping("/position_detail/{id}")
	public String detail(@PathVariable Long id, Model model) {
		Position position = positionService.getPosition(id);
		model.addAttribute("position", position);
		return AdminConstants.JSP_POSITION + "/position_detail.jsp";
	}
	
	@RequestMapping("/position_add")
	public String add(Model model) {
		model.addAttribute("levelNameMap", PositionLevel.LEVEL_NAME_MAP);
		return AdminConstants.JSP_POSITION + "/position_add.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/doAdd")
	public AjaxPageResponse doAdd(Position position) {
		try {
			positionService.add(position);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功！", "position_list");
		}catch (Exception e) {
			logger.error("添加失败！", e);
			return AjaxPageResponse.FAILD("添加失败！");
		}
	}
	
	@RequestMapping("/position_edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		Position position = positionService.getPosition(id);
		model.addAttribute("position", position);
		model.addAttribute("levelNameMap", PositionLevel.LEVEL_NAME_MAP);
		return AdminConstants.JSP_POSITION + "/position_edit.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/doEdit")
	public AjaxPageResponse doEdit(Position position) {
		try {
			positionService.update(position);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功！", "position_list");
		}catch (Exception e) {
			logger.error("修改失败", e);
			return AjaxPageResponse.FAILD("修改失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/position_delete/{id}")
	public AjaxPageResponse delete(@PathVariable Long id) {
		try {
			positionService.delete(id);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功！");
		}catch (Exception e) {
			logger.error("删除失败", e);
			return AjaxPageResponse.FAILD("删除失败！");
		}
	}*/

}

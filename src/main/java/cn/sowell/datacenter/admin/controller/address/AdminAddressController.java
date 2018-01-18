package cn.sowell.datacenter.admin.controller.address;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.sowell.copframe.dto.ajax.AjaxPageResponse;
import cn.sowell.copframe.dto.ajax.JSONObjectResponse;
import cn.sowell.copframe.dto.ajax.JsonRequest;
import cn.sowell.copframe.dto.ajax.ResponseJSON;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.datacenter.admin.controller.AdminConstants;
import cn.sowell.datacenter.model.address.pojo.SplitedAddressEntityTemp;
import cn.sowell.datacenter.model.address.service.AddressEntityService;
import cn.sowell.datacenter.model.position.service.PositionService;

import com.abc.address.service.AddressService;
import com.abc.address.service.AddressServiceFactory;
import com.abc.extface.dto.AddressCode;
import com.abc.extface.dto.AddressElement;
import com.abc.extface.dto.AddressEntity;
import com.abc.extface.dto.Position;
import com.abc.extface.dto.SplitedAddressEntity;
import com.abc.position.constant.PositionLevel;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(AdminConstants.URI_BASE + "/address")
public class AdminAddressController {
	
	@Resource
	AddressEntityService addressEntityService;
	
	@Resource
	PositionService positionService;
	
	Logger logger = Logger.getLogger(AdminAddressController.class);
	
	private static AddressService addressService = AddressServiceFactory.getInstance();
	
	@RequestMapping("/list")
	public String list(@RequestParam(required=false,defaultValue="") String addressStr, PageInfo pageInfo, Model model){
		List<SplitedAddressEntity> addressList = addressEntityService.queryAddressStrList(addressStr, pageInfo);
		model.addAttribute("list", addressList);	
		model.addAttribute("addressStr", addressStr);
		model.addAttribute("pageInfo", pageInfo);
		return AdminConstants.JSP_ADDRESS + "/address_list.jsp";
	}
	
	@RequestMapping("/add")
	public String add(Model model) {
		return AdminConstants.JSP_ADDRESS + "/address_add.jsp";
	}
	
//	@ResponseBody
//	@RequestMapping("/doAdd")
//	public AjaxPageResponse doAdd(SplitedAddressEntity splitedAddressEntity) throws UnsupportedEncodingException {
//		/*name = new String(name.getBytes("ISO-8859-1"),  "UTF-8");
//		SplitedAddressEntity splitedAddressEntity = new SplitedAddressEntity();
//		splitedAddressEntity.setName(name);*/
//		try {
//			SplitedAddressEntity sAddressEntity = addressService.queryEntity(splitedAddressEntity.getName());
//			System.out.println("splitedAddressEntity name:" + sAddressEntity.getName());
//			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("添加成功！", "address_list");
//		}catch (Exception e) {
//			logger.error("添加失败！", e);
//			return AjaxPageResponse.FAILD("添加失败！");
//		}
//		
//	}
	
	@RequestMapping("/doAdd")
	public String doAdd(SplitedAddressEntity splitedAddressEntity, Model model) {
		try {
			SplitedAddressEntity sAddressEntity = addressService.queryEntity(splitedAddressEntity.getName());
			System.out.println("splitedAddressEntity name:" + sAddressEntity.getName());
			if(sAddressEntity.getArtificialSplitName() != null && !sAddressEntity.getArtificialSplitName().equals("")) {
				model.addAttribute("list", sAddressEntity.getArtificialElements());
			}else {
				model.addAttribute("list", sAddressEntity.getElements());
			}
			model.addAttribute("splitedAddressEntity", sAddressEntity);
			model.addAttribute("levelNameMap", PositionLevel.LEVEL_NAME_MAP);
		}catch (Exception e) {
			logger.error("添加失败！", e);
		}
		return AdminConstants.JSP_ADDRESS + "/address_edit_after_add.jsp";
	}
	
//	@ResponseBody
//	@RequestMapping("/detail")
//	public String detail(@RequestParam String addressStr, Model model) throws UnsupportedEncodingException {
//		addressStr = new String(addressStr.getBytes("ISO-8859-1"),  "UTF-8");
//		SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(addressStr);
//		
//		/*if(splitedAddressEntity.getArtificialSplitName() != null && !splitedAddressEntity.getArtificialSplitName().equals("")) {
//			model.addAttribute("list", splitedAddressEntity.getArtificialElements());
//		}else {
//			model.addAttribute("list", splitedAddressEntity.getElements());
//		}*/
//		model.addAttribute("splitedAddressEntity", splitedAddressEntity);
//		model.addAttribute("addressStr", addressStr);
//		return AdminConstants.JSP_ADDRESS + "/address_detail.jsp";
//	}
	
	@ResponseBody
	@RequestMapping("/detail")
	public ResponseJSON detail(@RequestParam String addressStr, Model model) throws UnsupportedEncodingException {
		SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(addressStr);
		Position position = positionService.getPosition(splitedAddressEntity.getPositionCode());
		JSONObject jo = new JSONObject();
		jo.put("addressStr", splitedAddressEntity.getName());
		jo.put("position", position.getName());
		jo.put("keyPoint", splitedAddressEntity.getKeyPoint());
		jo.put("laterPart", splitedAddressEntity.getLaterPart());
		jo.put("splitName", splitedAddressEntity.getSplitNameToShow());
		jo.put("artificialSplitName", splitedAddressEntity.getArtificialSplitNameToShow());
		JSONObjectResponse jres = new JSONObjectResponse();
		jres.setJsonObject(jo);
		return jres;
	}
	
	@RequestMapping("/edit")
	public String edit(@RequestParam String addressStr ,Model model) throws UnsupportedEncodingException {
		addressStr = new String(addressStr.getBytes("ISO-8859-1"),  "UTF-8");
		SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(addressStr);
		
		if(splitedAddressEntity.getArtificialSplitName() != null && !splitedAddressEntity.getArtificialSplitName().equals("")) {
			model.addAttribute("list", splitedAddressEntity.getArtificialElements());
		}else {
			model.addAttribute("list", splitedAddressEntity.getElements());
		}
		model.addAttribute("splitedAddressEntity", splitedAddressEntity);
		model.addAttribute("addressStr", addressStr);
		model.addAttribute("levelNameMap", PositionLevel.LEVEL_NAME_MAP);
		return AdminConstants.JSP_ADDRESS + "/address_edit.jsp";
	}
	
	@RequestMapping("/edit_name")
	public String editName(@RequestParam String addressStr, Model model) throws UnsupportedEncodingException {
		addressStr = new String(addressStr.getBytes("ISO-8859-1"),  "UTF-8");
		model.addAttribute("addressStr", addressStr);
		return AdminConstants.JSP_ADDRESS + "/address_edit_name.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/doEdit")
	public AjaxPageResponse doEdit(SplitedAddressEntityTemp splitedAddressEntityTemp) {
		List<AddressElement> list = new ArrayList<AddressElement>();
		splitedAddressEntityTemp.getElements().forEach(ele->list.add(ele));
		splitedAddressEntityTemp.getSplitedAddressEntity().setArtificialElements(list);
		try {
			addressService.saveOrUpdate(splitedAddressEntityTemp.getSplitedAddressEntity());
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功！", "address_list");
		}catch (Exception e) {
			logger.error("修改失败！", e);
			return AjaxPageResponse.FAILD("修改失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/doEditName")
	public AjaxPageResponse doEditName(@RequestParam String oldName, @RequestParam String name) {
		try {
			addressEntityService.deleteAddressEntity(oldName);
			@SuppressWarnings("unused")
			SplitedAddressEntity sAddressEntity = addressService.queryEntity(name);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("修改成功！", "address_list");
		}catch (Exception e) {
			logger.error("操作失败！", e);
			return AjaxPageResponse.FAILD("修改失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public AjaxPageResponse delete(@RequestParam String addressStr) throws UnsupportedEncodingException {
		addressStr = new String(addressStr.getBytes("ISO-8859-1"),  "UTF-8");
		try {
			addressEntityService.deleteAddressEntity(addressStr);
			return AjaxPageResponse.REFRESH_LOCAL("删除成功！");
		}catch (Exception e) {
			logger.error("删除失败！", e);
			return AjaxPageResponse.FAILD("删除失败！");
		}
	}
	
	/**
	 * 查看相同  第一次进入查看相同
	 * @param addressStr
	 * @param theSamePageInfo
	 * @param addressPageInfo
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/categoryList")
	public String categoryList(@RequestParam String addressStr, PageInfo theSamePageInfo, PageInfo addressPageInfo, Model model) throws UnsupportedEncodingException {
		addressStr = new String(addressStr.getBytes("ISO-8859-1"),  "UTF-8");
		SplitedAddressEntity sAddressEntity = addressService.queryEntity(addressStr);
		List<SplitedAddressEntity> list = addressEntityService.findTheSameAddress(sAddressEntity.getCode(), theSamePageInfo);
		List<SplitedAddressEntity> addressList = addressEntityService.queryNotTheSameAddressList(null, sAddressEntity.getCode(), addressPageInfo);
		model.addAttribute("addressEntity", sAddressEntity);
		model.addAttribute("sameList", list);
		model.addAttribute("addressList", addressList);
		model.addAttribute("theSamePageInfo", theSamePageInfo);
		model.addAttribute("addressPageInfo", addressPageInfo);
		model.addAttribute("addressName", sAddressEntity.getName());
		model.addAttribute("addressCode", sAddressEntity.getCode());
		return AdminConstants.JSP_ADDRESS + "/address_same_list.jsp";
	}
	
	/**
	 * 查看相同--获取所有addressEntity的page list
	 * @param addressStr
	 * @param addressPageInfo
	 * @param model
	 * @return
	 */
	@RequestMapping("/getAddressList")
	public String getAddressEntityList(@RequestParam(required=false, defaultValue="") String addressStr, @RequestParam String addressCode, PageInfo addressPageInfo, Model model) {
		List<SplitedAddressEntity> addressList = addressEntityService.queryNotTheSameAddressList(addressStr, addressCode, addressPageInfo);
		model.addAttribute("addressStr", addressStr);
		model.addAttribute("addressList", addressList);
		model.addAttribute("addressPageInfo", addressPageInfo);
		model.addAttribute("addressCode", addressCode);
		return AdminConstants.JSP_ADDRESS + "/address_same_list_right.jsp";
	}
	
	/**
	 * 查看相同--获取相同地址列表
	 * @param addressCode
	 * @param theSamePageInfo
	 * @param model
	 * @return
	 */
	@RequestMapping("/sameList")
	public String getSameAddressEntityList(@RequestParam String addressName, @RequestParam String addressCode, PageInfo theSamePageInfo, Model model) {
		List<SplitedAddressEntity> list = addressEntityService.findTheSameAddress(addressCode, theSamePageInfo);
		model.addAttribute("sameList", list);
		model.addAttribute("theSamePageInfo", theSamePageInfo);
		model.addAttribute("addressName", addressName);
		return AdminConstants.JSP_ADDRESS + "/address_same_list_left.jsp";
	}
	
	/**
	 * 查看相同--单个移除相同地址
	 * @param addressStr
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping("/remove")
	public ResponseJSON removeCategory(@RequestParam String addressStr, Model model) throws UnsupportedEncodingException {
		JSONObjectResponse jres = new JSONObjectResponse();
		//addressStr = new String(addressStr.getBytes("ISO-8859-1"),  "UTF-8");
		SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(addressStr);
		splitedAddressEntity.createCode();
		//splitedAddressEntity.setCode(null);
		try {
			addressService.saveOrUpdate(splitedAddressEntity);
			jres.put("result", "success");
		}catch (Exception e) {
			logger.error("操作失败！", e);
			jres.put("result", "failed");
		}
		return jres;
	}
	
	@ResponseBody
	@RequestMapping("/batchRemove")
	public JSONObjectResponse batchRemoveCategory(@RequestBody JsonRequest jreq) {
		JSONObjectResponse jres = new JSONObjectResponse();
		try {
			for(int i = 0; i < jreq.getJsonObject().getJSONArray("addressNames").size(); i++) {
				SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(jreq.getJsonObject().getJSONArray("addressNames").get(i).toString());
				splitedAddressEntity.createCode();
				addressService.saveOrUpdate(splitedAddressEntity);
			}
			jres.put("result", "success");
		}catch (Exception e) {
			logger.error("操作失败！", e);
			jres.put("result", "failed");
		}
		return jres;
	}
	
	/**
	 * 查看当前的addressEntity和所有的addressCode（弃用）
	 * @param addressStr
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	@RequestMapping("/replyCategory")
	public String getCategory(@RequestParam String addressStr, Model model) throws UnsupportedEncodingException {
		addressStr = new String(addressStr.getBytes("ISO-8859-1"),  "UTF-8");
		SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(addressStr);
		List<AddressCode> addressCodeList = addressEntityService.getAddressCodeList();
		model.addAttribute("splitedAddressEntity", splitedAddressEntity);
		model.addAttribute("list", addressCodeList);
		return AdminConstants.JSP_ADDRESS + "/address_Category.jsp";
	}
	
	/**
	 * 更新重新分配相同地址后的addressEntity(暂时不用)
	 * @param splitedAddressEntity
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateCategory")
	public AjaxPageResponse updateCategory(SplitedAddressEntity splitedAddressEntity) {
		try {
			@SuppressWarnings("unused")
			AddressEntity addressEntity = addressService.saveOrUpdate(splitedAddressEntity);
			return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("操作成功！", "address_category_list");
		}catch (Exception e) {
			return AjaxPageResponse.FAILD("操作失败！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/updateAddressEntityCategory")
	public JSONObjectResponse updateAddressEntityCategory(@RequestParam String addressCode, @RequestParam String addressName) {
		JSONObjectResponse jres = new JSONObjectResponse();
		SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(addressName);
		splitedAddressEntity.setCode(addressCode);
		try {
			addressService.saveOrUpdate(splitedAddressEntity);
			jres.put("result", "success");
		}catch (Exception e) {
			logger.error("更新失败！", e);
			jres.put("result", "failed");
		}
		return jres;
	}
	@ResponseBody
	@RequestMapping("/batchUpdateAddressEntityCategory")
	public JSONObjectResponse batchUpdateAddressEntityCategory(@RequestBody JsonRequest jreq) {
		JSONObjectResponse jres = new JSONObjectResponse();
		try {
			for(int i = 0; i < jreq.getJsonObject().getJSONArray("addressNames").size(); i++) {
				SplitedAddressEntity splitedAddressEntity = addressService.queryEntity(jreq.getJsonObject().getJSONArray("addressNames").get(i).toString());
				splitedAddressEntity.setCode(jreq.getJsonObject().getString("addressCode"));
				addressService.saveOrUpdate(splitedAddressEntity);
			}
			jres.put("result", "success");
		}catch (Exception e) {
			logger.error("更新失败！", e);
			jres.put("result", "failed");
		}
		return jres;
	}
	
	@RequestMapping("/import")
	public String importExcel() {
		return AdminConstants.JSP_ADDRESS + "/address_import.jsp";
	}
	
	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping("/doImport")
	public AjaxPageResponse doImport(@RequestParam MultipartFile file) throws IOException, BiffException {
		InputStream inputStream = file.getInputStream();
		HSSFWorkbook hssfWorkbook = null;
		XSSFWorkbook xssfWorkbook = null;
		
		String fileName = file.getOriginalFilename();
		
		
		if(fileName.endsWith(".xlsx")) {
			xssfWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
			int rows = sheet.getLastRowNum(); //行数
			for(int i = 1; i <= rows; i++) {
				XSSFRow row = sheet.getRow(i);
				XSSFCell cell = row.getCell(1);	//第i行第2个单元格
				String addressStr = cell.getStringCellValue();
				try {
					addressService.queryEntity(addressStr);
				}catch (Exception e) {
					logger.error("第" + i + "条导入失败！", e);
					return AjaxPageResponse.FAILD("第" + i + "条导入失败！");
				}
			}
		}else {
			hssfWorkbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			int rows = sheet.getLastRowNum(); //行数
			for(int i = 1; i <= rows; i++) {
				HSSFRow row = sheet.getRow(i);
				HSSFCell cell = row.getCell(1);	//第i行第2个单元格
				String addressStr = cell.getStringCellValue();
				System.out.println("---->xls第" + i +"行:" + addressStr);
				try {
					addressService.queryEntity(addressStr);
				}catch (Exception e) {
					logger.error("第" + i + "条导入失败！", e);
					return AjaxPageResponse.FAILD("第" + i + "条导入失败！");
				}
			}
		}
		return AjaxPageResponse.CLOSE_AND_REFRESH_PAGE("导入 成功！", "address_list");
	}
	
}

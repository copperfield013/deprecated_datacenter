package cn.sowell.datacenter.admin.controller.basepeople;

import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.service.BasePeopleService;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/admin/peopleDictionary")
public class PeopleDateDcitionaryController {

    @Resource
    BasePeopleService basePeopleService;

    private TransportClient client;

    Logger logger = Logger.getLogger(PeopleDateDcitionaryController.class);

    @org.springframework.web.bind.annotation.InitBinder
    public void InitBinder(ServletRequestDataBinder binder) {
        //System.out.println("执行了InitBinder方法");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
    }


    @RequestMapping(value = "add")
    public void adddic (TBasePeopleDictionaryEntity tBasePeopleDictionaryEntity){

        basePeopleService.insert(tBasePeopleDictionaryEntity);
    }

}
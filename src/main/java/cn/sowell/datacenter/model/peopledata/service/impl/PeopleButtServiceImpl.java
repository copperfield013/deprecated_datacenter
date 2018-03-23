package cn.sowell.datacenter.model.peopledata.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;

import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.utils.date.FrameDateFormat;
import cn.sowell.datacenter.model.abc.service.ABCExecuteService;
import cn.sowell.datacenter.model.basepeople.ExcelModelCriteria;
import cn.sowell.datacenter.model.basepeople.dao.BasePeopleDao;
import cn.sowell.datacenter.model.basepeople.pojo.ExcelModel;
import cn.sowell.datacenter.model.basepeople.pojo.ExcelModelOrder;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleDictionaryEntity;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleInformationEntity;
import cn.sowell.datacenter.model.basepeople.pojo.TBasePeopleItemEntity;
import cn.sowell.datacenter.model.peopledata.pojo.PeopleData;
import cn.sowell.datacenter.model.peopledata.service.PeopleButtService;
import cn.sowell.datacenter.model.peopledata.service.PeopleDataService;

@Service
public class PeopleButtServiceImpl implements PeopleButtService{

	@Resource
	BasePeopleDao basePeopleDao;
	
	@Resource
	PeopleDataService peopleService;
	
	@Resource
	ABCExecuteService abcService;

	@Resource
	FrameDateFormat dateFormat;
	
	@Override
	public List<TBasePeopleItemEntity> fieldList(String field) {
		return basePeopleDao.fieldList(field);
	}

	@Override
	public void mergePeople(String peopleCode, Map<String, String> map) {
		PeopleData people;
		boolean hasPeopleCode = TextUtils.hasText(peopleCode);
		if(hasPeopleCode) {
			people = peopleService.getPeople(peopleCode);
			people.setPeopleCode(peopleCode);
		}else {
			people = new PeopleData();
		}
		bindProperties(people, map);
		if(!hasPeopleCode) people.setPeopleCode(null);
		abcService.savePeople(people);
	}

	@Override
	public List<TBasePeopleDictionaryEntity> titleSearch(String txt) {
		return basePeopleDao.searchList(txt);
	}

	private void bindProperties(PeopleData people, Map<String, String> map) {
		WebDataBinder binder = new WebDataBinder(people);
		binder.addCustomFormatter(new Formatter<Date>() {
			@Override
			public Date parse(String text, Locale locale) throws ParseException {
				return dateFormat.parse(text);
			}

			@Override
			public String print(Date object, Locale locale) {
				return dateFormat.formatDate(object);
			}
		}, Date.class);
		binder.bind(new MutablePropertyValues(map));
	}

	@Override
	public List<TBasePeopleDictionaryEntity> getColumnNames(Long modelId) {
		// TODO Auto-generated method stub
		return basePeopleDao.getColumns(modelId);
	}	

	@Override
	public List<String[]> columnLists(List<TBasePeopleDictionaryEntity> keys) {
		// TODO Auto-generated method stub
        List<String[]> columnLists = new ArrayList<String[]>();
        for(int i=0;i<keys.size();i++){
        	String key = keys.get(i).getcCnEnglish();
        	List<TBasePeopleItemEntity> columnList = fieldList(key);
        	String[] col = new String[columnList.size()];
        	for(int j=0; j<columnList.size(); j++){
        		col[j] = columnList.get(j).getC_enum_value();
        	}
        	columnLists.add(col);
        }
		return columnLists;
	}

	@Override
	public List<ExcelModel> queryModel(ExcelModelCriteria criteria, PageInfo pageInfo,String type) {
		// TODO Auto-generated method stub
		return basePeopleDao.queryExcelModel(criteria,pageInfo ,type);
	}

	@Override
	public void addExcelList(ExcelModel model, String[] list) {
		Long modelId = basePeopleDao.insert(model);
		for(int i=0;i<list.length;i++){
			ExcelModelOrder order = new ExcelModelOrder();
			order.setModel_id(modelId);
			order.setDictionary_id(Long.parseLong(list[i]));
			order.setOrder_id((long)i+1);
			basePeopleDao.insert(order);
		}
		
	}



	@Override
	public void updateExcelList(ExcelModel model, String[] list) {
		Long modelId = model.getId();
		basePeopleDao.saveOrUpdate(model);
		deleteExcelOrder(modelId);
		for(int i=0;i<list.length;i++){
			ExcelModelOrder order = new ExcelModelOrder();
			order.setModel_id(modelId);
			order.setDictionary_id(Long.parseLong(list[i]));
			order.setOrder_id((long)i+1);
			basePeopleDao.insert(order);
		}
	}
	
	@Override
	public void deleteModel(Long modelId) {
		ExcelModel model = new ExcelModel();
		model.setId(modelId);
		basePeopleDao.delete(model);
		deleteExcelOrder(modelId);
	}
	
	public void deleteExcelOrder(Long modelId) {
		basePeopleDao.deleteExcelOrder(modelId);
	}

	@Override
	public List<TBasePeopleDictionaryEntity> getDicByModelId(Long modelId) {
		return basePeopleDao.getDicByModelId(modelId);
		
	}

	@Override
	public ExcelModel getExcelModel(Long modelId) {
		return basePeopleDao.get(ExcelModel.class, modelId);
	}

	@Override
	public List<TBasePeopleInformationEntity> getDicInfo() {
		return basePeopleDao.infoList();
	}

}

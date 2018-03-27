package cn.sowell.datacenter.model.modules.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.common.property.PropertyPlaceholder;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.datacenter.model.abc.resolver.EntityPropertyParser;
import cn.sowell.datacenter.model.admin.pojo.ExportStatus;
import cn.sowell.datacenter.model.modules.bean.EntityPagingIterator;
import cn.sowell.datacenter.model.modules.bean.ExportDataPageInfo;
import cn.sowell.datacenter.model.modules.exception.ExportBreakException;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.datacenter.model.modules.service.ModulesService;
import cn.sowell.datacenter.model.tmpl.config.NormalCriteria;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.datacenter.model.tmpl.pojo.TemplateListTempalte;

@Service
public class ExportServiceImpl implements ExportService {

	private Map<String, ExportStatus> statusMap = new HashMap<String, ExportStatus>();
	
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
	
	Logger logger = Logger.getLogger(ExportServiceImpl.class);
	
	@Resource
	ModulesService mService;
	
	private Long exportCacheTimeout = null;
	
	@Override
	public void clearExportCache(){
		if(exportCacheTimeout == null){
			try {
				exportCacheTimeout = FormatUtils.toLong(PropertyPlaceholder.getProperty("export_cache_timeout")) * 1000;
			} catch (Exception e) {
				exportCacheTimeout = Long.MAX_VALUE;
			}
		}
		FileSystemResource folder = new FileSystemResource(PropertyPlaceholder.getProperty("export_cache_path"));
		File[] fs = folder.getFile().listFiles();
		Map<String, File> fMap = new HashMap<String, File>();
		if(fs != null){
			for (File file : fs) {
				fMap.put(file.getName().split("\\.")[0], file);
			}
		}
		Iterator<Entry<String, ExportStatus>> itr = statusMap.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, ExportStatus> entry = itr.next();
			ExportStatus status = entry.getValue();
			fMap.remove(status.getUuid());
			if(status != null && (status.isBreaked() || status.isCompleted())){
				if(System.currentTimeMillis() - status.getLastCheckTime() > exportCacheTimeout){
					FileSystemResource resource = new FileSystemResource(PropertyPlaceholder.getProperty("export_cache_path") + status.getUuid() + ".xlsx");
					if(resource.exists()){
						try {
							if(resource.getFile().delete()){
								itr.remove();
								logger.info("清除成功,uuid=" + status.getUuid());
							}
						} catch (Exception e) {
						}
					}
				}
			}
		}
		fMap.forEach((uuid, file)->{
			try {
				file.delete();
				logger.info("删除statusMap中不存在的缓存文件，uuid=" + uuid);
			} catch (Exception e) {
			}
		});
	}
	
	DecimalFormat df = new DecimalFormat("0.00");
	@Override
	public void startExport(String uuid, TemplateListTempalte ltmpl, Set<NormalCriteria> criteria, ExportDataPageInfo ePageInfo) {
		ExportStatus status = new ExportStatus(uuid);
		status.setExportPageInfo(ePageInfo);
		statusMap.put(uuid, status);
		status.setTotalCount(100);
		status.setCurrent(0);
		fixedThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				XSSFWorkbook workbook = new XSSFWorkbook();
				try {
					status.setCurrent(1);
					XSSFSheet sheet = workbook.createSheet();
					XSSFRow headerRow = sheet.createRow(0);
					int i = 0;
					status.setCurrent(10);
					for (TemplateListColumn column : ltmpl.getColumns()) {
						if("number".equals(column.getSpecialField()) || column.getSpecialField() == null){
							XSSFCell header = headerRow.createCell(i++);
							header.setCellType(CellType.STRING);
							header.setCellValue(column.getTitle());
						}
					}
					status.setCurrent(13);
					status.setMessage("开始查询数据...");
					EntityPagingIterator itr = mService.queryIterator(ltmpl, criteria, ePageInfo);
					status.setMessage("数据查找完成，共有" + itr.getDataCount() + "条数据。开始处理数据...");
					status.setCurrent(20);
					i = 1;
					status.setTotalData(itr.getDataCount());
					while(itr.hasNext()){
						checkBreaked(status);
						EntityPropertyParser parser = itr.next();
						XSSFRow row = sheet.createRow(i);
						float dataProgress = ((float)i)/itr.getDataCount();
						status.setCurrent(20 + (int)(dataProgress * 50));
						int j = 0;
						for (TemplateListColumn column : ltmpl.getColumns()) {
							if("number".equals(column.getSpecialField()) || column.getSpecialField() == null){
								XSSFCell cell = row.createCell(j++);
								cell.setCellType(CellType.STRING);
								if("number".equals(column.getSpecialField())){
									cell.setCellValue(i);
								}else{
									cell.setCellValue(FormatUtils.toString(parser.getProperty(column.getFieldKey())));
								}
							}
						}
						status.setCurrentData(i);
						status.setMessage("已处理数据(" + i + "/" + itr.getDataCount() + ")，速度" + df.format(itr.getSpeed()) 
								+ "条/秒，预计还需要" + df.format(itr.getRemainSecond()) + "秒");
						i++;
					}
					status.setMessage("数据处理完成，开始生成文件");
					FileSystemResource resource = new FileSystemResource(PropertyPlaceholder.getProperty("export_cache_path") + uuid + ".xlsx");
					File file = resource.getFile();
					if(!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
					}
					file.createNewFile();
					OutputStream os = resource.getOutputStream();
					status.setCurrent(80);
					workbook.write(os);
					os.flush();
					os.close();
					status.setMessage("文件生成成功，请点击“下载导出文件”按钮下载导出数据文件");
					status.setCurrent(100);
					status.setCompleted(true);
				}catch(ExportBreakException e){
					statusMap.remove(uuid);
					logger.info("取消导出");
				} catch (IOException e) {
					status.setErrorMsg("创建导出文件时发生错误");
					logger.error("创建导出文件时发生错误", e);
				} catch(Exception e){
					status.setErrorMsg("导出时发生错误");
					logger.error("导出时发生错误", e);
				}finally{
					try {
						workbook.close();
					} catch (IOException e) {
						logger.error("关闭导出工作簿之前发生错误", e);
					}
				}
			}
		});
	}
	
	Long exportCheckPollTimeout = null;
	
	private void checkBreaked(ExportStatus status) throws ExportBreakException {
		if(exportCheckPollTimeout == null){
			try{
				exportCheckPollTimeout = FormatUtils.toLong(PropertyPlaceholder.getProperty("export_check_poll_timeout")) * 1000;
			}catch(Exception e){exportCheckPollTimeout = Long.MAX_VALUE;}
		}
		if(System.currentTimeMillis() - status.getLastCheckTime() > exportCheckPollTimeout){
			throw new ExportBreakException("超过" + exportCheckPollTimeout + "毫秒没有检测导入状态，将关闭该导出");
		}
		if(status.isBreaked()){
			throw new ExportBreakException();
		}
		
	}
	
	@Override
	public ExportStatus getExportStatus(String uuid) {
		ExportStatus status = statusMap.get(uuid);
		return status;
	}
	
	@Override
	public AbstractResource getDownloadResource(String uuid) {
		ExportStatus status = getExportStatus(uuid);
		if(status != null && status.isCompleted()){
			return new FileSystemResource(PropertyPlaceholder.getProperty("export_cache_path") + uuid + ".xlsx");
		}
		return null;
	}

}

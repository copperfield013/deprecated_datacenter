package cn.sowell.datacenter.model.modules.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.spring.properties.PropertyPlaceholder;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.web.poll.ProgressPollableThreadFactory;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.model.modules.exception.ExportBreakException;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.dataserver.model.modules.bean.EntityPagingIterator;
import cn.sowell.dataserver.model.modules.bean.ExportDataPageInfo;
import cn.sowell.dataserver.model.modules.pojo.criteria.NormalCriteria;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListColumn;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateListTemplate;

@Service
public class ExportServiceImpl implements ExportService {

	//ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
	
	ProgressPollableThreadFactory pFactory = new ProgressPollableThreadFactory() {
		{
			setThreadExecutor(new ThreadPoolExecutor(6, 10, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()));
		}
	};
	
	
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
		if(fs != null){
			for (File file : fs) {
				String uuid = file.getName().split("\\.")[0];
				WorkProgress progress = pFactory.getProgress(uuid);
				if(progress == null || System.currentTimeMillis() - progress.getLastVeniTime() > exportCacheTimeout) {
					if(file.exists()) {
						try {
							file.delete();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}
	
	DecimalFormat df = new DecimalFormat("0.00");
	@Override
	public void startExport(WorkProgress progress, TemplateListTemplate ltmpl, Set<NormalCriteria> criteria, ExportDataPageInfo ePageInfo, UserIdentifier user) {
		progress.setTotal(100);
		progress.setCurrent(0);
		XSSFWorkbook workbook = new XSSFWorkbook();
		progress.veni();
		pFactory.createThread(progress, p->{
			progress.setCurrent(1);
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow headerRow = sheet.createRow(0);
			int i = 0;
			progress.setCurrent(10);
			for (TemplateListColumn column : ltmpl.getColumns()) {
				if("number".equals(column.getSpecialField()) || column.getSpecialField() == null){
					XSSFCell header = headerRow.createCell(i++);
					header.setCellType(CellType.STRING);
					header.setCellValue(column.getTitle());
				}
			}
			progress.setCurrent(13);
			progress.appendMessage("开始查询数据...");
			EntityPagingIterator itr = mService.queryIterator(ltmpl, criteria, ePageInfo, user);
			progress.getLogger().success("数据查找完成，共有" + itr.getDataCount() + "条数据。开始处理数据...");
			progress.setCurrent(20);
			i = 1;
			progress.setResponseData("totalData", itr.getDataCount());
			while(itr.hasNext()){
				checkBreaked(progress);
				ModuleEntityPropertyParser parser = itr.next();
				XSSFRow row = sheet.createRow(i);
				float dataProgress = ((float)i)/itr.getDataCount();
				progress.setCurrent(20 + (int)(dataProgress * 50));
				int j = 0;
				for (TemplateListColumn column : ltmpl.getColumns()) {
					if("number".equals(column.getSpecialField()) || column.getSpecialField() == null){
						XSSFCell cell = row.createCell(j++);
						cell.setCellType(CellType.STRING);
						if("number".equals(column.getSpecialField())){
							cell.setCellValue(i);
						}else{
							cell.setCellValue(FormatUtils.toString(parser.getFormatedProperty(column.getFieldKey())));
						}
					}
				}
				progress.setResponseData("currentData", i);
				progress.getLogger().success("已处理数据(" + i + "/" + itr.getDataCount() + ")，速度" + df.format(itr.getSpeed()) 
						+ "条/秒，预计还需要" + df.format(itr.getRemainSecond()) + "秒");
				i++;
			}
			progress.appendMessage("数据处理完成，开始生成文件");
			FileSystemResource resource = new FileSystemResource(PropertyPlaceholder.getProperty("export_cache_path") + progress.getUUID() + ".xlsx");
			File file = resource.getFile();
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			OutputStream os = resource.getOutputStream();
			progress.setCurrent(80);
			workbook.write(os);
			os.flush();
			os.close();
			progress.getLogger().success("文件生成成功，请点击“下载导出文件”按钮下载导出数据文件");
			progress.setCurrent(100);
			progress.setCompleted();
		}, (p, e)->{
			if(e instanceof ExportBreakException) {
				p.getLogger().warn("导出被取消");
				logger.info("取消导出");
			}else if(e instanceof IOException) {
				p.getLogger().error("创建导出文件时发生错误");
				logger.error("创建导出文件时发生错误", e);
			}else {
				p.getLogger().error("导出时发生错误");
				logger.error("导出时发生错误", e);
			}
		}, p->{
			try {
				workbook.close();
			} catch (IOException e) {
				logger.error("关闭导出工作簿之前发生错误", e);
			}
		}).start();;
	}
	
	Long exportCheckPollTimeout = null;
	
	private void checkBreaked(WorkProgress progress) throws ExportBreakException {
		if(exportCheckPollTimeout == null){
			try{
				exportCheckPollTimeout = FormatUtils.toLong(PropertyPlaceholder.getProperty("export_check_poll_timeout")) * 1000;
			}catch(Exception e){exportCheckPollTimeout = Long.MAX_VALUE;}
		}
		if(System.currentTimeMillis() - progress.getLastVeniTime() > exportCheckPollTimeout){
			throw new ExportBreakException("超过" + exportCheckPollTimeout + "毫秒没有检测导入状态，将关闭该导出");
		}
		if(progress.isBreaked()){
			throw new ExportBreakException();
		}
	}
	
	@Override
	public WorkProgress getExportProgress(String uuid) {
		return pFactory.getProgress(uuid);
	}
	
	@Override
	public AbstractResource getDownloadResource(String uuid) {
		WorkProgress progress = pFactory.getProgress(uuid);
		if(progress != null && progress.isCompleted()){
			return new FileSystemResource(PropertyPlaceholder.getProperty("export_cache_path") + uuid + ".xlsx");
		}
		return null;
	}
	
	@Override
	public void stopExport(String uuid) {
		WorkProgress progress = getExportProgress(uuid);
		if(progress != null) {
			progress.setBreaked();
			removeExport(uuid);
		}
	}
	@Override
	public void removeExport(String uuid) {
		pFactory.removeProgress(uuid);
	}

}

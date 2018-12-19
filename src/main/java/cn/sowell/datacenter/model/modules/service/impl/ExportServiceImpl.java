package cn.sowell.datacenter.model.modules.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import cn.sowell.copframe.common.UserIdentifier;
import cn.sowell.copframe.spring.properties.PropertyPlaceholder;
import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.copframe.web.poll.ConsumerThrowException;
import cn.sowell.copframe.web.poll.ProgressPollableThreadFactory;
import cn.sowell.copframe.web.poll.WorkProgress;
import cn.sowell.datacenter.entityResolver.ModuleEntityPropertyParser;
import cn.sowell.datacenter.entityResolver.UserCodeService;
import cn.sowell.datacenter.model.modules.bean.EntityExportWriter;
import cn.sowell.datacenter.model.modules.exception.ExportBreakException;
import cn.sowell.datacenter.model.modules.service.ExportService;
import cn.sowell.dataserver.model.modules.bean.EntityPagingIterator;
import cn.sowell.dataserver.model.modules.bean.ExportDataPageInfo;
import cn.sowell.dataserver.model.modules.pojo.criteria.NormalCriteria;
import cn.sowell.dataserver.model.modules.service.ModulesService;
import cn.sowell.dataserver.model.tmpl.pojo.TemplateDetailTemplate;
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
	
	@Resource
	UserCodeService userCodeService;
	
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
	
	@Override
	public void startExport(WorkProgress progress, TemplateListTemplate ltmpl, Set<NormalCriteria> criteria, ExportDataPageInfo ePageInfo, UserIdentifier user) {
		startWholeExport(progress, ltmpl, null, criteria, ePageInfo, user);
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
	
	
	Set<String> exportUUIDs = new HashSet<>();
	@Override
	public AbstractResource getDownloadResource(String uuid) {
		WorkProgress progress = pFactory.getProgress(uuid);
		if(progress != null && progress.isCompleted()
			|| exportUUIDs.contains(uuid)){
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
	
	
	DecimalFormat df = new DecimalFormat("0.00");
	@Override
	public void startWholeExport(
			WorkProgress progress, 
			TemplateListTemplate ltmpl, 
			TemplateDetailTemplate dtmpl,
			Set<NormalCriteria> criteria, 
			ExportDataPageInfo ePageInfo, 
			UserIdentifier user) {
		progress.setTotal(100);
		progress.setCurrent(0);
		XSSFWorkbook workbook = new XSSFWorkbook();
		progress.veni();
		pFactory.createThread(progress, p->{
			userCodeService.setUserCode((String) user.getId());
			progress.setCurrent(1);
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow headerRow = sheet.createRow(1);
			int colnum = 1;
			progress.setCurrent(10);
			CellStyle listHeaderStyle = entityExportWriter.getListHeaderStyle(workbook),
						listValueStyle = entityExportWriter.getListValueStyle(workbook);
			for (TemplateListColumn column : ltmpl.getColumns()) {
				if("number".equals(column.getSpecialField()) || column.getSpecialField() == null){
					XSSFCell header = headerRow.createCell(colnum++);
					header.setCellType(CellType.STRING);
					header.setCellValue(column.getTitle());
					header.setCellStyle(listHeaderStyle);
				}
			}
			progress.setCurrent(13);
			progress.appendMessage("开始查询数据...");
			EntityPagingIterator itr = mService.queryIterator(ltmpl, criteria, ePageInfo, user);
			progress.getLogger().success("数据查找完成，共有" + itr.getDataCount() + "条数据。开始处理数据...");
			progress.setCurrent(20);
			int entityNumber = 1;
			progress.setResponseData("totalData", itr.getDataCount());
			CellStyle linkStyle = entityExportWriter.createLinkStyle(workbook);
			linkStyle.setAlignment(HorizontalAlignment.CENTER);
			int lastColnum = 1;
			while(itr.hasNext()){
				checkBreaked(progress);
				ModuleEntityPropertyParser parser = itr.next();
				XSSFRow row = sheet.createRow(entityNumber + 1);
				float dataProgress = ((float)entityNumber)/itr.getDataCount();
				progress.setCurrent(20 + (int)(dataProgress * 50));
				int j = 1;
				for (TemplateListColumn column : ltmpl.getColumns()) {
					if("number".equals(column.getSpecialField()) || column.getSpecialField() == null){
						XSSFCell cell = row.createCell(j++, CellType.STRING);
						cell.setCellStyle(listValueStyle);
						if("number".equals(column.getSpecialField())){
							cell.setCellValue(entityNumber);
						}else{
							cell.setCellValue(FormatUtils.toString(parser.getFormatedProperty(column.getFieldKey())));
						}
					}
				}
				if(dtmpl != null) {
					String detailSheetName = "entity_" + entityNumber;
					
					XSSFCell linkCell = row.createCell(j);
					XSSFHyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.DOCUMENT);
					link.setAddress(detailSheetName + "!B2");
					linkCell.setHyperlink(link);
					linkCell.setCellValue("详情");
					linkCell.setCellStyle(linkStyle);
					
					
					XSSFSheet detailSheet = workbook.createSheet(detailSheetName);
					entityExportWriter.writeDetail(parser, dtmpl, detailSheet, sheet.getSheetName() + "!" + linkCell.getReference());
				}else {
					j--;
				}
				if(j > lastColnum) {
					lastColnum = j;
				}
				progress.setResponseData("currentData", entityNumber);
				progress.getLogger().success("已处理数据(" + entityNumber + "/" + itr.getDataCount() + ")，速度" + df.format(itr.getSpeed()) 
						+ "条/秒，预计还需要" + df.format(itr.getRemainSecond()) + "秒");
				entityNumber++;
			}
			entityExportWriter.wrapBorder(sheet, new CellRangeAddress(1, entityNumber + 1, 1, lastColnum), BorderStyle.MEDIUM);
			progress.appendMessage("数据处理完成，开始生成文件");
			writeExportFile(progress.getUUID(), os->{
				progress.setCurrent(80);
				workbook.write(os);
			});
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
	
	EntityExportWriter entityExportWriter = new EntityExportWriter();
	
	@Override
	public String exportDetailExcel(ModuleEntityPropertyParser parser, TemplateDetailTemplate dtmpl) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			XSSFSheet sheet = workbook.createSheet("详情" + parser.getTitle());
			entityExportWriter.writeDetail(parser, dtmpl, sheet);
			String uuid = TextUtils.uuid();
			
			writeExportFile(uuid, os->workbook.write(os));
			return uuid;
		} catch (Exception e) {
			throw e;
		}finally {
			workbook.close();
		}
	}

	private void writeExportFile(String uuid, ConsumerThrowException<OutputStream> consumer) throws Exception {
		Assert.notNull(uuid);
		Assert.notNull(consumer);
		FileSystemResource resource = new FileSystemResource(PropertyPlaceholder.getProperty("export_cache_path") + uuid + ".xlsx");
		File file = resource.getFile();
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
		OutputStream os = resource.getOutputStream();
		try {
			consumer.accept(os);
			exportUUIDs.add(uuid);
		} catch (Exception e) {
			throw e;
		}finally {
			os.flush();
			os.close();
		}
	}

}

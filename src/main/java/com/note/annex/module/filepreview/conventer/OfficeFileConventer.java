package com.note.annex.module.filepreview.conventer;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.note.annex.common.utils.SysConfig;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.utils.FileUtil;

@Component
public class OfficeFileConventer {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String uploadfileurl = SysConfig.getInstance().getConfig("file.uploadURL");//文件上传目录
	private String tempfileurl = SysConfig.getInstance().getConfig("file.tmpURL");//临时文件目录

	private String officeHome = SysConfig.getInstance().getConfig("openoffice.installURL");//openoffice安装目录
	private String port = SysConfig.getInstance().getConfig("openoffice.port");//openoffice服务端口号
	String executiontimeout = SysConfig.getInstance().getConfig("openoffice.executiontimeout");
	String queuetimeout = SysConfig.getInstance().getConfig("openoffice.queuetimeout");

	/**
	 * @Fields officeManager : openoffice 管理器
	 */
	private OfficeManager officeManager;

	@PostConstruct
	public void init() {
		startService();
	}

	@PreDestroy
	public void destroy() {
		stopService();
	}

	/**
	 * ����openoffice����
	 */
	private void startService() {
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		this.logger.warn("start openoffice....");
        // 设置OpenOffice.org安装目录
		configuration.setOfficeHome(officeHome);
		// 设置转换端口，默认为8100
		configuration.setPortNumbers(Integer.parseInt(port));
		// 设置任务执行超时为5分钟
		configuration.setTaskExecutionTimeout((executiontimeout!=null)?Long.valueOf(executiontimeout):(long)(1000*60*5));
		// 设置任务队列超时为24小时
		configuration.setTaskQueueTimeout((queuetimeout!=null)?Long.valueOf(queuetimeout):(long)(1000*60*60*24));
		officeManager = configuration.buildOfficeManager();
		officeManager.start(); // 启动服务
		this.logger.warn("openoffice start success!");
	}

	/**
	 * ֹͣopenoffice����
	 */
	private void stopService() {
		this.logger.warn("stop openoffice...");
		if (officeManager != null) {
			officeManager.stop();
		}
		this.logger.warn("stop openoffice success!");
	}

	/**
	 * office�ļ�ͳһתΪhtml��ʽ�ļ�
	 * @param fileModel
	 */
	public void conventerToHtml(FileModel fileModel) {
		// 创建hash目录
		String hashDirPath = uploadfileurl + File.separator + fileModel.getPathId();
		File hashDir = FileUtil.createDir(hashDirPath);
		if (hashDir.exists() && hashDir.isDirectory()) {
			if(fileModel.getTempDir()==null) {
				fileModel.setTempDir(tempfileurl);
			}
			// 复制源文件到hash目录
			String filePath = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
			FileUtil.copyFile(filePath, hashDirPath);
			// 计算文件大小
			fileModel.setFileSize(FileUtil.getFileSize(filePath));
			// 创建resource目录，存放源文件
			String resourceDirPath = hashDirPath + File.separator + "resource";
			File resourceDir = FileUtil.createDir(resourceDirPath);
			if (resourceDir.exists() && resourceDir.isDirectory()) {
				// 进行文件转换
				String fileName = FileUtil.getFileName(fileModel.getOriginal());
				String htmlFilePath = fileName + ".html";
				String inputFile = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
				// 转换后的文件放在resource目录中
				String outputFile = resourceDirPath + File.separator
						+ htmlFilePath;
				this.logger.info("进行文档转换:" + inputFile + " --> " + outputFile);
				OfficeDocumentConverter converter = new OfficeDocumentConverter(this.officeManager);
				File input = new File(inputFile);
				File html = new File(outputFile);
				converter.convert(input, html);
				fileModel.setState(FileModel.STATE_YZH);
				fileModel.setConventedFileName(htmlFilePath);
				// 设置content-type
				fileModel.setOriginalMIMEType("text/html");
			}
			// 创建meta文件，存放文件基本信息
			String metaPath = hashDirPath + File.separator + "meta";
			File metaFile = FileUtil.createFile(metaPath);
			FileUtil.writeContent(metaFile, fileModel, "GBK");
		}
	}

	/**
	 * office�ļ�תΪpdf��ʽ
	 */
	public void conventerToPdf(FileModel fileModel) {
		// 创建hash目录
		String hashDirPath = uploadfileurl + File.separator + fileModel.getPathId();
		File hashDir = FileUtil.createDir(hashDirPath);
		if (hashDir.exists() && hashDir.isDirectory()) {
			if(fileModel.getTempDir()==null) {
				fileModel.setTempDir(tempfileurl);
			}
			// 复制源文件到hash目录
			String filePath = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
			FileUtil.copyFile(filePath, hashDirPath);
			// 计算文件大小
			fileModel.setFileSize(FileUtil.getFileSize(filePath));
			// 创建resource目录，存放源文件
			String resourceDirPath = hashDirPath + File.separator + "resource";
			File resourceDir = FileUtil.createDir(resourceDirPath);
			if (resourceDir.exists() && resourceDir.isDirectory()) {
				// 进行文件转换
				String fileName = FileUtil.getFileName(fileModel.getOriginal());
				String htmlFilePath = fileName + ".pdf";
				String inputFile = fileModel.getTempDir() + File.separator + fileModel.getOriginalFile();
				// 转换后的文件放在resource目录中
				String outputFile = resourceDirPath + File.separator + htmlFilePath;
				this.logger.info("进行文档转换:" + inputFile + " --> " + outputFile);
				OfficeDocumentConverter converter = new OfficeDocumentConverter(this.officeManager);
				File input = new File(inputFile);
				File html = new File(outputFile);
				converter.convert(input, html);
				fileModel.setState(FileModel.STATE_YZH);
				fileModel.setConventedFileName(htmlFilePath);
				// 设置content-type
				fileModel.setOriginalMIMEType("application/pdf");
			}
			// 创建meta文件，存放文件基本信息
			String metaPath = hashDirPath + File.separator + "meta";
			File metaFile = FileUtil.createFile(metaPath);
			FileUtil.writeContent(metaFile, fileModel, "GBK");
		}
	}

}

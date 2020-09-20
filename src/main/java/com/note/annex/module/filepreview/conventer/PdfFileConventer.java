package com.note.annex.module.filepreview.conventer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.note.annex.common.utils.SysConfig;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.utils.FileUtil;

@Component
public class PdfFileConventer {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String uploadfileurl = SysConfig.getInstance().getConfig("file.uploadURL");//文件上传目录
	private String tempfileurl = SysConfig.getInstance().getConfig("file.tmpURL");//临时文件目录

	/**
	 * pdf文件转换后格式不变，改变存储目录为 hash码目录/resource目录 + 源文件 + meta文件
	 * meta文件存储文件基本信息
	 * resource目录存放转换之后的文件，此处依然为源文件
	 * @param fileModel
	 */
	public  void conventer(FileModel fileModel) {
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
				FileUtil.copyFile(filePath, resourceDirPath);
				fileModel.setState(FileModel.STATE_YZH);
				fileModel.setConventedFileName(fileModel.getOriginalFile());
			}
			// 创建meta文件，存放文件基本信息
			String metaPath = hashDirPath + File.separator + "meta";
			File metaFile = FileUtil.createFile(metaPath);
			FileUtil.writeContent(metaFile, fileModel, "GBK");
		}
	}

}

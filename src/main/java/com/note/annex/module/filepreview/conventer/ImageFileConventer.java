package com.note.annex.module.filepreview.conventer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.note.annex.common.utils.SysConfig;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.utils.FileUtil;

/**
 * 图片处理
 */
@Component
public class ImageFileConventer {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String uploadfileurl = SysConfig.getInstance().getConfig("file.uploadURL");//�ϴ�����Ŀ¼
	private String tempfileurl = SysConfig.getInstance().getConfig("file.tmpURL");//��ʱ�ļ�Ŀ¼

	/**
	 * 图片类型文件不转换,只更换存储目录 存储目录为 root/resource目录 + meta文件 + 源文件
	 * resource目录存放转换后的文件，此处依然为源文件
	 * meta文件存放文件基本信息
	 *  @param fileModel
	 */
	public void conventer(FileModel fileModel) {
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

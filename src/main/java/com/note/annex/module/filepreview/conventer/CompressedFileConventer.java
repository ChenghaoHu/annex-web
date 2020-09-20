package com.note.annex.module.filepreview.conventer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.note.annex.common.utils.SysConfig;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.utils.FileUtil;
import com.note.annex.module.filepreview.utils.ZipUtil;

/**
 * ��ѹ�ļ�����
 */
@Component
public class CompressedFileConventer {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private String uploadfileurl = SysConfig.getInstance().getConfig("file.uploadURL");//文件上传目录
	private String tempfileurl = SysConfig.getInstance().getConfig("file.tmpURL");//临时文件目录

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
				// 压缩文件解压到resource目录下
				String fileTree = "";
				try {
					// 解压文件并获取文件列表
					fileTree = ZipUtil.unCompress(filePath, resourceDirPath);
					fileTree = fileTree.replace(uploadfileurl, "");
				}catch (Exception e) {
					e.printStackTrace();
				}
				fileModel.setFileTree(fileTree);
				fileModel.setState(FileModel.STATE_YZH);
				int splitIndex = fileModel.getOriginalFile().lastIndexOf(".");
				fileModel.setConventedFileName(fileModel.getOriginalFile().substring(0, splitIndex));
			}
			// 创建meta文件，存放文件基本信息
			String metaPath = hashDirPath + File.separator + "meta";
			File metaFile = FileUtil.createFile(metaPath);
			FileUtil.writeContent(metaFile, fileModel, "GBK");
		}

	}
}

package com.note.annex.module.filepreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.note.annex.module.filepreview.dao.FileDao;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.service.DownloadNetFileService;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

@Component
public class DownloadNetFileServiceImpl implements DownloadNetFileService {

	@Autowired private FileDao fileDao;

	@Override
	public void download(FileModel fileModel) {
		if (fileModel.getState() != FileModel.STATE_WXZ) {
			throw new RuntimeException("the file state:" + fileModel.getState() + " is not 1");
		}
		try {
			URL url = new URL(fileModel.getOriginal());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			connection.setRequestMethod("GET");
			// 文件多媒体类型MIME
			fileModel.setOriginalMIMEType(connection.getContentType());
			// 源文件的路径
			String orignal = fileModel.getOriginal();
			// 第一个问
			int firstQM = orignal.indexOf("?");
			if (firstQM != -1) {
				orignal = orignal.substring(0, firstQM);
			}
			int lastSlash = orignal.lastIndexOf("/");
			// 源文件的名字
			fileModel.setOriginalFile(orignal.substring(lastSlash + 1));
			InputStream in = connection.getInputStream();
			try {
				// 文件存储到本地临时
				this.fileDao.saveFile(in, fileModel);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}

package com.note.annex.module.filepreview.service;

import com.note.annex.module.filepreview.model.FileModel;

public interface FileConventerService {
	
	/**
	 * 文件转换并存储
	 * @param fileModel
	 */
    void conventer(FileModel fileModel);

}

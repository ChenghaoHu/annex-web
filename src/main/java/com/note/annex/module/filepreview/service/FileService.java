package com.note.annex.module.filepreview.service;

import java.util.List;

import com.note.annex.module.filepreview.model.FileModel;

public interface FileService {

    /**
     * @param hashCode
     * @return FileModel
     */
    FileModel findFileModelByHashCode(String hashCode);

    List<String> findAllKeys();

    List<String> getImageFilesOfPPT(String pathId);
}

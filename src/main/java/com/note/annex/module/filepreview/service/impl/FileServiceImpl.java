package com.note.annex.module.filepreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.note.annex.module.filepreview.dao.FileDao;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.service.FileService;

import java.util.List;

@Component
public class FileServiceImpl implements FileService {


    @Autowired private FileDao fileDao;

    @Override
    public FileModel findFileModelByHashCode(String hashCode) {
        return fileDao.findByHashCode(hashCode);
    }

    @Override
    public List<String> findAllKeys() {
        return this.fileDao.findAllKeys();
    }

    @Override
    public List<String> getImageFilesOfPPT(String pathId) {
        return this.fileDao.getImageFilesOfPPT(pathId);
    }


}

package com.note.annex.module.filepreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.note.annex.common.utils.SysConfig;
import com.note.annex.module.filepreview.conventer.CompressedFileConventer;
import com.note.annex.module.filepreview.conventer.ImageFileConventer;
import com.note.annex.module.filepreview.conventer.OfficeFileConventer;
import com.note.annex.module.filepreview.conventer.PdfFileConventer;
import com.note.annex.module.filepreview.conventer.TextFileConventer;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.service.FileConventerService;
import com.note.annex.module.filepreview.utils.FileUtil;

/**
 * Created by chicheng on 2017/12/28.
 */
@Component
public class FileConventerServiceImpl implements FileConventerService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @SuppressWarnings("unused")
	private String uploadfileurl = SysConfig.getInstance().getConfig("file.uploadURL");//�ϴ�����Ŀ¼
    private String filetexttype = SysConfig.getInstance().getConfig("file.text.type");//�ı������ļ���׺
    private String fileimgtype = SysConfig.getInstance().getConfig("file.img.type");//ͼƬ�����ļ���׺
    private String fileofficetype = SysConfig.getInstance().getConfig("file.office.type");//Office�ļ������ļ���׺
    private String filecompresstype = SysConfig.getInstance().getConfig("file.compress.type");//ѹ���ļ������ļ���׺
    private String filepdftype = SysConfig.getInstance().getConfig("file.pdf.type");//pdf�ļ������ļ���׺

    @Autowired TextFileConventer textFileConventer;
    @Autowired PdfFileConventer pdfFileConventer;
    @Autowired ImageFileConventer imageFileConventer;
    @Autowired OfficeFileConventer officeFileConventer;
    @Autowired CompressedFileConventer compressedFileConventer;

    @Override
    public void conventer(FileModel fileModel) {
        if (fileModel.getState() != FileModel.STATE_YXZ) {
            throw new RuntimeException("the file state:" + fileModel.getState() + " is not 2.");
        }
        try {
            String subfix = FileUtil.getFileSufix(fileModel.getOriginalFile());
            if(this.filepdftype.contains(subfix.toLowerCase())) {
                this.pdfFileConventer.conventer(fileModel);
            }else if(this.filetexttype.contains(subfix.toLowerCase())) {
                this.textFileConventer.conventer(fileModel);
            }else if(this.fileimgtype.contains(subfix.toLowerCase())) {
                this.imageFileConventer.conventer(fileModel);
            }else if(this.filecompresstype.contains(subfix.toLowerCase())) {
                this.compressedFileConventer.conventer(fileModel);
            }else if(this.fileofficetype.contains(subfix.toLowerCase())) {
                if("xlsx".equals(subfix.toLowerCase()) || "xls".equals(subfix.toLowerCase())
                        || "pptx".equals(subfix.toLowerCase()) || "ppt".equals(subfix.toLowerCase())) {
                    this.officeFileConventer.conventerToHtml(fileModel);
                }else {
                    this.officeFileConventer.conventerToPdf(fileModel);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error("不支持该类型文件的转换");
            throw new RuntimeException(e);
        }
    }

}

package com.note.annex.module.filepreview.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.note.annex.common.utils.SysConfig;
import com.note.annex.module.filepreview.model.FileModel;
import com.note.annex.module.filepreview.service.DownloadNetFileService;
import com.note.annex.module.filepreview.service.FileConventerService;
import com.note.annex.module.filepreview.service.FileService;
import com.note.annex.module.filepreview.utils.FileUtil;

@Controller
public class FileConventerController {

	@Autowired FileService fileService;
	@Autowired FileConventerService fileConventerService;
	@Autowired DownloadNetFileService downloadNetFileService;

	@SuppressWarnings("unused")
	private String uploadfileurl = SysConfig.getInstance().getConfig("file.uploadURL");//文件上传目录
	private String tempfileurl = SysConfig.getInstance().getConfig("file.tmpURL");//临时文件目录
	private String filetexttype = SysConfig.getInstance().getConfig("file.text.type");//文本类型后缀
	private String fileimgtype = SysConfig.getInstance().getConfig("file.img.type");//图片类型后缀
	private String fileofficetype = SysConfig.getInstance().getConfig("file.office.type");//Office文本文件类型后缀
	private String filecompresstype = SysConfig.getInstance().getConfig("file.compress.type");//压缩文件类型后缀
	private String filepdftype = SysConfig.getInstance().getConfig("file.pdf.type");//pdf文件类型后缀

	@SuppressWarnings("unused")
	private Map<String, String> pptMap = new HashMap<>();

	@RequestMapping("/getfile")
	public void getfile(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String path = "D:/�����ĵ�.docx";
		File file = new File(path);
		String filename = file.getName();
		OutputStream out = response.getOutputStream();
		if(file.exists()) {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[1024];
			int len = 0;
			response.reset();
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
			while((len = bis.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			bis.close();
			out.close();
		}
	}

	/**
	 * 文件转换：1、从url地址下载文件 2、转换文件
	 * @param model
	 * @param filePath
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	@RequestMapping("/fileConventer")
	public String fileConventer(String filePath, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		// 先去查询,如果存在,不需要转化文档,为找到有效安全的url编码,所以这里使用循环来判断当前文件是否存在
		FileModel oldFileModel = null;
		List<String> keys = this.fileService.findAllKeys();
		System.out.println(keys.toString());
		for (String key : keys) {
			FileModel tmp = this.fileService.findFileModelByHashCode(key);
			if (tmp != null && tmp.getOriginal().equals(filePath)) {
				oldFileModel = tmp;
				break;
			}
		}
		System.out.println(" 2 --->");
		// 文件已下载，不需要转换
		if (oldFileModel != null) {
			System.out.println(" 3 preview --->");
			return previewUrl(oldFileModel, model, request);
		} else {
			System.out.println(" 3 --->");
			FileModel fileModel = new FileModel();
			// 文件来源url
						fileModel.setOriginal(filePath);
						// 创建时间,使用毫秒数
						fileModel.setCreateMs(System.currentTimeMillis());
						// 文件有效时间 10分钟
						fileModel.setLimitMs(10 * 60 * 1000);
						// 文件新建 未下载状态
						fileModel.setState(FileModel.STATE_WXZ);
						// 下载文件
						this.downloadNetFileService.download(fileModel);
						// 转换文件
						this.fileConventerService.conventer(fileModel);
						// 文件展现到前端
			if (fileModel.getState() != FileModel.STATE_YZH) {
				throw new RuntimeException("convert fail.");
			}
			return previewUrl(fileModel, model, request);
		}
	}

	/**
	 * 获取重定向路径
	 * @param fileModel
	 * @param model
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	private String previewUrl(FileModel fileModel, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		StringBuffer previewUrl = new StringBuffer();
		previewUrl.append("/viewer/document/");
		// pathId确定预览文件
		previewUrl.append(fileModel.getPathId());
		previewUrl.append(File.separator);

		// 判断转换后的文件是否存在,不存在则跳到error页面
		File file = new File(tempfileurl + File.separator + fileModel.getPathId()
		+ File.separator + "resource" + File.separator + fileModel.getConventedFileName());
		String subfix = FileUtil.getFileSufix(fileModel.getOriginalFile());
		model.addAttribute("pathId", fileModel.getPathId());
		model.addAttribute("fileType", subfix);
		if (file.exists()) {
			// 判断文件类型，不同的文件做不同的展示
			if (this.filepdftype.contains(subfix.toLowerCase())) {
				return "html";
			} else if (this.filetexttype.contains(subfix.toLowerCase())) {
				return "txt";
			} else if (this.fileimgtype.contains(subfix.toLowerCase())) {
				return "picture";
			} else if (this.filecompresstype.contains(subfix.toLowerCase())) {
				model.addAttribute("fileTree", fileModel.getFileTree());
				return "compress";
			} else if (this.fileofficetype.contains(subfix.toLowerCase())) {
				if ("pptx".equalsIgnoreCase(subfix.toLowerCase()) || "ppt".equalsIgnoreCase(subfix.toLowerCase())) {
					List<String> imgFiles = fileService.getImageFilesOfPPT(fileModel.getPathId());
					String imgPaths = "";
					for(String s : imgFiles) {
						imgPaths +=(fileModel.getPathId() + "/resource/" + s.substring(s.lastIndexOf("\\"), s.length()) + ",");
					}
					model.addAttribute("imgPaths", imgPaths);
					return "ppt";
				} else {
					return "office";
				}
			}
		} else {
			return "forward:/fileNotSupported";
		}
		return null;
	}

	/**
	 * 获取预览文件
	 * @param pathId
	 * @param response
	 * @param fileFullPath 此参数主要针对压缩文件,利用该参数获取解压后的文件
	 * @return
	 */
	@RequestMapping(value = "/viewer/document/{pathId}", method = RequestMethod.GET)
	@ResponseBody
	public void onlinePreview(@PathVariable String pathId, String fileFullPath, HttpServletResponse response) throws IOException {
		// 根据pathId获取fileModel
		FileModel fileModel = this.fileService.findFileModelByHashCode(pathId);
		if (fileModel == null) {
			throw new RuntimeException("fileModel 不能为空");
		}
		if (fileModel.getState() != FileModel.STATE_YZH) {
			throw new RuntimeException("convert fail.");
		}

		// 得到转换后的文件地址
		String fileUrl = "";

		if (fileFullPath != null) {
			fileUrl = tempfileurl + File.separator + fileFullPath;
		} else {
			fileUrl = tempfileurl + File.separator + fileModel.getPathId() + File.separator + "resource" + File.separator + fileModel.getConventedFileName();
		}
		File file = new File(fileUrl);

		// 设置内容长度
		response.setContentLength((int) file.length());

		// 内容配置中要转码,inline 浏览器支持的格式会在浏览器中打开,否则下载
		String fullFileName = new String(fileModel.getConventedFileName());
		response.setHeader("Content-Disposition", "inline;fileName=\"" + fullFileName + "\"");

		// 设置content-type
		response.setContentType(fileModel.getOriginalMIMEType());
		FileInputStream is = new FileInputStream(new File(fileUrl));
		OutputStream os = response.getOutputStream();
		printFile(is, os);
	}

	private void printFile(FileInputStream is, OutputStream os) throws IOException{
		try {
			byte[] bytes = new byte[1024 * 4];
			int tmp = 0;
			while ((tmp = is.read(bytes)) != -1) {
				os.write(bytes, 0, tmp);
				os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}

	@RequestMapping("aaa")
	public String test() {
		return "ppt";
	}

}

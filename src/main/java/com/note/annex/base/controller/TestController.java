package com.note.annex.base.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.note.annex.common.utils.SysConfig;

@Controller
public class TestController {

	@RequestMapping("/test")
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/ftl/test.ftl");
		return mv;
	}
	
	@RequestMapping("/tmpfile/list")
	public ModelAndView folderList(HttpServletRequest request) {
		String history_back = "";
		String tmpfilepath = request.getParameter("tmpfilepath");
		String nowFolder = SysConfig.getInstance().getConfig("file.uploadURL");
		if(tmpfilepath==null || "".equals(tmpfilepath)) {
			tmpfilepath = nowFolder;
		}else {
			history_back = tmpfilepath;
		}
		File file = new File(tmpfilepath);
		File[] fileList = file.listFiles();
		String[] strings = file.list();
		List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory() && !fileList[i].isHidden()) {
				Map<String, String> map = new HashMap<String, String>();
				for(String str : strings) {
					if(str!=null && fileList[i].getPath().indexOf(str)!=-1) {
						map.put("filename", str);
						break;
					}
				}
				if(map.get("filename")==null) {
					map.put("filename", "");
				}
				map.put("type", "folder");
				map.put("content", fileList[i].getPath());
				map.put("update", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(fileList[i].lastModified())));
				arrayList.add(map);
			}
		}
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isFile()) {
				Map<String, String> map = new HashMap<String, String>();
				for(String str : strings) {
					if(str!=null && fileList[i].getPath().indexOf(str)!=-1) {
						map.put("filename", str);
						break;
					}
				}
				if(map.get("filename")==null) {
					map.put("filename", "");
				}
				map.put("type", "file");
				map.put("content", String.valueOf(fileList[i].getPath()));
				map.put("update", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(fileList[i].lastModified())));
				arrayList.add(map);
			}
		}
		System.out.println(arrayList.toString());
		ModelAndView mv = new ModelAndView("/ftl/tmp_filelist.ftl");
		mv.addObject("nowFolder", nowFolder);
		mv.addObject("fileList", arrayList);
		mv.addObject("history_back", history_back);
		return mv;
	}
	
	@RequestMapping("/tmpfile/getfile")
	public void getfile(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String path = request.getParameter("tmpfilepath");
		if(path!=null && !"".equals(path)) {
			File file = new File(path);
			String filename = file.getName();
			OutputStream out = response.getOutputStream();
			if(file.exists()) {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				byte[] buf = new byte[1024 * 8];
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
	}
	
}

package com.note.annex.module.file.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.note.annex.common.utils.AnnexUtil;
import com.note.annex.common.utils.SysConfig;

@Controller
@RequestMapping("/file")
public class FileController {

	@RequestMapping("/getfilepage")
	public @ResponseBody void index(HttpServletRequest request, HttpServletResponse response, String filepath, String filetype) {
		System.out.println(filepath);
		File file = new File(filepath);
		if(file.exists()) {
			try {
				String filename = request.getParameter("filename");
				if(filename!=null && !"".equals(filename)) {
					if(filetype==null || "".equals(filetype)) {
						filetype = filename.substring(filename.lastIndexOf(".")+1);
					}
				}else {
					if(filetype==null || "".equals(filetype)) {
						filetype = filepath.substring(filepath.lastIndexOf(".")+1);
					}
				}
				System.out.println(filetype);
				String tempsavepath = SysConfig.getInstance().getConfig("file.tmpURL");
				InputStream fromFileInputStream = new FileInputStream(file);
				String htmFileName = AnnexUtil.file2Html(fromFileInputStream, filename.substring(0,filename.lastIndexOf(".")), tempsavepath, filetype);

				//String lookurl = tempsavepath + "/"+"tempfile001.html";
				String lookurl = tempsavepath + "/"+ htmFileName;
				File lookfile = new File(lookurl);
				BufferedReader reader = null;
				StringBuffer sbf = new StringBuffer();
				reader = new BufferedReader(new FileReader(lookfile));
				String tempStr;
				while ((tempStr = reader.readLine()) != null) {
					sbf.append(tempStr+"\n");
				}
				if (reader != null) {
					reader.close();
				}
				response.setHeader("Content-Type", "text/html;charset=UTF-8");
				response.getOutputStream().write(sbf.toString().getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("文件及目录不存在。 filepath : "+filepath);
		}
		//end
	}

}

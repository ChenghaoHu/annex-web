package com.note.annex.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

/**
 * 文件转换（主要将文件转化为pdf或html，支持doc|docx|ppt|pptx|xls|xlsx等文件的转换）
 * 
 * @author hch
 */
public class AnnexUtil {

	private static OfficeManager officeManager;
	
	/**
     * 转换文件为pdf
     *
     * @throws FileNotFoundException
     * */
    public static String openoffice4pdf(String filepath, String savepath, String type) throws Exception {
        File inputFile = new File(filepath);
        if (!inputFile.exists()) {
            throw new FileNotFoundException("要转换的文件不存在：" + filepath);
        }
        String filename = inputFile.getName().substring(0, inputFile.getName().lastIndexOf("."));
        String pdfFileName = filename + type!=null?type:DocUtil.pdfType;
        String docFileName = filename + filepath.substring(filepath.lastIndexOf("."));
        File toFileFolder = new File(savepath);
        if (!toFileFolder.exists()) {
            toFileFolder.mkdirs();//不存在时创建目录
        }
        File pdfOutputFile = new File(toFileFolder.toString() + File.separatorChar + pdfFileName);
        File docInputFile = new File(toFileFolder.toString() + File.separatorChar + docFileName);
        //由fromFileInputStream开始构建输入文件
        InputStream fromFileInputStream = new FileInputStream(inputFile);
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fromFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String officehome = SysConfig.getInstance().getConfig("openoffice.installURL");
		String openofficeport = SysConfig.getInstance().getConfig("openoffice.port");
		String executiontimeout = SysConfig.getInstance().getConfig("openoffice.executiontimeout");
		String queuetimeout = SysConfig.getInstance().getConfig("openoffice.queuetimeout");
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		configuration.setOfficeHome(officehome);//设置OpenOffice.org安装目录
		configuration.setPortNumbers((openofficeport!=null)?Integer.parseInt(openofficeport):8101); //设置转换端口，默认为8100
		configuration.setTaskExecutionTimeout((executiontimeout!=null)?Long.valueOf(executiontimeout):(long)(1000*60*5));// 设置任务执行超时为5分钟
		configuration.setTaskQueueTimeout((queuetimeout!=null)?Long.valueOf(queuetimeout):(long)1000*60*60*24);// 设置任务队列超时为24小时
		officeManager = configuration.buildOfficeManager();
		officeManager.start(); //启动openoffice服务
		//convert
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		converter.convert(docInputFile, pdfOutputFile);
		if (pdfOutputFile.exists()){
			System.out.println(" ** "+docInputFile.getName()+"文件转换成功");
		}else {
			System.out.println(" ** "+docInputFile.getName()+"文件转换失败");
		}
        //转换完之后删除word文件
        docInputFile.delete();
        //tip：目前这个方法中xlsx文件无法转成pdf。
        return pdfFileName;
    }
    
    /**
     * 转换文件成pdf
     * 
     * @param fromFileInputStream:
     * @throws IOException 
     */
    public static String file2pdf(InputStream fromFileInputStream, String filename, String toFilePath,String type) throws Exception{
        String timesuffix = filename;//UUID.randomUUID().toString().replaceAll("-", "");
        String docFileName = null;
        String htmFileName = null;
        if("doc".equals(type)){
            docFileName = timesuffix.concat(".doc");
            htmFileName = timesuffix.concat(".pdf");
        }else if("docx".equals(type)){
            docFileName = timesuffix.concat(".docx");
            htmFileName = timesuffix.concat(".pdf");
        }else if("xls".equals(type)){
            docFileName = timesuffix.concat(".xls");
            htmFileName = timesuffix.concat(".pdf");
        }else if("xlsx".equals(type)){
            docFileName = timesuffix.concat(".xlsx");
            htmFileName = timesuffix.concat(".pdf");
        }else if("ppt".equals(type)){
            docFileName = timesuffix.concat(".ppt");
            htmFileName = timesuffix.concat(".pdf");
        }else if("pptx".equals(type)){
            docFileName = timesuffix.concat(".pptx");
            htmFileName = timesuffix.concat(".pdf");
        }else if("txt".equals(type)){
            docFileName = timesuffix.concat(".txt");
            htmFileName = timesuffix.concat(".pdf");
        }else{
        	return null;
        }
        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists()){
        	htmlOutputFile.delete();
        }
		htmlOutputFile.createNewFile();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
    	OutputStream os = new FileOutputStream(docInputFile);
		while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
		    os.write(buffer, 0, bytesRead);
		}
		os.close();
        fromFileInputStream.close();
        //String openofficepost = SysConfig.getInstance().getConfig("openoffice.host");
		//String openofficeport = SysConfig.getInstance().getConfig("openoffice.port");
        //OpenOfficeConnection connection = new SocketOpenOfficeConnection(openofficepost,(openofficeport!=null)?Integer.valueOf(openofficeport):8100);
		//connection.connect();
        //convert
        //DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        //connection.disconnect();
        String officehome = SysConfig.getInstance().getConfig("openoffice.installURL");
		String openofficeport = SysConfig.getInstance().getConfig("openoffice.port");
		String executiontimeout = SysConfig.getInstance().getConfig("openoffice.executiontimeout");
		String queuetimeout = SysConfig.getInstance().getConfig("openoffice.queuetimeout");
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		configuration.setOfficeHome(officehome);//设置OpenOffice.org安装目录
		configuration.setPortNumbers((openofficeport!=null)?Integer.parseInt(openofficeport):8101); //设置转换端口，默认为8100
		configuration.setTaskExecutionTimeout((executiontimeout!=null)?Long.valueOf(executiontimeout):(long)(1000*60*5));// 设置任务执行超时为5分钟
		configuration.setTaskQueueTimeout((queuetimeout!=null)?Long.valueOf(queuetimeout):(long)1000*60*60*24);// 设置任务队列超时为24小时
		officeManager = configuration.buildOfficeManager();
		officeManager.start(); //启动openoffice服务
		// convert
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(docInputFile, htmlOutputFile);
        // 转换完之后删除word文件
        docInputFile.delete();
        return htmFileName;
    }
    
    /**
     * 文件转换成Html
     * @param fromFileInputStream
     * @param toFilePath
     * @param type
     * @return
     * @throws Exception
     */
    public static String file2Html (InputStream fromFileInputStream, String filename, String toFilePath, String type) throws Exception{
		String timesuffix = filename;//UUID.randomUUID().toString().replaceAll("-", "");
        String docFileName = null;
        String htmFileName = null;
        if("doc".equals(type)){
            docFileName = timesuffix.concat(".doc");
            htmFileName = timesuffix.concat(".html");
        }else if("docx".equals(type)){
            docFileName = timesuffix.concat(".docx");
            htmFileName = timesuffix.concat(".html");
        }else if("xls".equals(type)){
            docFileName = timesuffix.concat(".xls");
            htmFileName = timesuffix.concat(".html");
        }else if("xlsx".equals(type)){
            docFileName = timesuffix.concat(".xlsx");
            htmFileName = timesuffix.concat(".html");
        }else if("ppt".equals(type)){
            docFileName = timesuffix.concat(".ppt");
            htmFileName = timesuffix.concat(".html");
        }else if("pptx".equals(type)){
            docFileName = timesuffix.concat(".pptx");
            htmFileName = timesuffix.concat(".html");
        }else if("txt".equals(type)){
            docFileName = timesuffix.concat(".txt");
            htmFileName = timesuffix.concat(".html");
        }else if("pdf".equals(type)){
        	docFileName = timesuffix.concat(".pdf");
			htmFileName = timesuffix.concat(".html");
        }else{
        	return null;
        }
		File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists()){
        	htmlOutputFile.delete();
        }
		htmlOutputFile.createNewFile();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        int bytesRead = 0;
        byte[] buffer = new byte[1024 * 8];
    	OutputStream os = new FileOutputStream(docInputFile);
		while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
		    os.write(buffer, 0, bytesRead);
		}
		os.close();
        fromFileInputStream.close();
        //String FS_FILE_CONVERT_HOST = SysConfig.getInstance().getConfig("openoffice.host");
		//String openofficeport = SysConfig.getInstance().getConfig("openoffice.port");
        //OpenOfficeConnection connection = new SocketOpenOfficeConnection(FS_FILE_CONVERT_HOST,(openofficeport!=null)?Integer.valueOf(openofficeport):8100);
		//connection.connect();
        // convert
        //DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        //connection.disconnect();
        String officehome = SysConfig.getInstance().getConfig("openoffice.installURL");
		String openofficeport = SysConfig.getInstance().getConfig("openoffice.port");
		String executiontimeout = SysConfig.getInstance().getConfig("openoffice.executiontimeout");
		String queuetimeout = SysConfig.getInstance().getConfig("openoffice.queuetimeout");
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		configuration.setOfficeHome(officehome);//设置OpenOffice.org安装目录
		configuration.setPortNumbers((openofficeport!=null)?Integer.parseInt(openofficeport):8101); //设置转换端口，默认为8100
		configuration.setTaskExecutionTimeout((executiontimeout!=null)?Long.valueOf(executiontimeout):(long)(1000*60*5));// 设置任务执行超时为5分钟
		configuration.setTaskQueueTimeout((queuetimeout!=null)?Long.valueOf(queuetimeout):(long)1000*60*60*24);// 设置任务队列超时为24小时
		officeManager = configuration.buildOfficeManager();
		officeManager.start(); //启动openoffice服务
		// convert
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        converter.convert(docInputFile, htmlOutputFile);
        // 转换完之后删除word文件
        docInputFile.delete();
        return htmFileName;
	}	

}

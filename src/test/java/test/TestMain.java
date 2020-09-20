package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.note.annex.common.utils.AnnexUtil;

public class TestMain {

	public static void main(String[] args) throws Exception {
		InputStream fromFileInputStream = new FileInputStream(new File("d:\\20200817中债维护相关问题统计.xlsx"));
    	try {
			AnnexUtil.file2Html(fromFileInputStream, "123123", "D:\\temp_file", "xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	/*File file = new File(SysConfig.getInstance().getConfig("file.uploadURL"));//File类型可以是文件也可以是文件夹
		File[] fileList = file.listFiles();//将该目录下的所有文件放置在一个File类型的数组中
		for (int i = 0; i < fileList.length; i++) {
			System.out.println(fileList[i]);
		}*/
	}
}

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.note.annex.common.utils.AnnexUtil;
import com.note.annex.common.utils.SysConfig;

public class TestMain {

	public static void main(String[] args) throws Exception {
		InputStream fromFileInputStream = new FileInputStream(new File("d:\\20200817��ծά���������ͳ��.xlsx"));
    	try {
			AnnexUtil.file2Html(fromFileInputStream, "123123", "D:\\temp_file", "xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	/*File file = new File(SysConfig.getInstance().getConfig("file.uploadURL"));//File���Ϳ������ļ�Ҳ�������ļ���
		File[] fileList = file.listFiles();//����Ŀ¼�µ������ļ�������һ��File���͵�������
		for (int i = 0; i < fileList.length; i++) {
			System.out.println(fileList[i]);
		}*/
	}
}

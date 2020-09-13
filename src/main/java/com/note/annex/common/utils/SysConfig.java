package com.note.annex.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取configs.properties配置文件相关内容
 * 
 * @author hch
 */
@SuppressWarnings("serial")
public final class SysConfig extends Properties {
	
	public static SysConfig instance;
	
	public static synchronized SysConfig getInstance() {
		if (instance == null) {
			instance = new SysConfig();
		}
		return instance;
	}
	
	private SysConfig() {
		InputStream inputStream = this.getClass().getResourceAsStream("/conf/configs.properties");
		try {
			this.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getConfig(String key) {
		return this.getProperty(key);
	}
	
}

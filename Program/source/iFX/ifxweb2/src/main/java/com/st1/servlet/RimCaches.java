package com.st1.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

public class RimCaches {

	private static boolean enabled = false;
	private static HashMap<String, HashMap<String, CachedRom>> caches = new HashMap<String, HashMap<String, CachedRom>>();
	private static Properties cacheWantedProps;
	private static String rimCacheFile = "rim.txt";
	static final Logger logger = LoggerFactory.getLogger(RimCaches.class);

	public static void init(boolean cacheWanted) {
		FileInputStream fis = null;
		enabled = cacheWanted;
		if (enabled) {
			logger.info("rim cached enabled");
			String filePath = GlobalValues.getEnvFilePath(rimCacheFile);
			cacheWantedProps = new Properties();
			try {
				fis = new FileInputStream(FilterUtils.filter(filePath));
				cacheWantedProps.load(fis);
				cacheWantedProps.list(System.out);
			} catch (IOException e) {
				logger.error("failed to load rim propertites file");
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			} finally {
				SafeClose.close(fis);
			}
		} else {
			logger.info("rim cached disabled");
		}
	}

	public static boolean isCacheEnabled(String rimId) {
		return enabled && cacheWantedProps.getProperty(rimId) != null;
	}

	public static String get(String rimId, String rimText) {
		if (!isCacheEnabled(rimId)) {
			logger.info("rim cache no enabled");
			return null;
		}

		HashMap<String, CachedRom> map = caches.get(rimId);
		if (map != null) {
			CachedRom cachedRom = map.get(rimText);
			if (cachedRom != null)
				return cachedRom.getRom();
		}
		return null;
	}

	public static void put(String rimId, String rimText, String rom) {
		if (!isCacheEnabled(rimId)) {
			logger.info("rim cache no enabled");
			return;
		}
		HashMap<String, CachedRom> map = caches.get(rimId);
		if (map == null) {
			map = new HashMap<String, CachedRom>();
			caches.put(rimId, map);
		}
		map.put(rimText, new CachedRom(rom));
	}

	public static void main(String... aArgs) throws Exception {
		String rimId = "CR103";
		String rimText1 = "asd123232322sdsdsdsds232323232323232323232";
		String rimText2 = "asd123232322sdsdsdsds232323232323232323232asd";
		String rom1 = "this is a booooooooooooooooooook";
		String rom2 = "this is a cat";

		GlobalValues.ifxFolder = "D:/myProject2/var3/java/ifxfolder";
		RimCaches.init(true);

		String rom;
		if (RimCaches.isCacheEnabled(rimId)) {
			rom = RimCaches.get(rimId, rimText1);
			if (rom == null)
				RimCaches.put(rimId, rimText1, rom1);
			rom = RimCaches.get(rimId, rimText1);
			if (rom == null) {
				logger.info("error not cached");
			} else {
				logger.info("cached:" + rom);
			}
		} else {
			logger.info("cache is not enabled for " + rimId);
		}

	}
}

class CachedRom {
	String rom;
	Date time;

	public CachedRom(String rom) {
		this.rom = rom;
		touch();
		System.out.println("put cached " + time);
	}

	public String getRom() {
		// touch();
		System.out.println("get cached " + time);
		return rom;
	}

	public void touch() {
		time = new Date();

	}

	public void setRom(String rom) {
		this.rom = rom;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
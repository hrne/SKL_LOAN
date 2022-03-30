package com.st1.ifx.repos;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyFileUtl {
	static final Logger logger = LoggerFactory.getLogger(MyFileUtl.class);

	public static String removeExtension(File file) {
		return removeExtension(file.getName());
	}

	public static String removeExtension(String name) {
		return FilenameUtils.removeExtension(name);
	}

	public static String toDateString(File file) {
		return toDateString(new Date(file.lastModified()));
	}

	public static String toDateString(long t) {
		return toDateString(new Date(t));
	}

	public static String toDateString(Date dt) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return format.format(dt);
	}

	public static String toSizeString(File file) {
		return toSizeString(file.length());
	}

	public static String toSizeString(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * 讀取檔案內容.
	 * 
	 * @param file    檔案
	 * @param getutf8 是否讀取UFT-8編碼
	 * @return List<String>
	 */
	@SuppressWarnings("deprecation")
	public static List<String> readAll(File file, boolean getutf8) {
		try {
			if (getutf8) {
				return FileUtils.readLines(file, "UTF-8");
			}
			return FileUtils.readLines(file);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return null;
		}
	}
}

package com.st1.ifx.hcomm.fmt;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;

public class Env {
	static final Logger logger = LoggerFactory.getLogger(Env.class);
	public static String fmtFolder = "D:\\ifxfolder\\runtime\\fmt2";// "D:\\ifxfolder\\fmt";
	private static String logFolder = "D:\\ifxfolder\\runtime\\log";
	public static String SessionFolder = "sessions";
	public static String docTemplatesFolder = "templates";

	public static void setFmtFolder(String folder) {
		logger.info(FilterUtils.escape("fmt folder:" + FilterUtils.escape(folder)));
		logger.info("encoding:" + FilterUtils.escape(System.getProperty("file.encoding")));
		fmtFolder = folder;
	}

	public static String getFmtFolder() {
		return fmtFolder;
	}

	public static void setLogFolder(String folder) {
		logFolder = folder;
	}

	public static String getLogFolder() {
		return logFolder;
	}

	public static String getFullPath(String fileName) {
		return fmtFolder + File.separatorChar + fileName;
	}

	public static String getDocTemplatePath(String fileName) {
		return fmtFolder + File.separatorChar + docTemplatesFolder + File.separatorChar + fileName;
	}

	public static String getSessionPath(String fileName) {
		return fmtFolder + File.separatorChar + SessionFolder + File.separatorChar + fileName;
	}

	static public void main(String[] args) {
		setFmtFolder("a");
	}
}

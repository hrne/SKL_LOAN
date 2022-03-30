package com.st1.ifx.swiftauto.test;

import java.io.File;

import com.st1.servlet.GlobalValues;

public class TestMonitorSwift {
	static org.springframework.context.support.GenericXmlApplicationContext ctx;

	// 啟動swift-auto監控轉檔流程
	public static void main(String[] args) {
		setupGlobalValues();

		String[] xmls = { "file:d:/ifxfolder/webServerEnv/ifx-env.xml", "classpath:app-context-one.xml" };
		ctx = new org.springframework.context.support.GenericXmlApplicationContext();
		ctx.load(xmls);
		ctx.refresh();
	}

	private static void setupGlobalValues() {
		GlobalValues.swiftFolder = "D:\\ifxfolder\\runtime\\swift\\mt\\";
		GlobalValues.REPOSITORY_ROOT = "D:\\ifxwriter\\repos\\report";
		GlobalValues.runtimeFolder = "D:\\ifxfolder\\runtime";
		String fmtFolder = GlobalValues.runtimeFolder + File.separator + "fmt" + "2" + File.separator;
		com.st1.ifx.hcomm.fmt.Env.setFmtFolder(fmtFolder);

		String versionpath = GlobalValues.ifxFolder + File.separator + "webServerEnv" + File.separator;
		GlobalValues.jsVersionPath = versionpath + "js-version.txt";
		GlobalValues.helpjsVersionPath = versionpath + "helpjs-version.txt";

	}

}

package com.st1.help.excel;

import java.io.File;
import java.util.Date;

import com.st1.util.PoorManFile;

public class HelpFileGen2 {

	public synchronized static void generate(String excelFile, String jsFile, String enc, boolean forceNew) throws Exception {

		if (isJSOlder(excelFile, jsFile)) {
			System.out.println("excel file has been modified, produce new javascript file");

			ExcelConverter2 converter = new ExcelConverter2(excelFile);
			String json = converter.perform();
			PoorManFile poor = new PoorManFile(jsFile);
			poor.write(json);
			System.out.println(jsFile + " generated (with new generator)");
		} else {
			System.out.println("excel file is older than js file, no rebuild");
		}

	}

	public static boolean isJSOlder(String excelFile, String jsFile) {
		File f1 = new File(excelFile);
		File f2 = new File(jsFile);

		if (!f2.exists())
			return true;
		Date d1 = new Date(f1.lastModified());
		Date d2 = new Date(f2.lastModified());
		if (d1.compareTo(d2) < 0)
			return false;
		else
			return true;
	}

	public static void main(String[] args) throws Exception {

		String helpJS = "d:/ifxfolder/runtime/props/HELP.js";
		String helpXls = "d:/ifxfolder/runtime/props/HELP.xls";
		System.out.println("excel:" + helpXls);
		System.out.println("js:" + helpJS);
		HelpFileGen2.generate(helpXls, helpJS, "BIG5", false);

	}

}

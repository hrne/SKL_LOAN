package com.st1.ifx.etc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileLog {
	public static String getNowwithFormat(String fmt) {

		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Calendar c1 = Calendar.getInstance(); // today
		return sdf.format(c1.getTime());
	}

	public static void write(String filename, String s) {

		String t = getNowwithFormat("yyyy/MM/dd HH:mm:ss.SSS") + "  " + s;

		try {
			File log = new File(filename);
			if (!log.exists()) {
				log.createNewFile();
			}

			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			pr.print(t);
			pr.println();
			pr.close();

		} catch (IOException e) {
		}
	}
}

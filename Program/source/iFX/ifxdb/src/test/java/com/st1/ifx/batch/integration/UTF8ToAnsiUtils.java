package com.st1.ifx.batch.integration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

public class UTF8ToAnsiUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(UTF8ToAnsiUtils.class);
	// FEFF because this is the Unicode char represented by the UTF-8 byte order
	// mark (EF BB BF).
	public static final String UTF8_BOM = "\uFEFF";

	public static void main(String args[]) {
		logger.info("in UTF8ToAnsiUtils!");
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader r = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		Writer w = null;
		try {
			if (args.length != 2) {
				logger.info("Usage : java UTF8ToAnsiUtils utf8file ansifile");
				System.exit(1);
			}

			boolean firstLine = true;
			 fis = new FileInputStream(FilterUtils.filter(args[0]));
			 isr = new InputStreamReader(fis,"UTF8");
			 r = new BufferedReader(isr);
			 fos = new FileOutputStream(FilterUtils.filter(args[1]));
			 osw = new OutputStreamWriter(fos, "Cp1252");
			 w = new BufferedWriter(osw);			
			for (String s = ""; (s = r.readLine()) != null;) {
				if (firstLine) {
					s = UTF8ToAnsiUtils.removeUTF8BOM(s);
					firstLine = false;
				}
				w.write(s + System.getProperty("line.separator"));
				w.flush();
			}		
			
			System.exit(0);
		}
		catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
			System.exit(1);
		}
		finally{
			SafeClose.close(w);
			SafeClose.close(osw);
			SafeClose.close(fos);
			SafeClose.close(r);
			SafeClose.close(isr);
			SafeClose.close(fis);			
		}
	}

	private static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}
}
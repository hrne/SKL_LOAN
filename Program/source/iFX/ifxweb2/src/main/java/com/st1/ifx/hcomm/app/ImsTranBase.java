package com.st1.ifx.hcomm.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.fmt.Env;

public abstract class ImsTranBase {
	public abstract boolean perform();

	// @Autowired
	// @Qualifier("queueSend")
	// protected IComm sender;

	// @Value("${queue_rcv_timeout:60}")
	// protected Integer receiveTimeout;

	protected String getToday() {
		String DATE_FORMAT = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		return sdf.format(c1.getTime());
	}

	protected String getLogFileName(String user) {
		String today = getToday();
		return Env.getLogFolder() + File.separator + today + "_" + user + ".log";
	}

	protected void writeLog(String filename, String txcode, String key, String tmpstr, String tag) {
		String t = "[" + tag + "] TXCD:" + txcode + ",key:" + key;
		try {
			File log = new File(FilterUtils.filter(filename));
			if (!log.exists()) {
				log.createNewFile();
			}
			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(FilterUtils.filter(filename), true)));
			pr.println(t);
			pr.println(tmpstr);
			pr.println();
			pr.close();

		} catch (IOException e) {
		}
	}
}

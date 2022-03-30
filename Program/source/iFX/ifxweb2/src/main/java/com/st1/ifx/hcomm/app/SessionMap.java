package com.st1.ifx.hcomm.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;
import com.st1.ifx.hcomm.fmt.Env;
import com.st1.ifx.hcomm.fmt.FmtLoader;

public class SessionMap extends HashMap<String, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4683678331666599257L;
	static final Logger logger = LoggerFactory.getLogger(SessionMap.class);
	static String fileName = "sessionvar.ttt";

	// static SessionMap origMap;

	private SessionMap() {
	}

	public static SessionMap getInstance(String user) {
		try {
			SessionMap aMap = loadSession(user);
			if (aMap != null)
				return aMap;

			// clone an empty one
			return newSessionMap();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return null;
	}

	public static SessionMap newSessionMap() {
		FmtLoader loader = new FmtLoader();
		try {
			// clone an empty one
			List<String> lines = loader.load(fileName);
			SessionMap origMap = makeMap(lines);
			return copyOne(origMap);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return null;
	}

	// SessionFolder
	private static String getTodayFile(String user) {
		String DATE_FORMAT = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		String today = sdf.format(c1.getTime());
		String todayFile = today + '_' + user + ".txt";
		String fullPath = Env.getFmtFolder() + File.separatorChar + Env.SessionFolder + File.separatorChar + todayFile;
		logger.info(FilterUtils.escape("session var:" + fullPath));
		return fullPath;
	}

	private static SessionMap loadSession(String user) {
		String fullPath = getTodayFile(user);
		boolean exists = (new File(FilterUtils.filter(fullPath))).exists();
		if (!exists)
			return null;
		try {
			SessionMap map = makeMap(loadFile(fullPath));
			return map;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return null;
		}
	}

	public void storeSession(String user) throws IOException {
		FileWriter fw = null;
		Writer output = null;

		String fullPath = getTodayFile(user);
		try {
			File file = new File(FilterUtils.filter(fullPath));
			fw = new FileWriter(file);
			output = new BufferedWriter(fw);
			Iterator iter = this.keySet().iterator();
			while (iter.hasNext()) {
				String k = (String) iter.next();
				String v = this.get(k);
				String line = k + "=" + v + "\n";

				output.write(line);

			}
		} finally {
			SafeClose.close(output);
			SafeClose.close(fw);
		}
	}

	private static List<String> loadFile(String fullPath) throws Exception {
		List<String> records = new ArrayList<String>();
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = new FileInputStream(FilterUtils.filter(fullPath));
			reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				records.add(line);
			}
		} finally {
			SafeClose.close(reader);
			SafeClose.close(is);
		}
		return records;
	}

	private static SessionMap copyOne(SessionMap origMap) {
		SessionMap map = new SessionMap();
		Iterator iter = origMap.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			String v = origMap.get(k);
			map.put(k, v);
		}
		return map;
	}

	public void dump() {
		Iterator iter = this.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			String v = this.get(k);
			logger.info(k + "=" + v);
		}
	}

	private static SessionMap makeMap(List<String> lines) {
		SessionMap map = new SessionMap();
		Iterator<String> iter = lines.iterator();
		while (iter.hasNext()) {
			String s = iter.next();
			s = s.trim();
			if (s.length() == 0 || s.startsWith("#"))
				continue;

			String[] ss = s.split("=");
			String k = ss[0];
			String v = "";
			if (ss.length > 1)
				v = ss[1];
			map.put(k, v);
		}
		return map;
	}

	public void put(String k, Object o) {
		super.put(k, (String) o);
	}

	public void touch() {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSS");
		String s = formatter.format(new Date());
		s = s.substring(0, 8);
		this.put("TIME", s);
	}

}

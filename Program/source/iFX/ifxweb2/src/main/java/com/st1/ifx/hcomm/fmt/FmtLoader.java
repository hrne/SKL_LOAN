package com.st1.ifx.hcomm.fmt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

public class FmtLoader {
	static final Logger logger = LoggerFactory.getLogger(FmtLoader.class);

	public List<String> load(String filename) throws Exception {
		List<String> records = new ArrayList<String>();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;

		// String fullResName = "folder/" + filename;
		// InputStream is = getClass().getResourceAsStream(fullResName);
		String fullPath = Env.getFullPath(filename);
		logger.info("Env load:" + FilterUtils.escape(fullPath));
		try {
			is = new FileInputStream(FilterUtils.filter(fullPath));
			isr = new InputStreamReader(is);
			reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				if (!isCommentOrEmpty(line))
					records.add(line);
			}
		} finally {
			SafeClose.close(reader);
			SafeClose.close(isr);
			SafeClose.close(is);
		}
		return records;
	}

	public List<HostField> loadAndParse(String filename) {
		try {
			List<String> lines = load(filename);
			return parse(lines);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return null;
		}
	}

	public List<HostField> parse(List<String> rr) {
		List<HostField> fields = new ArrayList<HostField>();
		Iterator<String> iter = rr.iterator();
		while (iter.hasNext()) {
			fields.add(HostField.from(iter.next()));
		}
		return fields;
	}

	private boolean isCommentOrEmpty(String s) {
		if (s.trim().length() == 0 || s.startsWith("#"))
			return true;
		return false;
	}

	public void dump(List<String> records) {
		Iterator<String> iter = records.iterator();
		while (iter.hasNext()) {
			logger.info(iter.next());
		}
	}
}

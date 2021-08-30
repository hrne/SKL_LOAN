package com.st1.itx.util.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.util.filter.FilterUtils;
import com.st1.itx.util.filter.SafeClose;
import com.st1.itx.util.log.SysLogger;

/**
 * FmtLoader
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
@Component
@Scope("prototype")
public class FmtLoader extends SysLogger {
	static final Logger logger = LoggerFactory.getLogger(FmtLoader.class);

	@Value("${iTXTomFolder}")
	public String urlTom;

	@Value("${iTXTimFolder}")
	public String urlTim;

	public List<String> load(String fileName) throws IOException {
		List<String> records = new ArrayList<String>();
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;

		String fullPath = null;
		if (fileName.endsWith("Label.tim"))
			fullPath = urlTim + fileName;
		else if (fileName.endsWith("Label.tom") || fileName.equals("ERROR.tom")|| fileName.equals("NOTICE.tom"))
			fullPath = urlTom + fileName;
		else if (fileName.endsWith(".tim"))
			fullPath = urlTim + fileName.substring(0, 2) + File.separator + fileName;
		else if (fileName.endsWith(".tom"))
			fullPath = urlTom + fileName.substring(0, 2) + File.separator + fileName;

		this.info("Env load:" + FilterUtils.escape(fullPath));
		try {
			is = new FileInputStream(FilterUtils.filter(fullPath));
			isr = new InputStreamReader(is, "UTF-8");
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

	public List<HostField> loadAndParse(String filename) throws IOException {
		List<String> lines = load(filename);
		return parse(lines);
	}

	public List<HostField> parse(List<String> rr) {
		List<HostField> fields = new ArrayList<HostField>();
		Iterator<String> iter = rr.iterator();
		while (iter.hasNext()) {
			String v = iter.next();
			fields.add(HostField.from(v));
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
			this.info(iter.next());
		}
	}
}

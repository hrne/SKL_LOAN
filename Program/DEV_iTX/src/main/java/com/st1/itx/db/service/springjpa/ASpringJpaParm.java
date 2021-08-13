package com.st1.itx.db.service.springjpa;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.st1.itx.util.log.SysLogger;

public class ASpringJpaParm extends SysLogger {

	public List<Map<String, String>> convertToMap(List<Object> list) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		try {
			for (Iterator<Object> iter = list.iterator(); iter.hasNext();) {
				Object[] values = (Object[]) iter.next();
				Map<String, String> m = new LinkedHashMap<String, String>();
				for (int i = 0; i < values.length; i++)
					m.put("F" + Integer.toString(i), values[i] == null ? "" : values[i].toString());
				result.add(m);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		this.info("result:" + result.size());
		return result;
	}
}

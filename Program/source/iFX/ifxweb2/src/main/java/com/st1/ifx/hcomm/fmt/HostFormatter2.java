package com.st1.ifx.hcomm.fmt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostFormatter2 {
	// List<HostField> hostFields;
	static final Logger logger = LoggerFactory.getLogger(HostFormatter2.class);

	public void format(String fmtName, ByteArrayOutputStream arrayStream, HashMap<String, String> map) {

		List<HostField> hostFields = loadFmt(fmtName);
		HashMap<String, String> values = converMapKeysToUppercase(map);

		Iterator<HostField> iter = hostFields.iterator();
		while (iter.hasNext()) {
			HostField fld = iter.next();
			String value = values.get(fld.name.toUpperCase());
			String hostText = fld.toHost(value);
			logger.info("to host, name:" + fld.name + ",[" + hostText + "]");
			try {
				arrayStream.write(toBytes(hostText));
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
	}

	public static HashMap<String, String> splitParts(String text) {

		int offset = 0;
		String h = text.substring(offset, 150);
		offset += 150;
		String b = text.substring(offset, offset + 40);
		offset += 40;
		String t = text.substring(offset);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("h", h);
		map.put("b", b);
		map.put("t", t);
		return map;

	}

	public HashMap<String, String> parse(List<HostField> hostFields, String text, int startOffset) {

		byte[] bb = null;
		try {
			bb = text.getBytes("Big5_HKSCS"); // 潘 20180305
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		int offset = startOffset;
		HashMap<String, String> map = new HashMap<String, String>();
		Iterator<HostField> iter = hostFields.iterator();
		while (iter.hasNext()) {
			HostField fld = iter.next();
			logger.info("total len:" + text.length() + ", offset:" + offset);
			fld.dump();
			int lengthToGet = fld.getTotalLen();
			String v = getString(bb, offset, lengthToGet); // text.substring(offset,
															// offset+lengthToGet);
			offset += lengthToGet;

			if (fld.type.equalsIgnoreCase("M")) {
				v = fld.makeMoney(v);
			}

			map.put(fld.name, v);

			logger.info("====>" + map.get(fld.name));

		}
		return map;
	}

	public static String getString(byte[] bb, int offset, int length) {
		byte[] a2 = new byte[length];
		System.arraycopy(bb, offset, a2, 0, length);
		try {
			return new String(a2, "Big5_HKSCS");// 潘 20180305
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return "";
		}
	}

	public static byte[] toBytes(String s) {
		return s.getBytes();
	}

	public static String toText(byte[] bb) {
		return new String(bb);
	}

	public static HashMap<String, String> converMapKeysToUppercase(HashMap<String, String> map) {
		HashMap<String, String> map2 = new HashMap<String, String>();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			String v = map.get(k);
			map2.put(k.toUpperCase(), v);
		}

		return map2;
	}

	public static HashMap<String, String> converMapKeysToLowercase(HashMap<String, String> map) {
		HashMap<String, String> map2 = new HashMap<String, String>();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			String v = map.get(k);
			map2.put(k.toLowerCase(), v);
		}

		return map2;
	}

	public List<HostField> loadFmt(String fmtName) {
		FmtLoader loader = new FmtLoader();
		return loader.loadAndParse(fmtName);
	}

}

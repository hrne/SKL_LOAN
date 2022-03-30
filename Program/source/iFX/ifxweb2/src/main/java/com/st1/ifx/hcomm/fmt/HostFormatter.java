package com.st1.ifx.hcomm.fmt;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.ifx.twnb.AStarLoadUtils;
import com.st1.servlet.GlobalValues;

public class HostFormatter {
	static final Logger logger = LoggerFactory.getLogger(HostFormatter.class);
	List<HostField> hostFields;
	String fmtName;

	public HostFormatter(String fmtName) {
		this.fmtName = fmtName;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, String> toUpperCase(Map<String, String> map) {
		Map map2 = new LinkedHashMap<String, String>();
		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			Object v = map.get(k);
			map2.put(k.toUpperCase(), v);
		}
		return map2;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String format(boolean includeHeader, Map map) throws JsonProcessingException {
		map = toUpperCase(map);
		PoorMap values = new PoorMap();
		values.putAll(map);
		loadFmts(includeHeader);
		StringBuffer sb = new StringBuffer();
		Iterator<HostField> iter = this.hostFields.iterator();
		logger.info("===  begin of format() ===");
		while (iter.hasNext()) {
			HostField fld = iter.next();
			String value = values.getValueAt(fld.name);

			if (value == null && fld.defaultValue != null) {
				value = fld.defaultValue;
				logger.info(fld.name + " use default value:[" + fld.defaultValue + "]");
			}

			String hostText = fld.toHost(value);
			sb.append(hostText);
			map.put(fld.name, hostText);
		}
		logger.info("=== end of format() ===");
		// return sb.toString();
		return new ObjectMapper().writeValueAsString(map);
	}

	private void loadFmts(boolean includeHeader) {
		String[] files = getFmtFiles(includeHeader);
		this.hostFields = new ArrayList<HostField>();

		FmtLoader loader = new FmtLoader();
		for (int i = 0; i < files.length; i++) {
			List<HostField> fields = loader.loadAndParse(files[i]);
			addPrefixByFmt(fields, files[i]);
			this.hostFields.addAll(fields);
		}
	}

	private void addPrefixByFmt(List<HostField> fields, String fmtName) {
		// 目前沒有欄位名稱重複狀況
		// if (fmtName.toUpperCase().startsWith("IMSHEADER.")) {
		// addPrefix(fields, "IMS.");
		// } else if (fmtName.toUpperCase().startsWith("TITALABEL.")) {
		// addPrefix(fields, "L.");
		// }
	}

	private void addPrefix(List<HostField> fields, String prefix) {
		List<HostField> prefixFields = new ArrayList<HostField>();
		Iterator<HostField> iter = fields.iterator();
		while (iter.hasNext()) {
			HostField fld = iter.next();
			fld.name = prefix + fld.name;
		}
	}

	private String[] getFmtFiles(boolean includeHeader) {
		String[] files;
		if (includeHeader) {
			if (this.fmtName.endsWith(".tim") || this.fmtName.endsWith(".rim")) {
				files = new String[] { "titaLabel.tim", this.fmtName };
			} else {
				// tota
				files = new String[] { "totaLabel.tom", this.fmtName };
			}
		} else {
			files = new String[] { this.fmtName };
		}
		return files;
	}

	private String getString(byte[] bb, int offset, int length) {
		byte[] a2 = new byte[length];

		// added 2015/07/28 for swift-auto
		if (offset + length > bb.length) {
			length = bb.length - offset;
		}

		System.arraycopy(bb, offset, a2, 0, length);
		try {
			return new String(a2, "UTF-8");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return "";
		}

	}

	public Map<String, String> parse(boolean includeHeader, String text) {
		loadFmts(includeHeader);
		Map<String, String> map = new HashMap<String, String>();
		Iterator<HostField> iter = hostFields.iterator();
		int offset = 0;
		String t = "", label = "";
		byte[] bb = null;
		try {
			if (GlobalValues.bR6) {
				bb = AStarLoadUtils.convertUnicodeStrToIBM937Bytes(text);
			} else {
				// bb = text.getBytes("big5");
				bb = text.getBytes("UTF-8");
			}
			logger.info("text:" + text + ",bb:" + bb);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		if (bb != null)
			logger.info("bb.length:" + bb.length);
		if (bb != null && bb.length >= GlobalValues.minTotaLen) {
			label = getString(bb, offset, GlobalValues.minTotaLen);
			offset += GlobalValues.minTotaLen;
			t = getString(bb, offset, bb.length - offset);
		} else {
			logger.info("tota <" + GlobalValues.minTotaLen + ",system error. ");
			if (bb != null)
				t = getString(bb, 0, bb.length);
			map.put("TXRSUT", "E");
			map.put("MSGID", "ERROR"); // 連線錯誤-> ERROR
			map.put("_T", t);
			logger.info("error:" + t);
			return map;
		}
		map.put("_L", label);
		map.put("_T", t);
		logger.info("label:" + label);
		logger.info("text:" + t);

		logger.info("===  begin of parse()  ===");
		offset = 0;
		try {
			while (iter.hasNext()) {
				HostField fld = iter.next();
				logger.info("total len:" + text.length() + ", offset:" + offset);
				fld.dump();
				int lengthToGet = fld.getTotalLen();
				String v = getString(bb, offset, lengthToGet); // text.substring(offset,
				// offset+lengthToGet);
				offset += lengthToGet;

				if (fld.type.equals("9")) {
					try {
						v = fld.makeMoney(v);
					} catch (NumberFormatException ex) {
						logger.info(fld.name + ":tota is not numeric:" + v + " keep going.");
						v = "0";
					} catch (Exception ex) {
						// 理論上不會到這了
						logger.error(fld.name + ":tota is not numeric:" + v + " ex:" + ex.getMessage());
						v = "0";

					}
				}

				map.put(fld.name, v);
				// 有些開很長的欄位,會導致LOG檔案變很大.. 例: SWIFT-AUTO.tom 的TEXT
				// logger.info(fld.name + ":[" + v + "], " + v.length() + "\n");
			}
		} catch (NullPointerException ex1) {
			logger.info("tota fld null:" + ex1.getMessage() + " keep going.");
		} catch (Exception ex1) {
			logger.error("tota error:" + ex1.getMessage());
		}
		logger.info("===  end of parse()  ===");
		return map;
	}
}

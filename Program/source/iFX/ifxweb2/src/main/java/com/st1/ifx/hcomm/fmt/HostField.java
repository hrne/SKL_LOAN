package com.st1.ifx.hcomm.fmt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostField {
	static final Logger logger = LoggerFactory.getLogger(HostField.class);
	static String allowTypes = "X,9,M,C";
	String type = "X";
	String name;
	int length = 1;
	int afterDecimalPoint = 0;
	boolean arrayField = false;
	boolean occursField = false;
	String value;
	String pattern;
	String defaultValue = null;

	public static HostField from(String line) {
		line = line.trim().toUpperCase();
		String[] ll = line.split("=");

		HostField fld = new HostField();

		if (ll.length > 2) {
			fld.defaultValue = ll[2];
		}
		fld.name = ll[0].trim();
		// logger.info("fmt field name:"+fld.name);
		if (fld.name.startsWith("*")) {
			fld.occursField = true;
			fld.name = fld.name.substring(1);
		}
		String s = ll[1];
		fld.pattern = ll[1];
		String[] ss = s.split(",");

		fld.type = ss[0].trim();
		if (allowTypes.indexOf(fld.type) < 0)
			return null;

		if (fld.type.equalsIgnoreCase("X")) {
			fld.length = Integer.parseInt(ss[1]);
		} else if (fld.type.equalsIgnoreCase("9") || fld.type.equalsIgnoreCase("M")) {
			String[] tt = ss[1].split("\\.");
			if (tt.length == 1) {
				fld.length = Integer.parseInt(ss[1]);
				fld.afterDecimalPoint = 0;
			} else {
				fld.length = Integer.parseInt(tt[0]);
				fld.afterDecimalPoint = Integer.parseInt(tt[1]);
			}
		} else {
			fld.length = Integer.parseInt(ss[1]);
		}
		return fld;
	}

	public void dump() {
		String occ = occursField ? "  occurs " : "";
		logger.info(occ + "name:" + name + ", type:" + type + ",length:" + length + ", " + afterDecimalPoint + ",value:" + value);
	}

	public String toHost(String v) {
		if (type.equals("X")) {
			if (v == null)
				v = "";
			return FormatUtil.padX(v, this.length);
		} else if (type.equals("9")) {
			if (v == null)
				v = "0";
			return FormatUtil.pad9(v, this.length, afterDecimalPoint);
		} else if (type.equalsIgnoreCase("M")) {
			if (v == null)
				v = "0";
			return FormatUtil.pad9(v, this.length, afterDecimalPoint);
		} else {
			if (v == null)
				v = "";
			return FormatUtil.padX(v, this.length);
		}
	}

	public int getTotalLen() {
		return this.length + this.afterDecimalPoint;
	}

	public String makeMoney(String v) {
		logger.info("makeMoney:" + v);
		if (this.afterDecimalPoint == 0) {
			int i = Integer.parseInt(v);
			return Integer.toString(i);
		}
		// int width = this.length - this.afterDecimalPoint;
		int i = Integer.parseInt(v.substring(0, this.length));
		String de = v.substring(this.length);
		return Integer.toString(i) + "." + de;
		// return FormatUtil.left(v, width) + "." + FormatUtil.right(v,
		// this.afterDecimalPoint);
	}

	public boolean isOccursField() {
		return occursField;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}

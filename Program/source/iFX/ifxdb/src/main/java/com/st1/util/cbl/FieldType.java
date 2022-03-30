package com.st1.util.cbl;

public class FieldType {
	public static final char X = 'x';
	public static final char N = '9';
	public static final char D = 'D';
	public static final char T = 'T';

	String tagName;
	char type = X;
	int len = 1;
	int fractionLen = 0;

	private FieldType() {

	}

	public static FieldType build(String tag, String type, String pic) {
		FieldType f = new FieldType();
		f.tagName = tag;
		f.type = type.toLowerCase().charAt(0);
		if (pic == null)
			pic = "1";

		if (f.type == N) {
			String[] ss = pic.split("[.]");
			f.len = Integer.parseInt(ss[0]);
			if (ss.length > 1)
				f.fractionLen = Integer.parseInt(ss[1]);
		} else {

			f.len = Integer.parseInt(pic);
		}

		return f;
	}

	public String put(String value) {
		return "" + this.type + ":" + value + "|";
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getFractionLen() {
		return fractionLen;
	}

	public void setFractionLen(int fractionLen) {
		this.fractionLen = fractionLen;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String toString() {
		return "FieldType [tagName=" + tagName + ", type=" + type + ", len=" + len + ", fractionLen=" + fractionLen
				+ "]";
	}

}

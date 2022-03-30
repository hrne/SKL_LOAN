package com.st1.ifx.file.excel.item.help;

import java.util.ArrayList;
import java.util.List;

public class HelpDef {
	List<String> colNames = null;// = new ArrayList<String>();
	List<List<String>> values = null;// = new ArrayList<HashMap>();

	String sheetName = null;
	String segmentName = null;

	public static HelpDef from(String sheetName, String segmentName, List<String> colNames) {
		HelpDef def = new HelpDef();
		def.sheetName = sheetName;
		def.segmentName = segmentName;
		def.colNames = colNames;
		def.values = new ArrayList<List<String>>();
		return def;
	}

	public void addValues(List<String> rowValues) {
		this.values.add(rowValues);

	}

	public List<String> getColNames() {
		return colNames;
	}

	public void setColNames(List<String> colNames) {
		this.colNames = colNames;
	}

	public List<List<String>> getValues() {
		return values;
	}

	public void setValues(List<List<String>> values) {
		this.values = values;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

}

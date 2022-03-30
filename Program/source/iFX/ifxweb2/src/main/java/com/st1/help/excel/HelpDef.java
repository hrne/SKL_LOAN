package com.st1.help.excel;

import java.util.ArrayList;
import java.util.List;

public class HelpDef {
	List<String> colNames = null;// = new ArrayList<String>();
	List<List<String>> values = null;// = new ArrayList<HashMap>();

	String sheetName = null;
	String segmentName = null;

	static HelpDef from(String sheetName, String segmentName, List<String> colNames) throws Exception {
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
}

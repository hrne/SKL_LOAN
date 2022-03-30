package com.st1.help.excel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class HelpDefToOldStyleJsonProcessor implements HelpDefProcessor {

	private Map<String, Object> allMap = new LinkedHashMap<String, Object>();

	@Override
	public void process(HelpDef def) throws Exception {
		// System.out.println("process " + def.sheetName + "." +
		// def.segmentName);
		// System.out.println("bye!");

		// List<Map<String, String>> results = new ArrayList<Map<String,
		// String>>();
		Map<String, List<String>> results = new LinkedHashMap<String, List<String>>();
		results.put("FILD", def.colNames);
		int col = 0;
		for (String name : def.colNames) {
			List<String> r = new ArrayList<String>();
			for (List<String> list : def.values) {
				r.add(list.get(col));
			}
			results.put(name, r);
			col++;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> old = (Map<String, Object>) allMap.get(def.sheetName);
		if (old == null) {
			old = new LinkedHashMap<String, Object>();
			allMap.put(def.sheetName, old);
		}

		old.put(def.segmentName, results);

	}

	@Override
	public String output() throws Exception {

		ObjectMapper jsonMapper = new ObjectMapper();
		ObjectWriter jsonWriter = jsonMapper.writer();
		// jsonWriter = jsonWriter.withDefaultPrettyPrinter();

		String s = "var Helpfile=\n" + jsonWriter.writeValueAsString(allMap) + ";";
		return s;

	}

}

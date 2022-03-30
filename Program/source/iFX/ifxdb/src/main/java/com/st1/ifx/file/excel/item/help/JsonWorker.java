package com.st1.ifx.file.excel.item.help;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonWorker extends UntypedWorker {
	static final Logger logger = LoggerFactory.getLogger(JsonWorker.class);

	LinkedBlockingDeque<Object> master;

	public JsonWorker(MasterSystem system, String name) {
		super(system.pool, system.itemQueue, name);
		this.master = system.master;
		startListen();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof HelpDef) {
			process((HelpDef) message);
		} else {
			unhandled(message);
		}

	}

	private void process(HelpDef def) {
		String s = null;
		try {
			System.out.println("process " + def.sheetName + "." + def.segmentName);
			List<Map<String, String>> results = new ArrayList<Map<String, String>>();
			String name, value;
			for (List<String> list : def.values) {
				Map<String, String> m = new LinkedHashMap<String, String>();
				for (int i = 0; i < list.size(); i++) {
					name = def.colNames.get(i);
					value = list.get(i);
					m.put(name, value);
				}
				results.add(m);
			}

			ObjectMapper jsonMapper = new ObjectMapper();
			ObjectWriter jsonWriter = jsonMapper.writer();
			// jsonWriter = jsonWriter.withDefaultPrettyPrinter();
			s = jsonWriter.writeValueAsString(results);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		}
		master.add(new StoredMsg(def, s));

	}

}

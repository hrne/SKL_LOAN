package com.st1.ifx.file.item.general;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class GeneralLineProcessor implements ItemProcessor<GeneralLine, GeneralLine> {
	private static final Logger logger = LoggerFactory.getLogger(GeneralLineProcessor.class);
	private StepExecution stepExecution;
	private ExecutionContext executionContext;

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
		this.executionContext = stepExecution.getExecutionContext();
	}

	String keyNames = "names";
	String delim = ",";
	String keyDisplayOrder = "order";
	String help;

	// @Scope("step")
	@Value("#{jobParameters['help']}")
	public void setHelp(final String help) {
		this.help = help;
	}

	@Override
	public GeneralLine process(GeneralLine g) throws Exception {
		String[] names;
		g.setHelp(help);
		if (g.isCommandLine()) {
			logger.info("this is command line:" + g.toString());
			return g;
		} else if (g.isHead()) {
			logger.info("this is HEAD line:" + g.toString());

			logger.info("column name change to:" + g.content);
			// names = g.content.split(delim);
			// this.executionContext.put(keyNames, names);
			this.executionContext.putInt(keyDisplayOrder, 0);
			g.displayOrder = 0;
			return g;
		} else {
			// if (!executionContext.containsKey(keyNames)) {
			// // TODO: throw execption? or log?
			// System.err.println("missing column names defined");
			// return null;
			// }
			int order = executionContext.getInt(keyDisplayOrder);
			order++;
			executionContext.putInt(keyDisplayOrder, order);
			// names = (String[]) executionContext.get(keyNames);
			// String jsonContent = zip(g.key, names, g.content.split(delim));
			// if (jsonContent == null) {
			// // TODO: log error and skip?
			// System.out.println("failed to convert to JSON from:"
			// + g.content);
			// return null;
			// }
			// g.setContent(jsonContent);
			g.setDisplayOrder(order);
			return g;
		}

	}

	String zip(String keyValue, String[] kk, String[] vv) {
		if (kk.length != vv.length + 1) {// vv
			logger.info("kk length != vv.length");
			return null;
		}
		HashMap<String, String> m = new HashMap<String, String>();
		m.put(kk[0], keyValue); // 捕到第一個
		// kk從1開始,
		for (int i = 1; i < kk.length; i++) {
			m.put(kk[i], vv[i - 1]);
		}
		m.put("$", keyValue); // add key
		return toJSON(m);
	}

	String toJSON(HashMap<String, String> m) {
		ObjectMapper jsonMapper = new ObjectMapper();
		ObjectWriter jsonWriter = jsonMapper.writer();
		// jsonWriter = jsonWriter.withDefaultPrettyPrinter();
		try {
			return jsonWriter.writeValueAsString(m);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return null;
		}
	}

	int printCounter() {
		String kee = "counts";
		int counts = 0;
		if (this.executionContext.containsKey(kee)) {
			counts = this.executionContext.getInt(kee);
		}
		logger.info("##===>" + counts);
		counts++;
		this.executionContext.putInt(kee, counts);
		return counts;
	}

}

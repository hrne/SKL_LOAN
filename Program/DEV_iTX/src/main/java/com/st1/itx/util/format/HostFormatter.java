package com.st1.itx.util.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.util.log.SysLogger;

/**
 * HostFormatter
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
@Component
@Scope("prototype")
public class HostFormatter extends SysLogger {
	static final Logger logger = LoggerFactory.getLogger(HostFormatter.class);

	@Autowired
	FmtLoader fmtLoader;

	List<HostField> hostFields;
	String fmtName;

	public void init(String fmtName) {
		this.fmtName = fmtName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String format(boolean includeHeader, LinkedHashMap map) throws LogicException, IOException {
//		map = toUpperCase(map);
		boolean isEc = map.get("ISEC") != null;
		PoorMap values = new PoorMap();
		values.putAll(map);
		loadFmts(includeHeader);
		StringBuffer sb = new StringBuffer();
		Iterator<HostField> iter = this.hostFields.iterator();
		this.info("===  begin of format() ===");
		this.info(values.toString());
		while (iter.hasNext()) {
			HostField fld = iter.next();

			this.info("Fld Name : " + fld.name);
			String value = values.getValueAt(fld.name);
			this.info("value    : " + value);

			if (value == null && fld.defaultValue != null) {
				value = fld.defaultValue;
				this.info(fld.name + " use default value:[" + fld.defaultValue + "]");
			}

			if (value == null)
				if (isEc)
					value = "";
				else
					throw new LogicException("CE000", "Tota Field Not Found : " + fld.name);

			String hostText = fld.toHost(value);
			this.info("value    : " + hostText);
			sb.append(hostText);
		}
		this.info("=== end of format() ===");
		return sb.toString();
	}

	private void loadFmts(boolean includeHeader) throws IOException {
		String[] files = getFmtFiles(includeHeader);
		this.hostFields = new ArrayList<HostField>();

		for (int i = 0; i < files.length; i++) {
			List<HostField> fields = fmtLoader.loadAndParse(files[i]);
			this.hostFields.addAll(fields);
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
}

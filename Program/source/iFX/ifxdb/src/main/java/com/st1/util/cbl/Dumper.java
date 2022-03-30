package com.st1.util.cbl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Dumper {
	private static final Logger logger = LoggerFactory.getLogger(Dumper.class);
	private static boolean useJson = false;

	public static String dump(String name, Object o, Class<?> class1) {
		if (name == null || name.trim().length() == 0)
			name = "ObjetDump";
		XStream xStream;
		if (useJson) {
			xStream = new XStream(new JsonHierarchicalStreamDriver());
		} else {
			xStream = new XStream(new DomDriver());
			xStream.alias(name, class1);
		}
		return (xStream.toXML(o));

	}

	public static void dump(String root, Object o) {
		logger.info(dump(root, o, o.getClass()));
	}
}
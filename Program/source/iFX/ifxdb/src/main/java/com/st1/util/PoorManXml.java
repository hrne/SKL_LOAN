package com.st1.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class PoorManXml {

	public Document loadXml(String filePath) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(filePath);
		return builder.parse(is);

	}

	// public NodeList getNodes(Document doc, String tagName) {
	// try {
	//
	// return XPathAPI.selectNodeList(doc, tagName);
	// } catch (Exception e) {
	// return null;
	// }
	// }

}

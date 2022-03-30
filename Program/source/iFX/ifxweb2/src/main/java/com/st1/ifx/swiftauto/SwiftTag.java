package com.st1.ifx.swiftauto;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

enum TagType {
	SIMPLE, LOOP, OPTION
}

public class SwiftTag {
	static final Logger logger = LoggerFactory.getLogger(SwiftTag.class);

	String name;
	String tag;
	TagType tagType;
	String options = "";
	boolean complexTag = false;
	List<SwiftTag> tags;
	String formatFrom;

	private SwiftTag(String name, String tag, TagType tagType) {
		this.name = name;
		this.tag = tag;
		this.tagType = tagType;
	}

	// bForceComplex: true (option tag之complex位於上一層 所以在parseOption時傳入
	// 一般tag在$層
	public static SwiftTag parseTag(JsonNode tagNode, boolean bForceComplex) {
		TagType tagType = TagType.SIMPLE;
		JsonNode $node = tagNode.get("$");
		if ($node.has("type")) {
			if ($node.path("type").textValue().equalsIgnoreCase("loop"))
				tagType = TagType.LOOP;
		} else if ($node.has("options"))
			tagType = TagType.OPTION;
		String name = $node.path("name").textValue();
		String tag = $node.path("tag").textValue();

		SwiftTag swiftTag = new SwiftTag(name, tag, tagType);
		swiftTag.formatFrom = $node.path("format-from").textValue();
		if (bForceComplex)
			swiftTag.complexTag = bForceComplex;
		else {
			swiftTag.complexTag = isComplexTag($node);
		}
		swiftTag.parseSub(tagNode);
		return swiftTag;
	}

	private void parseSub(JsonNode tagNode) {
		// System.out.println("parseSub:" + this.tag);
		if (this.tagType == TagType.LOOP) {
			parseLoop(tagNode);
		} else if (tagType == TagType.OPTION) {
			parseOption(tagNode);
		} else {
			if (this.complexTag)
				parseComplexTags(tagNode);
		}
	}

	private void parseComplexTags(JsonNode tagNode) {
		this.tags = new ArrayList<SwiftTag>();
		for (JsonNode subTag : tagNode.get("tags")) {
			this.tags.add(SwiftTag.parseTag(subTag, false));
		}

	}

	private static boolean isComplexTag(JsonNode node) {
		JsonNode n = node.path("$").path("complex");
		if (!n.isMissingNode())
			return n.textValue().equalsIgnoreCase("true");
		return false;
	}

	private void parseLoop(JsonNode tagNode) {
		this.tags = new ArrayList<SwiftTag>();
		for (JsonNode subTag : tagNode.get("tags")) {
			tags.add(SwiftTag.parseTag(subTag, false));
		}
	}

	private void parseOption(JsonNode tagNode) {
		this.tags = new ArrayList<SwiftTag>();
		this.options = tagNode.path("$").path("options").textValue();
		this.tag = this.tag.substring(0, this.tag.length() - 1);
		JsonNode optNodes = tagNode.path("opt");
		for (String s : this.options.split(",")) {
			String optName = this.tag + s.trim().toUpperCase();
			JsonNode optNode = optNodes.path(optName);
			logger.info("option:" + optNode.toString());
			if (!optNode.isMissingNode()) {
				SwiftTag swiftTag = SwiftTag.parseTag(optNode, isComplexTag(optNode));
				swiftTag.tag = optName;
				// swiftTag.name = optNode.path("$").path("name").getTextValue();
				logger.info("optNode.name:" + optNode.path("$").path("name").textValue());
				// if(swiftTag.name==null) swiftTag.name = this.name;
				swiftTag.name = this.name;
				logger.info("optNode.name change:" + swiftTag.name);

				this.tags.add(swiftTag);
			}

		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String s = "SwiftTag [tag=" + tag + ", name=" + name + ", tagType=" + tagType + ", formatFrom=" + formatFrom + "]";
		sb.append(s).append("\n");
		if (this.tagType == TagType.SIMPLE) {
			if (this.complexTag) {
				sb.append("* * * complex tag:\n");
				for (SwiftTag t : this.tags) {
					sb.append("\t\t" + t.toString());
				}
			}
		} else {
			for (SwiftTag t : this.tags) {
				sb.append("\t" + t.toString()).append("\n");
			}
		}

		return sb.toString();
	}

	public SwiftTag find(String tagToFind) {
		if (this.tagType == TagType.SIMPLE) {
			if (this.tag.equalsIgnoreCase(tagToFind))
				return this;
		} else if (this.tagType == TagType.OPTION) {
			for (SwiftTag t : this.tags) {
				if (t.tag.equalsIgnoreCase(tagToFind))
					return t;
			}
		} else { // loop
			for (SwiftTag t : this.tags) {
				SwiftTag t2 = t.find(tagToFind);
				if (t2 != null)
					return t2;
			}
		}
		return null;
	}

	public String getFormatFrom() {
		return formatFrom;
	}

}

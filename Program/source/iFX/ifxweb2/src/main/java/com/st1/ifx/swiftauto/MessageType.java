package com.st1.ifx.swiftauto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.ifx.filter.SafeClose;

public class MessageType {
	static final Logger logger = LoggerFactory.getLogger(MessageType.class);

	String type;
	String name;
	List<SwiftTag> tags = new ArrayList<SwiftTag>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTag(SwiftTag tag) {
		this.tags.add(tag);
	}

	public List<SwiftTag> getTags() {
		return tags;
	}

	public static MessageType fromFile(String mtFileName) {
		MessageType mt = null;

		ObjectMapper mapper = new ObjectMapper();
		// 柯 改寫法,不清楚為何websphere與單元測試會不同 valueToTree nomethod找不到
		JsonNode root = null;
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(mtFileName));
			root = mapper.readTree(fileReader);
		} catch (JsonProcessingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(fileReader);
		}
		// websphere Method 找不到valueToTree
		// JsonNode root = mapper.valueToTree(new java.io.File(mtFileName));//new
		// java.io.File(mtFileName)
		JsonNode head = root.get("$");
		mt = new MessageType();
		if (head.has("type")) {
			mt.type = head.get("type").textValue();
		}
		if (head.has("name")) {
			mt.name = head.get("name").textValue();
		}
		if (root.has("tags")) {
			for (JsonNode tagNode : root.get("tags")) {
				mt.addTag(SwiftTag.parseTag(tagNode, false));
			}
		}
		// System.out.println(mt.toString());
		// System.out.println(mt.dump());
		// System.out.println(tags.size());

		return mt;
	}

	@Override
	public String toString() {
		return "MessageType [type=" + type + ", name=" + name + ", tags=" + tags.size() + "]";
	}

	public String dump() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.toString()).append("\n");
		for (SwiftTag tag : this.tags) {
			sb.append(tag.toString());
			sb.append("----------------------\n");
		}
		return sb.toString();
	}

	int currTag = 0; // or -1

	public void rewind() {
		currTag = 0;
	}

	public SwiftTag findByTagName(String tagToFind) {
		// String s = "";
		// System.out.println("size:"+this.tags.size());

		int lastCurrTag = currTag;
		while (currTag < this.tags.size()) {
			// System.out.println(currTag);
			SwiftTag tag = this.tags.get(currTag);
			SwiftTag foundTag = tag.find(tagToFind);
			if (foundTag != null) {
				// s = foundTag.name;
				return foundTag;
			}
			currTag++;
		}
		System.err.println("can't find tag:" + tagToFind + " in mt:" + type);
		currTag = lastCurrTag; // 找不到tag定義, 保留在原先位置
		return null;
	}

}

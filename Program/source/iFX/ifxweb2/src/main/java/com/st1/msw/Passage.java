package com.st1.msw;

import java.util.Date;

public class Passage {
	String brno;
	String from;
	String to;
	String action;
	String type;
	String content;
	Date time;
	String ackSessionId;

	String callback;

	public Passage() {

	}

	public Passage(String brno, String from, String action, String type, String content) {
		this.time = new Date();
		this.brno = brno;
		this.from = from;
		this.action = action;
		this.type = type;
		this.content = content;

	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getAckSessionId() {
		return ackSessionId;
	}

	public void setAckSessionId(String ackSessionId) {
		this.ackSessionId = ackSessionId;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	@Override
	public String toString() {
		return "Passage [brno=" + brno + ", from=" + from + ", to=" + to + ", action=" + action + ", type=" + type + ", content=" + content + ", time=" + time + ", ackSessionId=" + ackSessionId
				+ ", callback=" + callback + "]";
	}

}

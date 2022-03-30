package com.st1.bean;

import java.util.Date;

import com.st1.dao.JustDateUtil;
import com.st1.util.PoorManJson;

public class ReplyBase {
	public String requestTime;
	public String responseTime;
	public boolean success = true;
	public String errmsg;
	public String form;

	public ReplyBase() {
		requestTime = JustDateUtil.formatDate(new Date(), null);
	}

	public String toJson() {
		return PoorManJson.toJson(this);
	}

	public void makeSuccess(String form) {
		success = true;
		this.form = form;
		responseTime = JustDateUtil.formatDate(new Date(), null);
	}

	public void makeError(String msg) {
		responseTime = JustDateUtil.formatDate(new Date(), null);
		success = false;
		errmsg = msg;
	}
}

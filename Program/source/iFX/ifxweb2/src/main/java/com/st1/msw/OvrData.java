package com.st1.msw;

import java.io.Serializable;

public class OvrData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5261532954079989747L;
	String userId;
	String name;
	String token;
	String txcd;
	String screenFile;
	String reasons;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTxcd() {
		return txcd;
	}

	public void setTxcd(String txcd) {
		this.txcd = txcd;
	}

	public String getScreenFile() {
		return screenFile;
	}

	public void setScreenFile(String screenFile) {
		this.screenFile = screenFile;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	@Override
	public String toString() {
		return "OvrData [userId=" + userId + ", name=" + name + ", token=" + token + ", txcd=" + txcd + ", screenFile=" + screenFile + ", reasons=" + reasons + "]";
	}

}

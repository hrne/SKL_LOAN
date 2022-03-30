package com.st1.ifx.web.authentication;

import java.io.Serializable;

// from attach, c12
public class IfxUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 160309736958090539L;
	/**
	 * 
	 */
	protected String userId;
	// protected String password;
	protected String userName;
	protected String level;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	// public String getPassword() {
	// return password;
	// }
	// public void setPassword(String password) {
	// this.password = password;
	// }

	public String getLevel() {
		return level;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "IfxUser [userId=" + userId + ", userName=" + userName + ", level=" + level + "]";
	}

}

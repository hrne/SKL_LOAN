package com.st1.ifx.web.authentication;

public interface UserContext {
	IfxUser getCurrentUser();

	void setCurrentUser(IfxUser user);
}

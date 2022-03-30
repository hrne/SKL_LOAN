package com.st1.ifx.web.management;

import java.util.List;

public interface AppStatistics {

	public int getLoggedInUserCount();

	public List<Object> getLoggedInUsers();
}

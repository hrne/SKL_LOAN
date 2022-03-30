package com.st1.ifx.web.management;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;

public class AppStatisticsImpl implements AppStatistics {
	private static final Logger logger = LoggerFactory.getLogger(AppStatisticsImpl.class);
	@Autowired
	private SessionRegistry sessionRegistry;

	public int getLoggedInUserCount() {
		return sessionRegistry.getAllPrincipals().size();
	}

	public List<Object> getLoggedInUsers() {

		List<Object> objs = sessionRegistry.getAllPrincipals();
		// for (Object o : objs) {
		// IfxUserDetails u = (IfxUserDetails)o;
		// logger.info("userDetauls:"+u);
		// }
		return objs;
	}
}

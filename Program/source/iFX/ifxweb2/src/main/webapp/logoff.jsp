<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.st1.msw.UserPub"%>
<%@page import="com.st1.msw.UserInfo"%>
<%@page import="com.st1.util.MySpring"%>
<%@page import="com.st1.ifx.hcomm.app.SimpleJournal"%>
<%@page import="com.st1.ifx.hcomm.app.SessionMap"%>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta HTTP-EQUIV="REFRESH" content="1; url=index.jsp">
<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
<title>Out!!</title>
<%
	final Logger logger = LoggerFactory.getLogger("IFX-logoff");
	//SimpleJournal simple = MySpring.getSimpleJournal();
	//SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);

	//String tlrno = (String) sessionMap.get("TLRNO");
	//String brn = (String) sessionMap.get("BRN");
	//String entDay = (String) sessionMap.get("ENTDY");
	//simple.setBrn(brn);
	//simple.setTlrno(tlrno);
	//simple.setEntDay(entDay);
	//simple.writeJournal("XX004");
	try {
		String wait = StringEscapeUtils.escapeXml10(request.getParameter("w"));
		String currSession = null; //(String) session.getAttribute("currentSession");
		Object oldSessionobj = null;
		String oldSession = null;

		if (session != null) {
			currSession = session.getId();
			oldSessionobj = session.getAttribute("oldSession");
			if (oldSessionobj != null) {
				oldSession = oldSessionobj.toString();
			}
		}

		logger.info("currSession:" + currSession);
		logger.info(FilterUtils.escape("oldSession:" + oldSession));
		if (currSession != null) {
			logger.info("before UserPub.invalidateSession(currSession):" + currSession);
			UserPub.invalidateSession(currSession);
			logger.info("after UserPub.invalidateSession(currSession):" + currSession);
		}

		if (oldSession != null && oldSession != "" && !oldSession.equals(currSession)) {
			logger.info(FilterUtils.escape("before UserPub.invalidateSession(oldSession):" + oldSession));
			UserPub.invalidateSession(oldSession);
			logger.info(FilterUtils.escape("after UserPub.invalidateSession(oldSession):" + oldSession));
		}

		/* for weblogic start */
		String sessionId = session.getId();
		UserPub pub = UserPub.getInstance();
		UserInfo user = pub.getBySessionId(sessionId);
		logger.info("SessionListener removeUser-user!");
		if (user != null) {
			boolean removed = pub.removeUser(user);
			logger.info(FilterUtils.escape("user " + user.getId() + "'s session:" + sessionId + ",  removed:" + removed));
		} else {
			logger.info("no user for session:" + sessionId);
		}
		pub.dump();
		/* for weblogic end */

	} catch (Exception e) {
		logger.info("IFX-logoff warn:" + e.getMessage());
	}
%>

</head>
<body>
	<h2>系統已登出</h2>
	<p>一秒後將自動跳轉...</p>
</body>
</html>
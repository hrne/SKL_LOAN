<%@page import="com.st1.msw.UserPub"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta HTTP-EQUIV="REFRESH" content="2; url=index.jsp">
<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
<title>強制登出</title>
</head>
<%
//UserPub.invalidateSession(session.getId());
//final Logger logger = LoggerFactory.getLogger("IFX-Systeam");

String error = (String) session.getAttribute("error1");
String currSession =  session.getId(); //(String) session.getAttribute("currentSession");
String oldSession = (String) session.getAttribute("oldSession");
String user = (String) session.getAttribute("user");


if(currSession != null) {
	//logger.info("kill curr session:"+ currSession);
	UserPub.invalidateSession(currSession);
	//session.invalidate();
}
if(oldSession != null && !oldSession.equals(currSession)) {
	//logger.info("kill old session:"+ oldSession);
	UserPub.invalidateSession(oldSession);
}
if(user != null){
 UserPub.removeUserbyKey(user);
}
%>
<body>
2秒後導入登入畫面如沒跳轉請點擊以下連結
<a href='index.jsp'>首頁</a>
</body>
</html>
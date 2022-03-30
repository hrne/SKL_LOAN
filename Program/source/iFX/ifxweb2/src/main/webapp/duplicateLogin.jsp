<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
<title>重覆登入</title>
</head>
<%
final Logger logger = LoggerFactory.getLogger("/duplicateLogin.jsp");
String error = (String) session.getAttribute("error1");
String currSession = (String) session.getAttribute("currentSession");
String oldSession = (String) session.getAttribute("oldSession");
String user = (String) session.getAttribute("user");
String password = (String) session.getAttribute("password");

%>
<body>

<div>
<h1>重覆登入!!</h1><br/>
<h2><%=StringEscapeUtils.escapeXml10(error) %></h2><br/>
<a href='forceIn.jsp'>再次登入</a>
或
<a href='forceOut.jsp'>強制登出</a>
</div>
</body>
</html>
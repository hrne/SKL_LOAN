<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String brno = StringEscapeUtils.escapeXml10(request.getParameter("ifxbrno"));
if(brno == null){
	 brno = "1058T";
	}
com.st1.msw.UserInfo user = new com.st1.msw.UserInfo();
user.setBrno(brno);
user.setId("99");
user.setName("列印99");

session.setAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO, user);

 pageContext.forward("../today.jsp");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <br>
  </body>
</html>

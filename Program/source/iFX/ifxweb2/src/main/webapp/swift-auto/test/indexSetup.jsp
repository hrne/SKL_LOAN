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
pageContext.forward("../setup_boot.jsp");

%>


<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

  //SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
	
	//String brn = (String) sessionMap.get("BRN");
	
//com.st1.msw.UserInfo user = new com.st1.msw.UserInfo();
//user.setBrno(brn);
//user.setId("05");
//user.setName("桂圓05");

//session.setAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO, user);
 pageContext.forward("ifx-report/browseReport.jsp");

%>


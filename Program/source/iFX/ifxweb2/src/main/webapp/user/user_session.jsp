<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.st1.servlet.GlobalValues"%>  
<%@page language="java" import="java.util.*" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>My JSP 'log.jsp' starting page</title>
   	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">


  </head>
  
  <body>
   系統session<br>
     <div>
     	Session attributes:
<%
  //session.setAttribute("test.name", "Test Attribute List");
  //session.setAttribute("test.float", new Float(5.0));
  //session.setAttribute("test.int", new Integer(10));
  //session.setAttribute("test.Object", new StringBuffer("StringBuffer"));
  //session.setAttribute("test.boolean", new Boolean(true));
  //session.setAttribute("test.double", new Double(343.1));
  for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {     
    String attribName = (String) e.nextElement();
    Object attribValue = session.getAttribute(attribName);
%>
<BR><%= attribName %> - <%= attribValue %>

<%
}
%>
  </div>  
  </body>
</html>

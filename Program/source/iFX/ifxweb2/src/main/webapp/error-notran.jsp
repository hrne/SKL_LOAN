<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script"  value="${pageContext.request.contextPath}" />
<!--
潘  20180314 測試
<c:set var="script" value="${pageContext.request.contextPath}/script" />
-->
<body>
<div style="color:red;">

TXCODE: <%=request.getAttribute("txcode") %><br/>
Error:  <%=request.getAttribute("msg") %><br/>
<%
System.out.println(StringEscapeUtils.escapeXml10(request.getParameter("msg")));
%>
<input value="aaa"/>
</div>
<script src="<%=request.getContextPath()%>/jquery.js"></script>
<script>
$(function() {
	 var theFrame =  $('iframe', parent.document).last(); // new tab is last child of easytab's window
	 if($(document.body).height() > theFrame.height()) {
		 theFrame.height($(document.body).height() + 30);
	 }	 
});

</script>
</body>
</html>
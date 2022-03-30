<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.st1.servlet.GlobalValues"%>
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
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<script>
		<%
String version = GlobalValues.applicationVersion;
String ifxfolder = GlobalValues.ifxFolder;
String ifxwriter = GlobalValues.ifxWriter;
String runtimefolder = GlobalValues.runtimeFolder;
String tranfolder = GlobalValues.tranFolder;
String hostlogfolder = GlobalValues.hostLogFolder;
String swiftfolder = GlobalValues.swiftFolder;
String jsversion = GlobalValues.jsVersion;
String helpjsversion = GlobalValues.helpjsVersion;
String sealurl = GlobalValues.sealUrl;
int helpcacheminutes = GlobalValues.helpCacheMinutes;
String mqserverPort = GlobalValues.mqServerPort;
String localaddr = GlobalValues.localAddr;
%>
</script>


</head>

<body>
	系統參數
	<br>
	<div>
		<table border="1">
			<caption>system setup</caption>
			<tr>
				<td>version</td>
				<td><%=version %></td>
			</tr>
			<tr>
				<td>ifxfolder</td>
				<td><%=ifxfolder %></td>
			</tr>
			<tr>
				<td>ifxwriter</td>
				<td><%=ifxwriter %></td>
			</tr>
			<tr>
				<td>runtimefolder</td>
				<td><%=runtimefolder %></td>
			</tr>
			<tr>
				<td>tranfolder</td>
				<td><%=tranfolder %></td>
			</tr>
			<tr>
				<td>hostlogfolder</td>
				<td><%=hostlogfolder %></td>
			</tr>
			<tr>
				<td>swiftfolder</td>
				<td><%=swiftfolder %></td>
			</tr>
			<tr>
				<td>jsversion</td>
				<td><%=jsversion %></td>
			</tr>
			<tr>
				<td>helpjsversion</td>
				<td><%=helpjsversion %></td>
			</tr>
			<tr>
				<td>sealurl</td>
				<td><%=sealurl %></td>
			</tr>
			<tr>
				<td>helpcacheminutes</td>
				<td><%=helpcacheminutes %></td>
			</tr>
			<tr>
				<td>mqserverPort</td>
				<td><%=mqserverPort %></td>
			</tr>
			<tr>
				<td>localaddr</td>
				<td><%=localaddr %></td>
			</tr>
			<tr>
				<td>basePath</td>
				<td><%=basePath %></td>
			</tr>
			<tr>
				<td>pageContext.request.contextPath</td>
				<td><%=request.getContextPath()%></td>
			</tr>

		</table>

		<!--version      :<%=version %><br>
ifxfolder    :<%=ifxfolder %><br>
ifxwriter    :<%=ifxwriter %><br>
runtimefolder:<%=runtimefolder %><br>
tranfolder   :<%=tranfolder %><br>
hostlogfolder:<%=hostlogfolder %><br>
swiftfolder  :<%=swiftfolder %><br>
jsversion    :<%=jsversion %><br>
sealurl      :<%=sealurl %><br>
helpcacheminutes:<%=helpcacheminutes %><br>
-->
	</div>
</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="BIG5"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'fake.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body style="margin-left:30px;line-height:1.5">

  <div style="float:left;">
  <p style="font-size:xx-large">安裝步驟:</p>
	<ol style="font-size:x-large">
	  <li>下載Fake Device Agent到你的目錄(例如d:\agent) <a href="env/fakeagent/fakeAgent.rar">下載Device Agent</a></li>
	  <li>解開fakeAgent.rar</li>
	  <li>執行runFakeServer.bat</li>
	  <li>執行後, 該目錄會產生docs子目錄</li>
	</ol>
</div>
        
  <img style="float:left;margin-right: 15px;" src="images/IBM-9068_A03-500x500.JPG"/>
  
  </body>
</html>

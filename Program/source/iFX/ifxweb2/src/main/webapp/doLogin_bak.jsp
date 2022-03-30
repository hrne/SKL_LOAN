<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page import="com.st1.ifx.hcomm.app.ImsLoginSock"%>
<%@page import="com.st1.ifx.hcomm.app.SessionMap"%>
<%@page import="com.st1.ifx.service.JournalService"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.util.MySpring"%>
<%@page import="com.st1.msw.UserPub"%>
<%@page import="com.st1.msw.UserInfo"%>

<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%
	final Logger logger = LoggerFactory.getLogger("/doLogin.jsp");

	boolean checkDupLogin = true;
	String message = "";
	String user = StringEscapeUtils.escapeXml10(request.getParameter("user"));
	user = user.toUpperCase();//小潘改user id轉大寫
	String password = request.getParameter("password");
	String addr = request.getRemoteAddr(); //127.0.0.1
	logger.info("client:" + request.getRemoteAddr());

	//Login login = new Login(com.st1.servlet.GlobalValues.proxy, user,	password, addr);
	logger.info(FilterUtils.escape("user:" + user));
	logger.info("addr:" + addr);

	ImsLoginSock login = MySpring.getLoginHostBean();
	login.setUser(user);
	login.setPassword(password);
	login.setAddress(addr);

	//move to GlobalValues
	//login.setLogFolder(GlobalValues.hostLogFolder);
	
	List<Map<String, String>> li = login.rimLc013();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>IFX login</title>
<!--<link href="css/site.css" rel="stylesheet">-->
<!--<link href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" rel="stylesheet">-->
<link rel="shortcut icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/favicon.ico">
</head>
<body>
	<form name="form1">
    <input type="checkbox" name="c1" value="1" onclick="return chk(this);">A
    <input type="checkbox" name="c1" value="2" onclick="return chk(this);">B
    <input type="checkbox" name="c1" value="3" onclick="return chk(this);">C
  </form>
</body>
<script>
	function chk(input)
  {
    for(var i=0;i<document.form1.c1.length;i++)
    {
      document.form1.c1[i].checked = false;
    }
    
    input.checked = true;
    return true;
  }
</script>
</html>
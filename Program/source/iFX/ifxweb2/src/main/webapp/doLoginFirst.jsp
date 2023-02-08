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

<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>

<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%
	final Logger logger = LoggerFactory.getLogger("/doLoginFirst.jsp");
  boolean checkDupLogin = true;
	String message = "";
	String user = StringEscapeUtils.escapeXml10(request.getParameter("user") == null ? (String) session.getAttribute("user") : request.getParameter("user"));
	user = user.toUpperCase();//小潘改user id轉大寫
	String password = request.getParameter("password")  == null ? (String) session.getAttribute("password") : request.getParameter("password");
	String addr = request.getRemoteAddr(); //127.0.0.1
	logger.info("client:" + request.getRemoteAddr());

	//Login login = new Login(com.st1.servlet.GlobalValues.proxy, user,	password, addr);
	logger.info(FilterUtils.escape("user:" + user));
	logger.info("addr:" + addr);

	ImsLoginSock login = MySpring.getLoginHostBean();
	login.setUser(user);
	login.setPassword(password);
	login.setAddress(addr);
	
	UserPub pub = UserPub.getInstance();
	String oldSessionId = UserPub.checkUser(user);
	
	/*
	String clientWinName = request.getParameter("winname");
	String sessionWinName = (String) session.getAttribute("WINDOW_NAME");
	logger.info("client  Win Name:" + FilterUtils.escape(clientWinName));
	logger.info("session Win Name:" + FilterUtils.escape(sessionWinName));
	
	if (sessionWinName == null) {
		session.setAttribute("WINDOW_NAME", clientWinName);
		logger.info(FilterUtils.escape("first time login with WINDOW_NAME:" + clientWinName));
	} else {
		if (!(clientWinName.equals(sessionWinName))) {
			// different window, can't login
			if (checkDupLogin) {
				logger.info(FilterUtils.escape("can't login, session alredy contains WINDOW_NAME:" + session.getAttribute("WINDOW_NAME")));
				request.setAttribute("error", "E003-此瀏覽器已登入本系統!(WNAME)");
				request.getRequestDispatcher("errorLogin.jsp").forward(request, response);
				return;
			}
		} else {
			// same winname
			logger.info(FilterUtils.escape("same winname:" + clientWinName));
		}
	}
	*/
	
	if (oldSessionId != null) {
		if (checkDupLogin) {
			logger.info("duplicate login, old sessionId:" + FilterUtils.escape(oldSessionId));
			session.setAttribute("error1", "登入異常(Session Error:E001 DUPLOGIN)");
			session.setAttribute("currentSession", session.getId());
			session.setAttribute("oldSession", oldSessionId);
			session.setAttribute("user", user);
			session.setAttribute("password", password);
			response.sendRedirect("duplicateLogin.jsp");
			return;
		}
	}
	
	//move to GlobalValues
	//login.setLogFolder(GlobalValues.hostLogFolder);

	List<Map<String, String>> li = null;
	try{
		li = login.rimLc013();
	}catch(Exception e){
	  logger.info("Rim Lc013 Error");
	  StringWriter errors = new StringWriter();
	  e.printStackTrace(new PrintWriter(errors));
		logger.error(errors.toString());
	}
	
	if(li == null || li.size() == 0 || !login.isAdStatus()){
	    if(!login.isAdStatus())
	    	message = "AD驗證失敗";
	    else
		    message = "員編錯誤";

		session.setAttribute("error", message);
		session.setAttribute("user", user);

		//response.sendRedirect("login.jsp");
		request.getRequestDispatcher("login.jsp").forward(request, response);
		return;
	}
	
	try{
		if(li.size() == 1){
		  request.setAttribute("user", user);
		  request.setAttribute("password", password);
		  request.setAttribute("AuthNo", li.get(0).get("AuthNo"));
		  request.setAttribute("AuthItem", li.get(0).get("AuthItem"));
		  request.setAttribute("AgentNo", li.get(0).get("AgentNo"));
		  request.setAttribute("AgentItem", li.get(0).get("AgentItem"));
		  
		  request.getRequestDispatcher("doLoginSecond.jsp").forward(request, response);
		  return;
		}
	}catch(Exception e){
		StringWriter errors = new StringWriter();
	  e.printStackTrace(new PrintWriter(errors));
		logger.error(errors.toString());
	}

	for(int i = 0; i < li.size(); i++){
	  for (Map.Entry<String, String> entry : li.get(i).entrySet()) {
	    logger.info("Apan key : " + entry.getKey() + " Value : " + entry.getValue());
	  }
	}
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
<style type="text/css">
input[type=checkbox]:not(old),
input[type=radio   ]:not(old){
  width   : 28px;
  margin  : 0;
  padding : 0;
  opacity : 0;
}
input[type=checkbox]:not(old) + label{
  display      : inline-block;
  margin-left  : -28px;
  padding-left : 28px;
  line-height  : 24px;
  background   : url('images/checks.png') no-repeat 0 -48px;
}

input[type=checkbox]:not(old):checked + label{
  background-position : 0 -73px;
}

input[type=radio   ]:not(old) + label{
  display      : inline-block;
  margin-left  : -24px;
  padding-left : 28px;
  line-height  : 24px;
  background   : url('images/checks.png') no-repeat 0 3px;
}
input[type=radio]:not(old):checked + label{
  background-position : 0 -22px;
}

table{
	border: 1px solid #FFF;
    border-collapse: collapse;
    font-family: Microsoft JhengHei;
}
tr{
  border: 1px solid #FFF;
}
td{
  border: 1px solid #FFF;
  padding: 10px 30px;
  background-color: #a48686;
  color: #FFF;
}
td:first-child{
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
}

td:last-child{
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
}

#autd {
  width: 600px;
  height: 300px;
  position: absolute;
  top: 50%;
  left: 50%;
  margin: -150px 0 0 -300px;
}
</style>
<script src="script/jquery.js"></script>
<script src="script/jquery.validate.js"></script>
<script src="script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
</head>
<body>
	<div id="autd">
	<form id="form1" name="form1" action="doLoginSecond.jsp" method="post" autocomplete="off">
		<table width="100%" border="1px" border-collapse="collapse">
			<tr><td colspan="2" align="center">經辦身分選擇</td></tr>
			<tr>
	<%
	  for(int i = 0; i < li.size(); i++){
	%>
	    <tr>
	    <td>
	    		<% if(i == 0){ %>
	    		  <input id="radio<%= i%>" type="radio" name="AuthItem" value='<%= li.get(i).get("AuthNo")%>' checked="checked" onclick="checkRadio(id, value)"><label for="radio<%= i%>"><%= li.get(i).get("AuthItem").trim()%></label>
	    		<% }else{ %>
	    		  <input id="radio<%= i%>" type="radio" name="AuthItem" value='<%= li.get(i).get("AuthNo")%>' onclick="checkRadio(id, value)"><label for="radio<%= i%>"><%= li.get(i).get("AuthItem").trim()%></label>

	    		<% } %>
	    </td>
	    <td>
	    	<label id="AgentItem<%= i%>"><%= li.get(i).get("AgentItem").trim()%></label>
	    </td>

	    </tr>
	    <input type="text" id="AgentNo<%= i%>" name="AgentNo<%= i%>" value='<%= li.get(i).get("AgentNo")%>' style="display:none"/>
	<%
		}
	%>
	    </tr>
	    <tr><td colspan="2" align="right"><input class="button" type="submit" name="submit0" id="submit0" value="確定"/></td></tr>
	    <input type="text" id="user" name="user" value="" style="display:none"/>
	    <input type="text" id="password" name="password" value="" style="display:none"/>
	    <input type="text" id="AuthNo" name="AuthNo" value="" style="display:none"/>
	    <input type="text" id="AuthItem" name="AuthItem" value="" style="display:none"/>
	    <input type="text" id="AgentNo" name="AgentNo" value="" style="display:none"/>
	    <input type="text" id="AgentItem" name="AgentItem" value="" style="display:none"/>
    </table>
  </form>
  </div>
  <body onload="init()">
</body>
<script>
	var count = "<%= li.size()%>";
	var pawd = "<%= password%>";
	var ur = "<%= user%>";
	function init(){
	  $("#user").val(ur);
	  $("#password").val(pawd);
		
	  $("#AuthNo").val($("#radio0").val());
	  $("#AgentNo").val($("#AgentNo0").val());
    $("#AuthItem").val($("input[type='radio']:checked").parent().text().trim());
	  if(count == 1){
	  	checkRadio("radio0", $("#radio0").val());
	  	document.forms["form1"].submit();
	  }
	}

	function checkRadio(id, value) {
		var num = id.match(/[\d]+$/g);
		$("#AuthNo").val(value);
		$("#AuthItem").val($("input[type='radio']:checked").parent().text().trim());
		$("#AgentNo").val($("#AgentNo" + num.join("")).val());
		$("#AgentItem").val($("#AgentItem" + num.join("")).text());		
	}

  var body = document.body;
  body.addEventListener('keydown', goRocket ,false) //偵測按下按鍵的行為
  function goRocket(e){
  	switch(e.keyCode){
  	  case 13:
  	    document.forms["form1"].submit();
  	  break;
  	}
	}
</script>
</html>
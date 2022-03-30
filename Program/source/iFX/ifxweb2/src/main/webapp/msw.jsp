<%@page import="com.st1.servlet.GlobalValues"%>
<%@ page   language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>msw</title>
<script src="script/fixIE.js"></script>
<script src="script/jquery.js"></script>
 
<script type='text/javascript' src='dwr/engine.js'></script>

<!--
<script src="dwrForPolling/engine.js"></script>
 -->
<script type='text/javascript' src='dwr/interface/MswServer.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<script type="text/javascript" src='script/ifx-msw.js'>
	
</script>
</head> 
<%
if(session.getAttribute(GlobalValues.SESSION_UID)==null) {
	//request.setAttribute( "error",  "太久沒活動，請重新登入系統");
	//request.getRequestDispatcher("login.jsp").forward( request, response );
	session.setAttribute( "error",  "太久沒活動或信息系統有誤，請重新登入系統");
    response.sendRedirect("login.jsp");
}
%>
<body>
<div id='mswPanel'>
<input	type="button" value="block" onclick="testB()" />
<span id='userInfo'></span>&nbsp;<span id="message"></span><br/>
users:<select id="list"></select>

Message: <input id="text"
		onkeypress="dwr.util.onReturn(event, sendMessage)" /> <input
		type="button" value="Send" onclick="sendMessage()" />
		
		<input	type="button" value="find supervisors" onclick="getSupListReq()" />
		supervisor:<select id="supList"></select>
		<input	type="button" value="ovr request" onclick="sendOvrReq()" />
		ovr list:<select id="ovrList"></select>
		
<div id="chatlog"></div>
</div>
</body>
</html>
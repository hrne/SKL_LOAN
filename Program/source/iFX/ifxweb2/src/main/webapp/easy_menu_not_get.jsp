<%@page import="com.google.gson.Gson"%>
<%@page import="com.st1.ifx.menu.TranItem"%>
<%@page import="java.util.Map"%>
<%@page import="com.st1.ifx.etc.Pair"%>
<%@page import="com.st1.msw.UserInfo"%>
<%@page import="com.st1.ifx.menu.TranListBuilder"%>
<%@page import="com.st1.util.MySpring"%>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.def.menu.MenuBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/site.css" rel="stylesheet">

<link rel=stylesheet type=text/css
	href="script/jqueryui/css/redmond/jquery-ui-1.8.23.custom.css" />
<link rel=stylesheet type=text/css href="css/menu.css" />
<style>
.ui-autocomplete {
 max-height: 450px;
 overflow-y: auto;
 overflow-x: hidden;
 padding-right: 20px;
}

</style>

<body style="margin: 0px; padding: 0; border: 1">
	<div id="home"
		style="background-color: white; width: 0px; height: 0px; margin: 0px; display: none">&nbsp;</div>
	<div id="txcodeArea">
		<a href="#home" id='aa' style='color: red'></a>交易代號&nbsp;<input
			type="text" name="txcode" id="txcode" size="5" maxlength="5">
	</div>
	<div id=tabs>

		<script  type="text/javascript">
			
		<%String attachText = (String)session
					.getAttribute(GlobalValues.SESSION_USER_ATTACH);
			String c12Text = (String)session
					.getAttribute(GlobalValues.SESSION_USER_C12);

		
			UserInfo userInfo =  (UserInfo)session.getAttribute(GlobalValues.SESSION_USER_INFO);
			
			TranListBuilder builder =  MySpring.getTranListBuilder();
			//Pair<String,Map<String,TranItem>> pair =  builder.build(userInfo.getBrno(), userInfo.getId(), attachText, c12Text);
			//out.println("\n\nvar _tranMap = " + new Gson().toJson(pair.second)  + ";\n\n");
			//String content = GlobalValues.getMenuHtml();
			//out.print(content);%>
			
		</script>
		<% 
			//out.print(pair.first);
		%>
	</div>

	<div id="dashboard2"></div>
	
	<script src="script/jquery.js"></script>
	<script src="script/jqueryui/js/jquery-ui-1.8.19.custom.min.js"></script>
	<script src="script/sidemenu/side.js"></script>
	<script src="script/ifx-easymenu.js"></script>
</body>
</html>
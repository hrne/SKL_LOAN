<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>IFX USER 設定頁面</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<meta http-equiv="cache-control" content="no-store">
<!-- environment variables -->
<link rel=stylesheet type=text/css
	href="${pageContext.request.contextPath}/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/jqgrid/css/ui.jqgrid.css" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/swift-auto/bootstrap/css/bootstrap.css">
<script src="<%=request.getContextPath()%>/script/jquery.js"></script>	
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/env/script/device/ifx-device.js"></script>	
<script src="<%=request.getContextPath()%>/user/menu-setup.js"></script>
<script>
<%
String brno = StringEscapeUtils.escapeXml10(request.getParameter("_b"));
String addr = request.getRemoteAddr();  
	com.st1.msw.UserInfo user = (com.st1.msw.UserInfo) session
			.getAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO);
%>
var _contextPath = '<%=request.getContextPath()%>';
var _brno = '<%=brno%>';
var _addr = '<%=addr%>';
</script>
<script src="<%=request.getContextPath()%>/script/ifx-file2.js"></script>	

</head>

<body>
<%
//if(brno == null){
//	out.println("<h2>error, too few parameters</h2>");
//	out.flush();
//	return;
//}
%>

<div id="tabs">
  <ul>
  	<li><a href="user_printer_set.jsp">設定印表機</a></li>
  	<li><a href="user_printer_parms.jsp">印表機參數</a></li>
  	<li><a href="menu_boot.jsp">自訂選單</a></li>
  	<li><a href="sys_properties.jsp">參數設定</a></li>
  	<!--<li><a href="user_menu_set.jsp">設定自訂選單</a></li>  -->
    <li><a href="user_session.jsp">session</a></li>
  </ul>
  <div id="tabs-1">
    <p>本機各項參數設定 
    	 <small class="label  label-warning"><%=user.getName()%></small>
    </p>
  </div>
</div>
<script>
$(function() {
    $( "#tabs" ).tabs({
      beforeLoad: function( event, ui ) {
       // ui.jqXHR.error(function() {
       //   ui.panel.html(
       //     "Couldn't load this tab. We'll try to fix this as soon as possible. " +
       //     "If this wouldn't be a demo." );
       // });
      }
    });
  });

</script>
</body>
</html>

<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>檔案上傳</title>

	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<meta HTTP-EQUIV="Expires" CONTENT="-1">
	<meta http-equiv="cache-control" content="no-store">

	<!-- environment variables -->
	<link rel=stylesheet type=text/css href="${pageContext.request.contextPath}/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jqgrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/swift-auto/bootstrap/css/bootstrap.css">

	<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>

	<script>
		<%
			String brno = StringEscapeUtils.escapeXml10(request.getParameter("_b"));
			String addr = request.getRemoteAddr();
			com.st1.msw.UserInfo user = (com.st1.msw.UserInfo) session.getAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO);
		%>
		var _contextPath = '<%=request.getContextPath()%>';
		var _brno = '<%=brno%>';
		var _addr = '<%=addr%>';
	</script>
</head>
<body>
	<div id="tabs">
  	<ul>
  		<li><a href="setSendIp.jsp">回送IP</a></li>
  		<li><a href="fileUpload/uploadSwift.jsp">檔案上傳</a></li>
  	</ul>
  	<div id="tabs-1" >
    	<p>個別檔案不得超過5MB，總大小不得超過20MB</p>
    	<p style="text-align:right;"><small class="label  label-warning">	<%=user.getName()%>	</small></p>
    </div>
	</div>
	<script>
		$(function() {
    	$( "#tabs" ).tabs({
      	beforeLoad: function( event, ui ) {}
    	});
  	});
	</script>
</body>
</html>
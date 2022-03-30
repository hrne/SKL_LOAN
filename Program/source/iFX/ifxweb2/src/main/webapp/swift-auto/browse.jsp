<%@page import="com.st1.servlet.GlobalValues"%>
<!DOCTYPE html>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width,initial-scale=1">

<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />
<c:set var="v" value="?_v=2017042001" />


<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	com.st1.msw.UserInfo user = (com.st1.msw.UserInfo) session
			.getAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO);
	//System.out.println(user.getBrno());
	//System.out.println("basePath:" + basePath);
	//System.out.println("path:" + path);
%>

<head>
<base href="<%=basePath%>">

<link rel=stylesheet type=text/css
	href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css" />


<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/swift-auto/bootstrap/css/bootstrap.css">

<!--[if lt IE 9]>
            <script src="js/html5shiv.js"></script>
            <script src="js/respond.min.js"></script>
        <![endif]-->
<title>請至交易面處理</title>

<style>


#mtArea div {
	margin: 5px;
	line-height: 140%;
	background-color: Azure;
	
	padding-bottom: 10px;
	border-radius: 10px;
}

#mtArea div span {
	margin: 5px;
	display: inline-block;
	text-transform: uppercase;
}

#mtArea div span input[type=checkbox]:checked+label {
	color: blue;
	background-color: GreenYellow;
}

#mtArea div:nth-child(odd) {
	color: ForestGreen;
	background-color: MistyRose;
}

#mtArea div:nth-child(even) {
	color: black;
}
</style>
</head>
<body>
	<div class="page-header">
			<h1>
				如需重印請至交易功能面處理 XW520..等 <span class="label  label-success"></span>
			</h1>
	</div>
</body>
</html>
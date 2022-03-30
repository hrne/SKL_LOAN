<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />

<link rel=stylesheet type=text/css href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
<script>
            $(document).ready(function() {
            	alert("hi");
            	$('#btnPrev').click(function(){
            		alert("prev");
            	});
            	$('#btnNext').click(function(){
            		alert("next");
            	});
            });
        </script>
</head>

<body>

	<div>
		<div>
			<button id="btnPrev">&lt;</button>
			<span id="currPage" />
			<button id="btnNext">&gt;</button>
		</div>
		<div id="tblLog" />

	</div>
</body>
</html>

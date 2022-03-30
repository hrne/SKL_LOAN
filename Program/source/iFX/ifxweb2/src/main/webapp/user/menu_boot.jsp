<%@page import="com.st1.servlet.GlobalValues"%>
<!DOCTYPE html>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">

<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />
<c:set var="v" value="?_v=2013082509" />


<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	//System.out.println(user.getBrno());
	//System.out.println("basePath:" + basePath);
	//System.out.println("path:" + path);
%>

<head>
<base href="<%=basePath%>">


<!--[if lt IE 9]>
            <script src="js/html5shiv.js"></script>
            <script src="js/respond.min.js"></script>
        <![endif]-->
<title>Local Menu setup</title>

<style>


#mtArea div {
	margin: 4px;
	line-height: 100%;
	background-color: Azure;
	
	padding-bottom: 6px;
	border-radius: 6px;
}

#mtArea div span {
	margin: 4px;
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

  <%@include file="/env/myDevice.jsp" %>
</head>

<body>
		<div class="page-header">
			<h1>本機自訂選單設定</h1>

		</div>
		<div id="main">
			<div class="form-group">
			<div class="well  well-md">
				<h3>您打算指定那些交易至<div class="label label-primary">自訂選單</div>?</h3>
			<div id="mtArea" class="row"></div>
			<div>
				<button id="btnSave" class="btn btn-success btn-sm">儲存</button>
				<button id="btnUpload" class="btn btn-default btn-sm">Debug上傳</button>
				<button id="btnPutdata" class="btn btn-default btn-sm">讀取本機設定</button>
			</div>
 			</div>
		</div>
	</div>
	<div style="height:20px"></div>


	<script src="<%=request.getContextPath() %>/jqgrid/js/grid.locale-tw.js"></script>
	<script src="<%=request.getContextPath() %>/jqgrid/js/jquery.jqGrid.src.js"></script>
	<script src="<%=request.getContextPath() %>/script/async.js/async.js"></script>
	<script src="<%=request.getContextPath() %>/swift-auto/bootstrap/js/bootstrap.min.js"></script>
	<script>
		var _contextPath = "<%=request.getContextPath()%>";
		var __selectedPrinter = null;
		var _swfSetup;
		function startRun123() {
			_swfSetup = UserMenu.setup;
			_swfSetup.init(_contextPath, "main");
			_swfSetup.getMenuDef();
		}
		
		$( document ).ready(function() {
				Device.pb.setup('ifxws', 'myDevice');
				Device.pb.init();
				setTimeout(startRun123, 1);
			});
	</script>
</body>
</html>

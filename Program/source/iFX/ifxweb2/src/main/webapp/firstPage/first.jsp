<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<!--END-->
<c:set var="context" value="${pageContext.request.contextPath}" />
<link rel=stylesheet type=text/css
	href="../script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />
<link rel="stylesheet" type="text/css"
	href="../jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" href="css/firstPage.css?20161125" type="text/css" />


</head>

<body style="margin:0px">
	<div id="pagewidth" style="margin:0px">
		<div id="wrapper" class="clearfix">
			<div id="maincol">
				<div class="tabPanel">
					<div id='msgBoxStaus'>請稍候</div>
					
					<table id="list1"></table>
					<div id="pager1"></div>
				</div>
				<div class="tabPanel">
					<div id='tickerStatus'>請稍候</div>
					<table id="list2"></table>
					<div id="pager2"></div>
				</div>
			</div>
			<div id="leftcol">
				<div class="tabButton" id="btnMsg">個人訊息</div>
				<div class="tabButton" id="btnTicker">重要消息</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		var _contextPath = '<%=request.getContextPath()%>';
	</script>
	<script type="text/javascript" src="../script/jquery.js"></script>

	<script type="text/javascript" src="../jqgrid/js/grid.locale-tw.js"></script>
	<script type="text/javascript" src="../jqgrid/js/jquery.jqGrid.min.js"></script>

	<script type="text/javascript" src="script/myTab.js"></script>
	<script type="text/javascript" src="firstFn.js?20161125"></script>
	
</body>

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<!--You can also disable or stop caching using  the following meta tag START-->
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>
</html>

<!DOCTYPE html>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />



<script src="<%=request.getContextPath()%>/script/stacktrace-min-0.4.js"></script>
<script src="<%=request.getContextPath()%>/script/fixIE.js"></script>
<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
<script src="<%=request.getContextPath()%>/script/jquery.blockUI.js"></script>
<script src="<%=request.getContextPath()%>/script/jquery.alerts.js"></script>
<script src="<%=request.getContextPath()%>/script/json2.js"></script>
<script src="<%=request.getContextPath()%>/script/underscore.js"></script>
<script src="<%=request.getContextPath()%>/script/underscore.string.js"></script>
<script src="<%=request.getContextPath()%>/script/ifx-core2.js"></script>
<script src="<%=request.getContextPath()%>/script/ifx-fld.js"></script>
<script src="<%=request.getContextPath()%>/script/ifx-rtn.js"></script>
<script src="<%=request.getContextPath()%>/script/ifx-call.js"></script>

<!-- 
<script src="script/help/Help.js"></script>
<script src="script/help/errors.js"></script>
 -->
<script src="mini/external/Help.js"></script>
<script src="mini/external/Errors.js"></script>

<script src="<%=request.getContextPath()%>/script/help/ifx-help.js"></script>
<script src="<%=request.getContextPath()%>/script/ifx-utl.js"></script>
<script src="<%=request.getContextPath()%>/script/ifx-keys.js"></script>
<script src="<%=request.getContextPath()%>/script/host/ifx-host.js"></script>
<script src="<%=request.getContextPath()%>/script/ifx-file.js"></script>
<script src="<%=request.getContextPath()%>/script/display/ifx-display.js"></script>
<script src="<%=request.getContextPath()%>/script/shareTool.js"></script>

<!-- 
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/easyui/jquery.easyui.min.js"></script>
 -->	
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery.ui.datepicker-zh-TW.js"></script>

<script src="<%=request.getContextPath()%>/script/sidemenu/side.js"></script>
<script src="<%=request.getContextPath()%>/script/jquery.scrollTo-1.4.3-min.js"></script>


<link rel=stylesheet type=text/css
	href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css" />

<!-- 
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/script/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/script/easyui/themes/icon.css">
 -->
 
<script src="<%=request.getContextPath()%>/jqgrid/js/grid.locale-tw.js"
	type="text/javascript"></script>

<script src="<%=request.getContextPath()%>/jqgrid/js/jquery.jqGrid.src.js"
	type="text/javascript"></script>

<script src="<%=request.getContextPath()%>/script/ifx-grid.js"></script>

<!-- TODO: if swift tran include swift-*.js else not include -->
<script src="<%=request.getContextPath()%>/sweet/script/swift-lib.js"></script>
<script src="<%=request.getContextPath()%>/sweet/script/swift-generic.js"></script>
<script src="<%=request.getContextPath()%>/sweet/script/swift-errors.js"></script>
<script src="<%=request.getContextPath()%>/sweet/script/swift-validators.js"></script>
<script src="<%=request.getContextPath()%>/sweet/script/swift-cur.js"></script>
<link href="<%=request.getContextPath()%>/css/site.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/jquery.alerts.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/swift.css" rel="stylesheet">

</head>
<body style="margin: 10px; margin-top: 3px;border:0">
	<div id='main-container' style="width: 100%;">		<div id="topbuttons"></div>
		<div id="helpArea">this is help</div>
		<div id="tooltipArea">this is tooltip</div>
		<div id="entry-errmsg"></div>
		<div id="entryArea">
		</div>
		<br />
		
		<div id='queryHeader'></div>
		<div id="queryArea" >
			
			<table id="grid"></table>
			<div id="pager"></div>
			<input type="BUTTON" id="btnSearch" style='color: blue'
				class='querybutton' value=" 搜　尋 " /> <input type="BUTTON"
				id="btnReset" style='color: orangered' class='querybutton'
				value=" 重　設 " /> <input type='button' id='btnExport'
				style='color: rosy' class='querybutton' data-url='viewgrid.jsp'
				value=' 匯　出 ' /> <input type='button' id='openWin'
				style='color: blue' class='querybutton' data-url='viewgrid.jsp'
				value=' 在新視窗檢視 ' />
		</div>
		<div id="outputArea"></div>

	</div>
	<!-- 
		<div id="dashboard2"></div>
	<div id="dashboard"></div>
 -->


	<script src="${txReq.jsFileUrl}"></script>
 
	
 
	<script type="text/javascript">
		$.jgrid.no_legacy_api = true;
		$.jgrid.useJSON = true;
		var _contextPath  = '<%=request.getContextPath()%>';
		
		<c:out value="${txReq.jsBlock1}"  escapeXml="false" />
	</script>
	<script src="<%=request.getContextPath()%>/script/ifx-start.js"></script>
</body>
</html>
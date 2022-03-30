<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />
<link rel=stylesheet type=text/css href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />


<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/env/script/device/ifx-device.js"></script>
<script src='<%=request.getContextPath()%>/dwr/engine.js'></script>
<script>
<%String brno = StringEscapeUtils.escapeXml10(request.getParameter("_b"));
			String addr = request.getRemoteAddr();%>
var _contextPath = '<%=request.getContextPath()%>';
var _brno = '<%=brno%>';
var _addr = '<%=addr%>';
//var _contextPath = "<%=request.getContextPath()%>
	";
</script>
<script src="<%=request.getContextPath()%>/script/ifx-file2.js"></script>
<title>console</title>


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
			<li><a href="session.jsp">系統session</a></li>
			<li><a href="printer_set.jsp">測試設定印表機</a></li>
			<li><a href="adtest.jsp">測試AD連線</a></li>
			<li><a href="helptest.jsp">測試DB2連線</a></li>
			<li><a href="webfile.jsp">測試讀取檔案</a></li>
			<!--測試錯誤<li><a href="webfilelog.jsp">測試讀取log檔案</a></li>-->
			<li><a href="#tabs-1">Console(<%=brno%>)
			</a></li>
			<!--<li><a href="../mvc/rpt/log/1058/20150416/1">System Log</a></li>
    <li><a href="swift-print.jsp">Swift重印</a></li>
    <li><a href="report-print.jsp">報表重印</a></li>-->
			<li><a href="setup.jsp">系統設定</a></li>
		</ul>
		<div id="tabs-1">
			<p>
				Hello Branch
				<%=brno%>
				ifx branch controller console
			</p>
		</div>
	</div>
	<script>
		$(function() {
			$("#tabs").tabs({
				beforeLoad : function(event, ui) {
					// ui.jqXHR.error(function() {
					//   ui.panel.html(
					//     "Couldn't load this tab. We'll try to fix this as soon as possible. " +
					//     "If this wouldn't be a demo." );
					// });
				}
			});
		});
		//20180111 新增檢測
		dwr.engine.setActiveReverseAjax(true);
		dwr.engine.setNotifyServerOnPageUnload(true);
		dwr.engine.setErrorHandler(function(msg, ex) {
			msg = msg || "A server error has occurred.";
			if (confirm('信息系統錯誤，請確認安裝是否正常?\n' + msg + "\n" + JSON.stringify(ex))) {
			}
		});
	</script>
</body>
</html>

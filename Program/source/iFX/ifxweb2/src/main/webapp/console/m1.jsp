<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script"  value="${pageContext.request.contextPath}/script" />
<c:set var="css"     value="${pageContext.request.contextPath}/css" />
<link rel=stylesheet type=text/css
	href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />
	
<script src="<%=request.getContextPath()%>/script/jquery.js"></script>	
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script>
<%
String brno = StringEscapeUtils.escapeXml10(request.getParameter("_b"));
%>
var _contextPath = '<%=request.getContextPath()%>';
var _brno = '<%=brno%>';
</script>
<title>console</title>


</head>

<body>
<%
if(brno == null){
	out.println("<h2>error, too few parameters</h2>");
	out.flush();
	return;
}
%>

<div id="tabs">
  <ul>
    <li><a href="#tabs-1">Console(<%=brno %>)</a></li>
    <li><a href="../mvc/rpt/log/<%=brno%>/1">System Log</a></li>
    <li><a href="swift-print.jsp">Swift重印</a></li>
    <li><a href="report-print.jsp">報表重印</a></li>
    <li><a href="setup.jsp">系統設定</a></li>
    
  </ul>
  <div id="tabs-1">
    <p>
    	Hello Branch <%=brno %>
    	ifx branch controller console
    </p>
  </div>
</div>
<script>
$(function() {
    $( "#tabs" ).tabs({
      beforeLoad: function( event, ui ) {
        ui.jqXHR.error(function() {
          ui.panel.html(
            "Couldn't load this tab. We'll try to fix this as soon as possible. " +
            "If this wouldn't be a demo." );
        });
      }
    });
  });

</script>
</body>
</html>

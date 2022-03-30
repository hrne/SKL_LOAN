<%@page import="com.st1.util.PoorManUtil"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<style>
html {
	font-size: 10px;
}

.iframetab {
	width: 100%;
	height: auto;
	border: 0px;
	margin: 0px;
	//background: url("img/iframeno.png");
	position: relative;
	top: -13px;
}

.ui-tabs-panel {
	padding: 5px !important;
}

.openout {
	float: right;
	position: relative;
	top: -28px;
	left: -5px;
}
</style>

<link rel=stylesheet type=text/css
	href="<%=request.getContextPath()%>/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<script src="<%=request.getContextPath()%>/jquery.js"></script>
<script src="<%=request.getContextPath()%>/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script>
<%
String brno = StringEscapeUtils.escapeXml10(request.getParameter("_b"));
String dt = PoorManUtil.getToday();
%>
var _contextPath = '<%=request.getContextPath()%>';
var _brno = '<%=StringEscapeUtils.escapeXml10(brno)%>';
</script>
<title>console</title>


</head>

<body>
	<%
		if (brno == null) {
			out.println("<h2>error, too few parameters</h2>");
			out.flush();
			return;
		}
	%>

	<div id="tabs">
		<ul>
			<li><a class="tabref" href="#tabs-1" rel="<%=request.getContextPath()%>/mvc/rpt/log/<%=StringEscapeUtils.escapeXml10(brno)%>/<%=StringEscapeUtils.escapeXml10(dt)%>/1">log</a></li>
			<li><a class="tabref" href="#tabs-2" rel="http://bing.co.uk">bing</a></li>
			<li><a class="tabref" href="#tabs-3" rel="http://yahoo.co.uk">yahoo</a></li>
			
		</ul>
		<div id="tabs-1" class="tabMain"></div>

		<div id="tabs-2"></div>

		<div id="tabs-3"></div>
	</div>
	<script>
		$(document)
				.ready(
						function() {
							var $tabs = $('#tabs').tabs();

							//get selected tab
							function getSelectedTabIndex() {
								return $tabs.tabs('option', 'active');
							}

							//get tab contents
							beginTab = $(
									"#tabs ul li:eq(" + getSelectedTabIndex()
											+ ")").find("a");

							loadTabFrame($(beginTab).attr("href"), $(beginTab)
									.attr("rel"));

							$("a.tabref").click(
									function() {
										loadTabFrame($(this).attr("href"), $(
												this).attr("rel"));
									});

							//tab switching function
							function loadTabFrame(tab, url) {
								if ($(tab).find("iframe").length == 0) {
									var html = [];
									html.push('<div class="tabIframeWrapper">');
									//html.push('<div class="openout"><a href="' + url + '"><img src="img/world.png" border="0" alt="Open" title="Remove iFrame" /></a></div>');
									html.push('<iframe class="iframetab" src="' + url + '">Load Failed?</iframe>');
									html.push('</div>');
									$(tab).append(html.join(""));
									$(tab).find("iframe").height(
											$(window).height() - 80);
								}
								return false;
							}
						});
	</script>
</body>
</html>

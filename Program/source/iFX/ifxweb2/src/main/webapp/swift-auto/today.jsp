<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<!DOCTYPE html>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">

<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />
<c:set var="v" value="?_v=2013082509" />


<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
com.st1.msw.UserInfo user = (com.st1.msw.UserInfo)session.getAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO);
//System.out.println(user.getBrno());
//System.out.println("basePath:"+basePath);
//System.out.println("path:"+path);

%>



  <head>
    <base href="<%=basePath%>">

    <link rel=stylesheet type=text/css
			href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css" />
		<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/swift-auto/bootstrap/css/bootstrap.css">

    <title>今日swift來電</title>

	 <style>
  #toolbar {
    padding: 4px;
    display: inline-block;
  }
  /* support: IE7 */
  *+html #toolbar {
    display: inline;
  }
  </style>
  </head>
  <body>
  	<div>
  		<h1>
    Swift 自動列印:<span class="label  label-success"><%=user.getBrno()%>分行</span>
    </h1>
    </div>
    <div>
    <p>
    	<span style="font-size:20px;">
        1.點「開始列印」前請先確認設定之印表機電源已打開且已裝好電文紙， 否則，電文可能會不見了，須另由XW520補印。<br>
		2.點「開始列印」後每60秒啟動一次擷取中心資料，請耐心等候約60秒。<br>
		3.確認來電是否印出之方式，執行XW923電文類別空白，再比對是否相符。<br>
		4.無專用電文印表機分行，印完電文後記得點「暫緩列印」，通常每天分 早上、中午、傍晚三次收電即可。
		 </span>
	</p>
   	</div>
    <div>
    <h2>
     今日未列印電文稿:<span id="waitcount" class="label  label-primary"></span>
    </h2>
    <h2 id="waitstr">
     <span class="label  label-danger">暫緩列印中...</span>
    </h2>
    </div>
    <div id="toolbar" class="ui-widget-header ui-corner-all">
  		<button id="btnOpen">顯示執行訊息</button>
  		<button id="btnClear">清除執行訊息</button>
  		<button id="btnWait">開始列印/暫緩列印</button>
  	</div>
    <div id="dAuto" class="ui-widget-content" style="width:700px">
    </div>




    <script src="<%=request.getContextPath()%>/script/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/grid.locale-tw.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/jquery.jqGrid.min.js"></script>
    <script src="<%=request.getContextPath()%>/script/async.js/async.js"></script>
    <script src="swift-auto/swift-printer.js"></script>
   	<script src="swift-auto/swift-auto.js"></script>
    <script>
    <%String addr = request.getRemoteAddr();%>
    var _contextPath = '<%=request.getContextPath()%>';
    var _addrip = "<%=StringEscapeUtils.escapeXml10(addr)%>";// 2017/11/23 潘
    var swfAuto;
    $(function(){
    	initToolBar();
    	swfAuto = SwiftAuto.main;
    	swfAuto.init(_contextPath, _contextPath +"/swift-auto/test/index.jsp", '#dAuto', 60);
    	swfAuto.setPrinter(_printers);
    	_getPrinters();
    	swfAuto.setcheckAllowip(_addrip);
    	swfAuto.waitPrint();
    	swfAuto.start();

    	function initToolBar(){
    	 	$( "#btnOpen" ).button({
      			text: true,
      			icons: {
        			primary: "ui-icon-mail-open"
      			}
    		}).click(function(){
    			swfAuto.openLog();
    		});
    		$( "#btnClear" ).button({
      			icons: {
        			primary: "ui-icon-close"
      			}
    		}).click(function(){
    			swfAuto.clearLog();
    		});
    		$( "#btnWait" ).button().click(function(){
    			swfAuto.waitPrint();
    		});
    	}
    });
    </script>
  </body>
</html>

<%@page import="com.st1.servlet.GlobalValues"%>
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
<c:set var="v" value="?_v=2013082420" />

<script>
var _contextPath = '<%=request.getContextPath() %>';
var _version = '?_v=2013082420';
</script>

<title></title>
<link rel="stylesheet" type="text/css"
	href="script/easyui/themes/default/easyui.css">
<link rel="stisylesheet" type="text/css"
	href="script/easyui/themes/icon.css">
<link href="css/site.css" rel="stylesheet">
<link rel=stylesheet type=text/css
	href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />
<link type="text/css" rel="stylesheet"
	href="script/jquery.layout/layout-default-latest.css" />
<link rel="stylesheet" type="text/css" href="css/layout.css">
<link rel="stylesheet" type="text/css" href="script/countdown/jquery.countdown.css">

<link rel="stylesheet" type="text/css" href="script/alertify/css/alertify.css">
<link rel="stylesheet" type="text/css" href="script/alertify/css/themes/default.css">

<link rel="stylesheet" type="text/css" href="script/bootstrap-4.5.0/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="script/bootstrap-submenu-3.0.1/css/bootstrap-submenu.css">

<style type="text/css">

html,body {
	padding: 0px;
	margin: 0px;
}

.tabs-panels {
	border: 0;
}

.tabs-title {
	font-size: 14px;
}

.tabs-selected  .tabs-title {
	font-size: 16px;
	color: #cd1919;
}
.ui-layout-north {margin:0;padding:0;background-color: white }
<%--潘  #tickers {line-height: 1.1;margin:2px;margin-left:10px;color:ivory;font-size:16px;text-align: left; background-color: #585858 ; border-radius: 2px;}--%>
#tickers {line-height:1.2; margin:2px; margin-left:10px; color:red; font-size:19px;text-align:left; background-color:white; border-radius: 2px; font-weight:bold;}
#tickers.paused { background-color: white; color: black; }
</style>

</head>

<body class="content" style="width:100%;height:100%;margin: 0; padding: 0; border: 0">

	<div id='center' class="ui-layout-center" style='overflow:visible'>
			<div style='height:56px;'>
			<div style='width: 100%;position: fixed;z-index: 100;'>
			<nav class="navbar navbar-light bg-light navbar-expand-sm">
        <!--
        <a class="navbar-brand"></a>

        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target=".navbar-collapse">
          <span class="navbar-toggler-icon"></span>
        </button>
         -->
        <div class="collapse navbar-collapse">
          <ul id="sklMenu" class="navbar-nav mr-auto">
          </ul>
        </div>
      </nav>
		</div>
		</div>
		<div id="tt" class="easyui-tabs" style="width: auto; border: 0; margin-bottom: 0px;"></div>
	</div>

	<div class="ui-layout-west">
		<div id='dashboard-title'></div>
		<div id='dashboard3'></div>
	</div>

	<!--<DIV class="ui-layout-south">

		<div id="swift-code-area" style='display:none'>
			<div id='swifit-code-result' style='display:none;color:black'>
			<button id='btnApply'>Apply</button>
				<table border='1'>
				<tr><td>SWIFT CODE</td><td><span id="txtCode"></span></td></tr>
				<tr><td>BANK NAME</td><td><span id="txtName"></span></td></tr>
				<tr><td>Address1</td><td><span id="txtAddr1"></span></td></tr>
				<tr><td>Address2</td><td><span id="txtAddr2"></span></td></tr>
				<tr><td>Address3</td><td><span id="txtAddr3"></span></td></tr>
				</table>

			</div>
			<button id='btnLoad'>Load</button>
			<table id="grid"></table>
			<div id="pager"></div>
		</div>
	</DIV> -->

<!-- 	<DIV class="ui-layout-east"></DIV>-->
	<DIV class="ui-layout-north">
		<div id="tickers">
        </div>
	</DIV>
	<jsp:include page="env/myDevice.jsp"></jsp:include>



	<div id='ovrPanel' title="遠端主管授權"
		style='display: none; margin: 10; padding: 10'></div>

	<div id='mswPanel' style='display: none' title="msw panel"
		style='font-size: 15px; color: blue;'>
		<div>
			<button id='btnGetUsers'>Users</button>
			<button id='btnGetSupervisors'>Supervisors</button>
		</div>
		<div>
			to:<input type="text" id="txtTo" value="ALL" size='10' /> <input
				type="text" id="txtMessage" size='50' />
			<button id='btnTalk'>Talk</button>
		</div>
		<div>
			<textarea id="mswHistory" Rows="7" Cols="88" readonly></textarea>
			<br />
		</div>
	</div>

	<div id='supervisorPanel' title="主管授權"
		style='font-size: 15px; color: blue; display: none'>
		<div id='divOvrList' style="margin:15px;">
			<div  style="margin:10px;">
			<select id='listSupOvr' size='5' style="width:auto">

			</select>
			</div>
		</div>
		<div id='divBtns'  style="margin:15px;">
			<button id='btnSupScreenCopy'>處理</button>
			<button id='btnSupRemoveItem'>捨棄</button>
			<button id='btnSupCancelSelect'>離開</button>
		</div>
		<div id='divProcessOvr'  style="margin:10px;">
			經辦:<span id='ovrUserId'></span> &nbsp;交易:<span id='ovrTxcd'></span><br />
				<div  style="margin:10px;">
			<textarea id="ovrReasons" rows="1" cols="100" readonly></textarea>
			</div>

			<button id='btnSupAccept'>核准</button>
			<button id='btnSupReject'>拒絕</button>
			<input type="password" style="display:none">
			<b>密碼:</b><input type="password" id='password' autocomplete="new-password">
			<span style="color:red;"><b>輸入後可按ENTER核准送出</b></span>
			<!--退回原因:--><input type='text' id='ovrRejectReason' style="display:none" />
			<div id="ovrWaitScreen">下載交易畫面中....</div>
			<div id="ovrScreen" style="margin:5px"></div>
		</div>
	</div>

	<div id="dialog-confirm" title="確定要離開?" style='display: none'>
		<!--<p>
			<span class="ui-icon ui-icon-alert"
				style="float: left; margin: 0 7px 20px 0;"> </span>
				<span id="dialog-showerror" style="">請選擇離開交易或清除畫面</span>
		</p>-->
	</div>

	<div id="logoff-dialog" title="即將登出" style='display: none'>
        <div id="logoff-shortly"></div>
        <div id="logoff-monitor" style="float: left;margin-top: 10px;margin-left:30px;color: red"></div>
    </div>

<script src="script/fixIE.js"></script>
<script src="script/jquery.js"></script>
<script src="script/jquery.blockUI.js"></script>
<script src="script/jquery.pulse.min.js"></script>
<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>

<script
	src="script/jqueryui/js/dialogOnly/jquery-ui-1.10.3.dlg_btn.min.js"></script>

<!-- layout -->
<script type="text/javascript"
	src="script/jquery.layout/jquery.layout-latest.js"></script>
<!--  DWR -->

<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/interface/MswCenter.js'></script>

<script type='text/javascript' src='dwr/util.js'></script>

<script type="text/javascript" src="script/countdown/jquery.countdown.min.js"></script>
<script type='text/javascript' src='script/ticker/jquery.vticker.min.js'></script>
<!--
<script src="_IFX_<%=request.getContextPath() %>/script/ifx-utl.js"></script>
<script src="_IFX_<%=request.getContextPath() %>/script/ifx-ovrReq.js"></script>
<script src='_IFX_<%=request.getContextPath() %>/script/base64.js'></script>
<script src='_IFX_<%=request.getContextPath() %>/script/ifx-mswCenter.js'></script>
<script src="_IFX_<%=request.getContextPath() %>/script/sidemenu/side.js"></script>
<script src="_IFX_<%=request.getContextPath() %>/script/ifx-easytab.js"></script>
 -->

<script src="<%=request.getContextPath() %>/script/yepnope.1.5.4-min.js"></script>
<script>
var _contextPath = '<%=request.getContextPath() %>';
<%
String s = GlobalValues.jsVersion;

String sysvar = (String) session.getAttribute(GlobalValues.SESSION_SYSVAR);
out.println("\n var _sysvar=" + sysvar);

%>
var _version = "?v=<%=s%>";
var resources = [
 "_IFX_<%=request.getContextPath() %>/script/ifx-utl.js"
,"_IFX_<%=request.getContextPath() %>/script/ifx-ovrReq.js"
,"_IFX_<%=request.getContextPath() %>/script/base64.js"
,"_IFX_<%=request.getContextPath() %>/script/ifx-mswCenter.js"
,"_IFX_<%=request.getContextPath() %>/script/sidemenu/side.js"
,"_IFX_<%=request.getContextPath() %>/script/ifx-easytab.js"
,"_IFX_<%=request.getContextPath() %>/script/ifx-menu-util.js"
,"_IFX_<%=request.getContextPath() %>/script/underscore.js"
,"_IFX_<%=request.getContextPath() %>/env/script/device/ifx-device.js"
,"<%=request.getContextPath() %>/script/alertify/src/alertify.js"

,"<%=request.getContextPath() %>/script/bootstrap-4.5.0/js/bootstrap.js"
,"<%=request.getContextPath() %>/script/bootstrap-submenu-3.0.1/js/bootstrap-submenu.js"
];

function updateVersion() {
	upd(resources);
	function upd(r) {
		var ifxJS = '_IFX_';
		var len = ifxJS.length;
		for ( var i = 0; i < r.length; i++) {
			if (r[i].slice(0, len) == ifxJS) {
				r[i] = r[i].slice(len) + _version;
			}
		}
	}
}
updateVersion();

yepnope({
	load : resources,
	complete : function() {
		fixIE();
		$ = jQuery;
		jQuery(document).ready(function() {
			initTab();
		});
		/* 潘 Jquery UI 與 bootstrap衝突 需增加以下兩行*/
		bootstrapButton = $.fn.button.noConflict();
	    $.fn.bootstrapBtn = bootstrapButton;
	}
});
</script>
</body>
</html>
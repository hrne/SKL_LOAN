<%@page import="com.st1.servlet.GlobalValues"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />
<c:set var="v" value="?_v=2013082509" />

<!--
<link href="<%=request.getContextPath()%>/css/jquery.alerts.css" rel="stylesheet">

<link rel=stylesheet type=text/css
	href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css" />
<link href="<%=request.getContextPath()%>/css/site.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/swift.css" rel="stylesheet">
 -->
</head>
<body
	style="margin: 10px; margin-top: 3px; margin-right: 0px; border: 0">
	<div id='center-part' class="ui-layout-center"
		style="margin: 0; padding: 0">
		<div style='height: 10px;'>		<span id="var_version" style='color: rgba(255, 255, 255, 1)'></span></div>
		<div id='main-container'
			style="display: none; width: 97%; height: 100%">
			<div id="topbuttons"></div>
			<div id="helpArea">this is help</div>
			<div id="tooltipArea">this is tooltip</div>
			<div id="entry-errmsg" style="display: none"></div>
			<div id="entryArea"></div>
			<div id="buttonsArea"></div>
			<div id='queryHeader'></div>
			<div id="queryArea">
        <table id="grid"></table>
				<div id="pager"></div>
				<input type="BUTTON" id="btnBatch" style='display:none;color: blue'
					class='querybutton' value=" 整批處理 " />
				<input type="BUTTON" id="btnSearch" style='display:none;color: rosy'
					class='querybutton' value=" 搜　尋 " /> <input type="BUTTON"
					id="btnReset" style='display:none;color: rosy' class='querybutton'
					value=" 重　設 " /> <input type='button' id='btnExport'
					style='display:none;color: rosy' class='querybutton' data-url='viewgrid.jsp'
					value=' 匯出Excel ' /><input type='button' id='btnExportxml'
					style='display:none;color: rosy' class='querybutton' data-url='viewgrid.jsp'
					value=' 匯出XML ' />
					<input type='button' id='btnMoreData'
						style='display:none;color: blue' class='querybutton' data-url='viewgrid.jsp'
						value=' 擷取更多資料... ' />
					 <input type='button' id='openWin'
					style='display:none;color: blue' class='querybutton' data-url='viewgrid.jsp'
					value=' 在新視窗檢視 ' />
			</div>
			<div id="queryArea2">
        <table id="grid2"></table>
				<div id="pager2"></div>
				<input type="BUTTON" id="btnBatch2" style='display:none;color: blue'
					class='querybutton' value=" 整批處理 " />
				<input type="BUTTON" id="btnSearch2" style='display:none;color: rosy'
					class='querybutton' value=" 搜　尋 " /> <input type="BUTTON"
					id="btnReset2" style='display:none;color: rosy' class='querybutton'
					value=" 重　設 " /> <input type='button' id='btnExport2'
					style='display:none;color: rosy' class='querybutton' data-url='viewgrid.jsp'
					value=' 匯出Excel ' /><input type='button' id='btnExportxml2'
					style='display:none;color: rosy' class='querybutton' data-url='viewgrid.jsp'
					value=' 匯出XML ' />
					<input type='button' id='btnMoreData2'
						style='display:none;color: blue' class='querybutton' data-url='viewgrid.jsp'
						value=' 擷取更多資料... ' />
					 <input type='button' id='openWin2'
					style='display:none;color: blue' class='querybutton' data-url='viewgrid.jsp'
					value=' 在新視窗檢視 ' />
			</div>
			<div id="outputArea"></div>
			<!--小柯增加area2 for clone-->
			<div id="buttonsArea2"></div>
			<div style='height: 100px;'></div>

			<div id="_psbk_fake_passbook" style="display:none">
        	<table class="ui-widget" border="1" style="width:99%">
            	<tbody class="ui-widget-content"></tbody>
        	</table>
    	</div>
    	<div id="_psbk_fyi_dialog" style="display:none">
        	存摺列印中....
		</div>
	</div>



	</div>
	<div id='east-part' style='display: none' class="ui-layout-east">
		<div id="swift_text_panel">
			<div id="swift_text_title">SWIFT電文</div>
			<div id="swift_text_counts_line">
				<span class='swift_text_counts_area'>總字數:<span id="swift_text_counts">0</span></span>
				<span class='swift_text_rest_area'>剩餘字數:<span id="swift_text_rest"></span></span>
			</div>
			<div id="swift_text"></div>
		</div>
	</div>
	<div id="dialog-message" title="Eror"  style='display: none'>
	  <p>
	    <span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
	    <span id="_errtext"></span>
	  </p>

	</div>


	<div id="dialog-locovr" title="本機主管授權"  style='display: none'>
  <fieldset>
    <label for="_superId">帳號</label>
    <input type="text" name="_superId" id="_superId" class="text ui-widget-content ui-corner-all"
    			 maxlength="6"  />

    <label for="_superPassword">密碼</label>

    <input type="password" name="_superPassword" id="_superPassword" value=""
    			class="text ui-widget-content ui-corner-all"
    			 maxlength="20"  />
  </fieldset>

</div>

<div id="dialog-confirm" title="no title" style="display:none;">
  <p>
  	<span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
  	<span id="dialog-confirm-text">還有更多資料，是否繼續?</span>
</p>
</div>
<div id="dialog-swift-code" title="SWIFT Code" style="display:none">
	  <div> 至少先輸入4位字元 </div>
<input type="text" id="_m_swift" size="40" maxlength="30" placeholder="敲幾個字母..."/>
<button id="_btn_clear_swift">Clear</button>
<div id="swift_result" style="margin:5px;display:none">
	<table border='0' style="padding:3px;">
	<tr><td align='right'>SWIFT CODE:</td><td><span id="swift_code"></span></td></tr>
	<tr><td align='right'>BANK NAME:</td><td><span id="swift_bankName"></span></td></tr>
	<tr><td align='right'>Address 1:</td><td><span id="swift_addr1"></span></td></tr>
	<tr><td align='right'>Address 2:</td><td><span id="swift_addr2"></span></td></tr>
	<tr><td align='right'>Address 3:</td><td><span id="swift_addr3"></span></td></tr>
	</table>
	</div>
</div>
	<!--
		<div id="dashboard2"></div>
	<div id="dashboard"></div>
 -->





	<script src="<%=request.getContextPath()%>/script/yepnope.1.5.4-min.js"></script>

	<script>

		var _contextPath = '<%=request.getContextPath()%>';
		//var _version = '$?_v=2013082509';
		<% String s = GlobalValues.jsVersion;%>
		<% String helps = GlobalValues.helpjsVersion;%>
		var _version = "?v=<%=s%>";
		var _helpversion = "?v=<%=helps%>";
		<c:out value="${txReq.jsBlock1}"  escapeXml="false" />

   //20170119 柯:jsFileUrl 為該交易runtime檔案,第一個加載,避免跳出getTranDef未經定義
		var resources = [
                "_IFX_${txReq.jsFileUrl}",
		        "<%=request.getContextPath()%>/script/stacktrace-min-0.4.js", "_IFX_<%=request.getContextPath()%>/script/fixIE.js",
				"<%=request.getContextPath()%>/script/jquery.js", "<%=request.getContextPath()%>/script/jquery.blockUI.js",
				"<%=request.getContextPath()%>/script/jquery.alerts.js", "<%=request.getContextPath()%>/script/json2.js",
				"<%=request.getContextPath()%>/script/underscore.js", "<%=request.getContextPath()%>/script/underscore.string.js",
				"_IFX_<%=request.getContextPath()%>/script/ifx-core2.js", "_IFX_<%=request.getContextPath()%>/script/ifx-fld.js",
				"_IFX_<%=request.getContextPath()%>/script/sheetjs/xls.js",
				"_IFX_<%=request.getContextPath()%>/script/sheetjs/jquery.elastic.source.js",
				"_IFX_<%=request.getContextPath()%>/script/ifx-rtn.js", "_IFX_<%=request.getContextPath()%>/script/ifx-call.js",
				"_IFX_mini/external/Help.js",
				"_IFX_<%=request.getContextPath()%>/script/help/ifx-help-2.js", "_IFX_<%=request.getContextPath()%>/script/ifx-utl.js",
				"_IFX_<%=request.getContextPath()%>/script/ifx-keys.js",
				"_IFX_<%=request.getContextPath()%>/script/host/ifx-ims-host.js",
				"_IFX_<%=request.getContextPath()%>/script/ifx-file2.js",
				"_IFX_<%=request.getContextPath()%>/script/display/ifx-display.js",
				"_IFX_<%=request.getContextPath()%>/script/ifx-psbk.js",
				"_IFX_<%=request.getContextPath()%>/script/shareTool.js",
				"<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js",
				"<%=request.getContextPath()%>/script/jqueryui/js/jquery.ui.datepicker-zh-TW.js",
				"_IFX_<%=request.getContextPath()%>/script/sidemenu/side.js",
				"<%=request.getContextPath()%>/script/jquery.scrollTo-1.4.3-min.js",
				"<%=request.getContextPath()%>/script/jquery.layout/jquery.layout-latest.js",
				"<%=request.getContextPath()%>/script/equals.js", "<%=request.getContextPath()%>/script/jquery.caret.js",
				"<%=request.getContextPath()%>/script/jquery.jticker.js",
				"<%=request.getContextPath()%>/jqgrid/js/grid.locale-tw.js",
				"<%=request.getContextPath()%>/jqgrid/js/jquery.jqGrid.min.js",
				"_IFX_<%=request.getContextPath()%>/script/ifx-grid.js",
				"<%=request.getContextPath()%>/script/async.js/async.js",
				 "_IFX_<%=request.getContextPath()%>/script/ifx-start.js",
				"<%=request.getContextPath()%>/css/jquery.alerts.css",
				"<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css",
				"<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css",
				"<%=request.getContextPath()%>/script/jquery.layout/layout-default-latest.css",
				"_IFX_<%=request.getContextPath()%>/css/site.css",
				
				"<%=request.getContextPath() %>/script/watermark.js",
				"<%=request.getContextPath() %>/script/alertify/src/alertify.js",
				"<%=request.getContextPath() %>/script/alertify/css/alertify.css",
                "<%=request.getContextPath() %>/script/alertify/css/themes/default.css"];

		var cssResources = [ "<%=request.getContextPath()%>/css/jquery.alerts.css",
				"<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css",
				"<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css", "_IFX_<%=request.getContextPath()%>/css/site.css",
				"_IFX_<%=request.getContextPath()%>/css/swift.css", 
				
				"<%=request.getContextPath() %>/script/alertify/css/alertify.css",
		        "<%=request.getContextPath() %>/script/alertify/css/themes/default.css",];

		var swiftResources = [ "_IFX_<%=request.getContextPath()%>/sweet/script/swift-lib.js",
				"_IFX_<%=request.getContextPath()%>/sweet/script/swift-generic.js",
				"_IFX_<%=request.getContextPath()%>/sweet/script/swift-errors.js",
				"_IFX_<%=request.getContextPath()%>/sweet/script/swift-validators.js",
				"_IFX_<%=request.getContextPath()%>/sweet/script/swift-cur.js",
				"_IFX_<%=request.getContextPath()%>/sweet/script/swift-print.js",
				"_IFX_<%=request.getContextPath()%>/css/swift.css", ];

		function updateVersion() {
			upd(resources);
		//	upd(cssResources);
			upd(swiftResources);
			function upd(r) {
				var ifxJS = '_IFX_';
				var len = ifxJS.length;
				for ( var i = 0; i < r.length; i++) {
					if(r[i]==undefined)
						break;
					if (r[i].slice(0, len) == ifxJS) {
						if(r[i].indexOf("external/Help.js") != -1){
							r[i] = r[i].slice(len) + _helpversion;
						}else{
							r[i] = r[i].slice(len) + _version;
						}
					}
				}
			}
		}
		updateVersion();
		//loadCSS();
		function loadCSS() {
			yepnope({
				load : cssResources,
				complete : function() {
					loadJS();
				}
			});
		}
		loadJS();
		function loadJS() {
			yepnope({
				load : resources,
				complete : function() {
					fixIE();
					$ = jQuery;
					jQuery(document).ready(function() {
						$.jgrid.no_legacy_api = true;
						$.jgrid.useJSON = true;
						//20170119 柯:延遲呼叫
							setTimeout(function() {
								starting_point();
							}, 0);
					});
				}
			});
		}
	</script>

</body>
</html>
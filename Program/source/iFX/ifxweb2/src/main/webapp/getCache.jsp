<%@page import="com.st1.servlet.GlobalValues" %>
<!DOCTYPE html>
<HTML>
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <HEAD>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>快取檢測中...</title>
    <!--
    <c:set var="context" value="http://192.168.10.6:9080/ifx" />
    <c:set var="script" value="http://192.168.10.6:9080/ifx/script" />
    <c:set var="css" value="http://192.168.10.6:9080/ifx/css" />
    -->
    <c:set var="context" value="${pageContext.request.contextPath}" />
    <c:set var="script" value="${pageContext.request.contextPath}/script" />
    <c:set var="css" value="${pageContext.request.contextPath}/css" />
    <link href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" rel="stylesheet">
    <link href="script/easyui/themes/default/easyui.css" rel="stylesheet">
    <link rel="stylesheet" href="script/loader/jquery.percentageloader-0.1.css">
    <link rel=stylesheet type=text/css href="css/menu.css" />
    <script>
        //window.resizeTo(0,0);   //window.resizeTo(0,0)設置大小
        //window.moveTo(0,window.screen.availHeight-30)設置窗口位置
        window.moveTo(0, 0 - window.screen.availHeight);
    </script>
    <script src="script/jquery.js"></script>
    <script src="script/jquery.validate.js"></script>
    <script src="script/loader/jquery.percentageloader-0.1.min.js"></script>
    <script src="script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
    <script src="script/jquery.pulse.min.js"></script>
    <script src="script/ifx-menu-util.js"></script>
    <script src="script/jquery.cookie.js"></script>
    <script src="script/ifx-prt.js"></script>
    <script src="script/ifx-global.js"></script>
    <script src="script/ifx-dup.js"></script>
    <script type='text/javascript' src='dwr/engine.js'></script>
    <script type='text/javascript' src='dwr/interface/MswCenter.js'></script>
    <script type='text/javascript' src='dwr/util.js'></script>
    <script type="text/javascript" src="script/countdown/jquery.countdown.min.js"></script>
    <script type='text/javascript' src='script/ticker/jquery.vticker.min.js'></script>
    <script src="script/jqueryui/js/dialogOnly/jquery-ui-1.10.3.dlg_btn.min.js"></script>
    <script src="script/sidemenu/side.js"></script>
    <script src="script/yepnope.1.5.4-min.js"></script>
    <style>
      .lds-ring {
      	display: inline-block;
      	position: absolute;
      	/*絕對位置*/
      	top: 50%;
      	/*從上面開始算，下推 50% (一半) 的位置*/
      	left: 50%;
      	/*從左邊開始算，右推 50% (一半) 的位置*/
      	margin-top: -100px;
      	/*高度的一半*/
      	margin-left: -100px;
      	/*寬度的一半*/
      	width: 80px;
      	height: 80px;
      }
      .lds-ring div {
      	box-sizing: border-box;
      	display: block;
      	position: absolute;
      	width: 100px;
      	height: 100px;
      	margin: 8px;
      	/*
      	border: 16px solid #cd1919;
      	*/
      	border: 16px solid #a48686;
      	border-radius: 50%;
      	animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
      	border-color: #a48686 transparent transparent transparent;
      }
      .lds-ring div:nth-child(1) {
      	animation-delay: -0.45s;
      }
      .lds-ring div:nth-child(2) {
      	animation-delay: -0.3s;
      }
      .lds-ring div:nth-child(3) {
      	animation-delay: -0.15s;
      }
      @keyframes lds-ring {
      	0% {
      		transform: rotate(0deg);
      	}
      	100% {
      		transform: rotate(360deg);
      	}
      }
    </style>
  </HEAD>
  <BODY>
          <div id="topLoader">
          </div>
          <div class="lds-ring"><div></div><div></div><div></div><div></div></div>

          <!-- <button id="animateButton">Animate Loader</button>-->

          <script>
              var _contextPath = '<%=request.getContextPath() %>'; <%
                  String s = GlobalValues.jsVersion;
              String helps = GlobalValues.helpjsVersion;

              String sysvar = (String) session.getAttribute(GlobalValues.SESSION_SYSVAR);
              out.println("\n var _sysvar=" + sysvar);

               %>
//              var $topLoader = $("#topLoader").percentageLoader({
//                  width: 300,
//                  height: 300,
//                  controllable: false,
//                  progress: 0,
//                  onProgressUpdate: function (val) {
//                      //$topLoader.setValue(Math.round(val * 100.0));
//                  }
//              });
              var count = 0;
              var totalCount = 45;
//              $topLoader.setValue("檢測中..");

              var animateFunc = function () {
                  count += 1;
//                  $topLoader.setProgress(count / totalCount);
                  //$topLoader.setValue(count.toString() );//+ 'count'
                  if (count >= totalCount) {
//                      window.returnValue = true;
//                      window.close();
                       location = "easy_main.jsp";
                  }
              }

              var _version = "?v=<%=s%>";
              var _helpversion = "?v=<%=helps%>";
              var resources1 = [
                  "_IFX_<%=request.getContextPath() %>/script/ifx-utl.js", "_IFX_<%=request.getContextPath() %>/script/ifx-ovrReq.js", "_IFX_<%=request.getContextPath() %>/script/base64.js", "_IFX_<%=request.getContextPath() %>/script/ifx-mswCenter.js", "_IFX_<%=request.getContextPath() %>/script/sidemenu/side.js", "_IFX_<%=request.getContextPath() %>/env/script/device/ifx-device.js", "_IFX_<%=request.getContextPath() %>/env/script/ifx-env-check1.js"
              ];
              var resources = [
                  "<%=request.getContextPath() %>/script/stacktrace-min-0.4.js", "_IFX_<%=request.getContextPath() %>/script/fixIE.js",
                  "<%=request.getContextPath() %>/script/jquery.blockUI.js",
                  "<%=request.getContextPath() %>/script/jquery.alerts.js", "<%=request.getContextPath() %>/script/json2.js",
                  "<%=request.getContextPath() %>/script/underscore.js", "<%=request.getContextPath() %>/script/underscore.string.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-core2.js", "_IFX_<%=request.getContextPath() %>/script/ifx-fld.js",
                  "_IFX_<%=request.getContextPath() %>/script/sheetjs/xls.js",
                  "_IFX_<%=request.getContextPath() %>/script/sheetjs/jquery.elastic.source.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-rtn.js", "_IFX_<%=request.getContextPath() %>/script/ifx-call.js",
                  "_IFX_mini/external/Help.js",
                  "_IFX_<%=request.getContextPath() %>/script/help/ifx-help-2.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-keys.js",
                  "_IFX_<%=request.getContextPath() %>/script/host/ifx-ims-host.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-file2.js",
                  "_IFX_<%=request.getContextPath() %>/script/display/ifx-display.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-psbk.js",
                  "_IFX_<%=request.getContextPath() %>/script/shareTool.js",
                  "<%=request.getContextPath() %>/script/jqueryui/js/jquery.ui.datepicker-zh-TW.js",
                  "<%=request.getContextPath() %>/script/jquery.scrollTo-1.4.3-min.js",
                  "<%=request.getContextPath() %>/script/jquery.layout/jquery.layout-latest.js",
                  "<%=request.getContextPath() %>/script/equals.js", "<%=request.getContextPath() %>/script/jquery.caret.js",
                  "<%=request.getContextPath() %>/script/jquery.jticker.js",
                  "<%=request.getContextPath() %>/jqgrid/js/grid.locale-tw.js",
                  "<%=request.getContextPath() %>/jqgrid/js/jquery.jqGrid.src.js",
                  "<%=request.getContextPath() %>/jqgrid/js/jquery.jqGrid.min.js",
                  "<%=request.getContextPath() %>/script/easyui/jquery.easyui.min.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-grid.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-easytab.js",
                  "_IFX_<%=request.getContextPath() %>/script/ifx-start.js",
                  "<%=request.getContextPath() %>/css/jquery.alerts.css",
                  "<%=request.getContextPath() %>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css",
                  "<%=request.getContextPath() %>/jqgrid/css/ui.jqgrid.css",
                  "<%=request.getContextPath() %>/script/jquery.layout/layout-default-latest.css",
                  "_IFX_<%=request.getContextPath() %>/css/site.css",
                  
                  "<%=request.getContextPath() %>/script/alertify/src/alertify.js",
                  "<%=request.getContextPath() %>/script/alertify/css/themes/alertify.core.css",
                  "<%=request.getContextPath() %>/script/alertify/css/themes/alertify.default.css"
              ];

              var swiftResources = ["_IFX_<%=request.getContextPath() %>/sweet/script/swift-lib.js",
                  "_IFX_<%=request.getContextPath() %>/sweet/script/swift-generic.js",
                  "_IFX_<%=request.getContextPath() %>/sweet/script/swift-errors.js",
                  "_IFX_<%=request.getContextPath() %>/sweet/script/swift-validators.js",
                  "_IFX_<%=request.getContextPath() %>/sweet/script/swift-cur.js",
                  "_IFX_<%=request.getContextPath() %>/sweet/script/swift-print.js",
                  "_IFX_<%=request.getContextPath() %>/css/swift.css",
              ];

              function updateVersion1() {
                  upd(resources1);

                  function upd(r) {
                      var ifxJS = '_IFX_';
                      var len = ifxJS.length;
                      for (var i = 0; i < r.length; i++) {
                          if (r[i].slice(0, len) == ifxJS) {
                              r[i] = r[i].slice(len) + _version;
                          }
                      }
                  }
              }


              function updateVersion() {
                  upd(resources);
                  //	upd(cssResources);
                  upd(swiftResources);

                  function upd(r) {
                      var ifxJS = '_IFX_';
                      var len = ifxJS.length;
                      for (var i = 0; i < r.length; i++) {
                          if (r[i] == undefined)
                              break;
                          if (r[i].slice(0, len) == ifxJS) {
                              if (r[i].indexOf("external/Help.js") != -1) {
                                  r[i] = r[i].slice(len) + _helpversion;
                              } else {
                                  r[i] = r[i].slice(len) + _version;
                              }
                          }
                      }
                  }
              }

              function loadJS() {
                  yepnope(
                      [{
                          load: resources1,
                          callback: function (url, result, key) {
                              setTimeout(animateFunc, 25)
                          },
                          complete: function () {
                              console.log("第一階段儲存快取完成!");
                          }
                      }, {
                          load: resources,
                          callback: function (url, result, key) {
                              setTimeout(animateFunc, 25)
                          },
                          complete: function () {
                              jQuery(document).load(function () {
                                  console.log("jQuery(document).load");
                                  console.log("第二階段儲存快取完成!");
                                  //alert("儲存快取完成!");
                                  //window.returnValue = true;
                                  //window.close();
                              });
                          }
                      }]
                  );
              }
              updateVersion1();
              updateVersion();
              loadJS();
          </script>
  </BODY>
</HTML>
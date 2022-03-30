<%@page import="com.st1.util.PoorManUtil"%>
<%@page import="com.st1.def.menu.MenuBuilder"%>
<%@page import="com.st1.servlet.GlobalValues"%>    
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	 <%@include file="/env/myDevice.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<c:set var="context" value="${pageContext.request.contextPath}" />
<title> 批次報表 </title>

<link href="css/site.css" rel="stylesheet">
      

<style type=text/css>
  li.ui-state-default {
    font-size : 10px;
  }
  div.ui-tabs-panel {
    font-size : 10px;
    font-family : arial;
	color:blue;
	line-height: 100%;
  }
   

.tabcontent{
	color:blue;
}


div.ui-tabs-panel a {
	text-decoration:none;
	color:SpringGreen;
	padding:2px;
}

myclass {
	color:red;
}
span.label {
	/*background-color:LightGoldenRodYellow ;
	color:MidnightBlue ;  */
	color:white; 
	padding:2px;
}

#batch_head {
margin:5px;
padding:2px;
/*vertical-align:middle;
position:fixed;*/
position:fixed;
top:2px;
left:5px;
background-color:green ;
}

#result {
	//background-color:lightyellow ;
	color:black;
	width:auto;
	
	font-family:細明體;  
	font-size: 11px;
	margin-top:1px;
	margin-left:1px;
	padding:px;
	border: 1;
}
#result hr {
	height:1px;
	color:blue;
	background-color:gray;
}
#result pre {
	font-family: "Courier New", Courier,新細明體,	
                    monospace;
	color:red;
}

</style>
<script src="script/yepnope.1.5.4-min.js"></script>
<script>
<%
// check login session

	String fileBrno = StringEscapeUtils.escapeXml10(request.getParameter("filebrno"));
    if(fileBrno==null) fileBrno = "";
	
	String fileTlrno = StringEscapeUtils.escapeXml10(request.getParameter("filetlrno"));
	if(fileTlrno==null) fileTlrno = "";	
	
	String fileName = StringEscapeUtils.escapeXml10(request.getParameter("filename"));
	if(fileName==null) fileName = "";		
	
    String gowhere = StringEscapeUtils.escapeXml10(request.getParameter("gowhere"));
	if(gowhere==null) gowhere = "";	
	
	String sysvar = (String) session.getAttribute(GlobalValues.SESSION_SYSVAR);
	out.println("\n var _sysvar=" + sysvar);
	
	String dt = StringEscapeUtils.escapeXml10(request.getParameter("dt"));
	
	String startLine = StringEscapeUtils.escapeXml10(request.getParameter("startline"));
	if(startLine==null) startLine = "0";
	
	String sCount = StringEscapeUtils.escapeXml10(request.getParameter("count"));
	if(sCount==null) sCount = "300";

	String sPagenum = StringEscapeUtils.escapeXml10(request.getParameter("pagenum"));
	if(sPagenum==null) sPagenum = "0";
%>
		<% String s = GlobalValues.jsVersion;%>
		var _version = "?v=<%=s%>";		

		var resources = [
		    "_IFX_script/fixIE.js",
				"script/jquery.js", "script/jquery.blockUI.js",
			  "_IFX_script/ifx-utl.js",
			  "_IFX_env/script/device/ifx-device.js",
				"_IFX_script/batch_report.js",
				"_IFX_script/ifx-file2.js" ];
				
		function updateVersion() {
			upd(resources);
			function upd(r) {
				var ifxJS = '_IFX_';
				var len = ifxJS.length;
				for ( var i = 0; i < r.length; i++) {
					if(r[i]==undefined)
						break;
					if (r[i].slice(0, len) == ifxJS) {
							r[i] = r[i].slice(len) + _version;
					}
				}
			}
		}
				updateVersion();
		loadJS();
		function loadJS() {
			yepnope({
				load : resources,
				complete : function() {
					$ = jQuery;
					jQuery(document).ready(function() {
	var xyz = <%=dt%>;
	if(xyz == null){
    $("#dt").val(_sysvar['FDATE']);
  }
					});
				}
			});
		}
</script> 
</head>
<body>
<div id="batch_head">
	<!--無用,都已在savedoc給-->
<input type="hidden" id="filebrno" value='<%=fileBrno%>'/>
<input type="hidden" id="filetlrno" value='<%=fileTlrno%>'/>
<input type="hidden" id="gowhere" value='<%=gowhere%>'/>
<span class='label' >報表名稱</span>
<input type="text" id="filename" size="20" maxlength="20" value='<%=fileName%>'/>
<span class='label'>日期</span>
<input type="text" id="dt" size="8"	maxlength="8" value='<%=dt%>'/>
<span class='label' style="display:none" >起始行</span>
<input type="text" style="display:none" id="startline" size="4" maxlength="4" value='<%=startLine%>'/>
<span class='label'style="display:none"  >讀取行數</span>
<input type="text" style="display:none" id="count" size="4" maxlength="4" value='<%=sCount%>'/>
<span class='label' >讀取頁數</span>
<input type="text" id="pagenum"  readonly="readonly"  size="4" maxlength="4" value='<%=sPagenum%>'/>
<!--<input type="button" class='button' style="display:none" id="btnReportup" value="上一頁"/>	-->
<input type="button" class='button' style="display:none" id="btnReportpageup" value="上一頁"/>	
<input type="button" class='button' id="btnReportpage" value="讀取頁次"/>	
<input type="button" style="display:none"  class='button' id="btnReportdown" value="送出"/>	
<input type="button" class='button' id="btnWinPrint" value="當頁列印"/>	
<input type="button" class='button' id="btnWinPrintall" value="全部列印"/>	
</div>
<div id="result">
</div>
</body>
</html>
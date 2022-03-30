<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.ifx.hcomm.app.SessionMap"%>
<!DOCTYPE html>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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


<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
com.st1.msw.UserInfo user = (com.st1.msw.UserInfo)session.getAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO);
if(user == null){
	out.println("<h2>error:查無登入資訊, 請先登入系統</h2>");
	out.flush();
	return;
}
		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		System.out.println(sessionMap.get("DAPKND"));
		System.out.println(sessionMap.get("OAPKND"));
//System.out.println(user.getBrno());
//System.out.println("basePath:"+basePath);
//System.out.println("path:"+path);


java.util.LinkedHashMap<String,String> apps = new java.util.LinkedHashMap<String,String>();
//值,顯示
apps.put("XR","XR");
apps.put("XG","XG");
com.google.gson.Gson gson = new com.google.gson.Gson();
String catAndDogJson =  gson.toJson(apps);
System.out.println(catAndDogJson);
%>



  <head>
    <base href="<%=basePath%>">
    
    <link rel=stylesheet type=text/css
			href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css" />
	
    <title>ifx report explorer</title>
    
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
    <h1> 批次報表列印</h1>
    <h3> 分行: <%=user.getBrno()%> 　 <%=user.getName()%> </h3>
    <!--//TODO: 依照權限來限制能選擇之業務
    <h3>DAPKND:<%=sessionMap.get("DAPKND")%></h3>
    <h3>OAPKND權限:<%=sessionMap.get("OAPKND")%></h3>-->
    </div>
    <div>
      業務 <div id="radios"></div>
    <p>選擇日期 <input type="text" id="datepicker">
       <!-- 業務大類:<select id="apps"></select> -->
    	<button id="btnFind">搜尋</button><span id="status1"></span>
    </p>
    </div>
    <div id="result" style="display:none">
    	<table id="grid"></table>
    	<button id="btnPrint">列印</button>
    
    </div>
    
    
    
    <script src="<%=request.getContextPath()%>/script/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/grid.locale-tw.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/jquery.jqGrid.min.js"></script>
    <script src="<%=request.getContextPath()%>/script/async.js/async.js"></script>

     <script src="ifx-report/browse-report.js"></script>

    <script>
    var _contextPath = '<%=request.getContextPath()%>';
    var optionJson = <%=catAndDogJson%>;
    $(function(){
    	var $radios = $('#radios');
    	var i=0;
    	for(var k in optionJson) {
    		var rid = "radio" + i++;
    		var n = optionJson[k];
    		var $r = $('<input type="radio"  name="radio" id="' + rid + '"><label for="' +rid + '">' + n +'</label>');
    		$r.attr('data-val',k).attr('data-text', n);
    		$radios.append($r);
    	}
    	$radios.buttonset();
    
    
    	  var dt = new Date();	
    	  var $dp = $( "#datepicker" ).datepicker({
    	  	dateFormat: 'yy-mm-dd'
    	  });
    	  $dp.datepicker("setDate", dt);
  		
  		/*
  		  var pp = [];
  		  for(var k in optionJson) pp.push({value:k,text:optionJson[k]});
  		  var $apps = $('#apps').html($.map(pp, function(x){
			var $opt= $('<option/>', {value:x.value, text: x.text});
			return $opt;
		
		}));  //.attr("size", ss.length);
  		*/
  		var reportBrowser= ifxReport.browser;
  		reportBrowser.init(_contextPath,'#result');
  		
  		var $status = $('#status1').empty();
  		$( "#btnFind" ).click(function(){
  			$status.empty();
  			var $r = $("#radios input[type='radio']:checked");
  			var app = $r.attr('data-val');
  			var appName =  $r.attr('data-text');
  			if(!app) {
  				errmsg("請選擇業務");
  				return;
  			}
  		
   			reportBrowser.getReports($dp.val(), app, appName, function(x){
   			 	errmsg(x);
   			});
   			
   			function errmsg(x){
   				var $li = $("<li/>").text(x).css("color",'red'); 
   				$status.append($li);
   			}
   		});
   		
    });
 
   

    </script>
  </body>
</html>

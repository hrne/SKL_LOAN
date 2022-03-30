<%@page import="com.st1.servlet.GlobalValues"%>
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
	
	
    <title>swift printer setup</title>
    
<style>
	#main div {
		margin:20px;
	}
  #mtArea { width:90%;}
  #mtArea div { 
  	
  	width:90%;
  	margin:10px;
  	line-height:150%;
  	background-color:Azure;
  	padding:10px;
  	border-radius: 10px;
  }
   
   #mtArea div span {
   	margin:5px;
  	display: inline-block;
  	text-transform:uppercase;
   }
   #mtArea div span input[type=checkbox]:checked + label {
   		color: blue ;
   		background-color:GreenYellow ;
  
   }
   
   #mtArea div:nth-child(odd){
   color:ForestGreen  ;
    	background-color:MistyRose ;
    }
    #mtArea div:nth-child(even){
    	
    	    	color:black ;
    }
  </style>
  </head>
  
  <body>
  	<div> 
    Swift printer setup for branch:<%=user.getBrno()%>
    </div>
   	<div id="main">
   		<div>首席印表機:
   			<input id="priPrinter" size="30">
   			<select id="primaryPrinters"></select></div>
   		<div>另類印表機:
   				<input id="altPrinter" size="30">
   		      <select id="altPrinters"></select></div>
   		<div>您打算指定那些SWIFT電文列印至另類印表機?</div>
   		<div id="mtArea"></div>
   		<button id="btnSave">儲存</button>	
   	</div>
    
    
    
    <script src="<%=request.getContextPath()%>/script/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/grid.locale-tw.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/jquery.jqGrid.min.js"></script>
    <script src="<%=request.getContextPath()%>/script/async.js/async.js"></script>
    <script src="swift-auto/swift-printer.js"></script>
   	<script src="swift-auto/swift-setup.js"></script>
    <script>
    var _contextPath = '<%=request.getContextPath()%>';
    
    $(function(){
    	_getPrinters("myPrintersCallback");
    	setTimeout(startRun123, 1000);
    });
    function myPrintersCallback(){
    	var printers = Array.prototype.slice.call(arguments);
    	printers.unshift("--select one printer--");
		
		$.each(printers, function(val, text) {
            $('#primaryPrinters').append( $('<option></option>').val(val).html(text) )
        }); 
		$('#altPrinters').html($('#primaryPrinters').html());
		
		
    }
    var _swfSetup;
    function startRun123(){
    	_swfSetup =  SwiftAuto.setup;
    	_swfSetup.init(_contextPath, "main");
    	_swfSetup.getPrinterDef();
}
    </script>
  </body>
</html>

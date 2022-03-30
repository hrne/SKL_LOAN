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

    <title>swift report explorer</title>

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
    Swift report explorer for branch:<%=user.getBrno()%>
    </div>
    <div>
    <p>選擇日期: <input type="text" id="datepicker">
    	<button id="btnFind">Go</button><span id="status1"></span>
    </p>
    </div>
    <div id="result" style="display:none">
    	<table id="grid"></table>
    	<button id="btnPrint">列印到預先指定印表機</button>
    	<div>
    		<button id="btnPrintTo">列印到你喜歡的印表機</button> :
    		<select id="printers"></select>
    	</div>
    </div>



    <script src="<%=request.getContextPath()%>/script/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/grid.locale-tw.js"></script>
    <script src="<%=request.getContextPath()%>/jqgrid/js/jquery.jqGrid.src.js"></script>
    <script src="<%=request.getContextPath()%>/script/async.js/async.js"></script>
   	<script src="swift-auto/swift-printer.js"></script>
    <script src="swift-auto/swift-browse.js"></script>

    <script>
    var _contextPath = '<%=request.getContextPath()%>';
    var _swf;
    $(function(){
    	_swf = SwiftAuto.browse;
    	_swf.init(_contextPath, '#result');

    	  var dt = new Date();
    	  var $dp = $( "#datepicker" ).datepicker({
    	  	dateFormat: 'yy-mm-dd'
    	  });
    	  $dp.datepicker("setDate", dt);

  		$( "#btnFind" ).click(function(){
  			var $status = $('#status1').empty();
   			_swf.findByDate($dp.val(), function(x){
   			 	var $li = $("<li/>").text(x).css("color",'red');
   				$status.append($li);
   			});
   		});
   		_swf.setPrinter(_printers);
   		_getPrinters("myPrintersCallback");
    });

   function myPrintersCallback(){
   		var args = Array.prototype.slice.call(arguments);
		var p = _buildPrinters(args);
   		$('#printers').html($.map(p.list, function(x){
			var $opt= $('<option/>', {text: x});
			if (x==_printers.defaultPrinter) {
				$opt.attr("selected", "true");
			}
			return $opt;

		}));//.attr("size", ss.length);

	}


    </script>
  </body>
</html>

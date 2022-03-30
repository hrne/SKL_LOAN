<%@page import="com.st1.servlet.GlobalValues"%>
<!DOCTYPE html>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width,initial-scale=1">

<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />
<c:set var="v" value="?_v=2013082509" />


<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String addr = request.getRemoteAddr();  //127.0.0.1
	com.st1.msw.UserInfo user = (com.st1.msw.UserInfo) session
			.getAttribute(com.st1.servlet.GlobalValues.SESSION_USER_INFO);
	//System.out.println(user.getBrno());
	//System.out.println("basePath:" + basePath);
	//System.out.println("path:" + path);
%>

<head>
<base href="<%=basePath%>">

<link rel=stylesheet type=text/css
	href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jqgrid/css/ui.jqgrid.css" />


<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/swift-auto/bootstrap/css/bootstrap.css">

<!--[if lt IE 9]>
            <script src="js/html5shiv.js"></script>
            <script src="js/respond.min.js"></script>
        <![endif]-->
<title>swift printer setup</title>

<style>


#mtArea div {
	margin: 5px;
	line-height: 140%;
	background-color: Azure;
	
	padding-bottom: 10px;
	border-radius: 10px;
}

#mtArea div span {
	margin: 5px;
	display: inline-block;
	text-transform: uppercase;
}

#mtArea div span input[type=checkbox]:checked+label {
	color: blue;
	background-color: GreenYellow;
}

#mtArea div:nth-child(odd) {
	color: ForestGreen;
	background-color: MistyRose;
}

#mtArea div:nth-child(even) {
	color: black;
}
</style>
</head>

<body>
	<div class="container">

		<div class="page-header">
			<h1>
				Swift自動列印設定 <span class="label  label-success"><%=user.getBrno()%>分行</span>
			</h1>
		</div>
		<div id="main">
		<div class="modal fade" id="myPrintersModal" role="dialog">
		    <div class="modal-dialog">
		    
		      <!-- Modal content-->
		      <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal">&times;</button>
		          <h4 class="modal-title">印表機清單</h4>
		        </div>
		        <div class="modal-body">
		          	<select	id="printers"  class="form-control"></select>
		        </div>
		        <div class="modal-footer">
		         <button type="button" id="btnApplyPriner" class="btn btn-danger" data-dismiss="modal">Apply</button>
		          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        </div>
		      </div>
		      
		    </div>
		  </div>
		  
			<div id="divErrmsg" style="display:none" class="alert alert-danger"></div>
			
			 <form class="form-horizontal" role="form">
			 <div class="form-group">
				<label for="priAllowip"  class="control-label text-center col-sm-2">允許電腦ip</label>
				<div class="col-sm-7"> <!-- 空值時預設local ip -->
				 	<input id="priAllowip"  class="form-control">
				 </div>
				 <label for="showAddrmsg" id="showAddrmsg"  class="control-label text-center col-sm-2"></label>
			</div>
			<div class="form-group">
				<label for="priPrinter"  class="control-label text-center col-sm-2">來電印表機</label>
				<div class="col-sm-7">
				 	<input id="priPrinter"  class="form-control">
				 </div>
				 <a title="按我顯示印表機清單" class="printerChooser btn btn-success col-sm-1"><span class="glyphicon glyphicon-print">瀏覽</a>
				 	 <!-- Modal -->
  
				 <!-- 
				 <select  class="form-control"
					id="primaryPrinters"></select>
					 -->
			</div>
			<div class="form-group">
				<label for="ackPrinter"  class="control-label text-center col-sm-2">ACK印表機</label>
				<div class="col-sm-7">
				 	<input id="ackPrinter"  class="form-control">
				 </div>
				 <a title="按我顯示印表機清單" class="printerChooser btn btn-success col-sm-1"><span class="glyphicon glyphicon-print">瀏覽</a>
				 	 <!-- Modal -->
  
				 <!-- 
				 <select  class="form-control"
					id="primaryPrinters"></select>
					 -->
			</div>
			<div class="form-group">
				<label for="nakPrinter"  class="control-label text-center col-sm-2">NAK印表機</label>
				<div class="col-sm-7">
				 	<input id="nakPrinter"  class="form-control">
				 </div>
				 <a title="按我顯示印表機清單" class="printerChooser btn btn-success col-sm-1"><span class="glyphicon glyphicon-print">瀏覽</a>
				 	 <!-- Modal -->
  
				 <!-- 
				 <select  class="form-control"
					id="primaryPrinters"></select>
					 -->
			</div>
			<div class="form-group">
				<label for="altPrinter"  class="control-label col-sm-2">電信科指定印表機</label>
				  <div class="col-sm-7">
				<input id="altPrinter" class="form-control">
				</div>
				 <a 
				 	title="按我顯示印表機清單" class="printerChooser btn btn-success col-sm-1"><span class="glyphicon glyphicon-print">瀏覽</a>
				<!--  
				<select	id="altPrinters"  class="form-control"></select>
				 -->
			</div>
			</form>
			<div class="well  well-md">
			<h3>您打算指定那些SWIFT電文列印至<div class="label label-info">指定印表機</div>?  (電信科用)</h3>
			<div id="mtArea" class="row"></div>
 			</div>
			<div>
				<button id="btnSave" class="btn btn-danger btn-sm">儲存</button>
			</div>
		
		</div>
		
		
	</div>
	<div style="height:20px"></div>


	<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
	<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="<%=request.getContextPath()%>/jqgrid/js/grid.locale-tw.js"></script>
	<script src="<%=request.getContextPath()%>/jqgrid/js/jquery.jqGrid.src.js"></script>
	<script src="<%=request.getContextPath()%>/script/async.js/async.js"></script>
	<script src="<%=request.getContextPath()%>/swift-auto/bootstrap/js/bootstrap.min.js"></script>
	<script src="swift-auto/swift-printer.js"></script>
	<script src="swift-auto/swift-setup.js"></script>
	<script>
		var _contextPath = '<%=request.getContextPath()%>';
		var _addrip = "<%=addr%>";//潘  2017/11/23
		var __selectedPrinter = null;
		$(function() {
			$('[data-toggle="tooltip"]').tooltip();   
			$(".printerChooser").on('click', choosePrinter);
			//_getPrinters("myPrintersCallback");
			setTimeout(startRun123, 1);
		});
		function choosePrinter() {
			var $that = $(this); // that points to button
			var $thatField = $that.prev().find('input');
			__selectedPrinter = $thatField.val();
			var $printers = $('#printers');
			
			//var $this = $(this);
			_getPrinters("myPrintersCallback");
			
			$('#btnApplyPriner').off('click').on('click', function(){
				if($printers.val()==0)
					return false;
				
				var t = $('option:selected', $printers).text();
				$thatField.val(t);
			});
			$("#myPrintersModal").modal();
		}
		function myPrintersCallback() {
			var printers = Array.prototype.slice.call(arguments);
			printers.unshift("--選一台吧--");
			var $printers = $('#printers').empty();
			$.each(printers, function(val, text) {
				$printers.append(
						$('<option></option>').val(val).html(text))
			});
			$printers.attr("size", printers.length);
			
			$.each(printers, function(val, text) {
				if(text== __selectedPrinter) {
					$printers.val(val);
					return false;
				}
			});
			
		}
		var _swfSetup;
		function startRun123() {
			_swfSetup = SwiftAuto.setup;
			_swfSetup.init(_contextPath, "main",_addrip);
			_swfSetup.getPrinterDef(_addrip);
			
		}
	</script>
</body>
</html>

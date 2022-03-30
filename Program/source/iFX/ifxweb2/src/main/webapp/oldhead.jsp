<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<%@page import="com.st1.servlet.GlobalValues"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ifx</title>
<link href="css/site.css" rel="stylesheet">
<script src="script/jquery.js"></script>
<script src = "script/jqueryui/js/jquery-ui-1.8.19.custom.min.js"></script>

<link rel=stylesheet type=text/css
      href="script/jqueryui/css/smoothness/jquery-ui-1.8.19.custom.css" />
      
      <!-- 因為tran2.jsp以$.load載入此頁, 若這邊不含入blockui, 則tran2.jsp之blockUI失效 -->
      <script src="script/jquery.blockUI.js"></script>
<script>
<%
String sysvar = (String) session.getAttribute(GlobalValues.SESSION_SYSVAR);
out.println("var _sysvar=" + sysvar);
%>
	$(function() {
		initValues();
		initHeader();
		handle_txcode();
	});
	function initValues() {
		$("#brn").text(_sysvar['BRN']);
		$("#radd").text(_sysvar['RADD']);
	}
	function handle_txcode() {
		$('#txcode').change(function() {
			var s = $(this).val().toUpperCase();
			var u = "tran2.jsp?txcode=" + s;
			change_src(u);
		}).keypress(function(){
			var s = $(this).val();
			if(s.length==5) $(this).trigger("change");
		});
		
	}
	function initHeader() {
		$("[id^=toolbar_btn]").button();
		//$("[id^=toolbar_btn]").click(function(){
		//	var s = parent.frames["work"].location.href;
			//alert($(this).text());
			//alert($(this).attr("data-url"));
		//	change_src($(this).attr("data-url")); 
	//	});

		$("#toolbar_btn_logoff").click(function(){
		    var r=confirm("確定要登出?");
			if(r)
				top.window.location = "logoff.jsp";
		});
		
		$("#toolbar_btn_dupreciept").click(function(){
		    openDupWindow();
		});
		
		function openDupWindow() {  
			var width = 900;  
			var height = 650;  
			var url = "dupdoc.jsp";  
			window.showModalDialog(url, window, 'dialogWidth=' + width + 'px;dialogHeight=' + height + 'px;resizable=yes;help=no;center=yes;status=no;scroll=yes;edge=sunken');                  
		}
		
		$("#toolbar_btn_switch").click(function(){
			var u = top.window.location.toString();
			if(u.indexOf('easy_') >0) u = u.replace('easy_','');
			else u=u.replace('main', 'easy_main');
			top.window.location = u;
		});
		
	}
	function change_src(u) {
		window.parent.go(u);
		//window.parent.frames["work"].location = u;
		//$('#work').attr({ src: u }); 

		
	}
</script>      
</head>
<body class="headclass">


<div id="container">
	<div id="logo"><img src="images/ifx_logo.png"/></div>
	<div id="userinfoArea">branch:<span id='brn'></span> user:<span id='radd'></span></div>
	<div id="talk" style="width:20px;height:20px">
		<iframe src="message.jsp"></iframe>
	</div>
	<div id="toolbarArea">
	<!-- 
		<span id="toolbar_btn_menu" data-url="menu.jsp" style="color:green"> Menu </span>&nbsp;&nbsp;&nbsp;
 -->		
 	<span id="toolbar_btn_switch" data-url="none" style="color:blue"> Switch UI </span>
 	<span id="toolbar_btn_dupreciept" data-url="none" style="color:green"> Dup Reciept </span>
		<span id="toolbar_btn_logoff" data-url="logoff.jsp" style="color:red"> Logoff </span>
		<span id="toolbar_btn_about" data-url="about.jsp"> About </span>
		
	</div>
	<!-- 
	<div id="txcodeArea">Tran Code&nbsp;<input type="text" id="txcode" size="5" maxlength="5"></div>
	 -->
</div>

 </body>
</html>
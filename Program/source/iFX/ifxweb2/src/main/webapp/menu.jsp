<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.def.menu.MenuBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/site.css" rel="stylesheet">
<script src="script/jquery.js"></script>
<script src = "script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="script/sidemenu/side.js"></script>
<link rel=stylesheet type=text/css
      href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />
<link rel=stylesheet type=text/css    href="css/menu.css" />      
<script>
$(function(){
	initMenu();
	initSideMenu();
});
function initSideMenu() {
	sidemenu.init('dashboard2',{top:42, growTo:200}, function(){
		sidemenu.enableAll(false);
		sidemenu.enable("dupprint");
		sidemenu.enable("logoff");
		sidemenu.enable("uiswitch");
	});
}
function initMenu(){
	var bg = "rgba(250, 211, 208, 1)";
	$("[id^=tab]").css("background-color", bg);
	//$("#tabs").tabs ();
	var tx = ["A","B"];
	$("#tabs").tabs ({
		  fx : { opacity : "toggle" },
	});
	$(".menuTable td a").click(function() {
		var s =$(this).text();
		var url = "tran2.jsp?txcode=" + s.slice(0,5);
		window.location = url;
		
		
	}).hover(function(){
	 	$(this).css("font-weight", "bold")
 		.css("background-color", "#FFCC66")
 		.css("border-radius", ".5em")
 		.fadeOut(150).fadeIn(400);
	},function(){
		//$(this).removeClass("myclass");
		$(this).css("font-weight", "normal").css("background-color", bg).stop(false, true);
	});//.filter(":even").css("color","black");
	$('#menulink >a').hover(function(){
		$(this).css("font-weight", "bold")
			.css("border", "2px	 dotted lightgreen").css("border-radius", ".5em");
	},function(){
		$(this).css("font-weight", "normal").css("border", "none");
	});
}
</script>
 
<body>
<div style="background-color:white;margin:10px;">
	<div id=tabs>
 
 <%
 String content = GlobalValues.getMenuHtml();
 out.print(content);
  %>


</div>
</div>
<div id="dashboard2"></div>
</body>
</html>
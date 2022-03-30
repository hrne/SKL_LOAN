<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="../script/jquery.js"></script>
<script>
$(function() {
	$('#btn1').click(function(){
		var data = {
				't1':$('#t1').val()
		};
		var sender = $.ajax({
			type : 'POST',
			url : "a2.jsp",
			data :data,
			dataType : "json",
			timeout : 60 * 1000, 
			cache : false
		});
		
		sender.done(function(d){
			alert(d);
		});
		sender.fail(function(d){
			alert(d);	
		});
	});
});
</script>
</head>
<body>
	<input id="t1" value="一乙／∕二丁" />
	<button id="btn1">send</button>
	<div id="r1"></div>
</body>
</html>
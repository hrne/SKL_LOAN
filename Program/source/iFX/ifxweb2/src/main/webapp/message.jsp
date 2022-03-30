<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src='dwr/engine.js'></script>
<script src="dwr/interface/Remote.js"></script>
 <script type='text/javascript' src='dwr/util.js'></script>

<script src="script/jquery.js"></script>
<script src="script/fixIE.js"></script>
<script>
	$(function() {
		dwr.engine.setActiveReverseAjax(true);
		setTimeout(function() {
			talking();
		}, 1000);
	});

	function talking() {
		Remote.getData(42, {
			callback : function(str) {
				$('#text').html(str);
			},
			timeout:5000,
			errorHandler:function(message) {
				alert(message);
			}
		});

	}
</script>
</head>
<body>
<div id='text'></div>
</body>
</html>
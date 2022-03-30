<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/site.css" rel="stylesheet">

<link rel=stylesheet type=text/css
	href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />
<link rel=stylesheet type=text/css href="css/menu.css" />
<style>
.ui-autocomplete {
 max-height: 450px;
 overflow-y: auto;
 overflow-x: hidden;
 padding-right: 20px;
}

</style>

<body style="width:100%;height:100%;margin: 0px; padding: 0; border: 0">
	<div id="home"
		style="background-color: white; width: 0px; height: 0px; margin: 0px; display: none">&nbsp;</div>
	<div id="txcodeArea">
		<a href="#home" id='aa' style='color: red'></a>交易代號&nbsp;<input
			type="text" name="txcode" id="txcode" size="5" maxlength="5">
	</div>
	<div id='tabs'>
		<div style='font-size:20px; width:300px;margin:10;padding:10;color:gold;background-color:black'>&nbsp;建立選單中, 請稍候...</div>
	
	</div>
	<div style='height:100px;'>
	</div>
	<div id='dashboard2'></div>
	<script>
		var _contextPath = '<%=request.getContextPath()%>';
	</script>
	<script src="script/fixIE.js"></script>
	<script src="script/jquery.js"></script>
	<script src="script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="script/sidemenu/side.js"></script>
	<script src="script/ifx-easymenu.js"></script>
</body>
</html>
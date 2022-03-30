<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'log.jsp' starting page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<script>
var basePath = "<%=request.getContextPath()%>";
</script>

</head>

<body>
	輸入測試用AD帳號
	<br> 帳號
	<input id="adid" value="1058001" />
	<!--密碼<input id="adpassword" value="Abc123"/>-->
	<button onclick="myFunction()">Click me</button>
	<br>

	<script>
		//var adtestpp=$('#adpassword').val().trim();
		var url = basePath + "/mvc/hnd/adsDB/ADUSERNAME/";
		function myFunction() {
			var adtestid = url + $('#adid').val().trim();

			$.ajax({
				type : 'post',
				dataType : 'json',
				url : adtestid,
				success : function(data) {
					//alert(adtestid);
					$("#rull").val(adtestid);
					var map = data;
					console.dir(map);
					console.log(map["usdesc"]);
				},
				complete : function() {
					console.log("complete done");
				},
				'error' : function() {
					//alert("{filename:"+$('#filennn').val().trim()+"}");
					alert("send bean error");
				}
			});
		};
	</script>
	<input id="rull" readonly="readonly" style="width: 600px;" />
	<br>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
var basePath2 = basePath+"/test/webfilelog.jsp";
</script>

</head>

<body>
	<button onclick="javascript:window.open(basePath2,'_self')">下載log</button>
	<br> web讀取檔案測試
	<br> 讀取ifx or ifw下檔案
	<br>
	<input id="root" value="ifx" />
	<br> 檔案ifx
	<br> 'runtime/fmt2/C1200_C1200.tom'
	<br> 'runtime/fmt2/sessionvar.ttt'
	<br> env.txt
	<br>
	<br> 檔案ifw
	<br> 'log/2015-04-23_105840.log'
	<br>
	<button onclick="myFunction()">Click me</button>
	<script>
			var url= basePath + "/mvc/hnd/web/file";
			
function myFunction() {
			$.ajax({
			type : 'post',
			dataType : 'json',
			url : url,
			data : {
				_d : "{filename:"+$('#filennn').val().trim()+",root:"+$('#root').val().trim()+"}",
			},
			success : function(data) {
				if (data.success) {
					$("#filetest").val("成功\n"+data.content);
				} else {
					alert('Error:\n' + data.errmsg);
				}
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
	<input id="filennn" value="RRR" style="width: 300px;" />
	<br>

	<textarea id="filetest" style="width: 700px; height: 600px;" wrap="off">失敗</textarea>
</body>
</html>

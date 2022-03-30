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
</script>

</head>

<body>
	隨便輸入字元
	<br> 看看是否有回傳db2值
	<br> 檔案
	<input id="headname" value="SYSTM" /> 代號
	<input id="bodyname" value="FORMDEF" />
	<br>
	<br>
	<input id="result" />

	<script>
$(function() {
		$('#result').autocomplete({
			source : function(request, response) {
				$.ajax({
					cache : false,
			async : false,
			minLength : 0,
			autoFocus : false,
					url : basePath + "/mvc/hnd/codeList/all/"+$('#headname').val().trim() +"/"+$('#bodyname').val().trim(),
					success : function(data) {
						response($.map(data, function(item) {
							if(item.content == "FORM,FORMX"){
								return;
							}
		         if (item.key.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
							return {
								value : item.key,
								label : item.key+"-"+item.content
							};
		          }							
							//$('#helptext').attr("value",helptext);

						}));
					//	$('#helptext').innerHTML(helptext);
					},
					error:function(data){
						alert("error:" + data);
					}
				})
			},
			minLength : 0,
			open : function() {
				$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
			},
			close : function() {
				$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
			}
		}).focus(function(){            
        //Use the below line instead of triggering keydown
             $(this).autocomplete("search");
     });
});
</script>
</body>
</html>

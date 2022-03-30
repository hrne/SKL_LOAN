<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<!-- environment variables -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="script" value="${pageContext.request.contextPath}/script" />
<c:set var="css" value="${pageContext.request.contextPath}/css" />

<link rel=stylesheet type=text/css href="<%=request.getContextPath()%>/script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<script src="<%=request.getContextPath()%>/script/jquery.js"></script>
<script src="<%=request.getContextPath()%>/script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script>
var basePath = "<%=request.getContextPath()%>";
</script>
<title>console</title>

</head>

<body>
	where are you
	<br />
	<input id="city" />

	<script>
			var i=0;
$(function() {
		$('#city').autocomplete({
			source : function(request, response) {
				$.ajax({
					cache : false,
			async : false,
					url : basePath + "/mvc/hnd/codeList/all/XLDEF/LNACCDDEF",
					success : function(data) {
						 i=0;
						response($.map(data, function(item) {
							i++;
							//$('#helptext').attr("value",helptext);
							return {
								value : i,
								label : item
							};
						}));
					//	$('#helptext').innerHTML(helptext);
					
					},
					error:function(data){
						alert("error:" + data);
					}
				})
			},
			minLength : 1,
			open : function() {
				$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
			},
			close : function() {
				$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
			}
		});
});
</script>
</body>
</html>

<!DOCTYPE HTML><%@page language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>upload</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../../script/jquery.js"></script>
</head>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"/>
<body>
	<form method="post" action="<%=request.getContextPath()%>/mvc/file/upload?form"
		enctype="multipart/form-data" method="post">

 		<c:if test="${not empty message}">
 		<p>
 			message:${message}
 		</p>
 		</c:if>
		<p>
			<label for="name">Name</label> <input id='name' name='name' />
		</p>
		<p>
			<label for="file">File:</label> <input name="file" type="file" />
		<p />
		<p>
			<button type="submit">
				<span>Save</span>
			</button>
			<button type="reset">
				<span>Reset</span>
			</button>
		</p>
	</form>
</body>
</html>
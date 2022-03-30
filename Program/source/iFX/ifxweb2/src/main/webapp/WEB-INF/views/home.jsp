<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
	<c:set var="myContext" value="<%=request.getContextPath()%>"/>
	
	<script>
	
		var _contextPath = '<%=request.getContextPath()%>';
		
	</script>
	<script src="<%=request.getContextPath()%>/mvc/resources/script/test1.js"></script>
</head>
<body>
<h1>
	Hello world!  
	  <c:if test="${not empty txReq}">
	  		${txReq.txcode}<br/>
	  	    ${txReq.title}<br/>
	  </c:if>
</h1>

<P>  The time on the server is ${serverTime}. </P>
</body>
</html>

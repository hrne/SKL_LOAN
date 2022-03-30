<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	String error = (String)request.getAttribute("error");
		%>
<h2>登入錯誤</h2>
<h3><%=error %></h3>
<%

if(error==null){
 error = "";
}
//else if(error != "E003-此瀏覽器已登入本系統!"){
//	}
%>
<a href='forceOut.jsp'>強制登出</a><br/>
<a href="JavaScript:var opened=window.open('about:blank','_self'); opened.opener=null; opened.close();">關閉目前視窗</a>
</body>
</html>
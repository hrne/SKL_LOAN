<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
//System.out.println("invalidate session");
//session.invalidate();
request.getRequestDispatcher("login.jsp").forward( request, response );
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Ifx</title>
<style type="text/css">
ol
 {
	 list-style-type:lower-alpha;
	 line-height:2;
 }
 ol li
 {
 	list-style-type:decimal;
}
a {
	text-decoration:none;
}
a:hover{
	color:orange;
	font-weight:bold;
}
</style>
<script>
function fullwin(){
 	window.open("login.jsp","login","fullscreen,scrollbars")
}
</script>
 
</head>
<body>



<br/>
選擇螢幕模式
<br/>
<ol>
<li>
<a href='javascript:void(0)' onclick='fullwin()'>Click(或Ctrl+Click)此連結進入全螢幕模式</a>
</li>
<li> 
<a href='login.jsp'>按此連結進入一般模式</a><br/>
</li>	
</ol>
</body>
</html>
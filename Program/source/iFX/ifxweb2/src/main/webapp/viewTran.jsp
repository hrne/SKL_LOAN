<%@page import="com.st1.bean.RequestBase"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
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
String txcd = StringEscapeUtils.escapeXml10(request.getParameter("txcd"));
String idString = StringEscapeUtils.escapeXml10(request.getParameter("id"));
idString = RequestBase.trimLeadingZero(idString);
Long id = Long.valueOf(idString);
//Journal jnl = dao.get(id);
request.setAttribute( "_view", "1");
request.setAttribute( "txcode",  txcd);
request.setAttribute("_id", id);
//request.setAttribute( "_f",  jnl.getDetail().getFields());
request.getRequestDispatcher("tran2.jsp").forward( request, response );
%>
</body>
</html>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
String txcode = (String)request.getAttribute("txcode");
String nextPage = getPage(txcode);
System.out.println("nextPage:"+nextPage);

%>
<%!static String getPage(String txcode) {
	String page=null;
	int iTxcd = Integer.parseInt(txcode.substring(1));
	switch(iTxcd) {
	case 1200:
	case 1201:
	case 1300:
	case 2000:
	case 3000:
	case 3100:
	case 4000:
		page = "HXXXX.jsp";break;
	case 1400:
		page = "Hairmail.jsp";
		break;
	case 1100:
		page = "Htelex.jsp";
		break;
	case 5100:
	case 301:
	case 302:
	case 5200:
		page = "Hprint.jsp";
		break;
	default:
		page = txcode.toUpperCase() + ".jsp";
		break;
	}
	return "pages/" + page;
}


%>

<jsp:forward page="<%=nextPage %>">
   <jsp:param name="no-use" value="nop" />
</jsp:forward>


<% 

%>
<%@page import="com.st1.bean.BeanProxy"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	System.out.println("enter beanProxy.jsp");

	response.setContentType("text/json");
	String bean = StringEscapeUtils.escapeXml10(request.getParameter("bean"));
	String reqJson = StringEscapeUtils.escapeXml10(request.getParameter("reqJson"));
	
	String result = send(bean, reqJson);
	System.out.println(result);

	out.print(result);
	System.out.println("leave beanProxy.jsp");
%>

<%!static String send(String bean, String reqJson) {
		BeanProxy proxy = new BeanProxy();
		return proxy.dispatch(bean, reqJson); 
}%>
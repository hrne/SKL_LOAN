<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<%@ page language="java" contentType="application/json; charset=utf-8" pageEncoding="UTF-8"%>
<%
 final Logger logger = LoggerFactory.getLogger("IFX-Systeam A2.jsp");
response.setContentType("text/json");
	String t1 = StringEscapeUtils.escapeXml10(request.getParameter("t1"));
	logger.info(FilterUtils.escape(t1));
	byte y[] = t1.getBytes("ms950");
	for (int i = 0; i < y.length; i++) {
		System.out.printf("%x ", y[i]);
	}
	logger.info("\n");
	byte y2[]= t1.getBytes("utf-8");
	for (int i = 0; i < y2.length; i++) {
		System.out.printf("%x ", y2[i]);
	}
	logger.info("\n");
	
	
	
	String t2 = new String(y,"ms950");
	logger.info("\n"+ FilterUtils.escape(t2));
	y = t2.getBytes("utf-8");
	for (int i = 0; i < y.length; i++) {
		System.out.printf("%x ", y[i]);
	}
	
	
	
	
	out.print(t1);
	logger.info("leave send");
%>

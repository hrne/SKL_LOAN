<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>tran2</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	request.getRequestDispatcher("mvc/tran/run").forward(request,
			response);
%>
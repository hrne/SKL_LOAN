<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="utf-8">
<head>
<title>IFX: <c:out value="${pageTitle}"/> </title>
        <meta http-equiv="content-type" content="text/html;charset=utf-8" />
        <c:url var="cssUrl" value="/resources/css/site.css"/>
        <link href="/resources/css/site.css" rel="stylesheet"/>
</title>

<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<!-- 
        <c:url var="cssUrl" value="/resources/css/bootstrap.css"/>
        <link href="/resources/css/bootstrap.css" rel="stylesheet"/>
         -->
<style>
body {
	padding-top: 60px;
	/* 60px to make the container go all the way to the bottom of the topbar */
}
</style>

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
          <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
</head>

<body>
<c:set var="pageTitle" value="Please Login" scope="request"/>
<c:url value="/login" var="loginUrl"/>

<div>
<form action="/login" method="post">
	<c:if test="${param.error != null }">
		<div class="alert alter-error">
			Failed to login
			 <c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
              Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
            </c:if>
		</div>
	</c:if>
	
	    <c:if test="${param.logout != null}">
        <div class="alert alert-success">
            You have been logged out.
        </div>
    </c:if>
     <label for="username">Username</label>
    <input type="text" id="username" name="username"/>
    <div class="form-actions">
        <input id="submit" class="btn" name="submit" type="submit"  
        value="Login"/>
    </div>
</form>

</div>
</body>
</html>
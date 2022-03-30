<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="com.st1.ifx.filter.FilterUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>	
<%

  final Logger logger = LoggerFactory.getLogger("IFX-step1");
  
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


logger.info("session Id step1:"+session.getId());
logger.info("KE1:"+GlobalValues.SESSION_USER_INFO);
logger.info("KE2:"+session.getAttribute(GlobalValues.SESSION_USER_INFO));

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IFX starting page 1</title>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/favicon.ico">
	<style>
	.ok-status { color:green;}
	.error-status { color:red; }
	</style>
  </head>
  
  <c:set var="context" value="${pageContext.request.contextPath}" />
  
  <%
	String ua = request.getHeader( "User-Agent" );
	boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );
	if(!isMSIE){
		if(ua.indexOf("Trident/7.0")!= -1 && ua.indexOf("rv:11.0") != -1){
			isMSIE = true;
		}
	}
	logger.info(FilterUtils.escape("user ageng:"+ua));
	
%>
<body>
<div id="activexxxx">
	<object id="RequisiteTester1" classid="clsid:FC65D619-B79F-4975-AF9A-BFB726D8A30B" width="0px" height="0px"></object><!--/CG5710/-->
<object id="RequisiteTester2" classid="clsid:1AEC6557-78C7-41C8-9FB4-EC441859E7BA" width="0px" height="0px"></object><!--/CG5710/-->
<object id="myDevice" classid="clsid:{4D162943-BE86-47A7-9088-F0BC30A6BDD2}" 
        codebase="env/cab/St1Wrapper.cab#Version=18,10,31,0" ></object>

</div>
<div>
環境檢查
</div>

<% if( !isMSIE ){ %>
<div id="pBrowser">
	<h1>請使用Internet Explorer 11以上版本瀏覽器!</h1>
</div>
<% } %>

<div>
	<a onclick="goNextpage()" href="javascript:void(0);">先跳過, 以後再說</a>&nbsp;
	<a href="env/help.jsp">說明</a>
</div>	
<div id="pTrust">
檢查是否加入信任網站
</div>
<div id="pDevice">
檢查是周邊設備Activex是否安裝
</div>

<!--<div id="pAgent">
下載 etabs device agent (Fake)<a href="env/fakeagent/fake.jsp"  target="_blank">前往</a>
</div>
-->
<div>
	<button id="btnCheck">再檢查</button>
</div>


<script>
var _contextPath = '<%=request.getContextPath()%>'; 
<% String ver = GlobalValues.jsVersion;%>
console.log("doc.cookie_step1:"+document.cookie);

</script>
<script src="script/jquery.js"></script>
<script src="env/script/ifx-env-check1.js?v=<%=ver%>"></script>
<script>
$(function(){
	
	var panels = {
		'trust':'pTrust',
		'device':'pDevice'
	};
	Env.one.init(panels, "../easy_main.jsp"); //第二參數已被封住
	//潘 登入元件檢查
	//if(Env.one.checkAll(true)){
		goNextpage();
	//}
	
});
	function goNextpage() {
		
		//新增try for Chrome 使用
		try {
			var sFeatures = "dialogHeight: 230px; dialogWidth: 210px; resizable:no;status:no;scroll:no;";
			var popup = window.showModalDialog("../getCache.jsp","",sFeatures);
	     setTimeout(function(){  
	  	if(popup){
		   location = "../easy_main.jsp";
		  }
	     },10); 			
			 // code to try
		} catch (e) {
			setTimeout(function(){  
		   location = "<%=request.getContextPath()%>/easy_main.jsp";  //chrome 要加入contextPath 才能正常轉換
	     },10); 	
		}
	}
	$("#btnCheck").on("click", function() {
      window.location.reload();
	});
				//停用本頁當上一頁此頁功能
		window.history.forward(1); 
</script>
  </body>
</html>

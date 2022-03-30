<%@page import="com.st1.util.PoorManUtil"%>
<%@page import="com.st1.def.menu.MenuBuilder"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易單據重印</title>
<script src="script/fixIE.js"></script>
<script src="script/jquery.js"></script>
<script src="script/jquery.blockUI.js"></script>
<script src="script/jquery.csv-0.71.js"></script>
<script src="script/ifx-utl.js"></script>
<script src="script/dupdoc.js"></script>

<link href="css/site.css" rel="stylesheet">
      

<style type=text/css>
  li.ui-state-default {
    font-size : 12px;
  }
  div.ui-tabs-panel {
    font-size : 15px;
    font-family : arial;
	color:blue;
	line-height: 200%;
  }
   

.tabcontent{
	color:blue;
}


div.ui-tabs-panel a {
	text-decoration:none;
	color:SpringGreen;
	padding:10px;
}

myclass {
	color:red;
}
span.label {
	/*background-color:LightGoldenRodYellow ;
	color:MidnightBlue ; */
	color:white;
	padding:2px;
}

#result {
	background-color:lightyellow ;
	color:black;
	width:100%;
	
	font-family:細明體;  
	font-size: 16px;
	margin-top:0px;
	margin-left:20px;
	padding:px;
	border: 1;
}
#result hr {
	height:3px;
	color:blue;
	background-color:gray;
}
#result pre {
	font-family: "Courier New", Courier,新細明體,	
                    monospace;
	color:red;
}
.printButton {
	background:url(images/print.png) repeat; 
    cursor:pointer; 
    color:black;
    background:lightgray;
    width:200px; 
    height: 30px; 

}

</style>
<script>
<%
// check login session

	String reqId = StringEscapeUtils.escapeXml10(request.getParameter("id"));
	if(reqId==null) reqId = "";
	
	
	String dt = StringEscapeUtils.escapeXml10(request.getParameter("dt"));
	if(dt==null) dt = PoorManUtil.getToday();
	


%>
</script> 
<body>
<div style="margin:15px;padding:5px;vertical-align:middle">
<input type="hidden" id="brno" value=""/>
<span class='label' >交易序號</span>
<input type="text" id="txtno" size="20" value='<%=reqId%>'/><
<span class='label'>日期</span>
<input type="text" id="dt" size="8" value='<%=dt%>'/>
<input type="button" id="btnSearch" value="送出"/>	
</div>
<div id="result">
</div>
</body>
</html>
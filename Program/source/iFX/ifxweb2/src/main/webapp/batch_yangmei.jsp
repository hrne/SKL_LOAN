<%@page import="com.st1.util.PoorManUtil"%>
<%@page import="com.st1.def.menu.MenuBuilder"%>
<%@page import="com.st1.servlet.GlobalValues"%>    
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@page import="java.io.*,java.net.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	 <%@include file="/env/myDevice.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<c:set var="context" value="${pageContext.request.contextPath}" />
<title> 央媒 </title>
<script src="script/jquery.js"></script>

<link href="css/site.css" rel="stylesheet">
      

<style type=text/css>
  li.ui-state-default {
    font-size : 10px;
  }
  div.ui-tabs-panel {
    font-size : 10px;
    font-family : arial;
	color:blue;
	line-height: 100%;
  }
   

.tabcontent{
	color:blue;
}


div.ui-tabs-panel a {
	text-decoration:none;
	color:SpringGreen;
	padding:2px;
}

myclass {
	color:red;
}
span.label {
	/*background-color:LightGoldenRodYellow ;
	color:MidnightBlue ;  */
	color:white; 
	padding:2px;
}

#batch_head {
margin:5px;
padding:2px;
/*vertical-align:middle;
position:fixed;*/
position:fixed;
top:2px;
left:5px;
background-color:green ;
}

#result {
	//background-color:lightyellow ;
	color:black;
	width:auto;
	
	font-family:細明體;  
	font-size: 11px;
	margin-top:1px;
	margin-left:1px;
	padding:px;
	border: 1;
}
#result hr {
	height:1px;
	color:blue;
	background-color:gray;
}
#result pre {
	font-family: "Courier New", Courier,新細明體,	
                    monospace;
	color:red;
}

</style>
<script src="script/yepnope.1.5.4-min.js"></script>
<script>
<%
// check login session

    String filePath = StringEscapeUtils.escapeXml10(request.getParameter("filepath"));
	if(filePath==null) filePath = "";
	
	String fileBrno = StringEscapeUtils.escapeXml10(request.getParameter("filebrno"));
	if(fileBrno==null) fileBrno = "";
	
	String filename = StringEscapeUtils.escapeXml10(request.getParameter("filename"));
	if(filename==null) filename = "";		
	
			if (StringEscapeUtils.escapeXml10(request.getParameter("hiddenTextBox2")) != null) {

				filePath = new String(filePath.getBytes("ISO-8859-1"), "Big5");
				filename = new String(filename.getBytes("ISO-8859-1"), "Big5");

				File file = new File(filePath);
				if (file.exists()) { //檢驗檔案是否存在
					try {
						response.setHeader(
								"Content-Disposition",
								"attachment; filename=\""
										+ URLEncoder.encode(filename, "UTF-8")
										+ "\"");
						OutputStream output = response.getOutputStream();
						InputStream in = new FileInputStream(file);
						byte[] b = new byte[2048];
						int len;

						while ((len = in.read(b)) > 0) {
							output.write(b, 0, len);
						}
						in.close();
						output.flush();
						output.close(); //關閉串流
						out.clear();
						out = pageContext.pushBody();
					} catch (Exception ex) {
						out.println("Exception : " + ex.toString());
						out.println("<br/>");
					}
				} else {
					out.println(filename + " : 此檔案不存在");
					out.println("<br/>");
				}
			}
			
%>
		
</script> 
</head>
<body>
<div id="batch_head">
	<!--無用,都已在savedoc給-->
<input type="hidden" id="filebrno" value='<%=fileBrno%>'/>
<input type="hidden" id="filepath" value='<%=filePath%>'/>
<input type="hidden" id="filename" size="20" maxlength="20" value='<%=filename%>'/>
<input type="button" class='button' id="btnyangmeiD" value="重新下載"/>	
</div>
<div id="result">
</div>
	<!--柯 myForm只是給通知報表取檔案-->
	<form id="myForm2" name="myForm2" method="post">
		<input type="hidden" name="hiddenTextBox2"><br>
	</form>
</body>
<script> 
  		document.myForm2.hiddenTextBox2.value = "go";
  		document.forms['myForm2'].submit();
  		
  		var btnYangmeiD = $('#btnyangmeiD');
 	 btnYangmeiD.click(function(){
  	document.forms['myForm2'].submit();
 	});
</script> 
</html>
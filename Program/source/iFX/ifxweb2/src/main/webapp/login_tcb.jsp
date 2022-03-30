<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>IFX login</title>
<link href="css/site.css" rel="stylesheet">
<link href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css"
	rel="stylesheet">
<link rel="shortcut icon" type="image/x-icon" href="<%=request.getContextPath()%>/favicon.ico">
<style>
#mapSurface {
	width: 600px;
	height: 450px;
	border: solid 1px black;
}

fieldset {
	margin-top: 100px;
	margin-left: 150px;
	width: 600px;
	-webkit-border-radius: 5em;
	-moz-border-radius: 5em;
	border-radius: 5em;
}

label.error {
	font-size: 16px;
}
<!--TEMP-->
.button {
    display: inline-block;
    text-align: center;
    vertical-align: middle;
    padding: 1px 10px;
    border: 1px solid #b3b3b3;
    border-radius: 13px;
    background: #ffffff;
    background: -webkit-gradient(linear, left top, left bottom, from(#ffffff), to(#b3b3b3));
    background: -moz-linear-gradient(top, #ffffff, #b3b3b3);
    background: linear-gradient(to bottom, #ffffff, #b3b3b3);
    font: normal normal normal 17px georgia;
    color: #111111;
    text-decoration: none;
}
.button:hover,
.button:focus {
    border: 1px solid #ffffff;
    background: #ffffff;
    background: -webkit-gradient(linear, left top, left bottom, from(#ffffff), to(#d7d7d7));
    background: -moz-linear-gradient(top, #ffffff, #d7d7d7);
    background: linear-gradient(to bottom, #ffffff, #d7d7d7);
    color: #111111;
    text-decoration: none;
}
.button:active {
    background: #b3b3b3;
    background: -webkit-gradient(linear, left top, left bottom, from(#b3b3b3), to(#b3b3b3));
    background: -moz-linear-gradient(top, #b3b3b3, #b3b3b3);
    background: linear-gradient(to bottom, #b3b3b3, #b3b3b3);
}
</style>


<script src="script/jquery.js"></script>
<script src="script/jquery.validate.js"></script>
<script src="script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>

<script>
var _winname = '<%=java.util.UUID.randomUUID()%>';
</script>
<script>

	if (top.window != window){
		//top.window.replace("index.jsp");
		//top.location = "index.jsp"; 
		//Response.Write("parent.location.href='index.jsp';");
		//top.window.parent.location.href = "index.jsp";
		//window.onbeforeunload = function() { 
	// return "請關閉IE，重新登入";
//};
//原先提醒如按保留導致有問題..
/*    top.window.onbeforeunload = function () {
//if (confirm('系統異常，\n請重新登入')) {
return '系統異常，重新登入IFX\n請按一下「離開」來修復這個問題。';
//}
}
*/
    top.window.location = "index.jsp";
		//top.window.location.reload();
		//document.location.replace = "index.jsp";
		//parent.window.location.assign("index.jsp");
	}

	function cb(o) {
		$('#user').val(o.name);
		$('#password').val(o.password);
	}
	function getJsonp() {
		var s = document.createElement('script');
		s.src = 'http://localhost:8088/?callback=cb&who=unknown';
		document.getElementsByTagName('head')[0].appendChild(s);
	}
	$(function() {
		//	getJsonp();
		$('#winname0').val(window.name);
		$('#winname9').val(_winname);
		if (!window.name)
			window.name = _winname;

		$('#winname').val(window.name);

		//2016.10.03 虛擬櫃員登入控管:登入使用者代碼第五碼為英文時控管不可登入
		$.validator.addMethod("ckfakeuser", function(value, element) {
         return  /^....\d{1}/.test( value );
      });


		$('#login').validate({

			rules : {
				user : {
					required : true,
					rangelength : [ 6, 6 ],
					ckfakeuser : true
				},
				password : {
					required : true,
					rangelength : [ 6, 20 ]  //密碼長度統一更長,中心還沒改
				},
				spam : "required"
			}, //end rules
			messages : {
				user : {
					required : "請輸入使用者ID.",
					rangelength : '使用者ID應為6位數字.',
					ckfakeuser: "登入帳號第5碼不可英文,請輸入正確帳號!"
				},
				password : {
					required : '請輸入密碼.',
					rangelength : '密碼最少6位.'   
				}

			},
			errorPlacementOld : function(error, element) {
				if (element.is(":radio") || element.is(":checkbox")) {
					error.appendTo(element.parent());
				} else {

					error.insertAfter(element.parent());
				}
			},

		}); // end validate 

		$('#login :text:first').focus();
		$("#submit").button();
		/*
		$("#login").submit(function(){
			
			if($("#user").val()=="") {
				alert("please supply user id");
				$("#user").focus();
				return false;
			}
			if($("#password").val()=="") {
				alert("please supply password");
				$("#password").focus();
				return false;
			}
			
			var subButton = $(this).find(':submit');
			subButton.attr('disabled',true);
			subButton.val('...sending information');
			  
		});
		 */

		//		getPos();
	});
     function openall()
{
var strComputer = ".";
var SWBemlocator = new ActiveXObject("WbemScripting.SWbemLocator");
var objWMIService = SWBemlocator.ConnectServer(strComputer, "/root/CIMV2");
var strProcess = "";
var colItems = objWMIService.ExecQuery("Select * from Win32_ComputerSystem");

var e = new Enumerator(colItems);
for(; ! e.atEnd(); e.moveNext())
{
strProcess += "";
}

colItems = objWMIService.ExecQuery("Select * from Win32_NetworkAdapterConfiguration where IPEnabled=true");
e = new Enumerator(colItems);
for(; ! e.atEnd(); e.moveNext())
{
strProcess +=e.item().IPAddress(0)+ "\n";
}
alert(strProcess);
}
//openall();

</script>

<%	if (!GlobalValues.systemStarted) {
		// redirect to wait page?

	}

	/*
	 // already signon
	 if(session.getAttribute(GlobalValues.SESSION_USER_INFO) != null) {
	 request.getRequestDispatcher("easy_main.jsp").forward( request, response );
	 }
	 */
	 String userAgent = request.getHeader("user-agent");
	String error = (String) session.getAttribute("error");
	if (error == null || error == "null") {
		error = "";
	}
	String user = (String) session.getAttribute("user");
	if (user == null)
		user = "";
	session.removeAttribute("error");
	String version = GlobalValues.applicationVersion;

	/*
	 String error = (String)request.getAttribute("error");
	 if (error == null || error == "null") {
	 error = "";
	 }
	 String user = (String) request.getAttribute("user");
	 if (user == null)
	 user = "";
	 */
	//System.out.println("* * * error:"+error);
%>
</head>

<body>
	<span style="font-size:12px; color:#FFFFFF; font-family:Microsoft JhengHei;">
			<a class="button" href="user/info_html/IFX_set_ENV.zip">首次登入環境安裝</a>
				&nbsp第一次由此電腦登入系統時須執行，如輪調或代理。<br><br>
			<a target="_blank" class="button" href="http://10.0.45.8:9080/ifx/swift-auto/brobro/publish.htm">自動列印電文下載</a>
				&nbsp每一分行應由同一電腦安裝啟動。不同人收電第一次啟動時請重新下載，如輪調或代理。<br><br>
				持管理者權限者與本系統不相容，請於安裝後，由控制台-使用者帳戶-移除持管理者權限之登入使用者，電腦登出再登入。
	</span>
	<div class="login-main">

		<form name="login" id="login" action="doLogin.jsp" method="post" autocomplete="off">

			<fieldset>
				<IMG alt=""
					style='margin-top: 15px; margin-bottom: 0px; margin-left: 180px; height: 50px; width: 110px; zoom: 1.5;'
					src="images/ifx_logo_1.jpg">
					
				<div class="error"><%=StringEscapeUtils.escapeXml10(error)%></div>
				<div>
					<label for="user" class="label">使用者代號</label> <input name="user"
						type="text"
						style='margin-left: 10px; width: 70px; font-size: 15px;' id="user"
						size="6" maxlength="6" value="<%=StringEscapeUtils.escapeXml10(user)%>" title="請輸入使用者ID">
				</div>
				<div>
					<label for="password" class="label">密 碼</label> 
					<!-- 避免瀏覽器將表單識別爲含有用戶名密碼的表單。在password 之前加一個隱藏的 type="password"-->
					<input type="password" style="display:none">
					<input  
						name="password"
						style='margin-left: 10px; font-size: 15px; width: 200px;'
						type="password" id="password" size="20" maxlength="20" title='請輸入密碼'>
				</div>
				<div style='display: none'>
					winname:<input type='text' name='winname' id='winname' size='40' /><br />
					winname ori:<input type='text' name='winname0' id='winname0'
						size='40' /><br /> generated:<input type='text' name='winname9'
						id='winname9' size='40' /><br />
				</div>
				<div>
					&nbsp; <input type="submit" name="submit" id="submit" value="確  定"
						style="font-size: 16px; font-family: '細明體';">
				</div>
			</fieldset>

		</form>
		<div id="mapSurface" style="display: none"></div>
		<div style="position: absolute; font-size: 14px;font-family: Consolas; color: #FFFFFF; bottom: 3px; right: 6px;"><%=version %></div>
		
	</div>
	
<!-- 	
	<%@ taglib prefix="sec"
		uri="http://www.springframework.org/security/tags"%>
	<sec:authorize access="authenticated" var="authenticated" />
	<c:url var="logoutUrl" value="/logout" />
	<div>
		Welcome
		<sec:authentication property="name" />
		
		[<sec:authentication property="principal.userName" />]
		<a href="/logout"> logout </a>
	</div>
 -->


</body>
</html>


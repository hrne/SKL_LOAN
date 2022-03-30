<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>IFX login</title>
<!--<link href="css/site.css" rel="stylesheet">-->
<!--<link href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" rel="stylesheet">-->
<link rel="shortcut icon" type="image/x-icon"
	href="<%=request.getContextPath()%>/favicon.ico">
<style>
.input {
	font: "微軟正黑體";
	font-size: 20px;
}
.button {
	font: "微軟正黑體";
	font-size: 20px;
	font-weight: bold;
	background-color: #CCC;
	color: #000
}
.h1 {
	font: "微軟正黑體";
	font-size: 24px;
}
.h2 {
	font: "微軟正黑體";
	font-size: 14px;
}
#tdiv {
	/* background-color: #fff; */
}
#bdiv {
	height: 400px;
	margin-left: 100px;
	margin-right: 100px;
}
#bdiv #login #mtb tr td {
	font-family: "微軟正黑體";
	font-size: 24px;
}
.bb {
   color: #cd1919;
	font-weight: bold;
	font-family: "微軟正黑體";
}
body {
	/* background-color: #FFF8F8; */
	background-image: url(images/sklBackground.png);
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-position: center;
  background-size: cover;
	color: #000;
	font-weight: bold;
	font-family: "微軟正黑體";
}
</style>

<script src="script/jquery.js"></script>
<script src="script/jquery.validate.js"></script>
<script src="script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>

<script>var _winname = '<%=java.util.UUID.randomUUID()%>';</script>
<script>
if (top.window != window) {
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
        return /^....\d{1}/.test(value);
    });

    $('#login').validate({

        rules: {
            user: {
                required: true,
                //rangelength: [6, 6],
                //ckfakeuser: true
            },
            password: {
                required: true,
                //rangelength: [6, 20]
                    //密碼長度統一更長,中心還沒改
            },
            spam: "required"
        }, //end rules
        messages: {
            user: {
                required: "",
                //rangelength: '使用者ID應為6位數字.',
                //ckfakeuser: "登入帳號第5碼不可英文,請輸入正確帳號!"
            },
            password: {
                required: '',
                //rangelength: '密碼最少6位.'
            }

        },
        errorPlacementOld: function(error, element) {
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

function openall() {
        var strComputer = ".";
        var SWBemlocator = new ActiveXObject("WbemScripting.SWbemLocator");
        var objWMIService = SWBemlocator.ConnectServer(strComputer, "/root/CIMV2");
        var strProcess = "";
        var colItems = objWMIService.ExecQuery("Select * from Win32_ComputerSystem");

        var e = new Enumerator(colItems);
        for (; !e.atEnd(); e.moveNext()) {
            strProcess += "";
        }

        colItems = objWMIService.ExecQuery("Select * from Win32_NetworkAdapterConfiguration where IPEnabled=true");
        e = new Enumerator(colItems);
        for (; !e.atEnd(); e.moveNext()) {
            strProcess += e.item().IPAddress(0) + "\n";
        }
        alert(strProcess);
    }
    //openall();
$(function(){
	var b = [];
	$('#fakpassword').keyup(function(){
		  var a = Array.from($("#fakpassword").val());
		  $("#password").val('');
		  for(var i = 0; i < a.length; i++){
		    if(a[i] != "*"){
		      b[i] = a[i];
		      a[i] = "*";
		    }	    
		  }
		  $("#fakpassword").val(a.join(''));
		  $("#password").val(b.join(''));
		  
	});
});
</script>

<%
	if (!GlobalValues.systemStarted) { } // redirect to wait page?

	/*
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
    <div id="tdiv"><img src="images/skl2.png" width="257" height="75" /></div>
    <div id="bdiv">
        <form name="login" id="login" action="doLoginFirst.jsp" method="post" autocomplete="off">
            <table width="100%" border="0" cellpadding="1" cellspacing="5" id="mtb">
                <tr>
                    <td align="right" valign="middle">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="right" valign="middle">&nbsp;</td>
                    <td>
                        <h1>放　款　帳　務　系　統</h1></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td width="237" align="right" valign="middle">&nbsp;</td>
                    <td width="800">員編&nbsp;&nbsp;
                        <input class="input" type="text" id="user" name="user" size="30" maxlength="6" value="<%=StringEscapeUtils.escapeXml10(user)%>" title="請輸入使用者ID" style="ime-mode:disabled"/>
                    </td>
                    <td width="205">&nbsp;</td>
                </tr>
                <tr>
                    <td align="right">&nbsp;</td>
                    <td>密碼&nbsp;&nbsp;
                        <input type="text" id="winname" name="winname" style="display:none"/>
                        <input type="text" id="password" name="password" style="display:none">
                        <input class="input" id="fakpassword" type="text" size="30" maxlength="20" title='請輸入密碼' />
                        <input class="button" type="submit" name="submit" id="submit" value="登入" />
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td align="right">&nbsp;</td>
                    <td>&nbsp;
                        <div class="bb">
                            <%=StringEscapeUtils.escapeXml10(error)%>
                        </div>
                    </td>
                    <td>&nbsp;</td>
                </tr>
            </table>
            <hr />
            <table width="100%" border="0">
                <tr>
                    <td align="left">
                        <p>&nbsp;</p>
                        <p>萬事宜股份有限公司｜電話：(02)2759-0022｜地圵：臺北市南港區忠孝東路6段21號8樓之6</p>
                        <p>萬事宜股份有限公司版權所有 Copyright © 2019 SystemOne Co., Ltd.</p>
                        <p>最佳瀏覽狀態為1024x768解析度以上及Edge瀏覽器</p>
                    </td>
                </tr>
            </table>
            <p>&nbsp;</p>
        </form>
        <div id="mapSurface" style="display: none"></div>
        <div style="position: absolute; font-size: 14px; font-family: Consolas; color: #FFFFFF; bottom: 3px; right: 6px;">
            <%=version%>
        </div>
    </div>
    <!--
	<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
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
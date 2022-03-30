<%@page import="com.st1.ifx.etc.Pair"%>
<%@page import="com.st1.msw.UserInfo"%>
<%@page import="com.st1.ifx.menu.TranListBuilder"%>
<%@page import="com.st1.util.MySpring"%>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="com.st1.def.menu.MenuBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/site.css" rel="stylesheet">
<script src="script/jquery.js"></script>
<script src="script/jqueryui/js/jquery-ui-1.8.19.custom.min.js"></script>
<script src="script/sidemenu/side.js"></script>
<link rel=stylesheet type=text/css
	href="script/jqueryui/css/redmond/jquery-ui-1.8.23.custom.css" />
<link rel=stylesheet type=text/css href="css/menu.css" />

<script>
	$(function() {

		initMenu();
		initSideMenu();
		handle_txcode();
		var fn = getTabFn('registerTran');
		fn('MENU', 'refresh', focusIt);
		setTimeout(function() {
			$('#txcode').focus();
		}, 600);

	});

	function focusIt() {
		setTimeout(function() {
			$('#txcode').focus();
		}, 100);

	}
	function getTabFn(name) {
		return top.frames['easytab'].contentWindow[name];
	}

	function initSideMenu() {
		//鍾 功能面板用原本的樣式及展開的方式,且所有功能在任何交易都可執行
		sidemenu.init('dashboard2', {
			top : 42
		}, function() {
			//end	
			sidemenu.enableAll(false);
			sidemenu.enable("dupprint");
			//sidemenu.enable('savealltran');
			sidemenu.enable("logoff");
			// begin hot tran		
			//鍾 且所有功能在任何交易都可執行
			sidemenu.enable("approval");
			//end	
			sidemenu.enable("ec");

			// end hot tran
			sidemenu.enable("msw");
		});
	}
	function handle_txcode() {
		$('#txcode').change(function() {
			var s = $(this).val().toUpperCase();
			if (s.length == 5) {
				var u = "tran2.jsp?txcode=" + s;
				parent.addTran(s, u);
				$(this).val('');
			}

		}).keypress(function() {
			var s = $(this).val();
			if (s.length == 5)
				$(this).trigger("change");
		}).click(function() {
			//window.location = $('#aa').attr('href');

			window.location = "#home";
			$('#txcode').focus();
			//window.scrollTo(0,50);
		});

	}
	function initMenu() {
		//$("#tabs").tabs ();
		var bg = "rgba(234, 251, 211, 1)";

		//	var tx = ["A","B"];
		$("#tabs").tabs({
			width : $("#tabs").parent().width(),
			height : "auto"

		});
		$("[id^=tab]").css("background-color", bg);
		$(".menuTable td a").click(function() {
			var txcode = $(this).text().slice(0, 5);
			var url = "tran2.jsp?txcode=" + txcode;
			//window.location = url;
			parent.addTran($(this).text().slice(0, 5), url);

		}).hover(
				function() {
					$(this).css("font-weight", "bold").css("background-color",
							"#FFCC66").css("border-radius", ".5em")
							.fadeOut(150).fadeIn(400);

				},
				function() {
					//$(this).removeClass("myclass");
					$(this).css("font-weight", "normal").css(
							"background-color", bg).stop(false, true);
				});
		//.filter(":even").css("color","black");

		$('#menulink >a').hover(
				function() {
					$(this).css("font-weight", "bold").css("border",
							"2px	 dotted lightgreen").css("border-radius",
							".5em");
				}, function() {
					$(this).css("font-weight", "normal").css("border", "none");
				});

	}
</script>
<body style="margin: 0px; padding: 0; border: 1">
	<div id="home"
		style="background-color: white; width: 0px; height: 0px; margin: 0px; display: none">&nbsp;</div>
	<div id="txcodeArea">
		<a href="#home" id='aa' style='color: red'></a>交易代號&nbsp;<input
			type="text" name="txcode" id="txcode" size="5" maxlength="5">
	</div>
	<div id=tabs>
		<%
			String attachText = (String)session
					.getAttribute(GlobalValues.SESSION_USER_ATTACH);
			String c12Text = (String)session
					.getAttribute(GlobalValues.SESSION_USER_C12);

			System.out.println("attach:" + attachText);
			System.out.println("c12:"+ c12Text);
			
			UserInfo userInfo =  (UserInfo)session.getAttribute(GlobalValues.SESSION_USER_INFO);
			
			TranListBuilder builder =  MySpring.getTranListBuilder();
			//Pair<String,java.util.Set<String>> pair =  builder.build(userInfo.getBrno(), userInfo.getId(), attachText, c12Text);
			//out.println(pair.first);
			
			//String content = GlobalValues.getMenuHtml();
			//out.print(content);
		%>
	</div>

	<div id="dashboard2"></div>
</body>
</html>
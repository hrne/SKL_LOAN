<%@page import="org.apache.commons.text.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ifx</title>
<link href="css/site.css" rel="stylesheet">
<script src="script/jquery.js"></script>
<script src = "script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>

<link rel=stylesheet type=text/css
      href="script/jqueryui/css/smoothness/jquery-ui-1.10.3.custom.css" />
      
<script>
<%
	String panel = StringEscapeUtils.escapeXml10(request.getParameter("d"));
	out.println("var _panel = '" + panel + "'");
	
%>
	function makeTranUrl(o, excludeList) {
		 var s= 'tran2.jsp?txcode=' + o['txcode'];
	 	 excludeList = excludeList || [];
		 
	 	 delete o['txcode'];
		 
		 $.each(excludeList, function(i,x){
			 delete o[x];
		 });
		 
		 for(var k in o) {
			 s += "&" +k + '='+ encodeURI(o[k]);
		 }
		 return s;
	}
	$(function() {
		initHeader();
	
		handle_txcode();
		initNav();
		$('#txcode').focus();
	});
	
	function initNav() {
				
	}
	function handle_txcode() {
		
		$('#txcode').change(function() {
			var s = $(this).val().toUpperCase();
			if(s.length == 5) {
				$(this).val('');
				var u = makeTranUrl({ txcode:s });
				change_src(u);
				
			}
		}).keypress(function(){
			var s = $(this).val();
			if(s.length==5) $(this).trigger("change");
		});
		
	}
	function initHeader() {
		
		if(_panel==='left') {
			$('#btn_left').show();
			$('#btn_left').on("click", function() {
				changeSize('left', 1);
			});
		}else {
			$('#btn_right').show();
			$('#btn_right').on("click", function() {
				changeSize('right', 1);
			});
		}
		
		
		//$("[id^=toolbar_btn]").button();
		$("#toolbar_btn_menu").button().click(function(){
			change_src($(this).attr("data-url")); 
		});			

		/*
		 $(".img-swap").on('click', function() {
			var m = "max";			 	
		 	if ($(this).attr("class") == "img-swap") {
				this.src = this.src.replace("_maximize","_restore");
				$(this).attr('title',"還原");
				
			} else {
				this.src = this.src.replace("_restore","_maximize");
				$(this).attr('title',"最大化");
				m = "";;
			}
			$(this).toggleClass("on");

		 	changeSize(_panel, m);
		});
		*/
	}
	
	function changeSize(fromPanel, max) {
		var p = parent;
		while (p && p.document.referrer && !p.toggleFrame)
			p = p.parent;
		if (p!= null && p.document.referrer){
			p.toggleFrame(fromPanel, max);
		}
		
	}
	
	function change_src(u) {
		//window.parent.go(u);
		//window.parent.frames["work"].location = u;
		$('#work',parent.document).attr({ src: u }); 
	}
	
	function getBuddyFn(name) {
		
		var grandPa = parent.parent,
			uncleTop = grandPa.frames[(_panel == 'left') ? 'right' : 'left'].frames['topnav'],
			fn = uncleTop[name];
		
		if(fn==null) {
			throw 'find no function ' + name + ' at the opposite side of ' + _panel;
		}
		return fn;
		
		
	}
	function handleChain(o){
		if (!o)
			return;
		
		var fn=null,
			action = o['action'],
			target = o['target'],
			excludeList = ['action', 'target'];
		
		if (action === 'run') {
			switch(target) {
			case 'new':
				fn = function() {
					var buddyFn = getBuddyFn('handleChain');
					o['target'] = 'self';
					buddyFn(o);
					changeSize(_panel, '1:1');
				}
				
				break;
			default:
				fn = function() {
					u = makeTranUrl(o, excludeList);
					change_src(u);
				};
				break;
			}
			setTimeout(function() {
				fn();

			}, 100);
		}
	}
</script>      
</head>
<body style="margin:0px;">

<div id="container2">
<span id="btn_right" style="display:none; left:0px;">
<img src="images/e_forward.gif"  title="向右移" class="img-swap">
</span> 
<span id="btn_left" style="display:none;position:absolute; right:0px;">
<img src="images/e_back.gif"  title="向左移" class="img-swap">
</span>

 
<span id="toolbar_btn_menu" data-url="menu.jsp" style="color:green"> Menu </span>
&nbsp;&nbsp;
	<span id="txcodeArea2">交易代號&nbsp;&nbsp;<input type="text" id="txcode" size="5" maxlength="5"></span>
	&nbsp;&nbsp;&nbsp;
	
</div>
</body>
</html>
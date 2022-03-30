<%@page import="com.st1.servlet.GlobalValues"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Ifx</title>
<%
if(session.getAttribute(GlobalValues.SESSION_SYSVAR)==null) {
	response.sendRedirect("login.jsp");
}
%>
<script src="script/fixIE.js"></script>
<script src="script/jquery.js"></script>
<script src="script/jquery.blockUI.js"></script>
<script src="script/underscore.js"></script>
<script src="script/ifx-global.js"></script>
<script src="script/ifx-dup.js"></script>
<script>

function block() {
	
	$.blockUI({ css: {
 		border: 'none',
 		padding: '15px',
 		backgroundColor: '#000',
 		'-webkit-border-radius': '10px',
 		'-moz-border-radius': '10px',   
 		opacity: .5, 
 		color: '#fff'  
		}});
}

function unblock() {
    $.unblockUI();
}


function go(u) {
	console.log(u);
	$("#work").attr('src',u); 
	
	//<frame id="work" src="menu.jsp" />
}
var theCols = "100%,0%";
function toggleFrame(fromPanel, max) {
	var $nav = $("#navFrameset");
	//alert("from:"+ fromPanel + ", size:"+ max);
	if(max=='1:1') {
		 $nav.attr("cols", '50%,50%');
		return;
	}
	theCols =  $nav.attr("cols");
	var cc = theCols.split(',');
	$.each(cc,function(i,x){
		if(x=="*") x = 0;
		cc[i]= parseInt(x, 10);
	});
	if(fromPanel=='left') {
		if(cc[0] <= 50) {
			cc[0] = 1;
			cc[1] = 99;
		}else{
			cc[0] = 50;
			cc[1]= 50;
		}
	}else {
		if(cc[1] <= 50) {
			cc[0] = 100;
			cc[1] = 0;
		}else{
			cc[0] = 50;
			cc[1]= 50;
		}
	}
	
	nCols = cc[0] + '%,' + cc[1] + '%';
		//var nCols = fromPanel == "left" ? "100%,*" : "*,90%";
	 $nav.attr("cols", nCols);
	

}

/*
var leftCols = "29.5%";
var rightCols = "70.5%";

function toggleFrame(title)
{
	var frameset = document.getElementById("helpFrameset"); 
	var navFrameSize = frameset.getAttribute("cols");
	var comma = navFrameSize.indexOf(',');
	var left = navFrameSize.substring(0,comma);
	var right = navFrameSize.substring(comma+1);

	if (left == "*" || right == "*") {
		// restore frames
		frameset.frameSpacing="3";
		frameset.setAttribute("border", "6");
		frameset.setAttribute("cols", leftCols+","+rightCols);
		notifyMaximizeListeners(false);
	} else {
		// the "cols" attribute is not always accurate, especially after resizing.
		// offsetWidth is also not accurate, so we do a combination of both and 
		// should get a reasonable behavior

		var leftSize = NavFrame.document.body.offsetWidth;
		var rightSize = ContentFrame.document.body.offsetWidth;

		
		leftCols = leftSize * 100 / (leftSize + rightSize);
		rightCols = 100 - leftCols;

		// maximize the frame.
		//leftCols = left;
		//rightCols = right;
		// Assumption: the content toolbar does not have a default title.

		if (title != "") // this is the left side for left-to-right rendering
			frameset.setAttribute("cols", "100%,*");
		else // this is the content toolbar
			frameset.setAttribute("cols", "*,100%");
	
		frameset.frameSpacing="0";
		frameset.setAttribute("border", "1");
		notifyMaximizeListeners(true);
	}
} */
</script>
</head>

<frameset rows="53,*" FRAMEBORDER=0 FRAMESPACING=0  FRAMESPACING=2>
   <frame id="head" name='head' src="head.jsp" noresize scrolling="no"/>
   <frameset id="navFrameset" cols="100%,*" FRAMEBORDER=1 border="2" FRAMESPACING=7 BORDERCOLOR="#CCFF99"  >
   		<frame src="leftpanel.jsp" id='left' name='left'>
   		<frame src="sidebar.jsp" id='right' name='right'>
   </frameset>
</frameset> 

</html>
<%@page import="com.st1.servlet.GlobalValues"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico">
<style type="text/css">>
html,body
{

  padding: 0px;
  margin: 0px;
}

</style>
<title>Ifx</title>
<%
  final Logger logger = LoggerFactory.getLogger("IFX-easy_main");

logger.info("session Id easy:"+session.getId());
logger.info("KE1:"+GlobalValues.SESSION_USER_INFO);
logger.info("KE2:"+session.getAttribute(GlobalValues.SESSION_USER_INFO));

if(session.getAttribute(GlobalValues.SESSION_USER_INFO)==null) {
	logger.info("redirect to index.jsp");
	response.sendRedirect("index.jsp");
}


%>
<script src="script/fixIE.js"></script>
<script src="script/jquery.js"></script>
<script src="script/jquery.blockUI.js"></script>
<script src="script/jquery.cookie.js"></script>
<script src="script/underscore.js"></script>
<script src="script/ifx-prt.js"></script>
<script src="script/ifx-global.js"></script>
<script src="script/ifx-dup.js"></script>

<script>
<%
out.println("var _tranEnv = " + GlobalValues.tranEnvJson + ";");
%>

$(function(){

	enableResize();
	__ifx_global.init(_tranEnv);

/*
    __ifx_global["part"]["tranEnv"] = {
    	'prt': new IfxPrt(jQuery, _tranEnv.printServiceUrl),
    	'unknownFormAction' : _tranEnv.unknownFormAction
    };
*/

});
function enableResize()
{
	parent.document.getElementById("easytab").noResize=false
	//parent.document.getElementById("msw").noResize=false
}


function go(u) {
	console.log(u);
	$("#work").attr('src',u);

	//<frame id="work" src="menu.jsp" />
}
var theCols = "90%,*";
function toggleFrame(fromPanel, max) {
	//alert("from:"+ fromPanel + ", size:"+ max);
	if(max == "max") {
		theCols = $("#navFrameset").attr("cols");
		var nCols = fromPanel == "left" ? "100%,*" : "*,90%";
		$("#navFrameset").attr("cols", nCols);
	}else {
		$("#navFrameset").attr("cols", theCols);
	}

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
/*
var refresh = false;
$(window).keydown(function(event) {
	  if (event.keyCode == 116) { // User presses F5 to refresh
	     refresh = true;
	  }
});
$(window).bind('beforeunload', function(){
    if (refresh == false) { // If F5 is not pressed
        return "確定1要關掉?";
    }
});
$(window).bind('unload', function(){
	alert("logout");
});

*/


function blockAWhile(message, timeout) {
	 $.blockUI({
		 message: message,
		 centerY: 0,
		 css: { top: '10px', left: '30px', right: '',width: '200px',  },
		 timeout: timeout
	 });
}
function block1() {
	$.blockUI(
            {
                message:'<p>正在處理....</p>',
                css: {
                    border: 'none',
                    padding: '15px',
                    backgroundColor: '#000',
                    '-webkit-border-radius': '10px',
                    '-moz-border-radius': '10px',
                    opacity: .5,
                    color: '#fff'
                }
            });
}

function unBlock() {
	$.unblockUI();
}

</script>

<script type="text/javascript">
//按保留會有問題..故直接先..
//window.onbeforeunload = function() {
//	return "確定要離開目前畫面? 所有交易畫面都會消失!!";
//};
window.onbeforeunload=null;
</script>

</head>


<frameset rows="58,*" FRAMEBORDER=0 FRAMESPACING=1>
   	<frame id="head" src="head.jsp" noresize scrolling="no"/>
	<frame id='easytab' src="easy_tab.jsp"  scrolling="no" noresize="false">

</frameset>

</html>

window.addEventListener("load", function() {

Modernizr.load([
    {
        load : [
			"script/fixIE.js",
			"script/jquery.js",
			"script/jquery.blockUI.js",
			"script/jquery.alerts.js",
			"script/json2.js",
			"script/ifx-core2.js",
			"script/ifx-fld.js",
			"script/ifx-rtn.js",
			"script/ifx-call.js",
			"script/help/Help.js",
			"script/help/errors.js",
			"script/help/ifx-help.js",
			"script/ifx-utl.js",
			"script/ifx-keys.js",
			"script/host/ifx-host.js",
			"script/display/ifx-display.js",
			"script/shareTool.js",
			"script/jqueryui/js/jquery-ui-1.8.19.custom.min.js",
			"script/easyui/jquery.easyui.min.js",
			"script/pdfjs-2.4.456/pdf.js"
        ],
        complete : function() {
        	starting_point();
        }
    }
]);

}, false);


function getSysvar(k) {
	return _sysvar[k];
}
function setSysvar(k, v) {
	return _sysvar[k] = v;
}
function makeIfx() {
	var tranDef = getTranDef();
	if(tranDef==null) alert("nukk");
	var ifx = new Ifx(_txcd, tranDef, _pfns, _sysvar, {
		"topbuttons" : "#topbuttons",
		"entry" : "#entryArea",
		"output" : "#outputArea",
		"help" : "#helpArea",
		"errmsg" : "#entry-errmsg"
	});
	return ifx;
}
var ifx;
function startRun() {
	//var host = new IfxHost(null);
	ifx = makeIfx();
	ifx.setup();
	ifx.display();
	ifx.addKeyFilter();
	ifx.collect();
	
	//ifx.printScreen();
	
	//ifx.dummytransmit();
	/*
	$('#fld_IIRTKD').numberbox({  
		    min:0,
	});  
	*/
	/*
	$("#fld_IIRTKD").keypress(function (e) { 
	    if (String.fromCharCode(e.keyCode).match(/[^0-9]/g)) return false; 
	}); 
*/
	
}


function startTest1() {
	var ifx = makeIfx();
	
	function testScreen() {
		var buf =ifxUI.renderScreen(ifx.def, function(tabIndex, x) {
			var fld = ifx.getField(x);
			return fld.display(tabIndex);
		});
		console.log(buf);
		
		$("#entryArea").append(buf);
		
		setTimeout(function(){
			var rtn = new IfxRtn(ifx);
			rtn.HIDE('n/a','#TC1','#TC2','#TC3');
		},3000);
		
	}
	function testPrint() {
		
		var buf =ifxUI.printScreen(ifx.def, function(tabIndex, x) {
			var fld = ifx.getField(x);
			if(fld.name=="#TT"){
				// textarea���C�L�ƪ�����ifx-fld�ӱ���
				return "TTTTT1\nTTTT2\nTTTT3";
			}
			return fld.name.slice(1).substr(0, fld.len);
		});
		console.log(buf);
		
		$("#outputArea").append("<pre>"+buf+"</pre>");
		$("#outputArea").show();
		
	}
	
	testPrint();
	testScreen();
	
}
function starting_point() {
	
//	initDashboard();
	initLeftBar('#dashboard2');
	startRun();
	//startTest1();
	
	//unloadScrollBars();
	function reloadScrollBars() { 
	    document.documentElement.style.overflow = 'auto';  // firefox, chrome 
	    document.body.scroll = "yes"; // ie only 
	} 
	 
	function unloadScrollBars() { 
	    document.documentElement.style.overflow = 'hidden';  // firefox, chrome 
	    document.body.scroll = "no"; // ie only 
	} 

};
function initDashboard(){
	
	$('#dashboard').load('head.jsp');
	$('#dashboard').hover(
	     function() {
			$(this).stop().animate(
			{
				top: '0' + $(window).scrollTop(),
			//	backgroundColor: 'rgb(255,255,255)'
			},
			500,
			'easeInSine'
			); // end animate
		 }, 
		 function() {
			 $(this).stop().animate(
			{
				top: -50 + $(window).scrollTop(),
				//backgroundColor: 'rgb(110,138,195)'
			},
			1500,
			'easeOutBounce'
			); // end animate
		 }
	  ); // end hover
}
function initLeftBar(divLeft) {
	var codes = ['I1000', 'G0230'],
		btnClass = 'speedKey';
	$.each(codes,function(i,x){
		$(divLeft).append('<div class="gray ' + btnClass + '">' + x + "</div>");
	});
	$("."+btnClass).bind('click',function(){
		window.location = "tran2.jsp?txcode=" + $(this).text();
	});
	$(divLeft).hover(
			 function() {
					$(this).stop().animate(
					{
						left: '0',
						backgroundColor: 'rgb(0,0,0)',
					},
					500,
					'easeInSine'
					); // end animate
				 }, 
				 function() {
					 $(this).stop().animate(
					{
						left: '-100px',
						backgroundColor: 'rgb(110,138,195)'
					},
					1500,
					'easeOutBounce'
					); // end animate
				 }
			  ); // end hover
	
	$('#btnLayout').on('change',function(x){
		alert($(this).val());
	});				  
}

$(window).scroll(function() { 
	console.log("scrollTop:"+ $(window).scrollTop() + ", top:"+$("#dashboard2").css("top"));
    $("#dashboard2").css("top", $(window).scrollTop() + 12); 
   // $("#dashboard").css("top", $(window).scrollTop() + (-50));
});


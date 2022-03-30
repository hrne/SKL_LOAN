// fix IE9 console log error 

//will set isIE to false in all browsers (because they ignore the comment),
//but it will be true in Internet Explorer, because of the negation ! in the
//conditional comment
// It’s as if IE sees:
//var isIE = !false; // true

//var isIE = /*@cc_on!@*/false;
//
//function fixIE9() {
//	var agent = navigator.userAgent;
//	var matchIE = /MSIE\s([\d]+)/;
//	var m = agent.match(matchIE);
//	
//	if(m!=null && m[1] != '10') {
//		if (!window.console) {console = {};} 
//		if (!console.log) {console.log = function() {};} 
//		if (!console.dir) {console.dir = function() {};}
//	}
//}
//
//
//fixIE9();
 
function fixIE(){
	window.console = window.console || {
	     log:function(){},
	     warn:function(){},
	     error:function(){},
	     dir:function(){}
	};
	if (typeof console.dir === "undefined") {
		console.dir = function() {};
	}
}

fixIE();
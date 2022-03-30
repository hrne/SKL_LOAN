// Swfcodeb.js
////////////////////////////////////////////////////////////////////
//須注意swift-generic.js 的 AMOUNT 有加入幣別為null時兩位00的判斷
var Swift = Swift || {};
(Swift.cur = function() {

var swfCodeb = new Array(
//允許小數點|幣別|全名		
"Y|ATS|AUSTRIAN BCHILLING|2" ,
"Y|AUD|AUSTRALIAN|2",
"N|BEC|BELGIAN FRANCE|0",
"N|BEF|BELGIAN FRANCE|0",
"Y|CAD|CANADIAN DOLLAR|2",
"Y|CHF|SWISS FRANCE|2",
"Y|CNY|CHINESE YUAN|2",
"Y|DEM|DEUTSCHE MARK|2",
"Y|DKK|DANISH KRONE|2",
"Y|EUR|EUROPEAN CURRENCY|2",
"Y|FRF|FRANCE|2",
"Y|GBP|POUND STERLING|2",
"Y|HKD|HONK KONG DOLLAR|2",
"Y|IDR|INDONESIAN RUPIAH|2",
"Y|INR|INDIAN RUPEE|2",
"N|ITL|ITALIAN LIRA|0",
"N|JPY|JAPANESE YEN|0",
"N|KHR|CAMBODIAN RIEL|0",
"Y|MYR|RINGGIT|2",
"Y|NLG|NETHERLANDS GUILDER|2",
"Y|NOK|NORWEGIAN KRONE|2",
"Y|NZD|NEW ZEALAND DOLLAR|2",
"Y|PCT||2",
"Y|PHP|PHILIPPINE PESO|2",
"Y|REN||2",
"Y|SAR|SAUDI ARABIAN RIYAL|2",
"Y|SEK|SWEDISH KRANC|2",
"Y|SGD|SINGARPORE DOLLAR|2",
"Y|THB|THAI BAHT|2",
"N|TWD|NEW TAIWAN DOLLAR|0",
"Y|USD|U.S. DOLLAR|2",
"Y|XEU|EUROPEAN CURRENCY|2",
"Y|ZAR|SOUTH AFRICAN RAND|2"
);


//////////////////////////////////////////////////////////////////
function MoneyCode(bFixed, c, n, format) {
	this.code = c;
	this.name = n;
	this.bFixedAllowed = bFixed;
	this.formatMoney = format;
	
	this.allowFixed = function allowFixed() { 
		return this.bFixedAllowed == "Y"; 
	};

	this.formatMoney = function formatMoney() {
	    return format;
	};
	
	this.toString = function toString() {
	    return this.bFixedAllowed + "|" + this.code + "|" + this.name ;
	};
}

codes = {};
curlist = [];
(function()
{
	console.log('init swift currency map');
	for(var i=0; i < swfCodeb.length; i++) {
		var ss = swfCodeb[i].split("|");
		codes[ss[1]]  = new MoneyCode(ss[0], ss[1], ss[2] , ss[3]);
		curlist.push(ss[1]);
	};
})();
	
function get(key) {
	try {
		return  codes[key];
	}
	catch(ex) { 
		return null;
	}
}
//新增 for 列印金額 add commas
function getCurlist() {
	try {
		return  curlist;
	}
	catch(ex) { 
		return null;
	}
}
function getName(key) {
	try {
		var mm=  codes[key];
		return mm.name;
	}
	catch(ex) { 
		return null;
	}
}

return {
	'get':get,
	'getCurlist':getCurlist,
	'getName':getName
};

}());
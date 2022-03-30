// shareTool.js

function amt2Chinese(value, pointNumber){
	var intUnit = "元拾佰仟萬拾佰仟億拾佰仟兆";
	var pointUnit = "角分";

	//var arr = value.split(".");
	//value = arr.join("");
	value = value.trim();
	var number = 0;
	
	number = parseFloat(value);
	
	if( number == 0 || value == ""){
		return "零元整";
	}
	var intPart= "0";
	var pointPart = "0";

/*
	if (n!= null && n >0) {
		intPart = value.substr(0, value.length - n);
		pointPart = value.substr(value.length-n);
	}
	else {
		intPart = value;
	}
	*/
	
	if (value.indexOf(".") < 0)  {
		if (pointNumber!= null && pointNumber >0) {
			intPart = value.substr(0, value.length - pointNumber);
			pointPart = value.substr(value.length-pointNumber);
		}
		else {
			intPart = value;
		}
	}
	else { // value contain decimal point
		var arr = value.split(".");
		intPart = arr[0];
		pointPart = arr[1].substr(0,2);  // max two number after decimal point
		
	}
	var s1="";

	
	intPart = intPart.trim();
	var commaArray = intPart.split(",");
	intPart = commaArray.join("");
	
	pointPart = pointPart.trim();
	
	
	// 整數部份
	intPart = ltrimZero(intPart);
	for(var i=0; i < intPart.length; i++) {
		var t = n2Big(intPart.charAt(i))
		if (t=="零")  {
			if (s1.charAt(s1.length-1) != "零") {
				s1=s1+ t;
			}
			continue;
		}
		var unit = intUnit.charAt(intPart.length-i -1);
		s1 +=   t + unit;
	}
	
	
	
	
	//小數部份
	pointPart=rtrimZero(pointPart);
	var s2="";
	if(pointPart.length != 0) {
		for(var i=0; i < pointPart.length; i++) {
			var t = n2Big(pointPart.charAt(i))
			if (t!="零")  {
				var unit = pointUnit.charAt(i);
				s2 += t + unit;
			}
		}
	}
	if (s2.length==0) s2 = "整";

	return s1+s2;
	
	function n2Big(n)
	{
		var scale = "零壹貳參肆伍陸柒捌玖";
		var ii = parseInt(n,0) - 0;
		return scale.charAt(ii);
	}
	
	
	
}


function amt2English(value, pointNumber)
{
	value = value.trim();
	if (parseFloat(value,10) == 0) {
		return "Zero";
	}
	
	var intPart= "0";
	var pointPart = "0";
	
	if (value.indexOf(".") < 0)  {
		if (pointNumber!= null && pointNumber >0) {
			intPart = value.substr(0, value.length - pointNumber);
			pointPart = value.substr(value.length-pointNumber);
		}
		else {
			intPart = value;
		}
	}
	else { // value contain decimal point
		var arr = value.split(".");
		intPart = arr[0];
		pointPart = arr[1].substr(0,2);  // max two number after decimal point
		
	}
	intPart = intPart.trim();
	var commaArray = intPart.split(",");
	intPart = commaArray.join("");
	pointPart = pointPart.trim();

	
	// 整數部份
	var s1="";
	intPart = ltrimZero(intPart);
	if(intPart.length > 0) {
	
		// 切成 3位數三位數  12345678--> 12  345 678
		
		var threePart  = new Array();
		for(var i=intPart.length;; )
		{
			var fromPos = (i - 3) > 0 ? (i-3) : 0;
		
			threePart[threePart.length] = intPart.substr(fromPos, i - fromPos);
			if(fromPos<=0) break;
			i = fromPos;
		}

		
		var unit = new Array( "", "thousand","million","billion", "trillion"  );
		var ttt;
		var pLen = threePart.length -1;
		for(var i=pLen; i >=0;i--)
		{
			ttt =get123(threePart[i]);
			
			if (i== pLen) {
				s1 += ttt;
				if(unit[i].length > 0) s1 +=   " " + unit[i];
			}
			else {	
				if (ttt.length > 0) {
					s1 += "," + ttt;
					if(unit[i].length > 0) s1 +=   " " + unit[i];
				}
			}
	
		}
		s1 += "";  //"dollars";
	
	}
	
		//小數部份
	var s2="";		
	pointPart=rtrimZero(pointPart);
	if (pointPart.length > 0) {
		s2  = "and cent ";
		s2 += get123(pointPart);
	}
	
	if (s1.length >0 && s2.length > 0) s1 += ",";
	s1 += s2;
	s1 = s1 + " only";
	s1 =s1.toUpperCase();
	return s1;
	
	
	function get123(v)
	{
		
		var oneArray = new Array("", "one", "two","three","four","five","six","seven","eight","nine",
			"ten","eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen" ,"twenty");
		var tenArray = new Array("twenty","thirty","forty","fifty","sixty","seventy","eighty","ninety") ;
		
		
		var s="";
		
		if(v.length == 3) {
			var tt = oneArray[parseInt(v.charAt(0),10) - 0 ];
			if (tt.length > 0) 
				s += tt + " hundred";
			v = v.substr(1);
		}
		var s2="";
		switch(v.length) {
			case 1:
				s2 =  oneArray[ parseInt(v,10) - 0  ] ; break;
			case 2:
				var i = parseInt(v,10);
				if (i <= 20) {
					s2 =  oneArray[i-0];
				}
				else {
					s2 =  tenArray[parseInt( v.charAt(0), 10) - 2 ]; // from 20
					if (v.charAt(1) != "0") {
						s2 += "-"
						s2 += oneArray[parseInt(v.charAt(1), 10) -0 ];
					}
				}
				break;
		}
		
		if(s2.length >0 )  {
			if (s.length>0)
				s += " and " + s2;
			else 
				s = s2;
		}
		return s;
	}

}	

function rtrimZero(s)
	{
		//var  re = /^0*/g;             // trim left-starting zeros  000012300--> 0000123
		//var re = /^[0]+|[0]+$/g;
		var re = /[0]+$/g;
		
//		var  re = /$0*/g;  
		return s.replace(re, "");

	}
	function ltrimZero(s)
	{
		var  re = /^0*/g;             // trim left-starting zeros  000012300--> 12300
		
		
//		var  re = /$0*/g;  
		return s.replace(re, "");

	}
	
function trimZero(s)
	{
		//var  re = /^0*/g;             // trim left-starting zeros  000012300--> 123
		var re = /^[0]+|[0]+$/g;
		
//		var  re = /$0*/g;  
		return s.replace(re, "");

	}
	
	
	String.prototype.ltrim = function()
	{
   	 // Use a regular expression to replace leading and trailing 
    	// spaces with the empty string
    	return this.replace(/(^\s*)|(\s*$)/g, "");
	}

String.prototype.rtrim = function()
	{
   	 // Use a regular expression to replace leading and trailing 
    	// spaces with the empty string
    	return this.replace(/(\s*$)/g, "");
	}	
	
	String.prototype.trim = function()
	{
		var s = this.rtrim();
		return this.ltrim();
	}
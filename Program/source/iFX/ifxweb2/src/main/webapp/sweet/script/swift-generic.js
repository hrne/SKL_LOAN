var Swift = Swift || {};
var _printifx;
(Swift.generic = function($) {
	var nonGeneric = {};

	function errmsg(code, pattern) {
		var s = pattern + '\n' + Swift.errors.get(code);
		return Swift.validators.errmsg(s);
	}
	function myError(code, p) {
		return Swift.validators.myError(code, p);
	}

	function isAlpha(value, pattern) {     //沒有人用這個function? 正規表缺^ +$?       小柯
		var re = /[a-z][A-Z]/;
		if (!re.test(value)) {
			return myError(1101, pattern);
		}
		return true;
	}
	function isCapitalAlpha(value, pattern) {
		var re = /^[A-Z]+$/;                         //小柯改 var re = /[A-Z]/
		if (!re.test(value)) {
			return myError(1001, pattern);
		}
		return true;
	}
	function isAlphaNumeric(value) {
		var re = /^[0-9a-zA-Z-_]+$/;
	    if (!re.test(value)) {
	        return myError(1127);
	    }
	    return true;
		//return value.match(re) != null;      //小柯 改v.match(re)
	}
	function isCommonGroup(msgtype) {
		val
		last2 = msgtype.slice(1);
		return last2 == "92" || last2 == "95" || last2 == "96";
	}
	 function isNumeric(value, pattern) {              // 0306小柯 START
		//var re = /^\d+\.?\d*$/;             
		var re = /^\d+$/;                     
		if (!re.test(value))                              
			return myError(1103, pattern);               
		return true;   
	}                                                  // 小柯END
	
	//function	isNumeric(value, pattern)                // 小柯 改START
	//{
	//	for(var i=0 ; i< value.length; i++)               
	//	{
	//		var oneChar = value.charAt(i);
	//		if (!(oneChar >="0" &&  oneChar <="9")) {          
	///			return myError(1103, pattern);
	//		}	
	//		
	//	}
	//return true;
	//}                                                    // 小柯 改END
	
	function isDigit(oneChar) {
		var re = /[\d]/;  
		if (!re.test(oneChar))
			return false;
		else
			return true;
	}
	function isABC(oneChar) {
		var re = /[a-zA-Z]/;
		return re.test(oneChar);
	}
	//統一問題 ’ 不等於  '
   function isCharacterSetX(value, pattern) {
   	//柯  新加入 冒號和 減號
	/*潘 新增檢核tab空白不可輸入*/
	   var re = /^[a-zA-Z0-9\/\:\-\?()\.,'\s+]*$/;
       var re2 = /[\t]/;
       var k = value.match(re);
       var k2 = value.match(re2);
       if(k!=null) {
       	if (k2 != null)
       	  return myError(2004, pattern);
       	else
       	  return true;
       }else {
       	return myError(2004, pattern);
       }
    }
   function isCharacterSetY(value, pattern) {
   	//柯
         var re = /^[A-Z0-9\/\=\:\-\?()\.,'\s+]*$/;    
        var k = value.match(re);
        if(k!=null) {
        	return true;
        }else {
        	return myError(2004, pattern);
        }
    }
   function isCharacterSetZ(value, pattern) {   	
        var re = /^[a-zA-Z0-9\/\=\#\<\>\{\:\-\?()\.,'\s+]*$/;   
        var k = value.match(re);
        if(k!=null) {
        	return true;
        }else {
        	return myError(2004, pattern);
        }
    }        
	function valDate(inp, len) {
		var testMo, testDay, testYr, inpMo, inpDay, inpYr, msg;
		if(isNaN(inp)){ //小柯 增加 數字判斷  (因parseInt 不保證和原始值相同if英文的話)
			return -1105;
			}
		if (len == 8 && inp.length == 8) { // yyyymmdd
			inpYr = parseInt(inp.substring(0, 4), 10);
			inpMo = parseInt(inp.substr(4, 2), 10);
			inpDay = parseInt(inp.substr(6, 2), 10);
		} else if (len == 6 && inp.length == 6) { // yymmdd
			inpYr = parseInt(inp.substring(0, 2));
			if (inpYr > 79) // partIII page 9
				inpYr += 1900;
			else
				inpYr += 2000;
			if (inpYr > 2060) {
				return -50;
			}

			var mm = inp.substr(2, 2);
			inpMo = parseInt(mm, 10);
			mm = inp.substr(4, 2);
			inpDay = parseInt(mm, 10);

		} else {
			return -1105;
		}
		//alert(inpMo+"  "+inpDay+"  "+inpYr);
		if (inpYr == 0) {
			// T50;
			return -50;
		}
		// make sure parseInt() succeeded on input components
		if (isNaN(inpMo) || isNaN(inpDay) || isNaN(inpYr)) {
			return -1105;
		}

		var ds = inpMo + "/" + inpDay + "/" + inpYr;
		// _fvalAlert(ds);
		var testDate = new Date(inpMo + "/" + inpDay + "/" + inpYr);
		// _fvalAlert(testDate +"," + inpYr);
		// extract pieces from date object
		testMo = testDate.getMonth() + 1;  		//小柯補 ;
		testDay = testDate.getDate();					//小柯補 ;
		testYr = testDate.getFullYear();			//小柯補 ;

		// make sure conversion to date object succeeded
		if (isNaN(testMo) || isNaN(testDay) || isNaN(testYr)) {
			return -1105;
		}
		// make sure values match
		if (testMo != inpMo || testDay != inpDay || testYr != inpYr) {
			// _fvalAlert("testMo:" + testMo + " inp:" + inpMo);
			// _fvalAlert("testd:" + testDay + " inp:" + inpDay);
			// _fvalAlert(len);
			return -1105;
		}
		return 0;
	}

	// ------------------------------------------------------------------
	
	nonGeneric.f_a = function f_a(value, minLen, maxLen, pattern) {
		// a:alphabetic capital letters (A through Z), upper case only
		if (minLen > 0 && !value) {
			return myError(2006, pattern);
		}
		if (value.length < minLen || value.length > maxLen) {
			return myError(2003, pattern);
		}
		return isCapitalAlpha(value, pattern);
	};

	nonGeneric.f_c = function f_c(value, minLen, maxLen, pattern) {
		// c : alpha-numeric capital letters (upper case), and digits only
		if (minLen > 0) {
			if (!value) {
				return myError(2006, pattern);
			}
		}else {if (!value){ return true;}  }     //小柯改   minlen = 0且 value沒有值
		
		if (value.length < minLen || value.length > maxLen) {
			return myError(2003, pattern);
		}
		return isAlphaNumeric(value, pattern);
	};
	nonGeneric.f_e = function f_e(value,pattern) {
		// e : blank or space
				var re = /[\s]/;  
		if (!re.test(value))
			return myError(2006, pattern);
		else
		return true;
	};
	nonGeneric.f_multiXYZ = function f_multiXYZ(value, row, col, pattern,
			characterSet, bFirstOption) {
		value = ReplaceTextArea2(value);
		// first line must input
		if (bFirstOption != null && bFirstOption == true) {
			var arr = value.split("\n");
			if (arr[0].length == 0) {
				return myError(-1137, pattern);
			}
		}
		switch (characterSet) {
		case "y":
			return nonGeneric.f_y(value, 0, row * col, pattern);
			break;
		case "z":
			return nonGeneric.f_z(value, 0, row * col, pattern);
			break;			
		default: // "x"
			return nonGeneric.f_x(value, 0, row * col, pattern);
			break;
		}
	}

	nonGeneric.f_n = function f_n(value, minLen, maxLen, pattern) {
		// n : numeric digits (0 through 9) only
		if (minLen > 0 && !value) {
			return myError(2006, pattern);
		}
		if (value.length < minLen || value.length > maxLen) {
			return myError(2003, pattern);
		}
		return isNumeric(value, pattern);
	};

	nonGeneric.f_x = function f_x(value, minLen, maxLen, pattern) {
		if (minLen > 0 && !value) {
			return myError(2006, pattern);
		}
		if (value.length < minLen || value.length > maxLen) {
			return myError(2003, pattern);
		}
		//return true;
		return isCharacterSetX(value, pattern);
			
	};
		nonGeneric.f_y = function f_y(value, minLen, maxLen, pattern) {
		if (minLen > 0 && !value) {
			return myError(2006, pattern);
		}
		if (value.length < minLen || value.length > maxLen) {
			return myError(2003, pattern);
		}
		//return true;
		return isCharacterSetY(value, pattern);
			
	};
		nonGeneric.f_z = function f_z(value, minLen, maxLen, pattern) {
		if (minLen > 0 && !value) {
			return myError(2006, pattern);
		}
		if (value.length < minLen || value.length > maxLen) {
			return myError(2003, pattern);
		}
		//return true;
		return isCharacterSetZ(value, pattern);
			
	};
	

	// ============================================================
	// Generic
	// ============================================================
	var generic = {};

	// see partII 4.6 <Number> <Amount>
	// <NIP> Nn - integer part of the number component
	// <NDP> [Nn] - decimal seperator part of the number component
	// the number component <NBR> = <NIP>,<NDP>

	generic.AMOUNT = function genericAmount(oMoney, value, len, pattern,
			bNegative) {
				//紀錄原始值
				var orgvalue = value;
				
		if (!value && value != 0)
			return myError(1113, pattern);

		var re = /^[\d|,]*$/;
		if (!re.test(value)) {
			return myError(1112, pattern);
		}

		value = trimMoney(value);
		if (bNegative != null && bNegative) {
			if (value == "0")
				return myError(1121, pattern);
		}

		var ss = value.split(",");
		var nCommas = ss.length - 1;
    var formatMoney = oMoney ? oMoney.formatMoney():0;
		if (nCommas == 0) { // 補上comma
			if (value.length >= len-formatMoney) { // 太長了
				return myError(1123, pattern);
			}
			value = value + ",";
		}
	    if (oMoney != null) {
		
		if (!oMoney || (oMoney.allowFixed())) { // 允許小數點後
			if (nCommas > 1) {
				return myError(1111, pattern);
			}
			var sslen=0;
			if(ss[1]){
				sslen = ss[1].length;
				if(ss[1].length > formatMoney){
					return myError(9999, "該幣別小數點位數錯誤");
	                }
	     }
	     //柯 新加入如果可以有小數，則補滿小數位數"0"
	     for(var i=sslen;i < parseInt(formatMoney,10);i++){
					value = value + "0";
			}      

		} else { // 只允許整數
			if (nCommas != 0 && ss[1]) {
				return myError(1110, pattern);
			}
		}
		
	    }else{
	    	//如果等於null...for MT203 TAG19
	    	if(orgvalue == value + "00"){
	    		value = value + "00";
	    	}
	    	
	    	}
	    
    
		return value;

		function trimMoney(s) {
			var re = /^0*/g; // trim left-starting zeros 000012300--> 12300
			var r = s.replace(re, "");
			if (r.indexOf(",") != -1) {
				re = /0*$/g; // trim right-ending zeros 0001230000> 000123
				r = r.replace(re, "");
			}
			// _fvalAlert("in trimMoney:" +r);
			if (r == "" || r == ",")
				r = "0";
			if (r[0] == ",") {
				r = "0" + r;
			}
			return r;
		}
	};
	generic.NUMBER = generic.AMOUNT;

	// 呼叫者必須先將value轉成大寫
	generic.CUR = function genericCur(value, checkSign) {
		if (!value) {
			return myError(1109, "<CUR>");
		}

		if (checkSign == true && value[0] == "N") {
			value = value.slice(1);
		}
		var moneyDef = Swift.cur.get(value.substr(0, 3));
		if (moneyDef == null) {
			return myError(-1109, "<CUR>");
		}
		return moneyDef;
	};

	generic.DATE = function genericDate(value, datePattern, tagName) {
		// <DATE1> MMDD
		// <DATE2> YYMMDD
		// <DATE3> YYMM
		// <DATE4> YYYYMMDD
		// <DATE5> YYYY

		if (!value) {
			return myError(1136, "<DATE>");
		}
		if (!isNumeric(value, "<" + datePattern + ">")) {    //小柯   isDigit 改  is num
			return myError(1105, "<" + datePattern + ">");    // 小柯  1141改 1105
		}

		try {
			switch (datePattern) {
			case "DATE1": // MMDD
				var yyyy;
				// partII 4.1
				// in field 61: the year value is selected from the system date
				// at the time the
				// validation is performed.

				var d = new Date();
				//更改為 getFullYear 112 -> 2012
				yyyy = d.getFullYear();
				var retval = valDate(yyyy.toString() + value.toString(), 8);
				if (retval < 0) return myError(1141, "<DATE1>");
				break;
			case "DATE2": // YYMMDD
				var retval = valDate(value, 6);
				if (retval < 0) {
					return myError(-retval, "YYMMDD");
				}
				break;
			case "DATE3": // YYMM

				var yy = value.substr(0, 2);//拿掉parseInt
				var mm = value.substr(2, 2);//拿掉parseInt
				if (isNaN(yy) || yy < 0) {
					return myError(1128, "<DATE3>");
				}
				if (isNaN(mm) || mm < 0 || mm > 12) {
					return myError(1128, "<DATE3>");
				}
				;
				break;
			case "DATE4": // YYYYMMDD
				var retval = valDate(value, 8);
				//alert(retval);
				if (retval < 0) {
					return myError(-retval, "<DATE4>")
				}
				;
				break;
				
			case "DATE5": // YYYY
				var retval = valDate(value +"1111", 8);
				if (retval < 0) {
					return myError(9999, "YYYY")
				}
				;
				break;	
							
			default:
				return myError(2000, "unknown date type:" + datePattern);
			}
			return true;
		} catch (ee) {
			return myError(9999, datePattern + '(' + ee + ')');
		}
	};

	generic.DC = function genericDC(value) {
		// <DC> ‘D’ | ‘C’

		if (value != "D" && value != "C") {
			return myError(-51, "<DC>");
		}
		return true;
	};
	generic.FLD50F = function(value) {
//		The first line of Field :50F: must be formatted as follow: 
//			:50F: <”/”34x> | <4!a”/”30x> <CrLf> 
	};
	
	
	generic.HHMM = function genericHHMM(value) {
		var retval = true;
		if (!value || value.length != 4) {
			retval = false;
		} else {
			var hh = value.substr(0, 2);// parseInt(value.substr(0, 2), 10);
			var mm = value.substr(2, 2); //原本parseInt 可傳入英文
			if (isNaN(hh) || isNaN(mm)) {
				retval = false;
			}
			if (hh < 0 || hh > 23) {
				retval = false;
			}
			if (mm < 0 || mm > 59) {
				retval = false;
			}
		}

		if (!retval)
			return myError(16, "<HHMM>");
		else
			return true;

	};

	// <IBAN> may be required in the following fields letter options:
	// Refer Part IV, Rule 119
	// 59 ’/’<IBAN>’CRLF’35x[’CRLF’35x]0-3
	// 59A ’/’<IBAN>’CRLF’
	// <SWIFTBIC>|<NON-SWIFTBIC>
	generic.IBAN = function(value) {
		// IBAN Structure: 2!A2!n1!B[B]0-29
		// 2 alphabetic characters followed by 2 numeric characters, followed by
		// 1 or up to
		// maximum 30 alphanumeric characters.
		
//		New character type: 
//			A : alphabetic character, upper or lower case 
//			B : alphanumeric character, alphabetic character may be upper or lower case 
//			 
	};
	generic.INSTR = function(value) {
		// <INSTR>:
		// (’/’1!c’/’[32x])|(’/’2!c’/’[31x])|(’/’3!c’/’[30x])|(’/’4!c’/’[29x])|
		// (’/’5!c’/’[28x])|(’/’6!c’/’[27x])|(’/’7!c’/’[26x])|(’/’8!c’/’[25x])
		return true;
	};

	generic.FLD72 = function(value) {
		// <FLD72>: <INSTR>
		// [’CRLF’(<INSTR>|’//’33x)]0-5
	};
	generic.FLD72_not_STP = function(value) {
		// <FLD72>: <INSTR>
		// [’CRLF’(<INSTR>|’//’33x)]0-5
	};
	generic.FLD72_STP = function(value) {
		// <FLD72>: <INSTR>
		// [’CRLF’(<INSTR>|’//’33x)]0-5
	};

	generic.MT = function(value, errmsg) {
		if (value.length == 3 && !isNaN(value)
				&& (value >= 100 && value <= 999)) {
			return true;
		}
		errmsg = errmsg || "";
		if (errmsg)
			errmsg = "\n" + errmsg;
		return myError(18, "<MT>" + errmsg);
	};

	generic.OFFSET = function genericOffset(value) {
		/*
		 * 4.26 <OFFSET> The < OFFSET > function checks the time component, it
		 * is similar to <HHMM> function where the validation is as follows: 00<=HH<=13
		 * 00<=MM<=59 Standard presentation: <HHMM> format: 4!n The code error
		 * ”T16” is returned if the <OFFSET> compo nent is invalid.
		 */
		if (!value) {
			return myError(16, "OFFSET未輸入");
		}
		if (isNaN(value) || value.length != 4)
			return myError(16, "OFFSET輸入錯誤");

		var hh = value.slice(0, 2);
		var mm = value.slice(2);
		if (!(hh >= 0 && hh <= 13) || !(mm >= 0 && mm <= 59)) {
			return myError(16, "OFFSET輸入錯誤");
		}
		return true;

	};

	  generic.RATE = function genericRate(value, len) {     //小柯 改
		if (!value) {
			return myError(3001, "<RATE>");
		}
		if (value.length > len) {
			return myError(2003, "太長");
		}
		// TODO: check RATE
		return generic.AMOUNT(null, value, len, "<RATE>");
	};
	
	  generic.N_RATE = function genericRate(value, formatN) {     //[N]
	  	var formatN,v1,v2,v3,v4;
	  	if(formatN[1] == "N"){
	 	 		if (value[0] == "N") {
					v1 = value[0].toUpperCase();
	  			v2 = value.slice(1);
	  			if(v2 == 0 || v2 == ""){
	  				return myError(9999, "格式有誤");	
	  			}
	  			v2 = generic.AMOUNT(null, v2, formatN.slice(3), "<RATE>");
					//v2 = generic.RATE( v2, formatN.slice(3));
				if (v2 === false){
					return myError(9999, "格式有誤");		}	
					v2 = v1 + v2;
				}else{
					v2 = generic.AMOUNT(null, value, formatN.slice(3), "<RATE>");
					//v2 = generic.RATE( value, formatN.slice(3));
					if (v2 === false)
					return myError(9999, "格式有誤");										
					}	  		
	  	}
	  	else{
	  		  v2 = generic.AMOUNT(null, value, formatN, "<RATE>");
	  			//v2 = generic.RATE( value, formatN);
					if (v2 === false)
					return myError(9999, "格式有誤");					
	  	}
	  		
			return v2;
	};	

	// see partII 4.8 SB - LC
	// The SB-LC function checks the validity of field 22, subfield 2, and field
	// 22C, this field or
	// subfield contains three components, which are defined as follow:
	// 1st component : <SB1><LC1>
	// 2nd component : 4!n
	// 3rd component : <SB2><LC2>
				generic.SB_LC = function genericSbLc(value) {                                               //小柯改start
					var pattern = "4!a2!c4!n4!a2!c";
					var anyError;
					//var arr =  mySplit(value, 4 ,2, 4, 4, 2);
					if(value.length != 16)
					{
						return myError(1102, "4!a2!c4!n4!a2!c");
					}

					anyError = !nonGeneric.f_a(value.substr(0,4), 4, 4, "4!a" + " of " + pattern)
					|| !nonGeneric.f_c(value.substr(4,2), 2, 2, "2!c" + " of " + pattern)
					|| !nonGeneric.f_n(value.substr(6,4), 4, 4, "4!n" + " of " + pattern)
					|| !nonGeneric.f_a(value.substr(10,4), 4, 4, "4!a" + " of " + pattern)
					|| !nonGeneric.f_c(value.substr(14,2), 2, 2, "2!c" + " of " + pattern);
					return !anyError;
				};
				//generic.SB_LC = function genericSbLc(msgtype, value) {
				//	if (isCommonGroup(msgtype)) {
				//		// 1st component : 4!a 2!c
				//		// 2nd component : 4!n
				//		// 3rd component : 4!a 2!c
				//		// IF 2nd_component = <nnn>"0"
				//		// THEN IF 2nd_component NOT= ("0000" | <nn>"10")
				//		// THEN <errror_T63>
				//		// When the last character of the component 2 consists of a ‘0’, and
			// 1st component : 4!a 2!c
			// 2nd component : 4!n
			// 3rd component : 4!a 2!c
			// IF 2nd_component = <nnn>"0"
			// THEN IF 2nd_component NOT= ("0000" | <nn>"10")
			// THEN <errror_T63>
			// When the last character of the component 2 consists of a ‘0’, and
			// its preceding character
			// does not consist of a ‘1’, then the entire component 2 must
			// consist of ‘0000’, if this control
			// is negative, the function will issue a T63 error code
			// 1st component : <SB1><LC1>
			// <SB1> = 4!a representing a bank code,
			// <LC1>= 2!c representing the location code of the message
			// originator or receiver
			// 2nd component : 4!n
			// 3rd component : <SB2><LC2>
			// <SB2> = 4!a representing the bank code of the other destination
			// <LC2>= 2!c representing the location code of the other
			// destination

	generic.SIGN = function genericSign(value) {   
		/*
		 * 4.25 <SIGN> The <SIGN> function checks the sign component value: '+'
		 * or '-'. Standard presentation: < SIGN > format: 1 ! x The code error
		 * ”T15” is returned if the <SIGN> component is not '+' or '-'.
		 */
		if (!value || (value != "+" && value != "-")) {
			return myError(15, "<SIGN>");  
		}
		return true;
	};
	generic.SUB_6 = function(value) {
		// n subfield 6 in field 61
		// The first character of the component must be either ‘S’, ‘N’ or ‘F’.
		// If the first character is ’S’, the last three characters must be
		// validated by the <MT> function.
		// If the first character is ‘N’ or ‘F’, the last three characters must
		// consist of alphanumerics.
		var firstCh = value[0];
		if ("SNF".indexOf(firstCh) != -1) {
			if (firstCh == 'S') {
				return generic.MT(value.slice(1),
						"the last three characters must be Message Type");
			} else {
				if (!isAlphaNumeric(value.slice(1))) {
					return myError("the last three characters must consist of alphanumerics");
				}
			}
		}
		return true;
	};
	// see partII 4.3 <SWIFTBIC> and <NON-SWIFTBIC> used in copy field(s) of MTs
	// n92, n95, n96
	// Bank code : 4!a four alphabetic characters (fixed length).
	// Country code : 2!a two alphabetic characters (fixed length).
	// Location code 1 : 1!c one alphanumeric character, digits ‘0’ and ‘1’ not
	// allowed.
	// Location code 2 : 1!c one alphanumeric character, letter ‘O’ not allowed.
	// Branch code : [3!c] optional, three alphanumeric characters.
	// The code ‘BIC’ is not allowed.
	// The letter ‘X’ is not allowed as the first letter, unless it is used in
	// ‘XXX’.
	// 4.4 <SWIFTBIC> and <NON-SWIFTBIC> used in other than:copy field(s) of MTs
	// n92, n95, n96
	// 6!a<LC>[3!x]

	var _bicCache = {}; // 增加此行
	//generic.getBicFromCache = function(dest){
	//	if (_bicCache[dest] != undefined){
	//		throw "no suck dest:"+dest;
	//	}
	//	return +bicCache[dest];
	//};
	  generic.getBicFromCache = function(dest){
        if (_bicCache[dest] != undefined){
        	return _bicCache[dest];
            
        }
        //throw "no suck dest:"+dest;
        return false;
    };
 generic.setIfx = function ( ifx) {
 	console.log("Set  ifx in generic!");
 	 _printifx = ifx;
};    

    generic.GETSWIFTBIC = function genericSwiftBic( msgtype,
			dest) {
		if (dest[0] == '/') {
			return "";
		}
		if (dest.length != 8 && dest.length != 11) {
			return "";
		}

		var text = "";
		text = IfxUtl.stringFormatter(dest,11,false) + IfxUtl.stringFormatter(msgtype,4,false);
			text += "$PRINT"; //應該也不需要有警告訊息
		// 增加這一段
		if(_bicCache[dest] != undefined) {
		    return _bicCache[dest];
		}
		
		function onOK() {
	      _bicCache[dest] = _printifx.getValue('#it9');//ifx.getValue('#it9');  //<===增加此行
	      return _bicCache[dest];
		}
		function onError(errmsg, type) {
				return "";
		}
		Swift.validators.trigger('swift_fn_XWR01', text, onOK, onError);

	};
	generic.SWIFTBIC = function genericSwiftBic(ifx, $currentField, msgtype,
			dest) {
		// 4.3 S.W.I.F.T. Address: <SWIFTBIC>
		// Standard presentation: <SWIFTBIC>::= <SWIFTDESTINATION>[3!c]
		// <SWIFTDESTINATION>::= 8 char. 6!a<LC> dest. recorded in the SWII db.
		// as either:

		if (dest[0] == '/') {
			return myError(2002, "<SWIFTBIC>");
		}
		if (dest.length != 8 && dest.length != 11) {
			return myError(2003, "<SWIFTBIC>");
		}

		var text = "";
		text = IfxUtl.stringFormatter(dest,11,false) + IfxUtl.stringFormatter(msgtype,4,false);
//		text += "$";
		text += "$PRINT";//潘 20180201
		// 增加這一段
		if(_bicCache[dest] != undefined) {
		    ifx.setValue('#it9',_bicCache[dest]);
		    onOK();
		    return;
		}

		
		function onOK() {
			Swift.ui.externalSetValDone($currentField);
			addBuddy(ifx.getValue('#it9'));
	        _bicCache[dest] = ifx.getValue('#it9');  //<===增加此行
			Swift.validators.goNext();
		}
		function onError(errmsg, type) {
			if(type=='W'){
				Swift.validators.goNext();
				return;
			}
			Swift.validators.myError(9998, errmsg);
		}
		function addBuddy(bankName) {
			var id = $currentField.attr('id');
			var buddyId = '_buddy' + id;
			$('#' + buddyId).remove();
			if (bankName) {
				// $('<div class="swift_field_buddy" id="' + buddyId + + '">' +
				// bankName + '</div>').insertAfter(currentField);
				var bb = $(
						"<div class='swift_field_buddy' id='" + buddyId + "'>"
						+ $.trim(bankName) + "</div>");
				
				//$currentField.parent().append(bb);
				bb.insertAfter($currentField.next());
			}
		}
		addBuddy();
		Swift.validators.trigger('swift_fn_XWR01', text, onOK, onError);

		// if (toms.isErrorTom()) {
		// return myError(-9998, toms.getErrorTom());
		// }

		// throw myError(-9998, tom.text);

	};

	generic.TIME2 = function genericTime2(value) {
		// 4.2
		// nonGeneric.f_n(value, 6, 6, "<TIME2>");
		var re = /^\d{6}$/;
		if (!value.match(re)) {
			return myError(38, "<TIME2>");
		}
		var hh = value.slice(0, 2);
		var mm = value.slice(2, 4);
		var ss = value.slice(4, 6);
		if ((hh >= 0 && hh <= 23) && (mm >= 0 && mm <= 59)
				&& (ss >= 0 && ss <= 59)) {
			return true;
		}
		return myError(38, "<TIME2>");
	};

	generic.TIME3 = function genericTime2(value) {
		var hh, mm;
		var notOK = true;
		switch (value.length) {
		case 2:
			hh = value;
			if (hh >= 0 && hh <= 23) {
				notOK = false;
			}
			break;
		case 4:
			if (!isNaN(value)) {
				hh = value.slice(0, 2);
				mm = value.slice(2);
				if ((hh >= 0 && hh <= 23) && (mm >= 0 && mm <= 59)) {
					notOK = false;
				}
			}
			break;
		}
		return notOK ? myError(38, "<TIME3>") : true;
	};
	generic.YEAR = function genericTime2(value) {
		var re = /^\d{4}$/;
		if (!value.match(re) || value == "0000") {
			return myError(50, "<YEAR>");
		}
		return true;
	};
	return {
		'generic' : generic
		,'nonGeneric' : nonGeneric
	//	'getBicFromCache': getBicFromCache,
	};
}(jQuery));

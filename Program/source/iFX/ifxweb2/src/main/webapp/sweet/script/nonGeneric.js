// nonGeneric.js
/*
Field Type
n      -   Digits
 d      -   Digits with decimal comma
 h      -   Uppercase hexadecimal
 a      -   Uppercase letters
 c      -   Uppercase alphanumeric
 e      -   Space
 x      -   S.W.I.F.T. character set
 y      -   Upper case level A ISO 9735 characters
 z      -   S.W.I.F.T. extended character set
*/

/* 
Field Length
nn : maximum length (minimum is 1)
nn-nn : minimum and maximum length
nn! : fixed length
nn*nn : maximum number of lines times maximum line length
*/

if(!window.nonGeneric)
{
	nonGeneric = new Object;
	
	nonGeneric.f_a = function f_a(value, minLen, maxLen, pattern)
	{
		// a:alphabetic capital letters (A through Z), upper case only
		if (minLen > 0 ) {
			if (value == null || value.length==0) {
				throw myError(-2006, pattern); 
			}
		}
		if( value.length < minLen || value.length > maxLen) {
			throw myError(-2003, pattern); 
		}
		isAlpha(value, pattern);
		value = value.toUpperCase();
		return value;
	}
	nonGeneric.f_A = function f_A(value, minLen, maxLen, pattern)
	{
		// A : alphabetic,upper case or lower case A through Z, a through z
	}
	nonGeneric.f_B = function f_B(value, minLen, maxLen, pattern)
	{
		// B : alphanumeric upper case or lower case A through Z, a through z and 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
	}
	
	nonGeneric.f_c = function f_c(value, minLen, maxLen, pattern)
	{
		//	c : alpha-numeric capital letters (upper case), and digits only
		if (minLen > 0 ) {
			if (value == null || value.length==0) {
				throw myError(-2006, pattern); 
			}
		}
		if( value.length < minLen || value.length > maxLen) {
			throw myError(-2003, pattern); 
		}
		isAlphaNumeric(value, pattern);
		value = value.toUpperCase();
		return value;
	}
	nonGeneric.f_h = function f_h(value, minLen, maxLen, pattern)
	{
		//	h : hexadecimal letters A through F (uppercase), and digits only
		return value.toUpperCase();
		
	}
	nonGeneric.f_e = function f_e(value, minLen, maxLen, pattern)
	{
		//e : blank or space
	}
	nonGeneric.f_n = function f_n(value, minLen, maxLen, pattern)
	{
		// n : numeric digits (0 through 9) only
		if (minLen > 0 ) {
			if (value == null || value.length==0) {
				throw myError(-2006, pattern); 
			}
		}
		if( value.length < minLen || value.length > maxLen) {
			throw myError(-2003, pattern); 
		}
		isNumeric(value, pattern);
	}
	nonGeneric.f_x = function f_x(value, minLen, maxLen, pattern)
	{
		if (minLen > 0 ) {
			if (value == null || value.length==0) {
				throw myError(-2006, pattern); 
			}
		}
		//	alert(value.length + "\n" + value);
		if( value.length < minLen || value.length > maxLen) {
			throw myError(-2003, pattern); 
		}
	//	isCharacterSetX(value, pattern);
	} 
	
	nonGeneric.f_z = function f_z(value, minLen, maxLen, pattern)
	{
		if (minLen > 0 ) {
			if (value == null || value.length==0) {
				throw myError(-2006, pattern); 
			}
		}
		if( value.length < minLen || value.length > maxLen) {
			throw myError(-2003, pattern); 
		}
		isCharacterSetZ(value, pattern);
	} 
	
	nonGeneric.f_multiXYZ = function f_multiXYZ(value, row, col, pattern, characterSet, bFirstOption)
	{
			value  =ReplaceTextArea2(value);
			// first line must input
			if (bFirstOption != null && bFirstOption == true) {
				var arr = value.split("\n");
				if (arr[0].length ==0) {
					throw myError(-1137, pattern); 
				}		
			}
			switch(characterSet) {
				case "z" :
					nonGeneric.f_y(value, 0, row * col, pattern); break;
				default: // "x"
					nonGeneric.f_x(value, 0, row * col, pattern); break;
			}
	}
	
	
} // end of if !window.nonGeneric


function isCharacterSetX(value,pattern)
{
/// - ? : ( ) . , ’ + SPACE CrLf
// TODO ebcdic
	for(var i=0; i < value.length; i++) { 
		if (value.charAt(i) == "t") { throw myError(-2004, pattern); }
	}

}

function isCharacterSetZ(value,patter)
{
/// - ? : ( ) . , ’ + SPACE CrLf
// TODO ebcdic
	for(var i=0; i < value.length; i++) { 
		if (value.charAt(i) == "t") { throw myError(-2013, pattern); }
	}

}
function	 isAlpha(value, pattern)
{
	for(var i=0 ; i< value.length; i++)
	{
		var oneChar = value.charAt(i);
		if (!((oneChar >= "a" && oneChar <= "z") || (oneChar >= "A" && oneChar <= "Z")) ) {
			throw myError(-1101, pattern);
		}	
			
	}
	return 0;
}


function	 isAlphaNumeric(value, pattern)
{
	for(var i=0 ; i< value.length; i++)
	{
			var oneChar = value.charAt(i);
		if (!((oneChar >= "a" && oneChar <= "z") || (oneChar >= "A" && oneChar <= "Z")) && !(oneChar >="0" && oneChar <="9")) {
			throw myError(-1127, pattern);
		}	
			
	}
	return 0;
}
function	isNumeric(value, pattern)
{
	for(var i=0 ; i< value.length; i++)
	{
		var oneChar = value.charAt(i);
		if (!(oneChar >="0" &&  oneChar <="9")) {
			throw(new myError(-1103, pattern));
		}	
			
	}
	return 0;
}
function isDigit(oneChar)
{
		if (!(oneChar >="0" &&  oneChar <="9")) {
			return false;
		}
		return true;
}
function isABC(oneChar)
{
	if (!((oneChar >= "a" && oneChar <= "z") || (oneChar >= "A" && oneChar <= "Z")) && !(oneChar >="0" && oneChar <="9")) {
		return false;
	}
	return true;
}
	

function test123333333(){
	var rs;
	var s = "*"
	var re = new RegExp("[*|&|>|<|%|$|#|@]+", "gi");
	if (re.test(s)) //Test for existence.
		_fvalAlert( " contains "); //s contains pattern.
	else
		_fvalAlert(" does not contain "); //s does not contain pattern.
}

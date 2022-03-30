var Swift = Swift || {};
(Swift.errors = function($) {
	var Terror = {};
	
	Terror.T15 = "Available Sign is not valid. <SIGN> must be either '+ ' or '- '";
	Terror.T16  = "Available Time offset is not val i d . < OFFSET > has the same format as time <HHMM> .\n" +
							" It must be : 00<=HH<=23 and 00<=MM<=59 ";
							
	Terror.T17  = "Field, line, subfield, or component consists of blanks, ('CrLf') or missing mandatory line, subfield or component.";
	
	Terror.T18 = "field 11R and 11S; the first component must have the format 3!n, and must be within the range 100-999.\n";
//								"- field 12, and MT = (105 or 106); this component must have the format 3!n, and must be within the range 100-999.\n"+
//								"- field 12, and MT NOT= (MT105 or 106); this component must have the format 3!n, refer to T88 for additional special exception checks.\n"+
//								"- field 61; if the first character of subfield 6 is \"S\", then the next 3 characters must have the format 3!n, and must be within the range 100-999.";


	Terror.T26= "The data content of this field may not contain a '/'  as its first character,\n" +
							"nor a '/' as its last character; nor may it contain '//' (two consecutiveslashes) anywhere within its contents.";

	Terror.T27 = "BIC incorrectly formatted or invalid.";
	Terror.T28 = " S.W.I.F.T. BIC is not a valid destination.";
	Terror.T29 = "S.W.I.F.T. BIC contains an invalid branch code.";
	Terror.T30 = "Excessive line(s), subfield(s) or component(s) were found in this field.\n" +
								"('CrLf:' or 'CrLf-' not found, or line, subfield or component length greater than defined length).";
	Terror.T31 = "The line, subfield or component separator or delimiter ('CrLf', blank,slash, or double slash) is missing or incorrect.";

	Terror.T38  = "Time illogical.";
	Terror.T39 = "Code word error. Subfield 2 field 66A.";
	Terror.T40  = "Missing amount/number or incorrect amount/number first character.";
	Terror.T41 = "Code word error. Subfield 3 field 66A.";
	Terror.T42 = "Code word error. Subfield 3 field 35U.";
	Terror.T43 = "The decimal separator in the amount/number subfield or component is\n";
							"missing, is not a comma and/or more than one comma is present.";
	Terror.T44  = "the SWIFT BIC exists but it is not enabled for FIN, or it is not cutover.";
	Terror.T45  = "invalid NON SWIFT BIC.";
	Terror.T46  = "a TestandTraining dest. must not be used in a LIVE message.";
								
	Terror.T50  = "Date error, or the value of \"year\" (YY) in a Value Date component (format<DATE2>) is invalid, i.e. within the range 61-79.\n" +
							"Note1: the valid range for YY in a Value Date component is:\n" +
							"\t00-60, for the years 2000-2060,\n"+
							"]tand 80-99, for the years 1980-1999.\n"+
							"Note2: in <DATE4>, the value '0000' for YYYY is invalid.\n";
							"in <YEAR>, the value '0000' is invalid.\n";
							

	
	
	Terror.T51 ="Code word 'C', 'D','RC','RD', 'EC' or 'ED' error.";
	Terror.T52= "Invalid currency code or price code 'PCT','REN','YLD'";
	Terror.T53 ="Code word error. Subfield 6 component 1 of field 61."
	Terror.T54 ="Available";
	Terror.T55 ="Available";
	Terror.T56 ="Available";
	Terror.T57 ="Code word error. Subfield 2 of fields 31H, 31J or 31X.";
	Terror.T58 ="Code word error. Subfield 1 field 35A, 35N, 35P or 35S; or subfield 2\n"; +
						"field 35H or 35T; or subfield 1 in the 2nd occurrence of field 35A in MT550.";
	Terror.T59 ="Available";
	Terror.T60 ="Code word error. This check applies to :\n" +
							"\t- field 26F, in MT 306\n"+
							"\t- field 40A in MTs 700, 705.";
	Terror.T61=" Code word 'D' or 'M' error.";


	Terror.T89 = "In a generic field:\n" +
							"\t- qualifier is invalid,\n"+
							"\t- qualifier is duplicated,\n" +
							"\t- mandatory qualifier is missing,\n" +
							"\t- qualifier format or syntax error.\n" +
							"\t(see Part V ISO 15022 message formats).";
							
							
	
	
	
	
	
var myErrCodes = {};
myErrCodes[1001] = "不是大寫英文字母";
myErrCodes[1101] = "不是英文字母";
myErrCodes[1102] = "長度不對";
myErrCodes[1103] = "不是數字";
myErrCodes[1104] = "必須有/";
myErrCodes[1105] = "日期格式錯誤";

myErrCodes[1109] = "無此幣別或幣別未輸入";
myErrCodes[1110] = "此幣別的金額不可以有小數點";
myErrCodes[1111] = "金額欄位小數點錯誤";
myErrCodes[1112] = "金額欄位不可有非數字資料";
myErrCodes[1113] = "金額欄位未輸入";
myErrCodes[1114] = "負號請輸入N";
myErrCodes[1115] = "負號請輸入N";
myErrCodes[1116] = "負號請輸入N";
myErrCodes[1117] = "輸入不符合的字串";
myErrCodes[1119] = "代碼不對";
myErrCodes[1120] = "利率資料欄位小數點錯誤";
myErrCodes[1121] = "金額為0時不得有負號(N)";
myErrCodes[1122] = "金額格式不對";
myErrCodes[1123] = "金額長度錯誤(長度包含小數點)";
myErrCodes[1124] = "TIME格式錯誤(HH > 24)";
myErrCodes[1125] =  "TIME格式錯誤(MM > 60)";
myErrCodes[1126] = "TIME OFFSET第一位應為+或-, 格式; +HHMM, -HHMM";
myErrCodes[1127] = "不是英文字母,數字";
myErrCodes[1128] = "日期的MM必須為0-12";
myErrCodes[1129] = "日期的DD不對必須為0-12";
myErrCodes[1130] = "應該輸入CrLn";
myErrCodes[1131] = "應該輸入CrLn";
myErrCodes[1132] =  "Sub6第一個字母應輸入'S', 'N' or 'F'";
myErrCodes[1133] = "代碼錯誤";

myErrCodes[1134] = "This field must contain the code PREADV followed by a slash '/' and a reference to the preadvice,eg, by date.";

myErrCodes[1135] = "應該只有一個 / ";
myErrCodes[1136] = "日期未輸入";
myErrCodes[1137] = "第一行應該輸入資料";
myErrCodes[1138] = "應該輸入<HHMM>資料";
myErrCodes[1139] = "幣別或金額格式不對";
myErrCodes[1140] = "輸入格式不對";
myErrCodes[1141] = "日期格式不對, 須為4位(MMDD)";
myErrCodes[1150] = "欄位應該以  / 開頭 ";

myErrCodes[2000] = "程式定義錯誤";
myErrCodes[2001] = "應該輸入:";
myErrCodes[2002] = "不應該輸入/";
myErrCodes[2003] = "長度錯誤";
myErrCodes[2004] = "含有非Swift X字集";
myErrCodes[2005] = "應該輸入//";
myErrCodes[2006] = "輸入格式錯誤";
myErrCodes[2007] = "應該輸入'CRLF'";
myErrCodes[2013] = "含有非Swift Z字集";
myErrCodes[2021] = "未輸入//";
myErrCodes[2022] = "//'之後請輸入<CUR>'/'<CUR>'/'<NUMBER>";

myErrCodes[3001] = "應該輸入";
myErrCodes[9998] = "[主機回應]";
myErrCodes[9999] = "錯誤";


function myErrorObj(c, mesg)
{
	this.retval = c;
	this.errmsg = mesg;
}
function myError(code, name)
{
	var s =  get(code);
	if (s == null) s = "undefine error message for:" + c; 
	if (name) { s = name +" - "  + s;}
	var o = new myErrorObj(c, s);
	return o;
}
function get(code)
{
	try {
		code = Math.abs(parseInt(code,10));
		if (code < 1000) { 
			return "[T"+code + '] ' + Terror["T"+code];
		}
		else {
			return '[' + code + '] ' +myErrCodes[code];
		}
	}catch(ex) { 
		return '[' + code + ']'; 
	}
}

return {
	'get':get
};

}(jQuery));
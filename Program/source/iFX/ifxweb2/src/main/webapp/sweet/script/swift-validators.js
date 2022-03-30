//多行欄位長度問題，是否有算ENTER?
// 4*35X  是 1行 1 行 1 行?
//mt103  59 自己出不來
//多行欄位不換行時應自動分行(給enter)
//swift 調rim需resetCached，ifx-rin.js 中 if(source.slice(0,9) =="#swift_fn"){resetCached(source,rimName);}
var Swift = Swift || {};
(Swift.validators = function($) {
	var Rtn = {};
	var me = Swift.validators;
	var _errors = Swift.errors;
	var generic = Swift.generic.generic;
	var nonGeneric = Swift.generic.nonGeneric;
	var ifx = null;
	var mt = null;
	var msgtype = null;

	function init(aMt, aIfx) {
		if (aMt) {
			mt = aMt;
			msgtype = aMt['$'].type;
		}
		ifx = aIfx;
		// 柯:因SWIFT.PRINT 找不到 ifx 故這邊直接設定
		generic.setIfx(ifx);
	}
	var currentTag = null;
	var $currentField = null;
	var value = null;
	var tagName = null;
	var tagLen = 0;
	var context = null;
	var tagMap;

	function setCurrentField(aTag, $aCurrentField, aValue, aTagMap) {
		currentTag = aTag;
		tagName = currentTag['$'].tag;
		tagLen = currentTag['$'].len;
		tagLen = eval(tagLen);
		$currentField = $aCurrentField;
		context = currentTag.$.context;
		value = aValue;
		tagMap = aTagMap;
	}

	function getTagByDomId(id) {
		return tagMap.get(id);
	}

	function updateFieldValueToUpper() {
		value = value.toUpperCase();
		$currentField.val(value);
	}

	function updateFieldValue(v) {
		value = v;
		$currentField.val(v);
	}

	function goNext() {
		Swift.ui.nextFieldNoVal(false);
	}

	function getError(code) {
		return _errors.get(code);
	}

	function errmsg(x) {
		if (!isNaN(x))
			x = getError(x);
		ifx.errmsg($.trim(x), $currentField);
		return false;
	}

	function myError(code, m) {
		var s = getError(code);
		if (m)
			s += ' ' + m;
		return errmsg(s);
	}

	function unknownTag(name) {
		name = name || tagName;
		var x = _errors.get(9999) + '(' + name + ')';
		return errmsg(x);
	}

	function trigger(name) {
		var args = Array.prototype.slice.call(arguments);
		name = '#' + name;
		args.shift();
		// TODO 這裡給值的內容有點怪異
		$.each(args, function(i, x) { // it1, it2,....
			var it = "#it" + (i + 1);
			console.log("call with " + it + ":" + x);
			ifx.setValue(it, x);
		});
		ifx.executePrefRtn(ifx.getField(name));
	}
	// function isDigit(oneChar) {
	// return (oneChar >= "0" && oneChar <= "9");
	// }
	function isDigit(s) {
		var re = /[\d]/;
		if (!re.test(s))
			return false;
		else
			return true;
	}

	function isABC(oneChar) {
		oneChar = oneChar.toUpperCase();
		return (oneChar >= "A" && oneChar <= "Z");
	}

	function isAlphaNumeric(v) {
		var re = /^[0-9a-zA-Z-_]+$/;
		return v.match(re);
	}

	function hasDefinedCodes(oTag, codeKey) {
		var codeDef = oTag.getCodesByKey(codeKey);
		return codeDef != false;
	}

	function checkCode(oTag, value, codeKey, errmsg) {
		codeKey = codeKey || '*';
		errmsg = errmsg || '';
		var codeDef = oTag.getCodesByKey(codeKey);
		if (!codeDef) {
			return myError(9999, "Code name:" + codeKey + ' undefined!');
		}
		if (!codeDef.must)
			return true;
		if (codeDef.codes.indexOf(value) == -1) {
			var codeText = codeDef.codes.join();
			codeText = codeText.length < 100 ? codeText
					: (codeText.slice(0, 100) + "...");
			return myError(1133, errmsg + "代碼:" + codeText);
		}
		return true;
	}

	function checkConsecutiveSlashes(val) {
		// T26 This field must not start or end with a slash ‘/’ and must not
		// contain two consecutive slashes ‘//’
		// applies to - fields 20 and 20C- fields 21, 21A, 21F, 21G, 21P and 21R
		if (val.charAt(0) == "/" || val.charAt(val.length - 1) == "/"
				|| val.indexOf("//") != -1) {
			return errmsg(26);
		}
		return true;
	}
	// 第五個變數為 不要檢查slashe大於2的邏輯
	function checkSlashe(v, minLen, maxLen, pattern, unckmoreone) {
		if (v.length == 0)
			return true;
		if (v.charAt(0) != "/") {
			return errmsg(1150);
		}
		if (v.split('/').length > 2 && !unckmoreone) {
			return errmsg(1135);
		}
		v = v.slice(1);
		if (v.length < minLen || v.length > maxLen) {
			return errmsg(2006);
		}
		return true;
	}

	function check_1a_34x(value) {
		var pattern = '[/1!a][/34x]';
		var arr = value.split('/');
		if (arr.length < 2 // 沒有任何slash
				// || arr.length > 3 // slash 超過兩個 (因x型態可輸入斜線 by小柯 2014/01/09 )
				|| arr[0]) // 第一個字元不是slash
		{
			return myError(2006, pattern);
		}
		// arr[1] ==> /1!a 或/34x
		if (arr.length == 2) { // 只有一個 slash
			// if(arr[1].length == 1){ // /1!a
			// return nonGeneric.f_a(arr[1],1,1,'[/1!a]');
			// }else { // 34x
			return nonGeneric.f_x(arr[1], 1, 34, '[/34x]');
			// }
		} else { // TODO 可能需要再研究一下這部分SWIFT邏輯是甚麼
			if (value.slice(0, 2) == "//") {
				return nonGeneric.f_x(value, 1, 34, '[/34x]');
			} else {
				if (!nonGeneric.f_a(arr[1], 1, 1, '[/1!a]'))
					return false;
				arr.shift(); // 第一位
				arr.shift(); // 1!a欄位
				return nonGeneric.f_x(arr.join("/"), 1, 34, '[/34x]');
			}
		}
		return false;
	}

	function deN(value, maxN) {
		var tmp = "";
		for (var i = 0; i < maxN && i < value.length; i++) {
			var oneChar = value.charAt(i);
			if (!(oneChar >= "0" && oneChar <= "9")) {
				break;
			}
			tmp += oneChar;
		}
		return [ tmp, value.slice(1) ];
	}

	function mySplit(s) {
		var arr = [], len, offset = 0, betweenSlash = "", bFirstSlash = true;
		if (s.indexOf("/") != -1) {
			var i = s.indexOf("/");
			var j = (s.substr(i + 1)).indexOf("/");
			betweenSlash = s.substr(i + 1, j);
		}
		for (var i = 1; i < arguments.length; i++) {
			if (arguments[i] == "/") {
				arr[arr.length] = s.substr(offset, 1);
				offset += 1;
				if (bFirstSlash) {
					arr.push(betweenSlash);
					offset += betweenSlash.length;
					bFirstSlash = false;
				}
			} else {
				len = parseInt(arguments[i], 10);
				try {
					arr.push(s.substr(offset, len));
				} catch (eee) {
					arr.push("");
				}
				offset += len;
			}
		}
		if (offset < s.length) {
			arr.push(s.substr(offset));
		}
		return arr;
	}

	function mySplitX(s) {
		var arr = [], len, offset = 0;
		for (var i = 1; i < arguments.length; i++) {
			len = parseInt(arguments[i], 10);
			try {
				arr.push(s.substr(offset, len));
			} catch (eee) {
				arr.push("");
			}
			offset += len;
		}
		if (offset < s.length) {
			arr.push(s.substr(offset));
		}
		return arr;
	}

	function mySlashSplit(value) {
		// "12d[//6!n1!a3n][/16x]"; parse this format
		var f1, f2, f3;
		var f2Pos = value.indexOf("//");
		if (f2Pos != -1) {
			f1 = value.substr(0, f2Pos);
			f2 = value.substr(f2Pos + 2);
			var arr = f2.split("/");
			f2 = arr[0];
			f3 = arr[1];
		} else { // no double slash
			var f3Pos = value.indexOf("/");
			if (f3Pos != -1) {
				f1 = value.substr(0, f3Pos);
				f3 = value.substr(f3Pos + 1);
			} else {
				f1 = value;
			}
		}
		var oo = new Object;
		oo.f1 = f1;
		oo.f2 = f2;
		oo.f3 = f3;
		return oo;
	}

	function check_98(value) {
		// "12d[//6!n1!a3n][/16x]"; parse this format
		var f2, f3;
		var f2Pos = value.indexOf(",");
		if (f2Pos != -1) {
			f2 = value.substr(f2Pos + 1);
			var arr = f2.split("/");
			f2 = arr[0];
			f3 = arr[1];
		} else { // no double slash
			var f3Pos = value.indexOf("/");
			if (f3Pos != -1) {
				f3 = value.substr(f3Pos + 1);
			}
		}
		var oo = new Object;
		oo.f2 = f2;
		oo.f3 = f3;
		return oo;
	}

	function splitQualifier(value) {
		var arr = value.split("//");
		var arr2 = arr.slice(1), textsum; // 小柯改 只需處裡第一個"//" 後面的給 其他檢查來判斷(前/後/包含//)
		textsum = arr2.join("//"); // 小柯改
		// alert("arr="+arr+", arr2 ="+arr2+",textsum ="+textsum);
		if (!fvalColon(arr[0].charAt(0))) {
			return false
		}
		if (arr.length < 2) {
			return myError(2021);
		}
		if (!nonGeneric.f_c(arr[0].slice(1), 4, 4, "4!c")) {
			return false;
		}
		return [ arr[0].slice(1).toUpperCase(), textsum ]; // 小柯改
	}

	function fvalColon(s) {
		if (s.length == 0 || s.charAt(0) != ":") {
			return myError(2001); // 小柯改
		}
		return true;
	}

	function fvalSlash(s, len, pattern) {
		if (len == 1) {
			if (s.charAt(0) != "/")
				return myError(2001, pattern); // ERROR 2002改2001 改成"應該輸入"
		} else if (len == 2) {
			if (s.charAt(0) != "/" || s.charAt(1) != "/")
				return myError(2005, pattern);
		} else {
			return myError(9999, pattern);
		}
		return true;
	}
	// "['/'<DC>]['/'34x]"
	function checkSlash_DC_nnx(line, maxlen, pattern) {
		if (line.length == 0 || line[0] != '/')
			return myError(2002, pattern);
		line = line.slice(1);
		if (!line)
			return myError(2006, pattern);
		if (line.length == 1 && !generic.DC(line))
			return false;
		return nonGeneric.f_x(line, 0, maxlen, pattern);
	}

	function fvalEmpty(value, pattern, len) // 小柯 增start
	{
		if (value == null || value.length == 0) {
			return myError(-1140, pattern);
		}
		if (len != null && len > 0) {
			if (value.length < len) {
				return myError(-1140, pattern);
			}
		}
		return true;
	} // 小柯 增end
	function isAllEmpty(lines) {
		var arr = $.grep(lines, function(x) {
			return x.length > 0;
		});
		return arr.length == 0;
	}

	function removeNewLine(s) {
		var r, re; // Declare variables.
		re = /[\x0d\x0a]/g; // Create regular expression pattern.
		r = s.replace(re, ""); // Replace "\r or \n" with "".
		return (r); // Return string with replacement made.
	}

	function getTextAreaValue(s) {
		return removeNewLine(s);
	}
	// getTextAreaValue2
	function toNewLine(s) {
		var r, re; // Declare variables.
		re = /\x0d\x0a/g; // Create regular expression pattern.
		r = s.replace(re, "\n"); // Replace "\r\r\n" with "\n".
		return (r); // Return string with replacement made.
	}

	function toLines(s) {
		return toNewLine(s).split('\n');
	}
	// retval:
	// false: validate failed
	// true: validate success
	function valCurAmount(v, bUpdate, bag, spupdate) {
		bag = bag || {};
		bUpdate = (bUpdate == undefined) ? true : bUpdate;
		try {
			var arr = mySplit(v, 3), cur = arr[0].toUpperCase(), amt = arr[1], oCur;
			oCur = generic.CUR(cur);
			if (!oCur)
				return false;
			amt = generic.AMOUNT(oCur, amt, 15, "<AMOUNT>");
			if (amt === false)
				return false;
			bag['curamt'] = cur + amt; // store new CURAMT
			if (bUpdate) {
				if (spupdate) {
					updateFieldValue(amt);
				} else {
					updateFieldValue(bag['curamt']);
				}
			}
			return true;
		} catch (ee) {
			return myError(1139, "<CUR><AMUNT>");
		}
	}

	function valCur(v, bUpdate, bag) {
		bag = bag || {};
		bUpdate = (bUpdate == undefined) ? true : bUpdate;
		try {
			var cur = v.toUpperCase(), oCur;
			oCur = generic.CUR(cur);
			if (!oCur)
				return false;
			bag['cur'] = cur; // store new CURAMT
			if (bUpdate) {
				updateFieldValue(bag['cur']);
			}
			return true;
		} catch (ee) {
			return myError(1139, "<CUR>");
		}
	}

	function valAmount(v, bUpdate, bag, num) { // 0307小柯增加純 金額 NUM 為長度
		bag = bag || {};
		bUpdate = (bUpdate == undefined) ? true : bUpdate;
		try {
			var amt = v;
			amt = generic.AMOUNT(null, v, num, "<AMOUNT>");
			if (amt === false)
				return false;
			bag['amt'] = amt; // store new CURAMT
			if (bUpdate) {
				updateFieldValue(bag['amt']);
			}
			return true;
		} catch (ee) {
			return myError(1139, "<AMUNT>");
		}
	}

	function valNCurAmount(v, bUpdate, bag) {
		var pattern = "['N']<CUR><AMOUNT>", bNegative = false;
		bUpdate = (bUpdate == undefined) ? true : bUpdate;
		if (v[0] == 'N') {
			bNegative = true;
			v = v.slice(1);
		}
		bag = bag || {};
		if (!valCurAmount(v, bUpdate, bag)) {
			return false;
		}
		if (bUpdate) {
			// 20140306 IZA FOR MT320 32H
			// updateFieldValue((bNegative ? '-' : '') + bag['curamt']);
			updateFieldValue((bNegative ? 'N' : '') + bag['curamt']);
		}
		return true;
	}

	function valDcDate2CodeAmount(oTag, value) {
		var pattern = "<DC><DATE2>3!a<AMOUNT>15"; // 1!a6!n3!a15d (D/C Mark)
		// (Date) (Unit) (Amount)
		var arr = mySplit(value, 1, 6, 3);
		arr[0] = arr[0].toUpperCase();
		if (arr[0] != "D" && arr[0] != "C")
			return myError(51, pattern);
		if (!generic.DATE(arr[1], "DATE2")) {
			return false;
		}
		// var oCode = getTagCode(oTag); //原
		// if (hasDefinedCodes(oTag)) { //小柯增
		// if(!checkCode(oTag, value)) {
		// return false
		// } //小柯增
		// }
		if (hasDefinedCodes(oTag)) { // 小柯改 原oCode 改
			if (!checkCode(oTag, arr[0])) {
				return false;
			}
			arr[3] = generic.AMOUNT(null, arr[3], 15, "<AMOUNT>");
			if (arr[3] === false)
				return false;
			updateFieldValue(arr.join(""));
		} else { // 有問題 小柯 62F M
			var bag = {};
			if (!valCurAmount(arr[2] + arr[3], false, bag)) {
				return false;
			}
			arr[2] = bag['curamt'];
			arr[3] = "";
			updateFieldValue(arr.join(""));
		}
		return true;
	}

	function complexval2DateCurAmount(type, v, bUpdate, spupdate, bag) { // 特殊的更新只更新 金額
		if (type == "date") {
			if (!generic.DATE(v, "DATE2", tagName)) {
				return false;
			} else {
				return true;
			}
		}
		if (type == "cur") {
			return valCur(v, bUpdate, bag);
		}
		if (type == "curamount") {
			return valCurAmount(v, bUpdate, bag, spupdate);
		}
	}

	function valDate2CurAmount(v, bUpdate, bag) {
		var arr = mySplit(v, 6), bag = bag || {};
		bUpdate = (bUpdate == undefined) ? true : bUpdate;
		if (!generic.DATE(arr[0], "DATE2", tagName))
			return false;
		if (!arr[1]) {
			return myError(1109, "<CUR><AMOUNT>");
		}
		if (!valCurAmount(arr[1], false, bag)) {
			return false;
		}
		arr[1] = bag.curamt;
		bag['date2curamt'] = arr.join('');
		if (bUpdate)
			updateFieldValue(bag['date2curamt']);
		return true;
	}

	function valNDate2CurAmount(v) {
		var pattern = "['N']<DATE2><CUR><AMOUNT>", bNegative = false;
		if (v[0] == "N") {
			bNegative = true;
			v = v.slice(1); // remove "N" or "n"
		}
		var bag = {};
		if (!valDate2CurAmount(v, false, bag)) {
			return false;
		}
		updateFieldValue((bNegative ? 'N' : '') + bag.date2curamt);
		return true;
	}

	function valDm3n2aCurAmount(oTag, value) { // field ??
		var pattern = "<DM>3!n2!a<CUR><AMOUNT>", arr = mySplit(value, 1, 3, 2, 3); // 改
		if (!checkCode(oTag, arr[0], "(Day/Month)")) { // 0307小柯改
			// return false; //0307小柯改
			return myError(-61, pattern); // 0307小柯改 需要寫哪種的error code?
		}
		// if (arr[0] != "D" && arr[0] != "M") //0307小柯改
		// return myError(-61, pattern); //0307小柯改
		if (!nonGeneric.f_n(arr[1], 3, 3, "3!n")) {
			return false;
		}
		if (!nonGeneric.f_a(arr[2], 2, 2, "2!a")) {
			return false;
		}
		if (!checkCode(oTag, arr[2], "(Code)")) {
			return false;
		}
		// arr[4] = generic.AMOUNT(null, arr[4], 15, "<AMOUNT>"); //改
		// updateFieldValue(arr.join(""));
		var bag = {};
		if (!valCurAmount(arr[3] + arr[4], false, bag)) { // 改
			return false;
		}
		arr[3] = bag['curamt'];
		arr[4] = "";
		updateFieldValue(arr.join(""));
		// var bag = {};
		// if (!valCurAmount(arr[3], false, bag)) {
		// return false;
		// }
		// arr.pop(); // remove last item (curamt)
		// updateFieldValue(arr.join() + bag['curamt']);
		return true;
	}

	function checkStartsWith(value, p) {
		var re = new RegExp("^" + p);
		if (!value.match(re)) {
			return myError(9999, "請以" + p + "開頭");
		} else {
			return true;
		}
	}
	// / end of helper
	// begin 10
	Rtn.fval_11A = function() {
		// //11 A ‘:’4!c ‘//’ <CUR> T52
		var arr, anyError;
		arr = mySplit(value, 1, 4, 2); // 小柯改 應該要少最後一碼
		anyError = !fvalColon(arr[0]) || !nonGeneric.f_c(arr[1], 4, 4, "4!c")
				|| !fvalSlash(arr[2], 2, "//") || !generic.CUR(arr[3]); // 小柯 先改
		return !anyError;
	};
	// 2014/03/10 IZA REMARK
	// Rtn.fval_11S = Rtn.fval_11R = function() {
	// var arr, brr;
	// // <MT>’CRLF’<DATE2>[’CRLF’4!n6!n]
	// value = toNewLine(value);
	// arr = value.split("\n"); // mySplit(value, 3, 2, 6, 2, 4,6);
	// if (!generic.MT(arr[0])) {
	// return false;
	// }
	// if (!generic.DATE(arr[1], "DATE2")) {
	// return false;
	// }
	// if (arr.length > 2 && arr[2]) {
	// brr = mySplit(arr[2], 4, 6);
	// if (!nonGeneric.f_n(brr[0], 4, 4, "4!n")
	// || !nonGeneric.f_n(brr[1], 6, 6, "6!n")) {
	// return false;
	// }
	// }
	// return true;
	// }
	// 2014/03/10 IZA ADD
	Rtn.fval_11R_0 = Rtn.fval_11S_0 = function() {
		return generic.MT(value);
	};
	// 2014/03/10 IZA ADD
	Rtn.fval_11R_1 = Rtn.fval_11S_1 = function() {
		pattern = "<DATE2>";
		return generic.DATE(value, "DATE2");
	};
	// 2014/03/10 IZA ADD
	Rtn.fval_11R_2 = Rtn.fval_11S_2 = function() {
		return nonGeneric.f_n(value, 10, 10, "4!n6!n");
	};
	Rtn.fval_12 = function() {
		var pattern = "??";
		if (msgtype == "105" || msgtype == "106") {
			pattern = "<MT>";
			return generic.MT(value);
		} else {
			return nonGeneric.f_n(value, 3, 3, pattern);
		}
		return true;
	};
	Rtn.fval_12A = function() {
		var pattern, arr, anyError;
		pattern = ":4!c/[8c]/30x";
		arr = mySplit(value, 1, 4, "/", "/");
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first" + " of " + pattern);
		if (anyError)
			return false;
		if (arr[3].length != 0) {
			if (!nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)) {
				return false;
			}
		}
		anyError = !fvalSlash(arr[4], 1, "last" + " of " + pattern)
				|| !nonGeneric.f_x(arr[5], 0, 30, "30x" + " of " + pattern);
		return !anyError;
	};
	Rtn.fval_12B = function() { // Qualifier是否為必須輸入?
		var codeDef;
		// alert(codeDef.cond);
		var pattern, arr;
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		pattern = ":4!c/[8c]/4!c"; // (Qualifier)(Data Source Scheme)(Instrument Type Code)
		arr = mySplit(value, 1, 4, "/", "/");
		codeDef = currentTag.getCodesByKey("(Instrument Type Code-" + arr[1] + ")"); // 找出是否有相符的設定
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "first 4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)
				|| !fvalSlash(arr[4], 1, "last" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5], 4, 4, "last 4!c" + " of " + pattern)
				|| !checkCode(currentTag, arr[1], arrcompt[0] + ")");
		if (anyError) { // 小柯改 FOR Qualifier
			return !anyError;
		} // 小柯改 FOR Qualifier
		if (codeDef && !arr[3]) { // (Qualifier) 替換成 codeDef.cond但傳入的其實還是固定的"位置"
			return checkCode(currentTag, arr[5], "(Instrument Type Code-" + arr[1] + ")")
		}
		return true;
	};
	Rtn.fval_12C = function() {
		var pattern, arr, anyError;
		// pattern - :4!c//6!c
		arr = mySplit(value, 1, 4, 2); // 小柯改 應該要少最後一碼
		anyError = !fvalColon(arr[0]) || !nonGeneric.f_c(arr[1], 4, 4, "4!c")
				|| !fvalSlash(arr[2], 2, "//") || !nonGeneric.f_c(arr[3], 6, 6, "6!c");
		return !anyError;
	};
	Rtn.fval_12D = function() {
		return checkCode(currentTag, value);
	};
	Rtn.fval_12E = function() {
		// pattern - 4!c (**)
		return nonGeneric.f_c(value, 4, 4, "4!c");
	};
	Rtn.fval_12F = function() {
		// pattern - 4!c (**)
		return checkCode(currentTag, value);
		// return nonGeneric.f_c(value, 4, 4, "4!c");
	};
	Rtn.fval_13A = function() {
		var pattern;
		var anyError = false;
		var arr;
		// A (MT= 507, 549)
		if (msgtype == "507" || msgtype == "549") {
			pattern = ":4!c//3!c"; // (**V)
			arr = mySplit(value, 1, 4, 2); // 小柯改 應該要少最後一碼
			anyError = !fvalColon(arr[0])
					|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
					|| !fvalSlash(arr[2], 2, " of " + pattern)
					|| !nonGeneric.f_c(arr[3], 3, 3, "3!c" + " of " + pattern);
		} else {
			pattern = ":4!c//3!c";
			arr = mySplit(value, 1, 4, 2); // 小柯改 應該要少最後一碼
			anyError = !fvalColon(arr[0])
					|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
					|| !fvalSlash(arr[2], 2, " of " + pattern)
					|| !nonGeneric.f_c(arr[3], 3, 3, "3!c" + " of " + pattern);
		}
		return !anyError;
	};
	Rtn.fval_13B = function() {
		var pattern;
		var anyError = false;
		var arr;
		pattern = ":4!c/[8c]/30x";
		arr = mySplit(value, 1, 4, "/", "/"); // 小柯改
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first " + "/" + " of " + pattern);
		if (anyError)
			return false;
		if (arr[3].length != 0
				&& !nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)) {
			return false;
		}
		anyError = !fvalSlash(arr[4], 1, "last " + "/" + " of " + pattern)
				|| !nonGeneric.f_x(arr[5], 1, 30, "30x" + " of " + pattern) // 小柯改 1
				|| !checkConsecutiveSlashes(arr[5]); // 小柯改
		return !anyError;
	};
	Rtn.fval_13C = function() {
		var pattern;
		var anyError = false;
		var arr;
		pattern = "/Code/<HHMM><SIGN><OFFSET>";
		updateFieldValueToUpper();
		arr = mySplit(value, "/", "/", 4, 1); // 小柯改
		// alert(arr[0]+"#"+arr[1]+"#"+arr[2]+"#"+arr[3]+"#"+arr[4]);
		// alert(arr[0] +"@"+ arr[1]+" "+ arr[2]+" "+ arr[3]+" "+ arr[4]+" "+ arr[5]);
		anyError = !fvalSlash(arr[0], 1, "first " + "/" + " of " + pattern)
		// || !checkCode(currentTag, "/"+arr[1]+"/", "(Code)", "(Code)") //小柯改
		|| !nonGeneric.f_c(arr[1], 1, 8, "8c") // 小柯改
				|| !fvalSlash(arr[2], 1, "last " + "/" + " of " + pattern)
				|| !generic.HHMM(arr[3]) || !generic.SIGN(arr[4])
				|| !generic.OFFSET(arr[5]);
		return !anyError;
	};
	Rtn.fval_13D = function() {
		var pattern;
		var anyError = false;
		var arr;
		pattern = "< DATE2 ><HHMM><SIGN><OFFSET>";
		arr = mySplit(value, 6, 4, 1, 4);
		anyError = !generic.DATE(arr[0], "DATE2") || !generic.HHMM(arr[1])
				|| !generic.SIGN(arr[2]) || !generic.OFFSET(arr[3]);
		return !anyError;
	};
	Rtn.fval_13E = function() {
		var pattern;
		var anyError = false;
		var arr;
		pattern = "<DATE4><HHMM>";
		arr = mySplit(value, 8); // 小柯改 應該要少最後一碼
		anyError = !generic.DATE(arr[0], "DATE4") || !generic.HHMM(arr[1]);
		return !anyError;
	};
	Rtn.fval_14A = function() {
		return checkCode(currentTag, value);
	};
	Rtn.fval_14C = function() {
		var codeDef = currentTag.getCodesByKey("*"); // iza 20140527
		// 4!n
		// return nonGeneric.f_n(value, 4, 4, "4!n"); //小柯 改
		if (generic.DATE(value, "DATE5")) {
			if (codeDef)
				return checkCode(currentTag, value); // iza 20140527
			else
				return true;
		} else
			return false;
	};
	Rtn.fval_14D = function() {
		// 20140307 IZA MODIFY
		// return nonGeneric.f_x(value, 0, 7, "7x");
		return checkCode(currentTag, value);
	};
	Rtn.fval_14F = function() {
		return nonGeneric.f_x(value, 0, 24, "24x");
	};
	// 範例 COMPONENT 分值
	Rtn.fval_14G = function() {
		// 1a/8!a
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		var arr, anyError;
		arr = value.split("/");
		if (arr.length != 2) {
			return myError(-2006, "請參照代碼輸入" + currentTag.$.component);
		}
		anyError = !checkCode(currentTag, arr[0], arrcompt[0] + ")")
				|| !checkCode(currentTag, arr[1], arrcompt[1] + ")");
		return !anyError;
	};
	Rtn.fval_14J = function() {
		return checkCode(currentTag, value);
	};
	Rtn.fval_14T = function() {
		var arr, maxarr;
		maxarr = arr.length; // 小柯 增 效能
		// "4!c['/4'!c['/'4!c]]"
		if (value.length > 12)
			return myError(-2003, "4!c['/'4!c['/'4!c]]");
		arr = value.split("/");
		for (var i = 0; i < maxarr; i++) { // 小柯 增 效能
			if (!nonGeneric.f_c(arr[i], 4, 4, "4!c")) {
				return false;
			}
		}
		return true;
	};
	Rtn.fval_14S = function() {
		// 3!a2!n
		var arr, anyError;
		arr = mySplit(value, 3); // 小柯改 應該要少最後一碼
		anyError = !nonGeneric.f_a(arr[0], 3, 3, "3!a")
				|| !nonGeneric.f_n(arr[1], 2, 2, "2!n");
		return !anyError;
	};
	Rtn.fval_15 = function() {
		return true;
		// TODO:不用檢查, 一定會放Crln到這個欄位
	};
	Rtn.fval_16A = function() {
		return nonGeneric.f_n(value, 0, 5, "5n");
	};
	Rtn.fval_16S = Rtn.fval_16R = function() {
		// return nonGeneric.f_c(value, 0, 16, "16c"); //小柯 改
		return checkCode(currentTag, value);
	};
	Rtn.fval_17 = function() {
		var pattern, arr, anyError;
		if (tagName == "17B") {
			pattern = "':'4!c'//'1a";
			arr = mySplit(value, 1, 4, 2); // 小柯改 應該要少最後一碼
			anyError = !fvalColon(arr[0])
					|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
					|| !fvalSlash(arr[2], 2, " of " + pattern)
					// || !nonGeneric.f_a(arr[3], 0, 1, "1a" + " of " + pattern);
					|| !checkCode(currentTag, arr[3], "(Flag)");
			return !anyError;
		}
		if ("AFGNORTUV".indexOf(tagName.slice(2)) != -1) {
			return nonGeneric.f_a(value, 0, 1, "1a");
			// field.updateValue(value);
		}
		return myError(9999, tagName);
	};
	// 20140307 IZA ADD
	Rtn.fval_17A = function() {
		return checkCode(currentTag, value);
	};
	// 20140307 IZA ADD
	Rtn.fval_17C = Rtn.fval_17E = Rtn.fval_17F = Rtn.fval_17G = Rtn.fval_17A;
	Rtn.fval_17H = Rtn.fval_17N = Rtn.fval_17O = Rtn.fval_17P = Rtn.fval_17A;
	Rtn.fval_17R = Rtn.fval_17T = Rtn.fval_17U = Rtn.fval_17V = Rtn.fval_17A;
	Rtn.fval_17W = Rtn.fval_17A;
	Rtn.fval_17I = Rtn.fval_17Y = Rtn.fval_17Z = Rtn.fval_17L = Rtn.fval_17M = Rtn.fval_17Q = Rtn.fval_17S = Rtn.fval_17X = Rtn.fval_17A;
	Rtn.fval_18A = function() {
		return nonGeneric.f_n(value, 0, 5, "5n");
	};
	Rtn.fval_19 = function() {
		var pattern = "<AMOUNT>17"; // 原
		// return true; //原
		var bag;
		bag = {};
		if (valAmount(value, false, bag, 17)) {
			updateFieldValue(bag['amt']);
			return true;
		}
		return false; // 小柯增加 0304 end
	};
	Rtn.fval_19A = function() {
		var pattern, arr, anyError;
		pattern = "':'4!c'//'['N']<CUR><AMOUNT>15"; // (Qualifier)
		// (Sign) (Currency
		// Code) (Amount)
		// TODO: check 19A code for 4!c
		// var arr = mySplit(value, 1,4,2);
		arr = mySplit(value, 1, 4, 2);
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 2, " of " + pattern);
		if (anyError)
			return false;
		var curAmt = value.slice(7); // '['N']<CUR><AMOUNT>15
		var sign = '';
		if (curAmt[0] == 'N') {
			sign = curAmt[0];
			curAmt = curAmt.slice(1);
		}
		var bag = {};
		if (!valCurAmount(curAmt, false, bag)) {
			return false;
		}
		updateFieldValue(arr.slice(0, 3).join('') + sign + bag['curamt']);
		return true;
	};
	Rtn.fval_19B = function() {
		var pattern, arr, bag, anyError;
		pattern = "':'4!c'//'<CUR><AMOUNT>15";
		// TODO: check 19B code for 4!c
		arr = mySplit(value, 1, 4, 2, 3); // 小柯改 應該要少最後一碼
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], len, " of " + pattern);
		if (anyError)
			return false;
		bag = {};
		if (!valCurAmount(arr[3] + arr[4], false, bag)) {
			return false;
		}
		updateFieldValue(arr[0] + arr[1] + bag['curamt']);
		return true;
	};
	// end of 10
	// begin 20
	Rtn.fval_20 = function() {
		var pattern;
		if (!checkConsecutiveSlashes(value))
			return false;
		switch (tagName) {
		case '20':
			if (!nonGeneric.f_x(value, 0, 16, "16x"))
				return false;
			checkRefNo(value);
			return false;
			break;
		default:
			return unknownTag();
		}
		return true;
	};
	// FOR 20 & 20C
	function checkRefNo(sendtext) {
		function onOK() {
			console.log("onOK");
			Swift.ui.externalSetValDone($currentField);
			Swift.validators.goNext();
		}

		function onError(errmsg, type) {
			console.log("onError");
			if (type == 'W') {
				Swift.ui.externalSetValDone($currentField);
				Swift.validators.goNext();
				return;
			}
			Swift.validators.myError(9998, errmsg);
		}
		Swift.validators.trigger('swift_fn_XWR10', sendtext, onOK, onError);
		return false;
	}
	Rtn.fval_20C = function() { // 小柯改start
		// var pattern,arr1,arr2,arr3,arr4;
		// pattern = ":4!c//16x";
		var pattern = ":4!c//16x"; // (Qualifier) )(Reference)
		var arr = splitQualifier(value);
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		// alert(arr[0]+"---"+arr[1]);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], arrcompt[0] + ")"))
			return false;
		if (!nonGeneric.f_x(arr[1], 1, 16, "16x" + " of " + pattern)) {
			return nonGeneric.f_x(arr[1], 1, 16, "16x" + " of " + pattern);
		}
		if (!checkConsecutiveSlashes(arr[1])) {
			return false;
		} else {
			updateFieldValue(":" + arr.join("//"));
			checkRefNo(arr[1]);
			return false;
		}
		// if (!checkConsecutiveSlashes(arr[1])) {
		// return false;
		// }
		// updateFieldValue(":" + arr.join("//"));
		// return true;
		// var arr1 = value.slice(0,1);
		// var arr2 = value.slice(1,5);
		// var arr3 = value.slice(5,7);
		// var arr4 = value.slice(7);
		// //alert(arr1+" - "+arr2+" - "+arr3+" - "+arr4);
		//
		// anyError =!fvalColon(arr1)
		// || !nonGeneric.f_c(arr2, 4, 4, "4!c" + " of " + pattern)
		// || !fvalSlash(arr3, 2, "//" + " of " + pattern)
		// || !nonGeneric.f_x(arr4, 1, 16, "16x" + " of " + pattern)
		// || !checkConsecutiveSlashes(arr4);
		// return !anyError;
	};
	// 小柯改end
	Rtn.fval_20D = function() { // 小柯改start
		var pattern = ":4!c//25x"; // (Qualifier) (Date)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)"))
			return false;
		if (!nonGeneric.f_x(arr[1], 1, 25, "25x" + " of " + pattern)) {
			return nonGeneric.f_x(arr[1], 1, 25, "25x" + " of " + pattern);
		}
		if (!checkConsecutiveSlashes(arr[1])) {
			return false;
		} else {
			updateFieldValue(":" + arr.join("//"));
			return true;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
		// var pattern,arr1,arr2,arr3,arr4,anyError;
		// pattern = "':'4!c'//'25x";
		//
		// var arr1 = value.slice(0,1);
		// var arr2 = value.slice(1,5);
		// var arr3 = value.slice(5,7);
		// var arr4 = value.slice(7);
		// //alert(arr1+" - "+arr2+" - "+arr3+" - "+arr4);
		//
		// anyError =!fvalColon(arr1)
		// || !nonGeneric.f_c(arr2, 4, 4, "4!c" + " of " + pattern)
		// || !fvalSlash(arr3, 2, "//" + " of " + pattern)
		// || !nonGeneric.f_x(arr4, 1, 25, "25x" + " of " + pattern)
		// || !checkConsecutiveSlashes(arr4);
		// return !anyError;
	};
	Rtn.fval_21 = function() {
		// 潘改 21E 可輸入'/' 欄位21E跳過檢核(MT104,MT405)
		if (tagName != "21E") {
			if (!checkConsecutiveSlashes(value, tagName)) {
				return false;
			}
		}
		if (tagName == "21") {
			return nonGeneric.f_x(value, 0, 16, "16x");
		}
		var x = tagName.slice(2);
		if ("ABFGNPR".indexOf(x) != -1) {
			return nonGeneric.f_x(value, 0, 16, "16x");
		} else if ("CDE".indexOf(x) != -1) {
			return nonGeneric.f_x(value, 0, 35, "35x");
		}
		return myError(9999, "");
	};
	Rtn.fval_22 = function() {
		var pattern, arr, anyError;
		// if (msgtype == 350) {
		// pattern = "8a'/'<SB-LC>"; // (**)
		// } else {
		// pattern = "8a'/'<SB-LC>";
		// }
		arr = value.split("/");
		// anyError = !nonGeneric.f_a(value, 0, 8, "8a") || !generic.SB_LC(field, value, len);
		if (arr.length == 2) {
			if (!checkCode(currentTag, arr[0], "(Function)")) {
				return checkCode(currentTag, arr[0], "(Function)");
			}
			if (!generic.SB_LC(arr[1])) {
				return generic.SB_LC(arr[1]);
				// return false
			}
			// return !anyError;
		} else {
			if (!checkCode(currentTag, arr[0], "(Function)")) {
				return checkCode(currentTag, arr[0], "(Function)");
			}
			return myError(9999, "請檢查輸入的值");
		}
		return true;
	};
	Rtn.fval_22A = function() {
		var pattern, anyError;
		pattern = "4!c";
		anyError = !nonGeneric.f_c(value, 4, 4, "4!c") || !checkCode(currentTag, value);
		return !anyError;
	};
	Rtn.fval_22B = function() {
		var pattern;
		pattern = "4!c";
		// check pattern = "4!c" (**);
		return checkCode(currentTag, value);
	};
	Rtn.fval_22X = Rtn.fval_22J = Rtn.fval_22G = Rtn.fval_22E = Rtn.fval_22D = Rtn.fval_22B;
	Rtn.fval_22C = function() {
		var pattern;
		pattern = "<SB-LC>";
		// return generic.SB_LC(field, value, len); //小柯改
		if (generic.SB_LC(value.toUpperCase())) {
			updateFieldValue(value.toUpperCase());
			return true;
		}
		return false; // 小柯改
	};
	Rtn.fval_22F = function() { // TODO TO DO 非常有問題!! 太多檢查且依照電文不同有不一樣的判斷
		var codeDef;
		// alert(codeDef.cond);
		var pattern, arr;
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		pattern = ":4!c/[8c]/4!c";
		arr = mySplit(value, 1, 4, "/", "/");
		codeDef = currentTag.getCodesByKey("(Indicator-" + arr[1] + ")"); // 找出是否有相符的設定
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "first 4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)
				|| !fvalSlash(arr[4], 1, "last" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5], 4, 4, "last 4!c" + " of " + pattern)
				|| !checkCode(currentTag, arr[1], arrcompt[0] + ")");
		if (anyError) { // 小柯改 FOR Qualifier
			return !anyError;
		} // 小柯改 FOR Qualifier
		if (codeDef && !arr[3]) { // (Qualifier) 替換成 codeDef.cond但傳入的其實還是固定的"位置"
			return checkCode(currentTag, arr[5], "(Indicator-" + arr[1] + ")")
		}
		return true;
	};
	Rtn.fval_22H = function() {
		var pattern, arr, anyError;
		pattern = "':'4!c'//'4!c"; // check (**)
		arr = mySplit(value, 1, 4, 2); // 小柯改 應該要少最後一碼
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 2, " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 4, 4, "4!c" + " of " + pattern);
		return !anyError;
	};
	Rtn.fval_22K = function() {
		var pattern, arr, textsum, arrtext;
		pattern = "4!c[/35x]";
		arr = value.split("/");
		if (arr[0] != "OTHR") {
			if (!checkCode(currentTag, value, "(Type of Event)"))
				return false; // 小柯 改
		} else {
			if (!checkCode(currentTag, arr[0], "(Type of Event)"))
				return false; // 小柯 改
			arrtext = value.slice(arr[0].length + 1); // 0313小柯 增 原本會有問題 因x型態可以包含斜線
			if (!nonGeneric.f_x(arrtext, 1, 35, "[35x]" + " of " + pattern))
				return false;
		}
		return true;
	};
	Rtn.fval_22L = function() { // iza 20140527 add
		return nonGeneric.f_x(value, 0, tagLen, context);
	};
	Rtn.fval_22U = function() { // iza 20140527 add
		return nonGeneric.f_a(value, 0, tagLen, context);
	};
	Rtn.fval_22M = Rtn.fval_22N = Rtn.fval_22Q = Rtn.fval_22P = Rtn.fval_22R = Rtn.fval_22T = Rtn.fval_22V = Rtn.fval_22W = Rtn.fval_22L;
	Rtn.fval_22S = function() { // iza 20140527
		var pattern, arr, anyError;
		arr = value.split("/");
		// context="1!a/35x"
		if (arr.length == 2) {
			if (!checkCode(currentTag, arr[0], "(Side Indicator)")) {
				return false;
			}
			if (!nonGeneric.f_x(arr[1], 1, 35, "35x")) {
				return false;
			}
		} else {
			return myError(9999, "請檢查輸入的值");
		}
		return true;
	};
	Rtn.fval_23 = function() {
		var pattern, arr, anyError; // 4a/4a/1!a/3!a 小柯改 START
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		switch (msgtype) {
		case "305":
			arr = value.split("/");
			if (arr.length != 4) {
				// throw myError(-2006, "請參照代碼輸入" + currentTag.get("component"));
				return myError(-2006, "請參照代碼輸入" + currentTag.$.component);
			}
			// anyError = !checkCode(currentTag, arr[0], 0) //
			// || !checkCode(currentTag, arr[1], 1) //
			// || !checkCode(currentTag, arr[2], 2) //
			// || !generic.CUR(field, arr[3], 3); //
			anyError = !checkCode(currentTag, arr[0] + "/", arrcompt[0] + ")")
					|| !checkCode(currentTag, arr[1] + "/", arrcompt[1] + ")")
					|| !checkCode(currentTag, arr[2] + "/", arrcompt[2] + ")")
					|| !generic.CUR(arr[3]);
			return !anyError;
			break; // 小柯改 END
		case "700":
		case "710": // 0306小柯增
			var re = new RegExp("PREADV/");
			if (value.match(re) == null) {
				return myError(9999, "沒有包含PREADV/");
			}
			return nonGeneric.f_x(value, 0, 16, "16x");
			break;
		// if (!nonGeneric.f_x(value, 0, 16, "16x"))
		// return false;
		// return checkStartsWith(value,"PREADV/"); //0304小柯改 (/PREADV)
		// break;
		// case "710":
		// if (!nonGeneric.f_x(value, 0, 16, "16x")) //0305小柯改
		// return false; //0305小柯改
		// return checkStartsWith(value,"PREADV/"); //0305小柯改
		// var codes = getCodeArray(currentTag);
		// if (value.indexOf("/" + codes[0]) == -1) {
		// return myError(-1134, "");
		// }
		// break;
		case "760":
		case "767": // 小柯 增
			return checkCode(currentTag, value);
			break;
		case "580":
			// 16x -->7!a[/5!a][//1!a] (Type) (Service) (Code)
			pattern = "7!a[/5!a][//1!a] (Type) (Service) (Code)";
			var codeType = value.substr(0, 7);
			return checkCode(currentTag, codeType); // check 7!a using first
			// code
			// element of current field
			break;
		case "935": // MT 935
			pattern = "3!a[2!n]11x"; // (Currency) (Number of Days)
			// (Function)
			var cur = value.substr(0, 3);
			// if (!generic.CUR(field, cur, 3)) //
			// return false;
			if (!generic.CUR(cur)) // 小柯 先改
				return false;
			var func;
			var numOfDay = "";
			if ((value.charAt(3) != null && isDigit(value.charAt(3)))
					&& (value.charAt(4) != null && isDigit(value.charAt(4)))) {
				numOfDay = value.charAt(3) + value.charAt(4);
				func = value.substr(5);
			} else {
				func = value.substr(3);
			}
			return checkCode(currentTag, func);
			break;
		default:
			if (!nonGeneric.f_x(value, 1, 16, "16x"))
				return false;
			if (hasDefinedCodes(currentTag)) {
				return checkCode(currentTag, value);
			}
			return true;
		}
	};
	Rtn.fval_23A = function() { // 小柯0313改 沒測!
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		arr = value.split("/");
		if (arr.length != 2) {
			return myError(-2006, "請參照代碼輸入" + currentTag.$.component);
		}
		if (!checkCode(currentTag, arr[0], arrcompt[0] + ")"))
			return false;
		if (!checkCode(currentTag, arr[1], arrcompt[1] + ")"))
			return false;
		return true;
	};
	Rtn.fval_23B = function() {
		var pattern = "4!c"; // (**) (Type)
		updateFieldValueToUpper();
		return checkCode(currentTag, value);
	};
	Rtn.fval_23F = Rtn.fval_23E = Rtn.fval_23C = function() {
		switch (msgtype) {
		case "103":
			var pattern = "4!a[/30x]";
			var isother = currentTag.$.other; // 小柯 改
			var ss = value.split('/');
			var arr = ss.slice(1), textsum; // 0307小柯 增 原本會有問題 因x型態可以包含斜線
			textsum = arr.join(""); // 0307小柯 增 原本會有問題 因x型態可以包含斜線
			var re = new RegExp(
					"^PHON\/|PHOB\/|PHOI\/|TELE\/|TELB\/|TELI\/|HOLD\/|REPA\/");
			if (!checkCode(currentTag, ss[0])) {
				return checkCode(currentTag, ss[0]);
			}
			if (ss.length != 1) {
				if (value.match(re) != null && isother == "OTHR") {
					return nonGeneric.f_x(textsum, 1, 30, pattern + "之[/30x]");
				} else {
					return myError(9999, "開頭不是PHON,PHOB,PHOI,TELE,TELB,TELI,HOLD or REPA");
				}
			}
			return true;
			break;
		case "103+":
			var pattern = "4!a[/30x]";
			var isother = currentTag.$.other; // 小柯 改
			var ss = value.split('/');
			var arr = ss.slice(1), textsum; // 0307小柯 增 原本會有問題 因x型態可以包含斜線
			textsum = arr.join(""); // 0307小柯 增 原本會有問題 因x型態可以包含斜線
			var re = new RegExp("^REPA\/");
			if (!checkCode(currentTag, ss[0])) {
				return checkCode(currentTag, ss[0]);
			}
			if (ss.length != 1) {
				if (value.match(re) != null && isother == "OTHR") {
					return nonGeneric.f_x(textsum, 1, 30, pattern + "之[/30x]");
				} else {
					return myError(9999, "開頭不是REPA");
				}
			}
			return true;
			break;
		default:
			// var nuuu = getTagByDomId($currentField);
			// var num = $currentField.prev().prev('input').val();
			// var num2 = $('*[data-tag="20"]');
			// var aaa = $currentField.attr('id');
			// var tbxValue = document.getElementById(aaa).value;
			// var codeDef = currentTag.getCodesByKey("*"); //小柯 增 抓 code 值測試成功
			// alert(codeDef.other);
			var pattern = "4!a[/30x]";
			var isother = currentTag.$.other; // 小柯 改
			var ss = value.split('/');
			var arr = ss.slice(1), textsum; // 0307小柯 增 原本會有問題 因x型態可以包含斜線
			textsum = arr.join(""); // 0307小柯 增 原本會有問題 因x型態可以包含斜線
			var re = new RegExp("^OTHR/");
			if (!checkCode(currentTag, ss[0])) {
				return checkCode(currentTag, ss[0]);
			}
			if (ss.length != 1) {
				if (value.match(re) != null && isother == "OTHR") {
					return nonGeneric.f_x(textsum, 1, 30, pattern + "之[/30x]");
				} else {
					return myError(9999, "開頭不是OTHR/");
				}
			}
			return true;
		}
	};
	Rtn.fval_23D = function() {
		// TODO: check 23D
		var pattern;
		pattern = "10a"; // (**)
		return checkCode(currentTag, value); // 小柯 改
	};
	Rtn.fval_23G = function() { // 4!c[/4!c]
		var pattern, arr, anyError;
		pattern = "4!c[/4!c]"; // (**)
		arr = value.split("/");
		if (!checkCode(currentTag, arr[0], "(Function)"))
			return checkCode(currentTag, arr[0], "(Function)");
		if (arr.length == 2) {
			return checkCode(currentTag, arr[1], "(Subfunction)");
		} else if (arr.length > 2) {
			return myError(-9999, tagName);
		}
		return true;
	};
	Rtn.fval_23H = function() {
		var pattern = "8!c";
		if (!checkCode(currentTag, value, "(Function)"))
			return false;
		return nonGeneric.f_c(value, 8, 8, "8!c" + " of " + pattern);
	}
	Rtn.fval_23S = function() {
		return nonGeneric.f_a(value, 6, 6, "6!a");
	}
	Rtn.fval_23X = function() {
		var pattern = "4!c/65x";
		var arr = mySplit(value, 4, 1, 65);
		if (!checkCode(currentTag, arr[0], "(Code)"))
			return false;
		if (!nonGeneric.f_c(arr[0], 4, 4, "4!c" + " of " + pattern))
			return false;
		if (!fvalSlash(arr[1], 1, "first" + " of " + pattern))
			return false;
		return nonGeneric.f_x(arr[2], 0, 30, "30x" + " of " + pattern);
	}
	Rtn.fval_24B = function() {
		var pattern, arr, anyError;
		pattern = "':'4!c'/'[8c]'/'4!c";
		arr = mySplit(value, 1, 4, "/", "/"); // 小柯改
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "[4!c]" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first " + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)
				|| !fvalSlash(arr[4], 1, "last " + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5], 4, 4, "[4!c]" + " of " + pattern);
		return !anyError;
	};
	Rtn.fval_24D = function() {
		var pattern, arr, textsum, arrtext;
		pattern = "4!c[/35x]";
		arr = value.split("/");
		if (arr.length == 1) {
			if (!checkCode(currentTag, value))
				return false; // 小柯 改
		} else {
			if (!checkCode(currentTag, arr[0]))
				return false; // 小柯 改
			arrtext = value.slice(arr[0].length + 1); // 0313小柯 增 原本會有問題 因x型態可以包含斜線
			if (!nonGeneric.f_x(arrtext, 1, 35, "[35x]" + " of " + pattern))
				return false;
		}
		return true;
	};
	Rtn.fval_25 = function() {
		var pattern;
		pattern = "'35x";
		return nonGeneric.f_x(value, 0, 35, pattern);
	};
	Rtn.fval_25A = function() {
		var pattern, arr, anyError;
		pattern = "'/'34x";
		arr = mySplit(value, 1);
		anyError = !fvalSlash(arr[0], 1, " of " + pattern)
				|| !nonGeneric.f_x(arr[1], 0, 34, "34x" + " of " + pattern);
		return !anyError;
	};
	Rtn.fval_25D = function() { // 這個Qualifier 是否為必須輸入CODE值??
		var codeDef;
		// alert(codeDef.cond);
		var pattern, arr;
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		pattern = ":4!c/[8c]/4!c"; // (Qualifier)(Data Source Scheme)(Status Code)
		arr = mySplit(value, 1, 4, "/", "/");
		codeDef = currentTag.getCodesByKey("(Status Code-" + arr[1] + ")"); // 找出是否有相符的設定
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "first 4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)
				|| !fvalSlash(arr[4], 1, "last" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5], 4, 4, "last 4!c" + " of " + pattern)
				|| !checkCode(currentTag, arr[1], arrcompt[0] + ")");
		if (anyError) { // 小柯改 FOR Qualifier
			return !anyError;
		} // 小柯改 FOR Qualifier
		if (codeDef && !arr[3]) { // (Qualifier) 替換成 codeDef.cond但傳入的其實還是固定的"位置"
			return checkCode(currentTag, arr[5], "(Status Code-" + arr[1] + ")")
		}
		return true;
	};
	Rtn.fval_26B = Rtn.fval_26A = function() {
		return true;
	};
	Rtn.fval_26C = function() {
		return true;
	};
	Rtn.fval_26E = function() {
		return nonGeneric.f_n(value, 1, 3, "3n");
	};
	Rtn.fval_26F = function() {
		// 20140311 IZA MODIFY
		// return nonGeneric.f_a(value, 1, 9, "9a");
		return checkCode(currentTag, value);
	};
	Rtn.fval_26G = function() {
		return nonGeneric.f_a(value, 7, 7, "7!a");
	};
	Rtn.fval_26H = function() {
		return nonGeneric.f_x(value, 1, 16, "16x");
	};
	Rtn.fval_26L = Rtn.fval_26J = function() {
		return nonGeneric.f_n(value, 1, 1, "1!n");
	};
	Rtn.fval_26K = function() {
		// K 6*35x (Narrative)
		return nonGeneric.f_multiXYZ(value, 6, 35, "35x['CRLF'35x]0-5", "x");
	};
	Rtn.fval_26P = Rtn.fval_26N = function() {
		var pattern, arr;
		pattern = "<CUR>'/'4x";
		arr = value.split("/");
		if (generic.CUR(arr[0])) // 小柯 先改 幣別
			return false;
		return nonGeneric.f_a(arr[1], 1, 4, "4x");
	};
	Rtn.fval_26T = function() {
		// 3!c (Type)
		return nonGeneric.f_c(value, 3, 3, "3!c");
	};
	Rtn.fval_27 = function() {
		var pattern = context, anyError;
		if (value.length != 3 || value[1] != "/") {
			return myError(2006, pattern);
		}
		anyError = !nonGeneric.f_n(value[0], 1, 1, "(第1位應為數字)")
				|| !nonGeneric.f_n(value[2], 1, 1, "(第3位應為數字)");
		return !anyError;
	};
	Rtn.fval_28 = function() {
		var pattern, arr;
		pattern = "5n['/'2n]";
		arr = value.split("/");
		if (!nonGeneric.f_n(arr[0], 1, 5, "5n" + " of " + pattern)) {
			return false;
		}
		if (arr.length == 2) {
			return checkCode(currentTag, arr[1], "(Indicator)");
		} else if (arr.length > 2) {
			return false;
		}
		return true;
	};
	Rtn.fval_28C = function() {
		var pattern, arr;
		pattern = "5n['/'5n]";
		arr = value.split("/");
		if (!nonGeneric.f_n(arr[0], 1, 5, "5n" + " of " + pattern)) {
			return false;
		}
		if (arr.length > 1) {
			return nonGeneric.f_n(arr[1], 1, 5, "[5n]" + " of " + pattern);
		}
		return true;
	};
	Rtn.fval_28D = function() {
		var pattern, arr;
		pattern = "5n'/'5n";
		arr = value.split("/");
		if (!nonGeneric.f_n(arr[0], 1, 5, "first part 5n" + " of " + pattern)) {
			return false;
		}
		return nonGeneric.f_n(arr[1], 1, 5, "second part 5n" + " of " + pattern);
	};
	Rtn.fval_28E = function() {
		var pattern, arr;
		pattern = "5n'/'4!c";
		arr = value.split("/");
		if (!nonGeneric.f_n(arr[0], 1, 5, "5n" + " of " + pattern)) {
			return false;
		}
		return nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern);
	};
	Rtn.fval_29B = Rtn.fval_29A = function() { // 新增 At least one of the following codes should be
		// used
		// 20140307 IZA MODIFY
		// return nonGeneric.f_multiXYZ(value, 4, 35, "35x['CRLF'35x]0-3", "x");
		var wholetext = getTextAreaValue(value);
		var textindex = true, i;
		var codeDef = currentTag.getCodesByKey('*');
		for (i = 0; i < codeDef.codes.length; i++) {
			if (wholetext.indexOf(codeDef.codes[i]) != -1) {
				textindex = false;
			}
		}
		if (textindex) {
			var codeText = codeDef.codes.join();
			codeText = codeText.length < 100 ? codeText
					: (codeText.slice(0, 100) + "...");
			return myError(1133, "代碼:" + codeText);
		}
		return nonGeneric.f_x(value, 0, 4 * 35, "4*35x");
	};
	Rtn.fval_29C = function() {
		return nonGeneric.f_x(value, 1, 35, "35x");
	};
	Rtn.fval_29K = Rtn.fval_29E = function() {
		var pattern, arr;
		pattern = "4!c'/'<HHMM>";
		arr = value.split("/");
		if (!nonGeneric.f_c(arr[0], 4, 4, pattern)) {
			return false;
		}
		// return generic.HHMM(field, arr[1]); //小柯 先改
		return generic.HHMM(arr[1]);
	};
	Rtn.fval_29F = function() {
		var pattern;
		return nonGeneric.f_multiXYZ(value, 4, 35, "35z['CRLF'35z]0-3", "z");
	};
	Rtn.fval_29G = function() {
		var pattern, arr;
		pattern = "1n'/'33z['CRLF'1n'/'33z]0-8";
		value = toNewLine(value);
		arr = value.split("\n");
		for (var i = 0; i < arr.length; i++) {
			var l = arr[i].length;
			if (l < 3) {
				return myError(-2003, pattern);
			}
			var firstN = arr[i].substr(0, 1);
			if (!fvalSlash((arr[i]).substr(1, 1), 1, pattern)) {
				return false;
			}
			if (!nonGeneric.f_z((arr[i]).substr(2), 1, 33, pattern)) {
				return false;
			}
		}
	};
	Rtn.fval_29H = function() {
		var pattern;
		pattern = "4!c";
		return nonGeneric.f_c(value, 4, 4, pattern);
	};
	Rtn.fval_29J = function() {
		var pattern, arr;
		pattern = "4!c['/'<HHMM>]";
		arr = value.split("/");
		if (!nonGeneric.f_c(arr[0], 4, 4, pattern)) {
			return false;
		}
		if (arr.length > 1) {
			// return generic.HHMM(field, arr[1]); //小柯先改
			return generic.HHMM(arr[1]);
		}
		return true;
	};
	// end of 20x
	// -----------------------------
	// ------------------------------
	// begin of 30x
	Rtn.fval_30 = function() {
		pattern = "<DATE2>";
		return generic.DATE(value, "DATE2");
	};
	Rtn.fval_30F = function() {
		pattern = "<DATE4>";
		return generic.DATE(value, "DATE4");
	};
	Rtn.fval_30J = function() {
		// 1!a3!n
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		var val = value, anyError;
		anyError = !checkCode(currentTag, val.slice(0, 1), arrcompt[0] + ")")
				|| !nonGeneric.f_n(val.slice(1), 3, 3, "3!n");
		return !anyError;
	};
	Rtn.fval_30X = Rtn.fval_30V = Rtn.fval_30U = Rtn.fval_30T = Rtn.fval_30Q = Rtn.fval_30P = Rtn.fval_30H = Rtn.fval_30F;
	Rtn.fval_30G = function() {
		var pattern = "<DATE4>'/'<DATE4>";
		var arr = value.split("/");
		return generic.DATE(arr[0], "DATE4") && generic.DATE(arr[1], "DATE4");
	};
	Rtn.fval_31B = function() {
		var pattern, arr, arr2;
		value = toNewLine(value);
		arr = value.split("\n");
		return generic.DATE(arr[0], "DATE2");
	};
	Rtn.fval_31C = function() {
		var pattern;
		pattern = "<DATE2>";
		return generic.DATE(value, "DATE2");
	};
	Rtn.fval_31L = Rtn.fval_31S = Rtn.fval_31E = Rtn.fval_31C;
	Rtn.fval_31P = Rtn.fval_31D = function() {
		var pattern, arr, arr2;
		// Option D 6!n29x (Date) (Place)
		pattern = "<DATE2>29x";
		arr = mySplit(value, 6);
		if (!generic.DATE(arr[0], "DATE2")) {
			return false;
		}
		return nonGeneric.f_x(arr[1], 1, 29, "29x");
	};
	Rtn.fval_31F = function() {
		var pattern, arr, arr2;
		pattern = "<DATE2>['/'<DATE2>]['//'35x]";
		arr = value.split("/");
		if (!generic.DATE(arr[0], "DATE2")) {
			return false;
		}
		arr2 = value.split("//");
		if (!generic.DATE(arr2[0], "DATE2")) {
			return false;
		}
		return nonGeneric.f_x(arr2[1], 0, 35, "35x");
	};
	Rtn.fval_31G = function() {
		var pattern, arr, anyError;
		pattern = "<DATE2>'/'<HHMM>'/'12a";
		arr = value.split("/");
		anyError = !generic.DATE(arr[0], "DATE2") // 小柯改
				|| !generic.HHMM(arr[1]) || !nonGeneric.f_a(arr[2], 1, 12, "12a");
		// anyError = !generic.DATE(field, arr[0], arr[0].length, "DATE2")
		// || !generic.HHMM(field, arr[1], arr[1].length)
		// || !nonGeneric.f_a(arr[2], 0, 12, "12a");
		return !anyError;
	};
	Rtn.fval_31R = function() {
		var pattern, arr, arr2;
		pattern = "<DATE2>['/'<DATE2>]";
		arr = value.split("/");
		if (!generic.DATE(arr[0], "DATE2")) {
			return false;
		}
		if (arr.length > 1) {
			return generic.DATE(arr[1], "DATE2");
		}
		return true;
	};
	Rtn.fval_31T = function() {
		var pattern;
		// 4!n (Time)
		return generic.HHMM(value);
	};
	Rtn.fval_31X = Rtn.fval_31J = Rtn.fval_31H = function() {
		var pattern, arr;
		pattern = "(<DATE2>[<HHMM>])|7!a";
		if (value.length == 7) {
			return nonGeneric.f_a(value, 7, 7, "7!a");
		} else {
			arr = mySplit(value, 6);
			if (!generic.DATE(arr[0], "DATE2")) {
				return false;
			}
			if (value.length > 6) {
				return generic.HHMM(arr[1]);
			}
		}
		return true;
	};
	Rtn.fval_32A_0 = function() {
		if (currentTag.$.status == "O" && value.length == 0) {
			return true;
		}
		return complexval2DateCurAmount("date", value, true);
	};
	Rtn.fval_32A_1 = function() {
		var prevalue = $currentField.prevAll('input').val();
		if (prevalue.length == 0 && (currentTag.$.status != "O" || value.length != 0)) {
			return myError(1136, "<DATE>");
		}
		if (currentTag.$.status == "O" && value.length == 0 && prevalue.length == 0) {
			return true;
		}
		return complexval2DateCurAmount("cur", value, true);
	};
	Rtn.fval_32A_2 = function() {
		var prevalue = $currentField.prevAll('input').val();
		if (prevalue.length == 0 && (currentTag.$.status != "O" || value.length != 0)) {
			return myError(1109, "<CUR>");
		}
		if (currentTag.$.status == "O" && value.length == 0 && prevalue.length == 0) {
			return true;
		}
		prevalue = prevalue + value; // 要加一個幣別才能算(小數位數等)
		valAmount(value, false); // 更新值
		return complexval2DateCurAmount("curamount", prevalue, true, true); // 不更新欄位，因為是用組合進去算的的
	};
	Rtn.fval_32P_0 = Rtn.fval_32D_0 = Rtn.fval_32C_0 = Rtn.fval_32A_0;
	Rtn.fval_32P_1 = Rtn.fval_32D_1 = Rtn.fval_32C_1 = Rtn.fval_32A_1;
	Rtn.fval_32P_2 = Rtn.fval_32D_2 = Rtn.fval_32C_2 = Rtn.fval_32A_2;
	Rtn.fval_32B_0 = function() {
		if (currentTag.$.status == "O" && value.length == 0) {
			return true;
		}
		return complexval2DateCurAmount("cur", value, true);
	};
	Rtn.fval_32B_1 = function() {
		var prevalue = $currentField.prevAll('input').val();
		if (prevalue.length == 0 && (currentTag.$.status != "O" || value.length != 0)) {
			return myError(1109, "<CUR>");
		}
		if (currentTag.$.status == "O" && value.length == 0 && prevalue.length == 0) {
			return true;
		}
		prevalue = prevalue + value; // 要加一個幣別才能算(小數位數等)
		valAmount(value, true); // 更新值
		return complexval2DateCurAmount("curamount", prevalue, true, true); // 不更新欄位，因為是用組合進去算的的
	};
	Rtn.fval_33V_0 = Rtn.fval_33S_0 = Rtn.fval_33B_0 = Rtn.fval_32U_0 = Rtn.fval_32M_0 = Rtn.fval_32G_0 = Rtn.fval_32B_0;
	Rtn.fval_33V_1 = Rtn.fval_33S_1 = Rtn.fval_33B_1 = Rtn.fval_32U_1 = Rtn.fval_32M_1 = Rtn.fval_32G_1 = Rtn.fval_32B_1;
	Rtn.fval_32E = function() {
		pattern = "<CUR>";
		return generic.CUR(value);
	};
	Rtn.fval_32F = function() {
		var pattern, arr, numberLen;
		pattern = "3!a<NUMBER>";
		arr = mySplit(value, 3);
		if (!nonGeneric.f_a(arr[0], 3, 3, "3!a")) {
			return false;
		}
		numberLen = tagLen - 3;
		return generic.NUMBER(arr[1], numberLen);
	};
	Rtn.fval_32H = function() {
		var pattern = "['N']<CUR><AMOUNT>";
		return valNCurAmount(value);
	};
	Rtn.fval_32J = function() {
		var pattern = "<NUMBER>";
		return generic.NUMBER(value, tagLen);
	};
	Rtn.fval_32K = function() {
		// ref by 33K
		var pattern = "<DM>3!n2!a<CUR><AMOUNT>";
		return valDm3n2aCurAmount(currentTag, value);
	};
	Rtn.fval_32N = function() {
		// ref by 33N
		return valNDate2CurAmount(value); // 小柯先改 (field,value)
	};
	Rtn.fval_32Q = function() {
		var pattern, arr, anyError;
		pattern = "<CUR>'/'<CUR>";
		var arr = value.split("/");
		if (arr.length != 2)
			return myError(9999, "輸入格式錯誤");
		anyError = !generic.CUR(arr[0]) || !generic.CUR(arr[1]);
		return !anyError;
	};
	// Rtn.fval_33A = function() {
	// return valDate2CurAmount(value, true);
	// };
	Rtn.fval_33R_0 = Rtn.fval_33P_0 = Rtn.fval_33D_0 = Rtn.fval_33C_0 = Rtn.fval_33A_0 = Rtn.fval_32A_0;
	Rtn.fval_33R_1 = Rtn.fval_33P_1 = Rtn.fval_33D_1 = Rtn.fval_33C_1 = Rtn.fval_33A_1 = Rtn.fval_32A_1;
	Rtn.fval_33R_2 = Rtn.fval_33P_2 = Rtn.fval_33D_2 = Rtn.fval_33C_2 = Rtn.fval_33A_2 = Rtn.fval_32A_2;
	// 20140307 IZA MODIFY
	// Rtn.fval_33F = Rtn.fval_33E = function() {/
	// return valCurAmount(value, trye);
	// };
	// 20140307 IZA MODIFY
	// Rtn.fval_33F = Rtn.fval_33E = function() {/
	// return valCurAmount(value, trye);
	// };
	Rtn.fval_34B_0 = Rtn.fval_33F_0 = Rtn.fval_33E_0 = Rtn.fval_32B_0;
	Rtn.fval_34B_1 = Rtn.fval_33F_1 = Rtn.fval_33E_1 = Rtn.fval_32B_1;
	Rtn.fval_33G = function() {
		var pattern, arr, bag;
		pattern = "3!a15d[5!c]"; // (Currency) (Price) (Code)
		if (len > 18) {
			arr = mySplit(value, 18);
			bag = {};
			if (!valCurAmount(arr[0], false, bag)) {
				return false;
			}
			if (!nonGeneric.f_c(arr[1], 5, 5, "[5!c]")) {
				return false;
			}
			updateFieldValue(baj['curamt'] + arr[1]);
		} else {
			return valCurAmount(value, true);
		}
		return true;
	};
	Rtn.fval_33K = function() {
		// ref to 32K
		var pattern = "<DM>3!n2!a<CUR><AMOUNT>";
		return valDm3n2aCurAmount(currentTag, value);
	};
	Rtn.fval_33N = function() {
		// ref to 32N
		return valNDate2CurAmount(value); // 小柯先改 (field,value)
	};
	Rtn.fval_33T = function() {
		var pattern, arr, bag;
		// 3!a15d (Currency) (Price)
		arr = mySplit(value, 3);
		if (!nonGeneric.f_a(arr[0], 3, 3, "3!a")) {
			return false;
		}
		bag = {};
		if (!valCurAmount(arr[1], false, bag)) {
			return false;
		}
		updateFieldValue(arr[0] + bag['curamt']);
		return true;
	};
	// Rtn.fval_33V = function() {
	// var pattern = "<CUR><AMOUNT>";
	// return valCurAmount(value);
	// return true;
	// };
	// Rtn.fval_34A = function() {
	// var pattern = "<DATE2><CUR><AMOUNT>";
	// return valDate2CurAmount(value);
	// };
	Rtn.fval_34R_0 = Rtn.fval_34P_0 = Rtn.fval_34D_0 = Rtn.fval_34C_0 = Rtn.fval_34A_0 = Rtn.fval_32A_0;
	Rtn.fval_34R_1 = Rtn.fval_34P_1 = Rtn.fval_34D_1 = Rtn.fval_34C_1 = Rtn.fval_34A_1 = Rtn.fval_32A_1;
	Rtn.fval_34R_2 = Rtn.fval_34P_2 = Rtn.fval_34D_2 = Rtn.fval_34C_2 = Rtn.fval_34A_2 = Rtn.fval_32A_2;
	// Rtn.fval_34B = Rtn.fval_34B = function() { // 有問題???
	// // 34e "[N]<CUR><AMOUNT>
	// var pattern = "<CUR><AMOUNT>";
	// return valCurAmount(value);
	// };
	// Rtn.fval_34B = function() { // 有問題???
	// var pattern = "<CUR>[<DC>]<AMOUNT>";
	// var dc = value.charAt(4).toUpperCase();
	// if (dc == "D" || dc == "C") {
	// var v = value.slice(0, 3) + value.slice(4);
	// if (!valCurAmount(v, false, bag)) {
	// return false;
	// }
	//
	// updateFieldValue(dc + bag['curamt']);
	// } else {
	// return valCurAmount(value);
	// }
	// return true;
	// };
	Rtn.fval_34C = function() {
		var pattern = "4!c/[N]3!a15d";
		var arr = mySplit(value, 4, 1, 18);
		var percent = arr[2].substring(0, 3);
		if (!checkCode(currentTag, arr[0]))
			return false;
		if (!fvalSlash(arr[1], 1, "first" + " of " + pattern))
			return false;
		if (isNaN(percent)) {
			bag = {};
			return valCurAmount(arr[2], false, bag);
		} else {
			return generic.RATE(arr[2].substring(3, 18), 15);
		}
	}
	Rtn.fval_34E = function() {
		var pattern = "['N']<CUR><AMOUNT>";
		return valNCurAmount(value);
	};
	Rtn.fval_34N = function() {
		// ref 32N
		return valNDate2CurAmount(value);
	};
	Rtn.fval_34H = Rtn.fval_34G = function() {
		var pattern, arr, lines, bag = {};
		// Option G [3!n] (Number)
		// 3!a15d (Currency) (Amount)
		lines = toLines(value);
		if (lines[0]) {
			if (!nonGeneric.f_n(value, 3, 3, "[3!n]")) {
				return false;
			}
		}
		arr = mySplit(lines[1], 3);
		if (!generic.CUR(arr[0])) {
			return false;
		}
		if (arr.length > 1) {
			return generic.NUMBER(arr[1]);
		}
		return true;
	};
	Rtn.fval_35A = function() { // 小柯改 start
		var pattern, arr;
		// 3!a15d (Type) (Quantity)
		arr = mySplit(value, 3);
		if (!checkCode(currentTag, arr[0], "(Type)")) {
			return false;
		}
		arr[1] = generic.NUMBER(null, arr[1], tagLen - 3, tagLen - 3 + "!n");
		if (arr[1] === false)
			return false;
		updateFieldValue(arr.join(""));
		return true;
	}; // 小柯改 end
	Rtn.fval_35B_0 = function() { // to do
		// [ISIN1!e12!c] (Identification of Security)
		// [4*35x] (Description of Security)
		// at lease one exists, or both exist
		if (value.length > 0) {
			if (value.slice(0, 4) != "ISIN")
				return false;
			if (!nonGeneric.f_e(value.slice(4, 5), context))
				return false;
			if (!nonGeneric.f_c(value.slice(5), 12, 12))
				return false;
		}
		return true;
	};
	Rtn.fval_35B_1 = function() { // todo
		// [ISIN1!e12!c] (Identification of Security)
		// [4*35x] (Description of Security)
		// at lease one exists, or both exist
		var lines = toLines(value);
		var linelen = lines.length;
		var prevalue = $currentField.prevAll('input').val();
		// alert(prevalue.length +" "+ lines.length);
		if (getTextAreaValue(value).length == 0 && prevalue.length == 0) { // TODO 有問題
			// 第二次刪除上個欄位時會失效
			// 且直接用滑鼠...可跳過
			return myError(9999, "at lease one exists, or both exist");
		}
		for (var test1 = 0; test1 <= 3; test1++) {
			if (linelen > test1) {
				if (!nonGeneric.f_x(lines[test1], 0, 35, "lines " + test1 + 1
						+ " of 4*35x")
						|| !checkConsecutiveSlashes(lines[test1])) {
					return false;
				}
			}
		}
		return true;
	};
	Rtn.fval_35C = function() {
		// pattern = "3!c";
		return nonGeneric.f_x(value, 3, 3, "3!c");
	};
	Rtn.fval_35D = function() {
		var pattern = "<DATE2>";
		return generic.DATE(value, "DATE2");
	};
	Rtn.fval_35E = function() {
		// 6*50x (Narrative)
		return true;
	};
	Rtn.fval_35F = function() {
		return true;
	};
	Rtn.fval_35H = function() {
		var pattern = "['N']3!a<NUMBER>";
		return true;
	};
	Rtn.fval_35L = function() {
		return true;
	};
	Rtn.fval_35U = Rtn.fval_35S = Rtn.fval_35P = Rtn.fval_35N;
	Rtn.fval_35N = function() {
		var pattern, arr;
		pattern = "3!a<NUMBER>15";
		arr = mySplit(value, 3);
		if (!nonGeneric.f_a(arr[0], 3, 3, "3!a")) {
			return false;
		}
		return generic.NUMBER(arr[1], tagLen - 3);
	};
	Rtn.fval_35T = function() {
		var pattern, arr, arr2;
		pattern = "7d/3!a7d"; // (Rights) (Type)
		// (Quantity)<NUMBER>'/'<NUMBER>
		arr = value.split("/");
		if (!generic.NUMBER(field, arr[0], 7)) {
			return false;
		}
		arr2 = mySplit(value, 3);
		if (!nonGeneric.f_a(arr2[0], 3, 3, "3!a")) {
			return false;
		}
		return generic.NUMBER(arr2[1], 7);
	};
	Rtn.fval_36A = Rtn.fval_36 = function() {
		var pattern, arr, anyError;
		// 12d (Rate)
		value = generic.RATE(value, tagLen);
		if (!value)
			return false;
		updateFieldValue(value);
		return true;
	};
	Rtn.fval_36B = function() {
		// var pattern, arr, anyError;
		// pattern = ":4!c//4!c15d";
		// arr = mySplit(1, 4, 2, 4);
		// anyError = !fvalColon(arr[0]) || !nonGeneric.f_a(arr[1], 4, 4, "4!c")
		// || !fvalSlash(arr[2], len, "//" + " of " + pattern)
		// || !checkCode(currentTag, arr[3],"(Quantity Type Code)")
		// || !generic.NUMBER(field, arr[4], 15);
		//
		// return !anyError;
		// :4!c//4!c/15d (Qualifier) (Percentage Type Code) (Price)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)"))
			return false;
		var arr2 = arr[1].split("/");
		arr2[0] = arr2[0].toUpperCase(); // second 4!c
		if (!checkCode(currentTag, arr2[0], "(Quantity Type Code)")) { // check amount type code
			return false;
		}
		arr2[1] = generic.AMOUNT(null, arr2[1], 15, "<AMOUNT>");
		if (arr2[1] === false) {
			return false;
		}
		arr[1] = arr2.join("/");
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_36C = function() {
		var pattern, arr, anyError;
		pattern = ":4!c//4!c";
		arr = mySplit(value, 1, 4, 2, 4); // 小柯先改???? mySpllit value,
		anyError = !fvalColon(arr[0]) || !nonGeneric.f_a(arr[1], 4, 4, "4!c")
				|| !fvalSlash(arr[2], len, " of " + pattern)
				|| !nonGeneric.f_a(arr[3], 4, 4, "4!c");
		return !anyError;
	};
	Rtn.fval_37F = Rtn.fval_37E = Rtn.fval_37D = Rtn.fval_37C = Rtn.fval_37B = Rtn.fval_37A;
	Rtn.fval_37A = function() {
		var pattern, o, arr;
		// 12d[//6!n1!a3n][/16x] (Rate) (Period) (Information)
		pattern = "12d[//6!n1!a3n][/16x]";
		o = mySlashSplit(value);
		if (oo.f1 != null) {
			if (!generic.RATE(field, oo.f1, 12)) {
				return false;
			}
		}
		if (oo.f2 != null) {
			arr = mySplit(oo.f2, 6, 1);
			if (generic.DATE(arr[0], "DATE2")) {
				return false;
			}
			arr[1] = arr[1].toUpperCase();
			if (arr[1] != "D" && arr[1] != "M") {
				return myError(61, "<DM>");
			}
			if (!nonGeneric.f_n(arr[2], 0, 3, "[3n]")) {
				return false;
			}
			oo.f2 = arr.join();
		}
		if (oo.f3 != null) {
			if (!nonGeneric.f_n(oo.f3, 0, 26, "[/16x]")) {
				return false;
			}
		}
		if (oo.f2 != null) { // update field because of DM(dm)
			var fff = "";
			if (oo.f1 != null)
				fff += oo.f1;
			fff = fff + "//" + oo.f2;
			if (oo.f3 != null)
				fff = fff + "/" + oo.f3;
			updateFieldValue(fff);
		}
		return true;
	};
	// Rtn.fval_37R = Rtn.fval_37M = Rtn.fval_37G;
	Rtn.fval_37R = Rtn.fval_37M = Rtn.fval_37G = function() {
		var v2;
		if (generic.N_RATE(value, "[N]12") === false) {
			return myError(9999, "格式有誤");
		} else {
			V2 = generic.N_RATE(value, "[N]12");
			updateFieldValue(V2);
			// // [N]12d (Sign) (Rate)
			// if (value[0] == "N") {
			// v1 = value[0].toUpperCase();
			// v2 = value.slice(1);
			// v2 = generic.RATE( v2, 12); // 小柯 改 field
			// if (v2 === false)
			// return false;
			// updateFieldValue(v1 + v2);
			// } else {
			// v3 = generic.RATE( value, 12); // 小柯 改 field
			// if (v3 === false)
			// return false;
			// updateFieldValue(value);
			// }
			return true;
		}
	};
	Rtn.fval_37H = function() {
		var pattern, o, arr;
		// 1!a12d (Indicator) (Rate)
		arr = mySplit(value, 1);
		arr[0] = arr[0].toUpperCase();
		if (arr[0] != "C" && arr[0] != "D") {
			return myError(-51, "<DC>");
		}
		arr[1] = generic.RATE(arr[1], 12);
		updateFieldValue(arr.join(""));
		return true;
	};
	// Rtn.fval_37U = Rtn.fval_37P = Rtn.fval_37L = Rtn.fval_37J;
	Rtn.fval_37U = Rtn.fval_37P = Rtn.fval_37L = Rtn.fval_37J = function() {
		// J 12d (Rate)
		value = generic.RATE(value, 12);
		if (value === false)
			return false;
		updateFieldValue(value);
		return true;
	};
	Rtn.fval_37K = function() {
		var pattern, arr;
		// 3!a12d (Currency) (Rate)
		arr = mySplit(value, 3);
		if (!generic.CUR(arr[0])) {
			return false;
		}
		arr[1] = generic.RATE(arr[1], 12);
		if (arr[1] === false)
			return false;
		updateFieldValue(arr.join("").toUpperCase());
		return true;
		// arr[1] = generic.RATE(arr[1], 12);
		// if (arr[1] === false)
		// return false;
		// updateFieldValue(arr.join("").toUpperCase());
		// return true;
	};
	Rtn.fval_37N = function() {
		// 6*35x
		return true;
	};
	Rtn.fval_37S = function() {
		var pattern, arr;
		// Option S 12d[/4!n] (Rate) (Basis)
		if (value.indexOf("/") == -1) {
			value = generic.RATE(value, 12);
			if (value === false)
				return false;
			updateFieldValue(value);
		} else {
			arr = value.split("/");
			arr[0] = generic.AMOUNT(null, arr[0], 12, "<RATE>");
			if (arr[0] === false)
				return false;
			if (!nonGeneric.f_n(arr[1], 4, 4, "[4!n]"))
				return false;
			updateFieldValue(arr.join(""));
		}
		return true;
	};
	Rtn.fval_38A = function() {
		// 3n (Period)
		return nonGeneric.f_n(value, 0, 3, "3n");
	};
	Rtn.fval_38B = function() {
		var pattern, arr, arr2, anyError;
		// 4!c/4!c[/2!n] (Frequency) (Timing in Period) (Day)
		arr = value.split("/");
		if (!checkCode(currentTag, arr[0], "(Frequency)")
				|| !checkCode(currentTag, arr[1], "(Timing in Period)")) {
			return false;
		}
		if (arr.length == 3) {
			return nonGeneric.f_n(arr[2], 2, 2, "[/2!n]");
		}
		return true;
	};
	Rtn.fval_38D = function() {
		// 4n (Period)
		return nonGeneric.f_n(value, 0, 4, "4n");
	};
	Rtn.fval_38E = function() { // 小柯改
		// 2n1!a (Number) (Period)
		var n = value.substr(0, value.length - 1);
		var a = value.charAt(value.length - 1);
		if (!nonGeneric.f_n(n, 1, 2, "2n"))
			return nonGeneric.f_n(n, 1, 2, "2n");
		if (!checkCode(currentTag, a, "(Period)")) {
			return checkCode(currentTag, a, "(Period)");
		}
		return true;
	};
	Rtn.fval_38H = Rtn.fval_38G = function() { // 小柯改
		var pattern, arr;
		// 2n1!a/2n1!a (Number From)(Period From)(Number To)(Period To)
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		arr = value.split("/");
		if (arr.length != 2)
			return myError(9999, "格式有誤");
		if (arr[0].length == 3) {
			var n1 = arr[0].substr(0, 2);
		} else if (arr[0].length == 2) {
			var n1 = arr[0].substr(0, 1);
		} else {
			return myError(9999, "格式有誤");
		}
		if (arr[1].length == 3) {
			var n2 = arr[1].substr(0, 2);
		} else if (arr[1].length == 2) {
			var n2 = arr[1].substr(0, 1);
		} else {
			return myError(9999, "格式有誤");
		}
		var a2 = arr[1].slice(-1);
		var a1 = arr[0].slice(-1);
		if (!nonGeneric.f_n(n1, 1, 2, "2n"))
			return false;
		if (!checkCode(currentTag, a1, arrcompt[1] + ")"))
			return false; // 小柯改
		if (!nonGeneric.f_n(n2, 1, 2, "2n"))
			return false;
		if (!checkCode(currentTag, a2, arrcompt[3] + ")"))
			return false; // 小柯改
		return true;
	};
	Rtn.fval_38J = function() {
		var pattern, arr;
		// 1!a3!n (Indicator) (Number)
		arr = mySplit(value, 1);
		arr[0] = arr[0].toUpperCase();
		if (arr[0] != "D" && arr[0] != "M") {
			return myError(-61, "<DM>");
		}
		return nonGeneric.f_n(arr[1], 3, 3, "3!n");
	};

	function _f2n1a(value, len) {
		var n = value.substr(0, len - 1);
		var a = value.charAt(len - 1);
		if (!nonGeneric.f_a(a, 1, 1, "1!a")) { // 小柯改
			return false;
		}
		if (n != null)
			return nonGeneric.f_n(n, 0, 2, "2n"); // 小柯改
		return true;
	}
	Rtn.fval_39A = function() {
		var pattern, arr, bag = {}, maxarr; // 小柯 增 效能
		pattern = "2n/2n"; // (Tolerance 1) (Tolerance 2)
		arr = value.split("/");
		maxarr = arr.length; // 小柯 增 效能
		if (maxarr > 2) { // 小柯 增 效能
			return myError(-1135, pattern);
		}
		for (var i = 0; i < maxarr; i++) { // 小柯 增 效能
			if (!nonGeneric.f_n(arr[i], 0, 2, pattern)) {
				return false;
			}
		}
		return true;
	};
	Rtn.fval_39B = function() {
		var pattern;
		// Option B 13x
		pattern = "13x";
		if (!nonGeneric.f_x(value, 0, 13, pattern)) {
			return false;
		}
		return checkCode(currentTag, value);
	};
	Rtn.fval_39P = function() {
		var pattern, arr, bag = {};
		pattern = "4!c/3!a15d"; // (Type) (Currency) (Amount)
		arr = value.split("/");
		if (arr.length != 2) {
			return false;
		}
		arr[0] = arr[0].toUpperCase();
		if (!checkCode(currentTag, arr[0], "(Type)")) {
			return false;
		}
		if (!valCurAmount(arr[1], false, bag)) {
			return false;
		}
		updateFieldValue(arr[0] + "/" + bag['curamt']);
		return true;
	};	
	Rtn.fval_39M = function() {
		return nonGeneric.f_a(value, 2, 2, "2!a")		
	}
	// end of 30x
	// -----------------------------
	// ------------------------------
	// begin of 40x
	Rtn.fval_40A = function() {
		// 24x (Type) code must be used
		return checkCode(currentTag, value);
	};
	// Rtn.fval_40B = function() {
	// // 24xn 24x (Type)(Code)
	// var lines = toLines(value);
	// if (lines.length != 2) {
	// return myError(2006, "請參照代碼輸入" + field.get("component"));
	// }
	//
	// if (!checkCode(currentTag, lines[0], 0, '第一行Type')) {
	// return false;
	// }
	//
	// if (!valByCode(lines[1], 1, '第二行Code')) {
	// return false;
	// }
	// return true;
	// };
	Rtn.fval_40B_0 = function() {
		return checkCode(currentTag, value);
	};
	Rtn.fval_40B_1 = function() {
		return checkCode(currentTag, value);
	};
	Rtn.fval_40C = function() { // 0305小柯增
		var pattern = "4!a[/35x]";
		var ss = value.split('/');
		var arr = ss.slice(1), textsum; // 0307小柯 增 原本會有問題 因x型態可以包含斜線
		textsum = arr.join(""); // 0307小柯 增 原本會有問題 因x型態可以包含斜線
		var re = new RegExp("^OTHR/");
		if (ss.length == 1) {
			return checkCode(currentTag, value);
		} else {
			if (value.match(re) == null) {
				// return false;
				return myError(9999, "開頭不是OTHR/"); // 錯誤代碼??
			} else {
				return nonGeneric.f_x(textsum, 1, 35, pattern + "之[/35x]"); // 0307小柯 增 ss[1] 改
				// textsum
			}
		}
		return true;
	};
	Rtn.fval_40E = function() { // 0306小柯增
		var pattern = "30x[/35x]";
		var ss = value.split('/');
		var arr = ss.slice(1), textsum; // 0307小柯 增 原本會有問題 因x型態可以包含斜線
		textsum = arr.join(""); // 0307小柯 增 原本會有問題 因x型態可以包含斜線
		var re = new RegExp("^OTHR/");
		if (ss.length == 1) {
			return checkCode(currentTag, value);
		} else {
			if (value.match(re) == null) {
				// return false;
				return myError(9999, "開頭不是OTHR/"); // 錯誤代碼??
			} else {
				return nonGeneric.f_x(textsum, 1, 35, pattern + "之[/35x]"); // 0307小柯 增 ss[1] 改
				// textsum
			}
		}
		return true;
	};
	Rtn.fval_40F = function() { // 0305小柯增
		// 30x (Applicable Rules) code must be used //0305小柯增
		return checkCode(currentTag, value); // 0305小柯增
	}; // 0305小柯增
	Rtn.fval_41A_0 = function() {
		updateFieldValueToUpper();
		generic.SWIFTBIC(ifx, $currentField, msgtype, value);
		return false;
	};
	Rtn.fval_41A_1 = function() {
		return checkCode(currentTag, value);
	};
	Rtn.fval_41D_0 = function() {
		var wholeText = getTextAreaValue(value);
		return nonGeneric.f_x(wholeText, 1, tagLen);
	};
	Rtn.fval_41D_1 = Rtn.fval_41A_1;
	Rtn.fval_42D = Rtn.fval_42A = call_5X_N;
	Rtn.fval_42C = function() {
		return nonGeneric.f_x(toNewLine(value), 0, tagLen, "3*35x");
	};
	Rtn.fval_39C = Rtn.fval_42P = Rtn.fval_42M = function() {
		return nonGeneric.f_x(toNewLine(value), 0, tagLen, "4*35x");
	};
	Rtn.fval_43P = function() {
		return nonGeneric.f_x(value, 0, tagLen, "11x");
	}
	Rtn.fval_43T = function() {
		return nonGeneric.f_x(value, 0, tagLen, "1*35x");
	};
	Rtn.fval_44F = Rtn.fval_44E = Rtn.fval_44B = Rtn.fval_44A = function() {
		return nonGeneric.f_x(value, 0, tagLen, "1*65x");
	};
	Rtn.fval_44C = function() {
		return generic.DATE(value, "DATE2");
	};
	Rtn.fval_44D = function() {
		var wholeText = getTextAreaValue(value);
		return nonGeneric.f_x(wholeText, 0, tagLen, "5*65x");
	};
	Rtn.fval_45A = function() {
		return nonGeneric.f_z(toNewLine(value), 0, tagLen, "100*65x");
	};
	Rtn.fval_45D = function() {
		return nonGeneric.f_x(toNewLine(value), 0, tagLen, "150*65x");
	};
	Rtn.fval_47B = Rtn.fval_46B = Rtn.fval_45B = Rtn.fval_47A = Rtn.fval_46A = Rtn.fval_45A; // 小柯
	// 增47B
	// 46B
	// 45B
	Rtn.fval_48 = function() {
		var pattern = "3n[/35x]";
		// var arr = mySplit(value, 3, 1, 35);
		var arr = value.split("/");
		/*
		if (arr.length < 2)
			return myError(9999, pattern);
		if (!nonGeneric.f_n(arr[0], 1, 3, "3n"))
			return false;
		return nonGeneric.f_x(arr[1], 0, tagLen, "35x");
		*/
		if (arr.length > 2)
			return myError(9999, pattern);
		if (arr.length < 2){
			return  nonGeneric.f_n(value, 1, 3, "3n");
		}else{
			if (!nonGeneric.f_n(arr[0], 1, 3, "3n"))
				return false;

		    return nonGeneric.f_x(arr[1], 0, tagLen, "35x");
		}
	};
	Rtn.fval_49 = function() {
		if (!nonGeneric.f_x(value, 7, 7, "7!x")) {
			return false;
		}
		return checkCode(currentTag, value);
	};
	Rtn.fval_49N = function() {
		return nonGeneric.f_x(toNewLine(value), 0, tagLen, "100*65x");
	};
	Rtn.fval_49G = Rtn.fval_49H = Rtn.fval_49M = Rtn.fval_49N;
	// end of 40x
	// -----------------------------
	// ------------------------------
	// begin of 50x
	function xxxf5X_0(opt, pattern) {
		switch (opt) {
		case "A":
		case "B":
		case "D":
		case "K":
			if (pattern.indexOf("1!a") != -1) {
				return check_1a_34x(value);
			} else {
				return checkSlashe(value, 1, tagLen - 1, pattern);
			}
		case "F":
			return nonGeneric.f_x(value, 1, tagLen, pattern);
		case "J":
			// pattern = "<PARTYFLD/J>";
			return nonGeneric.f_x(getTextAreaValue(value), 0, 5 * 40, "5*40x");
			// 20140307 IZA ADD "S"
		case "S":
			var pattern = "2!a[/34x]";
			var ss = value.split('/'), arr;
			arr = ss.slice(1);
			textsum = arr.join(""); // 0307小柯 增 原本會有問題 因x型態可以包含斜線
			if (!nonGeneric.f_a(ss[0], 2, 2, "2!a")) {
				return false;
			}
			if (ss.length > 1) {
				return nonGeneric.f_x(textsum, 1, 34, pattern + "之[/34x]");
			}
			return true;
		}
		return myError(9999, "unknown opt for xxxf5X_0:" + opt);
	}

	function xxxf5X_1(opt, pattern) {
		var wholeText;
		// var len = parseInt(currentTag['$'].length, 10);
		switch (opt) {
		case "A":
			pattern = "BIC";
			updateFieldValueToUpper();
			generic.SWIFTBIC(ifx, $currentField, msgtype, value);
			return false; // cursor停在原欄位, 收到電文後再跳下一欄
			break;
		case "B":
			return nonGeneric.f_x(value, 0, tagLen);
			break;
		case "C":
			pattern = "'/'34x";
			return checkSlashe(value, 1, tagLen - 1, pattern);
			break;
		// 20140307 ADD "S"
		case "D":
		case "S":
			// pattern = "['/'<DC>]['/'34x]['CRLF']35x['CRLF'35x]0-3";
			wholeText = getTextAreaValue(value);
			return nonGeneric.f_x(wholeText, 0, tagLen);
			break;
		case "F":
		case "K":
			wholeText = getTextAreaValue(value);
			return nonGeneric.f_x(wholeText, 1, tagLen);
			break;
		case "G":
			pattern = "'/'34x'CRLF'<SWIFTBIC>|<NONSWIFTBIC>'";
			var lines = toLines(value);
			if (lines.length == 2) { // '/34... and swiftbic
				var arr = mySplit(lines[0], 1);
				if (!fvalSlash(arr[0], 1, " of " + pattern))
					return false;
				if (!nonGeneric.f_x(arr[1], 0, 34, "34x" + " of " + pattern))
					return false;
				if (!generic.SWIFTBIC(field, lines[1], lines[1].length))
					return false;
			} else {
				return myError(2006, pattern);
			}
			break;
		case "J":
			pattern = "<PARTYFLD/J>";
			// TODO:
			// <Format1> | <Format2> | <Format3>
			break;
		default:
			return myError(9999, "");
		}
		return true;
	}

	function xxxf5X_onlyOne(tag, opt, pattern) {
		switch (opt) {
		case "B":
			return nonGeneric.f_x(toNewLine(value), 0, tagLen, "4*35x");
		case "C":
			if (tag == "50") {
				updateFieldValueToUpper();
				return generic.SWIFTBIC(ifx, $currentField, msgtype, value);
			} else {
				return checkSlashe(value, 1, tagLen - 1, pattern);
			}
		case "L":
			return nonGeneric.f_x(value, 1, tagLen, pattern);
		}
		return true;
	}

	function call_5X_N() {
		var context = currentTag.$.context;
		// tagName = 53A_0
		if (tagName.indexOf('_') == -1) { // one field only
			return xxxf5X_onlyOne(tagName.slice(0, 2), tagName.slice(-1), context);
		}
		var subFieldIndex = tagName.slice(-1);
		var opt = tagName.slice(2, 3);
		switch (subFieldIndex) {
		case "0":
			return xxxf5X_0(opt, context);
			break;
		case "1":
			return xxxf5X_1(opt, context);
			break;
		}
		return myError(9999, tagName);
	}
	Rtn.fval_51 = Rtn.fval_50 = call_5X_N;
	Rtn.fval_58 = Rtn.fval_56 = Rtn.fval_55 = Rtn.fval_54 = Rtn.fval_53 = Rtn.fval_52 = Rtn.fval_51;
	Rtn.fval_57 = function() {
		var pattern;
		var opt = tagName.slice(2);
		// 柯:現在好像沒有521 523???
		if (opt == "D" && (mt == "521" || mt == "523")) {
			pattern = "('/ABIC/'<SWIFT-BIC>|NON-SWIFTBIC>|'00000000'|'00000000000')}('/ABNK/'29x)'CRLF'35x['CRLF'35x]";
			alert("57D for mt521, mt523  under construction");
			return true;
		}
		return Rtn.fval_53();
	};
	//
	// Rtn.fval_59 = function() {
	// var pattern = "[/34x]4*35x"; // [/34x](Account) 4*35x (Name &
	// // Address)
	// return nonGeneric.f_x(getTextAreaValue(value), 0, 5 * 35, pattern);
	// };
	//
	// TODO 59A 和 59 有問題 右邊黑框不會印
	// for 59A(bcde...
	Rtn.fval_59A_1 = call_5X_N;
	// for 59
	Rtn.fval_59F = Rtn.fval_59A = Rtn.fval_59_0 = function() {
		// [/34x]
		var pattern = currentTag.$.context;
		return checkSlashe(value, 1, tagLen - 1, pattern);
	};
	Rtn.fval_59_1 = function() {
		var wholeText = getTextAreaValue(value);
		return nonGeneric.f_x(wholeText, 0, tagLen, "4*35x");
	};
	Rtn.fval_59F_1 = function() {
		var pattern = currentTag.$.context;
		var lines = toLines(value);
		var unckmoreone = false;
		// "4*(1!n/33x)"
		for (var i = 0; i < lines.length; i++) {
			if (!nonGeneric.f_n(lines[i].slice(0, 1), 1, 1, pattern)) {
				return false;
			}
			// 59F 特殊在第num等於3時,斜線可以超過1個
			if (lines[i].slice(0, 1) == "3") {
				unckmoreone = true;
			}
			if (!checkSlashe(lines[i].slice(1), 1, 33, pattern, unckmoreone)) { // 33x
				return false;
			}
		}
		return true;
	};
	// end of 50x
	// begin of 60x
	Rtn.fval_60B = Rtn.fval_60A = function() {
		var v2;
		if (value.charAt(0) == "N") {
			v2 = value.slice(1);
			v2 = generic.NUMBER(v2, tagLen - 1);
			if (v2 === false)
				return false;
			updateFieldValue('N' + v2);
		} else {
			v2 = generic.NUMBER(value, tagLen - 1);
			if (v2 === false) {
				return false;
			}
			updateFieldValue(v2);
		}
		return true;
	};
	Rtn.fval_60M = Rtn.fval_60F = function() {
		return valDcDate2CodeAmount(currentTag, value);
	};
	Rtn.fval_61_0 = function() {
		var pattern;
		var oneChar;
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		// in field 61: the year value is selected from the system date at the
		// time the
		// validation is performed.
		// FORMAT
		// 6!n[4!n]2a[1!a]15d1!a3!c16x[//16x]
		// [34x]
		// where:
		// Subfield Format Name
		// 1 6!n Value Date (YYMMDD)
		// 2 [4!n] Entry Date (MMDD)
		// 3 2a Debit/Credit Mark
		// 4 [1!a] Funds Code (3rd character of the currency code, if needed)
		// 5 15d Amount
		// 6 1!a3!c Transaction Type Identification Code
		// 7 16x Reference for the Account Owner
		// 8 [//16x] Account Servicing Institution’s Reference
		// 1 6!n Value Date (YYMMDD)
		pattern = "6!n[4!n]2a[1!a]15d1!a3!c16x[//16x]";
		var subF = [];
		// var lines = toLines(value);
		// var arr = mySplit(lines[0], 6);
		var arr = mySplit(value, 6);
		if (!fvalEmpty(arr[1], pattern)) {
			return false;
		}
		subF[0] = arr[0];
		if (!generic.DATE(subF[0], "DATE2"))
			return false;
		if (isDigit(arr[1].charAt(0))) {
			// digit. we got sub field 2
			arr = mySplit(arr[1], 4);
			subF[1] = arr[0];
			if (!generic.DATE(subF[1], "DATE1")) {
				return false;
			}
		} else {
			subF[1] = ""; // no sub field 2, let it be empty
		}
		// 3 2a Debit/Credit Mark
		// 4 [1!a] Funds Code (3rd character of the currency code, if needed)
		// 5 15d Amount
		// 數字以前應該都屬於sub field 3, 4
		// sub field3 可能值為 D,C, RC,RD
		// try {
		subF[2] = "";
		if ((arr[1].substr(0, 2)).toUpperCase() == "RC"
				|| (arr[1].substr(0, 2)).toUpperCase() == "RD") {
			subF[2] = arr[1].substr(0, 2);
		} else if ((arr[1].substr(0, 1)).toUpperCase() == "C"
				|| (arr[1].substr(0, 1)).toUpperCase() == "D") {
			subF[2] = arr[1].substr(0, 1);
		}
		subF[2] = subF[2].toUpperCase();
		if (!checkCode(currentTag, subF[2], arrcompt[2] + ")"))
			return false;
		if (isDigit(arr[1].charAt(subF[2].length))) {
			// no subField 4, let it be empty
			subF[3] = "";
			arr[1] = arr[1].substr(subF[2].length);
		} else {
			subF[3] = arr[1].charAt(subF[2].length);
			arr[1] = arr[1].substr(subF[2].length + 1);
		}
		// } catch (ex1) {
		// return myError(1140, "2a[1!a] of " + pattern);
		// }
		var i, maxarr; // 小柯 增 效能
		maxarr = arr[1].length; // 小柯 增 效能
		for (i = 0; i < maxarr && i < 15; i++) { // search 1!a // 小柯 增 效能
			var oneChar = arr[1].charAt(i);
			if (isDigit(oneChar))
				continue;
			if (isABC(oneChar)) {
				i;
				break;
			}
		}
		arr = mySplit(arr[1], i);
		subF[4] = arr[0];
		subF[4] = generic.AMOUNT(null, subF[4], 15, "<AMOUNT>");
		if (subF[4] === false)
			return false;
		// 6 1!a3!c Transaction Type Identification Code
		if (arr[1] == null)
			return myError(1140, pattern + "的1!a3!c部份應該輸入");
		if (arr[1].length < 4) {
			return myError(1140, pattern + "的1!a3!c部份應該輸入");
		}
		arr = mySplit(arr[1], 4);
		subF[5] = arr[0];
		// if (!nonGeneric.f_a(subF[5].substr(0, 1), 1, 1, "1!a")) return false;
		if (!checkCode(currentTag, subF[5].substr(0, 1), arrcompt[5] + ")"))
			return false;
		if (!nonGeneric.f_c(subF[5].substr(1, 3), 3, 3, "3!c"))
			return false;
		// 7 16x Reference for the Account Owner
		// 8 [//16x] Account Servicing Institution’s Reference
		if (!arr[1])
			return myError(9999, pattern + "的16x部份至少輸入1碼");
		arr = arr[1].split("//");
		subF[6] = arr[0];
		if (!nonGeneric.f_x(subF[6], 1, 16, "第一個16x"))
			return false;
		if (arr.length > 1) {
			subF[7] = arr[1];
			if (!nonGeneric.f_x(subF[7], 1, 16, "第二個16x"))
				return false;
			// subF[7] ok, so insert "//" at the beginning of it
			subF[7] = "//" + subF[7];
		}
		// 9 [34x] Supplementary Details
		// if (lines.length > 1) {
		// if (!nonGeneric.f_x(lines[1], 1, 34, "第二行的34x")) {
		// return false;
		// }
		// }
		// lines[0] = subF.join("");
		// value = lines.join("\r\n");
		value = subF.join("");
		updateFieldValue(value);
		return true;
	};
	Rtn.fval_61_1 = function() {
		// 9 [34x] Supplementary Details
		var prevalue = $currentField.prevAll('input').val(); // /type可能不同 prev 改 prevAll 並加 input
		// var prevalue = $currentField.prev().val();
		if (prevalue.length == 0 && value.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		if (!nonGeneric.f_x(value, 0, 34, "34x"))
			return false;
		return true;
	};
	Rtn.fval_62B = Rtn.fval_62A = function() {
		var v2, n = '';
		if (value.charAt(0) == "N") {
			n = 'N';
			v2 = value.slice(1);
			v2 = generic.NUMBER(v2, tagLen - 1);
		} else {
			v2 = generic.NUMBER(field, value, tagLen - 1);
		}
		if (v2 === false) {
			return false;
		}
		updateFieldValue(n + v2);
		return true;
	};
	Rtn.fval_62M = Rtn.fval_62F = Rtn.fval_65 = Rtn.fval_64 = function() {
		return valDcDate2CodeAmount(currentTag, value); // 原本沒有 CURRENTAG 小柯 暫時換
	};
	// Rtn.fval_62M = Rtn.fval_62F = Rtn.fval_65 = Rtn.fval_64 = function() { //小柯 暫時換
	// return valDcDate2CodeAmount(value);
	// };
	Rtn.fval_67A = function() {
		var pattern = "6!n[/6!n]";
		var arr = value.split("/");
		if (!generic.DATE(arr[0], "DATE2")) {
			return false;
		}
		if (arr.length > 1) {
			return generic.DATE(arr[1], "DATE2");
		}
		return true;
	};
	Rtn.fval_68A = function() {
		pattern = "6n3!a6n/2n[/15d][//10x]";
		var arr = value.split("/");
		// TODO: 68A
		return true;
	};
	Rtn.fval_68B = function() {
		pattern = "6!n6!n16x/1!a3!a15d3!a15d/6!n3!a15d";
		var arr = value.split("/");
		var arr1 = mySplit(arr[0], 6, 6); // 6!n6!n16x
		var arr2 = mySplit(arr[1], 1, 3); // 1!a3!a15d3!a15d
		var arr3 = mySplit(arr[2], 6, 3); // 6!n3!a15d
		// TODO: 68B
		return true;
	};
	// end of 60x
	// begin of 70x
	Rtn.fval_70 = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 4 * 35, "4*35x");
	};
	Rtn.fval_70C_0 = function() { // TODO: 分行 行數問題 MT500
		var pattern = ":4!c//1*35x"; // (Qualifier) (Name)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!nonGeneric.f_x(arr[1], 1, 35, "3*35x") || !checkConsecutiveSlashes(arr[1])) {
			return false;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_70C_1 = function() { // TODO: 分行 行數問題 MT500
		var pattern = "3*35x"; // (Qualifier) (Name)
		var prevalue = $currentField.prevAll('input').val(); // /type不同 prev 改 prevAll 並加 input
		var lines = toLines(value);
		var linelng, linereal;
		if (prevalue.length == 0 && lines.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		for (linelng = 0; linelng <= 2; linelng++) {
			if (lines.length > linelng) {
				linereal = linelng + 1;
				if (!nonGeneric.f_x(lines[linelng], 0, 35, "lines " + linereal
						+ " of 3*35x")
						|| !checkConsecutiveSlashes(lines[linelng])) {
					return false;
				}
			}
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_70D_0 = function() { // TODO: 分行 行數問題 MT500
		var pattern = ":4!c//1*35x"; // (Qualifier) (Name)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!nonGeneric.f_x(arr[1], 1, 35, "5*35x") || !checkConsecutiveSlashes(arr[1])) {
			return false;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_70D_1 = function() { // TODO: 分行 行數問題 MT500
		var prevalue = $currentField.prevAll('input').val(); // /type不同 prev 改 prevAll 並加 input
		var pattern = "5*35x"; // (Qualifier) (Name)
		var lines = toLines(value);
		var linelng, linereal;
		if (prevalue.length == 0 && lines.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		for (linelng = 0; linelng <= 4; linelng++) {
			if (lines.length > linelng) {
				linereal = linelng + 1;
				if (!nonGeneric.f_x(lines[linelng], 0, 35, "lines " + linereal
						+ " of 5*35x")
						|| !checkConsecutiveSlashes(lines[linelng])) {
					return false;
				}
			}
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_70E_0 = function() { // TODO: 分行 行數問題 MT500
		var pattern = ":4!c//35x"; // (Qualifier) (Name)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!nonGeneric.f_x(arr[1], 1, 35, "3*35x") || !checkConsecutiveSlashes(arr[1])) {
			return false;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_70E_1 = function() { // TODO: 分行 行數問題 MT500
		var pattern = "9*35x"; // (Qualifier) (Name)
		var prevalue = $currentField.prevAll('input').val(); // /type不同 prev 改 prevAll 並加 input
		var lines = toLines(value);
		var linereal, linelng;
		if (prevalue.length == 0 && lines.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		for (linelng = 0; linelng <= 8; linelng++) {
			if (lines.length > linelng) {
				linereal = linelng + 1;
				if (!nonGeneric.f_x(lines[linelng], 0, 35, "lines " + linereal
						+ " of 9*35x")
						|| !checkConsecutiveSlashes(lines[linelng])) {
					return false;
				}
			}
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_71A = function() {
		updateFieldValueToUpper();
		return checkCode(currentTag, value);
	};
	Rtn.fval_71B = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 6 * 35, "6*35x");
	};
	Rtn.fval_71D = function() {
		return nonGeneric.f_z(getTextAreaValue(value), 0, 6 * 35, "6*35z");
	};
	Rtn.fval_71N = function() {
		var arr = value.split("\n");
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Code)"))
			return false;
		return nonGeneric.f_z(getTextAreaValue(value), 0, 6 * 35, "6*35x");
	};
	Rtn.fval_71N_0 = function() {
		return checkCode(currentTag, value, "(Code)");
	};
	Rtn.fval_71N_1 = function() {
		return nonGeneric.f_z(getTextAreaValue(value), 0, 6 * 35, "6*35x");
	};
	Rtn.fval_72Z = function() {
		return nonGeneric.f_z(getTextAreaValue(value), 0, 6 * 35, "6*35x");
	};
	Rtn.fval_73A = Rtn.fval_72Z;
	Rtn.fval_73R = function() {
		var pattern, arr, textsum, arrtext;
		pattern = "4!c[/35x]";
		arr = value.split("/");
		if (!checkCode(currentTag, arr[0], "(Code)"))
			return false;
		arrtext = value.slice(arr[0].length + 1);
		return nonGeneric.f_x(arrtext, 1, 35, "[35x]" + " of " + pattern);
	};
	Rtn.fval_73S = Rtn.fval_73R;
	Rtn.fval_71G_0 = Rtn.fval_71F_0 = Rtn.fval_32B_0;
	Rtn.fval_71G_1 = Rtn.fval_71F_1 = Rtn.fval_32B_1;
	// Rtn.fval_71G = Rtn.fval_71F = function() {
	// // 3!a15d (Currency) (Amount)
	// return valCurAmount(value);
	// };
	Rtn.fval_72 = function() {
		// 6*35x (Narrative)
		return nonGeneric.f_x(getTextAreaValue(value), 0, 6 * 35, "6*35x");
	};
	// Rtn.fval_73 = function() {
	// // if (msgtype == "400" || msgtype == "596" || msgtype == "595") {
	// // return nonGeneric.f_x(value, 0, 6 * 35, "6*35x");
	// // } else {
	// // return nonGeneric.f_x(value, 0, 35, "35x['CRLF'35x]");
	// // }
	// };
	// 參考swift規格2013後,移除tag73原本判斷全部改成跑tag 72 (6*35)
	Rtn.fval_76 = Rtn.fval_75 = Rtn.fval_74 = Rtn.fval_73 = Rtn.fval_72;
	Rtn.fval_77 = function() {
		return nonGeneric.f_z(getTextAreaValue(value), 0, 20 * 35, "20*35z");
	};
	Rtn.fval_77A = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 20 * 35, "20*35x"); // 小柯改
	};
	Rtn.fval_77B = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 3 * 35, "3*35x");
	};
	Rtn.fval_77C = function() { // 小柯增
		return nonGeneric.f_x(getTextAreaValue(value), 0, 150 * 65, "150*65x"); // 小柯增
	};
	Rtn.fval_77D = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 6 * 35, "6*35x");
	};
	Rtn.fval_77D = function() {
		if (msgtype.substr(1) == "98") { // n98
			if (getTextAreaValue(value).length > 9800) {
				return myError(1102, "73xCrLf[n*78x]");
			}
		}
		return true;
	};
	// 2014/03/10 IZA ADD
	Rtn.fval_77E_0 = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 73, "73x");
	};
	// 2014/03/10 IZA ADD
	Rtn.fval_77E_1 = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 120 * 78, "120*78x");
	};
	Rtn.fval_77H = function() {
		var arr = value.split("/");
		var context = "6a[/8!n][//4!n]";
		if (!checkCode(currentTag, arr[0], "(Type of Agreement)")) { // iza 20140527
			return false;
		}
		if (arr.length != 1) {
			if (!generic.DATE(arr[1], "DATE4")) {
				return myError(9999, "格式有誤");
			}
		}
		if (arr.length > 2) {
			if (arr[2] != "") {
				return myError(9999, "格式有誤");
			}
			if (!generic.DATE(arr[3], "DATE5")) {
				return myError(9999, "格式有誤");
			}
		}
		if (arr.length <= 4) {
			return true;
		} else {
			return myError(9999, "格式有誤");
		}
	};
	Rtn.fval_77T = Rtn.fval_77S = function() {
		return true;
	};
	Rtn.fval_77J = function() { // 0305小柯增
		return nonGeneric.f_x(getTextAreaValue(value), 0, 70 * 50, "70*50x"); // 0305小柯增
	}; // 0305小柯增
	Rtn.fval_78 = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 12 * 65, "12*65x");
	};
	Rtn.fval_79 = function() {
		return nonGeneric.f_x(getTextAreaValue(value), 0, 35 * 50, "35*50x");
	};
	Rtn.fval_79Z = function() {
		return nonGeneric.f_z(getTextAreaValue(value), 0, 35 * 50, "35*50z");
	};
	// end of 70x
	// begin of 80x
	Rtn.fval_80C = Rtn.fval_80B = Rtn.fval_80A = function() {
		return true;
	};
	Rtn.fval_81 = call_5X_N; // iza 20140527
	Rtn.fval_89 = call_5X_N;
	// Rtn.fval_82 = function() {
	// var opt = tagName.slice(2);
	// if ("ABCDJ".indexOf(opt) != -1) {
	// return xxxf5n(opt);
	// }
	// if (tagName == "82S") {
	// pattern = "2!a[/34x]'CRLF'[4*35x]"; // (Country Code) (Account
	// // Id)(Place)
	// // value =ReplaceTextArea(value);
	// // var arr = value.split("\n");
	// // var arr2 = arr[0].split("/"); // 2!a[/34x]
	// return true;
	// }
	//
	// return myError(9999, tagName);
	// };
	Rtn.fval_82 = call_5X_N;
	// Rtn.fval_83 = function() {
	// var opt = tagName.slice(2);
	// if ("ABCDJ".indexOf(opt) != -1) {
	// return xxxf5n(opt);
	// }
	// if (tagName == "83R") {
	// return checkCode(currentTag, value);
	// }
	//
	// return myError(9999, tagName);
	// };
	Rtn.fval_83 = call_5X_N;
	// 20140307 IZA MODIFY
	// Rtn.fval_84 = function() {
	// return call_5n('ABDJ');
	// };
	Rtn.fval_84 = call_5X_N;
	// 20140307 IZA MODIFY
	// Rtn.fval_85 = function() {
	// return call_5n('ABCDJ');
	// };
	Rtn.fval_85 = call_5X_N;
	// 20140307 IZA MODIFY
	// Rtn.fval_86 = function() {
	// var opt = tagName.slice(2);
	// if ("ADJ".indexOf(opt) != -1) {
	// return xxxf5n(opt);
	// } else {
	// return nonGeneric.f_x(value, 1, 65, "6*65x");
	// }
	// };
	Rtn.fval_86 = call_5X_N;
	Rtn.fval_87 = call_5X_N;
	// 20140303 IZA ADD
	Rtn.fval_88 = call_5X_N;
	// end of 80x
	// begin of 90x
	Rtn.fval_90A = function() {
		// :4!c//4!c/15d (Qualifier) (Percentage Type Code) (Price)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		var arr2 = arr[1].split("/");
		arr2[0] = arr2[0].toUpperCase(); // second 4!c
		if (!checkCode(currentTag, arr2[0], "(Percentage Type Code)")) { // check amount type
			// code
			return false;
		}
		arr2[1] = generic.AMOUNT(null, arr2[1], 15, "<AMOUNT>");
		if (arr2[1] === false) {
			return false;
		}
		arr[1] = arr2.join("/");
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_90B = function() {
		// :4!c//4!c/3!a15d (Qualifier) (Amount Type Code) (CurrencyCode)
		// (Price)
		var arr = splitQualifier(value);
		if (arr === false)
			return false;
		var arr2 = arr[1].split("/");
		arr2[0] = arr2[0].toUpperCase(); // second 4!c
		if (!checkCode(currentTag, arr2[0], "(Amount Type Code)")) { // check amount type code
			return false;
		}
		var bag = {};
		if (!valCurAmount(arr2[1], false, bag)) { // 小柯 先改 field, arr2[1], false, bag
			return false;
		}
		arr2[1] = bag['curamt'];
		arr[1] = arr2.join("/");
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_90C = function() {
		// 5n3!a15d (Number) (Currency) (Amount)
		var arr = deN(value, 5);
		if (!nonGeneric.f_n(arr[0], 1, 5, "5n")) {
			return false;
		}
		var bag = {};
		if (!valCurAmount(arr[1], false, bag)) { // 小柯 先改 field, arr[1], false, bag
			return false;
		}
		updateFieldValue(arr[0] + bag['curamt']);
		return true;
	};
	Rtn.fval_91 = call_5X_N; // iza 20140527
	Rtn.fval_92A = function() {
		var pattern = ":4!c//[N]15!d"; // (Qualifier) (Sign) (Number)
		var arr = splitQualifier(value);
		if (arr) {
			if (arr[1].charAt(0) == "N") {
				arr[1] = generic.NUMBER(null, arr[1].slice(1), 15, "15!n");
				if (arr[1] === false)
					return false;
				arr[1] = 'N' + arr[1];
			} else {
				arr[1] = generic.NUMBER(null, arr[1], 15, "15!n");
				if (arr[1] === false) {
					return false;
				}
			}
			updateFieldValue(":" + arr.join("//"));
			return true;
		}
		return false;
	};
	Rtn.fval_92B = function() {
		var pattern = "':'4!c'//'<CUR>'/'<CUR>'/'<NUMBER>15";
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		var arr2 = arr[1].split("/");
		if (arr2.length < 3) {
			return myError(2022, "field 92B");
		}
		if (!generic.CUR(arr2[0]) || !generic.CUR(arr2[1])) {
			return false;
		}
		arr2[2] = generic.NUMBER(null, arr2[2], 15, "15!n");
		if (arr2[2] === false)
			return false;
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_92C = function() {
		var pattern = "':'4!c'/'[8c]'/'24x", arr = value.split("/"), anyError;
		if (arr.length < 3) {
			return myError(2021, "");
		}
		anyError = !fvalColon(arr[0].charAt(0))
				|| !nonGeneric.f_c(arr[0].substr(1), 4, 4, "4!c")
				|| !nonGeneric.f_c(arr[1], 0, 8, "8c")
				|| !nonGeneric.f_x(arr[2], 0, 24, "24x");
		return !anyError;
	};
	Rtn.fval_92D = function() {
		var pattern = "';'4!c'//'<NUMBER>15'/'<NUMBER>15", arr = splitQualifier(value), arr2 = arr[1]
				.split("/");
		if (!generic.NUMBER(null, arr2[0], 15, "15!n")
				|| !generic.NUMBER(null, arr2[1], 15, "15!n")) {
			return false;
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_92E = function() {
		var pattern = ":4!c//4!c'/'[N]<NUMBER>15['/'4c]"; // (Qualifier)
		// (Sign) (Number)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		var arr2 = arr[1].split("/");
		if (arr2.length < 2) {
			return myError(2021, "");
		}
		if (!nonGeneric.f_c(arr2[0], 4, 4, "4!c")) {
			return false;
		}
		if (arr2[1].charAt(0) == "N") {
			if (generic.NUMBER(null, arr2[1].slice(1), 15) === false) {
				return false;
			}
		} else {
			if (generic.NUMBER(null, arr2[1], 15) === false) {
				return false;
			}
		}
		if (arr2.length > 2) {
			return nonGeneric.f_c(arr2[2], 4, 4, "4!c");
		}
		return true;
	};
	Rtn.fval_92F = function() {
		var pattern = "':'4!c'//'<CUR><NUMBER>15";
		var arr = splitQualifier(value);
		var bag = {};
		if (!valCurAmount(arr[1], false, bag)) {
			return false;
		}
		arr[1] = bag['curamt'];
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_93A = function() {
		var pattern = "':'4!c'/'[8c]'/'4!c";
		var arr = value.split("/");
		var anyError;
		if (arr.length < 3) {
			return myError(2021, "");
		}
		anyError = !fvalColon(arr[0].charAt(0))
				|| !nonGeneric.f_c(arr[0].substr(1), 4, 4, "4!c")
				|| !nonGeneric.f_c(arr[1], 0, 8, "8c")
				|| !nonGeneric.f_c(arr[2], 4, 4, "4!c");
		return !anyError;
	};
	Rtn.fval_93B = function() { // TODO 改金額
		var pattern = "':'4!c'/'[8c]'/'4!c'/'[N]<NUMBER>15";
		var arr = value.split("/");
		var anyError;
		if (arr.length < 4) {
			return myError(2021, "");
		}
		anyError = !fvalColon(arr[0].charAt(0))
				|| !nonGeneric.f_c(arr[0].substr(1), 4, 4, "4!c")
				|| !nonGeneric.f_c(arr[1], 0, 8, "8c")
				|| !nonGeneric.f_c(arr[2], 4, 4, "4!c");
		if (anyError) {
			return false;
		}
		if (arr[1] == "") {
			if (!checkCode(currentTag, arr[2], "(Quantity Type Code)")) {
				return false;
			}
		}
		var bag;
		bag = {};
		if (arr[3].charAt(0) == "N") {
			if (!valAmount(arr[3].slice(1), false, bag, 15)) {
				return false;
			}
			arr[4] = "N" + bag['amt'];
		} else {
			if (!valAmount(arr[3], false, bag, 15)) {
				return false;
			}
			arr[4] = bag['amt'];
		}
		updateFieldValue(arr[0] + "/" + arr[1] + "/" + arr[2] + "/" + arr[4]);
		return true;
	};
	Rtn.fval_93C = function() {
		var pattern = ":4!c//4!c'/'4!c'/'[N]<NUMBER15";
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		var arr2 = arr[1].split("/");
		if (arr2.length < 3) {
			return myError(2021, "");
		}
		if (!nonGeneric.f_c(arr2[0], 4, 4, "4!c")
				|| !nonGeneric.f_c(arr2[1], 4, 4, "4!c")) {
			return false;
		}
		if (arr2[2].charAt(0) == "N") {
			if (generic.NUMBER(null, arr2[0].substr(1), 15) === false) {
				return false;
			}
		} else {
			if (generic.NUMBER(null, arr2[0], 15) === false) {
				return false;
			}
		}
		return true;
	};
	Rtn.fval_93D = function() {
		var pattern = "':'4!c'//'['N']<NUMBER>15";
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		arr[1] = arr[1].toUpperCase();
		if (arr[1].charAt(0) == "N") {
			if (generic.NUMBER(null, arr[1].substr(1), 15) === false) {
				return false;
			}
		} else {
			if (generic.NUMBER(null, arr2[1], 15) === false) {
				return false;
			}
		}
		return true;
	};
	Rtn.fval_93R = function() {
		// 4!a (Role)
		return checkCode(currentTag, value);
	};
	Rtn.fval_94A = function() {
		if (value) {
			return checkCode(currentTag, value);
		}
		return true;
	};
	Rtn.fval_94B = function() { // TODO 太多不一樣的例子
		var pattern = ":4!c/[8c]/4!c[/30x]";
		// var arr = value.split("/");
		var arr = mySplit(value, 1, 4, "/", "/");
		var anyError;
		// alert("arr[0]="+arr[0]);
		// alert("arr[1]="+arr[1]);
		// alert("arr[2]="+arr[2]);
		// alert("arr[3]="+arr[3]);
		// alert("arr[4]="+arr[4]);
		// alert("arr[5]="+arr[5]);
		// alert("arr[6]="+arr[6]);
		if (arr.length < 3) {
			return myError(2021, "");
		}
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "first 4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "8c")
				|| !fvalSlash(arr[4], 1, "middle" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5].slice(0, 4), 4, 4, "last 4!c" + " of "
						+ pattern);
		if (anyError) {
			return false;
		}
		if (!arr[3]) {
			if (!checkCode(currentTag, arr[5].slice(0, 4), "(Place Code)"))
				return false;
		}
		if (arr[5].length > 4) {
			if (arr[5].slice(4, 5) == "/") {
				if (checkConsecutiveSlashes(arr[5].slice(5))) {
					return nonGeneric.f_x(arr[5].slice(5), 1, 30, "30x");
				} else {
					// return myError(9999, "/30x of " + pattern);//
					return checkConsecutiveSlashes(arr[5].slice(5));
				}
			} else {
				if (!checkCode(currentTag, arr[5].slice(0), "(Place Code)"))
					return false;
			}
			return false;
		}
		return true;
	};
	Rtn.fval_94C = function() {
		var pattern = ":4!c//2!a";
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		// Country Code must be a valid ISO country code (Error code(s): T73).
		// todo if (!generic.Country(arr[1], "Country")) {
		// return false;
		if (!nonGeneric.f_a(arr[1], 2, 2, "2!a")) {
			return false;
		}
		return true;
		// }
	};
	Rtn.fval_94D = function() {
		var pattern = ":4!c//[2!a]/35x";
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		var arr2 = arr[1].split("/");
		if (arr2[0].length != 0) {
			if (!nonGeneric.f_a(arr2[0], 2, 2, "[2!a]")) {
				return false;
			}
		}
		return nonGeneric.f_x(arr2[1], 1, 35, "35x");
		// }
	};
	Rtn.fval_94F = function() {
		var pattern = "':'4!c'//'4!c'/'<SWIFTBIC>|<NONSWIFTBIC>";
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		var arr2 = mySplit(arr[1], 4, "/");
		var anyError;
		// if (arr.length < 4) {
		// return myError(2021, "");
		// }
		if (value.slice(1, 5) == "SAFE") {
			return checkCode(currentTag, arr2[0], "(Place Code)");
		} else {
			if (!nonGeneric.f_c(arr2[0], 4, 4, "4!c")) {
				return false;
			}
		}
		anyError = !fvalSlash(arr2[1], 1, "FIRST" + "/" + " of " + pattern);
		if (anyError) {
			return false;
		}
		if (!generic.SWIFTBIC(ifx, $currentField, msgtype, arr2[3])) {
			return false;
		}
		return true;
	};
	Rtn.fval_94G_0 = function() {
		var pattern = ":4!c//35x"; // (Qualifier) (Name) :4!c//2*35x
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!nonGeneric.f_x(arr[1], 1, 35, "2*35x") || !checkConsecutiveSlashes(arr[1])) {
			return false;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_94G_1 = function() {
		var prevalue = $currentField.prevAll('input').val(); // /type不同 prev 改 prevAll 並加 input
		var lines = toLines(value);
		if (prevalue.length == 0 && lines.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		if (lines.length > 0) {
			if (!nonGeneric.f_x(lines[0], 0, 35, "2*35x")
					|| !checkConsecutiveSlashes(lines[0])) {
				return false;
			}
		}
		return true;
	};
	Rtn.fval_94H = function() {
		var pattern = ":4!c//4!a2!a2!c[3!c]"; // (Qualifier) (Identifier Code )
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		// updateFieldValueToUpper();
		if (!generic.SWIFTBIC(ifx, $currentField, msgtype, arr[1])) {
			return generic.SWIFTBIC(ifx, $currentField, msgtype, arr[1]);
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_95C = function() {
		var pattern = ":4!c//2!a";
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)"))
			return false;
		if (!nonGeneric.f_a(arr[1], 2, 2, "2!a")) {
			return false;
		}
		return true;
		// }
	};
	Rtn.fval_95P = function() {
		var pattern = ":4!c//4!a2!a2!c[3!c]"; // (Qualifier) (Identifier Code )
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)"))
			return false;
		// updateFieldValueToUpper();
		if (!generic.SWIFTBIC(ifx, $currentField, msgtype, arr[1])) {
			return generic.SWIFTBIC(ifx, $currentField, msgtype, arr[1]);
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_95Q_0 = function() {
		var pattern = ":4!c//4*35x"; // (Qualifier) (Name) :4!c//2*35x
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)"))
			return false;
		if (!nonGeneric.f_x(arr[1], 1, 35, "4*35x") || !checkConsecutiveSlashes(arr[1])) {
			return false;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_95Q_1 = function() {
		var pattern = "4*35x"; // (Qualifier)(Name and Address)
		var prevalue = $currentField.prevAll('input').val(); // /type不同 prev 改 prevAll 並加 input
		var lines = toLines(value);
		var linelng, linereal;
		if (prevalue.length == 0 && lines.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		for (linelng = 0; linelng <= 3; linelng++) {
			if (lines.length > linelng) {
				linereal = linelng + 1;
				if (!nonGeneric.f_x(lines[linelng], 0, 35, "lines " + linereal
						+ " of 4*35x")
						|| !checkConsecutiveSlashes(lines[linelng])) {
					return false;
				}
			}
		}
		return true;
	};
	// ! 小柯 增加 95R
	Rtn.fval_95R = function() {
		var pattern, arr, anyError;
		pattern = ":4!c/8c/34x";
		arr = mySplit(value, 1, 4, "/", "/");
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 1, 8, "8c" + " of " + pattern)
				|| !fvalSlash(arr[4], 1, " of " + pattern)
				|| !nonGeneric.f_x(arr[5], 1, 30, "34x" + " of " + pattern);
		if (!checkCode(currentTag, arr[1], "(Qualifier)"))
			return false;
		if (!checkConsecutiveSlashes(arr[5])) {
			return false;
		}
		return !anyError;
	};
	Rtn.fval_95S = function() {
		var pattern = ":4!c/[8c]/4!c/2!a/30x";
		// var arr = value.split("/");
		var arr = mySplit(value, 1, 4, "/", "/", 4, "/", 2, "/");
		var anyError;
		// if (arr.length < 4) {
		// return myError(2021, "");
		// }
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "first 4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "FIRST" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "8c")
				|| !fvalSlash(arr[4], 1, "SECOND" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5], 4, 4, "last 4!c" + " of " + pattern)
				|| !fvalSlash(arr[6], 1, "THIRD" + "/" + " of " + pattern)
				|| !nonGeneric.f_a(arr[7], 2, 2, "2!a" + " of " + pattern)
				|| !fvalSlash(arr[8], 1, "LAST" + "/" + " of " + pattern)
				|| !nonGeneric.f_x(arr[9], 1, 30, "30x" + " of " + pattern);
		if (anyError) {
			return false;
		}
		// S 是O 不是 M 雖然加這段也沒關係，因外面定O
		// if(!checkCode(currentTag, arr[1], "(Qualifier)")) return false;
		if (arr[3].length == 0) {
			return checkCode(currentTag, arr[5], "(Type of ID)");
		}
		if (!checkConsecutiveSlashes(arr[9])) {
			return false;
		}
		return true;
	};
	Rtn.fval_95T_0 = function() {
		var pattern = ":4!c//35x"; // (Qualifier) (Name) :4!c//2*35x
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!nonGeneric.f_x(arr[1], 1, 35, "2*35x") || !checkConsecutiveSlashes(arr[1])) {
			return false;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_95T_1 = function() {
		var pattern = "2*35x";
		var prevalue = $currentField.prevAll('input').val(); // /type不同 prev 改 prevAll 並加 input
		var lines = toLines(value);
		var linelng, linereal;
		if (prevalue.length == 0 && lines.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		for (linelng = 0; linelng <= 1; linelng++) {
			if (lines.length > linelng) {
				linereal = linelng + 1;
				if (!nonGeneric.f_x(lines[linelng], 0, 35, "lines " + linereal
						+ " of 2*35x")
						|| !checkConsecutiveSlashes(lines[linelng])) {
					return false;
				}
			}
		}
		return true;
	};
	Rtn.fval_95U_0 = function() { // TODO: 分行 行數問題 MT500
		var pattern = ":4!c//35x"; // (Qualifier) (Name)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!nonGeneric.f_x(arr[1], 1, 35, "3*35x") || !checkConsecutiveSlashes(arr[1])) {
			return false;
		}
		// updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_95U_1 = function() {
		var prevalue = $currentField.prevAll('input').val(); // /type不同 prev 改 prevAll 並加 input
		var lines = toLines(value);
		if (prevalue.length == 0 && lines.length > 0)
			return myError(9999, "第一行未輸入, 第二行請勿輸入");
		if (lines.length > 0) {
			if (!nonGeneric.f_x(lines[0], 0, 35, "3*35x")
					|| !checkConsecutiveSlashes(lines[0])) {
				return false;
			}
		}
		if (lines.length > 1) {
			if (!nonGeneric.f_x(lines[1], 0, 35, "3*35x")
					|| !checkConsecutiveSlashes(lines[1])) {
				return false;
			}
		}
		return true;
	};
	Rtn.fval_96 = call_5X_N; // iza 20140527
	Rtn.fval_97A = function() {
		var pattern = ":4!c//35x"; // (Qualifier) (Account Number)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)"))
			return false;
		if (!nonGeneric.f_x(arr[1], 1, 35, "35x")) {
			return false;
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_97B = function() { // 97B有問題
		var pattern, arr;
		pattern = "':'4!c'/'[8c]'/'4!c/35x"; // (**);
		arr = mySplit(value, 1, 4, "/", "/", 4, "/");
		// alert(arr[0]+"#"+arr[1]+"#"+arr[2]+"#"+arr[3]+"#"+arr[4]+"#"+arr[5]+"#"+arr[6]+"#"+arr[7]);
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "[4!c]" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first " + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)
				|| !fvalSlash(arr[4], 1, "middle " + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5], 4, 4, "[4!c]" + " of " + pattern)
				|| !fvalSlash(arr[6], 1, "last " + "/" + " of " + pattern)
				|| !nonGeneric.f_x(arr[7], 1, 35, "35x" + " of " + pattern);
		if (!checkCode(currentTag, arr[1], "(Qualifier)")) {
			return false;
		}
		if (!checkCode(currentTag, arr[5], "(Account Type Code)")) {
			return false;
		}
		return !anyError;
	};
	Rtn.fval_97C = function() {
		var pattern = ":4!c//4!c";
		var arr = splitQualifier(value);
		if (!nonGeneric.f_c(arr[1], 4, 4, "4!c")) {
			return false;
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_97E = function() {
		var pattern = ":4!c//34x"; // (Qualifier) (International Bank Account Number)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)"))
			return false;
		if (!nonGeneric.f_x(arr[1], 1, 34, "34x")) {
			return false;
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_98A = function() {
		var pattern = ":4!c//8!n"; // (Qualifier) (Date)
		var arr = splitQualifier(value);
		// alert(arr[1]);
		if (arr) {
			if (!checkCode(currentTag, arr[0], "(Qualifier)")) {
				return false;
			}
			if (!generic.DATE(arr[1], "DATE4")) {
				return false;
			}
			updateFieldValue(":" + arr.join("//"));
			return true;
		}
		return false;
	};
	Rtn.fval_98B = function() {
		var codeDef;
		// alert(codeDef.cond);
		var pattern, arr;
		var compon = currentTag.$.component;
		var arrcompt = compon.split(")");
		pattern = ":4!c/[8c]/4!c"; // (Qualifier)(Data Source Scheme)(Date Code)
		arr = mySplit(value, 1, 4, "/", "/");
		codeDef = currentTag.getCodesByKey("(Date Code-" + arr[1] + ")"); // 找出是否有相符的設定
		anyError = !fvalColon(arr[0])
				|| !nonGeneric.f_c(arr[1], 4, 4, "first 4!c" + " of " + pattern)
				|| !fvalSlash(arr[2], 1, "first" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[3], 0, 8, "[8c]" + " of " + pattern)
				|| !fvalSlash(arr[4], 1, "last" + "/" + " of " + pattern)
				|| !nonGeneric.f_c(arr[5], 4, 4, "last 4!c" + " of " + pattern)
				|| !checkCode(currentTag, arr[1], arrcompt[0] + ")");
		if (anyError) { // 小柯改 FOR Qualifier
			return !anyError;
		} // 小柯改 FOR Qualifier
		if (codeDef && !arr[3]) { // (Qualifier) 替換成 codeDef.cond但傳入的其實還是固定的"位置"
			return checkCode(currentTag, arr[5], "(Date Code-" + arr[1] + ")")
		}
		return true;
	};
	Rtn.fval_98C = function() {
		var pattern = ":4!c//8!n6!n"; // (Qualifier) (Date) (Time)
		var arr = splitQualifier(value);
		if (arr) {
			if (!checkCode(currentTag, arr[0], "(Qualifier)")) {
				return false;
			}
			var arr2 = mySplit(arr[1], 8);
		} // 小柯改
		else {
			return false;
		}
		if (!generic.DATE(arr2[0], "DATE4")) {
			return false;
		}
		if (arr2[1]) {
			if (!generic.TIME2(arr2[1])) {
				return false;
			}
		} else {
			return myError(9999, pattern);
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_98D = function() {
		var pattern = "8!n6!n[,3n][/[N]2!n[2!n]]";
		if (!generic.DATE(value.slice(0, 8), "DATE4")) {
			return false;
		}
		if (!generic.TIME2(value.slice(8, 14))) {
			return false;
		}
		if (value.length > 14) {
			oo = check_98(value.substr(14));
			if (oo.f2 == null && oo.f3 == null)
				return myError(9999, pattern);
			;
			if (oo.f2 != null) {
				if (!nonGeneric.f_n(oo.f2, 1, 3, ",3n"))
					return false;
			}
			if (oo.f3 != null) {
				if (oo.f3.charAt(0) == "N") {
					oo.f3 = oo.f3.substr(1);
					if (oo.f3.length > 2) {
						if (!nonGeneric.f_n(oo.f3.slice(2), 2, 2, "2!n"))
							return false;
					}
					if (!nonGeneric.f_n(oo.f3.slice(0, 2), 2, 2, "2!n"))
						return false;
				} else {
					if (oo.f3.length > 2) {
						if (!nonGeneric.f_n(oo.f3.slice(2), 2, 2, "2!n"))
							return false;
					}
					if (!nonGeneric.f_n(oo.f3.slice(0, 2), 2, 2, "2!n"))
						return false;
				}
			}
		}
		return true;
	};
	Rtn.fval_98E = function() { // TODO : 98E
		var pattern = ":4!c//8!n6!n[,3n][/[N]2!n[2!n]]"; // (Qualifier)(Date)(Time)(Decimals)(UTC
		// Indicator)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!checkCode(currentTag, arr[0], "(Qualifier)")) {
			return false;
		}
		var valretest = arr[1];
		if (!generic.DATE(valretest.slice(0, 8), "DATE4")) {
			return false;
		}
		if (!generic.TIME2(valretest.slice(8, 14))) {
			return false;
		}
		if (valretest.length > 14) {
			oo = check_98(valretest.substr(14));
			if (oo.f2 != null) {
				if (!nonGeneric.f_n(oo.f2, 3, 3, "3!n")) {
					return false;
				}
			}
			if (oo.f3 != null) {
				if (oo.f3.charAt(0) == "N") {
					oo.f3 = oo.f3.substr(1);
					if (oo.f3.length > 2) {
						if (!nonGeneric.f_n(oo.f3.slice(2), 2, 2, "2!n")) {
							return false;
						}
					}
					if (!nonGeneric.f_n(oo.f3.slice(0, 2), 2, 2, "2!n")) {
						return false;
					}
				} else {
					if (oo.f3.length > 2) {
						if (!nonGeneric.f_n(oo.f3.slice(2), 2, 2, "2!n")) {
							return false;
						}
					}
					if (!nonGeneric.f_n(oo.f3.slice(0, 2), 2, 2, "2!n")) {
						return false;
					}
				}
			}
		}
		return true;
	};
	Rtn.fval_99A = function() {
		var pattern = ":4!c//[N]3!n"; // (Qualifier) (Sign) (Number)
		// DAAC Number of Days Accrued
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (arr[1].length > 3) {
			arr[1] = arr[1].toUpperCase();
			if (arr[1].charAt(0) != "N") {
				return myError(1114, pattern);
			}
			if (!nonGeneric.f_n(arr[1].substr(1), 3, 3, "3!n")) {
				return false;
			}
		} else {
			if (!nonGeneric.f_n(arr[1], 3, 3, "3!n")) {
				return false;
			}
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	Rtn.fval_99B = function() {
		var pattern = ":4!c//3!n"; // (Qualifier) (Number)
		var arr = splitQualifier(value);
		if (arr === false) {
			return false;
		}
		if (!nonGeneric.f_n(arr[1], 3, 3, "3!n")) {
			return false;
		}
		updateFieldValue(":" + arr.join("//"));
		return true;
	};
	// end of 90x
	return {
		'init' : init,
		'setCurrentField' : setCurrentField,
		'rtn' : Rtn,
		'errmsg' : errmsg,
		'myError' : myError,
		'trigger' : trigger,
		'goNext' : goNext
	};
}(jQuery));
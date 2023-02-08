// Modules:type extend, IfxUtl, IfxError
var IfxUtl;
if (!IfxUtl) {
	IfxUtl = {};
}
(function() {
	var fldPrefix = "fld_";
	IfxUtl.id2name = function(id) {
		return "#" + id.replace(fldPrefix, "");
	};
	IfxUtl.name2id = function(name) {
		return fldPrefix + name.slice(1);
	};
	IfxUtl.is_array = function(value) {
		return Object.prototype.toString.apply(value) === '[object Array]';
	};
	IfxUtl.is_function = function(fn) {
		return Object.prototype.toString.call(fn) === "[object Function]";
	};
	IfxUtl.bind1 = function(fn, object) {
		return function() {
			return fn.apply(object, arguments);
		};
	};
	IfxUtl.bind = function(context, name) {
		return function() {
			return context[name].apply(context, arguments);
		};
	};
	IfxUtl.method = function(object, name) {
		return function() {
			object[name].apply(object, arguments);
		};
	};
	IfxUtl.clone = function(object) {
		function OneShotConstructor() {}
		OneShotConstructor.prototype = object;
		return new OneShotConstructor();
	};
	IfxUtl.deepCopy = function(obj) {
		var val, length, i, temp = [];
		if (Array.isArray(obj)) {
			for (i = 0, length = obj.length; i < length; i++) {
				// Store reference to this array item's value
				val = obj[i];
				// If array item is an object (including arrays), derive new
				// value by cloning
				if (typeof val === "object") {
					val = clone(val);
				}
				temp[i] = val;
			}
			return temp;
		}
		// Create a new object whose prototype is a new, empty object,
		// Using the second properties object argument to copy the source
		// properties
		return Object.create({}, (function(src) {
			// Initialize a cache for non-inherited properties
			var props = {};
			Object.getOwnPropertyNames(src).forEach(function(name) {
				// Store short reference to property descriptor
				var descriptor = Object.getOwnPropertyDescriptor(src, name);
				// Recurse on properties whose value is an object or array
				if (typeof src[name] === "object") {
					descriptor.value = clone(src[name]);
				}
				props[name] = descriptor;
			});
			return props;
		}(obj)));
	};
	IfxUtl.serial_maker = function() {
		// Produce an object that produces unique strings. A
		// unique string is made up of two parts: a prefix
		// and a sequence number. The object comes with
		// methods for setting the prefix and sequence
		// number, and a gensym method that produces unique
		// strings.
		var prefix = '';
		var seq = 0;
		return {
			set_prefix: function(p) {
				prefix = String(p);
			},
			set_seq: function(s) {
				seq = s;
			},
			gensym: function() {
				var result = prefix + seq;
				seq += 1;
				return result;
			}
		};
	};
	IfxUtl.forEach = function(array, actor) {
		for (var i = 0; i < array.length; i++) actor(array[i]);
	};
	IfxUtl.negate = function(func) {
		return function() {
			return !func.apply(null, arguments);
		};
	};
	IfxUtl.reduce = function(combine, base, array) {
		this.forEach(array, function(element) {
			base = combine(base, element);
		});
		return base;
	};
	IfxUtl.map = function(func, array) {
		var result = [];
		this.forEach(array, function(element) {
			result.push(func(element));
		});
		return result;
	};
	IfxUtl.count = function(test, array) {
		var counted = 0;
		this.forEach(array, function(element) {
			if (test(element)) counted++;
		});
		return counted;
	}
	IfxUtl.compose = function(f1, f2) {
		return function() {
			return f1(f2.apply(null, arguments));
		};
	};
	IfxUtl.round = function(n, fixedLen) {
		var b = Math.pow(10, fixedLen);
		var sum = accMul(n, b);
		return Math.round(sum) / b;
	};
	IfxUtl.floor = function(n, fixedLen) {
		// 需求:負數的無條件捨去與正數時相同
		// 10.725 -> 10
		// -10.725 -> -10
		var isnegative = false;
		if (n < 0) {
			n = n * -1;
			isnegative = true;
		}
		var b = Math.pow(10, fixedLen);
		var sum = accMul(n, b);
		return isnegative ? (Math.floor(sum) / b * -1) : Math.floor(sum) / b;
	};
	// 柯 返回值：arg1乘以 arg2的精確結果(for IfxUtl.floor:1029.09 --> 1029.8999)
	function accMul(arg1, arg2) {
		var m = 0,
			s1 = arg1.toString(),
			s2 = arg2.toString();
		try {
			m += s1.split(".")[1].length
		} catch (e) {}
		try {
			m += s2.split(".")[1].length
		} catch (e) {}
		return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
	};
	// 柯 返回值：arg1除以 arg2的精確結果 順便加入 不知哪邊會用到
	function accDiv(arg1, arg2) {
		var t1 = 0,
			t2 = 0,
			r1, r2;
		try {
			t1 = arg1.toString().split(".")[1].length
		} catch (e) {}
		try {
			t2 = arg2.toString().split(".")[1].length
		} catch (e) {}
		with(Math) {
			r1 = Number(arg1.toString().replace(".", ""))
			r2 = Number(arg2.toString().replace(".", ""))
			return (r1 / r2) * pow(10, t2 - t1);
		}
	}

	function round2(number, digits) {
		if (typeof digits === "undefined" || digits < 0) {
			digits = 0;
		}
		var power = Math.pow(10, digits),
			fixed = (Math.round(number * power) / power).toString();
		return fixed;
	}
	// http://joncom.be/code/javascript-rounding-errors/
	function Round(Number, DecimalPlaces) {
		return Math.round(parseFloat(Number) * Math.pow(10, DecimalPlaces)) / Math.pow(10, DecimalPlaces);
	}

	function RoundFixed(Number, DecimalPlaces) {
		return Round(Number, DecimalPlaces).toFixed(DecimalPlaces);
	}

	function formatCurrency(sSymbol, vValue) {
		aDigits = vValue.toFixed(2).split(".");
		aDigits[0] = aDigits[0].split("").reverse().join("").replace(/(\d{3})(?=\d)/g, "$1,").split("").reverse().join("");
		return sSymbol + aDigits.join(".");
	}
	IfxUtl.substr_big5 = function(value, offset, length, bPad) {
		bPad = bPad || false;
		var gotLen = 0;
		var startLen = 0;
		var t = "";
		for (var i = 0; i < value.length; i++) {
			var ch = value.charAt(i);
			var chlen2 = IfxUtl.strlen2("" + ch);
			if (startLen < offset) {
				startLen += chlen2;
				continue;
			}
			gotLen += chlen2;
			t += ch;
			if (gotLen >= length) {
				break;
			}
		}
		if (bPad) {
			while (gotLen < length) {
				t += " ";
				gotLen++;
			}
		}
		return t;
	};
	IfxUtl.trim = function(v) {
		return v.replace(/^\s+|\s+$/g, "");
	};
	IfxUtl.ltrim = function(v) {
		return v.replace(/^\s+/, "");
	};
	IfxUtl.rtrim = function(v) {
		return v.replace(/\s+$/, "");
	};
	IfxUtl.trim4LongString = function(t) {
		var str = t.replace(/^\s\s*/, ''),
			ws = /\s/,
			i = str.length;
		while (ws.test(str.charAt(--i)));
		return str.slice(0, i + 1);
	};
	IfxUtl.matchXmlTag = function(s) {
		var x = /<(\w+)>(.+)<\/\1>/;
		return s.match(x);
	};
	IfxUtl.strlen = function(s) {
		var arr = s.match(/[^\x00-\xff]/ig);
		return arr == null ? s.length : s.length + arr.length;
	};
	IfxUtl.strlen2 = function(s) {
		return s.replace(/[^\x00-\xff]/g, "rr").length;
	}
	IfxUtl.lengthInUtf8Bytes = function(str) {
		// Matches only the 10.. bytes that are non-initial characters in a
		// multi-byte sequence.
		var m = encodeURIComponent(str).match(/%[89ABab]/g);
		return str.length + (m ? m.length : 0);
	}
	// X type to String,
	IfxUtl.stringFormatter = function(value, len, fillLeft) {
		/*
		 * if (typeof (value) != "String") value = new String(value); var l = value.length; if (l >=
		 * length) return value.substring(0, l);
		 *
		 * var fillChar = " "; for (; l < length;) { value += fillChar; l = value.length; } return
		 * value;
		 */
		if (value.length > len) return value.slice(0, len);
		var p = new Array(len - value.length + 1).join(" ");
		if (fillLeft) {
			return p + value;
		} else {
			return value + p;
		}
	};
	IfxUtl.stringFormatterBig5 = function(value, len, bPadLeft) {
		bPadRight = (bPadLeft == undefined) ? true : bPadLeft;
		var l = IfxUtl.strlen(value);
		var o;
		// 鍾 stringFormatterBig5加上該變數是null的處理
		if (!len) {
			len = l;
		}
		if (l >= len) {
			return IfxUtl.substr_big5(value, 0, len, false);
		}
		var padLen = len - l;
		var p = new Array(padLen + 1).join(" ");
		if (bPadRight) {
			return value + p;
		} else {
			return p + value;
		}
	};
	IfxUtl.stringtohostcC = function(value, len) {
		console.log("R6 need to cut space.");
		var o = this.checkChinese(value, len, true);
		console.log("host:" + o.host + ",CUT:" + o.cut + ",CUT len:" + o.cut.length);
		var t = o.host;
		var ocut = o.cut;
		if (!o.status) {
			console.log("too len return cut");
			return ocut;
		} else {
			var num4 = t.split(/\4/).length - 1;
			var num7 = t.split(/\7/).length - 1;
			console.log("tohostcC value:" + value + ",len " + len + "num4:" + num4 + "num7:" + num7);
			return value.slice(0, len - num4 - num7);
		}
	};
	// in "c" have 新增轉換特殊會錯誤的字元
	IfxUtl.respecialstr = function(str) {
		var v = str;
		v = v.replace(/\u2015/g, "\u2500");
		// v = v.replace(/\u2014/g, "\u2500"); //這個不會
		v = v.replace(/\u2013/g, "\u2500");
		v = v.replace(/\u2012/g, "\u2500");
		v = v.replace(/\u2011/g, "\u2500");
		v = v.replace(/¯/g, "\u2500");
		return v;
	};
	IfxUtl.stringPadLeft = function(value, len) {
		if (value.length > len) return value.slice(0, len);
		var p = new Array(len - value.length + 1).join(" ");
		return p + value;
	};
	IfxUtl.stringPadRight = function(value, len) {
		if (value.length > len) return value.slice(0, len);
		var p = new Array(len - value.length + 1).join(" ");
		return value + p;
	};
	IfxUtl.numericFormatter = function(value, length) {
		// 柯:如果傳空白進來 就補滿0 出去~ for XX010
		if (value.length > 0 && this.trim(value) == "") {
			value = "";
		}
		if (typeof(value) != "String") value = new String(value);
		var l = value.length;
		if (l > length) { // too long, cut it
			value = value.substr(l - length);
		} else { // too short, fill it
			var fillChar = "0";
			for (; value.length < length;) {
				value = fillChar + value;
			}
		}
		return value;
	};
	IfxUtl.hundredFormatter = function(length) {
		var temp = 1;
		for (var i = 1; i < length; i++) {
			temp *= 10;
		}
		return temp;
	};
	// 柯: for資金
	// 公式:100-6 = 100+(6/32)
	// 公式:100-6+ = 100+(6.5/32)
	// 公式:100-6 3/4 = 100+ [6+(3/4)/32]
	IfxUtl.specialCalculate = function(v, dlen) {
		if (v.indexOf("-") != -1) {
			console.log("in IfxUtl specialCalculate!");
			var valarray = v.split("-");
			var valspecail, fannumber = 0;
			if (valarray[1].indexOf(" ") != -1) {
				var valtemp2 = valarray[1].split(" ");
				if (valtemp2.length != 2 || valtemp2[1].length < 3) {
					console.error("請檢查輸入規格是否有誤?" + valarray[1]);
					throw ifx_makeFvalError(IfxUtl.formatError("EV4A"));
				}
				valspecail = parseInt(valtemp2[0], 10);
				fannumber = parseFloat(eval(valtemp2[1]));
			} else {
				if (valarray[1].indexOf("+") != -1) {
					if (valarray[1].indexOf("+") != valarray[1].length - 1) {
						console.error("請檢查輸入規格是否有誤?" + valarray[1]);
						throw ifx_makeFvalError(IfxUtl.formatError("EV4A"));
					}
					if (valarray[1].length < 2) {
						valarray[1] = 0;
					} else {
						valarray[1] = valarray[1].slice(0, valarray[1].indexOf("+"));
					}
					valspecail = parseInt(valarray[1], 10) + 0.5;
				} else {
					valspecail = parseInt(valarray[1], 10);
					if (valspecail != valarray[1].toString()) {
						console.error("請檢查輸入規格是否有誤?" + valspecail + " != " + valarray[1].toString());
						throw ifx_makeFvalError(IfxUtl.formatError("EV4A"));
					}
				}
			}
			valspecail = valspecail + fannumber;
			if (valspecail > 32) {
				console.error("規格錯誤:0~32");
				throw ifx_makeFvalError(IfxUtl.formatError("EV4A"));
			}
			valspecail = valspecail / 32;
			var result = parseInt(valarray[0], 10) + valspecail;
			result = dlen ? result.toFixed(dlen) : parseInt(result, 10);
			console.log("before:" + v + ",after:" + result.toString());
			v = result.toString();
		}
		return v;
	};
	IfxUtl.decimalPlaces = function(float, dlen) {
		var ret = "",
			str = float.toString(),
			i = 0,
			array = str.split(".");
		if (array.length == 2) {
			ret += array[0];
			if (dlen > 0) ret += ".";
			for (i = 0; i < dlen; i++) {
				if (i >= array[1].length) ret += '0';
				else ret += array[1][i];
			}
		} else if (array.length == 1) {
			ret += array[0];
			if (dlen > 0) ret += ".";
			for (i = 0; i < dlen; i++) {
				ret += '0';
			}
		}
		return ret == "" ? "0" : ret;
	};
	IfxUtl.getToday = function() {
		var d = new Date();
		return IfxUtl.numericFormatter(d.getFullYear(), 4) + IfxUtl.numericFormatter(d.getMonth() + 1, 2) + IfxUtl.numericFormatter(d.getDate(), 2);
	};
	IfxUtl.getTimeString = function() {
		var d = new Date();
		return IfxUtl.numericFormatter(d.getHours(), 2) + IfxUtl.numericFormatter(d.getMinutes(), 2) + IfxUtl.numericFormatter(d.getSeconds(), 2); + IfxUtl.numericFormatter(d.getMilliseconds(), 2);
	};
	IfxUtl.getNowString = function() {
		var d = new Date();
		return IfxUtl.numericFormatter(d.getHours(), 2) + ':' + IfxUtl.numericFormatter(d.getMinutes(), 2) + ':' + IfxUtl.numericFormatter(d.getSeconds(), 2);
	};
	IfxUtl.myToFixed = function(value, fixedLen) {
		if (typeof(value) != "String") value = new String(value);
		var t = "";
		if (value.indexOf(".") < 0) { // no .
			t = value;
			if (fixedLen > 0) t += ".";
			for (var i = 0; i < fixedLen; i++) t += "0";
		} else {
			var arr = value.split(".");
			if (fixedLen > 0) {
				t = arr[0];
				t += "." + arr[1].substr(0, fixedLen);
			} else {
				t = arr[0];
			}
		}
		return t;
	};
	IfxUtl.moneyFormatter = function(nStr, swift) {
		var sign = swift ? ',' : '.'; // 柯:Swift的金額欄位是相反的
		var threesign = swift ? '.' : ',';
		nStr += '';
		x = nStr.split(sign);
		x1 = x[0];
		x2 = x.length > 1 ? sign + x[1] : '';
		var re = /(\d+)(\d{3})/;
		while (re.test(x1)) {
			x1 = x1.replace(re, "$1" + threesign + "$2");
		}
		return x1 + x2;
	};
	IfxUtl.addCommas = function(nStr) {
		nStr += '';
		var x = nStr.split('.');
		var x1 = x[0];
		var x2 = x.length > 1 ? '.' + x[1] : '';
		var rgx = /(\d+)(\d{3})/;
		while (rgx.test(x1)) {
			x1 = x1.replace(rgx, '$1' + ',' + '$2');
		}
		return x1 + x2;
	};
	// 帳號格式
	IfxUtl.addDash = function(nStr) {
		nStr = nStr.replace(/-/g, ""); // 0704新增這段
		var str1, str2, str3, str4, strdesh;
		nStr += '';
		str1 = nStr.slice(0, 4);
		str2 = nStr.slice(4, 7);
		str3 = nStr.slice(7, 12);
		str4 = nStr.slice(12, 13);
		strdesh = "-";
		// alert(str1 +"&"+ strdesh +"&"+ str2 +"&"+ strdesh +"&"+ str3 +"&"+ strdesh +"&"+ str4)
		return str1 + strdesh + str2 + strdesh + str3 + strdesh + str4;
	};
	// 密碼格式
	IfxUtl.repassword = function(nStr, len) {
		var pas = "*";
		for (var i = 1; i < len; i++) {
			pas = pas + "*"
		}
		return pas;
	};
	IfxUtl.stripCommas = function(numString) {
		var re = /,/g;
		return numString.replace(re, "");
	}
	IfxUtl.stripDot = function(value) {
		if (typeof(value) != "String") value = new String(value);
		if (value.indexOf(".") < 0) {
			return value;
		}
		var re = /\./g;
		return value.replace(re, "");
	}
	IfxUtl.formatNumber = function(num, decplaces) {
		// convert in case it arrives as a string value
		num = parseFloat(num);
		// make sure it passes conversion
		if (!isNaN(num)) {
			// multiply value by 10 to the decplaces power;
			// round the result to the nearest integer;
			// convert the result to a string
			var str = "" + Math.round(eval(num) * Math.pow(10, decplaces));
			// exponent means value is too big or small for this routine
			if (str.indexOf("e") != -1) {
				return "Out of Range";
			}
			// if needed for small values, pad zeros
			// to the left of the number
			while (str.length <= decplaces) {
				str = "0" + str;
			}
			// calculate decimal point position
			var decpoint = str.length - decplaces;
			// assemble final result from: (a) the string up to the position of
			// the decimal point; (b) the decimal point; and (c) the balance
			// of the string. Return finished product.
			return str.substring(0, decpoint) + "." + str.substring(decpoint, str.length);
		} else {
			return "NaN";
		}
	}
	IfxUtl.getrealText = function(val) {
		if (!val) {
			return "";
		}
		if (val.charAt(0) === '#') {
			return IfxUtl.ifx.getValue(x);
		} else {
			return val;
		}
	};
	IfxUtl.dec2Hex = function(dec) {
		dec = parseInt(dec, 10);
		if (!isNaN(dec)) {
			hexChars = "0123456789ABCDEF";
			if (dec > 255) {
				return "Out of Range";
			}
			var i = dec % 16;
			var j = (dec - i) / 16;
			result = "0x";
			result += hexChars.charAt(j) + hexChars.charAt(i);
			return result;
		} else {
			return NaN;
		}
	};
	IfxUtl.byteToHex = function(n) {
		var hexVals = "0123456789ABCDEF";
		return String(hexVals.substr((n >> 4) & 0x0F, 1)) + hexVals.substr(n & 0x0F, 1);
	};
	IfxUtl.daysBetween = function(date1, date2) {
		var DSTAdjust = 0;
		// constants used for our calculations below
		oneMinute = 1000 * 60;
		var oneDay = oneMinute * 60 * 24;
		// equalize times in case date objects have them
		date1.setHours(0);
		date1.setMinutes(0);
		date1.setSeconds(0);
		date2.setHours(0);
		date2.setMinutes(0);
		date2.setSeconds(0);
		// take care of spans across Daylight Saving Time changes
		if (date2 > date1) {
			DSTAdjust = (date2.getTimezoneOffset() - date1.getTimezoneOffset()) * oneMinute;
		} else {
			DSTAdjust = (date1.getTimezoneOffset() - date2.getTimezoneOffset()) * oneMinute;
		}
		var diff = Math.abs(date2.getTime() - date1.getTime()) - DSTAdjust;
		return Math.ceil(diff / oneDay);
	}
	IfxUtl.BasicCheckDate = function(fld) {
		var mo, day, yr;
		var entry = fld.value;
		var re = /\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/;
		if (re.test(entry)) {
			var delimChar = (entry.indexOf("/") != -1) ? "/" : "-";
			var delim1 = entry.indexOf(delimChar);
			var delim2 = entry.lastIndexOf(delimChar);
			mo = parseInt(entry.substring(0, delim1), 10);
			day = parseInt(entry.substring(delim1 + 1, delim2), 10);
			yr = parseInt(entry.substring(delim2 + 1), 10);
			var testDate = new Date(yr, mo - 1, day);
			alert(testDate)
			if (testDate.getDate() == day) {
				if (testDate.getMonth() + 1 == mo) {
					if (testDate.getFullYear() == yr) {
						return true;
					} else {
						alert("There is a problem with the year entry.");
					}
				} else {
					alert("There is a problem with the month entry.");
				}
			} else {
				alert("There is a problem with the date entry.");
			}
		} else {
			alert("Incorrect date format. Enter as mm/dd/yyyy.");
		}
		return false;
	}
	IfxUtl.EnhancedCheckDate = function(fld) {
		var mo, day, yr;
		var entry = fld.value;
		var reLong = /\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/;
		var reShort = /\b\d{1,2}[\/-]\d{1,2}[\/-]\d{2}\b/;
		var valid = (reLong.test(entry)) || (reShort.test(entry));
		if (valid) {
			var delimChar = (entry.indexOf("/") != -1) ? "/" : "-";
			var delim1 = entry.indexOf(delimChar);
			var delim2 = entry.lastIndexOf(delimChar);
			mo = parseInt(entry.substring(0, delim1), 10);
			day = parseInt(entry.substring(delim1 + 1, delim2), 10);
			yr = parseInt(entry.substring(delim2 + 1), 10);
			// handle two-digit year
			if (yr < 100) {
				var today = new Date();
				// get current century floor (e.g., 2000)
				var currCent = parseInt(today.getFullYear() / 100) * 100;
				// two digits up to this year + 15 expands to current century
				var threshold = (today.getFullYear() + 15) - currCent;
				if (yr > threshold) {
					yr += currCent - 100;
				} else {
					yr += currCent;
				}
			}
			var testDate = new Date(yr, mo - 1, day);
			if (testDate.getDate() == day) {
				if (testDate.getMonth() + 1 == mo) {
					if (testDate.getFullYear() == yr) {
						// fill field with database-friendly format
						fld.value = mo + "/" + day + "/" + yr;
						return true;
					} else {
						alert("There is a problem with the year entry.");
					}
				} else {
					alert("There is a problem with the month entry.");
				}
			} else {
				alert("There is a problem with the date entry.");
			}
		} else {
			alert("Incorrect date format. Enter as mm/dd/yyyy.");
		}
		return false;
	};
	IfxUtl.validateDate = function(value, type, len) {
		value = value.replace("/", "");
		var yyyy, yy, mm, dd;
		if (!value) return true;
		if (value.match(/^[0-9]+$/) == null) return false;
		// alert(field.type);
		if (type == "D") { // taiwan date
			// 有需要增加民國年的這段檢查??? start
			if (value.length < 6 || value.length > 7) {
				console.log("If type D, length need >6 & <7");
				if (!(value.charAt(0) == '0' && value.length > 7)) { // ifx-fld有寫如8位去掉前面"0"
					return false;
				}
			}
			// 有需要增加這段的檢查??? end
			console.log("convert ROC date:" + value);
			value = this.ROC2BC(value);
			console.log(" BC date is:" + value);
		}
		/* 潘 追加日期檢核 不得小於1900-01-01 */
		if (len == 8 && value < 19000101) {
			return false;
		}
		if (len == 8 && value.length != 8) {
			return false;
		}
		if (len == 6 && value.length != 6) {
			return false;
		}
		if (len == 6) { // sa:只有這個區間1950~2049
			yy = parseInt(value.substring(0, 2), 10);
			mm = parseInt(value.substr(2, 2), 10);
			dd = parseInt(value.substr(4, 2), 10);
			if (yy >= 50 && yy <= 99) {
				yyyy = yy + 1900;
			} else if (yy >= 00 && yy <= 49) {
				yyyy = yy + 2000;
			}
		} else {
			yyyy = parseInt(value.substring(0, 4), 10);
			mm = parseInt(value.substr(4, 2), 10);
			dd = parseInt(value.substr(6, 2), 10);
		}
		if (type == "D" && yyyy <= 1911 && yyyy != 0) return false;
		if ((yyyy == 0 || yy == 0) && mm == 0 && dd == 0) return true;
		// end
		if (isNaN(yyyy) || isNaN(mm) || isNaN(dd)) {
			console.log("date must contain digits only");
			return false;
		}
		try {
			var ds = mm + "/" + dd + "/" + yyyy;
			var testDate = new Date(ds);
			// extract pieces from date object
			testMo = testDate.getMonth() + 1;
			testDay = testDate.getDate();
			testYr = testDate.getFullYear();
			if (testMo != mm || testDay != dd || testYr != yyyy) {
				console.log("date not match");
				return false;
			}
			return true;
		} catch (oEx) {
			console.log(oEx);
			return false;
		}
	};
	IfxUtl.BC2ROC = function(field) {
		if (field.length != 8) {
			alert("BC2ROC 輸入長度錯誤，請重新輸入");
			return null;
		}
		// YYYYMMDD
		yyyy = parseInt(field.substring(0, 4), 10);
		mm = parseInt(field.substr(4, 2), 10);
		dd = parseInt(field.substr(6, 2), 10);
		yyyy -= 1911;
		if (yyyy <= 0) {
			alert("BC2ROC 年份轉換錯誤，請重新輸入");
			return null;
		}
		return this.numericFormatter(yyyy, 4) + this.numericFormatter(mm, 2) + this.numericFormatter(dd, 2);
	};
	IfxUtl.ROC2BC = function(field) {
		// YYYYMMDD
		var r = parseInt(field, 10);
		r += 19110000;
		return new String(r);
	};
	var _monthNames = {
		en: {
			month_names_long: ['', 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
			month_names: ['', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
		},
		tw: {
			month_names: ['', '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
		}
	};
	IfxUtl.getMonthName = function(m, locale) {
		if (!m) return "";
		locale = locale || "en";
		return _monthNames[locale]["month_names"][m].toUpperCase();
	};
	IfxUtl.daysBetween = function(date1, date2, bAbs) {
		// The number of milliseconds in one day
		var ONE_DAY = 1000 * 60 * 60 * 24;
		// Convert both dates to milliseconds
		var date1_ms = date1.getTime();
		var date2_ms = date2.getTime();
		// Calculate the difference in milliseconds
		var difference_ms = (date1_ms - date2_ms);
		if (bAbs) difference_ms = Math.abs(difference_ms);
		// Convert back to days and return
		return Math.round(difference_ms / ONE_DAY);
	};
	IfxUtl.dayOfYear = function(d) {
		var onejan = new Date(d.getFullYear(), 0, 1);
		return Math.ceil((d - onejan) / 86400000);
	};
	IfxUtl.n2d = function(value) {
		var yyyy = parseInt(value.substring(0, 4), 10);
		var mm = parseInt(value.substr(4, 2), 10);
		var dd = parseInt(value.substr(6, 2), 10);
		var ds = mm + "/" + dd + "/" + yyyy;
		return new Date(ds);
	};
	IfxUtl.d2n = function(d) {
		console.log(d);
		console.log(d.getMonth());
		console.log(d.getDate());
		return d.getFullYear() + IfxUtl.numericFormatter(d.getMonth() + 1, 2) + IfxUtl.numericFormatter(d.getDate(), 2);
	};
	IfxUtl.dateFromDay = function(year, day) {
		var date = new Date(year, 0); // initialize a date in `year-01-01`
		return new Date(date.setDate(day)); // add the number of days
	};
	IfxUtl.addDays = function(dt, days) {
		if (isNaN(parseInt(days, 10))) return;
		var d, s, t;
		var MinMilli = 1000 * 60;
		var HrMilli = MinMilli * 60;
		var DyMilli = HrMilli * 24;
		var n = Math.abs(days);
		var daysMilli = (DyMilli) * parseInt(n, 10);
		t = dt.getTime();
		if (days < 0) {
			t -= daysMilli;
		} else {
			t += daysMilli;
		}
		var d = new Date();
		d.setTime(t);
		return d;
	};
	IfxUtl.dateFormatter = function(v, len) {
		if (len == undefined) len = v.length;
		if (v.length < len) {
			v = IfxUtl.numericFormatter(v, len);
		}
		if (parseInt(v, 10) == 0)
			return "";
		switch (len) {
			case 0:
				return "";
				break;
			case 8:
				return v.substring(0, 4) + '/' + v.substring(4, 6) + '/' + v.substring(6);
				break;
			case 7:
				return v.substring(0, 3) + '/' + v.substring(3, 5) + '/' + v.substring(5);
				break;
			case 6:
				return v.substring(0, 2) + '/' + v.substring(2, 4) + '/' + v.substring(4);
				break;
			default:
				throw "date error:" + v;
		}
	};
	IfxUtl.dateFormatterfake = function(v, len) {
		if (len == undefined) len = v.length;
		if (v.length < len) {
			v = IfxUtl.numericFormatter(v, len);
		}
		switch (len) {
			case 0:
				return "";
				break;
			case 8:
				return "9999/99/99";
				break;
			case 7:
				return "999/99/99";
				break;
			case 6:
				return "99/99/99";
				break;
			default:
				throw "date error:" + v;
		}
	};
	IfxUtl.validateTime = function(v, pattern, man) {
		var reTimes = {
			"HHmmss": /^(0[0-9]|1[0-9]|2[0-3])[0-5][0-9][0-5][0-9]$/,
			"hhmmss": /^(0[1-9]|1[012])[0-5][0-9][0-5][0-9]$/,
			"HHmm": /^(0[0-9]|1[0-9]|2[0-3])[0-5][0-9]$/,
			"hhmm": /^(0[1-9]|1[012])[0-5][0-9]$/
		};
		pattern = pattern || "HHmmss"; // default is HHmmss
		var re = reTimes[pattern];
		if (re == null) throw "無此時間格式:[" + pattern + "]";
		if (!man) {
			if (v.length == 0 || parseInt(v, 10) === 0) return true;
		}
		return v.match(re);
	};
	IfxUtl.partial = function(fn /* ,args... */ ) {
		var aps = Array.prototype.slice,
			args = aps.call(arguments, 1);
		return function() {
			return fn.apply(this, args.concat(aps.call(arguments)));
		};
	};
	IfxUtl.partialRight = function(fn /* ,args... */ ) {
		var aps = Array.prototype.slice,
			args = aps.call(arguments, 1);
		return function() {
			return fn.apply(this, aps.call(arguments).concat(args));
		};
	};
	IfxUtl.inherit = function(p) {
		if (p == null) // p must be a non-null
			throw TypeError();
		if (Object.create) // Use Object.create()
			return Object.create(p); // if it is defined.
		var t = typeof p; // Make sure p is an object
		if (t !== "object" && t !== "function") throw TypeError();

		function f() {}; // Define a dummy constructor.
		f.prototype = p; // Set its prototype property
		return new f(); // Use it to create an "heir" of p.
	};
	// format("{0} {1}", ["hello", "world"]) <-- one array only
	// format("{0} {1}", "hello", "world") <-- arguments
	IfxUtl.format = function(src, args) {
		if (arguments.length < 1) return src;
		if (!IfxUtl.is_array(args)) {
			args = Array.prototype.slice.call(arguments, 1);
		}
		return src.replace(/\{(\d+)\}/g, function(m, i) {
			return args[i];
		});
	};
	IfxUtl.formatError = function(defaultErrorCode, smileArgs, others) {
		var errCode = smileArgs ? smileArgs.shift() : defaultErrorCode,
			fmt = getErrorText(errCode);
		others = others || {};
		smileArgs = smileArgs || [];
		for (var k in others) {
			fmt = fmt.replace("%" + k + "%", others[k]);
		}
		for (var i = 0; i < smileArgs.length; i++) {
			try {
				if (smileArgs[i][0] == '#') {
					var v = IfxUtl.ifx.getValue(smileArgs[i]);
					smileArgs[i] = v;
				}
			} catch (ignoreIt) {}
		}
		return IfxUtl.format(fmt, smileArgs);

		function getErrorText(errCode) {
			// 從檔案變數換成自身定義 start
			var ifxErrorCode2 = {
				"EA3": "帳號輸入錯誤",
				"SE_INTEGRAL_TOO_LONG": "小數點前位數過多",
				"EA0": "證號格式錯誤",
				"EA0_1": "DBU統一編號或身分證輸入錯誤(不能輸入BUA~BUZ)",
				"EA0_2": "OBU統一編號輸入錯誤(只能輸入BUA~BUZ)",
				"EA0_3": "\n請確認是否為外國人",
				"EA2": "統一編號輸入錯誤",
				"EA1": "身分證號輸入錯誤",
				"EVC": "全形、半形請擇一輸入",
				"SE_OVERFLOW": "資料溢位(fld:{0},value:{1})",
				"EVE_1": "不可輸入下列範圍內資料%codes%",
				"EVE_0": "請輸入下列範圍內資料%codes%",
				"EA4": "日期輸入錯誤",
				"SE_NUMERIC": "請輸入數值資料",
				"EV1": "請輸入此範圍內數值:%codes%",
				"EV2": "不可輸入此範圍內數值:%codes%",
				"EV3": "請輸入此範圍內資料:%codes%",
				"EV4": "不可輸入此範圍內資料:%codes%",
				"EV4A": "輸入資料限制錯誤",
				"EV5": "必須輸入下列範圍%from% ~ %to% 內數值",
				"EV6": "不可輸入下列範圍%from% ~ %to% 內數值",
				"EV7": "必須輸入",
				"EV8": "輸入資料超過限制(限%from% ~ %to%字元)", // MANTIS提議更改
				"EV8A": "輸入資料長度不足(限%len%字元)",
				"EV8B": "輸入資料超過限制(限%len%字元),刪除後字串:[%str%]",
				"EV9": "不可輸入數值型態",
				"SE_DATE": "日期輸入錯誤",
				"SE_TOO_LONG_AFTER_DECIMAL_POINT": "小數點後位數過多",
				"EI": "小數點後位數過多,(只能輸到小數後第{0}位)",
				"EVB": "不允許之特殊符號包括!@#$%&*~|<>[]" + '"' + "=^;或  TAB",
				"EVL": "限輸入英文字母",
				"EVNL": "限輸入數字或英文字母"
			}
			var t = ifxErrorCode2[errCode];
			// end
			// var t = _ifxErrorCode[errCode];
			return t || errCode;
		}
	};
	// template = <li><a href="%s">%s</a></li>
	// var result = sprintf(templateText, "/item/4", "Fourth item");
	IfxUtl.sprintf = function(text) {
		var i = 1,
			args = arguments;
		return text.replace(/%s/g, function() {
			return (i < args.length) ? args[i++] : "";
		});
	};
	// var templ =Hi {0}, go {1}!!
	// format2(templ, "John", "hell") ==> Hi John, go hell!
	IfxUtl.format2 = function x(s) {
		var args = Array.prototype.slice.call(arguments, 1);
		return s.replace(/{(\d+)}/g, function(match, number) {
			return typeof args[number] != 'undefined' ? args[number] : match;
		});
	};
	IfxUtl.getCursorPos = function(input) {
		if ("selectionStart" in input && document.activeElement == input) {
			return {
				start: input.selectionStart,
				end: input.selectionEnd
			};
		} else if (input.createTextRange) {
			var sel = document.selection.createRange();
			if (sel.parentElement() === input) {
				var rng = input.createTextRange();
				rng.moveToBookmark(sel.getBookmark());
				for (var len = 0; rng.compareEndPoints("EndToStart", rng) > 0; rng.moveEnd("character", -1)) {
					len++;
				}
				rng.setEndPoint("StartToStart", input.createTextRange());
				for (var pos = {
						start: 0,
						end: len
					}; rng.compareEndPoints("EndToStart", rng) > 0; rng.moveEnd("character", -1)) {
					pos.start++;
					pos.end++;
				}
				return pos;
			}
		}
		return -1;
	};
	IfxUtl.setCursorPos = function(input, start, end) {
		if (arguments.length < 3) end = start;
		if ("selectionStart" in input) {
			setTimeout(function() {
				input.selectionStart = start;
				input.selectionEnd = end;
			}, 1);
		} else if (input.createTextRange) {
			var rng = input.createTextRange();
			rng.moveStart("character", start);
			rng.collapse();
			rng.moveEnd("character", end - start);
			rng.select();
		}
	};
	IfxUtl.atLastLine = function(event, $target, bEnter) {
		var text = $target.val();
		console.log('textarea a:' + text);
		var firstNewline = text.indexOf('\n');
		var lastNewline = text.lastIndexOf('\n');
		var cursor = this.getCursorPos($target.get()[0]);
		var rows = parseInt($target.attr('rows'), 10);
		var cols = parseInt($target.attr('cols'), 10);
		var lines = [];
		var offset;
		$.each(text.split('\n'), function(i, x) {
			offset = 0;
			if (IfxUtl.strlen(x) > cols) {
				while (offset < IfxUtl.strlen(x)) {
					lines.push(IfxUtl.substr_big5(x, offset, cols));
					offset += cols;
				}
			} else {
				lines.push(x);
			}
		});
		if (lines.length >= rows && bEnter) {
			event.preventDefault();
		}
		// var lines = text.match(/\n/g).length + 1;
		console.log('lines:' + lines.length);
		console.log('rows:' + rows);
		console.log('cursor:{' + cursor.start + ',' + cursor.end + '}');
		console.log('firstNewline:' + firstNewline + ',lastnewline:' + lastNewline);
		if (cursor.start == cursor.end) {
			if (firstNewline == -1) return false; // no new newline, cursor down
			if (cursor.start <= lastNewline) return false; // at least one newline, before lastnewline,
			// move cursor down
			return lines.length == rows; // enough lines? no more down
		}
		return false; // don't go next field
	};
	IfxUtl.atFirstLine = function(event, $target) {
		var text = $target.val();
		console.log('textarea atFirstLine');
		var firstNewline = text.indexOf('\n');
		var cursor = this.getCursorPos($target.get()[0]);
		console.log('firstNewline:' + firstNewline);
		console.log('cursor:{' + cursor.start + ',' + cursor.end + '}');
		return (cursor.start == cursor.end && (firstNewline == -1 || cursor.start <= firstNewline));
	};
	IfxUtl.textareaFilter = function(event, $target, change) {
		var rows = parseInt($target.attr('rows'), 10);
		var cols = parseInt($target.attr('cols'), 10);
		if (isNaN(rows) && isNaN(cols)) {
			// 給 畫面輸出要edit的部分使用 好像有點leg
			return;
		}
		var maxlength = parseInt($target.attr('maxlength'), 10);
		var text = $target[0].value;
		console.log('textarea f:' + text);
		var pos = cols;
		// var changeval =false;
		var lines = [];
		var offset;
		$.each(text.split('\n'), function(i, x) {
			var k = 0;
			while (k < x.length) {
				var tmpval = x.slice(k);
				if (tmpval.length > pos && tmpval.charAt(pos - 1) != " " && tmpval.charAt(pos) != " ") {
					var j = tmpval.lastIndexOf(" ", pos - 1);
					if (j != -1) {
						var p1 = tmpval.substring(0, j);
						lines.push(p1);
						k += p1.length;
						continue;
					}
				}
				k += pos;
				lines.push(tmpval.slice(0, pos));
			}
		});
		// console.log('lines:' + lines.length);
		if (lines.length > rows) { // &&
			console.log('reach rows:' + rows);
			event.preventDefault();
			// 是否要每次都計算?
			// for(var i=0;i<lines.length -rows ;i++){
			// lines.pop();
			// }
			// $target.val(lines.join('\n'));
		}
		// change = 最後一次
		if (change) { // changeval ||
			var reallan = 0;
			if (lines.length == 0) {
				return true;
			}
			for (var i = 0; i < lines.length - rows; i++) {
				lines.pop();
			}
			var lastline = lines.length - 1;
			if (lines[lastline].length == 0) {
				lines.pop();
				lastline = lines.length - 1;
			}
			// for 連續按住時可能因沒有輸入換行而造成多輸入的問題 start
			// for(var i=0;i<lines.length ;i++){
			// reallan+=lines[i].length;
			// }
			// var add = reallan+lines.length -maxlength-1;
			// if(add > 0 ){
			// lines[lastline]= lines[lastline].slice(0,lines[lastline].length-add);
			// }
			// end
			console.log("auto Change value! (textareaFilter)");
			console.log(lines.join('\n'));
			$target.val(lines.join('\n'));
			return true;
		}
	};
	// = Array.prototype.slice.call(arguments),
	IfxUtl.tupple = function(arr) {
		var args, o = {},
			i;
		args = IfxUtl.is_array(arr) ? arr : Array.prototype.slice.call(arguments);
		for (i = 0; i < args.length; i++) {
			o['_' + (i + 1)] = this.trim(args[i]);
		}
		return o;
	};
	IfxUtl.arr2pair = function(arr) {
		var o = this.tupple(arr);
		o.fst = o._1;
		o.snd = o._2;
		return o;
	};
	IfxUtl.pair = function(s, tok, trimFst, trimSnd) {
		var o = {},
			tok = tok || '=',
			ss;
		ss = s.split(tok);
		o.fst = trimFst ? IfxUtl.trim(ss[0]) : ss[0];
		o.snd = trimSnd ? IfxUtl.trim(ss[1]) : ss[1];
		return o;
	};
	IfxUtl.pairs2obj = function(pairs, prefix) {
		prefix = prefix || '';
		var o = {},
			p;
		for (var i = 0; i < pairs.length; i++) {
			p = pairs[i];
			o[prefix + p.fst] = p.snd;
		}
		return o;
	};
	IfxUtl.line2map = function(s) {
		var m = {},
			ss = s.split(';');
		for (var i = 0; i < ss.length; i++) {
			var p = IfxUtl.pair(ss[i]);
			m[p.fst] = p.snd;
		}
		return m;
	};
	IfxUtl.str2map = function(s) {
		if (IfxUtl.str2map.re == null) IfxUtl.str2map.re = new RegExp("\\{([\\s\\S]*)\\}");
		var ss = s.match(IfxUtl.str2map.re);
		if (!ss) {
			console.log("Failed to convert s to map:" + s);
			return {};
		}
		var r = ss[1], // matched string
			rr = r.split(';'),
			arr;
		arr = IfxUtl.map(function(x) {
			return IfxUtl.pair(x, '=', true);
		}, rr);
		return IfxUtl.pairs2obj(arr);
	};
	var reVar = /\s*(#\w+)\s*/;

	function extractVar(s) {
		var m = s.match(reVar);
		if (m) return m[1];
		else return s;
	}
	IfxUtl.collectValue = function(map, fnGetByName) {
		var k = null,
			v;
		for (k in map) {
			v = map[k];
			v = extractVar(v);
			map[k] = fnGetByName(v);
		};
	};
	// a simple method to append JS files to the DOM
	IfxUtl.appendJS = function(url) {
		// 潘 更改亂數寫法
		url += "&" + (new Date()).getTime();
		var scr = document.createElement('script');
		scr.setAttribute('src', url);
		scr.setAttribute('type', 'text/javascript');
		document.getElementsByTagName('head').item(0).appendChild(scr);
	};
	IfxUtl.loadScript = function(url, callback) {
		var script = document.createElement('script');
		script.async = true;
		script.src = url;
		var entry = document.getElementsByTagName('script')[0];
		entry.parentNode.insertBefore(script, entry);
		script.onload = script.onreadystatechange = function() {
			var rdyState = script.readyState;
			if (!rdyState || /complete|loaded/.test(script.readyState)) {
				callback();
				script.onload = null;
				script.onreadystatechange = null;
			}
		};
	};
	IfxUtl.loadStylesheet = function(url) {
		var link = document.createElement('link');
		link.rel = 'stylesheet';
		link.type = 'text/css';
		link.href = url;
		var entry = document.getElementsByTagName('script')[0];
		entry.parentNode.insertBefore(link, entry);
	};
	IfxUtl.injectCss = function(css) {
		var style = document.createElement('style');
		style.type = 'text/css';
		css = css.replace(/\}/g, "}\n");
		if (style.styleSheet) {
			style.styleSheet.cssText = css;
		} else {
			style.appendChild(document.createTextNode(css));
		}
		var entry = document.getElementsByTagName('script')[0];
		entry.parentNode.insertBefore(style, entry);
	};
	// url --> http://abc.com:8088/aa/bb/cc?p=1&q=2&r=3
	// var params = getQueryParameters(url.replace(/^.*\?/, ''));
	// http://abc.com/xyz.js#product_id=1234
	// var params = getQueryParameters(url.replace(/^.*\#/, ''));
	IfxUtl.getQueryParameters = function(query) {
		var args = query.split('&');
		var params = {};
		var pair;
		var key;
		var value;

		function decode(string) {
			return decodeURIComponent(string || "").replace('+', ' ');
		}
		for (var i = 0; i < args.length; i++) {
			pair = args[i].split('=');
			key = decode(pair[0]);
			value = decode(pair[1]);
			params[key] = value;
		}
		return params;
	};
	IfxUtl.curry = function(fn) {
		var slice = Array.prototype.slice,
			stored_args = slice.call(arguments, 1);
		return function() {
			var new_args = slice.call(arguments),
				args = stored_args.concat(new_args);
			return fn.apply(null, args);
		};
	};
	IfxUtl.insertAt = function(array, index) {
		var arrayToInsert = Array.prototype.splice.apply(arguments, [2]);
		return insertArrayAt(array, index, arrayToInsert);
	};
	IfxUtl.insertArrayAt = function(array, index, arrayToInsert) {
		Array.prototype.splice.apply(array, [index, 0].concat(arrayToInsert));
		return array;
	};
	// 新增此段給
	IfxUtl.strltrimZero = function(str) {
		var array = str.split(".");
		if (array[0].length > 1) {
			return ltrimZero(str);
		}
		return str;
	};
	// ----數字轉金額大寫
	function rtrimZero(s) {
		var re = /[0]+$/g;
		return s.replace(re, "");
	}

	function ltrimZero(s) {
		var re = /^0*/g; // trim left-starting zeros 000012300--> 12300
		return s.replace(re, "") == "" ? "0" : s.replace(re, "");
	}

	function trimZero(s) {
		var re = /^[0]+|[0]+$/g;
		return s.replace(re, "");
	}
	/*
	 * function amt2Chinese(value, pointNumber) { var intUnit = "元拾佰仟萬拾佰仟億拾佰仟兆"; var pointUnit =
	 * "角分";
	 *
	 * //var arr = value.split("."); //value = arr.join(""); value = $.trim(value); var number = 0;
	 *
	 * number = parseFloat(value);
	 *
	 * if (number == 0 || value == "") { return "零元整"; } var intPart = "0"; var pointPart = "0"; if
	 * (value.indexOf(".") < 0) { if (pointNumber != null && pointNumber > 0) { intPart =
	 * value.substr(0, value.length - pointNumber); pointPart = value.substr(value.length -
	 * pointNumber); } else { intPart = value; } } else { // value contain decimal point var arr =
	 * value.split("."); intPart = arr[0]; pointPart = arr[1].substr(0, 2); // max two number after
	 * decimal point } var s1 = ""; intPart = $.trim(intPart); var commaArray = intPart.split(",");
	 * intPart = commaArray.join("");
	 *
	 * pointPart = $.trim(pointPart);
	 *  // 整數部份 intPart = ltrimZero(intPart); for (var i = 0; i < intPart.length; i++) { var t =
	 * n2Big(intPart.charAt(i)) if (t == "零") { if (s1.charAt(s1.length - 1) != "零") { s1 = s1 + t; }
	 * continue; } var unit = intUnit.charAt(intPart.length - i - 1); s1 += t + unit; }
	 *
	 * //小數部份 pointPart = rtrimZero(pointPart); var s2 = ""; if (pointPart.length != 0) { for (var i =
	 * 0; i < pointPart.length; i++) { var t = n2Big(pointPart.charAt(i)) if (t != "零") { var unit =
	 * pointUnit.charAt(i); s2 += t + unit; } } } if (s2.length == 0) s2 = "整";
	 *
	 * return s1 + s2;
	 *
	 * function n2Big(n) { var scale = "零壹貳參肆伍陸柒捌玖"; var ii = parseInt(n, 0) - 0; return
	 * scale.charAt(ii); } }
	 */
	// 取代 amt2Chinese 原本的會有少一些字
	function digit_uppercase(n, no, normal) {
		var fraction = ['角', '分'];
		var digit, unit;
		if (!normal) {
			digit = ['零', '壹', '貳', '參', '肆', '伍', '陸', '柒', '捌', '玖'];
			unit = [
				['元', '萬', '億', '兆'],
				['', '拾', '佰', '仟']
			];
		} else {
			digit = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
			unit = [
				['', '萬', '億', '兆'],
				['', '十', '百', '千']
			];
		}
		var head = n < 0 ? (!normal ? '欠' : '負') : '';
		n = Math.abs(n);
		var s = '';
		for (var i = 0; i < fraction.length; i++) {
			s += (digit[Math.floor((n * 10).toFixed(2) * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
		}
		s += (!normal) ? '整' : '';
		n = Math.floor(n);
		for (var i = 0; i < unit[0].length && n > 0; i++) {
			var p = '';
			for (var j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[n % 10] + unit[1][j] + p;
				n = Math.floor(n / 10);
			}
			s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
		}
		if (normal) {
			s = s.replace(/^一十/, '十'); // 去除最前面的"一十"
		}
		return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');
	}

	function amt2English(value, pointNumber) {
		value = value.trim();
		if (parseFloat(value, 10) == 0) {
			return "Zero";
		}
		var intPart = "0";
		var pointPart = "0";
		if (value.indexOf(".") < 0) {
			if (pointNumber != null && pointNumber > 0) {
				intPart = value.substr(0, value.length - pointNumber);
				pointPart = value.substr(value.length - pointNumber);
			} else {
				intPart = value;
			}
		} else { // value contain decimal point
			var arr = value.split(".");
			intPart = arr[0];
			pointPart = arr[1].substr(0, 2); // max two number after decimal point
		}
		intPart = intPart.trim();
		var commaArray = intPart.split(",");
		intPart = commaArray.join("");
		pointPart = pointPart.trim();
		// 整數部份
		var s1 = "";
		intPart = ltrimZero(intPart);
		if (intPart.length > 0) {
			// 切成 3位數三位數 12345678--> 12 345 678
			var threePart = new Array();
			for (var i = intPart.length;;) {
				var fromPos = (i - 3) > 0 ? (i - 3) : 0;
				threePart[threePart.length] = intPart.substr(fromPos, i - fromPos);
				if (fromPos <= 0) break;
				i = fromPos;
			}
			var unit = new Array("", "thousand", "million", "billion", "trillion");
			var ttt;
			var pLen = threePart.length - 1;
			for (var i = pLen; i >= 0; i--) {
				ttt = get123(threePart[i]);
				if (i == pLen) {
					s1 += ttt;
					if (unit[i].length > 0) s1 += " " + unit[i];
				} else {
					if (ttt.length > 0) {
						s1 += " " + ttt;
						if (unit[i].length > 0) s1 += " " + unit[i];
					}
				}
			}
			s1 += ""; // "dollars";
		}
		// 小數部份
		var s2 = "";
		pointPart = rtrimZero(pointPart);
		if (pointPart.length > 0) {
			/* 潘 20180301 cent & cents */
			if (pointPart == "01") s2 = "and cent ";
			else s2 = "and cents ";
			s2 += get123(pointPart, true); // 潘 20180109
		}
		if (s1.length > 0 && s2.length > 0) s1 += " "; // 整數與小數點之間
		s1 += s2;
		s1 = s1 + " only";
		s1 = s1.toUpperCase();
		return s1;

		function get123(v, dec) {
			var oneArray = new Array("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty");
			var tenArray = new Array("twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety");
			var s = "";
			var s2 = "";
			if (v.slice(0, 1) == "-") {
				s2 = "minus ";
				v = v.slice(1);
			}
			if (v.length == 3) {
				var tt = oneArray[parseInt(v.charAt(0), 10) - 0];
				if (tt.length > 0) s += tt + " hundred";
				v = v.substr(1);
			}
			switch (v.length) {
				case 1:
					/* 潘 20180109 修改金額轉英文邏輯 針對小數點部分 */
					if (dec && v > 1) {
						s2 += tenArray[parseInt(v, 10) - 2];
						break;
					}
					if (dec && v == 1) {
						s2 += oneArray[parseInt(v, 10) + 9];
						break;
					}
					if (!dec) {
						s2 += oneArray[parseInt(v, 10) - 0];
						break;
					}
				case 2:
					var i = parseInt(v, 10);
					if (i <= 20) {
						s2 += oneArray[i - 0];
					} else {
						s2 += tenArray[parseInt(v.charAt(0), 10) - 2]; // from 20
						if (v.charAt(1) != "0") {
							s2 += "-"
							s2 += oneArray[parseInt(v.charAt(1), 10) - 0];
						}
					}
					break;
			}
			var intAND = " "; // " and " 整數部分是否加and
			if (s2.length > 0) {
				if (s.length > 0) s += intAND + s2;
				else s = s2;
			}
			return s;
		}
	}
	// ------------------
	IfxUtl.amt2Chinese = digit_uppercase;
	IfxUtl.amt2English = amt2English;
	IfxUtl.chunkString = function(str, len) {
		var _size = Math.ceil(str.length / len),
			_ret = new Array(_size),
			_offset;
		for (var _i = 0; _i < _size; _i++) {
			_offset = _i * len;
			_ret[_i] = str.substring(_offset, _offset + len);
		}
		return _ret;
	};
	// end of Utl module
}());
// 小柯 增加檢查 START
IfxUtl.halfToFull = function(s) {
	var temp = "";
	for (var i = 0; i < s.toString().length; i++) {
		var charCode = s.toString().charCodeAt(i);
		if (charCode <= 126 && charCode >= 33) {
			charCode += 65248;
		} else if (charCode == 32) { // 半形空白轉全形
			charCode = 12288;
		}
		temp = temp + String.fromCharCode(charCode);
	}
	return temp;
};
IfxUtl.checkifChinese = function(s) {
	var temp = "";
	for (var i = 0; i < s.toString().length; i++) {
		var charCode = s.toString().charCodeAt(i);
		// console.log("charCode:"+charCode+","+s.toString().charAt(i));
		if (charCode > 255) {
			return true;
		}
	}
	return false;
};
// 送中心前 先檢查C TYPE的 中文欄位轉碼EDBIC轉換問題
IfxUtl.checkCharset = function(s, fldname) {
	// 因為實在效能不彰,故只挑FKEY 為 0 3 6 7
	// #FKEY 0:登錄,1:更正,2:放行,3:審核/在途登錄,5:更正重登,6:在途設定,7修改,8查詢,9檢視journal
	var ckfkey = IfxUtl.ifx.getValue("#FKEY");
	if ("0367".indexOf(ckfkey) == -1) {
		console.log("do not checkCharset !");
		return;
	}
	var textvalue = s.trim();
	var url = _contextPath + '/mvc/hnd/special/confirm';
	$.ajax({
		type: 'post',
		dataType: 'json',
		async: false,
		url: url,
		data: {
			_d: textvalue,
		},
		success: function(data) {
			if (!data.success) {
				var showerror = "中文轉碼錯誤 \n欄位[" + fldname + "]內,'" + data.errorstr + "' 字轉碼異常。請確認輸入內容是否有誤。\n欄位內容：" + textvalue + "\n*****無法送出交易*****";
				alert(showerror);
				// 20171228 先都關閉檢查有誤的處理 潘
				// IfxUtl.ifx.setTranEnd(true,fldname,showerror);
			} else {
				// 20171228 先都關閉檢查有誤的處理 潘
				// IfxUtl.ifx.setTranEnd(false,fldname,null);
			}
		},
		complete: function() {
			console.log("checkCharset done");
		},
		'error': function() {
			alert("send bean error,url:" + url);
		}
	});
}
IfxUtl.checkChinese = function(s, size, trim) {
	// 柯 賴:右邊去空白再檢查
	if (!trim) {
		s = s.rtrim();
	}
	console.log("checkChinese s:[" + s + "],size:[" + size + "]");
	// alert(s);
	// alert(s.toString());
	// s = s.replace(/\4/g, ''); //小柯測試for合庫r6...
	// s = s.replace(/\7/g, ''); //小柯測試for合庫r6...
	var s1 = s;
	var h = '';
	while (s1.length > 0) {
		h = toIbm(s1);
		if (IfxUtl.strlen2(h) > size) {
			s1 = s1.slice(0, -1);
		} else {
			break;
		}
	}
	return {
		status: s == s1,
		orig: s,
		cut: s1,
		host: h,
		toString: function() {
			var lines = [];
			lines.push('status=> ' + this.status);
			lines.push('orig=> ' + this.orig);
			lines.push('*cut=> ' + this.cut);
			var t = this.host.replace(/\4/g, '{');
			t = t.replace(/\7/g, '}');
			lines.push('host=> ' + t);
			lines.push('to host len=> ' + IfxUtl.strlen2(this.host));
			return lines.join("\n");
		}
	};
};

function toIbm(s) {
	var SHIFT_OUT = '\4',
		SHIFT_IN = '\7',
		bShiftOut = false,
		t = '',
		c;
	for (var i = 0; i < s.length; i++) {
		c = s.charAt(i);
		if (IfxUtl.isEnglish(c)) {
			if (bShiftOut) {
				//add(SHIFT_IN); // 潘 全形補位
				bShiftOut = false;
			}
			add(c);
		} else {
			if (!bShiftOut) {
				//add(SHIFT_OUT); // 潘 全形補位
				bShiftOut = true;
			}
			add(c);
		}
	}
	//    if (bShiftOut) add(SHIFT_IN);
	return t;

	function add(c) {
		t += c;
	}
}
IfxUtl.isEnglish = function(s) {
	var re = /[\x00-\xff]/g;
	return re.test(s); // true:ascii only
};
IfxUtl.isNumber = function(s) {
	return !isNaN(parseFloat(s)) && isFinite(s);
};
// 小柯 增加檢查 END
// Ifx custom Error
IfxUtl.stringToCenter = function(s, swidth, padding) {
	padding = padding || " ";
	padding = padding.substr(0, 1);
	var truelen = s.replace(/[^\x00-\xff]/g, "**").length; // 計算真實長度
	if (truelen < swidth) {
		var len = swidth - truelen;
		var remain = (len % 2 == 0) ? "" : padding;
		var pads = new Array(parseInt(len / 2) + 1).join(padding); // 因是array join故+1
		return pads + s + pads + remain;
	} else return s;
};
// 柯 字串位移至欄位右邊
IfxUtl.stringToRight = function(s, swidth, padding) {
	padding = padding || " ";
	padding = padding.substr(0, 1);
	var truelen = s.replace(/[^\x00-\xff]/g, "**").length; // 計算真實長度
	if (truelen < swidth) {
		var pads = new Array(parseInt(swidth - truelen) + 1).join(padding); // 因是array join故+1
		return pads + s;
	} else return s;
};
// 柯 字串位移至欄位左邊
IfxUtl.stringToLeft = function(s, swidth, padding) {
	padding = padding || " ";
	padding = padding.substr(0, 1);
	var truelen = s.replace(/[^\x00-\xff]/g, "**").length; // 計算真實長度
	if (truelen < swidth) {
		var pads = new Array(parseInt(swidth - truelen) + 1).join(padding); // 因是array join故+1
		return s + pads;
	} else return s;
};
// Ifx custom Error
IfxUtl.sortObjects = function(objArray, properties /* , primers */ ) {
	var primers = arguments[2] || {};
	var arr1 = [],
		arr2 = [];
	var aValue = "",
		bValue = "";
	properties = properties.split(/\s*,\s*/).map(function(prop) {
		prop = prop.match(/^([^\s]+)(\s*desc)?/i);
		// document.body.innerHTML += "<p> prop:" +prop[2] + "</p>" ;
		if (prop[2] && prop[2].toLowerCase() === ' desc') {
			// document.body.innerHTML += "descdescdesc" ;
			return [prop[1], -1];
		} else {
			return [prop[1], 1];
		}
	});

	function valueCmp(x, y) {
		if (x == y) {
			return 0;
		}
		if (x > y) {
			return 1;
		} else {
			return -1;
		}
		// return x > y ? 1 : x < y ? -1 : 0;
	}

	function arrayCmp(a, b) {
		arr1 = [];
		arr2 = [];
		properties.forEach(function(prop) {
			aValue = a[prop[0]];
			bValue = b[prop[0]];
			if (typeof primers[prop[0]] != 'undefined') {
				aValue = primers[prop[0]](aValue);
				bValue = primers[prop[0]](bValue);
			}
			arr1.push(prop[1] * valueCmp(aValue, bValue));
			arr2.push(prop[1] * valueCmp(bValue, aValue));
		});
		return arr1 < arr2 ? -1 : 1;
	}
	objArray.sort(function(a, b) {
		return arrayCmp(a.occursFields, b.occursFields);
	});
};
IfxUtl.LightField = function(fieldid, fldstatus, timeout, blink) {
	var cssname = "";
	switch (fldstatus.toLowerCase()) {
		case "red": // 背景紅色
			cssname = "fldred";
			break;
		case "green": // 背景綠色
			cssname = "fldgreen";
			break;
		case "yellow": // 背景黃色
			cssname = "fldyellow";
			break;
		case "txtred": // 文字紅色
			cssname = "fldtxtred";
			break;
		case "txtgreen": // 文字綠色
			cssname = "fldtxtgreen";
			break;
		case "txtwhite": // 文字白色
			cssname = "fldtxtwhite";
			break;
		case "txtblue": // 文字藍色
			cssname = "fldtxtblue";
			break;
		case "bold": // 粗體
			cssname = "fldbold";
			break;
		case "italic": // 斜體
			cssname = "flditalic";
			break;
		default:
			return;
	}
	if (fieldid.slice(0, 1) == "#") {
		fieldid = fieldid.slice(1);
	}
	if (timeout == -1) $("#fld_" + fieldid).removeClass(cssname);
	else $("#fld_" + fieldid).addClass(cssname);
	if (blink == '1') $("#fld_" + fieldid).addClass('blink');
	if (blink == '0') $("#fld_" + fieldid).removeClass('blink');
	if (timeout != 0 && timeout != -1) {
		setTimeout(function() {
			$("#fld_" + fieldid).removeClass(cssname);
		}, timeout * 1000);
	}
};
// end
// Ifx custom Error
// TODO: refactor
function IfxError(type, message) {
	this.type = type;
	this.message = message;
	this.isSkip = function() {
		return this.type === ifxErrorType.SKIPFIELD;
	};
	this.isFavlError = function() {
		return this.type === ifxErrorType.FVAL_ERROR;
	};
	this.isRimError = function() {
		return this.type === ifxErrorType.RIM_ERROR;
	};
	this.isTransmit = function() {
		return this.type === ifxErrorType.TRANSMIT;
	};
	this.isWaitHere = function() {
		return this.type === ifxErrorType.WAIT_HERE;
	};
}
var ifxErrorType = {
	SKIPFIELD: 1,
	FVAL_ERROR: 2,
	RIM_ERROR: 3,
	TRANSMIT: 9,
	WAIT_HERE: 99
};

function ifx_makeError(type, message) {
	console.log("makeError for type:" + type + ", message:" + message);
	return new IfxError(type, message);
};

function ifx_makeSkip() {
	console.log("make a skip");
	return new IfxError(ifxErrorType.SKIPFIELD, "skip to next field");
};

function ifx_makeFvalError(m) {
	console.log("throw validation error:" + m);
	return new IfxError(ifxErrorType.FVAL_ERROR, m);
};

function ifx_makeRimError() {
	alert("thro rim error");
	return new IfxError(ifxErrorType.RIM_ERROR, "rim failed");
};

function ifx_makeTransmit() {
	console.log("make a transmit");
	return new IfxError(ifxErrorType.TRANSMIT, "transmit now!!");
};

function ifx_makeWaitHere() {
	console.log("make a wait");
	return new IfxError(ifxErrorType.WAIT_HERE, "wait here");
};

function Dictionary(startValues) {
	this.values = startValues || {};
}
Dictionary.prototype.store = function(name, value) {
	this.values[name] = value;
};
Dictionary.prototype.lookup = function(name) {
	return this.values[name];
};
Dictionary.prototype.contains = function(name) {
	return Object.prototype.propertyIsEnumerable.call(this.values, name);
};
Dictionary.prototype.each = function(action) {
	forEachIn(this.values, action);
};

function blockIt(title, message) {
	$.blockUI({
		css: {
			border: 'none',
			padding: '8px',
			backgroundColor: '#fff',
			'-webkit-border-radius': '5px',
			'-moz-border-radius': '5px',
			opacity: 1,
			color: '#fff'
		},
		theme: false,
		title: title,
		message: message
	});
	setTimeout($.unblockUI, 100 * 1000);
}

function blockAWhile(message, timeout, css) {
	css = css || {
		top: '10px',
		left: '30px',
		right: '',
		width: '200px'
	};
	$.blockUI({
		message: message,
		centerY: 0,
		css: css,
		timeout: timeout
	});
}

function blockCenter(message, timeout) {
	$.blockUI({
		message: message,
		overlayCSS: {
			backgroundColor: '#00f'
		},
		css: {
			padding: '15px'
		},
		timeout: timeout
	});
}

function unBlock() {
	$.unblockUI();
}

function myDialog(title, content, btns, fnClosed, opts) {
	opts = opts || {};
	$("<div style='z-index:9999'>" + content + "</div>").dialog({
		autoOpen: true,
		title: title,
		modal: true,
		buttons: btns,
		show: "explode",
		position: [50, 50],
		overlay: true,
		resize: false,
		closeOnEscape: true,
		open: function(event, ui) {
			// default first button
			$(this).parents('.ui-dialog-buttonpane button:eq(0)').focus();
			// Hide close button
			// $(this).parent().children().children(
			// ".ui-dialog-titlebar-close").hide();
		},
		close: function() {
			if (fnClosed) fnClosed();
		}
	}).dialog(opts);
	// dialog({ closeOnEscape: false });
}

function moveCursorToEnd(el) {
	if (typeof el.selectionStart == "number") {
		el.selectionStart = el.selectionEnd = el.value.length;
	} else if (typeof el.createTextRange != "undefined") {
		el.focus();
		var range = el.createTextRange();
		range.collapse(false);
		range.select();
	}
}

function moveCaretToStart(el) {
	if (typeof el.selectionStart == "number") {
		el.selectionStart = el.selectionEnd = 0;
	} else if (typeof el.createTextRange != "undefined") {
		el.focus();
		var range = el.createTextRange();
		range.collapse(true);
		range.select();
	}
}
// 供XE973交易,輸出XML時使用 (美化XML)
IfxUtl.formatXml = function(xml) {
	var formatted = '';
	var reg = /(>)(<)(\/*)/g;
	xml = xml.replace(reg, '$1\r\n$2$3');
	var pad = 0;
	jQuery.each(xml.split('\r\n'), function(index, node) {
		var indent = 0;
		if (node.match(/.+<\/\w[^>]*>$/)) {
			indent = 0;
		} else if (node.match(/^<\/\w/)) {
			if (pad != 0) {
				pad -= 1;
			}
		} else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
			indent = 1;
		} else {
			indent = 0;
		}
		var padding = '';
		for (var i = 0; i < pad; i++) {
			padding += '  ';
		}
		formatted += padding + node + '\r\n';
		pad += indent;
	});
	return formatted;
};
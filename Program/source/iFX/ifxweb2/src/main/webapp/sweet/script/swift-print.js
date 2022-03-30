var Swift = Swift || {};
var CKNEWLINE = "\r\n";
(Swift.print = function() {
	function isFromMessage(msg) {
		var re_b2_from = /\{2:(O.+?)}/;
		return msg.match(re_b2_from);
	}
	//來電電文 {2:O.......
	function getSenderAddress(t) {
		return t.slice(14, 26);
	}

	function searchMessage(swiftMessage, name) {
		switch (name.toLowerCase()) {
			case 'sendingtid':
				return findSendingTid(swiftMessage);
			case 'dest':
				return findDest(swiftMessage);
		}

		function findSendingTid(s) {
			//{1:    F    01   BANKBEBB   2222   123456}
			var X = "{1:",
				i, result = '';
			i = s.indexOf(X);
			if (i != -1) {
				result = s.slice(i + X.length + 3);
				result = result.slice(0, 12);
			}
			return result;
		}

		function findDest(s) {
			//{2:    I     100    BANKDEFFXXXX    U       3       003}
			var X = "{2:",
				i, result = '';
			i = s.indexOf(X);
			if (i != -1) {
				result = s.slice(i + X.length + 4);
				if (result[0] == '+') // for mt103+ ??
					result = result.slice(1);
				result = result.slice(0, 12);
			}
			return result;
		}
	}
	var _parse_result = null;

	function get_parse_result() {
		return _parse_result;
	}
	//柯 for 類似XR929交易 重複執行 parse_fromSwift 時 先清空欄位
	function set_parse_result_null() {
		_parse_result = null;
	}
	//柯:新增此段判斷是否為特殊解析電文 例:012 019等~~
	function is_special_type(msgType) {
		if (msgType.slice(0, 1) == "0") {
			console.log("is special mtType!");
			return true;
		}
		return false;
	}

	function parse_FromSwiftMessage(msg, msgType) {
		var reBlk1 = /\{1:(.+?)}/g;
		var reAll = /\{1:(.+?)/g;
		//var reBlk2 = /\{2:(.+?)}/;
		var reBlk4 = /\{4:(.+?)-}/g;
		//var reBlk451 = /\{451:(.+?)}}/;
		//var reBlk5 = /(\{5:.+?})/;
		//var reS = /(\{S:.+?})/;
		//var reTRN = /(\{TRN:.+?}})/;
		//柯: 原先STOP '\n-}{5:'  更改成'-}{5:'  以便給  mergeMtWithTota  line.pop  (來電去電對齊) 
		var START = '}{4:'; //柯:移除這個試試看  +CKNEWLINE
		var STOP = '-}';
		var swiftText, i, j;
		//特殊電文需求
		if (is_special_type(msgType)) {
			START = '}{4:';
			STOP = '}{5:'; //沒有結束符號?
		}
		i = msg.indexOf(START);
		j = msg.indexOf(STOP);
		swiftText = msg.slice(i + START.length, j);
		console.log("msg index START :" + i);
		console.log("msg index STOP :" + j);
		console.log("msg :" + msg);
		var reMap = {
			"b2": /(\{2:.+?})/,
			"b3": /(\{3:.+?})/,
			"b451": /(\{451:(.+?)}})/,
			"b5": /(\{5:.+?}})/,
			"s": /(\{S:.+?}})/,
			"trn": /(\{TRN:.+?}})/
		};
		var m = {},
			match;
		for (var k in reMap) {
			match = msg.match(reMap[k]);
			if (match != null) {
				m[k] = match[1];
			} else {
				m[k] = null;
			}
		}
		m['b1'] = msg.match(reBlk1);
		m['b4'] = swiftText;
		m['all'] = msg.slice(msg.indexOf("{1:"));
		console.log("swiftText :" + swiftText);
		//m['b4'][1] = m['b4'][1];  //看似不需要?
		_parse_result = m;
		return m;
	}

	function get_block(blkId, index, etc) {
		var r = "";
		try {
			if (isNaN(index)) {
				r = _parse_result[blkId];
			} else {
				r = _parse_result[blkId][index];
			}
		} catch (ee) {
			console.error("get_block:" + blkId + ",index:" + index);
		}
		return r;
	}
	//第六個參數是特殊供出口輸出xml的 swift
	function mergeMtWithTota(type, blks, mt, tota, prefix, isxml) {
		var r = "hello";
		var currTagIndex = 0;
		var ss;
		var SPACES5 = '     ';
		var lines;
		var oMatchedTag = null;
		var endtext = "-}";
		var lastline = null;
		var tempstr = "";
		var tagtmp = "";
		//特殊電文需求
		if (is_special_type(mt.$.type)) {
			lines = tota.replace(/{/g, ":").replace(/}/g, '\n').split('\n');
		} else {
			lines = toNewLine(tota).split('\n');
		}
		prefix = (prefix === undefined) ? '' : prefix;
		lines.shift(); // we don't need first line
		lastline = lines[lines.length - 1];
		//MQ應該是夠,但如果折返就..應該傳入時就要完整
		if (lastline.length == 0 || lastline.slice(0, endtext.length) == endtext) { //柯,新增檢查最後是否空行?
			lines.pop(); // last line, either
		}
		console.log("mergeMtWithTota");
		console.dir(mt.tags);
		//TODO 這邊是....要印東西    名地資料
		var temptag, checkbicstr = "";
		//柯:遇到這種TAG.以後的全部都是該TAG的值 (MTn98)
		var septags = "77E",
			septagsCk = false;
		var tagcode = "",
			oldtagdata = "";
		var currline = 0;
		//isxml = false;
		var arr = $.map(lines, function(x) {
			console.log("lines:" + x);
			//柯:新增雙重檢查 3,4位須為冒號和1,2位須為數字
			if (x[0] == ':' && (x[3] == ':' || x[4] == ':') && IfxUtl.isNumber(x[1]) && IfxUtl.isNumber(x[2]) && !septagsCk) {
				tempstr = "";
				ss = x.split(':');
				ss.shift();
				tagtmp = ss[0];
				//獨立出來 for septags 檢查是否為特殊欄位
				if (tagtmp == septags) {
					septagsCk = true;
				}
				temptag = getTagName(tagtmp);
				tagtmp = prefix + temptag[0];
				ss[1] = ss.slice(1).join(":");
				checkbicstr = ss[1];
				//柯:新增列印時依照回傳temptag第二位,並找到金額字串替換money格式 start
				if (temptag[1] && !isxml) { //Money
					var currlist = Swift.cur.getCurlist(),
						ss1temp = ss[1],
						curindex = -1;
					var nocurvalue = true;
					for (var i = 0; i < currlist.length; i++) {
						curindex = ss[1].indexOf(currlist[i]);
						if (curindex != -1) {
							ss[1] = ss1temp.slice(0, curindex + 3) + IfxUtl.moneyFormatter(ss1temp.slice(curindex + 3), true);
							nocurvalue = false;
							break;
						}
					}
					//如tag 19都沒有幣別欄位
					if (nocurvalue) {
						ss[1] = IfxUtl.moneyFormatter(ss[1], true);
					}
				}
				//end
				if (isxml) {
					if (oldtagdata != "") { //END IF XML
						tempstr += oldtagdata;
					}
					if (tagcode != "") { //END IF XML
						tempstr += "&lt;/TAG" + tagcode + "&gt;\n";
					}
					tempstr += writeSwiftLine(isxml, true, ss[0]); //tagcode or  ss[1]
					if (currline == lines.length - 1) {
						tempstr = tempstr.slice(0, -1);
						tempstr += ss[1];
						tempstr += "&lt;/TAG" + ss[0] + "&gt;\n";
					} else {
						tagcode = ss[0]; //保留最原始
						oldtagdata = ss[1]; //保留最原始
					}
				} else {
					tempstr = tagtmp + "" + prefix + SPACES5 + printFormat(ss[1], prefix + SPACES5);
				}
			} else {
				tempstr = writeSwiftLine(isxml, false, x, oldtagdata);
				oldtagdata = "";
				// tempstr =  prefix + SPACES5 + x;
				checkbicstr = x;
				//如果LINE是最後一行的時候，補/TAG
				if (currline == lines.length - 1 && isxml) {
					tempstr += "\n&lt;/TAG" + tagcode + "&gt;";
				}
			}
			//該tag是否需要列印bic
			if (temptag && temptag[2] && !isxml) {
				//將資料加回快取
				Swift.generic.generic.GETSWIFTBIC(mt.$.type, checkbicstr);
				var bicresult = Swift.generic.generic.getBicFromCache(checkbicstr);
				//是否有值
				if (bicresult) {
					temptag[2] = false;
					if (bicresult.slice(0, 35).trim() != "") {
						tempstr += '\n';
						tempstr += (prefix + SPACES5 + '**' + bicresult.slice(0, 35));
					}
					if (bicresult.slice(35, 70).trim() != "") {
						tempstr += '\n';
						tempstr += (prefix + SPACES5 + '**' + bicresult.slice(35, 70));
					}
					if (bicresult.slice(70, 105).trim() != "") {
						tempstr += '\n';
						tempstr += (prefix + SPACES5 + '**' + bicresult.slice(70, 105));
					}
					if (bicresult.slice(105, 140).trim() != "") {
						tempstr += '\n';
						tempstr += (prefix + SPACES5 + '**' + bicresult.slice(105, 140));
					}
				}
			}
			currline++;
			return tempstr;
		});
		if (type == "to swift") {
			return arr.join('\n');
		}
		//enrichFromDoc(arr,blks);   //柯:趙大哥提出只要顯示中間的就好
		var result = arr.join('\n');
		//依照ISXML調整此段
		if (isxml) {
			result = result.replace(/&gt;@\n/g, "&gt;");
			result = result.replace(/&gt;&lt;LINE&gt;/g, "&gt;\n&lt;LINE&gt;");
		}
		return result; //柯: 改成這樣  顯示好像比較正確
		/* 替換此段

		 function enrichFromDoc(lines, m){
		     lines.unshift(prefix+'{4:');
		*/
		//沒有用到
		function enrichFromDoc(lines, m) {
			lines.unshift(prefix + '{4:');
			lines.push(prefix + '-}');
			lines.push(prefix + m['b5'] + '  Checksum Trailer');
			lines.push(prefix + m['s']);
			lines.push(prefix + m['trn']);
			return lines;
		}

		function printFormat(s, leadings) {
			if (type == "from swift") {
				try {
					var n = oMatchedTag['$']['format-from'] || "_";
					var fn = getFormatter(n);
					return fn(s, leadings);
				} catch (ee) {
					console.error("Maybe not in oMatchedTag!" + ee);
					return s;
				}
			} else {
				return s;
			}
		}

		function getFormatter(n, leadings) {
			var formatRtns = {
				"_": function(x) {
					return x;
				},
				"valueDate": formatValueDate,
				"statementLine": formatStatementline
			};
			return formatRtns[n];
		}
		//TODO 日期有需求時再優化 (TotaToPrn.java)
		function formatValueDate(x, leadings) {
			var arr = [];
			var cdkey = {
				"C": "Credit",
				"D": "Debit"
			};
			if (x[0] == "C" || x[0] == "D") {
				appendLine("Debit/Credit           :  " + cdkey[x[0]]);
				x = x.slice(1);
			}
			var date = x.slice(0, 6);
			appendLine("Date                   :  " + date);
			x = x.slice(6);
			var cur = x.slice(0, 3);
			appendLine("Currency               :  " + cur + "   (" + Swift.cur.getName(cur) + ")");
			x = x.slice(3);
			appendLine("Amount                 :  " + "#" + IfxUtl.moneyFormatter(x, true) + "#");
			return arr.join("\n");

			function appendLine(s) {
				if (arr.length > 0) s = leadings + s;
				arr.push(s);
			}
		}

		function formatStatementline(x, leadings) {
			var arr = [];
			var laststr = x;
			var vdate = laststr.slice(0, 6);
			var entry = "";
			var mark = "";
			var mount = "";
			var funds = "";
			var ident = "";
			var customerReference = "";
			var bankReference = "";
			laststr = laststr.slice(6);
			if ('0123456789'.indexOf(laststr.charAt(0)) !== -1) {
				// Is a number
				entry = laststr.slice(0, 4); //[4!n] Entry Date (MMDD)
				laststr = laststr.slice(4);
			}
			var ckmarkone = laststr.slice(0, 1).toUpperCase();
			var ckmarktwo = laststr.slice(0, 2).toUpperCase();
			if (ckmarktwo == "RC" || ckmarktwo == "RD") {
				mark = ckmarktwo;
				laststr = laststr.slice(2);
			} else if (ckmarkone == "C" || ckmarkone == "D") {
				mark = ckmarkone;
				laststr = laststr.slice(1);
			}
			//[1!a] Funds Code (3rd character of the currency code, if needed)
			if (!('0123456789'.indexOf(laststr.charAt(0)) !== -1)) {
				funds = laststr.slice(0, 1);
				laststr = laststr.slice(1);
			}
			var tmpmoney = untilEnglish(laststr);
			//15d Amount
			mount = tmpmoney[0];
			//1!a3!c Transaction Type Identification Code
			ident = tmpmoney[1].slice(0, 4);
			//Reference
			laststr = tmpmoney[1].slice(4);
			var tmpline = laststr.split("\n");
			var tmptext = tmpline[0].split("//");
			//16x Customer Reference
			customerReference = tmptext[0];
			//[//16x] Bank Reference
			if (tmptext.length > 1) {
				//FIRST LINE
				bankReference = tmptext[1];
			}
			appendLine(IfxUtl.stringPadRight(vdate, 6) + "   " + IfxUtl.stringPadRight(entry, 4) + "   " + IfxUtl.stringPadRight(mark, 2) + "   " + IfxUtl.stringPadRight(ident, 4) + "   " + "#" + IfxUtl.moneyFormatter(mount, true) + "#");
			//appendLine("Value :  " + IfxUtl.stringPadRight(vdate,6) + "    Entry :  " + IfxUtl.stringPadRight(entry,4) );
			//appendLine("Debit/Credit :  " + IfxUtl.stringPadRight(mark,2) + "    Code :  " + IfxUtl.stringPadRight(ident,4) + 
			//		"    Amount :  #" + IfxUtl.moneyFormatter(mount,true) + "#");
			appendLine("Customer Reference : " + customerReference);
			appendLine("Bank Reference     : " + bankReference);
			//all string 
			//no work,because by each line!!
			if (tmpline.length > 1) {
				appendLine(tmpline[1]);
			}
			return arr.join("\n");

			function appendLine(s) {
				if (arr.length > 0) s = leadings + s;
				arr.push(s);
			}

			function untilEnglish(s) {
				var rtnstr = "";
				var indexi = 0;
				var c;
				for (indexi = 0; indexi < s.length; indexi++) {
					c = s.charAt(indexi);
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						break;
					} else {
						rtnstr += c;
						continue;
					}
				}
				return [rtnstr, s.slice(indexi)];
			}
		}

		function getTagName(tag) {
			var name = "name " + tag;
			var oTag;
			var checkmoney = false;
			var checkbic = false;
			var loopreturn;
			oMatchedTag = null;
			for (var i = currTagIndex; i < mt.tags.length; i++) {
				oTag = mt.tags[i];
				console.log("getTagName oTag:" + oTag + ",tag:" + tag);
				console.log(oTag.$.type + "xxxx");
				if (oTag.$.type == "loop") {
					loopreturn = checkLoopTag(oTag, tag);
					if (loopreturn != null) {
						return loopreturn;
					}
				} else {
					name = checkSimpleTag(oTag, tag);
					if (name != null) {
						checkmoney = checkMoneyTag(oTag, tag); //新增 for 金額列印 add commas
						checkbic = checkBicTag(oTag, tag);
						currTagIndex++;
						break;
					}
				}
			}
			if (name == null) name = '';
			return [':' + tag + ":" + ((tag.length == 2) ? ' ' : '') + name + "\n", checkmoney, checkbic];
		}
		//新增 for 金額列印 add commas
		function checkMoneyTag(oTag, tag) {
			console.log("checkMoneyTag");
			if (oTag.$.options) {
				return checkOptionTagMoney(oTag, tag);
			} else if (oTag.$.bag == "money" || oTag.$.bag == "amt") {
				return true;
			}
			return false;
		}
		//新增 for 取得是否檢查名地
		function checkBicTag(oTag, tag) {
			console.log("checkBicTag");
			if (oTag.$.options) {
				return checkOptionTagBic(oTag, tag);
			} else if (oTag.$.validate == "xxxSwiftBic") {
				return true;
			}
			return false;
		}

		function checkSimpleTag(oTag, tag) {
			console.log("checkSimpleTag");
			console.log(oTag.$.options);
			//console.log(oTag.$.tag.toString() + "_" + tag);
			if (oTag.$.options) {
				return checkOptionTag(oTag, tag);
			} else if (oTag.$.tag == tag) {
				oMatchedTag = oTag;
				return oTag.$.name;
			}
			return null;
		}

		function checkOptionTag(oOptionTag, tag) {
			for (var k in oOptionTag.opt) {
				if (tag == k) {
					oMatchedTag = oOptionTag.opt[tag];
					return oOptionTag.$.name;
				}
			}
			return null;
		}
		//新增 for 金額列印 add commas
		function checkOptionTagMoney(oOptionTag, tag) {
			for (var k in oOptionTag.opt) {
				if (tag == k && (oOptionTag.opt[k].$.bag == "money" || oOptionTag.opt[k].$.bag == "amt")) {
					return true;
				}
			}
			return false;
		}
		//新增 for 取得是否檢查名地
		function checkOptionTagBic(oOptionTag, tag) {
			for (var k in oOptionTag.opt) {
				if (tag == k) {
					var optag = oOptionTag.opt[k].tags;
					for (var j in optag) {
						if (optag[j].$.validate == "xxxSwiftBic") {
							return true;
						}
					}
				}
			}
			return false;
		}

		function checkLoopTag(oLoopTag, tag) {
			var oTag, name, checkmoney, checkbic;
			for (var i = 0; i < oLoopTag.tags.length; i++) {
				oTag = oLoopTag.tags[i];
				name = checkSimpleTag(oTag, tag);
				if (name != null) {
					//20170217 add
					checkmoney = checkMoneyTag(oTag, tag);
					checkbic = checkBicTag(oTag, tag);
					return [':' + tag + ":" + ((tag.length == 2) ? ' ' : '') + name + "\n", checkmoney, checkbic];
				}
			}
			return null;
		}

		function toNewLine(s) {
			var r, re; // Declare variables.
			re = /\x0d\x0a/g; // Create regular expression pattern.
			r = s.replace(re, "\n"); // Replace "\r\r\n" with "\n".
			return (r); // Return string with replacement made.
		}

		/*潘 swift轉XML*/
		function writeSwiftLine(isxml, ishead, str, oldtagdata) {
			var result = "";
			if (isxml) {
				if (ishead) {
					result = "&lt;TAG" + str + "&gt;@";
				} else {
					//先補上上一筆紀錄的內容
					if (oldtagdata) {
						result = "&lt;LINE&gt;" + oldtagdata.replace(/</,"&lt").replace(/>/,"&gt") + "&lt;/LINE&gt;\n";
					}
					result += "&lt;LINE&gt;" + str.replace(/</,"&lt").replace(/>/,"&gt") + "&lt;/LINE&gt;";
				}
			} else {
				if (ishead) {
					result = tagtmp + "" + prefix + SPACES5 + printFormat(str, prefix + SPACES5);
				} else {
					result = prefix + SPACES5 + str;
				}
			}
			return result; // Return string with replacement made.
		}
	}
	return {
		"isFromMessage": isFromMessage,
		"merge": mergeMtWithTota,
		"parse_fromSwift": parse_FromSwiftMessage,
		"getSenderAddress": getSenderAddress,
		"searchMessage": searchMessage,
		"get_parse_result": get_parse_result,
		"set_parse_result_null": set_parse_result_null,
		"is_special_type": is_special_type,
		"get_block": get_block
	}
}());
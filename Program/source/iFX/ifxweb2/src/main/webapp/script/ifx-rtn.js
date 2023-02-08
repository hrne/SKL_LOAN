var IfxRtn = (function () {
	// Module begin
	function Rtn(theIfx) {
		ifx = theIfx; // ifx core2
	}

	function TRACE(s) {
		console.log(s);
	}

	function LOG(s) {
		console.log(s);
	}
	var aps = Array.prototype.slice;
	var ifx = null;
	// 柯 測試 循環檢查 start
	var testtmpke;
	var lastData = null;
	// 柯 磁條讀取 參數 初始值
	var pppsbk = null;
	// TODO 此部分邏輯更改多次,有空時可調整寫法
	Rtn.prototype.A = function (source, opt, flag, nnn, flag1) {
		console.log("A() for " + source + " opt:" + opt + ", flag:" + flag);
		var retval = false,
			errmsg, value = ifx.getValue(nnn);
		var mytype = ifx.getField(nnn).type;
		var mylen = ifx.getField(nnn).len;
		switch (opt) {
			case "0": // id or unino
			case "ID_UNINO":
				ifx.setValue("STAT$", "0");
				// 鍾 A function(0) X(10)輸入統編8位會不過
				value = value.trim();
				// end
				if (value.length == 0) return;
				retval = studIdNumberIdentify(0, value);
				if (retval)
					if (value.substring(1, 2) == "1" || value.substring(1, 2) == "2") ifx.setValue("STAT$", "1");
					else ifx.setValue("STAT$", "3");
				if (!retval) retval = studIdNumberIdentify(1, value);
				if (retval && ifx.getValue("STAT$") == "0") ifx.setValue("STAT$", "3");
				if (!retval) retval = checkUniNo(value, true);
				if (retval && ifx.getValue("STAT$") == "0") ifx.setValue("STAT$", "2");
				if (!retval) retval = checkTaxId(value);
				if (retval && ifx.getValue("STAT$") == "0") ifx.setValue("STAT$", "4");
				if (!retval) {
					if (flag == 1) {
						// 合:XC500 與A(6) 錯誤顯示相同
						if (confirm("證號格式錯誤, 繼續請按[確定]?")) {
							retval = true;
						}
					}
				}
				if (!retval) {
					errmsg = formatError("EA0");
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "1": // id
			case "ID":
				ifx.setValue("STAT$", "0");
				if (value.length == 0) return;
				ifx.setValue("STAT$", "1");
				if (value.length != 10) {
					errmsg = formatError("EA1");
					throw ifx_makeFvalError(errmsg);
				}
				retval = checkID(value);
				if (retval) {
					ifx.setValue("STAT$", "0");
				} else {
					if (flag == 1) {
						if (confirm("身份證錯誤,繼續請按[確定]")) {
							retval = true;
						}
					}
					if (!retval) {
						errmsg = formatError("EA1");
						throw ifx_makeFvalError(errmsg);
					}
				}
				break;
			case "2": // unino
			case "UNINO":
				ifx.setValue("STAT$", "0");
				if (value.length == 0) return;
				ifx.setValue("STAT$", "1");
				retval = checkUniNo(value);
				if (retval) {
					ifx.setValue("STAT$", "0");
				} else {
					if (flag == 1) {
						if (confirm("統一編號錯誤,繼續請按[確定]")) {
							retval = true;
						}
					}
					if (!retval) {
						errmsg = formatError("EA2");
						throw ifx_makeFvalError(errmsg);
					}
				}
				break;
			case "3": // account
			case "ACNO":
				if (value.length == 0) return;
				if (value.length != 13) {
					throw ifx_makeFvalError("帳號長度錯誤");
				}
				retval = calcAcnoChkDG(value);
				if (flag == 2) {
					ifx.setValue("CHKDG$", retval);
				} else {
					var tmp = (value.slice(-1) == retval) ? true : false;
					if (tmp) { } else {
						if (flag == 1) {
							if (confirm("帳號錯誤,繼續請按[確定]")) {
								tmp = true;
							}
						}
						if (!tmp) {
							throw ifx_makeFvalError(formatError("帳號檢查碼錯誤，檢查碼須為:" + retval));
						}
					}
				}
				break;
			case "4": // 日期檢查
			case "DATE":
				retval = IfxUtl.validateDate(ifx.getValue(nnn), mytype, mylen); // 有要加mytype??
				// 還是說給ifx-fld.js做?
				if (retval == false) {
					errmsg = formatError("EA4");
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "5":
			case "TIME": // time
				var man = flag1 || 'M';
				retval = IfxUtl.validateTime(value, flag, man.toUpperCase() === "M");
				if (!retval) {
					throw ifx_makeFvalError("時間輸入錯誤");
				}
				break;
			case "6": // 有包含檢查OBUFG$的系統變數
			case "CH_OBUFG":
				ifx.setValue("STAT$", "0");
				// 鍾 A function(0) X(10)輸入統編8位會不過
				value = value.trim();
				// end
				if (value.length == 0) return;
				var obufg_tmp = ifx.getValue("OBUFG$").toString();
				ifx.setValue("STAT$", "1");
				// 不是OBU時才需要檢查身分證
				if (obufg_tmp == "2") {
					if (!isObu(value)) {
						errmsg = formatError("EA0_2");
						throw ifx_makeFvalError(errmsg);
					}
					retval = true;
					console.log("not checkID()!");
				} else {
					if (isObu(value)) {
						errmsg = formatError("EA0_1");
						throw ifx_makeFvalError(errmsg);
					}
					retval = checkID(value);
					if (!retval) {
						// 有包含檢查OBUFG$的系統變數
						retval = checkUniNo(value, true);
					}
				}
				if (retval) {
					ifx.setValue("STAT$", "0");
				} else {
					// 警告訊息者,多加入 請確認是否為外國人
					errmsg = formatError("EA0_1");
					errmsg += formatError("EA0_3");
					if (confirm(errmsg + ",繼續請按[確定]")) {
						retval = true;
					}
				}
				if (!retval) {
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "7": // 檢查統編是否為OBU DBU
				value = value.trim();
				ifx.setValue(flag1, checObudbu(value, true));
				break;
			case "8": // 檢查欄位是否是中英文,並回傳值
				var len = value.length;
				var ckchinese = 0;
				var checklen = value.replace(/[^\x00-\xff]/g, "**").length;
				if (len == checklen / 2) { // 純全形
					ckchinese = 2;
				} else if (len == checklen) { // 純半形
					ckchinese = 1;
				} else { // 夾雜
					ckchinese = 3;
				}
				ifx.setValue(flag1, ckchinese);
				break;
			case "9": // 檢查年月 1-12
			case "YM":
			case "YM13": // 檢查年月 1-13
				var isError = false;
				if (ifx.getField(nnn).type.replace(/[xXcC]+/g, "") == "" && $.trim(value).length != 0 && parseInt($.trim(value), 10) == 0 || value.replace(/\d+|\s+/g, "").length != 0)
					isError = true;
				if (!isError)
					if (value.length == 0 && flag == 1) {
						isError = true;
					} else {
						var fldLen = ifx.getField(nnn).len;
						var ym = parseInt(value, 10);
						var yy = parseInt(ym / 100, 10);
						var mm = parseInt(yy * 100, 10);
						mm = parseInt(ym - mm);
						console.log("fldLen:" + fldLen + " ym:" + ym + ", yy:" + yy + ", mm=" + mm);
						if (ym == 0) {
							if (flag == 1)
								isError = true;
							// throw ifx_makeFvalError("請輸入年月!");
						} else {
							if (yy == 0) {
								if (fldLen == 5)
									isError = true;
								// throw ifx_makeFvalError("請輸入年月，格式yyyMM");
								else
									isError = true;
								// throw ifx_makeFvalError("請輸入年月，格式yyyyMM");
							}
							if (flag == 1 && fldLen == 6) {
								if (yy < 1912)
									isError = true;
								// throw ifx_makeFvalError("輸入西元年不可小於1912");
							}
							if (opt == "YM13") {
								if (mm == 0 || mm > 13)
									isError = true;
								// throw ifx_makeFvalError("月份限輸入1-13");
							} else {
								if (mm == 0 || mm > 12)
									isError = true;
								// throw ifx_makeFvalError("月份限輸入1-12");
							}
						}
					}
				if (isError)
					throw ifx_makeFvalError("年月格式錯誤");
				break;
			case "M":
				var myreg = /^[^\[\]\(\)\\<>:;,@.]+[^\[\]\(\)\\<>:;,@]*@[a-z0-9A-Z]+(([.]?[a-z0-9A-Z]+)*[-]*)*[.]([a-z0-9A-Z]+[-]*)+$/g;
				var ems = value.split("\.");
				if (ems[ems.length - 1].length > 16)
					throw ifx_makeFvalError("E-Mail格式錯誤");
				else
					if (myreg.test(value) == false) throw ifx_makeFvalError("E-Mail格式錯誤");
				break;
			case "PH":
				var reg = value.replace(/\d|\(|\)|\-|\#|\s/g, "");
				if (reg.length > 0)
					throw ifx_makeFvalError("電話格式錯誤");
				break;
			default:
				alert("還沒寫 A, " + opt);
		}
		return retval;
		//檢核稅籍編號
		function checkTaxId(id) {
			if (id.replace(/\s+/g, '').length < 10) return false;
			var bcDate = id.substring(0, 8);
			var en = id.substring(8, 10);
			if (!IfxUtl.validateDate(bcDate, "F", 8)) return false;
			var myreg = /[^\a-\z\A-\Z]+/g;
			if (myreg.test(en) == true) return false;
			return true;
		}
		// helper of A
		function checkID(id) {
			tab = "ABCDEFGHJKLMNPQRSTUVXYWZIO";
			A1 = new Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3);
			A2 = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5);
			Mx = new Array(9, 8, 7, 6, 5, 4, 3, 2, 1, 1);
			if (id.length != 10) return false;
			var i = tab.indexOf(id.charAt(0));
			if (i == -1) return false;
			sum = A1[i] + A2[i] * 9;
			for (i = 1; i < 10; i++) {
				v = parseInt(id.charAt(i));
				if (isNaN(v)) return false;
				sum = sum + v * Mx[i];
			}
			if (sum % 10 != 0) return false;
			return true;
		}
		/*
		 * 第一個字元代表地區，轉換方式為：A轉換成1,0兩個字元，B轉換成1,1……但是Z、I、O分別轉換為33、34、35
		 * 第二個字元代表性別，1代表男性，2代表女性
		 * 第三個字元到第九個字元為流水號碼。
		 * 第十個字元為檢查號碼。
		 * 每個相對應的數字相乘，如A123456789代表1、0、1、2、3、4、5、6、7、8，相對應乘上1987654321，再相加。
		 * 相加後的值除以模數，也就是10，取餘數再以模數10減去餘數，若等於檢查碼，則驗證通過
		 */
		function studIdNumberIdentify(nationality, idNumber) {
			studIdNumber = idNumber.toUpperCase();
			//本國人
			if (nationality == 0) {
				//驗證填入身分證字號長度及格式
				if (studIdNumber.length != 10) return false;
				//格式，用正則表示式比對第一個字母是否為英文字母
				if (isNaN(studIdNumber.substr(1, 9)) || (!/^[A-Z]$/.test(studIdNumber.substr(0, 1)))) return false;
				var idHeader = "ABCDEFGHJKLMNPQRSTUVXYWZIO"; //按照轉換後權數的大小進行排序
				//這邊把身分證字號轉換成準備要對應的
				studIdNumber = (idHeader.indexOf(studIdNumber.substring(0, 1)) + 10) + '' + studIdNumber.substr(1, 9);
				//開始進行身分證數字的相乘與累加，依照順序乘上1987654321
				s = parseInt(studIdNumber.substr(0, 1)) + parseInt(studIdNumber.substr(1, 1)) * 9 + parseInt(studIdNumber.substr(2, 1)) * 8 + parseInt(studIdNumber.substr(3, 1)) * 7 + parseInt(studIdNumber.substr(
					4, 1)) * 6 + parseInt(studIdNumber.substr(5, 1)) * 5 + parseInt(studIdNumber.substr(6, 1)) * 4 + parseInt(studIdNumber.substr(7, 1)) * 3 + parseInt(studIdNumber.substr(8, 1)) * 2 + parseInt(
						studIdNumber.substr(9, 1));
				checkNum = parseInt(studIdNumber.substr(10, 1));
				//模數 - 總和/模數(10)之餘數若等於第九碼的檢查碼，則驗證成功
				//若餘數為0，檢查碼就是0
				if ((s % 10) == 0 || (10 - s % 10) == checkNum) {
					return true;
				} else {
					return false;
				}
			}
			//外籍生，居留證號規則跟身分證號差不多，只是第二碼也是英文字母代表性別，跟第一碼轉換二位數字規則相同，但只取餘數
			else {
				//驗證填入身分證字號長度及格式
				if (studIdNumber.length != 10) return false;
				//格式，用正則表示式比對第一個字母是否為英文字母
				if (isNaN(studIdNumber.substr(2, 8)) || (!/^[A-Z]$/.test(studIdNumber.substr(0, 1))) || (!/^[A-Z]$/.test(studIdNumber.substr(1, 1)))) return false;
				var idHeader = "ABCDEFGHJKLMNPQRSTUVXYWZIO"; //按照轉換後權數的大小進行排序
				//這邊把身分證字號轉換成準備要對應的
				studIdNumber = (idHeader.indexOf(studIdNumber.substring(0, 1)) + 10) + '' + ((idHeader.indexOf(studIdNumber.substr(1, 1)) + 10) % 10) + '' + studIdNumber.substr(2, 8);
				//開始進行身分證數字的相乘與累加，依照順序乘上1987654321
				s = parseInt(studIdNumber.substr(0, 1)) + parseInt(studIdNumber.substr(1, 1)) * 9 + parseInt(studIdNumber.substr(2, 1)) * 8 + parseInt(studIdNumber.substr(3, 1)) * 7 + parseInt(studIdNumber.substr(
					4, 1)) * 6 + parseInt(studIdNumber.substr(5, 1)) * 5 + parseInt(studIdNumber.substr(6, 1)) * 4 + parseInt(studIdNumber.substr(7, 1)) * 3 + parseInt(studIdNumber.substr(8, 1)) * 2 + parseInt(
						studIdNumber.substr(9, 1));
				//檢查號碼 = 10 - 相乘後個位數相加總和之尾數。
				checkNum = parseInt(studIdNumber.substr(10, 1));
				//模數 - 總和/模數(10)之餘數若等於第九碼的檢查碼，則驗證成功
				///若餘數為0，檢查碼就是0
				if ((s % 10) == 0 || (10 - s % 10) == checkNum) {
					return true;
				} else {
					return false;
				}
			}
		}

		function checkUniNo(idvalue, A0Type) {
			var idvalue3 = idvalue.slice(0, 3);
			var idvalue7 = idvalue.slice(3);
			if (isObu(idvalue) && A0Type) {
				if (idvalue.length != 10) {
					return false;
				}
				re = /^\d{7}$/;
				if (!re.test(idvalue7)) {
					return false;
				}
				return true;
			}
			// for END
			var tmp = new String("12121241");
			var sum = 0;
			re = /^\d{8}$/;
			if (!re.test(idvalue)) {
				return false;
			}
			for (var i = 0; i < 8; i++) {
				s1 = parseInt(idvalue.substr(i, 1));
				s2 = parseInt(tmp.substr(i, 1));
				sum += cal(s1 * s2);
			}
			if (!valid(sum)) {
				if (idvalue.substr(6, 1) == "7") return (valid(sum + 1));
			}
			return (valid(sum));

			function valid(n) {
				return (n % 10 == 0) ? true : false;
			}

			function cal(n) {
				var sum = 0;
				while (n != 0) {
					sum += (n % 10);
					n = (n - n % 10) / 10; // 取整數
				}
				return sum;
			}
		}
		// 確認開頭是否為BUA~BUZ
		function isObu(idvalue) {
			var idvalue3 = idvalue.slice(0, 3);
			if (idvalue3.match(/^BU[A-Z]/)) {
				return true;
			}
			return false;
		}
		// 回傳是否為obu dbu 等等
		function checObudbu(idvalue, A0Type) {
			var idvalue3 = idvalue.slice(0, 3);
			var idvalue7 = idvalue.slice(3);
			if (isObu(idvalue) && A0Type) {
				if (idvalue.length != 10) {
					return 0;
				}
				re = /^\d{7}$/;
				if (!re.test(idvalue7)) {
					return 0;
				}
				return 2;
			}
			// for END
			var tmp = new String("12121241");
			var sum = 0;
			re = /^\d{8}$/;
			if (!re.test(idvalue)) {
				return 0;
			}
			for (var i = 0; i < 8; i++) {
				s1 = parseInt(idvalue.substr(i, 1));
				s2 = parseInt(tmp.substr(i, 1));
				sum += cal(s1 * s2);
			}
			if (!valid(sum)) {
				if (idvalue.substr(6, 1) == "7") return Ckobufg((valid(sum + 1)));
			}
			return Ckobufg(valid(sum));

			function Ckobufg(temp) {
				if (temp) {
					return 1;
				} else {
					return 0;
				}
			}
		}

		function valid(n) {
			return (n % 5 == 0) ? true : false;
		}

		function cal(n) {
			var sum = 0;
			while (n != 0) {
				sum += (n % 10);
				n = (n - n % 10) / 10; // 取整數
			}
			return sum;
		}

		function calcAcnoChkDG(ACNOid) {
			var oneadd = [7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2]; // 合庫公布基碼
			var varray = ACNOid.split(""); // 輸入欄位取各個陣列
			var ginumber = []; // 以上兩個位置相乘
			var sum = 0; // 積數總和
			var checknum; // 檢查碼
			var unumber; // 餘數
			var baynumber = 11; // 被除數
			for (var i = 0; i < 12; i++) {
				ginumber[i] = varray[i] * oneadd[i];
				sum += ginumber[i];
			}
			unumber = sum % baynumber;
			if (unumber == 1) {
				checknum = 0;
			} else if (unumber == 0) {
				checknum = 1;
			} else {
				checknum = baynumber - unumber;
			}
			return checknum;
		}
		// end of A
	};
	Rtn.prototype.ALERT = function (source, target) {
		var args = aps.call(arguments);
		var SHOW = "";
		console.log("before alert");
		for (var i = 1; i < args.length; i++) {
			SHOW += easyValue(args[i]).replace(/\\n/g, '\n') + '\n'; // 自己加入換行的也一併替換
		}
		alert(SHOW);
		console.log("after alert");
	};
	Rtn.prototype.ASGN = function (source, target, fg) {
		console.log("ASGN() " + source + " to " + target);
		ifx.copyTo(source, target);
		if (fg) ifx.changSysVar(target.replace("$", ""), ifx.getValue(source), "更新sysVar...");
		TRACE("ASGN() result, source: " + ifx.getValue(source) + ", target:" + target + ', value:' + +ifx.getValue(target));
	};
	Rtn.prototype.B = function (target, source, pos) {
		var value = ifx.getValue(source);
		if (value.length > pos && value.charAt(pos - 1) != " " && value.charAt(pos) != " ") {
			var i = value.lastIndexOf(" ", pos - 1);
			if (i != -1) {
				var p1 = value.substring(0, i);
				var p2 = value.substring(i + 1);
				var gap = new Array(pos - i + 1).join(" ");
				p1 = p1 + gap + p2;
				var fldLen = ifx.getField(target).len;
				if (p1.length > fldLen) {
					p1 = p1.slice(0, fldLen);
				} else if (p1.length < fldLen) {
					p1 = IfxUtl.stringFormatter(p1, fldLen);
				}
				ifx.setValue(target, p1);
			}
		} else {
			ifx.setValue(target, value);
		}
	};
	Rtn.prototype.C1 = function () {
		var args = aps.call(arguments);
		args.shift(); // ignore field name
		var value = easyValue(args.shift()); // ifx.getField(args.shift());
		var fn1 = args.shift(),
			fn2 = args.shift(),
			fn3 = args.shift();
		if (value < 0) fn1(this);
		else if (value == 0) fn2(this);
		else fn3(this);
	};
	Rtn.prototype.C2 = function () {
		var args = aps.call(arguments);
		args.shift(); // ignore first arg
		var n = parseInt(easyValue(args.shift()), 10);
		TRACE(" value:" + n + ", fns length:" + args.length);
		if (isNaN(n))
			// 鍾多加一些錯誤log
			console.log(" n isNaN ");
		else if (args.length <= n) console.log(" length <= n");
		else {
			// end
			var rtn = args[n];
			TRACE(typeof (rtn));
			rtn(this);
		}
	};
	Rtn.prototype.C3 = function () {
		var args = aps.call(arguments);
		args.shift();
		var compareTo = new Number(easyValue(args.shift())),
			value = Number((args.shift())());
		console.log("C3, compareTo:" + compareTo + ", value:" + value);
		// 鍾 多加一些錯誤 log
		if (args.length == 2 || compareTo == value) {
			var rtn = (compareTo == value) ? args[0] : args[1];
			rtn();
		} else console.log(" false is unfine ");
		// end
	};
	Rtn.prototype.C4 = function () {
		var fn, args = aps.call(arguments);
		args.shift(); // ignore field name;
		var value = $.trim(easyValue(args.shift()));
		// 鍾 多加一些錯誤 log
		if (value.length == 0 && args.length == 1) console.log(" false is unfine ")
		else {
			fn = args.shift(); // fn1
			if (value.length != 0) {
				fn = args.shift(); // fn2
			}
			fn(this);
		}
		// end
	};
	Rtn.prototype.C5 = function () {
		var fn, args = aps.call(arguments);
		args.shift();
		var target = $.trim(easyValue(args.shift())); // 柯 修改 去左右空白
		var value = (args.shift())();
		value = $.trim(value); // leo 不同欄位長度之比較有問題,故也去空白..
		// 因為字串的S 會被變成函式S 故 加入"S" 去除前後引號
		if (value.slice(0, 1) == value.slice(-1) && (value.slice(0, 1) == '"' || value.slice(0, 1) == "'")) {
			value = value.slice(1, -1);
		}
		console.log("C5  target:" + target + ", value:" + value);
		fn = args.shift();
		if (target != value) {
			fn = args.shift();
		}
		fn();
	};
	Rtn.prototype.C6 = function () {
		var args = aps.call(arguments);
		var fn, target = args.shift();
		var value = easyValue(args.shift());
		for (var i = 0; i < args.length; i++) {
			fn = args[i];
			var temp = fn();
			if (temp && temp.slice(0, 1) == temp.slice(-1) && (temp.slice(0, 1) == '"' || temp.slice(0, 1) == "'")) {
				temp = temp.slice(1, -1);
			}
			if (value == temp) {
				// 鍾 打錯字
				ifx.setValue(target, i);
				// end
			}
		}
	};
	Rtn.prototype.C7 = function () {
		var fn,
			fldName,
			dirty,
			args = aps.call(arguments);
		args.shift();
		fldName = args.shift();
		dirty = ifx.getField(fldName).isDirty();
		console.log('C7(),' + fldName + " dirty?" + dirty);
		fn = args.shift(); // fn1
		if (!dirty) {
			if (fn == null) { // no fn1 defined, skip to next field
				console.log('no fn defined, call skip()');
				throw ifx_makeSkip();
			}
		} else {
			fn = args.shift(); // fn2
		}
		if (fn != null) fn(this);
	};
	// 此函式有經過make的特殊處裡 funcmapper
	Rtn.prototype.C8 = function () {
		var args = aps.call(arguments);
		args.shift(); // ignore field name
		var target = parseFloat(easyValue(args.shift()));
		var value = parseFloat(easyValue(args.shift()));
		var fn1 = args.shift(),
			fn2 = args.shift(),
			fn3 = args.shift();
		if (target < value) fn1(this);
		else if (target == value) fn2(this);
		else fn3(this);
	};
	Rtn.prototype.gigo = function (x) {
		if (x.charAt(0) === "#") return ifx.getValue(x);
		else {
			console.log("gigo:" + x);
			return x;
		}
	};
	var theDate = new Date(2010, 1, 1);
	Rtn.prototype.D = function () {
		var args = aps.call(arguments),
			target = args[0],
			opt = args[1], // =parseInt(args[1],10),
			nnn, flag, d, yyyy, mm, dd, value;
		switch (opt.toLowerCase()) {
			case '1':
				if (!easyValue(args[2])) break;
				nnn = args[2];
				value = getBC(nnn);
				yyyy = parseInt(value.substring(0, 4), 10);
				mm = parseInt(value.substr(4, 2), 10);
				dd = parseInt(value.substr(6, 2), 10);
				var mmm = IfxUtl.getMonthName(mm);
				ifx.setValue(target, mmm + " " + dd + ", " + yyyy);
				break;
			case '1a':
				if (!easyValue(args[2])) break;
				nnn = args[2];
				value = getBC(nnn);
				if (!value) break;
				yyyy = parseInt(value.substring(0, 4), 10);
				mm = parseInt(value.substr(4, 2), 10);
				dd = parseInt(value.substr(6, 2), 10);
				var mmm = IfxUtl.getMonthName(mm);
				ifx.setValue(target, yyyy + " " + mmm + " " + dd);
				break;
			case '2':
				if (!easyValue(args[2])) break;
				d = IfxUtl.n2d(getBC(args[2]));
				value = IfxUtl.daysBetween(d, theDate, false);
				ifx.setValue(target, value);
				break;
			case '3':
				if (!easyValue(args[2])) break;
				nnn = parseInt(easyValue(args[2]), 10); // days
				value = IfxUtl.addDays(theDate, nnn);
				value = IfxUtl.BC2ROC(IfxUtl.d2n(value)); // conver to ROC
				ifx.setValue(target, value);
				break;
			case '4':
				if (!easyValue(args[3])) break;
				flag = args[2];
				nnn = args[3];
				if (flag == 0) {
					value = IfxUtl.ROC2BC(easyValue(nnn));
				} else {
					yyyy = parseInt(nnn.substring(0, 2), 10);
					mm = parseInt(nnn.substr(2, 2), 10);
					dd = parseInt(nnn.substr(4, 2), 10);
					yyyy = 1912 - yyyy;
					value = yyyy + "" + mm + "" + dd;
				}
				ifx.setValue(target, value);
				break;
			case '6':
				if (!easyValue(args[3])) break;
				flag = args[2];
				nnn = IfxUtl.n2d(easyValue((args[3])));
				if (flag == 1) {
					mmm = IfxUtl.n2d(easyValue(args[4]));
					value = IfxUtl.daysBetween(mmm, nnn, false);
					ifx.setValue(target, value);
				} else if (flag == 2 || flag == 3) {
					mmm = parseInt(easyValue(args[4]), 10);
					if (flag == 3) mmm = -mmm;
					value = IfxUtl.addDays(nnn, mmm);
					value = IfxUtl.d2n(value);
					ifx.setValue(target, value);
				}
				break;
			case '7':
				console.log("date 7");

				function monthDayDiff() {
					var flag = [1, 3, 5, 7, 8, 10, 12, 4, 6, 9, 11, 2];
					var start = new Date(sDateY, sDateM, sDateD);
					var end = new Date(eDateY, eDateM, eDateD);
					var year = end.getFullYear() - start.getFullYear();
					var month = end.getMonth() - start.getMonth();
					var day = end.getDate() - start.getDate();
					if (month < 0) {
						year--;
						month = end.getMonth() + (12 - start.getMonth());
					}
					if (day < 0) {
						month--;
						var index;
						for (var i = 0; i < flag.length; i++)
							if (flag[i] == (end.getMonth() - 1)) index = i;
						// var index = flag.findIndex((temp) => {
						// return temp === end.getMonth() - 1;
						// });
						var monthLength;
						if (index <= 6) {
							monthLength = 31;
						} else if (index > 6 && index <= 10) {
							monthLength = 30;
						} else {
							monthLength = 28;
							if (sDateY % 4 === 0 && sDateY % 100 !== 0 || sDateY % 400 === 0) monthLength = 29;
						}
						day = end.getDate() + (monthLength - start.getDate());
					}
					yy = parseInt((12 * year + month) / 12);
					mm = (12 * year + month) % 12;
					dd = day;
				}
				flag = args[2];
				var bc = true;
				var ylen = easyValue(args[3]).length <= 7 ? 3 : 4;
				var sDateY = parseInt(easyValue(args[3]).substring(0, ylen), 10);
				var sDateM = parseInt(easyValue(args[3]).substr(ylen, 2), 10);
				var sDateD = parseInt(easyValue(args[3]).substr(ylen + 2, 2), 10);
				if (sDateY < 1911) {
					sDateY = sDateY + 1911;
					bc = false;
				}
				console.log("date 7, flag=" + flag);
				if (flag == 1) {
					var eDateY = parseInt(easyValue(args[4]).substring(0, ylen), 10);
					var eDateM = parseInt(easyValue(args[4]).substr(ylen, 2), 10);
					var eDateD = parseInt(easyValue(args[4]).substr(ylen + 2, 2), 10);
					var day = args.length == 6 ? easyValue(args[5]) : 0;
					if (eDateY < 1911) eDateY = eDateY + 1911;
					var s = new Date(sDateY, sDateM - 1, sDateD);
					var e = new Date(eDateY, eDateM - 1, eDateD);
					if (e < s) break;
					monthDayDiff();
					value = IfxUtl.numericFormatter(yy, 4) + IfxUtl.numericFormatter(mm, 2) + IfxUtl.numericFormatter(dd, 2);
					ifx.setValue(target, value);
				}
				if (flag == 2 || flag == 3) {
					if (!easyValue(args[4])) break;
					if (!easyValue(args[5])) break;
					mm = parseInt(easyValue(args[4]), 10);
					dd = parseInt(easyValue(args[5]), 10);
					var date;
					var dayLimt = [1, 3, 5, 7, 8, 10, 12, 4, 6, 9, 11, 2];
					var index;
					var monthLength;
					//n鍾
					//for (var i = 0; i < dayLimt.length; i++)
					//	if (flag == 2) {
					//		if (dayLimt[i] == parseInt(((sDateM + mm) % 12), 10) || dayLimt[i] == (sDateM + mm)) index = i;
					//	} else {
					//		var mt = parseInt(((sDateM - mm) % 12), 10);
					//		if (mt == 0) mt = 12;
					//		if (mt < 0) mt = 12 + mt;
					//		if (dayLimt[i] == mt) index = i;;
					//	}
					var nmm;
					if (flag == 2) {
						nmm = parseInt(((sDateM + mm) % 12), 10);
						if (nmm == 0) nmm = 12;
					} else {
						nmm = parseInt(((sDateM - mm) % 12), 10);
						if (nmm == 0) nmm = 12;
						if (mmm < 0) nmm = 12 + nmm;
					}
					console.log("date 7, flag=" + flag + ",nmm=" + nmm);
					for (var i = 0; i < dayLimt.length; i++)
						if (nmm == dayLimt[i]) index = i;
					//n鍾 end
					if (index <= 6) {
						monthLength = 31;
					} else if (index > 6 && index <= 10) {
						monthLength = 30;
					} else {
						monthLength = 28;
						var temp = sDateY;
						if (flag == 2 && sDateM + mm > 12) sDateY = parseInt(sDateY, 10) + parseInt(((sDateM + mm) / 12).toFixed(0), 10);
						if (flag == 3 && sDateM + mm < 0) sDateY = parseInt(sDateY, 10) + (sDateM - mm > -12 ? -1 : ((sDateM - mm) / 12).toFixed(0) - 1);
						if (sDateY % 4 === 0 && sDateY % 100 !== 0 || sDateY % 400 === 0) monthLength = 29;
						sDateY = temp;
					}
					if (sDateD > monthLength) sDateD = monthLength;
					if (flag == 2) date = new Date(sDateY, sDateM + mm - 1, sDateD + dd);
					else date = new Date(sDateY, sDateM - mm - 1, sDateD - dd);
					value = (bc ? IfxUtl.numericFormatter(date.getFullYear(), 4) : IfxUtl.numericFormatter(date.getFullYear() - 1911, 4)) + IfxUtl.numericFormatter(date.getMonth() + 1, 2) + IfxUtl.numericFormatter(
						date.getDate(), 2);
					ifx.setValue(target, value);
				}
				break;
			case '8':
				/*
				var id = target.replace(/#/, "#fld_");
				var defdate;
				var year;
				var mon;
				var day;
				if (args.length >= 3) {
					defdate = args[2] < 19110000 ? args[2] + 19110000 : args[2];
					year = defdate / 10000;
					mon = (defdate - year * 10000) / 100 - 1;
					day = defdate % 100;
					defdate = new Date(year, mon, day);
				} else defdate = new Date();
				$(id).on('blur', function() {
					// $(id).datepicker('hide');
				});
				$(id).datepicker({
					yearSuffix: "", // 將年改為空白
					changeYear: true, // 手動修改年
					changeMonth: true, // 手動修改月
					showWeek: true, // 顯示第幾周
					firstDay: 1, // 0為星期天
					showOtherMonths: true, // 在本月中顯示其他月份
					selectOtherMonths: true, // 可以在本月中選擇其他月份
					showButtonPanel: true, // 顯示bottom bar
					closeText: '清除', // 將離開改為清除
					dateFormat: "yy/mm/dd",
					viewDate: defdate,
					onSelect: function(dateText, inst) {
						var dateFormate = inst.settings.dateFormat == null ? "yy/mm/dd" : inst.settings.dateFormat; // 取出格式文字
						var reM = /m+/g;
						var reD = /d+/g;
						var objDate = {
							y: inst.selectedYear - 1911 < 0 ? inst.selectedYear : inst.selectedYear - 1911,
							m: String(inst.selectedMonth + 1).length != 1 ? inst.selectedMonth + 1 : ("0" + String(inst.selectedMonth + 1)),
							d: String(inst.selectedDay).length != 1 ? inst.selectedDay : "0" + String(inst.selectedDay)
						};
						$.each(objDate, function(k, v) {
							var re = new RegExp(k + "+");
							dateFormate = dateFormate.replace(re, v);
						});
						inst.input.val(dateFormate);
						// ifx.setValue(target, dateFormate);
						ifx.KeyForward('datepicker');
					}
				}).keydown(function(event) {
					if (event.which === $.ui.keyCode.ENTER) {
						event.preventDefault();
					}
				});
				*/
				break;
			case 'today':
				ifx.setValue(target, IfxUtl.getToday());
				break;
			case "terms":
				var dFrom = easyValue(argv[2]),
					dTo = easyValue(argv[3]),
					period = easyValue(argv[4]),
					termsName = argv[5],
					monthsRemainedName = argv[6],
					daysRemainedName = argv[7],
					termEndDateName = argv[8],
					r;
				r = calcPeriod(dFrom, dTo, period);
				ifx.setValue(termsName, r.terms);
				if (monthsRemainedName) {
					ifx.setValue(monthsRemainedName, r.months);
				}
				if (daysRemainedName) {
					ifx.setValue(daysRemainedName, r.days);
				}
				if (termEndDateName) {
					ifx.setValue(termEndDateName, r.dEnd);
				}
				break;
			default:
				alert("dont know " + opt);
		}
		// ---------------------------------------------------
		// sync with ifx-rtn.js (d1.html)
		function calcPeriod(dFrom, dTo, period) {
			var dEnd, terms = 0,
				m,
				lastEnd = null;
			if (period[period.length - 1].toUpperCase() === 'M') {
				m = parseInt(period);
			} else {
				alert('error period:' + period);
				return;
			}
			dFrom = toDate(dFrom);
			dTo = toDate(dTo);
			console.log("dFrom:" + dFrom);
			console.log("dTo:" + dTo);
			while (true) {
				dEnd = advance(new Date(dFrom), m * (terms + 1));
				if (dTo >= dEnd) {
					console.log("term " + terms + ", dEnd:" + dEnd.toString());
					terms++;
					lastEnd = new Date(dEnd);
				} else {
					if (terms == 0) {
						terms = 1;
						lastEnd = new Date(dTo);
					}
					break;
				}
			}
			var i = 0,
				days = 0,
				lastMonth = null;
			if (lastEnd < dTo) { // 計算剩餘日數
				for (i = 0; i < m; i++) {
					dEnd = advance(new Date(lastEnd), i + 1);
					if (dEnd > dTo) {
						break;
					}
					lastMonth = new Date(dEnd);
					if (dEnd == dTo) {
						i++;
						break;
					}
				}
				if (lastMonth == null) {
					days = dateDiff(dTo, lastEnd);
				} else {
					days = dateDiff(dTo, lastMonth);
				}
			}
			return {
				terms: terms,
				dEnd: formatDate(lastEnd),
				months: i,
				days: days
			};

			function toDate(d) {
				return new Date(d.slice(0, 4), d.slice(4, 6) - 1, d.slice(6));
			}

			function advance(d, m) {
				var yNow, mNow,
					yFrom = d.getFullYear(),
					mFrom = d.getMonth() + 1;
				var dNow = new Date(d.setMonth(d.getMonth() + m));
				yNow = dNow.getFullYear();
				mNow = dNow.getMonth() + 1;
				if ((yNow - yFrom) * 12 + mNow - mFrom > m) { // 超過三個月月底 到了下一月
					if (mNow == 1) {
						dNow = new Date(yNow - 1, 12, 0); // 一月 就回到去年年底 (不過 不可能有這種情形 因為12月是大月)
					} else {
						dNow = new Date(yNow, mNow - 1, 0); // 否則回到上個月月底
					}
				}
				return new Date(dNow);
			}

			function formatDate(d, sep) {
				sep = sep || '';
				var yyyy = d.getFullYear().toString();
				var mm = (d.getMonth() + 1).toString(); // getMonth() is zero-based
				var dd = d.getDate().toString();
				return yyyy + sep + (mm[1] ? mm : "0" + mm[0]) + sep + (dd[1] ? dd : "0" + dd[0]);
			}

			function dateDiff(date1, date2) {
				date1.setHours(0);
				date1.setMinutes(0, 0, 0);
				date2.setHours(0);
				date2.setMinutes(0, 0, 0);
				var datediff = Math.abs(date1.getTime() - date2.getTime()); // difference
				return parseInt(datediff / (24 * 60 * 60 * 1000), 10); // Convert values days
				// and
				// return value
			}

			function getLastDay(year, month) {
				new Date(year, month, 0);
			}
		}
		// end of calcPeriod
		// ------------------------------------
		function getBC(nnn) {
			var f = ifx.getField(nnn);
			var value = ifx.getValue(nnn);
			if (f.type == "D") {
				value = IfxUtl.ROC2BC(value);
			}
			return value;
		}
	};
	Rtn.prototype.E = function (fldname) {
		var args = aps.call(arguments),
			target = args.shift(),
			opt = easyValue(args.shift());
		TRACE("E() fldname:" + fldname + ",opt:" + opt);
		if (opt === "A") throw "unimplemented for E(" + opt + ")";
		if (args.length > 1) {
			target = args.shift();
		}
		TRACE("target len:" + ifx.getField(target).len);
		TRACE("target dlen:" + ifx.getField(target).dlen);
		var dlen = ifx.getField(target).dlen;
		var len = ifx.getField(target).len;
		var s = args.shift();
		TRACE("orig expression:" + s);
		var ss = s.split(/[+\-*\/\()]/);
		$.each(ss, function (i, x) {
			if (x.slice(0, 1) == "#" || x.slice(-1) == "$") {
				var v = new Number(ifx.getValue(x).replace(/[,]/g, "")).toString();
				s = s.replace(x, v);
			}
		});
		// 鍾 修改 減負數時會錯誤
		s = s.replace("--", "+");
		TRACE("s:" + s);
		// end
		var v = eval(s);
		TRACE("eval v:" + v);
		// var math = require('mathjs');
		var tmpv = 0;
		// 20180511 柯 新寫法在0 - 0.0000001時會出錯，故在錯誤時使用舊寫法
		try {
			tmpv = calc(s); // 潘
			TRACE("calc v:" + v);
		} catch (e) {
			tmpv = v;
			TRACE("calc error: v = eval");
		}
		v = parseFloat(tmpv.toString()).toFixed(dlen);
		/* 潘 修改小數進位不直接四捨五入 */
		// v = parseFloat(tmpv.toString()).toFixed(dlen == 0? dlen : dlen + 1);
		TRACE("out v:" + v);
		// 20180511 柯 測試一下，怪異數字就變0吧
		// 20180518 怕後續衍生其他問題，還是先把這個修正註解
		/*
		 * if(isNaN(v)){ v = 0; TRACE("v isNaN... to:" + 0); }
		 */
		opt = parseInt(opt, 10);
		if (opt > 0) {
			if (opt == 99) { // 99 是無條件捨去到該小數點長度
				v = IfxUtl.floor(v, dlen);
			} else {
				v = IfxUtl.round(v, opt - 1);
			}
			// 鍾 修改 不時會出現奇怪的小數
		} else {
			// 20180511 柯 測試一下 換寫法後加這段有可能運算錯誤，故全部註解
			/*
			 * TRACE("dlen:" + dlen); v = IfxUtl.round(v, dlen + 1); //柯 新增 for opt =0 時 + -運算bug
			 * TRACE("round:" + v); v = IfxUtl.floor(v, dlen); TRACE("opt=0,v floor:" + v);'
			 */
		}
		// end
		// 找時間再改寫法 start
		var vlen = v.toString().length;
		if (dlen + len < vlen) {
			var delnum = vlen - dlen - len - 1; // 如果超過則必定有小數點的符號,故減1
			if (ifx.getField(target).signAllowed && v.toString().slice(0, 1) == "-") {
				v = v.toString().slice(delnum);
				v = v.toString().slice(0, 1) == "-" ? v : "-" + v;
			} else {
				v = v.toString().slice(delnum);
			}
		}
		var intvlen = parseInt(v).toString().length;
		if (len < intvlen) { // 如果整數長度大於欄位整數長度
			var delnum2 = intvlen - len;
			if (ifx.getField(target).signAllowed && v.toString().slice(0, 1) == "-") {
				v = v.toString().slice(delnum2);
				v = v.toString().slice(0, 1) == "-" ? v : "-" + v;
			} else {
				v = v.toString().slice(delnum2);
			}
		}
		// 找時間再改寫法 end
		// 20180511 柯 測試一下 綜合原始版本與新的寫法
		try {
			ifx.setValue(target, v.toFixed(dlen).toString());
		} catch (e) {
			ifx.setValue(target, v.toString());
		}
	};
	Rtn.prototype.EXIT = function (fldname) {
		var args = aps.call(arguments),
			target = args.shift(),
			opt = easyValue(args.shift()),
			message = easyValue(args.shift());
		console.log("EXIT() fldname:" + fldname + ",opt:" + opt + ",message:" + message);
		alert("Exit:" + message);
		ifx.KeysEscapeTran(); // 依照更新,查詢類做CLOSE TAB的邏輯
	};
	// 不動作 直接沒事並給值
	Rtn.prototype.CHKHELP = function (fldname) {
		var args = aps.call(arguments);
		var check = args.splice(1, 1);
		var settarget = args.splice(1, 1);
		args = $.map(args, function (x) {
			return easyValue(x);
		});
		args[0] = fldname; // restore fld name
		ifx.help.displayHelp(args, ifx.getField(fldname).id(), '', function () {
			ifx.KeyForward('HELP');
		}, check, settarget);
	};
	Rtn.prototype.HELP = function (fldname) {
		var args = aps.call(arguments);
		var posOrNav;
		var orgContent, content;
		args = $.map(args, function (x) {
			return easyValue(x);
		});
		args[0] = fldname; // restore fld name
		ifx.help.displayHelp(args, ifx.getField(fldname).id(), '', function () {
			ifx.KeyForward('HELP');
		});
	};
	Rtn.prototype.HELP_AC = function (fldname) {
		var args = aps.call(arguments);
		args = $.map(args, function (x) {
			return easyValue(x);
		});
		args[0] = fldname; // restore fld name
		ifx.help.displayHelp(args, ifx.getField(fldname).id(), 'autocomplete', function () {
			ifx.KeyForward('HELP_AC');
		});
	};
	Rtn.prototype.TOOLTIP = function (fldname) {
		var args = aps.call(arguments);
		ifx.help.tooltip(args, ifx.getField(fldname).id());
	};
	// 鍾 增加 SKIPLN功能
	Rtn.prototype.SKIPLN = function (fldname) {
		var args = aps.call(arguments);
		var v = parseFloat(easyValue(args[1]));
		console.log(" SKIPLN " + fldname + "-" + v);
		ifx.getField(fldname).setSkipLn(v);
	};
	// end
	Rtn.prototype.I = function (fldname, flag, nnn, filename, segment, tagKey, tagField) {
		var value = ifx.getValue(fldname);
		console.log("enter I0(), value:" + value);
		if (parseFloat(value) === parseInt(value, 10)) return;
		var pos = value.indexOf(".");
		if (pos == -1) return;
		var dlen = value.length - pos - 1;
		var t = ifx.help.getLookupTable(filename, segment, tagKey, tagField);
		console.dir(t);
		var n = ifx.getValue(nnn);
		if (n in t) {
			var precision = t[n];
			if (precision < dlen) {
				throw ifx_makeFvalError(formatError("", ["EI", precision]));
			}
			return;
		} else {
			throw "program bug, can't match:" + n + " in table";
		}
	};
	Rtn.prototype.L = function () {
		var dlen = 0,
			idx, value;
		var args = aps.call(arguments),
			target = args.shift(),
			opt = easyValue(args.shift()).toUpperCase(),
			source = easyValue(args.shift());
		idx = source.indexOf(".");
		if (idx == -1) dlen = 0;
		else {
			dlen = source.length - (idx + 1);
		}
		if (opt == "C") {
			value = IfxUtl.amt2Chinese(source, dlen);
		} else if (opt == "B") {
			value = IfxUtl.amt2Chinese(source, dlen, true);
		} else if (opt == "E") {
			value = IfxUtl.amt2English(source, dlen);
		} else if (opt == "LEN") { // 計算欄位長度
			value = source.replace(/[^\x00-\xff]/g, "**").length;
		}
		// 四角轉碼 fourcoCode
		// 四角轉碼 RF = codeFourco
		if (opt == "F" || opt == "RF") {
			var furl = (opt == "F") ? "four" : "refour";
			var vendMultiply = (opt == "F") ? 4 : 1;
			$.ajax({
				type: 'post',
				dataType: 'json',
				async: false, // 重要!!!
				url: _contextPath + '/mvc/hnd/special/code/' + furl,
				data: {
					_d: source,
				},
				success: function (data) {
					if (data.success) {
						value = data.result;
						ifx.setValue(target, value);
						for (var i = 0; i < args.length; i++) {
							var vend = (i * vendMultiply) + vendMultiply; // 超過值就不做了
							if (value.length >= vend) {
								ifx.setValue(args[i], value.slice(i * vendMultiply, vend));
							} else {
								return;
							}
						}
					} else {
						console.log("四角轉碼回覆不成功!! input:" + source);
					}
				},
				'error': function () {
					alert("系統error:四角轉碼錯誤");
				}
			});
		} else {
			ifx.setValue(target, value);
		}
	};
	Rtn.prototype.M = function (target, x, f1, f2) {
		var v1 = (f1.charAt(0) == "#" ? ifx.getValue(f1) : f1),
			v2 = (f2.charAt(0) == "#" ? ifx.getValue(f2) : f2),
			v;
		v1 = parseFloat(v1);
		v2 = parseFloat(v2);
		v = (x.toUpperCase() == "L") ? Math.min(v1, v2) : Math.max(v1, v2);
		ifx.setValue(target, v);
	};
	Rtn.prototype.P = function (targetFld, type, sourceFld, _fillChar) {
		var args = aps.call(arguments);
		if (args[2].indexOf("@") != -1) {
			args.shift();
			type = args.shift();
			targetFld = args.shift();
			sourceFld = args.shift();
			if (args.length > 0) _fillChar = args.shift();
			else _fillChar = undefined;
		}
		var sourceValue = $.trim(ifx.getValue(sourceFld)),
			value = null,
			fillChar = _fillChar || " ",
			len = IfxUtl.strlen(sourceValue),
			targetLen = ifx.getField(targetFld).len,
			toPad = targetLen - len;
		switch (type) {
			case 'L':
				value = (toPad > 0) ? (sourceValue + getPad(fillChar, toPad)) : sourceValue;
				break;
			case 'R':
				value = (toPad > 0) ? (getPad(fillChar, toPad) + sourceValue) : sourceValue;
				break;
			case 'M':
				var m = targetLen - len,
					n = m / 2 + 1,
					n2 = m - n;
				if (m > 0) {
					value = getPad(fillChar, n) + sourceValue + getPad(fillChar, n2);
				} else {
					value = sourceValue;
				}
				break;
			case "1":
				value = "" + len;
				break;
			case "2":
				value = sourceValue.toUpperCase();
				break;
			default:
				throw "unknown type " + type + " for P function";
		}
		if (value != null) ifx.setValue(targetFld, value);

		function getPad(ch, len) {
			var s = "";
			while (s.length < len) s += ch;
			return s;
		}
	};
	Rtn.prototype.R = function () {
		var args = aps.call(arguments);
		if (args.length <= 4) {
			console.log('R() using form mode;');
			romWithForm(args, this.romTota);
			return;
		}
		//潘 直return 不使用舊的方法
		return;

		// old style (R1,@aaa,1,5)
		if (args.length != 5)
			return;
		var rimField = args.shift();
		console.log("in rim field:" + rimField);
		var romIndex = args.shift();
		console.log("romIndex:" + romIndex);
		this.rom = this.romTota[romIndex - 1].text;
		console.log("this.rom:" + this.rom);
		var target = args.shift();
		target = "#" + target.slice(1);
		console.log("R for:" + target);
		var offset = parseInt(args.shift(), 10);
		var len = parseInt(args.shift(), 10);
		console.log(this.rom); // rom is a closure value from the caller
		console.log("rom text length:" + IfxUtl.strlen(this.rom));
		console.log("R for:" + target + ", offset:" + offset + ", len:" + len);
		var v = IfxUtl.substr_big5(this.rom, offset - 1, len, false);
		ifx.getField(target).setValueFromTom(v);
		console.log("set " + target + " to:[" + v + "]");
	};

	function romWithForm(args, totaList) {
		var form = "";
		var decorator = null;
		form = args[2];
		if (args.length == 4) {
			decorator = args[3];
		}
		console.log("form name:" + form);
		if (!ifx.def.tom[form]) {
			alert("form:" + form + "未定義!!");
			throw 'missing tom:' + form;
		}
		var oTota = totaList;
		if (oTota == null) {
			console.error('missing tota form:' + form);
			throw 'missing tota form:' + form;
		}
		var f = null;
		var tomFields = $.map(ifx.def.tom[form], function (x) {
			if (!checkX(x.slice(1)) && decorator != null) {
				if (decorator[0] == '_') {
					x = x + decorator;
				} else if (decorator[decorator.length - 1] == '_') {
					x = "#" + decorator + x.slice(1);
				} else {
					x = x + decorator;
				}
			}
			return ifx.getFieldForTom(x);
		});
		if (oTota[0].host == "0") {
			oTota[0].parseTotaForm(tomFields);
		} else {
			ifx.ifxHost.parseTmp(oTota[0], tomFields);
		}
	}

	function checkX(name) {
		var reX = /X\((\d+)\)/;
		return name.match(reX);
	}

	function buildText(args) {
		var tmp = {
			fnCount: 0,
			fnOK: "",
			fnError: "",
			jsonText: "",
			text: "",
		}
		for (var i = 0; i < args.length; i++) {
			n = args[i];
			if (n.charAt(0) == "#") {
				tmp.jsonText += '"' + n.substring(1) + '"' + ":" + '"' + ifx.timFieldToHost(n, true) + '",';
			} else if (n.charAt(0) == "@") {
				var fnName = '#' + n.slice(1);
				if (tmp.fnCount == 0) tmp.fnOK = ifx.getValue(fnName);
				else tmp.fnError = ifx.getValue(fnName);
				tmp.fnCount++;
			} else tmp.text += n.toString();
		}
		if (tmp.jsonText != "{") {
			if (tmp.text.length > 0)
				tmp.jsonText += '"text"' + ":" + '"' + tmp.text + '",';
			tmp.jsonText = tmp.jsonText.substr(0, tmp.jsonText.length - 1).replace(/\r\n|\n/g, "$n") + "}";
		} else tmp.jsonText = "";
		return tmp;
	}
	Rtn.prototype.RESET_RIM = function () {
		var args = aps.call(arguments);
		args.shift();
		var rimSenderField = args.shift();
		var rimName = args.shift();
		console.log("RESET_RIM for " + rimSenderField + "," + rimName);
		resetCached(rimSenderField, rimName);
	};
	Rtn.prototype.STORE_RIM = function () {
		var args = aps.call(arguments);
		args.shift();
		var rimText = "";
		var n;
		var fnOK = '',
			fnError = '',
			fnCount = 0;
		var rimSenderField = args.shift();
		var rimName = args.shift();
		var hostType = args.shift();
		var poperror, errtemp;
		if (hostType == "WEBDB") {
			poperror = args.shift(); // 拿掉一個隱藏顯示功能
			if (poperror == "1") {
				errtemp = args.shift(); // 拿掉一個隱藏時的欄位
			}
		}
		var tmp = buildText(args);
		fnOK = tmp.fnOK;
		fnError = tmp.fnError;
		fnCount = tmp.fnCount;
		rimText = tmp.jsonText;
		storeCached(rimSenderField, rimName, rimText);
	};
	// 柯:此變數 for 欄位快速連按造成跳過很多v欄位的bug
	var interval_chk = false;
	Rtn.prototype.setIntervalChkOff = function () {
		interval_chk = false;
	};
	Rtn.prototype.S = function () {
		// ifx.rimSent = true; //小柯 增加後導致錯誤 刪除
		var self = this; // store this for callbacks
		ifx.RIMTESTNO = false;
		var args = aps.call(arguments);
		if (ifx.escSync) args = args[0];
		var source = args.shift();
		var rtnMode = self.mode;
		console.log("S() for " + source);
		if (interval_chk == true) {
			console.log("查詢間隔時間過短。");
			throw ifx_makeFvalError("查詢間隔時間過短。");
		}
		var swiftMode = ifx.isSwiftMode();
		var bMq = false;
		// swift就固定走mq 吧
		/*
		 * 潘 rim不送MQ if(swiftMode){ console.log("and GO bMq"); bMq = true; }
		 */
		if (self.origCaller) {
			source = self.origCaller.source;
			rtnMode = self.origCaller.mode;
		}
		// 小柯
		var tabStop = false;
		try {
			tabStop = ifx.getCurrentField().isTabStop();
		} catch (dontCare) { }
		var rimName = args.shift();
		// 柯 test time out -start
		var timeouttime = 0;
		// var romFields = parseInt(args.shift(), 10);
		var hostType = args.shift();
		var poperror, errtemp;
		var setadname;
		if (hostType == "WEBDB") {
			poperror = args.shift();
			if (poperror == "1") {
				errtemp = args.shift();
			}
		}
		if (hostType == "ADSDB") {
			setadname = args.shift();
		}
		// 同一個欄位調RIM，會導致第二個欄位被POP，故S後面不能再S
		var fnRimParse = args.pop();
		var text = "",
			xx = null;
		var n;
		var fnOK = '',
			fnError = '',
			fnCount = 0;
		console.log("!!!ifx.RIMTESTNO:" + ifx.RIMTESTNO);
		var tmp = buildText(args);
		fnOK = tmp.fnOK;
		fnError = tmp.fnError;
		fnCount = tmp.fnCount;
		text = tmp.jsonText;
		console.log("text:" + text);
		console.log("!!!ifx.RIMTESTNO:" + ifx.RIMTESTNO);
		if (ifx.RIMTESTNO) {
			interval_chk = false;
			throw ifx_makeFvalError("上送中心X型態不能包含中文");
		}

		function onError(oTota, type) {
			resetCached(source, rimName);
			if (fnError && $.isFunction(fnError)) {
				var errmsg = "";
				if (oTota.host == "0") {
					errmsg = oTota.getErrmsg();
				} else {
					errmsg = oTota.message;
				}
				fnError(errmsg, type);
			}
			if (swiftMode) {
				interval_chk = false;
				return;
			}
			if (!tabStop) {
				console.log("not a tabstop field, goback previous tabable field");
				ifx.KeyBackward();
			} else {
				console.log("a tabstop field, stay here  ({})", source);
			}
			interval_chk = false;
		}

		function onSuccess(oTota) {
			// if(oTota.host=="0") { // from host (cobol)
			if ($.isArray(oTota)) {
				self.rom = oTota; // oTota is an Array of Tota //oTota.text;
				self.romTota = oTota; // for form mode
			} else { // host==1 local rim
				self.rom = oTota.message;
				self.romTota = oTota;
			}
			// 柯 新增 for K(Z)
			if (fnRimParse != "") fnRimParse(self);
			storeCached(source, rimName, text);
			if (fnOK && $.isFunction(fnOK)) {
				fnOK();
			}
			if (swiftMode) {
				interval_chk = false;
				return;
			}
			if (!tabStop) {
				console.log('non tabstop, go next field');
				setTimeout(function () {
					ifx.KeyForward('S() success');
				}, 0);
			} else {
				if (rtnMode == "post" && ifx.checkerrmsg()) { // 柯 檢查有無顯示錯誤訊息. for 自動重複執行的rim
					// 自動跳欄位問題
					console.log("post filed RIM, go next field");
					setTimeout(function () {
						ifx.KeyForward('S() success', true);
					}, 0);
				} else {
					console.log("stay at currentfield");
				}
			}
			interval_chk = false;
		}
		// 柯 swift的調rim需resetCached，不然會有延遲且衍生部分問題
		// Swift.validators 欄位20
		var fkey = ifx.getValue("FKEY$");
		if (source.slice(0, 9) == "#swift_fn") {
			resetCached(source, rimName);
		}
		if (isSameRim(source, rimName, text)) {
			console.log("RIM " + source + "." + rimName + " sent before, don't send");
			return;
		} else {
			console.log("RIM " + source + "." + rimName + " not sent before, send first time");
		}
		if (hostType == "WEBDB") {
			interval_chk = true;
			ifx.rimSent = true;
			var localRim = $.post(_contextPath + '/mvc/hnd/rim/' + rimName + "/" + text); // 柯:
			// 如果用rim有時會錯?
			// 不懂...
			localRim.done(function (x) {
				interval_chk = false;
				if (x.retval) {
					onSuccess(x);
					if (poperror != "0") {
						ifx.setValue(errtemp, 0); // 回傳成功與否? 0回傳成功
					}
				} else { // poperror 1 隱藏錯誤訊息 0 顯示錯誤訊息
					if (poperror != "1") {
						alert(x.message);
						onError(x);
					} else { // 回傳成功與否? 1回傳失敗
						resetCached(source, rimName);
						alert(x.message);
						ifx.setValue(errtemp, 1);
						setTimeout(function () {
							ifx.KeyForward('S() error but go', true);
						}, 0);
					}
				}
			});
			localRim.fail(function (x) {
				interval_chk = false;
				alert('傳送失敗:' + x + '\n請稍後再試!');
			});
		} else if (hostType == "ADSDB") { // rimName = "ADUSERNAME" ,text=AD帳號
			// S(ADUSERNAME,ADSDB,#setadname,ADID)
			ifx.rimSent = true;
			text = text.trim();
			console.log("AD帳號:" + text);
			interval_chk = true;
			if (text.indexOf(".") > 0) {
				var addbRim = $.post(_contextPath + '/mvc/hnd/adsDB/' + rimName + "/" + text + ".");
			} else {
				var addbRim = $.post(_contextPath + '/mvc/hnd/adsDB/' + rimName + "/" + text);
			}
			addbRim.done(function (x) {
				interval_chk = false;
				console.log("AD帳號  x.code:" + x.code + ",x.description:" + x.description + ",usdesc:" + x.usdesc);
				if (x.code == "0") {
					onSuccess(x);
					ifx.setValue(setadname, x.usdesc); // 回傳成功與否? 0回傳成功
					// if(x[text]){
					// onSuccess(x);
					// ifx.setValue(setadname,x[text]); //回傳成功與否? 0回傳成功
					// }else { //poperror 1 隱藏錯誤訊息 0 顯示錯誤訊息
					// //resetCached(source,rimName);
					// alert("查無此AD帳號名稱");
					// ifx.setValue(setadname,"");
					// setTimeout(function() {
					// ifx.KeyForward('S() error but go', true);
					// }, 0);
					// }
				} else {
					alert(x.code ? x.description : "查無此AD帳號名稱");
					onError(x);
				}
			});
			addbRim.fail(function (x) {
				interval_chk = false;
				alert('傳送失敗:' + x + '\n請稍後再試!');
			});
		} else {
			// 柯 增第5個參數 傳入time out 時間
			interval_chk = true;
			ifx.sendRim(rimName, text, onSuccess, onError, timeouttime, bMq);
		}
	};
	var rimCached = {};

	function isSameRim(currFld, rimName, text) {
		var k = currFld + "." + rimName;
		var lastTimeText = rimCached[k];
		return lastTimeText !== undefined && lastTimeText === text;
	}

	function storeCached(currFld, rimName, text) {
		console.log("store_Cached:" + currFld + "," + rimName + "," + text);
		if (rimName == "XXR22") {
			console.log("不暫存XXR22快取!");
			return;
		}
		var k = currFld + "." + rimName;
		rimCached[k] = text;
	}

	function resetCached(currFld, rimName) {
		var k = currFld + "." + rimName;
		delete rimCached[k];
	}
	Rtn.prototype.GETRIMCACHED = function () {
		console.dir(rimCached);
		console.log("rimCached" + rimCached);
		return rimCached;
	};
	// 解析儲存的rim tita
	Rtn.prototype.SETRIMCACHED = function () {
		var args = aps.call(arguments);
		var cachetext = args.shift();
		console.log("testted" + cachetext);
		rimCached = JSON.parse(cachetext);
		console.dir(rimCached);
	};
	// Rtn.prototype.S2 = function() {
	//
	// var args = aps.call(arguments);
	// var source = args.shift();
	// console.log("S() for " + source);
	// var tabStop = ifx.getField(source).isTabStop();
	//
	//
	// var rimName = args.shift();
	// var romFields = parseInt(args.shift(),10);
	// var fnRimParse = args.pop();
	// var text="";
	// var n;
	// for(var i=0; i < args.length; i++) {
	// n = args[i];
	// if(n.charAt(0) == "#") text += ifx.getField(n).toHost();
	// else text += n.toString();
	// }
	// console.log("text:"+text);
	// var self = this;
	// ifx.rimSent = true;
	//
	//
	// blockIt();
	// ifx.sendRim(rimName, text, function(totaList) {
	//
	// unBlock();
	// var list = [];
	// for(var i=0; i < totaList.length; i++) {
	// var totaObj = totaList[i];
	// if(totaObj.isError()) {
	// alert("錯誤訊息:"+totaObj.getErrmsg());
	// if(!tabStop) {
	// console.log("not a tabstop field, goback previous tabable field");
	// ifx.KeyBackward();
	// }else {
	// console.log("a tabstop field, stay here");
	// }
	// return;
	// }
	// if(totaObj.isWarnning()) {
	// alert("警告訊息:" + totaObj.getErrmsg());
	// continue;
	// }
	// list.push(totaObj);
	// }
	// console.log("good");
	// console.log("##### rom:"+list[0].text);
	// self.rom = list[0].text;
	// fnRimParse(self);
	// ifx.KeyForward();
	//
	// });
	//
	//
	// };
	Rtn.prototype.skip = function () {
		var args = aps.call(arguments),
			target = args.shift(),
			action = args.shift();
		console.log("x.Rtn, target:" + target + ", action:" + action);
		switch (action) {
			case "S":
				var fld = ifx.getField(target);
				fld.setValue(fld.getDefaultValue());
				if (fld.type == "D" && fld.len >= 7)
					$('#fld_' + fld.name.substring(1)).datepicker('disable');
				throw ifx_makeSkip();
			// **** fall through
			case "s":
				var fld = ifx.getField(target);
				if (fld.type == "D" && fld.len >= 7)
					$('#fld_' + fld.name.substring(1)).datepicker('disable');
				throw ifx_makeSkip();
				break;
			case "$":
				// nop
				break;
			case "O":
				ifx.getField(target).setEntryMode("O");
				break;
			case "I":
			case "H":
				console.log("nop");
				break;
			default:
				throw "unknow skip action:" + action;
		}
	};
	Rtn.prototype.T = function () {
		var args = aps.call(arguments),
			target = args.shift(),
			opt = args.shift(),
			value;
		var exp, ss, source, offset, len;
		var fields;
		var swidth, padding;
		TRACE("T() fldname:" + target + ",opt:" + opt);
		switch (opt) {
			case "1":
			case "1TOSET":
				if (args.length > (opt == '1TOSET' ? 4 : 3)) {
					target = args.shift();
				}
				source = args.shift();
				offset = parseInt(easyValue(args.shift()), 10);
				// 鍾 T function 數值變數一律先補0,再取值
				len = parseInt(easyValueT(args.shift()), 10);
				// end
				// 應該只有TXRP9使用此功能
				if (opt == '1TOSET') {
					var toset = parseInt(easyValue(args.shift()), 10) - 1; // 插入原欄位位置 起始位為1,故減1
					var targetv = ifx.getValue(target);
					var targetvlen = targetv.slice(0, toset).length;
					var targetvreadlen = targetv.slice(0, toset).replace(/[^\x00-\xff]/g, "**").length;
					var realdata = ifx.getField(source).substing(offset - 1, len);
					var reallen = realdata.length;
					// var real2len = realdata.replace(/[^\x00-\xff]/g,"**").length; //好像用不到
					var tosetdata, tosetdata1, tosetdata2, tosetdata3;
					tosetdata1 = targetv.slice(0, toset - (targetvreadlen - targetvlen));
					tosetdata2 = realdata;
					tosetdata3 = targetv.slice(toset - (targetvreadlen - targetvlen) + reallen);
					tosetdata = tosetdata1 + tosetdata2 + tosetdata3;
					// 為了對齊,只能補兩格空白 FOR中文欄位 (TXRP9)
					tosetdata = tosetdata.replace(/\4/g, '').replace(/\7/g, '  ');
					console.log("after //4->'' , //7->'  ' ");
					ifx.setValue(target, IfxUtl.stringFormatter(tosetdata, ifx.getField(target).len));
				} else {
					if (ifx.getField(target).dlen > 0) {
						var dlen = ifx.getField(target).dlen;
						var bAmt = ifx.getField(source).substing(offset - 1, len).substring(0, len - dlen);
						var aAmt = ifx.getField(source).substing(offset - 1, len).slice(dlen * -1);
						ifx.setValue(target, bAmt + "." + aAmt);
					} else
						ifx.setValue(target, ifx.getField(source).substing(offset - 1, len));
				}
				break;
			case "2BIG":
			case "2":
			case "2CLN":
				if (args.length > 1) {
					target = args.shift();
				}
				exp = args.shift();
				if (exp == '+') {
					value = '+';
					ifx.setValue(target, IfxUtl.stringFormatter(value, ifx.getField(target).len));
					return;
				}
				ss = exp.split('+');
				value = "";
				// 鍾 T function 數值變數一律先補0,再取值
				for (var i = 0; i < ss.length; i++) value += easyValueT(ss[i]);
				// end
				value = (opt == '2BIG') ? IfxUtl.halfToFull(value) : value; // 柯 轉換全形大寫
				value = (opt == '2CLN') ? value.replace(/,/g, "") : value; // 柯
				// 去除因讀取TXT/EXL檔而有逗號的問題
				ifx.setValue(target, IfxUtl.stringFormatter(value, ifx.getField(target).len));
				break;
			case "3":
				if (args.length > 1) target = args.shift();
				ifx.setValue(target, IfxUtl.stringFormatter(args.shift(), ifx.getField(target).len));
				TRACE("T3 target fld:" + target + ", value " + ifx.getValue(target));
				break;
			case "4":
				if (args.length > 1) target = args.shift();
				ifx.copyTo(args.shift(), target);
				TRACE("v2:" + ifx.getValue(target));
				break;
			case "6":
			case "6A":
				if (args.length > 1) {
					target = args.shift();
				}
				exp = args.shift();
				if (exp == '+') {
					value = '+';
					ifx.setValue(target, IfxUtl.stringFormatter(value, ifx.getField(target).len));
					return;
				}
				if (opt == "6") {
					exp = "#ERIM+" + exp + "+";
					//					ifx.escRim = exp.split('+');
					ifx.setEscRim(exp.split('+'));
				} else {
					value = "";
					var ss = exp.split('+');
					for (var i = 0; i < ss.length; i++) {
						if (ss[i].substr(0, 1) != "#") value += ('"RimText' + i + '":"' + ifx.getValue(ss[i]) + '",');
						else value += ('"' + ss[i].substr(1) + '":"' + ifx.getValue(ss[i]) + '",');
					}
					value = value.substr(0, value.length - 1) + "}";
					ifx.setValue(target, IfxUtl.stringFormatter(value, ifx.getField(target).len));
				}
				// value = buildText(ss);
				// ifx.setValue(target, IfxUtl.stringFormatter(value, ifx.getField(target).len));
				break;
			case "6B":
				if (args.length > 1) target = args.shift();
				exp = args.shift();
				if (exp == '+') {
					value = '+';
					ifx.setValue(target, IfxUtl.stringFormatter(value, ifx.getField(target).len));
					return;
				}
				exp = "#ERIM+" + exp + "+";
				ifx.setValue(target, IfxUtl.stringFormatter(exp, ifx.getField(target).len));
				break;
			case "A":
				if (args[0][0] == "@") {
					target = args.shift();
					target[0] = "#";
				}
				var targetfld = ifx.getField(target);
				var o;
				var textType = false;
				var fldLen = 0;
				var compareField = args.shift();
				var compareTo = ifx.getValue(compareField);
				if (compareField[0] == '#') {
					textType = ifx.getField(compareField).canContainBig5();
					if (textType) fldLen = ifx.getField(compareField).getDisplayLength();
				}
				// 鍾 T function(A) 參數都可用變數代入
				var filename = easyValue(args.shift());
				var segment = easyValue(args.shift());
				var valueTag = easyValue(args.shift());
				var labelTag = easyValue(args.shift());
				var moreCode = easyValue(args.shift() || '');
				// end
				var table = ifx.help.getLookupTable(filename, segment, valueTag, labelTag, moreCode);
				console.dir(table);
				var table2 = {};
				var tmpV = null;
				if (textType) {
					compareTo = IfxUtl.stringFormatterBig5(compareTo, fldLen);
					for (var k in table) {
						if (k.length < fldLen) {
							tmpV = table[k];
							k = IfxUtl.stringFormatterBig5(k, fldLen);
							table2[k] = tmpV;
						} else {
							table2[k] = table[k];
						}
					}
					table = table2;
				}
				console.log("lookup table:" + JSON.stringify(table));
				if (compareTo in table) {
					if (table[compareTo]) { // 小柯 20150428 新增 如沒有值則給空白
						// 新增型態並直接截斷
						if (targetfld.type.toLowerCase() == "c") {
							o = IfxUtl.checkChinese(table[compareTo], targetfld.skipln || targetfld.len);
							ifx.setValue(target, o.cut);
						} else {
							ifx.setValue(target, table[compareTo]);
						}
					} else {
						ifx.setValue(target, ""); // undefined
					}
				} else {
					ifx.setValue(target, ""); // 小柯 20140808 新增 如找無應該需要自動空白
					console.log("value [" + compareTo + "] is not in lookup table");
				}
				console.log("lookup table:" + JSON.stringify(table));
				break;
			// 鍾 新增T(B) 同 T(2),但變數會先去空白再相加
			// \\n 取代成 換行記號
			case "B":
				if (args.length > 1) target = args.shift();
				ss = args.shift().split('+');
				value = "";
				tmpvalue = "";
				var tblen = 0,
					tbchar = "";
				for (var i = 0; i < ss.length; i++) {
					// 特別先判斷一次因怕 變數也是下方符號開頭結尾
					if (ss[i].charAt(0) != "#" && ss[i].slice(-1) == "]") {
						tmpvalue = ss[i];
						if (tmpvalue.slice(0, 2) == "X[") {
							tbchar = " ";
						} else if (tmpvalue.slice(0, 2) == "9[") {
							tbchar = "0";
						}
						if (tbchar != "") {
							tblen = parseInt(tmpvalue.slice(2, tmpvalue.length - 1), 10);
							tmpvalue = "";
							for (var j = 0; j < tblen; j++) {
								tmpvalue += tbchar;
							}
						}
					} else {
						tmpvalue = easyValueT(ss[i]).trim();
					}
					value += tmpvalue;
				}
				ifx.setValue(target, IfxUtl.stringFormatter(value, ifx.getField(target).len).replace(/\\n/g, '\n')); // 取代換行
				break;
			case "F":
				if (args.length > 1) target = args.shift();
				value = easyValueT(args.shift());
				if (value == '0') $("#fld_" + target.substring(1)).addClass('noBorder');
				if (value == '1') $("#fld_" + target.substring(1)).removeClass('noBorder');
				break;
			case "LABEL":
				if (args.length > 1) target = args.shift();
				var txcdname = easyValue(args.shift());
				ifx.setValue(target, ifx.ifxHost.buildTitaLabelForRim(txcdname));
				console.log("build TITA Label:" + ifx.ifxHost.buildTitaLabelForRim(txcdname));
				TRACE("TLABEL target fld:" + target + ", value " + ifx.getValue(target));
				break;
			case "SCENTER":
			case "SRIGHT":
			case "SLEFT":
				if (args.length > 2) {
					target = args.shift();
				}
				swidth = ifx.getField(target).len; // center基準長度 ?
				padding = args.shift(); // 補滿的字串 只取一位 預設" "
				exp = args.shift();
				ss = exp.split('+');
				value = "";
				// 鍾 T function 數值變數一律先補0,再取值
				for (var i = 0; i < ss.length; i++) value += easyValueT(ss[i]);
				// end
				if (opt == "SCENTER") {
					value = IfxUtl.stringToCenter(IfxUtl.trim(value), swidth, padding); // 柯
					// 轉換成center字串
				} else if (opt == "SRIGHT") {
					value = IfxUtl.stringToRight(IfxUtl.trim(value), swidth, padding); // 柯
					// 轉換成right字串
				} else if (opt == "SLEFT") {
					value = IfxUtl.stringToLeft(IfxUtl.trim(value), swidth, padding); // 柯
					// 轉換成left字串
				}
				ifx.setValue(target, value);
				break;
			case "Z1":
				if (args.length % 3 != 0) target = args.shift();
				value = "";
				var nv, start, lenToGet;
				for (var i = 0; i < args.length; i += 3) {
					// 鍾 T function 數值變數一律先補0,再取值
					nv = easyValueT(args[i]);
					// end
					len = IfxUtl.strlen(nv);
					start = args[i + 1] == "_" ? 1 : parseInt(easyValue(args[i + 1]), 10);
					start -= 1;
					lenToGet = args[i + 2] == "_" ? len : parseInt(easyValue(args[i + 2]), 10);
					if ((start + lenToGet) > len) lenToGet = len;
					value += IfxUtl.substr_big5(nv, start, lenToGet);
				}
				ifx.setValue(target, value);
				break;
			case "H":
				if (args.length > 2) target = args.shift();
				console.log(" length " + args.length);
				var compareTo = ifx.getValue(args.shift());
				compareTo = IfxUtl.trim(compareTo);
				console.log(" compareto " + compareTo);
				var table = {},
					ss;
				var tmp = easyValue(args.shift());
				console.log(" tmp " + tmp);
				$.each(tmp.split(';'), function (i, x) {
					ss = x.split(':');
					table[ss[0].trim()] = ss[1];
				});
				console.log("lookup table:" + JSON.stringify(table));
				if (compareTo in table) {
					ifx.setValue(target, table[compareTo]);
				}
				console.log("value [" + compareTo + " is not in lookup table");
				console.log("lookup table:" + JSON.stringify(table));
				break;
			case 'UPPERCASE':
			case 'lowercase':
				switch (args.length) {
					case 0:
						source = target;
						break;
					case 1:
						source = args.shift();
						break;
					case 2:
						target = args.shift();
						source = args.shift();
						break;
					default:
						console.error('too many parameters');
						return;
				}
				value = ifx.getValue(source);
				value = (opt == 'UPPERCASE') ? value.toUpperCase() : value.toLowerCase();
				ifx.setValue(target, value);
				break;
			case "R":
				source = args.shift();
				exp = args.shift();
				ss = exp.split('+');
				fields = $.map(ss, function (x) {
					return ifx.getField(x);
				});
				ifx.ifxHost.parseTmp(easyValue(source), fields);
				break;
			case "S":
				if (args.length > 1) target = args.shift();
				var tmpDecimal = ifx.getValue(args.shift());
				var negative = ['}', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'];
				var positive = ['{', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'];
				var negindex = negative.indexOf(tmpDecimal.slice(-1));
				var posindex = positive.indexOf(tmpDecimal.slice(-1));
				if (negindex != -1) {
					tmpDecimal = "-" + tmpDecimal.slice(0, -1) + negindex;
				} else if (posindex != -1) {
					tmpDecimal = "+" + tmpDecimal.slice(0, -1) + posindex;
				}
				ifx.setValue(target, tmpDecimal);
				TRACE("T(S):" + ifx.getValue(target));
				break;
			case "HELP":
				if (args.length > 3) target = args.shift();
				var orgContent = easyValue(args.shift()).split(";");
				var posOrNav = easyValue(args.shift());
				var content = easyValue(args.shift()).split(";");
				for (var i = 0; i < orgContent.length; i++) {
					var isOn = false;
					content.forEach((x) => {
						if (x == orgContent[i].split(":")[0]) isOn = true;
					});
					if (posOrNav == "+") {
						if (!isOn) orgContent[i] = "";
					} else {
						if (isOn) orgContent[i] = "";
					}
				}
				content = "";
				for (var i = 0; i < orgContent.length; i++)
					if (orgContent[i].length > 1) content += orgContent[i] + ";";
				content = content.replace(/;+$/g, "");
				ifx.setValue(target, content);
				break;
			default:
				throw "don't know T:" + opt;
		}
	};
	Rtn.prototype.K = function () {
		var args = aps.call(arguments);
		var target = args.shift(); // ignore field name
		var opt = args.shift(); // ifx.getField(args.shift());
		var fn;
		var startttmp, fn1, fn2, timeoutk = 20;
		// if(errmsg!=null) args.pop();
		console.log("K " + opt + " .");
		switch (opt) {
			case "LIGHT":
				var field = args.shift();
				var fldstatus = easyValue(args.shift());
				var timeout = parseInt(easyValue(args.shift()), 10);
				var blink = args.shift();
				IfxUtl.LightField(field, fldstatus, timeout, blink); // 欄位,顏色,秒數
				break;
			case "Z": // 柯 測試 循環檢查
				startttmp = (args.shift())();
				if (startttmp == "1") {
					StoptestFunction();
				} else {
					fn1 = args.shift(); // 有更新
					fn2 = args.shift(); // 沒更新
					timeoutk = parseInt((args.shift())(), 10); // 秒數
					console.log("timeoutk->" + timeoutk);
					StarttestFunction(); // 第一次做
					// setInterval() 方法可按照指定的周期（以毫秒计）来调用函数或计算表达式。潘
					testtmpke = setInterval(function () {
						StarttestFunction(); // 後續使用20秒一次
					}, timeoutk * 10000);
				}
				break;
			case "NOBUTTON": // 柯 測試 關閉 BUTTON
				var showtype = easyValue(args.shift()); // 關閉 顯示
				if (showtype == "CLOSE") {
					$("#buttonsArea").hide();
				} else if (showtype == "CLOSEY")
					$('#btn_yn').hide();
				else if (showtype == "SHOW") {
					$("#buttonsArea").show();
				} else if (showtype == "SHOWY")
					$('#btn_yn').show();
				if (showtype == "NOSEND") {
					// 為了要可以FOCUS在此按鈕上，故假裝成離開按鈕，並隱藏[儲存]與[離開]按鈕
					// 此功能沒有用到
					$('#btn_yn').attr("value", "離開");
					$('#xbtn_cancel').off("click").hide();
					$('#xbtn_save').off("click").hide();
					$('#btn_yn').off("mousedown").on('mousedown', function () {
						ifx.KeysEscapeTran();
					});
				} else if (showtype == "SAVE") {
					$('#xbtn_save').off("click").hide();
				}
				break;
			case "MODIFYBUTTON":
				var value, btnIndex;
				if (args.length == 1) {
					value = easyValue(args.shift());
					if (value == "acsave") $("#xbtn_save").show();
					else $("#btn_yn").attr("value", value);
				} else {
					btnIndex = easyValue(args.shift());
					value = easyValue(args.shift());
					$("#btn_new" + btnIndex).show();
					$("#btn_new" + btnIndex).attr("value", value);
				}
				break;
			case "PSBACC": // 柯 測試 讀取磁條
				var psbkprompt = easyValue(args.shift()); // 顯示紙張?
				var noeject = easyValue(args.shift()); // 是否退紙
				var setfld = args; // 給值欄位
				// 新增是否退紙的功能
				if (noeject.toLowerCase() == "true") {
					noeject = true;
				} else {
					noeject = false;
				}
				// alert("psbkprompt:"+psbkprompt+",setfld:"+setfld);
				var useFakePB = false;
				if (!useFakePB) {
					if (pppsbk == null) {
						pppsbk = new ifxPsbk(ifx, '_psbk_fyi_get_dialog');
						pppsbk.useTruePrinter();
					}
					setTimeout(function () {
						ifx.keys.unbindAll(); // 柯 bind 不然會可以一直輸入
						pppsbk.getBlack(psbkprompt, setfld, noeject); // noeject?
					}, 100);
				}
				// ifx.showPrompt(psbkprompt,5);
				break;
			case "PSBACCR":
				var deviceX = ifx.getifxDevice();
				deviceX.returnSession();
				break;
			case "GOFLD": // 柯 測試 跳回欄位
				var gofiled = args.shift(); // 關閉 顯示
				if (gofiled) {
					if (gofiled.charAt(0) === "#") {
						gofiled = gofiled.slice(1);
					}
					var targerid = $("#fld_" + gofiled).attr('dc-index');
					ifx.jumptoBack2(targerid, true);
					console.log("go to by k:[GOFLD]!");
				}
				break;
			case "SPECIAL": // 柯 小鍵盤密碼
				var spopt = args.shift();
				switch (spopt) {
					case "PINPAD":
						var pwfiled = args.shift();
						var deviceX = ifx.getifxDevice();
						ifx.block("<h2>等待使用者輸入密碼....<h2>");
						deviceX.pinpad.init();
						console.log("密碼輸入器功能初始化成功！");
						console.log("密碼輸入器1秒後 Read開始！");
						setTimeout(function () {
							var pindata = deviceX.pinpad.read();
							if (pindata != null) {
								console.log("密碼輸入器Read成功！");
								ifx.setValue(pwfiled, pindata);
							} else {
								console.log("密碼輸入器read內容失敗 or Esc!");
							}
							ifx.KeyForward('PINPAD skip!'); // 無論如何都下一個欄位
							ifx.unblock();
						}, 1200);
						break;
					default:
						break;
				}
				break;
			// K(NEXTNOCLOSE,true) 原交易固定加入
			// K(CLOSESHOW,#???,true) 被顯示的交易
			case "CLOSESHOW":
				var showerror = easyValue(args.shift()); // 關閉視窗時顯示文字
				var canclose = easyValue(args.shift()).toUpperCase() == "TRUE" ? true : false;
				ifx.closeTabText(showerror, canclose);
				break;
			case "NEXTNOCLOSE":
				var nextnoclose = easyValue(args.shift()).toUpperCase() == "TRUE" ? true : false;
				ifx.nextNoClose(nextnoclose);
				break;
			case "RSWIFTOVER":
				ifx.block("讀取swift over 檔案.");
				// #中鋼網銀電文超長時使用 d:\ifxwriter\repos\swit\I201600004.txt
				var filename = easyValue(args.shift()).trim();
				var post_data = {
					"cmd": "RSWIFTOVER",
					"filename": filename
				};
				var set_fld = args.shift();
				$.ajax({
					type: 'POST',
					url: "savedoc.jsp",
					data: post_data,
					dataType: "text",
					async: false,
					success: function (data) {
						ifx.unblock();
						console.log(data);
						// 去除不知名開頭空行
						while (data.slice(0, 2) == "\r\n") {
							data = data.replace(/^\r\n/, "");
						}
						// 補前後給swift-print 切
						data = "}{4:\r\n" + data + "-}";
						ifx.setValue(set_fld, data);
					},
					error: function (data) {
						ifx.unblock();
						alert("Send Error!!" + data);
					}
				});
				break;
			case "PLIST":
				if (args.length > 1)
					target = args.shift();
				$.ajax({
					type: 'POST',
					url: "http://" + easyValue(args.shift()).replace(/\s+/, "") + ":8090/St1Printer/",
					data: "[{\"Action\": 1}]",
					dataType: "json",
					async: false,
					cache: false,
					success: function (data) {
						if (data.Result == "S")
							;
						else {
							alert("Catch Printer List Error : " + data.Result + " " + data.resultDesc);
							return;
						}
						var printerList = "";
						data.PrinterList.forEach((x, i) => {
							printerList += x.PrinterName + ": ;";
						});
						ifx.setValue(target, printerList.slice(0, -1));
					},
					error: function (e) {
						alert("Printer Agent Connect Error!!");
						return;
					}
				});
				break;
			default:
				alert("dont know K:" + opt);
		}
		// *******K(Z) 使用函式 START
		function isChanged(datatwenty) {
			if (lastData == null) {
				return true;
			}
			// 哪一個比較快?
			if (lastData != datatwenty) {
				return true;
			}
			// 哪一個比較快?
			// for(var i=19; i >= 0 ; i--) {
			// if(lastData[i] != data[i])
			// return true;
			// }
			return false;
		}

		function StoptestFunction() {
			if (testtmpke != "") {
				clearInterval(testtmpke);
			}
			testtmpke = "";
		}
		// 已統一不快取
		var candoExchrate; // 是否已完成該次作業
		var checkActivefnn;
		// SessionExpirationFilter 有判斷 EXCHRATE/ALL過濾TIMEOUT檢查
		function StarttestFunction() {
			if (!checkActivefnn) {
				checkActivefnn = window.parent["checkActive"];
			}
			if (!checkActivefnn("XG910 ")) {
				// StoptestFunction();
				// console.log("XG910 Not Active Tab ,return!");
				return;
			}
			if (candoExchrate == false) {
				console.log("The ajax not come back! next try!");
				return;
			}
			var url = _contextPath + "/mvc/hnd/rim/EXCHRATE/ALL"; // 參數化可能無意義 rimNEW=不調快取
			candoExchrate = false;
			var g = $.get(url);
			var datamesg, datamesgty; // data.message //data.message.slice(0,20)
			g.done(function (data) {
				candoExchrate = true;
				datamesg = data.message;
				datamesgty = data.message.slice(0, 20);
				if (!datamesg || datamesg.length == 0) {
					alert("沒有任何匯率，請洽資訊處。");
				} else if (data.retval) {
					if (isChanged(datamesgty)) { // data.message.toString()
						lastData = datamesgty;
						if (fn1) fn1(this); // 柯 有新匯率時，實作fn1
					} else {
						if (fn2) fn2(this); // 柯 沒有新匯率時，實作fn2
					}
				}
			});
			g.fail(function (xhr, textStatus, errorThrown) {
				alert("更新匯率失敗，請洽資訊處。" + xhr);
				alert("textStatus:" + textStatus);
				console.log("更新匯率失敗，請洽資訊處。" + xhr);
				console.dir(xhr);
				alert("通訊錯誤! status:" + xhr.status + ",readyState:" + xhr.readyState + ",statusText:" + xhr.statusText + ",responseText:" + xhr.responseText);
				candoExchrate = true;
			});
		}
		// *******K(Z) 使用函式 END
	};
	Rtn.prototype.V = function () {
		var args = aps.call(arguments),
			smileArgs = splitSmile(args),
			target = args.shift(),
			value = "" + ifx.getValue(target), // conver to
			// string
			opt = args.shift(),
			fromValue, toValue, errmsg, fn; // = isSmile(args);
		// if(errmsg!=null) args.pop();
		console.log("V" + opt + " for " + target);
		switch (opt) {
			case "0":
				var timeouttime = parseInt(args.shift());
				console.log("V:0:秒數->" + timeouttime);

				function foo() {
					console.log("ifx.timeoutt()::" + ifx.timeoutt());
					if (ifx.timeoutt() != 0) {
						alert("通訊錯誤:超過 time out 時間。" + timeouttime);
						ifx.timeoutt(targerid, true);
						ifx.timeoutt(0);
					}
				}
				if (timeouttime > 0) {
					var targerid = $("[id^='fld_" + target.slice(1) + "']").attr('dc-index');
					console.log(" $(this).attr('dc-index')=" + $("[id^='fld_" + target.slice(1) + "']").attr('dc-index'));
					if (ifx.timeoutt() == 0) {
						ifx.timeoutt(targerid);
						setTimeout(foo, timeouttime * 1000);
					}
				}
				break;
			case "1":
				if (!myInArrayNumeric(value, args)) {
					errmsg = formatError("EV1", smileArgs, {
						"codes": easyJoin(args)
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "2":
				if (myInArrayNumeric(value, args)) {
					errmsg = formatError("EV2", smileArgs, {
						"codes": easyJoin(args)
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "3":
				if (!myInArray(value, args)) {
					errmsg = formatError("EV3", smileArgs, {
						"codes": easyJoin(args)
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "4":
				if (myInArray(value, args)) {
					errmsg = formatError("EV4", smileArgs, {
						"codes": easyJoin(args)
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			// 柯 檢查特殊字串並帶參數 (暫時設置此4A)
			case "4A":
				if (myInArraySpecial(value, args)) {
					errmsg = formatError("EV4A", smileArgs, {
						"codes": easyJoinSpecial(args.splice(1))
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "5":
				value = parseFloat(value == "" ? 0 : value);
				var s = easyValue(args.shift());
				var e = easyValue(args.shift());
				fromValue = parseFloat(s == "" ? "0" : s);
				toValue = parseFloat(e == "" ? "0" : e);
				// var temp ;
				// if(fromValue > toValue){
				// temp = fromValue;
				// fromValue = toValue;
				// toValue = temp;
				//
				// }
				if (fromValue > toValue) {
					var valtemp = fromValue;
					fromValue = toValue;
					toValue = valtemp;
					console.log("fromValue > toValue,程式邏輯可能有誤,交換內容!");
				}
				console.log("v5, value:" + value + ",not over fromValue:" + fromValue + ",toValue:" + toValue + ",so error.");
				if (!(value >= fromValue && value <= toValue)) {
					errmsg = formatError("EV5", smileArgs, {
						from: fromValue,
						to: toValue
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "6":
				value = parseFloat(value);
				fromValue = parseFloat(easyValue(args.shift()));
				toValue = parseFloat(easyValue(args.shift()));
				// var temp ;
				// if(fromValue > toValue){
				// temp = fromValue;
				// fromValue = toValue;
				// toValue = temp;
				//
				// }
				if (fromValue > toValue) {
					var valtemp = fromValue;
					fromValue = toValue;
					toValue = valtemp;
					console.log("fromValue > toValue,程式邏輯可能有誤,交換內容!");
				}
				console.log("v6, value:" + value + ",over fromValue:" + fromValue + ",toValue:" + toValue + ",so error.");
				if ((value >= fromValue && value <= toValue)) {
					errmsg = formatError("EV6", smileArgs, {
						from: fromValue,
						to: toValue
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "7":
				// 鍾 V7 空白也要出錯誤
				var s = value.trim();
				if (s.length == 0) {
					// end
					errmsg = formatError("EV7", smileArgs);
					throw ifx_makeFvalError(errmsg);
				}
				break;
			// 增加測試長度限制
			// TODO: 中文的長度到底要判斷2還是1?
			case "8":
				var s = value.trim();
				fromLEN = parseInt(easyValue(args.shift()));
				toLEN = parseInt(easyValue(args.shift()));
				// alert("ssslen" + s.length +" fromLEN"+ fromLEN +" toLEN"+toLEN);
				if (s.length < fromLEN)
					errmsg = formatError("EV8A", smileArgs, {
						len: fromLEN,
					});
				if (s.length > toLEN)
					errmsg = formatError("EV8", smileArgs, {
						from: fromLEN,
						to: toLEN
					});
				if(errmsg)
					throw ifx_makeFvalError(errmsg);
				break;
			// alert("dont know V8");
			case "9":
				if (null == value.match(/^[0-9]+$/)) {
					errmsg = formatError("SE_NUMERIC", smileArgs);
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "9A":
				if (null != value.match(/^[0-9]+$/)) {
					errmsg = formatError("EV9", smileArgs);
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "A":
				value = parseFloat(value);
				fromValue = parseFloat(easyValue(args.shift()));
				difValue = parseFloat(easyValue(args.shift()));
				toValue = fromValue + difValue;
				fromValue = fromValue - difValue;
				console.log("va, value:" + value);
				if (!(value >= fromValue && value <= toValue)) {
					errmsg = formatError("EV5", smileArgs, {
						from: fromValue,
						to: toValue
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			// 潘 新增欄位檢核
			case "B":
				var s = value.trim();
				if (s.match(/[;@#\$%\^&\*\!~|<>\[\]\"\=\t\f]/)) {
					errmsg = formatError("EVB", smileArgs);
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "C": // 全形半形擇一輸入
				var len = value.length;
				var checklen = value.replace(/[^\x00-\xff]/g, "**").length;
				if (len != checklen && len != checklen / 2) {
					errmsg = formatError("EVC", smileArgs);
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "E":
				var inRangeCheck = args.shift();
				var moreKey = null;
				var bMatchBegind = null;
				// var arr = ifx.help.getLookupArray(parseInt(args.shift(),10),
				// args.shift(), args.shift());
				// 鍾 V(E) 參數都可用變數代入
				var filename = easyValue(args.shift());
				var segment = easyValue(args.shift());
				if (args.length > 0) {
					moreKey = easyValue(args.shift());
				}
				if (args.length > 0) { // 篩選
					bMatchBegind = easyValue(args.shift()).split(":");
				}
				var arr = ifx.help.getLookupArray(filename, segment, moreKey, bMatchBegind);
				// end
				console.dir(arr);
				var bIn = myInArray(value, arr);
				var bValid = true;
				if (inRangeCheck == "0") {
					if (!bIn) bValid = false;
				} else {
					if (bIn) bValid = false;
				}
				if (!bValid) {
					console.log("BAD!! [" + value + "] is " + (inRangeCheck == "0" ? "not" : "") + " in " + arr.join());
					errmsg = formatError("EVE" + "_" + inRangeCheck, smileArgs, {
						"codes": arr.join()
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "L":
				//限輸入英文字母
				var s = value.trim();
				if (s.replace(/[a-zA-Z]/g, "").trim().length > 0) {
					errmsg = formatError("EVL", smileArgs);
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "NL":
				//限輸入數字或英文字母
				var s = value.trim();
				if (s.replace(/[a-zA-Z0-9]/g, "").trim().length > 0) {
					errmsg = formatError("EVNL", smileArgs);
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "H":
				var targs = [];
				var tmp = easyValue(args[0]),
					ss;
				$.each(tmp.split(';'), function (i, x) {
					ss = x.split(':');
					// 鍾,
					// targs.push(ss[0].trim());
					if (ss.length < 3) {
						targs.push(ss[0].trim());
					} else {
						if (ss[2].trim() != "N") {
							targs.push(ss[0].trim());
						}
					}
					//end
				});
				if (!myInArray(value, targs)) {
					errmsg = formatError("EV3", smileArgs, {
						"codes": easyJoin(targs)
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "P":
				var SHOW = easyValue(args.shift());
				throw ifx_makeFvalError(SHOW);
				break;
			case "Q":
				var SHOW = "";
				for (var i = 0; i < args.length; i++) SHOW += easyValue(args[i]) + '\n';
				var tmp = false;
				if (confirm(SHOW)) {
					tmp = true;
				}
				if (!tmp) {
					throw ifx_makeFvalError(IfxUtl.formatError(SHOW));
				}
				break;
			case "T":
				if (value.trim().match(/[\u4E00-\u9FA5]/g)) {
					throw ifx_makeFvalError("請勿輸入中文!");
				}
				break;
			case "5M": // TODO 需求確認後改寫法並合併6M
				fromValue = easyValue(args.shift());
				toValue = easyValue(args.shift());
				var tempint1 = 0;
				for (var i = 0; i < value.length; i++) {
					tempint1 += value.charCodeAt(i) * IfxUtl.hundredFormatter(value.length * (value.length - i));
				}
				var tempint2 = 0;
				for (var i = 0; i < fromValue.length; i++) {
					tempint2 += fromValue.charCodeAt(i) * IfxUtl.hundredFormatter(fromValue.length * (fromValue.length - i));
				}
				var tempint3 = 0;
				for (var i = 0; i < toValue.length; i++) {
					tempint3 += toValue.charCodeAt(i) * IfxUtl.hundredFormatter(toValue.length * (toValue.length - i));
				}
				console.log("vm, value:" + value + ",not over fromValue:" + fromValue + ",toValue:" + toValue + ",so error.");
				console.log("vm, value:" + tempint1 + ",not over fromValue:" + tempint2 + ",toValue:" + tempint3 + ",so error.");
				if (!(tempint1 >= tempint2 && tempint1 <= tempint3)) {
					errmsg = formatError("EV5", smileArgs, {
						from: fromValue,
						to: toValue
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			case "6M":
				fromValue = easyValue(args.shift());
				toValue = easyValue(args.shift());
				var tempint1 = 0;
				for (var i = 0; i < value.length; i++) {
					tempint1 += value.charCodeAt(i) * IfxUtl.hundredFormatter(value.length * (value.length - i));
				}
				var tempint2 = 0;
				for (var i = 0; i < fromValue.length; i++) {
					tempint2 += fromValue.charCodeAt(i) * IfxUtl.hundredFormatter(fromValue.length * (fromValue.length - i));
				}
				var tempint3 = 0;
				for (var i = 0; i < toValue.length; i++) {
					tempint3 += toValue.charCodeAt(i) * IfxUtl.hundredFormatter(toValue.length * (toValue.length - i));
				}
				console.log("vm, value:" + value + ",not over fromValue:" + fromValue + ",toValue:" + toValue + ",so error.");
				console.log("vm, value:" + tempint1 + ",not over fromValue:" + tempint2 + ",toValue:" + tempint3 + ",so error.");
				if ((tempint1 >= tempint2 && tempint1 <= tempint3)) {
					errmsg = formatError("EV6", smileArgs, {
						from: fromValue,
						to: toValue
					});
					throw ifx_makeFvalError(errmsg);
				}
				break;
			default:
				alert("dont know V:" + opt);
		}
	};
	Rtn.prototype.X = function () {
		var value, args = aps.call(arguments),
			target = args.shift();
		if (args[0].indexOf("@") != -1) target = args.shift();
		if (args.length == 1) {
			value = IfxUtl.moneyFormatter(ifx.getValue(args.shift()));
			ifx.setValue(target, value);
		} else if (args.length >= 2) {
			var maxComma = parseInt(args.shift(), 10);
			value = IfxUtl.moneyFormatter(ifx.getValue(args.shift()));
			var arr = value.split(",");
			if (arr.length > maxComma + 1) { // 2 commas three parts
				var ar1 = arr.splice(0, arr.length - (maxComma + 1)); // ar1
				// 為不需要comma部份
				value = ar1.join("") + arr.join(",");
			}
			if (args.length > 0) {
				value = easyValue(args.shift()) + value;
			}
			ifx.setValue(target, value);
		}
	};
	Rtn.prototype.CALL = function () {
		var self = this;
		var args = aps.call(arguments),
			triggerFld, it;
		var source = args.shift();
		self.origCaller = {
			source: source,
			mode: self.mode
		};
		console.log("call " + args[0]);
		triggerFld = ifx.getTrigger(args[0]);
		if (triggerFld == null) throw "Programmer BUG, no such trigger field:" + args[0];
		args.shift(); // shift triggerFld
		args = args.reverse();
		$.each(args, function (i, x) { // it1, it2,....
			if (i < 9) {
				it = "#it" + (args.length - i);
				console.log("call with " + it + ":" + x);
				if (x[0] == "@") {
					x = "_@@_reffield_@@_#" + x.slice(1);
					ifx.setValue(it, x);
				} else {
					ifx.setValue(it, easyValue(x));
				}
			}
		});
		var ret = ifx.executePrefRtn(triggerFld);
		delete self.origCaller;
		return ret;
	};
	Rtn.prototype.CALL2 = function () {
		var self = this;
		var args = aps.call(arguments),
			triggerFld, it;
		var source = args.shift();
		self.origCaller = {
			source: source,
			mode: self.mode
		};
		console.log("call2 " + args[0]);
		triggerFld = ifx.getTrigger(args[0]);
		if (triggerFld == null) throw "Programmer BUG, no such trigger field:" + args[0];
		args.shift(); // shift triggerFld
		args = args.reverse();
		$.each(args, function (i, x) { // x1, x2,....
			if (i < 9) {
				it = "#x" + (args.length - i);
				console.log("call2 with " + x + ":" + x);
				if (x[0] == "#") {
					ifx.getField(it).copyRef(ifx.getField(x), true);
				} else {
					ifx.setValue(it, easyValue(x));
				}
			}
		});
		var ret = ifx.executePrefRtn(triggerFld);
		delete self.origCaller;
		$.each(args, function (i, x) { // x1, x2,....
			if (i < 9) {
				it = "#x" + (args.length - i);
				console.log("restore call2 with " + x + ":" + x);
				if (x[0] == "#") {
					ifx.getField(it).copyRef(null);
				}
			}
		});
		return ret;
	};
	// 遵照原本的Rtn.prototype.CALL ,只是多加了calltimes
	Rtn.prototype.CALL3 = function () {
		var self = this;
		var args = aps.call(arguments),
			triggerFld, it, timefiled, calltimes;
		var source = args.shift();
		self.origCaller = {
			source: source,
			mode: self.mode
		};
		console.log("call " + args[0]);
		triggerFld = ifx.getTrigger(args[0]);
		if (triggerFld == null) throw "Programmer BUG, no such trigger field:" + args[0];
		args.shift(); // shift triggerFld
		timefiled = args.shift(); // 檢測欄位 為0則跳出FOR迴圈
		calltimes = parseInt(easyValue(timefiled), 10); // 次數
		console.log("Call3 重複" + calltimes + "次");
		args = args.reverse();
		$.each(args, function (i, x) { // it1, it2,....
			if (i < 9) {
				it = "#it" + (args.length - i);
				console.log("call with " + it + ":" + x);
				if (x[0] == "@") {
					x = "_@@_reffield_@@_#" + x.slice(1);
					ifx.setValue(it, x);
				} else {
					ifx.setValue(it, easyValue(x));
				}
			}
		});
		var ret;
		// 增加循環LOOP次數
		for (var i = 0; i < calltimes; i++) {
			if (ifx.getValue(timefiled) == 0) { // 檢查欄位是否為0?
				break;
			}
			ret = ifx.executePrefRtn(triggerFld);
		}
		delete self.origCaller;
		return ret;
	};
	Rtn.prototype.JS = Rtn.prototype.INVOKEJS = function () {
		var args = aps.call(arguments);
		this.callHandler.handle(args);
	};
	Rtn.prototype.SHOW = function () {
		showOrHide(aps.call(arguments), true);
	};
	Rtn.prototype.HIDE = function () {
		showOrHide(aps.call(arguments), false);
	};
	Rtn.prototype.ENABLE = function () {
		enableFields(aps.call(arguments), true);
	};
	Rtn.prototype.DISABLE = function () {
		enableFields(aps.call(arguments), false);
	};
	Rtn.prototype.RUN = function () {
		setRun(aps.call(arguments), true);
	};
	Rtn.prototype.NO_RUN = function () {
		setRun(aps.call(arguments), false);
	};

	function setRun(args, bRun) {
		var names = [],
			ss, dcFields = null,
			startIndex, stopIndex;
		args.shift(); // skip source field
		$.each(args, function (i, x) {
			if (x == "*") {
				dcFields = ifx.getFieldsByRtnType("dc");
				$.each(dcFields, function (i, x) {
					if (x.name[0] == "#") {
						names.push(x.name);
					}
				});
			} else if (x.indexOf("::") == -1) {
				names.push(x);
			} else {
				ss = x.split('::');
				dcFields = ifx.getFieldsByRtnType("dc");
				startIndex = searchByName(ss[0]);
				stopIndex = searchByName(ss[1]);
				if (startIndex != -1 && stopIndex != -1 && startIndex < stopIndex) {
					for (; startIndex <= stopIndex; startIndex++) {
						names.push(dcFields[startIndex].name);
					}
				}
			}
		});
		$.each(names, function (i, x) {
			var fld = ifx.getField(x);
			fld.setRunnable(bRun);
		});

		function searchByName(n) {
			var index = -1;
			$.each(dcFields, function (i, x) {
				if (x.name == n) {
					index = i;
					return false;
				}
			});
			return index;
		}
	}
	var myEvalRe = /(#[\w]+)/g;
	Rtn.prototype.myTest = function () {
		var args = aps.call(arguments),
			condition;
		condition = args.shift();
		condition = condition.replace(myEvalRe, getEvalString);
		var r = eval('(' + condition + ')');
		console.log('mytest:' + condition + ' ==>' + r);
		return r;
	};
	Rtn.prototype.SWITCH = function () {
		var args = aps.call(arguments),
			switchValue, fnTest, fnRun = null;
		args.shift();
		switchValue = easyValue(args.shift());
		console.log("switchValue:" + switchValue);
		for (var i = 0; i < args.length - 1; i += 2) { // skip last on
			// (default)
			fnTest = args[i];
			console.log("args[" + i + "]:" + args[i]());
			if (fnTest() == switchValue) {
				fnRun = args[i + 1];
				break;
			}
		}
		if (!fnRun) fnRun = args.pop(); // pop last on
		console.log("fnRun:" + fnRun);
		fnRun();
	};
	Rtn.prototype.IS = function () {
		var args = aps.call(arguments),
			value, target, opt, str;
		if (args.length != 3) {
			args.shift();
		}
		target = args.shift();
		opt = args.shift();
		str = args.shift();
		value = str.replace(myEvalRe, getEvalString);
		value = eval('(' + value + ')');
		ifx.setValue(target, value ? "1" : "0");
	};
	Rtn.prototype.BIND = function () {
		var args = aps.call(arguments),
			target, key, map;
		if (args.length > 3) args.shift();
		target = args[0];
		key = args[1];
		map = IfxUtl.str2map(args[2]);
		IfxUtl.collectValue(map, easyValue);
		// 柯:查詢交易瀏覽(表格)時重送bug，因隱藏按鈕是不需要bind，真正的關閉
		if (args[2].toUpperCase() == "CANCEL") {
			map = "";
			console.log("取消BIND");
		}
		console.log("target:" + target);
		console.log("key:" + key);
		console.log("map:" + map);
		ifx.getField(target).bag(key, map);
		// add
		if (map.now == '1') {
			ifx.bindHandler(map);
		}
		// end add
	};
	Rtn.prototype.STATUS = function () {
		var args = aps.call(arguments);
		args.shift();
		args = $.map(args, function (x) {
			return easyValue(x);
		});
		var cmd = args[0].toUpperCase();
		switch (cmd) {
			case "OD":
				ifx.setStatusField("OD", args[1]);
				break;
			default:
				console.log("unknown STATUS cmd:" + args[0]);
		}
		ifx.refreshStatus();
	};
	Rtn.prototype.XMT = function () {
		throw ifx_makeTransmit();
	};
	Rtn.prototype.PDF = function () {
		var args = aps.call(arguments),
			target = args.shift(),
			value = easyValueT(args.shift()),
			ip = location.host.split(":"),
			port = ip.length > 1 ? ip[1].substring(0, 3) + "5" : "7005";

		var url = "http://" + ip[0] + ":" + port + "/iTX/mvc/hnd/download/file/" + ifx.getValue('TLRNO$') + "/" + value + "/1/_";
		//		var url = (ip == "192.168.10.8:7003" ?
		//				"http://" + ip.substring(0, ip.length - 1) + "5/iTX/mvc/hnd/download/file/" + value + "/1" : "http://" + ip + ":7005/iTX/mvc/hnd/download/file/" + value + "/1");
		$("#pdf-container").empty();
		// If absolute URL from the remote server is provided, configure the CORS
		// header on that server.00001531
		// Loaded via <script> tag, create shortcut to access PDF.js exports.
		var pdfjsLib = window['pdfjs-dist/build/pdf'];
		// The workerSrc property shall be specified.
		pdfjsLib.GlobalWorkerOptions.workerSrc = 'pdfjs-2.4.456/pdf.worker.js';
		// Asynchronous download of PDF
		var loadingTask = pdfjsLib.getDocument(url);
		loadingTask.promise.then(function (pdf) {
			console.log('PDF loaded');
			pdfSync();
			// Fetch the first page
			async function pdfSync() {
				for (var pageNumber = 1; pageNumber <= pdf.numPages; pageNumber++) {
					await new Promise((resolve, reject) => {
						pdf.getPage(pageNumber).then(function (page) {
							console.log('Page loaded');
							var scale = 1.5;
							var viewport = page.getViewport({
								scale: scale
							});
							// Prepare canvas using PDF page dimensions
							//    var canvas = document.getElementById('the-canvas')
							var canvas = document.createElement('canvas');
							var context = canvas.getContext('2d');
							canvas.height = viewport.height;
							canvas.width = viewport.width;
							// Render PDF page into canvas context
							var renderContext = {
								canvasContext: context,
								viewport: viewport
							};
							var renderTask = page.render(renderContext);
							renderTask.promise.then(function () {
								console.log('Page rendered');
								document.getElementById('pdf-container').appendChild(canvas);
								resolve();
							});
						});
					});
				}
			}
		}, function (reason) {
			// PDF loading error
			console.error(reason);
		});
	}

	function getEvalString(x, i) {
		// 鍾 IF function 及IS function ,使用evil函數,變數值要先去除空白
		// always be field
		var fld = ifx.getField(x);
		var s = easyValue(x);
		if (fld.isNumeric()) return s;
		s = s.trim();
		// 解決eval中 單引號導致執行出錯的問題
		s = s.replace(/\'/g, "\\\'");
		return "'" + s + "'";
		// end
	}
	// // helper rtn
	function enableFields(args, enabled) {
		args.shift(); // skip source field
		$.each(args, function (i, x) {
			var fld = ifx.getField(x);
			fld.enabled(enabled);
			// TODO:change to readonly?
		});
	}

	function showOrHide(args, bVisible) {
		var list1 = [],
			showList = [],
			showIdList = [],
			hideList = [],
			hideIdList = [],
			ids, opt;
		args.shift(); // skip source field
		if (args[0].charAt(0) != '#') {
			opt = (args.shift()).toUpperCase();
			switch (opt) {
				case "RADIO": // show/hide args[0] and hide the other fields in
					// same TD
					if (bVisible) {
						if (args.length == 3) {
							// RADIO "TB_", 1, 3
							for (var i = parseInt(easyValue(args[1]), 10); i <= parseInt(easyValue(args[2]), 10); i++) {
								showList.push("#" + easyValue(args[0]) + i);
							}
						} else {
							showList.push(args[0]); // #TB_n
						}
						// find hide fields in same cell
						$.each(showList, function (i, x) { // x is field name
							var jqFld = ifx.getField(x).ui();
							var sameCellFlds = (jqFld).parent().find('input');
							$.each(sameCellFlds, function (j, y) {
								var name = IfxUtl.id2name(y.id); // y is DOM
								// object
								if (name != x) hideList.push(name);
							});
						});
					}
					break;
				default:
					alert("unknown opt for SHOW/HIDE");
			}
		} else {
			if (bVisible) {
				showList = args;
				console.log("showList:" + showList);
			} else {
				hideList = args;
				console.log("hideList:" + hideList);
			}
		}
		$.each(showList, function (i, x) {
			var fld = ifx.getField(x);
			// TODO: refactor to ifx-fld.method?
			fld.enabled(true);
			if (fld.isUI()) {
				showIdList.push('#' + fld.id());
			} else {
				console.log("show no ui:" + fld.id());
			}
		});
		$.each(hideList, function (i, x) {
			var fld = ifx.getField(x);
			// TODO: refactor to ifx-fld.method?
			fld.enabled(false);
			if (fld.isUI()) {
				hideIdList.push('#' + fld.id());
			} else {
				console.log("hide no ui:" + fld.id());
			}
		});
		if (hideIdList.length > 0) {
			ids = hideIdList.join(",");
			$(ids).hide('fast');
		}
		if (showIdList.length > 0) {
			ids = showIdList.join(",");
			$(ids).show('fast');
		}
		// ifx.goCurrentField();
	}

	function easyValue(x) {
		return x.charAt(0) == "#" ? ifx.getValue(x) : x;
	}
	// 鍾 T function 數值變數一律先補0,再取值
	function easyValueT(x) {
		// toHost 第二個參數為 不是上真正中心
		return x.charAt(0) == "#" ? ifx.toHost(x) : x;
	}
	// end
	function easyJoin(arr) {
		return $.map(arr, function (x) {
			return easyValue(x);
		}).join();
	}
	// 柯 增加特殊join 為了特殊字元不須easyValue
	function easyJoinSpecial(arr) {
		return $.map(arr, function (x) {
			if (x == " ") {
				return "空白";
			}
			return x;
		}).join();
	}

	function isInt(x) {
		var y = parseInt(x);
		if (isNaN(y)) return false;
		return x == y && x.toString() == y.toString();
	}

	function isSmile(args) {
		if (args[args.length - 1] && args[args.length - 1].substr(0, 3) == "^_^") {
			return args[args.length - 1].substr(3);
		}
		return null;
	}

	function myInArrayNumeric(n, arr) {
		n = parseFloat(n);
		for (var i = 0; i < arr.length; i++) {
			if (n == parseFloat(easyValue(arr[i]))) return true;
		}
		return false;
	}

	function myInArray(n, arr) {
		for (var i = 0; i < arr.length; i++) {
			// 鍾 V function 字串比較要先去除空白
			if (n.trim() == easyValue(arr[i]).trim()) return true;
			// end
		}
		return false;
	}
	// 柯 增加特殊檢查 0全部1前2後
	function myInArraySpecial(n, arr) {
		var lnum;
		for (var i = 1; i < arr.length; i++) {
			lnum = 0 - arr[i].length;
			if (arr[0] == "0" && n.indexOf(arr[i]) != -1) {
				return true;
			}
			if (arr[0] == "1" && n.slice(0, arr[i].length) == arr[i]) {
				return true;
			}
			if (arr[0] == "2" && n.slice(lnum) == arr[i]) {
				return true;
			}
			if (arr[0] == "3" && n.trim().indexOf(arr[i]) != -1) {
				return true;
			}
			if (arr[0] == "4" && n.trim().slice(0, arr[i].length) == arr[i]) {
				return true;
			}
			if (arr[0] == "5" && n.trim().slice(lnum) == arr[i]) {
				return true;
			}
		}
		return false;
	}

	function splitSmile(args) {
		var smileArgs = null,
			smileTag = "^_^";
		for (var i = 0; i < args.length; i++) {
			if (args[i].indexOf(smileTag) == 0) {
				smileArgs = args.splice(i);
				// shift 1st element, replace it, then unshift it
				smileArgs.unshift(smileArgs.shift().replace("^_^", ""));
				break;
			}
		}
		return smileArgs;
	}

	function formatError(defaultErrorCode, smileArgs, others) {
		return IfxUtl.formatError(defaultErrorCode, smileArgs, others);
	}
	// / end of helper
	// //////////////////////////////////////////////
	// end of Module IfxRtn
	return Rtn;
}());
! function (e, r) {
	"object" == typeof exports && "object" == typeof module ? module.exports = r() : "function" == typeof define && define.amd ? define("calc", [], r) : "object" == typeof exports ? exports.calc = r() :
		e.calc = r()
}(this, function () {
	return function (e) {
		function r(n) {
			if (t[n]) return t[n].exports;
			var u = t[n] = {
				i: n,
				l: !1,
				exports: {}
			};
			return e[n].call(u.exports, u, u.exports, r), u.l = !0, u.exports
		}
		var t = {};
		return r.m = e, r.c = t, r.i = function (e) {
			return e
		}, r.d = function (e, t, n) {
			r.o(e, t) || Object.defineProperty(e, t, {
				configurable: !1,
				enumerable: !0,
				get: n
			})
		}, r.n = function (e) {
			var t = e && e.__esModule ? function () {
				return e.default
			} : function () {
				return e
			};
			return r.d(t, "a", t), t
		}, r.o = function (e, r) {
			return Object.prototype.hasOwnProperty.call(e, r)
		}, r.p = "", r(r.s = 2)
	}([function (e, r, t) {
		"use strict";

		function n(e) {
			for (var r = ""; e--;) r += "0";
			return r
		}

		function u(e) {
			var r = String(e).split(/[eE]/);
			if (1 == r.length) return r[0];
			var t = "",
				n = e < 0 ? "-" : "",
				u = r[0].replace(".", ""),
				o = Number(r[1]) + 1;
			if (o < 0) {
				for (t = n + "0."; o++;) t += "0";
				return t + u.replace(/^\-/, "")
			}
			for (o -= u.length; o--;) t += "0";
			return u + t
		}

		function o(e) {
			var r = void 0;
			r = e < 1e-6 ? u(e) : e + "";
			var t = r.lastIndexOf(".");
			return t < 0 ? [r, 0] : [r.replace(".", ""), r.length - t - 1]
		}

		function i(e, r, t, n) {
			switch (t) {
				case "+":
					return (e + r) / n;
				case "-":
					return (e - r) / n;
				case "*":
					return e * r / (n * n);
				case "/":
					return e / r
			}
		}

		function f(e, r, t) {
			var u = o(e),
				f = o(r),
				a = Math.max(u[1], f[1]);
			if (0 === a) return i(e, r, t, 1);
			var c = Math.pow(10, a);
			return u[1] !== f[1] && (u[1] > f[1] ? f[0] += n(u[1] - f[1]) : u[0] += n(f[1] - u[1])), i(+u[0], +f[0], t, c)
		}

		function a(e, r) {
			return f(e, r, "+")
		}

		function c(e, r) {
			return f(e, r, "-")
		}

		function p(e, r) {
			return f(e, r, "*")
		}

		function s(e, r) {
			return f(e, r, "/")
		}

		function l(e, r) {
			return Math.round(e * Math.pow(10, r)) / Math.pow(10, r)
		}
		e.exports = {
			add: a,
			sub: c,
			mul: p,
			div: s,
			round: l
		}
	}, function (e, r, t) {
		"use strict";

		function n(e) {
			for (var r = 0, t = void 0, n = [], u = d; e[r];) {
				t = e[r];
				var o = x[t];
				if (o) n.push({
					type: o
				}), u = d;
				else if (/[0-9]/.test(t)) {
					if (u == d) n.push({
						type: i,
						value: t
					}), u = y;
					else if (u == y || u == h) {
						var f = n[n.length - 1];
						f.value += t
					}
				} else if ("." == t) {
					if (u != y) throw "语法错误";
					u = h;
					var a = n[n.length - 1];
					a.value += "."
				} else {
					if (" " != t) throw "语法错误";
					u = d
				}
				r++
			}
			return n.push({
				type: v
			}), n
		}

		function u(e) {
			function r() {
				return x = y[h++]
			}

			function t() {
				x = y[--h]
			}

			function u() {
				var e = 0,
					t = !1;
				if (r(), x.type == a && (t = !0, r()), x.type == i) e = x.value;
				else if (x.type == s && (e = d(), r(), x.type != l)) throw "缺少 ) ";
				return e = Number(e), t && (e = -e), e
			}

			function v() {
				var e = void 0,
					n = void 0;
				for (e = u(); ;) {
					var i = r();
					if (i.type != c && i.type != p) {
						t();
						break
					}
					n = u(), i.type == c ? e = o.mul(e, n) : i.type == p && (e = o.div(e, n))
				}
				return e
			}

			function d() {
				var e = void 0,
					n = void 0;
				for (e = v(); ;) {
					var u = r();
					if (u.type != f && u.type != a) {
						t();
						break
					}
					n = v(), u.type == f ? e = o.add(e, n) : u.type == a && (e = o.sub(e, n))
				}
				return e
			}
			var y = n(e),
				h = 0,
				x = y[h];
			return d()
		}
		var o = t(0),
			i = 1,
			f = 2,
			a = 3,
			c = 4,
			p = 5,
			s = 6,
			l = 7,
			v = 8,
			d = 1,
			y = 2,
			h = 4,
			x = {
				"+": f,
				"-": a,
				"*": c,
				"/": p,
				"(": s,
				")": l
			};
		e.exports = {
			parseExpression: u
		}
	}, function (e, r, t) {
		"use strict";

		function n(e) {
			return o(e)
		}
		var u = t(1),
			o = u.parseExpression,
			i = t(0);
		for (var f in i) n[f] = i[f];
		e.exports = n
	}])
});
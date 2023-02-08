function _Storage(key, idx) {
	this._id = null;
	this.id = function (v) {
		if (v == undefined) return this._id;
		else this._id = v;
	};
	this.key = key;
	this.idx = idx;
	this.value = "";
	this.dup = "";
	this.dirty = false;
	this.runnable = true; // true:可執行 false:不執行
	this.get = function () {
		return this.value;
	};
	this.put = function (v) {
		this.value = v;
	};
	this.saveStorage = function () {
		this.dup = this.value;
	};
	this.swapDup = function () {
		this.value = this.dup;
	};
	this.bag = {};
	this.putBag = function (k, o) {
		this.bag[k] = o;
	};
	this.getBag = function (k) {
		return this.bag[k];
	};
}
// runtime field
var __once__ = false;

function __refactor1(rtn) {
	if (rtn._auto_ == true) {
		return; // auto field, no convert
	}
	console.log("rtn._" + rtn._);
	if (rtn._ === undefined) {
		if (!__once__) {
			console.log("請重新make此交易");
			alert("請重新make此交易!!");
			__once__ = true;
		}
		return;
	}
	// devtool/gen/lib/parser.js(518)
	// fld._ = [ fld.name, fld.type,fld.len,fld.dlen,fld.attr,fld.pref_body,fld.post_body];
	var nn = ["name", "type", "len", "dlen", "attr", "pref_body", "post_body"];
	for (var i = 0; i < nn.length; i++) {
		rtn[nn[i]] = rtn._[i];
	}
	delete rtn._;
}

function _FieldRunTime(fldJson) {
	__refactor1(fldJson);
	return $.extend(this, fldJson);
}
var _fldFn = _FieldRunTime.prototype;
_fldFn.init = function (rtn) {
	this.rtnType = rtn.type;
	this.rtnName = rtn.name;
	this._enabled = true; // 可輸入或不可輸入
	this._tabbable = true;
	// 小柯 定義 新增 測試中文 英文輸入時 自動轉換
	// this.onlyEN = (this.type.toUpperCase() != "X" && this.type.toUpperCase() != "C");
	this.onlyEN = false;
	this.entryMode = (this.attr == "I" || this.attr == "H"); // &&
	// this.isUiField);
	// 鍾 增加 SKIPLN 功能,可用變數控制輸入某長度後自動跳下一個欄位,而非打滿
	this.skipln = 0;
	// end
	// 鍾 增加 L 形態之變數,顯示樣式比照一般的常數,且更正/放行都要執行
	// if(this.attr=='L') {
	// this.extraCssClass = ' field_label ';
	// this.attr = 'O';
	// }
	// end
	if (this.ref) return;
	this.storage = new _Storage(this.name, this.idx);
	// add +-
	if (this.type[0] == '+') {
		console.log('fld ' + this.name + ' is ' + this.type);
		this.signAllowed = true;
		this.type = this.type[1];
	}
	// add specialCalculate
	// 公式:100-6 = 100+(6/32)
	if (this.type[0] == '*') {
		console.log('fld ' + this.name + ' is ' + this.type);
		this.specialCalculate = true;
		this.type = this.type[1];
	}
	this.value(this.getDefaultValue());
	if (this.type.toUpperCase() != "M" && this.type.toUpperCase() != "N" && this.dlen > 0) {
		this.multiline = true;
		this.rows = this.len;
		this.cols = this.dlen;
		this.len = this.len * this.dlen;
		this.dlen = 0;
	} else if (this.type == "P") {
		this.type = "X";
		this.passwordType = true;
	}
	this.chars = 0;
};
_fldFn.copyRef = function (r, bSaveOrig) {
	if (bSaveOrig) {
		this.orig = {
			type: this.type,
			len: this.len,
			dlen: this.dlen,
			storage: new _Storage(this.name, this.idx)
		};
	}
	if (r == null) { // restore;
		r = this.orig;
	}
	if (!r) return;
	this.storage = r.storage;
	this.type = r.type;
	this.len = r.len;
	this.dlen = r.dlen;
	console.log("field " + this.name + "#" + this.idx + ",dc index:" + this.index + " ref to " + r.index + r.storage.key + "#" + r.storage.idx + " storage area");
};
_fldFn.getName = function () {
	return this.name;
};
_fldFn.value = function (v) {
	if (v != undefined) {
		try {
			this.storage.put(v);
			console.log("_fldFn.value storage put:" + v);
		} catch (e) {
			alert("確認欄位是否正確:" + e);
			console.dir(this);
			console.error("set value to Call fld?" + e);
		}
	} else {
		// console.log("_fldFn.value storage get:"+this.storage.get());
		return this.storage.get();
	}
};
_fldFn.bag = function (k, o) {
	if (o == undefined) {
		console.log("get from " + this.name + "'s bag, key:" + k);
		console.log(JSON.stringify(this.storage.getBag(k)));
		return this.storage.getBag(k);
	} else {
		console.log("put something to " + this.name + "'s bag, key:" + k);
		console.log(JSON.stringify(o));
		this.storage.putBag(k, o);
	}
};
_fldFn.isEntryField = function () {
	return this.entryMode;
};
_fldFn.setEntryMode = function (m) {
	// if(m=="I") this.entryMode = true;
	// 鍾 增加 L 形態之變數,不可輸入,顯示樣式比照一般的常數,且更正/放行都要執行
	// 增加 OH形態之變數,同O,但更正/放行時會執行
	if (m == "O" || m == "L" || m == "OH") this.entryMode = false;
	// end
	if (!this.isUI()) return;
	var $jq = this.ui();
	if (!this.entryMode) {
		if ($jq.hasClass('field_input')) $jq.removeClass('field_input');
		$jq.addClass('field_output');
		$jq.attr('readonly', true);
	}
};
_fldFn.setHMode = function () {
	// if(this.isUI())
	// if(this.attr=="H") this.entryMode = true;
};
_fldFn.isH = function () {
	// 鍾 增加 L 形態之變數,更正/放行都要執行
	// 增加 OH,SH形態之變數,但更正/放行時會執行
	// return this.attr == "OH" || this.attr == "SH" || this.attr == "H" || this.attr == "L";
	// end
	return this.attr == "OH" || this.attr == "SH" || this.attr == "H";
};
_fldFn.isTabStop = function () {
	return this.entryMode;
};
_fldFn.getValue = function () {
	if (this.extra) { // 柯 新增FOR 按鈕內容
		console.log("extra val:" + this.extra.value);
		console.log("value    :" + this.value());
		// return this.extra.value;
		if (IfxUtl.trim(this.value()).length > 0 || this.extra.maxWidth) {
			return this.value();
		} else {
			return this.extra.value;
		}
	}
	return this.value();
};
// T(2)時會有問題，因帳號分行只會給前面幾個字 但一給就出錯! (還沒到該真正帳號欄位)
_fldFn.setValue = function (v) {
	// v = $.trim(v); //T(2) T(3) 會把前面空白去掉
	if (typeof v === "string")
		v = v.replace(/\s+$/g, "");

	console.log("try set value:[" + (this.passwordType ? "密碼欄位->隱藏1" : v) + "] to field:" + this.name);
	if ($.isFunction(v)) {
		this.value(v);
		return;
	}
	if (!this.checkValidValue(v)) {
		console.log(this.name + " data content error:" + v);
		return false;
	}
	// 測試 暫存時好像會有此問題,故去除
	if (typeof v == 'undefined') {
		return;
	}
	v = this.formatValue(v.toString());
	this.value(v.toString());
	if (this.isUI()) {
		this.refreshUI();
	}
};
_fldFn.setSkipLn = function (v) {
	this.skipln = v;
	if (this.isUI()) {
		this.ui().attr('maxlength', v);
		this.refreshUI();
	}
};
_fldFn.formatValue = function (v) {
	if (this.type == "A" || this.type == "D") {
		if (this.type == "D" && v == "")
			;
		else
			return IfxUtl.numericFormatter(v, this.len);
	} else if (this.isNumeric()) {
		v = IfxUtl.strltrimZero(v);
		if (this.type == "m" || this.type == 'n') { // 原本小寫的
			// 輸出前置換
			if (this.specialCalculate) { // 公式:100-6 = 100+(6/32)
				v = IfxUtl.specialCalculate(v, this.dlen);
			}
			return IfxUtl.decimalPlaces(v, this.dlen); // 補小數點位數0
		}
	}
	if (this.type == "X" || this.type == "x") v = v.replace(/[\\]+/g, "");
	if (this.canContainBig5()) {
		if (this.type == "X") {
			v = v.toUpperCase();
			var o = IfxUtl.checkChinese(v, this.skipln || this.len);
			if (!o.status) {
				v = o.cut;
				//				throw ifx_makeFvalError(IfxUtl.formatError("EV8B", "", {
				//							len: this.skipln || this.len,
				//							str: o.cut
				//						}));
			}
			// X型態是否有要限制?
			// if (!IfxUtl.isEnglish(v)) {
			// console.log("X-type-No-Chinese:" + this.name);
			// throw ifx_makeFvalError(IfxUtl.formatError("X-type-No-Chinese"));
			// }
		} else if (this.type.toLowerCase() == "c") {
			// 特殊字先轉換...
			v = IfxUtl.respecialstr(v);
			// 柯新增 為了TOM回來時 長度打滿的問題
			v = v.replace(/\4/g, '').replace(/\7/g, '');
			if (this.type == "C") {
				v = IfxUtl.halfToFull(v);
			}
			// 送中心前 先檢查C TYPE的 中文欄位轉碼EDBIC轉換問題
			// 每欄上平台檢查,讓平台效能很慢..故挑選fkey實作
			// IfxUtl.checkCharset(v,this.name); //20171228 潘 送中心檢查中文
			// skipln
			var o = IfxUtl.checkChinese(v, this.skipln || this.len); // 柯: 補上skipln 為了rita
			// 交易限制小c長度
			if (!o.status) {
				console.log("checkChinese too len:" + this.name);
				if (this.attr != "O" && this.attr != "S") { // 潘 type S or O 不用丟訊息直接截掉
					if (confirm("輸入資料超過限制(限" + (this.skipln || this.len) + "字元，全形字佔２字元),是否刪除多餘字串?" + "\n刪除後字串:[" + o.cut + "]")) {
						console.log(this.name + " -len: " + v + " -> " + o.cut);
						v = o.cut;
					} else {
						console.log("checkChinese too len and Error");
						throw ifx_makeFvalError(IfxUtl.formatError("EV8B", "", {
							len: this.skipln || this.len,
							str: o.cut
						}));
					}
				} else {
					v = o.cut;
				}
			} else {
				var t = o.host.replace(/\4/g, ''); // 原本多前後 需取代掉
				t = t.replace(/\7/g, '');
				v = t;
				// v = o.host; //小柯測試for合庫r6...
			}
		}
		var l = IfxUtl.strlen(v);
		if (l < this.len) return v;
		else return IfxUtl.substr_big5(v, 0, this.len);
	} else {
		return v;
	}
};
_fldFn.checkValidValue = function (v) {
	v = "" + v;
	var ok = true;
	if (v.length == 0) return ok;
	switch (this.type) {
		case 'A': // 0-9
		case 'D': // ROC Date
		case 'F': // B.C. Date
			ok = (v.match(/^[0-9]+$/) != null);
			if (!ok) {
				console.log(this.name + " content is not digit");
				throw ifx_makeFvalError(IfxUtl.formatError("SE_NUMERIC")); // 原本註解 打開
			}
			// TODO:check valid DATE
			if (this.type == "D" || this.type == "F") {
				var checkDateWanted = false; // 是要開還是關???
				if (checkDateWanted) {
					if (parseInt(v, 10) != 0 && !IfxUtl.validateDate(v, this.type, this.len)) {
						console.log("invalid date:" + v);
						console.log("SE_DATE:" + this.name);
						throw ifx_makeFvalError(IfxUtl.formatError("SE_DATE"));
					}
				}
			}

			if (v.length > this.len) {
				console.log("SE_OVERFLOW:" + this.name);
				throw ifx_makeFvalError(IfxUtl.formatError("", ["SE_OVERFLOW",
					this.name, v
				]));
			}
			break;
		case 'n':
		case 'm':
		case 'M':
		case 'N':
			// 特殊規格 轉換後檢查
			if (this.specialCalculate) { // 公式:100-6 = 100+(6/32)
				v = IfxUtl.specialCalculate(v, this.dlen);
			}
			if (this.dlen == 0 && v.split("\.").length > 1)
				v = v.split("\.")[0];
			var float = parseFloat(v).toFixed(this.dlen); // 潘
			ok = (float.toString() == v || float == 0 || parseInt(float, 10) == parseInt(v, 10));
			// 柯 新增這行 檢查"負號"不在第一位的怪異..??
			if (v.indexOf("-") != -1 && v.slice(0, 1) != "-") {
				ok = false;
			}
			if (!ok) {
				console.log(this.name + " content is not digit");
				console.log("**v:" + v + ", float:" + float);
				console.log("SE_NUMERIC:" + this.name);
				throw ifx_makeFvalError(IfxUtl.formatError("SE_NUMERIC"));
			}
			v = float.toString();
			// signAlloed, remove '-' when value is negative
			if (this.signAllowed && v[0] == '-') {
				v = v.slice(1);
			}
			ss = v.split(".");
			if (ss[0].length > this.len) { // integral part overflow
				// throw "數字型欄位整數部份太長" +this.name +":["+ v+"]"
				console.log("SE_INTEGRAL_TOO_LONG:" + this.name);
				throw ifx_makeFvalError(IfxUtl.formatError("SE_INTEGRAL_TOO_LONG"));
			}
			if (ss.length == 2) { // 有小數部份
				if (ss[1].length > this.dlen) {
					console.log("SE_TOO_LONG_AFTER_DECIMAL_POINT:" + this.name);
					throw ifx_makeFvalError(IfxUtl.formatError("SE_TOO_LONG_AFTER_DECIMAL_POINT"));
					// throw "數字型欄位小數點後部份太長" +this.name +":["+ v+"]"
				}
			}
			break;
		// case 'j':
		// if(v.length != 16 )
		// {
		// console.log(this.name + " length != 16");
		// throw ifx_makeFvalError(IfxUtl.formatError("XXXXX TEMP LENGTH"));
		// }
		// break;
		default:
			/*
			 * TODO:判斷中英文??? var l = IfxUtl.strlen(v); if(l > this.len) throw "資料溢位"+ +this.name
			 * +":["+ v+"]"
			 */
			break;
	}
	return ok;
};
_fldFn.addInputFormat = function () { // 已改回正確版本
	var $jq = this.ui(),
		v;
	if (this.type.toLowerCase() == "m") {
		$($jq).on("focus", function () {
			v = $jq.val().replace(/,/g, "");
			$($jq).val(v);
			$($jq).select();
		}).on("blur", function () {
			if ($jq.val()) {
				$jq.val(IfxUtl.addCommas($jq.val()));
			}
		}).bind('paste', function (e) {
			e.preventDefault(); // 先停止這次的貼上事件
			var clbmvale = "";
			//clbmvale = window.clipboardData.getData("Text");
			clbmvale = window.event.clipboardData.getData("text");
			clbmvale = clbmvale.replace(/,/g, "");
			$jq.val(clbmvale);
		});
	} else if (this.type.toLowerCase() == "j") {
		$($jq).on("focus", function () {
			v = $jq.val().replace(/-/g, "");
			v = IfxUtl.trim(v); // 增加去空白 因須給部分值時其他會補空白 會有問題
			$($jq).val(v);
			$($jq).select();
		}).on("blur", function () {
			if ($jq.val()) {
				v = $jq.val().replace(/-/g, "");
				console.log("addInputFormat:" + v + " to " + IfxUtl.addDash($jq.val()));
				$jq.val(IfxUtl.addDash(v));
			}
		}).bind('paste', function (e) {
			e.preventDefault(); // 先停止這次的貼上事件
			var clbvale = window.clipboardData.getData("Text");
			clbvale = clbvale.replace(/-/g, "");
			clbvale = IfxUtl.trim(clbvale);
			$jq.val(clbvale);
		});
	} else if (this.type.toLowerCase() == "f" || this.type.toLowerCase() == "d") {
		$($jq).on("focus", function () {
			v = $jq.val().replace(/\//g, "");
			v = IfxUtl.trim(v); // 增加去空白 因須給部分值時其他會補空白 會有問題
			$($jq).val(v);
			$($jq).select();
		}).on("blur", function () {
			if ($jq.val()) {
				v = $jq.val().replace(/\//g, "");
				console.log("addInputFormat:" + v + " to " + IfxUtl.dateFormatter(v, v.length));
				$jq.val(IfxUtl.dateFormatter(v, v.length));
			}
		});
	} else if (this.isUI()) { // 柯:Eric需求 欄位右邊去空白
		$($jq).on("focus", function () {
			v = $jq.val();
			if ($($jq).prop("type").toLowerCase() != 'button') v = IfxUtl.rtrim(v);
			$($jq).val(v);
			$($jq).select();
		}).on("blur", function () {
			// 柯: YN click 改 mousedown for ie10 BUG?
			console.log("isUI blur:" + $jq.val() + "->" + $jq.attr("value"));
			$jq.val($jq.attr("value"));
		});
	}
};
_fldFn.addKeyFilter = function (fnAutoSkipper) {
	var self = this,
		displayLen, maxLen, downfld = "";
	if ("ADF".indexOf(this.type) != -1) {
		console.log("add key filter for " + this.name);
		self.ui().keypress(function (e) {
			if (e.char.match(/[^0-9]/g)) // 柯:String.fromCharCode(e.keyCode)
				return false;
		});
	} else if ("mn".indexOf(this.type.toLowerCase()) != -1) {
		// var re = /^\d+(\.\d{0,2})?$/;
		self.ui().keypress(function (e) {
			var v = $(this).val(),
				ch = e.char == undefined ? e.key : e.char; // 柯:String.fromCharCode(event.keyCode)
			if (!expandMagic(self, $(this), e)) {
				e.preventDefault();
				return;
			}
			if (self.signAllowed) {
				displayLen = self.getDisplayLength();
				maxLen = self.ui().attr('maxlength');
				if (ch == '-' && v.length == 0) {
					if (displayLen == maxLen) {
						// 不動態變長, 正負號欄位一開始即+1
						// self.ui().attr('maxlength', displayLen + 1);
					}
					console.log("* *  maxlen:" + self.ui().attr('maxlength'));
					return true;
				}
				/* '-' make number negative */
				/* 柯 調整成非常人性化 */
				if (ch == '-') {
					if (v.length != 0) {
						if (v == 0) {
							$(this).val("-");
							e.preventDefault();
							// return;
						} else if (v == "-") {
							$(this).val(0);
							e.preventDefault();
							// return;
						} else if (this.selectionStart == this.selectionEnd) { // 沒有選時，則直接*-1後離開
							var v2 = v;
							if (!isNaN(v2)) {
								v2 = v * -1;
							}
							$(this).val(v2);
							e.preventDefault();
						} else if (this.selectionStart == 0) { // 只有在全選 第一個開頭的時候，才做判斷
							var v2 = "-";
							if (this.selectionEnd == v.length) {
								$(this).val(v2);
							} else {
								$(this).val(v2 + v.slice(this.selectionEnd));
							}
							e.preventDefault();
							// return;
						}
						IfxUtl.setCursorPos(this, this.value.length, this.value.length);
						// 小柯 增加此行for +n +m欄位，到該欄位自動全選再輸入"-"時 游標會跑掉
						if (v < 0) {
							displayLen++;
						}
						self.ui().attr('maxlength', displayLen);
						console.log("* * * maxlen:" + self.ui().attr('maxlength'));
						return false;
					}
				}
			} else if (self.specialCalculate) {
				// 特殊需求需要可以輸入負號 1次
				var chartext = ['-', ' ', '/', '+'];
				for (var i = 0; i < chartext.length; i++) {
					if (ch == chartext[i]) {
						if (i > 0 && v.indexOf(chartext[0]) == -1) {
							e.preventDefault();
						}
						if (v.indexOf(chartext[i]) != -1) {
							e.preventDefault();
						}
						return;
					}
				}
			} else {
				// 沒有同意輸入負號就無用//直接跳出
				if (ch == '-') {
					e.preventDefault();
					return;
				}
			}
			console.log("* * * :" + ch);
			function thErr() {
				IfxUtl.ifx.errmsg(IfxUtl.formatError("SE_NUMERIC"));
				// 潘 mn數值欄位錯誤消除
				/*
				setTimeout(function() {
					  IfxUtl.ifx.errmsg();
				  }, 600);
				  */
			}

			if (v) {
				if (v.indexOf(".") == -1) { // 尚未輸入小數點
					if (v == '-') { // 只有負號時,不可先輸入小數點,只能輸入0-9
						if (ch.match(/[^0-9]/g)) {
							thErr();
							return false;
						}
					}
					if (ch.match(/[^0-9\.]/g)) {
						thErr();
						return false;
					}
				} else {
					if (ch.match(/[^0-9]/g)) {
						thErr();
						return false;
					}
				}
				if (v[0] == '-') { }
			} else {
				if (ch.match(/[^0-9]/g)) {
					thErr();
					return false;
				}
			}
		});

		function changeMaxLength() { }

		function expandMagic(self, $this, event) {
			var ch = event.char == undefined ? event.key.toUpperCase() : event.char.toUpperCase(); // 柯:String.fromCharCode(event.keyCode)
			// event.char.toUpperCase()輸入鍵
			var ss; // 新值
			if (ch != "M" && ch != "K") { // && ch != "-"
				return true; //
			}
			var pos = $this.caret(),
				value = $this.val(),
				dotPos = value.indexOf('.');
			// pos 輸入位數
			// val 輸入值
			if (!value) return true; // 尚未輸入任何直
			// 合庫提出1.5M 要可以變1500000
			// if(dotPos >=0 && dotPos < pos) {
			// return true; // 已過小數點位置
			// }
			var zzz = ch == "M" ? 1000000 : 1000;
			// 柯 全部邏輯都調整過
			if (dotPos == -1) { // 尚未輸入小數點
				if (value.length + zzz.toString().length - 1 <= self.len) {
					var sstemp = parseFloat(value.slice(0, pos)) * zzz;
					if (parseFloat(value.toString().slice(pos))) {
						value = sstemp + parseFloat(value.toString().slice(pos));
					} else {
						value = sstemp;
					}
					$this.val(value);
					return false;
				}
			} else {
				if (pos <= dotPos) { // 小數點之前
					ss = value.split('.');
					if (ss[0].length + zzz.toString().length - 1 <= self.len) {
						var sstemp = parseFloat(ss[0].slice(0, pos)) * zzz;
						if (parseFloat(ss[0].toString().slice(pos, dotPos))) {
							ss[0] = sstemp + parseFloat(ss[0].toString().slice(pos, dotPos));
						} else {
							ss[0] = sstemp;
						}
						value = ss.join('.');
						$this.val(value);
						return false;
					}
				} else {
					var sstemp = value.split('.');
					var ss1 = parseFloat(value.slice(0, pos)) * zzz;
					var ss2 = parseFloat(value.slice(pos)) ? parseFloat(value.slice(pos)) : 0;
					if (sstemp[0].length + zzz.toString().length - 1 <= self.len) {
						value = ss1 + ss2;
						$this.val(value);
						return false;
					}
				}
			}
			return true;
		}
	}
	// add auto skip
	// 鍾 只要沒有小數的所有變數都要能打滿跳下一個
	// 有bug在 nma時 沒小數就可以輸入負號和不是數值的值 ->已修正
	if (this.dlen == 0 && !self.signAllowed && "mn".indexOf(this.type.toLowerCase()) == -1) {
		self.ui().off('keyup keydown keypress');
		// 柯:加入在不同欄位down和up的保護機制
		self.ui().on('keydown keypress', function (e) {
			downfld = this.name; // 柯:紀錄在哪個input按下
			var ch = e.char == undefined ? e.key : e.char;
			if (autokeycode(e))
				if ("ADF".indexOf(self.type) != -1) {
					if (e.ctrlKey == true && (ch == "v" || ch == "V"))
						return;
					if (ch.match(/[^0-9]/g)) {
						IfxUtl.ifx.errmsg(IfxUtl.formatError("SE_NUMERIC"));
						return false;
					}
				}
		}).on('blur', function (e) {
			downfld = ""; // 柯:清空欄位
		}).on('keyup', function (e) {
			if (downfld != this.name) { // 柯:如up與down不同步，則return
				console.log("key up and down is different. go return");
				return;
			}
			// 鍾 下列按鍵不需auto skip
			console.log('==>e.keycode:' + e.keyCode);
			if (autokeycode(e)) {
				// 鍾 其餘打滿或設定之長度,auto skip
				self.chars = $(this).val().length;
				// var skiplen = self.len;
				var skiplen = self.ui().attr('maxlength');
				// if(self.signAllowed) {
				// v = $(this).val();
				// if(v && v[0]=='-') skiplen++;
				// }
				if (self.skipln > 0) {
					skiplen = self.skipln;
				}
				/*
				 * 2/18測試後有BUG(輸入法之類的),故只能檢查一次. //增加此段for中文欄位每次檢查長度 if
				 * ("c".indexOf(self.type.toLowerCase()) != -1) { //中文欄位 var v = $(this).val(); var
				 * o = IfxUtl.checkChinese(v, skiplen); //柯: 補上skipln 為了rita 交易限制小c長度 if (!o.status) {
				 * console.log("checkChinese too len:" + self.name + " autoskip, keyCode:" +
				 * e.keyCode); e.preventDefault(); //在keyup,這行也沒用 $(this).val(o.cut);
				 * fnAutoSkipper(self.id()); } }else
				 */
				if (self.chars == skiplen) {
					console.log(self.name + " autoskip, keyCode:" + e.keyCode);
					fnAutoSkipper(self.id()); // 小柯 補上self.id() 傳入值供ifx-core2.js 中 function
					// autoSkip(targetId)
					// 判斷
				}
			} else {
				return;
			}
		});
	}
	// end
};
// 柯:獨立出來
function autokeycode(e) {
	// F1 - F12
	if (e.keyCode >= 112 && e.keyCode <= 123) return false;
	// Navigation: HOME, END, LEFT ARROW, RIGHT ARROW, UP ARROW, DOWN ARROW
	if (e.keyCode >= 33 && e.keyCode <= 40) return false;
	switch (e.keyCode) {
		case 8:
		case 9:
		case 13:
		case 16:
		case 17:
		case 18:
		case 27:
		case 37:
		case 38:
		case 39:
		case 40:
		case 44:
		case 46:
			return false;
			break;
		default:
			return true;
	}
};
_fldFn.getTomLen = function () {
	return this.len + this.dlen + (this.signAllowed ? 1 : 0);
};
_fldFn.getTomType = function () {
	return this.type;
};
_fldFn.setValueFromTom = function (v) {
	try {
		var negative = false;
		if (/[NnMm]/.test(this.type)) {
			v = $.trim(v);
			if (v.length == 0)
				v = "0";
			if (this.signAllowed) {
				negative = (v[0] == '-');
				if (negative)
					v = v == "0" ? v : v.slice(1);
			}
			if (this.dlen != 0) {
				// v = v / Math.pow(10, this.dlen);
				v = parseFloat(v).toFixed(this.dlen);
				v = v.toString();
			} else {
				v = parseInt(v, 10);
				v = v.toString();
			}
			if (negative) v = v * -1;
		} else if (/[ADF]/.test(this.type)) {
			v = $.trim(v);
			if (v.length < this.len) {
				for (var i = v.length; i < this.len; i++) {
					v = "0" + v;
				}
				console.log("after v:" + v);
			}
		} else {
			v = IfxUtl.rtrim(v);
		}
		this.setValue(v);
		this.refreshUI();
		var $fld = this.ui();
		$fld.effect("highlight", {
			color: "#99FF99"
		}, 'slow'); // .fadeIn();
	} catch (ee) {
		alert("set " + this.name + " value from tom/rom [" + v + "],\n" + ee);
	}
};
_fldFn.toHost = function (resv, checkC) {
	var v, r6v;
	switch (this.type) {
		case "A":
		case "D":
		case "F":
			v = IfxUtl.numericFormatter(this.value(), this.len);
			break;
		case "m":
		case "n":
		case "M":
		case "N":
			// 潘 送中心跳過補滿零及去小數點
			var t = this.value();
			var negative = this.signAllowed && t && t[0] == '-';
			if (negative) t = t.slice(1);
			v = IfxUtl.numericFormatter(IfxUtl.stripDot(t), this.len + this.dlen);
			if (this.signAllowed) {
				if (negative) v = '-' + v;
				else v = '+' + v;
			}
			if (resv) v = this.value();
			break;
		default:
			if (this.canContainBig5()) {
				if (this.type.toUpperCase() == "X") {
					var ischinese = IfxUtl.checkifChinese(this.value());
					if (ischinese) {
						console.log("X-type-No-Chinese:" + this.name);
						if (checkC) {
							return "X-type-No-Chinese2";
						}
					}
				}
				// console.log("@@stringFormatterBig5 this.value():" + this.value() + ",this.len:" +
				// this.len);
				v = IfxUtl.stringFormatterBig5(this.value(), this.len);
				// console.log("$$stringFormatterBig5 v:" + v + ",len:" + v.length);
				if (this.type.toLowerCase() == "c" && !resv) {
					var r6v = IfxUtl.stringtohostcC(v, this.len);
					console.log("_server_os ,1 : R6, 0: Windows::?" + _server_os);
					if (_server_os == "1") {
						v = r6v;
					}
					console.log("===>to R6 host, " + this.name + ":" + this.value() + "==>[" + r6v + "]");
				}
			} else {
				v = IfxUtl.stringFormatter(this.value(), this.len);
			}
	}
	console.log("to host, " + this.name + ":" + this.value() + "==>[" + v + "]");
	return v;
};
_fldFn.postEntry = function (v) {
	return v;
};
_fldFn.isUI = function () {
	// return "IOH".indexOf(this.a) >=0 && this.r && this.c;
	return this.id() != null;
};
_fldFn.isNumeric = function () {
	return /["NnMm"]/.test(this.type);
};
// 鍾 日期也是 isCalcMode
// 柯 帳號也是 isCalcMode (為了印出 - - -)
_fldFn.isCalcMode = function () {
	return /["NnMmDFj"]/.test(this.type);
};
// end
// 新增FOR列印規格書時使用
_fldFn.isFackStarMode = function () {
	return /["NnMmDFjA"]/.test(this.type);
};
_fldFn.canContainBig5 = function () {
	// return "XxCc".indexOf(this.t) >=0;
	return /[XxCc]/.test(this.type);
};
_fldFn.baseTypeValidate = function () {
	return true;
};
_fldFn.dump = function () {
	if (this.passwordType) {
		console.log("name:" + this.name + ",密碼欄位->隱藏2");
	} else {
		console.log("name:" + this.name + ", index:" + this.index + ", attr:" + this.attr + ", value:" + this.value());
		console.log('===>toHost:[' + this.toHost() + ']');
		console.log("===>toPrint(f):[" + this.getPrintValue(true) + '], toPrint(nf):' + this.getPrintValue(false) + ']');
		console.log("------------------------------------------\n\n");
	}
};
_fldFn.getShowLength = function () {
	var l = this.len;
	l = Math.floor(l / 3); // 3 6 9 12 15 位時會多1位,但應該沒差
	if (this.signAllowed) l = l + 1;
	return l;
};
_fldFn.getDisplayLength = function () {
	var l;
	if (this.multiline) return this.cols * this.rows;
	if (this.dlen > 0) l = this.len + this.dlen + 1;
	else l = this.len;
	if (this.signAllowed) l = l + 1;
	return l;
};
_fldFn.getViewLength = function () {
	if (this.type == "c") return this.len / 2;
	if (this.dlen > 0) return this.len + this.dlen + 1;
	else return this.len;
};
_fldFn.id = function (v) {
	return this.storage.id(v);
};
_fldFn.ui = function () {
	return $("#" + this.id());
};
_fldFn.isTabbable = function () {
	return this._tabbable;
};
_fldFn.isEnabled = function () {
	return this._enabled;
};
_fldFn.isVisible = function () {
	return this.ui().is(':visible');
};
_fldFn.enabled = function (enableIt) {
	this._enabled = enableIt;
	if (this.extra) {
		this.ui().attr("disabled", !enableIt);
	}
};
_fldFn.enableUI_deprecated = function (enableIt) { //
	if (!this.isUI()) return;
	if (enableIt) this.ui().removeAttr('disabled');
	else this.ui().attr('disabled', '');
};
_fldFn.refreshUI = function () { // 有bug 在帳號檢查錯誤時是會怪
	console.log("do refreshUI!");
	if (this.isUI()) {
		var $jq = this.ui();
		var v = this.value(),
			vlen = v.length,
			resultval = ""; // 後面要刪除
		console.log("refreshUI v:" + this.value());
		if (this.type === 'm' || this.type === 'M') {
			if (this.value()) {
				$jq.val(IfxUtl.addCommas(this.value()));
			}
			return;
		}
		if (this.type === 'j') {
			// 小柯 增加去空白後檢查字串是否為空 以免變成空的addDash
			if (IfxUtl.trim(this.value()) != "") {
				console.log("refreshUI:" + this.value() + " to " + IfxUtl.addDash(this.value()));
				$jq.val(IfxUtl.addDash(this.value()));
			} else {
				$jq.val(this.value())
			}
			return;
		}
		// 有需要民國年前置0自動刪除? start
		if (this.type === 'D') {
			if (IfxUtl.trim(this.value().replace(/0/g, "")) != "") {
				$jq.val(IfxUtl.dateFormatter(this.value(), this.len));
			} else {
				$jq.val("");
			}
			if (this.value()) {
				for (var i = 0; i < vlen; i++) {
					if (v.charAt(0) == 0) {
						v = v.slice(1);
					}
				}
			}
			$jq.attr('value', v); // 增加這行
			return;
		}
		if (this.type.toLowerCase() === 'f') {
			// 小柯 增加去空白後檢查字串是否為空 以免變成空的addDash
			if (IfxUtl.trim(this.value().replace(/0/g, "")) != "") {
				$jq.val(IfxUtl.dateFormatter(this.value(), this.len));
			} else {
				$jq.val("");
			}
			return;
		}
		// 有需要民國年前置0自動刪除? end
		// 鍾 若該欄位有設定skipln,則該欄位需以skipln為長度
		if (this.skipln > 0 && this.type != "c") { // 更改成小c 不進入此段
			if ("x".indexOf(this.type.toLowerCase()) != -1) {
				resultval = IfxUtl.stringFormatter(this.value(), this.skipln);
			} else {
				resultval = IfxUtl.numericFormatter(this.value(), this.skipln);
			}
		} else {
			resultval = this.value();
		}
		console.log("refreshUI attr And $jq.val set:" + resultval);
		try {
			$jq.val(resultval);
		} catch (ee) {
			$jq.val("");
		}
		$jq.attr('value', resultval); // 好像因為IE10會有輸入後用滑鼠移開的bug
		return;
		// end
	}
};
_fldFn.isRunnable = function () {
	return this.storage.runnable;
};
_fldFn.setRunnable = function (bRun) {
	this.storage.runnable = bRun;
	this.enabled(bRun);
};
_fldFn.isDirty = function (bDirty) {
	if (bDirty == undefined) return this.storage.dirty;
	else this.storage.dirty = bDirty;
};
_fldFn.getUIvalue = function () {
	var lastV = this.getValue();
	var v;
	// 鍾 非輸入欄位增加 L,OH
	if (this.isUI() && this.isEntryField()) {
		// end
		v = (this.ui().val());
		console.log(this.name + " ui value:" + (this.passwordType ? "密碼欄位->隱藏3" : v));
		// 鍾 m形態時,輸入時,要將,去除
		if (this.type.toLowerCase() == "m" && isNaN(v)) // ??有問題
			v = v.replace(/,/g, "");
		// end
		if (this.type.toLowerCase() == "j") {
			v = v.replace(/-/g, ""); // 增加去空白 因須給部分值時T(2)其他會補空白 會有問題
			v = IfxUtl.trim(v);
		}
		if (this.type.toLowerCase() == "f" || this.type.toLowerCase() == "d") {
			v = v.replace(/\//g, ""); // 增加去空白 因須給部分值時T(2)其他會補空白 會有問題
			v = IfxUtl.trim(v);
		}
		// end
		console.log("_fldFn.getUIvalue:" + v);
		if (v.length == 0) {
			v = this.getDefaultValue();
			console.log("_fldFn.getUIvalue-Def:" + v);
		}
		this.setValue(v);
		console.log(this.name + " getUIvalue return");
	} else {
		v = this.value();
		if (v.length == 0) {
			v = this.getDefaultValue();
			this.setValue(v);
		}
	}
	this.isDirty((lastV !== this.getValue()));
	console.log("===>" + this.name + " dirty?" + this.isDirty());
	if (this.passwordType) {
		console.log("密碼欄位->隱藏4");
	} else {
		console.log("old:[" + lastV + "], new:[" + this.getValue() + ']');
	}
};
_fldFn.getDefaultValue = function () {
	// if ("AaNnMm".indexOf(this.t) == -1) return "";
	return (/[AaNnMm]/.test(this.type)) ? "0" : "";
};
_fldFn.isExtraUI = function () {
	return this.extra;
};
_fldFn.display = function (tabindex) {
	this.isUiField = true;
	this.id("fld_" + this.name.slice(1));
	var s = "",
		styles = [];
	var fldClass;
	var ckbvalue; // checkbox
	if (this.extra) {
		if (this.extra.ui == 'button') {
			// s= "<a class='button white field_button' id='{id}' name='{id}'
			// tabindex='{tab}'>" + this.extra.text + "</a>";
			s = "<input type='button' class='field_button' {style} {disabled} id='{id}' name='{id}'   tabindex='{tab}' value='" + this.extra.value + "' title='" + (this.extra.title == undefined ? this.extra.value : this.extra.title) + "'{accept} " + (this.extra.width ? "style='width: " + this.extra.width + "px'" : "") + "/>";
			s = s.replace(/{id}/g, this.id());
			s = s.replace(/{varname}/g, this.name);
			s = s.replace(/{tab}/, tabindex);
			// 區顯button顏色
			// TODO 有無多種顏色? 有需要改成CLASS方式?
			if (this.extra.highlight == 'yellow') {
				styles.push('background-image:linear-gradient(to bottom, #FCFCFC 5%, rgba(255, 255, 87, 1)100%)');
			}
			if (this.extra.enable == '0') {
				s = s.replace(/{disabled}/, 'disabled="disabled"');
				// styles.push('display:none');
				this._enabled = false;
			} else {
				s = s.replace(/{disabled}/, '');
				this._enabled = true; // 因為此時還沒加到畫面, 所以不能呼叫enable();
			}
			if (this.extra.tabbable == '0') this._tabbable = false;
			if (styles.length > 0) {
				s = s.replace(/{style}/, 'style="' + styles.join(";") + '"');
			} else {
				s = s.replace(/{style}/, ""); // 如需要則可依照參數調整此style
			}
			if (this.extra.file == "true") {
				var tps = "type='file' ";
				if (this.extra.filename) {
					tps += "filename='" + this.extra.filename + "' ";
				}
				if (this.extra.encode) {
					tps += "encode='" + this.extra.encode + "' ";
				}
				s = s.replace("type='button'", tps);
				s = s.replace("field_button", "field_button field_file_button");
				if (this.extra.multiple == "true") s = s.replace(/{accept}/, " accept='.cvs, .xls, .xlsx, .txt' multiple");
				else s = s.replace(/{accept}/, " accept='.cvs, .xls, .xlsx, .txt' ");
				s += "<div class='progress' id='progress-div" + tabindex + "'><div class='bar' id='bar" + tabindex + "'></div><div class='percent' id='percent" + tabindex + "'>0%</div></div><a class='boxclose' id='boxclose" + tabindex + "' title='取消上傳'></a><div class='status' id='status" + tabindex + "' style='display: none;'></div><div class='fldName' id='fldName" + tabindex + "' style='display: none;'>" + (this.extra.fieldName ? this.extra.fieldName : "") + "</div>";
			} else {
				s = s.replace(/{accept}/, "");
			}
			return s;
		} else if (this.extra.ui == 'checkbox') {
			if (this.extra.value.length != 1) {
				ckbvalue = "";
			} else {
				ckbvalue = this.extra.value;
			}
			s = "<INPUT {pswd} class='{field-class}' {style}  title='{varname}' id='{id}' name='{id}' value='" + ckbvalue + "' dc-index = '{dc-index}' maxlength='{len}'  tabindex='{tab}' {readonly}/>";
			fldClass = ' ifx-checkbox ';
			// #BTN1=X,15,I,ui:button;value:瀏覽;enable:1;tabbable:0
		} else if (this.extra.ui == "pdf") {
			s = "<div id='pdf-container'></div>"
		} else if (this.extra.maxWidth) {
			s = "<INPUT {pswd} class='{field-class}' {style}  title='{varname}' id='{id}' name='{id}' dc-index = '{dc-index}' maxlength='{len}'  tabindex='{tab}' {readonly} autocomplete='off'/>";
		}
	} else {
		if (this.multiline) {
			s = "<TEXTAREA class='{field-class}' {style}     title='{varname}'  id='{id}'name='{id}' wrap='physical' dc-index = '{dc-index}' rows ='{rows}' cols='{cols}' maxlength='{len}' tabindex='{tab}' {readonly} />";
			s = s.replace(/{rows}/, this.rows);
			s = s.replace(/{cols}/, this.cols);
		} else {
			s = "<INPUT {pswd} class='{field-class}' {style}  title='{varname}' id='{id}' name='{id}' dc-index = '{dc-index}' maxlength='{len}'  tabindex='{tab}' {readonly} autocomplete='off'/>";
		}
	}
	var len = this.getDisplayLength();
	s = s.replace(/{pswd}/, this.passwordType ? "type='password'" : "");
	s = s.replace(/{len}/g, len);
	s = s.replace(/{id}/g, this.id());
	s = s.replace(/{varname}/g, this.name);
	s = s.replace(/{tab}/, tabindex);
	s = s.replace(/{dc-index}/, this.index);
	fldClass += ' field_attr_' + this.attr + ' ';
	if (this.isEntryField()) {
		s = s.replace(/{readonly}/, "");
		fldClass += ' field_input ';
	} else {
		s = s.replace(/{readonly}/, "readonly");
		// 鍾 增加 L 形態之變數,不可輸入,顯示樣式比照一般的常數,且更正/放行都要執行
		if (this.attr == "L") {
			fldClass += ' field_label ';
		} else {
			// end
			fldClass += ' field_output ';
		}
	}
	s = s.replace(/{field-class}/, fldClass);
	styles = []; // 移除 var
	if (this.isCalcMode()) {
		styles.push("text-align:right");
	}
	// 鍾 增加 L 形態之變數,不可輸入,顯示樣式比照一般的常數,且更正/放行都要執行
	// if (this.attr == "L") {
	// var ll = IfxUtl.round(len * 7, 2);
	// } else
	if (this.type == "m" || this.type == "M") {
		var ll = len + this.getShowLength();
		ll = ll * 7;
	} else if (this.type == "j") { // 柯 帳號格式顯示長度需多 3
		var ll = len + 3;
		ll = ll * 7;
	} else if (this.type.toLowerCase() == "f" || this.type.toLowerCase() == "d") { // 柯 帳號格式顯示長度需多
		// 3
		var ll = len + 2;
		ll = ll * 7;
	} else {
		var ll = len * 7;
		if (this.extra)
			if (this.extra.maxWidth)
				ll = this.extra.maxWidth * 7;
	}
	ll += 20;
	if (ll < 12 && this.attr != "L") ll = 12;
	// end
	if (!this.multiline) {
		styles.push("width: " + (ll) + "px");
	}
	// 小柯 新增 測試中文 英文輸入時 自動轉換
	if (this.onlyEN) {
		styles.push("-ms-ime-mode: disabled");
	}
	// styles.push("width: " + (len) + "em");
	// 鍾字體統一細明体
	// styles.push("font-family:monospace");
	// end
	if (styles.length > 0) {
		s = s.replace(/{style}/, 'style="' + styles.join(";") + '"');
	} else s = s.replace(/{style}/, "");
	return s;
};
_fldFn.simpleDisplay = function () {
	var s, styles = [];
	if (this.multiline) {
		s = "<TEXTAREA class='{field-class}'  {style}     title='{varname}'   rows ='{rows}' cols='{cols}' readonly >";
		s = s.replace(/{rows}/, this.rows);
		s = s.replace(/{cols}/, this.cols);
	} else {
		s = "<INPUT  class='{field-class}'   {style}  title='{varname}' readonly value='{value}' />";
		s = s.replace(/{value}/, this.getPrintValue(true).replace(/\'/g, "&rsquo;"));
	}
	var len = IfxUtl.strlen(this.getPrintValue(true));
	s = s.replace(/{len}/g, len);
	s = s.replace(/{varname}/g, this.name);
	var styles = [];
	if (this.isCalcMode()) {
		styles.push("text-align:right");
	}
	var ll;
	if (this.type == "m" || this.type == "M") {
		ll = len + this.getShowLength();
		ll = ll * 7;
	} else if (this.type == "j") { // 柯 新增y格式為 xxxx-xxx-xxxxx-x
		ll = len + 3;
		ll = ll * 7;
	} else if (this.type.toLowerCase() == "f" || this.type.toLowerCase() == "d") { // 柯 新增y格式為
		// xxxx-xxx-xxxxx-x
		ll = len;
		ll = ll * 7;
	} else {
		ll = len * 7;
	}
	ll += 20;
	if (ll < 12 && this.attr != "L") ll = 12;
	// end
	if (!this.multiline) {
		styles.push("width: " + (ll) + "px");
	}
	// 小柯 新增 測試中文 英文輸入時 自動轉換
	if (this.onlyEN) {
		styles.push("-ms-ime-mode: disabled");
	}
	var fldClass = "query-header-field";
	if (this.attr == "L") {
		fldClass += ' query-header-field-label ';
	}
	s = s.replace(/{field-class}/, fldClass);
	if (styles.length > 0) {
		s = s.replace(/{style}/, 'style="' + styles.join(";") + '"');
	} else {
		s = s.replace(/{style}/, "");
	}
	if (this.multiline) {
		s += this.getValue() + "</textarea>";
	}
	return s;
};
_fldFn.focus = function () {
	this.ui().focus();
};
_fldFn.pref = function (rtnModule) {
	rtnModule.mode = "pref";
	if (this.pref_body) {
		// var actor = xfun(this.pref_body);
		// actor(rtnModule);
		this.pref_body(rtnModule);
	}
};
_fldFn.fval = function (rtnModule) {
	rtnModule.mode = "post";
	if (this.post_body) {
		// var actor = xfun(this.post_body);
		// actor(rtnModule);
		this.post_body(rtnModule);
	}
};
_fldFn.getPrintValue = function (bEditPattern) {
	/*
	 * 2013/11/11 因為放行列印時 欄位為空白直 暫時變註解 // 鍾 若不可輸入,screen print 傳回空白 if (bEditPattern) if (this.extra ||
	 * !this.isEnabled()) return ''; // end 2013/11/11
	 */
	if (this.isCalcMode()) {
		// left pad space
		var displayLen = this.getDisplayLength();
		console.log('displayLen:' + displayLen);
		if (bEditPattern) {
			switch (this.type) {
				case 'm':
				case 'M':
					displayLen += (Math.floor((this.len - 1) / 3)); // comma數目
					console.log('displayLen for commas:' + displayLen);
					return IfxUtl.stringFormatter(IfxUtl.addCommas(this.value()), displayLen, true);
					break;
				case 'F':
				case 'D':
					return IfxUtl.dateFormatter(this.value(), this.len);
					break;
				case 'j':
					displayLen += 3; // dash數目
					console.log('displayLen for dash:' + displayLen);
					console.log("getPrintValue:" + this.value() + " to " + IfxUtl.addDash(this.value()));
					return IfxUtl.stringFormatter(IfxUtl.addDash(this.value()), displayLen, true);
					break;
				default:
					break;
			}
		}
		return IfxUtl.stringFormatter(this.value(), displayLen, true);
	} else {
		// right pad space
		if (this.canContainBig5()) {
			// 小柯0912 密碼欄位是否有要在印表時顯示為 *** 暫時新增
			// 新增 空白時不印 *
			if (this.passwordType == true && this.value().length > 0) {
				return IfxUtl.stringFormatterBig5(IfxUtl.repassword(this.value(), this.len), this.len);
			}
			return IfxUtl.stringFormatterBig5(this.value(), this.len);
		} else {
			return IfxUtl.stringFormatter(this.value(), this.len);
		}
	}
};
_fldFn.getPrintFake = function (bEditPattern) {
	var tempp = this;
	// 已知欄位使用 L 型態, 直接RETURN
	if (this.name == "#RBRNO_LABEL") {
		return "受理單位";
	} else if (this.name == "#FBRNO_LABEL") {
		return "指定單位";
	}
	console.dir(tempp);
	if (this.isFackStarMode()) {
		// left pad space
		var displayLen = this.getDisplayLength();
		console.log('displayLen:' + displayLen);
		// 給做規格書使用
		var result = "";
		if (this.signAllowed) {
			result += "+";
		}
		for (var i = 0; i < this.len; i++) {
			result += "9";
		}
		for (var i = 0; i < this.dlen; i++) {
			if (i == 0) {
				result += "."
			}
			result += "9";
		}
		if (bEditPattern) {
			switch (this.type) {
				case 'm':
				case 'M':
					displayLen += (Math.floor((this.len - 1) / 3)); // comma數目
					console.log('displayLen for commas:' + displayLen);
					return IfxUtl.stringFormatter(IfxUtl.addCommas(result), displayLen, true);
					break;
				case 'F':
				case 'D':
					return IfxUtl.dateFormatterfake(result, this.len);
					break;
				case 'j':
					displayLen += 3; // dash數目
					console.log('displayLen for dash:' + displayLen);
					console.log("getPrintValue:" + result + " to " + IfxUtl.addDash(result));
					return IfxUtl.stringFormatter(IfxUtl.addDash(result), displayLen, true);
					break;
				default:
					break;
			}
		}
		return IfxUtl.stringFormatter(result, displayLen, true);
	} else {
		// 給做規格書使用
		var result = "";
		for (var i = 0; i < this.len; i++) {
			result += "X";
		}
		// right pad space
		if (this.canContainBig5()) {
			// 小柯0912 密碼欄位是否有要在印表時顯示為 *** 暫時新增
			// 新增 空白時不印 *
			if (this.passwordType == true && result.length > 0) {
				return IfxUtl.stringFormatterBig5(IfxUtl.repassword(result, this.len), this.len);
			}
			return IfxUtl.stringFormatterBig5(result, this.len);
		} else {
			return IfxUtl.stringFormatter(result, this.len);
		}
	}
};
// 這段是要做甚麼
_fldFn.getPrintFormatted = function () {
	// return this.getPrintValue();
	var v, l;
	switch (this.type) {
		case "A":
		case "D":
		// v = IfxUtl.numericFormatter(this.value(), this.len);
		// break;
		case "F":
			v = IfxUtl.dateFormatter(this.value(), this.len);
			break;
		case "n":
		case "N":
			v = this.value();
			l = this.len + (this.dlen > 0) ? (1 + this.dlen) : 0;
			v = IfxUtl.stringPadLeft(v, l);
			break;
		case "m":
		case "M":
			v = IfxUtl.moneyFormatter(this.value());
			l = this.len + (this.len - 1) / 3 + (this.dlen > 0) ? (1 + this.dlen) : 0;
			v = IfxUtl.stringPadLeft(v, l);
			break;
		case "j": // 這段用意?
			v = IfxUtl.addDash(this.value());
			// l = this.len + 3;
			// v = IfxUtl.stringPadLeft(v, l);
			break;
		default:
			if (this.canContainBig5()) {
				v = IfxUtl.stringFormatterBig5(this.value(), this.len);
			} else {
				v = IfxUtl.stringFormatter(this.value(), this.len);
			}
	}
	return v;
};
_fldFn.setDisplayValue = function () {
	if (this.type == "A") {
		var v = IfxUtl.numericFormatter(this.value(), this.len);
		this.setValue(v);
	}
};
_fldFn.substing = function (offset, len) {
	var v;
	if (this.canContainBig5()) {
		v = IfxUtl.substr_big5(this.value(), offset, len);
	} else {
		v = this.value().substr(offset, len);
	}
	return v;
};
_fldFn.serialize = function () {
	var v, attr, h, e;
	v = this.getValue();
	h = this.ui().is(':visible') ? 'h' : 'H';
	e = this._enabled ? 'E' : 'e';
	attr = h + e;
	return {
		v: v,
		a: attr
	};
};
_fldFn.deserialize = function (o) {
	var v = o['v'],
		attr = o['a'],
		h = attr.charAt(0),
		e = attr.charAt(1);
	this.setValue(v);
	/*
	 * 柯:不再介入VAR欄位,純粹給值 (因部分只有隱藏/顯示行,沒有獨立到欄位) if (h == 'H') this.ui().hide(); this.enabled(e ==
	 * 'E');
	 */
};
// end of runtime field
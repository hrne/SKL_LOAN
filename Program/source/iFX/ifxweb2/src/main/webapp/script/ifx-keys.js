var IfxKeys = (function () {
	// Module begin
	function IfxKeys(ifx) {
		this.ifx = ifx;
		//bindSysKeys();
	}
	// key scancode
	var Keys = {
		K_F1: 112,
		K_F2: 113,
		K_F3: 114,
		K_F4: 115,
		K_F5: 116,
		K_F6: 117,
		K_F7: 118,
		K_F8: 119,
		K_F9: 120,
		K_F10: 121,
		K_F11: 122,
		K_F12: 123,

		//Navigation: HOME, END, LEFT ARROW, RIGHT ARROW, UP ARROW, DOWN ARROW
		K_PGUP: 33,
		K_PGDN: 34,
		K_END: 35,
		K_HOME: 36,
		K_LEFT: 37,
		K_UP: 38,
		K_RIGHT: 39,
		K_DOWN: 40,

		//System: ESC, SPACEBAR, SHIFT, TAB
		K_TAB: 9,
		K_ESC: 27,
		K_SHIFT: 16, // shift tab
		K_BKSP: 8,
		K_SPACE: 32,
		K_DELETE: 46,
		K_ENTER: 13,

		// my virtual key
		VK_NEXTFIELD: 10000,
		VK_PREVFIELD: 10001,
		VK_SUBMIT: 10002,
		VK_EC: 10003,
		VK_RELEASE: 10004,
		VK_DUMP1: 10005,
		VK_DUMP2: 1006,
		VK_ESCAPE: 10007,
		VK_EXPLAIN: 10008,
		VK_DUPFLD: 10009,
		VK_RESUME: 10010,
		VK_TOGGLE_HELP: 10011,
		VK_DUMP3: 10012,
		VK_COPYTITA: 10013,
		VK_LOGOFF: 10014

	};
	//delete Keys.K_ESC to avoid when the program was done and need to esc the program .
	//有需要加入 Keys.K_TAB ?
	var _naviKeys = [Keys.K_ENTER, Keys.K_LEFT, Keys.K_UP, Keys.K_RIGHT, Keys.K_DOWN];
	function isNavigationKey(keyCode) {
		return _naviKeys.indexOf(keyCode) != -1;
	}


	//end of keycode defines

	function translateKey(oEvent) {
		var decorator = "";
		var kcode = oEvent.keyCode;
		if (oEvent.altKey || oEvent.ctrlKey || oEvent.shiftKey) {
			if (kcode >= Keys.K_F1 && kcode <= Keys.K_F12) {
				if (oEvent.altKey) decorator += "A";
				if (oEvent.ctrlKey) decorator += "C";
				if (oEvent.shiftKey) decorator += "S";
				if (decorator != "") {
					// return "C.F1" --> Ctrl + F1
					// AC.F2 --> Alt+Ctrl+F2,....
					var t = decorator + ".F" + new String((oEvent.keyCode - Keys.K_F1 + 1));
					return t;
				}
			} else if (kcode == Keys.K_TAB && oEvent.shiftKey) {
				return "S.TAB";
			}
			// ���B�z Alt,Ctrl, Shift  + key
			return 0;
		}
		return kcode;
	}

	var _naviKeyEnabled = true;
	IfxKeys.prototype.enableNaviKeys = function (enabled) {
		console.log("# # # enableNaviKeys:" + enabled);
		_naviKeyEnabled = enabled;
	};


	var _blocking = false;
	IfxKeys.prototype.blocking = function (enabled) {
		_blocking = enabled;
		//console.log('key blocked:' + _blocking);
	};
	var keymapper = null;

	var $this = null;
	IfxKeys.prototype.bind = function (bRebind) {
		console.log("IfxKeys.prototype.bind");
		if (bRebind) {
			this.unbindAll();
		}

		keymapper = new KeyMapper(this.ifx);

		console.log("System key bound");
		$(document).keydown(function (evt) {
			console.log("IfxKeys.prototype.keydown");
			if (_blocking) {
				console.log('blocked, no working now');
				return false;
			}
			console.log("key " + evt.keyCode + " pressed!");
			if (!_naviKeyEnabled) {
				console.log("navigation key disable mode");
				if (isNavigationKey(evt.keyCode) && evt.target.parentElement.id != "input_pager") {
					console.log("is navigation key, ignore it");
					return false;
				}

			}
			if (evt.keyCode == 8 && !(evt.target.type == "text" || evt.target.type == "textarea")) {
				console.log("Can not return the page!!");
				return false;
			}
			if (evt.keyCode == 8 && (evt.target.type == "text" || evt.target.type == "textarea") && evt.target.readOnly == true) {
				console.log("Can not return the page!!");
				return false;
			}
			$this = $(this);
			if (evt.target.parentElement.id != "input_pager")
				keymapper.perform(evt, translateKey(evt));
			//			if (evt.keyCode == 9) {  //tab pressed
			//			        evt.preventDefault(); // stops its action
			//			    }


		});

		$(document).keypress(function (evt) {
			console.log("IfxKeys.prototype.keypress");
			var $target = $(evt.target);
			if ($target.is("textarea")) {
				IfxUtl.textareaFilter(evt, $target);
			}
		});
	};
	IfxKeys.prototype.unbindAll = function () {
		console.log("IfxKeys.prototype.unbindAll");
		$(document).off('keydown keypress');
	};
	// TODO: refactor unbind for every key
	IfxKeys.prototype.unbindNavigationKeys = function () {
		keymapper.unDefine(Keys.K_ENTER);
		//柯 新增 更新類交易送出後需無法PREVFIELD OR NEXTFIELD
		//下
		keymapper.unDefine(Keys.K_DOWN);
		keymapper.unDefine(Keys.K_TAB);
		//上
		keymapper.unDefine(Keys.K_UP);
		keymapper.unDefine("S.TAB");
	};
	// KeyMapper class
	function KeyMapper(ifx) {
		this.ifx = ifx;
		var self = this;
		this.map = {};

		// setup key transform map
		this.map[Keys.K_UP] = Keys.VK_PREVFIELD;
		this.map["S.TAB"] = Keys.VK_PREVFIELD;
		this.map[Keys.K_DOWN] = Keys.VK_NEXTFIELD;
		this.map[Keys.K_ENTER] = Keys.VK_NEXTFIELD;
		this.map[Keys.K_TAB] = Keys.VK_NEXTFIELD;
		this.map[Keys.K_ESC] = Keys.VK_ESCAPE;
		this.map[Keys.K_F2] = Keys.VK_TOGGLE_HELP;//當下欄位開關HELP
		this.map[Keys.K_F3] = Keys.VK_EC;
		this.map[Keys.K_F4] = Keys.VK_LOGOFF;
		this.map[Keys.K_F6] = Keys.VK_DUPFLD;//同交易上次送出之欄位內容
		this.map[Keys.K_F7] = Keys.VK_RESUME;
		this.map[Keys.K_F9] = Keys.VK_EXPLAIN;//開起說明網頁 ifx/runtime/explain/交易代號
		this.map[Keys.K_F10] = Keys.VK_SUBMIT;  //送出交易
		this.map["C.F7"] = Keys.VK_DUMP1;
		this.map["C.F8"] = Keys.VK_DUMP2;
		this.map["C.F6"] = Keys.VK_DUMP3;

		this.map["S.F6"] = Keys.VK_COPYTITA;
		// add your key mapping above


		var rtnMap = {};
		rtnMap[Keys.VK_SUBMIT] = new KeyDef(Keys.VK_SUBMIT, "transaction sumbit", function (event) {
			console.log("press submit.(trigger mousedown)");
			$('#btn_yn').trigger('mousedown');
			event.preventDefault();
		});

		var defNextfield = new KeyDef(Keys.VK_NEXTFIELD, "next field", function (event) {
			var $target = $(event.target);
			console.log("\n------------------------\nPress KEYS.NEXTFIELD keyCode" + event.keyCode + ",target:" + $target.attr('id') + ", " + $.now());
			console.log("press nextfield:" + event.keyCode);
			if ($target.is("input") && event.target.id.indexOf("btn_") != -1) {
				console.log("on button, stop here");
				self.ifx.clearLastField();	//柯 測試
				//				 $("#" + event.target.id).trigger("click");
				//柯:加入 ENTER 不能送
				if (event.keyCode == Keys.K_TAB || event.keyCode == Keys.K_ENTER) {
					console.log("請使用滑鼠送出.");
					event.preventDefault();
				}
				return;
			}

			// var button, let click handler invoke nextfield
			// 鍾 按鈕需要按entry 才可執行
			if ($target.is(":button") && $target.hasClass('field_button') && event.keyCode == 13) {
				// end
				return;
			}

			if ($target.is("textarea") && (event.keyCode == Keys.K_ENTER || event.keyCode == Keys.K_DOWN)) {
				console.log("textarea");
				if (!IfxUtl.atLastLine(event, $target, event.keyCode == Keys.K_ENTER)) {
					return;
				}
				event.preventDefault();
			}
			//柯 新增此段 start
			if ($target.is("textarea") && (event.keyCode == Keys.K_TAB)) {
				console.log("textarea tab");
				if (!IfxUtl.textareaFilter(event, $target, true)) {
					return;
				}
				event.preventDefault();
			}
			//end
			if (!self.ifx.isSwiftMode) {
				console.log("defNextfield-notSwiftMode");
				if (isSameTarget()) {
					self.ifx.KeyForward('call ifx-core2 key down handler');
					event.preventDefault();
				}
				//event.preventDefault();

				function isSameTarget() {
					var fld = self.ifx.getCurrentField();
					if (fld == null) return false;
					console.log("now current field id:" + fld.id() + ", event target id:" + $target.attr('id'));
					return (fld.id() == $target.attr('id'));
				}
			} else {
				console.log("defNextfield-isSwiftMode");
				self.ifx.KeyForward('key down handler');
				event.preventDefault();
			}

			//if(isSameTarget()) {
			//	self.ifx.KeyForward('call ifx-core2 key down handler');
			//	//小柯1030
			//	event.preventDefault();
			//}
			////event.preventDefault();
			//
			//function isSameTarget(){
			//	var fld = self.ifx.getCurrentField();
			//	if(fld==null) return false;
			//	console.log("now current field id:"+fld.id() + ", event target id:"+  $target.attr('id'));
			//	return (fld.id() == $target.attr('id'));
			//}

			/*小柯   快速ENTER問題
			console.log("in field");
//				if(self.ifx.KeyForward('key down handler'))
//					event.preventDefault();
			self.ifx.KeyForward('key down handler');
			event.preventDefault();


			*/


		});
		rtnMap[Keys.VK_NEXTFIELD] = defNextfield;

		rtnMap[Keys.VK_PREVFIELD] = new KeyDef(Keys.VK_PREVFIELD, "prev field", function (event) {
			console.log("press previous key");
			var $target = $(event.target);
			if ($target.is("textarea") && event.keyCode == Keys.K_UP) {
				if (!IfxUtl.atFirstLine(event, $target)) {
					return;
				}
			}
			//					if(event.keyCode == Keys.K_TAB) {
			//						console.log("is shift tab, oh no");
			//						event.preventDefault();
			//						return;
			//					}
			self.ifx.KeyBackward();
			event.preventDefault();
		});

		rtnMap[Keys.VK_EC] = new KeyDef(Keys.VK_EC, "EC", function (event) {
			event.preventDefault();
			console.log("press EC key");
			//	ifx.KeysEC();
		});
		rtnMap[Keys.VK_LOGOFF] = new KeyDef(Keys.VK_LOGOFF, "LOGOFF", function (event) {
			event.preventDefault();
			console.log("press LOGOFF key");
			parent.addTran("LC101", "tran2.jsp?txcode=LC101");
		});
		rtnMap[Keys.VK_COPYTITA] = new KeyDef(Keys.VK_COPYTITA, "COPYTITA", function (event) {
			event.preventDefault();
			console.log("press copy tita key");
			ifx.KeysCopyTita();
		});
		rtnMap[Keys.VK_ESCAPE] = new KeyDef(Keys.VK_ESC, "escape", function (event) {
			event.preventDefault();
			console.log("press ESC key");
			ifx.KeysEscapeTran();
		});
		rtnMap[Keys.VK_DUPFLD] = new KeyDef(Keys.VK_DUPFLD, "dup field", function (event) {
			event.preventDefault();
			console.log("press dup field key");
			ifx.KeysDupfield();
		});
		rtnMap[Keys.VK_RESUME] = new KeyDef(Keys.VK_RESUME, "Resume Tran", function (event) {
			event.preventDefault();
			console.log("press resume tran Key");
			ifx.resumeTran(_tmpfile);
		});
		rtnMap[Keys.VK_EXPLAIN] = new KeyDef(Keys.VK_EXPLAIN, "open explain html", function (event) {
			event.preventDefault();
			console.log("press open explain html key");
			var settings = 'menubar=no, toolbar=no, location=no, directories=no, status, scrollbars, resizable, dependent=yes, width=800, height=600, left=0, top=0';
			var exname = ifx.getExplainFilename();
			if (exname != null) {
				window.open(exname, 'explain', settings);
			} else {
				alert("此交易無說明訊息");
			}
		});

		rtnMap[Keys.VK_TOGGLE_HELP] = new KeyDef(Keys.VK_TOGGLE_HELP, "toggle help list", function (event) {
			event.preventDefault();
			console.log("press toggle help list key");
			ifx.KeysToggleHelpList();
		});



		rtnMap[Keys.VK_DUMP1] = new KeyDef(Keys.VK_DUMP1, "dump1", function (event) {
			ifx.dumpVar(1);
		});
		rtnMap[Keys.VK_DUMP2] = new KeyDef(Keys.VK_DUMP2, "dump2", function (event) {
			ifx.dumpVar(2);
		});
		rtnMap[Keys.VK_DUMP3] = new KeyDef(Keys.VK_DUMP3, "dump3", function (event) {
			ifx.dumpVar(3);
		});

		this.rtnMap = rtnMap;

		this.transform = function (scannedKeyCode) {

			var vkey = null;
			vkey = this.map[scannedKeyCode];
			return vkey;
		};
		this.perform = function (event, scannedKeyCode) {
			var bStop = false;
			var vkcode = this.transform(scannedKeyCode);
			if (vkcode == null) return;

			var oKeydef = this.rtnMap[vkcode];
			if (oKeydef != null) {
				bStop = oKeydef.execute(event);
			}
			if (bStop) {
				event.preventDefault();
			}
			return bStop;

		};

		this.unDefine = function (keyCode) {
			delete this.map[keyCode];
		};

	}
	// end of KeyMapper class

	// KeyDef class
	function KeyDef(vkcode, name, rtn) {
		this.vkcode = vkcode; // virtual key code, not scaned code
		this.name = name;
		this.rtnArray = null;
		this.bStop = false;
		this.add = function () {
			if (this.rtnArray == null) {
				this.rtnArray = new Array();
			}
			var numargs = arguments.length;
			for (var i = 0; i < numargs; i++) {
				this.rtnArray.push(arguments[i]);
			}
		};
		this.addRange = function (fArray) {
			for (var i = 0; i < fArray.length; i++) {
				this.add(fArray[i]);
			}
		};
		this.remove = function (fptr) {
			// todo:	 remove fptr from rtnArray;
		};
		this.execute = function (event) {
			// execute rtnArray;
			this.bStop = false;
			try {
				if (this.rtnArray == null) return;
				for (var i = 0; i < this.rtnArray.length && !this.bStop; i++) {
					routine = this.rtnArray[i];
					this.bStop = routine(event);
				}
			} catch (ex) {
				this.bStop = true;
				alert(ex.message);
			}

			return this.bStop;
		};
		this.toString = function () {
			var t = "";
			t += "KeyDef object\n";
			t += this.name + "\n";
			if (this.rtnArray != null) {
				t += "total " + this.rtnArray.length + " routines mapped\n";
				t += this.rtnArray.join("\n");
			}
			else {
				t += "no routine mapped\n";
			}
			return t;
		};


		for (var i = 2; i < arguments.length; i++) {
			this.add(arguments[i]);
		}


	}
	//end of KeyDef class


	//end of IfxKeys Module
	return IfxKeys;
}());
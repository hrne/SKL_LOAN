//備註:封面內頁寫法需改成PFN式讓使用者自訂
var ifxPsbk = (function($) {
	var BOOK_LINES = {
		total : 24, // 存摺總行數
		topPage : 12, // 上半頁行數
		marginTop : 3, // 頁首跳幾行
		centerfold : 2
	// 中間跳幾行(12 - 13行之間)
	}
	var def = null; // 存摺列印完成, 通知列印其餘單據
	var prnApi; // 列印介面
	var fyi_id = "#_psbk_fyi_dialog";
	var ifx;
	var useFakePB = false;
	var sessionObtained = false;
	var self;
	var check = true;
	var curcoverlo;
	var showalert;
	// aIfx : ifx
	// divId:顯示存摺處理訊息, 在tran2.jsp加入
	// <div id="_psbk_fyi_dialog" style="display:none">
	// 存摺列印中....
	// </div>
	function PB(aIfx, divId) {
		self = this;
		ifx = aIfx;
		fyi_id = '#' + divId;
		def = $.Deferred();
		$(fyi_id).dialog({
			autoOpen : false,
			modal : true,
			title : '列印',
			width : 600,
			height : 100
		});
		$(fyi_id).parent().children().children('.ui-dialog-titlebar-close').hide();
	}
	PB.prototype.setalert = function(){
		showalert = true;
	};
	// 使用畫面存簿
	PB.prototype.useFakePrinter = function(div) {
		prnApi = new _FakePrnApi(div);
		useFakePB = true;
	};
	// 使用真的存簿
	PB.prototype.useTruePrinter = function() {
		prnApi = new PsbkPrintApi();
		useFakePB = false;
	};
	PB.prototype.reset = function() {
		def = null;
	};
	PB.prototype.checksession = function() {
		return sessionObtained;
	};
	// ifx等待此promise(), 註冊always function來繼續列印其餘單據
	PB.prototype.promise = function() {
		console.log("create main deferred");
		def = $.Deferred();
		return def.promise();
	};
	// ifx將存摺之tota form傳給print()
	PB.prototype.printBlack = function(coverdata) {
		console.log("printBlack");
		printblack("磁條", coverdata);

		function printblack(oPname, coverdata) {
			// check psbk inserted
			prnApi.open(function() {
				$(fyi_id).dialog("open");
				setTimeout(yesPrint(), 10);
			}, cancelPrint, oPname);

			function cancelPrint() {
				def.reject("user cancelled");
			}

			function yesPrint() {
				if (useFakePB) {
					console.log("printblack.useFakePB:" + useFakePB);
					def.reject("user cancelled");
				}
				do {
					pdevicex.obtainSession();
					sessionObtained = true;
					var cpi = 13.3; // 10;12;13.3;15 //應該改成13 會比較好
					var lpi = 5; // 3;4;5;6;8
					pdevicex.setCpi(cpi);
					pdevicex.setLpi(lpi);
					var rbrnobig = IfxUtl.halfToFull(coverdata.rbrno);
					var locadata = "合庫" + rbrnobig + " 記帳員：" + coverdata.tlrno + "  "
							+ coverdata.ddate.slice(1, 4) + "-"
							+ coverdata.ddate.slice(4, 6) + "-"
							+ coverdata.ddate.slice(6, 8) + "  "
							+ coverdata.dtime.slice(0, 2) + ":"
							+ coverdata.dtime.slice(2, 4) + ":"
							+ coverdata.dtime.slice(4, 6);
					prnApi.printloca(31, locadata);
					if (!pdevicex.flushPrintData()) {
						alert("文字列印失敗！");
						break;
					}
					if (!pdevicex.writeMSData(coverdata.black, false, locadata)) // 資料,退紙?
					{
						alert("writeMSData() 失敗！");
						break;
					}
					// def.resolve("done");
					if (sessionObtained) {
						if (!pdevicex.eject())
							alert("eject() 失敗！");
						if (!pdevicex.returnSession())
							alert("returnSession() 失敗！");
					}
				} while (false);
				$(fyi_id).dialog("close");
			}
		}
	};
	// "獨立" 讀取磁條 for check
	PB.prototype.getBlackcheck = function(blacktext) {
		console.log("blacktext:" + blacktext);
		pdevicex.obtainSession();
		if (blacktext.indexOf(":") == -1) {
			var one = blacktext.slice(blacktext.indexOf("A") + 1, blacktext.indexOf("E"));
			var two = blacktext.slice(blacktext.indexOf("E") + 1);
			blacktext = ":" + one + ">" + two;
		}
		console.log("change blacktext:" + blacktext);
		// pdevicex.obtainSession();
		var msData = pdevicex.readMSData(false, true);
		if (msData != null) {
			if (blacktext != msData) {
				pdevicex.eject();
				alert("存摺資料不符，請重新插入存摺。");
				// pdevicex.setPromptMessage("存摺資料不符，請重新插入存摺。");
				return false;
			}
		} else {
			if (!window.confirm("存摺磁條放置錯誤，是否列印?")) {
				pdevicex.eject();
				return false;
			}
		}
		pdevicex.returnSession();
		return true;
	};
	// "獨立" 讀取磁條
	PB.prototype.getBlack = function(prompttext, setfld, noeject) {
		console.log("getBlack" + prompttext + ",setfld:" + setfld);
		alertify.set('notifier', 'position', 'top-left');
		alertify.error("請插入存摺....");
		setTimeout(
				function getblack(oPname, setfld) {
					// check psbk inserted
					prnApi.open(function() {
						// $(fyi_id).dialog("open");
						setTimeout(yesPrint(), 10);
					}, cancelPrint, oPname, true); // 最後一個參數是為了要重新re bind
					function cancelPrint() {
						console.log("cancelPrint");
						ifx.keys.bind(true);
					}

					function yesPrint() {
						sessionObtained = true;
						var msData = null;
						if ((sessionObtained = pdevicex.obtainSession()))
							msData = pdevicex.readMSData(!noeject);
						else {
							alertify.error("連接存摺印表機失敗...請重啟周邊..");
						}
						/* 潘 20180126 識別碼空白詢問重讀存摺 */
						if (msData != null
								&& (msData.slice(msData.indexOf(">") + 2) == "" || msData
										.slice(msData.indexOf(">") + 2).trim().length < 4)) {
							while (window.confirm('存摺識別碼讀取為空白，是否重新讀摺?(請注意是否磁條已毀損!!)')) {
								msData = pdevicex.readMSData(!noeject);
								if (msData.slice(msData.indexOf(">") + 2) != ""
										|| msData.slice(msData.indexOf(">") + 2).trim().length >= 4)
									break;
							}
						}
						var msDatatmp = [];
						if (msData != null) {
							// alert("readMSData() 成功：\"" + msData + "\""+",setfld:"+setfld);
							// 回傳格式依照type來切割 ":9997188543216>14243"
							msDatatmp[0] = msData;
							msDatatmp[1] = msData.slice(msData.indexOf(":") + 1, msData
									.indexOf(">"));
							msDatatmp[2] = msData.slice(msData.indexOf(">") + 1, msData
									.indexOf(">") + 2);
							msDatatmp[3] = msData.slice(msData.indexOf(">") + 2);
							// K(PSBACC/紙張/ 0全部/ 1存摺帳號 /2存摺摺號/ 3存摺識別碼/回傳欄位)
							if (setfld.length > 0) {
								for (var i = 0; i < setfld.length; i++) {
									if (setfld[i]) {
										var flttemp = setfld[i].split('#');
										for (var j = 1; j < flttemp.length; j++) {
											ifx.setValue("#" + flttemp[j], msDatatmp[i]);
										}
									}
								}
								ifx.setValue("PBREAD$", 1); // 已讀取磁條記號
								ifx.setValue("MSRBUF$", msData); // 已讀取磁條內容
							}
						} else {
							// alert("readMSData() 失敗！");
							if (sessionObtained)
								alertify.error("讀取磁條失敗...");
						}
						// def.resolve("done");
						if (sessionObtained) {
							if (!noeject) {
								// if (!pdevicex.eject()) //20180115
								// alert("eject() 失敗！"); //20180115
								if (!pdevicex.returnSession())
									alert("returnSession() 失敗！");
							}
						}
						ifx.keys.bind(true);
					}
				}, 1500, prompttext, setfld);
	};
	// ifx將存摺之tota form傳給print()
	PB.prototype.print = function(oPsbk, rePsbk) {
		if (oPsbk.fake) { // 測試用假資料
			oPsbk = convertTota(oPsbk);
		}
		if (oPsbk == null) {
			def.reject("pb format error");
		} else {
			printLines(oPsbk, rePsbk, "存摺內頁");
		}
	};

	function getTabFn(name) {
		// return top.frames['easytab'][name];
		return window.parent[name];
	}
	// 主參數,起始,上次日期,上次金額
	// TODO: 需求一直變動.寫得很雜..TEST OK再改
	function printLinezero(oPsbk, isfirst, lastlinedata, lastlineamt) {
		var tmpdata = "";
		if (isfirst) {
			tmpdata = oPsbk.date;
			if (tmpdata.length == 0)
				return;
		} else {
			tmpdata = lastlinedata;
		}
		if (oPsbk.prntype == 0) { // 一般存摺
			prnApi.print(0, "   " + tmpdata + "                             "
					+ oPsbk.acnumber);
		} else if (oPsbk.prntype == 2 && !isfirst) { // 黃金存摺
			prnApi.print(0, "  " + tmpdata + "                            "
					+ oPsbk.acnumber + "           " + lastlineamt);
		}
	}
	var pdevicex;
	// rePsbk to check
	function printLines(oPsbk, rePsbk, oPname) {
		console.log("print pb....");
		// check psbk inserted
		if (!rePsbk) { // if resend psbk don't open, just yesPrint
			prnApi.open(function() {
				$(fyi_id).dialog("open");
				var nTimeout = 10;
				if (oPsbk.fake)
					nTimeout = 1000;
				setTimeout(yesPrint(false), nTimeout);
			}, cancelPrint, oPname);
		} else {
			var nTimeout = 10;
			if (oPsbk.fake)
				nTimeout = 1000;
			setTimeout(yesPrint(true), nTimeout);
		}

		function cancelPrint() {
			// $(fyi_id).dialog("close");
			def.reject("user cancelled");
		}

		function yesPrint(reSpsbk) {
			if (!useFakePB && !reSpsbk) {
				var blackcheck = false;
				var checkmsdata = ifx.getValue("MSRBUF$"); // ifx.getValue("MSRBUF$")
															// //:1058188000985>10000
				/* 潘 20180307 印存摺不檢核 */
				/*
				 * do{ if(!checkmsdata){ break; } console.log("磁條檢測須符合:"+checkmsdata); blackcheck =
				 * self.getBlackcheck(checkmsdata);// if(blackcheck){ break; } }while (true) //end
				 */
				pdevicex.obtainSession();
				sessionObtained = true;
				var cpi = 13.3; // 10;12;13.3;15
				var lpi = 5; // 3;4;5;6;8
				pdevicex.setCpi(cpi);
				pdevicex.setLpi(lpi);
			}
			if (reSpsbk) {
				// ifx.diyblock("存摺列印中....");
			}
			var currLine = 0;
			// 存摺第0行的 日期+ 帳號
			if (oPsbk.startLine == 1) {
				printLinezero(oPsbk, true); // is first
			}
			// 紀錄上次日期
			var lastlinedata = "";
			var lastlineamt = "";
			for (var i = 0; i < oPsbk.totalLines; i++) {
				currLine = oPsbk.startLine + i;
				if (currLine == BOOK_LINES.total + 1) {
					printLinezero(oPsbk, false, lastlinedata, lastlineamt);
				}
				console.log("印:" + currLine + "," + oPsbk.lines[i]);
				// if(i == oPsbk.totalLines -1 && oPsbk.hasMore() === false){ //柯 新加
				// console.log("最後一行");
				// prnApi.print(currLine, oPsbk.lines[i],false); //contprin
				// }else{
				prnApi.print(currLine, oPsbk.lines[i]); // contprin
				// }
				lastlinedata = oPsbk.linedate[i]; // 當次日期紀錄
				if (oPsbk.prntype == 2) {
					lastlineamt = oPsbk.linepbbal[i]; // 當次黃金餘額
				}
			}
			console.log("oPsbk.hasMore():" + oPsbk.hasMore());
			if (oPsbk.hasMore() === false) {
				console.log("done currLine:" + currLine);
				ifx.diyunblock(); // 取消block
				// 剛好滿行 補印下一頁第0行
				if (currLine == BOOK_LINES.total) {
					printLinezero(oPsbk, false, lastlinedata, lastlineamt);
					console.log("currLine == BOOK_LINES!");
				}
				prnApi.eject(); // 都踢出
				console.log("def.resolve(done)");
				def.resolve("done");
			} else {
				getMore(oPsbk.returnText);
			}
			$(fyi_id).dialog("close");
		}

		function getMore(returnText) {
			console.log("* * * get more lines");
			ifx.sendPsbkReturn(returnText, function(bSuccess, oData) {
				if (!bSuccess) {
					def.reject(oData.errmsg); // get error message from tota
				} else {
					// 柯:更改成三筆三筆印，不要收完(滿頁)才印
					pdevicex.flushPrintData();
					setTimeout(function() {
						self.print(oData, true); // true to rePsbk
					}, 100);
				}
			}, oPsbk.prntype);
		}
		/*
		 * function fyiMessage(bON ,fn) { var id = "_psbk_fyi_dialog", dialogDiv = "<div id='" + id +
		 * "'></div>"; if (bON) { $(dialogDiv).appendTo('body') .html('<div ><h2>' + '存摺列印中' + '</h2></div>')
		 * .dialog({ my: "center", at: "center", of: window, modal: true, title: '列印中', zIndex:
		 * 10000, width: 600, resizable: false, closeOnEscape: false }); $("#" +
		 * id).parent().children().children('.ui-dialog-titlebar-close').hide();
		 * 
		 *  } else { $("#" + id).empty(); $("#" + id).remove(); } }
		 */
	}
	// 將tota form轉成存摺列印需要地格式
	function convertTota(oTota) {
		// 從MSGID:FS001 格式轉成存摺列印格式
		var oPsbk = {
			lines : [],
			hasMore : function() {
				return this.more == 1;
			},
			getReturnText : function() {
				return this.returnText;
			}
		};
		if (oTota.fake == undefined) {
			// TODO:真的tota, 目前不知道格式
			alert("TODO: 轉換TOTA");
			return null;
		} else {
			// 獨立測試用 (假tota)
			oPsbk.startLine = oTota.startLine;
			oPsbk.totalLines = oTota.totalLines;
			oPsbk.more = oTota.more;
			oPsbk.returnText = oTota.returnText;
			oPsbk.fake = oTota.fake;
			for (var i = 0; i < oPsbk.totalLines; i++) {
				oPsbk.lines.push("this is line " + (oPsbk.startLine + i) + "!!");
			}
			return oPsbk;
		}
	}
	// 真的存摺印表機介面
	function PsbkPrintApi() { // startInfo
		var skipControl = new SkipControl(this);
		var paperInserted = false;
		var waittime = 10;
		curcoverlo = 0;
		// connect to printer
		this.open = function(fnInserted, fnCancel, psbkprompt, getblack) {
			if (paperInserted && !getblack) {
				console.log("paper inserted already");
				setTimeout(fnInserted, 1);
				return;
			}
			console.log("PSBK:connect to psbk");
			// init()
			var fn = getTabFn('getDevice');
			pdevicex = fn();
			if (!pdevicex.init()) {
				waittime = 100;
			} else {
				waittime = 10;
			}
			/* 潘 20180308存摺不詢問 */
			paperInserted = true;
			if (showalert){
				alertify.set('notifier', 'position', 'top-left');
				alertify.error("請插入存摺內頁....");
				setTimeout(fnInserted, 1000);
				showalert = false;
			}				
			else
				setTimeout(fnInserted, waittime);
			// confirmDialog("存摺印表機", "請插入"+psbkprompt, function () {
			// paperInserted = true;
			// setTimeout(fnInserted, waittime);
			// }, fnCancel,getblack);
		};
		// 插紙
		this.insert = function() {
			console.log("PSBK:please insert psbk");
		};
		// 紙插了沒?
		this.isInserted = function() {
			console.log("PSBK:is psbk inserted?");
		};
		// TODO:
		// open, insert, isInserted 必須視etabs agent介面而定
		// 存摺列印
		// line:存摺行次
		// text:該行內容 + 換行?
		this.print = function(line, text) {
			console.log("PSBK:print line:" + line + ", " + text);
			skipControl.beforePrint(line);
			// pdevicex.printLine(line + " " + text);
			pdevicex.printLine(text); // 移除 前面三個空白 在ifx-core2 中formatPsbkLine
			skipControl.afterPrint(line);
		}
		// 好像有問題
		this.printloca = function(line, text) {
			console.log("PSBK:print text:" + line + ", " + text);
			pdevicex.newLine(line - curcoverlo - 1);
			pdevicex.printLine(text, true); // 印最下面的不要換行，讓writeMSData可重覆在同樣位置再印一次
			curcoverlo = line;
		}
		// 換頁退紙
		this.eject = function() {
			console.log("PSBK:eject psbk");
			pdevicex.eject();
		}
		// 跳行
		this.skipLines = function(lines) {
			console.log("PSBK:skip ", lines);
			pdevicex.newLine(lines);
		}
	}

	function confirmDialog(title, message, fnYes, fnNo, getblack) {
		$('<div></div>').appendTo('body').html('<div><h4>' + message + '</h4></div>')
				.dialog({
					modal : true,
					title : title,
					zIndex : 10000,
					autoOpen : true,
					width : 600,
					resizable : false,
					draggable : false, // 因為拖曳會亂跑故 draggable
					buttons : {
						Yes : function() {
							// $(obj).removeAttr('onclick');
							// $(obj).parents('.Parent').remove();
							$(this).dialog("close");
							if (fnYes != null)
								fnYes();
						},
						No : function() {
							$(this).dialog("close");
							if (fnNo != null)
								fnNo();
						}
					},
					close : function(event, ui) {
						if (getblack) { // 柯 重新bind
							ifx.keys.bind();
						}
						$(this).remove();
					}
				});
	}
	;
	// 畫面存摺印表機
	// **介面必須參考PsbkPrintApi
	function _FakePrnApi(divFakePassbook) {
		var skipControl = new SkipControl(this);
		var paperInserted = false;
		var $fakePsbk = $('#' + divFakePassbook).dialog("open");
		$('tr', $fakePsbk).remove();
		// $fakePsbk.dialog("open");
		function fakePrint(s) {
			$("tbody", $fakePsbk).append("<tr><td>" + s + "</td></tr>");
		}
		this.open = function(fnInserted, fnCancel, psbkprompt) {
			if (paperInserted) {
				console.log("paper inserted already");
				setTimeout(fnInserted, 1);
				return;
			}
			console.log("PSBK:connect to psbk");
			confirmDialog("存摺印表機", "請插入" + psbkprompt, function() {
				paperInserted = true;
				setTimeout(fnInserted, 1);
			}, fnCancel);
		};
		this.insert = function() {
			console.log("PSBK:please insert psbk");
		};
		this.isInserted = function() {
			console.log("PSBK:is psbk inserted?");
		};
		this.print = function(line, text) {
			console.log("PSBK:print line:" + line + ", " + text);
			skipControl.beforePrint(line);
			fakePrint(line + " " + text);
			skipControl.afterPrint(line);
		}
		this.eject = function() {
			paperInserted = false;
			fakePrint("passbook ejected");
			console.log("PSBK:eject psbk");
		}
		this.skipLines = function(lines) {
			console.log("PSBK:skip lines", lines);
			while (lines--)
				fakePrint("---");
		}
	}
	// 跳行控制, 將存摺行次轉成實際行次 依據存簿之天地自動跳行
	function SkipControl(api) {
		var currpLine = 1;

		function getRealLine(psbkLine) {
			var line = BOOK_LINES.marginTop;
			if (psbkLine <= 12) {
				line += psbkLine;
			} else {
				line += BOOK_LINES.centerfold;
				line += psbkLine;
			}
			return line;
		}

		function skipTo(psbkLine) {
			if (psbkLine > 24) {
				psbkLine = psbkLine - BOOK_LINES.total;
			}
			var realLine = getRealLine(psbkLine);
			console.log("PSBK psbk line:" + psbkLine + ",real line:" + realLine
					+ ",currpLine:" + currpLine);
			var linesToSkip = realLine - currpLine;
			api.skipLines(linesToSkip);
			currpLine = realLine + 1; // 假設印完一行後會自動跳行
			console.log("after currpLine:" + currpLine);
		}
		// 列印每行資料前呼叫此function
		this.beforePrint = function(line) {
			skipTo(line);
		};
		// 列印每行資料後呼叫此function
		this.afterPrint = function(line) {
			if (line == BOOK_LINES.total) {
				currpLine = 1;
				// alert("line:"+line+",contprin:"+contprin);
				api.eject();
				// ifx.showPrompt("換頁續印提示",5);
				// ifx.fyi('換頁續印提示...', 1000);
				pdevicex.setPromptMessage("換頁續印提示"); // 無用 因順序問題
				ifx.diyblock("存摺內頁[換頁續印]", "存摺印表機");
				// alert("換頁續印提示");
				console.log("換頁續印提示");
				console.log("line==BOOK_LINES.tota:" + currpLine);
			}
		};
	}
	// --------------------------------------------------------
	return PB;
}(jQuery));
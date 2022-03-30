var IfxHost = (function () {
	var _ifx = null; // reference to Ifx {}
	function IfxHost(aIfx) {
		_ifx = aIfx;
	};

	function get(name) {
		return _ifx.getValue(name);
	}

	function putValue(name, value) {
		_ifx.setValue(name, value);
	}
	var _RIMTASKID = "CR";
	IfxHost.prototype.setTaskId = function (tid) {
		_RIMTASKID = tid;
	};
	var _noCombine = false;
	IfxHost.prototype.setNoCombine = function () {
		_noCombine = true;
	};
	IfxHost.prototype.parseHostOvrRequest = function (t, b) {
		var arr = [], r, t;
		t.forEach((m) => {
			arr.push([m.NO, m.MSG])
		});
		return arr;
	};
	IfxHost.prototype.parseTim = function (fields, t, arr) {
		//新增如果是JSON字串
		try {
			if (typeof t == "object" || typeof JSON.parse(t) == "object") {
				var arr = arr || [];
				if (typeof t != "object")
					t = JSON.parse(t);
				for (var i = 0; i < fields.length; i++) {
					var f = fields[i];
					if (t[fields[i].name.substring(1)]) {
						if (typeof f === 'string') {
							var len = IfxUtl.strlen(f);
							s = 'tita filler:[' + t[fields[i].name.substring(1)] + ']' + ", we defined:[" + f + "] " + IfxUtl.strlen(f);
						} else {
							s = f.name + ":[" + t[fields[i].name.substring(1)] + "] " + f.getTomLen();
							f.setValueFromTom(t[fields[i].name.substring(1)]);
						}
						arr.push(s);
					}
				}
				return arr;
			}
		} catch (e) {
			;
		}

		var commData = new CommData(t),
			value, f, len, arr = arr || [],
			s;
		for (var i = 0; i < fields.length; i++) {
			f = fields[i];
			if (typeof f === 'string') {
				// commData.skip(IfxUtl.strlen(f));
				len = IfxUtl.strlen(f);
				value = commData.get(len);
				s = 'tita filler:[' + value + ']' + ", we defined:[" + f + "] " + len;
				console.log(s);
			} else {
				// TODO 小柯 研究 測試中文tota問題
				value = commData.get(f.getTomLen());
				if (f.getTomType().toLowerCase() == "c") {
					var num4 = value.split(/\4/).length - 1;
					var num7 = value.split(/\7/).length - 1;
					console.log("parseTim getTom value:[" + value + "],num4:" + num4 + "num7:" + num7);
					if (num4 + num7 > 0 && num4 == num7) {
						value = value.replace(/\4/g, ''); // 小柯測試for合庫r6...
						value = value.replace(/\7/g, ''); // 小柯測試for合庫r6...
						console.log("parseTim getTom value:[" + value + "]");
						value = IfxUtl.stringFormatterBig5(value, f.getTomLen());
						console.log("parseTim getTom value with big5:[" + value + "]");
					}
				}
				s = f.name + ":[" + value + "] " + f.getTomLen();
				console.log(s);
				f.setValueFromTom(value);
			}
			arr.push(s);
		}
		return arr;
	};
	// TODO: refactor function name
	// IfxHost.prototype.parseHeader = function(t) {
	// var commData = new CommData(t);
	// var titaLabel = new TitaLabel();
	// titaLabel.deserialize(commData);
	//
	// console.log("titaLabel:");
	// console.log(titaLabel.toDebug());
	//
	// };
	var sim = false;
	var simTota = null;
	IfxHost.prototype.setSim = function (tota) {
		sim = true;
		simTota = tota;
	};
	// var ar = {};
	// var jnlHeader = {};
	// IfxHost.prototype.setTranArchive = function(aar, aHdr) {
	// ar = aar;
	// jnlHeader = aHdr;
	// };
	// function resetTranArchive() {
	// ar = {};
	// jnlHeader = {};
	//
	// }
	var msgMode = 0; // normal
	var nextText = '';
	var origSeq = 0;
	var totaBank = null;
	var totwCount = 0;
	var totaList;
	var sendTimes = 0;
	var jnlId = -1;
	var hostOvrMode = 0;
	var sendBefore = false;
	var psbkReturnMode = false; // true -> 存摺折返模式
	function _setJnlId(j) {
		jnlId = j;
		_ifx.setJnlId(j);
	}
	IfxHost.prototype.setNextBox = function (bFirstTime, j) {
		jnlId = j;
	};
	IfxHost.prototype.setHostOvrMode = function (j) {
		jnlId = j;
		hostOvrMode = 1;
	};
	IfxHost.prototype.setsendBefore = function (issend) {
		sendBefore = issend;
		_ifx.closeTabText("", true); // 鍾提只擋一次
		console.log("setsendBefore:" + issend);
	};
	IfxHost.prototype.getsendBefore = function () {
		console.log("getsendBefore:" + sendBefore);
		return sendBefore;
	};
	IfxHost.prototype.initSend = function (oEtc, isrim) {
		totaBank = new TotaList();
		msgMode = 0;
		totwCount = 0;
		totaList = [];
		sendTimes = 0;
		jnlId = -1;
		hostOvrMode = 0;
		console.log("initSendinitSend");
		if (!isrim) {
			sendBefore = true;
		}
		psbkReturnMode = false;
		if (oEtc != undefined) {
			psbkReturnMode = oEtc['psbkReturn'];
		}
	};
	var noPopup = false;
	// 柯 增第7個參數 傳入time out 時間
	IfxHost.prototype.send = function (txcd, bRim, text, fnReceive, obj, fnError, timeouttime, bMq, titasix, settxtno) {
		console.log("fakerim?" + bRim);
		console.log("text?" + text);
		if (titasix == undefined) titasix = text;
		if (_ifx.batchMode) {
			console.log("no popup message at host module");
			noPopup = true;
		}
		var self = this;
		if (sim) {
			processSim(fnReceive, obj);
			sim = false;
			return;
		}
		// inner functions
		var fnConfirmGetMore, // display get more dialog
			fnProcessReceived, // call ifx-core2 output process
			fnOnSuccess, fnOnError, fnOnComplete, // $.ajax's success/errro/always callback
			fnSendMain; // main send routine
		// get some variables
		var key = get("BRN$") + get("TLRNO$");
		var rqsp = $.trim(get("RQSP$"));
		var supno = $.trim(get("SUPNO$"));
		var level = $.trim(get("LEVEL$"));
		var pbrno = $.trim(get("#PBRNO"));
		// 手動更新TITA序號
		if (settxtno) {
			console.log("set new old TXTNO!!" + settxtno);
			_ifx.setValue("#TXTNO", settxtno);
		}
		var tita = buildTita(txcd, bRim, text);
		console.log("send times:" + sendTimes + ", msg mode:" + msgMode + "\ntita:" + tita);
		// start 小柯 增 只是不想junal id 太多，故 此LCR07 比照 XX005 辦理不需要更新TXTNO
		if (txcd == "LCR07") {
			msgMode = 1;
		}
		// end
		// change tita for msgMode=1
		if (msgMode == 1 && txcd != "LCR07") { // 新增 XXR07 主管授權 不會折返
			console.log("msgMode==1, change txcd to XX005");
			// replace txcd of tita
			// 小柯 此更改為var不同代號時，置換XX005有問題。
			// 注意 TITA長度如有變動，需調整此項目
			var tita1 = tita.slice(0, 34);
			var tita2 = "XX005";
			var tita3 = tita.slice(39, -1);
			console.log(tita1 + tita2 + tita3);
			tita = tita1 + tita2 + tita3;
			// tita = tita.replace(txcd, "XX005");
			tita = tita.slice(0, 180);
			console.log("XX005 tita:" + tita);
		}
		// 必須在XX005後,才能設定9
		// 9->平台不理會此序號
		// Ifx.fn.lastDataSend 的 XX006
		if (settxtno || !_ifx.isUpdatedTran(txcd)) {
			console.log("set msgMode!! 9 :" + txcd);
			msgMode = 9;
		}
		// ajax success
		fnOnSuccess = function (data) {
			// console.dir(data);
			if (data.status) {
				// 逾時重印 XX006交易 Only once time! START
				if (txcd == "XX006") {
					if ($("#xbtn_timeoutp").length > 0) {
						$("#xbtn_timeoutp").off('click').hide();
					}
				}
				// END
				// jnlId = data.jnlId;
				if (!bRim) {
					// TODO: 把值放入 CURJNLID$ 供 CORE2 function _save 時 存取.
					_setJnlId(data.jnlId);
				}
				console.log("jnlId:" + jnlId);
				console.log("tota:" + data.totaLi);
				var totwList = data.totaLi;
				// tota, split totw now
				totwCount += totwList.length;
				totaList = refactorTota(totwList, true);
				// var totaList = refactorTota(data.totw, true); // data.totw - server splited totw
				// array
				var lastTota = totaList[totaList.length - 1];
				console.log("lastTota form:" + lastTota.getTxForm());
				var lastMsgEnd = lastTota.getMsgEnd();
				var lastText = lastTota.getLastText();
				origSeq = data.origSeq; // save first tita txno
				// 注意:折返要配合一樣的MSGID
				if (lastMsgEnd == 2) {
					msgMode = 2;
					nextText = lastText;
					lastTota.removeLastTotw();

					requestNext();
					return;
				} else if (lastMsgEnd == 3) {
					nextText = lastText;
					var fnGetMore = function () {
						msgMode = 2;
						requestNext();
					};
					var nexter = {
						dialog: function (fnPostTran) {
							fnConfirmGetMore(this.fnMore, fnPostTran);
						},
						fnMore: function () {
							fnGetMore();
						},
						buttonized: false
					};
					lastTota.removeLastTotw();
					fnProcessReceived(nexter);
					return;
				}

				function requestNext() {
					requestNext.called = true;
					var sendAgain = function () {
						// text = text.slice(0,180) + lastText;
						self.send(txcd, bRim, text, fnReceive, obj, fnError, null, bMq, nextText);
					};
					setTimeout(sendAgain, 0);
				}
				fnProcessReceived();
			} else {
				if (!bRim) {
					_setJnlId(data.jnlId || -1);
				}
				var errordata = data.message.toString();
				var errorhead = data.errorhead.toString();
				var erheadtext = errorhead != "" ? errorhead : "";
				console.log("errordata:" + errordata + "|");
				errordata = errordata.replace(/\n/g, "");
				errordata = errordata.replace(/\r/g, "");
				console.log("error message:" + errordata + "|");
				console.log("error getsendBefore:" + self.getsendBefore() + "|");
				console.log("error indexOf0:" + errordata.indexOf("TIMEOUT") + "|");
				console.log("error indexOf1:" + errordata.indexOf("TIMEOUT!!") + "|");
				var titatxtno = data.origSeq.toString(); // 交易序號在errordata 也會有
				// 柯: 加入重印功能 transmit & timeout 顯示重印按鈕
				var istimeout = errordata.indexOf("TIMEOUT") != -1;
				var btnametimeout = false;
				if (istimeout && self.getsendBefore() && _ifx.isUpdatedTran() && _ifx.isNotxxTran()) {
					// 預設timeout 文字
					var timeoutmsg = "交易逾時狀態不明!請執行交易明細查詢確認是否成功，並解除控管!\n經辦逾時，查到後點選逾時重印，若交易失敗查不到作逾時解控，\n主管逾時，點選逾時解控，由放行再送出或XA976確認是否成功。";
					var sys_timeoutmsg = $.trim(get("TIMEOUTMSG$"));
					if (sys_timeoutmsg != "") {
						timeoutmsg = sys_timeoutmsg;
					}
					alert(timeoutmsg);
					btnametimeout = true;
					titatxtno = IfxUtl.numericFormatter(titatxtno, 8);
					_ifx.setValue("ERTXTNO$", titatxtno); // 此系統變數存錯誤的 TXTNO
					console.log("The last tota on time out!:" + titatxtno);
					/*
					 * //移除當下的逾時重印，只能去交易明細重印
					 * _ifx.lastDataSend("XX006","逾時重印",key+titatxtno,false,titatxtno);
					 */
				}
				// end
				var tmpObj = makeErrorTota(titatxtno + "-" + errordata + "|");
				if (!noPopup) {
					alert(tmpObj.getErrmsg());
				}
				_ifx.diyunblock();
				if (errordata.slice(0, 5) == "TT101") { // 賴提出:如有TT101就unblock -> 改全部都unblock
					console.log("請關閉瀏覽器，重新登入。");
					// alert("session有誤,請重新登入");
					// alert("session有誤,請重新登入");
				}
				// 原則上不會進入此段
				if (errordata.slice(0, 5) == "TT996") {
					$('#btn_yn').off("focus click");
					$('#btn_yn').attr("value", "交易終止");
				}
				if (errordata.slice(0, 5) == "TT999" && !bRim) { // 20180102 modify
					// console.log("Socket接收失敗，可能已進入中心邏輯但無回應。");
					$('#btn_yn').off("focus click");
					if (btnametimeout) {
						$('#btn_yn').attr("value", "已逾時!請由交易明細查詢點選逾時重印解除控管");
					} else {
						$('#btn_yn').attr("value", "交易停止");
					}
					// 新增除了查詢才有暫存
					if (_ifx.isUpdatedTran()) {
						console.log("自動暫存。"); // TODO SWIFT不存
						_ifx.trySave(false); // 柯新增交易直接幫忙暫存
					} else {
						console.log("查詢類，取消自動暫存。並關閉重作交易");
					}
					console.log("關閉[確認傳送]按鈕。");
					_ifx.KeysEscapeTran(); // 交易給使用者選擇關閉? 直接關閉問題太多
				}

				if (errordata.indexOf("connection timed out") != -1)
					IfxHost.prototype.setsendBefore(false);

				if (fnError != null) {
					fnError(tmpObj);
				} else { // 新增
					if (!noPopup) { // 應該是有跳出訊息才能unblock不然使用者不清楚發生何事??
						console.log("FnError=null,unblock.");
						_ifx.unblock(); // 改全部都unblock
					}
				}
			}
		};
		// ajax failed
		/*
		 * 0 － （未初始化）還沒有調用send()方法 1 － （載入）已調用send()方法，正在發送請求 2 － （載入完成）send()方法執行完成，已經接收到全部響應內容 3 －
		 * （交互）正在解析響應內容 4 － （完成）響應內容解析完成，可以在客戶端調用了
		 */
		fnOnError = function (data) {
			console.log("ajax error");
			console.log(data);
			_ifx.diyunblock();
			if (data.statusText === "abort") return;
			var tmpObj = makeErrorTota("通訊錯誤! status:" + data.status + ",readyState:" + data.readyState + ",statusText:" + data.statusText + ",responseText:" + data.responseText);
			console.log(tmpObj.getErrmsg());
			// 理論上前端必須要收到sna回覆訊息
			// && data.statusText != "timeout"
			if (!noPopup) {
				alert(tmpObj.getErrmsg());
			}
			if (fnError != null) {
				fnError(tmpObj);
			}
			if (timeouttime > 0 && data.statusText == "timeout" && !noPopup) {
				fntimeoutdiolog();
			}
		};
		// always
		fnOnComplete = function () {
			_ifx.rimSent = false; // 小柯 補增這行 0912
			// console.log("complete-ims-host");
			// resetTranArchive();
			// _ifx.unblock(); //移除柯 1015
			// 柯 新增 start
			if (bRim) {
				_ifx.unblock();
			}
			// end
		};
		// call receive handler
		fnProcessReceived = function (callback) {
			if (typeof fnReceive == "function") {
				fnReceive(totaList);
			} else {
				obj[fnReceive](totaList, callback);
			}
		};
		// display get more dialog
		fnConfirmGetMore = function (fnGetMore, fnPostTran) {
			$dlg = $("#dialog-confirm");
			$dlg.attr('title', "更多資料?");
			var t = "還有更多資料...，是否繼續?";
			$('#dialog-confirm-text').text(t);
			$dlg.dialog({
				resizable: false,
				height: 220,
				width: 400,
				modal: true,
				buttons: {
					"繼續": function () {
						$(this).dialog("close");
						setTimeout(fnGetMore, 0);
					},
					"不繼續": function () {
						fnPostTran();
						$(this).dialog("close");
						// setTimeout(fnProcessReceived,0);
						return false;
					}
				}
			});
			return false; // The actual submission of the form happens in the click handler for
			// the delete button
		};
		// 柯 test start
		fntimeoutdiolog = function () {
			$dlg = $("#dialog-confirm");
			$dlg.attr('title', "超過time out 時間");
			var t = "是否重送?";
			$('#dialog-confirm-text').text(t);
			$dlg.dialog({
				resizable: false,
				height: 220,
				width: 400,
				modal: true,
				buttons: {
					"重送": function () {
						$(this).dialog("close");
						var sendAgaintime = function () {
							self.send(txcd, bRim, text, fnReceive, obj, fnError, timeouttime, bMq);
						};
						setTimeout(sendAgaintime, 0);
					},
					"不重送": function () {
						$(this).dialog("close");
						return false;
					}
				}
			});
			return false; // The actual submission of the form happens in the click handler for
			// the delete button
		};
		// end
		fnSendMain = function () {
			_ifx.block('<h2>執行中(' + txcd + ')...<h2>');
			var data = {
				"txcd": txcd,
				"pbrno": pbrno,
				"key": key,
				"tita": tita,
				"titaRsix": titasix,
				"msgMode": msgMode,
				"origSeq": origSeq,
				"rim": bRim ? 1 : 0,
				"text": text,
				"jnlId": jnlId,
				"updTx": "0",
				"resv": "",
				"mq": bMq ? "true" : "false",
				"rqsp": _ifx.getRqspReasons(),
				"supno": supno,
				"level": level,
				"hostOvrMode": hostOvrMode,
				"psbkReturnMode": psbkReturnMode ? 1 : 0
			};
			// start
			console.log("******data['updTx'] = 0");
			console.log("bRim******" + bRim);
			console.log("chTisnotRim**" + checkTxcdisnotRim(txcd));
			console.log("txcd******" + txcd);
			console.log("pbrno******" + pbrno);
			console.log("hostOvrMode******" + hostOvrMode);
			console.log("mq******" + bMq);
			console.log("msgMode******" + msgMode);
			console.log("origSeq******" + origSeq);
			console.log("jnlId******" + jnlId);
			console.log("supno******" + supno);
			console.log("level******" + level);
			console.log("psbkReturnMode******" + psbkReturnMode);
			// 因為在整批時 會導致 ifx-ims-host.js中 data['updTx'] = "1"; 無法更改
			if (!bRim && checkTxcdisnotRim(txcd)) {
				console.log("******data['updTx'] = 1");
				data['updTx'] = "1";
			}
			// 新增此功能 依照ifx-core2 來做 (for 整批放行不能抓resv)
			// 移除 查詢類判斷 因為 查詢也要記 journal
			function checkTxcdisnotRim(txcd) {
				// 為了 XX005 時 不要可進入 prelogJournal
				if (msgMode == 1) {
					return false;
				}
				if (txcd[0] == "X") {
					return (txcd[2] != "R");
				} else {
					return (txcd[1] != "R");
				}
			}
			// end
			if (!bRim && jnlId == -1) { // first time
				data['resv'] = _ifx.buildResv();
				data['titaRsix'] = titasix;
			}
			// 柯 test start
			var timeoutt = timeouttime;
			var asynctype = true;
			// 最前端基本3分鐘
			if (timeoutt > 0) {
				timeoutt = timeoutt * 1000;
			} else {
				timeoutt = 300 * 1000;
			}
			// end
			// 柯:**為了在列印時的要同步處理,不然值無法回調**
			// 增加 getnotinField 確保是列印時產生的xwr01
			if (txcd == "XWR01" && _ifx.getnotinField() || _ifx.escSync) {
				console.log("XWR01 Change [async] to false!!!! ");
				asynctype = false;
			}
			var sender = $.ajax({
				type: 'POST',
				url: "send.jsp",
				cache: false,
				async: asynctype,
				data: data,
				dataType: "json",
				timeout: timeoutt,
				cache: false
			});
			sender.done(fnOnSuccess);
			sender.fail(fnOnError);
			sender.always(fnOnComplete);
			return sender;
		};
		return fnSendMain();
	};
	IfxHost.prototype.parseTmp = function (totaObj, fields) {
		parseTommy(totaObj, fields);
	};
	// 普通function轉成IfxHost for ifx-core2 viewJournal
	IfxHost.prototype.buildTitaLabelForRim = function (rimTxcd) {
		console.log("build rim tita label for:" + rimTxcd);
		var origTxcd = _ifx.getValue("#TXCD");
		console.log("tran txcd is saved:" + origTxcd);
		try {
			//			_ifx.setValue("#TXCD", rimTxcd); // 潘
			_ifx.setValue("#CALTM", _ifx.getValue("TIME$"));
			_ifx.setValue("#NBSDY", _ifx.getValue("NBSDY$"));
			_ifx.setValue("#NNBSDY", _ifx.getValue("NNBSDY$"));
			var tt = $.map(_titaLabelFields, function (x) {
				return '"' + x.substring(1) + '"' + ":" + '"' + _ifx.timFieldToHost(x) + '"' + ",";
			});
			console.log("end");
			return "{" + tt.join("");
		} catch (ee) {
			console.log(ee);
		} finally {
			_ifx.setValue("#TXCD", origTxcd);
			console.log("restore tran txcd :" + _ifx.getValue("#TXCD"));
		}
	};

	function parseTommy(totaObj, fields) {
		for (var i = 0; i < fields.length; i++) {
			var f = fields[i];
			var value = totaObj[f.name.substring(1)] ? totaObj[f.name.substring(1)] : "";
			if (totaObj[f.name.substring(1)] == undefined)
				console.warn(f.name + " not in totaObj field");
			console.log("setting " + f.name + " to [" + value + "] " + f.getTomLen());
			f.setValueFromTom(value);
		}
	}

	function makeErrorTota(msg) {
		var obj = {
			isError: function () {
				return true;
			},
			getErrmsg: function () {
				return msg;
			}
		};
		return obj;
	}

	function processSim(fnReceive, obj) {
		var totaList = refactorTota(getSimData(), true);
		if (typeof fnReceive == "function") {
			fnReceive(totaList);
		} else {
			obj[fnReceive](totaList);
		}
	}

	function getSimData() {
		return simTota;
	}

	function convertTota2TotwList(t) {
		// tak
		var msgLen, sTotw, totwList = [];
		var commData = new CommData(t);
		while (commData.hasMore()) {
			msgLen = commData.get(5); // get msglng
			console.log("msg length:" + msgLen);
			// 柯 新增主機上沒有init時可能導致loop..
			var intmsgLen = parseInt(msgLen.trim(), 10);
			if (intmsgLen <= 0 || isNaN(intmsgLen)) {
				alert("注意:hasMore.下行電文內容是否有誤? 請通知資訊人員.(msgLen:" + intmsgLen + ",soffset:" + commData.soffset + ",bufLen:" + commData.bufLen + ")" + ",t:" + t);
				console.log("注意:hasMore.下行電文內容是否有誤? 請通知資訊人員.(msgLen:" + intmsgLen + ",soffset:" + commData.soffset + ",bufLen:" + commData.bufLen + ")" + ",t:" + t);
				break;
			}
			commData.back(5);
			sTotw = commData.get(msgLen);
			totwList.push(sTotw);
		}
		console.dir(totwList);
		return totwList;
	}

	function refactorTota(arrTota, bCheck) {
		var oTota, totaMode;
		console.log(arrTota.length);
		console.dir(arrTota);
		// var totaList = new TotaList(); // reset by initSend
		for (var i = 0; i < arrTota.length; i++) {
			oTota = new Tota(arrTota[i]);
			totaMode = _ifx.getTotaMode(oTota.getTxForm());
			console.log("refactorTota 合併?:" + oTota.getTxForm() + ":" + totaMode);
			bCheck = totaMode == 0 ? true : false;
			// 警告訊息不要合併,
			if (oTota.isWarnning()) {
				bCheck = false;
				console.log("arrTota.isWarnning not bCheck!");
			}

			//通知書
			if (oTota.getTxForm() == "NO001") {
				//換行
				var text = oTota.obj.noticeMsg.replace(/\n/g, "<br>");
				//空白
				text = text.trim().replace(/\s/g, "&nbsp;");
				//字體顏色 粗細
				text = text.replace(/\{red-s\}/g, "<font color='red'>").replace(/\{blue-s\}/g, "<font color='blue'>").replace(/\{blue-e\}/g, "</font>").replace(/\{red-e\}/g, "</font>");
				text = text.replace(/\{b-s\}/g, "<b>").replace(/\{b-e\}/g, "</b>");

				$("#dialog-notice").html(text);

				$("#dialog-notice").dialog({
					autoOpen: true,
					draggable: false,
					resizable: false,
					modal: true,
					width: 900,
					maxWidth: 1100,
					close: function (event, ui) {
						$(this).dialog("destroy");
					},
					buttons: {
						"確定": function () {
							$(this).dialog("destroy");
						}
					},
					show: {
						effect: "blind",
						duration: 200
					}
				});
			}

			totaBank.add(oTota, bCheck); // //�N�ۦPformid��tota�X�֬��@��
		}
		var arr = totaBank.asArray();
		console.log("refactorTota:" + arr.length);
		return arr;
	};
	// modify from webtsa.hostran.js sendMain()
	function buildTita(txcd, bRim, text) {
		if (bRim) {
			text = IfxHost.prototype.buildTitaLabelForRim(txcd) + text; // 更改成ifxhost
		} else { // tita or psbk return mode
		}
		console.log("tita built:" + text);
		return text;
	}
	var _titaLabelFields = (function () {
		var _labelDef = "#KINBR#TLRNO#TXTNO#ENTDY#ORGKIN#ORGTLR#ORGTNO#ORGDD#OrgEntdy#TRMTYP" + "#TXCD#MRKEY#CIFKEY#CIFERR#HCODE#CRDB#HSUPCD#CURCD#CURNM#TXAMT#EMPNOT#EMPNOS" + "#CALDY#CALTM#MTTPSEQ#TOTAFG#OBUFG#ACBRNO#RBRNO#FBRNO#RELCD" + "#ACTFG#SECNO#MCNT#TITFALLZ#RELOAD#BATCHNO#DELAY#FMTCHK#FROMMQ#FUNCIND#LockNo#LockCustNo#AUTHNO#AGENT#ORGEMPNM#NBSDY#NNBSDY"; // TITFCD
		// ->
		// TITFALLZ
		// 使用收付欄記號永遠0
		// #BATCHNO
		// SECNO 在 LABEL.var內動手腳
		var arr = _labelDef.split('#');
		arr.shift();
		arr = $.map(arr, function (x) {
			return "#" + x;
		});
		return arr;
	}());

	function buildTitaLabelForRim_old(txcd) {
		var titaLabel = new TitaLabel();
		titaLabel.KINBR.setValue(get("BRN$"));
		titaLabel.TLRNO.setValue(get("TLRNO$"));
		// 第1碼，類別，值0~9，值由IFX前端決定，目前暫時放0。
		// 第2~8碼，序號，值由IFX Web遞增編號，1~50,000循環使用。
		// 因為會在WebServer遞增序號 所以Browser放0即可
		titaLabel.TXTNO.setValue("0");
		titaLabel.ENTDY.setValue(get("ENTDY$"));
		titaLabel.APTYPE.setValue(txcd.slice(0, 1));
		titaLabel.TXNO.setValue(txcd.slice(1));
		titaLabel.TOTAFG.setValue("1");
		return titaLabel.serialize();
	}

	function buildImsHeader(txcd) {
		var imsHeader = new ImsHeader();
		return imsHeader.serialize();
	}
	// -----------------------------------------------
	// Data structure
	// ----------------------------------------------------
	function HostField(name, len, type, oVar) {
		this.name = name;
		this.len = len;
		this.value = "";
		type = type || "X";
		this.type = String(type).toUpperCase();
		this.linkedVar = oVar;
		this.getLinkedVar = function () {
			return this.linkedVar;
		};
		this.setValue = function (value) {
			this.value = value;
		};
		this.serialize = function () {
			var v = '';
			if (this.type == "X") {
				v = IfxUtl.stringFormatter(this.value, this.len);
			} else if (this.type == "9") {
				v = IfxUtl.numericFormatter(this.value, this.len);
			}
			console.log(this.name + "==>" + v);
			return v;
		};
		this.deserialize = function (commDataObj) {
			var s = commDataObj.get(this.name, this.len, this.type);
			this.value = s;
		};
		this.valueOf = function () {
			return this.value;
		};
		this.toString = function () {
			return this.value;
		};
		this.toDebug = function () {
			var s = "";
			s += "name:" + this.name + ", ";
			s += "type:" + this.type + ", ";
			s += "length:" + this.len + ", ";
			s += "value:[" + this.value + "]";
			s += this.value.length;
			return s;
		};
	}
	// basic class of Header, Basic,...
	function TitaTotaType() {
		this.serialize = function () {
			var s = "";
			for (var i = 0; i < this.array.length; i++) {
				s += (this.array[i]).serialize();
			}
			return s;
		};
		this.deserialize = function (commDataObj) {
			var arr = this.array;
			var l = arr.length;
			for (var i = 0; i < this.array.length; i++) {
				if (this.array[i] != null)
					(this.array[i]).deserialize(commDataObj);
			}
		};
		this.toDebug = function () {
			var s = "";
			for (var i = 0; i < this.array.length; i++) {
				s += (this.array[i]).toDebug() + "  ^\n";
			}
			return s;
		};
		this.toString = function () {
			return this.toDebug();
		};
		this.debug = function (s) {
			var debugWin = window.open("", "debugTitaTota", "height=300,width=450,resizable=yes,scrollbars=yes");
			debugWin.document.writeln("<pre>" + s + "</pre>");
			debugWin.scrollBy(0, 1000);
		};
		this.toSysVar = function () {
			console.log("calling toSysVar");
			for (var i = 0; i < this.array.length; i++) {
				var f = this.array[i];
				if (f == null) continue;
				var svar = f.getLinkedVar();
				if (svar != null) {
					putValue(svar, f.valueOf());
					console.log("toSysVar:" + svar + "=" + get(svar));
				}
			}
			console.log("leave toSysVar");
		};
	}

	function ImsHeader() {
		TitaTotaType.call(this);
		this.length = 14;
		this.array = [this.LL = new HostField("LL", 2, "9"), // S9(2)Comp
		// 4-->2
		this.ZZ = new HostField("ZZ", 2, "9"), // S9(2)Comp 4-->2
		this.IMSTX = new HostField("IMSTX", 8), // 12
		this.FILLER_1 = new HostField("FILLER_1", 1), // 13
		];
	}

	function TitaLabel() {
		TitaTotaType.call(this);
		this.length = 180;
		this.array = [this.KINBR = new HostField("KINBR", 4, "9"), // 0
		this.TLRNO = new HostField("TLRNO", 6), // 4
		this.TXTNO = new HostField("TXTNO", 8, "9"), // 6
		this.ENTDY = new HostField("ENTDY", 8, "9"), // 14
		this.ORGKIN = new HostField("ORGKIN", 4, "9"), // 16
		this.ORGTLR = new HostField("ORGTLR", 2), // 20
		this.ORGTNO = new HostField("ORGTNO", 8, "9"), // 22
		this.ORGDD = new HostField("ORGDD", 2, "9"), // 30
		this.OrgEntdy = new HostField("OrgEntdy", 8, "9"),
		this.TRMTYP = new HostField("TRMTYP", 2), // 32
		this.APTYPE = new HostField("APTYPE", 1), // 34
		this.TXNO = new HostField("TXNO", 4), // 35
		this.MRKEY = new HostField("MRKEY", 20), // 39
		this.CIFKEY = new HostField("CIFKEY", 10), // 59
		this.CIFERR = new HostField("CIFERR", 1), // 69
		this.HCODE = new HostField("HCODE", 1, "9"), // 70
		this.CRDB = new HostField("CRDB", 1, "9"), // 71
		this.SUPCD = new HostField("SUPCD", 1, "9"), // 72
		this.CURCD = new HostField("CURCD", 2, "9"), // 73
		this.CURNM = new HostField("CURNM", 3), // 75 CURNM 柯 新增
		this.TXAMT = new HostField("TXAMT", 14, "9"), // 78
		this.EMPNOT = new HostField("EMPNOT", 6), // 92
		this.EMPNOS = new HostField("EMPNOS", 6), // 98
		this.CALDY = new HostField("CALDY", 8, "9"), // 104
		this.CALTM = new HostField("CALTM", 8, "9", "TIME$"), // 112
		this.MTTPSEQ = new HostField("MTTPSEQ", 2, "9"), // 120
		this.TOTAFG = new HostField("TOTAFG", 1, "9"), // 122
		this.OBUFG = new HostField("OBUFG", 1, "9"), // 123
		this.ACBRNO = new HostField("ACBRNO", 4, "9"), // 124
		this.RBRNO = new HostField("RBRNO", 4, "9"), // 128
		this.FBRNO = new HostField("FBRNO", 4, "9"), // 132
		this.RELCD = new HostField("RELCD", 1, "9"), // 136
		this.ACTFG = new HostField("ACTFG", 1, "9"), // 137
		this.SECNO = new HostField("SECNO", 2), // 138
		this.MCNT = new HostField("MCNT", 2, "9"), // 140
		this.TITFCD = new HostField("TITFCD", 1, "9"), // 142
		this.RELOAD = new HostField("RELOAD", 1, "9"), // 143
		this.BATCHNO = new HostField("BATCHNO", 12), // 143
		this.DELAY = new HostField("DELAY", 1), // 143
		this.FMTCHK = new HostField("FMTCHK", 1), this.FROMMQ = new HostField("FROMMQ", 4), this.FUNCIND = new HostField("FUNCIND", 1),
		this.LockNo = new HostField("LockNo", 11),
		this.LocCustkNo = new HostField("LockCustNo", 7),
		this.AUTHNO = new HostField("AUTHNO", 6),
		this.AGENT = new HostField("AGENT", 6),
		this.ORGEMPNM = new HostFIeld("ORGEMPNM", 8),
		//		this.Reject = new HostField("Reject", 100),
		// this.SYSFIL17= new HostField("SYSFIL17",17)
		this.FILLER_FINAL = new HostField("FILLER_FINAL", 9), // 144
		]; // 180
	}

	function TotaLabel() {
		TitaTotaType.call(this);
		this.length = 180;
		this.array = [this.MSGLNG = new HostField("MSGLNG", 5, "9"), // 34
		this.BRNO = new HostField("BRNO", 4, "9"), // 0
		this.TLRNO = new HostField("TLRNO", 6), // 4
		this.TXTNO = new HostField("TXTNO", 8, "9", "TXTNO$"), // 12
		this.CALDY = new HostField("CALDY", 8, "9"), // 13
		this.CALTM = new HostField("CALTM", 8, "9", "CALTM$"), // 22
		this.MSGEND = new HostField("MSGEND", 1, "9"), // 24
		this.TXRSUT = new HostField("TXRSUT", 1), // 32
		this.MSGID = new HostField("MSGID", 5), // 32
		// this.MSGLNG = new HostField("MSGLNG", 4, "9"),// 34
		this.MLDRY = new HostField("MLDRY", 1, "9"), // 38
		this.MRKEY = new HostField("MRKEY", 20), // 40
		this.FILLER = new HostField("FILLER", 4), // 40
		];
	}


	function TotaList() {
		this.list = [];
		this.add = function (oTota, bCheck) {
			if (bCheck && this.list.length > 0) {
				// combin forms by formname
				var prevTota = this.list[this.list.length - 1];
				if (prevTota.getTxForm() == oTota.getTxForm()) // same form
				{
					console.log("find same form:" + oTota.getTxForm() + ", append it");
					if (prevTota.getMsgEnd() == "2")
						prevTota.isAuto = true;
					prevTota.appendObj(oTota);
					prevTota.setMsgEnd(oTota.getMsgEnd()); // 以最後面的MSGEND為主
					return;
				}
			}
			this.list.push(oTota);
		};
		this.asArray = function () {
			return this.list;
		};
	}

	function Tota(sTota) {
		var self = this;
		this.host = "0"; // back-end host
		this.obj = {};
		this.isAuto = false;
		var objArray = [];
		var msgEnd = 0;
		var msgId = "E????";
		var msgType = ' ';
		var mrkey;
		var txrsut;
		var totaLabel = new TotaLabel();
		var commDataObj = new CommData(sTota);

		function deserialize() {
			totaLabel.deserialize(commDataObj);
			self.obj = sTota;
			objArray.push(self.obj);
			msgEnd = totaLabel.MSGEND.valueOf();
			msgId = totaLabel.MSGID.valueOf();
			mrkey = totaLabel.MRKEY.valueOf();
			msgType = totaLabel.MSGID.valueOf().slice(1, 2);
			txrsut = totaLabel.TXRSUT.valueOf();
		};
		deserialize(commDataObj);
		this.getLabel = function () {
			return totaLabel;
		}
		this.getHostSeq = function () {
			return totaLabel.BRNO.valueOf() + totaLabel.TLRNO.valueOf() + totaLabel.TXTNO.valueOf();
		}
		this.appendObj = function (t) {
			objArray.push(t.obj);
		};
		// 柯 新增此項給調rim 折返使用
		// 新增給 formProcessor使用
		this.getObjArray = function () {
			console.log("getObjArray : " + objArray);
			return objArray;
		};
		this.getLastObj = function () {
			return objArray.slice(-1)[0]; // take last one
		};
		this.getLastText = function () {
			if (objArray.slice(-1)[0].obj) {
				delete objArray.slice(-1)[0].obj["MSGEND"];
				delete objArray.slice(-1)[0].obj["TXRSUT"];
				delete objArray.slice(-1)[0].obj["MSGID"];
				delete objArray.slice(-1)[0].obj["MLDRY"];
				delete objArray.slice(-1)[0].obj["FILLER"];
				return JSON.stringify(objArray.slice(-1)[0].obj);
			}
			return JSON.stringify(objArray.slice(-1)[0]);
		};
		this.removeLastTotw = function () {
			objArray.pop();
		};
		this.getMsgEnd = function () {
			return msgEnd;
		};
		this.setMsgEnd = function (a) {
			msgEnd = a;
		};
		this.getMsgType = function () {
			return msgType;
		};
		this.isCE999 = function () {
			return msgId == "CE999";
		};
		this.getWarnMsg = function () {
			if (self.obj.warnMsg)
				return self.obj.warnMsg
			return "";
		};
		this.getErrmsg = function (isReason) {
			if (msgId == "CE999") {
				return msgId + "交易序號錯誤!!(" + $.trim(objArray[0].ERRMSG ? objArray[0].ERRMSG : "") + ")";
			} else {
				// var t1 = _ifx.help.lookupMsgId(msgId);
				var t2 = $.trim(objArray[0].ERRMSG ? objArray[0].ERRMSG : "");
				// 柯:如果為空或是W000,則直接不SHOW
				var t = ($.trim(msgId).length == 0 || msgId.slice(1, 5) == "W000") ? "" : msgId;
				// if (t1) t += " " + t1;
				// if (t2) t += (" (" + t2 + ")");
				t += " " + t2;
				if (isReason)
					return t2;
				return t;
			}
		};
		this.getTxRsut = function () {
			return txrsut;
		};
		this.isError = function () {
			return txrsut == "E";
		};
		this.isWarnning = function () {
			return txrsut == "W";
		};
		this.getTxForm = function () {
			return totaLabel.MSGID.valueOf();
		};
		this.getMrkey = function () {
			return totaLabel.MRKEY.valueOf();
		};
		this.getJnlId = function () {
			if (self.obj.EC)
				return self.obj.EC.BRNO + self.obj.EC.TLRNO + self.obj.EC.TXTNO;
			return self.obj.BRNO + self.obj.TLRNO + self.obj.TXTNO;
		};
		this.setTxForm = function (value) {
			totaLabel.MSGID.setValue(value);
		};
		this.isMulti = function () {
			return msgType == "M";
		};
		this.toSysVar = function () {
			totaLabel.toSysVar();
			putValue("TXFORM$", this.getTxForm());
		};
		this.parseTotaForm = function (formFields) {
			parseTommy(self.obj, formFields);
		};
		var occurs = [];
		var currTota = 0;
		this.resetOccurs = function () {
			occurs = [];
		}
		this.getOccurs = function (oSettings, fields, bResetOccurs) {
			bResetOccurs = !!bResetOccurs;
			if (bResetOccurs) {
				occurs = [];
			}
			var hdrfields = {};
			var occursfields = {};
			var noHdrFields = [];
			var HdrFields = [];
			var fieldsMap = {};

			fields.forEach((field) => {
				fieldsMap[field.name] = field;
			});

			var occursList = [];
			var totaObj = objArray[currTota++];

			if (!self.isAuto)
				// Hader
				Object.keys(totaObj).forEach(function (key) {
					if (fieldsMap["#" + key])
						hdrfields[fieldsMap["#" + key].name] = totaObj[key];

					if (key == "occursList")
						occursList = occursList.concat(totaObj[key]);
				});
			else
				objArray.forEach((tObj) => {
					Object.keys(tObj).forEach(function (key) {
						if (fieldsMap["#" + key])
							hdrfields[fieldsMap["#" + key].name] = tObj[key];

						if (key == "occursList")
							occursList = occursList.concat(tObj[key]);
					});
				});


			// Detail
			if (occursList.length > 0)
				occursList.forEach((data) => {
					var occursfields = {};
					Object.keys(data).forEach(function (key) {
						if (fieldsMap["#" + key])
							occursfields[fieldsMap["#" + key].name] = data[key];
					});
					occurs.push(this.makeOccurRec(hdrfields, occursfields));
				});

			return occurs;
		};
		this.debug1 = function (o) {
			var s = "";
			for (k in o) {
				s += k + "=" + o[k] + "\n";
			}
			alert(s);
		};
		this.makeOccurRec = function (hdrfields, occursfields) {
			return new OccursRec(hdrfields, occursfields);
		};
		this.toDebug = function () {
			var s = "";
			s += "[CommData]\n";
			s += sTota + "\n";
			s += "[totaLabel]\n";
			s += (totaLabel).toDebug();
			s += "\n[text]\n";
			s += "[" + text + "]";
			return s;
		};
		this.toString = function () {
			return this.toDebug();
		};
		this.debug = function () {
			// nop
		};
	}

	function OccursRec(hdrfields, occursfields) {
		this.hdrFields = hdrfields;
		this.occursFields = occursfields;
		this.getHdr = function () {
			return this.hdrFields;
		};
		this.getOccurs = function () {
			return this.occursFields;
		};
		this.get = function (name) {
			var value = "";
			value = this.hdrFields[name];
			if (value != null) return value;
			value = this.occursFields[name];
			if (value == null) value = "";
			return value;
		};
		this.toDebug = function () {
			var s = "";
			s += "header fields:\n";
			var bGot = false;
			for (k in this.hdrFields) {
				s += (k + "=[" + this.hdrFields[k] + "]; ");
				bGot = true;
			}
			if (!bGot) s += "\t\t*  *  *  *  N O N E  *  *  *  *";
			s += "\n---------------------------------------------------------------------------";
			s += "\noccurs fields:\n";
			var bGot = false;
			for (k in this.occursFields) {
				s += (k + "=[" + this.occursFields[k] + "]; ");
				bGot = true;
			}
			if (!bGot) s += "\t\t*  *  *  *  N O N E  *  *  *  *";
			return s;
		};
	}

	function CommData(obj) {
		this.buffer = obj;
		this.bufLen = 0; // not use for new strcut
		this.soffset = 0;
		this.get = function (name, len, type) {
			var t = "";
			var value = this.buffer[name] ? this.buffer[name] : "";
			for (var i = 0; i < len;) {
				var one = value.charAt(i);
				t += one;
				if (this.isBIG5(one))
					i += 2;
				else
					i++;
			}
			return t;
		};
		this.isBIG5 = function (ch) {
			return IfxUtl.strlen(ch) != 1;
		};
		this._getOne = function () {
			if (this.soffset >= this.bufLen) return null;
			return this.buffer.substr(this.soffset, 1);
		};
	}
	// ---FreeFormatter (sync with
	// ifx-imshost.js)--------------------------------------------------------------------
	function FreeFormatter(raw) {
		var title, blocks = parse();

		function parse() {
			var blocks = [],
				commData = new CommData(raw),
				len, row, col, style, data;
			title = commData.get(30);
			while (commData.hasMore()) {
				len = commData.get(3);
				if (isNaN(len)) break;
				row = commData.get(2);
				if (isNaN(row)) break;
				col = commData.get(2);
				if (isNaN(col)) break;
				style = commData.get(2);
				len = len - 9;
				if (len <= 0) break;
				data = commData.get(len);
				blocks.push({
					len: len,
					row: row,
					col: col,
					style: style,
					data: data
				});
			}
			return blocks;
		}

		function ScreenPrinter() {
			var result = [];

			function print(row, col, len, data) {
				--row;
				--col;
				var r = row,
					line, leftPart, rightPart;
				while (r >= result.length) {
					result.push(IfxUtl.stringFormatterBig5("", 100));
				}
				var commData = new CommData(result[row]);
				leftPart = commData.get(col);
				commData.skip(len);
				rightPart = commData.getToEnd();
				result[row] = [leftPart, data, rightPart].join("");
			}

			function getResult() {
				return $.map(result, function (x) {
					return IfxUtl.rtrim(x);
				}).join("\n");
			}
			return {
				print: print,
				getResult: getResult
			};
		}

		function print() {
			var prn = new ScreenPrinter();
			$.each(blocks, function (i, x) {
				prn.print(x.row, x.col, x.len, x.data);
			});
			return prn.getResult();
		}
		return {
			getTitle: function () {
				return title;
			},
			getBlocks: function () {
				return blocks;
			},
			print: print
		};
	}
	// --- end of FreeFormatter
	IfxHost.prototype.generateFreeFormat = function (totaText) {
		var formatter = new FreeFormatter(totaText);
		return {
			prompt: formatter.getTitle(),
			content: formatter.print()
		};
	};
	return IfxHost;
}());
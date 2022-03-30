// 配合 St1Wrapper.cab 版本：v15,10,11,0

var Device = Device || {};

(Device.pb = function($) {
	var _seed;
	var _objId;
	var deviceX;
	// \CG5218\var bIninted;
	// /CG5218/==>
	var singleWidthToken = "{{D2-E}}";
	var doubleWidthToken = "{{D2-S}}";
	var formFeedToken = "{{formfeed}}";
	var debug = false;
	var _pbcpi = null, _pblpi = null;
	// <==/CG5218/

	// \CG5515\function setup(seed, oid){
	// \CG5515\ _seed = seed;
	// \CG5515\ _objId = oid;
	// \CG5515\ deviceX = document.getElementById(_objId);
	// \CG5515\}
	// /CG5515/==>
	function setup(seed, oid) {
		var requisiteTesterID1 = "RequisiteTester1";
		var requisiteTesterID2 = "RequisiteTester2";
		var requisiteTester;
		var requisiteRequiredMessage = " 未安裝或版本太舊！(最低版本需求：15,7,31,0)";
		var versionString;
		var errorDesc = "";
		var returnValue = true;

		try {
			requisiteTester = document.getElementById(requisiteTesterID1);

			if (requisiteTester == null) {
				errorDesc = "\"" + requisiteTesterID1 + "\" Object ID 不存在！";
				returnValue = false;
			} else {
				versionString = requisiteTester.getVersion();
				// alert(requisiteTesterID1 + " 版號：" + versionString);
			}
		} catch (e) {
			errorDesc = "IKVMSetup1" + requisiteRequiredMessage;
			returnValue = false;
		}

		try {
			requisiteTester = document.getElementById(requisiteTesterID2);

			if (requisiteTester == null) {
				if (errorDesc.length > 0)
					errorDesc += "\n";

				errorDesc += "\"" + requisiteTesterID2 + "\" Object ID 不存在！";
				returnValue = false;
			} else {
				versionString = requisiteTester.getVersion();
				// alert(requisiteTesterID2 + " 版號：" + versionString);
			}
		} catch (e) {
			if (errorDesc.length > 0)
				errorDesc += "\n";

			errorDesc += "IKVMSetup2" + requisiteRequiredMessage;
			returnValue = false;
		}

		try {
			_seed = seed;
			_objId = oid;
			deviceX = document.getElementById(_objId);

			if (deviceX == null) {
				if (errorDesc.length > 0)
					errorDesc += "\n";

				errorDesc += "\"" + _objId + "\" Object ID 不存在！";
				returnValue = false;
			} else {
				versionString = deviceX.getVersion();
				// alert("Device Wrapper 版號：" + versionString);
			}
		} catch (e) {
			if (errorDesc.length > 0)
				errorDesc += "\n";

			errorDesc += "Device Wrapper ActiveX 元件未建立！";
			returnValue = false;
		}

		if (!returnValue)
			alert("Device.pb.setup():\n" + errorDesc);

		return (returnValue);
	}
	// <==/CG5515/

	// \CG5218\==>
	// function init(){
	// bIninted = deviceX.passbook_init();
	// return bIninted;
	// }
	// <==\CG5218\
	// /CG5218/==>
	function init() {
		var errorDesc;
		var returnValue;
		try {
			if (!(returnValue = deviceX.passbook_init()))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.init():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.init(): " + returnValue);
		return (returnValue);
	}
	/*
	 * 清除保留值
	 */
	function initvari() {
		_pbcpi = null;
		_pblpi = null;
	}
	/*
	 * 給在printDOC ERROR使用
	 */
	function error_init() {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_init())) {
				errorDesc = deviceX.getLastErrorMessage();
			} else {
				// ERROR 多增加重新設定此值
				if (_pbcpi) {
					deviceX.passbook_setCpi(_pbcpi);
				}
				if (_pblpi) {
					deviceX.passbook_setLpi(_pblpi);
				}
			}
		} catch (e) {
			errorDesc = "Device.pb.init():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.init(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/
	function restartdevice() {
		var errorDesc;
		var returnValue;
		try {
			if (!(returnValue = deviceX.passbook_stop_start())) // 印表機,數字機同步restart
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.stop_start():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.stop_start(): " + returnValue);
		return (returnValue);
	}

	function version() {
		return deviceX.getVersion();
	}

	function echo() {
		return deviceX.passbook_echo("hello");
	}

	function helloServer(s) {
		// \CG5409\return deviceX.passbook_helloServer(s);
		// /CG5409/==>
		var errorDesc;
		var returnValue;

		try {
			if ((returnValue = deviceX.passbook_helloServer(s)) == null)
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.helloServer():\n" + e;
			returnValue = null;
		}

		if (returnValue == null)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.helloServer(): " + returnValue);
		return (returnValue);
		// <==/CG5409/
	}

	// /CG5218/==>
	function obtainSession() {
		console.log("obtainSession!!!!");
		var errorDesc;
		var returnValue;
		initvari();
		try {
			if (!(returnValue = deviceX.passbook_obtainSession()))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.obtainSession():\n" + e;
			returnValue = false;
		}

		// if (! returnValue) alert(errorDesc);
		if (!returnValue)
			console.log(errorDesc);
		if (debug)
			alert("Device.pb.obtainSession(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function returnSession() {
		
		var errorDesc;
		var returnValue = false;

		/*
		try {
			if (!(returnValue = deviceX.passbook_returnSession()))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.returnSession():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.returnSession(): " + returnValue);
		*/
		return (returnValue);
	}
	// <==/CG5218/

	// /CG540A/==>
	function setPromptMessage(promptMessage) {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_setPromptMessage(promptMessage)))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.setPromptMessage():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.setPromptMessage(): " + returnValue);
		return (returnValue);
	}
	// <==/CG571E/

	// /CG540A/==>
	function checkDocument() {
		var errorDesc;
		var resultValue;
		var documentLoaded = false;

		try {
			if ((resultValue = deviceX.passbook_checkDocument()) < 0)
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.checkDocument():\n" + e;
			resultValue = (-1);
		}

		if (resultValue < 0)
			alert(errorDesc);
		else {
			if (resultValue > 0)
				documentLoaded = true;
		}

		if (debug)
			alert("Device.pb.checkDocument(): " + documentLoaded);
		return (documentLoaded);
	}
	// <==/CG540A/

	// /CG5218/==>
	function setLpi(newLpi) {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_setLpi(newLpi))) {
				errorDesc = deviceX.getLastErrorMessage();
			} else {
				_pblpi = newLpi;
			}
		} catch (e) {
			errorDesc = "Device.pb.setLpi():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.setLpi(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function setCpi(newCpi) {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_setCpi(newCpi))) {
				errorDesc = deviceX.getLastErrorMessage();
			} else {
				_pbcpi = newCpi;
			}

		} catch (e) {
			errorDesc = "Device.pb.setCpi():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.setCpi(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function setSingleWidth() {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_setDoubleWidth(false)))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.setSingleWidth():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.setSingleWidth(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function setDoubleWidth() {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_setDoubleWidth(true)))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.setDoubleWidth():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.setDoubleWidth(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function newLine(newLineCount) {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_newLines(newLineCount)))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.newLine():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.newLine(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function printText(textData) {
		var singleWidthTokenIndex = 0;
		var doubleWidthTokenIndex = 0;
		var formFeedTokenIndex = 0;
		var tokenIndex;
		var errorDesc;
		var returnValue = true;

		try {
			while (textData.length >= 0) {
				if (singleWidthTokenIndex >= 0)
					singleWidthTokenIndex = textData.indexOf(singleWidthToken);

				if (doubleWidthTokenIndex >= 0)
					doubleWidthTokenIndex = textData.indexOf(doubleWidthToken);

				if (formFeedTokenIndex >= 0)
					formFeedTokenIndex = textData.indexOf(formFeedToken);

				if ((singleWidthTokenIndex < 0) && (doubleWidthTokenIndex < 0)
						&& (formFeedTokenIndex < 0)) {
					returnValue = deviceX.passbook_printText(textData);
					break;
				}

				if (singleWidthTokenIndex < 0)
					tokenIndex = textData.length;
				else
					tokenIndex = singleWidthTokenIndex;

				if ((doubleWidthTokenIndex >= 0) && (doubleWidthTokenIndex < tokenIndex))
					tokenIndex = doubleWidthTokenIndex;

				if ((formFeedTokenIndex >= 0) && (formFeedTokenIndex < tokenIndex))
					tokenIndex = formFeedTokenIndex;

				if (tokenIndex > 0) {
					if (!(returnValue = deviceX.passbook_printText(textData.substr(0,
							tokenIndex))))
						break;
				}

				if (tokenIndex == singleWidthTokenIndex) {
					if (!(returnValue = deviceX.passbook_setDoubleWidth(false)))
						break;

					textData = textData.substr(tokenIndex + singleWidthToken.length);
				} else {
					if (tokenIndex == doubleWidthTokenIndex) {
						if (!(returnValue = deviceX.passbook_setDoubleWidth(true)))
							break;

						textData = textData.substr(tokenIndex + doubleWidthToken.length);
					} else // (tokenIndex == formFeedTokenIndex)
					{
						if (!(returnValue = deviceX.passbook_eject()))
							break;

						textData = textData.substr(tokenIndex + formFeedToken.length);
					}
				}
			}

			if (!returnValue)
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.printText():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.printText(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function printLine(textLine) {
		var returnValue;

		try {
			if (returnValue = printText(textLine))
				returnValue = newLine(1);
		} catch (e) {
			alert("Device.pb.printLine():\n" + e);
			returnValue = false;
		}

		if (debug)
			alert("Device.pb.printLine(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function printLines(textLines) {
		var returnValue = true;

		try {
			var endLine = textLines.length - 1;

			if (endLine >= 0) {
				$.each(textLines, function(i, textLine) {
					// 20180105 ADD REPLACE FOR 存摺印表機
					textLine = textLine.replace(/\s+$/, "");
					if (returnValue) {
						if (i < endLine)
							returnValue = printLine(textLine);
						else
							returnValue = printText(textLine);
					}
				});
			}
		} catch (e) {
			alert("Device.pb.printLines():\n" + e);
			returnValue = false;
		}

		if (debug)
			alert("Device.pb.printLines(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function flushPrintData() {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_flushPrintData()))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.flushPrintData():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.flushPrintData(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG5218/==>
	function eject() {
		var errorDesc;
		var returnValue;

		try {
			if (!(returnValue = deviceX.passbook_eject()))
				errorDesc = deviceX.getLastErrorMessage();
		} catch (e) {
			errorDesc = "Device.pb.eject():\n" + e;
			returnValue = false;
		}

		if (!returnValue)
			alert(errorDesc);
		if (debug)
			alert("Device.pb.eject(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// \CG5218\==>
	// function printDoc(lines,prompt){
	// var x;
	//
	// while(true) {
	// try {
	// x = deviceX.passbook_obtain(_seed);
	// break;
	// }catch(ee){
	// var b = confirm("印表機連線錯誤:"+ee + "\n請確定印表機服務程式已經啟動\n是否再試?");
	// if(!b) return;
	// init();
	// }
	// }
	//
	// $.each(lines,function(i,line){
	// var r = deviceX.passbook_print(x,line +"<br/>","",0,0);
	// });
	//
	// deviceX.passbook_eject(x);
	// deviceX.passbook_returnSession(x);
	// }
	// <==\CG5218\
	// /CG5218/==>
	function printDoc(textLines, prompt) {
		var returnValue = true;

		do {
			if (returnValue) {
				try {
					if (returnValue = setPromptMessage(prompt)) {
						if (returnValue = printLines(textLines)) {
							if (returnValue = eject())
								break;
						}
					}
				} catch (e) {
					alert("Device.pb.printDoc():\n" + e);
					returnValue = false;
				}
			}

			var tryAgain = confirm("文件列印失敗！\n\n請按「確定」再試一次，否則請按「取消」...");
			if (!tryAgain)
				break;
			returnValue = error_init();
		} while (true);

		if (debug)
			alert("Device.pb.printDoc(): " + returnValue);
		return (returnValue);
	}
	// <==/CG5218/

	// /CG520A/==>
	function readMSData(eject, once) {
		var errorDesc;
		var msData;

		do {
			try {
				if ((msData = deviceX.passbook_decode("請插入存摺...", eject)) == null)
					errorDesc = deviceX.getLastErrorMessage();
			} catch (e) {
				errorDesc = "磁條資料讀取失敗：\n" + e;
				msData = null;
			}

			if (msData != null)
				break;
			if (once)
				break; // for ifx-psbk getBlackcheck
			var tryAgain = confirm(errorDesc + "\n請按「確定」再試一次，否則請按「取消」...");
			if (!tryAgain)
				break;
		} while (true);

		if (debug)
			alert("Device.pb.readMSData(): \"" + msData + "\"");
		return (msData);
	}
	// <==/CG520A/

	// /CG520A/==>
	function writeMSData(msData, eject) {
		var errorDesc;
		var returnValue;

		do {
			try {
				if (!(returnValue = deviceX.passbook_encode(msData, "請插入存摺...", eject)))
					errorDesc = deviceX.getLastErrorMessage();
			} catch (e) {
				errorDesc = "磁條資料寫入失敗：\n" + e;
				returnValue = false;
			}

			if (returnValue)
				break;
			var tryAgain = confirm(errorDesc + "\n請按「確定」再試一次，否則請按「取消」...");
			if (!tryAgain)
				break;
		} while (true);

		if (debug)
			alert("Device.pb.writeMSData(): " + returnValue);
		return (returnValue);
	}
	// <==/CG520A/

	// /CG5806K/==>
	// 寫磁條同時印文字
	function writeMSDataprint(msData, eject, withtext, cpi, lpi) {
		var errorDesc;
		var returnValue;

		do {
			do {
				var remsData = this.readMSData(false, true); // 為了出去(空白) 第二參數給true
				console.log("msData:" + remsData);
				if (remsData == null)
					break;

				if (!window.confirm("非空白磁條，是否繼續寫入磁條?")) {
					this.eject();
				} else {
					break;
				}
			} while (true);

			try {
				if (!(returnValue = deviceX.passbook_encode(msData, "請插入存摺...", eject)))
					errorDesc = deviceX.getLastErrorMessage();
			} catch (e) {
				errorDesc = "磁條資料寫入失敗：\n" + e;
				returnValue = false;
			}

			if (returnValue)
				break;
			var tryAgain = confirm(errorDesc + "\n請按「確定」再試一次，否則請按「取消」...");
			if (!tryAgain) {
				break;
			} else {
				// 為何字體變了!!!!??
				this.returnSession();
				this.obtainSession();
				if (cpi != "") { // printcpi
					this.setCpi(parseInt(cpi, 10));
				}
				if (lpi != "") { // printlpi
					this.setLpi(parseInt(lpi, 10));
				}
				this.printLines(withtext);
				this.flushPrintData();
			}
		} while (true);

		if (debug)
			alert("Device.pb.writeMSData(): " + returnValue);
		return (returnValue);
	}
	// 柯:讀取excel轉換josn

	// /CG5806K/==>
	// 柯:讀取excel轉換josn
	function excelReadtoJosn(filepath, listname) {
		var josnlist = deviceX.excelReadtoJosn(filepath, listname);
		console.dir(josnlist);
		return josnlist;
	}
	;
	// <==/CG5806K/
	// 直接預設big5
	var _file = {
		'read' : function(p, encode) {
			if (encode == null || !encode) {
				encode = 950;
			}
			return deviceX.fileReadText(p, encode);
		},
		'write' : function(p, t, append, encode) { // TODO 回傳true false?
			if (encode == null || !encode) {
				encode = 950;
			}
			deviceX.fileWriteText(p, t, append, encode);
		},
		'delete' : function(p) {
			deviceX.fileDelete(p);
		},
		'exists' : function(p, create) {

			return deviceX.fileExists(p, create);
		},
		'encrypt' : function(p) {
			deviceX.fileAddEncryption(p);
		},
		'decrypt' : function(p) {
			deviceX.fileRemoveEncryption(p);
		},
		'copy' : function(s, d, bOverwrite) {
			deviceX.fileCopy(s, d, bOverwrite);
		}
	};
	var _folder = {
		'combine' : function(f1, f2) {
			return deviceX.folderCombinePath(f1, f2);
		},
		'create' : function(f) {
			try {
				deviceX.folderCreate(f);
			} catch (ee) {
				alert(ee);
			}
		},
		'exists' : function(f) {
			return deviceX.folderExists(f);
		},
		'move' : function(s, d) {
			return deviceX.folderMove(s, d);
		},
		'delete' : function(f, recur) {
			deviceX.folderDelete(f, recur);
		}
	};

	var _winowsPrinter = {
		'list' : function() {
			return deviceX.prn_printers();
		},

		// \CG531F\==>
		// 'print':function(lines){
		// deviceX.prn_print(lines);
		// ///CG520AKE/==>
		// },
		// 'printsize':function(size){
		// deviceX.prn_printsize(size);
		// //<==/CG520AKE/
		// <==\CG531F\

		// /CG531E/==>
		'beginDocument' : function(documentName) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_beginDocument(documentName)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.beginDocument():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.beginDocument(): " + returnValue);
			return (returnValue);
		},

		'setPrinterName' : function(printerName) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_setPrinterName(printerName)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setPrinterName():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setPrinterName(): " + returnValue);
			return (returnValue);
		},

		'setPaperSize' : function(paperWidth, paperHeight) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX
						.winPrint_setPaperSize(paperWidth, paperHeight)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setPaperSize():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setPaperSize(): " + returnValue);
			return (returnValue);
		},

		'setPaperOrientation' : function(landscapeOrientation) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX
						.winPrint_setPaperOrientation(landscapeOrientation)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setPaperOrientation():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setPaperOrientation(): " + returnValue);
			return (returnValue);
		},
		// 縮印成功,但無法避免已超出內容折行的問題
		'setScaleTransform' : function(paperScalesx, paperScalesy) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_setScaleTransform(paperScalesx,
						paperScalesy)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setScaleTransform():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setScaleTransform(): " + returnValue);
			return (returnValue);
		},

		'setPaperMargins' : function(leftMargin, topMargin, rightMargin, bottomMargin) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_setPaperMargins(leftMargin,
						topMargin, rightMargin, bottomMargin)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setPaperMargins():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setPaperMargins(): " + returnValue);
			return (returnValue);
		},

		'adjustPageMargins' : function(leftMarginAdjustment, topMarginAdjustment,
				rightMarginAdjustment, bottomMarginAdjustment) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_adjustPageMargins(
						leftMarginAdjustment, topMarginAdjustment, rightMarginAdjustment,
						bottomMarginAdjustment)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.adjustPageMargins():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.adjustPageMargins(): " + returnValue);
			return (returnValue);
		},

		'drawImageFile' : function(imageFilePath, upperLeftX, upperLeftY, drawWidth,
				drawHeight, stretchImage, drawOnPreview, drawOnPrint,
				drawOnEachSucceedingPage) {

			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_drawImageFile(imageFilePath,
						upperLeftX, upperLeftY, drawWidth, drawHeight, stretchImage,
						drawOnPreview, drawOnPrint, drawOnEachSucceedingPage)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.drawImageFile():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.drawImageFile(): " + returnValue);
			return (returnValue);
		},

		'drawImageData' : function(base64ImageData, upperLeftX, upperLeftY, drawWidth,
				drawHeight, stretchImage, drawOnPreview, drawOnPrint,
				drawOnEachSucceedingPage) {

			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_drawImageData(base64ImageData,
						upperLeftX, upperLeftY, drawWidth, drawHeight, stretchImage,
						drawOnPreview, drawOnPrint, drawOnEachSucceedingPage)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.drawImageData():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.drawImageData(): " + returnValue);
			return (returnValue);
		},

		'setTaoForm' : function(formbyte) {
			var returnValue;

			try {
				returnValue = _winowsPrinter.drawImageData(formbyte, 0, 0, 0, 0, false,
						true, true, true);
			} catch (e) {
				alert("Device.pb.prn.setTaoForm():\n" + e);
				returnValue = false;
			}

			if (debug)
				alert("Device.pb.prn.setTaoForm(): " + returnValue);
			return (returnValue);
		},

		'adjustPrintPosition' : function(xPositionAdjustment, yPositionAdjustment) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_adjustPrintPosition(
						xPositionAdjustment, yPositionAdjustment)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.adjustPrintPosition():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.adjustPrintPosition(): " + returnValue);
			return (returnValue);
		},

		'setLpi' : function(newLpi) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_setLpi(newLpi)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setLpi():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setLpi(): " + returnValue);
			return (returnValue);
		},

		'setCpi' : function(newCpi) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_setCpi(newCpi)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setCpi():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setCpi(): " + returnValue);
			return (returnValue);
		},

		'setSingleWidth' : function() {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_setCharWidth(false)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setSingleWidth():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setSingleWidth(): " + returnValue);
			return (returnValue);
		},

		'setDoubleWidth' : function() {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_setCharWidth(true)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.setDoubleWidth():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.setDoubleWidth(): " + returnValue);
			return (returnValue);
		},

		'printText' : function(textData) {
			var singleWidthTokenIndex = 0;
			var doubleWidthTokenIndex = 0;
			var formFeedTokenIndex = 0;
			var tokenIndex;
			var errorDesc;
			var returnValue = true;

			try {
				while (textData.length > 0) {
					if (singleWidthTokenIndex >= 0)
						singleWidthTokenIndex = textData.indexOf(singleWidthToken);

					if (doubleWidthTokenIndex >= 0)
						doubleWidthTokenIndex = textData.indexOf(doubleWidthToken);

					if (formFeedTokenIndex >= 0)
						formFeedTokenIndex = textData.indexOf(formFeedToken);

					if ((singleWidthTokenIndex < 0) && (doubleWidthTokenIndex < 0)
							&& (formFeedTokenIndex < 0)) {
						returnValue = deviceX.winPrint_printText(textData);
						break;
					}

					if (singleWidthTokenIndex < 0)
						tokenIndex = textData.length;
					else
						tokenIndex = singleWidthTokenIndex;

					if ((doubleWidthTokenIndex >= 0)
							&& (doubleWidthTokenIndex < tokenIndex))
						tokenIndex = doubleWidthTokenIndex;

					if ((formFeedTokenIndex >= 0) && (formFeedTokenIndex < tokenIndex))
						tokenIndex = formFeedTokenIndex;

					if (tokenIndex > 0) {
						if (!(returnValue = deviceX.winPrint_printText(textData.substr(0,
								tokenIndex))))
							break;
					}

					if (tokenIndex == singleWidthTokenIndex) {
						if (!(returnValue = deviceX.winPrint_setCharWidth(false)))
							break;

						textData = textData.substr(tokenIndex + singleWidthToken.length);
					} else {
						if (tokenIndex == doubleWidthTokenIndex) {
							if (!(returnValue = deviceX.winPrint_setCharWidth(true)))
								break;

							textData = textData.substr(tokenIndex
									+ doubleWidthToken.length);
						} else // (tokenIndex == formFeedTokenIndex)
						{
							if (!(returnValue = deviceX.winPrint_newPage()))
								break;

							textData = textData.substr(tokenIndex + formFeedToken.length);
						}
					}
				}

				if (!returnValue)
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.printText():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.printText(): " + returnValue);
			return (returnValue);
		},

		'printLine' : function(textLine) {
			var returnValue;

			try {
				if (returnValue = _winowsPrinter.printText(textLine))
					returnValue = _winowsPrinter.newLines(1);
			} catch (e) {
				alert("Device.pb.prn.printLine():\n" + e);
				returnValue = false;
			}

			if (debug)
				alert("Device.pb.prn.printLine(): " + returnValue);
			return (returnValue);
		},

		'newLines' : function(newLineCount) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_newLines(newLineCount)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.newLines():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.newLines(): " + returnValue);
			return (returnValue);
		},

		'newPage' : function() {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_newPage()))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.newPage():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.newPage(): " + returnValue);
			return (returnValue);
		},

		'endDocument' : function(showPrintPreviewDialog, showPrintDialog) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.winPrint_endDocument(showPrintPreviewDialog,
						showPrintDialog)))
					errorDesc = deviceX.winPrint_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.prn.endDocument():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.prn.endDocument(): " + returnValue);
			return (returnValue);
		},

		'printsize' : function(cpi, lpi) {
			var returnValue;

			try {
				if (returnValue = _winowsPrinter.setCpi(cpi))
					returnValue = _winowsPrinter.setLpi(lpi);
			} catch (e) {
				alert("Device.pb.prn.printsize():\n" + e);
				returnValue = false;
			}

			if (debug)
				alert("Device.pb.prn.printsize(): " + returnValue);
			return (returnValue);
		},

		'print' : function(textData) {
			var returnValue;

			try {
				if (returnValue = _winowsPrinter.beginDocument("IFX 文件")) {
					if (returnValue = _winowsPrinter.printText(textData))
						returnValue = _winowsPrinter.endDocument(true, true);
				}
			} catch (e) {
				alert("Device.pb.prn.print():\n" + e);
				returnValue = false;
			}

			if (debug)
				alert("Device.pb.prn.print(): " + returnValue);
			return (returnValue);
			// <==/CG531E/
		}
	};

	var _pdf = {
		// \CG5513\'write':function(filePath, title, content){
		// \CG5513\ deviceX.pdf_file(filePath, title, content);
		// \CG5513\}
		// /CG5513/==>
		'init' : function() {
			try { // 初始化設定
				deviceX.pdf_setPaperSize(830, 1170);
				deviceX.pdf_setCpi(12);
				deviceX.pdf_setLpi(5);
				deviceX.pdf_setPaperMargins(25, 25, 0, 0);
			} catch (e) {
				alert("Device.pb.pdf.printsize():\n" + e);
			}
		},
		'printsize' : function(pdfsize) {
			try {
				deviceX.pdf_printsize(pdfsize);
			} catch (e) {
				alert("Device.pb.pdf.printsize():\n" + e);
			}
		},
		'printcpi' : function(pdfcpi) {
			try {
				deviceX.pdf_setCpi(parseFloat(pdfcpi));
			} catch (e) {
				alert("Device.pb.pdf.setCpi():\n" + e);
			}
		},
		'printlpi' : function(pdflpi) {
			try {
				deviceX.pdf_setLpi(parseInt(pdflpi, 10));
			} catch (e) {
				alert("Device.pb.pdf.setLpi():\n" + e);
			}
		},
		'setPaperSize' : function(pdfpaperWidth, pdfpaperHeight) {
			try {
				deviceX.pdf_setPaperSize(pdfpaperWidth, pdfpaperHeight);
			} catch (e) {
				alert("Device.pb.pdf.setPaperSize():\n" + e);
			}
		},
		'setPaperMargins' : function(leftMargin, topMargin, rightMargin, bottomMargin) {
			try {
				deviceX.pdf_setPaperMargins(leftMargin, topMargin, rightMargin,
						bottomMargin);
			} catch (e) {
				alert("Device.pb.pdf.setPaperMargins():\n" + e);
			}
		},
		// <==/CG5617KE/
		// /CG5513/==>
		'write' : function(filePath, title, content, formbyte) {
			var returnValue;

			try {
				deviceX.pdf_file(filePath, title, content, formbyte);
				returnValue = true;
			} catch (e) {
				alert("Device.pb.pdf.write():\n" + e);
				returnValue = false;
			}

			if (debug)
				alert("Device.pb.pdf.write(): " + returnValue);
			return (returnValue);
		}
	// <==/CG5513/
	};

	// /CG530A/==>
	var _TCBSealInterop = {
		'verifySeal' : function(accountNumber, userID, userName) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.seal_verifySeal(accountNumber, userID,
						userName)))
					errorDesc = deviceX.seal_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.seal.verifySeal():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			return (returnValue);
		},

		'reportLoss' : function(accountNumber, userID, userName) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.seal_reportLoss(accountNumber, userID,
						userName)))
					errorDesc = deviceX.seal_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.seal.reportLoss():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			return (returnValue);
		},

		'revokeLoss' : function(accountNumber, userID, userName) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.seal_revokeLoss(accountNumber, userID,
						userName)))
					errorDesc = deviceX.seal_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.seal.revokeLoss():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			return (returnValue);
		},

		'closeAccount' : function(accountNumber, userID, userName) {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.seal_closeAccount(accountNumber, userID,
						userName)))
					errorDesc = deviceX.seal_getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.seal.closeAccount():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			return (returnValue);
		}
	};
	// <==/CG530A/

	// /CG5513/==>
	var _pinpad = {
		'init' : function() {
			var errorDesc;
			var returnValue;

			try {
				if (!(returnValue = deviceX.pinpad_init()))
					errorDesc = deviceX.getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.pinpad.init():\n" + e;
				returnValue = false;
			}

			if (!returnValue)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.pinpad.init(): " + returnValue);
			return (returnValue);
		},

		'read' : function() {
			var errorDesc;
			var pinData;

			try {
				if ((pinData = deviceX.pinpad_read()) == null)
					errorDesc = deviceX.getLastErrorMessage();
			} catch (e) {
				errorDesc = "Device.pb.pinpad.read():\n" + e;
				pinData = null;
			}

			if (pinData == null)
				alert(errorDesc);
			if (debug)
				alert("Device.pb.pinpad.read(): \"" + pinData + "\"");
			return (pinData);
		}
	};
	// <==/CG5513/

	return {
		setup : setup,
		init : init,
		restartdevice : restartdevice,
		version : version,
		echo : echo,
		hello : helloServer,
		printDoc : printDoc,
		// /CG520A/==>
		obtainSession : obtainSession,
		returnSession : returnSession,
		setPromptMessage : setPromptMessage,
		checkDocument : checkDocument,
		setLpi : setLpi,
		setCpi : setCpi,
		setSingleWidth : setSingleWidth,
		setDoubleWidth : setDoubleWidth,
		newLine : newLine,
		printText : printText,
		printLine : printLine,
		printLines : printLines,
		flushPrintData : flushPrintData,
		eject : eject,
		readMSData : readMSData,
		writeMSDataprint : writeMSDataprint, // /CG5806K/
		writeMSData : writeMSData,
		seal : _TCBSealInterop,
		pinpad : _pinpad,
		// <==/CG520A/
		file : _file,
		excelReadtoJosn : excelReadtoJosn, // /CG5806K/
		folder : _folder,
		prn : _winowsPrinter,
		pdf : _pdf
	};
}(jQuery));

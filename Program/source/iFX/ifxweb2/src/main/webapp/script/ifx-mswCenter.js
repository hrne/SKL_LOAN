function initDWR() {
	dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setNotifyServerOnPageUnload(true);
	dwr.engine.setErrorHandler(function(msg, ex) {
		msg = msg || "A server error has occurred.";
		if (confirm('信息系統錯誤，是否重新登入?\n' + msg + "\n" + JSON.stringify(ex))) {
			//			window.location.reload();
		}
	});
	dwr.engine.setPollStatusHandler(updatePollStatus);
	initControls();
	join();
}
var $mswHistory;

function initControls() {
	$mswHistory = $('#mswHistory');
	$('#btnGetUsers').on('click', function() {
		getUsers();
	});
	$('#btnGetSupervisors').on('click', function() {
		getSupervisors();
	});
	$('#btnTalk').on('click', function() {
		talk();
	});
}

function updatePollStatus(pollStatus) {
	if (!pollStatus) {
		//alert("ovr center is offline");
	} else {
		//alert("online now");
	}
	var fn = getTopFn('changeServerStatus');
	fn(pollStatus);
	//dwr.util.setValue("message", pollStatus ? "Online" : "Offline", {escapeHtml:false});
}

function addLog(s) {
	s = s || "";
	$mswHistory.val($mswHistory.val() + s + "\n");
	scrollDown($mswHistory);
}

function scrollDown($ele) {
	$ele.animate({
		scrollTop: $ele[0].scrollHeight - $ele.height()
	}, 100);
	//	 $ele.scrollTop($ele.scrollHeight - $ele.height());
}

function join() {
	addLog("join MswCenter");
	MswCenter.join(null);
}

function __joinDone(retval, m) {
	if (retval == 0) {
		var fn = getTopFn('changeServerStatus');
		fn(true);
		addLog("joinDone:" + m);
	} else {
		addLog("join error:" + m);
	}
}

function getUsers(callback) {
	if (callback === undefined) callback = null;
	MswCenter.getUsers(callback);
}

function __getUsersDone(retval, m) {
	if (retval == 0) {
		//	alert(JSON.stringify(m));
		addLog("getUser ok, numbers:" + m.length);
		for (var i = 0; i < m.length; i++) {
			//var ss = list[i].split('|');
			addLog(m[i]);
		}
	} else {
		addLog("getUsers Error, retval:" + retval + ":" + m);
	}
	addLog();
}

function getSupervisors(callback) {
	if (callback === undefined) callback = null;
	MswCenter.getSupervisors(callback, null, null); //MSW不需要傳入 TXCD
}

function __getSupervisorsDone(retval, m) {
	if (retval == 0) {
		addLog("getSupervisors ok, numbers: " + m.length);
		//alert(JSON.stringify(m));
		for (var i = 0; i < m.length; i++) {
			//var ss = list[i].split('|');
			addLog(m[i]);
		}
	} else {
		addLog("getSupervisors Error, retval:" + retval + ":" + m);
	}
	addLog();
}
var listSupOvr = [];

function SupOvrReq(ovrData) {
	if (!ovrData.userId) {
		console.log("SupOvrReq replace ovrData.");
		ovrData = ovrData[0];
	}
	this.userId = ovrData.userId;
	this.name = $.trim(ovrData.name);
	this.token = ovrData.token;
	this.txcd = ovrData.txcd;
	this.screenFile = ovrData.screenFile;
	this.reasons = ovrData.reasons;
	this.cancelled = false;
	this.getKey = function() {
		return this.userId + '-' + this.token;
	};
	this.getValue = function() {
		return '櫃員:' + this.name + ' ' + this.userId + ',交易:' + this.txcd + ',原因:' + this.reasons;
	};
	this.equals = function(key) {
		var ss = key.split('-');
		var userId = ss[0];
		var token = ss[1];
		return (this.userId === userId && this.token === token);
	};
	this.toString = function() {
		return 'user:' + this.userId, +', token:' + this.token + ', txcd:' + this.txcd + ', reasons:' + this.reasons + ', cancelled:' + this.cancelled;
	};
}

function findSupOvrByKey(key) {
	for (var i = 0; i < listSupOvr.length; i++) {
		if (listSupOvr[i].equals(key)) return listSupOvr[i];
	}
	return null;
}

function findSupOvrBy(userId, token) {
	return findSupOvrByKey(userId + '-' + token);
}

function removeSupOvrItem(objOvr) {
	listSupOvr = myFilter(listSupOvr, function(x) {
		return x !== objOvr;
	});
	updateOvrCount(false);
	displayOvrList();
}

function displayOvrList(bAlert) {
	if (listSupOvr.length == 0) {
		if (bAlert) {
			alert('無待授權交易');
		}
		return;
	}
	$supPanel = $('#supervisorPanel');
	//	if($supPanel!=null && $supPanel.dialog( "isOpen" )) {
	//		return;
	//	}
	//
	$supPanel.dialog({
		autoOpen: false,
		height: 300,
		width: 600,
		modal: true
	});
	$('#btnSupScreenCopy').off('click').on('click', supScreenCopyClicked).button();
	//	$('#btnSupAcceptNow').off('click').on('click', supAcceptNowClicked);
	//	$('#btnSupRejectNow').off('click').on('click', supRejectNowClicked);
	$('#btnSupRemoveItem').off('click').on('click', supRemoveItemClicked).button();
	$('#btnSupCancelSelect').off('click').on('click', supCancelSelectClicked).button();
	//var $ovrReqList = $('#ovrReqList');
	$('#listSupOvr option').remove();
	var $list = $('#listSupOvr');
	$.each(listSupOvr, function(i, x) {
		console.log(x.getKey());
		console.log(x.getValue());
		$list.append(new Option(x.getValue(), x.getKey()));
	});
	$("#listSupOvr option:first").attr('selected', 'selected');
	$('#divOvrList,#divBtns', $supPanel).show();
	$('#divProcessOvr', $supPanel).hide();
	$supPanel.dialog("open");
}

function isOvrSelected() {
	var item = $("#listSupOvr :selected").val();
	if (!item) {
		alert('請選擇授權交易');
		return null;
	}
	// conver to ovr req from selected value
	var ovrReq = findSupOvrByKey(item);
	if (ovrReq == null) {
		//alert(item + " is not exists, probably remove by the requester");
		alert(item + " 授權請求已被取消");
		return null;
	}
	//	$supPanel.dialog('close');
	return ovrReq;
}

function supScreenCopyClicked() {
	//潘 遠端授權密碼預設空白 要部瀏覽器會帶預設值
	$("#password").val("");
	var objReq = isOvrSelected();
	if (objReq === null) return;
	//alert('screen copy ' + objReq.getValue());
	//changeOvrStep(objReq.userId, objReq.token, 'pWait', '主管開始處理授權, screen copying....');
	ovrNotify(objReq.userId, '主管正在檢視您的畫面');
	$('#divOvrList,#divBtns', $supPanel).hide();
	$('#divProcessOvr', $supPanel).show();
	$('#ovrUserId').text(objReq.name + objReq.userId);
	$('#ovrTxcd').text(objReq.txcd);
	$('#ovrReasons').text(objReq.reasons);
	// screen copy
	//$('#ovrScreen').text(objReq.screenFile);
	var url = "tran2.jsp?txcode=" + objReq.txcd + "&ovr=1&ovrScrFile=" + objReq.screenFile;
	url = encodeURI(url);
	$('#ovrScreen').html(buildIframe("screenCopy", url));
	$("#password").trigger("focus");
	//潘Enter偵測送出
	$("#password").off('keypress').on('keypress', function(e) {
		code = (e.keyCode ? e.keyCode : e.which);
		if (code == 13) {
			$("#btnSupAccept").trigger('click');
		}
	});
	// bind button handler
	$("#btnSupAccept").off('click').on('click', function(e) {
		//潘 廢除彈跳視窗 直接在畫面上輸入密碼並帶入
		var adPaswd = $("#password").val();;
		//補檢查是否已被取消
		var objReq = isOvrSelected();
		if (objReq === null) {
			return;
		}
		/*新增POP視窗輸入密碼  潘*/
		/*加密Base64 潘*/
		//adPaswd = window.showModalDialog('user/info_html/Passwd/paswd.html','',
		//                                 'toolbar=no, location=no, directories=no, status=yes, menubar=no, scrollbars=no, resizable=no');
		/*解密*/
		//var unecs = new Base64();
		//adPaswd = unecs.decode(adPaswd);
		/*
		while(adPaswd == ""){
			adPaswd = window.prompt("重新認證登入密碼:","");
		}
		*/
		MswCenter.checkSameGuys(adPaswd, function(data) {
			//補檢查是否已被取消
			var objReq = isOvrSelected();
			if (objReq === null) {
				return;
			}
			//認證符合登入帳號密碼
			if (data.length > 1) {
				removeSupOvrItem(objReq);
				MswCenter.ovrDone("accept", objReq.userId, objReq.token, "good job", data);
				$supPanel.dialog('destroy');
			} else {
				alert("請再次確認是否為您此次登入的密碼.");
			}
		});
	}).button();
	$("#btnSupReject").off('click').on('click', function(e) {
		//補檢查是否已被取消
		var objReq = isOvrSelected();
		if (objReq === null) {
			return;
		}
		removeSupOvrItem(objReq);
		var rejectReason = $("#ovrRejectReason").val();
		MswCenter.ovrDone("reject", objReq.userId, objReq.token, rejectReason);
		$supPanel.dialog('destroy');
	}).button();
	// reposition dialog
	$supPanel.dialog("option", "width", 1200);
	$supPanel.dialog("option", "height", 678);
	//$supPanel.dialog().data("height.dialog", 800);
	$supPanel.dialog('option', 'position', [50, 5]);
	$('#ovrWaitScreen').show();
	setTimeout(function() {
		$('#ovrScreen').animate({
			height: 500
		}, 300, function() {
			$('#ovrWaitScreen').hide();
		});
	}, 300);
}

function ovrNotify(userId, m) {
	MswCenter.ovrNotify(userId, m);
}

function supAcceptNowClicked() {
	var objReq = isOvrSelected();
	if (objReq === null) return;
	removeSupOvrItem(objReq);
	alert('accept ' + objReq.getValue());
}

function supRejectNowClicked() {
	var objReq = isOvrSelected();
	if (objReq === null) return;
	alert('reject ' + objReq.getValue());
	if (window.confirm('確定拒絕授權?')) {
		removeSupOvrItem(objReq);
	}
}

function nop() {}

function supRemoveItemClicked() {
	var objReq = isOvrSelected();
	if (objReq === null) return;
	var btnOK = {
		text: '確定',
		click: function() {
			changeOvrStep(objReq.userId, objReq.token, 'pChoose', '授權要求已被主管移除');
			$supPanel.dialog('destroy');
			setTimeout(function() {
				removeSupOvrItem(objReq);
			}, 10);
		}
	};
	var btnCancel = {
		text: '取消',
		click: nop
	};
	myConfirm([btnOK, btnCancel], '確定捨棄此項目?');
}

function supCancelSelectClicked() {
	$supPanel.dialog('destroy');
}
// DWR
//temp for MswCenter 統一sendTo規格
function __appendSupOvrReq(temp, ovrData) {
	listSupOvr.push(new SupOvrReq(ovrData));
	updateOvrCount(true);
}

function __cancelSupOvrReq(status, arr) {
	if (status == 0) {
		var userId = arr[0];
		var token = arr[1];
		var ovrReq = findSupOvrBy(userId, token);
		if (ovrReq == null) {
			ovrNotify(userId, '正在處理或無此項目');
			return;
		} else {
			removeSupOvrItem(ovrReq);
			//changeOvrStep(userId, token, 'pChoose', '授權要求已取消');
		}
	}
}

function changeOvrStep(userId, token, stepName, message) {
	MswCenter.changeOvrStep(userId, token, stepName, message);
}

function updateOvrCount(bEffect) {
	var fn = getTopFn('setOvrCount');
	fn(listSupOvr.length, bEffect);
}

function talk() {
	var toUser = $('#txtTo').val() || 'ALL';
	var msg = $('#txtMessage').val() || 'Hello!';
	$('#txtMessage').val('');
	MswCenter.talk(toUser, msg);
}

function __broadcast(m) {
	addLog(m);
}
var _msgCallbacks = [];

function mswcenter_addTalkToCallback(fn) {
	if ($.inArray(fn, _msgCallbacks) == -1) _msgCallbacks.push(fn);
}

function __talkTo(retval, m) {
	//	alert(m);
	console.log("__talkTo me:" + m);
	addLog(m);
	m = m[0]; // 不知何時變成陣列?
	console.log("__talkTo me m[0]:" + m);
	for (var i = 0; i < _msgCallbacks.length; i++) {
		_msgCallbacks[i](m);
	}
}

function __beforLogoff() {
	//window.top.location = "logoff.jsp?w=1";
	beforeAutoLogoff();
}

function ovrError(t) {
	alert("Msw Center Error:" + t);
}

function members(list) {
	console.log(JSON.stringify(list));
	list.unshift("All|All");
	addMyList("list", list);
}

function addMyList(elementId, list) {
	var $o = $('#' + elementId);
	$o.html('');
	for (var i = 0; i < list.length; i++) {
		var ss = list[i].split('|');
		$o.append(new Option(ss[1], ss[0]));
	}
}

function getSupListResp(list) {
	console.log(JSON.stringify(list));
	//	dwr.util.removeAllOptions("supList");
	//	dwr.util.addOptions("supList", list);
	addMyList("supList", list);
}

function jsOvrHandler(passage) {
	var hnd = "jsOvrHandler";
	console.log('ovrResp:' + JSON.stringify(passage));
	switch (passage.action) {
		case 'ovrReq': // supervisor got ovr request
			$("#ovrList").append(new Option(passage.content, passage.from));
			var ack = createPassage(passage.from, 'ack', 'text', hnd + '.ovrReqAck');
			ack.ackSessionId = passage.ackSessionId;
			setTimeout(function() {
				MswServer.send(ack);
			}, 200);
			break;
		case 'ovrReqAck':
			unlock();
			lock("授權要求已送出, 等候處理", 60);
			break;
		default:
			alert("unknown action:" + passage.action);
	}
}

function receiveMessages(p) {
	console.log(JSON.stringify(p));
	var chatlog = "";
	chatlog = "<div>" + dwr.util.escapeHtml(p.content) + "</div>" + chatlog;
	dwr.util.setValue("chatlog", chatlog, {
		escapeHtml: false
	});
}
// util
function combine(f1, f2) {
	return function() {
		if (f1 != null && typeof(f1) == 'function') {
			f1();
		}
		if (f2 != null && typeof(f2) == 'function') {
			f2();
		}
	};
}

function myConfirm(btns, dialogText, dialogTitle, opts) {
	var $dlg = null;
	var closeDialog = function() {
		$dlg.dialog("destroy");
	};
	btns = $.map(btns, function(x) {
		x.click = combine(x.click, closeDialog);
		return x;
	});
	$dlg = $('<div style="padding: 10px; max-width: 500px; word-wrap: break-word;">' + dialogText + '</div>').dialog({
		draggable: true,
		modal: true,
		resizable: false,
		width: 'auto',
		title: dialogTitle || '請確認',
		minHeight: 75,
		buttons: btns
	});
}

function myConfirm1(dialogText, okFunc, cancelFunc, dialogTitle) {
	$('<div style="padding: 10px; max-width: 500px; word-wrap: break-word;">' + dialogText + '</div>').dialog({
		draggable: false,
		modal: true,
		resizable: false,
		width: 'auto',
		title: dialogTitle || 'Confirm',
		minHeight: 75,
		buttons: {
			OK: function() {
				if (typeof(okFunc) == 'function') {
					//setTimeout(okFunc, 50);
					okFunc();
				}
				$(this).dialog('destroy');
			},
			Cancel: function() {
				if (typeof(cancelFunc) == 'function') {
					//setTimeout(cancelFunc, 50);
					cancelFunc();
				}
				$(this).dialog('destroy');
			}
		}
	});
}

function myFilter(array, fn) {
	var r = [];
	for (var i = 0; i < array.length; i++) { //2018/01/19 潘
		if (fn(array[i])) r.push(array[i]);
	}
	return r;
}
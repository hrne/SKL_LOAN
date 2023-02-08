function getTabfn(n) {
	return top.frames['easytab'].contentWindow[n];
}

function getTopFn(name) {
	return top.frames['head'].contentWindow[name];
}

function getFrameId() {
	var i = 0;
	var tx = "tx_";
	getFrameId = function () {
		return tx + (i++);
	};
	return tx + (i++);
}

function addTab(title, url, bClosable, prefix, noclose) {
	bClosable = bClosable === false ? false : true;
	// 鍾 menu title 連動->I0000/A0000, 更正(審核/放行)->更正(審核/放行)/I0000
	if (prefix) {
		if (prefix.indexOf("連動") == -1) {
			if (prefix.indexOf("重登") >= 0) {
				prefix = "重登/";
			} else if (prefix.indexOf("訂正") >= 0) {
				prefix = "訂正/";
			} else if (prefix.indexOf("交易明細") >= 0) {
				prefix = "交易明細/";
			} else if (prefix.indexOf("放行") >= 0) {
				prefix = "放行/";
			} else if (prefix.indexOf("審核") >= 0) {
				prefix = "審核/";
			} else if (prefix.indexOf("修正") >= 0) {
				prefix = "修正/";
			} else if (prefix.indexOf("在途查詢") >= 0) {
				prefix = prefix.slice(4);
			} else if (prefix.indexOf("提交") >= 0) prefix = "提交/";
		} else {
			prefix = prefix.replace("連動", "/");
		}
	} else {
		prefix = '';
	}
	var content;
	var i = 0;
	var t = prefix + title;
	var frameId = '';
	// var ocetabs = ["XG910 "]; //移除部分交易不能重複開啟的功能
	if (bClosable == true) {
		for (var i = 0, t = prefix + title + ' '; i < Number.MAX_VALUE; i++, t = prefix + title + ' (' + i + ') ') {
			if (!$('#tt').tabs('exists', t)) {
				title = t;
				break;
			} else if (t.indexOf("主機") != -1 || candupOpentab(title, prefix) || (t.substring(2, 3) == "0" || t.substring(2, 3) == "9" || t.substring(2, 3) == "R")) { // 可重複開啟頁籤
				console.log("連動交易/主機連動:" + t + ",可重複開啟...");
			} else {
				console.log("The tab is duplicate! Go return");
				alert("相同頁籤已重複開啟");
				return;
			}
			/*
			 * 移除部分交易不能重複開啟的功能 else if($.inArray(t,ocetabs) > -1){ //是否可以重複開啟tab?? console.log("The
			 * tab is duplicate! Go return"); return; }
			 */
		}
		frameId = getFrameId();
		url += '&id=' + frameId;
		content = buildIframe(frameId, url);
	} else {
		frameId = 'menuFrame';
		content = buildIframe(frameId, url);
	}
	// content = '<div style="overflow:auto;width:100%;height:100%;padding:0px;">' + content +
	// '</div>';
	// 新增測試
	if (noclose) {
		bClosable = false;
	}
	$('#tt').tabs('add', {
		title: title,
		content: content,
		closable: bClosable
	});

	//添加關閉全部頁簽
	addCloseAll(title);

	addId(frameId, title);
	setMapValueAt(title, 'id', frameId);
	setMapValueAt(title, 'noclose', noclose);
}

function closeAll() {
	if (window.confirm("確定關閉全部頁簽?")) {
		$(".tabs li").each(function (index, obj) {
			//所有全部可關閉的頁簽
			var tab = $(".tabs-closable", this).text();
			$(".easyui-tabs").tabs('close', tab);
		});
		$("#close").remove();//此按鈕也關閉
	}
}

function addCloseAll(title) {
	if (title != "Menu" && $(".tabs li").size() > 2) {
		var li = $(".tabs-wrap ul li:last-child");
		$("#close").remove();
		li.after("<li id='close'><a class='tabs-inner' style='height: 32px; line-height: 30px;' href='javascript:void()' onClick='javascript:closeAll()' TITLE='關閉全部頁簽'><img src='images/icon/close.png' style='width:32px;height:26px; padding-top: 4px;'></a></li>");
	}
}

// 判斷是否可以重複開啟頁籤 (單一頁簽不重複,其餘皆可)
function candupOpentab(title, prefix) {
	if (prefix) {
		return true;
	}
	return false;
}

function buildIframe(id, url) {
	// return '<iframe scrolling="auto" frameborder="0" src="' + url
	// + '" style="padding-bottom:0px;margin-bottom:0px;width:100%;height:' +
	// viewSize.height + 'px;"></iframe>';
	url = encodeURI(url);
	var t = '<iframe id="' + id + '" src="' + url + '" ';
	var style = ' scrolling="auto" frameborder="0" style="width:100%;height:100%;padding-bottom:0px;margin-bottom:0px;"></iframe>';
	return t + style;
}
var __tabSeq = 1;
var __tabMap = {};
var _tabId2Title = {};
var _lastTabTitle, submitTab = new Map();

function setAutoSunmitFg(title) {
	var tts = title.split("/");
	submitTab.set(title, tts[tts.length - 1]);
}

function getTabTitle() {
	var tab = $('#tt').tabs('getSelected');
	var title = tab.panel("options").title;
	return title;
}

function addId(id, title) {
	_tabId2Title[id] = title;
}

function getTitleById(id) {
	return _tabId2Title[id];
}

function deleteId(id) {
	delete _tabId2Title[id];
}

function setMapValueAt(title, key, value) {
	if (!__tabMap[title]) __tabMap[title] = {};
	__tabMap[title][key] = value;
}

function registerTran(title, name, fn) {
	if (!__tabMap[title]) __tabMap[title] = {};
	__tabMap[title][name] = fn;
	return __tabMap[title]['id'];
}

function getTabMap() {
	return __tabMap;
}

function getTabs() {
	var tabs = $('#tt').tabs('tabs');
	var r = [];
	/*
	 * for(var i=0; i < tabs.length; i++) r.push(tabs[i].title); return r;
	 */
	return tabs;
}

function getTabslogoff() {
	console.log("getTabslogoff!");
	var tabs = $('#tt').tabs('tabs');
	var r = [];
	// 如果是強制登出,就不管頁簽數量..直接回覆1
	var pp = $('#tt').tabs('getSelected'); // get selected panel
	var tab = pp.panel('options').tab;
	if (tab.text() == "強制登出") {
		return 1;
	}
	return tabs;
}
// for map back
function getCallerTab(title) {
	var muilttile = "";
	// 鍾 menu title 連動->I0000/A0000
	var ss = title.split('/');
	if (ss.length < 2) {
		return null;
	}
	// 柯新增 審核/修改交易 menu 會多個名稱，故在此新增 muilttile
	for (var i = 1; i < ss.length - 1; i++) {
		// if (ss[i].length > 4)
		// ss[i] += " ";
		muilttile = muilttile + "/" + ss[i];
	}
	// if (ss[0].length > 4)
	// ss[0] += " ";
	if (ss.length > 1) {
		return ss[0] + muilttile;
	}
}

function redirectCall(title, name, mapper, data) {
	var fn = __tabMap[title][name];
	fn(mapper, data);
}
// end map back
function selectMenu() {
	console.log("selectMenu");
	$('#tt').tabs('select', 'Menu');
}
var _ifxMenu = null;

function registerIfxMenu(ifxMenu) {
	_ifxMenu = ifxMenu;
}

function getIfxMenu() {
	console.log("getIfxMenu");
	return _ifxMenu;
}

function isUpdatedTran(txcdtmp) {
	var chktxcd = txcdtmp;
	if (chktxcd[0] == "X") {
		return (chktxcd[2] != "9" && chktxcd[2] != "R");
	} else {
		return (chktxcd[1] != "0" && chktxcd[1] != "R");
	}
}
// begin hot tran
function addHotTran(title, txcode, url, prefix) {
	console.log("addHotTran select Menu!");
	$('#tt').tabs('select', 'Menu');
	console.log("###addHotTran");
	if ($('#tt').tabs('exists', title)) {
		$('#tt').tabs('select', title);
	} else {
		addHotTab(title, url, true);
		addCloseAll(title);
	}
}

function addHotTab(title, url, bClosable) {
	bClosable = bClosable === false ? false : true;
	var content;
	frameId = getFrameId();
	url += '&id=' + frameId;
	url += '&hot=1';
	content = buildIframe(frameId, url);
	$('#tt').tabs('add', {
		title: title,
		content: content,
		style: {
			margin: 0,
			padding: 0
		},
		closable: bClosable
	});
	addId(frameId, title);
	setMapValueAt(title, 'id', frameId);
}
// end hot tran
function addTran(txcode, url, prefix, noclose) {
	// $('#tt').tabs('select', 'Menu'); //TODO 註解後確認有無影響
	// 計算tabs數量
	var maxtabs = 20; // 交易開啟上限
	var tabsconut = $('.tabs-wrap >ul >li').size();
	if (tabsconut > maxtabs && url.indexOf("chain") == -1) {
		alert("同時開啟頁籤超過上限(" + maxtabs + ") 請關閉頁籤後再試。");
		return;
	}
	console.log("####addTran");
	addTab(txcode, url, true, prefix, noclose);
}

function addMenu() {
	var title = 'Menu';
	console.log("####addMenu");
	if ($('#tt').tabs('exists', title)) {
		$('#tt').tabs('select', title);
	} else {
		// addTab(title, "easy_menu.jsp", false);
		addTab(title, "easy_menu2.jsp", false);
		menuFrame = $('#menuFrame');
	}
}

var menuFrame;
var whiteMenuLi = [];

function isInMenu(txCode) {
	return whiteMenuLi.indexOf(txCode) != -1;
}

function handle_txcode() {
	$('#txcode').change(function () {
		console.log("handle_txcode!!");
		var s = $(this).val().toUpperCase();
		if (s.length == 5) {
			var u = "tran2.jsp?txcode=" + s;
			console.log("####handle_txcode");
			addTran(s, u);
			$(this).val('');
		}
	}).keypress(function () {
		var s = $(this).val();
		if (s.length == 5) $(this).trigger("change");
	});
}
// for XG910交易 判斷是否是停在該頁籤上
function checkActive(title) {
	var pp = $('#tt').tabs('getSelected'); // get selected panel
	var tab = pp.panel('options').tab;
	if (tab.text() == title) {
		return true;
	} else {
		return false;
	}
}
// end
var __forceCloseTab = true;
var ifx_self = {};

function setIfxSelf(v) {
	ifx_self[getTabTitle().trim()] = v;
}

function closeTab(title, bForce) {
	__forceCloseTab = bForce;
	if ($('#tt').tabs('exists', title)) {
		try {
			var deviceX = getDevice();
			// 20171227 add
			// if(deviceX.checkDocument())
			// deviceX.eject();
			// else
			// deviceX.returnSession();//潘 20171230
		} catch (ee) { }
		$('#tt').tabs('close', title);
	}
}

function handleChain(o) {
	if (!o) return;
	var action = o['action'];
	if (action === 'run' && o['target'] == 'new') {
		var p = o['callee'] + o['prompt'];
		if (o['chain'] == '9') p = o['prompt'];
		delete o['target'];
		delete o['action'];
		delete o['prompt'];
		var noclose = false;
		if (o['noclose']) {
			noclose = o['noclose'];
		}
		delete o['noclose'];
		var u = "tran2.jsp?txcode=" + o['txcode'] + '&chain=' + o['chain'];
		for (var k in o.extra) {
			u += ('&' + k + '=' + o.extra[k]);
		}
		console.log("handleChain!!");
		console.log("handleChain:" + o['txcode'] + "," + u + "," + p.toUpperCase());
		if (o['goChain'] == "F") {
			$(".tabs li").each(function (index, obj) {
				//所有全部可關閉的頁簽
				var tab = $(".tabs-closable", this).text();

				if (tab.indexOf(o['txcode']) != -1)
					$(".easyui-tabs").tabs('close', tab);
			});
		}
		setTimeout(function () {
			console.log("####handleChain");
			addTran(o['txcode'], u, o['goChain'] == "F" ? "" : p.toUpperCase(), noclose);
		}, 100);
	}
}

function updatePanel() {
	// update the selected panel with new title and content
	var tab = $('#tt').tabs('getSelected'); // get selected panel
	$('#tt').tabs('update', {
		tab: tab,
		options: {
			title: 'New Title',
			href: 'get_content.php' // the new content URL
		}
	});
	// call 'refresh' method for tab panel to update its content
	var tab = $('#tt').tabs('getSelected'); // get selected panel
	tab.panel('refresh', 'get_content.php');
}

function initSideMenu() { // 潘
	sidemenu.init('dashboard3', {
		top: 0,
		orientation: 'h',
		startWidth: 40
	}, function () {
		// 潘 動態顯示側邊條
		if (_sysvar['LEVEL'] < 3) { // 主管櫃員左方顯示不同
			sidemenu.enable("modify", false);
			sidemenu.enable("review", false);
			sidemenu.enable("delay", false);
			sidemenu.enable("ecreload", false);
			sidemenu.enable("SendOut", false);
		} else {
			sidemenu.enable("approval", false);
			sidemenu.enable("swift_approval", false);
		}
		// sidemenu.enable("file_upload", true);
		sidemenu.enable("divice_restart", false);
		sidemenu.enable("user_setting", false);
		sidemenu.enable("eBank", false);
		sidemenu.enable("xx9tl", false);
		sidemenu.enable("delay", false);
		sidemenu.enable("swift_approval", false);
	});
}

function scrollTabWin(top, ms) {
	$('html,body').animate({
		scrollTop: top
	}, ms);
	// $('#center').animate({
	// scrollTop : top
	// }, ms);
}

function getScrollTop() {
	return $(window).scrollTop();
	// return $('#center').scrollTop();
}

function getTabWindow$() {
	return $(window);
	// return $('#center');
}
var logoutFlag = false;
var enableBeforeUnload = false;
if (enableBeforeUnload) {
	$(window).bind('beforeunload', function () {
		// IE
		if (event.clientY < 0 || event.altKey) {
			logoutFlag = true;
			return '為了您的交易安全，我們將為您執行登出，';
		} else {
			logoutFlag = false;
		}
	});
}

function xhtr() {
	var obj = null;
	try {
		obj = new ActiveXObject("Msxml2.XMLHTTP")
	} catch (e) {
		try {
			obj = new ActiveXObject("Microsoft.XMLHTTP")
		} catch (oc) {
			obj = null;
		}
	}
	if (!obj && typeof XMLHttpRequest != "undefined") {
		obj = new XMLHttpRequest();
	}
	return obj
}
$(window).bind('unload', function () {
	if (logoutFlag) {
		var xmlhttp = xhtr();
		var htmlUrl = 'logoff.jsp';
		xmlhttp.open("POST", htmlUrl, false);
		xmlhttp.send();
	}
});
// begin ovr
var $supPanel;
var $mswPanel;

function initOvrCenter() {
	$mswPanel = $('#mswPanel');
	$mswPanel.dialog({
		autoOpen: false,
		height: 250,
		width: 900,
		modal: false
	}).show();
	// openMswDlg();
	initDWR();
	// if is superviso, then init supervisor panel
	$supPanel = $('#supervisorPanel');
	$supPanel.dialog({
		autoOpen: false,
		height: 300,
		width: 600,
		modal: true
	});
	$('#btnSupScreenCopy').off('click').on('click', supScreenCopyClicked);
	// $('#btnSupAcceptNow').off('click').on('click', supAcceptNowClicked);
	// $('#btnSupRejectNow').off('click').on('click', supRejectNowClicked);
	$('#btnSupRemoveItem').off('click').on('click', supRemoveItemClicked);
	$('#btnSupCancelSelect').off('click').on('click', supCancelSelectClicked);
}

function openMswDlg() {
	if ($mswPanel.dialog("isOpen")) {
		return;
	}
	$mswPanel.dialog("open");
}
var clientOvrReq = null;

function startOvr(oData) {
	oData['localDiv'] = 'ovrPanel';
	oData['remotePage'] = 'supovr/remoteOvr.jsp';
	oData['remoteDiv'] = 'ovrMain';
	clientOvrReq = new OvrReq(oData);
	return clientOvrReq.init();
}
// end ovr
// esc control panel
function openConfirmDialog(title) {
	$(window).on('keypress.confirm', function (e) {
		console.log('key:' + e.keyCode);
		switch (e.keyCode) {
			case 49: // 1
			case 27: // esc
				exit();
				break;
			case 50: // 2
				clear();
				break;
			case 51: // 3
				cancel();
				break;
		}
	});

	function closeDialog(fn) {
		if (fn) fn();
		$("#dialog-confirm").dialog('close');
		$(window).off('keypress.confirm');
	}

	function exit() {
		closeDialog(function () {
			closeTab(title, true);
		});
	}

	function clear() {
		var fnClr = __tabMap[title]['clr'];
		closeDialog(fnClr);
	}

	function cancel() {
		var fn = __tabMap[title]['goCurrent'];
		closeDialog(fn)
	}
	$("#dialog-confirm").dialog({
		resizable: false,
		bgiframe: true,
		position: 'center',
		height: 'auto',
		maxHeight: 500,
		width: 420,
		title: '確定要離開' + title + '嗎?',
		closeOnEscape: false,
		modal: true,
		closeText: "hide",
		buttons: {
			"離開": function () {
				// $( this ).dialog( "close" );
				exit();
			},
			/*
			"按2清除" : function() {
				clear();
			},
			*/
			"按3取消": function () {
				cancel();
			}
		}
	}).show();
}
var myLayout;

function startLayout() {
	myLayout = $('body').layout({
		center__maskContents: true // IMPORTANT - enable iframe masking
		,
		stateManagement__enabled: true // automatic cookie load & save enabled
		// by default
		,
		north__size: 28,
		north__maxSize: 28,
		// north__closable:false,
		west: {
			size: 110,
			spacing_closed: 20,
			togglerLength_closed: 150,
			togglerAlign_closed: "top",
			togglerContent_closed: "<BR><BR>功<BR><BR>能<BR><BR>列<BR><BR>",
			togglerTip_closed: "開啟(釘選)",
			sliderTip: "滑出",
			slideTrigger_open: "mouseover",
			onclose_end: function () {
				$('#tt').tabs('resize');
			}
		}
	});
	myLayout.close('west');
	myLayout.close('east');
	myLayout.close('south');
	myLayout.close('north');
}

function initSouthPark() {
	$.fn.saveText = function () {
		return this.each(function () {
			var $this = $(this);
			$this.attr('__mytext__', $this.text());
		});
	};
	$.fn.restoreText = function () {
		return this.each(function () {
			var $this = $(this);
			$this.text($this.attr('__mytext__'));
		});
	};
	// bigTable();
	// $('#swift-code-area').show();
}

function bigTable() {
	$btn = $('#btnLoad');
	$btn.click(function () {
		$btn.attr("disabled", true);
		// $btn.attr("oldText", $btn.text());
		$btn.saveText();
		//$btn.text('please wait...');
		var url = _contextPath + "/mvc/hnd/rim/SWIFT?m=*";
		var $aj = $.ajax({
			cache: true,
			url: url,
			dataType: 'json'
		});
		$aj.done(function (data) {
			populate(data.result);
		});
		$aj.fail(function (err) {
			alert(err);
		});
		$aj.always(function () {
			$btn.attr("disabled", false);
			// $btn.text($btn.attr("oldText"));
			$btn.restoreText();
		});
	});
}

function populate(mydata) {
	var $grid = $("#grid");
	$grid.jqGrid({
		datatype: "local",
		colNames: ['', 'SWIFT CODE', 'BANK NAME', 'ADDRESS'],
		colModel: [{
			name: 'id',
			index: 'id',
			width: 15,
			sorttype: "int",
			hidden: true
		}, {
			name: 'key',
			index: 'key',
			width: 60
		}, {
			name: 'name',
			index: 'name',
			width: 150
		}, {
			name: 'address',
			index: 'address',
			width: 250
		},],
		height: '100%',
		width: 1000,
		resize: true,
		gridview: true,
		// rownumbers:true,
		viewrecords: true,
		shrinkToFit: true,
		autoencode: true,
		filter: false,
		rowNum: 10,
		rowList: [10, 20, 50, 100, 200],
		pager: $('#pager'),
		multiselect: false,
		caption: "Big Swift Data",
		onSelectRow: function (rowid) {
			var row = $(this).getLocalRow(rowid);
			console.log(row);
			updateResult(row);
		}
	});
	for (var i = 0; i < mydata.length; i++) {
		$grid.jqGrid('addRowData', i + 1, convertData(mydata[i], i));
	}
	$grid.jqGrid('setGridParam', {
		page: 1
	}).trigger("reloadGrid");
	$grid.jqGrid('filterToolbar', {
		searchOnEnter: true,
		stringResult: true,
		defaultSearch: 'cn'
	});
}

function convertData(r, i) {
	var d = {};
	d['id'] = r.id;
	try {
		d['key'] = r.key;
	} catch (ee) {
		alert(ee);
	}
	d['name'] = r.data.slice(0, 35);
	d['address'] = r.data.slice(37, 72) + "\n" + r.data.slice(72, 107) + "\n" + r.data.slice(107);
	return d;
}

function updateResult(row) {
	$('#swifit-code-result').fadeIn();
	$('#txtCode').text(row['key']);
	$('#txtName').text(row['name']);
	var addr = row['address'].split('\n');
	$('#txtAddr1').text(addr[0] || '');
	$('#txtAddr2').text(addr[1] || '');
	$('#txtAddr3').text(addr[2] || '');
}

function beforeAutoLogoff() {
	var waitSeconds = 50.5;
	$("#logoff-dialog").dialog({
		position: [0, 0],
		modal: true,
		buttons: {
			"繼續使用": function () {
				var stopLogoff = true;
				autoLogoff(stopLogoff);
			},
			"登出系統": function () {
				autoLogoff();
			}
		}
	});
	$("div#logoff-dialog").dialog().prev().find(".ui-dialog-titlebar-close").hide();
	$('#logoff-shortly').countdown({
		format: 'MS',
		onExpiry: autoLogoff,
		onTick: watchCountdown
	});
	var shortly = new Date();
	shortly.setSeconds(shortly.getSeconds() + waitSeconds);
	$('#logoff-shortly').countdown('option', {
		until: shortly
	});

	function autoLogoff(bStop) {
		bStop = !!bStop;
		$('#logoff-shortly').countdown('destroy');
		if (bStop) {
			var cmdObj = {};
			cmdObj["cmd"] = "ping";
			$.ajax({
				type: 'post',
				dataType: 'json',
				url: _contextPath + "/mvc/hnd/keepAlive",
				data: { _d: JSON.stringify(cmdObj) },
				success: function (res) {
					console.log("系統回覆..繼續使用(" + res.msg);
				}
			});
		} else {
			// window.top.location = "logoff.jsp?w=1";
			sidemenu.handlers["logoff"].run(true);
		}
		$("div#logoff-dialog").dialog("close");
	}

	function watchCountdown(periods) {
		$('#logoff-monitor').text('還有 ' + periods[5] + ' 分鐘  ' + periods[6] + ' 秒就登出 !');
	}
}
var returnclose;

function loadmenu() {
	//var url = _contextPath + "/mvc/hnd/menu2/jsonp?menu=";
	var ip = location.host.split(":");
	var port = ip.length > 1 ? ip[1].substring(0, 3) + "5" : "7005";
	var url = "http://" + ip[0] + ":" + port + "/iTX/mvc/hnd/menu2/jsonp?authNo=";
	//url = _contextPath + "/mvc/hnd/menu2/jsonp?menu=";
	//	var $rootGetter = $.get(url);
	var root;
	//	$rootGetter.done(function(data) {
	//		len = data.length - 1;
	//		IfxMenu.setup('#sklMenu', url);
	//		root = IfxMenu.buildRoot(data);
	//	});
	//	$rootGetter.fail(function() {
	//		alert("無法建立選單, 請重新整理!");
	//	});
	$.ajax({
		type: "get",
		url: url,
		async: false,
		dataType: "json",
		jsonpCallback: "callback",
		success: function (res) {
			IfxMenu.setup('#sklMenu', url);
			root = IfxMenu.buildRoot(res.data);
		},
		error: function (e) {
			console.log("fail,," + e.toString());
			console.dir(e);
		}
	});
	console.log(root);
	var drop = "dropdown-menu-right";
	var styleElement = document.getElementById('sklMenu');
	if (styleElement && screen.width == 1024) styleElement.style.cssText = 'font-size: 0.84rem;';
	//root.forEach((x) => {
	$.ajax({
		type: "get",
		//url: url + $.trim(x.txcd),
		url: url + $.trim(_sysvar.AUTHNO),
		async: false,
		dataType: "json",
		jsonpCallback: "callback",
		success: function (res) {
			var items = IfxMenu.expandData(res.data);
			var tanin = 0;
			var temp = {};
			var dropLeft = "dropleft";
			var dropRight = "dropright";
			$.each(items, function (i, x) {
				var t;
				//var tanin = x.sbtype.substring(1, 2);
				console.log("childMenu" + x.txcd + " = " + x.txnm);
				if ($.trim(x.txcd).length >= 5) {
					if ($("#" + x.sbtype).length > 0) {
						;
					} else {
						if ($("#menu" + x.sbtype.substring(0, 2)).length > 0)
							;
						else {
							t = '<li class="nav-item dropdown"><a class="nav-link dropdown-toggle" tabindex="0" data-toggle="dropdown" data-submenu="">' + root[x.sbtype.substring(0, 2)] + '</a><div id="menu' + $.trim(x.sbtype.substring(0, 2)) + '" class="dropdown-menu ' + (tanin == 0 || tanin == 1 ? "dropdown-menu-left" : "dropdown-menu-right") + '"></div></li>';
							$("#sklMenu").append(t);
							tanin++;
						}
						t = '<div class="dropdown ' + (tanin >= 7 ? dropLeft : dropRight) + ' dropdown-submenu"><button class="dropdown-item dropdown-toggle" type="button" data-toggle="dropdown">' + temp[x.sbtype] + '</button><div id="' + $.trim(x.sbtype) + '" class="dropdown-menu scrollable-menu"></div></div>';
						$("#menu" + x.sbtype.substring(0, 2)).append(t);
					}
					t = '<button class="dropdown-item" type="button" onclick=\"addTran(\'' + x.txcd + '\',\'tran2.jsp?txcode=' + x.txcd + '\');\">' + x.txcd + '.' + $.trim(x.txnm) + '</button>';
					whiteMenuLi.push(x.txcd.trim());
					$("#" + x.sbtype).append(t);
				} else
					temp[x.sbtype] = x.sbtype.substring(2, 3) + "." + $.trim(x.txnm);
				/*
				if (x.sbtype.length == 3 && $.trim(x.txcd).length < 5) {
					//				  	t = "<li><a href='#'>" + x.sbtype.substring(2, 3) + "." + $.trim(x.txnm) + "</a><ul id='menu" + $.trim(x.sbtype) + "'></ul></li>";
					t = '<div class="dropdown ' + (tanin >= 7 ? dropLeft : dropRight) + ' dropdown-submenu"><button class="dropdown-item dropdown-toggle" type="button" data-toggle="dropdown">' + x.sbtype.substring(
						2, 3) + "." + $.trim(x.txnm) + '</button><div id="' + $.trim(x.sbtype) + '" class="dropdown-menu scrollable-menu"></div></div>';
					console.log(t)
					$("#menu" + x.sbtype.substring(0, 2)).append(t);
				} else {
					if ($.trim(x.txcd).length >= 5) {
						//				  		t = "<li><a href=\"javascript:void(0)\" onclick=\"addTran('" + x.txcd + "', 'tran2.jsp?txcode=" + x.txcd + "');\">" + x.txcd + "." + $.trim(x.txnm) + "</a></li>";
						t = '<button class="dropdown-item" type="button" onclick=\"addTran(\'' + x.txcd + '\',\'tran2.jsp?txcode=' + x.txcd + '\');\">' + x.txcd + '.' + $.trim(x.txnm) + '</button>';
						$("#" + x.sbtype).append(t);
					}
				}
				*/
			});

			$("#sklMenu").append("</ul>");
		},
		error: function (e) {
			console.log("fail,," + e.toString());
			console.dir(e);
		}
	});
	//});
	$('[data-submenu]').submenupicker();
}

function initTab() {
	var currTitle = null;
	initSideMenu();
	var fn;
	loadmenu();
	addMenu();
	$('#tt').tabs({
		fit: true,
		onBeforeClose: function (title) {
			var closeIt = false;
			if (__forceCloseTab) {
				closeIt = true;
			} else {
				if (__tabMap[title]) {
					// if(__tabMap[title]['noclose']){
					// alert("請按[離開]按鈕退出交易!");
					// return false;
					// }
					var fnSaved = __tabMap[title]['issaved'];
					var bSaved = fnSaved == undefined || fnSaved(); // no fnSave
					// --> let
					// bSave =
					// true
					var p = '確定要關掉 ';
					if (!bSaved) p = "尚未儲存交易\n" + p;
					// closeIt = confirm(p + title);
					openConfirmDialog(title);
					$("#dialog-confirm").focus();
				} else {
					closeIt = true;
				}
				__forceCloseTab = true;
			}
			if (closeIt) {
				var title2 = title.split("/");
				title2 = title2.length > 1 ? title2[1] : title2[0];
				if (ifx_self[title.trim()] && ifx_self[title.trim()].escRim.length > 0 && ifx_self[title.trim()].txcd == title2.trim()) {
					ifx_self[title.trim()].escSync = true;
					// var bindMap = IfxUtl.str2map(_self.getValue('ESCRIM$').trim());
					// _self.bindHandler(bindMap);
					ifx_self[title.trim()].rtn.S(ifx_self[title.trim()].escRim);
					ifx_self[title.trim()].escSync = false;
				}
				// 鍾 menu title 連動->I0000/A0000, 更正(審核/放行)->更正(審核/放行)/I0000
				var ss = title.split('/');
				// 潘 控制連棟交易關閉時 returnSession 控制記號
				if (ss.length > 1) returnclose = true;
				else returnclose = false;
				console.log("關閉時連動" + ss);
				if (ss[0].indexOf("主機") != -1) {
					setTimeout(function () {
						$('#tt').tabs('select', 'Menu');
					}, 100);
				} else {
					var muilttile = "";
					// if (ss[0].length > 4)
					// 柯新增 審核/修改交易 menu 會多個名稱，故在此新增 tpod
					// end
					// 柯新增 審核/修改交易 menu 會多個名稱，故在此新增 tpod
					for (var i = 0; i < ss.length - 1; i++) {
						// if (ss[i].length > 4)
						// ss[i] += " ";
						muilttile = muilttile + "/" + ss[i];
						if (muilttile.slice(0, 1) == "/") {
							muilttile = muilttile.slice(1);
						}
					}
					if (ss.length > 1) {
						setTimeout(function () {
							$('#tt').tabs('select', muilttile);
						}, 100);
					}
				}
			}
			return closeIt;
		},
		onClose: function (title) {
			console.log("onClose!!");
			title = title.toUpperCase();
			if (title != "MENU") {
				try {
					var deviceX = getDevice();
					if (!returnclose) deviceX.returnSession(); // 潘 20180104
				} catch (ee) { }
				deleteId(__tabMap[title]['id']);
				delete __tabMap[title];
				_lastTabTitle = title;
			}

			if ($(".tabs li").size() == 3 && $("#close").length > 0)
				$("#close").remove();
		},
		onSelect: function (title) {
			currTitle = title;
			// 柯,for換tab時,畫面亂掉的bug
			myLayout.close('west');
			console.log("onSelect!!");
			$('#tt').tabs('getSelected').focus();
			title = title.toUpperCase();
			// 鍾 menu title 連動->I0000/A0000, 更正(審核/放行)->更正(審核/放行)/I0000
			// if (title != "MENU" && title != "更正" && title != "放行") {
			if ($.inArray(title, ["MENU"]) == -1) {
				// end
				if (__tabMap[title]) {
					fn = __tabMap[title]['refresh'];
					if (fn) fn();
					else {
						console.log("no refresh!"); // "no fn"
					}
				}
			} else {
				fn = top.frames['head'].contentWindow['resetTranStatus'];
				if (fn) {
					fn();
				}
				if (__tabMap['MENU']) {
					fn = __tabMap['MENU']['refresh'];
					if (fn) fn();
				}
			}
			var callerTab;
			if (__tabMap[title] && title != "MENU") {
				//callerTab = getCallerTab(title);
				redirectCall(title, 'scrollRollBack');
			}
			if (submitTab.get(_lastTabTitle) != undefined) {
				callerTab = getCallerTab(_lastTabTitle);
				redirectCall(callerTab, 'chainAutoSubmit', "A", "B");
				submitTab.delete(_lastTabTitle);
			}
		},
		onUnselect: function (title) {
			_lastTabTitle = title;
			if (currTitle == "XG910 ")
				console.log("on Unselect XG910 !");
		}
		// width : $(window).width(),
		// height : $(window).height()
	});
	// 未真的進入頁籤時 實作esc 按鈕
	// 換頁籤時 如果沒有點裡面的畫面,則跑此段(故沒有tranEnd邏輯)
	$(window).keypress(function (evt) {
		var kcode = evt.keyCode;
		if (kcode == 27 && currTitle != "Menu" && currTitle) {
			console.log(" in ifx-easytab.js window keypress [ESC]!");
			var closeitt = isUpdatedTran(currTitle);
			closeTab(currTitle, !closeitt);
		}
	});
	// handle_txcode();
	$('#txcode').focus();
	// $("#tt").css("display","block").tabs('resize');
	$(window).resize(function () {
		// document.getElementsByTagName('body')[0].style.height = '100%';
		// document.getElementsByTagName('body')[0].style.width =
		// $(window).width();
		// $('#tt').width( $(window).width());
		// $('#tt').height($(window.top).height());
		console.log("changed in eztab 123");
		/*
		 * console.log("tab size:"+$('#tt').width() + ","+$('#tt').height()) console.log("resizing
		 * to:"+$(window).width() + ","+$(window.top).height()) console.log("tab
		 * win:"+$(window).width() + ","+$(window).height())
		 */
		if (this.resizeTO) clearTimeout(this.resizeTO);
		this.resizeTO = setTimeout(function () {
			console.log('resize handler');
			$('#tt').tabs('resize');
			console.log('curr title:' + currTitle);
			setTimeout(function () {
				// $('#tt').tabs('select', currTitle);
				try {
					console.log("window .resize!!");
					if (currTitle) {
						currTitle = currTitle.toUpperCase();
						if (__tabMap[currTitle]) {
							var fn = __tabMap[currTitle]['resize'];
							if (fn) {
								fn();
							}
						}
					}
				} catch (ee) {
					console.error(ee);
				}
			}, 10);
		}, 500);
		console.log("$(window).resize done");
		// tab.panel('refresh');
		// alert($(window).width());
		// $('#tt').width($(window).width()-20);
		// $("#tt").css("display","block").tabs('resize');
	});
	// alert($(window).width());
	$(window).trigger('resize');
	// unloadScrollBars();
	function reloadScrollBars() {
		document.documentElement.style.overflow = 'auto'; // firefox, chrome
		document.body.scroll = "yes"; // ie only
	}

	function unloadScrollBars() {
		document.documentElement.style.overflow = 'hidden'; // firefox, chrome
		document.body.scroll = "no"; // ie only
	}
	initOvrCenter();
	startLayout();
	setTimeout(function () {
		$('#tt').tabs('select', 'Menu');
		console.log("select Menu end");
	}, 10);
	initSouthPark();
	setTimeout(initTickers, 1000);
	setTimeout(initDevice, 1);
} // end of initTab
function initTickers() {
	var $tic = $('#tickers');
	var lastData = null;
	var u = _contextPath + "/mvc/hnd/tickers";
	var repeater = function () {
		// 柯:新增 now 到 ticker內
		$.get(u, {
			"_": $.now()
		}).done(function (data) {
			if (data.length > 0) {
				if (isChanged(data)) {
					lastData = data;
					$tic.html(makeUL(data));
					setTimeout(function () {
						$tic.vTicker({
							speed: 1000,
							padding: 2
						});
					}, 10);
					myLayout.show('north');
					myLayout.disableClosable('north', true);
				}
			} else {
				myLayout.disableClosable('north', false);
				// myLayout.close('north');
			}
		}).always(function () {
			setTimeout(repeater, 60000);
			// TODO 是否要移除此LOG?
			console.log("get tickers in 60 seconds");
		});
	};
	setTimeout(repeater, 10000);
	mswcenter_addTalkToCallback(function (m) {
		// m 格式為:MSGNO + "-" + text
		// 柯 + 如果第1碼為U時，表unique只保留最後一個訊息內容，但訊息讀取狀態需回恢成未讀取
		var fn = getTopFn('addNewMsgBtn');
		if (m.slice(0, 6) == "host: ") {
			m = m.substring(6);
			var ss = m.split("-"); // 這樣有可能會多分?
			if (ss[0][0] == "A") {
				alert(m);
			} else { // ItxtSample.pdf rim.txt
				// ss[1] = "111111111111 DItxtSample.pdf ";
				// ss[1] = "111111111111 YXC777RIM.TXT";
				fn(ss, true);
			}
		} else {
			fn(m, false);
		}
	});

	function isChanged(data) {
		if (lastData == null || lastData.length != data.length) {
			return true;
		}
		for (var i = 0; i < lastData.length; i++) {
			if (lastData[i] != data[i]) return true;
		}
		return false;
	}

	function makeUL(list) {
		var lines = [];
		lines.push("<ul>");
		$.each(list, function (i, x) {
			lines.push("<li>" + x + "</li>");
		});
		// var s = '<a href="http://tw.news.yahoo.com/快訊-馬媒-希臘郵輪-發現海上漂流行李-091200559.html"
		// target="_blank" >〈快訊〉馬媒：希臘郵輪 發現海上漂流行李</a>';
		// lines.push("<li>" + s +"</li>");
		lines.push("</ul>");
		return lines.join("");
	}
}

function getDevice() {
	return Device.pb;
}

function initDevice() {
	try {
		var deviceX = getDevice();
		// 潘 登入檢查元件
		// deviceX.setup('ifxws', 'myDevice');
		// deviceX.init();
		// 已關閉下方Layout,故不顯示
		// $("#device_version").text("device activex version:"+ deviceX.version());
	} catch (ee) {
		alert("device activex error:" + ee);
		if (ee.toString().indexOf("998") != -1) alert("缺少deviceagent，請另從合庫網站中下載。");
	}
	// 設定通知訊息音樂
	initMswMusic(deviceX);
}
// 依據參數檔設定通知訊息音樂
function initMswMusic(deviceX) {
	var filetmpname = "C:/ifx/IFX_PROPERTIES.config";
	var ifx_properties;
	try {
		ifx_properties = deviceX.file.read(filetmpname);
	} catch (ee) {
		console.log("initMswMusic error:" + ee)
	}
	// 可能沒有該檔案
	if (!ifx_properties) {
		ifx_properties = "{}";
	}
	var jsonlist = JSON.parse(ifx_properties);
	var fn = getTopFn("setMusicnum");
	$.each(jsonlist, function (i, x) {
		if (i == "mswmusicS") {
			fn(parseInt(x, 10));
		}
	});
}
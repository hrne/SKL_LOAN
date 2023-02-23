//var _contextPath = '${pageContext.request.contextPath}';
function getSysvar(k) {
	return _sysvar[k];
}
function setSysvar(k, v) {
	return _sysvar[k] = v;
}
function makeIfx() {
	//柯  改亂打進入錯誤
	console.log("makeIfx");
	try {
	var tranDef = getTranDef();
	} catch (e) {
		alert(_txcd + " 無此交易代號");
		var _tabKey = window.parent['getTitleById'](_changeSysVar["__tid"]);
    window.parent['closeTab'](_tabKey, true);
		return false;
	}
	//if (!getTranDef()) {
	//	alert("failed to get Ifx tran def");
	//	return;
	//}

	//Ifx.fn.init
	var ifx = new Ifx(_txcd, _tranAuth, tranDef, _pfns, _sysvar, {
		"topbuttons" : "#topbuttons",
		"entry" : "#entryArea",
		"buttons" : "#buttonsArea",  // 將buttonsArea傳進去
		'queryHeader' : '#queryHeader',
		'queryFooter' : '#queryFooter',
		'query' : '#queryArea',
		'gridControls' : {
			'grid' : '#grid',
			'pager' : '#pager',
			'search' : '#btnSearch',
			'reset' : '#btnReset',
			'newWin' : '#openWin',
			'moreData' :"#btnMoreData",
			'exportExcel' : '#btnExport',
			'exportXml' : '#btnExportxml',
			'batch' : '#btnBatch_',
			'queryHeader' : '#queryHeader',
		},
		'query2' : '#queryArea2',
		//第二個表格不會有輸出功能(XML,EXCEL)
		'gridControls_2' : {
			'grid' : '#grid2',
			'pager' : '#pager2',
			'search' : '#btnSearch2',
			'reset' : '#btnReset2',
			'newWin' : '#openWin2',
			'moreData' :"#btnMoreData2",
			'exportExcel' : '#btnExport2',
			'exportXml' : '#btnExportxml2',
			'batch' : '#btnBatch2'
		},
		"output" : "#outputArea",
		"help" : "#helpArea",
		"tooltip" : "#tooltipArea",
		"errmsg" : "#entry-errmsg"
	});

	return ifx;
}
// TODO: move initSideMenu to ifx.postDisplay
function initSideMenu(ifx) {
	sidemenu.init('dashboard2', {
		top : 10,
		growTo : 300
	}, function() {
		// TODO:或許menuitems也在這邊增加
		// begin hot tran
		// sidemenu.registerHandler("ec", ifx, "KeysEC");
		// end hot tran
		sidemenu.registerIfx(ifx);
		// 鍾 在任何交易下都可放行
		// sidemenu.registerHandler("approval", ifx, "KeysApproval");
		// end
	});
}
//更新var的時間
function updateVarversion(){
			//$("#var_version").text("VAR MAKE日期:"+_varVersion);
}
function updateSysVar() {
	_.extend(_sysvar, _changeSysVar);
	_sysvar['TXCODE'] = _txcd;
	_sysvar['FKEY'] = _fkey;
	console.log("TXCODE$ change to "+_sysvar['TXCODE']);
}
function startRun() {
	// var host = new IfxHost(null);
	updateSysVar();
	updateVarversion();
	console.log("makeIfx??");
	var ifx = makeIfx();
	//柯  新增
	if(!ifx){
		return;
	}
	ifx.setup();

	if(ifx.isSwiftTran()){
		yepnope({
			load: swiftResources
			,complete: function() {
				//swiftLayout();
				setTimeout(	stepDisplay,0);
			}
		});
	}else {

		setTimeout(	stepDisplay,0);
	}

	function stepDisplay() {
		ifx.display(true);

		if (_ovrScrFile) { // screen copy mode
			ifx.screenCopy(_ovrScrFile);
			showScreen();
			return;
		}

		ifx.postDisplay();
		ifx.addKeyFilter();
		// initSideMenu(ifx);


		if (_chain == '9') {
			console.log("viewJournal resend? "+_resend);
			ifx.viewJournal(_key,_resend);
			showScreen();
			return;
		}
		setTimeout(	stepCollect,0);

	}

	function stepCollect(){
		try {
			showScreen();
			if (_mode != "") {
				ifx.collect(false);
				ifx.KeysECPostProcess(_mode, _key, _rim, parseInt(_fkey, 10));
			}else {
				ifx.collect();
				/*
				if (_tmpfileExists) {
					ifx.resumeTran(_tmpfile);	 //collect移到 resumeTran內實作
				}	else {
					ifx.collect();
				}
				*/
			}
			ifx.watermark({
			});
			//showScreen();
		}catch(ee) {
			// IfxError message
			//alert(ee.message || ee.toString());
			printException(ee);
		}
	}

	function printException(ex) {
		console.error(ex);
		var t = printStackTrace({
			e : ex
		});
		console.error(t);
		alert("caught at ifx-start:" + t);
		throw ex;
	}

	function showScreen(){
		$('#main-container').show();
	}
}

function viewJournal(ifx) {
	$(".field_input").attr("disabled", "true");
	$("[id^='btn_']").hide(); // submit

	// reset ntxcd
	ifx.setValue('NTXCD$', '0');
	var jnlId = _key;

	var bean = 'url:/mvc/hnd/jnl/tita/'+jnlId;

	ifx.sendBean(bean, {}, "reading jouranl.....", function(data) {
		if(!data.retval){
			alert(data.errmsg);
			return;
		}
		// var o = eval('('+ data.detail.fields + ')');

		/*
		 * begin old var o = eval('(' + data.fields + ')'); var flds = o.fields;
		 * $(".field_input, .field_output").each(function(i, x) { var name =
		 * IfxUtl.id2name(x.id); // ifx.setValue(name, flds[name] || '');
		 * ifx.getField(name).deserialize(flds[name]); }); _.each(o.etc.hide,
		 * function(p) { $("#p_" + p).hide(); }); end old
		 */

		ifx.deserialize(data.fields, true);

		// var btnPrint = $('xbtn_cancel');
		$("#xbtn_cancel").clone(true).attr("id", "xbtn_print").attr("name",
				"xbtn_print").attr("value", "重印單據").click(
				function() {
					sidemenu.handlers["dupprint"].searchAndPrint(data.txtno,
							data.date.replace(/\//g, ''));
				}).appendTo($("#xbtn_cancel").parent());

	});
}
function startTest1() {
	var ifx = makeIfx();

	function testScreen() {
		var buf = ifxUI.renderScreen(ifx.def, function(tabIndex, x) {
			var fld = ifx.getField(x);
			return fld.display(tabIndex);
		});
		console.log(buf);

		$("#entryArea").append(buf);

		setTimeout(function() {
			var rtn = new IfxRtn(ifx);
			// rtn.HIDE('n/a','#TC1','#TC2','#TC3');
		}, 3000);

	}
	function testPrint() {

		var buf = ifxUI.printScreen(ifx.def, function(tabIndex, x, bBuddy) {
			var fld = ifx.getField(x);
			if (fld.name == "#D1") {
				return "20120919";
			} else if (fld.name == "#D2") {
				return "01000103";
			}
			return fld.name.slice(1).substr(0, fld.len)
					+ (bBuddy === true ? "" : " ");
		});
		console.log(buf);
		alert(buf);
		$("#outputArea").append("<pre>" + buf + "</pre>");
		$("#outputArea").show();

	}

	testScreen();
	testPrint();

}
function underscore_mixin() {
	_.mixin(_.str.exports());

}

// $(function() {
// starting_point();
// });
function starting_point() {
	underscore_mixin();
	// initDashboard();
	// initLeftBar('#dashboard2');

	try {
		startRun();
	} catch (ee) {
		alert('fatal:' + ee);
		//printException(ee);
		// $('<div id="err1"
		// style="color:red">Error"</div>').insertBefore('#main-container');
	}
	// startTest1();

	// unloadScrollBars();
	function reloadScrollBars() {
		document.documentElement.style.overflow = 'auto'; // firefox, chrome
		document.body.scroll = "yes"; // ie only
	}

	function unloadScrollBars() {
		document.documentElement.style.overflow = 'hidden'; // firefox, chrome
		document.body.scroll = "no"; // ie only
	}

}
function initDashboard() {

	$('#dashboard').load('head.jsp');
	$('#dashboard').hover(function() {
		$(this).stop().animate({
			top : '0' + $(window).scrollTop(),
		// backgroundColor: 'rgb(255,255,255)'
		}, 500, 'easeInSine'); // end animate
	}, function() {
		$(this).stop().animate({
			top : -50 + $(window).scrollTop(),
		// backgroundColor: 'rgb(110,138,195)'
		}, 1500, 'easeOutBounce'); // end animate
	}); // end hover
}
function initLeftBar(divLeft) {
	var codes = [ 'I1000', 'G0230' ], btnClass = 'speedKey';
	$.each(codes,
			function(i, x) {
				$(divLeft).append(
						'<div class="gray ' + btnClass + '">' + x + "</div>");
			});
	$("." + btnClass).bind('click', function() {
		window.location = "tran2.jsp?txcode=" + $(this).text();
	});
	$(divLeft).hover(function() {
		$(this).stop().animate({
			left : '0',
			backgroundColor : 'rgb(0,0,0)',
		}, 500, 'easeInSine'); // end animate
	}, function() {
		$(this).stop().animate({
			left : '-100px',
			backgroundColor : 'rgb(110,138,195)'
		}, 1500, 'easeOutBounce'); // end animate
	}); // end hover

	$('#btnLayout').on('change', function(x) {
		alert($(this).val());
	});
}

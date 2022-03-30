var MyMenu = MyMenu || {};
MyMenu.tranUrl = 'tran2.jsp';

MyMenu.getUrl = function (txcode) {
	return MyMenu.tranUrl + "?txcode=" + txcode;
};

MyMenu.getTabFn = function (name) {
	return top.frames['easytab'].contentWindow[name];
};

MyMenu.init = function () {
	MyMenu.useAjax();
};

MyMenu.useAjax = function () {
	// $.ajaxSetup({ cache: true });

	var d = new Date(),
		k = "" + d.getFullYear() + (d.getMonth() + 1) + d.getDate();

	var getMenuHtml = $.get(_contextPath + '/mvc/hnd/menu/html?key=' + k);
	getMenuHtml.done(getMenuHtmlDone, getTranCodeMap);
	getMenuHtml.fail(function (x) {
		alert('無法建立選單頁面:' + x + '\n請稍後再試!');
	});

	function queueTask(fn) {
		setTimeout(function () {
			fn();
		}, 0);
	}

	function getMenuHtmlDone(data) {
		MyMenu.create(data);
		queueTask(MyMenu.initTxCode);
		//queueTask(MyMenu.initSideBar);
		queueTask(MyMenu.adjust);

	}

	function getTranCodeMap() {
		var errHandler = function (x) {
			alert('無法建立交易清單:' + x + '\n請稍後再試!');
		};

		$.get(_contextPath + '/mvc/hnd/menu/json?key=' + k).then(
			MyMenu.autoComplete, errHandler);
	}

	/*
	 * var head = document.getElementsByTagName('head')[0]; var script =
	 * document.createElement('script'); script.onload = function() {
	 * console.log('loaded'); }; script.src =
	 * _contextPath+'/script/not-easy.js'; head.appendChild(script);
	 */
};

MyMenu.create = function (data) {
	var bg = "rgba(234, 251, 211, 1)";
	$('#tabs').html(data);
	$("#tabs").tabs({
		width: $("#tabs").parent().width(),
		height: "auto"
	});
	$("[id^=tab]").css("background-color", bg);
	$(".menuTable td a").click(function () {
		var txcode = $(this).text().slice(0, 5);
		var url = MyMenu.getUrl(txcode);
		// window.location = url;
		parent.addTran($(this).text().slice(0, 5), url);

	}).hover(
		function () {
			$(this).css("font-weight", "bold").css("background-color",
					"#FFCC66").css("border-radius", ".5em").fadeOut(150)
				.fadeIn(400);

		},
		function () {
			// $(this).removeClass("myclass");
			$(this).css("font-weight", "normal")
				.css("background-color", bg).stop(false, true);
		});
	// .filter(":even").css("color","black");

	$('#menulink >a').hover(
		function () {
			$(this).css("font-weight", "bold").css("border",
				"2px	 dotted lightgreen").css("border-radius", ".5em");
		},
		function () {
			$(this).css("font-weight", "normal").css("border", "none");
		});

};

MyMenu.initTxCode = function () {
	var jqTxcode = $('#txcode'),
		fn = MyMenu.getTabFn('registerTran');


	fn('MENU', 'refresh', focusIt);
	fn('MENU', 'resize', MyMenu.adjust);
	setTimeout(function () {
		jqTxcode.focus();

	}, 600);


	function focusIt() {
		setTimeout(function () {
			jqTxcode.focus();
			MyMenu.adjust();
		}, 100);

	}


	jqTxcode.change(function () {
		var s = $(this).val().toUpperCase();
		if (s.length == 5) {
			var u = MyMenu.getUrl(s);
			parent.addTran(s, u);
			$(this).val('');
		}

	}).keypress(function () {
		var s = $(this).val();
		if (s.length == 5)
			$(this).trigger("change");
	}).click(function () {
		// window.location = $('#aa').attr('href');

		window.location = "#home";
		jqTxcode.focus();
		// window.scrollTo(0,50);
	});


};

MyMenu.initSideBar = function () {
	// 鍾 功能面板用原本的樣式及展開的方式,且所有功能在任何交易都可執行
	sidemenu.init('dashboard2', {
		top: 42
	}, function () {
		// end


		sidemenu.enableAll(false);
		sidemenu.enable("toggle_help_list");
		sidemenu.enable("ej");
		sidemenu.enable("dupprint");
		sidemenu.enable('systemInfo');
		sidemenu.enable("logoff");
		// begin hot tran
		// 鍾 且所有功能在任何交易都可執行
		sidemenu.enable("approval");
		// end
		sidemenu.enable("ec");

		// end hot tran
		sidemenu.enable("msw");
	});
};

MyMenu.autoComplete = function (_tranMap) {
	var txcodeSource = [],
		jqTxcode = $("#txcode");

	console.log("build txcodeSource");
	for (var k in _tranMap) {
		var o = _tranMap[k];
		txcodeSource.push({
			label: k + ' ' + o['txnm'],
			value: k
		});
	}
	txcodeSource.sort(function (a, b) {
		if (a.value < b.value) return -1;
		if (a.value > b.value) return 1;
		return 0;

	});

	jqTxcode.autocomplete({
		minLength: 1,
		source: function (request, response) {
			var matches = $.map(txcodeSource, function (tag) {
				if (tag.label.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
					return tag;
				}
			});
			response(matches);
		},
		focus: function (event, ui) {
			// $( "#txcode" ).val( ui.item.value );
			// return false;
			// $(this).autocomplete ("search", "");
		},
		select: function (event, ui) {
			jqTxcode.val(ui.item.value);
			// $( "#project-id" ).val( ui.item.value );
			return false;
		}
	});
};

MyMenu.adjust = function () {

	console.log("menu adjust doc height1:" + $(document.body).height());

	var theFrame = $('#menuFrame', parent.document.body);
	var theCenter = $('#center', parent.document.body);
	console.log('center height:' + theCenter.height());
	//theFrame.height($(document.body).height());
	console.log("menu iframe height1:" + theFrame.height());
	theFrame.height(theCenter.height() - 30);
	console.log("menu iframe height2:" + theFrame.height());

};

$(function () {
	// startingPoint();
	MyMenu.init();
	$(window).resize(function () {
		MyMenu.adjust();
	});


});
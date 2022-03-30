/*
 **   ___ _______  __
 **  |_ _|  ___\ \/ /
 **   | || |_   \  /   SystemOne 2014
 **   | ||  _|  /  \ 
 **  |___|_|   /_/\_\
 ** 
 */
//欄位上按F6是回調上次交易[成功]之欄位內容
var sidemenu = {
	handlers : {},
	content : [ {
		text : "隱藏隱藏",
		cmd : "toggle_help_list",
		enabled : true,
		key : "F2"
	}, {
		text : "修    正",
		cmd : "modify",
		enabled : true,
		key : "F33"
	}, {
		text : "訂    正",
		cmd : "ec",
		enabled : true,
		key : "F3"
	}, {
	  text : "登錄提交",
		cmd : "SendOut",
		enabled : true,
		key : ""
	}, {
		text : "交易放行",
		cmd : "approval",
		enabled : true,
		key : "F4"
	}, {
		text : "電文放行",
		cmd : "swift_approval",
		enabled : true,
		key : "F4"
	}, {
		text : "審核登錄",
		cmd : "review",
		enabled : true,
		key : "F4"
	}, {
		text : "在途查詢",
		cmd : "delay",
		enabled : true,
		key : "F4"
	}, 
//	{
//		text : "訂正重登",
//		cmd : "ecreload",
//		enabled : true,
//		key : "F4"
//	}, 
	{
		text : "交易明細",
		cmd : "personal",
		enabled : true,
		key : "F66"
	}, 
	{
		text : "表單補印",
		cmd : "xx9jl",
		enabled : false,
		key : "F66"
	}, {
		text : "水單補印",
		cmd : "xx9tl",
		enabled : true,
		key : "F66"
	}, {
		text : "自動化交易",
		cmd : "eBank",
		enabled : true,
		key : "F66"
	}, {
		text : "重印單據",
		cmd : "dupprint",
		enabled : false,
		key : "F6"
	}, {
		text : "報表及製檔",
		cmd : "batchreport",
		enabled : true,
		key : "F6"
	}, {
		text : '暫存交易',
		cmd : 'savetran',
		enabled : false,
		key : 'F7'
	}, {
		text : '全部暫存',
		cmd : 'savealltran',
		enabled : false,
		key : 'F7'
	}, {
		text : "MSW",
		cmd : "msw",
		enabled : false, // 有測試成功
		fkey : "F16"
	}, {
		text : "系統資訊",
		cmd : "systemInfo",
		enabled : false,
		fkey : "F35"
	}, {
		text : "系統設定",
		cmd : "user_setting",
		enabled : true,
		fkey : "F35"
	}, {
		text : "周邊重啟",
		cmd : "divice_restart",
		enabled : true,
		fkey : "F35"
	}, {
		text : "應處理清單",
		cmd : "toDoList",
		enabled : true,
		key : ""
	},{
		text : "解除鎖定",
		cmd : "lockOff",
		enabled : true,
		key : ""
	},{
		text : "登    出",
		cmd : "logoff",
		enabled : true,
		fkey : "F12"
	},{
		text : "IP設定",
		cmd : "file_upload",
		enabled : true,
		key : "F66"
	} ],
	ifx : null,
	btnClass : ' button speedKey ',
	$container : 'n/a',
	top : 30,
	orientation : 'p',
	startWidth : 0,
	growTo : 0,
	slice : true,
	configure : function(cfg) {
		if (cfg == null)
			return;
		if (cfg.top != undefined)
			this.top = cfg.top;
		if (cfg.growTo != undefined)
			this.growTo = cfg.growTo;
		if (cfg.orientation != undefined)
			this.orientation = cfg.orientation;
		if (cfg.startWidth != undefined)
			this.startWidth = cfg.startWidth;
		cfg.slide = false;
	},
	init : function(placeHolder, cfg, fn) {
		var self = this;
		this.configure(cfg);
		if (fn)
			fn(this);
		this.$container = $("#" + placeHolder);

		if (this.slide) {
			this.$container.append('<span class="caption caption1">功</span>');
			this.$container.append('<span class="caption caption2">能</span>');
		}
		$.each(this.content, function(i, x) {
			if (x.enabled) {
				if (x.seperator) {
					if (self.orientation == 'h') {
						self.$container.append(x.seperator.replace(/<br\/>/g, '&nbsp;'));
					} else {
						self.$container.append(x.seperator);
					}
				}
				self.$container.append('<a id="speed_cmd_' + x.cmd + '" class="button ' + self.btnClass + '" data-cmd="' + x.cmd + '">' + x.text + "</a>" + (self.orientation == 'p' ? "<br/>" : ""));
			}
		});
		this.$container.show();
		// $jq.css({
		// top: $(window).scrollTop() + top,
		// });

		if (this.slide) {
			if (this.orientation == 'p') {
				this.slide_p();
			} else {
				this.slide_h(this.$container.html());
			}
			window.scrollBy(1, 1);
			window.scrollBy(-1, -1);

		} else {
			$.each(self.content, function(i, x) {
				if (self.handlers[x.cmd].preShow)
					self.handlers[x.cmd].preShow();
			});
		}

		$('.speedKey').on("click", function(evt) {
			var cmd = $(this).attr("data-cmd");
			if (cmd) {
				if (self.handlers[cmd] == null) {
					alert(cmd + " undefined handler");
					return;
				}
				self.handlers[cmd].run(self);
			}

		});
	},
	registerIfx : function(aIfx) {
		ifx = aIfx;
	},
	getIfx : function() {
		return ifx;
	},
	getTabFn : function(name) {
		return parent.frames['easytab'][name];
	},
	registerHandler : function(cmd, context, name) {
		var fn = function() {
			return context[name].apply(context, arguments);
		};
		this.handlers[cmd] = {
			run : fn
		};
	},
	enable : function(cmd, enabled) {
		if (enabled === undefined) {
			enabled = true;
		} else {
			enabled = false;
		}
		$.each(this.content, function(i, x) {
			if (x.cmd === cmd) {
				x.enabled = enabled;
				return false;
			}
		});
	},
	enableAll : function(enabled) {
		if (enabled === undefined)
			enabled = true;
		$.each(this.content, function(i, x) {
			x.enabled = enabled;
		});
	},
	slide_p : function() {
		// 鍾 功能面板用原本的樣式及展開的方式
		var self = this, origLeft = this.$container.position().left;

		this.$container.hover(function() {
			$.each(self.content, function(i, x) {
				if (self.handlers[x.cmd].preShow)
					self.handlers[x.cmd].preShow();
			});

			$(this).stop().animate({
				left : '0',
				height : 450
			}, 300, 'swing'); // end animate
			// Jquery UI animate
		}, function() {
			$(this).stop().animate({
				left : -134, // '-140px',
				height : 150
			}, 500, 'swing'); // end animate
		}); // end hover

		$(window).scroll(function() {
			var y = $(window).scrollTop() + self.top;
			// console.log("side:" + y);
			self.$container.css("top", y);
		});
		// end
	},
	slide_h : function(content) {
		var self = this, origTop = this.$container.position().top;
		width = this.$container.width();
		function smaller() {
			self.$container.css({
				width : self.startWidth
			});
			self.$container.html('');
			self.$container.append('<div style="position: absolute; bottom: 0;left: 2px;padding:4px; color:white;font-size:15px; opacity:1">功能表</div>');

		}
		function larger() {
			self.$container.stop().animate({
				width : width
			}, 500, 'easeInSine'); // end animate
			self.$container.css({
				width : width
			});
			self.$container.html(content);
		}

		smaller();

		this.$container.hover(function() {
			larger();
			// 鍾 功能面板用原本的樣式及展開的方式
			/*
			 * $(this).stop().animate({ top: '0' + $(window).scrollTop(), opacity:1, backgroundColor :
			 * 'rgb(0,0,0)', }, 200, 'easeInSine'); // end animate
			 */
			// end
		}, function() {
			// 鍾 功能面板用原本的樣式及展開的方式
			/*
			 * $(this).stop().animate({ top: origTop + $(window).scrollTop(), backgroundColor :
			 * 'rgb(110,138,195)', opacity:0.6 }, 1500, 'fast'); // end animate
			 */
			// end
			smaller();
		}); // end hover

		$(window).scroll(function() {
			self.$container.css("top", $(window).scrollTop() + origTop);
		});
	}

};

sidemenu.handlers["lockOff"] = (function() {
	function run() {
		var txcode = 'LC010';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('交易明細', txcode, u);
		addHotTran('解除鎖定', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["logoff"] = (function() {
	function run(bNoConfirm) { // bNoConfirm
		// if (bNoConfirm == true || window.confirm("確定要登出?")) {
		var txcode = 'LC101';
		var logofftitle = "正常登出";
		var u = "tran2.jsp?txcode=" + txcode;
		// 逾時時從easytab.js踢出
		if (bNoConfirm == true) {
//			alert("系統逾時，請重新登入...");
			logofftitle = "強制登出";
		}
		addHotTran(logofftitle, txcode, u);

		// window.top.onbeforeunload = null; // see easy_main.jsp, disable window.onbeforeunload
		// window.top.location = "logoff.jsp";

		// return;
		// }
	}
	return {
		run : run
	};
})();
// 多卡一層
sidemenu.handlers["batchreport"] = (function() {
	function run() {
		var txcode = 'LC009';
		var u = "tran2.jsp?txcode=" + txcode;
		addHotTran('報表及製檔', txcode, u);
	}

	return {
		run : run
	};
})();
// sidemenu.handlers["batchreport"] = (function() {
// function run() {
// openDupWindow();
// }
//
// function openDupWindow() {
// var width = 974;
// var height = 718;
// var url = "batch_report.jsp";
// url += "?startline=0&count=500";
// window
// .showModalDialog(
// url,
// window,
// 'dialogWidth='
// + width
// + 'px;dialogHeight='
// + height
// + 'px;resizable=yes;help=no;center=yes;status=no;scroll=yes;edge=sunken');
// }
//
// return {
// run : run,
// searchAndPrint : openDupWindow
// };
// })();
sidemenu.handlers["dupprint"] = (function() {
	function run() {
		openDupWindow(null, null);
	}

	function openDupWindow(id, dt) {
		var width = 900;
		var height = 650;
		var url = "dupdoc.jsp";
		if (id != null)
			url += "?id=" + id + "&dt=" + dt;
		window.showModalDialog(url, window, 'dialogWidth=' + width + 'px;dialogHeight=' + height + 'px;resizable=yes;help=no;center=yes;status=no;scroll=yes;edge=sunken');
	}

	return {
		run : run,
		searchAndPrint : openDupWindow
	};
})();

sidemenu.handlers["uiswitch"] = (function() {
	function run() {
		var u = top.window.location.toString();
		if (u.indexOf('easy_') > 0)
			u = u.replace('easy_', '');
		else
			u = u.replace('main', 'easy_main');
		top.window.location = encodeURI(u);
	}

	return {
		run : run
	};
})();
// 有誤
sidemenu.handlers["savetran"] = (function() {
	function run() {
		var ifx = sidemenu.getIfx();
		ifx.trySave(true);
	}
	return {
		run : run
	};
})();

sidemenu.handlers["savealltran"] = (function() {
	function run() {
		alert('save all');
		var fn = sidemenu.getTabFn('getTabMap');
		var map = fn();
		var arr = [];
		for ( var k in map) {
			if (k.toLowerCase() != "menu")
				arr.push(k);
		}

	}
	return {
		run : run
	};
})();
// begin hot tran
sidemenu.handlers["modify"] = (function() {
	function run() {
		var txcode = 'LC002';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('訂正', txcode, u);
		addHotTran('修正', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["ec"] = (function() {
	function run() {
		var txcode = 'LC001';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('訂正', txcode, u);
		addHotTran('訂正', txcode, u);
	}

	return {
		run : run
	};
})();

// begin hot tran
sidemenu.handlers["approval"] = (function() {
	function run() {
		var txcode = 'LC003';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('放行', txcode, u);
		addHotTran('放行', txcode, u);
	}

	return {
		run : run
	};
})();
sidemenu.handlers["swift_approval"] = (function() {
	function run() {
		var txcode = 'XW900';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('放行', txcode, u);
		addHotTran('電文放行', txcode, u);
	}

	return {
		run : run
	};
})();
sidemenu.handlers["review"] = (function() {
	function run() {
		var txcode = 'LC004';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('放行', txcode, u);
		addHotTran('審核登錄', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["delay"] = (function() {
	function run() {
		var txcode = 'XX904';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('放行', txcode, u);
		addHotTran('在途查詢', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["ecreload"] = (function() {
	function run() {
		var txcode = 'XX905';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('放行', txcode, u);
		addHotTran('訂正重登', txcode, u);
	}

	return {
		run : run
	};
})();

// end hot tran

sidemenu.handlers["personal"] = (function() {
	function run() {
		var txcode = 'LC011';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('交易明細', txcode, u);
		addHotTran('交易明細', txcode, u);
	}

	return {
		run : run
	};
})();
sidemenu.handlers["xx9jl"] = (function() {
	function run() {
		var txcode = 'LC012';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('交易明細', txcode, u);
		addHotTran('表單補印', txcode, u);
	}

	return {
		run : run
	};
})();
sidemenu.handlers["xx9tl"] = (function() {
	function run() {
		var txcode = 'XX9TL';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('交易明細', txcode, u);
		addHotTran('水單補印', txcode, u);
	}

	return {
		run : run
	};
})();
sidemenu.handlers["file_upload"] = (function() {
	
	function run() {
		var width = 900, height = 650, url = "fileUpload/uploadMenu.jsp";

		if(!window.showModalDialog){
			window.showModalDialog=function(url,name,option){
				if(window.hasOpenWindow){
					window.newWindow.focus();
				}
				var re = new RegExp(";", "g");  
				var option  = option.replace(re, '","'); // 把option转为json字符串
				var re2 = new RegExp(":", "g");
				option = '{"'+option.replace(re2, '":"')+'"}';
				option = JSON.parse(option);
				var openOption = 'width='+parseInt(option.dialogWidth)+',height='+parseInt(option.dialogHeight)+',left='+(window.screen.width-parseInt(option.dialogWidth))/2+',top='+(window.screen.height-30-parseInt(option.dialogHeight))/2;
				window.hasOpenWindow = true;
				window.newWindow = window.open(url,name,openOption);
			}
		}
		
		var tranfg = window.showModalDialog(url, window, 'dialogWidth:' + width + 'px;dialogHeight:' + height
				+ 'px;resizable:yes;help:no;center:yes;status:no;scroll:yes;edge:sunken;toolbar:no;location:no;menu:no');

		var txcode = 'XW300';
		var u = "tran2.jsp?txcode=" + txcode;
//		if (tranfg)
//			addHotTran('電文變更', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["toDoList"] = (function() {
	function run() {
		var txcode = 'L6001';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('交易明細', txcode, u);
		addHotTran('應處理清單', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["SendOut"] = (function() {
	function run() {
		var txcode = 'LC005';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('交易明細', txcode, u);
		addHotTran('登錄提交', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["eBank"] = (function() {
	function run() {
		var txcode = 'XX9EB';
		var u = "tran2.jsp?txcode=" + txcode;
		// parent.addHotTran('交易明細', txcode, u);
		addHotTran('自動化交易', txcode, u);
	}

	return {
		run : run
	};
})();

sidemenu.handlers["msw"] = (function() {
	function run() {
		// parent.openMswDlg();
		openMswDlg();
	}

	return {
		run : run
	};
})();

sidemenu.handlers["systemInfo"] = (function() {
	function run() {
		var width = 900, height = 650, url = "systeminfo.jsp";

		window.showModalDialog(url, window, 'dialogWidth=' + width + 'px;dialogHeight=' + height + 'px;resizable=yes;help=no;center=yes;status=no;scroll=yes;edge=sunken');
	}

	return {
		run : run
	};
})();
sidemenu.handlers["user_setting"] = (function() {
	function run() {
		var width = 900, height = 650, url = "user/user_m1.jsp";

		window.showModalDialog(url, window, 'dialogWidth=' + width + 'px;dialogHeight=' + height
				+ 'px;resizable=yes;help=no;center=yes;status=no;scroll=yes;edge=sunken;toolbar=no;location=no;menu=no');
	}

	return {
		run : run
	};
})();

sidemenu.handlers["divice_restart"] = (function() {
	function run() {
		var result = Device.pb.restartdevice();
		console.log("restartDevice!!--->" + result);
		alertify.set('notifier', 'position', 'top-left');
		alertify.set('notifier', 'delay', 10);
		alertify.error("周邊重啟已開始..請稍後.....");
	}
	return {
		run : run
	};
})();

sidemenu.handlers["toggle_help_list"] = (function() {
	var global = searchUp('__ifx_global');
	function run() {
		global.cfg['helpList'].toggle();
		updateTitle();
	}

	function searchUp(name) {
		var w = window;

		while (true) {
			if (w[name] != null) {
				return w[name];
			}
			if (w == window.top)
				throw 'no such data:' + name;
			w = w.parent;
		}
	}
	function updateTitle() {
		var $item = $('#speed_cmd_toggle_help_list');
		$item.text(global.cfg['helpList'].title());
		$item.attr('title', '按我' + global.cfg['helpList'].title());
	}
	function preShow() {
		updateTitle();
	}
	return {
		run : run,
		preShow : preShow
	};
})();
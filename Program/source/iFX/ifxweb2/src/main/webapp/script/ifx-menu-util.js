var IfxMenu = (function($) {
	var $divMenu;
	var _m = {};
	var _url;
	var _loaded = {};

	$(document).keydown(function(evt){
	    console.log(evt);
	    if(evt.key == "F4"){
	      parent.addTran("LC101", MyMenu.getUrl("LC101"));
	    }
	});

	function setup(divName, url) {
		$divMenu = $(divName);
		_url = url;
	}

	function expandData(arr) {
		console.log("expandData r=> " + arr.length);
		var keys = (arr.shift()).split(","), ss;
		var r = $.map(arr, function(x) {
			ss = x.split(",");
			// 在 underscore.js
			return _.object(keys, ss);
		});

		$.each(r, function(i, x) {
			// console.log("each r=> " + x);
			_m[x.txcd] = x;
		});

		return r;
	}

	function makeSubMenu(arr) {
		var items = expandData(arr);

		$.each(items, function(i, x) {
			// console.log("X=> " + x+"X.type=> "+ x.type );
			if (x.type == 2) {
				// console.log("XX==> " + x);
				_m[x.txcd] = x;
			}
		});

		var subs = $.grep(items, function(x) {
			return (x.type == 0 && $.trim(x.txcd).length > 2);
		});

		$.each(subs, function(i, x) {
			x.txcd = $.trim(x.txcd);
			console.log("sub " + i + "=" + x.txcd + ", " + x.txnm);

			x.items = [];
			$.each(items, function(i, y) {
				if (y.type == 2 && x.txcd == $.trim(y.sbtype)) {
					x.items.push(y);
					// console.log("X===> " + x.txcd + "," + $.trim(y.sbtype));
				}
			});
			// $.each(x.items, function (i, y) {
			// console.log("====> " + y.txcd + "," + y.txnm);
			// });
		});

		return drawSubMenu(subs);
	}

	function drawSubMenu(subs) {
		var lines = [];
		var t;

		lines.push("<span id='menulink'>");
		$.each(subs, function(i, x) {
			t = "<a href='#mi_" + x.txcd + "'>" + (i + 1) + " " + x.txnm + "</a>";
			// console.log(t);
			lines.push(t);
		});
		lines.push("</span><br/>")

		// items
		// 柯 新增直式menu表單
		$.each(subs, function(i, x) {
			t = "<a class='menuanchor'  name='mi_" + x.txcd + "'>" + (i + 1) + " " + x.txnm + "</a><br/>";
			lines.push(t);
			lines.push("<table class='menuTable'>");
			var longg = Math.ceil(x.items.length / 2);
			var rows = [];
			var i = 0;
			for (var j = 0; j < x.items.length; j += 1) {
				if ((j + 2) % 2 == 0) {
					if (!rows[i]) {
						rows[i] = "";
						rows[i] += "<tr>";
					}
					rows[i] += drawItem(x.items[j]);
					if (rows[i].split("<td>").length - 1 == 2) {
						rows[i] += "</tr>";
					}
				}
				if ((j + 2) % 2 == 1) {
					if (!rows[i]) {
						rows[i] = "";
						rows[i] += "<tr>";
					}
					rows[i] += drawItem(x.items[j]);
					if (rows[i].split("<td>").length - 1 == 2) {
						rows[i] += "</tr>";
					}
				}
				i++;
				if (i == longg) {
					i = 0;
				}
			}
			for (var c = 0; c <= longg; c++) {
				lines.push(rows[c]);
			}

			lines.push("</table><br/>");
		});
		// 橫式 取消
		/*
		 * $.each(subs, function (i, x) { t = "<a class='menuanchor' name='mi_" + x.txcd + "'>" +
		 * (i+1)+" " + x.txnm + "</a><br/>"; lines.push(t); lines.push("<table
		 * class='menuTable'>"); for (var j = 0; j < x.items.length; j += 2) { lines.push("<tr>");
		 * lines.push(drawItem(x.items[j])); if (j + 1 < x.items.length) {
		 * lines.push(drawItem(x.items[j + 1])); } lines.push("</tr>"); } lines.push("</table><br/>");
		 * });
		 */
		return lines.join("");

		function drawItem(item) {
			var t;
			var templates = [ "<span class='itemDisabled'>%s</span>", "<a href=\"#\">%s</a>" ];
			var templ = templates[item.enabled];
			var text = item.txcd + " " + item.txnm;
			t = sprintf(templ, text);
			t = "<td>" + t + "</td>";
			// console.log(t);
			return t;
		}
	}
	function sprintf(format, etc) {
		var arg = arguments;
		var i = 1;
		return format.replace(/%((%)|s)/g, function(m) {
			return m[2] || arg[i++]
		})
	}

	function buildRoot(rootDef) {
		var root = expandData(rootDef);
		// 選單改依交易頻率排序
		var sortroot = {
			"L1" : 0,
			"L2" : 1,
			"L3" : 2,
			"L4" : 3,
			"L5" : 4,
			"L6" : 5,
			"L7" : 6,
			"L8" : 7,
			"L9" : 8
		};
		/*
		 * 原 英文字母排序 root = _.sortBy(root, function (x) { return x.txcd; });
		 */
		root = root.sort(function(a, b) {
			return sortroot[a.sbtype] - sortroot[b.sbtype]
		});
		//createMainTab(root); 	//潘

		var temp ={};
		$.each(root, function(i, x) {
			temp[$.trim(x.txcd)] = $.trim(x.txcd) + "." + $.trim(x.txnm);
		});

		return temp;
	}

	function createMainTab(items) {
		var arr = [], t, tabscount = 0;
//		arr.push("<ul class=\"drop-down-menu\">");

		// make first page 原始頁簽移除 所以不用
		// t = "<li><a href='firstPage.jsp'>訊息</a></li>";
		// arr.push(t);

		// make 自定義MENU 潘 自訂選單移除
		// t = "<li><a href='#tab_DIY'>自訂選單</a></li>";
		// arr.push(t);

    var tanin = 0;
    var dropLeft  = "dropdown-menu-left";
    var dropRight = "dropdown-menu-right";
		$.each(items, function(i, x) {
			console.log("createMainTab:" + x.txcd + " = " + x.txnm);
			t = '<li class="nav-item dropdown"><a class="nav-link dropdown-toggle" tabindex="0" data-toggle="dropdown" data-submenu="">' + $.trim(x.txcd) + "." + $.trim(x.txnm) + '</a><div id="menu' + $.trim(x.txcd) + '" class="dropdown-menu ' + (tanin == 0 || tanin == 1 ? dropLeft : dropRight) + '"></div></li>';
//			t = "<li><a href='#'>" + $.trim(x.txcd) + "." + $.trim(x.txnm) + "</a><ul id='menu" + $.trim(x.txcd) + "'></ul></li>";
			arr.push(t);
			tanin++;
		});

		function getTabId(txType) {
			var s = "tab_" + $.trim(txType);
			return s;
		}

		tabscount = arr.length - 1; // 除去第一個ul

		arr.push("</ul>");

		$divMenu.html(arr.join(""));
	}

	function getifxMenu() {

		Device.pb.setup('ifxws', 'myDevice');
		Device.pb.init();
		return ifxFile.getformM(Device.pb);
	}
	function addStyle(tabId) {
		var $tab = $(tabId);
		var bg = "rgba(255, 255, 255, 1)";

		$tab.css("background-color", bg);
		$(".menuTable td a", $tab).click(function() {
			var txcode = $(this).text().slice(0, 5);
			var url = MyMenu.getUrl(txcode);
			// window.location = url;
			parent.addTran($(this).text().slice(0, 5), url);
		}).hover(function() {
			$(this).css("font-weight", "bold").css("background-color", "#FFCC66").css("border-radius", ".5em").fadeOut(50).fadeIn(50);

		}, function() {
			// $(this).removeClass("myclass");
			$(this).css("font-weight", "normal").css("background-color", bg).stop(false, true);
		});

		$('#menulink >a', $tab).hover(function() {
			$(this).css("font-weight", "bold").css("border", "2px	 dotted lightgreen").css("border-radius", ".5em");
		}, function() {
			$(this).css("font-weight", "normal").css("border", "none");
		});

	}

	function selectTab(i) {
		if (!isNaN(i)) {
			$divMenu.tabs("option", "active", i);
		}
	}

	return {
		setup : setup,
		buildRoot : buildRoot,
		selectTab : selectTab,
		expandData : expandData
	};
}(jQuery));
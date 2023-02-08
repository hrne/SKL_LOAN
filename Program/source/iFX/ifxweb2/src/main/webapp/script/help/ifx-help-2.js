// HELP(0,0,0,0,MSG)
// HELP(HELPFILE,segment name,field name1,field name2嚙皺)
var IfxHelp = (function() {
	var divName = "";
	var $div = null;
	var $divTooltip = null;
	var leftEar = '';
	var rightEar = '';
	var ifx = null;
	var currHelpFieldId = null;
	var props;
	var HELP_JS_FMT = "2";

	function IfxHelp(aIfx, placeHolder, theTooltip) {
		ifx = aIfx;
		divName = placeHolder;
		$div = $(divName);
		$divTooltip = $(theTooltip);
		console.log("IfxHelp(), divName:" + divName);
	}

	function getDefaultProps() {
		return {
			'x': 0,
			'y': 0,
			'cols': 1,
			'ajax': false,
			'fmt': '{value} -- {label}'
		};
	}
	var _useLayout = false;
	var _autoComplete;
	IfxHelp.prototype.setLayout = function(useLayout) {
		_useLayout = useLayout;
	}
	// rename to displayHelp for search easily
	IfxHelp.prototype.displayHelp = function(argv, fieldId, behavior, fnCallback, check, settarget) {
		currHelpFieldId = fieldId;
		var fldname = argv[0],
			ss, helpfileName = null,
			segmentName = null,
			valueName, labelName, matrix;
		var bMatchBegin = true;
		var bMatchBegind;
		var isDrawRight = argv[argv.length - 1] == "R" ? true : false;
		_autoComplete = (behavior === 'autocomplete');
		props = getDefaultProps();
		// var provide data
		if (argv.length <= 4) {
			matrix = parseUserProvideData(argv[1]);
			parseProps(argv[2]);
			bMatchBegin = argv[3] ? (argv[3] == "*" ? false : true) : true;
		} else {
			helpfileName = argv[1];
			segmentName = argv[2];
			valueName = argv[3];
			labelName = argv[4];
			parseProps(argv[5]);
			bMatchBegin = argv[6] ? (argv[6] == "*" ? false : true) : true;
			bMatchBegind = (argv[6] && argv[6].indexOf(":") != -1) ? argv[6].split(":") : null;
			matrix = getHelpList(argv[7]); // args[7] user supplied code
		}
		if (matrix == "chktrue") {
			ifx.setValue(settarget, "0");
		} else if (matrix == "chkfalse") {
			ifx.setValue(settarget, "1");
		}
		if (_autoComplete) {
			function getLocalSource(request, response) {
				var list = [];
				var values = matrix[0];
				var labels = matrix[1];
				for (var i = 0; i < values.length; i++) {
					list.push({
						label: format(props.fmt, {
							'value': values[i],
							'label': labels[i]
						}) + '  ', // add some spaces
						value: values[i],
						origLabel: labels[i]
					});
				}
				// response(list);
				return list;
			}

			function getRemoteSource(request, response) {
				var u = _contextPath + "/mvc/hnd/helpList/" + helpfileName + "/" + segmentName + "/" + valueName + "/" + labelName;
				$.ajax({
					cache: true,
					url: u,
					success: function(data) {
						response($.map(data.result, function(item) {
							return {
								value: item.first,
								label: format(props.fmt + " -- from ajax", {
									'value': item.first,
									'label': item.second
								}),
								origLabel: item.second
							};
						}));
					},
					error: function(data) {
						alert("error getting ajax source:" + u);
					}
				});
			}
			var dataSourceGetter = (props.ajax == "true") ? getRemoteSource : getLocalSource;
			displayAutoComplete(fieldId, dataSourceGetter, bMatchBegin, props, fnCallback);
		} else {
			displayLookup(fldname, fieldId, matrix, props, fnCallback, isDrawRight);
		}
		// x:-10;y:30;w:200;h:400;cols:2
		function parseProps(s) {
			if (!s) return;
			_.each(s.split(';'), function(x) {
				var xx = x.split(':');
				props[$.trim(xx[0])] = xx[1];
			});
		}

		function getHelpList(moreCode) {
			var m = [];
			var helptemp, helpmother, result = [];
			console.log("props.ajax:" + props.ajax);
			if (props.ajax != "true") { // read from help.js
				try {
					for (var i = 3; i < 5; i++) {
						result = [];
						console.log("HELP_JS_FMT:" + HELP_JS_FMT);
						if (HELP_JS_FMT == "2") {
							console.log(helpfileName + ";" + segmentName + ";" + argv[i]);
							helpmother = Helpfile[helpfileName + "." + segmentName];
							helptemp = helpmother[argv[i]];
							if (helptemp.length > 0) {
								// 篩選內容
								if (bMatchBegind) {
									$.each(helpmother[bMatchBegind[0]], function(j, x) {
										if (x == bMatchBegind[1].trim()) {
											result.push(helpmother[argv[i]][j]);
										}
									});
									console.dir(result);
									helptemp = result; // 取代回來
								}
								m.push(helptemp);
							}
						} else {
							console.log(helpfileName + ";" + segmentName + ";" + argv[i]);
							helpmother = Helpfile[helpfileName][segmentName];
							helptemp = helpmother[argv[i]];
							if (helptemp.length > 0) {
								if (bMatchBegind) {
									$.each(helpmother[bMatchBegind[0]], function(j, x) {
										if (x == bMatchBegind[1]) {
											result.push(helpmother[argv[i]][j]);
										}
									});
									console.dir(result);
									helptemp = result; // 取代回來
								}
								m.push(helptemp);
							}
						}
					}
					console.log("help result length:" + m.length);
					if (check == "CHECK") {
						if (m.length > 0) { // 新增檢查長度
							return "chktrue";
						} else {
							return "chkfalse";
						}
					}
					if (m.length == 0) {
						throw "HELP()沒資料!!\n" + argv.join();
					}
				} catch (ee) {
					if (check == "CHECK") {
						return "chkfalse";
					} else {
						throw "HELP()有誤!!\n" + argv.join();
					}
				}
			}
			if (moreCode) {
				var moreCodeArray = parseUserProvideData(moreCode);
				m[0] = m[0].concat(moreCodeArray[0]);
				m[1] = m[1].concat(moreCodeArray[1]);
			}
			return m;
		}
	};

	function parseUserProvideData(s) {
		var m = [
			[],
			[]
		];
		// s = s.charAt(0) == "#" ? ifx.getValue(s) : s;
		$.each(s.split(';'), function(i, x) {
			ss = x.split(':');
			// 鍾,
			// m[0].push(ss[0]);
			// m[1].push(ss[1]);
			if (ss.length < 3) {
				m[0].push(ss[0]);
				m[1].push(ss[1]);
			} else {
				if (ss[2].trim() != "N") {
					m[0].push(ss[0]);
					m[1].push(ss[1]);
				}
			}
			//end
		});
		return m;
	}
	IfxHelp.prototype.getLookupTable = function(helpFile, segmentName, valueTag, labelTag, moreCode) {
		var valueArray = null,
			labelArray = null;
		try {
			if (HELP_JS_FMT == "2") {
				valueArray = Helpfile[helpFile + "." + segmentName][valueTag];
				labelArray = Helpfile[helpFile + "." + segmentName][labelTag];
			} else {
				valueArray = Helpfile[helpFile][segmentName][valueTag];
				labelArray = Helpfile[helpFile][segmentName][labelTag];
			}
			var result = {};
			$.each(valueArray, function(i, x) {
				result[x] = labelArray[i];
			});
			if (moreCode) {
				var more = parseUserProvideData(moreCode);
				$.each(more[0], function(i, x) {
					result[x] = more[1][i];
				});
			}
			return result;
		} catch (ee) {
			var t = "無法讀取Help:" + helpFile + ',' + segmentName + ',' + valueTag + ',' + labelTag;
			alert(t + '\n' + ee.message);
			throw new Error(t + '\n' + ee.message);
		}
	};

	function getRemoteHelpTable(help, segment, valueTag, lableTag) {
		var u = _contextPath + "/mvc/hnd/helpList/" + help + "/" + segment + "/" + valueTag + "/" + lableTag;
		var result = {};
		$.ajax({
			cache: true,
			async: false,
			url: u,
			success: function(data) {
				$.each(data.result, function(i, item) {
					result[item.first] = item.second;
				});
			},
			error: function(data) {
				alert("error getting ajax source:" + u);
			}
		});
		return result;
	}

	function getRemoteLookupArray(help, segment, col) {
		var u = _contextPath + "/mvc/hnd/helpList/" + help + "/" + segment + "?index=" + col;
		var result = [];
		$.ajax({
			cache: true,
			async: false,
			url: u,
			success: function(data) {
				result = data;
			},
			error: function(data) {
				alert("error getting ajax source:" + u);
			}
		});
		return result;
	}
	IfxHelp.prototype.getLookupArray = function(helpFile, segmentName, moreKey, bMatchBegind) {
		var keyTag;
		if (HELP_JS_FMT == "2") { // fmt=2 now
			keyTag = Helpfile[helpFile + "." + segmentName]["FILD"][0];
		} else {
			keyTag = Helpfile[helpFile][segmentName]["FILD"][0];
		}
		console.log("helpFile:" + helpFile + ", seg:" + segmentName + ", keyTag:" + keyTag);
		var r = null;
		if (HELP_JS_FMT == "2") {
			var helpmother = Helpfile[helpFile + "." + segmentName];
			r = helpmother[keyTag];
			// 篩選內容
			if (bMatchBegind) {
				var result = [];
				$.each(helpmother[bMatchBegind[0]], function(j, x) {
					if (x == bMatchBegind[1].trim()) {
						result.push(r[j]);
					}
				});
				console.dir(result);
				r = result; // 取代回來
			}
		} else {
			r = Helpfile[helpFile][segmentName][keyTag];
		}
		if (moreKey) {
			var more = parseUserProvideData(moreKey);
			r = r.concat(more[0]);
		}
		return r;
	};
	IfxHelp.prototype.lookupMsgId = function(msgid) {
		var keys = Helpfile["MSG.MSGDEF"]["MSGID"],
			values = Helpfile["MSG.MSGDEF"]["TEXT"],
			index = keys.indexOf(msgid),
			text = index == -1 ? "" : values[index];
		return text;
	};
	IfxHelp.prototype.getOvrReasonMap = function(code) {
		// return
		// getRemoteHelpTable("RQSPDEF","RQSPREASON","NUM","DESCRIPTION");
		var keys, values;
		try {
			if (HELP_JS_FMT == "2") {
				keys = Helpfile["RQSPDEF.RQSPREASON"]["NUM"];
				values = Helpfile["RQSPDEF.RQSPREASON"]["DESCRIPTION"];
			} else {
				keys = Helpfile["RQSPDEF"]["RQSPREASON"]["NUM"];
				values = Helpfile["RQSPDEF"]["RQSPREASON"]["DESCRIPTION"];
			}
			var m = {};
			for (var i = 0; i < keys.length; i++) {
				m[keys[i]] = values[i];
			}
			return m;
		} catch (ee) {
			alert('failed to get  Helpfile["RQSPDEF"]["RQSPREASON"]["NUM"]');
			throw ee;
		}
	};
	IfxHelp.prototype.getHelpTableDiv = function() {
		// 嚙諄許嚙踝為getTranObj().window.document
		return document.all.helptablediv;
	};
	IfxHelp.prototype.hide = function() {
		if ($divTooltip.is(':visible')) {
			$divTooltip.hide();
		}
		if ($div.is(':visible')) {
			$div.stop(true, true).hide(); // fadeOut("fast");
			$div.empty();
		}
	};
	IfxHelp.prototype.toggle = function(id) {
		if (id == currHelpFieldId && $div.html().length) {
			if ($div.is(':visible')) {
				$div.hide();
			} else {
				$div.show().draggable();
			}
		}
	};

	function displayLookup(fldname, fieldId, matrix, props, fn, isDrawRight) {
		// var oDiv = getHelpTable();
		var ss = [];
		var wCount = 0,
			index = 0;
		matrix[1].forEach((x) => {
			if (wCount < (x.trim().length + matrix[0][index].trim().length)) wCount = x.trim().length + matrix[0][index].trim().length;
			index++;
		})
		ss.push("<table class='altrowstable' width='" + (wCount * 11 + 60) * props.cols + "'>");
		var rows = matrix[0].length; // matrix[0] is an array,
		var trClosed = false,
			cells = 0;
		for (var i = 0; i < rows; i++) {
			if (i % props.cols == 0) {
				ss.push("<tr>");
				trClosed = false;
				cells = 0;
			}
			// for(var column = 0; column < matrix.length; column++) {
			// ss.push("<td>" + matrix[column][i] + "</td>");
			// }
			if (matrix[1][i] == "") ss.push("<td>" + matrix[0][i] + "\n" + matrix[1][i] + "</td>");
			else ss.push("<td>" + matrix[0][i] + "\n" + leftEar + matrix[1][i] + rightEar + "</td>");
			/*
			if (matrix[1][i] == "")
				ss.push("<td>"  + matrix[1][i] + "</td>");
			else
				ss.push("<td>"  + leftEar + matrix[1][i] + rightEar +	"</td>");
			*/
			// end
			cells++;
			if (i % props.cols == props.cols - 1) {
				ss.push("</tr>");
				trClosed = true;
			}
		}
		if (cells < props.cols) {
			for (var i = cells; i < props.cols; i++) {
				ss.push("<td> \n  </td>");
			}
		}
		// end
		if (!trClosed) {
			// while(cells++ < props.cols) ss.push("<td/>");
			ss.push("</tr>");
		}
		// var pos = $("#"+fieldId).position();
		// end
		// var o = $(divName);
		$div.html(ss.join(""));
		$(divName + " table td:even").addClass("evenrowcolor");
		$(divName + " table td:odd").addClass("oddrowcolor");
		// $div.css({
		// left: pos.left-5 + props.x,
		// top: pos.top+( $("#"+fieldId).height() + 15 + props.y)
		// });
		$(divName + " table td").hover(function() {
			$(this).addClass("hovcolor");
		}, function() {
			$(this).removeClass("hovcolor");
		}).click(function() {
			var v = $(this).text(); // 柯 html 改 text
			console.log("####" + v);
			console.log("####" + v.toString());
			if (!v) return;
			v = v.split('\n')[0];
			/*
			matrix[1].forEach(function (x,index) {
				if(v.trim() == x.trim())
					v = matrix[0][index];
		    });
		    */
			console.log("####" + v);
			ifx.setValue(fldname, v);
			$("#" + fieldId).blur();
			$div.stop().hide();
			if (fn != null && typeof fn == "function") fn();
		});
		setTimeout(positionHelp, 10, isDrawRight);
		// end
	}

	function positionHelp(isDrawRight) {
		var scrollTop = ifx.getScrollTop();
		var fld = ifx.getCurrentField();
		// 柯 正常help碰到 下方有help_ac 時 會導致 重複出現的bug
		// 故RETURN
		if (_autoComplete) {
			return;
		}
		if (fld && fld.id && fld.id() == currHelpFieldId) {
			pos = $("#" + currHelpFieldId).position();
			if (!pos) {
				console.log("fail to reposition help");
				return;
			}
			if (_useLayout) pos.top += scrollTop;
			/*
			 * 柯:移除此段,因TOOLTIP全部更改為上方 if ($divTooltip.is(':visible')) { props.y +=
			 * $divTooltip.height() + 14; }
			 */
			console.log("pos.top:" + pos.top);
			console.log("c height:" + $("#" + currHelpFieldId).height());
			console.log("props.y:" + props.y);

			if(isDrawRight)
				$div.css({
					left: pos.left + props.x + $("#" + currHelpFieldId).width() + 16,
					top: pos.top  - (3 + props.y)
				});
			else
				$div.css({
					left: pos.left + props.x,
					top: pos.top + ($("#" + currHelpFieldId).height() + 15 + props.y)
				});
			pos = $("#" + currHelpFieldId).position();
			if ($divTooltip.is(':visible')) {
				// $div
			}
			// 隱藏"輸入畫面"時 help顯示的問題
			if (ifx.isHelpVisible() && $("#entryArea").css("display") != "none") {
				$div.show().draggable();
			}
		} else {
			$div.stop().hide();
			$div.empty();
		}
	};
	IfxHelp.prototype.repositionHelp = positionHelp;
	IfxHelp.prototype.addHelpTableHover = function(oRow, field, value) {
		var ff = "updateHelpfield(\"" + field.elementID + "\",\"" + value + "\")";
		oRow.onclick = new Function(ff);
		oRow.onmouseover = new Function(" this.style.backgroundColor='darkslategray';this.style.color='orange' ");
		oRow.onmouseout = new Function("  this.style.backgroundColor='darkslategray';this.style.color='white'");
	};

	function updateHelpfield(id, value) {
		document.all[id].value = value;
		// dishelp();
		// nextfield();
		// KEYSnextfield();
		console.log("should go next field");
	}

	function easyValue(x) {
		return x.charAt(0) == "#" ? ifx.getValue(x) : x;
	}
	IfxHelp.prototype.tooltip = function(args, controlId) {
		args.shift();
		var msg = replaceEscape(args[0]);
		msg = easyValue(msg);
		var props = {
			x: 0,
			y: -7, // 柯更改7 -> -7
			w: IfxUtl.strlen(msg) * 7 + 50,
			h: 18
		};
		if (!isNaN(args[1]) && args[1]) props.x = parseInt(args[1], 10);
		if (!isNaN(args[2]) && args[2]) props.y = parseInt(args[2], 10);
		if (!isNaN(args[3]) && args[3]) props.w = parseInt(args[3], 10);
		if (!isNaN(args[4]) && args[4]) props.h = parseInt(args[4], 10);
		// end
		$divTooltip.html(msg);
		setTimeout(function() {
			if (ifx.dcFields[ifx.currentIndex].id() == controlId) {
				var pos = $("#" + controlId).offset();
				$divTooltip.css({
					left: pos.left - 6 + props.x,
					top: ifx.dcFields[ifx.currentIndex].multiline ? pos.top - $("#" + controlId).height() + 125 : pos.top - ($("#" + controlId).height() * 3 + props.y), // 柯更改成減 ()*3
					width: props.w,
					height: props.h
				}).show();
			} else {
				$divTooltip.hide();
			}
		}, 0);
		// end
	};

	function replaceEscape(t) {
		var x = ['&c;'];
		var y = [','];
		var re;
		for (var i = 0; i < x.length; i++) {
			re = new RegExp(x[i], 'g');
			t = t.replace(re, y[i]);
		}
		return t;
	}
	// auto complete
	// 柯:因應會計科目的問題，故調整help_AC 為自動依照value排序
	function displayAutoComplete(fieldId, sourceGetter, bMatchBegin, props, fnCallback) {
		if (!props.fmt) props.fmt = "{value} -- {label}";
		// $('.ui-autocomplete').css({'height': '200px'});
		var $jqFld = $('#' + fieldId);
		$jqFld.autocomplete({
			source: function(request, response) {
				var sourceSet = sourceGetter();
				if (!bMatchBegin) return sourceSet;
				sourceSet = sourceSet.sort(function(a, b) {
					if (a['value'] == b['value']) {
						return 0;
					}
					if (a['value'] > b['value']) {
						return 1;
					} else {
						return -1;
					}
				});
				var matches = $.map(sourceSet, function(tag) {
					if (tag.label.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
						return tag;
					}
				});
				response(matches);
			},
			// source : function(request, response) {
			// try {
			// sourceGetter(request, response);
			// console.log('leave source getter');
			//
			// }catch(ee){
			//
			// }
			// },
			autoFocus: false,
			// autoSelect:true,
			// selectFirst: false,
			// matchSubset: true,
			minLength: 1,
			select: function(e, ui) {
				console.log("##select");
				// e.preventDefault();
				if (ui.item != null) {
					if (props.buddy) {
						// var tt =
						// labels[values.indexOf(ui.item.value)];
						var tt = ui.item.origLabel;
						ifx.setValue(props.buddy, tt);
					}
					if (e.originalEvent.type == "menuselect" && !e.key) {
						$jqFld.val(ui.item.value);
						console.log("## autocomplete selected, key? " + e.key);
						if (e.key != "Tab") {
							console.log("====>Oh! you press " + e.key + " !!");
							fnCallback();
						} else {
							console.log("====>Oh! you press tab!!");
						}
						/*
						 * 柯:ERIC- AUTO 只給滑鼠 setTimeout(function() { ifx.NaviKeyEnabled(true); },
						 * 10);
						 */
					} else {
						// 只能使用滑鼠點擊(修正滑鼠不小心放在視窗上跳下個欄位時會是該值的BUG)
						e.preventDefault();
					}
					// $jqFld.autocomplete( "close" );
				}
			},
			open: function(e, ui) {
				console.log("##open");
				// ifx.NaviKeyEnabled(false); // 柯:ERIC- AUTO 只給滑鼠
				if ($divTooltip.is(':visible')) {
					var autocomplete = $(".ui-autocomplete");
					var newTop = autocomplete.offset().top + $divTooltip.height() + 14; // 14=
					// margin??
					if (props.y) newTop += props.y;
					var newLeft = autocomplete.offset().left;
					if (props.x) newLeft += props.x;
					autocomplete.css({
						'top': newTop,
						'left': newLeft
					}).show(); // 柯 新增
				}
				console.log('leave open');
			},
			close: function(e, ui) {
				console.log("##close");
				/*
				 * 柯:ERIC- AUTO 只給滑鼠 $jqFld.caretToEnd(); setTimeout(function() {
				 * ifx.NaviKeyEnabled(true); }, 5);
				 */
			},
		});

		function isOpened() {
			return !!$($jqFld.autocomplete('widget')).is(':visible');
		}

		function openIt() {
			console.log("\n\n### search............. \n\n\n");
			$jqFld.autocomplete('search'); // 移除第二個參數，直接驅動AUTOCOMPLETE
			$jqFld.off('focus.my1');
		}
		$jqFld.on('focus.my1', openIt);
		myHandler($jqFld);

		function myHandler($element) {
			// find the keydown handler that autocomplete attached
			var keyDownHandler = null;
			var keyDownNamespace = null;
			var events = $._data($element.get(0), "events");
			if (events && events.keydown) {
				for (var i = 0; i < events.keydown.length; i++) {
					if (events.keydown[i].namespace.indexOf("autocomplete") !== -1) {
						keyDownHandler = events.keydown[i].handler;
						keyDownNamespace = events.keydown[i].namespace;
					}
				}
			}
			// ?? if we found a handler, replace it with our version that may or
			// ?? may not call the default handler
			// TODO .....
			if (keyDownHandler !== null) {
				var bindhandler = function(event) {
					var keyCode = $.ui.keyCode;
					console.log("$$$event.keyCode:" + event.keyCode);
					if (event.keyCode === keyCode.UP || event.keyCode === keyCode.DOWN) {}
				};
				$element.unbind("keydown." + keyDownNamespace).bind("keydown." + keyDownNamespace, bindhandler);
			}
		}
		// setTimeout(function() {
		// $(".ui-autocomplete").css({
		// 'height' : props.h || 200,
		// 'cursor' : 'default',
		// 'overflow-y' : 'scroll',
		// 'position' : 'absolute'
		// });
		// }, 50);
		console.log("auto complete at " + fieldId);
	}

	function format(src, map) {
		if (arguments.length < 1) return src;
		for (var k in map) {
			var re = new RegExp("\\{" + k + "\\}", "g");
			src = src.replace(re, function() {
				return map[k];
			});
		}
		return src;
	};
	// end of Ifx Field Help Module
	return IfxHelp;
}());

function setupCombobox($) {
	$.widget("custom.combobox", {
		_create: function() {
			this.wrapper = $("<span>").addClass("custom-combobox").insertAfter(this.element);
			this.element.hide();
			this._createAutocomplete();
			this._createShowAllButton();
		},
		_createAutocomplete: function() {
			var selected = this.element.children(":selected"),
				value = selected.val() ? selected.text() : "";
			this.input = $("<input>").appendTo(this.wrapper).val(value).attr("title", "").addClass("custom-combobox-input ui-widget " + " ui-widget-content ui-state-default ui-corner-left").autocomplete({
				delay: 0,
				minLength: 0,
				source: $.proxy(this, "_source")
			}).tooltip({
				tooltipClass: "ui-state-highlight"
			});
			this._on(this.input, {
				autocompleteselect: function(event, ui) {
					ui.item.option.selected = true;
					this._trigger("select", event, {
						item: ui.item.option
					});
				},
				autocompletechange: "_removeIfInvalid"
			});
		},
		_createShowAllButton: function() {
			var input = this.input,
				wasOpen = false;
			$("<a>").attr("tabIndex", -1).attr("title", "Show All Items").tooltip().appendTo(this.wrapper).button({
				icons: {
					primary: "ui-icon-triangle-1-s"
				},
				text: false
			}).removeClass("ui-corner-all").addClass("custom-combobox-toggle ui-corner-right").mousedown(function() {
				wasOpen = input.autocomplete("widget").is(":visible");
			}).click(function() {
				input.focus();
				// Close if already visible
				if (wasOpen) {
					return;
				}
				// Pass empty string as value to search
				// for,
				// displaying all results
				input.autocomplete("search", "");
			});
		},
		_source: function(request, response) {
			var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
			response(this.element.children("option").map(function() {
				var text = $(this).text();
				if (this.value && (!request.term || matcher.test(text))) return {
					label: text,
					value: text,
					option: this
				};
			}));
		},
		_removeIfInvalid: function(event, ui) {
			// Selected an item, nothing to do
			if (ui.item) {
				return;
			}
			// Search for a match (case-insensitive)
			var value = this.input.val(),
				valueLowerCase = value.toLowerCase(),
				valid = false;
			this.element.children("option").each(function() {
				if ($(this).text().toLowerCase() === valueLowerCase) {
					this.selected = valid = true;
					return false;
				}
			});
			// Found a match, nothing to do
			if (valid) {
				return;
			}
			// Remove invalid value
			this.input.val("").attr("title", value + " didn't match any item").tooltip("open");
			this.element.val("");
			this._delay(function() {
				this.input.tooltip("close").attr("title", "");
			}, 2500);
			this.input.data("ui-autocomplete").term = "";
		},
		_destroy: function() {
			this.wrapper.remove();
			this.element.show();
		}
	});
}
var Swift = Swift || {};
(Swift.util = function($) {
	var _cfg = {
		logDiv : 'results',
		contentBasePath : './sweet/swift/def/mt-converted/',
		basePath : './mvc/hnd/swift/mt/'
	};

	var _cache = {};
	
	function load(mtName) {
		var deferred = null;
		var oCachedMt = _cache[mtName];
		if (oCachedMt) {
			deferred = new $.Deferred();
			setTimeout(function() {
				deferred.resolve(oCachedMt);
			}, 1);
			return deferred.promise();
		}

		var url = _cfg.basePath + 'mt' + mtName;// + '.txt';
		// return $.ajax({
		// type : "GET",
		// cache : false,
		// url : url,
		// dataType : "json"
		// });
		return $.ajax({
			type : "GET",
			cache : false,
			url : url,
			dataType : "json"
		}).then(function(data) {
			_cache[mtName] = data;
			return data;
		}, function(err) {
			return err;
		});
	}

	function init(opt) {
		_cfg = $.extend(_cfg, opt);
	}
	function assert(value, desc) {
		var li = document.createElement("li"), doc = document; // 小柯增效能
		li.className = value ? "pass" : "fail";
		li.appendChild(document.createTextNode(desc));
		doc.getElementById(_cfg.logDiv).appendChild(li); // 小柯增效能
		// document.getElementById(_cfg.logDiv).appendChild(li); //小柯增效能
		console.log(desc);
	}

	return {
		load : load,
		init : init,
		assert : assert
	};
}(jQuery));

(Swift.ui = function($) {
	var SWIFT_FORM = 'swiftForm';
	var SWIFT_FIELD_PREFIX = '_swf_';
	var mt = null;
	var ifx = null;
	var curr = -1;
	var currFocusedDom = null;
	var displaying = true;
	var addingField = false;
	var jumpTargetId = null; // mouse click target
	var $divSwift = null;

	var SWIFT_TEXT_PANEL = '__div_swift_text_panel';
	var SWIFT_TEXT = 'swift_text';
	var SWIFT_TEXT_COUNTS = 'swift_text_counts';
	var SWIFT_TEXT_REST = "swift_text_rest";
	var SWIFT_MAX_BUF = 10000;
	var SWIFT_TEXT_TITLE = 'SWIFT電文';

	var _Vars = {};
	var _loopMap = {};
	
	// -----------------------------------------------------
	// MyTag
	function MyTag(id, oTag) {
		this.id = id;
		var inst = $.extend(this, oTag);
		if (inst.isMandatory())
			inst.resetValDone();
		else
			inst.setValDone('');
		return inst;
	}
	var _tagFn = MyTag.prototype;
	_tagFn.getTagName = function() {
		return this['$'].tag;
	};
	_tagFn.getName = function() {
		return this['$'].name;
	};
	_tagFn.isMandatory = function() {
		return this['$'].status == 'M';
	};
	_tagFn.isOptions = function() {
		return (this['$']['options']) ? true : false;
	};
	_tagFn.dump = function() {
		var s = 'id' + this.id;
		s += ', tag:' + this['$'].tag;
		s += ', mandatory:' + this.isMandatory();
		s += ', option? ' + this.isOptions();
		console.log(s);
	};
	_tagFn.setValDone = function(value) {
		this['valdone'] = true;
		this['value'] = value;
		console.log('set ' + this.id + ' valdone');
	};
	_tagFn.resetValDone = function() {
		this['valdone'] = false;
		this['value'] = null;
		console.log('set ' + this.id + ' non-valdone');
	};
	_tagFn.bag = function() {
		return this['$']['bag'];
	};
	_tagFn.getLength = function() {
		return parseInt(this['$'].len, 10);
	};
	_tagFn.getValue = function() {
		return this.value;
	};
	_tagFn.isDirty = function(newValue) {
		return this.value != newValue;
	};
	_tagFn.addCodes = function(forWhat, must, other, codes) { // 小柯 增 抓 code
																// 值測試成功
		this.must = must;
		this.codeMap = this.codeMap || {};
		this.codeMap[forWhat] = {
			must : must,
			codes : codes,
			other : other
		// 小柯 增 抓 code 值測試成功
		};
	};

	_tagFn.getCodesByKey = function(key) {
		if (this.codeMap == undefined || this.codeMap[key] == undefined)
			return false;
		return this.codeMap[key];
	};

	var _m = null; // instance of IdTagMap, store every input {id -> MyTag}
	function IdTagMap() {
		this.map = {};
		this.add = function(id, oTag) {
			this.map[id] = new MyTag(id, oTag);
			return this.map[id];
		};
		this.get = function(id) {
			return this.map[id];
		};
	}

	// end of MyTag
	// -----------------------------------------------------

	function log(t) {
		console.log(t);
	}

	function validateMT(mt) {
		return mt && mt.tags;
	}
	function display(aMt, placeHolder, aIfx, fnCallback) {
		curr = -1;
		mt = aMt;
		ifx = aIfx;

		if (!validateMT(aMt)) {
			displaying = false;
			alert('無法載入電文格式檔');
			return;
		}

		// get some values from VAR

		// _Vars['sendingTid'] = $.trim(ifx.getValue('#SENDINGTID'));
		// _Vars['msgType'] = $.trim(ifx.getValue('#MSGTYPE'));
		// _Vars['pri'] = $.trim(ifx.getValue('#PRI'));
		// _Vars['dest'] = $.trim(ifx.getValue('#DEST'));

		_m = new IdTagMap();
		Swift.validators['init'](mt, ifx);
		$divSwift = $('#' + placeHolder);

		$(
				'<div id="' + SWIFT_FORM
						+ '"><table id="swiftMainTable"><caption>'
						+ mt['$'].type + " " + mt['$'].name
						+ '</caption><tbody></tbody></table></div>').appendTo(
				$divSwift);
		$main = $('#swiftMainTable');

		$.each(mt.tags, function(i, x) {
			initTag(i, x);
			$('#swiftMainTable  > tbody:last').append(
					'<tr><td>' + drawField(x, x.id) + '</td></tr>');
			// if (i > 1)
			// return false;
		});

		// repositionSwiftTextPanel(true);

		// 必須等待畫面產生完後, 才呼叫fnCallback, 否則以非同步產生之部份畫面會產生詭異效果
		//
		setTimeout(function() {
			displaying = false;
			if (fnCallback) {
				fnCallback();
			}
		}, 50);

		$main.delegate(".swiftField", "click paste ", function(evt) {
			console.log("EVENT " + evt.type.toUpperCase() + " on "
					+ $(this).attr('id'));
			if (currFocusedDom == null || currFocusedDom != this)
				fieldClicked($(this));
			if (evt.type == "click")
				evt.preventDefault();
		});

		function fieldClicked($target) {
			var targetId = $target.attr('id');
			console.log('click swift form field, target:' + targetId);

			if (ifx.isSwiftMode()) { // in swift fields
				console.log('from swift to swift');
				var ids = $.map(getInputs(), function(x) {
					return x.id;
				});
				var targetIndex = ids.indexOf(targetId);
				if (targetIndex < curr) { // moving up
					curr = targetIndex;
					myFocus($target[0]); // get dom from $target
				} else { // moving down
					jumpTargetId = targetId;
					nextField(true, 'jump');
					jumpTargetId = null;
				}
			} else {
				console.log('from non-swift form to swift form');
				if (ifx.isBeforeSwiftForm()) { // current field is before swift
					// form
					console.log('before swift from to swift form');
					jumpTargetId = targetId;
					ifx.nextField(ifx.currentIndex + 1, false, true,
							ifx.swiftcIndex);
					jumpTargetId = null;

				} else { // current field is after swift form

				}
			}

		}
	}

	function repositionSwiftTextPanel(bFirstTime) {
		if (bFirstTime) {
			$('#' + SWIFT_TEXT_PANEL).remove();
			var t = "<div id='" + SWIFT_TEXT_PANEL + "'   title='"
					+ SWIFT_TEXT_TITLE + "'>";
			t += "<div>Count:<span class='swiftTextCounts' id='"
					+ SWIFT_TEXT_COUNTS
					+ "'></span><span class='swiftTextRest' id='"
					+ +SWIFT_TEXT_REST + "'></span>" + "</div>";
			t += "<div class='swiftText' id='" + SWIFT_TEXT + "'></div>";
			t += "</div>";
			$(t).appendTo($divSwift);
			$('#' + SWIFT_TEXT_PANEL).dialog(
					{
						width : 400,
						minWidth : 200,
						height : 500,
						maxHeight : 700,
						closeOnEscape : false,
						open : function(event, ui) {
							$(this).closest('.ui-dialog').find(
									'.ui-dialog-titlebar-close').hide();
						}
					});
			$('#' + SWIFT_TEXT_PANEL).dialog({
				dialogClass : 'no-close'
			});
		}
		var left = $divSwift.position().left + $divSwift.width() + 10;
		// var top = $(window).scrollTop() + $divSwift.offset().top;
		var top = ifx.getScrollTop() + $divSwift.offset().top;
		$("#" + SWIFT_TEXT_PANEL).dialog("option", "position", [ left, top ]);
	}

	function initTag(i, x) {
		x.id = SWIFT_FIELD_PREFIX + i;
		x.ident = 0;
	}

	function drawField(x, id) {
		var t = "";
		if (x['$'].type == 'loop') {
			var x2  = $.extend({}, x); // clone new one
			if (x2.times == undefined)
				x2.times = 1;
			t = drawLoop(x2);
			setTimeout(function() {
				for (var i = 0; i < x2.times; i++) {
					drawOneLoop(x2, i);
				}
			}, 1);
		} else {
			if (x['$'].options) {
				t = drawOptions(x, id);
			} else {
				if (x['$'].complex) {
					t = drawComplexField(x, id);
				} else {
					t = drawSimpleField(x, id);
				}
			}
		}
		// return wrapIdent(x, t);
		return t;
	}
	function getIdent(ident) {
		return ident * 30 + 10;
	}
	function wrapIdent(x, html) {
		var width = getIdent(x.ident);
		return "<div style='padding-left:" + width + "px'>" + html + "</div>";
	}
	function makeContainer(id, ident) {
		var width = getIdent(ident);
		width = 0;
		return "<div id='" + id + "' style='padding-left:" + width
				+ "px'></div>";
	}

	
	
	function drawLoop(loopTag) {
		var key = 'tblLoop' + loopTag.id;
		_loopMap[key] = loopTag; // 把loopTag 儲存於_loopMap, 再刪除其他列之後 部會因為列改變而找不到loopTag
								// desearial時也可以用
		loopTag.container = key;
		
		var h = "<table id='" + loopTag.container
				+ "' class='loopTable' ><caption>"
				+ (loopTag['$'].name || 'loop')
				+ "</caption><tbody></tbody></table>";
		h += "<span id='btnLoopAdd"
				+ loopTag.id
				+ "' class='button gray loopButton loopAddButton'>Add Loop</span>";

		setTimeout(function() {
			$('#btnLoopAdd' + loopTag.id).on('click', function() {
				var $this = $(this);
				var id = $this.attr('id');
				var key = id.replace('btnLoopAdd','tblLoop');
				var loopTag = _loopMap[key]; // 取回loopTag
				var max = loopTag['$'].count;
				// var rowCount = $('#' + loopTag.container + ' > tbody >
				// tr').length;
				var rowCount = (getLoopRows(loopTag.container)).length;
				if (max != undefined && rowCount >= max) {
					alert('no more loop');
					return;
				}
				drawOneLoop(loopTag, rowCount);
				ifx.resizeSwift();
			});
		}, 1);

		return h;

	}

	function getLoopRows(container) {
		return $('#' + container + '  > tbody > tr');
	}

	function drawOneLoop(loopTag, loopIndex) {
		var t = [];
		var id, firstId = null;

		var loopIndexPrefix = loopTag.id + "_loop_" + loopIndex;
		t.push("<table class='oneLoopTable'>");
		$.each(loopTag.tags, function(i, x) {
			t.push("<tr><td align='left'>");
			//x.id = loopTag.id + "_" + loopIndex + '_' + i;
			x.id = loopIndexPrefix + '_' + i;
			if (firstId == null)
				firstId = x.id;
			x.ident = loopTag.ident + 1;
			
			t.push(drawField(x, x.id));
			
			t.push("</td></tr>");
		});
		t.push("</table>");
		
		var tt = t.join('');// '<br/>');
		tt = "<div class='oneLoopWrapper'>" + tt + "</div>";

		var btnDelete = 'btnLoopDel' + loopIndexPrefix;
		var b = "<span id='"
				+ btnDelete
				+ "' "
				+ " data-prefix='" + loopIndexPrefix + "' " 
				+" class='button darkblue loopButton  loopDeleteButton' >Delete</span>";

		var theHtml = '<tr><td class="loopIndex">'
				+ (loopIndex + 1)
				+ '</td><td align="center" style="  vertical-align: top;"  colspan="8">'
				+ tt + '</td><td style=" vertical-align: top;">' + b
				+ '</td></tr>';

		$('#' + loopTag.container + '  > tbody:last').append(theHtml);

		//
		// $('#' + loopTag.container + ' > tbody:last').append(
		// '<tr><td class="loopIndex">' + (loopIndex + 1) + '</td><td style="
		// vertical-align: top;" colspan="8">' + tt
		// + '</td><td style=" vertical-align: top;">' + b + '</td></tr>');

		setTimeout(function() {
			console.log('******* register ' + btnDelete);
			$('#' + btnDelete).on('click', deleteRow);
			$('#' + btnDelete).hover(hoverRow,hoverRowOff);
		}, 150); // point:timeoutDeleteLoop
		function hoverRow(){
			var td = $(this).parent();
			var tr = td.parent();
			//tr.effect('highlight', {}, 500);
			//tr.css('background-color', '#ff0');
			var ttt = $("td",tr);
			ttt.css('background-color', '#C8C8C8');
		}
		function hoverRowOff(){
			var td = $(this).parent();
			var tr = td.parent();
			//tr.effect('highlight', {}, 500);
			//tr.css('background-color', '');
			var ttt = $("td",tr);
			ttt.css('background-color', '');
		}
		
		function deleteRow() {
			var $deleteBtn = $(this);
			
			var thePrefix = $deleteBtn.attr("data-prefix");
			
			console.log("******* deleteRow for:"+ thePrefix);
			
			var td = $(this).parent();
			
			var tr = td.parent();

			// var rows = $('#' + loopTag.container + ' > tbody > tr');
			var rows = getLoopRows(loopTag.container);
			if (rows.length == 1) {
				console.log("only one loop remains, can't delete");
				return;
			}
			// var deletedRow = $('#' + loopTag.container + ' > tbody >
			// tr').index(tr);
			var deletedRow = getLoopRows(loopTag.container).index(tr);
			// var rows = $('#' + loopTag.container + ' tr').length;

			var trInputs = $('[id^=' + SWIFT_FIELD_PREFIX + ']', tr);

			$.each(trInputs, function(i, x) {
				if (currFocusedDom == x) { // focus在刪掉的列之中
					// 將focus先移到此TR之前一個輸入控制項
					var flds = getInputs();
					$.each(flds, function(i, x) {
						if (x == trInputs[0]) {
							curr = i > 0 ? i - 1 : 0; // 總會剩一個吧
							myFocus(flds[curr]);
							return false; // break;
						}
					});
					return false; // break $.each(trInputs...
				}
			});

			// change the background color to red before removing
			tr.css("background-color", "#FF3700");
			tr.fadeOut(400, function() {
				tr.remove();
				reorder();
				resetCursor();
				ifx.resizeSwift();
			});
			function resetCursor() {
				var currFocus = $('.myFocusHint');
				if (currFocus) {
					currFocus = currFocus[0];
					var inputs = getInputs();
					$.each(inputs, function(i, x) {
						if (currFocus == x) {
							curr = i;
							return false; // break $.each
						}
					});
					myFocus(currFocus);
				}

			}
			function reorder() {
				// var trs = $('#' + loopTag.container + ' > tbody > tr');
				console.log("reorder container:"+loopTag.container);
				$tblCotainer = $('#'+loopTag.container);
				
				var trs = getLoopRows(loopTag.container);
				// $('#' + loopTag.container + ' > tbody > tr').length;
				$.each(trs, function(row, tr) {
					if (row < deletedRow)
						return true; // continue next row
					
					var ss = thePrefix.split('_');
					var newPrefix = ss.join('_'); // the deleted row prefix
					ss[ss.length-1] = (row+1);
					var oldPrefix = ss.join('_');
					thePrefix = oldPrefix; // advance to next row
					
					
					
					$('td', tr).each(function(col, td) {
						var $td = $(td);
						if (col == 0)
							$td.text(row + 1);
					});
					
					var subLoop = $('[id^=tblLoop]', $tblCotainer);
					$.each(subLoop, function(i,x){
						var saved = _loopMap[x.id];
						delete  _loopMap[x.id];
						x.id = newId(x.id);
						saved.container = x.id; // see drawLoop()
						_loopMap[x.id] = saved;
					});
					
					
					// replace every input field's ID in this TR
					var inputs = $('[id^=' + SWIFT_FIELD_PREFIX + ']', tr);
					$.each(inputs, function(i, x) {
						// _swf_4_2_0
						console.log('input oid:' + x.id);
						x.id = newId(x.id);
						console.log('  ====>' + x.id);
					});

					// replace every delete button ID in this TR
					var inputs = $('[id^=btnLoopDel]', tr);
					$.each(inputs, function(i, x) {
						// _swf_4_2_0
						console.log('btn deleete oid:' + x.id);
						x.id = newId(x.id);

						$(x).attr("data-prefix",newPrefix);
						console.log('  ====>' + x.id);
						console.log("====prefix:"+$(x).attr("data-prefix"));
					});

					// replace every add loop button ID in this TR
					var inputs = $('[id^=btnLoopAdd]', tr);
					$.each(inputs, function(i, x) {
						// _swf_4_2_0
						console.log('btn add oid:' + x.id);
						x.id = newId(x.id);
						console.log('  ====>' + x.id);
					});

					
					
					// replace every container's ID in this TR
					var containers = $('[id^=div' + SWIFT_FIELD_PREFIX + ']',
							tr);
					$.each(containers, function(i, x) {
						var oid = x.id;
						console.log('container oid:' + x.id);
						x.id = newId(x.id);
						// $('#' + x.id).html(oid + '===>' + x.id);
						console.log('  ====>' + x.id);
					});

					function newId(oid) {
//						var ss = oid.split('_');
//						console.log("newId(), oid:"+oid +", ss length:"+ss.length);
////						ss[3] = row;
//						ss[ss.length-1] = row;
//						return ss.join('_');
						return oid.replace(oldPrefix, newPrefix);
					}
				});

			}
		} // end of function deleteRow

		resetFocus();

		function resetFocus() {
			var flds = getInputs();
			var pos = -1, found = false;
			$.each(flds, function(i, x) {
				if (x.id == firstId) {
					found = true;
					pos = i;
				}
			});

			if (!found) {
				setTimeout(resetFocus, 10);
				return;
			}
			if (curr > pos) {
				myFocus(flds[pos]);
			}
		}
	}

	function drawOptions(x, id) {
		x.currOpt = null;
		_m.add(id, x);

		var options = x['$'].options;

		/*
		 * var label1 = x['$'].tag + ' '; var clsName = ' lblTag '; if
		 * (x['$'].status == 'M') { label1 = '*' + label1; clsName = '
		 * requiredTag ' + clsName; }
		 * 
		 * label1 = "<span class='"+ clsName + "'>" + label1 + "</span>";
		 * 
		 * var label2 = x['$'].name; label2 = "<span class='lblName'>" + label2 + "</span>";
		 */
		var label = drawLabel(x);

		var len = 1;

		var title = options;

		var input = "<input type='text'  class='swiftField' id='" + id
				+ "'  size='" + len + "' maxlength='" + len + "' title='"
				+ title + "' data-tag='" + x['$'].tag.slice(0, 2) + "' />";

		var container = 'div' + id;
		var optDiv = makeContainer(container, x.ident + 1);

		setTimeout(function() {
			$('#' + id).on('keypress', function(e) {
				var k = String.fromCharCode(e.keyCode);
				var oo = options.split(',');
				if (oo.indexOf(k.toUpperCase()) == -1)
					return false;
			});
		}, 1);

		return label + input + "<span class='lblOptions'>(" + options
				+ ")</span>" + optDiv;
	}

	function drawOpt(containerId, n, abc, fnCallBack) {
		var $container = $('#' + containerId);
		$container.empty();

		if (abc) {
			var tag = n['$'].tag;
			tag = tag.slice(0, tag.length - 1);

			var tagName = $.trim(tag + abc); // trim for no-letter option
			var optTag = n['opt'][tagName];
			optTag = $.extend({}, optTag); // clone an option field
			optTag.id = n.id + '_' + abc + '_' + '0';
			optTag.parentId = n.id;
			optTag.ident = n.ident + 1;
			optTag['$'].tag = tag + $.trim(abc); // 補tag名稱
			optTag['$'].name = n.$.name; // 使用Parent 名稱
			optTag.noLabel = true;
			$container.html(drawField(optTag, optTag.id));

			ifx.resizeSwift();
		}
		var savedCurr = curr;
		addingField = true;
		setTimeout(function() {
			if (fnCallBack != undefined)
				fnCallBack();

			var flds = getInputs();
			curr = savedCurr + 1;
			console.log('drawOpt() curr:' + curr + '==>' + flds[curr].id);
			myFocus(flds[curr]);

			addingField = false;
		}, 1);
	}
	function drawLabel(x, statusWanted, tagWanted, nameWanted) {
		if (x.noLabel && x.noLabel == true)
			return '';
		statusWanted = statusWanted == undefined ? true : statusWanted;
		tagWanted = tagWanted == undefined ? true : tagWanted;
		// nameWanted = nameWanted == undefined ? true : nameWanted;
		if (!statusWanted && !nameWanted)
			return '';

		var lblStatus = '', label1 = '', label2 = '', clsName = " lblTag ";
		if (statusWanted) {
			lblStatus = "<span class='lblStatus ";
			if (x.$.status == 'M') {
				clsName = ' requiredTag ' + clsName;
				lblStatus += " lblStatus-M'>M</span>";
			} else if (x.$.status == 'O') {
				lblStatus += " lblStatus-O'>O</span>";
			} else {
				lblStatus += " '></span>";
			}

		}
		if (tagWanted) {
			if (x['$'].tag)
				label1 = x['$'].tag + ' ';

			label1 = "<span class='" + clsName + "'>" + label1 + "</span>";
		}

		if (x['$'].name)
			label2 = "<span class='lblName'>" + x['$'].name + "</span>";

		return lblStatus + label1 + label2 + '<br/>';
	}
	function drawSimpleField(x, id, statusWanted, tagWanted, nameWanted) {
		x = _m.add(id, x); // convert to MyTag and add to _m
		console.log('drawSimpleField for ' + x.$.tag);
		// labelWanted = labelWanted === undefined ? true: labelWanted;
		// nameWanted = nameWanted === undefined ? true: nameWanted;
		var label = drawLabel(x, statusWanted, tagWanted, nameWanted);

		var context = x.$.context || '';
		var component = x.$.component || context;
		var s = "";

		var len = x.$.len;
		var w = len;
		var styles = [];
		if (len.indexOf('*') > 0) {
			x.multiLine = true;
			var ss = len.split('*');
			x.rows = parseInt(ss[0], 10);
			x.cols = parseInt(ss[1], 10);
			len = x.rows * x.cols;
			w = x.cols > 100 ? 100 : x.cols;
		}

		var input = " data-tag='" + x.$.tag + "' class='swiftField' id='" + id
				+ "' " + " maxlength='" + len + "' title='" + component + "' ";
		if (x.multiLine) {
			styles.push("overflow:scroll;");
			var ta = "<textarea cols='" + x.cols + "' rows='" + x.rows + "' "
			if (x.rows > 4) {
				styles.push("height: 100px");
			}
			input = ta + input;
		} else {
			styles.push("width:" + (w * 10 + 14) + "px");
			input = "<input type='text' " + input;
		}

		if (styles) {
			input += ("style='" + styles.join(";") + "' ");
		}
		if (len == 0) {
			input += " disabled='disabled' data-constant='" + x.$.constant
					+ "' data-swift-tag='" + x.$.tag + "' data-swift-text='' ";
		}

		input += "/>";

		var labelContext = "<span class='lblContext'> " + context + " </span>";
		s = label + input + labelContext;
		// if (x.code && x.code[0]) { //當第一格有值的時?
		if (x.code && x.code[0]) {
			x.codeDivId = 'codeDiv_' + x.id;
			s += "<div class='tagCodeArea'>";
			$.each(x.code, function(i, c) {
				s += drawCode(x, c, i);
			});
			s += "</div>";

		}
		return s;

		function drawCode(oTag, codeTag, idx) {
			var s = "", o = null, targetId = oTag.id, codeAreaId = null;

			o = parseCodeDef(oTag, codeTag, idx);
			codeAreaId = o.containerId;
			// alert(x.$.tag + "," + x.code[0].$.must + "\n" + x.code[0]._);
			s = "<div id='" + o.containerId + "' class='code-accordion'>";

			var title = '';// oTag.getTagName();
			if (o.forWhat)
				title += o.forWhat;
			if (o.must)
				title += "必須輸入下列代碼";
			else
				title += "可輸入下列代碼";
			s += "<h1>" + title + "</h1>";
			s += "<div><div class='code-content'>";
			s += "<table class='code-table'>";

			for (var i = 0; i < o.codes.length; i++) {
				if (i % o.cols == 0)
					s += "<tr>";

				s += "<td class='code-cell'>" + o.codes[i] + "</td>";
				if ((i % o.cols) == (o.cols - 1))
					s += "</tr>";
			}
			s += "</table>";
			s += "</div></div>";
			s += "</div>";

			var activeIt = o.show ? 0 : false;
			activeIt = 0;
			var bReplace = o.must && !(o.forWhat);
			setTimeout(function() {
				var $z = $('#' + codeAreaId);
				$z.accordion({
					collapsible : true,
					active : 0
				});
				if (o.width) {
					$z.css('width', o.width + "px");
				}
				$('td', $z).on('click', function() {
					var t = $(this).text();
					if (t) {
						var $target = $('#' + targetId);
						if (bReplace) {
							$target.val(t);
						} else {
							appendAtCaret($target, getCaret($target), t);
							var maxLen = $target.attr("maxlength");
							if ($target.val().length > maxLen) {
								$target.val($target.val().slice(0, maxLen));
							}

						}
						goCurrentField();
					}
				}).hover(function() {
					$(this).addClass('code-cell-over');
				}, function() {
					$(this).removeClass('code-cell-over');
				});

			}, 10);

			return s;
		}
		function parseCodeDef(oTag, oCode, idx) {
			var o = {};
			o.containerId = 'code' + oTag.id + '-' + idx;

			o.must = parseBool(oCode.$.must);
			o.other = oCode.$.other; // 小柯 增 抓 code 值測試成功
			o.show = oCode.$.show;
			if (o.show === undefined) {
				o.show = o.must;
			} else {
				o.show = parseBool(oCode.$.show);
			}

			if (oCode.$.width)
				o.width = oCode.$.width;
			o.cols = 2;
			if (oCode.$.cols)
				o.cols = oCode.$.cols;

			o.forWhat = oCode.$['for'] || '';

			oTag
					.addCodes(o.forWhat || '*', o.must, o.other, oCode._
							.split(',')); // 這個codes是為了檢查用 //小柯 增 抓 code 值測試成功

			o.codes = oCode._.split(','); // 故意重複split(), o.codes是未了顯示,
			// 會補滿空白td
			if (o.cols > 1) {
				while (o.codes.length % o.cols != 0)
					o.codes.push('');
			}

			return o;
		}
	}
	function parseBool(s, defaultTrueOrFalse) {
		if (defaultTrueOrFalse === undefined)
			defaultTrueOrFalse = false;
		if (!s)
			return defaultTrueOrFalse;

		s = s.toLowerCase();
		if (s === "true")
			return true;
		else
			return false;
	}
	function getCaret(el) {
		var doc = document; // 小柯增效能
		if (el.prop("selectionStart")) {
			return el.prop("selectionStart");
		} else if (doc.selection) { // 小柯增效能 document->doc
			el.focus();

			var r = doc.selection.createRange(); // 小柯增效能 document->doc
			if (r == null) {
				return 0;
			}

			var re = el.createTextRange(), rc = re.duplicate();
			re.moveToBookmark(r.getBookmark());
			rc.setEndPoint('EndToStart', re);

			return rc.text.length;
		}
		return 0;
	}
	;
	function appendAtCaret($target, caret, $value) {
		var value = $target.val();
		if (caret != value.length) {
			var startPos = $target.prop("selectionStart");
			var scrollTop = $target.scrollTop;
			$target.val(value.substring(0, caret) + $value
					+ value.substring(caret, value.length));
			$target.prop("selectionStart", startPos + $value.length);
			$target.prop("selectionEnd", startPos + $value.length);
			$target.scrollTop = scrollTop;
		} else if (caret == 0) {
			$target.val($value + value);
		} else {
			$target.val(value + $value);
		}
	}
	;

	function drawComplexField(complex, id) {
		var t = '', tmp;
		_m.add(id, complex);
		var emptyTagName = "<span class='lblSubField'> </span>";
		t += drawLabel(complex);
		$.each(complex.tags, function(i, x) {
			x.$.tag = complex.$.tag + '_' + i;
			x.id = id + '-sub-' + i + '-' + complex.tags.length;
			x.ident = 1;
			tmp = drawSimpleField(x, x.id, true, false);
			tmp = tmp.replace(/lblName/g, "lblSubName");
			t += (emptyTagName + tmp);
			t += "<br/>";
		});

		return t;
	}

	function getInputs(containerId) {
		containerId = containerId || SWIFT_FORM;
		return $('[id^=' + SWIFT_FIELD_PREFIX + ']:enabled:not([readonly])',
				$('#' + containerId));
	}

	function getAllSwiftInputs(containerId) { // 小柯
		containerId = containerId || SWIFT_FORM; // 小柯
		return $('[id^=' + SWIFT_FIELD_PREFIX + ']', // 小柯
		$('#' + containerId)); // 小柯
	} // 小柯

	function nextField(val, mode, fn) {
		var valWanted = true;
		if (val == false)
			valWanted = false;

		var flds = getInputs();

		if (flds.length == 0) {
			setTimeout(function() {
				nextField(val, mode, fn);
			}, 10);
			return;
		}

		// 滑鼠可能在var欄位間亂移動, 若在swift form之後的var 欄位移到swift form之前欄位,
		// curr值會維持為flds.length, 所以在此重設
		if (curr >= flds.length)
			curr = -1;

		if (!mode) {
			if (curr >= 0) {
				if (!validateCurrentField(valWanted, flds[curr]))
					return;
			}
		} else {
			if (mode == 'jump') {
				if (curr < 0)
					curr = 0; // TODO: 應該都是由0開始

				// 企圖跳到swift後面之var欄位
				if (jumpTargetId == null) {
					for (; curr < flds.length; curr++) {
						myFocus(flds[curr]);
						if (!validateCurrentField(valWanted, flds[curr]))
							return;
					}
					removeFocus();
					curr = flds.length;// save swift form cursor position to
					// last field
					if (fn)
						fn();
					return;
				}

				// 企圖跳到某個swift欄位
				var jumpToIndex = getIndexById(jumpTargetId);
				if (jumpToIndex != -1) {

					for (; curr < flds.length && curr < jumpToIndex; curr++) {
						myFocus(flds[curr]);
						if (!validateCurrentField(valWanted, flds[curr]))
							return;
					}
				}
				myFocus(flds[curr]);
				return;
			}
		}

		function getIndexById(id) {
			var idx = -1;
			$.each(flds, function(i, x) {
				if (x.id == id) {
					idx = i;
					return false; // break $.each
				}
			});
			return idx;
		}

		curr++;
		if (curr >= flds.length) {
			if (!displaying && !addingField)
				return 'LEAVE SWIFT';
			else
				return null; // 正在增加新欄位(option field), 停留在Swift Form
		}
		console.log('nextField() curr:' + curr + '==>' + flds[curr].id);
		myFocus(flds[curr]);

	}
	var validateCurrentField = function(valWanted, dom, updateWanted) {
		var $curr;
		var o;
		if (updateWanted === undefined)
			updateWanted = true;
		if (valWanted) {
			$curr = $(dom);

			o = _m.get(dom.id);
			if (o.isDirty($curr.val())) {
				o.resetValDone();
				var ok = validated(dom);
				if (!ok) {
					return false;
				}
				// ok == true
				o.setValDone($curr.val()); // TODO: check multi line
			}
		}
		if (updateWanted) {
			convertToSwiftFormat(dom.id);
			displaySwiftText('');
		}
		return true;
	}

	function externalSetValDone($field) {
		var o = _m.get($field.attr('id'));
		o.setValDone($field.val());
	}
	function removeFocus() {
		$('.myFocusHint').removeClass('myFocusHint');
		$('.myFocusRowHint').removeClass('myFocusRowHint');
		$('.tagCodeArea').hide();
	}
	function myFocus(domObj) {
		var $jq, aaa, target;
		// var isJqueryObject = domObj instanceof jQuery;
		// console.log('is jquery ? ' + isJqueryObject);
		removeFocus();

		if (domObj) {
			domObj.focus();
			window.setTimeout(function() {
				try {
					domObj.select();
				} catch (ee) {
				}
			}, 1);

			currFocusedDom = domObj;
			$jq = $(domObj);
			$jq.addClass('myFocusHint');
			$jq.closest("td").addClass('myFocusRowHint');// .get(0).scrollIntoView();
			// $jq.prev().css('background-color', 'red');

			$('#' + domObj.id + ' ~ .tagCodeArea').show();
			// ifx.scrollFieldInToView($(domObj));

		} else {
			console.error('null swift dom object');
		}
	}

	function prevField() {
		ifx.errmsg();
		var flds = getInputs();
		if (flds[curr]) {
			convertToSwiftFormat(flds[curr].id);
		}
		displaySwiftText();
		curr--;

		if (curr < 0) {
			curr = -1;
			return 'LEAVE SWIFT';
		}

		myFocus(flds[curr]);
	}
	function validated(oInput) {
		ifx.errmsg();
		if (!oInput)
			return true;
		var $currentField = $(oInput);
		// var oTag = getTagByDomId(oInput.id);
		var oTag = _m.get(oInput.id);
		if (!oTag) {
			alert('find no tag for id:' + oInput.id);
			return false;
		}
		dumpTag(oTag);
		var value = oInput.value || '';
		// oInput.value = value = value.toUpperCase();
		// check mandaroy
		if (!validateRequired(oTag, $currentField, value)) {
			if (oTag['$'].options) {
				return validateOptions(oTag, $currentField, ' ');
				// return false;
			}
			ifx.errmsg("Required Field !!", $currentField);
			return false;
		}
		if (oTag['$'].options) {
			return validateOptions(oTag, $currentField, value);
			// return false;
		}

		// if (!checkProhibitedChars(oTag, $currentField, value))
		// return false;

		if (oTag['$'].fval) {

		} else {
			if (!baseValidator(oTag, $currentField, value))
				return false;
		}

		// if (oTag['$'].fval2) {
		// return runFVAL2(oTag, $currentField, value);
		// }
		return true;
	}

	function skipEmpty_OptionalField(oTag, value) {
		return (!(oTag.isMandatory()) && !value);
	}

	// fval_59_0
	// fval_59A_0
	// fval_59A_1

	function baseValidator(oTag, $currentField, value) {
		var tag, fn, fnName;
		// var context = oTag['$'].context;
		if (skipEmpty_OptionalField(oTag, value))
			return true;

		Swift.validators.setCurrentField(oTag, $currentField, value, _m);
		tag = oTag.getTagName();

		// find fval by full tag name
		fnName = 'fval_' + tag;
		fn = Swift.validators['rtn'][fnName];
		if (fn) {
			console.log('tag:' + tag + ", using fn:" + fnName);
			return fn();
		}
		// for fval by tag name, fval_59A or fval_59
		var ff = tag.split('_');
		fnName = 'fval_' + ff[0];
		fn = Swift.validators['rtn'][fnName];
		if (fn) {
			console.log('tag:' + tag + ", using fn:" + fnName);
			return fn();
		}
		// final, find fval by tag(0,2): fval_59
		fnName = 'fval_' + tag.slice(0, 2)
		fn = Swift.validators['rtn'][fnName];
		if (fn) {
			console.log('tag:' + tag + ", using fn:" + fnName);
			return fn();
		}

		throw new Error("Programmer's bug!! No such function:" + "fval_" + tag);

	}

	function runFVAL2(oTag, $currentField, value) {
		var rtnName = oTag['$'].fval2;
		// alert('running ' + rtnName + ' for tag:'+oTag['$'].tag);

		if (rtnName[0] == '@') {
			if (rtnName.indexOf('swift_fn_HR010') != -1) {
				function onOK() {
					Swift.validators.goNext();
				}
				function onError(errmsg, type) {
					if (type == 'W') {
						Swift.validators.goNext();
						return;
					}
					Swift.validators.myError(9998, errmsg);
				}
				Swift.validators
						.trigger(rtnName.slice(1), value, onOK, onError);
				return false;
			} else {
				return Swift.validators.trigger(rtnName.slice(1), value);
			}
		}

	}

	// var prohbString = ";{}~`!@#$%^&*_=[]\|><";
	var reProhbString = /[;{}~`!@#$%^&*_=\[\]\\|><]/;
	// var prohbString2 = ".:";
	var reProhbString2 = /[.:]/;

	var prohbErrmsg = "不允許之特殊符號包括(;{}~`!@#$%^&*_=[]\|><)或 .: ";
	function checkProhibitedChars(oTag, $currentField, value) {
		// 內有不允許之特殊符號(;{}~`!@#$%^&*_=[]\|><)或 .: ",
		var k, msg;
		if (value) {
			k = value.match(reProhbString);
			if (k == null) {
				k = value.match(reProhbString2);
			}
			if (k != null) {
				msg = '輸入資料之第' + (k.index + 1) + "位 [" + k[0] + "]為不允許符號<br/>"
						+ prohbErrmsg;
				ifx.errmsg(msg, $currentField);
				return false;
			}
		}
		return true;
	}

	function validateRequired(oTag, $currentField, value) {
		if (value.length == 0 && oTag['$'].status == 'M'
				&& oTag['$'].tag != "77E_0") { // 103.03.10小柯改 MT198 77E 特例
			return false;
		}
		return true;
	}
	function validateOptions(oTag, $currentField, abc, optionFieldValue,
			fnCallBack) {
		abc = abc.toUpperCase();
		var oo = oTag['$'].options.split(',');
		if (abc && oo.indexOf(abc) == -1) {
			ifx.errmsg('please select ' + oTag['$'].options, $currentField);
			return false;
		}

		// oTag.setValDone(abc);

		$currentField.val(abc);
		if (oTag.currOpt != abc) {
			var containerId = 'div' + $currentField.attr('id');
			drawOpt(containerId, oTag, abc, optionFieldValue, fnCallBack);
		}
		oTag.currOpt = abc;
		return true;
	}

	function getTagByDomId(id) {
		console.log('dom id:' + id);

		// id.replace(SWIFT_FIELD_PREFIX, '');
		var ss = id.split('_');
		ss.shift();
		ss.shift();
		var tagIndex = parseInt(ss.shift(), 10);
		var oTag = mt.tags[tagIndex];
		if (ss.length == 0)
			return oTag;

		if (oTag['$'].type == 'loop') {
			ss.shift();
			var loopIndex = parseInt(ss.shift(), 10);
			oTag = oTag.tags[loopIndex];
		}

		if (oTag['$'].options && ss.length > 0) {
			return idMapTag.get(id);

			// var optTagName = $('#'+id).attr('data-tag');
			// console.log('option:' + optTagName);
			// return oTag['opt'][ optTagName ];
		}

		return oTag;

	}

	function dumpTag(o) {
		var s = JSON.stringify(o, null, 2);
		console.log(s);
	}
	function goCurrentField() {
		var flds = getInputs();
		myFocus(flds[curr]);
	}
	function leaveSwiftForm(direction) {
		removeFocus();
		if (direction == undefined)
			direction = 'up';
		if (direction == 'up') {
			curr = -1;
		} else { // down
			var flds = getInputs();
			curr = flds.length;
		}
	}

	function goSwiftForm(direction, mode, fn) {
		var retval = undefined;
		direction = direction || 'forward';
		if (direction == 'forward')
			retval = nextField(true, mode, fn);
		else if (direction == 'current') {
			goCurrentField();
		} else {
			retval = prevField();
		}
		console.log('leave goSwiftForm(), direction:' + direction + ', curr:'
				+ curr);
		return retval;
	}

	// ------------------------------------
	// SWIFT TITA
	// -----------------------------------
	function convertToSwiftFormat(id) {
		_m.get(id).dump();
		var $fld = $('#' + id), value = $fld.val(), tagName = _m.get(id)
				.getTagName();

		if (_m.get(id).isOptions())
			return null;

		value = $.trim(value);
		if (!_m.get(id).isMandatory() && !value) {
			// return storeSwift('','');
			$fld.attr('data-swift-tag', ''); // data-swift-tag == ''時,
			// 電文排除此tag
			console.log('an empty tag:' + tagName);
			return;
		}

		// return storeSwift(":" + tagName + ":" + value);

		console.log('store ' + id + ', tag:' + tagName + '=>' + value + '<<<');
		$fld.attr('data-swift-tag', tagName);
		$fld.attr('data-swift-text', value);
		console.log('data-swift-tag:' + $fld.attr('data-swift-tag'));
		console.log('data-swift-text' + $fld.attr('data-swift-text'));

	}

	function displaySwiftText(bName) {
		var t = buildSwiftTim(bName);
		$('#' + SWIFT_TEXT).html("<pre>" + t + "</pre>");
		$('#' + SWIFT_TEXT_COUNTS).text(t.length);
		var rest = SWIFT_MAX_BUF - t.length;
		$('#' + SWIFT_TEXT_REST).text(rest);
		$('#swift_text_counts_line').show();
		// setTimeout(repositionSwiftTextPanel, 10);
		// 小柯測試 !!!! START
		// var swiftDiv = document.getElementById("swift_text_panel");
		// //即時變更到最後(1)
		// swiftDiv.scrollTop = swiftDiv.scrollHeight;
		// $("#swift_text_panel").scrollTop($("#swift_text_panel")[0].scrollHeight);
		// //即時變更到最後(2)
		$("#swift_text_panel").animate({
			scrollTop : $('#swift_text_panel')[0].scrollHeight
		}, 900); // 動畫
		// 小柯測試 !!!! END
	}

	function replaceTT(s) {
		var r, re; // Declare variables.
		re = /\n/g; // Create regular expression pattern.
		r = s.replace(re, "\r\n"); // Replace "\r\r\n" with "\n".

		return (r); // Return string with replacement made.
	}

	function printSwift(prefix) {

		var t = buildSwiftText(null, true);
		var tt = t.split('\r\n');
		tt = $.map(tt, function(x) {
			return prefix + x;
		});
		return tt.join('\r\n');
	}
	function buildSwiftTim(bName) {
		var t = buildSwiftText(bName);
		return buildSwiftHeader() + t + buildSwiftTail();
	}
	function buildSwiftText(bName, bPrint) {
		// var flds = getInputs(); //小柯
		var flds = getAllSwiftInputs();// getInputs(); //小柯
		var x, $x;
		var arr = [], s = null;
		var tag, value;
		for (var i = 0; i < flds.length; i++) {
			x = flds[i];
			$x = $(x);
			if (x.id.indexOf('-sub-') != -1) { // complex field
				i += (processSub(x.id, i) - 1); // processSub回傳sub數目,
				// -1讓i指向最後sub之最後一筆

			} else {
				tag = $x.attr('data-swift-tag');
				if (tag) {
					if (bPrint) {
						s = printSwiftTag(tag, (_m.get(x.id)).getName(), $x
								.attr('data-swift-text'));
					} else {
						s = formatSwiftTag(tag, $x.attr('data-swift-text'));
					}
					arr.push(s);
				}
			}

		}

		var tt = arr.join('\n') + '\n';
		tt = replaceTT(tt);
		console.log(tt);
		return tt;

		function formatSwiftTag(tag, text) {
			return ":" + tag + ":" + text;
		}
		function printSwiftTag(tag, name, text) {
			var s = ":" + tag + ":";
			if (s.length == 4)
				s += ' ';
			s += (name + '\n');

			var tt = text.split('\n');
			$.each(tt, function(i, t) {
				s += ('     ' + t);
				if (i < tt.length - 1)
					s += '\n';
			});

			return s;
		}
		function processSub(id, startIndex, nameWanted) {
			var sub = [], ss = id.split('-'), counts = ss.pop(), parentName = (_m
					.get(ss[0])).getName(), idx = 0;
			var x, $x;
			var tag, parentTag = null;
			var tt;
			while (idx < counts) {
				x = flds[startIndex + idx];
				$x = $(x);
				tag = $x.attr('data-swift-tag');
				if (tag) {
					tt = tag.split('_');
					parentTag = tt[0];
					sub.push($x.attr('data-swift-text'));
				}
				idx++;
			}
			if (parentTag) {
				if (bPrint) {
					tt = printSwiftTag(parentTag, parentName, sub.join('\n'));
				} else {
					tt = formatSwiftTag(parentTag, sub.join('\n'));
				}
				arr.push(tt);
			}
			return counts;

		}
	}

	function buildSwiftHeader() {
		var sendingTid = _Vars['sendingTid'].slice(0, 12), msgType = _Vars['msgType'], pri = _Vars['pri'], dest = _Vars['dest'], s = '';

		s = "{1:F01" + sendingTid + ".SN..ISN..}{2:I"
				+ (msgType == '103+' ? '103' : msgType);

		dest = (dest.length == 8) ? dest + "XXXX" : dest.substr(0, 8) + "X"
				+ dest.substr(8, 3);

		s += (dest + pri + "}");
		if (msgType == "103+") {
			s += "{3:{119:STP}}";
		}
		s += "{4:\r\n";
		return s;
	}

	function buildSwiftTail() {
		var msgTypeCategory = _Vars['msgType'][0];
		return (msgTypeCategory == '3' || msgTypeCategory == '9') ? "-}"
				: "-}{5:{MAC:FFFFFFFF}}";
	}

	// ------------------------------------
	// end of SWIFT TITA
	// -----------------------------------

	// /////////////////////////////////////////////////////////////////////////////////////////
	// 應將下面定義改放置於swift-dd.xml
	var verifyFields = new Array("19", "19A", "19B", "32A", "32B", "32C",
			"32D", "32F", "32G", "32H", "32J", "32K", "32M", "32N", "32P",
			"32U", "33A", "33B", "33C", "33D", "33E", "33F", "33G", "33K",
			"33N", "33P", "33R", "33S", "33T", "33V", "34A", "34B", "34C",
			"34D", "34E", "34F", "34G", "34H", "34N", "34P", "34R", "39P",
			"60F", "60M", "62F", "62M", "64", "65", "71E", "71F", "71G", "71H",
			"71J", "71K", "71L", "90A", "90B", "90C", "90D");
	var moneyFields = verifyFields;
	var prnMoneyFields = moneyFields;

	function mapFromVar(theIfx, pairList) {
		// _Vars['sendingTid'] = $.trim(ifx.getValue('#SENDINGTID'));
		// _Vars['msgType'] = $.trim(ifx.getValue('#MSGTYPE'));
		// _Vars['pri'] = $.trim(ifx.getValue('#PRI'));
		// _Vars['dest'] = $.trim(ifx.getValue('#DEST'));
		// sendingTid=#SENDINGTID;msgType=#MSGTYPE;pri=#PRI;dest=#DEST
		var pp = pairList.split(';');
		$.each(pp, function(i, x) {
			var xx = x.split('=');
			_Vars[$.trim(xx[0])] = $.trim(theIfx.getValue(xx[1]));
		});
	}
	function mapToVar() {
		var flds = getInputs();
		var bagName2VarNameMap = {
			'trn' : '#swiftTRN',
			'valday' : '#swiftVALDAY',
			'money' : {
				'cur' : '#swiftCURAMT',
				'amt' : '#swiftXAMT'
			}
		};

		// ifx.setValue("#swiftMsgType",mt['$'].type);
		ifx.setValue("#swiftMsgName", mt['$'].name);

		ifx.setValue(getVar('trn'), getBag('trn', '').value);
		ifx.setValue(getVar('valday'), getBag('valday', '0').value);

		var moneyTag = getBag('money', '');
		var moneyType = getVar('money');
		var curAmt = parseMoney(moneyTag.tag, moneyTag.value);
		ifx.setValue(moneyType['cur'], curAmt[0]);
		ifx.setValue(moneyType['amt'], curAmt[1]);

		// #swiftWholeData=X,10000,s
		// #swiftData=X,1000,s
		// #swiftMsgLen=9,5,s
		var t = buildSwiftTim('');
		ifx.setValue('#swiftWholeData', t);
		ifx.setValue('#swiftData', t);
		ifx.setValue('#swiftMsgLen', t.length);

		function parseMoney(oTag, value) {
			// 32A "len": "24","component": "(Date)(Currency)(Amount)",
			// 32B "len": "18", "component": "(Currency)(Price)",
			// TODO: hard code now
			if (!value)
				return [ '', '' ];

			switch (oTag.getLength()) {
			case 24:
				value = value.substr(6);
				break;
			default:
				break;
			}

			var cur = value.slice(0, 3);
			var amt = value.substr(3);
			return [ cur, amt ];
		}
		function getVar(k) {
			return bagName2VarNameMap[k];
		}
		function getBag(bagName, defaultValue) {
			var tag;
			var result = {
				value : defaultValue
			};
			$.each(flds, function(i, x) {
				tag = _m.get(x.id);
				if (tag.bag() == bagName) {
					result.value = tag.getValue();
					result.tag = tag;
					return false;
				}
			});
			return result;
		}
	}

	function putAllValues(text, valueObjects, bDisableAll) {
		// var valueObjects = parseSwiftText(text);
		var flds = getInputs(), map = {}, $x, s, oTag;

		if (bDisableAll) {
			flds.prop("disabled", true);
			$('.button', $('#' + SWIFT_FORM)).css('visibility', 'hidden'); // preserve
			// spaces
			// $('.button', $('#' + SWIFT_FORM)).remove(); //
		}

		$.each(flds, function(i, x) {
			$x = $(x);
			console.log($x.attr('id') + '=>' + $x.attr('data-tag'));
			if ($x.attr('data-tag')) {
				map[$x.attr('data-tag')] = map[$x.attr('data-tag')] || []; // if
				// empty
				// array,
				// create
				// []
				map[$x.attr('data-tag')].push($x);
			}
		});

		$.each(valueObjects, function(i, v) {
			s = v.lines.join('\r\n');

			if (v.tag.length == 3) { // options field
				$x = getMappedValueObject(v);
				if ($x) {
					var abc = v.tag.slice(2);
					validateOptions(_m.get($x.attr('id')), $x, abc, s);
				}
			} else {
				oTag
				$x = getMappedValueObject(v);

				if ($x) {
					$x.val(s);
				}
			}

		});

		setTimeout(function() {
			removeFocus();
			$('#' + SWIFT_TEXT).html("<pre>" + text + "</pre>");
			$('#' + SWIFT_TEXT_COUNTS).text(text.length);
			var rest = SWIFT_MAX_BUF - text.length;
			alert(rest);
			$('#' + SWIFT_TEXT_REST).text(rest);
			// setInterval(repositionSwiftTextPanel,3000);
		}, 10);

		function getMappedValueObject(vo) {
			var n = vo.tag.slice(0, 2);
			if (vo.idx != undefined) {
				return map[n][vo.idx];
			} else {
				return map[n][0];
			}
		}
	}
	function deserialize(aMT, text) {

		var valueObjects = parseSwiftText(text);
		var countsMap = {};
		$.each(valueObjects, function(i, x) {
			var n = x.tag.slice(0, 2);
			if (countsMap[n] == undefined)
				countsMap[n] = 0;
			countsMap[n] = countsMap[n] + 1;
		});

		var n2;
		var loopTimes = []; // 計算單一Loop各欄位之筆數

		// begin $.each(aMT.tags,
		$.each(aMT.tags, function(i, x) {
			if (x['$'].type == 'loop') {
				loopTimes = [];
				var firstM = null;
				$.each(x.tags, function(j, y) {
					n2 = y['$'].tag.slice(0, 2);
					if (y['$'].status == 'M' && !firstM)
						firstM = n2;

					loopTimes.push(countsMap[n2] || 0);
				});
				var max = 0; // 0 -->這組loop沒用到
				if (loopTimes)
					max = maxArray(loopTimes); // 最大值代表畫面要出現幾個這組loop
				if (max > 0) {
					console.log('total ' + max + ' loops');
					x.times = max; // drawLoop會依據times繪製times組loop
				}
				var loopTagNames = $.map(x.tags, function(y) {
					return y['$'].tag.slice(0, 2);
				});

				var idx = 0;
				var anchors = []; // firstM (status 'M')之索引值
				var nonAnchors = [];
				$.each(valueObjects, function(k, z) {
					n2 = z.tag.slice(0, 2);
					if (loopTagNames.indexOf(n2) != -1) {
						if (n2 == firstM) {
							z.idx = idx;
							idx = idx + 1;
							anchors.push(k);
						} else {
							nonAnchors.push({
								vIndex : k,
								vObj : z
							});
						}
					}
				});

				var mIndex = loopTagNames.indexOf(firstM);
				var i3;
				var i4;
				$.each(nonAnchors, function(j, y) {
					n2 = y.vObj.tag.slice(0, 2);
					i3 = loopTagNames.indexOf(n2);
					if (i3 < mIndex) { // before anchor
						i4 = findBeforeAnchor(y.vIndex);
					} else { // after anchor
						i4 = findAfterAnchor(y.vIndex);
					}
					if (i4 != -1)
						y.vObj.idx = i4;
				});

				// vIndex在anchors之前
				function findBeforeAnchor(vIndex) {
					var theIdx = -1;
					$.each(anchors, function(i, x) {
						if (x > vIndex) {
							theIdx = i; // 此欄位屬於loop index i
							return false;
						}
					});
					return theIdx;
				}
				function findAfterAnchor(vIndex) {
					for (var i = anchors.length - 1; i >= 0; i--) {
						if (anchors[i] < vIndex)
							return i;
					}
					return -1;
				}

			}
		});
		// end $.each(aMT.tags,

		$.each(valueObjects, function(i, x) {
			console.log(x.tag + "==>" + x.idx);
		});
		return valueObjects;

		function maxArray(array) {
			return Math.max.apply(Math, array);
		}
		;
	}

	function parseSwiftText(text) {
		console.log('text len:' + text.length);
		text = text.replace(/[\r]/g, "");
		console.log('text len:' + text.length);
		var START = '}{4:\n', STOP = '\n-}{5:{', i = text.indexOf(START), j = text
				.indexOf(STOP), swiftText;
		if (i != -1 && j != -1) {
			swiftText = text.slice(i + START.length, j);
			var ss = swiftText.split('\n');
			var r = [], tmpResult = null, t, tt;

			for (i = 0; i < ss.length; i++) {
				if (ss[i][0] == ':') {
					if (tmpResult)
						r.push(tmpResult);
					t = ss[i].slice(1);
					tt = t.split(':');
					tmpResult = {
						tag : tt[0],
						lines : [ tt[1] ]
					};
				} else {
					tmpResult.lines.push(ss[i]);
				}
			}
			if (tmpResult)
				r.push(tmpResult);
			$.each(r, function(i, x) {
				console.log('-----------');
				console.log(x.tag);
				console.log(x.lines.join('\r\n'));
			});

			return r;
		}
	}

	function deserialize2(aMT, text) {
		var valueObjects = parseSwiftText(text);
//		$.each(valueObjects, function(i, x) {
//			console.log(x.tag + "==>" + x.idx);
//		});
		return valueObjects;
	}
	function putAllValues2(cmd, text, valueObjects, protectedTags, fnCallback) {
		setTimeout(function() {
			$('#' + SWIFT_TEXT).html("<pre>" + text + "</pre>");
			$('#' + SWIFT_TEXT_COUNTS).text(text.length);
			var rest = SWIFT_MAX_BUF - text.length;
			$('#' + SWIFT_TEXT_REST).text(rest);
			// $('#swift_text_counts_line').show();
		}, 10);

		var currItem = 0, // 第幾個SWIFT電文Tag
		$x;
		
		try {
			$.each(mt.tags, function(i, x) {
				deserializeTag(x);
			});
		} catch (ee) {
			if (ee !== "no more")
				throw new Error("failed to desearialize swift text");
		}

		function disableSome() {
			var bDisableAll = (protectedTags == null || protectedTags === '*'), flds = getInputs(), arrProtectedTags, aTag, $fld;

			if (bDisableAll) {
				$('.button', $('#' + SWIFT_FORM)).css('visibility', 'hidden'); // preserve
				// spaces
				flds.prop("disabled", true);
			} else {
				arrProtectedTags = splitBySize(protectedTags, 3);
				$.each(flds, function(i, dom) {
					$fld = $(dom)
					aTag = _m.get(dom.id);
					aTag.setValDone($fld.val()); // TODO: tag定義應增加設定,
					// 讓tag在反序列化之後 不設valdone
					convertToSwiftFormat(dom.id);
					if ($.inArray(aTag.getTagName(), arrProtectedTags) != -1) {
						$fld.prop("disabled", true);
					}
				});

			}
		}
		
		function disableAllButMoney() {
			var flds = getInputs(), aTag, $fld;

			$('.button', $('#' + SWIFT_FORM)).css('visibility', 'hidden'); // preserve
			// spaces
			flds.prop("disabled", true);
			$.each(flds, function(i, dom) {
				$fld = $(dom)
				aTag = _m.get(dom.id);
				// aTag.setValDone($fld.val()); // TODO: tag定義應增加設定, 讓tag在反序列化之後
				// 不設valdone
				// convertToSwiftFormat(dom.id);

				if (aTag.$.bag === "money" && $fld.val() != "") { // 有輸入資料的,才需要審核
					$fld.prop("disabled", false);
					aTag.$.valueToVerify = $fld.val();
					aTag.verified = false; // 先設定此欄位 檢核未通過
					console.log(aTag.$.tag + ", valueToVerify:"
							+ aTag.$.valueToVerify);
					$fld.val('');
				}
			});
			displaySwiftText = function() {
			}; // erase displaySwiftText function
			var oldValidateCurrentField = validateCurrentField;
			validateCurrentField = function(valWanted, dom) {
				if (!oldValidateCurrentField(valWanted, dom, false))
					return false;
				// return true;
				var aTag = _m.get(dom.id);
				var $fld = $(dom);
				aTag.verified = false;
				if (aTag.$.valueToVerify == $fld.val()) {
					aTag.verified = true;
					return true;
				} else {
					$fld.blur();
					// alert("not match:" +aTag.$.valueToVerify );
					var r = confirm("輸入資料與原電文內容不符! 停止審核電文?\n\n[確定] 不繼續審核    [取消] 重新輸入")
					if (r == true) { // click [確定]
						setTimeout(function() {
							ifx.jumpToAfterSwiftForm();
						}, 1);
						return false;
					} else { // ESC or click [取消]
						$fld.focus();
						return false; // entry again
					}
				}

			};

		}
		setTimeout(function() {
			removeFocus();
			ifx.resizeSwift();
			curr = -1; // reset curr field index;

			if (cmd == 'edit')
				disableSome();
			else if (cmd == 'verify')
				disableAllButMoney();

			
			if (fnCallback)
				fnCallback();

		}, 7234);

		function splitBySize(text, size) {
			var arr = [], t;
			for (var i = 0; i < text.length; i += size) {
				t = $.trim(text.slice(i, i + size));
				if (t)
					arr.push(t);
			}
			return arr;
		}
		function deserializeTag(oTag) {
			if (oTag['$'].type == 'loop') {
				var x2  = $.extend({}, oTag); // clone a new one
				deserializeLoop(x2);
			} else {
				if (oTag['$'].options) {
					deserializeOptions(oTag);
				} else {
					if (oTag['$'].complex) {
						deserializeComplexField(oTag);
					} else {
						deserializeSimpleField(oTag);
					}
				}
			}
		}

		function getTagItem(tagName) {
			if (currItem >= valueObjects.length) {
				throw "no more";
			}
			if (tagName == valueObjects[currItem].tag) {
				return valueObjects[currItem++];
			}
			return false;
		}
		function peekTagItem() {
			return valueObjects[currItem];
		}

		function deserializeLoop(loopTag) {
			// drawOneLoop(x, i)
			var key = 'tblLoop' + loopTag.id;
			loopTag.container = _loopMap[key].container; // 這個loopTag裁示畫面上使用的
			
			
			var savedCurrItem = -1;
			var maxLoop = loopTag.$.count || 15; // Number.MAX_VALUE;

			for (var i = 1; i <= maxLoop; i++) {
				savedCurrItem = currItem; // save curr valueObj pointer
				// add loop field controls to screen
				if (i > 1){
					addLoopControls();
				}
				// desearialize loop tags
				try {
					$.each(loopTag.tags, function(i, x) {
						deserializeTag(x);
					});
				} catch (ee) {
					if (ee !== "no more")
						throw new Error("failed to desearialize swift text");
				}
				if (savedCurrItem == currItem) { // currItem未能前進,
					// 表示已無此Loop電文TAG
					// 按一下DELETE
					var aContainer = loopTag.container;
					var aId = loopTag.id;
					
					setTimeout(function(){
						var nRows = getLoopRows(aContainer).length;
						var loopIndexPrefix = aId + "_loop_" + (nRows - 1); // 參考drawOneLoop
						var btnDel = '#btnLoopDel' + loopIndexPrefix;
							
						console.log('******* click ' + btnDel);
						$(btnDel).trigger('click'); // 觸發 click	
					},888); // 因為drawOneLoop在10 ms(search point:timeoutDeleteLoop)後才註冊click event,  20 ms就夠
					break;
				}
			}

			function addLoopControls(row) {
//				drawOneLoop(loopTag, row);
				var rowCount = getLoopRows(loopTag.container).length;
				if (rowCount < maxLoop) {
					drawOneLoop(loopTag, rowCount);
					// ifx.resizeSwift();
				}
			}

		}

		function deserializeOptions(oTag) {
			var peekItem = peekTagItem();
			if (!peekItem || peekItem.tag.slice(0, 2) != oTag.$.tag.slice(0, 2))
				return;
			var oItem = getTagItem(peekItem.tag);
			var abc = oItem.tag.slice(2);

			var optTag = oTag.opt[peekItem.tag];
			var savedId = oTag.id; // save option's id of loop
			validateOptions(oTag, $('#' + oTag.id), abc, function() {
				// 因為在反序列化時, oTag是取自mtxxx.xml, Loop 之option tag(a,b,c tag)尚無dom
				// id
				// 在呼叫validateOptions()-->drawOpt之後才會產生dom id
				// 因此, 改從option tag之container取得輸入欄位之dom id(s), 再依序放術optTag.tags
				var containerId = 'div' + savedId; // We need actual DOM id for
				console.log("@# conatiner:"+containerId);
				// Loop tag
				var flds = getInputs(containerId); // get real DOM id from
				console.log("@# conatiner flds:"+flds.length);

				if (optTag.$.complex) {
					$.each(flds, function(i, x) {
						optTag.tags[i].id = x.id;
					});
					deserializeComplexField(optTag, oItem);						
				} else {
					optTag.id = flds.get(0).id;
					deserializeSimpleField(optTag, oItem);	
				
				}
			});
		}

		function deserializeSimpleField(oTag, oItemPassed) {
			var oItem = oItemPassed != undefined ? oItemPassed
					: getTagItem(oTag.$.tag);
			if (!oItem)
				return;
			
			var $x = $('#' + oTag.id);
			$x.val(oItem.lines.join('\n'));
		}

		function deserializeComplexField(oTag, oItemPassed) {
			var oItem = oItemPassed != undefined ? oItemPassed
					: getTagItem(oTag.$.tag);
			var $x;
			if (!oItem)
				return;

			console.log("deserializeComplexField, tag:" + oTag.$.tag);
			// if(oTag.$.deserialize === 'reverse'){
			if (oTag.$.tag[0] == "4") { // 似乎4開頭之tag是採用這個模式
				console.log("using reverse seralizer");
				$x = $('#' + oTag.tags[1].id); // second tag
				$x.val(oItem.lines.pop()); // put last line to second tag

				$x = $('#' + oTag.tags[0].id); // first tag
				$x.val(oItem.lines.join('\n')); // put rest of lines to first
				// tag
				return;
			}

			// 貌似tag 5xx都是這個模式
			if (oItem.lines[0][0] == '/') {
				$x = $('#' + oTag.tags[0].id);
				$x.val(oItem.lines.shift());
			}
			$x = $('#' + oTag.tags[1].id);
			$x.val(oItem.lines.join('\n'));
		}
	}

	function getStatus(whatStatus) {
		var retval = '0';
		switch (whatStatus.toLowerCase()) {
		case 'verify':
			retval = getVerifyStatus();
			break;
		}
		return retval;
	}
	function getVerifyStatus() {
		var flds = getInputs(), aTag, allVerified = true;

		$.each(flds, function(i, dom) {
			aTag = _m.get(dom.id);
			if (aTag.$.bag === "money" && aTag.verified == false) {
				allVerified = false;
				return false;
			}
		});
		return allVerified ? "1" : "0";
	}

	function setIfxValueByName(name, value) {
		if (ifx.hasField(name)) {
			ifx.setValue(name, value);
		}
	}

	function batchPrint(aIfx, swift_direction, oFromSwift, msgType, data,
			prefix, callback) {
		ifx = aIfx;
		Swift.util.load($.trim(msgType)).fail(function(x) {
			callback(x);
		}).done(function(mt) {
			callback(null, print_with(mt));
		});

		function print_with(mt) {
			setIfxValueByName("#swiftMsgName", mt['$'].name);
			// var content = mergeMtWithTota(mt, data, prefix);
			var content = Swift.print.merge(swift_direction, oFromSwift, mt,
					data, prefix);
			return content;

		}
	}
	// move to swift-print.js
	/*
	 * function mergeMtWithTota(mt, tota, prefix) { var r = "hello"; var
	 * currTagIndex = 0; var ss; var SPACES5 = ' '; var lines =
	 * toNewLine(tota).split('\n'); prefix = (prefix===undefined) ? '':prefix;
	 * lines.shift(); // we don't need first line lines.pop(); // last line,
	 * either var arr = $.map(lines, function (x) { if (x[0] == ':') { ss =
	 * x.split(':'); ss.shift(); ss[0] = prefix + getTagName(ss[0]); ss[1] =
	 * prefix + SPACES5 + ss[1]; return ss.join('');
	 *  } else { return prefix+SPACES5 + x; } }); r = arr.join('\n'); return r;
	 * 
	 * function getTagName(tag) { var name = "name " + tag; var oTag; for (var i =
	 * currTagIndex; i < mt.tags.length; i++) { oTag = mt.tags[i]; if
	 * (oTag.$.type == "loop") { name = checkLoopTag(oTag, tag); if (name !=
	 * null) { break; } } else { name = checkSimpleTag(oTag, tag); if (name !=
	 * null) { currTagIndex++; break; } } } if (name == null) name = '';
	 * 
	 * return ':' + tag + ":" + ((tag.length == 2) ? ' ' : '') + name + "\n"; }
	 * 
	 * function checkSimpleTag(oTag, tag) { if (oTag.$.options) { return
	 * checkOptionTag(oTag, tag); } else if (oTag.$.tag == tag) { return
	 * oTag.$.name; } return null; }
	 * 
	 * function checkOptionTag(oOptionTag, tag) { for (var k in oOptionTag.opt) {
	 * if (tag == k) { return oOptionTag.$.name; } } return null; } function
	 * checkLoopTag(oLoopTag, tag) { var oTag, name; for (var i = 0; i <
	 * oLoopTag.tags.length; i++) { oTag = oLoopTag.tags[i]; name =
	 * checkSimpleTag(oTag, tag); if (name != null) return name; } return null; }
	 * 
	 * function toNewLine(s) { var r, re; // Declare variables. re =
	 * /\x0d\x0a/g; // Create regular expression pattern. r = s.replace(re,
	 * "\n"); // Replace "\r\r\n" with "\n". return (r); // Return string with
	 * replacement made. } }
	 */

	function tagValue(tagName, value){
		var f = $('#'+SWIFT_FORM).find("[data-tag='" + tagName + "']");
		if(f.length > 0) {
			if(value==null){
				console.log("get value tag "+tagName);
				return f.val();
			}else {
				console.log("set value " + value + " to tag:"+tagName);
				f.val(value);
			}
		}else {
			console.log("Failed to find tag:"+tagName);
		}
		return null;
	}
	
	return {
		display : display,
		goSwiftForm : goSwiftForm,
		leaveSwiftForm : leaveSwiftForm,
		nextFieldNoVal : nextField,
		externalSetValDone : externalSetValDone,
		mapToVar : mapToVar,
		mapFromVar : mapFromVar,
		deserialize : deserialize2,
		putAllValues : putAllValues2,
		printSwift : printSwift,
		getStatus : getStatus,
		batchPrint : batchPrint,
		tagValue:tagValue

	};
}(jQuery));
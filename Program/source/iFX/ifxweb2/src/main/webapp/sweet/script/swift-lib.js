/*備註:
xml 定義 aTag.$.special =1 則當作一般欄位,在審核時不特別可輸入
*/
var Swift = Swift || {};
var CKNEWLINE = "\r\n";
(Swift.util = function($) {
	var _cfg = {
		logDiv : 'results',
		contentBasePath : './sweet/swift/def/mt-converted/',
		basePath : './mvc/hnd/swift/mt/'
	};

	var _cache = {};
	var _json_h_obj = {};
	function getFromCache(mtName) {
		var o = _cache[mtName];
		if(o==null) return null;
		return $.extend({}, o);
	}
	function load(mtName) {
		var deferred = null;
		var oCachedMt = getFromCache(mtName);
		if (oCachedMt) {
			deferred = new $.Deferred();
			setTimeout(function() {
				deferred.resolve(oCachedMt);
			}, 1);
			return deferred.promise();
		}

		var url = _cfg.basePath + 'mt' + mtName;// + '.txt';
		return $.ajax({
			type : "GET",
			cache : false,
			url : url,
			dataType : "json"
		}).then(function(data) {
			_cache[mtName] = preProcess(data);
			return getFromCache(mtName);
		}, function(err) {
			return err;
		});
	}
	function preProcess(mt){
		function addLoopBox(n, tag){
			$.each(tag.tags, function(i, x) {
				if (x['$'].type == 'loop') {
					x.box = n + '_' + i;
					addLoopBox(x.box, x);
				}
			});
		}
		addLoopBox("loop", mt);
		return mt;
	}
	function init(opt) {
		_cfg = $.extend(_cfg, opt);
	}
	function assert(value, desc) {
		var doc = document,li = doc.createElement("li"); // 小柯增效能
		li.className = value ? "pass" : "fail";
		li.appendChild(doc.createTextNode(desc));
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
	var SWIFT_MAX_BUF = 10000; //TODO 確認總長多少??
	var SWIFT_TEXT_TITLE = 'SWIFT電文';

	var _Vars = {};
	var _loopMap = {};
	var _doReplace = false;

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
	_tagFn.getvalidate = function() {  //新增function
		return this['$'].validate;
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
		console.log('set ' + this.id + ' valdone;' + value);
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
		//應該是 趙大哥提的?
		return this.value == this.value;  //小柯  更改每次都進入欄位  this.value != newValue;
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
		setTimeout(resetLoopButtonsHandlers,0);
		// repositionSwiftTextPanel(true);

		// 必須等待畫面產生完後, 才呼叫fnCallback, 否則以非同步產生之部份畫面會產生詭異效果
		//
		setTimeout(function() {
			displaying = false;
			if (fnCallback) {
				fnCallback();
			}
		}, 50);

    //這裡不能 mousedown 不然會有一些小問題
    //2016/04/11 增加select
		$main.delegate(".swiftField", "click paste select", function(evt) {
			console.log("EVENT " + evt.type.toUpperCase() + " on "
					+ $(this).attr('id'));
			//if (currFocusedDom == null || currFocusedDom != this)

			//回傳值, for 0長度欄位的問題?
				if(!fieldClicked($(this))){
						evt.preventDefault();
				}
		});

		function fieldClicked($target) {
			var targetId = $target.attr('id');
			console.log('click swift form field, target:' + targetId);

			if (ifx.isSwiftMode()) { // in swift fields
				console.log('from swift to swift');
				var flds = getInputs();
				var ids = $.map(flds, function(x) {
					return x.id;
				});
				var targetIndex = ids.indexOf(targetId);
				//如果是-1 可能是該欄位沒有長度,故回傳 false 當作沒事情發生(preventDefault)
				if(targetIndex == -1){
					console.log('targetIndex= -1,do not move.');
					return false;
				}
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
		       var ids = $.map(getInputs(), function(x) {
		       	return x.id;
		       });
		       curr = ids.indexOf(targetId);
		       myFocus($target[0]); // get dom from $target
		       ifx.clearLastField();
		       ifx.jumpIntoSwiftMode(); //kn 修復從var使用滑鼠按上方的swift欄位時 焦點有問題的bug
				}
			}
      return true;
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
		console.log("drawField!");
		var t = "";
		if (x['$'].type == 'loop') {
			var x2  = $.extend({}, x); // clone new one
			if (x2.times == undefined)
				x2.times = 1;
			t = drawLoop(x2);

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

	// TODO:
	// 1. loop 重寫  不要非同步產生(drawOneloop)
    // 2. 定義LOOP時必須定義name 做為程式識別之用
	// 3. deserialize LOOP 重寫   先隱藏反解電文  取得loop次數後  再顯示畫面  再解電文一次
	function getLoopTagById(id){
		return _loopMap['tblLoop' + id];
	}
	function drawLoop(loopTag) {
		var key = 'tblLoop' + loopTag.id;
		_loopMap[key] = loopTag; // 把loopTag 儲存於_loopMap, 再刪除其他列之後 部會因為列改變而找不到loopTag
								// desearial時也可以用
		loopTag.container = key;
		var hh = [];
		var loopcount = (loopTag['$'].count || 15);
		hh.push("<table id='" + loopTag.container + "' ");
		hh.push(" data-loopTable='" + loopTag.box + "' " );
		hh.push(" data-max='" + loopcount + "' ");
		hh.push(" class='loopTable' >");
		hh.push("<caption>"	+ (loopTag['$'].name || 'loop')	+ "</caption>");
		hh.push("<tbody>");

		for (var i = 0; i < loopTag.times; i++) {
			hh.push(drawOneLoop(loopTag,i,loopcount));
		}
		hh.push("</tbody></table>");
		var addBtn = "<span id='btnLoopAdd"
				+ loopTag.id
				+ "' class='button gray loopButton loopAddButton'>&nbsp&nbsp+&nbsp&nbsp</span>"; //Add loop
		//柯: max > 1 筆的時候 才顯示 btnLoopAdd!
	  if(loopcount > 1 ){
		  hh.push(addBtn);
	  }

	//新增功能 loop部分
    if(loopTag['$'].optional) {
    var b = "<span id='btnLoopOptional"
        + loopTag.id
        + "' class='button blue loopOptionalButton'>Skip Loop</span>";
    hh.push(b);
    }
//		setTimeout(function() {
//			$('#btnLoopAdd' + loopTag.id).on('click', addRowHandler);
//		}, 1);

		return hh.join("");

	}
	function addRowHandler() {
		var $this = $(this);
		var id = $this.attr('id');
		id = id.replace('btnLoopAdd','');
		var loopTag =getLoopTagById(id);
		var max = loopTag['$'].count;
		// var rowCount = $('#' + loopTag.container + ' > tbody >
		// tr').length;
		var rowCount = (getLoopRows(loopTag.container)).length;
		if (max != undefined && rowCount >= max) {
			alert('已達LOOP上限, 無法新增');
			return;
		}
		var theHtml = drawOneLoop(loopTag, rowCount);
		$('#' + loopTag.container + '  > tbody:last').append(theHtml);
		resetLoopButtonsHandlers();
		ifx.resizeSwift();
	}


	function getLoopRows(container) {
		return $('#' + container + '  > tbody > tr');
	}
			//新增功能 loop部分
function loopOptionalHandler(){
        var $this = $(this);
        var id = $this.attr('id');
        id = id.replace('btnLoopOptional','');
        var loopTag =getLoopTagById(id);

        var rowCount = (getLoopRows(loopTag.container)).length;
        if(rowCount > 1) {
            alert("刪除到剩下一組, 才能跳過");
            return;
        }else {
            var resumeInput="Resume Loop";
            // swiftField
            var $thisLoopTable = $('#' + loopTag.container);
            if($this.text() != resumeInput) {
                goNext($thisLoopTable);
                $('.swiftField', $thisLoopTable ).attr('disabled', true);
                $thisLoopTable.next('.loopAddButton').css('visibility', 'hidden');
                $thisLoopTable.css({ opacity: 0.3});
                $this.attr('oldText', $this.text());
                $this.text(resumeInput);
            }else {
                $this.text($this.attr('oldText'));
                $('.swiftField', $thisLoopTable ).attr('disabled', false);
                $thisLoopTable.next('.loopAddButton').css('visibility', 'visible');
                $thisLoopTable.css({ opacity: 1});
                goPrev($thisLoopTable);
            }
        }
        function goPrev($thisLoopTable) {
            var $loopFlds = $('.swiftField', $thisLoopTable );
            var firstLoopFldId = $loopFlds[0].id;
            var $flds = getInputs();
            var currId = $flds[curr].id;
             var currtmp=0;
            if(firstLoopFldId < currId) { // 目前focus在loop下方
                $.each($flds, function(i, x) {
                    if (x.id == firstLoopFldId) {
                        currtmp  = i;
                        myFocus($flds[currtmp]);
                    }
                });
            }
        }
        function goNext($thisLoopTable) {
            if( $('.myFocusHint', $thisLoopTable ).length == 0) {
                console.log("not in loop");
                return;
            }
            console.log("current focus belongs to this loop");
            var $loopFlds = $('.swiftField', $thisLoopTable );
            var lastLoopFldId = $loopFlds[$loopFlds.length-1].id;
             console.log("lastLoopFldId:"+lastLoopFldId);

            var $flds = getInputs();
            //TODO 這邊邏輯可能怪怪的
            $.each($flds, function(i, x) {
            	 console.log("x.id:"+x.id);
                if (x.id == lastLoopFldId ) {
                    curr  = i + 1; // 此loop之後, 下一個輸入欄位
                    console.log("curr:"+curr+",flds len:"+$flds.length);
                    if(curr < $flds.length) {
                        myFocus($flds[curr]);
                        curr -= $loopFlds.length; // loopFlds稍後被隱藏, 所以扣掉
                    }else { // 此loop是swift最後面
                        ifx.clearLastField();
                        ifx.jumpIntoSwiftMode();
                        ifx.jumpToAfterSwiftForm();
                    }
                }
            });
        }
    }
	function drawOneLoop(loopTag, rowNum, loopcount) {
		var t = [];
		var id, firstId = null;

		// drawOneLoop 一定是加在最末列  所以 loopIndex 並不需要保持連續,被刪掉的號碼就空著
		// 新增的列 就往後加
		if(loopTag.seq == undefined) loopTag.seq = 0;
 		loopIndex = loopTag.seq++;

		var loopIndexPrefix = loopTag.id + "_loop_" + loopIndex;
		t.push("<table class='oneLoopTable'>");
		$.each(loopTag.tags, function(i, x) {
			t.push("<tr><td  align='left'>");
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
				+ " data-parentId='" + loopTag.id + "' "
				+" class='button darkblue loopButton  loopDeleteButton' >Delete</span>";
		//柯: loop數量如果等於1 就不需要顯示delete
		if(loopcount && loopcount <= 1){
			b = "";
		}


		var theHtml = '<tr><td class="loopIndex">'
				+ (rowNum + 1)
				+ '</td><td align="center" style="  vertical-align: top;"  colspan="8">'
				+ tt + '</td><td style=" vertical-align: top;">' + b
				+ '</td></tr>';

		//$('#' + loopTag.container + '  > tbody:last').append(theHtml);
		return theHtml;


//		resetFocus(firstId);


	}

	function resetLoopButtonsHandlers() {
		$('.loopAddButton').off('click').on('click', addRowHandler);
		$('.loopDeleteButton').off('click').on('click', deleteRow);
		$('.loopDeleteButton').off('mouseenter mouseleave').hover(hoverRow,hoverRowOff);

		//新增功能 loop部分
		$('.loopOptionalButton').off('click').on('click', loopOptionalHandler);

	}


	// loop event handler
	function resetFocus(firstId) {
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

		var parentId = $deleteBtn.attr("data-parentId");
		var loopTag =getLoopTagById(parentId);
		var thePrefix = $deleteBtn.attr("data-prefix");

		console.log("******* deleteRow for:"+ thePrefix);

		var tr = $deleteBtn.closest("tr");
		var $tblCotainer = $deleteBtn.closest("table");
		// var rows = $('#' + loopTag.container + ' > tbody > tr');
		var rows = getLoopRows(loopTag.container);
//		var rows = ('tr', $tblCotainer.find('tbody'));

		if (rows.length == 1) {
			console.log("only one loop remains, can't delete");
			return;
		}

		var deletedRow = rows.index(tr);
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
			var rows = getLoopRows(loopTag.container);
			var $td;
			$.each(rows, function(i, tr){
				$('td', tr).each(function(col, td) {
					if (col == 0) {
						$td = $(td);
						$td.text(i + 1);
						return false;
					}
				});
			});
//			var trs = ('tr', $tblCotainer.find('tbody'));
			// $('#' + loopTag.container + ' > tbody > tr').length;
//			$.each(trs, function(row, tr) {
//				if (row < deletedRow)
//					return true; // continue next row
//
//				var ss = thePrefix.split('_');
//				var newPrefix = ss.join('_'); // the deleted row prefix
//				ss[ss.length-1] = (row+1);
//				var oldPrefix = ss.join('_');
//				thePrefix = oldPrefix; // advance to next row




//				var subLoop = $('[id^=tblLoop]', $tblCotainer);
//				$.each(subLoop, function(i,x){
//					var saved = _loopMap[x.id];
//					delete  _loopMap[x.id];
//					x.id = newId(x.id);
//					saved.container = x.id; // see drawLoop()
//					_loopMap[x.id] = saved;
//				});
//
//
//				// replace every input field's ID in this TR
//				var inputs = $('[id^=' + SWIFT_FIELD_PREFIX + ']', tr);
//				$.each(inputs, function(i, x) {
//					// _swf_4_2_0
//					console.log('input oid:' + x.id);
//					x.id = newId(x.id);
//					console.log('  ====>' + x.id);
//				});
//
//				// replace every delete button ID in this TR
//				var inputs = $('[id^=btnLoopDel]', tr);
//				$.each(inputs, function(i, x) {
//					// _swf_4_2_0
//					console.log('btn deleete oid:' + x.id);
//					x.id = newId(x.id);
//
//					$(x).attr("data-prefix",newPrefix);
//					console.log('  ====>' + x.id);
//					console.log("====prefix:"+$(x).attr("data-prefix"));
//				});
//
//				// replace every add loop button ID in this TR
//				var inputs = $('[id^=btnLoopAdd]', tr);
//				$.each(inputs, function(i, x) {
//					// _swf_4_2_0
//					console.log('btn add oid:' + x.id);
//					x.id = newId(x.id);
//					console.log('  ====>' + x.id);
//				});
//
//
//
//				// replace every container's ID in this TR
//				var containers = $('[id^=div' + SWIFT_FIELD_PREFIX + ']',
//						tr);
//				$.each(containers, function(i, x) {
//					var oid = x.id;
//					console.log('container oid:' + x.id);
//					x.id = newId(x.id);
//					// $('#' + x.id).html(oid + '===>' + x.id);
//					console.log('  ====>' + x.id);
//				});
//
//				function newId(oid) {
//					return oid.replace(oldPrefix, newPrefix);
//				}
//			});

		}
	} // end of function deleteRow


	// end of loop event handler


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
			qid(id).on('keypress', function(e) {
				var k = e.char;  // 柯:String.fromCharCode(e.keyCode)
				var oo = options.split(',');
				if (oo.indexOf(k.toUpperCase()) == -1)
					return false;
			});
		}, 1);

		return label + input + "<span class='lblOptions'>(" + options
				+ ")</span>" + optDiv;
	}

	function drawOpt(containerId, n, abc, fnCallBack) {
		var $container =qid(containerId);
		// 因為反序列化(XW200,XW300)之處理方式, 未(也沒辦法)紀錄目前opt,
		// 所以偷偷紀錄於DOM,
		var currOpt = $container.attr('currOpt');
		//原先重複時不處理(return),但會導致範本後沒有更新到內容
		if(currOpt && currOpt == abc && _doReplace == false){
			return;
		}

		$container.empty();
    $container.attr('currOpt', abc); //紀錄currOpt

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
				//柯:新增FOR空"_"的OPT時 會停住的BUG? 20160115
			if(flds[curr] && !$container.attr('currOpt') ){
				console.log("no myFocus fld with currOpt null ");
				addingField = false;
			 return;
			}
			//柯 在loop時會跳不出來，故加入超過的話 跳出var
			if(flds[curr]){
					console.log('drawOpt() curr:' + curr + '==>' + flds[curr].id);
			     myFocus(flds[curr]);
		  }else{
		  	 ifx.jumpToAfterSwiftForm();
		  }

			addingField = false;
		}, 1);
	}
	function drawLabel(x, statusWanted, tagWanted, nameWanted,context ) {
		//20160331補上context
		if (x.noLabel && x.noLabel == true){
			return context ?( "<span class='lblContext'>　 " + context + " </span>"+'<br/>'):"";
		}
		statusWanted = statusWanted == undefined ? true : statusWanted;
		tagWanted = tagWanted == undefined ? true : tagWanted;
		// nameWanted = nameWanted == undefined ? true : nameWanted;
    if (!statusWanted && !nameWanted) {
			return '';
		}

		var lblStatus = '', label1 = '', label2 = '',label3 = '',label4 = '', clsName = " lblTag ";
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
			label3 = "<span class='lblName' style='float:right;'></span>";	//移除span中的 No." + x['$'].no + "
		}

		if (x['$'].name)
			label2 = "<span class='lblName'>" + x['$'].name + "</span>";
	  if(context)
      label4 = "<span class='lblContext'>　" + context + " </span>";

		return lblStatus + label1 + label2 + label4 + label3 + '<br/>';
	}
	function drawSimpleField(x, id, statusWanted, tagWanted, nameWanted) {
		x = _m.add(id, x); // convert to MyTag and add to _m
		console.log('drawSimpleField for ' + x.$.tag);
		// labelWanted = labelWanted === undefined ? true: labelWanted;
		// nameWanted = nameWanted === undefined ? true: nameWanted;
		var context = x.$.context || '';
		var label = drawLabel(x, statusWanted, tagWanted, nameWanted,context);


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
				+ "' " + " maxlength=" + len + " title='" + component + "' ";

		if (x.multiLine) {
			styles.push("overflow:visible;");
			var ta = "<textarea cols=" + x.cols + " rows=" + x.rows + " ";
			if (x.rows > 10) {
				styles.push("height: 185px");
			}else {
				styles.push("height: "+x.rows*20+"px");
			}
			//欄位35位以上改成此方式
			if (x.cols > 35) {
		  		//styles.push("width:" + (35 * 10 + 14) + "px");
				//styles.push("word-wrap:normal");
			}
			input = ta + input;
		} else {
			var sizelen = parseInt(len,10);//(len > 35) ? 35 : parseInt(len,10);
			//柯 新增bag是money時,多增加commas的長度
			if(x.$.bag=="amt"){
			 var tek = parseInt((sizelen-3)/3);
			 tek = tek+sizelen;
			 sizelen = (x.$.bag=="amt") ? tek :sizelen;
		  }
			//畫面顯示大小,為了長欄位位移.
	    input += " size=" + sizelen+" ";
			//styles.push("width:" + (w * 10 + 14) + "px");
			input = "<input type='text' " + input;
		}

		if (styles) {
			input += ("style='" + styles.join(";") + "' ");
		}
		if (len == 0) {
			input += " readonly='true' data-constant='" + x.$.constant
					+ "' data-swift-tag='" + x.$.tag + "' data-swift-text='' ";
		}

		input += "/>";

		s = label + input ;
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
			//新增 依照必須輸入替換顏色
			if (o.must)
				s += "<h1 class='code-redmust'>" + title + "</h1>";
			else
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
			setTimeout(function() { //
				var $z = qid(codeAreaId);  // $('#' + codeAreaId)
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
						var $target = qid(targetId);// $('#' + targetId)
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
		if (el.prop("selectionStart") == el.prop("selectionEnd")) {
		return el.prop("selectionStart");
	}else if(el.prop("selectionStart") != 0){
		return el.prop("selectionStart");
		}else{
		return el.prop("selectionEnd");
		}
		//var doc = document; // 小柯增效能
		//if (el.prop("selectionStart")) {
		//	return el.prop("selectionStart");
		//} else if (doc.selection) { // 小柯增效能 document->doc
		//	el.focus();
    //
		//	var r = doc.selection.createRange(); // 小柯增效能 document->doc
		//	if (r == null) {
		//		return 0;
		//	}
    //
		//	var re = el.createTextRange(), rc = re.duplicate();
		//	re.moveToBookmark(r.getBookmark());
		//	rc.setEndPoint('EndToStart', re);
    //
		//	return rc.text.length;
		//}
		//return 0;
	}
	;
	function appendAtCaret($target, caret, $value) {
		var value = $target.val();
		if (caret == 0) {
			$target.val($value + value);
		}else if(caret == value.length){
			$target.val(value + $value);
		}else  {  //if (caret != value.length)
			var startPos = $target.prop("selectionStart");
			var endPos = $target.prop("selectionEnd");
			var scrollTop = $target.scrollTop;
			$target.val(value.substring(0, startPos) + $value
					+ value.substring(endPos, value.length));
			$target.prop("selectionStart", startPos );
			$target.prop("selectionEnd", startPos + $value.length);
			$target.scrollTop = scrollTop; //不懂為何要換??
		}

		//		if (caret != value.length) {
		//	var startPos = $target.prop("selectionStart");
		//	var scrollTop = $target.scrollTop;
		//	$target.val(value.substring(0, caret) + $value
		//			+ value.substring(caret, value.length));
		//	$target.prop("selectionStart", startPos + $value.length);
		//	$target.prop("selectionEnd", startPos + $value.length);
		//	$target.scrollTop = scrollTop;
		//} else if (caret == 0) {
		//	$target.val($value + value);
		//} else {
		//	$target.val(value + $value);
		//}
	};

	function drawComplexField(complex, id) {
		console.log("drawComplexField!");
		var t = '', tmp;
		_m.add(id, complex);
		var emptyTagName = "<span class='lblSubField'> </span>";
		t += drawLabel(complex);
		$.each(complex.tags, function(i, x) {
			x.$.tag = complex.$.tag + '_' + i;
			x.id = id + '-sub-' + i + '-' + complex.tags.length;
			if(complex.$.seralizer != undefined){
				x.seralizer = complex.$.seralizer;
			}
			x.ident = 1;
			tmp = drawSimpleField(x, x.id, true, false);
			tmp = tmp.replace(/lblName/g, "lblSubName");
			t += (emptyTagName + tmp);
			t += "<br/>";
		});

		return t;
	}

	function getInputsAll(containerId) {
		containerId = containerId || SWIFT_FORM;
		return $('[id^=' + SWIFT_FIELD_PREFIX + ']:enabled:not([disabled])', //有需要改成 :not([value!=''])  ??
				qid(containerId));
	}
	function getInputs(containerId) {
		containerId = containerId || SWIFT_FORM;
		return $('[id^=' + SWIFT_FIELD_PREFIX + ']:enabled:not([readonly])', //:enabled:not([readonly])',
				qid(containerId));//$('#' + containerId))
	}

	function nextField(val, mode, fn) {
		var valWanted = true;
		if (val == false)
			valWanted = false;

			//柯:移除此段好像才會正確
		//if (getInputs().length == 0) {
		//	setTimeout(function() {
		//		nextField(val, mode, fn);
		//	}, 10);
		//	return;
		//}

		var flds = getInputs();

		if (flds.length == 0) {  // flds : 所有可輸入欄位
			setTimeout(function() {
				// 都不用輸入, 離開swift form
			//	nextField(val, mode, fn);
					ifx.jumpToAfterSwiftForm();
			}, 10);
			return;
		}

		// 滑鼠可能在var欄位間亂移動, 若在swift form之後的var 欄位移到swift form之前欄位,
		// curr值會維持為flds.length, 所以在此重設
		if (curr >= flds.length)
			curr = -1;

		if (!mode) {
			if (curr >= 0) {
				if (!validateCurrentField(valWanted, flds[curr])){
					  if(curr >= flds.length){
					  	//柯  回到swift後之var
					  	console.log("curr:"+curr+">= flds.length:"+flds.length);
					  }else{
					  	//myFocus(flds[curr]);  //柯 依據skip loop的問題,增加此段看似可解決紅框問題
					    return;
				   }
					}
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

    //柯 ***START*** 新增給 SIWFT中 欄位
    //但至少會調一次 無法避免(沒DB存)
    if(jumpTargetId != null){
			var jumpToIndex = getIndexById(jumpTargetId);
				if (jumpToIndex != -1) {
					for (; curr < flds.length && curr < jumpToIndex; curr++) {
						myFocus(flds[curr]);
						if (!validateCurrentField(true, flds[curr]))
							return;
					}
				}
    }

		console.log('nextField() curr:' + curr + '==>' + flds[curr].id);
    //***END***
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
				console.log("is Dirty? true:"+dom.id);
				o.resetValDone();
				var ok = validated(dom);
				if (!ok) {
					return false;
				}
				// ok == true
				o.setValDone($curr.val()); // TODO: check multi line
			}else{
		 	console.log("is Dirty? false:"+dom.id);
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
		//柯 給金額欄位取代commas用

		if (domObj) {
    var bag = _m.get(domObj.id).bag();
    var val = domObj.value,v="";
			domObj.focus();
			 if(bag == "amt" && val){ 	//柯 給金額欄位取代commas用
		     v =val.replace(/\./g, "");
			   domObj.value = v;
			  }

			//柯:移除SWIFT全選欄位的功能
			//window.setTimeout(function() {
			//	try {
			//		domObj.select();
			//	} catch (ee) {
			//	}
			//}, 1);

			currFocusedDom = domObj;
			$jq = $(domObj);
			$jq.addClass('myFocusHint');
			$jq.closest("td").addClass('myFocusRowHint');// .get(0).scrollIntoView();
			// $jq.prev().css('background-color', 'red');

			$('#' + domObj.id + ' ~ .tagCodeArea').show();
			// ifx.scrollFieldInToView($(domObj));
			$('.ui-accordion-content').css("height","auto");


			// ifx.scrollFieldInToView($(domObj));

		} else {
			//不知為何突然一堆
			console.log('null swift dom object');
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
		
		/*潘 20180322 此檢核打開並修改*/
		if (!checkProhibitedChars(oTag, $currentField, value))
			return false;

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
		return (!(oTag.isMandatory()) && !value && !(oTag.seralizer));
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
			if (rtnName.indexOf('swift_fn_XWR10') != -1) {
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

/*潘 20180323 打開SWIFT非法字元檢核*/
//	var reProhbString = /[;{}~`!@#$%^&*_=\[\]\\|><]/;
//	var reProhbString2 = /[.:]/;
//	var reProhbString = /[;@#\$%\^&\*\!~|<>\[\]\"\=\t\f]/;
	var reProhbString = /[;@#\$%\^&\*\!~|\[\]\"\=\t\f]/;
	var prohbErrmsg = "不允許之特殊符號包括!@#$%&*~|[]" + '"' + "=^;或  TAB";
	function checkProhibitedChars(oTag, $currentField, value) {
		// 內有不允許之特殊符號(;{}~`!@#$%^&*_=[]\|><)或 .: ",
		var k, msg;
		if (value) {
			var sp = value.split("\n");
			for (var i = 0; i < sp.length; i++)
			if(sp[i].substr(0,1) == "-"){
			//if(value.substr(0,1) == "-"){
				msg = "輸入資料之第一位 不可為[－]<br/>";
				ifx.errmsg(msg, $currentField);
				return false;
			}

			k = value.match(reProhbString);
			if (k != null) {
				/*
				msg = '輸入資料之第' + (k.index + 1) + "位 [" + k[0] + "]為不允許符號<br/>"
						+ prohbErrmsg;
				*/
				msg = prohbErrmsg;
				ifx.errmsg(msg, $currentField);
				return false;
			}
		}
		return true;
	}

	function validateRequired(oTag, $currentField, value) {

	if (value.length == 0 && oTag['$'].status == 'M' && oTag['$'].tag != "77E_0" && oTag['$'].tag.slice(0,3) != "35B"){
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
		//原先重複時不處理drawOpt,但會導致範本後沒有更新到內容
		if (oTag.currOpt != abc || _doReplace == true) {
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
      //柯:重組 for 最後一組loop時,skip該loop但欄位內容還是存在在TITA上
		  setTimeout(function() {
			  displaySwiftText();
		  }, 1);
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
	function qid(id) {
		return  $("[id='" + id + "']");
	}
	function convertToSwiftFormat(id) {
		_m.get(id).dump();
		var $fld =qid(id);
		var value = $fld.val(), tagName = _m.get(id)
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


		//柯 新增bag是money時,add commas
	  if (_m.get(id).bag() =="amt" && value) {
	  	value = value.replace(/\./g, ""); //移除增加的"."記號
	  	$fld.val(IfxUtl.moneyFormatter(value,true));
		}
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

   //TODO #**增加千分位顯示
	function printSwift(prefix) {

		var t = buildSwiftText(null, true);
		var tt = t.split('\r\n');
		tt = $.map(tt, function(x) {
			return prefix + x;
		});
		return tt.join('\r\n');
	}
	function buildSwiftTim(bName) {
		var h = buildSwiftHeader();
		var b = buildSwiftText(bName);
		var f = buildSwiftTail();

		var h_result = swiftjsonparse(h) + "{4:\r\n";

		return h_result + b + f;
	}
	function buildSwiftText(bName, bPrint) {
	  //var flds = getInputs();
		var flds = getInputsAll(); //柯: 更改成 getInputsAll() 為了在審核之類的會導致沒抓到欄位
		var x, $x;
		var arr = [], s = null;
		var tag, value;
		var orgTag,orgVal;
		var msgType = _Vars['msgType'];
		for (var i = 0; i < flds.length; i++) {
			x = flds[i];
			$x = $(x);
			if (x.id.indexOf('-sub-') != -1) { // complex field
				i += (processSub(x.id, i) - 1); // processSub回傳sub數目,
				// -1讓i指向最後sub之最後一筆

			} else {
				tag = $x.attr('data-swift-tag');
				if (tag) {
					orgTag = tag;
					orgVal = $x.attr('data-swift-text');
					if (bPrint) {
						s = printSwiftTag(orgTag, (_m.get(x.id)).getName(),orgVal,(_m.get(x.id)).getvalidate() ); //,(_m.get(x.id)).getvalidate()
					} else {
						s = formatSwiftTag(orgTag, orgVal);
					}
				  addHeaderbyTagval(msgType,orgTag,orgVal);
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
		function printSwiftTag(tag, name, text ,checkbic) { //增加第四個參數
			var s = ":" + tag + ":";
			if (s.length == 4)
				s += ' ';
			s += (name + '\n');

			var tt = text.split('\n');

			$.each(tt, function(i, t) {
				var aaaa = Swift.generic.generic.getBicFromCache(t); //不懂為何突然需要前面 "Swift.generic."
				s += ('     ' + t);
				console.log('generic.getBicFromCachett:'+ tag + ":" + aaaa);
				if(aaaa && checkbic == "xxxSwiftBic") {  // && checkbic == "xxxSwiftBic" 判斷是否有需要印 BIC
					if(aaaa.slice(0,35).trim() != ""){
						s += '\n';
						s += ('     **' +aaaa.slice(0,35)) ;
            }
					if(aaaa.slice(35,70).trim() != ""){
						s += '\n';
						s += ('     **' +aaaa.slice(35,70)) ;
            }
					if(aaaa.slice(70,105).trim() != ""){
						s += '\n';
						s += ('     **' +aaaa.slice(70,105)) ;
            }
					if(aaaa.slice(105,140).trim() != ""){
						s += '\n';
						s += ('     **' +aaaa.slice(105,140)) ;
            }
        }
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
			var combine ='\n';
			if((_m.get(x.id)).seralizer){  //已在 drawComplexField 加入
				combine = "";
			}
			if (parentTag) {
				if (bPrint) {
					tt = printSwiftTag(parentTag, parentName, sub.join(combine),(_m.get(x.id)).getvalidate());
				} else {
					tt = formatSwiftTag(parentTag, sub.join(combine));
				}
				arr.push(tt);
			}
			return counts;

		}
	}

	 function padRight(str, lenght) {
	    if (str.length >= lenght) {
	        return str;
	    } else {
	        return padRight(str + " ", lenght);  //20140513修改 X ->  " "
	    }
	}

	function buildSwiftHeader() {//潘
		var sendingTid = _Vars['sendingTid'].slice(0, 12), msgType = _Vars['msgType'], pri = _Vars['pri'], dest = _Vars['dest'], s = '';
		var sdmkey = _Vars['sdmkey'];//新增變數
		var fiscfg = _Vars['fiscfg'];//新增變數

		//前置變數
		if (sendingTid.length != 12) {
		    sendingTid = padRight(sendingTid, 12);
		}
		dest = (dest.length == 8) ? dest + "XXXX" : dest.substr(0, 8) + "X"
				+ dest.substr(8, 3);

		_json_h_obj = {
      "1": "F01" + sendingTid + "0000000000",
      "2": "I"+ msgType.slice(0,3)+(padRight(dest,12) + pri),
      "3": {}
    };
    //block 3
    if(fiscfg == "1"){
			_json_h_obj['3']['103'] = "TWP";
		}
		_json_h_obj['3']['108'] = sdmkey;

		//依照電文增加不同資料
		if (msgType == "103+" ) {
			_json_h_obj['3']['119'] = "STP";
		    s += "{119:STP}";
		}else if(msgType == "202+"){
			_json_h_obj['3']['119'] = "COV";
			 s += "{119:COV}";
		}else if (msgType == "104" && fiscfg == "1") {  //104 & 財金時 增加{119:RFDD}
			_json_h_obj['3']['119'] = "RFDD";
		}

		//s += "{4:\r\n";
		return _json_h_obj;
	}

	function buildSwiftTail() {
		//var msgTypeCategory = _Vars['msgType'][0];
		//return (msgTypeCategory == '3' || msgTypeCategory == '9') ? "-}"
		//		: "-}{5:{MAC:FFFFFFFF}}";
		//柯:20151125
		//return (msgTypeCategory == '3' || msgTypeCategory == '9') ? "-}"
		//		: "-}{5:}";
		//
		var pdefg = _Vars['pdefg'];
		console.log("Var pdefg -> "+pdefg);
		if(pdefg.toString() == "1"){
			 return "-}{5:{PDE:}}";
		}else {
			 return "-}{5:}";
		}
	}

	function swiftjsonparse(jsonData){
		var blockresult = "";
		var tagstart = "{";
		var tagend = "}";
	  for(var i in jsonData){
	  	blockresult += tagstart;
      var key = i;
      blockresult += ( key + ":");
      var val = jsonData[i];

      if(typeof val === 'object'){
        for(var j in val){
        	  blockresult += tagstart;
            var sub_key = j;
            blockresult += (sub_key + ":");
            var sub_val = val[j];
            blockresult += sub_val;
            blockresult += tagend;
        }
      }else{
      	blockresult += val;
      }
      blockresult += tagend;
    }
    return blockresult;
	}

	//ADD FOR MT104 23E RFDD
	function addHeaderbyTagval(msgtype,orgtag,orgval){
		if(msgtype == "104" && orgtag == "23E" && orgval.indexOf("RFDD") != -1 ){
			_json_h_obj['3']['119'] = "RFDD";
		}
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

    //20140513  新增  function  padLeft
    function padLeft(str,length) {
        if (str.length >= length) {
            return str;
        } else {
            return padLeft("0" + str, length);
        }
    }

    function mapToVar() {
        var flds = getInputsAll(); //要全部的欄位掃一遍
        var bagName2VarNameMap = {
            'trn': '#swiftTRN',
            'valday': '#swiftVALDAY',
            'money': {
                'mvalday': '#swiftXDAY',   //20140513  新增  mvalday
                'cur': '#swiftCURNM',
                'amt': '#swiftXAMT'
            },
            //小柯   20140505 start
            'trn21': '#swiftTRN21',
            'tag': '#swiftTAG'
        };
        //小柯   20140505 end

        // ifx.setValue("#swiftMsgType",mt['$'].type);
        ifx.setValue("#swiftMsgName", mt['$'].name);
        ifx.setValue(getVar('trn'), getBag('trn', '','trn20c').value); //for 20C 的欄位
        ifx.setValue(getVar('trn21'), getBag('trn21', '').value);
        ifx.setValue(getVar('valday'), getBag('valday', '0').value);

       // 2015/7/1 柯: 更改如下 start
       var moneyTag = getBag('cur', '');
       var moneyType = getVar('money');
       var amtTag = getBag('amt', '').value;
       var amttext = [], amtemp ="";
       //此段其實前方已完成邏輯
       if(amtTag != ""){
       amtTag =amtTag.replace(/\./g, "");//移除增加的"."記號
       amttext = amtTag.split(",");
       if (amttext[1] == "") {
           amtemp = amttext[0] + "00";
       } else {
           amtemp = amttext.join("");
       }
     }

        ifx.setValue(moneyType['mvalday'], getBag('mvalday', '0').value);
        ifx.setValue(moneyType['cur'], moneyTag.value);
        ifx.setValue(moneyType['amt'], amtemp);  //好像不用 padLeft?

        //end

        // 2015/7/1 柯: 註解 start

        //var moneyTag = getBag('money', '');
        //var moneyType = getVar('money');
        //var curAmt = parseMoney(moneyTag.tag, moneyTag.value);
        ////20140513以下修改---->
        //var amttext = [], amtemp;
        //if (curAmt[2]) {
        //amttext = curAmt[2].split(",");
        //
        //if (amttext[1] == "") {
        //    amtemp = amttext[0] + "00";
        //} else {
        //    amtemp = amttext.join("");
        //}
        //}

        //if (getBag('money', '').value != '') {
        //    ifx.setValue(moneyType['mvalday'], curAmt[0]);
        //    ifx.setValue(moneyType['cur'], curAmt[1]);
        //    ifx.setValue(moneyType['amt'], padLeft(amtemp, 15));  //20140513  新增  mvalday
        //} else {
        //    ifx.setValue(moneyType['mvalday'], '');
        //    ifx.setValue(moneyType['cur'], '');
        //    ifx.setValue(moneyType['amt'], '');
        //}
        //20140513---->

        //20140513---->


		//小柯   20140505 start
		if ( typeof moneyTag.tag  === 'undefined' || moneyTag.tag === null ){
			ifx.setValue(getVar('tag'),'');
		}else{
			ifx.setValue(getVar('tag'), moneyTag.tag['$'].tag.slice(0,3)); //改成下一層 moneyTag.tag['$']
		}
		//小柯   20140505 end

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
		    var mvalday = 0;                  //20140513  新增mvalday 欄位
			if (!value)
				return [ '', '' ];

			switch (oTag.getLength()) {
			    case 24:
			        mvalday = value.slice(0, 6);  //20140513  新增
				    value = value.substr(6);
				break;
			    default:
				break;
			}
			var cur = value.slice(0, 3);
			var amt = value.substr(3);
			return [mvalday, cur, amt];  //20140513  修改
		}
		function getVar(k) {
			return bagName2VarNameMap[k];
		}
		function getBag(bagName, defaultValue,secbagName) { //for 20C 的欄位
			var tag;
			var result = {
				value : defaultValue
			};
			$.each(flds, function(i, x) {  //柯 allflds 改回 flds
				tag = _m.get(x.id);
				if (tag.bag() == bagName || (secbagName && tag.bag() == secbagName)) {
                    if (tag.getValue() != null) { //小柯 新增 getValue檢查 如果是null 帶defaultValue
                    	if(secbagName && tag.bag() == secbagName){
                    		result.value = tag.getValue().slice(7); //for 20C 的欄位 去除前面字串:XXXX//
                    	}else{
					result.value = tag.getValue();
				}
                    } else {
                        result.value = defaultValue;
                    }

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
			flds.prop("readonly", true);
			flds.addClass('cantInput');
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

		console.log('text len:' + text.length);
		var START = '}{4:'+CKNEWLINE, STOP = '-}', i = text.indexOf(START), j = text
				.indexOf(STOP), swiftText;
		if (i != -1 && j != -1) {
			swiftText = text.slice(i + START.length, j);
			var ss = swiftText.split(CKNEWLINE);
			var r = [], tmpResult = null, t, tt;
			//柯:遇到這種TAG.以後的全部都是該TAG的值 (MTn98)
            var septags = "77E",septagsCk = false;

			for (i = 0; i < ss.length; i++) {
				//柯:新增雙重檢查 3,4位須為冒號和1,2位須為數字
				if (ss[i][0] == ':' && (ss[i][3] == ':' || ss[i][4] == ':' ) && IfxUtl.isNumber(ss[i][1]) && IfxUtl.isNumber(ss[i][2]) && !septagsCk) {
					if (tmpResult)
						r.push(tmpResult);
					t = ss[i].slice(1);
					//TODO CHECK直接交易執行的有需要更改MONEY顯示?
					tt = t.split(':');
					var tagtmp = tt.shift();
					var linestmp = tt.join(":");
					if(tagtmp == septags){
						septagsCk = true;
					}
					tmpResult = {
						tag : tagtmp,
						lines : [linestmp]
					};
				} else {
//					tmpResult.lines.push(ss[i]); //潘 20180130
					try {
				        tmpResult.lines.push(ss[i]);  
			        } catch (ee) {
			        	alert("電文內容錯誤!!")
			      	  	break;
			        }
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
	function putAllValues2(cmd, text, valueObjects, protectedTags, fnCallback , pastevalueObjects) {
		var tempvalueObjects = null;
		setTimeout(function() {
			$('#' + SWIFT_TEXT).html("<pre>" + text + "</pre>");
			$('#' + SWIFT_TEXT_COUNTS).text(text.length);
			var rest = SWIFT_MAX_BUF - text.length;
			$('#' + SWIFT_TEXT_REST).text(rest);
			// $('#swift_text_counts_line').show();
		}, 10);

		var currItem = 0, // 第幾個SWIFT電文Tag
		$x;

		if(pastevalueObjects){
			tempvalueObjects = valueObjects;
				valueObjects = pastevalueObjects;
				currItem = 0;
			console.log("複製電文");
					try {
			$.each(mt.tags, function(i, x) {
				deserializeTag(x);
			});
		} catch (ee) {
			if (ee !== "no more")
				throw ee;
			//new Error("failed to desearialize swift text");
		}
		}

		try {
			if(tempvalueObjects){
			 valueObjects = tempvalueObjects;
			 _doReplace = true;
		  }
			currItem = 0;
			$.each(mt.tags, function(i, x) {
				deserializeTag(x);//潘-1
			});
		} catch (ee) {
			if (ee !== "no more")
				throw ee;
			//new Error("failed to desearialize swift text");
		}
		//回覆強制取代範本時更改的初始值
   	_doReplace = false;



		function disableSome() {
			var bDisableAll = (protectedTags == null || protectedTags === '*'), flds = getInputs(), arrProtectedTags, aTag, $fld;

			if (bDisableAll) {
				$('.button', $('#' + SWIFT_FORM)).css('visibility', 'hidden'); // preserve
				// spaces
				flds.prop("readonly", true);
				flds.addClass('cantInput');
			} else {
				arrProtectedTags = splitBySize(protectedTags, 3);
				$.each(flds, function(i, dom) {
					$fld = $(dom)
					aTag = _m.get(dom.id);
					aTag.setValDone($fld.val()); // TODO: tag定義應增加設定,
					// 讓tag在反序列化之後 不設valdone
					convertToSwiftFormat(dom.id);
					if ($.inArray(aTag.getTagName().slice(0,3), arrProtectedTags) != -1) { //柯:因有拆欄位，故要改成前3位
						$fld.prop("readonly", true); //測試
						$fld.addClass('cantInput');
					}
				});

			}
		}

		function disableAllButMoney(view) {
			var flds = getInputs(), aTag, $fld;

			$('.button', $('#' + SWIFT_FORM)).css('visibility', 'hidden'); // preserve
			// spaces
			flds.prop("readonly", true);
			flds.addClass("cantInput");
			$.each(flds, function(i, dom) {
				$fld = $(dom)
				aTag = _m.get(dom.id);
				// aTag.setValDone($fld.val()); // TODO: tag定義應增加設定, 讓tag在反序列化之後
				// 不設valdone
				// convertToSwiftFormat(dom.id);

				if ((aTag.$.bag === "money" ||aTag.$.bag === "cur"||aTag.$.bag === "amt"||aTag.$.bag === "mvalday") && $fld.val() != "" ) { // 有輸入資料的,才需要審核
					//如果邏輯成功,則該欄位當作一般欄位處理
					if(aTag.$.noverify){
						var noverify = aTag.$.noverify.split(",");
						if($.inArray(mt['$'].type,noverify) != -1){
							return;
							}
					}
					if (view == "view" && ( aTag.$.bag === "amt" || aTag.$.bag === "money" ) && $fld.val() != ""){//潘
					  	$fld.val(IfxUtl.moneyFormatter($fld.val(),true));
					}
					if (view === undefined){
					  $fld.prop("readonly", false);
					  $fld.removeClass('cantInput');
					  aTag.$[dom.id] = $fld.val();  //給檢查此欄位使用資料
					  aTag.verified = false; // 先設定此欄位 檢核未通過
					  console.log("domid:"+dom.id+", "+"id:"+aTag.id+", tag:" +aTag.$.tag);
					  console.dir(aTag);
				    $fld.val('');
				  }
				}
			});

			displaySwiftText = function() {
			}; // erase displaySwiftText function
			var oldValidateCurrentField = validateCurrentField;
			validateCurrentField = function(valWanted, dom) {
				//if (!oldValidateCurrentField(valWanted, dom, false)) 潘
				if (!oldValidateCurrentField(valWanted, dom))
					return false;
				// return true;
				var aTag = _m.get(dom.id);
				var $fld = $(dom);
				var amtChk = false;
				if (_m.get(dom.id).bag() =="amt" && $fld.val().trim()){
				  if (aTag.$[dom.id] == $fld.val().replace(/\./ig,"")) // 潘 
				    amtChk = true;
				  else
				  	amtChk = false;
				}else {
					if (aTag.$[dom.id] == $fld.val())
					  amtChk = true;
				  else
				  	amtChk = false;
				}
				aTag.verified = false;
				if (amtChk) {	//檢查欄位
					aTag.verified = true;
					return true;
				} else {
					$fld.blur();
//					// alert("not match:" +aTag.$.valueToVerify );
//					var r = confirm("輸入資料與原電文內容不符! 停止審核電文?\n\n[確定] 不繼續審核    [取消] 重新輸入")
//					if (r == true) { // click [確定]
//						setTimeout(function() {
//							ifx.jumpToAfterSwiftForm();
//						}, 1);
//						return false;
//					} else { // ESC or click [取消]
//						$fld.focus();
//						return false; // entry again
//					}
					var msg = "輸入與原電文不符!!";
					ifx.errmsg(msg, $fld);
					return false;
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
				else if (cmd == 'view')//潘
					disableAllButMoney("view");

			if (fnCallback)
				fnCallback();

		}, 1);

		function splitBySize(text, size) {
			var arr = [], t;
			for (var i = 0; i < text.length; i += size) {
				t = $.trim(text.slice(i, i + size));
				if (t)
					arr.push(t);
			}
			return arr;
		}
		function deserializeTag(oTag,inloop) {
			if (oTag['$'].type == 'loop') {
				//var x2  = $.extend({}, oTag); // clone a new one
				deserializeLoop(oTag);
			} else {
				if (oTag['$'].options) {
					deserializeOptions(oTag);
				} else {
					if (oTag['$'].complex) {
						deserializeComplexField(oTag);
					} else {
						deserializeSimpleField(oTag,null,inloop);
					}
				}
			}
		}

		function getTagItem(tagName,oTag,inloop) {

		 if (currItem >= valueObjects.length) {
				throw "no more";
			}

				//pastevalueObjects
			if (tagName == valueObjects[currItem].tag ) {
				if(tagName.slice(0,2) == "16"){
					if(oTag.code && oTag.code[0]._ == valueObjects[currItem].lines[0]){//柯:16A導致錯誤,新增CODE檢查
						return valueObjects[currItem++];
					}else{
						if(!oTag.code){//回傳 16A  值
							console.log("Maybe 16A??");
							return valueObjects[currItem++];
						}else{
						return false;
					}
					}
				}
				return valueObjects[currItem++];
		 }
		 //柯:在inloop當中,如果是必填欄位,但接下來的不是該tag名稱,則跳出此loop (for MT104)
		 //不確定會不會產生其他問題
		 if(inloop && oTag.$.status == "M" && valueObjects[currItem].tag != tagName){
		 	 console.log("inloop=true and oTag.status=M but next not correct! throw");
		   throw "no more";
		 }

			return false;
		}

		function peekTagItem() {
			return valueObjects[currItem];
		}

		function deserializeLoop(loopTag) {
			loopTag = getLoopTagById(loopTag.id); //傳入之loopTag並未反映目前已有幾組LOOP  必須使用getLoopTagById 取得Runtime之tag

			//deserializeTag第二參數是讓在getTagItem 判斷是否在loop中
			var inloop = true;
			var savedCurrItem = -1;
			var maxLoop = loopTag.$.count || 15; // Number.MAX_VALUE;

			for (var i = 1; i <= maxLoop; i++) {
				savedCurrItem = currItem; // save curr valueObj pointer
				// add loop field controls to screen
				if (i > 1){
					addLoopControls(i-1);
				}
				// desearialize loop tags
				try {
					$.each(loopTag.tags, function(i, x) {
						deserializeTag(x,inloop);
					});
				} catch (ee) {
					if (ee !== "no more")
						throw ee;
						//throw new Error("failed to desearialize swift text");
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
					},1); // 因為drawOneLoop在10 ms(search point:timeoutDeleteLoop)後才註冊click event,  20 ms就夠
					break;
				}
			}

			function addLoopControls(row) {
				var theHtml = drawOneLoop(loopTag, row);
				$('#' + loopTag.container + '  > tbody:last').append(theHtml);
				resetLoopButtonsHandlers();
			}

		}

		function deserializeOptions(oTag) {
			var peekItem = peekTagItem();
			if (!peekItem){
				return;
			}
			var peekItemtag = peekItem.tag;
			//柯 新增第三段 避免掉一些巧合造成的塞值bug
			if (peekItemtag.slice(0, 2) != oTag.$.tag.slice(0, 2) ||
			    oTag.$.options.indexOf(peekItemtag.slice(2,3)) == -1){
				return;
			}
			var oItem = getTagItem(peekItemtag,oTag);
			var abc = oItem.tag.slice(2);
			if(abc=='')  {
				abc = ' ';

			}
			var optTag = oTag.opt[peekItemtag];
			var savedId = oTag.id; // save option's id of loop
			validateOptions(oTag, qid(oTag.id), abc, function() {
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
						try {
								optTag.tags[i].id = x.id;
						} catch (e) {
							//TODO 待追蹤問題,為何getInputs 超長!
							console.log("validateOptions error!");
							console.log(e);
						}
					});
					deserializeComplexField(optTag, oItem);
				} else {
					optTag.id = flds.get(0).id;
					deserializeSimpleField(optTag, oItem);

				}
			});
		}

		function deserializeSimpleField(oTag, oItemPassed, inloop) {
			var oItem = oItemPassed != undefined ? oItemPassed
					: getTagItem(oTag.$.tag,oTag,inloop);
			if (!oItem)
				return;

			var $x = qid(oTag.id); //$('#' + oTag.id);
			$x.val(oItem.lines.join('\n'));
		}

		function deserializeComplexField(oTag, oItemPassed) {
			var oItem = oItemPassed != undefined ? oItemPassed
					: getTagItem(oTag.$.tag);
			var $x;
			if (!oItem)
				return;

	    if (oTag.$.seralizer == "combine") {
	    	var dolen = 0;
	    	var combinevalue = oItem.lines.join('');
	    	for(var i=0;i<oTag.tags.length;i++){
				//$x = $('#' + oTag.tags[i].id); // first tag
	    		$x = qid(oTag.tags[i].id);
				var len = parseInt(oTag.tags[i].$.len, 10);
				console.log(len);
				$x.val(combinevalue.slice(dolen, dolen+len)); // put last line to first tag
				dolen += len;
	    	}
	    		return;
	   }

			console.log("deserializeComplexField, tag:" + oTag.$.tag);
			// if(oTag.$.deserialize === 'reverse'){
			if (oTag.$.tag[0] == "4") { // 似乎4開頭之tag是採用這個模式
				console.log("using reverse seralizer");
				// 潘 試修改40B問題
				if (oTag.$.tag == "40B") {
					$x = qid(oTag.tags[0].id);
					$x.val(oItem.lines.shift());

					$x = qid(oTag.tags[1].id);
					$x.val(oItem.lines.join('\n'));
				} else {
					// $x = $('#' + oTag.tags[1].id); // second tag
					$x = qid(oTag.tags[1].id);
					$x.val(oItem.lines.pop()); // put last line to second tag

					// $x = $('#' + oTag.tags[0].id); // first tag
					$x = qid(oTag.tags[0].id); // first tag
					$x.val(oItem.lines.join('\n')); // put rest of lines to first
					// tag
				}
				return;
			}


			// 貌似tag 5xx都是這個模式
			if (oTag.$.tag[0] == "5") {
			if (oItem.lines[0][0] == '/') { //5開頭的第一個
				//$x = $('#' + oTag.tags[0].id);
				$x = qid(oTag.tags[0].id); // first tag
				$x.val(oItem.lines.shift());
			}
			 	 $x = qid(oTag.tags[1].id);   //5開頭的第二個
			 	 $x.val(oItem.lines.join('\n'));
			 	 return;
		  }

			// 貌似tag 8xx都是這個模式
			if (oTag.$.tag[0] == "8") {
			if (oItem.lines[0][0] == '/') { //8開頭的第一個
				//$x = $('#' + oTag.tags[0].id);
				$x = qid(oTag.tags[0].id); // first tag
				$x.val(oItem.lines.shift());
			}
			if(oTag.tags.length > 1){
			 	 $x = qid(oTag.tags[1].id);   //8開頭的第二個
			 	 $x.val(oItem.lines.join('\n'));
			 }else {
				$x = qid(oTag.tags[0].id); // 只有一個tag
				$x.val(oItem.lines.join('\n'));
			 }
			 	 return;
		  }

		  //其餘欄位給第一個..
		  //TODO 541有錯 1058 20150515 541 89
		  try {
		  	 var testtry = oTag.tags[1].id;
		  	 $x = qid(oTag.tags[0].id);
			   $x.val(oItem.lines.shift());
		  	 $x = qid(oTag.tags[1].id);
			   $x.val(oItem.lines.join('\n'));
		  } catch (e) {
		  	$x = qid(oTag.tags[0].id);   //沒有第二個欄位直接合併內容
			   $x.val(oItem.lines.join('\n'));
		  }
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
			if ((aTag.$.bag === "money" ||aTag.$.bag === "cur"||aTag.$.bag === "amt"||aTag.$.bag === "mvalday") && aTag.verified == false ) {
				 //如果邏輯成功,則該欄位當作一般欄位處理
				 if(aTag.$.noverify){
						var noverify = aTag.$.noverify.split(",");
						if($.inArray(mt['$'].type,noverify) != -1){
							return true;
							}
					}

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
			var swift2xml;
			var isxml = false;
			try {
				swift2xml = ifx.getValue("SWIFT2XML$") ;
				isxml = swift2xml == "Y" ? true:false;
			} catch (e) {
			}
			console.log("print_with: isxml ="+isxml);
			//end

			//特殊 聯發通知電文範本
			if(swift2xml == "T"){
				return oFromSwift["all"];
			}
			// var content = mergeMtWithTota(mt, data, prefix);
			var content = Swift.print.merge(swift_direction, oFromSwift, mt,
					data, prefix,isxml);

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
				convertToSwiftFormat(f.attr('id'));
				displaySwiftText('');
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
var IfxGrid = (function() {
	var data = null,
		fieldMap = {},
		gridDef = {},
		colModel = {},
		colNames = null,
		id = "",
		rId = null;
	var _specialsum = false;

	function Grid(theGridDef, theFieldMap, theData, theColModel) {
		gridDef = theGridDef;
		fieldMap = theFieldMap;
		data = theData;
		colModel = theColModel;
	}
	var grid = null;
	var defaultCfg = {
		// width:1100,
		autowidth: true,
		rowNum: 10,
		rowList: [10, 20, 40, 100],
		caption: 'just a simple grid',
		viewInNewWindow: false,
		exportExcel: true,
		exportXml: false,
		filter: false,
		search: true,
		refresh: true,
		hiddengrid: false,
	};
	var cfg = {};
	Grid.prototype.render = function(panel, controls, fnResize) {
		id = controls["grid"];
		grid = $(id);
		$(panel).slideDown(function() {
			afterShow(controls);
			if (fnResize != null && typeof fnResize == 'function') {
				setTimeout(fnResize, 0);
			}
		});
	};
	Grid.prototype.refreshGrid = function(panel, newData, goLastPage, nouse) {
		var id = data[data.length-1].id;
		for(var i =0; i < newData.length; i++){
			id++;
			newData[i].id = id;
		}
		data = data.concat(newData);
		mydata = data;

		grid.trigger('reloadGrid');
		var lastP = grid.getGridParam('lastpage'); // 最後一頁

		grid.trigger('reloadGrid', [{
			page: lastP
		}]);


		var currPage = grid.getGridParam('page'); // 當前頁
		var rowid = parseInt(grid.jqGrid("getDataIDs")[grid.jqGrid("getDataIDs").length - 1], 10);

		newData.forEach((x) => {
			grid.jqGrid('addRowData', ++rowid, x, 'last');
		});

		if (goLastPage) {
			grid.trigger('reloadGrid');
			var lastP = grid.getGridParam('lastpage'); // 最後一頁

			grid.trigger('reloadGrid', [{
				page: lastP
			}]);
		}
		/*
		grid.jqGrid('clearGridData').jqGrid('setGridParam', {
			data: data
		}).trigger('reloadGrid', [{
			page: 1
		}]);
		*/

		$(panel).slideDown();
	};
	Grid.prototype.getCheckedRow = function(controls) {
		grid = $(controls["grid"]);
		var ss = grid.jqGrid('getGridParam', 'selarrrow');
		var selectedData = [];
		var len = ss.length;
		var thisdata = grid.jqGrid('getRowData');
		$.each(ss, function(i, x) {
			$.each(thisdata, function(j, v) {
				if (v.id == x) {
					selectedData.push(v);
				}
			});
		});
		// $.each(ss,function(i,x){
		// selectedData.push(thisdata[x]);
		// });
		return selectedData;
	};
	Grid.prototype.deleterowGrid = function(rows) {
		console.log("delRowData" + rows);
		grid.jqGrid("delRowData", rows);
	};

	function injectBtnActions() {
		$('.chainAction').unbind().click(function() {
			callChain($(this));
		});
	}
	Grid.prototype.unload = function() {
		grid.GridUnload2(id);
	};
	// add grid map back
	var _mapBack = null;
	Grid.prototype.setMapper = function(m) {
		_mapBack = m;
	};
	var _ifxHandlerMap = {};
	Grid.prototype.registerHandler = function(handlerMap) {
		_ifxHandlerMap = handlerMap;
	};

	function getIfxFn(name) {
		if (_.isEmpty(_ifxHandlerMap)) throw 'BUG!! no such ifx handler:' + name + ' registered!!';
		return _ifxHandlerMap[name];
	}

	function afterShow(controls) {
		initUISettings();
		initConfig();
		initGrid(controls);
		initButtons(controls);
		// 柯,新增
		doclick(controls);
		// TODO 使用
	}
	window.dupGrid = function() {
		return {
			gridDef: gridDef,
			fieldMap: fieldMap,
			data: $.extend(true, data, {}),
			colModel: colModel
		};
	};

	function initUISettings() {
		$.datepicker.setDefaults($.datepicker.regional["zh-TW"]);
	}

	function initConfig() {
		_.extend(cfg, defaultCfg);
		if (_.isEmpty(gridDef.config)) {
			return;
		}
		_.extend(cfg, gridDef.config);
	}

	function makeModel(colNames) {
		var result = [],
			i = 1, // i=1, skip id(gridDef.fiedls without ID
			id = {
				name: 'id',
				index: 'id',
				width: 70,
				align: 'center',
				sorttype: 'int',
				hidden: true
			},
			hidden = {
				hidden: true
			};
		result.push(id);
		_.each(gridDef.fields, function(x) {
			result.push((colNames[i++] == undefined) ? hidden : toModelItem(x));
		});
		return result;
	}
	var defaultFormatOptions = {
		currency: {
			decimalSeparator: ".",
			thousandsSeparator: ",",
			decimalPlaces: 2,
			prefix: ""
		}
	};
	var initDatepicker = function(elem) {
		$(elem).datepicker({
			dateFormat: 'yy/mm/dd',
			autoSize: true,
			changeYear: true,
			changeMonth: true,
			showButtonPanel: true,
			onSelect: function(dateText, inst) {
				if (this.id.substr(0, 3) === "gs_") {
					setTimeout(function() {
						grid[0].triggerToolbar();
					}, 50);
				} else {
					$(this).trigger('change');
				}
			}
		});
		$('.ui-datepicker').css({
			'zIndex': '1200',
			'font-size': '75%'
		});
		$(".ui-datepicker").draggable();
		$(".ui-datepicker").resizable();
	};
	var initROCDatepicker = function(elem) {
		$(elem).datepicker({
			dateFormat: 'yy/mm/dd',
			autoSize: true,
			changeYear: true,
			changeMonth: true,
			showButtonPanel: true,
			showWeek: false,
			onSelect: function(dateText, inst) {
				var ss = dateText.split('/');
				var newVal = '' + (parseInt(ss[0], 10) - 1911) + '/' + ss[1] + '/' + ss[2];
				$(elem).val(newVal);
			}
		});
	};
	// ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
	// ['等於 ', '不等於 ', '小於 ', '小於等於 ','大於 ','大於等於 ', '開始於 ','不開始於 ','等於 ','不等於 ','結束於 ','不結束於 ','包含
	// ','不包含 '],
	function decorateByType(type, o) {
		var more = {};
		switch (type) {
			case 'F':
				more = {
					searchoptions: {
						sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge'],
						dataInit: initDatepicker
					}
				};
				_.extend(o, more);
				break;
			case 'D':
				more = {
					searchoptions: {
						sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge'],
						dataInit: initROCDatepicker
					}
				};
				_.extend(o, more);
				break;
			case 'm':
			case 'n':
				more = {
					searchoptions: {
						sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge']
					}
				};
				_.extend(o, more);
				break;
			default:
				more = {
					searchoptions: {
						sopt: ['bw', 'bn', 'in', 'ni', 'ew', 'en', 'cn', 'nc']
					}
				};
				_.extend(o, more);
				break;
		}
	}

	function toModelItem(x) {
		var fld = fieldMap[x.name],
			numeric = (fld.type == 'M' || fld.type == 'm' || fld.type == 'n'),
			o = {
				name: x.name,
				index: x.name,
				sortable: true,
				canExport: true,
				align: (numeric ? 'right' : 'left'),
			};
		// 原本應該要給var設定,但怕sa遺漏,故還是幫忙加入
		// 別的型態是否也要幫忙擋住canExport ?
		if (x.opt && x.opt.formatter && x.opt.formatter.indexOf("bind-button") != -1) {
			o['canExport'] = false; // default 'text'
		}
		if (x.formatter == 'checkbox') alert("check:" + x.name);
		if (numeric) {
			o['sorttype'] = 'float'; // default 'text'
			o['formatter'] = 'currency';
			o['formatoptions'] = _.extend({}, defaultFormatOptions['currency']);
			o['formatoptions']['decimalPlaces'] = fld.dlen;
		}
		// console.log('decorate ' + x.name);
		decorateByType(fld.type, o);
		// console.dir(o);
		// console.dir(o.searchoptions);
		var opt = processOpt(_.extend({}, x.opt));
		return _.extend(o, opt);
	}
	var custFormatterMapper = {
		trimZero: function(x) {
			return x.replace(/^(0)+/, '');
		},
		ltrimZero: function(x) {
			return x.replace(/^(0)+?\d/, '');
		},
		rtrimZero: function(x) {
			return x.replace(/(0)+$/, '');
		},
		editpat: function(x, arr) {
			return editpatFormatter(x, arr.join(''));
		},
		action_link: function(x, arr, options, rowObject) {
			return actionFormater('link', x, arr, options, rowObject);
		},
		action_button: function(x, arr, options, rowObject) {
			return actionFormater('button', x, arr, options, rowObject);
		},
		chain: function(x, arr, options, rowObject) {
			return chainFormater(x, arr, options, rowObject);
		},
		'bind-button': function(x, arr, options, rowObject) {
			return bindFormater('button', x, arr, options, rowObject);
		},
		openNew: function(x, arr, options, rowObject) {
			return openNewFormatter(x, arr, options, rowObject);
		}
	};

	function editpatFormatter(value, pat) {
		var r = '',
			offset = 0;
		_.each(pat.split(''), function(x) {
			r += (x == '#') ? value.charAt(offset++) : x;
		});
		return r;
	}

	function bindFormater(uiType, cellvalue, argv, options, rowObject) {
		if (cellvalue.visible != undefined && cellvalue.visible == 0) return "";
		// 柯 如果沒有成功bind 則直接隱藏該欄位btn按鈕
		if (cellvalue == "") {
			return "";
		}
		var enabled = cellvalue.enabled != 0;
		var action = enabled ? ' button white chainAction' : ' button light-gray-disabled ';
		var tmplBegin = '<a class=" ' + action + ' query-link" href="javascript:void(0)"',
			text = cellvalue.text ? $.trim(cellvalue.text) : argv[0],
			tmplEnd = '\>' + text + '</a>',
			rowId = options['rowId'],
			pos = options['pos'],
			name = options.colModel.name,
			dataBag = '';
		var t = text.split(";");
		if (t.length > 1) tmplEnd = 'title="' + t[1] + '" \>' + t[0] + '</a>';
		// dataBag = grid.jqGrid('getCell',rowId, name);
		dataBag = ' data-bag="' + rowId + ',' + name + '" ';
		return tmplBegin + dataBag + tmplEnd;
	}

	function chainFormater(cellvalue, argv, options, rowObject) {
		var tmplBegin = '<a class="button white chainAction query-link" href="javascript:void(0)"',
			tmplEnd = '\>' + argv[0] + '</a>',
			tmplData = ' data-ntxcd = "' + cellvalue + '" ',
			i = 0,
			start = 3;
		tmplData += ' data-delete="' + argv[1] + '" ';
		tmplData += ' data-chain="' + argv[2] + '" ';
		for (i = start; i < argv.length; i++) {
			tmplData += 'data-buf-' + (i - start) + '="' + escape(rowObject[argv[i]]) + '" ';
		}
		tmplData += ' data-max-buf="' + (i - start) + '" ';
		tmplData += ' data-id="' + rowObject['id'] + '" ';
		var s = tmplBegin + tmplData + tmplEnd;
		console.log(s);
		return s;
	}
	// for BIND'ed Button
	function callChain($a) {
		var ss = $a.attr('data-bag').split(',');
		if (ss[0].indexOf("jqg") != -1) {
			ss[0] = ss[0].replace("jqg", "");
			ss[0] = parseInt(ss[0], 10);
			ss[0] = ss[0] + rId - 2;
			ss[0] += "";
		}
		var row = parseInt(ss[0], 10);
		var name = ss[1];
		var bag = data[row][name];
		var fn = getIfxFn('bind'); // get ifx registered function-->IFx.bindHandler()
		var once = fn(bag);
		if (once) {
			grid.jqGrid('delRowData', row);
		}
		return false;
	}

	function callChainOld($a) {
		// var dataBag = data[0]['#bind-Btn1'];
		var ntxcd = $a.attr('data-ntxcd'),
			deleteRow = $a.attr('data-delete').toLowerCase() == 'true' ? true : false,
			chain = $a.attr('data-chain'),
			rowid = $a.attr('data-id'),
			maxBuf = parseInt($a.attr('data-max-buf'), 10),
			ntxbuf = {},
			i = 1;
		if (maxBuf > 0) ntxbuf['NTXBUF$'] = unescape($a.attr('data-buf-0'));
		for (i = 1; i < maxBuf; i++) {
			ntxbuf['NTXBUF' + i + '$'] = unescape($a.attr('data-buf-' + i));
		}
		console.log('row id:' + rowid);
		console.log('ntxcd:' + ntxcd);
		console.log('ntxbuf:');
		console.dir(ntxbuf);
		var fn = getIfxFn('chain'); // get ifx registered function
		fn(ntxcd, chain, ntxbuf);
		if (deleteRow) {
			grid.jqGrid('delRowData', rowid);
		}
		return false;
	}

	function makepair(s, tok) {
		var o = {},
			ss, tok = tok || '=';
		ss = s.split(tok);
		o.fst = _(ss[0]).trim();
		o.snd = _(ss[1]).trim();
		return o;
	}

	function openNewFormatter(cellvalue, argv, options, rowObject) {
		// var html = '<a class="button white query-link" href="' + rowObject[p.fst] + '"\>' + p.snd
		// + '</a>';
		var o = argv2map(argv, rowObject),
			text = o['text'],
			page = o['page'],
			action = makeHref(page, o);
		html = '<a class="button white query-link" target="_blank" href="' + action + '"\>' + text + '</a>';
		return html;

		function makeHref(page, o) {
			var s = '',
				k = null;
			delete o['text'];
			delete o['page'];
			for (var k in o) {
				s = s + k + '=' + encodeURI(o[k]) + '&';
			}
			return page + '?' + s;
		}
	}

	function argv2map(argv, rowObject) {
		var o = {},
			p;
		_.each(argv, function(x) {
			p = makepair(x);
			if (p.snd.charAt(0) == '#') p.snd = rowObject[p.snd];
			o[p.fst] = p.snd;
		});
		return o;
	}

	function drawLink(p, rowObject) {
		return '<a class="button white query-link" href="' + rowObject[p.fst] + '"\>' + p.snd + '</a>';
	}

	function drawButton(p, rowObject) {
		return '<a class="query-button" href="' + rowObject[p.fst] + '"\>' + p.snd + '</a>';
	}

	function actionFormater(uiType, cellvalue, argv, options, rowObject) {
		var drawer = (uiType == 'link') ? drawLink : drawButton,
			result = _.map(argv, function(x) {
				return drawer(makepair(x), rowObject);
			});
		return result.join('');
	}

	function getCustFormatter(x, arr) {
		var realFn = custFormatterMapper[x];
		if (realFn == null) {
			alert("no such cust:formatter:" + x);
			return null;
		}
		var fn = function(cellvalue, options, rowObject) {
			return realFn(cellvalue, arr, options, rowObject);
		};
		return fn;
	};

	function processOpt(opt) {
		var ss;
		opt = opt || {};
		if (opt.formatter && opt.formatter.indexOf('cust:') == 0) {
			ss = opt.formatter.split(':');
			opt.formatter = getCustFormatter(ss[1], ss.slice(2));
		}
		return opt;
	}

	function trimZero(cellvalue, options, rowObject) {
		return cellvalue.replace(/^(0)+/, "");
	}

	function dateFormatter(cellvalue, options, rowObject) {
		if (!isNaN(cellvalue) && parseInt(cellvalue, 10) == 0) return "";
		return IfxUtl.dateFormatter(cellvalue);
	}
	// 新增 ifxDisplay: 參數 給是否隱藏ifxBatch
	function chkifxDisplay(ifxDisplay) {
		var getValuefn = getIfxFn('getValue');
		if (ifxDisplay != undefined && ifxDisplay != "1") {
			cfg.ifxBatch = null;
			console.log("Close cfg.ifxBatch!");
		}
	}
	var mydata;

	function initGrid(controls) {
		// var ie = $.browser.msie ? true:false; // IE cause some problem
		var ie = /MSIE/.test(navigator.userAgent);
		var oldFrom = $.jgrid.from,
			lastSelected;
		$.jgrid.from = function(source, initalQuery) {
			var result = oldFrom.call(this, source, initalQuery),
				old_select = result.select;
			result.select = function(f) {
				lastSelected = old_select.call(this, f);
				return lastSelected;
			};
			return result;
		};
		colNames = _.pluck(gridDef.fields, 'caption');
		var getValuefn = getIfxFn('getValue');
		// 柯 新增 grid 欄位名稱可帶入變數
		$.each(colNames, function(i, x) {
			if (x) {
				var fldid = x.toString().trim();
				if (fldid.slice(0, 1) == "#") {
					colNames[i] = getValuefn(fldid);
				}
			}
		});
		colNames.unshift('id');
		var pager = controls.pager;
		var rowList = [];
		if (ie) {
			colModel = makeModel(colNames);
			_.each(cfg.rowList, function(r) {
				rowList.push(r);
			});
		} else {
			if (colModel == undefined) colModel = makeModel(colNames);
			rowList = cfg.rowList;
		}
		_.each(colModel, function(o) {
			console.dir(o);
			console.dir(o.searchoptions);
		});
		mydata = [];
		_.each(data, function(d) {
			mydata.push(d);
		});
		/* 潘 為了折返後追加之前的資料長度 */
		rId = mydata.length;
		var footerRowWanted = false;
		$.each(colModel, function(i, x) {
			if (x.sum) {
				footerRowWanted = true;
				return false;
			}
		});
		var setwidth;
		if (cfg.width > 800) {
			if (cfg.width < ($(window).width() - ($(window).width() / 6))) {
				setwidth = cfg.width;
			} else {
				setwidth = $(window).width() - ($(window).width() / 6);
			}
		} else {
			setwidth = cfg.width;
		}
		// 檢測是否要關閉 cfg.ifxBatch
		chkifxDisplay(cfg.ifxDisplay);
		// 檢測是否為特殊需要加總的交易
		if (cfg.crdsum) {
			_specialsum = true;
		}
		grid.jqGrid({
			datatype: "local",
			data: mydata, // ie bug, when pass data to another window.
			colNames: colNames,
			colModel: colModel,
			rowNum: cfg.rowNum,
			rowList: rowList,
			//width: setwidth,// //不管VAR設定,超過就自動調整
			width: $(window).width() - 150,
			pager: pager,
			pginput: true,
			gridview: true,
			rownumbers: true,
			viewrecords: true,
			//shrinkToFit: (cfg.width > 800) ? false : cfg.shrinkToFit || true,
			shrinkToFit: false,
			autoencode: true,
			multiSort: true, // ke 新增
			sortname: cfg.sortname ? (cfg.sortname) : null,
			resize: true,
			caption: cfg.caption,
			height: cfg.height || '100%',
			multiselect: cfg.ifxBatch || false,
			hiddengrid: cfg.hiddengrid,
			footerrow: footerRowWanted,
			loadComplete: function(data) {
				// TODO 可能需要限定不能取消勾選
				/*
				 * var cbs = $("tr.jqgrow > td > input.cbox:even", grid[0]); cbs.attr("disabled",
				 * "disabled");
				 */
				$(".ui-pg-selbox option[value='All']").val(9999999);
				this.p.lastSelected = lastSelected; // set this.p.lastSelected
				injectBtnActions();
			},
			beforeSelectRow: function(id, e) {
				if (cfg.setSelectAll && cfg.noCancel) {
					return false;
				}
				return true;
			},
			onSelectRow: function(id) {
				if (_mapBack) { // 潘
					var fn = getIfxFn('mapBack');
					if(id.indexOf("jqg") != -1){
						var ss = id.replace("jqg", "");
						ss = parseInt(ss, 10);
						ss = ss + rId - 2;
						id = ss;
					}
					fn(_mapBack, mydata[id]);
				}
				// 給特殊雙表格使用
				if (_specialsum) {
					setCrdbenAmt();
				}
				// grid.jqGrid('setSelection',id, true);
			}, // 給特殊雙表格使用
			onSelectAll: function(id, status) {
				if (_specialsum) {
					setCrdbenAmt();
				}
			},
			onSortCol: function() {},
			gridComplete: function() {
				// injectBtnActions();
				if (footerRowWanted) {
					var colName, colSum, nv;
					$.each(colModel, function(i, x) {
						if (!x.sum) return true;
						colName = x.name;
						nv = {};
						if (x.sum === true) {
							colSum = grid.jqGrid('getCol', colName, false, 'sum');
							nv[colName] = colSum;
							grid.jqGrid('footerData', 'set', nv);
						} else {
							nv[colName] = x.sum;
							grid.jqGrid('footerData', 'set', nv);
						}
					});
				}
				// 柯 新增 自動全選
				if (cfg.setSelectAll && cfg.ifxBatch) {
					grid.jqGrid('resetSelection');
					var ids = grid.getDataIDs();
					for (var i = 0, il = ids.length; i < il; i++) {
						grid.jqGrid('setSelection', ids[i], true);
					}
				}
				// 柯 新增 依據欄位實作勾選 勾選預設值
				if (cfg.setSelect && cfg.ifxBatch) {
					var allRowsInGrid = grid.jqGrid('getGridParam', 'data');
					var thisdata = grid.jqGrid('getRowData');
					$.each(allRowsInGrid, function(i, x) {
						if (x[cfg.setSelect].toUpperCase() == "Y") {
							grid.jqGrid('setSelection', x.id, true);
						}
					});
				}
				if (cfg.setSelectAll && cfg.noCancel && cfg.ifxBatch) {
					// 新增不能取消單獨資料
					var cbs = $("tr.jqgrow > td > input.cbox", grid);
					cbs.attr("disabled", "disabled");
				}
			}
		});
		// grid.jqGrid('clearGridData');
		// grid.clearGridData();
		// ie bug,
		// if(ie){
		// for(var i=0;i<=data.length;i++)
		// grid.jqGrid('addRowData',i+1,data[i]);
		// }
		var navGridCfg = {
			search: cfg.search,
			refresh: cfg.refresh,
			edit: false,
			add: false,
			del: false
		};
		grid.jqGrid('navGrid', pager, navGridCfg);
		if (cfg.filter) {
			grid.jqGrid('filterToolbar', {
				searchOnEnter: true,
				stringResult: true,
				defaultSearch: 'cn'
			});
		}
		var pos = $(controls.search).position();
		grid.jqGrid('searchGrid', {
			multipleSearch: true,
			showOnLoad: false,
			// top:pos.top-100,
			// left:pos.left + 100,
			modal: false,
			loadOnStart: false,
			resize: true,
			beforeShowSearch: function($form) {
				// because beforeShowSearch will be called on all open Search Dialog,
				// but the old dialog can be only hidden and not destroyed
				// we test whether we already bound the 'keydown' event
				var events = $('.vdata', $form).data('events'),
					event;
				for (event in events) {
					if (events.hasOwnProperty(event) && event === 'keydown') {
						return;
					}
				}
				$('.vdata', $form).keydown(function(e) {
					var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
					if (e.which == 13) { // save
						// $(".ui-search", $form).click();
						grid[0].SearchFilter.search();
					}
				});
			}
		});
		// grid.jqGrid('setFrozenColumns');
		grid.jqGrid('gridResize', {
			minWidth: 450,
			minHeight: 150
		});
		// grid.jqGrid('setGridParam',{page:2}).trigger("reloadGrid");
		// first page can't freeze columnes, call reloadGrid to fix this bug
		grid.jqGrid('setGridParam', {
			page: 1
		}).trigger("reloadGrid");
		// grid.draggable();
		// .jqGrid('sortGrid', '#OAVBAL', true, 'desc')
		// .jqGrid('sortGrid', '#OCLSDAY', true, 'desc');
		// grid.draggable();
		// inject actions
		/*
		 * $('.chainAction').on('click',function(){ callChain($(this)); });
		 */
		changeFont();
	}

	function changeFont() {
		if (cfg.font) {
			if (cfg.font.title) {
				$('.ui-jqgrid .ui-jqgrid-title').css('font-size', cfg.font.title); /* 修改grid標題的字體大小 */
			}
			if (cfg.font.caption) {
				$('.ui-jqgrid-sortable').css('font-size', cfg.font.caption); /* 修改列名的字體大小 */
			}
			if (cfg.font.cell) {
				$('.ui-jqgrid tr.jqgrow td').css('font-size', cfg.font.cell); /* 修改表格內容字體 */
			}
			if (cfg.font.footer) {
				$('.ui-jqgrid tr.footrow td').css('font-size', cfg.font.footer); /* 修改表格footer內容字體 */
			}
		}
	}

	function doclick(controls) {
		if (cfg.ifxBatch && cfg.doclick) {
			console.log("自動送出列印");
			$(controls.batch).click();
		}
	}

	function initButtons(controls) {
		// 柯 增 start
		for(var i = 0; i < 10; i++)
			  $(controls.batch + i).hide();

		if (cfg.ifxBatch) {
			var btnName = cfg.ifxBatch.name.split(";");
			var btnIndex = 0;

			btnName.forEach((x) => {
				$(controls.batch + btnIndex).attr("disabled", false);
			  $(controls.batch + btnIndex).attr("style", "color:blue");
			  $(controls.batch + btnIndex).show();
			  btnIndex++;
            })
			// $('#btn_yn').hide();
		}else
			for(var i = 0; i < 10; i++)
			  $(controls.batch + i).hide();


		// end
		if (cfg.search) {
			$(controls.search).button().show();
			$(controls.search).off("click").click(function() {
				grid.jqGrid('searchGrid', {
					sopt: ['cn', 'bw', 'eq', 'ne', 'lt', 'gt', 'ew']
				});
			});
			$(controls.reset).button().show();
			$(controls.reset).off("click").click(function() {
				grid.jqGrid('setGridParam', {
					search: false
				});
				// for singe search you should replace the line with
				// $.extend(postData,{searchField:"",searchString:"",searchOper:""});
				grid.trigger("reloadGrid", [{
					page: 1
				}]);
			});
		} else {
			$(controls.search).hide();
			$(controls.reset).hide();
		}
		if (cfg.viewInNewWindow) {
			if (controls.newWin) {
				$(controls.newWin).button().show();
				$(controls.newWin).off("click").click(function() {
					var $jq = $(this),
						url = $jq.attr('data-url');
					var settings = 'menubar=no, toolbar=no, location, directories, status, scrollbars, resizable, dependent, width=1400, height=900, left=0, top=0';
					window.open(url, 'gridWin', settings);
					return false;
				});
			}
		} else {
			$(controls.newWin).hide();
		}
		// KE:目前功能只FOR表格1使用 匯出 XML檔案
		if (cfg.exportXml && controls.exportXml && controls.exportXml.slice(-1) != "2") {
			$(controls.exportXml).button().show();
			$(controls.exportXml).off("click").click(function() {
				var dataFromGrid = {
					gridRow: grid.jqGrid('getGridParam', 'data')
				};
				// 柯 新增存檔路徑
				var selectFolder = selecttoFolder();
				if (!selectFolder) {
					return;
				}
				// XE973 特殊規格 友達獨立輸出
				var ex2eachxml = cfg.ex2eachxml;
				if (ex2eachxml) {
					$.each(dataFromGrid.gridRow, function(i, x) {
						makexmldata([x], (i + 1).toString(), parseInt(ex2eachxml, 10)); // 柯:
						// 給他括號
						// 統一(typeof(v)
						// ===
						// "object")邏輯
					});
				} else {
					makexmldata(dataFromGrid.gridRow);
				}
				alert('檔案轉換完畢。(路徑: ' + selectFolder + "\\docs)");
				// 輸出成xml
				function makexmldata(dataFromGridData, num, notify) {
					var xmldata = '<?xml version="1.0" encoding="big5" ?>\n<LC>\n' + myXmlJsonClass(dataFromGridData, '\t', notify) + '</LC>';
					var deviceX = getIfxFn('deviceX');
					var folderUtil = deviceX.folder;
					var fileUtil = deviceX.file;
					var p = folderUtil.combine(selectFolder, "docs");
					folderUtil.create(p);
					var exportname = "#exportname";
					var getValuefn = getIfxFn('getValue');
					var getFieldfn = getIfxFn('getField');
					var fld = getFieldfn(exportname, true);
					var filenum = num ? ("-" + IfxUtl.numericFormatter(num, 3)) : "";
					var name = null;
					if (fld) {
						name = getValuefn(exportname).trim() + filenum + ".xml";
					} else {
						name = IfxUtl.getToday() + "-" + IfxUtl.getTimeString() + filenum + ".xml";
					}
					p = folderUtil.combine(p, name);
					fileUtil.write(p, IfxUtl.formatXml(xmldata.replace(/	/g, "")));
					console.log('saved to ' + p);
				}
			});
		} else {
			$(controls.exportXml).hide();
		}
		// KE:目前功能只FOR表格1使用 匯出 EXCEL檔案
		if (cfg.exportExcel && controls.exportExcel && controls.exportExcel.slice(-1) != "2") {
			$(controls.exportExcel).button().show();
			$(controls.exportExcel).off("click").click(function() {
				var $jq = $(this),
					url = $jq.attr('data-url');
				var a = getExportData();
				if ($(controls.queryHeader).is(":visible")) {
					var temp2 = null;
					temp2 = $(controls.queryHeader + ' tbody  tr').map(function() {
						// $(this) is used more than once; cache it
						// for performance.
						var $row = $(this);
						// For each row that's "mapped", return an
						// object that
						// describes the first and second <td> in
						// the row.
						var result = [],
							result2 = [];
						var datahead = $row.find(':nth-child(1)').text();
						// var datatext =
						// $row.find(':nth-child(2)').find(':input').val();
						var datatext = "";
						$row.find(':nth-child(2)').children().each(function() { // 柯:右邊只找"欄位"(INPUT)
							datatext += $(this).val() || $(this).text();
						});
						var datahead2 = $row.find(':nth-child(3)').text();
						var datatext2 = "";
						$row.find(':nth-child(4)').children().each(function() { // 柯:右邊只找"欄位"(INPUT)
							datatext2 += $(this).val() || $(this).text();
						});
						if (datahead) {
							datahead = datahead.trim();
							result2.push(datahead);
						}
						if (datatext) {
							datatext = datatext.trim();
							result2.push(datatext);
						}
						if (datahead2) {
							datahead2 = datahead2.trim();
							result2.push(datahead2);
						}
						if (datatext2) {
							datatext2 = datatext2.trim();
							result2.push(datatext2);
						}
						result.push(result2);
						return result;
					}).get();
					// var text2 = getGridHeader($(controls.queryHeader).html());
					console.dir(temp2);
					temp2 = temp2.slice(1);
					var alen = a[0].length;
					var tmplen = temp2.length;
					console.log(JSON.stringify(temp2));
					for (var j = 0; j < tmplen; j++) {
						for (var i = tmplen; i < alen; i++) {
							temp2[j].push(' ');
						}
						temp2[j].push("HaderX");
					}
					console.log(JSON.stringify(a));
					a = temp2.concat(a);
					console.log(JSON.stringify(a));
				}
				//
				// _.each(a, function(b){
				// console.dir(b);
				// });
				// a = [["abc1","def1","ghi1"],["abc2","def2","ghi2"]];
				// TODO 確認到底有幾種換行符號?
				s = encodeURI(JSON.stringify(a).replace(/\<br\>/g, '\n').replace(/\<\/br\>/g, '\n').replace(/\<br\/\>/g, '\n').replace(/\<\/BR\>/g, '\n').replace(/\<BR\/\>/g, '\n').replace(/\4/g, '').replace(/\7/g, ''));
				var fn = getIfxFn('getValue'),
					outputtitle;
				if (cfg.caption.length == 0) {
					outputtitle = fn("#SCRTIL");
				} else {
					outputtitle = cfg.caption;
				}
				var form = $('<form action="ExportServlet" method="post" accept-charset="utf-8"></form>'); // 調用一個SERVLET
				form.append('<input type="text" name="oper" value="excel"/>');
				form.append('<input type="text" name="title" value="' + encodeURI(outputtitle.trim()) + '"/>');
				// form.append('<input type="text" name="title" value="' +
				// "ifx_export" + '"/>');
				form.append('<input type="text" name="data" value="' + s + '"/>');
				form.appendTo("body");
				form.css('display', 'none');
				form.submit();
				return false;
			});
		} else {
			$(controls.exportExcel).hide();
		}
		if (getIfxFn('moreData')) {
			var $btnMoreData = $(controls.moreData);
			$btnMoreData.button().show();
			setTimeout(function() {
				$btnMoreData.effect('pulsate', {}, 2000);
			}, 0);
			$btnMoreData.off("click").click(function() {
				var fn = getIfxFn('moreData');
				fn();
			});
		} else {
			$(controls.moreData).hide();
		}
		if (cfg.ifxBatch) {
			$(controls.search).hide();
			$(controls.reset).hide();
			// 原調整為關閉,2016/06/28又開啟
			// $(controls.exportExcel).hide();
			// 柯應該是這邊有問題 20150515
			if (!cfg.ifxBatch.name) {
				$(controls.batch).hide();
				return;
			}
			var btnName = cfg.ifxBatch.name.split(";");
			//			if(btnName.length > 1){
			//				var btnIndex = 0;
			//				_.each(btnName, function(y){
			//					btnIndex++;
			//					if(btnIndex != 1)
			//					  $(controls.batch + "_" + btnIndex).val(y).button().show();
			//				});
			//			}
			var btnIndex = 0;
			btnName.forEach((x) => {
				$(controls.batch + btnIndex).val(x).button().show().off("click").click(function(btn) {
					var ss = grid.jqGrid('getGridParam', 'selarrrow');
					var selectedData = [];
					var len = ss.length;
					var rowstatus = "#STATUS"; // 柯 新增 for 上傳交易的每行狀態
					var cango = true; // 柯 新增 for 上傳交易的每行狀態
					// 柯 無任何沒有資料時
					if (len == 0) {
						alert("請勾選資料。");
						return;
					}
					// 特殊交易加總金額時，需要金額相等
					if (_specialsum) {
						var fn = getIfxFn('getValue');
						var daiamtAll = fn("#DAIAMT"); // 總TOTAL 貸
						var jieamtAll = fn("#JIEAMT"); // 總TOTAL 借
						if (daiamtAll != jieamtAll) {
							alert("借貸金額不平!");
							return;
						}
					}
					// 20171222 待測試
					$.each(ss, function(i, x) {
						$.each(mydata, function(j, v) {
							if (v.id == x) {
								selectedData.push(v);
							}
						});
					});
					for (var i = 0; i < selectedData.length; i++) selectedData[i]["btnFild"] = btn.target.id.substring(btn.target.id.length - 1);
					// $.each(ss,function(i,x){
					// selectedData.push(mydata[x]);
					// });
					$.each(selectedData, function(i, x) {
						console.dir(x);
						// 柯 新增 for 上傳交易的每行狀態 start
						if (cfg.ifxBatch.type == "5") {
							if (x[rowstatus] && x[rowstatus].trim() != "成功") {
								cango = false;
							}
						}
						// end
					});
					// 柯 新增 for 上傳交易的每行狀態
					if (cfg.ifxBatch.type == "5" && !cango) {
						alert("檢查不通過，無法上傳。");
						return;
					}
					// 柯 看是否有需要 整批要可以連按??? start
					// cfg.ifxBatch.type == 9 可以連按?
					$(this).attr("disabled", true);
					$(this).attr("style", "color:gray"); // 柯 新增
					// end
					var fn = getIfxFn('batchProcessor');
					fn(cfg, selectedData, mydata.length, ss);
					// 此段 更改成 成功時才刪除表格 在ifx-core2.js
					// for 多筆時刪除表格
					// for(var i = 0;i < len ;i ++) {
					// grid.jqGrid("delRowData", ss[0]);
					// }
				});
				btnIndex++;
			})
		} else {
			$(controls.batch).hide();
		}
	}

	function getExportData() {
		var captions = [],
			names = [],
			m, type = [];
		for (var i = 0; i < colModel.length; i++) {
			m = colModel[i];
			if (m.hidden != true && m.canExport == true) {
				captions.push(colNames[i]);
				names.push(m.name);
				type.push(fieldMap[m.name]);
			}
		}
		var result = _.map(data, function(d) {
			// return _.pick(d, names);
			var r = [];
			_.each(names, function(n) {
				// r.push((d[n] != null) ? d[n].replace(/\4/g, '').replace(/\7/g, ''):"");
				// //移除表格匯出時,var沒有使用c型態時所產生的問題
				r.push(d[n]);
			});
			return r;
		});
		var r = [];
		_.map(type, function(d) {
			r.push(d.type);
		});
		result.push(r);
		result.unshift(captions);
		return result;
	}

	function myXmlJsonClass(o, tab, notify) {
		var toXml = function(v, name, ind, notify) {
			var xml = "";
			var i, n;
			if (v instanceof Array) {
				if (v.length === 0) {
					xml += ind + "<" + name + ">__EMPTY_ARRAY_</" + name + ">\n";
				} else {
					for (i = 0, n = v.length; i < n; i += 1) {
						var sXml = ind + toXml(v[i], name, ind + "\t") + "\n";
						xml += sXml;
					}
				}
			} else if (typeof(v) === "object") {
				var hasChild = false;
				if (!notify) {
					xml += ind + "<" + name;
				}
				var m;
				var count = 0;
				for (m in v)
					if (v.hasOwnProperty(m)) {
						if (m.charAt(0) === "@") {
							xml += " " + m.substr(1) + "=\"" + v[m].toString() + "\"";
						} else {
							hasChild = true;
						}
					}
				if (!notify) {
					xml += hasChild ? ">" : "/>";
				}
				if (hasChild) {
					var colnum = 0;
					for (m in v)
						if (v.hasOwnProperty(m)) {
							if (notify - 1 == colnum) { // 起始0
								xml += ind + "<Notify>";
							}
							var colnamed = ""; // 欄位名稱
							var temppp = colModel[colnum];
							// 同EXCEL輸出邏輯
							if (temppp.hidden == true || temppp.canExport != true) {
								colnum++;
								continue;
							} else {
								colnamed = colNames[colnum];
								var fldid = colnamed.toString().trim();
								if (fldid.slice(0, 1) == "#") {
									var fn = getIfxFn('getValue');
									colnamed = fn(fldid);
								}
							}
							v[m] = v[m].toString();
							if (m === "#text") {
								xml += v[m];
							}
							// 柯:XE973給中心直接組內容,不幫忙切欄位
							// else if (v[m].charAt(0) === "<") {
							// xml += toXml("<![CDATA[" + v[m] + "]]>", m, ind+"\t");
							// }
							else if (m === "#cdata") {
								xml += "<![CDATA[" + v[m] + "]]>";
							} else if (m.charAt(0) !== "@") {
								xml += toXml(v[m], colnamed, ind + "\t");
							}
							colnum++;
						}
					xml += (xml.charAt(xml.length - 1) === "\n" ? ind : "");
					if (!notify) {
						xml += "</" + name + ">";
					} else {
						xml += "</Notify>";
					}
					xml += "\n";
				}
			} else if (typeof(v) === "function") {
				xml += ind + "<" + name + ">" + "<![CDATA[" + v + "]]>" + "</" + name + ">";
			} else {
				if (v.toString() === "\"\"" || v.toString().length === 0) {
					xml += ind + "<" + name + "></" + name + ">"; // 空欄位
				} else {
					xml += ind + "<" + name + ">" + v.toString() + "</" + name + ">";
				}
			}
			return xml;
		};
		var xml = "";
		var m;
		for (m in o)
			if (o.hasOwnProperty(m)) {
				xml += toXml(o[m], m, "", notify);
			}
		return tab ? xml.replace(/\t/g, tab) : xml.replace(/\t|\n/g, "");
	}
	// 同 ifx-core2.js
	function selecttoFolder() {
		try {
			var Message = "請選擇存檔路徑"; // 選擇框提示信息
			var Shell = new ActiveXObject("Shell.Application");
			var Folder = Shell.BrowseForFolder(0, Message, 0x0040, 17); // 起始目錄為
			// 17我的電腦
			// var Folder = Shell.BrowseForFolder(0,Message,0); //起始目錄為：桌面
			if (Folder != null) {
				// Folder = Folder.Items().Item().Path;
				// Folder = Folder.items(); // 返回 FolderItems 對象
				// Folder = Folder.item(); // 返回 Folderitem 對象
				Folder = Folder.Self.Path; // 返迴路徑
				// if(Folder.charAt(Folder.length-1) != "\\"){
				// Folder = Folder + "\\";
				// }
			}
		} catch (e) {
			// fix選擇桌面時的bug
			console.log(e.message)
			alert(e.message);
			return undefined;
		}
		return Folder;
	}
	// 給特殊雙表格使用
	function setCrdbenAmt() {
		try {
			var grid, addData, ocrdben = "";
			var jieamt = 0,
				daiamt = 0;
			var jieamt1 = 0,
				daiamt1 = 0;
			var jieamt2 = 0,
				daiamt2 = 0;
			// 柯 新增 指定執行一個欄位的call
			// var fnrtncall = getIfxFn('rtnCall');
			// fnrtncall("#RTNCALL","#RTNCALL");
			// end
			for (var j = 0; j < 2; j++) {
				if (j == 0) {
					grid = $("#grid");
				} else {
					grid = $("#grid2");
				}
				addData = grid.jqGrid('getGridParam', 'selarrrow'); // 可以獲取到選中的行數組。
				// 如果沒有第二個資料FOR XA98V。
				if (!addData) {
					break;
				}
				for (var i = 0; i < addData.length; i++) {
					ocrdben = grid.getCell(addData[i], "#OCRDBEN");
					if (ocrdben == "D") {
						jieamt += parseFloat(grid.getCell(addData[i], "#OTXAMT"));
						if (j == 0) {
							jieamt1 += parseFloat(grid.getCell(addData[i], "#OTXAMT"));
						} else {
							jieamt2 += parseFloat(grid.getCell(addData[i], "#OTXAMT"));
						}
					} else if (ocrdben == "C") {
						daiamt += parseFloat(grid.getCell(addData[i], "#OTXAMT"));
						if (j == 0) {
							daiamt1 += parseFloat(grid.getCell(addData[i], "#OTXAMT"));
						} else {
							daiamt2 += parseFloat(grid.getCell(addData[i], "#OTXAMT"));
						}
					}
				}
			}
			daiamt = daiamt.toFixed(2);
			jieamt = jieamt.toFixed(2);
			daiamt1 = daiamt1.toFixed(2);
			jieamt1 = jieamt1.toFixed(2);
			daiamt2 = daiamt2.toFixed(2);
			jieamt2 = jieamt2.toFixed(2);
			var fn = getIfxFn('setValue');
			fn("#DAIAMT", daiamt);
			fn("#JIEAMT", jieamt);
			fn("#DAIAMT1", daiamt1);
			fn("#JIEAMT1", jieamt1);
			fn("#DAIAMT2", daiamt2);
			fn("#JIEAMT2", jieamt2);
			// alert("daiamt:"+daiamt+",jieamt:"+jieamt);
			// code to try
		} catch (e) {
			alert(e);
			console.error("setCrdbenAmt:" + e);
			// handle errors here
		}
	}
	// end of Module IfxGrid
	return Grid;
}());

var IfxCall = (function(){
	var ifx=null;
	var _selt = this;
	function Fun(theIfx) {
		ifx = theIfx;
	}
	
	function name2id(name) {
		return name.replace(/#/,"#fld_");
	}
	Fun.prototype.handle = function(args) { 
		var funname = args[1];
		//var method = this[funname.toLowerCase()];
		this[funname.toLowerCase()](args);
	};
	
	Fun.prototype.calendar_easyui = function(args) {
		name = args[0],
		id = name2id(name);//.replace(/#/,"#fld_");
		
		$(id).addClass("easyui-datebox");
		$(id).datebox();

	};
	
	Fun.prototype.calendar = function() {
		
		
		var old_generateMonthYearHeader = $.datepicker._generateMonthYearHeader;
	var old_get = $.datepicker._get;
	var old_CloseFn = $.datepicker._updateDatepicker;
	$.extend($.datepicker, {
		_generateMonthYearHeader: function(a, b, c, d, e, f, g, h) {
			var htmlYearMonth = old_generateMonthYearHeader.apply(this, [a, b, c, d, e, f, g, h]);
			if ($(htmlYearMonth).find(".ui-datepicker-year").length > 0) {
				htmlYearMonth = $(htmlYearMonth).find(".ui-datepicker-year").find("option").each(function(i, e) {
					if (Number(e.value) - 1911 > 0) $(e).text(Number(e.innerText) - 1911);
				}).end().end().get(0).outerHTML;
			}
			return htmlYearMonth;
		},

		_get: function(a, b) {
			a.selectedYear = a.selectedYear - 1911 < 0 ? a.selectedYear + 1911 : a.selectedYear;
			a.drawYear = a.drawYear - 1911 < 0 ? a.drawYear + 1911 : a.drawYear;
			a.curreatYear = a.curreatYear - 1911 < 0 ? a.curreatYear + 1911 : a.curreatYear;
			return old_get.apply(this, [a, b]);
		},

		_updateDatepicker: function(inst) {
			old_CloseFn.call(this, inst);
			$(this).datepicker("widget").find(".ui-datepicker-buttonpane").children(":last")
				.click(function(e) {
					inst.input.val("");
				});
		},

		_setDateDatepicker: function(a, b) {
			if (a = this._getInst(a)) {
				this._setDate(a, b);
				this._updateDatepicker(a);
				this._updateAlternate(a)
			}
		},

		_widgetDatepicker: function() {
			return this.dpDiv
		}
	});
		/*
		$(id).on('blur', function() {
    		$(id).datepicker('hide');
		});
		$(id).datepicker({
			  showOtherMonths: true,
			   showButtonPanel: true, 
			   autoSize: true, 
	            gotoCurrent: true,
	            dateFormat:"yymmdd", 
	            onSelect: function(date, datepicker) {
	            	if(date) {
	            		ifx.KeyForward('datepicker');
	            	}
	            }
		}).keydown(function(event) {

			if (event.which === $.ui.keyCode.ENTER) {
				event.preventDefault();
			}
		});
		*/
	};
	Fun.prototype.combo = function(args) {
		var self=this,
			name = args[0],
			id = name2id(name),
			data = [['0', '????????????'], ['1', '?????????'], ['2', '?????????']];
		

		$(id).keydown(function(evt){
			
			
			if(event.which== 40) {
				console.log("down pressed");
				return false;
				
			}
		});
	};
	//???: ?????? for XX004??????
	Fun.prototype.check_tabs=function(args){
		args.shift(); // currentfield
		args.shift(); // check_tabs		
		var temp = ifx.gettabs();

		console.log("temp.length:"+temp.length);
		if(temp.length > 2 && !confirm(args.shift())){
			try {
//				if(deviceX.checkDocument())
//					deviceX.eject();
//				else
					deviceX.returnSession();//??? 20171230
			} catch (ee) {
			}
			 ifx.closetab();
		}else {
			throw ifx_makeSkip();
		}
	};	
	Fun.prototype.show = function(args) {
		args.shift(); // currentfield
		args.shift(); // show
		
		var pname=easyValue(args.shift());//??? ????????????????????????
		var visible="0";
		var dc = findDcDef();
		if(args.length <=1 ){  // show/hide whole dc 
			visible = args.shift() || "0";
			visible = easyValue(visible);   //can't not togather ,???????????????
			hideOrShow("#p_" + pname);
			if(dc!=null) {  // hide/show whole dc
				dc.visible = visible == "1";
			}else {
				// hide/show grid row
				//INVOKEJS(SHOW,grd9_1,0),
				if(pname.match(/grd(.)+_\d+/) != null) {
					ifxUI.hideGridRow(pname,visible);
				}else {
           var ss = pname.split("_");
           pname = ss[0];
           var pnum = parseInt(ss[1],10);
           dc = findDcDef();
           if(dc != null ){
           var r = dc['hideRows'] || (dc['hideRows']=[]);
           if(visible=='1') {
					   if(r.indexOf(pnum) != -1) {  // the row was set to hide
					   	r.splice(r.indexOf(pnum),1); // remove this row from hideRows
					   }
				     }else {
					   if(r.indexOf(pnum) == -1) {  // the hidden row is not in hideRows
					   	r.push(pnum); // add this row to hideRows
					   }
				   }
         }
				}
			}
		}
		else {
			var from ,to,
			 visible ,
				rows=[];
			 from = parseInt(easyValue(args.shift()),10);//??? ????????????????????????
			 to = parseInt(easyValue(args.shift()),10);//??? ????????????????????????
			 visible = args.shift() || "0";//can't not togather
       visible = easyValue(visible); //can't not togather ,???????????????

			//dc['rowVisible']  = dc['rowVisible'] || {};
			if(dc==null) {
				// grid mode
				for(var i=from; i <=to; i++) {
					var gRow = "#p_" + pname  + '_'+ i
					hideOrShow(gRow);
					ifxUI.hideGridRow(gRow,visible);
				}
				return;
			}
			// dc mode
			//??????  ??????????????? 
			var r = dc['hideRows'] || (dc['hideRows']=[]);
			//var r = (dc['hideRows']) ? dc['hideRows'] : (dc['hideRows']=[]);
			//var r = dc['hideRows'] || [];
			
			for(var i=from; i <=to; i++) {
				rows.push('#p_' + pname + '_'+ i);
				// save hide row
				if(visible=='1') {
					if(r.indexOf(i) != -1) {  // the row was set to hide
						r.splice(r.indexOf(i),1); // remove this row from hideRows
					}
				}else {
					if(r.indexOf(i) == -1) {  // the hidden row is not in hideRows
						r.push(i); // add this row to hideRows
					}
				}
			}
			hideOrShow(rows.join(','));
			
		}
		

		function hideOrShow(jqSelector){
			if(visible=="1")
				$(jqSelector).show();
			else 
				$(jqSelector).hide();
		}
		
		function findDcDef() {
			for(var i=0; i < ifx.def.display.length; i++){
				if(ifx.def.display[i].name == pname) return ifx.def.display[i]; 
			}
			//return {}; // null for grid grid    ????????????
      return null; // ???????????????
		}
		
	};
	//??? ????????????????????????
  function easyValue(x) {
		  return x.charAt(0) == "#" ? ifx.getValue(x) : x;
	};
	function dcVisible(pname, visible) {

		
		if(visible=="1") {
			$("#p_" + pname).show();
			
		}else {
			$("#p_" + pname).hide();
		}
		
		
		
		//setFields(this.ifx.dcFields, visible == "1");
		function setFields(flds, enabled) {
			$.each(flds, function(i,x){
				if(x.rtnName == pname) {
					x.enabled(enabled);
					console.log(x.name  + " " + x.isEnabled());
				}else {
					console.log(x.name + " is not " + pname + " field");
				}
			});
		}
	};
	Fun.prototype.convert=function(args){
		
	};
	
	Fun.prototype.swift = function(args){
		var target = args.shift(); // currentfield
		args.shift(); // swift
		console.log('Invoke swift ');
		var cmd = (args.shift()).toLowerCase();
		console.log('cmd:'+cmd);
		
		switch(cmd) {
		case 'load':
		case 'edit':
		case 'verify':
		case 'view':
		case 'temp':
			swift_load(cmd,IfxUtl.trim(ifx.getValue(args[0])), args[1], args[2], args[3], args[4], args[5]);
			break;
		case 'status':
			swift_getStatus(target,args[0]);
			break;
		case 'fromvar':
			swift_fromvar(args[0]);
			break;
		case 'tovar':
			swift_tovar();
			break;
		case 'load-print':
			swift_print_wait(cmd, target,IfxUtl.trim(ifx.getValue(args[1])), args[2]); 
			break;
		case 'print':
			swift_print(target, args[0]);
			break;
//		case 'init-wait':
//			createDeferred();
//			break;
		case 'wait':
			checkWait();
			break;
		case 'parse':
			// ???swift??????(default)  args[1] = 0(??????)  1 = ??????
			var msgType = ifx.getValue(args[2]);
			swift_parse(ifx.getValue(args[0]), args[1],msgType);
			break;
		case 'get-block':	
			// ' blk id, index, extra
			var v = swift_get_block(args[0], args[1], args[2]);
			ifx.setValue(target, v);
			break;
		case 'settagvalue':
			var tag = args.shift();
			var v = args[0];
			if(v.match(/\$$/) || v.match(/^#/)){
				v = ifx.getValue(v);
			}
			Swift.ui.tagValue(tag, v);
			break;
		case 'gettagvalue':
			var tag = args.shift();
			var fld = args[0];
			var v = Swift.ui.tagValue(tag, null);
			ifx.setValue(fld, v);
			break;
		default:
			break;
		}
		
	};
	var waitDeferred = null;
	function createDeferred(){
		waitDeferred = new $.Deferred();
	}
	function loadDone(){
		console.log("swift load done");
		if(waitDeferred!=null){
			setTimeout(function(){
				waitDeferred.resolve();	
			},10);
			
		}
	}
	function checkWait(){
		console.log("checkWait()");
		if(waitDeferred==null) {
			console.log("waitDeferred is not inited, return ");
			return;
		}
		var savedIndex = ifx.currentIndex;
		console.log("store resume ifx currindex:"+savedIndex);
		
		waitDeferred.always(function(){
			console.log("receive deferred always event, reset deferred");
			waitDeferred = null;
		});
		waitDeferred.done(function(){
			console.log('swift wait done');
			console.log("calling nextField from index:" + savedIndex);
			if(!ifx.isAfterYN()) {
				setTimeout(function(){
					ifx.nextField(savedIndex, true); // savedIndex - 1 ==>re-execute current	
				},10);
				
			}
		});
		waitDeferred.fail(function(){
			alert("wait failed");
		});
		
		throw ifx_makeWaitHere();
	}
	function swift_getStatus(targetName, whatStatus){
		var s= Swift.ui.getStatus(whatStatus);
		ifx.setValue(targetName, s);
	}
	function swift_print(targetName, prefix){
		var s= Swift.ui.printSwift(prefix);
		ifx.setValue(targetName, s);
	}
	function swift_fromvar(pairList){
		Swift.ui.mapFromVar(ifx, pairList);
	}
	function swift_tovar(){
		Swift.ui.mapToVar();
	}
	
	function swift_parse(data, direction,msgType){
		direction = (!direction) ? 0:direction; // 0??????
		if(direction == 0){
			Swift.print.parse_fromSwift(data,msgType);
		}else {
			alert("?????????parse??????, ???????????????");
		}
	}
	// ' blk id, index, extra
	function swift_get_block(blkId, index, etc){
		return  Swift.print.get_block(blkId, index, etc);
	}
	
	
	// TODO:???swift_load()?????????Swift.ui??????
	function swift_load(cmd, mtName, placeHolder, bufVarName, protectedTagsName ,pastebufVarName){
		var promise,
			editMode = (cmd !='view' && cmd != 'temp') ? true: false,
			valueObjects=null,
			pastevalueObjects=null,
			bDeserialize = bufVarName != undefined;
			buf=null,
			pastebuf=null,
			protectedTags= editMode ? '' : '*', // protected all tags;
			$placeHolder =$('#p_' + placeHolder);
		
		
		// prepare UI
		$('#swiftPanel').remove();
		$('<div id="swiftPanel"><span class="swift-panel-title">SWIFT????????????</span><span> (???Tab?????????.???Enter?????????)</span></div>').insertAfter($placeHolder);
		$placeHolder.hide();

		
		// create deferred for swift wait
		createDeferred();	
		
		// load mtxxx.txt and process
		promise = Swift.util.load(mtName);
		
		promise.fail(function(x){
			alert(x.responseText);
		});
		
		promise.done(function (mt) {
			setIfxToSwiftMode();
			
        	if(bDeserialize=='1') {

        		buf = ifx.getValue(bufVarName);
        		if(protectedTagsName!=undefined) {
        			protectedTags = ifx.getValue(protectedTagsName);
        		}

        		valueObjects = Swift.ui.deserialize(mt, buf);
                Swift.ui.display(mt, 'swiftPanel', ifx, function(){
                	setTimeout(function(){
                	if(pastebufVarName){
           		    pastebuf = ifx.getValue(pastebufVarName);
           		    if(pastebuf){
           		    pastevalueObjects = Swift.ui.deserialize(mt, pastebuf);
           		    }
           	     }
            			Swift.ui.putAllValues(cmd,buf, valueObjects, protectedTags,loadDone,pastevalueObjects);
                	},10);

                });

            }else {
            	$('#swift_text_counts_line').hide();
                Swift.ui.display(mt, 'swiftPanel', ifx, loadDone);
            }
        	//ifx.resizeSwift();
        });
		
		
		function initSwiftLayout(){
			var myLayout = $('body').layout({
				east:{
					closable:false,
					size:220,
					minSize:50,
					maxSize:850
					
				}
			});
			return myLayout;
		}
		
		function setIfxToSwiftMode(){
			if(editMode) {
				console.log('swift form is edit mode');
				ifx.enableSwift(true);
			}
			var myLayout = initSwiftLayout();
			ifx.setScrollTarget($('#center-part'), myLayout); 
			ifx.help.setLayout(true);
		}
	}
	
	Fun.prototype.popup = function(args){
		var source= args.shift();
		args.shift(); // popup
		var type = args.shift(); 
		//var fldsource = "#fld_"+source.slice(1);    
		//???  ?????? pop ??????  start
		//console.log("sw_RIM "+ source + "." + ifx.getValue(source));
		//if(isSameSwpop(source, ifx.getValue(source))) {
		//	console.log("sw_RIM "+ source + "." + ifx.getValue(source) +  " pop before, don't pop");
		//	return;
		//}
		
		/*????????????
		  if (ifx.getValue(source) !=""){
			return;
			}*/
		 //if( $( fldsource ).val() != ""){
		 // 	return;
		 // }
		
		
		if(type=="SWIFT-CODE"){
			var $fld = ifx.getField(source).ui();
			$fld.off('click.' + type);
			$fld.on('click.' + type, function(){
				popup_swiftCode(source, null, args);
			}).attr('title', '????????????????????????SWIFT CODE??????');
			
			setTimeout(function(){
				$fld.trigger('click');
			},10);
			stayHere(); //??? ????????????
			
		}
		
	};
	function popup_swiftCode(sourceFld, opt, flds){
		 ifx.keys.unbindAll();
		 var selectedItem = null;
		 //var diovalue = ifx.getValue(flds.shift());
		 $dlg = $( "#dialog-swift-code" );
		// $diov = $( "#_m_swift" );
		 $dlg.attr('title', "SWIFT CODE");
		// $diov.val(diovalue);
		 $dlg.dialog({
	        minHeight:200,
	        minWidth:250,
	        height:400,
	        width:900,
	        modal: true,
	        resizable:true,
	        closeOnEscape: true,
	        //buttons: {
	        //    "??????": function() {
	        //    	updateSelected(sourceFld,true);
	        //        $( this ).dialog( "close" );
	        //    },
	        //    "???": function() {
          //
	        //        $( this ).dialog( "close" );
          //      return false;
	        //    }
	        //},
	         open: function( event, ui ) {
	        	setTimeout(function(){
	        		neverComplete();
	        	},10);
	        	 
	        },close:function(){
	        	setTimeout(function() {
	        		ifx.keys.bind(true);
				}, 200);
	        }
	    });
		 function updateSelected(sourceFld,toVar){   //???????????? for ?????? ??????
			 if(selectedItem!=null){
				
				 var ss=[];
				 var data = selectedItem.data;
				 ss.push(selectedItem.value);
				 ss.push(data.slice(0,35));
				 ss.push(data.slice(35,70));
				 ss.push(data.slice(70,105));
				 ss.push(data.slice(105,140));
				 if(toVar){
					 $.each(flds, function(i,x){
						 if(x!='_') {
							 ifx.setValue(x, ss[i]);
						 }
					 });
					 console.log("updateSelected:"+sourceFld+","+ss[0]);
					// storeSwpopCached(sourceFld,ss[0]);//???  ?????? pop ??????  start
				 }else {
					 $('#swift_code').text(ss[0]);
					 $('#swift_bankName').text(ss[1]);
					
						$('#swift_addr1').text(ss[2] || '');
						$('#swift_addr2').text(ss[3] || '');
						$('#swift_addr3').text(ss[4] || '');
					 $('#swift_result', $dlg).show();
				 }
				 
			}
		 }
		 
		 
		 function neverComplete(){
			 var $m = $('#_m_swift');
			 var $dlg = $( "#dialog-swift-code" );
			 $('#_btn_clear_swift').off('click').on('click',function(){
				 $m.val('');
				 selectedItem = null;
				 $('#swift_result', $dlg).hide();
			 });
			$m.autocomplete({
				minLength : 3,
				source : function (request, callback){
					var url = _contextPath + "/mvc/hnd/rim/SWIFT?m=" + $m.val();
			
				    $.ajax ({
						cache : true,
				      	url :url,
				      	success : function(data) {
							callback($.map(data.result, function(x) {
								return {
									value : x.key,
									label :  x.key + ' - ' +(x.data).slice(0,35),
									data:x.data
								};
							}));
				      	}
				    });
				},
				open: function() { 
				   $m.autocomplete("widget").width(850); 
				   $m.autocomplete("widget").height(250); 
				},  
				select : function(e, ui) {
					if (ui.item != null) {
						selectedItem = ui.item;
						//updateSelected();           //?????????????????????
						updateSelected(null,true);
	          $dlg.dialog( "close" );
					}
				}
			}).focus(function(){
				if ($(this).autocomplete("widget").is(":visible")) {
					console.log("no visible");
	                return;
	            }
	            $(this).autocomplete('search',$(this).val());
			}).focus();
			
			
		} 
		
	}
	//??? ????????????
	function stayHere() {
   throw ifx_makeWaitHere();
  }
	//???  ?????? pop ??????  start
	var swpopCached = {};
	function isSameSwpop(currFld, swName){
		var k = currFld ;
		var lastTimeText = swpopCached[k];
		return lastTimeText!==undefined && lastTimeText === swName;
	}
	function storeSwpopCached(currFld, swName){
		console.log("store_sw_Cached:"+currFld+","+swName);
		var k = currFld;
		swpopCached[k] = swName;
	}
	//end
	Fun.prototype.mouse = function(args){
		args.shift(); // currentfield
		args.shift(); // mouse 
		var i = args.shift(); // 1: enable mouse first, else disable mouse first
		ifx.setMouseFirst(i==1);
		
		
	};
	Fun.prototype.logoff = function(){  
			window.top.onbeforeunload = null; // see easy_main.jsp, disable window.onbeforeunload
			window.top.location = "logoff.jsp";
	};
	Fun.prototype.append_title = function(args){  //??????????????????????????? [??????] [??????]
			var pageTable1;
			 args.shift(); // currentfield
		args.shift(); // appendtitle
		var t1 = easyValue(args.shift());  
		var t2 = easyValue(args.shift());
		t = t1+t2;
		for (i = 0; i < 10; i++) {  //???????????????10??? DC???
			try {
				 ifx.def.display[i].def.list[0][0] += t;  //????????????????????? 
				 pageTable1 = $('.pageTable')[i];  
				break;     //ok???????????????for??????
			} catch (e) {   //????????????(??????) continue??? [i]
				continue;
			}
		}
		//if(ifx.def.display[0].name != "BFHEAD"){   //???for???????????? 
		//	ifx.def.display[0].def.list[0][0] += t;
		//	pageTable1 = $('.pageTable')[0];
		//	}else{
		//		ifx.def.display[1].def.list[0][0] += t;
		//		pageTable1 = $('.pageTable')[1];
		//		}
		//
		var o = $('tbody > tr.caption > td > pre', pageTable1);
		o.text(o.text() + t);
		
	};
	//	Fun.prototype.append_title = function(args){
	//	args.shift(); // currentfield
	//	args.shift(); // appendtitle
	//	var t = args.shift();
	//	if(t[0] == '#')
	//		t= ifx.getValue(t);
	//	
	//	ifx.def.display[0].def.list[0][0] += t;
	//	
	//	var pageTable1 = $('.pageTable')[0];
	//	var o = $('tbody > tr.caption > td > pre', pageTable1);
	//	o.text(o.text() + t);
	//};
	var __countdown_handler=null;
	var __countdown_seconds=0;
	Fun.prototype.stop_countdown = function(){
		clearInterval(__countdown_handler);
	};
	Fun.prototype.countdown = function(args){
		args.shift(); // currentfield
		args.shift(); // appendtitle
		var _self = this;
		
		var t =  easyValue(args.shift()) || "60"; //???????????????
		var dofunc = args.shift() || "";
		
		//???:????????? ????????????
    t = parseInt(t,10);
		if(t <= 0){
			console.log("????????????????????????:"+t);
			return;
			}
		console.log("????????????:"+t+"???.");
		//??? ??? ??????????????? 
		this.stop_countdown();    //?????????????????????
		ifx.setCountDownTran();
		ifx.countDownToDefult();  //??????#btn_yn
		//end
		
		__countdown_seconds = t;
		__countdown_handler = setInterval(function(){
			// var st=$(window).scrollTop() + 5;
			__countdown_seconds--;
			if(__countdown_seconds == t-1){
						 $('.countdown_timer').remove();
			 var $newdiv = $( "<div class='countdown_timer'>" + 
					 __countdown_seconds + "</div>" );
		//	 $newdiv.css({ position:'absolute',top:st,left:3 });
             $newdiv.css({ position:'fixed',top:3,left:3 });
			 $( "body" ).append( $newdiv);
			}else{
				$('.countdown_timer').html(__countdown_seconds );
				}
			 if(__countdown_seconds==0){
			 	$('.countdown_timer').html(__countdown_seconds + "(????????????)" );
				 _self.stop_countdown();
				 ifx.countDownToZero();
				if (dofunc != ""){
           ifx.rtn.CALL("#"+dofunc,"#"+dofunc);
       }
			 }
		},1000);
		
	};
	return Fun;
}());	
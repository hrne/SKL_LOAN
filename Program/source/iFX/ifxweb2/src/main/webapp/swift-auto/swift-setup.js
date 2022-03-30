var SwiftAuto = SwiftAuto || {};

(SwiftAuto.setup = function($) {
	var $main;
	var $priPrinter;
	var $primaryPrinters;
	var $ackPrinter;
	var $ackPrinters;
	var $nakPrinter;
	var $nakPrinters;
	var $altPrinter;
	var $altPrinters;
	var $priAllowip;
	var $mtArea;
	var $mySave;
	var $divErrmsg;
	var $showAddrmsg;//潘 2017/11/17
	var _contextPath;
	var _controllerPath = "/mvc/swiftrepos/swift/printer/";
	var _listUrl="";
	var _saveUrl="";
	var addrip_chk = "";
	function init(contextPath, divMain,addrip_local){
		addrip_chk = addrip_local;
		_contextPath = contextPath;
		_controllerPath = _contextPath + _controllerPath;
		_listUrl = _controllerPath + "list";
		_saveUrl = _controllerPath + "save";
		$main = $('#'+divMain);
		
		$primaryPrinters = $('#primaryPrinters', $main);
		$priPrinter = $('#priPrinter', $main);
		$primaryPrinters.on('change',function(){
			if(this.value!=0)
				$priPrinter.val($('option:selected', $(this)).text());
		});
		
		$ackPrinters = $('#ackPrinters', $main);
		$ackPrinter = $('#ackPrinter', $main);
		$ackPrinters.on('change',function(){
			if(this.value!=0)
				$ackPrinter.val($('option:selected', $(this)).text());
		});
		
		$nakPrinters = $('#nakPrinters', $main);
		$nakPrinter = $('#nakPrinter', $main);
		$nakPrinters.on('change',function(){
			if(this.value!=0)
				$nakPrinter.val($('option:selected', $(this)).text());
		});


		$altPrinters = $('#altPrinters', $main);
		$altPrinter = $('#altPrinter', $main);
		$altPrinters.on('change',function(){
			if(this.value!=0)
				$altPrinter.val($('option:selected', $(this)).text());
		});
		
		$priAllowip  = $('#priAllowip' , $main);
		$showAddrmsg = $('#showAddrmsg', $main); //潘  2017/11/17

		$mtArea = $('#mtArea', $main);
		$mySave = $('#btnSave', $main);
		$mySave.on('click', savePrinterDef);
		
		$divErrmsg = $('#divErrmsg', $main);
		$divErrmsg.hide();
		$divErrmsg.addClass("alert alert-danger alert-dismissable");
	}
	function savePrinterDef(){
		$divErrmsg.hide();
		if($priAllowip.val() == "") {
			$priAllowip.focus();
			$divErrmsg.text("請設定允許ip").show();			
			return;
		}
		if($priAllowip.val() != addrip_chk) {
			$priAllowip.focus();
			$divErrmsg.text("請設定本機ip " + addrip_chk).show();
			return;
		}		
		if($priPrinter.val() == "") {
			$priPrinter.focus();
			$divErrmsg.text("請設定來電印表機").show();
			
			return;
		}
		if($ackPrinter.val() == "") {
			$ackPrinter.focus();
			$divErrmsg.text("請設定ACK印表機").show();
			
			return;
		}
		if($nakPrinter.val() == "") {
			$nakPrinter.focus();
			$divErrmsg.text("請設定NAK印表機").show();
			
			return;
		}
		if($altPrinter.val() == "") {
			//不特別檢查電信科指定印表機
			//$altPrinter.focus();
			//$divErrmsg.text("請設定電信科指定印表機").show();
			//return;
			
		}
		
		
		var o = {
			priPrinter:$priPrinter.val(),
			ackPrinter:$ackPrinter.val(),
			nakPrinter:$nakPrinter.val(),
			altPrinter: $altPrinter.val(),
			priAllowip: $priAllowip.val(),
			altMsgList:[]
		};
		$("input:checked", $mtArea).each(function() {
			o.altMsgList.push(this.defaultValue); //value -> defaultValue (for tcb抓不到真正值)
		});
		// convert array to string for easier access  at Server 
		o.altMsgList = o.altMsgList.join();
		alert("儲存內容:"+JSON.stringify(o));
		var poster = $.post(_saveUrl, {
			_d: JSON.stringify(o) 
		});
		poster.done(function(data){
			alert("儲存成功");
			getPrinterDef();
		});
		poster.fail(function(ex){
			alert("儲存失敗:"+ex.statusText);
		});
	}
	function getPrinterDef(addrip){
		var poster = $.post(_listUrl);
		var db_priAllowip = '';
		poster.done(function(data) {
			if(data.defined=="no"){
				alert("請先設定「自動列印之電腦IP」及「來去電印表機」並儲存，方可正常自動列印。");
				$priAllowip.val(addrip);
				$showAddrmsg.val('(已預設本機ip)');
			}else {
				var pInfo = data["printer-info"];
				$priPrinter.val(pInfo["primaryPrinter"]);
				$ackPrinter.val(pInfo["ackPrinter"]);
				$nakPrinter.val(pInfo["nakPrinter"]);
				$altPrinter.val(pInfo["altPrinter"]);
				db_priAllowip = pInfo["priAllowip"];
				$priAllowip.val(db_priAllowip);
				if ($priAllowip.val == ""){
					  $priAllowip.val(addrip);
				}//潘 2017/11/23
				if(addrip != db_priAllowip){
					$showAddrmsg.val('(自動列印已設於IP ' + db_priAllowip + '，請由該電腦啟動，或由設定重設為本電腦IP並儲存後再列印。)');
				}else{
					$showAddrmsg.val('(與系統設定ip相符)');
				}
			}
			
			populateMtCheckboxes(data);
		});
		poster.fail(function(ex){
			alert(ex.statusText);
		});
	}
	function populateMtCheckboxes(data){
		$mtArea.empty(); //清除畫面
		var mtFiles = data.mtFiles;
		for(var m=1; m <=9; m++){
			var arr = $.map(mtFiles, function(x, i){
				if(x.charAt(2) == m) {
					return x;
				}else return;
			});
			if(arr.length >0) {
				var $d =$("<div class='col-md-12' />");
				 $mtArea.append($d);
				
				$.each(arr, function(i, x){
					 var $span = $("<span/>");
					 $d.append($span);
					var chkId = 'myChk_' + x;
					$span.append($(document.createElement('input')).attr({
				           id:    chkId
				          ,name:  chkId
				          ,value: x.replace('mt','')
				          ,type:  'checkbox'
					}));
					$span.append(
				    		$(document.createElement('label')).attr({
				    			'for':	chkId
				    		})
				    		.text( x ));
				});
			}
		}
		try {
			var checkedMt =  data['printer-info']['altMsgList'];
			if( checkedMt) {
				var ss = checkedMt.split(",");
				$.each(ss, function(i,x){
					var chkId = '#myChk_mt' + x;
					$(chkId).prop( "checked", true );
				});
			}
		}catch(ee) {}
	}
	return {
		init:init,
		getPrinterDef:getPrinterDef
		
	};
}(jQuery));

var ifxFile =  (function() {

	var _serviceUrl =_contextPath + "/mvc/hnd/suspendedTran";

	function setUrl(u){
		url = u;
	}
	function createCmd(cmd, filename, content){
		return {
			cmd:cmd,
			filename:filename,
			content:encodeURI(content)
		};
	}
	function putFile(filename, content, blockers, fnReceive, bAsync){
		//var data = {'p':filename, 'c':encodeURI(content)};
		var cmd = createCmd("put", filename, content);
		sendCmd(cmd, blockers, fnReceive);
	}

	function getFile(filename,blockers, fnReceive){
		var cmd = createCmd("get", filename, "");
		sendCmd(cmd, blockers, fnReceive);
	}

	function deleteFile(filename,blockers, fnReceive){
		var cmd = createCmd("delete", filename, "");
		sendCmd(cmd, blockers, fnReceive);
	}

	function sendCmd(cmdObj, blockers, fnReceive) {

		if(blockers!=null && blockers.block){
			blockers.block();
		}

		var $p = $.ajax({
			type:'post',
			dataType:'json',
			url:_serviceUrl,
			data:cmdObj
		});

		$p.done(function(data) {
			if(fnReceive!=null)
				fnReceive(data);

		});

		$p.always(function(){
			if(blockers!=null && blockers.unblock!=null){
				blockers.unblock();
			}
		});
		$p.fail(function(err){
			alert("suspended tran operation error");
		});

	}
	var fileroot = "C:/ifx";
	var filetmpname = fileroot+"/IFX_PRINTER.config";
	//讀取印表機
	function readIfxPrinter(deviceX) {
		      //是否有資料夾?
		      deviceX.folder.create(fileroot);
					//預設路徑
					deviceX.file.exists(filetmpname,true);
					var ifx_printer = deviceX.file.read(filetmpname);
					//有可能沒有該檔案
					if(!ifx_printer){
						ifx_printer ="{}";
					}
					console.log(ifx_printer);
				 var jsonlist = JSON.parse(ifx_printer);

				 return jsonlist;
	}
	//取得
	function getformPrinter(deviceX,formname,txcd){
		    var formlist = readIfxPrinter(deviceX);
		    formname = checkSwifpfn(formname);
				var result = 	formlist[formname] ? formlist[formname] :null;
		    var str = result ? result.split(';') : '';
		    var txcdid    = str[1] ? str[0] : '' ;
		    var pselectid = str[1] ? str[1] : str[0] ;
		    var txcdch    = txcdid ? (txcdid.indexOf(txcd)) : false ;
				var result = 	( pselectid && ( txcdch != -1 ) ) ? pselectid : null;
				console.log("getformPrinter:"+result);
				return result;
	}
	//檢查列印是否為相關Swift電文稿
	function checkSwifpfn(formname) {
					//柯:SWIFT 相關電文稿
			var swiftform = ["MT001.PFNX","MT002.PFNX","XW100.PFNX","XW110.PFNX",
			"XW120.PFNX","XW121.PFNX","XW130.PFNX","XW140.PFNX","XW300.PFNX",
			"XW310.PFNX","XW400.PFNX","XW510.PFNX","XW520.PFNX","XW931.PFNX","XW932.PFNX"];
			if(swiftform.indexOf(formname) != -1){
			 return "SWIFTPFNX";
			}
			return formname;
	}
	var menutmpname = fileroot+"/IFX_MENU.config";
	//讀取MENU
	function readIfxMenu(deviceX) {
				  //是否有資料夾?
		      deviceX.folder.create(fileroot);
					//預設路徑
					deviceX.file.exists(menutmpname,true);
					var ifx_menu = deviceX.file.read(menutmpname);
					//有可能沒有該檔案
					if(!ifx_menu){
						ifx_menu ='["type,sbtype,enabled,txcd,txnm","0,DD,0,DD  ,NONO","0,DD1,0,DD1  ,尚未設定(步驟：系統設定→自訂選單)"]';
					}
					console.log(ifx_menu);
				 var jsonlist = JSON.parse(ifx_menu);

				 return jsonlist;
	}
	//取得
	function getformMenu(deviceX){
		    var formlist = readIfxMenu(deviceX);
				return formlist;
	}

	return {
		'put':putFile,
		'get':getFile,
		'remove':deleteFile,
		'getformP':getformPrinter,
		'getformM':getformMenu,
		'setUrl':setUrl
	};
})();
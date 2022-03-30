var SwiftAuto = SwiftAuto || {};

(SwiftAuto.main = function($) {
	var _contextPath="";
	var _parentPage ="";
	var $pDiv=null;
	var pollInterval = 600; //刷新時間 單位:秒
	var $waitcount = $("#waitcount");
	var $waitstr = $("#waitstr");
	var _waitprint = true; 
	var _listUrl="";
	var _canPrint=false; //是否可以自動列印(poll)的狀態
	var _checkAllowip = ""; //先設定一次本機ip
	waitPrint(); //預設正常,故先執行一次
	
	var _controllerPath = "/mvc/swiftrepos/swift/";
	function init(contextPath, parentPage, placeHolder, nSeconds) {
		_contextPath = contextPath;
		_controllerPath = _contextPath + _controllerPath;
//		_listUrl = _controllerPath + "list";
		_listUrl = _contextPath + "/mvc/swiftrepos/swift/printer/list"; //潘 2017/11/22
		_parentPage = parentPage;
		$pDiv = $(placeHolder);
		openLog();
		if (nSeconds != undefined) pollInterval = nSeconds;
		logInfo("hello, poll server every " + pollInterval + " seconds.");
		
	}
	function setcheckAllowip(addrip){
		_checkAllowip = addrip;
	}
	function checkAllowip(){
		var poster = $.post(_listUrl);
		var db_priAllowip = '';
		poster.done(function(data) {
			if(data.defined=="no"){
				alert("此分行尚未定義印表機配置,無法自動列印");
				_canPrint = false;
			}else {
				var pInfo = data["printer-info"];
				db_priAllowip = pInfo["priAllowip"];
				if(_checkAllowip != db_priAllowip){
					alert('與系統設定ip不相符,無法自動列印!');
					_canPrint = false;
				}else{
					//不跳窗,直接繼續
					logInfo("checkAllowip done.");
					_canPrint = true;
				}
			}
			
		});
		poster.fail(function(ex){
			alert(ex.statusText);
			_canPrint = false;
		});
	}
	
	var _printers=null;
	function setPrinter(printers){
		_printers = printers;
	}
	
	function openLog() {
		$pDiv.dialog({
			width:'70%'
		});
	}
	 function logInfo(desc) {                             
		 __log(desc,"green");
	 }        
	 function logError(desc) {                             
		 __log(desc,"red");   
	 } 
	 function logWarn(desc) {                             
		 __log(desc,"blue");   
	 }  	        
	 function __log(desc, color) {   
		 desc =  now() + " - " + desc;
        var $li = $("<li/>").text(desc).css("color",color);                
        $pDiv.prepend($li);   
	 }        
	var _job;
	function start(){
		_job = setTimeout(pollServer, pollInterval * 1000);
		logInfo("started");
	}
	var $def = null;
	function pollServer() {
		var promise = null;
		//檢查是否符合條件
		checkAllowip();
		if(_canPrint){
		  promise = poll();
		  promise.always(function(){
				logInfo("done, start next polling in " + pollInterval + " seconds.");
				_job = setTimeout(pollServer, pollInterval * 1000);
			});
		}else{
			logInfo("Not Allow!, start next polling in " + pollInterval + " seconds.");
			_job = setTimeout(pollServer, pollInterval * 1000);
		}
	}
	
	function poll() {
		$def = $.Deferred();
		logInfo("polling server");
		var dt = getYYYYMMDD();
		var fnError = logError;
		
		var url = _controllerPath + "autoprint/" + dt;
		var poster = $.post(url);
		poster.done(function(data) {
			if(data.status) {
				logInfo("total un-printed files:"+ data.size);
				if(data.size > 0 ) {
					if( !_waitprint){
					 logInfo(data.files);
					 processFiles($def, dt, data);
					}else{
						logWarn("Wait print! "+data.size );
						$def.resolve();
					}
				}else {
					$def.resolve();
				}
				//更新值
				 $waitcount.text(data.size);
			}else {
				$def.resolve();
				logError(data.message);
				logError("redirecting to login page....");
				setTimeout(function(){
					window.location = _parentPage;
				},3000);
			}
		});
		poster.fail(function(ex){
			fnError("poll failed:"+ex.statusText);
			$def.resolve();
			
		});
		//setTimeout($def.resolve, 3000);
		return $def.promise();
	}
	function processFiles($aDef, dt, data){
		var arr = data.files.map(function(x,i){
			return {
				filename:x,
				brno:data.brno,
				printTo: data.printTo[i]
			};
		});
		
		async.mapSeries(arr, downloadAndPrint, function(err, results) {
			if (err) {
				logError(err);
			} else {
				logInfo(results.length + " files printed");
			}
			$aDef.resolve();
		});

		
		function downloadAndPrint(obj, callback){
			var filename = obj.filename,
			    brndept = obj.brno,
				url = _controllerPath + brndept + "/" + dt + "/" + filename,
				poster = $.post(url);
			poster.done(function(data) {
				printFile(obj, data, callback);
			});
			poster.fail(function(err){
				callback(err);
			});
		}
		function printFile(obj, data, callback){
			var filename = obj.filename,
				printTo = obj.printTo;
			try {
				if (data.status && data.size > 0){
					if(!(_printers.isNull()))
						_printers.print(filename, data.lines.join("\n"), printTo);
					else dummyPrint();
				}
				function dummyPrint(){
					logInfo("** printing " + data.file + ", total lines:" + data.size + " to printer:[" + data.printer + "]");
					logInfo("**** " + data.lines[0]);
				}
			}catch(ex){
				logError(ex);
			}finally {
				callback(null, data);
			}
		}
	} // end of processFiles()
	
	
	
	function stop() {
		
	}
	
	 function pad2(n){
		 return n>9?n:'0'+n;   
	 }
	 function now() {
		  var date = new Date();
		  var hours = date.getHours();
		  var minutes = date.getMinutes();
		  var seconds = date.getSeconds();
		  
		  minutes = minutes < 10 ? '0'+minutes : minutes;
		  var strTime = pad2(hours) + ':' + pad2(minutes) + ':' + pad2(seconds);
		  var year = date.getFullYear();
		  var month = date.getMonth() + 1;
		  var day = date.getDate();
		  var strDate = year + "/" + pad2(month) + "/" + pad2(day);
		  return strDate+" " + strTime;
	}
 	function getYYYYMMDD() {
	    var d = new Date(),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();

	    if (month.length < 2) month = '0' + month;
	    if (day.length < 2) day = '0' + day;

	    return [year, month, day].join('');
	}
	 function clearLog(){
		 $pDiv.empty();
	 }
	 /*使否等待列印記號*/
	 /*正反法*/
	 function waitPrint(){
		 _waitprint = _waitprint ? false: true;
		 if(_waitprint){
		 	 $waitstr.show();
		 	 $('body').css('background-color', 'gray ');
		 	 $('p').css('color','#FFFFFF');
		 	}else{
		 	$waitstr.hide();
		 	$('body').css('background-color', 'rgba(199, 242, 144, 1)');
		 	$('p').css('color','red');
		 	}
	 }
	 
	return {
		init:init,
		setPrinter:setPrinter,
		setcheckAllowip:setcheckAllowip,
		start:start,
		stop:stop,
		openLog:openLog,
		clearLog:clearLog,
		waitPrint:waitPrint
	};
}(jQuery));
var SwiftAuto = SwiftAuto || {};

(SwiftAuto.browse = function($) {
	var $grid = null;
	var $panel = null;
	var $btnPrint = null;
	var $btnPrintTo=null;
	var $printers=null;
	var _printers = null;
	var _controllerPath = "/mvc/swiftrepos/swift/";
	var _contextPath = null;
	function init(contextPath, divPanel){
		_contextPath = contextPath;
		_controllerPath = _contextPath + _controllerPath;
		$panel = $(divPanel);
		$grid = $('#grid', $panel);
		$btnPrint = $('#btnPrint', $panel);
		$btnPrintTo = $('#btnPrintTo', $panel);
		$printers = $('#printers', $panel);
	}
	function setPrinter(printers){
		_printers = printers;
	}
	function findByDate(dt, fnMessage){
		var url =  _controllerPath + dt.replace(/-/g,"");
		var poster = $.post(url);
		$panel.hide();
		$grid.jqGrid("clearGridData");
		poster.done(function(data) {
			if(!data.status){
				fnMessage("Failed:"+data.message);
				return;
			}
			if(data.size==0){
				fnMessage(dt + "無檔可印");
				return;
			}
			showFiles(data, dt);
		});
		poster.fail(function(ex){
			fnMessage(ex.statusText);
		});
	}
	
	
	function showFiles(data, dt){
		if(data.size > 0) {
			$panel.show();
		}else {
			return;
		}
		var mydata = $.map(data.files, function(x, i){
			//var ss =  x.split("_"); // 假設只有一個 .
			return {
				id:(i+1),
				original:x,
				name: x, //ss[0]
				brno:data.brno,
				printTo:data.printTo[i]
			};
		});
		
		initGrid(dt);
		for(var i=0;i<=mydata.length;i++)
			$grid.jqGrid('addRowData',i+1,mydata[i]);
		
		var $fakePrinter=null;
		
		$btnPrint.off('click').on('click',printSelected);
		$btnPrintTo.off('click').on('click',printSelected);
		function printSelected(){
			var $elem = $(this);
			var printToPrinter = null;
			if ($elem.attr('id') ==  'btnPrintTo') {
				printToPrinter = $printers.val();	
			}
			
			
			$('#fakePrinter').remove();
			$fakePrinter = $("<div id='fakePrinter'></div>");
			$panel.append($fakePrinter);
			//20171222 如有選中行數問題，請參考ifx-grid.js
			var s = $grid.jqGrid('getGridParam','selarrrow');
			if(s.length > 0) {
				var arr = s.map(function(x,i){
					return {
						filename:mydata[x-1].original,
						brno:mydata[x-1].brno,
						printTo: (!printToPrinter) ? mydata[x-1].printTo : printToPrinter
					}
				});
				// TODO: call block-ui?
				async.mapSeries(arr, downloadAndPrint, function(err, results) {
					if (err) {
						alert(err);
					} 
					// refresh data from server
					findByDate(dt, function(){});
				});
			}
		}

		
		function downloadAndPrint(obj, callback){
			var filename = obj.filename;
			var brndept = obj.brno;
			var url = _controllerPath + brndept + "/" + dt.replace(/-/g,"") + "/" + filename;
			var poster = $.post(url);
			poster.done(function(data) {
				printFile(obj, data, callback);
			});
			poster.fail(function(err){
				callback(err.statusText);
			});
		}
		 function fakePrint(desc, color) {   
	        var $li = $("<li/>").text(desc).css("color",color);                
	        $fakePrinter.append($li);   
		 }        
		
		function printFile(obj, data, callback){
			var filename = obj.filename;
			var printTo = obj.printTo;
			try {
				if (data.status && data.size > 0){
					if(!(_printers.isNull()))
						_printers.print(filename, data.lines.join("\n"), printTo);
					else dummyPrint();
				}
				
				function dummyPrint(){
					fakePrint("** printing " + data.file + ", total lines:" + data.size + " to printer:[" + data.printer + "]", 'blue');
					fakePrint("**** " + data.lines[0], 'black');
					fakePrint("  ----------------------- " , 'yellow');
				}
				
			}catch(ex){
				callback(ex);
			}finally {
				callback(null, data);
			}
		}
	}
	function initGrid(dt) {
		$grid.jqGrid({
			datatype: "local",
			height: 'auto',
			width:'auto',
		   	colNames:['No', '檔案名稱', '預先指定印表機'],
		   	colModel:[
   	          	{name:'id',index:'id', width:30, align:'center', sorttype:"int"},
		   		{name:'name',index:'name', width:300, sorttype:"string"},
		   		{name:'printTo', width:250, align:"left"}
		   	],
		   	multiselect: true
		   	
		});
		$grid.jqGrid('setCaption',  dt + " swift來電");
	}
	
	return {
		init:init,
		setPrinter:setPrinter,
		findByDate:findByDate
	};
}(jQuery));

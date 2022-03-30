var ifxReport = ifxReport || {};
(ifxReport.browser = function($) {
	var _contextPath = null;
	var _controllerPath = "/mvc/ifxReport/";
	var $grid = null;
	var $panel = null;
	var $btnPrint = null;
	function init(contextPath,divPanel){
		_contextPath = contextPath;
		_controllerPath = _contextPath + _controllerPath;
		$panel = $(divPanel);
		$grid = $('#grid', $panel);
		$btnPrint = $('#btnPrint', $panel);
	}
	
	var curDt=null;
	var curApp=null;

	function getReports(dt, app, appName, fnMessage){
		// /mvc/report/dt/app
		var url =  _controllerPath + "get/"+ dt.replace(/-/g,"") + "/" + app;
		var poster = $.post(url);
		$panel.hide();
		curDt=null;
		curApp=null;
	
		$grid.jqGrid("clearGridData");
		poster.done(function(data) {
			if(!data.status){
				fnMessage("Failed:"+data.message);
				return;
			}
			if(data.size==0){
				fnMessage(dt +"  "+app+ "  無檔可印");
				return;
			}
			curDt = dt;
			curApp = app;
			showGrid(data, dt, app, appName);
		});
		poster.fail(function(ex){
			fnMessage(ex.statusText);
		});
	}
	
    function download(filename) {
    	var url = _controllerPath + "download/"+curDt.replace(/-/g,"") + "/" + curApp + "/" +filename;
    	var iframe = document.createElement("iframe");
    	  iframe.setAttribute("src", url);
    	  iframe.setAttribute("style", "display: none");
    	  document.body.appendChild(iframe);
    }
    function printSingleFile(x){
    	resetFakePrinter();
    	var arr = [x];
    	async.mapSeries(arr, downloadAndPrint, function(err, results) {
			if (err) {
				alert(err);
			} 
		});
    }
	function initGrid(dt, app ,appName) {
		var myButton = function(cellVal,options,rowObject) {
	        var b1=  "<input style='height:22px;' type='button' value='下載' onclick=\"ifxReport.browser.download('"+cellVal+"')\"  />";  
	        var b2=  "<input style='height:22px;' type='button' value='列印' onclick=\"ifxReport.browser.printSingleFile('"+cellVal+"')\"  />";
	        return b1+ " " + b2;
	    };
		$grid.jqGrid({
			datatype: "local",
			height: 'auto',
			width:'auto',
		   	colNames:['No', '檔案名稱',"Action"],
		   	colModel:[
   	          	{name:'id',index:'id', width:30, align:'center', sorttype:"int"},
		   		{name:'name',index:'name', width:300, sorttype:"string"},
		   		{name:'name', width: 120, align: 'center', sortable: false, formatter:myButton}
		   	],
		   	multiselect: true
		   	
		});
		$grid.jqGrid('setCaption', dt +" "+appName +" 報表");
		
	}
	function showGrid(data, dt, app, appName){
		if(data.size > 0) {
			$panel.show();
		}else {
			return;
		}
		var mydata = $.map(data.files, function(x, i){
			return {
				id:(i+1),
				original:x,
				name: x
			};
		});
		
		initGrid(dt,app, appName);
		
		for(var i=0;i<=mydata.length;i++)
			$grid.jqGrid('addRowData',i+1,mydata[i]);
		
		
		
		$btnPrint.off('click').on('click',function(){
			resetFakePrinter();
			var s = $grid.jqGrid('getGridParam','selarrrow');
			if(s.length > 0) {
				var arr = s.map(function(x,i){
					return mydata[x-1].original;
				});
				// TODO: call block-ui?
				async.mapSeries(arr, downloadAndPrint, function(err, results) {
					if (err) {
						alert(err);
					} 
				});
			}
		});
		
	
	}
	var $fakePrinter=null;
	function resetFakePrinter(){
		$('#fakePrinter').remove();
		$fakePrinter = $("<div id='fakePrinter'></div>");
		$panel.append($fakePrinter);
	}
	function downloadAndPrint(filename, callback){
		var url = _controllerPath + "get/"+curDt.replace(/-/g,"") + "/" + curApp + "/" +filename;
		var poster = $.post(url);
		poster.done(function(data) {
			printFile(filename, data, callback);
		});
		poster.fail(function(err){
			callback(err.statusText);
		});
	}
	 function fakePrint(desc, color) {   
        var $li = $("<li/>").text(desc).css("color",color);                
        $fakePrinter.append($li);   
	 }        
	
	function printFile(filename, data, callback){
		try {
			if (data.status && data.size > 0){
				dummyPrint(3);
			}
			
			function dummyPrint(linesToPrint){
				fakePrint("** printing " + data.file + ", total lines:" + data.size, 'blue');
				for(var i=0; i < linesToPrint; i++)
					fakePrint("**** " + data.lines[i], 'black');
				fakePrint("  ----------------------- " , 'green');
			}
			
		}catch(ex){
			callback(ex);
		}finally {
			callback(null, data);
		}
	}

	return {
		init:init,
		getReports:getReports, 
		download:download,
		printSingleFile:printSingleFile
	};
}(jQuery));

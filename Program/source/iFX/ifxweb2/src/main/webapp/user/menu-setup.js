var UserMenu = UserMenu || {};

(UserMenu.setup = function($) {
	var $main;
	var $mtArea;
	var $mySave;
	var $myUpload;
	var $myPutdata;
	var _contextPath;
	var _controllerPath = "/mvc/hnd/menu2/jsonp?menu=";
	var _listUrl="";
	//需要被自訂的業務清單
	var _menu =["XA","XC","XE","XF","XG","XI","XL","XM","XR","XS","XT","XW","XX"];
	var _okmenu = 0;
	var _firsttemp = null;
	var _menulist = {};
	var filetmpname = "C:/ifx/IFX_MENU.config";
	//換頁籤時變數歸零
	function initval(){
		_listUrl = "";
		_menulist={};
		_firsttemp=null;
		_okmenu=0;
	}
	function init(contextPath, divMain){
		initval();
		_contextPath = contextPath;
		_listUrl = _contextPath + _controllerPath;
		$main = $('#'+divMain);

		$mtArea = $('#mtArea', $main);
		$mySave = $('#btnSave', $main);
		$myUpload = $('#btnUpload', $main);
		$myPutdata = $('#btnPutdata', $main);
		$mySave.on('click', saveMenuDef);
    $myUpload.on('click', uploadMenuDef);
    $myPutdata.on('click', readIfxMenu);
	}
	/*儲存到本機端路徑*/
	function saveMenuDef(){
		var savedata = [];
		var checktemp ;
		$("input:checked", $mtArea).each(function() {
			checktemp = _menulist[this.defaultValue].split(",");
			//取得ROOT NAME 
			var getrootName = checktemp[1].trim();
			if(getrootName.length > 2){
				getrootName = getrootName.slice(0,2);
		  }
		  //替代ROOT NAME內容,製作一個假象的子目錄
		  var fackRoot = _menulist[getrootName].split(",");
		  fackRoot[1] = getrootName +"X";
		  fackRoot[3] = getrootName +"X  ";
		  //檢查重複
			if(jQuery.inArray( fackRoot.join(","), savedata ) == -1 ){
				savedata.push(fackRoot.join(","));
			}
			checktemp[1] = fackRoot[1];
			savedata.push(checktemp.join(",")); //value -> defaultValue (for tcb抓不到真正值IE10?)
		});
		var stringsss ="";
		if(savedata.length != 0){
			savedata.unshift(_firsttemp);
			stringsss = savedata.join('","');
		  stringsss = '["'+stringsss+'"]';
		}
		//加入起始行
		console.log(stringsss);
		console.dir(savedata);
		Device.pb.file.write(filetmpname,stringsss,false);
		return;
	}
	/*上傳到R6*/
	function uploadMenuDef(){
		var ifx_menu = Device.pb.file.read(filetmpname);
		var filename = "user_menu/"+_addr;
		    filename += '.txt';
		    ifxFile.put(filename,ifx_menu);
	}
	/*取得MENU的內容*/
	function getMenuDef(menuindex){
		if(!menuindex){
			menuindex = 0;
			_okmenu = 0;
		}else if(menuindex >= _menu.length){
		  console.log("結束..");
		  readIfxMenu();
			return;
		}
    	 var listUrl = _listUrl + _menu[menuindex];
    	console.log("list uil:"+listUrl);
    $.ajax({
        type: "get",
        url: listUrl,
        dataType: "jsonp",
        jsonpCallback: "callback",
        success: function (res) {
        	console.dir(res);
        	console.log("res to json:" + JSON.stringify(res));
          makeChkMenu(res)
        },
        error: function (e) {
        	  console.log("menu-util:fail"+e.toString());
           //alert('fail'+e.toString());
        }
    });	
	}
	/*解析MENU內容並製作成按鈕清單*/
    function makeChkMenu(items) {
       	var menuarr =[];
        var tempp = [];

        $.each(items, function (i, x) {
        	if(i==0){
        		_firsttemp = x;
        		return;
        	}
        	tempp = x.split(",");
        	_menulist[tempp[3].trim()] = x;
          if(tempp[0] != 0){
          	menuarr.push(tempp[3]);
          }
        });
        
        console.dir(menuarr);
			if(menuarr.length >0) {
				var $d =$("<div class='col-md-12' />");
				 $mtArea.append($d);
				$.each(menuarr, function(j, x){
					 var $span = $("<span/>");
					 $d.append($span);
					var chkId = 'myChk_' + x;
					$span.append($(document.createElement('input')).attr({
				           id:    chkId
				          ,name:  chkId
				          ,value: x
				          ,type:  'checkbox'
					}));
					$span.append(
				    		$(document.createElement('label')).attr({
				    			'for':	chkId
				    		})
				    		.text( x ));
				});
			}
			_okmenu++;
       getMenuDef(_okmenu);
    }
    /*還原本機MENU內容並勾選*/
		function readIfxMenu() {
					var localtemp ,chkId;
					console.log("readIfxMenu..");
					console.log("filetmpname:"+filetmpname);
					//預設路徑
					Device.pb.file.exists(filetmpname,true);
					var ifx_menu = Device.pb.file.read(filetmpname);
					//有可能沒有該檔案
					if(!ifx_menu){
						return;
					}
					console.log("ifx_menu:"+ifx_menu);
					ifx_menu = JSON.parse(ifx_menu);
        $.each(ifx_menu, function (i, x) {
        	localtemp = x.split(",");
					chkId = '#myChk_' + localtemp[3];
					$(chkId).prop( "checked", true );
        });
		};    
	return {
		init:init,
		getMenuDef:getMenuDef
	};
}(jQuery));

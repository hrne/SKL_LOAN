var Env = Env || {};
(Env.one = function($) {
	var _divTrusted=null, _divDevice=null;
	var _nextPage=null;
	function init(divNames, nextPage){
		_divTrusted = divNames['trust'];
		_divDevice = divNames['device'];
		_version = "18.10.31.0"; //小包的版號:注意不是逗號 //15,7,13,01  ->  15,7,13,1
		_nextPage = nextPage;
	}
	
	function loadActiveX() {
		try {
			var f = new ActiveXObject("Scripting.FileSystemObject");
			return true;
		}catch(ee){
			return false;
		}
	}
	function displayStatus(place, bOK, text, oLink){
		var $place =$('#' + place).html('');
		var $p = $("<p/>");
		$p.addClass(bOK ? "ok-status":"error-status");
		$p.text(text).appendTo($place);
		if(oLink){
			$("<a/>").attr({'href':oLink.url, target:'_blank'}).text(oLink.text).appendTo($p);
		}
		
	}
	function checkTrust(){
		if(loadActiveX()){
			displayStatus(_divTrusted, true, "信任網站檢查:成功");
			return true;
		}else {
			displayStatus(_divTrusted, false, "load ActiveX 檢查:失敗");
			//柯  先改
			//displayStatus(_divTrusted, false, "load ActiveX 檢查:失敗, {
			//url:'https://eservice.nhi.gov.tw/Personal1/System/smc/new/trust.htm',
			//text:' 了解如何加入信任網站 '
		    //});
			return false;
		}
	}
	
	function AXOrNull(progId) {
		  try {
		    return new ActiveXObject(progId);
		  }
		  catch (ex) {
		    return null;
		  }
	}
	function detectActivex(domId, methodName){
		var m = domId + '.' + methodName;
		try {
			if(eval(m) == undefined){
				return false;
			}
			return true;
		}catch(ee){
			return false;
		}
	}
	function checkDevice(){
		var obj = 'myDevice';
		var method = "getVersion";
		var oLink = {
			url:'http://10.0.30.149/HADMS',
			text:'前往'
		};
		//檢查support 的版本 回傳false則表示ok
		var tmp = checkSupDevice();
		if(tmp){
			displayStatus(_divDevice, false, tmp,oLink);
			return false;		
		}
		if(detectActivex(obj, method)) {
			var version = document.getElementById(obj).getVersion();
			console.log("check version:"+version +"!="+_version);
			if(version == _version){   
			displayStatus(_divDevice, true, "device activex:成功, "+ version);
				return true;
		}else{
		  displayStatus(_divDevice, false, "device activex:版本不符，請安裝新版本。");
		  return false;			
			}
		
		}
		displayStatus(_divDevice, false, "device activex:fail　(檢查是否有先行下載安裝IKVMSetup套件)",oLink);
		return false;
	}
	
	    function checkSupDevice() {
        var requisiteTesterID1 = "RequisiteTester1";
        var requisiteTesterID2 = "RequisiteTester2";
        var requisiteTester;
        var requisiteRequiredMessage = " 未安裝或版本太舊！(最低版本需求：15,7,31,0)";  //大包的版號
        var versionString;
        var errorDesc = "";
        var returnValue = true;

        try {
            requisiteTester = document.getElementById(requisiteTesterID1);

            if (requisiteTester == null)
            {
                errorDesc = "\"" + requisiteTesterID1 + "\" Object ID 不存在！";
                returnValue = false;
            }
            else
            {
                versionString = requisiteTester.getVersion();
                //alert(requisiteTesterID1 + " 版號：" + versionString);
            }
        } catch(e) {
            errorDesc = "IKVMSetup1" + requisiteRequiredMessage;
            returnValue = false;
        }

        try {
            requisiteTester = document.getElementById(requisiteTesterID2);

            if (requisiteTester == null)
            {
                if (errorDesc.length > 0)
                    errorDesc += "\n";

                errorDesc += "\"" + requisiteTesterID2 + "\" Object ID 不存在！";
                returnValue = false;
            }
            else
            {
                versionString = requisiteTester.getVersion();
                //alert(requisiteTesterID2 + " 版號：" + versionString);
            }
        } catch(e) {
            if (errorDesc.length > 0)
                errorDesc += "\n";

            errorDesc += "IKVMSetup2" + requisiteRequiredMessage;
            returnValue = false;
        }

        if (! returnValue){
            //alert("Device.pb.setup():\n" + errorDesc);
            return  errorDesc;
          }

        return false;  //false = ok
    }
	function checkAll(bGoNext) {
	
		if(!checkTrust())
			return false;
		
		if(!checkDevice())
			return false;
		if(bGoNext){
			//暫時移除
			//document.location.href = _nextPage;
		}
		return true;
	}
	
	
	return {
		init : init,
		checkAll:checkAll,
		checkDevice:checkDevice,
		checkTrust:checkTrust
		
	};
}(jQuery));




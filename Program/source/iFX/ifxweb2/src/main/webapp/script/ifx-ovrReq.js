
function OvrReq(oTranData){
	var token,supervisor,myDeferred,		
	    userId = oTranData['userId'],    
	    txcd = oTranData['txcd'],
		screenFile = oTranData['screenFile'],
		obufgch = oTranData['obufgch'],
		myDeferred,
		$localDiv = $('#' + oTranData['localDiv']),
		reasons = (oTranData['reasons']).join('\n');
	
	
	function getToken() { return token; }
	function getSupvisor(v) {
		if(!v) return supervisor;
		supervisor = v; 
	}
	
	var supIndex = ["0007", "0017", "0027"];
	function init() {
		myDeferred = new $.Deferred();
		var remotePage = oTranData['remotePage'];
		var remoteDiv = oTranData['remoteDiv'];
		
		var ii=0;
		$localDiv.load(remotePage + ' #' + remoteDiv, function(r){
			
//			$localDiv.css({
//				left : 10,
//				top : 20,
//			}).show();
			
			var $p = $('#reasons', $localDiv);
			$p.val(reasons);
			
			$localDiv.show();
			
			$localDiv.dialog({
				  autoOpen: true,            
				  height: 450,            
				  width: 700,            
				  modal: true,
				  beforeClose:function() {
					  //var retval = window.confirm('are you sure?');
					  //return retval;
						myDeferred.resolve(false, 'cancelled by teller');
				  }
			});
			$localDiv.dialog().prev().find(".ui-dialog-titlebar-close").hide();
		    
			togglePanel('pChoose');
			$('#btnSupervisors').button()
				.off('click').on('click',function() {
					console.log("btnSupervisors click!");
					ovrGetSupervisor(txcd, obufgch, supIndex.indexOf(reasons.substring(0, 4)));
			} ).trigger('click');
			
			$('#btnSendReq').button().off('click').on('click',function() {
				supervisor = null;
				token = (new Date()).getTime();
				var selectedIndex = $("#supervisors :selected").index();
				if(selectedIndex==0) {
					alert("請選擇主管");
					return;
				}
				supervisor = $('#supervisors :selected').val();
				MswCenter.sendOvrReq(supervisor, {
					userId:userId,
					token:token, 
					txcd:txcd, 
					screenFile:screenFile, 
					reasons:reasons});
				//promptDeferred.reolve();
			});
			$('#btnCancelSelect').button().off('click').on('click', function(){
				$localDiv.dialog('close');
			});
			
			$('#btnCancelWait').button().off('click').on('click',function(){
				MswCenter.cancelWaitOvr(supervisor, token);
				//$localDiv.dialog('close');
				togglePanel('pChoose');
			});

			
			
			/*
			var promptDeferred = new $.Deferred();
			
			promptDeferred.always(function(){
				alert('always');
				$localDiv.dialog('close');
			});
			
			promptDeferred.done(function(){
				clearTimeout(nTimeout);
				alert("done");
			});
			promptDeferred.fail(function(){
				alert("failed");
			});
			
			var nTimeout = setTimeout(promptDeferred.reject,30*1000);
			*/
		});
		
		return myDeferred.promise();
	} //end of init
	function progress(s) {
		s = s || "";
//		var $o = $('#ovrProgressInfo');
//		$o.val($o.val()  + s + "\n").hide().slideDown();
//		scrollDown($o);
		addLog('[OVR] '+s);
		
		li(s);
		
		function li(desc, color) {
			var time = new Date();
			var t =  "(" +
			    ("0" + time.getHours()).slice(-2)   + ":" + 
			    ("0" + time.getMinutes()).slice(-2) + ":" + 
			    ("0" + time.getSeconds()).slice(-2) + ")"; 
			    
			
	        $('<li/>').text(desc+t).css("color",color !=null ? color: 'greeb').appendTo($('#pProgress'));
	           
	    }     
	}
	
	  
	
	var stepFuncMap = {
			'pChoose':function() {
				token = null;
				supervisor = null;
			},
			'pWait':function(){
				
			},
			'pResponse':function() {
				
			}
	};
	
	function togglePanel(p) {
		console.log('enter ovr step:' + p);
		$('#pChoose, #pWait, #pResponse').hide();
		var fn = stepFuncMap[p];
		if(fn) fn();
		$('#'+p).show();
	}
	function postOvr(action, token, msg, supervisor, supnm) {
		if(isSameToken(token)) {
			token = null;
			if(action=='accept') {
				myDeferred.resolve(true,supervisor,supnm);
			}else {
				myDeferred.reject(msg);
			}
			// 先變更myDefferred狀態  再close dialog  否則在dialog之beforeClose()會將myDeffer設為resolve with error
			$localDiv.dialog('close');
		}
	}
	function isSameToken(aTok) {
		if(token==null) {
			console.log('ovr renew');
			return false;
		}
		if(token == aTok){
			console.log('same token');
			return true;
		}
		console.log('different token');
		return false;
	}
	return {
		init:init,
		getToken:getToken,
		progress:progress,
		togglePanel:togglePanel,
		isSameToken:isSameToken,
		postOvr:postOvr
	};
}


function buildSelect(elementId, list) {
	var $o = $('#' + elementId);
	$o.html('');
	var first=null;
	for(var i=0; i < list.length; i++) {
		var ss = list[i].split('|');
		ss[2] = $.trim(ss[2]);
		$o.append(new Option(ss[2]+ ' ' + ss[0] , ss[0]));
		if(i==0) first = ss[0];
	}
	 setTimeout(function() {
		 $o.attr('size', list.length);
		 $o.val(first);
         $o.focus().select();
      }, 10);
}

function ovrGetSupervisor(txcd , obufgch, supLevel){
	console.log("in ovrGetSupervisor!");
	MswCenter.getSupervisors("ovrGetSupervisorDone", txcd, obufgch, supLevel ? supLevel : 0);  //柯 給交易代號和權限
}
function ovrGetSupervisorDone(retval, m) {
	console.log("in ovrGetSupervisorDone!");
	if(retval==0) {
		if(!m || m.length==0) {
			m = [ "目前線上無主管||"];
		}else {
			m.unshift('請選擇|| ');
		}
		buildSelect('supervisors', m);
	}else {
		alert(m);
	}
}


//function sendOvrReq(token, message) {
//	//$('#selectList :selected').text();
//	var selectedIndex = $("#supervisors :selected").index();
//	if(selectedIndex==0) {
//		alert("請選擇主管");
//		return;
//	}
////	alert($('#supervisors :selected').val());
//	var sup = $('#supervisors :selected').text();
////	alert(sup);
//	MswCenter.sendOvrReq(sup, token, message);
//}
function __sendOvrReqDone(retval, m){
	if(retval==0) {
		$("#theSelectedSupervisor").text("("+ m[2] + m[1] +")");
		clientOvrReq.togglePanel('pWait');
		clientOvrReq.progress('已送出授權要求');
	}else {
		alert(m);
	}
}


function __cancelWaitOvr(retval, m){
	if(retval==0) {
		clientOvrReq.progress('取消授權要求已送出');
	}else {
		addLog(m);
		alert(m);
	}
}
function __ovrProgress(retval, arr){
	if(retval>=0) {
		var sup = arr[0];
		var msg = arr[1];
		var t = msg;
		clientOvrReq.progress(t);
	}
}

function __changeOvrStep(retval, arr) {
	if(retval==0) {
		var rToken = arr[0];
		// TODO:check token
		var stepName = arr[1];
		var msg = arr[2];
		clientOvrReq.togglePanel(stepName);
		clientOvrReq.progress(msg);
		
	}else {
		addLog(arr);
		alert(arr);//error message
	}
}


function __ovrDone(retval, arr) {
	if(retval==0) {
		var action = arr[0];
		var token = arr[1];
		var msg = arr[2];
		var supervisor = arr[3];
		var supnm = arr[4];
		clientOvrReq.postOvr(action, token, msg, supervisor, supnm);
	}
}
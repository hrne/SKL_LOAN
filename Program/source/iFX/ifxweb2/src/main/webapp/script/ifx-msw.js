function initDwr() {
	dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setNotifyServerOnPageUnload(true);
	dwr.engine.setErrorHandler(function(msg, ex) { 
		msg = msg || "A server error has occurred.";
		if(confirm('信息系統錯誤，是否重新登入?\n' + msg  + "\n" + JSON.stringify(ex))){
//			window.location.reload();
		}
	});
	 
	dwr.engine.setPollStatusHandler(updatePollStatus);

	
	dwr.util.addOptions("list", [ "All" ]);

	join();
}
function join(){
	MswServer.join();
}
function updatePollStatus(pollStatus) 
{
    dwr.util.setValue("message", pollStatus ? "Online" : "Offline", {escapeHtml:false});
}


function signon(info){
	dwr.util.setValue("message", "signed");
	
	dwr.util.setValue("userInfo", info);
}
function showError(t) {
	dwr.util.setValue("message", t);
}
function members(list){
	console.log(JSON.stringify(list));
	list.unshift("All|All");
	
//	dwr.util.removeAllOptions("list");
//	dwr.util.addOptions("list", list);
	addMyList("list", list);
}
function addMyList(elementId, list) {
	var $o = $('#' + elementId);
	$o.html('');
	for(var i=0; i < list.length; i++) {
		var ss = list[i].split('|');
		$o.append(new Option(ss[1], ss[0]));
	}
}
function sendMessage() {
	var m = createPassage($('#list').val(), null, null,$('#text').val()); 
	MswServer.send(m);
	dwr.util.setValue("text", "");
}
function createPassage(to, action, type, content) {
	to = to || 'ALL';
	action = action || 'TALK';
	type = type || 'text';
	return {
		to:to,
		action:action,
		type:type,
		time: new Date(),
		content:content
	};
}
function getSupListReq(){
	var passage = createPassage(null, 'getSupListReq');
	MswServer.send(passage);
}

function getSupListResp(list){
	console.log(JSON.stringify(list));
//	dwr.util.removeAllOptions("supList");
//	dwr.util.addOptions("supList", list);
	
	addMyList("supList", list);
}
function sendOvrReq(){
	var to = $('#supList').val();
	alert(to);
	if(!to) {
		alert("select a supervisor");
		return;
	}
	var passage = createPassage(to, 'ovrReq',null, 'just a ovr request');
	

	MswServer.send(passage);
	
}
function lock(m, timeoutSeconds) {
	//var fn = getTabfn('blockCenter');
	//fn(m, timeoutSeconds * 1000);
}
function unlock() {
//	var fn = getTabfn('unBlock');
	//fn();
}
function jsOvrHandler(passage){
	var hnd = "jsOvrHandler";
	console.log('ovrResp:' + JSON.stringify(passage));

	switch(passage.action) {
	case 'ovrReq': // supervisor got ovr request
		$("#ovrList").append(new Option(passage.content, passage.from));
		var ack = createPassage(passage.from, 'ack','text', hnd + '.ovrReqAck');
		ack.ackSessionId = passage.ackSessionId;
		setTimeout(function(){
			MswServer.send(ack);	
		},200);
		
		break;
	case 'ovrReqAck':
		unlock();
		lock("授權要求已送出, 等候處理", 60);
		break;
	default:
		alert("unknown action:"+ passage.action);
	}
	
	
}

function receiveMessages(p) {
	console.log(JSON.stringify(p));
	var chatlog = "";
	chatlog = "<div>" + dwr.util.escapeHtml(p.content) + "</div>"
				+ chatlog;
	
	dwr.util.setValue("chatlog", chatlog, {
		escapeHtml : false
	});
}

function getTabfn(n) {
	return top.frames['easytab'].contentWindow[n];
}
function testB() {
	
	var fn = top.frames['easytab'].contentWindow['blockAWhile'];
	fn('block####', 3000);
}

/*
$(function(){
	//init();
});
*/
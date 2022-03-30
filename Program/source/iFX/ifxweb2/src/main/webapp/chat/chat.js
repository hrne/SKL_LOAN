function init() {
	dwr.engine.setActiveReverseAjax(true);
	dwr.engine.setErrorHandler(function(msg, ex) { 
		msg = msg || "A server error has occurred.";
		alert(msg);
	});
	
	
	dwr.util.addOptions("list", [ "All" ]);
}

function join() {
	var name = dwr.util.getValue("name");
	ChatServer.join(name);
}
function signon(){
	$('#sign').hide();
	$('#play').show();
	$('#chatlog').show();
}
function members(list){
	list.unshift("All");
	dwr.util.removeAllOptions("list");
	dwr.util.addOptions("list", list);
}
function showError(t) {
	dwr.util.setValue("message", t);
}
function sendMessage() {
	var text = dwr.util.getValue("text");
	var m = { from:$('#name').val(),
				to:$('#list').val(),
				text:$('#text').val()};
	
	ChatServer.addMessage(m);
	dwr.util.setValue("text", "");
}

function receiveMessages(messages) {
	var chatlog = $('#chatlog')[0].innerHTML;  //�_��令��log��
	for(var i=0; i < messages.length; i++) {

		chatlog = "<div>" + dwr.util.escapeHtml(messages[i]) + "</div>"
				+ chatlog;
	}
	dwr.util.setValue("chatlog", chatlog, {
		escapeHtml : false
	});
}

// talk.js


// socket settings

var _LISTENNING_PORT = "9999";


var serverHandler=null;
var clientHandler=null;



function startListener()
{
	//alert("start listener to " + _LISTENNING_PORT);
	//±Ò°Êtcp server
	TalkTalk.LocalPort = _LISTENNING_PORT;
	TalkTalk.MaxClients = 10;  // max connections
	TalkTalk.StartListening(); // 
	setServerHandler(generalHandler);
	
	idle();
	
}

function generalHandler(index,ip, strData)
{
	
	//alert("receive msg from socket index:" + index + ", ip:"+ ip );
	//alert("data:"+ strData);
	
	var cmd = strData.substr(0,3);
	//alert("cmd:" + cmd);
	switch(cmd) {
		case OVR_REQUEST:
			//alert("request for override!!");
			processOvrRequest(index, ip, strData);
			break;
		case OVR_PROCESSING:
			//alert(index + " ovr is processing, supervisor is copying your screen");
			processOvrScrnCopy(index, ip, strData);
			break;
		case OVR_APPROVED:
			//alert("ovr is approved");
			processOvrApproved(index, ip, strData);
			break;
		case OVR_REJECTED:
			//alert("ovr is rejected");
			processOvrRejected(index, ip, strData);
			break;
		case OVR_CANCEL:
			//alert("cancel for override!!");
			processOvrCancel(index, ip, strData);
			break;
	}
	

	
}
function serverReply(index, msg)
{
	//alert("serverReply:" + msg);
	return TalkTalk.Send(index, msg);
}
var bConnected = false;
function setServerHandler(rtn) {	serverHandler = rtn; }
function resetServerHandler()  { serverHandler == null; } 
function setClientHandler(rtn) {	
	clientHandler = rtn; 
}
function resetClientHandler() { 	clientHandler = null; }

function connectRemoteServer(ip, port)
{
	//alert("try connect to " + ip + ":" + port);
	// client connect to remote tcp server
	bConnected = TalkTalk.tcpClientConnect(ip, port);
	
	if (bConnected) {
		//alert("connected");
	}
	else  {
		alert("not connected, reason:" + TalkTalk.tcpClientLastError());
	}
	
}
function isConnected()
{
	return bConnected;
}

function sendRequest(ip, s, handlerRtn)
{
	// client send message to tcp server
	connectRemoteServer(ip, _LISTENNING_PORT);
	if (!isConnected()) { return false; }
	
	//alert("sending "+ s + " to remote site");
	
	setClientHandler(handlerRtn);

	if (TalkTalk.tcpClientSend(s)) {
		//alert("send tcp ok");
		return true;
	}
	else {
		//alert("send tcp fail");
		resetClientHandler();
		return false;
	}
}

function nap(i)
{
alert("nap:" + i);
	TalkTalk.myNap(parseInt(i,10));
}


function idle()
{
	try {
	TalkTalk.pause();
	}catch(ee) {
		//AlternatePause(100);
	}
}


 function AlternatePause(numberMillis) {
        var dialogScript = 
           'window.setTimeout(' +
           ' function () { window.close(); }, ' + numberMillis + ');';
        var result = 

         window.showModalDialog(
           'javascript:document.writeln(' +
            '"<script>' + dialogScript + '<' + '/script>")');


     }
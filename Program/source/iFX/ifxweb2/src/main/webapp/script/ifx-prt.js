function IfxPrt($, serviceUrl) {
	var printService = serviceUrl + '/hnd/p';
	var states = [ "opening", "printing", "closing" ];
	var curr = 0;
	var lines = [];
	var printDeferred=null;
	function open() {
		return sendCmd({
			'cmd' : 'open',
			'p' : 'printer1'
		});
	}
	function sendCmd(request) {
		return $.ajax({
			type : "GET",
			crossDomain : true,
			cache : false,
			url : printService,
			dataType : 'jsonp',
			data : request,
			success : function(r) {
				handler(r);
			},
			error : function(xhr) {
				alert('Error:print request:' + xhr);
				printDeferred.reject('error printing');
			}
		});
	}

	function handler(response) {
		var currState = states[curr];
		switch (currState) {
		case 'opening':
			if (isSuccess(response)) {
				this.token = response['token'];
				curr++;
				printLine();
			}
			break;
		case 'printing':
			if (isSuccess(response)) {
				printLine();
			}
			break;
		case 'closing':
			isSuccess(response);
			printDeferred.resolve();
			break;
		default:
			break;
		}
		return true;

		function isSuccess(response) {
			if (response.retval == 0)
				return true;
			console.log("curr state:" + currState + "->" + response.message);
			alert(response.message);
			return false;
		}
	}
	;

	function printLine() {
		//var t = lines.shift();
		var a = lines.splice(0,10);
		
		if (a.length > 0) {
			var t = a.join('\r\n') + '\r\n';
			return sendCmd({
				'token' : this.token,
				'cmd' : 'write',
				'p' : t
			});
		} else { // no more line
			curr++; // close state
			return close();
		}
	}

	function close() {
		return sendCmd({
			'token' : this.token,
			'cmd' : 'close'
		});
	}

	// public method

	this.printDoc = function(arr) {
		lines = arr;
		curr = 0;
		printDeferred = new $.Deferred();
		open();
		return printDeferred.promise();
	};

	this.hello = function(fnOk, fnErr)
	{
		var u = serviceUrl + '/hello';
		return $.ajax({
			type : "GET",
			crossDomain : true,
			cache : false,
			url : u,
			success : function(r) {
				if(fnOk!=null)
					fnOk(r);
			},
			error : function(xhr) {
				if(fnErr!=null) fnErr();
				else
					alert("Error:print service no response\n"+xhr);
			}
		});
	};

}

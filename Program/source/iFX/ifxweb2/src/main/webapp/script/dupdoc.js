$(function(){
	var jqTxtNo = $('#txtno'),
		jqBtnSearch = $('#btnSearch');
	
	jqBtnSearch.click(function(){
		if(jqTxtNo.val()) {
			sendSearch($('#brno').val(), jqTxtNo.val(),$('#dt').val());
		}
	});
	
	
	if(jqTxtNo.val().length > 0) {
		jqBtnSearch.trigger('click');
	}
});

function sendSearch(brno, txtno, dt) {
	$('#result').html('');
	var data = { "cmd":"search", "brno" : brno, "txtno": txtno, 'dt':dt};
	sendData(data, function(x){
		
		console.dir("dir:"+x);
		console.log("log:"+x);
		var printTimesMap = {};
		if(x['printLog']!= undefined) {
			var arrLog = $.csv.toArrays(x['printLog']);
			console.dir(arrLog);
			for(var i=0;i<arrLog.length; i++){
				var d = arrLog[i][0];
				if(printTimesMap[d]==undefined) printTimesMap[d] = 1;
				else printTimesMap[d]++;
			}
			console.dir(printTimesMap);
		}

		for(var k in x.result) {
			 x.result[k].prints= printTimesMap[k] === undefined ? 0 : printTimesMap[k];
			showDoc(brno, txtno, dt, k, x.result[k]);
			
		}
		
		$('#result').css('padding',"20px");
		/*
		$('#result').append('<br/><input type="button" id="btnFetch" value="Fetch"/>');
		$('#btnFetch').on('click', function() {
			$("[id^=chk]").each(function(){
				alert($(this).attr('id') + ":" +  $(this).attr('checked')); 
			});
		});
		*/
	});
	
} 
function showDoc(brno, txtno, dt, formId, doc) {
	var btnId = 'btn' + formId,
		contentId = 'txt' + formId;
	
	var t = '<br/><input type="button" class="printButton" id="'+ btnId  + '" value="列印 ' + formId + '" /><br/>';
	t += '<div id="' + contentId + '">' + '</div><hr/>';
	
	$('#result').append(t);
	
	function addDupPrintMark(){
		incrementPrintTimes(doc);
		displayOutput(contentId, doc.content, doc.prints++);
	}
	
	addDupPrintMark();
	
	$('#' + btnId).on('click', function() {

		var data = { "cmd":"incr", "brno" : brno, "txtno": txtno, 'dt':dt, 'form':formId};
		sendData(data, function(x){
			console.log("updated");
			alert(doc.content);
			addDupPrintMark();
		});
		
	});
	function incrementPrintTimes(doc) {
		var ss = doc.content.split("\n"),
			dupLabel = '[重印:' + (doc.prints + 1) + ']';
		if(doc.line0 == undefined) doc.line0 = ss[0]; // save original line[0]
		ss[0]= dupLabel + doc.line0; //先不管超長或位置改變
		doc.content = ss.join('\n');
	}
	function displayOutput(p, content) {
		p = '#' + p;
		var t = content.replace(/[ ]/g, "&nbsp;");
		//t = t.replace(/\n/g,"<br/>");
		var ss = t.split("\n");
		console.log(ss.length);
		t = "";
		$.each(ss, function(i,x){
			t += "<tr><td>&nbsp;&nbsp;</td><td>"  + x + "</td><td>&nbsp;&nbsp;	</td></tr>";
		});
		t = "<table class='doctable'>" + t + "</table>";
		
		console.log(t);
		console.log(p);
		$(p).html(t);
		$(p + " table tr:even").addClass("evencolor");
		$(p + " table tr:odd").addClass("oddcolor");
		$(p).hide().delay(300).slideDown(1000);      
	}
	
}
function createCheckbox(name) {
	return '<input type="checkbox" id="chk_' + name + '" />' + name + "<br/>"; 
}

function sendData(post_data, recv_handler) {
	blockIt('請稍候');
$.ajax({
	type: 'POST',
	url:"savedoc.jsp",
	data: post_data,
	dataType: "json",
	success: function(data) {
		unBlock();
		mutext = false;
		console.dir(data);
		if(data.status == "ok") {
			if(recv_handler) recv_handler(data);
			
		}else {
			alert("Savedoc fail:"+data.message);
		}
	},
	error : function(data){
		unBlock();
		console.log("Send Error!!" + data.status + ",readyState:" + data.readyState +",statusText:"+data.statusText+",responseText:"+data.responseText+ ",message:" + data.message);
		//"通訊錯誤! status:" + data.status + ",readyState:" + data.readyState +",statusText:"+data.statusText+",responseText:"+data.responseText
		alert("Send Error!!" + data.status + ",readyState:" + data.readyState +",statusText:"+data.statusText+",responseText:"+data.responseText+ ",message:" + data.message);
	}
 });
}
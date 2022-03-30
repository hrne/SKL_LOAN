<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'log.jsp' starting page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<script>
			var filetmpname = "C:/ifx/IFX_PRINTER.config";
			
			// 使用message對象封裝消息  
var message = {  
time: 0,  
title: document.title,  
timer: null,  
// 顯示新消息提示  
show: function () {  
var title = message.title.replace("【　　　】", "").replace("【新消息】", "");  
// 定時器，設置消息切換頻率閃爍效果就此產生  
message.timer = setTimeout(function () {  
message.time++;  
message.show();  
if (message.time % 2 == 0) {  
document.title = "【新消息】" + title  
}  

else {  
document.title = "【　　　】" + title  
};  
}, 600);  
return [message.timer, message.title];  
},  
// 取消新消息提示  
clear: function () {  
clearTimeout(message.timer);  
document.title = message.title;  
}  
};  
message.show();  //播放

			var basePath = "<%=request.getContextPath()%>
	";
	//set the default value
	var txtId = 2;

	//var adtestpp=$('#adpassword').val().trim();
	$(document).ready(function() {
		Device.pb.setup('ifxws', 'myDevice');
		Device.pb.init();
		var printstring = Device.pb.prn.list();
		printstring = "\r\n" + printstring;
		var printlist = printstring.split("\r\n");
		console.dir(printlist);

		$("#pselect1").empty();
		for (var i = 0; i < printlist.length - 1; i++) {
			$('#pselect1').append($("<option></option>").attr("value", printlist[i]).text(printlist[i]));
		}
		console.log(printstring);
	});

	//add input block in showBlock
	$("#btn").click(
			function() {

				$("#showBlock").append(
						'<div id="div' + txtId + '">FORM:<input type="text" id="form'+txtId+'" size=10  /> 印表機:<select id="pselect'+txtId+'" class="printselect"  /> <input type="button" value="刪除" onclick="deltxt('
								+ txtId + ')"></div>');

				$("#pselect" + txtId).html($("#pselect1").html());
				txtId++;
			});

	//remove div
	function deltxt(id) {
		$("#div" + id).remove();
	}
</script>

<%@include file="/env/myDevice.jsp"%>

</head>

<body>
	設定FORM_NAME印表機
	<input type="button" id="btn_read" value="讀取設定檔" />
	<br>
	<br>
	<!-- add new item Dynamically in the show block -->
	<div id="div1">
		FORM:<input type="text" id="form1" size=10 /> 印表機:<select id="pselect1" />
	</div>

	<div id="showBlock"></div>
	</BR>
	<!-- click the button to add new item -->
	<input type="button" id="btn" value="增加設定" />
	<input type="button" id="btn_save" value="儲存" />
	<input type="button" id="btn_upload" value="上傳" />
	<br>

	<script>
		//開始預設強制讀取一次
		$(document).ready(function() {
			readIfxPrinter();
		});

		$("#btn_save").click(function() {
			saveFunction();
		});
		$("#btn_upload").click(function() {
			uploadFunction();
		});
		$("#btn_read").click(function() {
			message.clear(); //清除
			readIfxPrinter();
		});
		function saveFunction() {
			//Device.pb.setup('ifxws', 'myDevice');
			var json = {};
			for (var i = 1; i < txtId; i++) {
				try {
					// code to try

					var formid = document.getElementById("form" + i).value;
					var pselectid = document.getElementById("pselect" + i).value;
					pselectid = pselectid.slice(0, pselectid.length);
					console.log(pselectid);
					if (formid && pselectid) {
						json[formid] = pselectid;
					}
				} catch (e) {
					console.log("continue");
				}
			}
			console.dir(json);
			console.log(JSON.stringify(json));

			//預設路徑
			Device.pb.file.write(filetmpname, JSON.stringify(json), false);

			//document.getElementById("pselect1").value="Fax";
		};
		//上傳給資訊部查看設定檔案
		function uploadFunction() {
			var ifx_printer = Device.pb.file.read(filetmpname);
			var filename = "printer/" + _addr;
			filename += '.txt';
			ifxFile.put(filename, ifx_printer);
		};

		function readIfxPrinter() {
			//預設路徑
			Device.pb.file.exists(filetmpname, true);
			var ifx_printer = Device.pb.file.read(filetmpname);

			//有可能沒有該檔案
			if (!ifx_printer) {
				ifx_printer = "{}";
			}
			var jsonlist = JSON.parse(ifx_printer);
			var count = 0;
			$("#showBlock").empty();
			txtId = 2;
			$.each(jsonlist, function(i, x) {
				count++;
				document.getElementById("form" + count).value = i;
				console.log("(" + count + ") " + i + ":" + x);
				document.getElementById("pselect" + count).value = x;
				//有可能上次設定但這次沒有這個印表機，故檢查一次
				if (document.getElementById("pselect" + count).value != x) {
					console.log(i + ":印表機名稱錯誤");
					alert(i + ":印表機名稱錯誤");
				}
				$('#btn').trigger('click');
			});
		};
	</script>
</body>
</html>

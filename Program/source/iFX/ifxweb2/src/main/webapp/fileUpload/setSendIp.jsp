<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title>設定回覆IP位置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">

<script>
var txId = 2;
$("#btn").click(function() {
	$("#showBlock").append('<div id="div' + txId + '">交易名稱 <input type="text" id="p_' + txId + '" value="" onkeyup="this.value = this.value.toUpperCase();" /> IP位置 <input type="text" id="s_' + txId + '" value="" /><input type="button" value="刪除" onclick="deltxt(' + txId + ')"></div>');
	txId++;
});

$(document).ready(function() {
	io(0);
});

//remove div
function deltxt(id) {
	$("#div" + id).remove();
}

$(function() {
	$("#ipForm").submit(function (e){
		e.preventDefault();
		io(1);
	});
})

function io(funcd) {
		var reqUrl = '<%=request.getContextPath()%>' + "/mvc/file/sendIp";
		console.log(reqUrl);
		//	    var formData = new FormData($("#ipForm"));
		//		document.getElementById('submit').style.color = '#a6acaf';
		//		document.getElementById('submit').style.backgroundColor = '#e5e7e9';
		var jsonuserinfo = {};
		for (var i = 1; i <= txId - 1; i++)
			jsonuserinfo[$("#p_" + i).val()] = $("#s_" + i).val();
		if (funcd == 1)
			jsonuserinfo["funcd"] = "set";
		else
			jsonuserinfo["funcd"] = "get";
		var fs = JSON.stringify(jsonuserinfo);
		$.ajax({
			type : "post",
			url : reqUrl,
			data : {
				_d : fs,
			},
			cache : false,
			dataType : 'text', // 回傳的資料格式
			success : function(data) {
				if (funcd == 0) {
					if (data.indexOf("Error!!") != -1) {
						$("#st").html(data);
						return;
					}
					var map = JSON.parse(data);
					var count = 0;
					$.each(map, function(k, v) {
						count++;
						if (k.indexOf("funcd") != -1)
							;
						else {
							document.getElementById("p_" + count).value = k;
							document.getElementById("s_" + count).value = v;
							$('#btn').trigger('click');
						}
					});
				} else {
					$("#st").html(data);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("上傳失敗 Status : " + XMLHttpRequest.status + "readyStatus : " + XMLHttpRequest.readyState + "textstatus : " + textStatus);
			}
		}).fail(function() {
			alert("Fail!!");
		});
	}
</script>
</head>
<body>
	<form id="ipForm" name="ipForm">
		<div id="div1">
			交易名稱 <input type="text" id="p_1" value="" onkeyup="this.value = this.value.toUpperCase();"/> IP位置 <input type="text" id="s_1" value="" />
		</div>
		<div id="showBlock"></div>
		<input type="button" id="btn" value="增加" /> <input id="submit" name="submit" type="submit" value="送出">
		<div id="st"></div>
	</form>
</body>
</html>
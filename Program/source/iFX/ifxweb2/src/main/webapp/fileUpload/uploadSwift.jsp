<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ page session="false"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
/* 進度條的css */
.progress {
	position: relative;
	width: 400px;
	border: 1px solid #ddd;
	padding: 1px;
	border-radius: 3px;
}

.bar {
	background-color: #7cc7ee;
	width: 0%;
	height: 20px;
	border-radius: 3px;
}

.percent {
	position: absolute;
	display: inline-block;
	top: 3px;
	left: 48%;
}
</style>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<script src="https://code.jquery.com/jquery-3.3.1.min.js">
	</script>
<title></title>
</head>
<body>
	<div class="page-header">
		<h1>檔案上傳</h1>
	</div>
	<div class="form-group">
		<div class="well well-md">
			<form id="myForm" name="myForm">
				<table>
					<tr>
						<th><input id="file" name="file" type="file" accept=".txt" multiple></th>
						<th>－</th>
						<th><input id="submit" name="submit" type="submit" value="送出"></th>
					</tr>					
				</table>
			</form>
			<!-- 進度條 -->
			<div class='progress' id="progress-div">
				<div class='bar' id='bar'></div>
				<div class='percent' id='percent'>0%</div>
			</div>
			<div class="status" style="display: none;">
				<div class="notif">
					<div class="shine"></div>
					<div class="head" id='st'>上傳成功...</div>
				</div>
			</div>
		</div>
	</div>
	<script>
	$(function(){
	 $("#myForm").submit(function(e){
	   e.preventDefault();
	   
	   if($("#file").val() === ""){
	     alert('請選擇上傳檔案');
	     return;
	   }
	  			var swiftUploadUrl = '<%=request.getContextPath()%>' + "/mvc/file/Upload";
	   			//var ip = location.host;
	   			//var url = "http://" + ip.substring(0, ip.length - 1) + "5/iTX/mvc/hnd/Upload";
				var formData = new FormData($("#myForm")[0]);

				this.submit.disabled = true;
				this.file.disabled = true;
				document.getElementById('submit').style.color = '#a6acaf';
				document.getElementById('submit').style.backgroundColor = '#e5e7e9';

				$.ajax({
					type : "POST",
					url : swiftUploadUrl,
					data : formData,
					cache : false,
					processData : false,
					contentType : false,
					dataType : 'text', // 回傳的資料格式
					success : function(data) {
						$("#st").html(data);
						$(".status").show();
						window.returnValue = true;
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert("上傳失敗 Status : " + XMLHttpRequest.status + "readyStatus : " + XMLHttpRequest.readyState + "textstatus : " + textStatus);
					},
					xhr : function() {
						var xhr = new window.XMLHttpRequest(); // 建立xhr(XMLHttpRequest)物件
						xhr.upload.addEventListener("progress", function(progressEvent) { // 監聽ProgressEvent
							if (progressEvent.lengthComputable) {
								var percentComplete = progressEvent.loaded / progressEvent.total;
								var percentVal = Math.round(percentComplete * 100) + "%";
								$("#percent").text(percentVal); // 進度條百分比文字
								$("#bar").width(percentVal); // 進度條顏色
							}
						}, false);
						return xhr; // 注意必須將xhr(XMLHttpRequest)物件回傳
					}
				}).fail(function() {
					$("#percent").text("0%"); // 錯誤發生進度歸0%
					$("#bar").width("0%");
				});

			});
		})

		var result = '${uploadstatus}';
		if (result == 'true') {
			$(".notifications").show();
		}
	</script>
</body>
</html>
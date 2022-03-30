<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="cache-control" content="no-cache">

<title>遠端授權</title>
</head>
<body>
	<div id="ovrMain"  style="margin:5px">
		<div id="pInfo"  style="margin:5px">
			<span id="title"></span>
			<textarea id="reasons" rows="3" cols="55" readonly></textarea>
		
		</div>
		<div id="pChoose"  style="margin:5px">
			<table  cellpadding="5">
				<tr valign='top'>
					<td>線上主管:</td>
					<td><select id="supervisors"></select></td>
					<td><button id='btnSupervisors'>更新線上主管清單</button></td>
				</tr>
				<tr>
					<td colspan='3'>
						<div   style="margin:10px">
							<button id="btnSendReq">送出</button>
							<button id="btnCancelSelect">取消</button>
						</div>
					</td>
				</tr>
			</table>
		</div>

		<div id="pWait" style="margin:5px">
			請等候主管<span id="theSelectedSupervisor"></span>授權....
			<button id="btnCancelWait">放棄</button>
		</div>
		<div id="pResponse"  style="margin:5px">
			授權訊息:<span id="ovrStatus"></span><br />
			<button id="btnDone">完成</button>
		</div>
			<div id='pProgress'>
			<!-- 
				<textarea id='ovrProgressInfo' Rows="3" Cols="50" readonly></textarea>
				 -->
			</div>
	</div>
</body>
</html>
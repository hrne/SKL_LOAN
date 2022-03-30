<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
 
	<div id="topbar" style="color: white">
		<table id="tablebar">
			<tr>
				
				<td>
					<span class='label'>櫃員姓名 </span><span id="tlrno"   class='fld'></span>
					<span id='ovrPanel' style='display:inline-block'><button id='btnOpenOvrList'>授權</button></span>
				</td>
				<td colspan="2">
					<span class='label'>營業日 </span>
					 <span id="date"   class='fld'></span>
					 <span id="time" class="fld">hh:mm:ss</span>
				 </td>
				<td><span class='label'>結帳部門 </span><span id="dep"   class='fld'></span></td>
				<td><span class='label'>分行名稱 </span><span id="brn"  class='fld'></span></td>
			</tr>
			<tr>
				<td colspan="1">
				<span class='label'>交易狀態 </span>
				<span id="normal" class='fld'></span>
				<span id="step" class='fld step'></span>
				<span id="ec" class='fld ec'></span>
				<span id="chain" class='tranbase fld  chain'></span></td>
				<td colspan="3">
				<span id="od" style='display:nono'></span>
				<span id="txcode" class='txcode'></span></td>
				<td align="center"><span>v311-11-2300.9.2</span></td>				
			</tr>
		</table>
	</div>

</body>
</html>
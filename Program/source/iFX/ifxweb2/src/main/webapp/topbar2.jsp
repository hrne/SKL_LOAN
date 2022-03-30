<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
  <div id="topbar" style="color: black">
    <table id="tablebar">
      <tr>
        <td rowspan="2">
	    	<img src="images/logo.png"/>
		</td>
        <td><span id="time" class="fld">hh:mm:ss</span></td>
        <td>
          <span class='label'>日曆日 </span>
          <span id='dateNow' class='fld'></span>
        </td>
        <td colspan="2">
          <span class='label'>會計日 </span>
          <span id="date" class='fld'></span>
        </td>
        <td><span id="nDayMode" class='fld'></span></td>
        <td><span id="dbTo" class='fld'></span></td>
      </tr>
      <tr>
        <td colspan="1">
          <span class='label'> </span>
          <span id="normal" class='fld'></span>
          <span id="step" class='fld step'></span>
          <span id="ec" class='fld ec'></span>
          <span id="chain" class='tranbase fld  chain'></span>
        </td>
        <td colspan="3">
          <span id="od" style='display:nono'></span>
          <span id="txcode" class='txcode'></span>
        </td>
      </tr>
    </table>
  </div>
</body>
</html>
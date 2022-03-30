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
        <td colspan="2" style="text-align:right;"><span class='label'></span><span id="dep" class='fld'></span></td>
      </tr>
      <tr>
      	<td>
          <span class='label'>上次登錄 </span>
          <span id='lslogin' class='fld'></span>
        </td>
        <td>
        <span class='label'>經辦 </span><span id="tlrno" class='fld'></span>
        <span id='ovrPanel' style='display:inline-block'>
        <button id='btnOpenOvrList'>授權</button>
            <span id="serverStatus"></span>
          </span>
        </td>
      </tr>
    </table>
  </div>
</body>
</html>
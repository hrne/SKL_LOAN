<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/site.css" rel="stylesheet">
<link rel=stylesheet type=text/css
	href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

<link rel="stylesheet" type="text/css" href="jqgrid/css/ui.jqgrid.css" />

<script src="script/fixIE.js"></script>
<script src="script/jquery.js"></script>
<script src="script/underscore.js"></script>
<script src="script/underscore.string.js"></script>
<script src = "script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="jqgrid/js/grid.locale-tw.js" type="text/javascript"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="script/ifx-grid.js"></script>
<script>
function underscore_mixin() {
	 _.mixin(_.str.exports());
	
}
$(function(){
	
	underscore_mixin();
	var gridControls =  {
		'grid':'#grid',
		'pager':'#pager',
		'search':'#btnSearch',
		'reset':'#btnReset'
	};
	var obj = window.opener.dupGrid();
	var grid= new IfxGrid(obj.gridDef, obj.fieldMap ,obj.data, obj.colModel);
	grid.render('#queryArea', gridControls);

});
</script>
</head>
<body>
	<div id="queryArea">
		<table id="grid"></table>
		<div id="pager"></div>
		<input type="BUTTON" id="btnSearch" class='querybutton' value=" 搜 尋 " /> 
		<input type="BUTTON"id="btnReset" class='querybutton' value=" 重 設 " />
	</div>

</body>
</html>
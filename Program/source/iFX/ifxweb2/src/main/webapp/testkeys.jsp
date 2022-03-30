<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="script/jquery.js"></script>
<script src="script/ifx-keys.js"></script>
<script src="script/host/ifx-host.js"></script>
<script>
$(function(){
	var ifxKeys = new IfxKeys();
	//ifxKeys.bind();
/*
	console.log("sending");
	$.ajax({
		type: 'POST',
		url:"send.jsp",
		data: {"tita": "this is a book!"},
		dataType: "json",
		success: function(data) {
			console.log(data);
		},
		error : function(data){
			console.log("ajax error");
			console.log(data);
		}
	 });
	 */
	 var t  = "GI505002612091 0452100000000000000000000000000100010319080077026100000000000000000000000000000000000000000000000000000000000000000000                 5050026100000000GI00G0230                         0000026100000000000000000000000505261      000M000                    201101035050000000000000                000000505000           00000000    000                                                                                                      01";
	 var m = "GI505002610001 0357100000000000000000000000000100010321345456026100000000000000000000000000000000000000000000000000000000000000000000                 5050026100000000GI00G0230                         0000026100000000000000000000000505261      000M000                    201101035050000000000000                000000505000           00000000    000   01";
	 var ifxHost = new IfxHost();
	 ifxHost.parseHeader(m);
	 
});
</script>
</head>
<body>
<input type="text" value="aaa"><br/>
<input type="text" value="bbbaaa"><br/>
</body>
</html>
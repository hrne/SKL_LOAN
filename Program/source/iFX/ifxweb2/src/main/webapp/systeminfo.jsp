<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>index</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="css/systeminfo.css" rel="stylesheet">

</head>
<body>
	<div id='wp' class='wp printBg'>
		<div class='container'>
			<h3>
				看不到印表機就是有問題!! <a href='javascript: void(0)'
					onclick='window.location.reload(true);'>refresh</a> &nbsp; <a
					href="device/setup/St1Device.exe" target="_blank">還沒安裝?</a>
			</h3>
			<img id='imgDeviceServer' src="http://localhost:3310" alt="看不到圖就是有問題" />
			<br />
			<textarea id="buf" rows="10" cols="80">美股過去四年來雖經歷大風大浪，但仍在波濤洶湧中一路向上攀升，
即使到了現在美股還是牛光滿面，《MarketWatch》專欄作家
 Gene Peroni 認為美股無論短期或是長期仍具投資吸引力，大
 膽預測美股今年底之前指數將衝上 14750-15100 點之間，
 2015 年前更是會攀上 18000 點高峰。</textarea>
			<br />
			<button id="print">print</button>
			<input id='num' value='1' />
			<button id="hello">hello</button>
		</div>
	</div>


	<script>
		
	<%out.println("var printServiceUrl='http://localhost:3310';");%>
		
	</script>
	<script src="script/jquery.js"></script>
	<script src="script/ifx-prt.js"></script>
	<script src="script/ifx-systeminfo.js"></script>
</body>
</html>
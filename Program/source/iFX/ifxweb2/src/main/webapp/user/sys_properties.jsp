<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>My JSP 'log.jsp' starting page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<script>
			var filetmpname = "C:/ifx/IFX_PROPERTIES.config";
			
			var basePath = "<%=request.getContextPath()%>";

			//var adtestpp=$('#adpassword').val().trim();
			$( document ).ready(function() {
				Device.pb.setup('ifxws', 'myDevice');
				Device.pb.init();
			});

		</script>
		
  <%@include file="/env/myDevice.jsp" %>

	</head>

	<body>
	<div class="page-header">
			<h1>參數設定</h1>
		</div>
		<div id="main">
			<div class="form-group">
			<div class="well  well-md">
				<h3>	<div class="label label-primary">主管授權撥放提示音</div></h3>
			<label class="radio-inline"><input  type="radio" name="mswmusicS" value="0">soap3</label>
      <label class="radio-inline"><input  type="radio" name="mswmusicS" value="1">soundS</label>
      <label class="radio-inline"><input  type="radio" name="mswmusicS" value="2">soundT</label>
      <label class="radio-inline"><input  type="radio" name="mswmusicS" value="3">0013-2</label>
     <label class="radio-inline"> <input  type="radio" name="mswmusicS" value="4">runescape</label>
				<div style="height:20px"></div>
				<div>儲存後請關閉本視窗，再重新整理網頁(F5)或重新登入即可生效。</div>
			<div>
				<button id="btn_pp_save" class="btn btn-success btn-sm">儲存</button>
				<button id="btn_pp_read" class="btn btn-default btn-sm">讀取本機設定</button>
			</div>
 			</div>
		</div>
	</div>


			<script>
	    var music = new Array("mp3/soap3.wav", "mp3/soundS.wav", "mp3/soundT.wav",
			"mp3/0013-2.wav", "mp3/runescape.wav");
			
			$("#btn_pp_save").click(function () {
              saveFunction();
			});
			$("#btn_pp_read").click(function () {
        readIfxPp();
			});
				function saveFunction() {
					var json ={};
					json["mswmusicS"] = $('input[name="mswmusicS"]:checked').val(); ;
					console.dir(json);
					console.log(JSON.stringify(json));
					//預設路徑
					Device.pb.file.write(filetmpname,JSON.stringify(json),false);
				};
				function readIfxPp() {
					//預設路徑
					Device.pb.file.exists(filetmpname,true);
					var ifx_properties = Device.pb.file.read(filetmpname);
					
					//有可能沒有該檔案
					if(!ifx_properties){
						ifx_properties ="{}";
						}
					var jsonlist = JSON.parse(ifx_properties);
					$.each(jsonlist, function(i, x) {
						var inputname = 'input[name='+i+']';
						var inputfld = document.querySelector(inputname);
						if(inputfld.type == "radio"){
							$(inputname+"[value='"+x+"']").prop("checked",true);
						}
					});
				};
		$( document ).ready(function() {
				setTimeout(readIfxPp, 1);
			});
			//撥個聲音 單次
   $('input[type=radio][name=mswmusicS]').change(function() {
   			 var docSound = document.getElementById('usersound');
   			 docSound.src = "../"+music[this.value];
    });
			</script>
				<bgSound id="usersound" src="" loop=1 />
		</body>
	</html>

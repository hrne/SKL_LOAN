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
			var filetmpname = "C:/ifx/IFX_PRINTER_PARMS.config";
			
			var basePath = "<%=request.getContextPath() %>";
			//set the default value
			var parmsId = 2;

			//var adtestpp=$('#adpassword').val().trim();
			$( document ).ready(function() {
				Device.pb.setup('ifxws', 'myDevice');
				Device.pb.init();
				var printstring = Device.pb.prn.list();
				printstring ="\r\n"+ printstring;
				var printlist = printstring.split("\r\n");
				console.dir(printlist);

				$("#paraselect1").empty();
				for (var i = 0; i < printlist.length-1; i++) {
					$('#paraselect1')
					.append($("<option></option>")
					.attr("value",printlist[i])
					.text(printlist[i]));
				}
				console.log(printstring);
			});

			//add input block in showBlock
			$("#btn_parms").click(function () {

				$("#showBlock_parms").append('<div id="div' + parmsId + '">印表機:<select id="paraselect'+parmsId+'" class="printselect"  /> 上邊距:<input type="text" id="formpara'+parmsId+'" class="iupformpara" size=10  /> <input type="button" class="btn btn-danger btn-xs" value="刪除" onclick="deltxt_parms('+parmsId+')"></div>');

				$("#paraselect"+parmsId).html($("#paraselect1").html());
				parmsId++;
			});

			//remove div
			function deltxt_parms(id) {
				$("#div"+id).remove();
			}
		</script>
		
  <%@include file="/env/myDevice.jsp" %>

	</head>

	<body>
			<div class="page-header">
			<h1>印表機參數</h1>
		</div>
					<div class="form-group">
			<div class="well  well-md">
		<h3>印表機起始位置調整</h3> <input type="button" id="btn_read_parms"  class="btn btn-default btn-sm" value="讀取本機設定檔" />
			<br>
			<br>
			<!-- add new item Dynamically in the show block -->
			<div id="div1">印表機:<select id="paraselect1"  /> 上邊距:<input type="text" id="formpara1" class="iupformpara" size=10 /></div>

				<div id="showBlock_parms"></div>
			<br>
			<!-- click the button to add new item -->
			<input type="button" id="btn_parms" class="btn btn-default btn-sm" value="增加設定" />
				<input type="button" id="btn_save_parms" class="btn btn-success btn-sm" value="儲存" />
     <input type="button" id="btn_upload_parms" class="btn btn-default btn-sm" value="Debug上傳" />
			<br>
		</div>
	</div>
			<script>
				
				//開始預設強制讀取一次
					$( document ).ready(function() {
						 readIfxPrinter_parms();
					});
				
			$("#btn_save_parms").click(function () {
              saveFunction_parms();
			});
			$("#btn_upload_parms").click(function () {
              uploadFunction_parms();
			});			
			$("#btn_read_parms").click(function () {
        readIfxPrinter_parms();
			});
				function saveFunction_parms() {
					//Device.pb.setup('ifxws', 'myDevice');
					var json ={};
					for(var i=1;i < parmsId;i++){
						try {
							 // code to try
					
						var formparaid = document.getElementById("formpara"+i).value;
						var paraselectid = document.getElementById("paraselect"+i).value;
						paraselectid = paraselectid.slice(0,paraselectid.length);
						console.log(paraselectid);
						if( formparaid && paraselectid ){
							json[paraselectid] = formparaid;
						}
							} catch (e) {
							console.log("continue");
						}
					}
					console.dir(json);
					console.log(JSON.stringify(json));
					
					//預設路徑
					Device.pb.file.write(filetmpname,JSON.stringify(json),false);



					//document.getElementById("paraselect1").value="Fax";
				};
				//上傳給資訊部查看設定檔案
				function uploadFunction_parms() {
						var ifx_printer = Device.pb.file.read(filetmpname);
						var filename = "printer/"+_addr;
		            filename += '.txt';
					      ifxFile.put(filename,ifx_printer);
				};
				
				function readIfxPrinter_parms() {
					//預設路徑
					Device.pb.file.exists(filetmpname,true);
					var ifx_printer = Device.pb.file.read(filetmpname);
					
					//有可能沒有該檔案
					if(!ifx_printer){
						ifx_printer ="{}";
						}
					var jsonlist = JSON.parse(ifx_printer);
					var count=0;
					$("#showBlock_parms").empty();
					parmsId=2;
					$.each(jsonlist, function(i, x) {
						count++;
						var paraselectid = x ;
						document.getElementById("formpara"+count).value=paraselectid;
						console.log("("+count+") "+i+":"+x);
						document.getElementById("paraselect"+count).value=i;
						//有可能上次設定但這次沒有這個印表機，故檢查一次
						if(document.getElementById("paraselect"+count).value != i){
							console.log(i+":印表機名稱錯誤");
							alert(i+":印表機名稱錯誤");
						}
						$('#btn_parms').trigger('click');
					});
				};
	
			</script>
		</body>
	</html>

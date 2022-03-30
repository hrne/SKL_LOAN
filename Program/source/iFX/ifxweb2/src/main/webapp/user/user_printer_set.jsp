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
			var filetmpname = "C:/ifx/IFX_PRINTER.config";
			
			var basePath = "<%=request.getContextPath()%>";
			//set the default value
			var txtId = 2;

			//var adtestpp=$('#adpassword').val().trim();
			$( document ).ready(function() {
				Device.pb.setup('ifxws', 'myDevice');
				Device.pb.init();
				var printstring = Device.pb.prn.list();
				printstring ="\r\n"+ printstring;
				var printlist = printstring.split("\r\n");
				console.dir(printlist);

				$("#pselect1").empty();
				for (var i = 0; i < printlist.length-1; i++) {
					$('#pselect1')
					.append($("<option></option>")
					.attr("value",printlist[i])
					.text(printlist[i]));
				}
				console.log(printstring);
			});

			//add input block in showBlock
			$("#btn").click(function () {

				$("#showBlock").append('<div id="div' + txtId + '">單據:<input type="text" id="form'+txtId+'" class="iupform" size=10  /> 交易:<input type="text" id="txcd'+txtId+'" size=5  /> 印表機:<select id="pselect'+txtId+'" class="printselect"  /> <input type="button" class="btn btn-danger btn-xs" value="刪除" onclick="deltxt('+txtId+')"></div>');

				$("#pselect"+txtId).html($("#pselect1").html());
				txtId++;
				setFunction();
			});

			//remove div
			function deltxt(id) {
				$("#div"+id).remove();
			}
		</script>
		
  <%@include file="/env/myDevice.jsp" %>

	</head>

	<body>
			<div class="page-header">
			<h1>設定印表機</h1>
		</div>
					<div class="form-group">
			<div class="well  well-md">
		<h3>設定單據自動導入 <div class="label label-primary">WINDOWS印表機</div></h3> <input type="button" id="btn_read"  class="btn btn-default btn-sm" value="讀取本機設定檔" />
			<br>
			<br>
			<!-- add new item Dynamically in the show block -->
			<div id="div1">單據:<input type="text" id="form1" class="iupform" size=10 /> 交易:<input type="text" id="txcd1" size=5 /> 印表機:<select id="pselect1"  /></div>

				<div id="showBlock"></div>
			<br>
			<!-- click the button to add new item -->
			<input type="button" id="btn" class="btn btn-default btn-sm" value="增加設定" />
				<input type="button" id="btn_save" class="btn btn-success btn-sm" value="儲存" />
     <input type="button" id="btn_upload" class="btn btn-default btn-sm" value="Debug上傳" />
			<br>
		</div>
	</div>
			<script>
				
				//開始預設強制讀取一次
					$( document ).ready(function() {
						 readIfxPrinter();
					});
				
			$("#btn_save").click(function () {
              saveFunction();
			});
			$("#btn_upload").click(function () {
              uploadFunction();
			});			
			$("#btn_read").click(function () {
        readIfxPrinter();
			});
				function saveFunction() {
					//Device.pb.setup('ifxws', 'myDevice');
					var json ={};
					for(var i=1;i < txtId;i++){
						try {
							 // code to try
					
						var formid = document.getElementById("form"+i).value;
						var txcdid = document.getElementById("txcd"+i).value;
						var pselectid = document.getElementById("pselect"+i).value;
						pselectid = pselectid.slice(0,pselectid.length);
						console.log(pselectid);
						if( formid && pselectid ){
							var semicolon = txcdid ? ';' : '' ;
							json[formid] = txcdid + semicolon + pselectid ;
						}
							} catch (e) {
							console.log("continue");
						}
					}
					console.dir(json);
					console.log(JSON.stringify(json));
					
					//預設路徑
					Device.pb.file.write(filetmpname,JSON.stringify(json),false);



					//document.getElementById("pselect1").value="Fax";
				};
				//上傳給資訊部查看設定檔案
				function uploadFunction() {
						var ifx_printer = Device.pb.file.read(filetmpname);
						var filename = "printer/"+_addr;
		            filename += '.txt';
					      ifxFile.put(filename,ifx_printer);
				};
				
				function readIfxPrinter() {
					//預設路徑
					Device.pb.file.exists(filetmpname,true);
					var ifx_printer = Device.pb.file.read(filetmpname);
					
					//有可能沒有該檔案
					if(!ifx_printer){
						ifx_printer ="{}";
						}
					var jsonlist = JSON.parse(ifx_printer);
					var count=0;
					$("#showBlock").empty();
					txtId=2;
					$.each(jsonlist, function(i, x) {
						count++;
						var str = x.split(";");
						var txcdid    = str[1] ? str[0] : '' ;
						var pselectid = str[1] ? str[1] : str[0] ;
						document.getElementById("form"+count).value=i;
						console.log("("+count+") "+i+":"+x);
						document.getElementById("txcd"+count).value=txcdid;
						document.getElementById("pselect"+count).value=pselectid;
						//有可能上次設定但這次沒有這個印表機，故檢查一次
						if(document.getElementById("pselect"+count).value != pselectid){
							console.log(i+":印表機名稱錯誤");
							alert(i+":印表機名稱錯誤");
						}
						$('#btn').trigger('click');
					});
				};
				
		//設置欄位內容
		setFunction();
		function setFunction(){
		$('.iupform').autocomplete({
			source : function(request, response) {
				$.ajax({
					cache : false,
			async : false,
			minLength : 0,
			autoFocus : false,
					url : basePath + "/mvc/hnd/codeList/all/SYSTM/FORMDEF",
					success : function(data) {
						response($.map(data, function(item) {
							if(item.content == "FORM,FORMX"){
								return;
							}
		         if (item.key.toUpperCase().indexOf(request.term.toUpperCase()) === 0) {
							return {
								value : item.key,
								label : item.key+"-"+item.content
							};
		          }							
							//$('#helptext').attr("value",helptext);

						}));
					//	$('#helptext').innerHTML(helptext);
					},
					error:function(data){
						alert("error:" + data);
					}
				})
			},
			minLength : 0,
			open : function() {
				$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
			},
			close : function() {
				$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
			}
		}).focus(function(){           
        //Use the below line instead of triggering keydown
             $(this).autocomplete("search");
     });	
   }			
			</script>
		</body>
	</html>

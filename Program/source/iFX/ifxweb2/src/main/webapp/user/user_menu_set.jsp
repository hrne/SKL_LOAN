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
			var filetmpname = "C:/ifx/IFX_MENU.config";
			
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
		設定MENU選單 <input type="button" id="btn_menu_read" value="讀取本機MENU設定檔" />
			<br>
			<br>
			<div >
					<textarea  id="menubox" >blah blah</textarea>
				</div>

			<br>
			<!-- click the button to add new item -->
				<input type="button" id="btn_menu_save" value="儲存" />
     <input type="button" id="btn_menu_upload" value="Debug上傳" />
			<br>

			<script>
				
				//開始預設強制讀取一次
					$( document ).ready(function() {
						 readIfxMenu();
						 $("#menubox").width("60%").height("50%");
						 //document.getElementById("menubox").style.height = "60%";
             //document.getElementById("menubox").style.width = "80%";
					});
				
			$("#btn_menu_save").click(function () {
              saveFunction();
			});
			$("#btn_menu_upload").click(function () {
              uploadFunction();
			});			
			$("#btn_menu_read").click(function () {
        readIfxMenu();
			});
				function saveFunction() {
						console.log("saveFunction!");
						var menutext =$("#menubox").val();
					console.log(menutext);
					menutext = menutext.replace(/\n/g, ',');
					menutext ='["type,sbtype,enabled,txcd,txnm",'+menutext+"]";
					//預設路徑
						console.log("menutext:"+menutext);
					Device.pb.file.write(filetmpname,menutext,false); //JSON.stringify(json)
					
				};
				//上傳給資訊部查看MENU設定檔案
				function uploadFunction() {
						var ifx_menu = Device.pb.file.read(filetmpname);
						var filename = "user_menu/"+_addr;
		            filename += '.txt';
					      ifxFile.put(filename,ifx_menu);
				};
				
				function readIfxMenu() {
					//預設路徑
					Device.pb.file.exists(filetmpname,true);
					var ifx_menu = Device.pb.file.read(filetmpname);
					//有可能沒有該檔案
					if(!ifx_menu){
						ifx_menu ='"0,DD1,0,DD1  ,沒有任何自訂選單1","2,DD1,1,DDDDD,測試選單交易1","2,DD1,1,DDDDD,測試選單交易2","0,DD2,0,DD2  ,沒有任何自訂選單2","2,DD2,1,DDDDD,測試選單交易3","2,DD2,1,DDDDD,測試選單交易4"';
					}else{
						ifx_menu = ifx_menu.slice(33,-1);
					}
						ifx_menu = ifx_menu.replace(/","/g, '"\n"');
						console.log("更新text內容!");
						$("#menubox").val(ifx_menu);
					//document.getElementById("menubox").value = ifx_menu;
				};
							
			</script>
		</body>
	</html>

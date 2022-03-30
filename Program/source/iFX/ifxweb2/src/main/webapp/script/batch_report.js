/** 需求一直改,故確認好後再調整程式碼
*/
//TODO 確認需求後,再美化程式碼與效能之類的
var sendbefore=false;
  	//Device.pb.setup('ifxws', 'myDevice');
  	//Device.pb.init();
var resultdata="";
var _middlelen = 0;
var _contextPath = '${context}';
$(function(){
	
	var jqFileBrno = $('#filebrno'),
	  jqFileTlrno = $('#filetlrno'),
	  jqFileName = $('#filename'),
		jqBtnReportup = $('#btnReportup'),
		jqBtnReportpage = $('#btnReportpage'),
		jqBtnReportpageup = $('#btnReportpageup'),
		jqBtnWinprint = $('#btnWinPrint'),
		jqBtnWinprintall = $('#btnWinPrintall'),
		jqBtnReportdown = $('#btnReportdown'),tempstartline=0,tempcount=0;
		
    jqFileName.change(function() {
      sendbefore = false;
      tempstartline =0;
      tempcount= 0;
      resultdata="";
      jqBtnReportdown.val("送出");
    });
    $('#startline').change(function() {
      sendbefore = false;
      tempstartline =0;
      tempcount= 0;
      resultdata="";
      jqBtnReportdown.val("送出");
    });
    
    $('#count').change(function() {
		  if(jqBtnReportdown.val() == "結束"){
			  alert("此張單據已結束");
			  return;
		  }
    });
  jqBtnWinprint.click(function(){
  	toprinter(resultdata);
 	});
 	
  jqBtnWinprintall.click(function(){
  	
  if(jqFileName.val()) {
	  var data = { "cmd":"reportpageall", "brno" : jqFileBrno.val(), "filetlrno": jqFileTlrno.val(), 'dt':$('#dt').val(), "filename": jqFileName.val(), "gowhere": $('#gowhere').val()};  //日期 分行 (抓前2當業務) 檔名
	  sendData(data, function(x){
	  	if(x.result.length <= 0){
	  		return;
	  		}
	  		toprinter(x.result);
		});
   }else{
			alert("沒有資料可供列印!");
			return;
   	}
 	});
 	
	jqBtnReportdown.click(function(){
		if(jqBtnReportdown.val() == "結束"){
			alert("此張單據已結束");
			return;
		}
		
		var startline = parseInt( $('#startline').val(),10);
		var count = parseInt( $('#count').val(),10);
		if(!sendbefore){
			$('#result').html('');
		}
		
		if(sendbefore ){
			if(tempstartline == startline ){
			 $('#startline').val(startline + count);
			 startline = parseInt( $('#startline').val(),10);
			}else{
			$('#result').html('');
			sendbefore=false;
			}
		}
		
		if(jqFileName.val()) {
			tempstartline = startline;
			tempcount = count;
			sendSearch(jqFileBrno.val(),jqFileTlrno.val(), jqFileName.val(),$('#dt').val(),startline,count,$('#gowhere').val());

		}
	});
	 //no need up page 
	jqBtnReportup.click(function(){

	  var startline = parseInt( $('#startline').val(),10);
		var count = parseInt( $('#count').val(),10);
		
			if(sendbefore && tempstartline == startline && tempcount == count){
			var checkline = startline-count;
			if(checkline <= 0){
				checkline = 0;
				}
			 $('#startline').val(checkline);
			 startline = parseInt( $('#startline').val(),10);
		}
		if(jqFileName.val()) {
			tempstartline = startline;
			tempcount = count;
			sendSearch(jqFileBrno.val(),jqFileTlrno.val(), jqFileName.val(),$('#dt').val(),startline,count,$('#gowhere').val());
			//sendbefore=true;  //已經是 true 才會顯示
		}
	});
	//頁次讀取
	jqBtnReportpage.click(function(){
		if(jqBtnReportpage.val() == "最末頁"){
			return;
		}
		if(jqFileName.val()) {
		resultdata = ""; //清空內容
	   $('#pagenum').val(parseInt( $('#pagenum').val(),10) + 1);
	  var pagenum = parseInt( $('#pagenum').val(),10);
		
		$('#result').html('');
			sendbefore=false;
			sendSearchpage(jqFileBrno.val(),jqFileTlrno.val(), jqFileName.val(),$('#dt').val(),pagenum,$('#gowhere').val());
		}else{
			alert("沒有報表可供讀取!");
			return;
		}
	});
	//頁次讀取上一頁
	jqBtnReportpageup.click(function(){

	  var pagenum = parseInt( $('#pagenum').val(),10);
	  if(pagenum == 1){
	  return;
	  }
	  if(jqFileName.val()) {
	  resultdata = "";//清空內容
	  pagenum = pagenum -1;
		$('#pagenum').val(parseInt( $('#pagenum').val(),10) - 1);
		$('#result').html('');
			sendbefore=false;
			sendSearchpage(jqFileBrno.val(), jqFileTlrno.val(),jqFileName.val(),$('#dt').val(),pagenum,$('#gowhere').val());
		}else{
			alert("沒有報表可供讀取!");
			return;
		}
	});	
	
	
	if(jqFileName.val().length > 0) {
		jqBtnReportpage.trigger('click');
	}
});
function sendSearchpage(brno,filetlrno, filename, dt, pagenum,gowhere) {

	var data = { "cmd":"reportpage","pagenum":pagenum, "brno" : brno, 'dt':dt, "filetlrno": filetlrno,"filename": filename, "gowhere": gowhere};  //日期 分行 (抓前2當業務) 檔名
	console.dir("brno:"+brno);
	console.dir("filetlrno:"+filetlrno);
	sendData(data, function(x){
		
		console.dir("dir:"+x);
		console.log("log:"+x);
		console.log("x.next:"+x.fnext);
		showDoc(brno, filename, dt, x.result,x.fnext);
		
		$('#result').css('padding',"20px");
		/*
		$('#result').append('<br/><input type="button" id="btnFetch" value="Fetch"/>');
		$('#btnFetch').on('click', function() {
			$("[id^=chk]").each(function(){
				alert($(this).attr('id') + ":" +  $(this).attr('checked')); 
			});
		});
		*/
	});
	
} 
function sendSearch(brno,  filetlrno,filename, dt, startline, scount,gowhere) {

	var data = { "cmd":"report","startline":startline,"scount":scount, "brno" : brno, 'dt':dt, "filetlrno": filetlrno,"filename": filename, "gowhere": gowhere};  //日期 分行 (抓前2當業務) 檔名
		console.dir("brno:"+brno);
	console.dir("filetlrno:"+filetlrno);
	sendData(data, function(x){
		
		console.dir("dir:"+x);
		console.log("log:"+x);
		console.log("x.next:"+x.fnext);
		showDoc(brno, filename, dt, x.result,x.fnext);
	  
		
		$('#result').css('padding',"20px");
		/*
		$('#result').append('<br/><input type="button" id="btnFetch" value="Fetch"/>');
		$('#btnFetch').on('click', function() {
			$("[id^=chk]").each(function(){
				alert($(this).attr('id') + ":" +  $(this).attr('checked')); 
			});
		});
		*/
	});
	
} 
function showDoc(brno, filename, dt, doc,fnext) {
	var btnId = 'btn' + filename,
		contentId = 'txt' + filename;
	
	var t = '<div id="' + contentId + '">' + '</div>'; //移除 <hr/>
	console.log("showDoc_sendbefore"+sendbefore);
	if(!sendbefore){
	$('#result').append(t);
 }
	

	displayOutput(contentId, doc ,fnext);
	//var str =  "<tr><td>&nbsp;&nbsp;</td><td>"  + 123123 + "</td><td>&nbsp;&nbsp;	</td></tr>";
	
	function displayOutput(p, content,fnext) {
		p = '#' + p;
		if(content.length <= 0){
			return;
		}
		resultdata +=content;
		console.log("content:"+content);
		var t = content.replace(/[ ]/g, "&nbsp;");
		//t = t.replace(/\n/g,"<br/>");
		var ss = t.split("\n");
		console.log(ss.length);
		t = "";
		$.each(ss, function(i,x){
			if(i == 0){
				//報表固定頭
				_middlelen = x.replace(/&nbsp;/g, " ").length/2-8; //從一半開始算,並包含8個字(合作金庫商業銀行)
				console.log("_middlelen:"+_middlelen);
				t += "<tr><td>&nbsp;&nbsp;</td><td style=font-size:16px align=center valign=middle>新光金控</td><td>&nbsp;&nbsp;</td></tr>";
				t += "<tr><td style=background-color:yellowgreen>&nbsp;&nbsp;</td><td>";
			}else{
			  t += "<tr><td>&nbsp;&nbsp;</td><td>";
		  }
		  t+= x + "</td><td>&nbsp;&nbsp;	</td></tr>";
		});
		if(fnext == "0"){
		   //t+= "<tr><td>&nbsp;&nbsp;</td><td>"  + ".....The end....." + "</td><td>&nbsp;&nbsp;	</td></tr>";
		}else{
		   //t+= "<tr><td>&nbsp;&nbsp;</td><td>"  + ".....To be continue....." + "</td><td>&nbsp;&nbsp;	</td></tr>";
		}
	  if(!sendbefore){
		  t = "<table class='doctable'>" + t + "</table>";
	  }
		
		console.log(t);
		console.log(p);
		console.log("dis_sendbefore"+sendbefore);
		if(!sendbefore){
		  $(p).html(t);
	  }else{
	  	//$(p+" tr:last").hide().delay(300).slideDown(500);      
		  $(p+" tr:last").after(t);
	  }
		$(p + " table tr:even").addClass("evencolor");
		$(p + " table tr:odd").addClass("oddcolor");
		//$(p).hide().delay(300).slideDown(500);      
	}
		sendbefore=true;
}
function toprinter(data_result){
		  var deviceX = Device.pb;
		if(data_result.length <= 0){
			alert("沒有資料可供列印!");
			return;
		}
		//等確定規格後再改寫法
  	deviceX.prn.beginDocument("線上報表");
		var formprinter = ifxFile.getformP(deviceX,"線上報表");
				  	
  	deviceX.prn.setPaperSize(830,1170);
  	deviceX.prn.setPaperOrientation(true);
  	 //縮印成功,但無法避免已超出內容折行的問題
  	//deviceX.prn.setScaleTransform(0.5, 1.0);
//TODO 是否要自動寬度? (大於160 19,大於140 15,大於120 13.3,12)
  	//新增windows的19寬
  	deviceX.prn.setCpi("15");
  	deviceX.prn.setLpi("8");
  	//TODO 報表頭定位點很難和網頁相同
  	var printmiddle = "{{D2-S}}";
  	//兩倍字寬內 /2
  	while (printmiddle.length < parseInt( _middlelen/2,10)) { 
				printmiddle += " ";
		}
		printmiddle += "合作金庫商業銀行{{D2-E}}";
		console.log("printmiddle len:"+printmiddle);
		
		
		data_result = data_result.replace(/{{formfeed}}/g, "{{formfeed}}"+printmiddle);
		
  	deviceX.prn.printText(printmiddle+"\n" + data_result); //$('#result').val()
  	
			if(formprinter){ //TAP系列前插式印表機或媒體 setprname
				console.log("成功指定Form to Printer:線上報表:"+formprinter);
				deviceX.prn.setPrinterName(formprinter);
				deviceX.prn.endDocument(false,false); //Show PrintPreviewDialog ,Show PrintDialog 
			}else{
				deviceX.prn.endDocument(true,true); //Show PrintPreviewDialog ,Show PrintDialog 
			}	
}

function sendData(post_data, recv_handler) {
	blockIt('請稍候');
$.ajax({
	type: 'POST',
	url:"savedoc.jsp",
	data: post_data,
	dataType: "json",
	success: function(data) {
		unBlock();
		mutext = false;
		console.dir(data);
		if(data.status == "ok") {
			if(recv_handler){ 
				recv_handler(data);
				$('#btnReportdown').val("繼續讀取");//行數
			  //$('#btnReportup').show();  //no need up page 
			}
		  if(data.fnext == "0"){
	      $('#btnReportdown').val("結束");
	      $('#btnReportpage').val("最末頁");//頁次
		    //$('#btnReportdown').off('click');
	    }else {
	    	$('#btnReportpage').val("下一頁");//頁次
	    	$('#btnReportpageup').show();
	    }
		}else if(data.status == "prt") {
			console.log("print!!");
			if(recv_handler){ 
				recv_handler(data);
			}
		}else {
			alert("通訊錯誤:"+data.message);
		}
	},
	error : function(data){
		unBlock();
		alert("Send Error!!" + data.status + "-" + data.message);
	}
 });
}
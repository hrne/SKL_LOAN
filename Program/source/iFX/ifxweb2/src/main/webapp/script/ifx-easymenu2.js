var MyMenu = MyMenu || {};
MyMenu.tranUrl = 'tran2.jsp';
MyMenu.getUrl = function(txcode) {
	return MyMenu.tranUrl + "?txcode=" + txcode;
};

// BUG:chrome top.frames bug
MyMenu.getTabFn = function(name) {
	//return top.frames['easytab'][name];
	//return window.parent.frames['easytab'][name];
	return window.parent[name];
};

MyMenu.init = function() {
	  var rootMenu = ["type,sbtype,enabled,txcd,txnm,dbucd,hodecd,chopcd,obucd,passcd,secno,tlrfg,drelcd,orelcd", "0,A,0,A    ,會計,0,0,0,0,0,0,0,0,0", "0,C,0,C    ,控制,0,0,0,0,0,0,0,0,0", "0,D,0,D    ,額度,0,0,0,0,0,0,0,0,0", "0,E,0,E    ,OBU債券,0,0,0,0,0,0,0,0,0", "0,F,0,F    ,資金,0,0,0,0,0,0,0,0,0", "0,G,0,G    ,共同,0,0,0,0,0,0,0,0,0", "0,H,0,H    ,SWIFT,0,0,0,0,0,0,0,0,0", "0,I,0,I    ,匯兌,0,0,0,0,0,0,0,0,0", "0,M,0,M    ,進口,0,0,0,0,0,0,0,0,0", "0,N,0,N    ,出口,0,0,0,0,0,0,0,0,0", "0,Q,0,P    ,存款,0,0,0,0,0,0,0,0,0", "0,Y,0,Y    ,測試,0,0,0,0,0,0,0,0,0", "0,Z,0,Z    ,放款,0,0,0,0,0,0,0,0,0"];
      //var url = "http://localhost:8080/ifxweb2/mvc/hnd/menu2/jsonp?menu=";
      var url = _contextPath+"/mvc/hnd/menu2/jsonp?menu=";

      var $rootGetter = $.get(url);

      $rootGetter.done(function(data){
    	  IfxMenu.setup('#tabs', url);
//        IfxMenu.buildRoot(data); // 潘 首頁選單產生 改為樹狀選單
          console.log("data1 "+data );

          // register ifxmenu to tab
          var fn = MyMenu.getTabFn('registerIfxMenu');
          fn(IfxMenu);
      });

      $rootGetter.fail(function(){
    	  alert("無法建立主選單, 請重新整理!");
      });


};


MyMenu.initTxCode = function() {
	var jqTxcode = $('#txcode'),
		fn = MyMenu.getTabFn('registerTran'),
		isInMenu = MyMenu.getTabFn('isInMenu');



	fn('MENU', 'refresh', focusIt);
	fn('MENU', 'resize', MyMenu.adjust );
	setTimeout(function() {
		jqTxcode.focus();

	}, 600);


	function focusIt() {
		setTimeout(function() {
			jqTxcode.focus();
			MyMenu.adjust();
		}, 100);

	}

	jqTxcode.change(function() {
		var s = $(this).val().toUpperCase();
		//柯  新增 start
		if (s.length == 5) {
			if(!(cache[s] && cache[s].length != 0)) {
				 		//return;   //柯  等MENU OK 再開啟
		   }
		//end
			var u = MyMenu.getUrl(s);
			$(this).val('');
			$(this).blur();
			console.log("uu:"+u+" ss:"+s);
			if (isInMenu(s) || s.substring(0,2) == "LC")
			  parent.addTran(s, u);
			else
				alert("無此交易或無此交易權限 " + s);
		}

	}).keyup(function() {  //keypress 改 keyup Chrome 和  IE還是一樣有問題 只改善"按住"的BUG
		var s = $(this).val();
		if (s.length == 5){
			$(this).trigger("change");
			return false;
		}
	}).click(function() {
		// window.location = $('#aa').attr('href');

		window.location = "#home";
		jqTxcode.focus();
		// window.scrollTo(0,50);
	});


};

MyMenu.initSideBar = function() {
	// 鍾 功能面板用原本的樣式及展開的方式,且所有功能在任何交易都可執行
	sidemenu.init('dashboard2', {
		top : 42
	}, function() {
		// end


		sidemenu.enableAll(false);
		sidemenu.enable("toggle_help_list");
		sidemenu.enable("ej");
		sidemenu.enable("dupprint");
		sidemenu.enable('systemInfo');
		sidemenu.enable("logoff");
		// begin hot tran
		// 鍾 且所有功能在任何交易都可執行
		sidemenu.enable("approval");
		// end
		sidemenu.enable("ec");

		// end hot tran
		sidemenu.enable("msw");
	});
};
var cache = {};
MyMenu.autoComplete = function() {
	var txcodeSource = [],
		jqTxcode = $("#txcode");

	console.log("build txcodeSource");
//	for ( var k in _tranMap) {
//		var o = _tranMap[k];
//		txcodeSource.push({
//			label : k + ' ' + o['txnm'],
//			value : k
//		});
//	}
//	txcodeSource.sort(function(a,b){
//		if (a.value < b.value) return -1;
//		if (a.value > b.value) return 1;
//		return 0;
//
//	});
//
	var ip = location.host.split(":");
	var port = ip.length > 1 ? ip[1].substring(0, 3) + "5" : "7005";
	var searchUrl = "http://" + ip[0] + ":" + port + "/iTX/mvc/hnd/txcd/jsonp";
	//searchUrl = _contextPath + "/mvc/hnd/txcd/jsonp";
	/*
	jqTxcode.autocomplete({
		minLength : 2, //不要小於 2 配合平台檢查權限
		  source: function (request, response) {
		  	console.log("data8 ");
		  	request.authNo = parent._sysvar.AUTHNO;
			  request.term =  request.term.toUpperCase();
			  var term = request.term;
			  if(term in cache) {
				  response( cache[ term ] );
		      return;
		    }
		    $.ajax({
				  url: searchUrl,
				  dataType: 'json',
				  async: false,
				  data: request,
				  success: function(data, status, xhr ) {
					  console.log("data2 "+data );
			        	if(data == null){
			        		//是否有要禁止後續輸入?
			        		jqTxcode.autocomplete( "close" );
			        		return false;
			        	}
			          data.data.sort(function(a,b){
			        	  a = a.slice(0,5);
			        	  b = b.slice(0,5);
			      		  if (a < b) return -1;
			      		  if (a > b) return 1;
			      		  return 0;
			          });
			          console.log("data3 "+data );
			          cache[ term ] = data.data;
			          //柯 新增 start
			          if(term.length == 5){
			          	jqTxcode.trigger("change");
			          	}
			          	// end
			          response( data.data );
				  }
				});
//	        $.getJSON( searchUrl, request, function( data, status, xhr ) {
//	        	console.log("data2 "+data );
//	        	if(data == null){
//	        		//是否有要禁止後續輸入?
//	        		jqTxcode.autocomplete( "close" );
//	        		return false;
//	        	}
//	          data.sort(function(a,b){
//	        	  a = a.slice(0,5);
//	        	  b = b.slice(0,5);
//	      		  if (a < b) return -1;
//	      		  if (a > b) return 1;
//	      		  return 0;
//	          });
//	          console.log("data3 "+data );
//	          cache[ term ] = data;
//	          //柯 新增 start
//	          if(term.length == 5){
//	          	jqTxcode.trigger("change");
//	          	}
//	          	// end
//	          response( data );
//	        });
		},
		focus : function(event, ui) {
			// $( "#txcode" ).val( ui.item.value );
			// return false;
			// $(this).autocomplete ("search", "");
		},
		select : function(event, ui) {
			var temp =ui.item.value.slice(0,5);
			jqTxcode.val(temp);
			// $( "#project-id" ).val( ui.item.value );
			return false;
		},close : function(event, ui) {
			var temp = jqTxcode.val();
	     if(temp.length >= 5){
	     	  jqTxcode.val(temp.slice(0,5));
	      }
			}
	});
	*/
};
MyMenu.adjust = function(){
	console.log("menu adjust doc height1:"+$(document.body).height() );

	 var theFrame = $('#menuFrame', parent.document.body);
	 var theCenter = $('#center', parent.document.body);
	 console.log('center height:' + theCenter.height());
	 //theFrame.height($(document.body).height());
	 console.log("menu iframe height1:"+theFrame.height() );
	 theFrame.height(theCenter.height() - 30);
	 console.log("menu iframe height2:"+theFrame.height() );

};
$(function() {
	// startingPoint();
	MyMenu.init();
	$(window).resize(function() {
		MyMenu.adjust();
	});

	MyMenu.initTxCode();
	MyMenu.autoComplete();
});
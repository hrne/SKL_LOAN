(function ($, myTab, basePath) {
		$(document).keydown(function(evt){
	    console.log(evt);
	    if(evt.key == "F4")
	      runNext("LC101", "1", {}, true, '');
    });

    /* 20161114 START
     * 增加個人訊息當中的交易連動功能 
     */
    function strlen2(s) {
		 return s.replace(/[^\x00-\xff]/g,"rr").length; 
	  }
    // 截取固定長度子字串 sSource為字串iLen為長度
    function getInterceptedStr(sSource, Start, iLen) {
    	  var ssorglan = strlen2(sSource);
        if (ssorglan <= Start) {
            return "";
        }

        if (!iLen) {
            iLen = ssorglan - Start;
        }
        var str = "";
        var l = 0;
        var schar;
        for (var i = 0; schar = sSource.charAt(i); i++) {

            l += (schar.match(/[^\x00-\xff]/) != null ? 2 : 1);
            if (l > Start) {
                str += schar;
                if (l >= Start + iLen) {
                    break;
                }
            }
        }
        return str;
    }

    function runHostChain(ntxcd, ntxbuf) {
            var global = searchUp('__ifx_global');
            var dup = global.get('dup');

            var chain = '1';
            var buf = {
                "NTXBUF$": ntxbuf
            };
            // 參考ifx-core2.js 之this.chainByCall(ntxcd, chain, ntxbuf);
            dup.dup(buf);
            var wait = true;
            runNext(ntxcd, chain, {}, wait, ' 連動');
        }
        // 節自ifx-core2.js

    function runNext(ntxcd, chain, extra, wait, prompt) {
        var fn = getTabFnfirst('handleChain');
        setTimeout(function () {
            fn({
                'action': 'run',
                'target': 'new',
                'txcode': ntxcd,
                'extra': extra,
                'chain': chain,
                'prompt': prompt ? prompt : ' 連動 ', // 柯修改
                'callee': '主機'
            });
        }, wait ? 10 : 777);
    }

    function searchUp(name) {
        var w = window;
        while (true) {
            if (w[name] != null)
                return w[name];
            if (w == window.top)
                throw 'no such data:' + name;
            w = w.parent;
        }
    }

    function getTabFnfirst(name) {
            // BUG:chrome top.frames bug			
            //return top.frames['easytab'][name];
            return window.parent.parent[name];
        }
        /* 20161114 END 
         */

    function fnPersonel() {
        var $grid = $('#list1');
        $('#msgBoxStaus').text("請稍候....").show();
        var url = basePath + "/mvc/hnd/msgbox?option=1";
        var g = $.get(url);
        g.done(function (data) {
            $('#msgBoxStaus').hide();
            if (!data || data.length == 0) {
                $grid.hide();
                $('#msgBoxStaus').text("沒有您的訊息....").show();;
            } else {
                $grid.show();
                displayMsgBox(data);  
            }
        });
        g.fail(function (ee) {
            $('#msgBoxStaus').text('Error:' + ee.statusText).show();;
        });
        
        //潘 個人訊息顯示
        function displayMsgBox(mydata) {
        	// 潘 nav 取消
        	document.onclick = function() { 
	            const menuNodeList = top.frames[1].document.querySelectorAll('.dropdown-menu.show');
              Array.from(menuNodeList).forEach((element) => {
                element.classList.remove('show');
              });
	         }
            $grid.jqGrid({
                datatype: "local",
                colNames: ["日期", "時間", "訊息代號", "內容", "其他"],
                colModel: [{
                    name: 'rcvdate',
                    index: 'rcvdate',
                    width: 90,
                    align: 'center',
                    sorttype: "text"
                }, {
                    name: 'rcvtime',
                    index: 'rcvtime',
                    width: 70,
                    align: 'center',
                    sorttype: "text"
                }, {
                    name: 'msgno',
                    index: 'msgno',
                    width: 80,
                    align: 'center'
                }, {
                    name: 'message',
                    index: 'message',
                    width: 500
                }, {
                    name: 'active',
                    index: 'active',
                    width: 100,
                    align: 'center',
                    formatter: chainFormatter
                }],
                multiselect: false,
                multiSort: true,
                sortname: "rcvdate ,rcvtime",
                sortorder: "desc",
                pager: '#pager2',
                pgbuttons: false,
                pgtext: null,
                viewrecords: false,
                height: 380,
                autowidth: true,
                caption: "您的訊息"
            });
            $grid.jqGrid('clearGridData');
            for (var i = 0; i <= mydata.length; i++)
                $grid.jqGrid('addRowData', i + 1, mydata[i]);

            setTimeout(function () {
                $(".viewed", $grid).on('click', function () {
                    var $this = $(this);
                    var id = $this.attr('data-id');
                    //		             var now = "1234 viwed:"+id;
                    //		             $this.replaceWith(now);
                    updateViewed($this, id);
                });
                $(".btnacitved", $grid).on('click', function () {
                    var $this = $(this);
                    var ntxcd = $this.attr('data-ntxcd');
                    var ntxbuf = $this.attr('data-ntxbuf');
                    runHostChain(ntxcd, ntxbuf);
                });

            }, 1);

            function updateViewed($btn, id) {
                var u = basePath + "/mvc/hnd/msgbox/update?id=" + id;
                $p = $.post(u);
                $p.done(function (data) {
                    $btn.replaceWith(data);
                });
                $p.fail(function (ee) {
                    alert('Error:' + ee.statusText);
                });
            }
        }

        //其他動作
        //open會位移,R6沒問題
        function chainFormatter(cellvalue, options, rowObject) {
        	var rowmessage = rowObject.message;
        	rowmessage = rowmessage.replace(/\4/g, '').replace(/\7/g, '  ');
        	//console.log("cellvalue len :" + rowmessage.length);
        	var rellenmessage = strlen2(rowmessage);
        	//console.log("cellvalue len :" + rellenmessage);
            if (rellenmessage <= 200) {
                return "";
            }
            var result = rowmessage;
            var htext, gochain, ntxcd, ntxbuf;
            htext = getInterceptedStr(result, 0, 200).trim();
            gochain = getInterceptedStr(result, 200, 1);

            //只有實作連動交易,其餘不常用功能不處理
            if (gochain == "Y") { //CHAIN 交易 
                ntxcd = getInterceptedStr(result, 201, 5) ? getInterceptedStr(result,
                    201, 5) : ""; //5位交易代號
                ntxbuf = getInterceptedStr(result, 206) ? getInterceptedStr(result,
                    206) : ""; //參數
                return "<button  class='btnacitved firstfn_btn_class' data-ntxcd='" + ntxcd + "' data-ntxbuf='" + ntxbuf + "' >連動" + ntxcd + "</button>";
            }
            
            return "";
        }

        function viewFormatter(cellvalue, options, rowObject) {
            if (!cellvalue) {
            	return "<button class='viewed firstfn_btn_class' data-id='" + rowObject.id + "'>了解</button>";
            }
            return cellvalue;
        }



    }

    function fnTickers() {
        $('#tickerStatus').css("color", "blue").text("請稍候....").show();;
        var url = basePath + "/mvc/hnd/tickers";
        //柯:加入,{ "_": $.now() }
        var g = $.get(url, {
            "_": $.now()
        });
        g.done(function (data) {
            var n = new Date();
            n = "資料最後更新時間:" + n.toLocaleString();
            $('#tickerStatus').css("color", "black").text(n).slideDown();
            displayData(data);
        });
        g.fail(function (ee) {
            $('#tickerStatus').css("color", "red").text('Error:' + ee.toString()).slideDown();
        });
        var $grid = $('#list2');

        function displayData(mydata) {
            $grid.jqGrid({
                datatype: "local",
                colNames: ['訊息內容'],
                colModel: [{
                    name: 'msg',
                    width: 750,
                    sortable: false
                }, ],
                multiselect: false,
                pager: '#pager2',
                pgbuttons: false,
                pgtext: null,
                viewrecords: false,
                height: 380,
                autowidth: true,
                caption: "重要訊息"
            });
            $grid.jqGrid('clearGridData');
            for (var i = 0; i <= mydata.length; i++)
                $grid.jqGrid('addRowData', i + 1, {
                    msg: mydata[i]
                });
        }
    }

    $(function () {
        myTab.init("tabButton", "tabPanel", [fnPersonel, fnTickers]);

        function getTopFn(name) {
            // BUG:chrome top.frames bug
            //return top.frames['head'][name];
            return top.frames[0][name];
        }


        function getTabFn(name) {
            // BUG:chrome top.frames bug			
            //return top.frames['easytab'][name];
            return window.parent.parent[name];
        }
        var fnMenu = getTabFn('selectMenu'); //eztab
        var oIfxMenu = getTabFn('getIfxMenu')(); // jquery ui tab (tran code menu)
        var fn = getTopFn('registerMsgCallback');

        fn(function () {
            //fnPersonel();
            $('#btnMsg').trigger("click");
            setTimeout(fnMenu, 100);
            setTimeout(function () {
                oIfxMenu.selectTab(0);
            }, 150);
        });

    });

})(jQuery, ifxMyTab, _contextPath);
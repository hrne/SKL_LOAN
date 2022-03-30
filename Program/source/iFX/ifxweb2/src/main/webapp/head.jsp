<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.apache.commons.text.StringEscapeUtils" %>
<%@page import="com.st1.servlet.GlobalValues" %>
<%@page import="java.io.*,java.net.*" %>
<%@page import="com.st1.ifx.filter.FilterUtils" %>
<%@page import="com.st1.ifx.filter.SafeClose" %>
<%@page import="java.util.regex.Matcher" %>
<%@page import="java.util.regex.Pattern" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- <meta http-equiv="cache-control" content="no-cache"> -->
	<title>Ifx</title>
	<link href="css/site.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="script/alertify/css/alertify.css">
	<link rel="stylesheet" type="text/css" href="script/alertify/css/themes/default.css">
	<script src="script/jquery.js"></script>
	<script src="script/jqueryui/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="script/jquery.pulse.min.js"></script>
	<link rel=stylesheet type=text/css href="script/jqueryui/css/redmond/jquery-ui-1.10.3.custom.css" />

	<!-- 因為tran2.jsp以$.load載入此頁, 若這邊不含入blockui, 則tran2.jsp之blockUI失效 -->
	<script src="script/jquery.blockUI.js"></script>
	<script src="script/alertify/src/alertify.js"></script>
	<script>
	<%
		String sysvar = (String) session.getAttribute(GlobalValues.SESSION_SYSVAR);
		out.println("\n var _sysvar=" + sysvar);
		String version = GlobalValues.applicationVersion;
		out.println("\n var _appver='" + version + "';\n");
		/**
		 * Passing Java Script Value to Jsp can be done in this way
		 * 
		 * 1. Assign the Java Script value to a hidden text box in the form
		 * 2. On Submit of that form, get that value and assign to Jsp variable
		 */
		String jspVar = null;

		if (StringEscapeUtils.escapeXml10(request.getParameter("hiddenTextBox")) != null) {
			jspVar = StringEscapeUtils.escapeXml10(request.getParameter("hiddenTextBox"));
			out.println("Jsp Var : " + jspVar);
			String path = GlobalValues.DOWNFILE_ROOT + "\\"; //檔案主要放置目錄
			String filename = jspVar; //檔案名
			filename = new String(filename.getBytes("ISO-8859-1"), "Big5");

			File file = new File(FilterUtils.filter(path + filename));
			if (file.exists()) { //檢驗檔案是否存在
				OutputStream output = null;
				InputStream in = null;
				try {
						String regex = "[`~!@#$%^&*()\\+\\=\\{}|:\"?><【】\\/r\\/n]";
						Pattern pa = Pattern.compile(regex);
						Matcher ma = pa.matcher(filename);
					if (ma.find()) {
						filename = ma.replaceAll("");
					}
					response.setContentType("text/html");
					response.setHeader("Content-Disposition",
						"attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"");
					output = response.getOutputStream();
						in = new FileInputStream(file);
					byte[] b = new byte[2048];
						int len;

					while ((len = in.read(b)) > 0) {
						output.write(b, 0, len);
					}
						in.close();
					output.flush();
					out.clear();
					out = pageContext.pushBody();
				} catch (Exception ex) {
					out.println("Exception : head.jsp Error");
					out.println("<br/>");
				} finally {
					SafeClose.close(output);
					SafeClose.close(in);
				}
			} else {
				out.println(filename + " : 此檔案不存在");
				out.println("<br/>");
			}
		}
	%>
		$(function () {
			$('#barContainer2').load("topbar2.jsp  #topbar", function (r) {
				setTimeout(startClock, 10);
				var date = new Date();
				$("#dateNow").text((date.getFullYear() - 1911) + "/" + (date.getMonth() < 9 ? "0" : "") + (date.getMonth() + 1) + "/" + (date.getDate() < 10 ? "0" : "") + date.getDate());
				var d = _sysvar['DATE'];
				d = d.slice(1, 4) + "/" + d.slice(4, 6) + "/" + d.slice(6, 8);
				$("#date").text(d);
				d = _sysvar['NDAYMOD'];
				if (d != 0)
					$("#nDayMode").text("[次日]");
				d = _sysvar['DBTO'];
				switch (d) {
					case "0":
						$("#dbTo").text("");
						break;
					case "1":
						$("#dbTo").text("	[已切換至日報查詢作業]");
						break;
					case "2":
						$("#dbTo").text("	[已切換至月報查詢作業]");
						break;
					case "3":
						$("#dbTo").text("	[已切換至歷史資料查詢作業]");
						break;
				}
			});
		});
		$(function () {
			$('#barContainer').load("topbar.jsp  #topbar", function (r) {
				//$('#appver').text(_appver);
				$("#brn").text(_sysvar['BRN'] + " " + $.trim(_sysvar['BNAM'] || ""));
				//$("#add").text( "[" + _sysvar['ADD'] + "]" );
				//$("#tlrno").text(_sysvar['TLRNO'] + " " + $.trim(_sysvar['EMPNM'] + " " + _sysvar['AgentItem'] || ""));
				$("#tlrno").text(_sysvar['TLRNO'] + " " + $.trim(_sysvar['EMPNM'] + " (" + _sysvar['AUTHITEM'] + ") " + _sysvar['AgentItem'].replace("理", "") || ""));
				$("#dep").text(_sysvar['BRN'] + " " + $.trim(_sysvar['BNAM'] + "( " + _sysvar['CLDEPTNA'] + " )" || ""));

				$("#lslogin").text(_sysvar['LSLOGIN']);
				if (_sysvar['LEVEL'] < 3) {
					$('#btnOpenOvrList').show();
					$('#btnOpenOvrList').on('click', openOvrList);
				} else {
					$('#btnOpenOvrList').hide();
				}
				$("#lslogin").text(_sysvar['LSLOGIN']);
			});
			//initValues();
			//initHeader();
			//handle_txcode();
		});
		function startClock() {
			var $time = $('#time');
			setInterval(now, 1000);
			function now() {
				var dt = new Date(), hh = dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours(), mm = dt.getMinutes() < 10 ? "0" + dt.getMinutes() : dt.getMinutes(), ss = dt.getSeconds() < 10 ? "0"
					+ dt.getSeconds() : dt.getSeconds(), s = hh + ":" + mm + ":" + ss;
				$time.text(s);
			}
		}

		function setStatus(o) {
			var ids = [], hides = [];
			for (var k in o) {
				$('#' + k).text(o[k]);
				if (o[k]) {
					ids.push('#' + k);
				} else {
					hides.push('#' + k);
				}
			}
			if (hides) {
				$(hides.join()).hide('fast');
			}
			if (ids) {
				$(ids.join()).slideDown('fast');
			}
			updateOthers(o.other);
		}
		function updateOthers(other) {
			var m = {
				"OD": function (o) {
					var $od = $("#od");
					var $tp = $("#topbar");
					if (o != null) {
						//o = o.toUpperCase();
						// 1:DBU
						// 2:OBU
						switch (o) {
							case "0":
								$("#dbTo").text("");
								break;
							case "1":
								$("#dbTo").text("	[已切換至日報查詢作業]");
								return;
								break;
							case "2":
								$("#dbTo").text("	[已切換至月報查詢作業]");
								return;
								break;
							case "3":
								$("#dbTo").text("	[已切換至歷史資料查詢作業]");
								return;
								break;
						}
					}
					$tp.removeClass();
					$od.text('');
					$od.hide();
				}
			}
			for (var k in m) {
				m[k](other[k]);
			}

		}
		function resetTranStatus() {
			$('.tranbase,#ec,#normal,#step,#txcode,#od').hide('fast');
			$("#od").removeClass(); //柯 新增
			$("#topbar").removeClass(); //柯 新增
			//		$('#txcode').slideUp();
		}
		function getTabfn(n) {
			return top.frames['easytab'].contentWindow[n];
		}

		function setOvrCount(n, bEffect) {
			var $b = $('#btnOpenOvrList');
			var text = (n > 0) ? '待授權(' + n + ')' : '授權';
			$b.text(text);
			if (bEffect) {
				playSound();//主管授權撥放提示音
				$b.effect("pulsate");
			}
			if (n > 0) {
				$b.addClass("setOvrCount-color");
			} else {
				stopSound(); //主管授權停止授權音樂
				$b.removeClass("setOvrCount-color");
			}
		}
		function openOvrList() {
			(getTabfn('displayOvrList'))(true);
			stopSound(); //主管授權停止授權音樂
		}

		function changeServerStatus(bOnline) {
			return;
			var $target = $('#serverStatus').text(bOnline ? 'UP' : 'DOWN').attr('title', bOnline ? '與伺服器連線中' : '與伺服器失去聯繫');
			$target.pulse('destroy');
			if (bOnline) {
				$target.pulse({
					color: 'green'
				}, {
					pulses: 10
				});
				setTimeout(function () {
					$target.pulse('destroy');
				}, 5000);

			} else {
				$target.pulse({
					color: 'red'
				}, {
					returnDelay: 1000,
					interval: 1000,
					pulses: -1
				});
			}

		}
		var _fnMsgCallBack = null;
		function registerMsgCallback(fn) {
			_fnMsgCallBack = fn;
		}
		//瀏覽器標題閃爍start 
		//使用message對象封裝消息  
		var message = {
			time: 0,
			title: parent.document.title,
			timer: null,
			// 顯示新消息提示  
			show: function () {
				var title = message.title.replace("【　　　】", "").replace("【新消息】", "");
				// 定時器，設置消息切換頻率閃爍效果就此產生  
				message.timer = setTimeout(function () {
					message.time++;
					message.show();
					if (message.time % 2 == 0) {
						parent.document.title = "【新消息】" + title
					}

					else {
						parent.document.title = "【　　　】" + title
					}
					;
				}, 600);
				return [message.timer, message.title];
			},
			// 取消新消息提示  
			clear: function () {
				clearTimeout(message.timer);
				parent.document.title = message.title;
			}
		};
		//end
		//music陣列用來存放各種音效的路徑,預設第一項
		var music = new Array("mp3/soap3.wav", "mp3/soundS.wav", "mp3/soundT.wav", "mp3/0013-2.wav", "mp3/runescape.wav");
		var _musicnum = 0;
		//initMswMusic設定音樂(故更改設定需重新登入或f5頁面)
		function setMusicnum(num) {
			_musicnum = num;
		}
		function playSound() {
			//指定bgSound其src = 某音效位置
			//user提議無限loop播放直到結束
			var docSound = document.getElementById('sound');
			docSound.loop = -1;
			docSound.src = music[_musicnum];
		}
		function stopSound() {
			//關閉音樂
			var docSound = document.getElementById('sound');
			docSound.loop = 0;
			docSound.src = null;

		}
		function addNewMsgBtn(result, sysuser) {
			message.clear();//先清除
			message.show(); //message 播放
			//XS976
			//tran2.jsp?txcode=XS976&chain=1
			//主機 連動
			var htmltext = "您有新訊息";
			console.log("addNewMsgBtn");
			console.log("result:" + result);
			console.log("sysuser:" + sysuser);
			console.dir(result);
			var htext, gochain, ntxcd, ntxbuf; //是否連動?:連動交易代號:連動參數(與正常連動相同)
			var filename;
			if (sysuser) { //主機通知還是 MSW通知
				console.dir(result);
				//result.shift();
				result = result.join("-"); //柯 ifx-easytab.js 是用split  待換成比較ok的方式 如indexof等
				result = result.replace(/\4/g, '').replace(/\7/g, '  ');
				var rellenresult = result.replace(/[^\x00-\xff]/g, "xx").length;
				if (rellenresult <= 200) {
					//htmltext += ":...";
					htmltext += ":" + result.trim();  //有需要顯示訊息內容?

				} else { //顯示內容(200)+狀態(1:值Y)+交易名稱(5)+連動參數
					htext = getInterceptedStr(result, 0, 200).trim();
					gochain = getInterceptedStr(result, 200, 1);

					if (gochain == "Y" || gochain == "F") { //CHAIN 交易
						ntxcd = getInterceptedStr(result, 201, 5) ? getInterceptedStr(result, 201, 5) : ""; //5位交易代號
						ntxbuf = getInterceptedStr(result, 206) ? getInterceptedStr(result, 206) : ""; //參數
						htmltext = "訊息通知";
						if($.trim(ntxcd) == "")
							htmltext += ":" + htext;
						else
							htmltext += ":" + htext + " => " + ntxcd + " (點擊後跳轉)";
					} else if (gochain == "D" || gochain == "O") { //開啟檔案報表
						filename = getInterceptedStr(result, 201) ? getInterceptedStr(result, 201) : "";
						if (filename) {
							filename = filename.trim();
							if (gochain == "D") {
								htmltext = "檔案下載";
							} else if (gochain == "O") {
								htmltext = "開啟檔案報表";
							}
							htmltext += ":" + filename;
							console.log("GET File name:" + filename);
						} else {
							htmltext = "無檔案名稱";
						}
					} else {
						htmltext = "訊息通知 : " + htext;
					}
				}
			} else {
				console.log("result:" + result);
				htmltext += "從:" + getInterceptedStr(result, 0, 6);
			}

			alertify.set('notifier', 'position', 'top-left');
			alertify.warning(htmltext, 0, function () {
				message.clear();
				if (filename) {
					if (gochain == "D") {
						//document.myForm.submit();
						//這個是下載報表
						document.forms['myForm'].submit(); //柯: 依照路徑傳送FORM 取得檔案
						//return;
					}
					if (gochain == "O") {
						//document.myForm.submit();
						//分段報表
						openDupWindow(filename); //可能檔名會有日期
						//return;
					}
				}
				if (gochain == "Y" || gochain == "F") {
					if ($.trim(ntxcd) != "") 
						runHostChain(ntxcd, ntxbuf);
					
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
						runNext(ntxcd, chain, {}, wait, ' 連動', gochain);
					}
					//此段應該不能整合在 ifxcore2
					function openDupWindow(filename) {
						var width = 974;
						var height = 718;
						var url = "batch_report.jsp";
						url += "?filename=" + filename + "&startline=0&count=500&dt=" + _sysvar['FDATE']; //預設營業日&從頭開始&讀500
						window.showModalDialog(url, window, 'dialogWidth=' + width + 'px;dialogHeight=' + height + 'px;resizable=yes;help=no;center=yes;status=no;scroll=yes;edge=sunken'); //raised  //sunken
					}

					// 節自ifx-core2.js
					function runNext(ntxcd, chain, extra, wait, prompt, goChain) {
						var fn = getTabfn('handleChain');
						setTimeout(function () {
							fn({
								'action': 'run',
								'target': 'new',
								'txcode': ntxcd,
								'extra': extra,
								'chain': chain,
								'prompt': prompt ? prompt : ' 連動 ', // 柯修改
								'callee': '主機',
								'goChain': goChain
							});
						}, wait ? 10 : 777);
					}
				} else {
					if (!sysuser) {
						//do something
					}
					if (_fnMsgCallBack)
						_fnMsgCallBack();
				}
			});

			if (gochain == "D" && filename) {
				document.myForm.hiddenTextBox.value = filename; //給資料
			}
			// 截取固定長度子字串 sSource為字串iLen為長度
			function getInterceptedStr(sSource, Start, iLen) {
				var ssreallen = sSource.replace(/[^\x00-\xff]/g, "xx").length;
				if (ssreallen <= Start) {
					return "";
				}

				if (!iLen) {
					iLen = ssreallen - Start;
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
		}
	</script>
</head>

<body class="headclass">
	<bgSound id="sound" src="" loop=-1 />
	<div id="container">
		<div id="barContainer"></div>
		<div id="barContainer2"></div>
	</div>
	<!--柯 myForm只是給通知報表取檔案-->
	<form id="myForm" name="myForm" method="post">
		<input type="hidden" name="hiddenTextBox"><br>
	</form>

</body>

</html>
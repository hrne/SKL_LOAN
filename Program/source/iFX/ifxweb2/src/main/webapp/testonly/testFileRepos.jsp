<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel=stylesheet type=text/css
	href="../script/jqueryui/css/redmond/jquery-ui-1.8.23.custom.css" />
<script src="../script/fixIE.js"></script>
<script src="../script/jquery.js"></script>
<script src="../script/jquery.blockUI.js"></script>
<script src="../script/underscore.js"></script>
<script src="../script/ifx-file.js"></script>
<script src="../script/ifx-utl.js"></script>
<script src="../script/jqueryui/js/jquery-ui-1.8.19.custom.min.js"></script>
<style>
.leftButton {
	margin-right: 10px !important;
}
</style>
<script>
	$(function() {
		ifxFile.setUrl('../beanProxy.jsp');
		$('#btnSave').click(saveFile);
		$('#btnSave3').click(saveAll);
		$('#btnRead').click(readFile);
	});
	function saveFile() {
		var filename = $('#filename').val();
		var content = $('#content').val();
		ifxFile.put(filename, content, makeBlockers(), function(data) {
			if(data.status) {
				alert('ok:' + data.msg);
			}else{
				alert('error:' + data.msg);
			}
		},false);
	}
	function saveAll() {
		var filename = 'scr/505000201/qqq_';
		var content = $('#content').val();
		var r = [];
		for(var i=1; i <=100; i++) {
			r.push({ 
				f: filename + i + '.txt',
				c: content + '___' + i
			});
		}
		var bOK = true;
		$('#btnSave3').text('sent');
		while(bOK && r.length > 0){
			bOK = false;
			var p = r.shift();
			console.log('sending ' + p.f);
			ifxFile.put(p.f, p.c,null, function(data) {
				if(data.status) {
					bOK = true;
					console.log(p.f + ' sent');
					$('#btnSave3').text(p.f + ' sent');
				}else{
					alert('error:' + data.msg);
				}
			},false);
		}
		
		console.log(r.length);
		if(r.length>0) alert(r.length + ' unSent');
	}
	function makeBlockers() {
		return {
			'block' : function() {
				blockIt('請稍候', 'saving/reading');
			},
			'unblock' : function() {
				unBlock();
			}
		};
	}

	function readFile() {
		var filename = $('#filename2').val();

		var content = $('#content2');
		ifxFile.get(filename, makeBlockers(), function(x) {
			//yesnodialog('ok', 'yes', 'cancel', null)
			mydialog('how are you?','do nothing', [ {
				text : 'ok',
				click : function() {
					alert('ok');
					$(this).dialog("close");
				}
			}, {
				'text' : 'cancel',
				'click' : function() {
					alert('cancle');
					$(this).dialog("close");
				}
			}, {
				'text' : 'abort',
				'click' : function() {
					alert('abort');
					$(this).dialog("close");
				}
			} ]);
			content.val(x.msg);
		});
	}
	function mydialog(title, content, btns) {
		
		
		var dlg = $("<div>" + content + "</div>").dialog(
				{
					autoOpen : true,
					title : 'this is a book',
					modal : true,
					buttons : btns,
					show: "explode",
					position : [ 50, 50 ],
					overlay : true,
					resize : false,
					closeOnEscape : false,
					open : function(event, ui) {
						// default first button
						$(this).parents('.ui-dialog-buttonpane button:eq(0)')
								.focus();

						// Hide close button         
						$(this).parent().children().children(
								".ui-dialog-titlebar-close").hide();

					},
					close : function() {
						alert('closeclose');
					}
				});
		
		dlg.on('keydown', function(evt) {
					alert('s');
			        if (evt.keyCode === $.ui.keyCode.ESCAPE) {
			        	$(this).dialog("close");
			        }                
			        evt.stopPropagation();
			    });
		
		
		
		//dialog({ closeOnEscape: false });
	}
	function yesnodialog(button1, button2, button3, fns) {
		var btns = {};
		btns[button1] = function() {
			alert('ok');
			$(this).dialog("close");
		};
		btns[button2] = function() {
			// Do nothing
			$(this).dialog("close");
		};
		btns[button3] = function() {
			// Do nothing
			$(this).dialog("close");
		};
		$("<div id='mydialog1'>this is a book</div>").dialog(
				{
					autoOpen : true,
					title : 'hello',
					modal : true,
					buttons : btns,
					position : [ 100, 20 ],
					overlay : true,
					resize : false,
					closeOnEscape : false,
					open : function(event, ui) {
						// default first button
						$(this).parents('.ui-dialog-buttonpane button:eq(0)')
								.focus();

						// Hide close button         
						$(this).parent().children().children(
								".ui-dialog-titlebar-close").hide();

					},
					close : function() {
						alert('closeclose');
					}
				});

	}
</script>
</head>
<body>
	<input id="filename" value="scr/505000201/xyz.txt" />
	<br />
	<textarea id="content" rows="5" cols="60">ttt</textarea>
	<br />
	<button id="btnSave">Save</button>
	
	<button id="btnSave3">Save 3 times</button>
	<br/>
	<hr />
	<input id="filename2" value="scr/505000201/xyz.txt" />
	<button id="btnRead">Read</button>
	<br />
	<textarea id="content2" rows="5" cols="60"></textarea>
	<br />


</body>
</html>
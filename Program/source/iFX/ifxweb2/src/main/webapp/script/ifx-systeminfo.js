$(function() {
	$("#imgDeviceServer").src = printServiceUrl;
	var prt = new IfxPrt($,printServiceUrl);
	sayHello();
	$('#print').click(function() {
		var buf = $('#buf').val();
		var n = parseInt($('#num').val(), 10);
		var docs = [];
		for ( var i = 0; i < n; i++) {

			docs.push({
				prompt : 'print ' + i + ' doc',
				content : ('doc ' + i + '\n\n' + buf).split('\n')
			});
		}

		function fnPrint() {
			var doc = docs.shift();
			if (doc != null) {
				var promise = prt.printDoc(doc.content);
				promise.done(fnPrint);
				promise.fail(function(x) {
					alert('print error:' + x);
				})
			}
		}
		fnPrint();
	});
	$('#hello').click(function() {
		sayHello();
	});

	function sayHello() {
		prt.hello(function(x) {
			alert(x);
		});
	}
}());

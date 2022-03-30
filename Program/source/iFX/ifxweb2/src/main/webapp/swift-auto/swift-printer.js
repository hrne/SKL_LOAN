 function _getPrinters(callbackname){
	 try {
		 if(!callbackname) callbackname = '_getPrintersCallback';
		 window.external.GetPrinters(callbackname);
	 }catch(nouse) {
		 
	 }
}
   
var _printers =  {
	hasNoPrinter: true,
	isNull:function() { return _printers.hasNoPrinter; },
	"defaultPrinter":"n/a",
	"list":[],
	print:function(docname, data, printerName) {
		if(_printers.isNull()) return;
		if(!printerName) printerName= _printers.defaultPrinter;
		window.external.PrintLines("_printCallback",printerName, docname, data);
	}
};

function _printCallback(s){
//	alert(s);
}

function _buildPrinters(arr){
	_printers.defaultPrinter = arr.shift();
	_printers.hasNoPrinter = false;
	_printers.list = arr;
	return _printers;
}   
function _getPrintersCallback(){
	var args = Array.prototype.slice.call(arguments);
	return _buildPrinters(args);

}
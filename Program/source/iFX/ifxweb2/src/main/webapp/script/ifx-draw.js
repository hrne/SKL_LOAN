var IfxWidget = (function(){
	var margin_top = 0;
	var margin_left = 22;
	var fontFamily = "細明體";
	var fontSize = "12pt";
	var h = 20;
	var w = 7.9;
	var unit="px";
	
	function Drawer()
	{
	}
	
	function getTop(r) {
		return (r-1) * h + margin_top; 
	}
	function getLeft(c) {
		return (c) * w + margin_left; 
	}

	Drawer.prototype.drawField = function(fld,pageStart, tabindex) {
		var s= "<INPUT  class='{field-class}' style='padding-top:1px; position:absolute; WIDTH:{width}px;HEIGHT:{height}px; TOP: {top}px;LEFT: {left}px' id='{id}' name='{id}' maxlength='{len}' tabindex='{tab}' {readonly}>"; 
		var len = fld.getDisplayLength();
		s = s.replace(/{len}/, len);
		var width = len * w;
		s = s.replace(/{width}/, width);
		s = s.replace(/{height}/, 12);
		s = s.replace(/{top}/, getTop(fld.r + pageStart));
		s = s.replace(/{left}/, getLeft(fld.c));
		s = s.replace(/{id}/g, "fld_" + fld.name.slice(1));
		s = s.replace(/{tab}/, tabindex);
		
//		if(fld.a=="O") { 
//			s = s.replace(/{readonly}/, "READONLY");
//			s = s.replace(/{field-class}/, "field_output");
//		}
//		else {
//			s = s.replace(/{readonly}/, "");
//			s = s.replace(/{field-class}/, "field_input");
//		}
//		console.log(s);
		return s;
		
	}

	//endof class
	return Drawer;
}());
var ifxUI = (function(){
	
	
	var CMD_EXPAND = "#<->#",
		CMD_GRID = "#grid#",
		cmds = [CMD_EXPAND, CMD_GRID],
		TABLE_CLASS = "pageTable",
		GRID_CLASS = "gridRow",
		CAPTION_CLASS = "caption black",
		LABEL_CLASS = "input-label",
		tabIndex=0,
		labelIndex=0,
		layout,
		MODE_LANDSCAPE='L',
		MODE_PORTRAIT = 'P',
		orientation = MODE_PORTRAIT,
		RE_LABEL_INDEX = /\s\^/,
		workArea="#tmp",
		bScreenMode = true;
		
	
	function renderScreen(def, fn) {
		var blocks = [];
			
		
		tabIndex = 0;
		labelIndex = 0;
		layout = getDefaultLayout();
		
		$.each(def.display, function(i,x) {
			blocks.push(preparePage(x, fn));
		});
		
		var buf = [], r=[], caption;
		
		$.each(blocks, function(i, block){
			
			layout = overrideLayout(layout, block.name, block.def);
			buf.push("<table id='p_"+block.name + "' class='" + TABLE_CLASS + "' >");
			buf.push(makeCaption(block,layout.cols*2));

			if(layout.cols==1) {
				r = renderOneColTable(block, layout);
			}else {
				r = renderTwoColsTable_P(block	, layout);
				
			}
			buf = buf.concat(r);
			
			buf.push("</table>");
		});
		return buf.join(""); 
	}
	
	function renderTwoColsTable_P(block) {
		var buf = [],
			widthDef,
			colspan,
			left=[], 
			right=[],
			expandCells =[],
			splitSize,
			max,
			b;
		
		
		$.each(block.boxes, function(i, box){
			if(isExpanded(box)){
				expandCells.push(i);
			}
			
			//if(box.label && box.label.match(RE_LABEL_INDEX)) {
			//	box.labelIndex = ++labelIndex;
			//}
				
		});
		
		var k=0;

		$.each(expandCells, function(i,n){
			splitSize = Math.ceil((n -k)/2);
			max = k + splitSize;
			
			for(;k<max; k++) {
				left.push(block.boxes[k]);
				if(k+splitSize < n)
					right.push(block.boxes[k+splitSize]);
				else {
					left[left.length-1].cmd=CMD_EXPAND;
					right.push(null);
				}
			}
			left.push(block.boxes[n]);
			right.push(CMD_EXPAND); // expanded
			k = n+1;
		});
		
		splitSize = Math.ceil((block.boxes.length -k)/2);
		max = k + splitSize;

		for(; k < max; k++) {
			left.push(block.boxes[k]);
			right.push(block.boxes[k + splitSize]);
			
		}
		
		
		
		for(var i=0; i < left.length; i++){
			buf.push("<tr>");
			b = left[i];
			widthDef = getLabelWidth(2, 0); // 0 --> left side
			renderOneSide(b, widthDef);
			
			b = right[i];
			widthDef = getLabelWidth(2, 1); // 1 --> right side
			renderOneSide(b, widthDef);
			
			buf.push("</tr>");
		}
		
		return buf;
		
		
		function renderOneSide(box, widthDef) {
			if(!box) {
				buf.push("<td style='border-left-width:0px;border-right-width:0px'></td><td style='border-left-width:0px'></td>");
				return;
			}
			if(box==CMD_EXPAND) return;
			
			colspan = 1;
			if(isExpanded(box)){
				colspan+=2;
			}
			if(box.label) {
				buf.push(makeLabel(box, widthDef.label));
			}else {
				colspan++;
			}
			
			buf.push("<td valign='top'  colspan='" + colspan + "' width='" + widthDef.field + "'>");
			if(box.cmd.action == CMD_GRID ) {
				buf.push(renderGrid(box.cols, box.cmd.opt));
			} else{
				$.each(box.cols, function(i,c){
					buf.push(c);
				});
			}
			buf.push("</td>");
		}
	}
	
	function renderTwoColsTable_L(block) {
		
		var buf = [],
			widthDef,
			cells=0,
			expand=false,
			colspan;
		
		$.each(block.boxes, function(i, box){
			widthDef = getLabelWidth(2, cells);
			
			if(cells%2==0)
				buf.push("<tr>");
			
			colspan = 1;
			if(isExpanded(box)){
				expand = true;
				colspan +=2;
				if(cells%2!=0) {
					// close last tr
					buf.push("<td colspan='2'></td></tr>");
					buf.push("<tr>");
					cells++;
				}
				cells++; // new TR, +1, �]���j��̫�|�A�[1

			}
			
			if(box.label) {
				//if(box.label.match(RE_LABEL_INDEX))
				//	box.labelIndex = ++labelIndex;
				
				buf.push(makeLabel(box,widthDef.label));
			}else {
				colspan++;
			}
			
			buf.push("<td valign='top'  colspan='" + colspan + "' width='" + widthDef.field + "'>");
			
			
			if(box.cmd.action == CMD_GRID ) {
				buf.push(renderGrid(box.cols, box.cmd.opt));
			} else{
				$.each(box.cols, function(i,c){
					buf.push(c);
				});
			}
			buf.push("</td>");
			
			
			if(cells%2!=0)
				buf.push("</tr>");
			cells++;
		});
		
		


		return buf;
	}
	function renderOneColTable(block) {
	
		var buf = [];
		
		var widthDef = getLabelWidth(1, 0);
		

			
		$.each(block.boxes, function(i, box){
			buf.push("<tr>");
			if(box.label) {
				//if(box.label.match(RE_LABEL_INDEX))
				//	box.labelIndex = ++labelIndex;
				
				buf.push(makeLabel(box,widthDef.label));
			}
			buf.push("<td valign='top'  colspan='" + (box.label?1:2) + "' width='" + widthDef.field + "'>");
			
			if(box.cmd.action == CMD_GRID ) {
				buf.push(renderGrid(box.cols, box.cmd.opt));
			} else{
				$.each(box.cols, function(i,c){
					buf.push(c);
				});
			}
			buf.push("</td>");
			
			buf.push("</tr>");
		});
		
		


		return buf;
	}
	function renderGrid(cols, opt) {
		var buf=[], 
			tblStarted=false;;


		
		function getColWidth(colIndex) {
			var t;
			if(opt && opt["s_cols"]) {
				t = opt["s_cols"][colIndex];
			}
			return parseInt(t,10);
		}	
		
		$.each(cols,function(i, x){
			if($.isArray(x)){
				if(!tblStarted) {
					tblStarted = true;
					buf.push("<table class='" + GRID_CLASS + "'>");
					buf.push("<tr class='gray'>");
				}else {
					buf.push("<tr>")

				}
				$.each(x, function(j,y){
					var width = getColWidth(j % (x.length));
					if(isNaN(width)) {
						buf.push("<td>");
					}else {
						buf.push("<td width='" + width + "'>");
					}
					if($.isArray(y)) {
						$.each(y,function(k,z){
							buf.push(z);
						});
					}else {
						buf.push(y);
					}
					buf.push("</td>");
				});
				
				buf.push("</tr>");
				
			}else {
				if(tblStarted) {
					buf.push("</table>");
					tblStarted = false;
				}
				buf.push(x);
			}
		});
		if(tblStarted) {
			buf.push("</table>");
			tblStarted = false;
		}

		return buf.join("");
	}
	function makeLabel(box, width) {
		var s= box.label;
//		if(s.match(RE_LABEL_INDEX)) {
//			var t=box.labelIndex.toString();
//			if(t.length==1) t = "&nbsp;" + t;
//			s=s.replace(RE_LABEL_INDEX, t);
//		}
		return "<td class='" + LABEL_CLASS + "' width='" + width + "' valign='top' align='left'>" + s + "</td>";
		
		
	}
	function makeCaption(block, colspan) {
		
		if(block.caption) {
			return "<tr class='" + CAPTION_CLASS + "'><td colspan='" + colspan + "'>" + block.caption +"</td></tr>";
		}else {
			return "";
		}
	}
	function replaceLabelIndex(s){
		if(s.match(RE_LABEL_INDEX)) {
			labelIndex++;
			var t=labelIndex.toString();
			if(t.length==1) t = (bScreenMode? '&nbsp;': ' ') + t;
			s=s.replace(RE_LABEL_INDEX, t);
		}
		return s;
	}
	function preparePage(p, fn){
		var boxes = [],
			//lines = p.def.list.slice(0),
			lines = $.extend(true, [], p.def.list), 
			caption = lines.shift(),
			cmd,label;
		
		$.each(lines, function(r,line){
			cmd = "";
			label = line.shift();
			
			var gridLoopTimes = 0;
			if(isCmd(label)) {
				cmd = parseCmd(label);
				label = line.shift();
				if(cmd.action==CMD_GRID) {
					gridLoopTimes = cmd.opt.loop;
				}
			}
			while(label.match(RE_LABEL_INDEX)) {
				label = replaceLabelIndex(label);
			}
			
			var cols = [],
				bCaptionRendered = false,
				origElement;
			
			$.each(line, function(c, element){
				if($.isArray(element)){ 
					if(gridLoopTimes >0 && bCaptionRendered) { // field row with loop
						origElement = $.extend(true, [], element);
						console.log("loop:"+ gridLoopTimes);
						console.log(origElement);
							
						for(var z=0; z < gridLoopTimes; z++) {
							
							element = copyFieldRow(origElement,z==0?0:1);  // first row add 0, else add 1
							console.log("%s:%s", (z+1), element);
							element = renderElement(fn, element);
							cols.push(element); // caption row, or no loop
						}
					} else{
						cols.push(renderElement(fn, element)); // caption row, or no loop
						bCaptionRendered = true;
					}
					
					
				}else {
					cols.push(renderElement(fn, element));
				}
				
			});
			
			boxes.push({
				cmd:cmd,
				label:label,
				cols:cols
			});
		});
		
		
		return {
			caption:caption,
			boxes:boxes,
			name:p.name,
			def:p.def
		};
	}
	function copyFieldRow(array, n){
		//var re=/(#\w+?)(\d{1,})/; //#TB_15, or #TC17
		var re=/(#\w+?)(\d+)/; //#TB_15, or #TC17
		
		function advanceRow(c)
		{
			c = c.replace(re, function(i,j,k){
				// if match then i=#TB_15, j=#TB_, k=15
				return j + (parseInt(k,10) + n); // #TB_ +  (15 + loopIndex).toString();
			});
			return c;
		}
		
		return (function iterate(arr){
			$.each(arr, function(i,x){
				if($.isArray(x)) {
					arr[i] =iterate(x); 
				}else {
					arr[i] = advanceRow(x);
				}
			});
			return arr;
		})(array);
	}
	function renderElement(fn, x) {
		if($.isArray(x)){ // multiple item in one grid cell
			var colsInCell = [];
			$.each(x,function(i, z){
				colsInCell.push(renderElement(fn, z));
			});
			return colsInCell;
		}else {
			if(x.match(/#/)) {
				return fn(tabIndex++, x);
			}else {
				return replaceLabelIndex(x);
			}
		}
	}
	function isCmd(x) {
		return startsWithdArray(cmds, x) != -1;
	}
	function isGird(box){
		return box.cmd.action == CMD_GRID;
	}
	function isExpanded(box) {
		return (box.cmd.action==CMD_EXPAND || (box.cmd.action == CMD_GRID && box.cmd.opt.expand));
	}
	function startsWithdArray(arr, k) {
		for(var i=0; i < arr.length; i++) {
			if(k.indexOf(arr[i]) !=-1) return i;
		}
		return -1;
	}
	function parseCmd(x) {
		var action="",
			opt={};
		
		if(x.indexOf(CMD_EXPAND)==0) {
			action = CMD_EXPAND;
			opt = null;
		}else if(x.indexOf(CMD_GRID)==0) {
			action = CMD_GRID;
			var ss = x.split(",");
			if(ss.length > 1) {
				ss.shift();
				var t = ss.join(",");
				opt = eval('(' + t + ')');
			}
		}
		return { action:action, opt:opt};
	}
	
	
// --------------------------------------------------
// Print
// ----------------------------------------------------
var NEWLINE = "\n",
	FORMFEED = "\f",
	RE_BR=/<br\/>/g;

function resetWorkArea() {
	$(workArea).empty();
}
function createPre(id){
	$(workArea).append("<pre></pre>").id(id);
}
function printScreen(def, fn) {
	var blocks = [];
	tabIndex = 0;
	labelIndex = 0;
	layout = getDefaultLayout();
	
	bScreenMode = false;
	$.each(def.display, function(i,x) {
		blocks.push(preparePage(x, fn));
	});
	
	var buf = [], r=[], caption;
	
	$.each(blocks, function(i, block){
		if(i != 0)
			buf.push("");
		
		layout = overrideLayout(layout, block.name, block.def);

		// put caption
//		buf.push(NEWLINE);
		if(block.caption) buf.push(block.caption+ NEWLINE);

		// advance labelIndex;
//		$.each(block.boxes, function(i, box){
//			if(box.label && box.label.match(RE_LABEL_INDEX)) {
//				box.labelIndex = ++labelIndex;
//			}
//		});
		
		if(layout.cols==1) {
			r = printOneColTable(block);
		}else {

			r = printTwoColsTable_P(block);
		}
		buf = buf.concat(r);

	});
	return buf.join(NEWLINE);
}

function sliceString(s, width) {
	if(IfxUtl.strlen(s) < width) return s;
	
	var r=[];
	var t;
	var len = IfxUtl.strlen(s);
	for(var i=0; i < len; i+= width) {
		t = IfxUtl.substr_big5(s, i, width);
		r.push(t);
	}
	return r.join(NEWLINE);
}

function printLabel(box,width) {
	var s= box.label,
		colon="";
	
	if(layout.printer.colon){
		colon = ":";
	}

//	if(s.match(RE_LABEL_INDEX)) {
//		var t=box.labelIndex.toString();
//		if(t.length<2) t = " " + t;
//		s=s.replace(RE_LABEL_INDEX, t);
//	}
	s = s.replace(RE_BR, NEWLINE);
	var ss= s.split(NEWLINE);
	var t=[];
	
	
	$.each(ss,function(i,x){
		if(IfxUtl.strlen(x) < width){ // last line of label
			x=IfxUtl.stringFormatterBig5(x, width - colon.length);
			t.push(x + colon);
		}else {
			t.push(x + colon);
		}
		
	});
	
	return t;
}


function printTwoColsTable_P(block) {
	var expandCells = [],
		k,i,
		leftSide=[],  // left side boxes
		rightSide=[];	// right side boxes
	
	// find expanded and advance labelIndex
	$.each(block.boxes, function(i, box){
		if(isExpanded(box)){
			expandCells.push(i);
		}
	});
	
	
	k=0; // first box
	
	var center;
	$.each(expandCells, function(ignore1,x){
		center = Math.ceil(x/2);
		for(i=0; k < x; k++,i++) {
			if(i < center) {
				leftSide.push(block.boxes[k]);
			}else {
				rightSide.push(block.boxes[k]);
			}
		}
		if(leftSide.length === rightSide.length + 1) {
			rightSide.push(null);
		}
		console.log("1 leftSide:"+ leftSide.length + ", rightSide:"+rightSide.length);

		leftSide.push(block.boxes[k++]); // put expanded box to left side;
		rightSide.push(null); // put an empty box to right for balance
	});

	center = Math.ceil((block.boxes.length - k)/2);
	for(i=0;k < block.boxes.length; k++,i++) {
		if(i < center) {
			leftSide.push(block.boxes[k]);
		}else {
			rightSide.push(block.boxes[k]);
		}
	}
	if(leftSide.length === rightSide.length + 1) {
		rightSide.push(null);
	}
	
	console.log("2leftSide:"+ leftSide.length + ", rightSide:"+rightSide.length);
	
	if(leftSide.length!=rightSide.length) {
		alert("BUG!! leftSide'length != rightSide's length");
		return;
	}
	
	var widthDefLeft = getPrintWidth(2, 0); // get left side layout
	var widthDefRight = getPrintWidth(2, 1); // get right side layout
	var leftSideWidth = widthDefLeft.labelWidth + widthDefLeft.fieldWidth;
	var expandedFieldWidth = widthDefLeft.fieldWidth + widthDefRight.labelWidth + widthDefRight.fieldWidth;
	var leftBuf, rightBuf, buf=[];
	
	var combinedBox = $.map(leftSide, function(leftBox, i){
		buf=[]; //reset buf
		// left box
		console.log("mapping " + i + " times");
		leftBuf = printOneBox(leftBox, widthDefLeft.labelWidth, isExpanded(leftBox) ? expandedFieldWidth : widthDefLeft.fieldWidth);
		rightBuf = rightSide[i]==null ? [] : printOneBox(rightSide[i], widthDefRight.labelWidth, widthDefRight.fieldWidth);
		
		if(!isExpanded(leftBox)) {  // not expand box, padding text to width of box(label+field)
			leftBuf = $.map(leftBuf, function(x){
				return IfxUtl.stringFormatterBig5(x, leftSideWidth);
			});
			$.each(leftBuf,function(i,tt) { console.log(IfxUtl.strlen(tt) + ":[" + tt + "]")});
		}
		console.log("leftBuf len:"+ leftBuf.length + ", rightBuf len:"+rightBuf.length);
		while(leftBuf.length < rightBuf.length) leftBuf.push(IfxUtl.stringFormatterBig5("", leftSideWidth));
		while(rightBuf.length < leftBuf.length) rightBuf.push("");
		console.log("==>leftBuf len:"+ leftBuf.length + ", rightBuf len:"+rightBuf.length);
		// combine left and right
		$.each(leftBuf, function(i, x){
			buf.push(x + " " + rightBuf[i]);
		});
		return buf;
	});
	
	
	// flattern combinedBox
	return  $.map(combinedBox, function(x) { return x;});
	
	
}
function printTwoColsTable_H(block) {
	var expandCells = [],
		k,i,
		leftSide=[],  // left side boxes
		rightSide=[];	// right side boxes
	
	// find expanded and advance labelIndex
	$.each(block.boxes, function(i, box){
		if(isExpanded(box)){
			expandCells.push(i);
		}
	});
	
	
	k=0; // first box
	var putLeft=true; // start from left side
	
	// helper method for putting box
	function easyPut(box) {
		if(putLeft) {
			leftSide.push(box);
		}else {
			rightSide.push(box);
		}
		putLeft = !putLeft;
	}
	
	$.each(expandCells, function(ignore1,x){
		for(; k < x; k++) {
			easyPut(block.boxes[k]);
		}
		if(!putLeft) easyPut(null); // put an empty box to rightSide for balance
		console.log("1 leftSide:"+ leftSide.length + ", rightSide:"+rightSide.length);

		leftSide.push(block.boxes[k++]); // put expanded box to left side;
		rightSide.push(null); // put an empty box to right for balance
		putLeft = true; // reset to left side
	});
	
	for(;k < block.boxes.length; k++) {
		easyPut(block.boxes[k]);
	}
	if(!putLeft) easyPut(null); // put an empty box to rightSide for balance
	
	console.log("2leftSide:"+ leftSide.length + ", rightSide:"+rightSide.length);
	
	if(leftSide.length!=rightSide.length) {
		alert("BUG!! leftSide'length != rightSide's length");
		return;
	}
	
	var widthDefLeft = getPrintWidth(2, 0); // get left side layout
	var widthDefRight = getPrintWidth(2, 1); // get right side layout
	var leftSideWidth = widthDefLeft.labelWidth + widthDefLeft.fieldWidth;
	var expandedFieldWidth = widthDefLeft.fieldWidth + widthDefRight.labelWidth + widthDefRight.fieldWidth;
	var leftBuf, rightBuf, buf=[];
	
	var combinedBox = $.map(leftSide, function(leftBox, i){
		buf=[]; //reset buf
		// left box
		console.log("mapping " + i + " times");
		leftBuf = printOneBox(leftBox, widthDefLeft.labelWidth, isExpanded(leftBox) ? expandedFieldWidth : widthDefLeft.fieldWidth);
		rightBuf = rightSide[i]==null ? [] : printOneBox(rightSide[i], widthDefRight.labelWidth, widthDefRight.fieldWidth);
		
		if(!isExpanded(leftBox)) {  // not expand box, padding text to width of box(label+field)
			leftBuf = $.map(leftBuf, function(x){
				return IfxUtl.stringFormatterBig5(x, leftSideWidth);
			});
			$.each(leftBuf,function(i,tt) { console.log(IfxUtl.strlen(tt) + ":[" + tt + "]")});
		}
		console.log("leftBuf len:"+ leftBuf.length + ", rightBuf len:"+rightBuf.length);
		while(leftBuf.length < rightBuf.length) leftBuf.push(IfxUtl.stringFormatterBig5("", leftSideWidth));
		while(rightBuf.length < leftBuf.length) rightBuf.push("");
		console.log("==>leftBuf len:"+ leftBuf.length + ", rightBuf len:"+rightBuf.length);
		// combine left and right
		$.each(leftBuf, function(i, x){
			buf.push(x + " " + rightBuf[i]);
		});
		return buf;
	});
	
	
	// flattern combinedBox
	return  $.map(combinedBox, function(x) { return x;});
	
	
}
function printOneColTable(block) {
	var buf=[];
	var fieldBuf = [];
	var labelBuf;
	var widthDef = getPrintWidth(1, 0); // one column, first column
	var fieldWidth;

	$.each(block.boxes, function(i, box){
		buf = buf.concat(printOneBox(box, widthDef.labelWidth, widthDef.fieldWidth));
	});

	return buf;
}	

function printOneBox(box, labelWidth, fieldWidth)
{
	var buf=[],	// for whole box
		labelBuf=[], // for label part
		fieldBuf = []; // for field part
		
	
	if(box.label) {
//		if(box.label.match(RE_LABEL_INDEX)) {
//			box.labelIndex = ++labelIndex;
//		}
		labelBuf = printLabel(box, labelWidth);
	}else {
		// no lable, expand field width
		fieldWidth += labelWidth;
	}
	
	if(box.cmd.action == CMD_GRID ) {
		fieldBuf.push(printGrid(box.cols, box.cmd.opt));
	} else{
		$.each(box.cols, function(i,c){
			c = c.replace(RE_BR, NEWLINE);
			fieldBuf.push(c + " ");  
		});
	}
	
	fieldBuf = fieldBuf.join("").split(NEWLINE);
	
	// combine label with field
	var t;
	if(box.label) {
		var max = Math.max(labelBuf.length, fieldBuf.length); //�ݽ֤���
		for(var i=0; i < max; i++) {
			// �p�Glabel�L�u(�Φ�Ƥ���), �ɺ����
			t = labelBuf[i] ? labelBuf[i] : IfxUtl.stringFormatterBig5("", labelWidth);
			if(fieldBuf[i]) {
				t += " " + (fieldBuf[i]);
			}
			buf.push(t);
		}
	}else {
		for(var i=0; i < fieldBuf.length; i++) {
			buf.push(fieldBuf[i]+ " ");
		}
	}
	
	return buf;
	
}
function printGrid(cols, opt) {
	var buf=[], 
		colsWidth=[],
		gridStarted=false,
		delimeterAdded=false,
		rowHeight = opt["row_height"] || 1;
	
	function getUserDefineColWidth(colIndex) {
		if(opt && opt["p_cols"]) {
			var t = opt["p_cols"][colIndex];
			return parseInt(t,10);
		}
		return null;
	}	

	$.each(cols,function(i, x){
		if($.isArray(x)){
			$.each(x, function(j,y){
				if(!colsWidth[j]) colsWidth[j]=0;
				if($.isArray(y)) {
					$.each(y,function(k,z){
						colsWidth[j] = Math.max(colsWidth[j], IfxUtl.strlen(z));
					});
				}else {
					colsWidth[j] = Math.max(colsWidth[j], IfxUtl.strlen(y));
				}
			});
		};
	});

	var widthBefoerMe=[0],
		widthNow=0;
	$.each(colsWidth,function(i,w){
		widthNow += w;  
		if(i>0) {
			widthBefoerMe.push(widthNow);
		}
	});
	
	
	var deli=" ";
	$.each(colsWidth, function(i,x){
		var w = getUserDefineColWidth(i);
		if(!isNaN(w)) {
			colsWidth[i] = Math.max(w, colsWidth[i]);
			deli += new Array(colsWidth[i]).join("-") + " ";
		};
	});
	
	var gridLine=[];
	var max=0;
	var cellContent="";
	buf.push(NEWLINE);
	$.each(cols,function(i, x){
		if($.isArray(x)){  // array item means start of grid 
			
			gridStarted=true;
			gridLine=[];
			buf.push(NEWLINE);
			$.each(x, function(j,y){
				if($.isArray(y)) {
					cellContent="";
					$.each(y,function(k,z){
						cellContent += (z);
						if(z!="<br/>") cellContent+=" ";
					});
					
				}else { 
					cellContent = y;
				}
				gridLine.push(cellContent.replace(RE_BR, NEWLINE).split(NEWLINE));
				
			});
			
			var max = _.reduce(gridLine,function(m,x) { 
									return Math.max(m,x.length); },0);	
			var ele;
			for(var y=0; y < max; y++) {
				cellContent = "";
				for(var x=0; x < colsWidth.length; x++) {
					ele = gridLine[x][y];
					cellContent += IfxUtl.stringFormatterBig5(
							ele ? ele: "" , colsWidth[x]);
				}
				buf.push(" "+	cellContent + " ");//fd
				if(y+1<max) buf.push(NEWLINE);
			}
			//cellContent=IfxUtl.stringFormatterBig5(cellContent, colsWidth[j]);
			//buf.push(" "+cellContent + " ");//fd
			
			
			
			// underline for grid column title
			if(!delimeterAdded) {
				buf.push(NEWLINE);
				buf.push(deli)
				delimeterAdded = true;
			}else{
				while(max++ < rowHeight) 
					buf.push(NEWLINE);
				
			}


		}else {
			// text description outside grid 
			if(gridStarted) {
				buf.push(NEWLINE);
				buf.push(NEWLINE);
				gridStarted = false;
			}
			buf.push(x.replace(RE_BR, NEWLINE) + " "); 
		}
		
	});
	//buf.push(NEWLINE);
	return buf.join("");
}
// ------------------------------------------
// layout
// -------------------------------------------	
	function getDefaultLayout() {

		var _layout ={
				cols:1,
				screen: {
					width:[160,400,160,400],
					left:0
				},
				printer:{
					width:[20,20,20,20],
					top:2,
					left:3,
					gap:1,
					colon:true,
					linesPerPage:66
				}
		};		
		return _layout;
	}	
	function overrideLayout(layout, name, d){
		
		if(d.layout) {
			var cc = d.layout.split(/\s*;\s*/);
			$.each(cc, function(i,x){
				if(x) {
					var o = layout;
					var ss=x.split(/\s*=\s*/);
					
					if(ss[0] == "cols" && ss[1]=="1") {
						layout.screen.left = 70;
					}
					var aa = ss[0].split(".");
					
					$.each(aa,function(i, y){
						
						if(!(y in o)) {
							console.log("find no attrib " + y + ", probably invalid layout override,  please check " +x + " of part " + name);	
						}
						else {
							if(i < aa.length -1) {
								o = o[y];
							}
							else {
								o[y] = eval('(' + ss[1] + ')');
							}
						}
					});
					
				}
			});
		}
		console.dir(layout);
		return layout;

	}
	function getLabelWidth(cols, i) {
		var offset = 0;
		if(cols>1 && i%2!=0) {
			offset = 2;
		}
		return {
			label:layout.screen.width[offset],
			field:layout.screen.width[offset+1]
		};
	}

	function getPrintWidth(cols,i) {
		var offset = 0;
		if(cols>1 && i%2!=0) {
			offset = 2;
		}
		return {
			labelWidth:parseInt(layout.printer.width[offset], 10),
			fieldWidth:parseInt(layout.printer.width[offset+1],10)
		};
	}		
	function getBuddy(def) {
		var a1 = _.map(def.display,function(x){
			if(!x.def || !x.def.buddy) return [];
			return x.def.buddy.split(',');
		});
		return _.flatten(a1).sort();
		
	}
	return {
		renderScreen: renderScreen,
		printScreen:printScreen,
		getBuddy:getBuddy
	};
})();
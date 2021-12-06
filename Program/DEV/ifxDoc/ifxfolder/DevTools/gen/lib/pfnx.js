var file = require("./file");


exports.parse = parse

function parse(defaultFolder, lines) {
   var result = {};
   lines = findBlock(lines, "doc", ["type"], function(attr, buf) {
      console.log("type:"+ attr["type"]);
      console.log("content:\n" + buf.join("\n"));
      result["type"] = attr["type"];
      
      buf = findBlock(buf, "head", null, partParseWithDefault(result, "head", "_head.pfnx",attr, buf));
      buf = findBlock(buf, "face", null, partParse(result, "face", attr, buf));
      buf = findBlock(buf, "body", null, partParse(result, "body", attr, buf));
      buf = findBlock(buf, "leg", null, partParse(result, "leg", attr, buf));
      buf = findBlock(buf, "foot", null, partParseWithDefault(result, "foot", "_foot.pfnx", attr, buf));
   
   //   console.dir(result);
   });
   return result;  

   function partParse(o, name, attr, buf) {
      return function(arrt, buf) {
         o[name] = processFmt(buf);
      }
   }
   function partParseWithDefault(o, name, defaultFileName, attr, buf) {
      return function(arrt, buf) {
         if(buf.length==0) {
            var filePath = defaultFolder + "/" + defaultFileName;
            buf = file.readLines(filePath);
            for(var i=0; i < buf.length;i++) {
               buf[i] = buf[i].replace(/[\r]/g,"");
            }
         }
         o[name] = processFmt(buf);
      }
   }
   
}
function processFmt(buf) {
   var result=[],
      fd =" ",
      ch,i, lastPos=0,
      bWidthCollected= false;
      colsWidth=[];
      
   buf.forEach(function(x) {
      ch = x.charAt(0).toString();
      x = x.slice(1);
      if(ch!=" "){
         if(ch=="x") fd=" "; // reset fd
         else  {
            fd = ch;
            bWidthCollected=false;
            colsWidth=[];
         }
      } // else use last fd
      if(fd!=" ") {
         //result.push(x.split(fd));
         var xx = x.split(fd);
         if(!bWidthCollected) {
            xx.forEach(function(y){
               colsWidth.push(y.length);
            });
            colsWidth[colsWidth.length-1] = -1; // last column no width
            bWidthCollected=true;
         }
         var tmp=[];
         for(var i=0; i < xx.length; i++) {
            var w = colsWidth[i];
            tmp.push(makeItem(xx[i],true, w));
         }
         result.push(tmp);
         
      }else {
         result.push([makeItem(x, true,-1)]);
      }
   });
   return result;   
}
function makeItem(x, alignLeft, width) {
   return {
      text:x,
       align: alignLeft? 'left' : 'right',
       width:width
     };
   
}

function findBlock(lines, tag, attribNames, fn) {
   var result=[],
      started = false,
      attribs={},
      reBegin = new RegExp("<\\s*"+ tag + "\s*"),
      reEnd = new RegExp("</\\s*" + tag + "\s*>"),
      i;
      
   for(i=0; i < lines.length; i++) {
      lines[i] = lines[i].replace(/[\r]/g,"");
      if(lines[i].match(reBegin)) {
         started = true;
         if(attribNames) {
            attribs = getAttributes(lines[i], attribNames);
         }
         if(lines[i].match(/\/>\s*/)) { // close tag
            break;
         }
      }else {
         if(started) {
            if(lines[i].match(reEnd)) {
               break;
            }else {
               result.push(lines[i]);
            }
         }
      }
   }
   if(started && fn) {
      fn(attribs, result);
      return lines.slice(i+1);
   }
   return lines;
}

function getAttributes(s, attrs) {
	var o = {};
	attrs.forEach(function(x){
		o[x] = getAttrib(s, x);
	});
	return o;
}

function getAttrib(s, attrName) {
	//var attr =  new RegExp( attrName + "\\s*=\\s*(['\"])[^'\"]*\\1","i");
	var attr =  new RegExp( attrName + "\\s*=\\s*(['\"])([^'\"]*)\\1","i");
	var r = s.match(attr);
	if(!r) return null;
	return r[2];
//	var rr = r[0].split("=");
	//var quote = new RegExp(r[1],"g");
	//return (rr[1].replace(quote,"")).trim();
}
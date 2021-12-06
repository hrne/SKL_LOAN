var file = require('./file'),
   expander = require('./loopExpander');
exports.parse = parse;
exports.processAppFields=processAppFields
exports.getLst = getLst;
exports.getListtitle = getListtitle;

var _cfg;
var _listtitle ="";

var tmpResult = [];
var checkfldindata = false;

var lst = [];
function getLst() {
	var i =0;
	var t= [];
	var s;

	lst.forEach(function(x) {
		i++;
		// lazy and idiot formatter
		if(i<10) s = "000" +i;
		else if(i <100) s = "00"+i;
		else if(i < 1000) s = "0" + i;
		else s = i;
		s += '| ';
		t.push(s + x);
	});
	return t.join('\n');
}
function getListtitle() {

	return _listtitle;
}
//幫忙store ....要注意CALL沒有意義
var storeunnum=0;
function processStoreRim(lines) {
	var tag = /^S\(/,
	    tag3 = /^#/,//  tag3 = /^\s*$/,
	    tag4 = /\=[xXADFnmNMJDPcC],[0-9]+,/,
	    tag1 = /^#/,
	    norim = /XXR22/,
	    tag0 = /\=@,/,
	    tag2 = /^@S\(/, //<sub type="PREDC" name="PreDC">
	    tag5 = /\)S\(/,
      dirty = true,
      result,
      texts,
      s,thefirst=0;

		result = [];
		storerim =[];
		storerim.push("!*****THE SYSTEM AUTO ADD!****");
		storerim.push("!*****(STORE XXR22 NOT WORK)****");
		storerim.push("!#systemstorerim"+storeunnum+"=X,1,S");
		storerim.push("!C(2,#FKEY,S,S,S,$,S,S,S,$,S,S)");
		for(var i=0; i < lines.length; i++) {
			s = lines[i];
			if(s.match(tag)) { //符合函式S(
				for (var j = i; j > 0; j--) {
				  if(lines[j].match(tag1) && !lines[j].match(tag0) && !s.slice(2,7).match(norim)) {//找到他的上一個的欄位,還有不等於@型態,不是XXR22
				  	storerim.push("!STORE_RIM("+	lines[j].slice(0,lines[j].indexOf("=")) + "," + s.slice(2,s.indexOf(')'))+")");
				  	break;
				  }
				}
			}else if(s.match(tag2)) {
				for (var j = i-1; j > 0; j--) {
				  if(lines[j].match(tag1) && !lines[j].match(tag0) && !s.slice(3,7).match(norim)) {
				  	storerim.push("!STORE_RIM("+	lines[j].slice(0,lines[j].indexOf("=")) + "," + s.slice(3,s.indexOf(')'))+")");
				  	break;
				  }
				}
			}else if(s.match(tag5)) {
				for (var j = i-1; j > 0; j--) {
				  if(lines[j].match(tag1) && !lines[j].match(tag0) && !s.slice(s.indexOf(")S(")+3,7).match(norim)) {
				  	var temp = s.slice(s.indexOf(")S(")+3);
				  	storerim.push("!STORE_RIM("+	lines[j].slice(0,lines[j].indexOf("=")) + "," + temp.slice(0,temp.indexOf(')'))+")");
				  	break;
				  }
				}
			}else if(s.match(tag3)&& s.match(tag4)) {
				if(thefirst == 0){
				 thefirst = i;
				}
			}
			result.push(s);
		}
		storerim.push("!*****END*****");
		if(storerim.length > 5){
		lines = result.slice(0,thefirst-1).concat(storerim,result.slice(thefirst-1));
		storeunnum++;
	}else{
		lines = result;
	}

	return lines;
}

function appendLst(r) {
		lst = lst.concat(r);
}

function parse(cfg, txcode ) {
	_cfg = cfg;
	console.log("parse :"+ txcode);
	var fileName;
	//改成從 SUPER INCLUDE 中取檔案
	 fileName = _cfg.getLstFilePathtmp(txcode);
	console.log("parse filename " + fileName  + "\n");
	var lines = file.readLines(fileName);

	console.log("parse length " + lines.length);

	tmpResult = [];
	while(lines!=null) {
	//	console.log(lines.length + " more");
		lines = findBlock(lines,"sub", function(x) {
			var type = getAttrib(x[0], "type");
			console.log("type:"+ type);
			var fn = getHandler(type);
			if(fn)
				tmpResult.push(fn(x));
		});
	}
	//console.log(result.length);
	//console.log(JSON.stringify(result, null, 4));


	return refactor1(tmpResult);
}
function refactor1(r) {
	var result = {};
	result.rtns = [];
	result.display = [];
	result.parts = [];
	var sysField = {};


	r.forEach(function(x) {
		var isPart;
		if(x.rtn) {
			isPart = (x.realType=='PART');
			if(!isPart) {
				result.rtns.push(x);
			}
			if(x.display) {
				if(!isPart) {
					result.display.push({
						name:x.name,
						def:x.display
					});
				}else {
					result.parts.push({
						name:x.name,
						def:x.display
					});

				}
				delete x.display;
			}
		}
		else {
			if(x.type=="select") {
				result[x.type] = x.list;
			}else if(x.type=="tom") {
				result[x.type] = x.map;
				result["tomlength"] = x.maplen;
			}else if(x.type=="tim")  {
				//result[x.type] = x.list;
				result[x.type] = x;
			}else {
				result[x.type] = x;
			}
		}
	});
	return result;
}

function procesBlock(lines) {
	var type = getAttrib(lines[0], "type");
	console.log("type:"+ type);
	var fn = getHandler(type);
	if(fn)
		fn(lines);
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
function getAttributes(s, attrs) {
	var o = {};
	attrs.forEach(function(x){
		o[x] = getAttrib(s, x);
	});
	return o;
}
function findBlock(lines, tag, fn) {
	var tagBegin = new RegExp("^<\\s*" +tag + "\\s+" , "i");
	var tagEnd1 = new RegExp("/>\\s*$");
	var tagEnd2 = new RegExp("^</\\s*" + tag + "\\s*>\\s*$" , "i");
	var tagInclude = new RegExp("<\\s*include\\s+" , "i");
	var lineStart = 0;
	var result = [];
	var s;
	var j=0;
	for(var i=0; i < lines.length; i++) {
		//if(isComment(lines[i]))continue;

		if(isBegin(lines[i])) {
			//console.log("matched begin:"+ lines[i]);
			var kk = lines[i];
			while(!(isClose(kk))) {
				kk +=  " " +  (lines[++i]).trim();
			}

			result.push(kk);
			if(isEnd(lines[i])) {
				if(fn) fn(result);
				return lines.slice(i+1);
			}


			for(j=i+1; j < lines.length; j++) {
				//if(isComment(lines[j]))continue;
				if(isEnd(lines[j])) {
				//	console.log("match end:"+ lines[j]);
					result.push(lines[j]);
					//if(fn) fn(result);
					expandLoop(result,fn);
					return lines.slice(j+1);
				}else {
					result.push(lines[j]);
				}
			}
		}
	}

   function expandLoop(result, fn) {
	  result = processInclude(result);
      result = expander.expand(result);
      if(fn) fn(result);
   }
	console.log("no more");
	function isBegin(s) {
		return s.match(tagBegin);
	}
	function isEnd(s) {
		return (s.match(tagEnd1) || s.match(tagEnd2)) && (!(s.match(tagInclude)));
	}
	function isClose(s) {
		return s.match(/>\s*$/) ||  s.match(tagEnd1) || s.match(tagEnd2);
	}
}


// handlers

var handlers = {};
handlers["SYS"] = handler_sys;

handlers["DC"] = handler_dc;
handlers["PREDC"] = handler_preDC;

handlers["PART"] = handler_dc;
handlers["RTN"] = handler_rtn;
handlers["FORM"] = handler_form;

handlers["TIM"] = handler_tim;
handlers["RESV_OFF"] = handler_tim;
handlers["TOM"] = handler_tom;

handlers["SELECT"] = handler_select;


function getHandler(type) {
	type = type.toUpperCase();
	var fn = handlers[type];
	if(fn!=null) return fn;
	return null;
}


function handler_sys(lines) {
	console.log("handler_sys");
	var name = getAttrib(lines[0], "name");
	var sysFile = _cfg.getSys(name);
	console.log(sysFile);

	var texts = file.readLines(sysFile);
	var sys_fields = processFields(texts, "sys:"  + (name? name : "sys"));
	if(!name) {
		sys_fields.forEach(function(x){
			x["_sys_"] = true;
		});
	}
	appendLst([lines[0]]);
	appendLst(texts);
	//fields.each(function(x) { console.log(x.def);});

	if(lines.length > 2) {
		lines = lines.slice(1,-1); // remove <sub> and </sub>
		appendLst(lines);
		var ovrFields = processFields(lines, "SYS.body");
		console.log(ovrFields.length);
		ovrFields.forEach(function(x) {
			var found = false;
			sys_fields.forEach(function(y){
				if(y.name==x.name) {
					if(x.pref_var) y.pref_var = x.pref_var;
					if(x.post_var) y.post_var = x.post_var;
					y.ovr = true;
					console.log("**** "+y.name + ":" + y.pref_var);
					found = true;
				}
			});
			if(!found) sys_fields.push(x);
		});
	}
	appendLst(["</sub>"]);
	//sys_fields.forEach(function(x){ dumpField(x);});
	return {
		"type":"sys",
		"name": "_" + (name? name : "sys"),
		"rtn":true,
		"fields":sys_fields.map(function(x) { return expandRtn(x);})
	};
}

var refFiles = {};
function addRef(n) {
	refFiles[n] = true;
}
function processInclude(lines) {
	var tag = /^\s*<include\s+/i,
      dirty = true,
      result,
      texts,
      s;

	while(dirty) {
		dirty = false;
		result = [];
		for(var i=0; i < lines.length; i++) {
			s = lines[i];
			if(s.match(tag)) {

				dirty = true;
				var src = getAttrib(s, "src");
				if(!src) {
					console.error("include with src"+ s);
					throw "include error:" + s;
				}

				var map = toMap(getAttrib(s,"map"));
				console.log("found " + src);
               if(src.indexOf("::") == -1) {
                  texts = file.readLines(_cfg.getInclude(src));
               }else {
                  var ss = src.split('::');
                  texts = readIncludeBlock(ss[0],  file.readLines(_cfg.getInclude(ss[0])).join("\n"), ss[1]);

               }
				console.log("incude " + texts.length);
				texts.forEach(function(x){
					result.push(replaceWith(x, map));
					//result.push(x);
				});
			}else {
				//console.log(s);
				result.push(s);
			}
		}
		lines = result;
	}
	return result;

   function readIncludeBlock(src, content, name) {
   //   /<(s\d+)>\n([\s\S]*)\n<\/\1>/m
      var re = new RegExp("<" + name + ">([\\s\\S]*)</" + name + ">","m"),
         m = content.match(re);
      if(m) {
         return m[1].split("\n");
      }
	  var error ="MERR find no block:"+name + " in file:"+ src;
	  console.error(error);
      throw error;
   }

    function replaceWith(s, o) {
      for(var k in o) {
         var re = new RegExp('\{' + k + '\}','g');
         s = s.replace(re, o[k]);
      }
      return s;
   }
	function toMap(s) {
		var m = {};
		if(s){
			var ss= s.split(';');
			var nn;
			ss.forEach(function(x){
				nn = x.split('=');
				m[nn[0].trim()] = nn[1].trim();
			});
		}
		return m;
   }
}


function isComment(s) {
	var t = /^\s*!/
	return s.match(t);
}
function removeComments(lines) {
	var t= [];
	lines.forEach(function(z) {
		if(!isComment(z)) t.push(z);
	});
	return t;
}

function processFields(lines, part, gostorerim) {
	lines = processInclude(lines);
	//if(gostorerim){
	// lines = processStoreRim(lines);
	//}
	var i=0;
	function getLine() {
		while(true) {
			var s = lines[i++];

			if(s==null) return null;
			if(!isComment(s) && s.trim().length > 0) return s;
         else console.log('comment:' + s);
		}
	}
	function pushBack() { i--; }
	function getField() {
		var s1 = getLine();
		if(s1==null) return null;
		console.log(">>> " + part + ", " + s1);
		var prefs = [];
		var posts = [];
		var t;
		var postStarted = false;
		while(true) {
			t = getLine();
			//console.log("t:"+t);
			if(t==null ||t.startsWith("#"))  { // new field
				pushBack();
				var fld = expandDef(s1.trim());
				fld["pref_var"] = prefs.join("");
				fld["post_var"] = posts.join("");
				//console.log("pref:"+fld["pref_var"]);
				//console.log("=========");
				return fld;
				break;
			}else if(t.startsWith("@")) { // post function
				postStarted = true;
				t = t.slice(1);
			}
			var y = t.match(/\\\s*$/); //續下行?
			if(y) t = t.slice(0, y.index);
			if(!postStarted) prefs.push(t.trim());
			else posts.push(t.trim());

		}
		return null;
	}


	function merge(obj1, obj2) {
		var i;
		for (i in obj2) {
			if (obj2.hasOwnProperty(i)) {
				obj1[i] = obj2[i];
			}
		}
	}
	var fields = [];
	while(true) {
		var o = getField();
		if(o) {
			checkField(o, part);
			fields.push(o);
		}
		else break;
	}
	console.log("fields :"+ fields.length);
	return fields;

}
var _fieldNames = {};
var _fieldlen = {};
//tmpResult
function checkField(fld, part) {
	var err;
	if(fld.type != "_" && fld.len!= "_" && fld.attr) { // new field?
		if(fld.name in _fieldNames) {
			err = "MERR duplicate field defined:" + fld.name + ", already defined at " + _fieldNames[fld.name];
			console.error(err);
			throw err;
		}
		console.log("new field "+ fld.name);
		_fieldNames[fld.name] = part;
		//長度(包含小數) & 如果是"+"開頭,則多加1位
		_fieldlen[fld.name] = fld.len + fld.dlen + (fld.type.indexOf("+") != -1 ? 1:0);
	}else {
		if(!(fld.name in _fieldNames)) { // override
			err = "MERR field " + fld.name + " is not defined previously";
			console.error(err);
			throw err;
		}
		console.log("ovrride field "+ fld.name);

		fld.ref = true;
	}
}
//柯:檢測是否有這個欄位..
function checkisinFields(fldname) {
	var err;

	if(!(fldname in _fieldNames)) { // override
		if(checkfldindata){
		 err = "MERR field " + fldname + " is not defined previously";
		 console.error(err);
		 throw err;
		 return null;
		}
		return 0;
	}
	return _fieldlen[fldname];
}
function expandDef(s) {
	if(s.indexOf("=")==-1) { return { name:s};}

	var ss=s.split(/\s*=\s*/);

	var xx = ss[1].split(/\s*,\s*/);
	var dlen=0;
	var dd = xx[1].split(".");
	var len = parseInt(dd[0],10);

	if(dd[1]) dlen=parseInt(dd[1],10);
   var extra = null;
   if(xx[3]) {
      extra = {};
      var zz = xx[3].split(';');
      zz.forEach(function(z) {
         var p = pair(z);
         extra[p.fst] = p.snd;
      });
   }

	var r = {
		name:ss[0].trim(),
		type:xx[0].trim(),
		len:len,
		dlen:dlen,
		attr:xx[2]

	};
	checkAllowedType(r.name, r.attr);

   if(extra) r['extra']=extra;
   return r;
}
function checkAllowedType(name, attr){
	var allowTypes = "IO@LSH";
	var errmsg="";
	if(attr.length > 1){
		errmsg = name + ",attribute too long:"+attr;
		throw new Error(errmsg);
	}
	if(allowTypes.indexOf(attr) == -1) {
		errmsg = name + ", no such attribute:"+attr;
		throw new Error(errmsg);
	}
}

function expandRtn(fld) {
	var funcParser = require("./funcparser");
	var funcMapper = require("./funcmapper.js");

	//var o = expandDef(fld.def);
	if(fld.pref_var) {
	//	o.pref_var = fld.pref_var;
		fld.pref = funcParser.parseFunc(fld.pref_var,0);
	}
	if(fld.post_var) {
		//o.post_var = fld.post_var;
		fld.post = funcParser.parseFunc(fld.post_var,0);
	}
	//o.pref = fld.pref;
	//o.post = fld.post;
	funcMapper.write(fld);
	delete fld.pref;
	delete fld.post;

	delete fld.pref_var;
	delete fld.post_var;


	fld._ = [ fld.name, fld.type,fld.len,fld.dlen,fld.attr,fld.pref_body,fld.post_body];
	delete fld.name;
	delete fld.type;
	delete fld.len;
	delete fld.dlen;
	delete fld.attr;
	delete fld.pref_body;
	delete fld.post_body;

	return fld;
}

function dumpField(o) {
		console.log(o.def);
		console.log("\tpref:" + o.pref.join(""));
		console.log("\tpost:" + o.post.join(""));
		if(o.ovr) console.log("\t*** overrided");
		console.log("    \n");
}

function handler_dc(lines) {

	return handler_xxx(lines, "dc", true);
}
function handler_preDC(lines) {
	return handler_xxx(lines, "preDC", false);
}
function handler_rtn(lines) {
	return handler_xxx(lines, "rtn", false);
}
function handler_form(lines) {
	return handler_xxx(lines, "form", false);
}

function handler_xxx(lines, type, bDisplay) {

lines = processInclude(lines);
//畫面時增加儲存RIM的功能
//PS:太多MAKE動手腳的部分,以後問題一定很多
//if(bDisplay){
// lines = processStoreRim(lines);
//}
appendLst(lines);
lines = removeComments(lines);
	var props = getAttributes(lines[0], ["type", "name","src","layout","buddy","render",'prompt','handler','tota-mode']);
	var srcFile;
	if(props.src) {
		srcFile = _cfg.getInclude(props.src);
		lines = file.readLines(srcFile);
	}else {
		lines = lines.slice(1, -1);
	}

	console.dir(props);

	var result = {
		"type": type,
		"realType": props.type,
		"name": props.name,
		"rtn":true
	};
	if(type=="form") {
		if(props.handler)
			result['handler'] = props.handler;
		if(props['tota-mode']){
			result['tota-mode']=props['tota-mode'];
		}
	}
	if(bDisplay) {
		var obj = {};
		console.log(" bf getdisplay ");
		obj.list = getDisplay();
		console.log(" display list " + obj.list);
		if(props["layout"])
			obj["layout"] = props["layout"];
		if(props["buddy"])
			obj["buddy"] = props["buddy"];
		//obj.layout = getLayout(props["layout"]);
		result["display"] = obj;
		if(props["render"]) {
			obj["render"] = props["render"];
			result['render'] = obj['render'];
		}
		if(props["prompt"]) {
			obj["prompt"] = props["prompt"];
		}
	}
	var hint = type + ":" + (props.name? props.name :"[noname]") + " " +  (props.src?  "src:" + props.src : "");


	result["fields"] = (processFields(lines, hint, true)).map(function(x) {
			return expandRtn(x);
	});

	return result;

	function getDisplay() {
   console.log(" getdisplay " + lines[0]);
      if(lines[0] && chomp(lines[0]).match( /<s\d+>/)) {
		console.log("matched");
         return expandSimple();
      }
		var result=[];
		for(var i=0;i<lines.length;i++) {
			var s = lines[i].trim();
			//柯-替代方案，@取代成空白
			s = s.replace(/@/g,"&nbsp");
			console.log( "s="+s+"   lines[i]" + lines[i]);
			//console();

//鍾 畫面定義,變數可以不加"
			s = resign(s);
//end
			result.push(s);
			//取得標題
			if(i == 1 ){
				_listtitle += s.slice(2,s.length-3);
			}
			if(s == "]")  {
				lines = lines.slice(i+1);

				var t = result.join("");
				t = t.replace(/[\t\r\n]/g,"");
				var o = eval('(' + t + ')');
				console.log(" return o " + o);
				return o;
			}
		}
	}

//鍾 畫面定義,變數可以不加"
   function  resign(s){
      var t = "", ts = "", lt = "",flg = 0;
	  for (var i=0;i<s.length;i++){
	      lt = t;
	      t = s.substr(i,1);
		  switch(t) {
		  case '#':
              if (flg == 0 && lt != '"' && s.substr(i-3,3) != " ^ ") ts += '"';
			  flg = 1;
			  break;
		  case ',':
		  case ']':
		      if (flg == 1){
                 if (s.substr(i-5,4) != "grid")	ts += '"';
			     flg = 0;}
			  break;
          case '"':
              if (flg == 1) flg = 0;
			  break;
		  default:
			  break;
		  }
		  ts = ts + t;
	  }
	  if (flg == 1) ts += '"';
      return ts;
   }
//end
   function chomp(s) {
      return s.replace(/[\s\t\r\n]/g,"");
   }
   function expandSimple() {
      var t = chomp(lines[0]),
         m = t.match(/<(s\d+)>/),
         tag = m[1],
         reEnd = new RegExp("</" + tag + ">"),
         result=[];

          //   /<(s\d+)>\n([\s\S]*)\n<\/\1>/m
          // extrace <s1>nnnn</s1>

         console.log("expand simple:"+tag);
      for(var i=1; i < lines.length; i++) {
         t = chomp(lines[i]);
         if(!t.match(reEnd)) {
            if(i>1 && tag=="s1") {
               t = " ^ " + t;
            }
            result.push(t.split(/,/));
         }else {
            lines = lines.slice(i+1);
            return result;
         }
      }


   }
	function getLayout(name) {
		var t = file.readLines(_cfg.getLayout(name));
		t = t.join("");
		console.log("t:"+t);
		return  eval('(' + t + ')');

	}
}




function handler_tim(lines) {
console.log("parse tim");
   lines = processInclude(lines);
   appendLst(lines);
   var startTag = lines[0];
	lines = lines.slice(1,-1);
	var reX = /X\((\d+)\)/;
	var re9 = /^\d+$/;

   var names = ["type", "target","bean","host-file","file-grid","file-txt-grid","input","output",'box','handler',"label","way","runrim","skipline"];
   var attrs = getAttributes(startTag, names);

  var type = attrs['type'].toUpperCase();
   var target= attrs['target'] || 'host';
   console.log("target:"+target);
   console.log("label:"+attrs["label"]);
   if(target=='host' && type == "TIM") {
		if(attrs["label"] != "no"){
			var titaLabelFile = _cfg.getSys("LABEL",".tim");
			console.log("tita label file:"+titaLabelFile);
			var label = file.readLines(titaLabelFile);
			appendLst(["* * * AUTO INCLUDE LABEL.TIM, new TIM BLOCK:"]);
			appendLst(["============================================"]);
			appendLst(label);
			appendLst(lines);
			appendLst(["* * * END OF AUTO INCLUDE LABEL.TIM\n"]);
			console.log("label:"+label);
			lines = label.concat(lines);
		}
   }
   var targetMapper = {
      'host':getTimeFields,
      'bean':getBeanDef,
      'host-file':getBeanDef,
      'file-grid':getFilegrid,
      'file-txt-grid':getFilegrid
   };

   var r = {
		"type":type.toLowerCase(),
      "target":target,
		"name": "_tim"
	};

	if(attrs['box']) r['box'] = attrs['box'];
	if(attrs['way']) r['way'] = attrs['way'];	 //是否走MQ?
	if(attrs['runrim']) r['runrim'] = attrs['runrim'];	//送出交易前先run CALL
	if(attrs['skipline']) r['skipline'] = attrs['skipline']; //使用txt檔案時跳過的行數

	if(attrs['handler']) r['handler'] = attrs['handler'];
	targetMapper[target](lines);
   return r;

   function getBeanDef(lines) {
      r['bean'] = attrs['bean'];
      //r['input'] = attrs['input'] || 'list';
      //r['output'] = attrs['output']

      var s = lines.join('');
      findAndSet('put');
      findAndSet('get');
      findAndSet('get.header');

      function findAndSet(tag) {
         var re =  new RegExp(tag+"={([\\s\\S]*?)}","m")
         var arr = s.match(re);
         if(arr) {
            r[tag] = parseAgain( arr[1].split('\n').join(''));
         }
      }

      function parseAgain(z) {
         z = z.replace(/[\t\r]/g,"");
         var zz = z.split(',');
         var o = {};
         zz.forEach(function(x){
            var p = pair(x);
            o[p.fst] = p.snd;
         });
         return o;
      }

   }
    //柯 新增 file-grid
     function getFilegrid(lines) {
      r['file-grid'] = attrs['file-grid'];
      //r['input'] = attrs['input'] || 'list';
      //r['output'] = attrs['output']

      var s = lines.join('');
      findAndSet('get');   //只有get

      function findAndSet(tag) {
         var re =  new RegExp(tag+"={([\\s\\S]*?)}","m")
         var arr = s.match(re);
         if(arr) {
            r[tag] = parseAgain( arr[1].split('\n').join(''));
         }
      }

      function parseAgain(z) {
         z = z.replace(/[\t\r]/g,"");
         var zz = z.split(',');
         var o = {};
         zz.forEach(function(x){
            var p = pair(x);
            o[p.fst] = p.snd;
         });
         return o;
      }

   }
   function getTimeFields(lines) {
   console.log("parse lines:"+lines);
      var s='';
      var timLength = 0;
      lines.forEach(function(x) { s += x.trim(); });
      var ss = s.split('#');
      ss.shift(); // ignore first one
	  var beginBox=false;
      ss = ss.map(function(x){
            var z =x.match(reX);
            if(z) {
               var n = parseInt(z[1],10);
               timLength += n;
               return (new Array(n+1)).join(" ");
            }else {
				if(x.match(re9)) return x;
				else {
					if(x.indexOf('{{')!=-1){
						beginBox = true;
						x=x.slice(0, x.indexOf('{{'));
					}else if(x.indexOf('}}')!=-1){
						x = x.slice(0, x.indexOf('}}'));
						r['boxStop']='#'+x;
					}else{
						if(beginBox) {
							r['boxStart']='#'+x;
							beginBox=false;
						}
					}
					var chktmp = checkisinFields("#"+x);
					if( chktmp != null){
						timLength += chktmp;
					 return "#"+x;
				 }
				}
            }
      });
      r["list"]=ss;
      r["length"]=timLength;
   }

}

function pair(t, tok) {
   tok = tok || ':';
   if(t.indexOf(tok)==-1) {
      return {
         fst:t.trim(),
         snd:null
      };
   }else {
      var tt = t.split(tok);
      return {
         fst:tt[0].trim(),
         snd:tt[1]
      };
   }
}

function handler_tim_101_10_03(lines) {
   lines = processInclude(lines);
	lines = lines.slice(1,-1);
	var reX = /X\((\d+)\)/;
	var re9 = /^\d+$/;

	var s="";
	lines.forEach(function(x) { s += x.trim(); });
	var ss = s.split("#");
	ss.shift(); // ignore first one
	ss = ss.map(function(x){
			var z =x.match(reX);
			if(z) {
				var n = parseInt(z[1],10);
				return (new Array(n+1)).join(" ");
			}else {
				if(x.match(re9)) return x;
				else return "#"+x;
			}
	});
	return {
		"type":"tim",
		"name": "_tim",
		"list":ss
	};
}

 function extractBrace(x){
	var r = {},
		i = x.indexOf("{"),
		j = x.indexOf("}");
	if(i!=-1 && j != -1 && i < j) {
		var t = x.slice(i, j+1);
		x = x.replace(t,"");
		t = t.slice(1,-1);
		r = { x:x, t:t }
		return r;
	}else {
		return null;
	}
}

function handler_tom(lines) {
	console.log("handler_tom");
   lines = processInclude(lines);
   appendLst(lines);

	lines = lines.slice(1,-1);
	var s="";

	lines.forEach(function(x) {
		s += x.trim();
	});

	var ff = s.split("^");
	var tomDef = {};
	var tomDeflen = {};
	ff.forEach(function(x){
		var tomLength = 0;
		x = x.trim();
		var m = extractBrace(x);
		if(m!=null) {
			x = m.x;
		}
		if(x.length > 0) {
			var ss = x.split("#");
			var s1 = ss.shift();
			var name = s1.split(/\s*=\s*/)[1];
			var kk = ss.map(function(x) {
					var chktmp = checkisinFields("#"+x);
					if( chktmp != null){
						tomLength += chktmp;
					 return "#"+x;
				 }
				});

			tomDef[name] = kk;
			tomDeflen[name] = tomLength;
			if(m!=null) {
				tomDef[name + "/buddy"] = m.t;
			}
		}
	});

	return {
		"type":"tom",
		"name":"_tom",
		"map":tomDef,
		"maplen":tomDeflen
	};
}
function handler_select(lines) {
	console.log("handler_select");
   lines = processInclude(lines);
   appendLst(lines);
	lines = lines.slice(1,-1);
	var s="";
	lines.forEach(function(x) {
		s += x.trim() + '\n';
	});
	var ff = s.split("^");
	var  outDef = [];
   var o = {};
	ff.forEach(function(x){
		x = x.trim();
		if(x.length > 0) {
			var ss = x.split("#");
			ss.shift();
			var s1 = ss.shift();
			s1 = s1.replace(/\n/g,"");
			//柯  新增  START
			//修復 列印時沒有表頭會有@在最後面的問題
			if(s1.slice(-1) == "%"){
				s1 = s1.slice(0,-1);
				ss[0] = "%"+ss[0];
				console.log("rr% s0:"+ss[0]);
			}
			//END
			var xx = s1.split(/\s*=\s*/);
			//START 柯: 新增移除多的尚未給值之參數 如 {i}  {p}等等
			console.log("old print_parameter:"+xx[1]);
				var START = '{', STOP = '}', i = xx[1].indexOf(START), j = xx[1].indexOf(STOP), print_parameter = xx[1];
					var partemp1,partemp2;
				 while (i != -1 && j != -1){
				 					//console.log("p sice:"+print_parameter.slice(0, i));
				partemp1 = print_parameter.slice(0, i);
				if(print_parameter.length > j +1){
					partemp2 = print_parameter.slice(j +1);
				//console.log("p len:"+print_parameter.length);
				//console.log("i:"+i);
				//console.log("j+1:"+j +1);
			}else{
			partemp2 = "";
			}
   print_parameter = partemp1 + partemp2;
   i = print_parameter.indexOf(START);
   j = print_parameter.indexOf(STOP);
				 	}

       	console.log("new print_parameter:"+print_parameter);
       	xx[1] = print_parameter;

     // END
			 if(xx[1].indexOf('GRID') != -1) {
				o = parseScreen(xx[0],xx[1],x.split('\n'));
			 }
			 else {
				//var kk = ss.map(function(x) { return "#" + x;});
				var hdr="",detail="",subtotal="", total="",tt = ss.join("#");  //tt = # +ss.join("#")
				tt = tt.replace(/\n/g,"");
				//柯  新增 START
				//修復 列印時沒有表頭會有@在最後面的問題
				if(tt.slice(0,1) == "%"){
					tt = "%#"+ tt.slice(1);
				}
					else{
						tt = "#"+tt;
				}
				//END
				console.log("!!tt:"+tt);
				var arr = find(["%"],tt);
				console.log("arr0:"+arr[0]);
				console.log("arr1:"+arr[1]);
				hdr = arr[0];
				if(hdr=='#') hdr="";
				console.log("hdr1:"+hdr);
				if(arr[1]) {
				   arr  = find(["@","+"], arr[1]);
				   detail = arr[0];
				   if(arr[1]) {
					  if(arr[2] == "@") {
						 arr = find(["+"], arr[1]);
						 subtotal = arr[0];
						 total = arr[1];

					  }else {
						 total = arr[1];
					  }
				   }
				}
				o = {
				   "field": "#"+xx[0],
				   "value":xx[1].slice(0,1),
				   "form":xx[1].slice(1,6),
				   "device":xx[1].slice(6),
				   "head":mySplit(hdr),
				   "detail":mySplit(detail),
				   "subtotal":mySplit(subtotal),
				   "total":mySplit(total)
				}
					// parse compound form
						console.log("xx[1]xx[1]xx[1]:"+xx[1]);
				if(xx[1].indexOf(',') != -1) {
					// value,composite pfn,device,mandatory
					var zz = xx[1].split(',');
					o['value'] = zz[0].trim();
					o['device'] = zz[2].trim();
					o['mandatory'] = false;
					if(zz.length >3  && zz[3].trim().toUpperCase() =='M') {
						o['mandatory'] = true;
					}
					//Eric提:只有查詢類才看此設定，更新類都是要重印
					//#PRTSCRFG=1,UPD01,NE,,D
					if(zz.length >4 ) {
					 if(zz[4].trim().toUpperCase() =='D') {
						o['dupable'] = true;
					 }else{
						o['dupable'] = false;
					 }
					}
					//#PRTSCRFG=1,UPD01,NE,,D,?   //柯 新增一行塞入字數?
					if(zz.length >5 ) {
						o['printcpi'] = zz[5];
					}else{
						o['printcpi'] ="";
						}
					//#PRTSCRFG=1,UPD01,NE,,D,?,?   //柯 行距?
					if(zz.length >6 ) {
						o['printlpi'] = zz[6];
					}else{
						o['printlpi'] = "";
						}
					//#PRTSCRFG=1,UPD01,NE,,D,1,1,?????   //柯 新增給印表時排序
					if(zz.length >7 ) {
						o['sort'] = zz[7];
					}else{
						o['sort'] = "";
					}
					//柯 新增給Windows 印表機的列印紙張尺寸
					if(zz.length >8 ) {
						o['papersize'] = zz[8];
					}else{
						o['papersize'] = "";
					}
					 //柯 新增給Windows 設定列印紙張的頁邊空白寬度
					if(zz.length >9 ) {
						o['papermargin'] = zz[9];
					}else{
						o['papermargin'] = "";
					}
					if(zz.length >10 ) {
						o['pdfform'] = zz[10];
					}else{
						o['pdfform'] = "";
					}
					if(zz.length >11 && zz[11].trim() =='1') {
						o['landscape'] = true;
					}else{
						o['landscape'] = false;
					}
					if(zz.length >12 ) {
						o['edit'] = zz[12];
					}else{
						o['edit'] = "";
					}
					if(zz.length >13 ) {
						o['editAll'] = zz[13];
					}else{
						o['editAll'] = "";
					}
					if(zz.length > 14)
					  o['printNo'] = zz[14];
					else
						o['printNo'] = "";
					if(zz.length > 15)
					  o['titleName'] = zz[15];
					else
						o['titleName'] = "";
					if(zz.length > 16)
					  o['execProg'] = zz[16];
					else
						o['execProg'] = "";					

					o['form'] = zz[1].trim();
					if(zz[1].indexOf('+')!=-1){
						o['form'] = [];
						var ww = zz[1].split('+');
						ww.forEach(function(w1) {
							o['form'].push(w1.trim());
						});
					}
				}
			} // end else
			outDef.push(o);
		} // end forEach
	});
	return {
		"type":"select",
		"name": "_select",
		"list":outDef
	}


	function mySplit(s) {
		//ke:新增for不一定都有最後的total
		if(s == null){
			return null;
		}
		s = s.trim();
		if(!s) return []
		var arr = s.split("#");
		arr.shift();
		return arr.map(function(x) { return "#"+x;});

	}

}
function parseScreen(fld, attr, lines) {
   var o;
   var fields = [];
   var ss = attr.split(',');
   var gridHeader = null;
   var gridFooter = null;
   lines = parseBlk('header',lines,parseHeader);
   lines.shift();
   var i =0;

   /*
   while(true) {
		var cfgLine = lines.shift();
		cfgLine = cfgLine.trim();
		if(cfgLine[0]=='#') break;
		i++;
		if(i>1000) {
			console.error('no GRID config found');
			throw new Error('no GRID config found');
		}

   }
   cfgLine = cfgLine.slice(cfgLine.indexOf('=') + 1);
   var grid_cfg = eval('(' + cfgLine + ')');
   */
   var t,bStartCfg=false, cfgLines=[];
   while(true) {
		t = lines.shift();
		if(t[0]=='#') {
			bStartCfg = true;
			cfgLines.push(t.slice(t.indexOf('=') + 1));
		}else{
			cfgLines.push(t);
		}
		if(isEndOFCfg()){
			break;
		}
   }
   var cfgLine = cfgLines.join('');
   console.log("cfgLine:"+cfgLine);
    var grid_cfg = eval('(' + cfgLine + ')');
   function isEndOFCfg(){
		var tt = cfgLines.join('');
		var left = count('{');
		var right = count('}');
		if(left==right) return true;
		else return false;

		function count(c) {
			var n=0;
			for(var i=0;i< tt.length; i++)
				if(tt[i]==c) n++;
			return n;
		}
   }


   var i=0; // line number
   while(true) {
		var o = getField();
		if(o) {
			fields.push(o);
		}
		else break;
	}

   return {
      'field': '#'+ fld,
      'value': ss[0],
      'device':ss[1],
      'print':ss[2] || '',
      'config':grid_cfg,
      'fields':fields,
      'embeded':true,
	  'header': gridHeader,
	  'footer':gridFooter
   };
	function parseHeader(lines){
		var props = getAttributes(lines.shift(), ["type","layout"]);
		console.log(props);
		lines.pop(); // remove final line
		console.log(lines.join('\n'));
		var t = lines.join("");
		t = t.replace(/[\t\r\n]/g,"");
		var  list = eval('(' + t + ')');

		var o = {
			'name': 'query-header',
			'def': {
				'list':list
			}
		};
		gridHeader = {
			display: [o]

		}
	}
	function makeJson(){
		var result=[];
		for(var i=0;i<lines.length;i++) {
			var s = lines[i].trim();
			result.push(s);
			if(s == "]")  {
				lines = lines.slice(i+1);
				var t = result.join("");
				t = t.replace(/[\t\r\n]/g,"");
				var o = eval('(' + t + ')');
				console.log(" return o " + o);
				return o;
			}
		}

	}
	function parseBlk(tag, lines, fn) {
		var i,j;
		var bTag = '<' + tag + ' ';
		var eTag = '</' + tag + '>';
		var s = lines.join('\n');
		console.log(lines.length);
		console.log('btag:'+bTag);
		console.log(eTag);

		i = s.indexOf(bTag);
		if(i==-1) {
			console.log('no ' + bTag);
			return lines;
		}
		j = s.indexOf(eTag);
		if(j==-1) {
			console.log('no ' + eTag);
			return lines;
		}

		j += eTag.length;

		var block = simpleReformat(s.slice(i,j));
		console.log('block:'+block);
		var t = s.slice(0,i-1) +s.slice(j);
		if(fn) {
			fn(block.split('\n'));
		}

		return t.split('\n');

	}
	function  simpleReformat(s){
		// JSON styles
		if(s.indexOf('"#')!=-1) return s;

		// quote #VAR
		var re = /(#[a-zA-Z0-9+#_]*)/g
		return s.replace(re,function(i,j,k) {
			return '"' + j + '"';
		});
	}
   function getField() {

   	function pushBack() { i--; }

      var ss = [];
      var s1 = getLine();
      if(s1==null) return null;
      ss.push(s1);
      while(true) {
         t = getLine();
         if(t==null ||t.startsWith("#"))  { // new field
            pushBack();
            var fld = parseDef(ss);
            return fld;
         }else {
            ss.push(t);
         }

      }

      function parseDef(ss) {
         var aa = ss[0].split('='),
            name=aa[0].trim(),  //柯:加入去空白
            caption = aa[1];
            opt = eval('(' + ss[1] + ')');
         return {
            name:name,
            caption:caption,
            opt:opt
         };
      }
      function getLine() {
         while(true) {
            var s = lines[i++];
            if(s==null) return null;
            if(!isComment(s) && s.trim().length > 0) return s;
         }
      }

   }
}


function find(tokens, s) {
	var arr = [];

	for(var k=0; k < tokens.length; k++) {
		token = tokens[k];
		var i = s.indexOf(token);
		if(i!= -1) {
			arr.push(s.slice(0,i));
			arr.push(s.slice(i+1	));
			arr.push(token);
			return arr;
		}
	}
	arr.push(s);
	arr.push(null);
	return arr;
}



function processAppFields(cfg, lines) {
	_cfg = cfg;
	return handler_rtn(lines);
}
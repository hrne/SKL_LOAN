var file = require('./file');
exports.superclude = superclude;


function superclude(cfg, txcode) {
	var result = "";
	_cfg = cfg;
	console.log("sup parse :"+ txcode);
	var fileName = _cfg.getSource(txcode);
	console.log(" filename " + fileName  + "\n");
	var lines = file.readLines(fileName);
	
	console.log("sup length " + lines.length);
	result = processSupclude(lines);
	return result;
}


function processSupclude(lines) {
	var tag = /^\s*<supclude\s+/i,
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
					console.error("supclude with src"+ s);
					throw "supclude error:" + s;
				}
				
				var map = toMap(getAttrib(s,"map"));
				console.log("found " + src);
               if(src.indexOf("::") == -1) {
                  texts = file.readLines(_cfg.getInclude(src));
               }else {
                  var ss = src.split('::');
                  texts = readSupcludeBlock(ss[0],  file.readLines(_cfg.getInclude(ss[0])).join("\n"), ss[1]);
                  
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
	return result.join("\n");
   
   function readSupcludeBlock(src, content, name) {
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
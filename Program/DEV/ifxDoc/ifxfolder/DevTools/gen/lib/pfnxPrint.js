(function(exports){
    exports.test = function(){
        return  'hello world'
    };
    var source;
    exports.setSource = function(src) {
        source = src;
    };
    function getPrtValue(fld) {
        if(source==null) return fld.slice(1);
        return source.getPrintValue(fld);
    }
    function mySlice(value, begin, end) {
        if(source==null) {
            if(end==undefined) return value.slice(begin);
            else return value.slice(begin, end);
        }else {
            // call IfxUtl.substring_Big5
            return value;
        }
    }
    exports.printTop = function(doc) {
        var result=[],
            topParts = ["face"];
           
        topParts.forEach(function(p) {
            console.log("process:"+p);
            result = result.concat(printPart(doc[p]));
        });
        
        return result;
    }
    function printPart(fmt) {
        console.log(JSON.stringify(fmt,null,4));
        var result = [],
            line;
        //console.log("lines:"+ fmt.length);   
        fmt.forEach(function(row){
            line=[];
          //  console.log("cells:"+row.length);
            row.forEach(function(cell){
                console.dir(cell);
                line.push(printCell(cell));
            });
            result.push(line);
        });
        return result;
    }
    function printCell(cell) {
        console.log(cell['text']);
        var ss = cell['text'].split("}}"),
            result={ width:cell.width,
                    align:cell.align,
                    buf:[]
                    };
            
        ss.forEach(function(s){
            var tt= s.split("{{");
            if(tt[0].length>0)
                result.buf.push(tt[0]);
            if(tt[1]!=null) {
                result.buf.push(expandMacro(tt[1]));
            }
        });
        result["text"] = result.buf.join("+");
        return result;
    }
    function expandMacro(fnstr) {
        var i = fnstr.indexOf(","),
            fld,
            value;
        if(i==-1) {
            value = getPrtValue(fnstr); // field name only
            return "put value:" +value;
        }
        fld =fnstr.slice(0,i),
        value = getPrtValue(fld)
        fnstr = fnstr.slice(i+1);
        
        return runmacro(value, fnstr);    
        //return "calling fn with value:"+ value +"," + fnstr;
    
    }
    function runmacro(value, fnstr) {
        if(fnstr.charAt(0)=='"') { // edit pattern
            return format(value, fnstr.slice(1, fnstr.length-1));
        }else {
            var ss = fnstr.split(',');
            if(ss.length==1) return mySlice(value,ss[0]);
            else return mySlice(value, ss[0], ss[1]);
        }
        return "calling fn with value:"+ value +"," + fnstr;
    }
    //function printItem(item, text) {
     //   return text + "(" + item.width + ")";
    //}
    
    function format(text, template){
        var eatOne = poundSignEater(text),
            replaceCh,
            result="";
        for (var i = 0; i < template.length; i++) {
            var ch = template.charAt(i);
            if (ch != "#") {
                result += ch;
            } else {
                replaceCh = eatOne();
                result += replaceCh;
                if (strlen("" + replaceCh) > 1)
                    i++;
            }
        }
        console.log(result);
        return result;
    }
    function poundSignEater(s) {
        var i = 0;
        return function() {
            return s.charAt(i++);
        };
    }
    function strlen(s) {
        var arr = s.match(/[^\x00-\xff]/ig);
        return arr == null ? s.length : s.length + arr.length;
    }
})(typeof  exports === 'undefined'? this['pfnxPrint']={}: exports);
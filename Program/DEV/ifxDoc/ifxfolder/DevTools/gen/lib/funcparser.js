
var _typeParam = {
	typeVar:"VAR",
	typeSysvar:"SysVar",
	typeConst:"Const",
	typeFunc:"Func",
	typeFuncList:"Func List",
	typeAction:"action(O/s/S/$)"
};
var is_array = function (value) {
    return Object.prototype.toString.apply(value) === '[object Array]';
};
 
function FuncParser(f,level)
{
	//this.funcstr = f;
	console.log("789 " + f);
	console.log("789 " + level);
	this.params = new Array();
	var that = this;
	this.funcname = function() {
		var pos = (this.funcstr).indexOf("(");
		return (this.funcstr).substr(0,pos);
	}
	function getParams(s) 
	{
		console.log("APANCD" + s);
		var ch;
		var aParam="";
		var lp=0;
		var rp = 0;
		var result = [];
		var pos = s.indexOf("(");	
			
		for(var i=pos+1; i< s.length; i++) {
			ch = s.charAt(i);
			switch(ch) {
				case "(":
					lp++;
					aParam += ch;
					break;
				case ")":
					rp++;
					if (rp > lp) { // end of func
						result.push(aParam);
					}
					aParam += ch;
					break;
				case ",":
					if (lp==0) {
						result.push(aParam);
						aParam="";
					}
					else {
						if(lp == rp) {
							lp = rp = 0;
							result.push(aParam);
							aParam="";
						}
						else {
							aParam += ch;
						}
					}
					break;
				default:
					aParam += ch;
			}
		}
		console.log("APANCD" + result);
		return result;
	}
	function init(level) {
		console.log("123");
		var ss = getParams(f);
		console.log(ss);
		that.params = ss.map(function(x) {
			return new Param(x, level);
		});
	}
	console.log("456");
	init(level);
}

function Param(s,level)
{
	/*this.typeVar = 1;
	this.typeSysvar =2;
	this.typeConst = 3;
	this.typeFunc = 4;
	this.typeFuncList = 5;
	*/
	
	var fPos = s.indexOf("(");
	if(fPos == -1) {
		this.value = s;
		if(s.length==1 && "IOHSs$".indexOf(s)>=0) {
			this.type = _typeParam.typeAction;
		}
		else if(s.charAt(0) == "#") { this.type= _typeParam.typeVar; }
		else if(s.charAt(s.length-1) == "$") { 
			this.type = _typeParam.typeSysvar; 
//			this.value = "_" +this.value.replace(/\$/,"");
		}
		else this.type = _typeParam.typeConst;
	}
	else {
		this.text = s;
		this.type = _typeParam.typeFunc;
		//console.log(this.type);
		this.functor = new Func(s, level + 1);
		if(is_array(this.functor))// instanceof Array) 
		{
			this.type = _typeParam.typeFuncList;
		}
	}
	this.toString = function toString()
	{
		var s="";
		for(var j=0; j<=this.level; j++) s += "\t";
		s += this.value;
		if (this.type == _typeParam.typeFunc) {
			s += " func\n";
			for(var j=0; j<=this.level; j++) s += "\t";
			s += (this.functor).toString();
		}
		
		if(this.type == _typeParam.typeFuncList) {
			s+="\n";
			for(var k=0; k < this.functor.length; k++) {
				var p = this.functor[k];
				s += p.toString();
			}
		}
		return s;
	}	
	
}


function Func(f, level)
{
	//console.log("Func() constructor(), level:" + level +", func text:"+ f);
	console.log("AAAAAAAAAAAAAAAAA2");
	console.log(f);
	var funcs = sepFunc(f);
	if(funcs.length > 1) {
		return parseFuncString(f,level+1);
	}
	
	var myfunc = new FuncParser(f,level);
	
	/*
	myfunc.getParams();
	
	for(var i=0; i < myfunc.params.length; i++) {
		myfunc.params[i] = new Param(myfunc.params[i], level);
	}
	*/
	this.text = f;
	//this.body = myfunc;
	this.params = myfunc.params;
	this.level = level;
	
	
	var pos = (this.text).indexOf("(");
	this.name= (this.text).slice(0,pos);
	//this.funcname = myfunc.funcname();
	
	
	this.toString = function toString()
	{
		var s="";
		s += "func name:" + this.name + "\n";
		for(var i=0; i < body.params.length; i++) {
			var p = body.params[i];
			for(var j=0; j<this.level; j++) s += "\t";
			s += "arg " + i + ":"+ p.toString() + "\n";
		}
		return s;
	}
}


function sepFunc(s)
{
	var ch;
	var lp=0;
	var rp = 0;
	var f="";
	var arr= [];

	for(var i=0; i < s.length; i++) {
		var ch = s.charAt(i);
		f += ch;
		switch(ch) {
			case "(":
					lp++;
					break;
			case ")":
					rp++;
					break;
		}
		if(lp == rp && lp > 0) {
			arr.push(f);
			f="";
			lp=rp=0;
		}
	}
	console.log("APANN " + arr);
	return arr;
}
exports.show = function(s) {
	var f = parseFuncString(s,0);
	dumpArr(f);
	return f;
}
exports.parseFunc =parseFuncString
function parseFuncString(s,level)
{
	if(level==null) level=0;
	var funcArray = []
	try {
		console.log("AAAAAAAAAAAAAAAAA1" + s);
		var arr = sepFunc(s);
		console.log(arr);
		//console.log(arr.join("\n"));
		arr.forEach(function(x) {
			funcArray.push(new Func(x,level));
		});
	}catch(ee) {}
	for(var i = 0; i < funcArray.length;i++){
		console.log("111111111111111111111111111111");
		console.log(funcArray[i]);
	}

	return funcArray;
}
exports.dump = dumpArr;
function dumpArr(farr)
{
	for(var i=0; i < farr.length; i++)
	{
		if(farr[i] instanceof Array) console.log(i + " is array");
		else if (farr[i] instanceof Func) {
	//			dumpfunc("" , farr[i]);
			console.log(JSON.stringify(farr[i],null, 4));
				console.log("----------------------------------------------------");
		}
	}
}

function dumpfunc(tabs,f)
{
	console.log(tabs + "original:" + f.text);
	console.log(tabs +"" + f.name + ", params :"+f.params.length);
	
	for(var i=0; i < f.params.length; i++) {
		var o = f.params[i] ;
		if(o instanceof Param){ 
			if(o.type!=_typeParam.typeFunc  && o.type !=_typeParam.typeFuncList) {
				console.log(tabs + "Param "+ i + " type:" + o.type + " value: " + o); 
			}
			if(o.type==_typeParam.typeFunc) {
				console.log(tabs + "Param " +i + " type:" + o.type + " Func " ); 
				var g = o.func;
				
				dumpfunc(tabs+"\t", g);
			}
			else if(o.type==_typeParam.typeFuncList) {
				console.log(tabs + "Param "+ i + " type:" + o.type + " Func list" ); 
				
				for(var j=0; j < o.func.length; j++) {
					console.log(tabs + " list " + j);
					dumpfunc(tabs + "\t" , o.func[j]);
				}
			}
			
		}
		else { console.log(tabs +"param " + i + " unknown");}
	
	}
}
/*
var fArr = parseFuncString("C(4,#GUNO,V(5,13,84),V(5,0,84)V(6,1,12))A(6,1,12,C(4,#GUNO,V(5,13,84),Very(5,0,84)V(6,1,12)V(6,2,V(5,13,84)V(5,0,84)V(6,1,12))))",0);
fArr = parseFuncString("C(4,#GUNO,V(5,13,84),V(5,0,84)V(6,1,12))A(6,1,12,C(4,#GUNO,V(5,13,84),Very(5,0,84)V(6,1,12)V(6,2,V(5,13,84)V(5,0,84)V(6,1,12))))",0);
//dumpArr(fArr);
function console.log(s) { WScript.Echo(s); }
*/
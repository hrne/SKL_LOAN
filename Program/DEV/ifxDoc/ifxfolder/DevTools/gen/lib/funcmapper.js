
var _typeParam = {
	typeVar:"VAR",
	typeSysvar:"SysVar",
	typeConst:"Const",
	typeFunc:"Func",
	typeFuncList:"Func List",
	typeAction:"action(O/s/S/$)"
};
var NS = "x";
var METHOD_NS = NS+ "."; // check processS
function getMethodName(n) { return METHOD_NS + n;}

var FMAP ={
	"A":generic_writer,
	"ALERT":generic_writer,
	"ASGN":generic_writer,	
	"B":generic_writer,
	"C":c_writer,
	"K":k_writer,
  "CALL":generic_writer,
	"CALL2":generic_writer,
	"CALL3":generic_writer, //��call�h���ϥ�
	"INVOKEJS":generic_writer,
	"JS":generic_writer, // shortname for INVOKEJS
	"D":generic_writer,
	"E":e_writer,
	"EXIT":generic_writer,
	"G":generic_writer,
	"CHKHELP":generic_writer,
	"HELP":generic_writer,
	"HELP_AC":generic_writer,
	"I":generic_writer,
	"L":generic_writer,
	"M":generic_writer,
	"P":generic_writer,
	"skip":generic_writer,
	"R":generic_writer,
	"S":generic_writer,
	"ES":generic_writer,
	"RESET_RIM":generic_writer,
	"STORE_RIM":generic_writer,
	"T":generic_writer,
	"V":generic_writer,
	"X":generic_writer,
	"IF":if_writer2,
  "ELIF":if_writer2,
	"SWITCH":switch_writer,
	"IS":is_writer,
	"SHOW":generic_writer,
  "BIND":generic_writer,
	"HIDE":generic_writer,
	"ENABLE":generic_writer,
	"DISABLE":generic_writer,
	"RUN":generic_writer,
	"NO_RUN":generic_writer,
	
// �� �W�[ SKIPLN �\��,�i���ܼƱ����J�Y���׫�۰ʸ��U�@�����,�ӫD���� 	
	"SKIPLN":generic_writer,
//end	
	"STATUS":generic_writer,
  "TOOLTIP":generic_writer,
	"TRIGGER":generic_writer,
  "XMT":generic_writer,
  "PDF":generic_writer,
}

exports.write = write
function write(fld) {
	console.log("*mapping function for " + fld.name);
	if(fld.pref) {
		console.log("mapping pref....");
		var s = mapping(fld.name, fld.pref);
      
		fld.pref_body = s;
		console.log("pref body:"+fld.pref_body);
	}	
	if(fld.post) {
		//console.log("mapping post....");
		var s = mapping(fld.name, fld.post);
		fld.post_body = s;
		//console.log("post body:"+fld.post_body);
	}
}	
function mapping(fldname, fobj) {
	//console.log(JSON.stringify(fobj));
	var result = [];
	console.log(fobj.length);
	for(var i=0; i < fobj.length; i++) {
		var x = fobj[i];
      //x.name = x.name.trim();
		if(x.name=="S") {
			result.push(processS(fldname, fobj, i));
			break;
		}else {
			result.push(rtn_handle(fldname, x));
		}
	}
	
	
	result = result.map(function(x) { return x + ";";});
	//return result.join("");
   if(result.length==0) {
		var error = "[ERROR] rtn mapping for Field:"+fldname;
		console.error(error);
		throw error;
   }
	return ">>>>>function(x){" + result.join("") + "}<<<<<";
	
}
function processS(fldname, fobj, offset) {
//console.log("============================");
//console.log("processS:"+ fldname);
	var i =0;
	while(i++ < offset) fobj.shift();
	var sFun  = fobj.shift();
	
	var result = [];
	fobj.forEach(function(x) {
		result.push(rtn_handle(fldname, x));
	});
	var s;
	s = "function(" + NS + ") { " + result.join(";") + "}";
	var pp = sFun.params;
	
	var args = [quote(fldname)]; //field name
	pp.forEach(function(x) {
		args.push( quote(x.value));
	});
	if(s) args.push(s);
	
	var s= makeMethod(sFun.name, args);
	//console.log(s);
	//console.log("============================");
	return s;
}
function rtn_handle(fldname, f) {
//console.log("rtn function name:"+f.name);
var error ="[ERROR] Function mapper not found or mapping error, fldname:"+fldname + ", function name:"+ f.name;
try {
	var r = FMAP[f.name.trim()];
	if(!r) {
		console.error(error);
		throw error;
	}
	return r(fldname, f);
}catch(ee){
	console.error(error);

	throw new Error(ee);
}
}


// templage
function w_getFldValue(name) {
	var s = "x.getFldValue('{name}')";
	s =s.replace("{name}",name);
	//console.log(s);
	return s;
}
function w_setFldValue(name, value) {
	var s = "x.setFldValue('{name}', {value})";
	return s.replace("{name}", name).replace("{value}",value);
}
function quote(n) { return "'" + n + "'";}
function getTab(level) {
	var arr = new Array(3 * (level+1));
	return arr.join(" ");
}
// end of templage

var is_array = function (value) {
    return Object.prototype.toString.apply(value) === '[object Array]';
};
 
function makeMethod(name, aa) {
	var s =getMethodName(name) + "(";
	if(is_array(aa)) {	
		s += aa.join(",");
	}else {
		s += aa;
	}
	s += ")";
	return s;
}
function makeReturnMethod(name, aa) {
	var s ="return "+ getMethodName(name) + "(";
	if(is_array(aa)) {	
		s += aa.join(",");
	}else {
		s += aa;
	}
	s += ");";
	return s;
}


function generic_writer2(fldname, fobj) {
	//console.log("generic writer for field:[" + fldname + "], function name:"+ fobj.name);
	var pp = fobj.params;
	
	var args = [quote(fldname)]; //field name
	pp.forEach(function(x) {
		if(x.type == _typeParam.typeFuncList) {
			generic_writer(fldname, x.functor);
		}else {
			args.push( quote(x.value));
		}
	});
	var s= makeMethod(fobj.name, args);
	//console.log(s);
	return s;
}

function e_writer(fldname, fobj) {
	//console.log("E writer for field:[" + fldname + "], function name:"+ fobj.name);
	var pp = fobj.params;
	
	var args = [quote(fldname)]; //field name
	pp.forEach(function(x) {
		args.push( quote(x.value || x.text));
	});
	var s= makeMethod(fobj.name, args);
	//console.log(s);
	return s;
}


function generic_writer(fldname, fobj) {
	//console.log("generic writer for field:[" + fldname + "], function name:"+ fobj.name);
	var pp = fobj.params;
	
	var args = [quote(fldname)]; //field name
	pp.forEach(function(x) {
		args.push( quote(x.value));
	});
	var s= makeMethod(fobj.name, args);
	//console.log(s);
	return s;
}

function is_writer(fldname, fobj, opt) {
	var pp = fobj.params;
	
	var args = [quote(fldname)]; //field name
	pp.forEach(function(x) {
		args.push( quote(x.value || x.text));
	});
	var s= makeMethod(fobj.name, args);
	//console.log(s);
	return s;
}



//client E function 
//>s='aa*2+3'
//> s.split(/[+\-*/]+/)
//[ 'aa', '2', '3' ]
//
// E(n,[@NNN],#NN1+#NN2-...)   
// 
// var p1 = "#NN1+#NN2-#NN3".replace("+"," + ").replace("-"," - ").replace("*", " * ").....
// var pp = p1..split(/[+\-*/]+/)
// pp.forEach(function(x) {
//	if(x is var name) p1.replace(x, get var value);
//	else if x is const // do nothing or convert x to Numeric
//	var value = eval(p1);
//	set value
 //});
 //
 


var CMAP = {
	1:cx_writer,
	2:cx_writer,
	3:cx_writer,
	4:cx_writer,
	5:cx_writer,	
	6:cx_writer,
	7:cx_writer,
	8:cx_writer	
}
function c_writer(fldname, fobj) {
	var opt = fobj.params[0].value;
	//console.log("opt:"+opt);
	var rtn = CMAP[opt];
	if(rtn) return rtn(fldname, fobj, opt);
	else {
		var error ="[ERROR] C" + opt + " writer not found";
		console.error(error);
		throw error;
	}
}
var KMAP = {
	Z:kx_writer,ZA:generic_writer,NOBUTTON:generic_writer,MODIFYBUTTON:generic_writer,
	GOFLD:generic_writer,PSBACC:generic_writer,
	PSBACCR:generic_writer,
	SPECIAL:generic_writer,CLOSESHOW:generic_writer,
	NEXTNOCLOSE:generic_writer,LIGHT:generic_writer,
	RSWIFTOVER:generic_writer
	
}
function k_writer(fldname, fobj) {
	var opt = fobj.params[0].value;
	//console.log("opt:"+opt);
	var rtn = KMAP[opt];
	if(rtn) return rtn(fldname, fobj, opt);
	else {
		var error ="[ERROR] K" + opt + " writer not found";
		console.error(error);
		throw error;
	}
}
function switch_writer(fldname, fobj) {
	var method = fobj.name.toUpperCase();//switch";;
	var args = [quote(fldname)]; // every client function got current fieldname
	var pp = fobj.params	
 
   
	args.push(quote(pp.shift().value)); //condition
	var i=0;
	pp.forEach(function(x) {  // for case0,FN0,case1,FN1.....
		if((i%2==0 && i!=pp.length-1) && x.type==_typeParam.typeAction)   //ke add[&& i!=pp.length-1] for the last one maybe s,S,$
			x.type = _typeParam.typeConst;
		var rtnBody  = ""+handleParam(fldname, x);
		args.push(createClosure(rtnBody));
		i++;
	});
	return makeMethod(method, args);
}
function if_writer(fldname, fobj) {
	var method = fobj.name.toUpperCase();//switch";;
	var args = [quote(fldname)]; // every client function got current fieldname
	var pp = fobj.params	
 
   
	args.push(quote(pp.shift().value)); //condition
	pp.forEach(function(x) {  // for FN0,FN1,.....
		var rtnBody  = ""+handleParam(fldname, x);
		args.push(createClosure(rtnBody));
	});
	return makeMethod(method, args);
}

function if_writer2(fldname, fobj) {
console.log("\n\n");
	var method = fobj.name.toUpperCase();//IF";;
	var args = [];//[quote(fldname)]; // every client function got current fieldname
	var pp = fobj.params	
 	
   var myTest = getMethodName("myTest");
   args.push("if(" + myTest + "(" +quote(pp.shift().value) + ")) {");
   
 
 
  	var rtnBody  = ""+handleParam(fldname, pp[0]);
   args.push(rtnBody);
   args.push("}else {");
    rtnBody  = ""+handleParam(fldname, pp[1]);
    args.push(rtnBody);
    args.push("}");
	//return makeMethod(method, args);
   
   console.log(args.join("\n"));
   return args.join('');
}
/*
> var f1 = function() { console.log("1");}
> var f2 = function() { console.=log("2');}
...
> var f2 = function() { console.=log("2:);}
...
> var f2 = function() { console.=log("2");}
...
> var f2 = function() { console.log("2");}
> var ff = function(i,fn1, fn2) {
... if(i>0) fn1();
... else fn2();
... }
> ff(1,f1,f2)
1
> ff(-1,f1,f2)
*/

/*
V,A function, �ˮ֥��Ѯ� throw exception, �Y�Sthrow exception�h����U���

*/

//C(1,#NNN,FN1,FN2,FN3)
//C(2,#NNN, FN0,FN1,FN2,FN3,....)
function cx_writer(fldname, fobj,opt) {
	var method = "C" + opt;
	var args = [quote(fldname)]; // every client function got current fieldname
	var pp = fobj.params	
	pp.shift(); // skip 2;
	//�_:�S�����B�z���T�����n���@���q�ӳB�z!!
	if(opt == 8){
		args.push(quote(pp.shift().value)); //#NNN
	}
	args.push(quote(pp.shift().value)); //#NNN
	pp.forEach(function(x) {  // for FN0,FN1,.....
		var rtnBody  = ""+handleParam(fldname, x);
		args.push(createClosure(rtnBody));
	});
	return makeMethod(method, args);
}
function kx_writer(fldname, fobj,opt) {
	var method = "K";
	var args = [quote(fldname)]; // every client function got current fieldname
	var pp = fobj.params	
	//pp.shift(); // skip 2;
	args.push(quote(pp.shift().value)); //#NNN
	pp.forEach(function(x) {  // for FN0,FN1,.....
		var rtnBody  = ""+handleParam(fldname, x);
		args.push(createClosure(rtnBody));
	});
	return makeMethod(method, args);
}

function createClosure(fnBody) {
	var result = [];
	result.push("function() {");
	var ss = fnBody.split("\n");
	ss.forEach(function(x) {
		result.push("" + x); // \t
	});
	result.push("}");
	return result.join("");  // \n
}
function handleParam(fldname, p) {
	//console.log("type:"+p.type);

	if(p.type==_typeParam.typeAction) return createActor(fldname, p.value)
	else if(p.type==_typeParam.typeFunc) return createFunctor(fldname, p.functor)
	else if(p.type==_typeParam.typeFuncList) {
		return createFunctorList(fldname, p.functor);
	}else {
		var s =  makeReturnMethod("gigo", quote(p.value));
		//console.log("s:"+s);
		return s;
	}
}
function createFunctorList(fldname, list) {
	var result = [];
	list.forEach(function(x) {
		result.push(createFunctor(fldname, x));
	});
	var s =  result.join(""); // \n
	//console.log("ss:"+s);
	return s;
}
function createActor(fldname, value) {
	return makeMethod("skip", [quote(fldname), quote(value)]) + ";";
}
function createFunctor(fldname, fobj) {
	var s = rtn_handle(fldname, fobj);	
	s += ";";
	//console.log("functor:"+s);
	return s;
}

 
 
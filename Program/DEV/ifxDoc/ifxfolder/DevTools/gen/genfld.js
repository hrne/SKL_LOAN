var file = require('./lib/file'); 
var path = require("path");
var parser = require("./lib/parser");
var helper = require("./lib/helper");
var config = require("./config");

helper.init();	
var cfg = config.get();

var arguments = process.argv.splice(2); 
var fileName = arguments[0];

var tranType = fileName.substring(0,1);
var source = cfg.getSharedSource(fileName);
var target = cfg.getSharedTarget(fileName);
console.log("source:"+source);
console.log("target:"+target);
var lines = file.readLines(source);
lines.forEach(function(t,i){
	console.log("line " + i + ":" + t);
});	

var openingTag= "<sub name='g' type='share'>"
var  closingTag = "</sub>";
lines.unshift(openingTag);
//lines.push(closingTag);
var tt = parser.processAppFields(config.get(), lines);
tt = fixIt(tt);
var ss = tt.split("\n");
	var result=[];
ss.forEach(function(x) {
	result.push(x);
	console.log(x);
});




function fixIt(r) {
	var tt = ""+ JSON.stringify(r,null,4);
	var ss = tt.split("\n");
	var result=[];
	ss.forEach(function(x) {
		x = x.replace("\">>>>>","");
		x = x.replace("<<<<<\"","");
		result.push(x);
	});
	var content = "var getShareFields = function() { return " + result.join("\n") +";};\n";
//	console.log(content);
	return content;
}

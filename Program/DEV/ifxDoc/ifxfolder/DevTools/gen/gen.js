var file = require('./lib/file'); 
var path = require("path");
var parser = require("./lib/parse");
var helper = require("./lib/helper");
var config = require("./config");

helper.init();	
var cfg = config.get();

var arguments = process.argv.splice(2); 
var txcode = arguments[0].toUpperCase();
txcode = txcode.toUpperCase();
var fileName = cfg.getSource(txcode);
console.log(fileName);

var lines = file.readLines(fileName);
/*lines.forEach(function(t,i){
	console.log("line " + i + ":" + t);
});	
*/

var output = parser.process(lines);
var tt = ""+ JSON.stringify(output,null,4);

var ss = tt.split("\n");
var result=[];
ss.forEach(function(x) {
	x = x.replace("\">>>>>","");
	x = x.replace("<<<<<\"","");
	result.push(x);
});


var content = "var getTranDef = function() { return " + result.join("\n") +";\n}\n";
var f = path.basename(fileName, path.extname(fileName)).toLowerCase();

mkdir_p(cfg.getTarget(txcode));
file.writeLine(cfg.getTarget(txcode)+f+".js", content);
//file.writeLine(fileName+".txt", JSON.stringify(output));

var rr = [];
output.select.forEach(function(x){
	rr.push(x.form);
});

file.writeLine(cfg.getTarget(txcode)+f+".pfns", rr.join("\n"));




function mkdir_p(dir){
   var fs=require('fs');
   var path=require('path');
   if(!path.existsSync(dir)){
     mkdir_p(path.dirname(dir));
     fs.mkdirSync(dir,0777);
   }
}
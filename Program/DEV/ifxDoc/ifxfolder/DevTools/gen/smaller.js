var fs = require('fs'), file = require('./lib/file');

// files locations
console.log("target:" + mtTargetFolder);

var arguments = process.argv.splice(2), mtTargetFolder = arguments[0], mtdTargetFolder = arguments[1];
main(mtTargetFolder);

function deepCopy(o) {
		return JSON.parse(o);
}

function readContent(callback) {
    fs.readFile(mtTargetFolder, function (err, content) {
    	console.log("in readFile ");
        if (err) return callback(err);
       return callback(null, content);
    })
}



function main(mtTargetFolder) {
	console.log(mtTargetFolder);
	var temp;
	readContent(function (err, content) {
    temp = content;
    smallerdata(mtdTargetFolder,temp);
 
})
 
}
function smallerdata(filePath,content) {
   
	var compressor = require('yuicompressor');
	
	var uglifyMini = function(){
		var jsp = require("./uglify-js").parser;
		var pro = require("./uglify-js").uglify;
		content = content.toString();
		var ast = jsp.parse(content); // parse code and get the initial AST
		ast = pro.ast_mangle(ast); // get a new AST with mangled names
		ast = pro.ast_squeeze(ast); // get an AST with compression optimizations
		var final_code = pro.gen_code(ast, {beautify:false}); // compressed code here
		console.log('uglifyMini done');
		file.writeLine(filePath, final_code);
		miniDone = true;
		console.log("all files written");
		console.log('bye3');
	}
	uglifyMini();


}
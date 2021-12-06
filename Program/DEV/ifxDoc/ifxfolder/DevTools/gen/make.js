var helper = require("./lib/helper"),
	config = require("./config"),
	parser = require("./lib/parser"),
	supinclude = require("./lib/supinclude"),
	file = require('./lib/file'),
	xref = require('./lib/xref'),
	fs = require('fs');

var allDone = true;
helper.init();
var cfg = config.get();

var arguments = process.argv.splice(2);
var txcode = arguments[0].toUpperCase();
txcode = txcode.toUpperCase();
//關閉時好用
//if(txcode !="XS010"){
//	alert("NO MAKE!!!!!!!!!!");
//	return;
//}
var bCompress = true; //壓縮   (arguments[1]!=null && arguments[1]=='compress')
//不管如何 都去SUPER INCLUDE
file.writeLine(cfg.getLstFilePathtmp(txcode), supinclude.superclude(cfg, txcode.toUpperCase()));
var result = parser.parse(cfg, txcode.toUpperCase());
file.writeLine(cfg.getLstFilePath(txcode), parser.getLst());
var content = fixIt(result);

mkdir_p(cfg.getTarget(txcode));
mkdir_p(cfg.getRefFolder(txcode));

var targetFiletitle = cfg.getBackpath(txcode) + "X_title" + '.txt';
var resultTitle = parser.getListtitle();
fs.appendFile(targetFiletitle, resultTitle + "\r\n");

var rr = [];
console.log("\r\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\r\n" + result.select + "\r\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\r\n");
result.select.forEach(function (x) {
	if (x.embeded) return;
	if (Array.isArray(x.form)) {
		x.form.forEach(function (z) {
			if (isNaN(z) && !exists(z)) {
				rr.push(z);
			}
		});
	} else {
		if (!exists(x.form)) {
			if (x.form.toUpperCase().indexOf('.PART') == -1) {
				rr.push(x.form);
			}
		}
	}

	function exists(f) {
		return rr.indexOf(f) !== -1;
	}
});

rr.forEach(function (r) {
	var checkExists = true;

	if (checkExists) {
		var p = cfg.getPFN(r);
		console.log('checking PFN:' + p);
		if (!fs.existsSync(p)) {
			allDone = false;
			console.error('Error! unknown output select FORMID:' + r + '(exists no ' + p + ')');
		}
	}
});

function outputOthers(where) {
	if (allDone) {
		cfg.addRef('_var_file_', cfg.getSource(txcode));
		xref.save(txcode, cfg);
		if (where) {
			file.writeLine(cfg.getBackpath(txcode) + txcode + ".pfns", rr.join("\n"));
		} else {
			file.writeLine(cfg.getTarget(txcode) + txcode + ".pfns", rr.join("\n"));
		}
	} else {
		console.error('something wrong, allDone is not true');
	}
}

//正常輸出壓縮後的檔案
console.log("compressing output file....");
compress(cfg.getTarget(txcode) + txcode + ".js", content);
//備份完整檔
outputOthers(true);
file.writeLine(cfg.getBackpath(txcode) + txcode + ".js", content);
console.log('bye1');

//if(bCompress) {
//	console.log("compressing output file....");
//	
//	compress(cfg.getTarget(txcode)+txcode+".js", content);
//}else {
//	outputOthers();
//	file.writeLine(cfg.getTarget(txcode)+txcode+".js", content);
//	console.log('bye1');
//}

function fixIt(r) {
	var tt = "" + JSON.stringify(r, null, 4);
	//var tt = ""+ JSON.stringify(r);
	var ss = tt.split("\n");
	var result = [];
	ss.forEach(function (x) {
		x = x.replace("\">>>>>", "");
		x = x.replace("<<<<<\"", "");
		x = x.replace(/\n/g, "\\\\n").replace(/\r/g, "\\\\r").replace(/\t/g, "\\\\t");

		result.push(x);
	});
	var content = "var getTranDef = function() { return " + result.join("\n") + ";};\n";
	//	console.log(content);
	return content;
}

function mkdir_p(dir) {
	var fs = require('fs');
	var path = require('path');
	if (!fs.existsSync(dir)) {
		mkdir_p(path.dirname(dir));
		fs.mkdirSync(dir, 0777);
	}
}

function compress(filePath, content) {
	var compressor = require('yuicompressor');

	var yuiMini = function (callback) {
		var miniDone = false;
		setTimeout(justWait, 1000);
		function justWait() {
			console.log('checking mini is done?');
			if (!miniDone) setTimeout(justWait, 500);
			else {
				console.log('mini is done');
				console.log('bye2');
			}
		}
		console.log('calling yuiMini');
		compressor.compressString(content, {
			//Compressor Options:
			charset: 'utf8',
			type: 'js',
			nomunge: true,
			'line-break': 80
		}, function (err, data, extra) {

			//err   If compressor encounters an error, it's stderr will be here
			//data  The compressed string, you write it out where you want it
			//extra The stderr (warnings are printed here in case you want to echo them
			if (err != null) {
				console.error(err);
				miniDone = true;
				return;
			}
			console.log('compress done');
			file.writeLine(filePath, data);
			outputOthers();
			miniDone = true;
			console.log("all files written");
		});
	}
	var uglifyMini = function () {
		var jsp = require("./uglify-js").parser;
		var pro = require("./uglify-js").uglify;
		content = content.toString();
		var ast = jsp.parse(content); // parse code and get the initial AST
		ast = pro.ast_mangle(ast); // get a new AST with mangled names
		ast = pro.ast_squeeze(ast); // get an AST with compression optimizations
		var final_code = pro.gen_code(ast, { beautify: false }); // compressed code here
		console.log('uglifyMini done');
		file.writeLine(filePath, final_code);
		outputOthers();
		miniDone = true;
		console.log("all files written");
		console.log('bye3');
	}
	uglifyMini();
}


var fs = require("fs");
var path = require("path");

exports.readLines = readLines;
exports.writeLine = writeLine;

function readLines(filename) {
	var buf = fs.readFileSync(filename);
	return buf.toString().split("\n");
}

function writeLine(filename, s) {
/*
	fs.writeFile(filename, s, function(err) {
		if(err) console.error("[ERROR] "+err)
		else console.log(filename + " is saved!");
	});
	*/
	fs.writeFileSync(filename,s);
	var t = fs.readFileSync(filename);
	if(s==t)
		console.log(filename + " was saved!");
	else {
		console.error("read/write " + filename + " not match");
		console.error(filename + ", write length:"+ s.length);
		console.error(filename + ", read length:"+ t.length);
	}
}


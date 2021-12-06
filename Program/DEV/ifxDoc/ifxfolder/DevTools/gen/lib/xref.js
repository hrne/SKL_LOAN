var file = require('./file');

exports.save = save;

function save(txcode, cfg){
	
	var refFiles = cfg.getRefFiles();
	var filePath = cfg.getRefPath(txcode);
	var content = JSON.stringify(refFiles,null,4);
	file.writeLine(filePath, content);
}


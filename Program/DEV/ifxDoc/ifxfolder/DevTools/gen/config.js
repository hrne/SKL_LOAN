var fs = require('fs'),
	hash_file = require('hash_file');
exports.get = getConfig;

function sleep(milliSeconds) {
    var startTime = new Date().getTime();
    while (new Date().getTime() < startTime + milliSeconds);
  }

function findIfxFolder(){
	console.log('Current directory: ' + process.cwd());
	var ss = process.cwd().split('/');
	console.log(ss + "---0");
  //sleep(5000);
	console.dir(ss);
	for(var i=ss.length-1; i> 0; i--) {
		if(ss[i].toLowerCase()=='ifxfolder') {
			ss.splice(i+1);
			return ss.join('/');
		}
	}
	throw "fatal error: can't not find ifxfolder in path:" + process.cwd();
}
function getConfig() {

	
	var currDrive = process.cwd().charAt(0) + ":";
	var var_folder = currDrive + "/ifxfolder/Dev/var";
	var target_folder = currDrive + "/ifxfolder/runtime/tran";
	var baktmp_folder = currDrive + "/ifxfolder/runtime/tran/BAK-FULL";
	
	var ifxFolder = findIfxFolder();
	console.log('ifx folder:'+ifxFolder);
	var_folder = ifxFolder + "/Dev/var";
	target_folder = ifxFolder + "/runtime/tran";
	baktmp_folder = ifxFolder + "/runtime/tran/BAK-FULL";
	
	console.log('var folder:'+var_folder);
	console.log('target folder:'+target_folder);
	
	var cfg = {
		refFiles:{},
		addRef:function(src, path) {
			var fstat = fs.statSync(path),
		    relPath = path.replace(var_folder + '/','');
		  //console.log(fstat);
		  //sleep(10000);
			this.refFiles[relPath] = {
					'key':src != null ? src : relPath,
					'path':relPath,
					'hash':this.getSha1(path),
					'mtime':fstat.mtime.getTime().toString()};
			
		},
		getSha1:function(filePath) {
			var shasum = require('crypto').createHash('sha1');
			var content = fs.readFileSync(filePath);
			shasum.update(content);
			var digest = shasum.digest('hex')
			return digest;
		},
		getSource:function(txcode) {
			txcode = txcode.toUpperCase();
			//if(txcode.slice(0,1) == 'X'  ){
			//	return var_folder + "/tran/" + txcode.slice(0,2) + "/" + txcode + ".var";
			//}else{
			//return var_folder + "/tran/" + txcode.slice(0,1) + "/" + txcode + ".var";
			//}
				return var_folder + "/tran/" + txcode.slice(0,2) + "/" + txcode + ".var";
		},
		getLstFilePath:function(txcode) {
			txcode = txcode.toUpperCase();
			//var lstFolder = var_folder + "/tran+/" + txcode.slice(0,1)
			//if(txcode.slice(0,1) == 'X'  ){
			//	var lstFolder = var_folder + "/tran+/" + txcode.slice(0,2) ;
			//}else{
			//	var lstFolder = var_folder + "/tran+/" + txcode.slice(0,1) ;
			//}
			var lstFolder = var_folder + "/tran+/" + txcode.slice(0,2) ;
			
			var mkdirp = require('mkdirp');
			mkdirp.sync(lstFolder)
			return lstFolder + "/" + txcode + ".var+";
		},
    getLstFilePathtmp:function(txcode) {
			txcode = txcode.toUpperCase();
			//var lstFolder = var_folder + "/tran+/" + txcode.slice(0,1)
			//if(txcode.slice(0,1) == 'X'  ){
			//	var lstFolder = var_folder + "/tran+/" + txcode.slice(0,2) ;
			//}else{
			//	var lstFolder = var_folder + "/tran+/" + txcode.slice(0,1) ;
			//}
			var lstFolder = var_folder + "/tran++/" + txcode.slice(0,2) ;
			var mkdirp = require('mkdirp');
			mkdirp.sync(lstFolder)
			return lstFolder + "/" + txcode + ".var++";
		},
		getTarget:function(txcode) {			
			return target_folder + "/" + txcode.slice(0,2) + "/";
		},
		getBackpath:function(txcode) {			
			return baktmp_folder + "/" + txcode.slice(0,2) + "/";
		},		
		getPFN:function(name) {
			name = name.toUpperCase();
			
			if(name.indexOf('.PFNX')!=-1) return ifxFolder + "/PFN/" +  name;
			else
				return ifxFolder + "/PFN/" +  name + ".PFN";	
		},
		getSys:function(name, ext) {
			var fullPath =  var_folder + "/sys/" + (name ? name : "sys") +  (ext ? ext : ".var");
			this.addRef( name, fullPath);
			return fullPath;
		},
		getInclude:function(src) {
			
			var fullPath =  var_folder + "/include/" +  src;
			this.addRef(src,fullPath);
			return fullPath;
		},
		getRefFiles:function(){
			return this.refFiles;
		},
		getRefFolder:function(txcode){
			return ifxFolder + '/Dev/xref/ref/'  + txcode.slice(0,2);
		},
		getRefPath:function(txcode) {
			return this.getRefFolder(txcode) + '/' + txcode + '.json';
		},
		getRefedByFolder:function(){
			return ifxFolder + '/Dev/xref/refedBy';
		},
		getRefedByPath:function(name) {
			return this.getRefedByFolder() + '/' + name + '.json';
		},
		getLayout:function(name) {
			return var_folder + "/layout/" + (name ? name : "default") + ".js";
		},
		getSharedSource:function(name) {
			return var_folder + "/tran/" + name.slice(0,2) + "/" + name + ".var";
		}
		,
		getSharedTarget:function(name) {
				return target_folder + "/" + name.slice(0,2) + "/";
		},
	};
	
	return cfg;
}
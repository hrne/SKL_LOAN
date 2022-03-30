function loadScript(url, callback) {
	var script = document.createElement("script");
	script.type = "text/javascript";
	if (script.readyState) { // IE
		script.onreadystatechange = function() {
			if (script.readyState == "loaded"
					|| script.readyState == "complete") {
				script.onreadystatechange = null;
				if(callback!=null)
					callback();
			}
		};
	} else { // Others
		script.onload = function() {
			if(callback!=null)
				callback();
		};
	}
	script.src = url;
	document.getElementsByTagName("head")[0].appendChild(script);
}
//loadScript("file1.js", function(){
//    alert("File is loaded!");
//});

//loadScript("file1.js", function(){
//    loadScript("file2.js", function(){
//        loadScript("file3.js", function(){
//            alert("All files are loaded!");
//        });
//    });
//});


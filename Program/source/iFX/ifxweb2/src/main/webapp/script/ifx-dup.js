__ifx_global["part"]["dup"] = (function() {
	var dupArea = {'hi':'how are you?'};
	var linkKeys = ["NTXBUF$","NTXBUF1$","NTXBUF2$","NTXBUF3$","NTXBUF4$","NTXBUF5$"];
	function put(k, value) {
		dupArea[k] = value;
	}
	function get(k, chain, ntxcd) {
		if(chain && dupArea[ntxcd])
		  return  dupArea[ntxcd][k];
		else
		  return dupArea[k];
	}
	function dup(map, ntxcd) {
		var temp = {};
		if(ntxcd)
		  temp[ntxcd] = map;
		
		_.extend(dupArea, map);
		_.extend(dupArea, temp);
		console.log("dup saved");
		dump();
	}
	function dump() {
		var keys = _.keys(dupArea).sort();
		console.log("=====   dup area  ====");
		_.each(keys,function(x){
			console.log(x + '=>[' + dupArea[x] + ']');
		});
		console.log("=====   end of dup area  ====");
	}
	function getLinkKeys() {
		return linkKeys;
	}
	
	
// end of ifx-dup
	return {
		'dup' : dup,
		'getLinkKeys':getLinkKeys,
		'get':get
	};
}());

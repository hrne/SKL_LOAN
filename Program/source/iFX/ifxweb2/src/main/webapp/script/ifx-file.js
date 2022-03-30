var ifxFile =  (function() {

	var url ='beanProxy.jsp';
	
	function setUrl(u){
		url = u;
	}
	function putFile(filename, content, blockers, fnReceive, bAsync){
		var bean = "FileReposBean.write";
		var data = {'p':filename, 'c':encodeURI(content)};
		data=JSON.stringify(data);
		sendBean(bean, data, blockers, fnReceive, bAsync);
	}
	function getFile(filename,blockers, fnReceive){
		var bean = "FileReposBean.read";
		var data = {'p':filename};
		data=JSON.stringify(data);
		sendBean(bean, data, blockers, fnReceive);
	}
	function deleteFile(filename,blockers, fnReceive){
		var bean = "FileReposBean.delete";
		var data = {'p':filename};
		data=JSON.stringify(data);
		sendBean(bean, data, blockers, fnReceive);
	}
	
	function sendBean(bean, dataJson, blockers, fnReceive, bAsync) {
		if(bAsync==undefined) bAsync = true;
		if(blockers!=null && blockers.block){
			blockers.block();
		}
		$.ajax({
			type:'post',
			dataType:'json',
			url:url,
			async:bAsync,
			data:{ 
				bean:bean,
				reqJson:dataJson
			}, 
			success:function(data) {
				if(fnReceive!=null)
					fnReceive(data);
				
			}, 
			complete:function(){
				if(blockers!=null && blockers.unblock!=null){
					blockers.unblock();
				}
			},
			'error':function(){
				alert("send bean error,url:"+url);
			}
		});
	}
	
	
	return {
		'put':putFile,
		'get':getFile,
		'remove':deleteFile,
		'setUrl':setUrl
	};
})();
var __ifx_global = {
	part : {},
	get : function(partName) {
		console.log('getting ' + partName);
		return __ifx_global['part'][partName];
	},
	init : function(_env) {
		__ifx_global["part"]["tranEnv"] = {
			'prt' : new IfxPrt(jQuery, _env.printServiceUrl),
			'unknownFormAction' : _env.unknownFormAction
		};
		for(var k in this.cfg){
			if(this.cfg[k].init)
				this.cfg[k].init();
		}
	},
	cfg:{}

};
__ifx_global.cfg.helpList = {
	COOKIE_NAME:'helpList',
	enabled:1,
	toggle:function() {
		this.enabled ^= 1;
		this.save();
	},
	visible:function() {
		return this.enabled == 1;
	},
	_title:['顯示代碼','隱藏代碼'],
	title:function(){
		return this._title[this.enabled];
	},
	init:function() {
		var v = $.cookie(this.COOKIE_NAME);
		if(v == undefined) {
			this.enabled = 1;
			this.save();
		}else {
			this.enabled = v;			
		}
	},
	save:function(){
		$.cookie(this.COOKIE_NAME, this.enabled);
	}
};
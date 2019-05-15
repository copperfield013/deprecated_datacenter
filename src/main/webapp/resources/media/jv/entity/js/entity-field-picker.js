define(function(require, exports, module){
	var Utils = require('utils');
	function EntityFieldPicker(_param){
		var defParam = {
			$page		: null,
			plhTarget	: null
		}
		this.param = $.extend({}, defParam, _param);
		this.uuid = Utils.uuid(10, 62);
		this.selectFieldCallbacks = $.Callbacks();
		this.composites = [];
		this.context = Utils.createContext({
			selectedFields		: []
		});
	}
	
	EntityFieldPicker.prototype.bindSelected = function(callback){
		this.selectFieldCallbacks.add(callback);
	}
	
	EntityFieldPicker.prototype.render = function(){
		var defer = $.Deferred();
		var that = this;
		require('tmpl').load('media/jv/entity/tmpl/entity-field-picker.tmpl').done(function(tmplMap){
			var $fieldPicker = tmplMap['fields-container'].replaceFor($('style[target="' + that.param.plhTarget + '"]', that.param.$page), {
				composites	: that.composites,
				uuid		: that.uuid
			}, {
				selectField	: function(field){
					that.selectFieldCallbacks.fireWith(that, [field, this]);
				}
			});
			defer.resolve($fieldPicker);
		});
		
		
		return defer.promise();
	}
	
	module.exports = EntityFieldPicker;
	
})
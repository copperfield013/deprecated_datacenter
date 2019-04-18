define(function(require, exports, module){
	var AbstractTemplateParameter = require('entity/js/entity-detail-input').AbstractTemplateParameter;
	
	
	function MultiSelectInputTemplateParameter(_param){
		var _this = this;
		
		this.param = $.extend({
			optonsKey			: null,
			options				: [],
			fieldOptionsFetcher	: require('field/js/field-option-fetcher.js').getGlobalFetcher(),
		}, _param)
		
		var fieldOptionsFetcher = this.param.fieldOptionsFetcher;
		
		this.data = {
			options	: []
		}
		
		this.valueGetter = function($dom){
			return getSelect($dom).val();
		}
		
		this.valueSetter = function($dom, val){
			if(val){
				var value = [];
				if($.isArray(val)){
					value = val;
				}else if(typeof val === 'string'){
					value = val.split(',');
				}
				getSelect($dom).val(value).trigger('change');
			}
		}
		
		function getSelect($dom){
			return $('select', $dom);
		}
		
		this.prepare = function(resolve){
			if(this.param.options){
				this.data.options = this.param.options;
				resolve();
			}else if(this.param.optionsKey){
				console.log('multi fetch' , this.param.optionsKey);
				fieldOptionsFetcher.fetch(this.param.optionsKey).done(function(options){
					_this.data.options = options;
					resolve();
				});
			}
		}
		this.afterRender = function($dom){
			fieldOptionsFetcher.afterCommit(function(){
				bindSelect2(getSelect($dom));
			});
		}
	}
	
	function bindSelect2($select, param){
		$select.select2({
			theme	: "bootstrap",
			width	: null
			
		});
	}
	
	$.extend(MultiSelectInputTemplateParameter.prototype, new AbstractTemplateParameter(), {
		tmplKey	: 'input-multiselect'
	});
	
	module.exports = MultiSelectInputTemplateParameter;
});
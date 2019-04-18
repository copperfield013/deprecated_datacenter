define(function(require, exports, module){
	var AbstractTemplateParameter = require('entity/js/entity-detail-input').AbstractTemplateParameter;
	/**
	 * 
	 */
	function AbstractSingleInputTemplateParameter(){
		this.valueGetter = function($dom){
			return $dom.val();
		}
		this.valueSetter = function($dom, value){
			return $dom.val(value);
		}
	}
	$.extend(AbstractSingleInputTemplateParameter.prototype, new AbstractTemplateParameter());
	
	module.exports = AbstractSingleInputTemplateParameter;
});
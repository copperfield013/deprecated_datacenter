define(function(require, exports, module){
	var AbstractSingleInputTemplateParameter = require('entity/js/tmpl_param/AbstractSingleInputTemplateParameter');
	/**
	 * 
	 */
	function DecimalInputTemplateParameter(){
		
	}
	$.extend(DecimalInputTemplateParameter.prototype, new AbstractSingleInputTemplateParameter(), {
		tmplKey	: 'input-decimal'
	});
	
	module.exports = DecimalInputTemplateParameter;
	
});
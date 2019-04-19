define(function(require, exports, module){
	var AbstractSingleInputTemplateParameter = require('entity/js/tmpl_param/AbstractSingleInputTemplateParameter');
	/**
	 * 
	 */
	function TextInputTemplateParameter(){
	}
	$.extend(TextInputTemplateParameter.prototype, new AbstractSingleInputTemplateParameter(), {
		tmplKey	: 'input-text'
	});
	
	module.exports = TextInputTemplateParameter;
});
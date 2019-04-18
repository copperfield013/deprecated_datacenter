define(function(require, exports, module){
	var AbstractSingleInputTemplateParameter = require('entity/js/tmpl_param/AbstractSingleInputTemplateParameter');
	/**
	 * 
	 */
	function TextInputTemplateParameter(){
		this.tmplKey = 'input-text';
	}
	$.extend(TextInputTemplateParameter.prototype, new AbstractSingleInputTemplateParameter());
	
	module.exports = TextInputTemplateParameter;
});
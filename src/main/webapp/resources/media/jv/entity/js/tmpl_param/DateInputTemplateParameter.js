define(function(require, exports, module){
	var AbstractSingleInputTemplateParameter = require('entity/js/tmpl_param/AbstractSingleInputTemplateParameter');
	function DateInputTemplateParameter(_param){
		var param = $.extend({
			$container	: null
		}, _param);
		
		this.afterRender = function($dom){
			require('utils').datepicker($dom, param.$container, param.$container);
		}
	}
	$.extend(DateInputTemplateParameter.prototype, new AbstractSingleInputTemplateParameter(), {
		tmplKey		: 'input-date'
	});
	
	module.exports = DateInputTemplateParameter;
});
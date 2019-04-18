define(function(require, exports, module){
	var AbstractSingleInputTemplateParameter = require('entity/js/tmpl_param/AbstractSingleInputTemplateParameter');
	/**
	 * 
	 */
	function IntInputTemplateParameter(){
		
	}
	$.extend(IntInputTemplateParameter.prototype, new AbstractSingleInputTemplateParameter(), {
		tmplKey		: 'input-int',
		afterRender : function($dom){
			$dom.on('input',function(){
				this.value = this.value.replace(/[^\d\-]/g, '').replace(/(\d+)\-+(\d*)/, '$1$2').replace(/\-+/, '-');
		    })
		}
	});
	
	module.exports = IntInputTemplateParameter;
});
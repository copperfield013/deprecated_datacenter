define(function(require, exports, module){
	function Console(_param){
		var defaultParam = {
				
		};
	}
	module.exports = Console;
	$.extend(Console, {
		log		: function(){
			doInConsole('log', arguments);
		},
		error	: function(){
			doInConsole('error', arguments);
		}
	});
	
	function doInConsole(method, args){
		if(console && typeof console[method] === 'function'){
			console[method].apply(console, args);
		}
	}
	
});
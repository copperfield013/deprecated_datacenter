define(function(require, exports, module){
	function Template(_param){
		var defaultParam = {
			$script	: $()
		};
		
		var param = $.extend({}, defaultParam, _param);
		
		/**
		 * 
		 */
		this.tmpl = function(obj){
			if(typeof $.fn.tmpl === 'function'){
				return param.$script.tmpl(obj);
			}
		};
		
		/**
		 * 
		 */
		this.getScript = function(){
			return param.$script;
		}
		
	}
	
	var tmplMap = {};
	Template.load = function(url, reqParam, tmplParam){
		var deferred = $.Deferred();
		if(tmplMap[url]){
			deferred.resolve(tmplMap[url]);
		}else{
			require('ajax').loadResource(url).done(function(data){
				var $script = $('<script>');
				$script.html(data);
				var tmpl = new Template($.extend({}, {
					$script	: $script
				}, tmplParam));
				tmplMap[url] = tmpl;
				deferred.resolve(tmpl);
			});
		}
		return deferred.promise();
	};
	
	module.exports = Template;
});
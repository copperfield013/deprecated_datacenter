define(function(require, exports, module){
	
	var composites = [{id:1,title:'基本信息',fields:[{id:1001,title:'姓名'},{id:1002,title:'性别'},{id:1003,title:'生日'},{id:1004,title:'标签'}]},{id:2,title:'就业信息',fields:[{id:1003,title:'工作单位'},{id:1004,title:'单位地址'},{id:1005,title:'入职时间'}]},{id:3,title:'家庭信息',fields:[{id:1006,title:'姓名'},{id:1007,title:'性别'},{id:1008,title:'生日'}]}];
	exports.initPage = function(_param){
		var defParam = {
			$page	: null,
			menuId	: null
		};
		
		var param = $.extend({}, defParam, _param);
		
		var Utils = require('utils');
		var Ajax = require('ajax');
		
		var $page = param.$page;
		
		var context = Utils.createContext({
			fieldDictionary	: [],
			tmpl			: null
		});
		
		require('tmpl').load('media/jv/entity/tmpl/entity-import-tmpl.tmpl').done(function(tmplMap){
			context
				.bind('fieldDictionary', renderFieldSelector)
				.bind('tmplFields', renderShownTemplateFields)
				;
			context.setStatus('fieldDictionary', null);
			function loadFields(){
				Ajax.ajax('api2/entity/import/fields/' + param.menuId).done(function(data){
					if(data.status === 'suc'){
						context.setStatus('fieldDictionary', data.fieldDictionary);
						context.setStatus('tmpl', null);
					}
				});
			}
			
			function renderFieldSelector(){
				var FieldPicker = require('entity/js/entity-field-picker');
				var fieldPicker = new FieldPicker({
					$page, plhTarget: 'fields-container'
				});
				fieldPicker.composites = composites;
				fieldPicker.bindSelected(whenFieldSelected);
				fieldPicker.render();
			}
			
			function whenFieldSelected(field, $field){
				var tmplFields = context.getStatus('tmplFields');
				tmplFields.push(field);
				context.setStatus('tmplFields');
			}
			
			
			function renderShownTemplateFields(){
				var tmplFields = context.getStatus('tmplFields');
				
				
				
			}
		});
		
		
		
		
	}
});
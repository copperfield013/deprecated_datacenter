define(function(require, exports, module){
	"use strict";
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
			fieldDictionary			: [],
			tmplFields				: [],
			fieldIndexMap			: {},
			relationLabelIndexMap	: {}
			
		});
		var $tbody = $('.fields-l tbody', $page);
		$tbody.sortable({
			helper 		: "clone",
			cursor 		: "move",// 移动时候鼠标样式
			opacity		: 0.5, // 拖拽过程中透明度
			tolerance 	: 'pointer'
		});
		
		require('tmpl').load('media/jv/entity/tmpl/entity-import-tmpl.tmpl').done(function(tmplMap){
			context
				.bind('fieldDictionary', renderFieldSelector)
				.bind('appendFields', renderShownTemplateFields)
				;
			loadFields();
			
			
			function loadFields(){
				Ajax.ajax('api2/entity/import/dict/' + param.menuId).done(function(data){
					if(data.fieldDictionary){
						context.setStatus('fieldDictionary', data.fieldDictionary);
						context.setStatus('appendFields', []);
					}
				});
			}
			
			function renderFieldSelector(){
				var fieldDictionary = context.getStatus('fieldDictionary');
				var FieldPicker = require('entity/js/entity-field-picker');
				var fieldPicker = new FieldPicker({
					$page, plhTarget: 'fields-container'
				});
				fieldPicker.setComposites(fieldDictionary.composites);
				fieldPicker.bindSelected(whenFieldSelected);
				fieldPicker.render();
			}
			
			function whenFieldSelected(field, $field, toggleDisabled){
				var fields = [];
				fields.push($.extend({uuid: Utils.uuid(5,62)}, field));
				if(field.composite.type === 'normal'){
					toggleDisabled(true);
				}
				context.setStatus('appendFields', fields);
			}
			
			
			function renderShownTemplateFields(){
				var appendFields = context.getStatus('appendFields');
				context.properties.appendFields = [];
				var rows = [];
				//关系和多值属性字段的当前最大索引值Map
				var fieldIndexMap = context.getStatus('fieldIndexMap');
				//关系当前最大索引值Map
				var relationLabelIndexMap = context.getStatus('relationLabelIndexMap');
				$.each(appendFields, function(){
					var title = this.name;
					var tmplField = {fieldId: this.id, compositeId: this.composite.id};
					if(this.composite.type === 'relation' || this.composite.type === 'multiattr'){
						//为关系和多值属性添加索引
						if(fieldIndexMap[this.id] === undefined){
							fieldIndexMap[this.id] = -1;
						}
						var fieldIndex = ++fieldIndexMap[this.id];
						title = this.composite.name + '[' + fieldIndex + ']' + this.name;
						tmplField['fieldIndex'] = fieldIndex;
						if(this.composite.type === 'relation'){
							//添加label字段
							if(relationLabelIndexMap[this.composite.id] === undefined){
								relationLabelIndexMap[this.composite.id] = -1;
							}
							if(relationLabelIndexMap[this.composite.id] < fieldIndexMap[this.id]){
								rows.push({
									title		: this.composite.name + '[' + ++relationLabelIndexMap[this.composite.id] + '].$label$',
									fieldName	: '',
									removable	: false
								});
							}
						}
					}
					
					var removable = true;
					rows.push($.extend({}, this, {
						title,
						removable,
						fieldName	: this.name,
					}));
					context.getStatus('tmplFields').push(tmplField);
				});
				console.log(context.getStatus('tmplFields'));
				$tbody.append(tmplMap['tmpl-field-rows'].tmpl({rows}, {
					removeRow	: function(field){
						
					}
				}));
			}
		});
		
		
		
		
	}
});
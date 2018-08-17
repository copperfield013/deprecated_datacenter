define(function(require, exports, module){
	var $CPF = require('$CPF');
	var FieldInput = require('field/js/field-input.js');
	exports.init = function($page, moduleName, premisesJson){
		var $chooseLtmpl = $('#choose-ltmpl', $page);
		
		var $tmplField = $('#tmpl-field', $page);
		
		var $fieldSearch = $('.field-search', $page);
		var $fieldContainer = $('.field-container', $page);
		
		
		$CPF.showLoading();
		FieldInput.loadGlobalOptions('admin/field/enum_json').done(function(){
			$CPF.closeLoading();
			var fieldSearch = require('field/js/field-search.js').bind($fieldSearch, {
				single				: true,
				textPicked			: true,
				module				: moduleName,
				showArrayComposite	: false,
				fieldFilters		: ['file'],
				afterChoose			: function(field){
					showPremiseField(field);
				}
			});
			function showPremiseField(f){
				fieldSearch.getFieldData(f.id).done(function(field){
					fieldSearch.enableField(field.id, false);
					var fieldData = {
						id				: f.premiseId || '',
						fieldId			: f.id,
						title			: field && field.title || '' 
					}
					var $field = $tmplField.tmpl(fieldData);
					$field.data('field-data', fieldData).appendTo($fieldContainer);
					$field.find('.remove-field').click(function(){
						$field.remove();
						fieldSearch.enableField(f.id, true);
					});
					if(field){
						var input = new FieldInput({
							type		: field.type,
							optionsKey	: field.optGroupId,
							fieldKey	: field.composite.module + '@' + field.name
						});
						$field.find('.field-view').append(input.getDom());
						if(f.value){
							input.setValue(f.value);
						}
						$field.data('field-input', input);
					}
					//Dtmpl.adjustFieldTitle($field.find('.field-title'));
				});
			}
			for(var i in premisesJson){
				var premise = premisesJson[i];
				showPremiseField({
					premiseId	: premise.id,
					id			: premise.fieldId,
					value		: premise.fieldValue
				});
			}
		});
		
		$('form', $page).on('cpf-submit', function(e, formData){
			var $form = $(this);
			formData.append('hideCreateButton', $('#showCreateButton', $page).prop('checked')? '': 1);
			formData.append('hideImportButton', $('#showImportButton', $page).prop('checked')? '': 1);
			formData.append('hideExportButton', $('#showExportButton', $page).prop('checked')? '': 1);
			$('.field-item', $page).each(function(index){
				var $this = $(this);
				var fieldData = $this.data('field-data');
				var fieldInput = $this.data('field-input');
				var premiseName = 'premises[' + index + ']';
				formData.append(premiseName + '.id', $this.attr('data-id'));
				formData.append(premiseName + '.fieldId', fieldData.fieldId);
				formData.append(premiseName + '.fieldValue', fieldInput.getValue());
				formData.append(premiseName + '.order', index);
			});
		});
	}
	
});
define(function(require, exports, module){
	"use strict";
	var Dialog = require('dialog'),
		Ajax = require('ajax'),
		$CPF = require('$CPF'),
		FieldInput = require('field/js/field-input.js'),
		DtmplUpdate = require('tmpl/js/dtmpl-update.js'),
		Utils = require('utils')
	
	exports.init = function($page, moduleName, entityCode){
		var isUpdateMode = entityCode !== '';
		
		$CPF.showLoading();
		FieldInput.loadGlobalOptions('admin/field/enum_json').done(function(){
			appendTo($page, $('.field-input', $page));
			$CPF.closeLoading();
		});
		
		var fuseMode = false;
		$('#save i', $page).click(function(){
			var validateResult = true;
			$('.dtmpl-field-validates>i', $page).each(function(){
				var $item = $(this).closest('.field-item');
				$item.removeClass('field-validate-error');
				validateResult = validate({
					$item 			: $item,
					validateName	: $(this).attr('validate-name'),
					title 			: $item.find('label.field-item').text(),
					value 			: $item.find(':input').val()
				}) && validateResult;
			});
			if(validateResult){
				var msg = '是否保存？';
				if(fuseMode){
					msg = '是否保存（当前为融合模式）？'
				}
				Dialog.confirm(msg, function(yes){
					if(yes){
						$('form', $page).submit();
					}
				});
			}
		});
		$('form', $page).on('cpf-submit', function(e, formData){
			//绑定部分自定义字段控件的表单值
			FieldInput.bindSubmitData(e.target, formData);
			formData.append('%fuseMode%', fuseMode);
		});
		$('#fuse-switch', $page).change(function(){
			fuseMode = $(this).prop('checked');
			$('#save', $page).toggleClass('fuse-mode', fuseMode);
		});
		
		
		$page.on('click', '.array-item-remove', function(){
			var $row = $(this).closest('tr');
			Dialog.confirm('确认删除该行？', function(yes){
				if(yes){
					var $table = $row.closest('table');
					$row.remove();
					refreshTable($table);
				}
			});
		});
		$('.array-item-add', $page).click(function(){
			var $table = $(this).closest('table');
			var $tbody = $table.children('tbody');
			var $titleRow = $table.find('.title-row');
			var $dataRow = $('<tr>').append('<td><span></span></td>')
			$titleRow.children('th.th-field-title').each(function(){
				var $title = $(this);
				var $td = $('<td>');
				if($title.is('.field-unavailable')){
					$td.addClass('field-unavailable');
				}else{
					var $fieldInput = $('<span class="field-input"></span></span>');
					$fieldInput
						.attr('fInp-type', $title.attr('fInp-type'))
						.attr('fInp-optkey', $title.attr('fInp-optkey'))
						.attr('fInp-fieldkey', $title.attr('fInp-fieldkey'))
						.appendTo($('<span class="field-value"></span>').appendTo($td));
					if($title.attr('fInp-optset')){
						$fieldInput.attr('fInp-optset', $title.attr('fInp-optset'));
					}
				}
				$dataRow.append($td);
			});
			$dataRow.append('<td><span class="array-item-remove" title="移除当前行">×</span></td>');
			$dataRow.appendTo($tbody);
			appendTo($page, $dataRow.find('.field-input')).done(function(){
				refreshTable($table);
			})
		});
		
		setTimeout(function(){
			$('.field-title', $page).each(function(){DtmplUpdate.adjustFieldTitle($(this))});
		}, 100);
		
	}
	
	function appendTo($page, $doms, paramGetter){
		var def = $.Deferred();
		paramGetter = paramGetter || function($dom){
			function attr(attrName){
				return $dom.attr(attrName);
			}
			return {
				type		: attr('fInp-type'),
				name		: attr('fInp-name'),
				id			: attr('fInp-id'),
				value		: attr('fInp-value'),
				styleClass	: attr('fInp-class'),
				optionsKey	: attr('fInp-optkey'),
				readonly	: attr('fInp-readonly'),
				optionsSet	: attr('fInp-optset'),
				fieldKey	: attr('fInp-fieldkey')
			};
		};
		$doms.each(function(){
			var $this = $(this);
			var param = paramGetter($this);
			var fInp = new FieldInput(param);
			var $sames = $page.find('.field-value[value-field-name="' + param.name + '"]');
			//字段与模板组合默认字段相同时，禁用字段编辑
			if($sames.filter('.premises-container *').length > 0){
				fInp.setDisabled();
			}
			//fieldName相同的字段，将其统一
			$sames.not('.premises-container *').each(function(){
				var otherInput = $(this).find('.field-input').data('field-input');
				if(otherInput){
					fInp.relateInput(otherInput);
					fInp.removeFormName();
				}
			});
			$this.append(fInp.getDom()).data('field-input', fInp);
		});
		def.resolve();
		return def.promise();
	};
	
	
	function validate(form){
		switch(form.validateName){
			case 'required'	:
				if(form.value === ''){
					validateError(form);
					return false
				}
				return true;
				break;
			default :
				return true;
		}
	}
	
	function validateError(form){
		form.$item.addClass('field-validate-error');
	}
	
	
	
	function refreshTable($table){
		var $titles = $('thead tr.title-row th', $table); 
		$('tbody tr', $table).each(function(i){
			var $tr = $(this);
			var $tds = $tr.children('td');
			$tds.eq(0).children('span').text(i + 1);
			for(var j = 0; j < $tds.length - 1; j ++){
				var nameFormat = $titles.eq(j).attr('fname-format');
				var inputName = nameFormat.replace('ARRAY_INDEX_REPLACEMENT', i);
				$tds.eq(j).find(':text,select,textarea,input[type="hidden"]').each(function(){
					$(this).attr('name', inputName);
				})
				.end().find('.cpf-field-input').each(function(){
					var fieldInputObject = $(this).data('fieldInputObject');
					if(fieldInputObject){
						fieldInputObject.setFormName(inputName);
					}
				});
			}
		});
	}
	
		
});
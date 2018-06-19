/**
 * 
 */
define(function(require, exports, module){
	var $CPF = require('$CPF'),
		Ajax = require('ajax'),
		Utils = require('utils'),
		Dialog = require('dialog');
	exports.init = function($page, config){
		var $moduleItemContainer = $('#level1-list', $page);
		var $moduleSelect = null;
		try{
			$moduleSelect = $('<select class="module-select">');
			for(var name in config.modules){
				var module = config.modules[name];
				$moduleSelect.append('<option value="' + module.name + '">' + module.title + '</option>');
				$moduleSelect.change(function(){
					var $moduleTitle = $(this).closest('.dd-handle').find('.level1-title :text');
					$moduleTitle.val(Utils.getCheckedOption(this).text()).select();
				});
			}
		}catch(e){}
		var saveButtonShowed = false;
		function bindSortable(target){
			$(target).sortable({
				update: function( event, ui ) {
					if(!saveButtonShowed){
						toggleSaveButton(1);
					}
				}
			});
		}
		bindSortable($('.dd-list', $page));
		function toggleSaveButton(toShow){
			saveButtonShowed = true;
			var $a = $('#save', $page).closest('a');
			if(toShow){
				Utils.removeStyle($a, 'display');
			}else{
				$a.hide();
			}
		}
		function toggleAddLevel1Button(toShow){
			var $a = $('#add-level1', $page).closest('a');
			if(toShow){
				Utils.removeStyle($a, 'display');
			}else{
				$a.hide();
			}
		}
		
		$('#add-level1', $page).click(function(){
			var $li = $('#level1-item-tmpl', $page).tmpl()
				.find('.dd-handle').append($moduleSelect)
				.end()
				.appendTo($moduleItemContainer)
				;
			bindSortable($li.find('.dd-list'));
			$moduleSelect.trigger('change');
			toggleSaveButton(false);
			toggleAddLevel1Button(false);
		});
		$('#save', $page).click(function(){
			var submitData = {
				modules	: []
			};
			$moduleItemContainer.find('li[module-name]').each(function(i){
				var $level1Li = $(this);
				var module = {
					id			: $level1Li.attr('data-id'),
					title		: $level1Li.find('.level1-title').text(),
					order		: i,
					moduleName	: $level1Li.attr('module-name'),
					groups		: []
				};
				submitData.modules.push(module);
				$level1Li.find('ol li[group-id]').each(function(j){
					var $level2Li = $(this);
					var group = {
							id			: $level2Li.attr('data-id'),
							title		: $level2Li.find('.level2-title').text(),
							order		: j,
							tmplGroupId	: $level2Li.attr('group-id')
					}
					module.groups.push(group);
				});
			});
			Dialog.confirm('确认保存该功能列表', function(yes){
				if(yes){
					Ajax.postJson('admin/config/sidemenu/save', submitData, function(data){
						if(data.status === 'suc'){
							Dialog.confirm('保存成功，是否重新打开系统？', function(yes){
								if(yes){
									window.location.reload();
								}else{
									$page.getLocatePage().refresh();
								}
							});
						}else if(data.status === 'duplicateModule'){
							Dialog.notice('保存失败，模块重复', 'error');
						}else{
							Dialog.notice('保存失败', 'error');
						}
					});
				}
			});
		});
		$moduleItemContainer.on('click', '.save-level1', function(){
			var $handler = $(this).closest('.dd-handle');
			$handler.find('.level1-title :text').replaceWith(function(){
				return $(this).val();
			});
			$handler.find('.tip-level-title').text(Utils.getCheckedOption($moduleSelect).text());
			$handler.closest('li').attr('module-name', $moduleSelect.val());
			$moduleSelect.detach();
			$(this).removeClass('save-level1 a-check').addClass('add-level2 fa-plus-square-o');
			toggleSaveButton(true);
			toggleAddLevel1Button(true);
		});
		$moduleItemContainer.on('click', '.add-level2', function(){
			var $item = $(this).closest('.dd-item');
			var $ol = $item.find('ol');
			var $groupSelect = $('<select class="group-select">').change(function(){
				var $moduleTitle = $(this).closest('.dd-handle').find('.level2-title :text');
				$moduleTitle.val(Utils.getCheckedOption(this).text()).select();
			});
			var moduleName = $item.closest('li').attr('module-name');
			var groups = config.modules[moduleName].groups;
			$groupSelect.append('<option value="0">默认</option>')
			for(var i in groups){
				$groupSelect.append('<option value="' + groups[i].id + '">' + groups[i].title + '</option>')
			}
			$('#level2-item-tmpl', $page).tmpl()
					.find('.dd-handle').append($groupSelect)
					.end()
					.appendTo($ol);
			$groupSelect.trigger('change');
			toggleSaveButton(false);
			toggleAddLevel1Button(false);
		});
		$moduleItemContainer.on('click', '.save-level2', function(){
			var $handler = $(this).closest('.dd-handle');
			$handler.find('.level2-title :text').replaceWith(function(){
				return $(this).val();
			});
			var $groupSelect = $('select.group-select', $handler);
			$handler.closest('li')
				.attr('group-id', $groupSelect.val());
			$handler.find('.tip-level-title').text(Utils.getCheckedOption($groupSelect).text());
			$groupSelect.remove();
			$(this).closest('a').remove();
			toggleSaveButton(true);
			toggleAddLevel1Button(true);
		});
		$moduleItemContainer.on('dblclick', '.level2-title,.level1-title', function(){
			var $this = $(this);
			if($this.find(':text').length === 0){
				Utils.toEditContent($this).bind('confirmed', function(){
					toggleSaveButton(true);
					toggleAddLevel1Button(true);
				});
				toggleSaveButton(false);
				toggleAddLevel1Button(false);
			}
		});
		$moduleItemContainer.on('click', '.del-level', function(){
			var $this = $(this);
			Dialog.confirm('确认删除？', function(yes){
				if(yes){
					$this.closest('li').remove();
					toggleSaveButton(true);
					toggleAddLevel1Button(true);
				}
			});
		});
	}
});
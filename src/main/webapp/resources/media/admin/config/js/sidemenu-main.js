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
		var saveButtonShowed = false;
		function bindSortable(target){
			var newItem;
			$(target).sortable({
				update	: function( event, ui ) {
					if(!saveButtonShowed){
						toggleSaveButton(1);
					}
				},
				beforeStop: function (event, ui) { 
				      newItem = ui.item;
				},
				receive	: function(e, ui){
					var $g = $(ui.sender),
						$moduleWrapper = $g.closest('.module-wrapper');
					var groupTitle = $g.text().trim();
					var data = {
						level2Title		: groupTitle,
						tmplGroupId		: $g.attr('group-id'),
						moduleTitle		: $moduleWrapper.find('.module-title').text().trim(),
						tmplGroupTitle	: groupTitle,
						moduleName		: $moduleWrapper.attr('module-name')
					};
					var $level2 = $('#level2-item-tmpl', $page).tmpl(data);
					$(newItem).replaceWith($level2);
					$level2.closest('li').attr('group-id', data.tmplGroupId);
					toggleSaveButton(false);
					toggleAddLevel1Button(false);
					$level2.find(':text')
							.on('keydown', enterTrigger)
							.on('enter blur', confirmTitleEdit)
							.select();
				}
			}).disableSelection();
		}
		$('ul li', $page).disableSelection();
		bindSortable($('.menu-container .dd-list', $page));
		$('.mds-container .md-group-title', $page).draggable({
			connectToSortable: '#' + $page.attr('id') + ' .menu-container #level1-list>.dd-item>.dd-list',
			helper: 'clone',
			scroll: true
		});
		function enterTrigger(e){if(e.keyCode == 13) $(this).trigger('enter')}
		function confirmTitleEdit(){
			var $text = $(this);
			var title = $text.val();
			if(!title){
				Dialog.notice('内容不能为空', 'error');
				return false;
			}
			$text.replaceWith(function(){
				return $text.val();
			});
			toggleSaveButton(true);
			toggleAddLevel1Button(true);
		}
		
		function toggleSaveButton(toShow){
			saveButtonShowed = true;
			var $a = $('#save', $page).closest('a');
			if(toShow){
				if($(':text', $moduleItemContainer).length == 0){
					Utils.removeStyle($a, 'display');
					return;
				}
			}
			$a.hide();
		}
		function toggleAddLevel1Button(toShow){
			var $a = $('#add-level1', $page).closest('a');
			if(toShow){
				if($(':text', $moduleItemContainer).length == 0){
					Utils.removeStyle($a, 'display');
					return;
				}
			}
			$a.hide();
		}
		
		$('#add-level1', $page).click(function(){
			var $li = $('#level1-item-tmpl', $page).tmpl()
				.appendTo($moduleItemContainer)
				;
			bindSortable($li.find('.dd-list'));
			toggleSaveButton(false);
			toggleAddLevel1Button(false);
			$li.find(':text')
				.on('keydown', enterTrigger)
				.on('enter blur', confirmTitleEdit)
				.select();
		});
		$('#save', $page).click(function(){
			var submitData = {
				modules	: []
			};
			$moduleItemContainer.find('>li').each(function(i){
				var $level1Li = $(this);
				var module = {
					id			: $level1Li.attr('data-id'),
					title		: $level1Li.find('.level1-title').text(),
					order		: i,
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
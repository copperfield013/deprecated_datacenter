define(function(require, exports, module){
	"use strict";
	 function adjustFieldTitle($titleLabel){
		var $titleSpan = $('<span class="field-title-d">').text($titleLabel.text());
		$titleLabel.empty().append($titleSpan);
		require('utils').removeStyle($titleLabel, 'font-size');
		require('utils').removeStyle($titleLabel, 'line-height');
		var thisWidth = $titleSpan.width(),
		thisHeight = $titleSpan.height(),
		parentWidth = $titleLabel.width(),
		parentHeight = $titleLabel.height(),
		parentFontsize = parseFloat($titleLabel.css('font-size'));
		;
		var row = Math.ceil(thisWidth / parentWidth);
		var parentLineheight = parentHeight / row;
		if(parentFontsize >= parentLineheight){
			$titleLabel.css('font-size', (parentLineheight - 2) + 'px');
		}
		$titleLabel.css('line-height', (parentLineheight - 1) + 'px');
		
		$titleLabel.text($titleSpan.text());
	};
	exports.adjustFieldTitle = adjustFieldTitle;
	
	exports.init = function($page, _param){
		var FieldSearch = require('field/js/field-search.js');
		//初始化参数
		var param = $.extend({
			tmplData	: {}
		}, _param);
		
		//字段组容器
		var $groupContainer = $('.group-container', $page);
		//字段模板
		var $tmplFieldGroup = $('#tmpl-field-group', $page);
		var $tmplField = $('#tmpl-field', $page);
		
		var tmplData = param.tmplData;
		
		/**
		 * 初始化某个字段组内的字段容器的拖拽事件
		 */
		function bindGroupFieldsDraggable($fieldContainer){
			$fieldContainer.sortable({
				helper 		: "clone",
				cursor 		: "move",// 移动时候鼠标样式
				opacity		: 0.5, // 拖拽过程中透明度
				placeholder	: function(curr){
					return curr.is('.dbcol')? 
							'field-item-placeholder dbcol'
							: 'field-item-placeholder'  
				},
				tolerance 	: 'pointer'
			});
		}
		
		/**
		 * 获得某个元素所在的字段最顶层dom
		 */
		function getLocateField($dom){
			return $($dom).closest('.field-item');
		}
		
		/**
		 * 获得某个元素所在的字段组最顶层dom
		 */
		function getLocateGroup($dom){
			return $($dom).closest('.field-group');
		}
		
		/**
		 * 获得字段组的字段容器dom
		 */
		function getFieldContainer($group){
			return $('.field-container', $group);
		}
		
		/**
		 * 初始化某个字段组的自动完成功能
		 */
		function initGroupFieldSearchAutocomplete($group){
			var $fieldSearch = $('.field-search', $group);
			var fieldSearch = FieldSearch.bind($fieldSearch, {
				single			: true,
				textPicked		: true,
				module			: param.module,
				afterChoose		: function(field){
					if(field.composite.isArray){
						//选择的字段是一个数组字段，锁定当前选择器的标签页
						fieldSearch.lockTab();
						//TODO: 将字段添加到数组当中
					}else{
						fieldSearch.hideArrayComposites();
					}
					appendFieldToGroup({
						title			: field.title,
						fieldId			: field.id,
						colNum			: 1
					}, $group, {
						isArrayField	: field.composite.isArray == 1,
						relations		: field.composite.relationSubdomain
					});
				}
			});
			$group.data('fieldSearch', fieldSearch);
		}
		/**
		 * 添加一个字段到指定的字段组中
		 * @param groupFieldData 字段数据，必须包含fieldId属性
		 */
		function appendFieldToGroup(groupFieldData, $group, option){
			//构造新字段的内容
			var fieldData = {
					id				: groupFieldData.id,
					title			: groupFieldData.title,
					fieldId			: groupFieldData.fieldId,
					dv				: groupFieldData.dv || 'XXXXX',
					colNum			: groupFieldData.colNum,
					fieldOriginTitle: groupFieldData.fieldOriginTitle,
					fieldAvailable	: groupFieldData.fieldAvailable == undefined? true: groupFieldData.fieldAvailable,
					validators		: []
			};
			var VALIDATORS = ['required'];
			if(!$.isArray(groupFieldData.validators)){
				groupFieldData.validators = [];
			}
			for(var i in VALIDATORS){
				fieldData.validators[VALIDATORS[i]] = groupFieldData.validators.indexOf(VALIDATORS[i]) >= 0;
			}
			//将字段插入到字段组中
			var $fieldContainer = getFieldContainer($group);
			var fieldSearch = $group.data('fieldSearch');
			fieldSearch.getFieldData(fieldData.fieldId).done(function(field){
				fieldData.fieldOriginTitle = field? (field.c_cname + '-' + field.title) : '';
				if(option.isArrayField){
					//添加数组字段
					var $arrayTable = $('.field-array-table', $fieldContainer);
					if($arrayTable.length == 0){
						//还没有添加字段过
						$arrayTable = $('#tmpl-field-array-table', $page).tmpl();
						$arrayTable.appendTo($fieldContainer);
						if($.isArray(option.relations)){
							var $titleCell = $('<th>关系</th>')
							var $relationSelect = $('<select class="tmpl-relation-labels">');
							for(var i in option.relations){
								$relationSelect.append('<option value="' + option.relations[i] + '">' + option.relations[i] + '</option>');
							}
							$arrayTable.find('.title-row').append($titleCell);
							$arrayTable.find('.value-row').append($('<td>').append($relationSelect));
						}
						$arrayTable.find('.title-row').sortable({
							helper		: 'original',
							cursor		: 'move',
							axis		: 'x',
							opacity		: 0.5,
							tolerance 	: 'pointer',
							stop		: function(e, ui){
								var Utils = require('utils');
								$(this).children().each(function(index){
									var $title = $(this);
									var fieldId = $title.attr('field-id');
									$arrayTable.find('tbody').children('tr').each(function(){
										var $row = $(this);
										var $cell = null;
										if(fieldId){
											$cell = $row.find('td[field-id="' + fieldId + '"]');
										}else if($title.is('.number-col')){
											$cell = $row.find('td.number-col');
										}
										if($cell != null){
											Utils.prependTo($cell, $row, index);
										}
									});
								});
								console.log(this);
								console.log(ui);
							}
						});
						var $arrayitemControl = $group.find('.select-arrayitem-control');
						if(field.composite.addType == 5){
							$arrayitemControl
								.find(':checkbox').change(function(){
									//勾选是否显示选择按钮
									var $checkbox = $(this);
									var $a = $checkbox.closest('label').prev('a');
									$a.toggle($checkbox.prop('checked'));
								})
								.end().find('a.btn-select').click(function(){
									var reqParam = {};
									var stmplId = $group.attr('stmpl-id'); 
									if(!stmplId){
										reqParam.moduleName = param.module;
										reqParam.compositeId = field.c_id;
									}
									require('dialog').openDialog(
											'admin/tmpl/stmpl/' + (stmplId? ('update/' + stmplId) : 'create')
											, '编辑选择模板' + field.c_title, undefined, {
												reqParam	: reqParam,
												width		: 1000,
												height		: 500,
												events		: {
													afterSave	: function(stmplId){
														if(stmplId){
															console.log(stmplId);
															$group.attr('stmpl-id', stmplId);
														}
													}
												}
											});
								})
								.end().show();
							$arrayitemControl.find(':checkbox.selectable').prop('checked', !!$group.attr('stmpl-id')).trigger('change');
						}else{
							$arrayitemControl.remove();
						}
					}
					var $titleCell = $('#tmpl-field-array-title', $page).tmpl(fieldData);
					$titleCell.data('field-data', fieldData);
					$arrayTable.find('.title-row').append($titleCell);
					$arrayTable.find('.value-row').append($('#tmpl-field-array-value', $page).tmpl(fieldData));
				}else{
					var $field = $tmplField.tmpl(fieldData);
					$field.data('field-data', fieldData).appendTo($fieldContainer);
					adjustFieldTitle($field.find('.field-title'));
				}
				fieldSearch.enableField(fieldData.fieldId, false).done(function(field){
					if(field){
						if(field.composite.isArray){
							$group.attr('composite-id', field.composite.c_id);
							fieldSearch.lockTabByCompositeId(field.composite.c_id);
						}else{
							fieldSearch.hideArrayComposites();
						}
					}
				});
			});
			return true;
		}
		
		/**
		 * 绑定双击时，编辑该文本的事件
		 */
		function bindDblClickEdit(selector, inputClass){
			$page.on('dblclick', selector, function(e){
				require('utils').toEditContent(e.target, inputClass).bind('confirmed', function(text, $this){
					if($this.is('.field-title')){
						adjustFieldTitle($this);
					}else if($this.is('.group-title')){
						//焦点放到字段搜索框中
						getLocateGroup($this).find('.search-text-input').select();
					}
				});
			});
		}
		
		/**
		 * 切换字段的显示长度
		 */
		function toggleFieldExpand($field, toExpand){
			var $i = $('.toggle-expand-field i', $field);
			require('utils').switchClass($i, 'fa-expand', 'fa-compress', toExpand, function(compressed){
				$field.toggleClass('dbcol', !compressed);
			});
		}
		
		/**
		 * 绑定全局事件
		 */
		function bindPageEvent(event, selector, callback){
			$page.bind(event, selector, function(e){
				var $target = $(e.target);
				console.log($target);
				if($target.is(selector)){
					try{
						callback.apply($target, [e]);
					}catch(e){}
					return false;
				}
			});
		}
		/**
		 * 检查并整合页面中的模板数据
		 */
		function checkSaveData(callback){
			var Dialog = require('dialog');
			var saveData = {
					tmplId	: param.tmplId,
					//模板名
					title	: $('#tmplName', $page).val(),
					//字段组
					groups	: [],
					//模板模块
					module	: param.module
			};
			//遍历所有字段组
			getAllGroups().each(function(){
				var $group = $(this);
				var $arrayTable = $group.find('.field-array-table');
				var isArray = $arrayTable.length > 0;
				
				var group = {
						id		: $group.attr('data-id'),
						title	: $group.find('span.group-title').text(),
						isArray	: isArray,
						fields	: []							
				};
				saveData.groups.push(group);
				if(isArray){
					var $selectable = $group.find('.selectable:checkbox');
					if($selectable.prop('checked')){
						group.selectionTemplateId = $group.attr('stmpl-id');
					}
					group.compositeId = $group.attr('composite-id');
					$arrayTable.find('.title-row>th[field-id]').each(function(){
						var $th = $(this);
						var field = $th.data('field-data');
						group.fields.push({
							id		: $th.attr('data-id'),
							fieldId	: $th.attr('field-id'),
							title	: $th.children('span').text(),
							viewVal	: 'XXX'
						});
					});
				}else{
					//遍历所有字段
					$group.find('.field-item').each(function(){
						var $field = $(this);
						var field = {
								id			: $field.attr('data-id'),
								fieldId		: $field.attr('field-id'),
								title		: $field.find('label.field-title').text(),
								viewVal		: $field.find('span.field-view').text(),
								dbcol		: $field.is('.dbcol'),
								validators	: ''
						};
						$field.find('.field-validate-menu>li.checked-validate').each(function(){
							field.validators += $(this).attr('validate-name') + ';';
						});
						if(field.validators){
							field.validators = field.validators.substring(0, field.validators.length - 1);
						}
						group.fields.push(field);
					});
				}
			});
			if(!saveData.title){
				Dialog.notice('请填写模板名', 'error');
			}else if(saveData.groups.length == 0){
				Dialog.notice('请至少添加一个字段组', 'error');
			}else{
				Dialog.confirm('确认保存模板？', function(yes){
					if(yes){
						(callback || $.noop)(saveData);
					}
				});
			}
		}
		
		function getAllGroups(){
			return $groupContainer.find('.field-group');
		}
		
		var disabledFieldIdSet = new Set();
		
		//初始化字段组容器的拖拽事件
		$groupContainer.sortable({
			helper		: 'clone',
			cursor		: 'move',
			opacity		: 0.5,
			placeholder	: 'group-item-placeholder',
			handle		: '.group-title',
			tolerance 	: 'pointer'
		});
		
		//绑定点击添加字段组按钮的事件
		$('#add-group', $page).click(function(){
			var $group = $tmplFieldGroup.tmpl({
				title	: '新字段组'
			}).appendTo($groupContainer);
			//绑定字段组内字段的拖动动作
			bindGroupFieldsDraggable(getFieldContainer($group));
			//初始化字段组的字段搜索自动完成功能
			initGroupFieldSearchAutocomplete($group);
			//页面滚动到底部
			require('utils').scrollTo($page.closest('.cpf-page-content'));
			//触发字段组的标题修改功能
			$group.find('.group-title').trigger('dblclick');
		});
		
		//绑定点击保存按钮时的回调
		$('#save', $page).click(function(){
			checkSaveData(function(saveData){
				require('ajax').postJson('admin/tmpl/dtmpl/save', saveData, function(data){
					if(data.status === 'suc'){
						require('dialog').notice('保存成功', 'success');
						$page.getLocatePage().close();
						var tpage = require('page').getPage(param.module + '_dtmpl_list');
						if(tpage){
							tpage.refresh();
						}
					}else{
						require('dialog').notice('保存失败', 'error');
					}
				});
			});
		});
		
		//绑定模板名称回车时，添加一个字段组
		$('#tmplName', $page).keypress(function(e){
			if(e.keyCode === 13){
				if($groupContainer.children('.field-group').length === 0){
					$('#add-group', $page).trigger('click');
				}
			}
		});
		
		//切换字段的显示长度
		bindPageEvent('click', '.toggle-expand-field i', function(e){
			var $field = getLocateField(this);
			toggleFieldExpand($field);
		});
		
		//删除字段
		bindPageEvent('click', '.remove-field i', function(e){
			var $field = getLocateField(e.target),
				$group = getLocateGroup(e.target),
				fieldTitle = $field.find('.field-title').text(),
				groupName = $group.find('.group-title').text();
			require('dialog').confirm('确认在字段组[' + groupName + ']中删除字段[' + fieldTitle + ']？', function(yes){
				if(yes){
					if($field.siblings('div[field-id]').length == 0){
						//如果是最后一个字段，那么就重置该字段组
						$field.closest('.field-group').removeAttr('data-id');
					}
					var fieldSearch = $group.data('fieldSearch');
					if(fieldSearch){
						fieldSearch.enableField($field.attr('field-id'));
					}
					$field.remove();
				}
			});
		});
		//恢复字段名称
		bindPageEvent('click', '.recover-field i', function(e){
			var $field = getLocateField(e.target),
			$fieldTitle = $field.find('.field-title'),
			fieldTitle = $fieldTitle.text(),
			fieldData = $field.data('field-data'),
			fieldName = fieldTitle;
			if(fieldData && fieldData.title){
				fieldName = fieldData.title;
			}
			require('dialog').confirm('确认恢复字段[' + fieldTitle + ']为原始名称[' + fieldName + ']？', function(yes){
				if(yes){
					$fieldTitle.text(fieldName);
					adjustFieldTitle($fieldTitle);
				}
			});
		});
		
		bindPageEvent('click', 'ul.field-validate-menu>li', function(e){
			var $field = getLocateField(e.target);
			var $li = $(this);
			var toChecked = !$li.is('.checked-validate');
			var validateName = $li.attr('validate-name');
			$field.find('.dtmpl-field-validates>.dtmpl-field-validate-' + validateName).toggleClass('active-validator', toChecked);
			$li.toggleClass('checked-validate', toChecked);
		});
		
		//删除数组字段
		bindPageEvent('click', '.remove-array-field i', function(e){
			var $title = $(this).closest('th'),
				title = $title.children('span').text(),
				fieldId = $title.attr('field-id'),
				$group = getLocateGroup(e.target),
				groupName = $group.find('.group-title').text();
			if(fieldId){
				require('dialog').confirm('确认在字段组[' + groupName + ']中删除字段[' + title + ']？', function(yes){
					if(yes){
						var isOnly = false;
						if($title.siblings('th[field-id]').length == 0){
							//如果是最后一个字段，那么就把整个列表移除
							$title.closest('.field-group').removeAttr('data-id');
							$title.closest('.field-array-table').remove();
						}else{
							$title.closest('table').find('td[field-id="' + fieldId + '"]').remove();
							$title.remove();
						}
						var fieldSearch = $group.data('fieldSearch');
						if(fieldSearch){
							fieldSearch.enableField(fieldId);
						}
					}
				});
			}
		});
		
		bindPageEvent('click', '.recover-array-field i', function(e){
			var $title = $(this).closest('th'),
				title = $title.children('span').text(),
				fieldData = $title.data('field-data'),
				fieldTitle = title;
			
			if(fieldData && fieldData.title){
				fieldTitle = fieldData.title;
			}
			require('dialog').confirm('确认恢复字段[' + title + ']为原始名称[' + fieldTitle + ']？', function(yes){
				if(yes){
					$title.text(fieldTitle);
				}
			});
		});
		
		//删除字段组
		bindPageEvent('click', '.remove-group i', function(e){
			var $group = getLocateGroup(e.target);
			var groupTitle = $group.find('.group-title').text();
			require('dialog').confirm('是否删除字段组[' + groupTitle + ']', function(yes){
				if(yes){
					//移除
					$group.remove();
					//释放字段组的字段选择器
					var fieldSearch = $group.data('fieldSearch');
					if(fieldSearch){
						fieldSearch.release();
					}
				}
			});
		});
		//双击编辑字段组标题
		bindDblClickEdit('span.group-title', 'group-title');
		//双击编辑字段标题
		bindDblClickEdit('label.field-title', 'field-title');
		bindDblClickEdit('span.field-view', 'field-view');
		bindDblClickEdit('.field-array-table tr.title-row th>span', 'field-title');
		
		//初始化默认数据
		if(tmplData && tmplData.groups){
			for(var i in tmplData.groups){
				var group = tmplData.groups[i];
				var $group = $tmplFieldGroup.tmpl(group).appendTo($groupContainer);
				if(!group.isArray){
					//绑定字段组内字段的拖动动作
					bindGroupFieldsDraggable(getFieldContainer($group));
				}
				//初始化字段组的字段搜索自动完成功能
				initGroupFieldSearchAutocomplete($group);
				for(var j in group.fields){
					var field = group.fields[j];
					if(field.validators){
						field.validators = field.validators.split(';');
					}
					appendFieldToGroup(field, $group, {
						isArrayField	: group.isArray == 1,
						relations		: group.relationSubdomain
					});
				}
			}
		}
		//字段的标题初始化，需要延迟，等到页面加载完之后执行
		setTimeout(function(){
			$('.field-title', $page).each(function(){adjustFieldTitle($(this))});
			$('#tmplName', $page).focus().select();
		}, 50);
		
		
	}
});
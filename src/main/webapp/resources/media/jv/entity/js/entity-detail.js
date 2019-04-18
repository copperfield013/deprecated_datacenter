define(function(require, exports, module){
	function defaultStatus(){
		return require('utils').createStatus({
			menu		: null,
			dtmpl		: null,
			premises	: null,
			actions		: null,
			entity		: null
		});
	}
	
	exports.init = function(_param){
		var defParam = {
			$page	: null,
			menuId	: null,
			code	: null,
			mode	: 'detail'
		}
		var param = $.extend({}, defParam, _param);
		var $page = param.$page;
		var $groupContainer = $('.group-container', $page);
		var status = defaultStatus();
		status.setStatus('mode', param.mode);
		
		var Ajax = require('ajax');
		var $CPF = require('$CPF');
		var DetailInput = require('entity/js/entity-detail-input.js');
		var Fetcher = require('field/js/field-option-fetcher.js');
		//覆盖渲染实体的普通字段
		var fieldOptionsFetcher = new Fetcher();
		$CPF.showLoading();
		require('tmpl').load('media/jv/entity/tmpl/entity-detail.tmpl').done(function(tmplMap){
			
			status
				.bind('dtmpl', renderFrame)
				.bind('premises', renderPremises)
				;
			switch(param.mode){
				case 'add':
					status
						.bind('dtmpl', renderTitle)
						.bind('dtmpl', renderDetailInputs)
						.bind('actions', renderActions)
						.bind('buttonStatus', renderSaveButton)
						.bind('entity', setDetailInputsValue)
					break;
				case 'update':
					status
						.bind('entity', renderTitle)
						.bind('dtmpl', renderDetailInputs)
						.bind('actions', renderActions)
						.bind('buttonStatus', renderSaveButton)
						.bind('entity', renderArrayItems)
						.bind('entity', setDetailInputsValue)
						.bind('normalFieldInputInited', setDetailInputsValue)
					break;
				case 'detail':
					status
						.bind('entity', renderTitle)
						.bind('entity', renderEntityDetail)
						.bind('entity', bindExport)
						.bind('history', renderHistory)
						.bind('historyId', setCurrentHistoryItem)
						;
					break;
			}
			
			//加载详情模板
			//渲染premises
			//渲染actions
			//如果传入了code，则加载实体数据，并且填充到页面中
			
			//初始化页面，加载模板后加载实体
			loadDetailTemplate().done(function(){
				switch(param.mode){
					//detail模式下，要加载entity和history
					case 'detail':
						loadHistory(1);
					//update模式下，只要加载entity
					case 'update':
						loadEntity();
				}
			})
			
			
			function loadDetailTemplate(){
				$CPF.showLoading();
				return Ajax.ajax('api2/meta/tmpl/dtmpl/' + param.menuId).done(function(data){
					status.setStatus(data, ['menu', 'dtmpl', 'premises', 'actions', 'buttonStatus']);
				});
			}
			
			function loadEntity(historyId){
				var reqParam = {};
				if(typeof historyId === 'number' || typeof historyId === 'string') reqParam.historyId = historyId;
				$CPF.showLoading();
				Ajax.ajax('api2/entity/curd/detail/' + param.menuId + '/' + param.code, reqParam).done(function(data){
					status.setStatus(data, ['entity', 'historyId']);
				});
				
			}
			
			function loadHistory(hisPageNo){
				Ajax.ajax('api2/entity/curd/history/' + param.menuId + '/' + param.code + '/1').done(function(data){
					status.setStatus(data, ['history']);
				});
			}
			
			function renderTitle(data){
				tmplMap['page-title']
					.replaceIn($page, this.properties);
			}
			
			function renderFrame(){
				$groupContainer.children().not('.entity-premises').remove();
				
				tmplMap['groups'].tmpl({
					groups	: status.getStatus('dtmpl').groups,
					mode	: param.mode
				}, {
					addArrayItemRow	: function(group){
						var $fieldGroup = $(this).closest('.field-group');
						var $tbody = $('tbody', $fieldGroup);
						var rownum = $('>tr', $tbody).length;
						var $row = createUpdateArrayItemRow(group, rownum);
						$tbody.append($row);
						refreshPagination($tbody, 'last');
					}
				}).appendTo($groupContainer);
				//bindTableEvent($page);
				//bindArrayItemEvents();
				bindIndexer($page);
			}
			
			
			function createUpdateArrayItemRow(group, rownum){
				var $row = tmplMap['create-array-item-row'].tmpl({
					group, rownum
				});
				tmplMap['group-field-input'].replaceFor(
					$('style[target="array-item-field-input"]', $row), {}, {}, 
					function($span, data, isLast){
						var field = data.data.field;
						createFieldInput(field, field.fieldName)
							.done(function(dom){
								$span.append(dom);
							});
					});
				return $row;
			}
			
			
			function renderPremises(){
				$groupContainer.children('.entity-premises').remove();
				tmplMap['premises'].tmpl({
					groupPremises	: status.getStatus('premises')
				}).prependTo($groupContainer);
			}
			
			function renderActions(){
				var actions = this.getStatus('actions');
				var innerActions = [], outgoingActions = [];
				
				$.each(actions, function(){
					if(this.outgoing === 1){
						outgoingActions.push(this)
					}else{
						innerActions.push(this);
					}
				});
				tmplMap['inner-actions'].replaceIn($page, {innerActions}, {doAction});
				tmplMap['outgoing-actions'].replaceIn($page, {outgoingActions}, {doAction});
				tmplMap['save-button'].replaceIn($page, {}, {doSave});
				
				
			}
			
			function renderSaveButton(){
				var buttonStatus = this.getStatus('buttonStatus');
			}
			
			function doAction(action){
				if(action){
					
				}
			}
			
			function doSave(){
				validate();
				var fuseMode = status.getStatus('fuseMode');
				var msg = '是否保存？';
				if(fuseMode){
					msg = '是否保存（当前为融合模式）';
				}
				require('dialog').confirm(msg).done(function(){
					var formData = getSubmitData();
					require('ajax').ajax('api2/entity/curd/save', formData);
				});
			}
			
			function validate(){
				
			}
			
			function getSubmitData(){
				var formData = new FormData();
				
				return formData;
			}
			
			/**
			 * 根据实体数据填充详情值
			 */
			function renderEntityDetail(){
				var entity = this.getStatus('entity');
				//覆盖渲染实体的普通字段
				tmplMap['group-field'].replaceIn($page, function(field){
					return {entity,field}
				});
				//覆盖渲染实体的数组字段
				tmplMap['array-item-rows'].replaceIn($page, function(group){
					return {entity, group}
				}, {}, function($rows){
					var $fieldGroup = $rows.closest('.field-group');
					//渲染覆盖数组字段表格的筛选器
					tmplMap['keyword-search-container'].replaceIn($fieldGroup, function(group){
						return {entity, group}
					})
					bindTableEvent($fieldGroup);
				});
				//绑定特殊类型字段的显示
				bindFieldInput($page);
				$CPF.closeLoading();
			}
			
			//根据详情模板来渲染表单
			function renderDetailInputs(){
				var dtmpl = this.getStatus('dtmpl');
				if(dtmpl){
					tmplMap['group-field-input'].replaceFor(
							$('style[target="group-field"]', $page), function(field){
								return {field};
							}, {}, function($span, data, isLast){
								var field = data.field;
								createFieldInput(field, data.field.fieldName)
									.done(function(dom){
										$span.append(dom);
										addInputValueSetSequence(this, field);
									}).progress(function(progress){
										if(progress === 'prepared'){
											if(isLast){
												console.warn('commit');
												fieldOptionsFetcher.commit().done(function(){
													status.setStatus('normalFieldInputInited', true);
												});
											}
										}
									});
								
							})
							;
				}
			}

			

			
			function createFieldInput(field, fieldName){
				//构造表单控件对象
				var detailInput = new DetailInput({
					type				: field.type,
					fieldId				: field.fieldId,
					optionsKey			: field.optionGroupKey,
					fieldOptionsFetcher,
					$container			: $page.find('.field-input-container')
				});
				detailInput.setName(fieldName);
				return detailInput.renderDOM();
			}

			function renderArrayItems(){
				var entity = this.getStatus('entity');
				//覆盖渲染实体的数组字段
				tmplMap['update-array-item-rows']
					.replaceFor($('style[target="array-item-rows"]', $page), function(group){
					return {entity, group}
				}, {}, function($rows){
					var $fieldGroup = $rows.closest('.field-group');
					$rows.filter('tr').each(function(){
						var $row = $(this);
						tmplMap['group-field-input'].replaceFor(
								$('style[target="array-item-field-input"]', $row), {}, {}, 
								function($span, data, isLast){
									var field = data.data.field;
									var arrayItem = data.data.arrayItem;
									createFieldInput(field, field.fieldName)
										.done(function(dom){
											$span.append(dom);
											this.setValue(arrayItem.fieldMap[field.id]);
										});
								});
					});
					bindTableEvent($fieldGroup);
				});
			}
			
			var inputValueSetSequence = [];
			function addInputValueSetSequence(detailInput, field){
				inputValueSetSequence.push({detailInput, field});
			}

			function setDetailInputsValue(){
				var entity = this.getStatus('entity');
				var normalFieldInputInited = this.getStatus('normalFieldInputInited');
				if(entity && normalFieldInputInited){
					while(1){
						var f = inputValueSetSequence.shift();
						if(!f) break;
						var value = entity.fieldMap[f.field.id];
						f.detailInput.setValue(value);
					}
					$CPF.closeLoading();
				}
			}
			
			function renderErrors(){
				
			}
			
			function renderHistory(){
				var history = this.getStatus('history');
				if(history){
					var historyItems = toHistoryItems(history);
					//渲染按钮
					tmplMap['timeline-toggler']
						.replaceIn($page, this.properties);
					//渲染历史框
					tmplMap['timeline-area']
						.replaceIn($page, {
								historyItems,
								hasMore	: false
							}, {
								goHistory:	function(historyId){
									var $dd = $(this).closest('dd');
									if(!$dd.is('.current')){
										$CPF.showLoading();
										loadEntity(historyId);
									}
								}
							});
					status.setStatus('historyId');
				}
			}
			
			/**
			 * 根据当前的historyId设置历史时间轴中当前的节点
			 */
			function setCurrentHistoryItem(){
				var historyId = status.getStatus('historyId');
				if(historyId){
					$('#timeline-area .VivaTimeline>dl>dd', $page)
						.filter('.current')
						.removeClass('current')
						.end()
						.filter('dd[data-id="' + historyId + '"]')
						.addClass('current');
						
				}
			}
			
			/**
			 * 渲染并绑定导出详情按钮
			 */
			function bindExport(){
				var _this = this;
				tmplMap['btn-export'].replaceIn($page, this.properties, {
					doExport	: function(){
						var entity = _this.getStatus('entity');
						var historyId = _this.getStatus('historyId');
						var reqParam = {};
						if(historyId){
							reqParam.historyId = historyId;
						}
						if(entity.code){
							require('dialog').confirm('确认导出当前详情页？').done(function(){
								$CPF.showLoading();
								Ajax.ajax('api2/entity/export/detail/' + param.menuId + '/' + entity.code, reqParam, function(data){
									if(data.status === 'suc'){
										if(data.uuid){
											Ajax.download('api2/entity/export/download/' + data.uuid);
										}
									}
								}, {
									afterLoad	: function(){
										$CPF.closeLoading();
									}
								});
							});
						}
					}
				});
			}
			
		});
		
		
		
		function bindTableEvent($page){
			/**
			 * 排序
			 */
			$('.field-array-table table', $page).each(function(){
				var $table = $(this);
				var $orderHeads = $('>thead>tr>th.sorting', $table);
				var $tbody = $('>tbody', $table);
				$orderHeads.click(function(){
					var $thisHead = $(this);
					var colIndex = $('>thead>tr>th', $table).index($thisHead);
					var fieldType = $thisHead.attr('field-type');
					$orderHeads.not(this).filter('.sorting_desc,.sorting_asc').removeClass('sorting_desc sorting_asc');
					if($thisHead.is('.sorting_asc')){
						$thisHead.removeClass('sorting_asc').addClass('sorting_desc');
						sortTable($tbody, colIndex, 'desc', fieldType);
					}else if($thisHead.is('.sorting_desc')){
						$thisHead.removeClass('sorting_desc sorting_asc');
						sortTable($tbody);
					}else{
						$thisHead.removeClass('sorting_desc').addClass('sorting_asc');
						sortTable($tbody, colIndex, 'asc', fieldType);
					}
				});
			});
			
			//筛选
			$('.keyword-search-container :text', $page).on('input propertychang', function(){
				var $text = $(this);
				var keyword = $text.val();
				var $tbody = $text.closest('.field-group').find('.field-container .field-array-table tbody');
				$tbody.children('tr').each(function(i){
					var $row = $(this);
					var flag = keyword == '';
					$row.children('td').slice(1).each(function(){
						var $cell = $(this);
						if(!isExceptFilterCell($cell)){
							var cellText = $cell.data('origin-text');
							if(cellText == undefined){
								cellText = $cell.text();
							}
							if(keyword){
								if(cellText.indexOf(keyword) >= 0){
									var html = cellText.replace(keyword, '<k>' + keyword + '</k>');
									$cell.html(html);
									flag = true;
									$cell.data('origin-text', cellText);
								}
							}else{
								if($cell.data('origin-text')){
									$cell.html(cellText);
								}
							}
						}
					});
					$row.toggleClass('hidden-row', !flag);
				});
				refreshPagination($tbody);
				return false;
			});
			
			function isExceptFilterCell($cell){
				return $cell.is('[field-type="file"]');
			}
			
			
			
			function sortTable($tbody, colIndex, orderDir, fieldType){
				var datas = [];
				var $rows = $tbody.children('tr').not('.hidden-row').each(function(i){
					var $row = $(this);
					var $orderCol = $row.children('td').eq(colIndex);
					datas.push({
						data	: $orderCol.text(),
						index	: i,
						order	: $row.attr('origin-order')
					});
				});
				
				for(var i = 0; i < datas.length; i++){
					for(var j = i + 1; j < datas.length; j++){
						if(orderDir && shouldSwap(datas[i].data, datas[j].data, orderDir, fieldType)
							|| !orderDir && shouldSwap(datas[i].order, datas[j].order, 'asc')){
							var t = datas[i];
							datas[i] = datas[j];
							datas[j] = t;
						}
					}
				}
				
				for(var i = 0; i < datas.length; i++){
					var $row = $rows.eq(datas[i].index);
					$row.children('td').eq(0).text(i + 1);
					$tbody.append($row);
				}
				
				refreshPagination($tbody);
			}
			
			function shouldSwap(x, y, orderDir, fieldType){
				var FieldInput = require('field/js/field-input.js');
				if(orderDir === 'asc'){
					return FieldInput.compare(x, y, fieldType) > 0;
				}else if(orderDir === 'desc'){
					return FieldInput.compare(x, y, fieldType) < 0;
				}
			}
			
			/**
			 * 分页
			 */
			$('.field-array-table>table', $page).each(function(){
				refreshPagination($('>tbody', this))
			});
		}
		

		var pageSize = 5;
		function refreshPagination($tbody, goPageNo){
			var $rows = $tbody.children('tr');
			var count = $rows.not('.hidden-row').each(function(i){
				var $indexCell = $(this).children('td').eq(0);
				$indexCell.text(i + 1);
			}).length;
			
			var $widgetHeader = $tbody.closest('.field-group').find('>div.widget-header');
			var $paginationContainer = $widgetHeader.find('>div.pagination-container');
			
			if(count <= pageSize){
				//不需要分页
				$paginationContainer.remove();
				$tbody.children('tr').addClass('show-page-row');
			}else{
				if($paginationContainer.length == 0){
					$paginationContainer = buildPaginationContainer();
					$widgetHeader.append($paginationContainer);
				}
				var $paginationList = $paginationContainer.children('ul');
				var $firstLi = $paginationList.children('li.page-first');
				$firstLi.nextUntil('li.page-last').remove();
				//刷新后的页号
				
				var pageCount = Math.ceil(count / pageSize);
				
				for(var i = pageCount; i >= 1; i--){
					var $pageLi = $('<li><a href="#">' + i + '</a></li>');
					$pageLi.children('a').click(function(){
						goPage(parseInt($(this).text()), $paginationList, $tbody);
						return false;
					});
					$firstLi.after($pageLi);
				}
				if(goPageNo === 'last'){
					goPageNo = pageCount;
				}else{
					goPageNo = goPageNo || 1;
				}
				goPage(goPageNo, $paginationList, $tbody);
			}
			
			
		}
		
		function goPage(pageNo, $paginationList, $tbody){
			//显示的最多页码个数
			var maxPaginator = 3;
			var $pageNos = $paginationList.children('li').removeClass('hidden-paginator').not('.page-first,.page-last');
			$pageNos.removeClass('active');
			if($pageNos.length > maxPaginator){
				var half = Math.ceil(maxPaginator / 2);
				if(pageNo >= half){
					$pageNos.slice(0, pageNo - half).addClass('hidden-paginator');
				}
				$pageNos.slice(pageNo + half - 1).addClass('hiden-paginator');
			}
			$pageNos.eq(pageNo - 1).addClass('active');
			$tbody.children('tr').removeClass('show-page-row');
			var start = (pageNo - 1) * pageSize;
			$tbody.children('tr').not('.hidden-row').slice(start, start + pageSize).addClass('show-page-row');
			var $goFirst = $paginationList.children('.page-first').removeClass('disabled').off('click'),
				$goLast = $paginationList.children('.page-last').removeClass('disabled').off('click');
			if(pageNo == 1){
				$goFirst.addClass('disabled');
			}else{
				$goFirst.click(function(){goPage(1, $paginationList, $tbody)})
			}
			if(pageNo == $pageNos.length){
				$goLast.addClass('disabled');
			}else{
				$goLast.click(function(){goPage($pageNos.length, $paginationList, $tbody)})
			}
		}
		
		function buildPaginationContainer(){
			var $container = $('<div class="widget-buttons pagination-container">'
					+ '<ul class="pagination pagination-sm">'
					+ '<li class="page-first disabled"><a href="#">«</a></li>'
					+ '<li class="page-last"><a href="#">»</a></li>'
					+ '</ul></div>');
			return $container;
			
		}
		
		
		function bindIndexer($page){
			var Indexer = require('indexer')
			var indexer = new Indexer({
				scrollTarget: $page.closest('.main-tab-content')[0],
				elements	: $('.group-container>.field-group', $page),
				titleGetter	: function(ele){
					return $(this).find('.group-title').text();
				},
				offsetGetter: function(){
					var $this = $(this);
					var thisOffsetTop = $this[0].offsetTop;
					var top = 0;
					if($this[0].offsetParent){
						top = $this[0].offsetParent.offsetTop;
					}
					return thisOffsetTop + top;
				}
			});
			$page.append(indexer.getContainer());
			indexer.triggerScroll();
		}
		
		function bindFieldInput($page){
			var FieldInput = require('field/js/field-input.js');
			$('.field-view[field-type],.value-row>td[field-type]', $page).each(function(){
				var $this = $(this);
				var type = $this.attr('field-type');
				switch(type){
					case 'file':
						var src = $this.text().trim();
						var fieldInput = new FieldInput({
							type	: 'file',
							value	: src,
							readonly: true
						});
						$this.empty().append(fieldInput.getDom());
						break;
					default:
				}
			});
		}
	}
	
	
	function toHistoryItems(history){
		var Utils = require('utils');
		var historyItems = [];
		var monthKeyMap = {};
		var monthes = [];
		$.each(history, function(){
			var thisMonthArray = monthKeyMap[this.monthKey]; 
			if(!thisMonthArray){
				monthes.push(this.monthKey);
				thisMonthArray = monthKeyMap[this.monthKey] = [];
			}
			var index = 0;
			for(var j = thisMonthArray.length - 1; j >= 0; j--){
				if(thisMonthArray[j].time > this.time){
					index = j + 1;
					break;
				}
			}
			thisMonthArray.splice(index, 0,{
				id		: this.id,
				time	: this.time,
				timeStr	: Utils.formatDate(new Date(this.time), 'yyyy-MM-dd hh:mm:ss'),
				userName: this.userName,
				current	: this.current
			});
		});
		$.each(monthes.sort(sequence), function(){
			var monthKey = this;
			historyItems.push({
				monthTime	: monthKey,
				monthStr	: Utils.formatDate(new Date(monthKey), 'yyyy年MM月')
			});
			$.merge(historyItems, monthKeyMap[monthKey]);
		});
		return historyItems;
	}
	function sequence(a, b){
		return parseInt(b) - parseInt(a);
	}
});
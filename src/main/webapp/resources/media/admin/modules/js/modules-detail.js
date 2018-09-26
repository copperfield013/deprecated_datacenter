define(function(require, exports, module){
	"use strict";
	var Dialog = require('dialog'),
		Ajax = require('ajax'),
		$CPF = require('$CPF'),
		DtmplUpdate = require('tmpl/js/dtmpl-update.js'),
		Utils = require('utils')
	
	exports.init = function($page, moduleName, entityCode, menuId, historyId){
		if(entityCode === ''){
			Dialog.notice('数据不存在', 'warning');
			$('.header-title h1').text('数据不存在');
		}
		
		var timelineInited = false;
		$('a.toggle-timeline', $page).click(function(){
			if(!timelineInited){
				$('.show-more-history', $page).trigger('click');
			}
		});
		
		//下一页
		var curPageNo = 0;
		$('.show-more-history', $page).click(function(){
			var $this = $(this);
			if(!$this.is('.disabled')){
				$this.addClass('disabled').text('加载中');
				Ajax.ajax('admin/modules/curd/paging_history/' + menuId + '/' + entityCode, {
					pageNo	: curPageNo + 1
				}, function(data){
					if(data.status === 'suc'){
						appendHistory(data.history);
						curPageNo ++;
						timelineInited = true;
						if(!historyId && $('.VivaTimeline>dl>dd.current', $page).length == 0){
							$('.VivaTimeline>dl>dd', $page).first().addClass('current');
						}
					}
					if(data.isLast){
						$this.text('没有更多了');					
					}else{
						$this.text('查看更多').removeClass('disabled');
					}
				});
			}
		});
		$('.VivaTimeline', $page).on('click', '.circ', function(){
			var hid = parseInt($(this).closest('dd').attr('data-id'));
			$page.getLocatePage().loadContent('admin/modules/curd/detail/' + menuId + '/' + entityCode, null, {historyId:hid});
			
		});
		
		function appendHistory(history){
			if(history.length > 0){
				var $dl = $('dl', $page);
				
				for(var i in history){
					var item = history[i];
					var $month = $('dt[data-month="' + item.monthKey + '"]', $dl);
					if($month.length == 0){
						var month = new Date(item.monthKey);
						$month = $('<dt data-month="' + item.monthKey + '">').text(Utils.formatDate(month, 'yyyy年MM月'));
						var inserted = false;
						$('dt', $dl).each(function(){
							var thisMonth = parseInt($(this).attr('data-month'));
							if(thisMonth <= month){
								$month.insertBefore(this);
								inserted = true;
								return false;
							}
						});
						if(!inserted){
							$('.show-more-history', $page).parent('dt').before($month);
							//$dl.append($month);
						}
					}
					var $item = $(
							'<dd class="pos-right clearfix">' +
								'<div class="circ"></div>' + 
								'<div class="time"></div>' +
								'<div class="events">' + 
			 						'<div class="events-header"></div>' + 
									'<div class="events-body"></div>' + 
								'</div>' +
							'</dd>');
					$item.find('.time').text(Utils.formatDate(new Date(item.timeKey), 'yyyy-MM-dd hh:mm:ss'));
					$item.find('.events-header').text('操作人：' + item.userName);
					$item.find('.events-body').text('详情');
					$item.attr('data-id', item.id).attr('data-time', item.timeKey);
					var inserted = false;
					var $dds = $month.nextUntil('dt');
					if($dds.length > 0){
						$dds.each(function(){
							var $this = $(this);
							if($this.is('dd[data-time]')){
								var thisTimeKey = parseInt($this.attr('data-time'));
								if(thisTimeKey <= item.timeKey){
									$item.insertBefore(this);
									inserted = true;
									return false;
								}
							}
						});
						if(!inserted){
							$dds.last().after($item);
						}
					}else{
						$month.after($item);
					}
				}
				var $dd = $('dd', $dl);
				var checked = false;
				$dd.each(function(i){
					var $this = $(this);
					if(!checked){
						var hid = $this.attr('data-id');
						if(historyId == hid){
							$this.addClass('current');
							checked = true;
						}
					}
					Utils.switchClass($this, 'pos-right', 'pos-left', i % 2 == 0);
				});
				
			}
		}
		
		
		$('#datetime', $page).datetimepicker({
			language	: 'zh-CN',
			format		: 'yyyy-mm-dd hh:ii:ss',
			minuteStep	: 1,
			autoclose	: true,
			startView	: 'day'
		}).on('changeDate', function(e){
			$page.getLocatePage().loadContent('admin/modules/curd/detail/' + menuId + '/' + entityCode, undefined, {
				datetime	: $(this).val()
			});
		});
		var $errors = $('#errors', $page);
		$('#showErrors', $page).mouseenter(function(e){
			$errors.show();
		});
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
		
		$('#btn-export', $page).click(function(){
			Dialog.confirm('确认导出当前详情页？', function(yes){
				if(yes){
					$CPF.showLoading();
					Ajax.ajax('admin/modules/export/export_detail/' + menuId + '/' + entityCode, {
						historyId	: historyId
					}, function(data){
						if(data.status === 'suc'){
							if(data.uuid){
								Ajax.download('admin/modules/export/download/' + data.uuid);
							}
						}
					}, {
						afterLoad	: function(){
							$CPF.closeLoading();
						}
					});
				}
			})
		});
		
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
		
		setTimeout(function(){
			$CPF.showLoading();
			$('.field-title', $page).each(function(){DtmplUpdate.adjustFieldTitle($(this))});
			$CPF.closeLoading();
		}, 100);
	}
	
	function sortTable($tbody, colIndex, orderDir, fieldType){
		var datas = [];
		var $rows = $tbody.children('tr').each(function(i){
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
		
	}
	
	function shouldSwap(x, y, orderDir, fieldType){
		var FieldInput = require('field/js/field-input.js');
		if(orderDir === 'asc'){
			return FieldInput.compare(x, y, fieldType) > 0;
		}else if(orderDir === 'desc'){
			return FieldInput.compare(x, y, fieldType) < 0;
		}
	}
	
	
	
	
	
});
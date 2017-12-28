<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/peopledata/css/peopledata-detail-tmpl.css" />
<div class="detail peopledata-detail-tmpl" id="peopledata-detail-tmpl-${peopleCode }">
	<div class="page-header">
		<div class="header-title">
			<h1>${people.name }-详情</h1>
			<c:if test="${people.errors != null && fn:length(people.errors) > 0 }">
			    <h1 id="showErrors" class="fa fa-info-circle" style="cursor: pointer;color: #CD5C5C;"></h1>
			</c:if>
		</div>
		<div class="history-container title-operate">
			<a href="#" title="查看历史" class="toggle-timeline"><i class="iconfont icon-historyrecord"></i></a>
		</div>
		<div class="template-container title-operate">
			<a href="#" title="查看模板" class="toggle-template"><i class="iconfont icon-template"></i></a>
		</div>
	</div>
	<div class="page-body">
		<div class="col-lg-offset-1 col-lg-10">
			<form class="form-horizontal group-container">
				<c:forEach var="tmplGroup" items="${tmpl.groups }">
					<div class="widget field-group">
						<div class="widget-header">
							<span class="widget-caption">
								<span class="group-title">${tmplGroup.title }</span>
							</span>
						</div>
						<div class="widget-body field-container">
							<c:forEach var="tmplField" items="${tmplGroup.fields }">
								<div class="form-group field-item ${tmplField.colNum == 2? 'dbcol': '' }">
									<label class="control-label field-title">${tmplField.title }</label>
									<div class="field-value">
										<span class="field-view">${parser[tmplField.fieldName] }</span>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</form>
		</div>
	</div>
	<div id="errors" style="display: none;">
		<ul>
			<c:forEach items="${people.errors }" var="error">
				<li>${error.error_str }</li>
			</c:forEach>
		</ul>
   </div>
	<div id="timeline-area" class="cpf-static-view" style="display: none;">
		<div class="timeline-wrapper">
			<div class="VivaTimeline">
				<dl>
					<dt><a href="#" class="show-more-history">查看更多</a></dt>
				</dl>
			</div>
		</div>
	</div>
	<div id="tmpl-list" style="display: none;">
		<ul class="tmpl-list-wrapper">
			<c:if test="${tmpl != null }">
				<li data-id="${tmpl.tmplId }" class="active">
					<span class="tmpl-icon"><i class="fa fa-lightbulb-o"></i></span>
					<span class="tmpl-item-body">
						<span class="tmpl-name">${tmpl.name }</span>
						<span class="tmpl-date"><fmt:formatDate value="${tmpl.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
					</span>
				</li>
			</c:if>
			<c:forEach var="tmplItem" items="${tmplList }">
				<c:if test="${tmplItem.tmplId != tmpl.tmplId }">
					<li data-id="${tmplItem.tmplId }">
						<span class="tmpl-icon"><i class="fa fa-lightbulb-o"></i></span>
						<span class="tmpl-item-body">
							<span class="tmpl-name">${tmplItem.name }</span>
							<span class="tmpl-date"><fmt:formatDate value="${tmplItem.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
						</span>
					</li>
				</c:if>
			</c:forEach>
		</ul>
		<div class="tmpl-operate">
			<a class="tab" title="配置模板" target="viewtmpl_list" href="admin/peopledata/viewtmpl/list"><i class="icon glyphicon glyphicon-cog"></i></a>
		</div>
	</div>
</div>
<!-- <script type="text/javascript" src="media/admin/peopledata/js/peopledata-detail-tmpl.js"></script> -->
<script>
	seajs.use(['dialog', 'ajax', 'utils', 'peopledata/js/viewtmpl-update.js', '$CPF'], function(Dialog, Ajax, Utils, ViewTmpl, $CPF){
		var $page = $('#peopledata-detail-tmpl-${peopleCode }');
		var hasRecord = '${people != null}';
		if(hasRecord != 'true'){
			Dialog.notice('数据不存在', 'warning');
			$('.header-title h1').text('数据不存在');
		}
		$('a.toggle-history', $page).click(function(){
			$(this).closest('.toggle-history').hide();
			$('.header-buttons', $page).show();
		});
		var $timelineArea = $('#timeline-area');
		var timelineInited = false;
		$('a.toggle-timeline', $page).click(function(){
			$timelineArea.show();
			if(!timelineInited){
				$('.show-more-history', $timelineArea).trigger('click');
			}
		});
		
		//下一页
		var curPageNo = 0;
		$('.show-more-history', $timelineArea).click(function(){
			var $this = $(this);
			if(!$this.is('.disabled')){
				$this.addClass('disabled').text('加载中');
				Ajax.ajax('admin/peopledata/paging_history/${peopleCode}', {
					pageNo	: curPageNo + 1
				}, function(data){
					if(data.status === 'suc'){
						appendHistory(data.history);
						curPageNo ++;
						timelineInited = true;
					}
					if(data.isLast){
						$this.text('没有更多了');					
					}else{
						$this.text('查看更多').removeClass('disabled');
					}
				});
			}
		});
		$page.on('click', '.circ', function(){
			var time = parseInt($(this).closest('dd').attr('data-time'));
			$page.getLocatePage().loadContent('admin/peopledata/detail_tmpl/${peopleCode}?tmplId=${tmpl.tmplId}', null, {timestamp:time});
			
		});
		var theTime = parseInt('${date.time}');
		function appendHistory(history){
			if(history.length > 0){
				var $dl = $('dl', $timelineArea);
				
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
							$('.show-more-history', $timelineArea).parent('dt').before($month);
							//$dl.append($month);
						}
					}
					$item = $(
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
						var thisTime = parseInt($this.attr('data-time'));
						if(theTime >= thisTime){
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
			$page.getLocatePage().loadContent('admin/peopledata/detail_tmpl/${peopleCode }', undefined, {
				datetime	: $(this).val(),
				tmplId		: '${tmpl.tmplId}'
			});
		});
		var $errors = $('#errors', $page);
		$('#showErrors', $page).mouseenter(function(e){
			$errors.show();
		});
		$page.click(function(e){
			var $target = $(e.target);
			if(!$target.is('#showErrors') && $target.closest('#errors').length == 0){
				$errors.hide();
			}
			if($target.closest('#tmpl-list').length == 0){
				$('#tmpl-list', $page).hide();
			}
			if($target.closest('#timeline-area').length == 0
				&& $target.closest('.toggle-timeline').length == 0 ){
				$timelineArea.hide();
			}
		});
		
		
		$('.toggle-template i', $page).click(function(){
			$('#tmpl-list', $page).toggle();
			return false;
		});
		$('#tmpl-list li[data-id]:not(.active)', $page).click(function(){
			var tmplId = $(this).attr('data-id');
			$page.getLocatePage().loadContent('admin/peopledata/detail_tmpl/${peopleCode}', undefined, {
				timestamp	: '${timestamp}',
				tmplId		: tmplId
			});
		});
		
		if('${tmpl == null}' == 'true'){
			Dialog.notice('当前没有选择默认模板', 'error');
		}
		
		setTimeout(function(){
			$CPF.showLoading();
			$('.field-title', $page).each(function(){ViewTmpl.adjustFieldTitle($(this))});
			$CPF.closeLoading();
		}, 100);
		
	});
</script>
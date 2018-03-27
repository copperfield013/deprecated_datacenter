<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/modules/css/modules-detail-tmpl.css" />
<div class="detail entity-detail-tmpl" id="entity-detail-tmpl-${entity.code }">
	<div class="page-header">
		<div class="header-title">
			<h1>${module.title}-${entity.title }-详情</h1>
			<c:if test="${entity.errors != null && fn:length(entity.errors) > 0 }">
			    <h1 id="showErrors" class="fa fa-info-circle" style="cursor: pointer;color: #CD5C5C;"></h1>
			</c:if>
		</div>
		<div class="history-container title-operate">
			<a href="page:#timeline-area.toggle" title="查看历史" class="toggle-timeline btn-toggle"><i class="iconfont icon-historyrecord"></i></a>
		</div>
		<div class="template-container title-operate">
			<a class="btn-toggle toggle-template" title="查看模板" href="page:#tmpl-list.toggle" ><i class="iconfont icon-template"></i></a>
		</div>
	</div>
	<div class="page-body">
		<div class="col-lg-offset-1 col-lg-10">
			<form class="form-horizontal group-container">
				<c:forEach var="tmplGroup" items="${dtmpl.groups }">
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
										<span class="field-view">${entity.map[tmplField.fieldName] }</span>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</form>
		</div>
	</div>
	<div id="errors" class="blur-hidden" style="display: none;">
		<ul>
			<c:forEach items="${entity.errors }" var="error">
				<li>${error.error_str }</li>
			</c:forEach>
		</ul>
   </div>
	<div id="timeline-area" class="blur-hidden" style="display: none;">
		<div class="timeline-wrapper">
			<div class="VivaTimeline">
				<dl>
					<dt><a href="#" class="show-more-history">查看更多</a></dt>
				</dl>
			</div>
		</div>
	</div>
	<div id="tmpl-list" class="blur-hidden" style="display: none;">
		<ul class="tmpl-list-wrapper">
			<c:if test="${dtmpl != null }">
				<li data-id="${dtmpl.id }" class="active">
					<span class="tmpl-icon"><i class="fa fa-lightbulb-o"></i></span>
					<span class="tmpl-item-body">
						<span class="tmpl-name">${dtmpl.title }</span>
						<span class="tmpl-date"><fmt:formatDate value="${dtmpl.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
					</span>
				</li>
			</c:if>
			<c:forEach var="dtmplItem" items="${dtmpls }">
				<c:if test="${dtmplItem.id != dtmpl.id }">
					<li data-id="${dtmplItem.id }">
						<span class="tmpl-icon"><i class="fa fa-lightbulb-o"></i></span>
						<span class="tmpl-item-body">
							<span class="tmpl-name">${dtmplItem.title }</span>
							<span class="tmpl-date"><fmt:formatDate value="${dtmplItem.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
						</span>
					</li>
				</c:if>
			</c:forEach>
		</ul>
		<div class="tmpl-operate">
			<a class="tab" title="配置模板" target="viewtmpl_list" href="admin/tmpl/dtmpl/list/people"><i class="icon glyphicon glyphicon-cog"></i></a>
		</div>
	</div>
</div>
<script>
	seajs.use(['dialog', 'ajax', 'utils', 'tmpl/js/dtmpl-update.js', '$CPF'], function(Dialog, Ajax, Utils, ViewTmpl, $CPF){
		var $page = $('#entity-detail-tmpl-${entity.code }');
		var hasRecord = '${entity != null}';
		if(hasRecord != 'true'){
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
				Ajax.ajax('admin/modules/curd/paging_history/${module.key}/${entity.code}', {
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
			$page.getLocatePage().loadContent('admin/modules/curd/detail/${module.key}/${entity.code}?tmplId=${tmpl.id}', null, {timestamp:time});
			
		});
		var theTime = parseInt('${date.time}');
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
			$page.getLocatePage().loadContent('admin/modules/curd/detail/${module.key}/${entity.code }', undefined, {
				datetime	: $(this).val(),
				tmplId		: '${tmpl.id}'
			});
		});
		var $errors = $('#errors', $page);
		$('#showErrors', $page).mouseenter(function(e){
			$errors.show();
		});
		$('#tmpl-list li[data-id]:not(.active)', $page).click(function(){
			var tmplId = $(this).attr('data-id');
			$page.getLocatePage().loadContent('admin/modules/curd/detail/${module.key}/${entity.code}', undefined, {
				timestamp	: '${timestamp}',
				tmplId		: tmplId
			});
		});
		
		if('${dtmpl == null}' == 'true'){
			Dialog.notice('当前没有选择默认模板', 'error');
		}
		
		setTimeout(function(){
			$CPF.showLoading();
			$('.field-title', $page).each(function(){ViewTmpl.adjustFieldTitle($(this))});
			$CPF.closeLoading();
		}, 100);
		
	});
</script>
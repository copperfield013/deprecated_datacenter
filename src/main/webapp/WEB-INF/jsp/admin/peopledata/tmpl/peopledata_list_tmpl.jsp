<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/peopledata/css/peopledata-list-tmpl.css" />
<div id="peopledata-list-tmpl" class="detail">
	<div class="page-header">
		<div class="header-title">
			<h1>人口列表</h1>
		</div>
		<div class="header-buttons">
			<a title="切换模板" class="btn-toggle" href="page:#tmpl-list.toggle">
				<i class="iconfont icon-template"></i>
			</a>
			<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
				<i class="glyphicon glyphicon-refresh"></i>
			</a>
			<a class="export btn-toggle" title="导出" id="btn-export " href="page:#export-window.toggle">
				<i class="glyphicon glyphicon-export"></i>
			</a>
			<a class="import tab" href="admin/peopledata/import" title="导入" target="people_import">
				<i class="glyphicon glyphicon-import"></i>
			</a>
		</div>
	</div>
	<div class="page-body">
		<form class="form-inline"  action="admin/peopledata/tmpl/list">
			<input type="hidden" id="tmplId" name="tmplId" value="${ltmpl.id }" />
			<c:if test="${not empty ltmpl.criterias }">
				<c:forEach var="criteriaItem" items="${ltmpl.criterias }">
					<c:if test="${criteriaItem.inputType == 'text' }">
						<div class="form-group">
							<label class="control-label">${criteriaItem.title }</label>
							<input class="form-control" type="text" name="criteria_${criteriaItem.id }" value="${vCriteriaMap[criteriaItem.id].value}" placeholder="${criteriaItem.placeholder }" />
						</div>
					</c:if>
				</c:forEach>
				<button type="submit" class="btn btn-default">查询</button>
			</c:if>
		</form>
		<div class="row list-area">
			<table class="table">
				<thead>
					<tr>
						<c:forEach items="${ltmpl.columns }" var="column">
							<th>${column.title }</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${parserList }" var="parser" varStatus="i">
						<tr>
							<c:forEach items="${ltmpl.columns }" var="column" varStatus="j" >
								<td>
									<c:choose >
										<c:when test="${column.specialField == 'number' }">
											${i.index + 1 }
										</c:when>
										<c:when test="${fn:startsWith(column.specialField, 'operate')}">
											<c:if test="${fn:contains(column.specialField, '-d') }">
												<a href="admin/peopledata/detail_tmpl/${parser['peopleCode']}" 
													target="people_detail_${parser['peopleCode'] }" 
													title="详情-${item.name }"
													class="tab btn btn-success btn-xs">
													<i class="fa fa-book"></i>详情
												</a>
											</c:if>
											<c:if test="${fn:contains(column.specialField, '-u') }">
												<a target="people_update_${parser['peopleCode'] }" 
													title="修改-${parser['name'] }" 
													href="admin/peopledata/update_tmpl/${parser['peopleCode'] }" 
													class="tab btn btn-info btn-xs edit">
													<i class="fa fa-edit"></i>修改
												</a>
											</c:if>
											<c:if test="${fn:contains(column.specialField, '-r') }">
												<a confirm="确认删除？"
													href="admin/peopledata/do_delete/${parser['peopleCode'] }" 
													class="btn btn-danger btn-xs delete">
													<i class="fa fa-trash-o"></i>删除
												</a>
											</c:if>
										</c:when>
										<c:otherwise>
											${parser[column.fieldKey] }
										</c:otherwise>
									</c:choose>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
		</div>
	</div>
	<div id="tmpl-list" class="detail-toggle-sublist blur-hidden" style="display: none;">
		<div class="detail-toggle-sublist-wrapper">
			<c:forEach items="${ltmplList }" var="ltmplItem">
				<a data-id="${ltmplItem.id }" class="${ltmplItem.id == ltmpl.id? 'active': '' }">
					<span class="detail-toggle-sublist-icon"><i class="fa fa-lightbulb-o"></i></span>
					<span class="detail-toggle-sublist-item-body">
						<span class="detail-toggle-sublist-item-name">${ltmplItem.title }</span>
						<span class="detail-toggle-sublist-item-date"><fmt:formatDate value="${ltmplItem.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
					</span>
				</a>
			</c:forEach>
		</div>
		<div class="detail-toggle-sublist-operate">
			<a class="tab" title="配置模板" target="ltmpl_list" href="admin/tmpl/ltmpl/list">
				<i class="icon glyphicon glyphicon-cog"></i>
			</a>
		</div>
	</div>
	<div id="export-window" class="detail-toggle-sublist blur-hidden" style="display: none;">
		<div class="detail-toggle-sublist-wrapper">
			<div class="export-window-title">
				<h3>导出</h3>
			</div>
			<div class="row range-toggle">
				<label class="col-lg-6">
					<input id="export-current-page" type="radio" class="colored-blue" checked="checked">
					<span class="text">导出当前页</span>
				</label>
				<label class="col-lg-6">
					<input id="export-all" type="radio" class="colored-blue">
					<span class="text">导出所有</span>
				</label>
			</div>
			<div class="row data-range" style="display: none;">
				<label class="col-lg-4">数据范围：</label>
				<div class="col-lg-8">
					<input type="number" disabled="disabled" title="不填写开始序号时，将从1开始" id="export-range-start" placeholder="开始序号" />
					-
					<input type="number" disabled="disabled" title="不填写结束序号时，将到最后结束" id="export-range-end" placeholder="结束序号" />
				</div>
			</div>
			<div class="row export-operate-area">
				<div class="col-lg-offset-4 col-lg-2">
					<input type="button" id="do-export" class="btn btn-xs btn-primary" value="开始导出" />
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['utils', 'ajax'], function(Utils, Ajax){
		var $page = $('#peopledata-list-tmpl');
		$('#tmpl-list a[data-id]:not(.active)').click(function(){
			var $this = $(this);
			$('#tmplId', $page).val($this.attr('data-id'));
			$('form', $page).submit();
		});
		+function(){
			var $exportAll = $('#export-all', $page),
				$exportCur = $('#export-current-page', $page);
			$exportAll.change(function(e, flag){
				var checked = $exportAll.prop('checked');
				if(!flag){
					$exportCur.prop('checked', !checked).trigger('change', [true]);
				}
			});
			
			$exportCur.change(function(e, flag){
				var checked = $exportCur.prop('checked');
				if(!flag){
					$exportAll.prop('checked', !checked).trigger('change', [true]);
				}
				var $dataRange = $('.data-range', $page);
				if(checked){
					$dataRange.hide();
				}else{
					$dataRange.show();
				}
			});
			
			$('#do-export', $page).click(function(){
				var scope = $exportAll.prop('checked')? 'all': $exportCur.prop('checked')? 'current': null;
				if(scope){
					var rangeStart = $('#export-range-start', $page).val() || undefined,
						rangeEnd = $('#export-range-start', $page).val() || undefined;
					Ajax.ajax('admin/peopledata/export/do_export', {
						scope		: scope,
						rangeStart	: rangeStart,
						rangeEnd	: rangeEnd
					}, function(data){
						if(data.uuid){
							var statusTimer = setInterval(function(){
								Ajax.ajax('admin/peopledata/export/status_export', {
									uuid	: data.uuid
								}, function(res){
									if(res.status === 'suc'){
										var progress = res.current/data.totalCount;
										
									}else{
										clearInterval(statusTimer);
									}
								});
							}, 1000);
						}
					});
				}else{
					$.error('导出范围不能为null');
				}
				
			});
			
			
		}();
	});
</script>
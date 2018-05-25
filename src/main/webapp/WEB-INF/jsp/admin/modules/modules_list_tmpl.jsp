<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/modules/css/modules-list-tmpl.css" />
<div id="${module.name }-list-tmpl-${RES_STAMP}" class="detail module-list-tmpl">
	<div class="page-header">
		<div class="header-title">
			<h1>${module.title }列表</h1>
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
			<a class="import tab" href="admin/modules/import/go/${module.name }" title="导入" target="module_${module.name }_import">
				<i class="glyphicon glyphicon-import"></i>
			</a>
			<a class="tab" href="admin/modules/curd/add/${module.name }?tg=${templateGroup.key }" title="创建${module.title }" target="module_${module.name }_add">
				<i class="fa fa-plus"></i>
			</a>
		</div>
	</div>
	<div class="page-body">
		<form class="form-inline"  action="admin/modules/curd/list/${module.name }">
			<input type="hidden" id="tmplId" name="tmplId" value="${ltmpl.id }" />
			<input type="hidden" name="tg" value="${templateGroup.key }"  />
			<c:if test="${not empty ltmpl.criterias }">
				<c:forEach var="criteriaItem" items="${ltmpl.criterias }">
					<c:if test="${criteriaItem.queryShow != null }">
						<div class="form-group">
							<label class="control-label">${criteriaItem.title }</label>
							<c:choose>
								<c:when test="${criteriaItem.inputType == 'text' }">
									<input class="form-control" type="text" name="criteria_${criteriaItem.id }" value="${vCriteriaMap[criteriaItem.id].value}" placeholder="${criteriaItem.placeholder }" />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'select' }">
									<select class="form-control" name="criteria_${criteriaItem.id }" data-value="${vCriteriaMap[criteriaItem.id].value}">
										<option value="">--请选择--</option>
										<c:forEach var="option" items="${criteriaOptionsMap[criteriaItem.fieldId]}">
											<option value="${option.value }">${option.title}</option>
										</c:forEach>								
									</select>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'date' }">
									<input class="form-control datepicker" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${vCriteriaMap[criteriaItem.id].value}"  />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'label' }">
									<select style="min-width: 14em;" class="cpf-select2 format-submit-value" name="criteria_${criteriaItem.id }" multiple="multiple" data-value="${vCriteriaMap[criteriaItem.id].value}">
										<c:forEach var="label" items="${labelsMap[criteriaItem.fieldKey].subdomain}">
											<option value="${label }">${label}</option>
										</c:forEach>								
									</select>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'daterange' }">
									<span class="cpf-daterangepicker format-submit-value" 
										data-name="criteria_${criteriaItem.id }" 
										data-value="${vCriteriaMap[criteriaItem.id].value}">
									</span>
								</c:when>
								<c:otherwise>
									<input type="text" disabled="disabled" placeholder="没有配置对应的控件${criteriaItem.inputType }" />
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>
				</c:forEach>
				<div class="form-group">
					<button type="submit" class="form-control btn btn-default" title="${hidenCriteriaDesc }">查询</button>
				</div>
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
												<a href="admin/modules/curd/detail/${module.name }/${parser.code}?tg=${templateGroup.key }" 
													target="module_detail_${parser.code }" 
													title="详情-${parser.title }"
													class="tab btn btn-success btn-xs">
													<i class="fa fa-book"></i>详情
												</a>
											</c:if>
											<c:if test="${fn:contains(column.specialField, '-u') }">
												<a target="module_update_${parser.code }" 
													title="修改-${parser.title }" 
													href="admin/modules/curd/update/${module.name }/${parser.code }?tg=${templateGroup.key }" 
													class="tab btn btn-info btn-xs edit">
													<i class="fa fa-edit"></i>修改
												</a>
											</c:if>
											<c:if test="${fn:contains(column.specialField, '-r') }">
												<a confirm="确认删除？"
													href="admin/modules/curd/delete/${module.name }/${parser.code }" 
													class="btn btn-danger btn-xs delete">
													<i class="fa fa-trash-o"></i>删除
												</a>
											</c:if>
										</c:when>
										<c:otherwise>
											${parser.smap[column.fieldKey] }
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
					<input type="number" title="不填写开始序号时，将从1开始" id="export-range-start" placeholder="开始序号" />
					-
					<input type="number" title="不填写结束序号时，将到最后结束" id="export-range-end" placeholder="结束序号" />
				</div>
			</div>
			<div class="row export-operate-area">
				<div class="col-lg-offset-2 col-lg-4">
					<input type="button" id="do-export" class="btn btn-xs btn-primary" value="开始导出" />
					<input type="button" id="do-download" class="btn btn-xs btn-primary" 
						value="下载导出文件" disabled="disabled" style="display: none" />
				</div>
				<div class="col-lg-2">
					<input type="button" id="do-break" class="btn btn-xs" value="取消导出" style="display: none;" />
				</div>
				<div id="export-progress" class="active progress progress-striped progress-xxs" style="display: none;">
					<div class="progress-bar progress-bar-orange" role="progressbar" aria-valuenow="0%" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
					</div>
					<span class="progress-text">0%</span>
				</div>
			</div>
			<div id="export-msg" class="row" style="display: none;">
				<p></p>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['utils', 'ajax', 'form'], function(Utils, Ajax, Form){
		var $page = $('#${module.name }-list-tmpl-${RES_STAMP}');
		console.log($page);
		$('#tmpl-list a[data-id]:not(.active)', $page).click(function(){
			var $this = $(this);
			$('#tmplId', $page).val($this.attr('data-id'));
			$('form', $page).submit();
		});
		+function(){
			var $exportAll = $('#export-all', $page),
				$exportCur = $('#export-current-page', $page),
				$exportMsg = $('#export-msg', $page),
				$msg = $('p', $exportMsg);
				
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
			var $btnExport = $('#do-export', $page),
				$btnBreak = $('#do-break', $page),
				$btnDownload = $('#do-download', $page);
			//页面一开始加载时的初始化表单参数
			
			var initParam = {};
			Utils.botByDom($page.getLocatePage().getContent(), 'cpf-page-inited', function(){
				var $form = $('form', $page),
					formData = new FormData($form[0]);
				Form.formatFormData($form, formData)
				initParam = Utils.converteFormdata(formData);
				$.extend(initParam, {
					pageNo	: '${pageInfo.pageNo}',
					pageSize: '${pageInfo.pageSize}'
				});
				console.log(initParam);
			});
			
			/* var initParam = $.extend(Utils.converteFormdata($('form', $page)), {
				pageNo	: '${pageInfo.pageNo}',
				pageSize: '${pageInfo.pageSize}'
			}); */
			var $exportProgress = $('#export-progress', $page);
			//轮询处理对象
			var handler = Ajax.poll({
				startupURL			: 'admin/modules/export/start/${module.name}',
				progressURL			: 'admin/modules/export/status',
				whenStartupResponse	: function(data, uuid){
					$msg.text('开始导出');
				},
				progressHandler	: function(progress, res){
					var progressText = parseFloat(progress * 100).toFixed(0);
					var percent = progressText + '%';
					$msg.text(res.statusMsg || '');
					$exportProgress.find('.progress-text').text(percent).css('left', (parseFloat(progressText) - 3) + '%');
					$exportProgress.find('.progress-bar').attr('aria-valuenow', percent).css('width', percent);
				},
				whenComplete		: function(res){
					if(res.uuid){
						$msg.text('导出完成');
						$btnDownload.removeAttr('disabled').off('click').click(function(){
							Ajax.download('admin/modules/export/download/' + res.uuid);
						}).show();
						$btnBreak.off('click').click(function(){
							resetExport(res.uuid);
						});
					}
				},
				whenUnsuccess		: function(res){
					
				}
			});
			$page.getLocatePage().getEventCallbacks(['afterClose', 'beforeReload'], null, function(callbacks){
				callbacks.add(function(){
					handler.disconnect();
				});
			});
			//判断当前session是否有导出工作正在处理
			var sessionExportStatus = {
				uuid		: '${exportStatus.uuid}',
				scope		: '${exportStatus.exportPageInfo.scope}',
				rangeStart	: '${exportStatus.exportPageInfo.rangeStart}',
				rangeEnd	: '${exportStatus.exportPageInfo.rangeEnd}'
			}
			if(sessionExportStatus.uuid && sessionExportStatus.scope){
				if(sessionExportStatus.scope === 'current'){
					$exportCur.prop('checked', true).trigger('change');
				}else if(sessionExportStatus.scope === 'all'){
					$('#export-range-start', $page).val(sessionExportStatus.rangeStart);
					$('#export-range-end', $page).val(sessionExportStatus.rangeEnd);
					$exportAll.prop('checked', true).trigger('change');
				}
				handler.pollWith(sessionExportStatus.uuid);
				startPolling();
			}
			$btnExport.click(function(){
				var scope = $exportAll.prop('checked')? 'all': $exportCur.prop('checked')? 'current': null;
				if(scope){
					var rangeStart = scope == 'all' && $('#export-range-start', $page).val() || undefined,
						rangeEnd = scope == 'all' && $('#export-range-end', $page).val() || undefined;
					handler.start({
						scope		: scope,
						rangeStart	: rangeStart,
						rangeEnd	: rangeEnd,
						parameters	: initParam
					});
					startPolling();
				}else{
					$.error('导出范围不能为null');
				}
				
			});
			function startPolling(){
				$exportMsg.show();
				$exportAll.attr('disabled', 'disabled');
				$exportCur.attr('disabled', 'disabled');
				$exportProgress.find('.progress-text').text('0%').css('left', 0);
				$exportProgress.find('.progress-bar').attr('aria-valuenow', 0).css('width', 0);
				$exportProgress.show();
				$btnExport.hide();
				$btnDownload.show().attr('disabled', 'disabled');
				$('.data-range :input', $page).attr('disabled', 'disabled');
				$btnBreak.show().off('click').click(function(){
					$btnBreak.attr('disabled', 'disabled');
					handler.breaks().done(function(){
						$btnBreak.removeAttr('disabled');
						resetExport();
					});
				});
			}
			function resetExport(){
				$exportMsg.hide();
				$('#export-progress', $page).hide();
				$btnExport.show();
				$btnBreak.removeAttr('disabled').hide();
				$btnDownload.hide();
				$exportAll.removeAttr('disabled');
				$exportCur.removeAttr('disabled');
				$('.data-range :input', $page).removeAttr('disabled');
			}
		}();
	});
</script>
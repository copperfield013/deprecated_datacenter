<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<c:set var="title">
	<c:choose>
		<c:when test="${group != null }">修改${module.title }模板组合-${group.title }</c:when>
		<c:otherwise>创建${group.title }模板组合</c:otherwise>
	</c:choose>
</c:set>
<title>${title }</title>
<link type="text/css" rel="stylesheet" href="media/admin/tmpl/css/tmpl-group-update.css" />
<div id="tmpl-group-update-${group.id }" class="tmpl-group-update">
	<script type="jquery/tmpl" id="tmpl-field">
		<div class="form-group field-item field-id="\${fieldId}" data-id="\${id}">
			<label class="control-label field-title">\${title}</label>
			<div class="field-value">
				<span class="field-view"></span>
			</div>
			<div class="operate-buttons">
				<a class="remove-field" title="删除字段"><i class="fa fa-trash-o"></i></a>
			</div>
		</div>
	</script>
	<script type="jquery/tmpl" id="tmpl-action">
		<tr>
			<td>\${index + 1 }</td>
			<td><input class="action-title" type="text" value="\${title }" /></td>
			{{if multable}}
				<td>
					<select class="multiple">
						<option value="2">事务型多选</option>
						<option value="1">多选</option>
						<option value="0">单选</option>
					</select>
				</td>
			{{/if}}
			<td>
				<a class="btn btn-danger btn-xs delete">
					<i class="fa fa-trash-o"></i>
					删除
				</a>
			</td>
		</tr>
	</script>
	<div class="float-operate-area">
		<div class="operate-area-cover"></div>
		<a id="save" class="btn-save" title="保存"><i class="fa fa-check-square"></i></a>
	</div>
	<div class="detail">
		<div class="page-header">
			<div class="header-title">
				<h1>${title }</h1>
			</div>
			<div class="header-buttons">
				<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
					<i class="glyphicon glyphicon-refresh"></i>
				</a>
			</div>
		</div>
		<div class="page-body">
			<div class="row">
				<div class="row">
					<div class="col-lg-offset-1 col-sm-offset-1 col-xs-offset-1 col-lg-10 col-sm-10 col-xs-10" id="group-container">
						<form class="bv-form form-horizontal validate-form" action="admin/tmpl/group/save">
							<div class="widget field-group">
								<div class="widget-header">
									<span class="widget-caption"> <span class="group-title">基本信息</span>
									</span>
								</div>
								<div class="widget-body">
									<input type="hidden" name="id" value="${group.id }" />
									<input type="hidden" name="module" value="${module.name }" />
									<div class="row">
										<div class="col-lg-6">
											<div class="form-group"> 
												<label class="col-lg-3 control-label" for="name">名称</label>
												<div class="col-lg-9">
													<input type="text"
													data-bv-notempty="true"
													data-bv-notempty-message="模板组合名称必填"
													class="form-control" name="title" value="${group.title }" />
												</div>
											</div>
										</div>
										<div class="col-lg-6">
											<div class="form-group"> 
												<label class="col-lg-3 control-label" for="name">模板Key</label>
												<div class="col-lg-9">
													<input type="text" ${group == null? '': 'disabled="disabled"' } placeholder="不填写时自动生成5位随机码" class="form-control"  name="key" value="${group.key }" />
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-lg-6">
											<div class="form-group"> 
												<label class="col-lg-3 control-label" for="name">列表模板</label>
												<div class="col-lg-9">
													<a 
														class="form-control"
														href="admin/tmpl/ltmpl/choose/${module.name }" 
														title="选择列表模板"
														choose-key="choose-ltmpl" 
														crn-choose-ltmpl="title" 
														>${group.listTemplateId != null? group.listTemplateTitle: '选择列表模板' }</a>
													<input type="hidden" crn-choose-ltmpl="id" name="listTemplateId" value="${group.listTemplateId }" />
												</div>
											</div>
										</div>
										<div class="col-lg-6">
											<div class="form-group"> 
												<label class="col-lg-3 control-label" for="name">详情模板</label>
												<div class="col-lg-9">
													<a 
														class="form-control"
														href="admin/tmpl/dtmpl/choose/${module.name }" 
														title="选择列表模板"
														choose-key="choose-dtmpl" 
														crn-choose-dtmpl="title" >${group.detailTemplateId != null? group.detailTemplateTitle: '选择详情模板' }</a>
													<input type="hidden" crn-choose-dtmpl="id" name="detailTemplateId" value="${group.detailTemplateId }" />
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="form-group"> 
											<div class="col-lg-9">
												<label class="col-lg-2 control-label" for="name">功能按钮</label>
												<div class="col-lg-9">
													<label class="col-lg-4 col-xs-3 form-control-static">
														<input id="showCreateButton" 
															type="checkbox" 
															class="checkbox-slider colored-blue" 
															${moduleWritable? '': 'disabled="disabled"' } 
															${group.hideCreateButton == 1? '': 'checked="checked"' }>
														<span class="text">创建按钮</span>
													</label>
													<label class="col-lg-4 col-xs-3 form-control-static">
														<input id="showImportButton" 
															type="checkbox" class="checkbox-slider colored-success" 
															${moduleWritable? '': 'disabled="disabled"' } 
															${group.hideImportButton == 1? '': 'checked="checked"' }>
														<span class="text">导入按钮</span>
													</label>
													<label class="col-lg-4 col-xs-3 form-control-static">
														<input id="showExportButton" 
															type="checkbox" 
															class="checkbox-slider colored-darkorange" 
															${group.hideExportButton == 1? '': 'checked="checked"' }>
														<span class="text">导出按钮</span>
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="form-group"> 
											<div class="col-lg-9">
												<label class="col-lg-2 control-label" for="name">操作按钮</label>
												<div class="col-lg-9">
													<label class="col-lg-4 col-xs-3 form-control-static">
														<input id="showQueryButton" 
															type="checkbox" 
															class="checkbox-slider colored-magenta" 
															${moduleWritable? '': 'disabled="disabled"' } 
															${group.hideQueryButton == 1? '': 'checked="checked"' }>
														<span class="text">查询及条件</span>
													</label>
													<label class="col-lg-4 col-xs-3 form-control-static">
														<input id="showDeleteButton" 
															type="checkbox" class="checkbox-slider colored-danger" 
															${moduleWritable? '': 'disabled="disabled"' } 
															${group.hideDeleteButton == 1? '': 'checked="checked"' }>
														<span class="text">删除按钮</span>
													</label>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="widget">
								<div class="widget-header">
									<span class="widget-caption">列表操作按钮</span>
									<div class="widget-buttons">
										<div id="list-action-select" class="chooser">
										</div>
									</div>
								</div>
								<div class="widget-body">
									<table class="table table-condensed">
										<thead>
											<tr>
												<th>#</th>
												<th>按钮文字</th>
												<th>
													多选选项
													<span 
														title="“事务型多选”指在选中多个实体进行操作时，只有全部都处理成功才算成功，中间任一实体处理失败都会放弃其他实体的处理"
														class="badge badge-darkorange badge-helper"></span>	
												</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody id="list-actions">
										</tbody>
									</table>
								</div>
							</div>
							<div class="widget">
								<div class="widget-header">
									<span class="widget-caption">详情操作按钮</span>
									<div class="widget-buttons">
										<div id="detail-action-select" class="chooser">
										</div>
									</div>
								</div>
								<div class="widget-body">
									<table class="table table-condensed">
										<thead>
											<tr>
												<th>#</th>
												<th>按钮文字</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody id="detail-actions">
										</tbody>
									</table>
								</div>
							</div>
							<div class="widget field-group">
								<div class="widget-header">
									<span class="widget-caption"> <span class="group-title">默认字段</span>
									</span>
									<div class="widget-buttons">
										<div class="input-icon field-search">
											<span class="search-input-wrapper"> <input type="text"
												class="search-text-input form-control input-xs glyphicon-search-input"
												autocomplete="off" placeholder="输入添加的字段名">
											</span> <i class="glyphicon glyphicon-search blue"></i> <i
												title="选择字段"
												class="glyphicon glyphicon-th blue field-picker-button"></i>
										</div>
									</div>
								</div>
								<div class="widget-body field-container"></div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['tmpl/js/tmpl-group-update'], function(TmplGroupUpdate){
		var $page = $('#tmpl-group-update-${group.id }');
		console.log($page);
		var premisesJson = [];
		var actions = [];
		var tmplActions = [];
		try{
			premisesJson = $.parseJSON('${premisesJson}');
			tmplActions = $.parseJSON('${tmplActions}');
			atmpls = $.parseJSON('${atmpls}');
		}catch(e){}
		console.log(actions);
		TmplGroupUpdate.init($page, '${module.name}', premisesJson, {
			tmplActions	: tmplActions,
			atmpls		: atmpls
		});
	});
</script>
<div></div>
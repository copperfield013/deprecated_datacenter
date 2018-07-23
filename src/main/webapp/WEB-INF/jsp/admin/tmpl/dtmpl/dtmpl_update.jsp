<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<c:set var="title">
	<c:choose>
		<c:when test="${tmpl != null }">修改${module.title }详情模板-${tmpl.title }</c:when>
		<c:otherwise>创建${module.title }详情模板</c:otherwise>
	</c:choose>
</c:set>


<title>${title }</title>
<link type="text/css" rel="stylesheet" href="media/admin/tmpl/css/dtmpl-update.css" />
<div id="dtmpl-update-${module.name}-${tmpl.id }" class="dtmpl-update">
	<script type="jquery/tmpl" id="tmpl-field-group">
		<div class="widget field-group" data-id="\${id}">
			<div class="widget-header">
				<span class="widget-caption">
					<span class="group-title">\${title}</span>
				</span>
				<div class="widget-buttons">
					<div class="input-icon field-search">
						<span class="search-input-wrapper">
							<input type="text" class="search-text-input form-control input-xs glyphicon-search-input" autocomplete="off" placeholder="输入添加的字段名">
						</span>
						<i class="glyphicon glyphicon-search blue"></i>
						<i title="选择字段" class="glyphicon glyphicon-th blue field-picker-button"></i>
					</div>
					<a class="remove-group"><i class="fa fa-trash-o"></i></a>
				</div>
			</div>
			<div class="widget-body field-container">
			</div>
		</div>
	</script>
	<script type="jquery/tmpl" id="tmpl-field-array-table">
		<div class="table-scrollable field-array-table">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr class="title-row">
						<th class="number-col">#</th>
					</tr>
				</thead>
				<tbody>
					<tr class="value-row">
						<td class="number-col">1</td>
					</tr>
				</tbody>
			</table>
		</div>
	</script>
	<script type="jquery/tmpl" id="tmpl-field-array-title">
		<th data-id="\${id}" field-id="\${fieldId}" 
			class="\${fieldAvailable? '': 'field-unavailable'}"
			title="\${fieldAvailable? fieldOriginTitle: '无效字段' }">
			<span>\${title }</span>
			<div class="operate-buttons">     
				<a class="remove-array-field" title="删除字段">
					<i class="fa fa-trash-o"></i>
				</a>
				<a class="recover-array-field" title="恢复默认名称">
					<i class="iconfont icon-recover"></i>
				</a>
			</div>
		</th>
	</script>
	<script type="jquery/tmpl" id="tmpl-field-array-value">
		<td field-id="\${fieldId}" class="\${fieldAvailable? '': 'field-unavailable'}">\${dv }</td>
	</script>
	<script type="jquery/tmpl" id="tmpl-field">
		<div class="form-group field-item movable \${fieldAvailable? '': 'field-unavailable'} \${colNum == 2? 'dbcol': ''}" field-id="\${fieldId}" data-id="\${id}"
			title="\${fieldAvailable? fieldOriginTitle: '无效字段' }">
			<div class="dtmpl-field-validates">
				<i class="dtmpl-field-validate-required \${validators.required? 'active-validator': ''}"></i>
			</div>
			<label class="control-label field-title">\${title}</label>
			<div class="field-value">
				<span class="field-view">\${dv}</span>
			</div>
			<div class="operate-buttons">
				<a class="remove-field" title="删除字段"><i class="fa fa-trash-o"></i></a>
				<a class="toggle-expand-field" title="拓展字段显示长度"><i class="fa fa-expand"></i></a>
				<a class="recover-field" title="恢复默认名称"><i class="iconfont icon-recover"></i></a>
				<a class="field-validate-a" title="字段约束"><i class="icon iconfont icon-rule"></i></a>
				<ul class="field-validate-menu">
					<li validate-name="required" class="\${validators.required? 'checked-validate': ''}">必填</li>
				</ul>
			</div>
		</div>
	</script>
		
		
	<div class="page-header">
		<div class="header-title">
			<h1>${title }</h1>
		</div>
	</div>
	<div class="page-body">
		<div id="operate-area">
			<div class="operate-area-cover"></div>
			<a id="add-group" title="添加字段组"><i class="fa fa-plus-square"></i></a>
			<a id="save" title="保存"><i class="fa fa-check-square"></i></a>
		</div>
		<div class="row header-row">
			<div class="col-lg-10 col-lg-offset-1">
				<input type="hidden" name="tmplId" value="" />
				<input type="text" class="form-control" id="tmplName" placeholder="请输入模板名称" value="${tmpl.title }">
			</div>
		</div>
		<div class="row">
			<div class="col-lg-offset-1 col-sm-offset-1 col-xs-offset-1 col-lg-10 col-sm-10 col-xs-10" id="group-container">
				<form class="form-horizontal group-container">
				</form>
			</div>
		</div>
	</div>
</div>
<script>
	console.log(1);
	seajs.use(['tmpl/js/dtmpl-update.js'], function(ViewTmpl){
		var $page = $('#dtmpl-update-${module.name}-${tmpl.id }');
		console.log($page);
		var updateMode = '${tmplJson != null}' == 'true';
		ViewTmpl.init($page, {
			tmplId		: '${tmpl.id}',
			tmplData	: updateMode && $.parseJSON('${tmplJson}'),
			mode		: updateMode? 'update': 'create',
			module		: '${module.name}'
		});
	});
</script>
<div></div>
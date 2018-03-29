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
<div id="dtmpl-update-${module.key}-${tmpl.id }" class="dtmpl-update">
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
	<script type="jquery/tmpl" id="tmpl-field">
		<div class="form-group field-item movable \${colNum == 2? 'dbcol': ''}" field-id="\${fieldId}" data-id="\${id}">
			<label class="control-label field-title">\${title}</label>
			<div class="field-value">
				<span class="field-view">\${dv}</span>
			</div>
			<div class="operate-buttons">
				<a class="remove-field" title="删除字段"><i class="fa fa-trash-o"></i></a>
				<a class="toggle-expand-field" title="拓展字段显示长度"><i class="fa fa-expand"></i></a>
				<a class="recover-field" title="恢复默认名称"><i class="iconfont icon-recover"></i></a>
			</div>
		</div>
	</script>
	<script type="jquery/tmpl" id="tmpl-fieldpicker">
		<div class="fieldpicker-container">
			<ul class="nav nav-tabs nav-justified">
				<li class="active"><a data-toggle="tab" href="#ctab_0">\${composites[0].cname }</a></li>
				{{if composites.length > 1}}
					<li><a data-toggle="tab" href="#ctab_1">\${composites[1].cname }</a></li>
				{{/if}}
				{{if composites.length > 2}}
					<li><a data-toggle="tab" href="#ctab_2">\${composites[2].cname }</a></li>
				{{/if}}
				{{if composites.length > 3}}
					<li class="dropdown">
						<a data-toggle="dropdown" class="fa dropdown-toggle" href="#">
							<span>更多</span>
						</a>
						<ul class="dropdown-menu dropdown-blue">
							{{each(i, composite) composites}}
								{{if i > 3}}
									<li><a data-toggle="tab" href="#ctab_\${i }">\${composite.cname }</a></li>
								{{/if}}
							{{/each}}
						</ul>
					</li>
				{{/if}}
			</ul>
			<div class="tab-content">
				{{each(i, composite) composites}}
					<div id="ctab_\${i }" class="tab-pane {{if i==0}}active{{/if}}">
						<div class="fieldpicker-field-container">
							{{each(i, field) composite.fields}}
								<a href="#" data-id="\${field.id}" class="fieldpicker-field-item">\${field.cname }</a>
							{{/each}}
						</div>
					</div>
				{{/each}}
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
		console.log(2);
		var $page = $('#dtmpl-update-${module.key}-${tmpl.id }');
		console.log($page);
		var updateMode = '${tmplJson != null}' == 'true';
		ViewTmpl.init($page, {
			tmplId		: '${tmpl.id}',
			tmplData	: updateMode && $.parseJSON('${tmplJson}'),
			mode		: updateMode? 'update': 'create',
			module		: '${module.key}'
		});
	});
</script>
<div></div>
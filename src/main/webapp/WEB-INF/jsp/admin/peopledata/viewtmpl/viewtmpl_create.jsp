<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/peopledata/css/viewtmpl-create.css" />
<div id="viewtempl-create">
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
		<div class="form-group field-item movable" field-id="\${fieldId}" data-id="\${id}">
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
				{{each(i, composite) composites}}
					<li class="{{if i==0}}active{{/if}}">
						<a data-toggle="tab" href="#ctab_\${i }">\${composite.cname }</a>
					</li>
				{{/each}}
			</ul>
			<div class="tab-content">
				{{each(i, composite) composites}}
					<div id="ctab_\${i }" class="tab-pane {{if i==0}}active{{/if}}">
						<div class="fieldpicker-field-container">
							{{each(i, field) composite.fields}}
								<a href="#" class="fieldpicker-field-item">\${field.cname }</a>
							{{/each}}
						</div>
					</div>
				{{/each}}
			</div>
		</div>
	</script>
	<div class="page-header">
		<div class="header-title">
			<h1>创建人口信息模板</h1>
		</div>
	</div>
	<div class="page-body">
		<div id="operate-area">
			<div class="operate-area-cover"></div>
			<a id="add-group" title="添加字段组"><i class="fa fa-plus-square"></i></a>
		</div>
		<div class="row header-row">
			<div class="col-lg-10 col-lg-offset-1">
				<input type="text" class="form-control" id="tmplName" placeholder="请输入模板名称">
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
<script type="text/javascript" src="media/admin/peopledata/js/viewtmpl-create.js"></script>
<div></div>
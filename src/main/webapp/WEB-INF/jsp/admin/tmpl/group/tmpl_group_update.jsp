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
												<input type="text" placeholder="不填写时自动生成5位随机码" class="form-control"  name="key" value="${group.key }" />
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
							</div>
						</div>
					
						<div class="widget field-group" data-id="\${id}">
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
						<div class="form-group">
				        	<div class="col-lg-offset-3 col-lg-3">
				        		<input class="btn btn-block btn-darkorange" type="submit" value="提交"  />
					        </div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	console.log(111);
	seajs.use(['tmpl/js/tmpl-group-update'], function(TmplGroupUpdate){
		var $page = $('#tmpl-group-update-${group.id }');
		var premisesJson = [];
		try{
			premisesJson = $.parseJSON('${premisesJson}');
		}catch(e){}
		TmplGroupUpdate.init($page, '${module.name}', premisesJson);
	});
</script>
<div></div>
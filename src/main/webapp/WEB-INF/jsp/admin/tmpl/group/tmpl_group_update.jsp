<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<c:set var="title">
	<c:choose>
		<c:when test="${group != null }">修改${module.title }模板组合-${group.title }</c:when>
		<c:otherwise>创建${group.title }模板组合</c:otherwise>
	</c:choose>
</c:set>
<title>${title }</title>
<div id="tmpl-group-update-${group.id }" class="tmpl-group-update">
	<div class="page-header">
		<div class="header-title">
			<h1>${title }</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/tmpl/group/save">
					<input type="hidden" name="id" value="${group.id }" />
					<input type="hidden" name="module" value="${module.key }" />
					<div class="form-group"> 
						<label class="col-lg-2 control-label" for="name">名称</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="title" value="${group.title }" />
						</div>
					</div>
					<div class="form-group"> 
						<label class="col-lg-2 control-label" for="name">列表模板</label>
						<div class="col-lg-5">
							<a 
								class="form-control"
								href="admin/tmpl/ltmpl/choose/${module.key }" 
								title="选择列表模板"
								choose-key="choose-ltmpl" 
								crn-choose-ltmpl="title" >${group.listTemplateId != null? group.listTemplateTitle: '选择列表模板' }</a>
							<input type="hidden" crn-choose-ltmpl="id" name="listTemplateId" value="${group.listTemplateId }" />
						</div>
					</div>
					<div class="form-group"> 
						<label class="col-lg-2 control-label" for="name">详情模板</label>
						<div class="col-lg-5">
							<a 
								class="form-control"
								href="admin/tmpl/dtmpl/choose/${module.key }" 
								title="选择列表模板"
								choose-key="choose-dtmpl" 
								crn-choose-dtmpl="title" >${group.detailTemplateId != null? group.detailTemplateTitle: '选择详情模板' }</a>
							<input type="hidden" crn-choose-dtmpl="id" name="detailTemplateId" value="${group.detailTemplateId }" />
						</div>
					</div>
					<div class="form-group"> 
						<label class="col-lg-2 control-label" for="name">模板Key</label>
						<div class="col-lg-5">
							<input type="text" class="form-control"  name="key" value="${group.key }" />
						</div>
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
<script>
	seajs.use(['dialog'], function(Dialog){
		var $page = $('#tmpl-group-update-${group.id }');
		var $chooseLtmpl = $('#choose-ltmpl', $page);
	});
</script>
<div></div>
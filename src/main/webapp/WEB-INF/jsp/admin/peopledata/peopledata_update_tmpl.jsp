
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/peopledata/css/peopledata-detail-tmpl.css" />
<style scoped="scoped">
	.field-input>input, 
	.field-input>select,
	.field-input>textarea {
	    height: 2.5em;
	    line-height: 2.5em;
	    padding: 10px 5px;
	    width: 100%;
	}
	.field-input>select {
	    line-height: 3em;
	    height: 3em;
	}
	.peopledata-detail-tmpl #operate-area {
	    font-size: 4em;
	    line-height: 1.23em;
	    padding-right: 1em;
	    position: absolute;
	    bottom: 20px;
	    right: 1em;
	    z-index: 1000;
	    width: 1em;
	}
	.peopledata-detail-tmpl #operate-area a i{
		cursor: pointer;
		color: #65b951;
		opacity: 0.5;
	}
	.peopledata-detail-tmpl #operate-area a i:HOVER{
		opacity: 1;
	}
</style>
<div class="detail peopledata-detail-tmpl" id="peopledata-update-tmpl-${peopleCode }">
	<div class="page-header">
		<div class="header-title">
			<h1>修改-${people.name }</h1>
		</div>
		<div class="template-container title-operate">
			<a href="#" title="查看模板" class="toggle-template"><i class="iconfont icon-template"></i></a>
		</div>
	</div>
	<div class="page-body">
		<div id="operate-area">
			<div class="operate-area-cover"></div>
			<a id="save" title="保存"><i class="fa fa-check-square"></i></a>
		</div>
		<div class="col-lg-offset-1 col-lg-10">
			<form class="form-horizontal group-container" action="admin/peopledata/do_update">
				<input type="hidden" name="peopleCode" value="${peopleCode }" />
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
										<span class="field-input" 
											fInp-type="${tmplField.type }"
											fInp-name="${tmplField.fieldName }"
											fInp-value="${parser[tmplField.fieldName] }"
											fInp-optkey="${tmplField.fieldId }"
										>
										</span>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</form>
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
<script>
	seajs.use(['dialog', 'ajax', 'utils', 'peopledata/js/viewtmpl-update.js', '$CPF',
	           'peopledata/js/field-input.js'], function(Dialog, Ajax, Utils, ViewTmpl, $CPF, FieldInput){
		var $page = $('#peopledata-update-tmpl-${peopleCode }');
		
		$('.toggle-template i', $page).click(function(){
			$('#tmpl-list', $page).toggle();
			return false;
		});
		$('#tmpl-list li[data-id]:not(.active)', $page).click(function(){
			var tmplId = $(this).attr('data-id');
			$page.getLocatePage().loadContent('admin/peopledata/update_tmpl/${peopleCode}', undefined, {
				timestamp	: '${timestamp}',
				tmplId		: tmplId
			});
		});
		
		if('${tmpl == null}' == 'true'){
			Dialog.notice('当前没有选择默认模板', 'error');
		}
		$CPF.showLoading();
		FieldInput.loadGlobalOptions('admin/peopledata/dict/enum_json').done(function(){
			FieldInput.appendTo($('.field-input', $page));
			$CPF.closeLoading();
		});
		
		$('#save i', $page).click(function(){
			$('form', $page).submit();
		});
		
		setTimeout(function(){
			$('.field-title', $page).each(function(){ViewTmpl.adjustFieldTitle($(this))});
		}, 100);
		
	});
</script>
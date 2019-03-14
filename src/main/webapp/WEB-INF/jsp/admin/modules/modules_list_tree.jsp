<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/modules/css/modules-list-tmpl.css" />
<c:set var="module" value="${view.module }" />
<c:set var="ltmpl" value="${view.listTemplate }" />
<div id="${module.name }-list-tree-${RES_STAMP}" class="detail entities-tree">
	<div class="page-header">
		<div class="header-title">
			<h1>${menu.title }-树形视图</h1>
		</div>
		<div class="header-buttons">
			<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
				<i class="glyphicon glyphicon-refresh"></i>
			</a>
			<a class="" title="切换列表视图" id="btn-toggle-tree" href="admin/modules/curd/list/${menu.id }" >
				<i class="fa fa-th-list"></i>
			</a>
		</div>
	</div>
	<div class="page-body">
		<form class="form-inline"  action="admin/modules/curd/list_tree/${menu.id }">
			<input type="hidden" id="tmplId" name="tmplId" value="${ltmpl.id }" />
			<c:forEach var="criteriaItem" items="${ltmpl.criterias }">
				<c:if test="${criteriaItem.queryShow != null }">
					<div class="form-group ${criteriaItem.fieldAvailable? '': 'criteria-field-unavailable'}"
						title="${criteriaItem.fieldAvailable? '': '无效字段'}">
						<label class="control-label">${criteriaItem.title }</label>
						<c:if test="${criteriaItem.fieldAvailable }">
							<c:choose>
								<c:when test="${criteriaItem.inputType == 'text' }">
									<input class="form-control" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}" placeholder="${criteriaItem.placeholder }" />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'select' }">
									<select class="form-control cpf-select2" name="criteria_${criteriaItem.id }" data-value="${criteria.templateCriteriaMap[criteriaItem.id]}">
										<option value="">--请选择--</option>
										<c:forEach var="option" items="${view.criteriaOptionMap[criteriaItem.fieldId]}">
											<option value="${option.value }">${option.title}</option>
										</c:forEach>								
									</select>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'multiselect' }">
									<select class="form-control cpf-select2 format-submit-value" name="criteria_${criteriaItem.id }" multiple="multiple" data-value="${criteria.templateCriteriaMap[criteriaItem.id]}">
										<c:forEach var="option" items="${view.criteriaOptionMap[criteriaItem.fieldId]}">
											<option value="${option.value }">${option.title}</option>
										</c:forEach>								
									</select>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'date' }">
									<input class="form-control datepicker" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}"  />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'label' }">
									<span class="cpf-select2-container">
										<select class="cpf-select2 format-submit-value" name="criteria_${criteriaItem.id }" multiple="multiple" data-value="${criteria.templateCriteriaMap[criteriaItem.id]}">
											<c:forEach var="label" items="${view.criteriaLabelMap[criteriaItem.fieldKey].subdomain}">
												<option value="${label }">${label}</option>
											</c:forEach>								
										</select>
										<c:choose>
											<c:when test="${criteriaItem.comparator == 'l1' }">
												<c:set var="labelSelectClass" value="cpf-select2-sign-or"></c:set>
											</c:when>
											<c:when test="${criteriaItem.comparator == 'l2' }">
												<c:set var="labelSelectClass" value="cpf-select2-sign-and"></c:set>
											</c:when>
										</c:choose>
										<span class="cpf-select2-sign ${labelSelectClass }"></span>
									</span>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'relation_existion' && criteriaItem.composite != null }">
									<span class="cpf-select2-container">
										<select class="cpf-select2 format-submit-value" name="criteria_${criteriaItem.id }" multiple="multiple" data-value="${criteria.templateCriteriaMap[criteriaItem.id]}">
											<c:forEach var="label" items="${criteriaItem.composite.relationSubdomain}">
												<option value="${label }">${label}</option>
											</c:forEach>								
										</select>
									</span>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'daterange' }">
									<span class="cpf-daterangepicker format-submit-value" 
										data-name="criteria_${criteriaItem.id }" 
										data-value="${criteria.templateCriteriaMap[criteriaItem.id]}">
									</span>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'range' }">
									<span class="cpf-textrange format-submit-value" 
										data-name="criteria_${criteriaItem.id }" 
										data-value="${criteria.templateCriteriaMap[criteriaItem.id]}">
									</span>
								</c:when>
								<c:when test="${criteriaItem.inputType == 'datetime' }">
									<input class="form-control datetimepicker" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}"  />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'time' }">
									<input class="form-control timepicker" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}"  />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'yearmonth' }">
									<input class="form-control yearmonthpicker" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}"  />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'ymrange' }">
									<input class="form-control ymrangepicker" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}"  />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'decimal' }">
									<input class="form-control cpf-field-decimal" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}"  />
								</c:when>
								<c:when test="${criteriaItem.inputType == 'int' }">
									<input class="form-control cpf-field-int" autocomplete="off" type="text" name="criteria_${criteriaItem.id }" value="${criteria.templateCriteriaMap[criteriaItem.id]}"  />
								</c:when>
								<c:otherwise>
									<input type="text" disabled="disabled" placeholder="没有配置对应的控件${criteriaItem.inputType }" />
								</c:otherwise>
							</c:choose>
						</c:if>
					</div>
				</c:if>
			</c:forEach>
			<div class="form-group">
				<c:if test="${not empty ltmpl.criterias and tmplGroup.hideQueryButton != 1}">
					<button type="submit" class="btn btn-default" title="${hidenCriteriaDesc }">查询</button>
				</c:if>
				<c:if test="${tmplGroup.hideDeleteButton != 1 }">
					<button id="btn-delete" class="btn btn-danger" disabled="disabled">删除选中</button>
				</c:if>
				<c:forEach items="${tmplGroup.actions }" var="action">
					<c:if test="${action.face == 'list' }">
						<button class="btn btn-azure shiny action-button" 
						data-id="${action.id }" data-multiple="${action.multiple }"
						title="${action.title }"
						disabled="disabled">
							<c:if test="${!empty action.iconClass }">
								<i class="${action.iconClass }"></i>
							</c:if>
							${action.title }
						</button>
					</c:if>
				</c:forEach>
			</div>
		</form>
	</div>
</div>
<!-- 用于标题中的文字在js中的 转义 -->
<c:set var="menuTitleInScript">
	${fn:replace(menu.title, '\\', '\\\\')}
</c:set>
<script>
	seajs.use(['modules/js/modules-list.js'], function(ModulesList){
		var $page = $('#${module.name }-list-tree-${RES_STAMP}');
	});
</script>
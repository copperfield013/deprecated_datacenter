<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/modules/css/modules-detail-tmpl.css" />
<c:set var="title">
	<c:choose>
		<c:when test="${entity != null }">修改-${entity.title }</c:when>
		<c:otherwise>${menu.title }-创建</c:otherwise>
	</c:choose>
</c:set>
<title>${title }</title>
<div class="detail entity-detail-tmpl" id="${module.name }-update-tmpl-${entity.code }-${RES_STAMP}">
	<div class="page-header">
		<div class="header-title">
			<h1>${title }</h1>
		</div>
		<div class="template-container title-operate">
			<label style="display: inline-flex" title="是否开启融合模式">
				<input id="fuse-switch" class="checkbox-slider toggle" type="checkbox">
				<span class="text"></span>
			</label> 
			<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
				<i style="font-size: 20px" class="glyphicon glyphicon-refresh"></i>
			</a>
		</div>
	</div>
	<div class="page-body">
		<div class="float-operate-area">
			<div class="operate-area-cover"></div>
			<a id="save" title="保存"><i class="fa fa-check-square"></i></a>
		</div>
		<div class="col-lg-offset-1 col-lg-10">
			<form class="form-horizontal group-container" action="admin/modules/curd/save/${menu.id }/${module.name }">
				<input type="hidden" name="${config.codeAttributeName }" value="${entity.code }" />
				<c:if test="${!empty groupPremises }">
					<div class="widget field-group">
						<div class="widget-header">
							<span class="widget-caption">
								<span class="group-title">默认字段（不可修改）</span>
							</span>
						</div>
						<div class="widget-body field-container premises-container">
							<c:forEach var="premise" items="${groupPremises }">
								<c:if test="${premise.fieldName != null }">
									<div class="form-group field-item">
										<label class="control-label field-title">${premise.fieldTitle }</label>
										<div class="field-value" value-field-name="${premise.fieldName }">
											${entity== null? premise.fieldValue: entity.smap[premise.fieldName] }
											<input type="hidden" name="${premise.fieldName }" value="${entity== null? premise.fieldValue: entity.smap[premise.fieldName] }" />
										</div>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</c:if>
				
				<c:forEach var="tmplGroup" items="${dtmpl.groups }">
					<div class="widget field-group">
						<div class="widget-header">
							<span class="widget-caption">
								<span class="group-title">${tmplGroup.title }</span>
							</span>
						</div>
						<div class="widget-body field-container">
							<c:choose>
								<c:when test="${tmplGroup.isArray != 1 }">
									<c:forEach var="tmplField" items="${tmplGroup.fields }">
										<c:set var="premise" value="${groupPremisesMap[tmplField.fieldName] }" />
										<div class="form-group field-item ${tmplField.fieldAvailable? '': 'field-unavailable' } ${tmplField.colNum == 2? 'dbcol': '' }"
											title="${tmplField.fieldAvailable? '': '无效字段' }"
										>
											<div class="dtmpl-field-validates">
												<c:if test="${fn:contains(tmplField.validators, 'required')}">
													<i validate-name="required"></i>
												</c:if>
											</div>
											<label class="control-label field-title">${tmplField.title }</label>
											<div class="field-value"  value-field-name="${tmplField.fieldName }">
												<span class="field-input" 
													fInp-type="${tmplField.type }"
													fInp-name="${tmplField.fieldName }"
													fInp-value="${entity != null? entity.smap[tmplField.fieldName] : premise != null? premise.fieldValue: '' }"
													fInp-optkey="${tmplField.optionGroupId }"
													fInp-fieldkey="${module.name }@${tmplField.fieldName }"
												>
												</span>
											</div>
										</div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<div class="table-scrollable field-array-table">
										<table class="table table-striped table-bordered table-hover">
											<thead>
												<tr class="title-row">
													<th>#</th>
													<c:if test="${tmplGroup.relationSubdomain != null }">
														<th
															class="th-field-title"
															fname-format="${tmplGroup.composite.name }[ARRAY_INDEX_REPLACEMENT].$$label$$"
															fInp-type="select"
															fInp-optset="${tmplGroup.relationSubdomain }"
															>关系</th>
													</c:if>
													<c:forEach var="field" items="${tmplGroup.fields }">
														<th 
															class="th-field-title ${field.fieldAvailable? '': 'field-unavailable'}"
															title="${field.fieldAvailable? '': '无效字段' }"
															fname-format="${fieldDescMap[field.fieldId].arrayFieldNameFormat }"
															fInp-type="${field.type }"
															fInp-optkey="${field.optionGroupId }"
															fInp-fieldkey="${module.name }@${tmplField.fieldName }"
															>${field.title }</th>
													</c:forEach>
													<th width="10px"><span class="array-item-add" title="添加一行">+</span></th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="entityItem" varStatus="i" items="${entity.arrayMap[tmplGroup.composite.name] }">
													<tr class="value-row">
														<td>
															<span>${i.index + 1 }</span>
															<input type="hidden" name="${entityItem.codeName }" value="${entityItem.code }" />
														</td>
														<c:if test="${tmplGroup.relationSubdomain != null }">
															<c:set var="relationName" value="${tmplGroup.composite.name }[${i.index }].$$label$$" />
															<td>
																<span class="field-value">
																	<span class="field-input" 
																		fInp-type="select"
																		fInp-name="${relationName }"
																		fInp-value="${entityItem.smap[relationName] }"
																		fInp-optset="${tmplGroup.relationSubdomain }"
																	>
																	</span>
																</span>
															</td>
														</c:if>
														<c:forEach var="tmplField" items="${tmplGroup.fields }">
															<td class="${tmplField.fieldAvailable? '': 'field-unavailable'}">
																<span class="field-value">
																	<span class="field-input" 
																		fInp-type="${tmplField.type }"
																		fInp-name="${fieldDescMap[tmplField.fieldId].arrayFieldNameMap[i.index] }"
																		fInp-value="${entityItem.smap[tmplField.fieldName] }"
																		fInp-optkey="${tmplField.optionGroupId }"
																		fInp-fieldkey="${module.name }@${tmplField.fieldName }"
																	>
																	</span>
																</span>
															</td>
														</c:forEach>
														<td><span class="array-item-remove" title="移除当前行">×</span></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
			</form>
		</div>
	</div>
</div>
<script>
	seajs.use(['modules/js/modules-update'], function(ModulesUpdate){
		var $page = $('#${module.name }-update-tmpl-${entity.code }-${RES_STAMP}');
		ModulesUpdate.init(
				$page, 
				'${module.name}', 
				'${entity.code}');
	});
</script>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/modules/css/modules-detail-tmpl.css" />
<c:set var="title">
	<c:choose>
		<c:when test="${entity != null }">修改-${entity.title }</c:when>
		<c:otherwise>创建${module.title }</c:otherwise>
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
			<a href="#" title="查看模板" class="toggle-template"><i class="iconfont icon-template"></i></a>
		</div>
	</div>
	<div class="page-body">
		<div class="float-operate-area">
			<div class="operate-area-cover"></div>
			<a id="save" title="保存"><i class="fa fa-check-square"></i></a>
		</div>
		<div class="col-lg-offset-1 col-lg-10">
			<form class="form-horizontal group-container" action="admin/modules/curd/save/${module.name }">
				<input type="hidden" name="${config.codeAttributeName }" value="${entity.code }" />
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
										<div class="form-group field-item ${tmplField.colNum == 2? 'dbcol': '' }">
											<label class="control-label field-title">${tmplField.title }</label>
											<div class="field-value">
												<span class="field-input" 
													fInp-type="${tmplField.type }"
													fInp-name="${tmplField.fieldName }"
													fInp-value="${entity.smap[tmplField.fieldName] }"
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
															class="th-field-title"
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
															<td>
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
	<div id="tmpl-list" style="display: none;">
		<ul class="tmpl-list-wrapper">
			<c:if test="${dtmpl != null }">
				<li data-id="${dtmpl.id }" class="active">
					<span class="tmpl-icon"><i class="fa fa-lightbulb-o"></i></span>
					<span class="tmpl-item-body">
						<span class="tmpl-name">${dtmpl.title }</span>
						<span class="tmpl-date"><fmt:formatDate value="${dtmpl.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
					</span>
				</li>
			</c:if>
			<c:forEach var="dtmplItem" items="${dtmpls }">
				<c:if test="${dtmplItem.id != dtmpl.id }">
					<li data-id="${dtmplItem.id }">
						<span class="tmpl-icon"><i class="fa fa-lightbulb-o"></i></span>
						<span class="tmpl-item-body">
							<span class="tmpl-name">${dtmplItem.title }</span>
							<span class="tmpl-date"><fmt:formatDate value="${dtmplItem.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
						</span>
					</li>
				</c:if>
			</c:forEach>
		</ul>
		<div class="tmpl-operate">
			<a class="tab" title="配置模板" target="people_dtmpl_list" href="admin/tmpl/dtmpl/list/people"><i class="icon glyphicon glyphicon-cog"></i></a>
		</div>
	</div>
</div>
<script>
	seajs.use(['dialog', 'ajax', 'utils', 'tmpl/js/dtmpl-update.js', '$CPF',
	           'field/js/field-input.js'], function(Dialog, Ajax, Utils, ViewTmpl, $CPF, FieldInput){
		"use strict";
		var $page = $('#${module.name }-update-tmpl-${entity.code }-${RES_STAMP}');
		
		var isUpdateMode = 'true' === '${entity != null}';
		
		
		$('.toggle-template i', $page).click(function(){
			$('#tmpl-list', $page).toggle();
			return false;
		});
		$('#tmpl-list li[data-id]:not(.active)', $page).click(function(){
			var tmplId = $(this).attr('data-id');
			var url = 'admin/modules/curd' + (isUpdateMode? '/update/${module.name}/${entity.code}': '/add/${module.name}');
			$page.getLocatePage().loadContent(url, undefined, {
				timestamp	: '${timestamp}',
				tmplId		: tmplId
			});
		});
		
		if('${dtmpl == null}' == 'true'){
			Dialog.notice('当前没有选择默认模板', 'error');
		}
		function appendTo($doms, paramGetter){
			var def = $.Deferred();
			paramGetter = paramGetter || function($dom){
				function attr(attrName){
					return $dom.attr(attrName);
				}
				return {
					type		: attr('fInp-type'),
					name		: attr('fInp-name'),
					id			: attr('fInp-id'),
					value		: attr('fInp-value'),
					styleClass	: attr('fInp-class'),
					optionsKey	: attr('fInp-optkey'),
					readonly	: attr('fInp-readonly'),
					optionsSet	: attr('fInp-optset'),
					fieldKey	: attr('fInp-fieldkey')
				};
			};
			$doms.each(function(){
				var $this = $(this);
				var param = paramGetter($this);
				var fInp = new FieldInput(param);
				$this.append(fInp.getDom());
			});
			def.resolve();
			return def.promise();
		};
		$CPF.showLoading();
		FieldInput.loadGlobalOptions('admin/field/enum_json').done(function(){
			appendTo($('.field-input', $page));
			$CPF.closeLoading();
		});
		
		var fuseMode = false;
		$('#save i', $page).click(function(){
			var msg = '是否保存？';
			if(fuseMode){
				msg = '是否保存（当前为融合模式）？'
			}
			Dialog.confirm(msg, function(yes){
				if(yes){
					$('form', $page).submit();
				}
			});
		});
		$('form', $page).on('cpf-submit', function(e, formData){
			formData.append('%fuseMode%', fuseMode);
		});
		$('#fuse-switch', $page).change(function(){
			fuseMode = $(this).prop('checked');
			$('#save', $page).toggleClass('fuse-mode', fuseMode);
		});
		
		$page.on('click', '.array-item-remove', function(){
			var $row = $(this).closest('tr');
			Dialog.confirm('确认删除该行？', function(yes){
				if(yes){
					var $table = $row.closest('table');
					$row.remove();
					refreshTable($table);
				}
			});
		});
		$('.array-item-add', $page).click(function(){
			var $table = $(this).closest('table');
			var $tbody = $table.children('tbody');
			var $titleRow = $table.find('.title-row');
			var $dataRow = $('<tr>').append('<td><span></span></td>')
			$titleRow.children('th.th-field-title').each(function(){
				var $title = $(this);
				var $td = $('<td>');
				var $fieldInput = $('<span class="field-input"></span></span>');
				$fieldInput
					.attr('fInp-type', $title.attr('fInp-type'))
					.attr('fInp-optkey', $title.attr('fInp-optkey'))
					.attr('fInp-fieldkey', $title.attr('fInp-fieldkey'))
					.appendTo($('<span class="field-value"></span>').appendTo($td));
				if($title.attr('fInp-optset')){
					$fieldInput.attr('fInp-optset', $title.attr('fInp-optset'));
				}
				$dataRow.append($td);
			});
			$dataRow.append('<td><span class="array-item-remove" title="移除当前行">×</span></td>');
			$dataRow.appendTo($tbody);
			appendTo($dataRow.find('.field-input')).done(function(){
				refreshTable($table);
			})
		});
		
		function refreshTable($table){
			var $titles = $('thead tr.title-row th', $table); 
			$('tbody tr', $table).each(function(i){
				var $tr = $(this);
				var $tds = $tr.children('td');
				$tds.eq(0).children('span').text(i + 1);
				for(var j = 1; j < $tds.length - 1; j ++){
					var nameFormat = $titles.eq(j).attr('fname-format');
					var inputName = nameFormat.replace('ARRAY_INDEX_REPLACEMENT', i);
					$tds.eq(j).find(':text,select,textarea,input[type="hidden"]').each(function(){
						$(this).attr('name', inputName);
					});
				}
			});
		}
		setTimeout(function(){
			$('.field-title', $page).each(function(){ViewTmpl.adjustFieldTitle($(this))});
		}, 100);
		
	});
</script>
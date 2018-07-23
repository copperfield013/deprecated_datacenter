<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="module-import-tmpl-dialog-${RES_STAMP }" class="module-import-tmpl-dialog">
	<div class="page-header">
		<div class="header-title">
			<h1>导入模板配置</h1>
		</div>
		<div class="header-buttons">
			<a title="切换模板" class="btn-toggle" href="page:#tmpl-list.toggle">
				<i class="iconfont icon-template"></i>
			</a>
			<a class="export btn-toggle" title="保存导入模板" id="btn-save" href="javascript:;">
				<i class="fa fa-save"></i>
			</a>
			<a class="btn-toggle" title="下载导入模板" id="btn-download" href="javascript:;">
				<i class="fa fa-download"></i>
			</a>
			<a class="btn-toggle" title="新建模板" id="btn-new" href="javascript:;">
				<i class="fa fa-file"></i>
			</a>
		</div>
	</div>
	<div class="page-body">
		<form class="form-horizontal"  action="">
			<div class="form-group">
				<label class="control-label col-sm-2 col-xs-2 col-lg-1 col-md-2">模板名</label>
				<div class="col-sm-6 col-xs-6 col-lg-6 col-md-6">
					<input class="form-control" id="tmpl-title" type="text" name="title" value="${tmpl.title }" placeholder="输入导入模板名称" />
				</div>
			</div>
			<div class="row">
				<div class="fields-l col-lg-8">
					<h4>已选字段</h4>
					<table id="" class="table table-hover table-striped table-bordered">
						<thead>
							<tr>
								<th>表头</th>
								<th>字段</th>
								<th width="10%"></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="fields-r col-lg-4">
					<h4>可选字段</h4>
					<ul class="list-group">
						<c:forEach var="field" items="${fields }">
							<li class="list-group-item ${field.isMultipleField? 'multiple-field' : '' }" 
								relation-key="${field.relationKey }"
								pattern="${field.fieldNamePattern }">${field.fieldName }</li>
						</c:forEach>
					</ul>
				</div>
			</div>	
		</form>
	</div>
	<div id="tmpl-list" class="detail-toggle-sublist blur-hidden" style="display: none;">
		<div class="detail-toggle-sublist-wrapper">
			<c:forEach items="${tmpls }" var="tmplItem">
				<a href="admin/modules/import/tmpl/show/${menu.id }/${tmplItem.id}" data-id="${tmplItem.id }" class="${tmplItem.id == tmpl.id? 'active': '' }">
					<span class="detail-toggle-sublist-icon"><i class="fa fa-lightbulb-o"></i></span>
					<span class="detail-toggle-sublist-item-body">
						<span class="detail-toggle-sublist-item-name">${tmplItem.title }</span>
						<span class="detail-toggle-sublist-item-date"><fmt:formatDate value="${tmplItem.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
					</span>
				</a>
			</c:forEach>
		</div>
	</div>
	<script type="jquery/tmpl" id="tmpl-col-row-tmpl">
		<tr field-index="\${fieldIndex}", data-id="\${fieldId}">
			<td>
				<span class="field-title">\${title}</span>
			</td>
			<td class="field-name">\${fieldName}</td>
			<td>
				{{if removable != false }}
					<a class="btn btn-xs remove-col">
						<i class="fa fa-trash-o"></i>
					</a>
				{{/if}}
			</td>
		</tr>
	</script>
</div>
<script>
	seajs.use(['utils', 'ajax', '$CPF', 'dialog'], function(Utils, Ajax, $CPF, Dialog){
		var $page = $('#module-import-tmpl-dialog-${RES_STAMP }');
		var $colTmpl = $('#tmpl-col-row-tmpl', $page);
		var $tbody = $('.fields-l tbody', $page);
		$tbody.sortable({
			helper 		: "clone",
			cursor 		: "move",// 移动时候鼠标样式
			opacity		: 0.5, // 拖拽过程中透明度
			tolerance 	: 'pointer'
		});
		$('.fields-r', $page).on('dblclick', 'ul li', function(e, field){
			console.log(arguments);
			var $field = $(this);
			var fieldName = $field.text();
			var pattern = $field.attr('pattern');
			var $row = null;
			var fieldId = undefined;
			if(field){
				fieldId = field.id;
			}
			if($field.is('.multiple-field')){
				var $rows = $field.data('fieldRows');
				if(!$rows){
					$field.data('fieldRows', $rows = []);
				}
				var fieldIndex = $rows.length;
				var relationKey = $field.attr('relation-key');
				if(relationKey){
					//添加的字段是关系字段，必须自动添加关系label字段
					var hasLabelRow = false;
					relationKey += '[' + fieldIndex + '].${relationLabelKey}';
					$tbody.children('tr').each(function(){
						var fieldName = $('.field-name', this).text();
						if(fieldName === relationKey){
							hasLabelRow = true;
							return false;
						}
					});
					if(!hasLabelRow){
						$colTmpl.tmpl({
							title		: relationKey,
							fieldName	: relationKey,
							fieldIndex	: fieldIndex,
							removable	: false
						}).addClass('relation-label').appendTo($tbody)
					}
				}
				fieldName = pattern.replace('%INDEX%' , fieldIndex);
				$row = $colTmpl.tmpl({
					title		: fieldName,
					fieldName	: fieldName,
					fieldIndex	: fieldIndex,
					fieldId		: fieldId,
					removable	: true
				}).appendTo($tbody);
				$rows.push($row);
			}else{
				$row = $colTmpl.tmpl({
					title		: fieldName,
					fieldName	: fieldName,
					fieldId		: fieldId,
					removable	: true
				}).appendTo($tbody);
				$field.hide();
			}
			$row.data('originLi', $field);
		});
		
		//初始化模板数据
		+function(){
			try{
				var tmplFieldsJson = $.parseJSON('${tmplFieldsJson}');
				console.log(tmplFieldsJson);
				var $lis = $('.fields-r ul li', $page);
				for(var i in tmplFieldsJson){
					var field = tmplFieldsJson[i];
					var $li = $lis.filter('li[pattern="' + field.fieldPattern + '"]');
					if($li.length == 1){
						$li.trigger('dblclick', [field]);
					}
				}
			}catch(e){}
		}();
		
		console.log('111');
		$('.fields-l', $page).on('click', '.remove-col', function(){
			var $row = $(this).closest('tr');
			var $originLi = $row.data('originLi');
			if($originLi){
				var $rows = $originLi.data('fieldRows');
				if($rows && $.isArray($rows)){
					var pattern = $originLi.attr('pattern');
					var index = 0;
					while(!$row.is($rows[index])) index++;
					if(index < $rows.length){
						$rows.splice(index, 1);
						for(var i = index; i < $rows.length; i++){
							$rows[i]
								.attr('field-index', i)
								.find('.field-name').text(pattern.replace('%INDEX%', i));
						}
					}
				}
				$originLi.show();
			}
			$row.remove();
			$('tr.relation-label[field-index]', $tbody).each(function(){
				var relationLabelKey = $(this).find('.field-name').text();
				var fieldIndex = $(this).attr('field-index');
				var hasRelationField = false;
				$('tr[field-index]', $tbody).not('.relation-label').each(function(){
					var $originLi = $(this).data('originLi');
					var relationKey = $originLi.attr('relation-key');
					if(relationKey){
						var thisFieldIndex = $(this).attr('field-index');
						if(relationKey + '[' + thisFieldIndex + '].${relationLabelKey}' == relationLabelKey){
							hasRelationField = true;
							return false;
						}
					}
				});
				if(!hasRelationField){
					$(this).remove();
				}
			});
		});
		
		function checkSubmitData(operate){
			var def = $.Deferred();
			var title = $('#tmpl-title', $page).val();
			if(!title){
				Dialog.notice('请填写模板名称后保存', 'error');
			}else{
				var $rows = $tbody.children('tr');
				if($rows.length == 0){
					Dialog.notice('模板内没有选择字段', 'error');
				}else{
					Dialog.confirm('确认' + operate + '当前模板[' + title + ']，模板内共有' + $rows.length + '个字段', function(yes){
						if(yes){
							var fields = [];
							$rows.each(function(){
								var $row = $(this);
								var $field = $row.data('originLi');
								fields.push({
									id			: $row.attr('data-id'),
									title		: $row.find('.field-title').text(),
									fieldName	: $row.find('.field-name').text(),
									fieldPattern: !$field? null: $field.attr('pattern'),
									fieldIndex	: $row.attr('field-index')
								});
							});
							def.resolve({
								tmplId		: '${tmpl.id}',
								fields		: fields,
								title		: title,
								module		: '${module.name}'
							});
						}
					});
				}
			}
			return def.promise();
		}
		
		$('#btn-download', $page).click(function(){
			checkSubmitData('下载').done(function(sData){
				$CPF.showLoading();
				Ajax.postJson('admin/modules/import/submit_field_names/${menu.id}', sData, function(data){
					if(data.uuid){
						Ajax.download('admin/modules/import/download_tmpl/' + data.uuid);
					}
					$CPF.closeLoading();
				});
			});
		});
		
		$('#btn-save', $page).click(function(){
			checkSubmitData('保存').done(function(sData){
				Ajax.postJson('admin/modules/import/save_tmpl/${menu.id}', sData, function(data){
					if(data.status === 'suc'){
						Dialog.notice('保存成功', 'success');
						$page.getLocatePage().loadContent('admin/modules/import/tmpl/show/${menu.id}/' + data.tmplId);
					}else{
						Dialog.notice('保存失败', 'error');
					}
				});
			})
		});
		$('#btn-new', $page).click(function(){
			Dialog.confirm('是否创建新的模板？当前模板若已修改，将不会被保存。', function(yes){
				if(yes){
					$page.getLocatePage().loadContent('admin/modules/import/tmpl/${menu.id}');
				}
			});
		});
	});
</script>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="tree-add">
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form id="tree-relation-add-form" class="bv-form form-horizontal validate-form">
					<input type="hidden" id="parentNodeId" name="parentNodeId" value="${parentNodeId }"/>
					<input type="hidden" id="parentMappingName" name="parentMappingName" value="${mappingName }">
					<div id="select-entity-div" class="form-group">
						<label class="col-lg-2 control-label" for="select-entity">关系名称</label>
						<div class="col-lg-5">
							<input type="hidden" id="mappingName" name="mappingName">
							<select id="select-entity">
								<option value="">--请选择--</option>
								<c:forEach items="${nameList }" var="entityName">
									<option value="${entityName }" >${entityName }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div id="relation-selected-div"></div>
					<div id="attr-area-div"></div>
					<div class="form-group">
			        	<div class="col-lg-offset-3 col-lg-3">
			        		<input id="close-and-submit-btn" class="btn btn-block btn-darkorange" type="button" value="提交" />
				        </div>
					</div>
				</form>
				<script type="text/javascript">
					seajs.use(['ajax'], function(Ajax){
						console.log("******添加节点******");
						$("#select-entity").on("change", function(){
							var checkedVal = $("#select-entity").val();
							var labelSetMap =  $.parseJSON('${labelSetMap }');
							var mappingNameMap =  $.parseJSON('${mappingNameMap }');
							var html = '<div class="form-group">' + 
										'<label class="col-lg-2 control-label" for="relation">关系类型</label> ' + 
										'<div class="col-lg-5"> ' + 
											'<select id="relation" name="relation"> ' +
												'<option value="">--请选择--</option> ' +
												'<c:forEach items="' + labelSetMap[checkedVal] + '" var="relationName"> ' +
													'<option value="${relationName }">${relationName }</option> ' +
												'</c:forEach> ' +
											'</select> ' +
										'</div> ' +
									'</div>';
							//var mappingName = $("#parentMappingName").val() + "." + checkedVal;
							var mappingName = mappingNameMap[checkedVal];
							$("#mappingName").val(mappingName);
							console.log(mappingNameMap);
							console.log("mapping name is : " + mappingName);
							Ajax.ajax('admin/relationtreeview/getEntityList', {
								mappingName	:	mappingName
							}, function(data){
								var entityList = data.entityList; 
								var attrNameSet = data.attrNameSet;
								var attrHtml = '';
								//for(var key in entityList){
									//var array = $.parseJSON(entityList[key]);
									//for (var key in attrNameSet) {
										//alert(array[attrNameSet[key]]);
										//alert(""+ array['姓名'] +"");
										 attrHtml = '<div class="form-group">' + 
										'<label class="col-lg-2 control-label" for="relation">实体列表</label> ' + 
										'<div class="col-lg-5"> ' + 
											'<select id="relation" name="relation"> ' +
												'<option value="">--请选择--</option> ';
												for(var key in entityList){
													var array = $.parseJSON(entityList[key]);
												 	attrHtml+="<option value='"+array['唯一编码']+"'>"+array['姓名']+"</option>";
												}
											attrHtml +=	'</select> ' +
										'</div> ' +
									'</div>';
										
									//}
								//}
								
								console.log("data json is : ");
								console.log(data);
								console.log(attrHtml);
								$("#attr-area-div").html(attrHtml);
							});
							$("#relation-selected-div").html(html);
							//$("#select-entity-div").append(html);
							//$("#select-entity-div").after(html);
						});
						
						$("#close-and-submit-btn").on('click', function(){
							$(this).attr("disabled","true"); //设置变灰按钮,防止多次提交
							//var $page = $("#tree_view_${rootId}");
							var $page = $("#tree_view_panel");
							console.log($page);
							var mappingName = $("#select-entity").val();
							var relation = $("#relation").val();
							var map = new Map();
							console.log($("#tree-relation-add-form").serializeJson());
							//$page.data("test")(JSON.stringify(map));
							$page.data("addSubmit")($("#tree-relation-add-form").serializeJson());
						});
						
					}); 
				
				</script>
			</div>
		</div>
	</div>
</div>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-same-list-left">
	<div class="row">
		<label class="col-lg-2">地址名称</label>
		<div class="col-lg-4">${addressName }</div>
	</div>
	<form action="admin/address/sameList">
		<input type="hidden" name="addressName" value="${addressName }"/>
		<div class="list-area">
			<h4>相同地址列表</h4>
			<table class="table">
				<thead>
					<tr>
						<th><input type="checkbox" id="select-all-the-same-address-name" style="left:0; opacity: 1; margin-top: -17px;"/></th>
						<th>序号</th>
						<th>地址名称</th>
						<th><a href="#" id="remove-from-the-same">移出相同</a></th>
						<!-- <th>操作</th> -->
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${sameList }" var="address" varStatus="i">
						<tr data-name="${address.name }">
							<td><input type="checkbox" name="same-address-name" style="left:0; opacity: 1;"/></td>
							<td>${i.index + 1 }</td>
							<td><a href="#" title="详情-${address.name }" >${address.name }</a></td>
							<td>
								<a class="address-remove-category" href="#">移除</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="cpf-paginator" pageNo="${theSamePageInfo.pageNo }" pageSize="${theSamePageInfo.pageSize }" count="${theSamePageInfo.count }"></div>
		</div>
	</form>
</div>
<script>
	seajs.use(['page', 'ajax', 'dialog', 'utils'], function(Page, Ajax, Dialog, Utils){
		var $page = $('#address-same-list-left');
		
		$(".address-remove-category", $page).click(function(){
			var addressName = $(this).closest('tr[data-name]').attr("data-name");
			Dialog.confirm("确定移除？", function(yes){
				if(yes){
					Ajax.ajax("admin/address/remove", {
						addressStr	:	addressName
					}, function(json){
						if(json.result == 'success'){
							console.log("success!");
							Dialog.notice("操作成功！", 'success');
							Page.getPage("address-same-list").refresh();
							Page.getPage("address-all-list").refresh();
						}else{
							console.log("failed");
							Dialog.notice("操作失败！", 'error');
						}
					})
				}
			})
		});
		
		/**
			全选和反选
		**/
		$("#select-all-the-same-address-name", $page).click(function(){
			if($(this).is(':checked')){
				$("input[name='same-address-name']", $page).each(function(){
					 $(this).prop("checked",true);
					 //console.log("all checked");
				});
			}else{
				$("input[name='same-address-name']", $page).each(function(){
					$(this).removeAttr("checked",false);
					 //console.log("remove all checked");
				});
			}
		});
		
		$("#remove-from-the-same", $page).click(function(){
			var length = $("input[name='same-address-name']:checked", $page).length;
			var addressNames = [];
			$("input[name='same-address-name']:checked", $page).each(function(){
				addressNames.push($(this).closest('tr[data-name]').attr('data-name'));
			});
			console.log("---->" + addressNames);
			if(length > 0){
				Dialog.confirm("确定移出相同地址？", function(yes){
					if(yes){
						Ajax.postJson("admin/address/batchRemove", {
							addressNames	:	addressNames
						}, function(json){
							if(json.result == 'success'){
								console.log("success!");
								Dialog.notice("操作成功！", 'success');
								Page.getPage("address-same-list").refresh();
								Page.getPage("address-all-list").refresh();
							}else{
								console.log("failed");
								Dialog.notice("操作失败！", 'error');
							}
						});
					}
				});
			}else{
				Dialog.notice("至少选择一条记录！", "warning");
			}
		});
		
	});
</script>
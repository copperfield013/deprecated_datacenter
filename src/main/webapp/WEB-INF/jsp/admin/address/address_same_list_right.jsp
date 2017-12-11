<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-same-list-right">
	<form action="admin/address/getAddressList"  class="form-inline">
		<input type="hidden" id="curAddressCode" name="addressCode" value="${addressCode }"/>
		<nav>
			<div class="form-group">
				<label for="addressStr">地址名称</label>
				<input type="text" class="form-control" id="addressStr" name="addressStr" value="${addressStr }" />
			</div>
			<button type="submit" id="address-search-btn" class="btn btn-default">查询</button>
		</nav>
		<div class="row list-area">
			<h4>相同地址列表</h4>
			<table class="table">
				<thead>
					<tr>
						<th><input type="checkbox" id="select-all-address-ids" style="left:	0; opacity: 1; margin-top: -17px;"/></th>
						<th>序号</th>
						<th>地址名称</th>
						<th><a href="#" id="move-to-the-same">移至相同</a></th>
						<!-- <th>操作</th> -->
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${addressList }" var="address" varStatus="i">
						<tr data-name="${address.name }">
							<td><input type="checkbox" name="address-id-checkbox" style="left:	0; opacity: 1;"/></td>
							<td>${i.index + 1 }</td> 	
							<td><a class="address_detail_hover" href="#">${address.name }</a></td>
							<td>
								<a class="address_add_category" href="#">选择</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="cpf-paginator" pageNo="${addressPageInfo.pageNo }" pageSize="${addressPageInfo.pageSize }" count="${addressPageInfo.count }"></div>
		</div>
	</form>
</div>
<script>
	seajs.use(['dialog', 'ajax', 'page', 'utils'], function(Dialog, Ajax, Page, Utils){
		var $page = $('#address-same-list-right');
		
		$(".address_add_category", $page).click(function(){
			var curAddressCode = $("#curAddressCode").val();
			var addressName = $(this).closest('tr[data-name]').attr('data-name');
			Dialog.confirm("确定添加到相同地址？", function(yes){
				if(yes){
					Ajax.ajax("admin/address/updateAddressEntityCategory", {
						addressCode	:	curAddressCode,
						addressName	:	addressName
					}, function(json){
						if(json.result == 'success'){
							console.log("success!");
							Dialog.notice("操作成功！", 'success');
							Page.getPage("address-same-list").refresh();
						}else{
							console.log("failed");
							Dialog.notice("操作失败！", 'error');
						}
					});
				}
			});
		});
		
		/**
			全选和反选
		**/
		$("#select-all-address-ids", $page).click(function(){
			if($(this).is(':checked')){
				$("input[name='address-id-checkbox']", $page).each(function(){
					 $(this).prop("checked",true);
					 //console.log("all checked");
				});
			}else{
				$("input[name='address-id-checkbox']", $page).each(function(){
					$(this).removeAttr("checked",false);
					 //console.log("remove all checked");
				});
			}
		});
		
		$("#move-to-the-same", $page).click(function(){
			var length = $("input[name='address-id-checkbox']:checked", $page).length;
			var addressNames = [];
			$("input[name='address-id-checkbox']:checked", $page).each(function(){
				addressNames.push($(this).closest('tr[data-name]').attr('data-name'));
			});
			if(length > 0){
				Dialog.confirm("确定添加到相同地址？", function(yes){
					if(yes){
						Ajax.postJson("admin/address/batchUpdateAddressEntityCategory", {
							addressCode	:	$("#curAddressCode").val(),
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
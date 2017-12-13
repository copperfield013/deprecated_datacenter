<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<style>
	#address-same-list-right  {
		border: 1px solid #d9d9d9;
		padding: 9px 20px 40px 20px;
	}
	#address-same-list-right .search-wrap .search-input {
		width: 300px;
	}
	#address-same-list-right .address-subtitle {
		margin:19px 0 10px 0;
		font-size: 14px;
		color: #126def;
	}
</style>
<div id="address-same-list-right">
	<form action="admin/address/getAddressList"  class="form-inline" id="address-same-list-right-search">
		<input type="hidden" id="curAddressCode" name="addressCode" value="${addressCode }"/>
		<div class="operation-search">
			<label class="search-label" for="addressStr">地址名称</label>
			<span class="colon">:</span>
			<div class="search-wrap">
				<input type="text" class="search-input" id="addressStr" name="addressStr" value="${addressStr }" />
				<span id="address-search-btn" class="search-button">查询</span>
			</div>
		</div>
		<!-- <button type="submit" id="address-search-btn" class="btn btn-default">查询</button> -->
		<div class=" list-area">
			<h4 class="address-subtitle">全部地址列表</h4>
			<table class="table">
				<thead>
					<tr>
						<th><input type="checkbox" id="select-all-address-ids"/><span class="zcheckbox all-choose"></span></th>
						<th>序号</th>
						<th class="td-tleft">地址名称</th>
						<!-- <th></th> -->
						<!-- <th>操作</th> -->
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${addressList }" var="address" varStatus="i">
						<tr data-name="${address.name }">
							<td><input type="checkbox" name="address-id-checkbox"/><span class="zcheckbox"></span></td>
							<td>${i.index + 1 }</td> 	
							<td class="td-tleft"><a class="address_detail_hover" href="#">${address.name }</a></td>
							<!-- <td>
								<a class="address_add_category" href="#">选择</a>
							</td> -->
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
		$("#address-search-btn",$page).click(function(){
			$('#address-same-list-right-search').submit();
		})
		
	});
</script>
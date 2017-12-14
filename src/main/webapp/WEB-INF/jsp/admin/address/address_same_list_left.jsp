<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<style>
	#address-same-list-left {
		border: 1px solid #d9d9d9;
		padding: 20px 20px 40px 20px;
	}
	
	#address-same-list-left .address-detailinfor {
		margin-left: 10px;
	}	
	#remove-from-the-same {
		display: block;
	    color: #ffffff;
	    background-color: #126def;
	    width: 30px;
	    margin-left: 10px;
	    text-align: center;
	    font-size: 16px;
	    border: 1px solid #126def;
	    margin-bottom: 20px;
	    cursor: pointer;
	    
	}
	#move-to-the-same {
		display: block;
	    color: #656565;
	    background-color: #f3f3f3;
	    width: 30px;
	    margin-left: 10px;
	    text-align: center;
	    font-size: 16px;
	    border: 1px solid #d9d9d9;
	    cursor: pointer;
	}
</style>
<div id="address-same-list-left">
	<span>地址名称：</span>
	<span class="address-detailinfor">${addressName }</span>
	<form action="admin/address/sameList">
		<input type="hidden" name="addressName" value="${addressName }"/>
		<div class="list-area">
			<h4 class="address-subtitle">相同地址列表</h4>
			<table class="table">
				<thead>
					<tr>
						<th><input type="checkbox" id="select-all-the-same-address-name"/><span class="zcheckbox all-choose"></span></th>
						<th>序号</th>
						<th class="td-tleft">地址名称</th>
						<!-- <th></th> -->
						<!-- <th>操作</th> -->
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${sameList }" var="address" varStatus="i">
						<tr data-name="${address.name }">
							<td><input type="checkbox" name="same-address-name"/><span class="zcheckbox"></span></td>
							<td>${i.index + 1 }</td>
							<td class="td-tleft"><a href="#" title="详情-${address.name }" >${address.name }</a></td>
							<!-- <td>
								<a class="address-remove-category" href="#">移除</a>
							</td> -->
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="cpf-paginator" pageNo="${theSamePageInfo.pageNo }" pageSize="${theSamePageInfo.pageSize }" count="${theSamePageInfo.count }"></div>
		</div>
	</form>
</div>
<div class="opera-move">
	<span id="remove-from-the-same">></span>
	<span id="move-to-the-same"> < </span>
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
					 $(this).prop("checked",true).next('span').addClass('active');
					 //console.log("all checked");
				});
			}else{
				$("input[name='same-address-name']", $page).each(function(){
					$(this).removeAttr("checked",false).next('span').addClass('active');
					 //console.log("remove all checked");
				});
			}
		});
		
		//勾选框选择
		/* $("#move-to-the-same", $page).click(function(){
			console.log("1")
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
		}); */
		
		/* $("#remove-from-the-same", $page).click(function(){
			console.log("1")
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
								console.log(Page.getPage("address-same-list"));
								console.log(Page.getPage("address-all-list"));
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
		}); */
		
		
		
	});
</script>
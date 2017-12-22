<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<style>
	/* 左右两个框 */
	#address-category-list #address-same-list {
		float:left;
		padding-right: 25px;
		width: 50%;
		position: relative;
	}
	#address-category-list #address-all-list {
		float:left;
		padding-left: 25px;
		width: 50%;
	}
	/* 左右按钮块 */
	#address-category-list .opera-move {
		position: absolute;
		top: 0;
		right: -25px;
		background-color: #ffffff;
		width: 50px;
		height: 100%;
		padding-top: 136px;
		z-index: 9;
	}
	#address-category-list .table {
		border: 1px solid transparent;
	}
</style>
<div id="address-category-list" class="zpage margin-b30 clear-fix">
	<h1 class="zpage-title">相同地址</h1>
	<div id="address-same-list" class="cpf-inner-page" inner-page-id="address-same-list" url="admin/address/sameList?addressCode=${addressCode }">
		<jsp:include page="address_same_list_left.jsp"></jsp:include>		
	</div>
	<div id="address-all-list" class="cpf-inner-page" inner-page-id="address-all-list" url="admin/address/getAddressList?addressCode=${addressCode }">
		<jsp:include page="address_same_list_right.jsp"></jsp:include>
	</div>
</div>
<script>
	seajs.use(['page', 'ajax', 'dialog', 'utils'], function(Page, Ajax, Dialog, Utils){
		var $page = $('#address-category-list');
		
		$page.on('click','.zcheckbox',function(){
			if($(this).hasClass('active')){
				$(this).removeClass('active').prev('input').prop("checked",false);
				if($(this).hasClass('all-choose')){
					$(this).closest('table').find('.zcheckbox').removeClass('active').prev('input').prop("checked",false);
				}
			}else {
				$(this).addClass('active').prev('input').prop("checked",true);
				if($(this).hasClass('all-choose')){
					$(this).closest('table').find('.zcheckbox').addClass('active').prev('input').prop("checked",true);
				}
			}
		})
		
		$page.on("click","#move-to-the-same",function(){
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
		});
		$page.on("click","#remove-from-the-same",function(){
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
								/* console.log(Page.getPage("address-category-list")); */
								/* Page.getPage("address-category-list").refresh(); */
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
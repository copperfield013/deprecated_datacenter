<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-same-list-right">
	<form action="admin/address/getAddressList"  class="form-inline">
		<input type="hidden" id="curAddressCode" name="curAddressCode" value="${addressEntity.code }"/>
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
						<th>序号</th>
						<th>地址名称</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${addressList }" var="address" varStatus="i">
						<tr data-name="${address.name }">
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
	seajs.use(['Dialog', 'ajax', 'utils'], function(Dialog, Ajax, Utils){
		var $page = $('#address-same-list-right');
		
		/* $("#address-search-btn", $page).click(function(){
			var addressStr = $("#addressStr").val();
			var pageInfo = '${addressPageInfo}';
			Ajax.ajax("admin/address/getAddressList",{
				addressStr	:	addressStr,
				pageInfo	:	pageInfo
			},{
				//TODO ajax的回调函数，提交后的处理方法
			})
		}); */
		
		$(".address_add_category", $page).click(function(){
			var curAddressCode = $("#curAddressCode").val();
			var addressName = $(this).closest('tr[data-name]').attr('data-name');
			Dialog.confirm("确定添加到相同地址？", function(yes){
				if(yes){
					Ajax.ajax("admin/address/updateAddressEntityCategory", {
						addressCode	:	curAddressCode,
						addressName	:	addressName
					}, {
						//TODO 回调  请求提交后页面的处理方法
					});
				}
			});
		});
	});
</script>
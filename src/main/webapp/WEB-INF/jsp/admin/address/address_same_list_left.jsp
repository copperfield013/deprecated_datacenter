<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-same-list-left">
	<div class="row">
		<label class="col-lg-2">地址名称</label>
		<div class="col-lg-4">${addressEntity.name }</div>
	</div>
	<form action="#">
		<div class="list-area">
			<h4>相同地址列表</h4>
			<table class="table">
				<thead>
					<tr>
						<!-- <th><input type="checkbox"></th> -->
						<th>序号</th>
						<th>地址名称</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${sameList }" var="address" varStatus="i">
						<tr data-name="${address.name }">
							<!-- <td><input type="checkbox"></td> -->
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
	seajs.use(['ajax', 'dialog', 'utils'], function(Ajax, Dialog, Utils){
		var $page = $('#address-category-list-left');
		
		$(".address-remove-category", $page).click(function(){
			var addressName = $(this).closest('tr[data-name]').attr("data-name");
			Dialog.confirm("确定移除？", function(yes){
				if(yes){
					Ajax.ajax("admin/address/remove", {
						addressStr	:	addressName
					}, {
						//TODO 回调--移除请求提交后的处理方法
					})
				}
			})
		});
	});
</script>
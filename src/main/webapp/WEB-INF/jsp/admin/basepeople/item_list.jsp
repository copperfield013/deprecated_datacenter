<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="item-list">
	<nav>

		<form class="form-inline" action="admin/people/diclist">
			<a class="btn btn-primary tab" href="admin/people/add" title="创建人口" target="people_add" >创建</a>
		</form>

	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>id</th>
					<th>枚举名</th>
					<th>枚举值</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>

			<c:forEach items="${list }" var="item" varStatus="i">
					<tr>
						<td>${item.c_id}</td>
						<td>${item.c_enum_cn_name }</td>
						<td>${item.c_enum_value }</td>
						<td>
123
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
	</div>
</div>

<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#people-list');
		Utils.datepicker($('#date', $page));
	});
</script>
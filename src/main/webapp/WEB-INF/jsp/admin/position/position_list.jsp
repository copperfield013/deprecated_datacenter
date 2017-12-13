<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="position-list">
	<nav>
		<form class="form-inline" action="admin/position/position_list">
			<div class="form-group">
				<label for="name">名称</label>
				<input type="text" class="form-control" name="name" value="${criteria.name }" />
			</div>
			<div class="form-group">
				<label for="alias">别名</label>
				<input type="text" class="form-control" name="alias" value="${criteria.alias }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a class="btn btn-primary tab" href="admin/position/position_add" title="添加地点" target="position_add" >添加</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>名称</th>
					<th>别名</th>
					<!-- <th>状态</th> -->
					<th>级别</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="position" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td><a class="tab" href="admin/position/position_detail/${position.code }" target="position_detail_${position.name }" title="详情-${position.name }" >${position.name }</a></td>
						<td>${position.alias }</td>
						<%-- <td>${position.status }</td> --%>
						<td>${position.levelName }</td>
						<td>
							<a class="tab" href="admin/position/position_edit/${position.code }" target="position_edit_${position.name }" title="修改-${position.name }">修改</a>
							<a href="admin/position/position_delete/${position.code }" confirm="确认删除？">删除</a>
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
		var $page = $('#position-list');
	});
</script>
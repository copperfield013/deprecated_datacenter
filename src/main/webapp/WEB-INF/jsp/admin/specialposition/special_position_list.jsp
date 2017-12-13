<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="special-position-list">
	<nav>
		<form class="form-inline" action="admin/special_position/special_position_list">
			<div class="form-group">
				<label for="name">名称</label>
				<input type="text" class="form-control" name="name" value="${criteria.name }" />
			</div>
			<div class="form-group">
				<label for="alias">通用名称</label>
				<input type="text" class="form-control" name="alias" value="${criteria.commonName }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a class="btn btn-primary tab" href="admin/special_position/special_position_add" title="添加地点" target="special_position_add" >添加</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>名称</th>
					<th>通用名称</th>
					<th>所属行政区域</th>
					<th>级别</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="specialPosition" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td><a class="tab" href="admin/special_position/special_position_detail/${specialPosition.id }" target="special_position_detail_${specialPosition.name }" title="详情-${specialPosition.name }" >${specialPosition.name }</a></td>
						<td>${specialPosition.commonName }</td>
						<td>${specialPosition.belongPosition }</td>
						<td>${specialPosition.levelName }</td>	
						<td>
							<a class="tab" href="admin/special_position/special_position_edit/${specialPosition.id }" target="special_position_edit_${specialPosition.name }" title="修改-${specialPosition.name }">修改</a>
							<a href="admin/special_position/special_position_delete/${specialPosition.id }" confirm="确认删除？">删除</a>
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
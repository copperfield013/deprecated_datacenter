<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="special-position-list" class="zpage">
	<h1 class="zpage-title">特殊地名</h1>
	<div class="operation-bar">
		<form class="form-inline" action="admin/special_position/special_position_list">
			<div class="zform-group">
				<span class="zform-label">名称</span>
				<input type="text" class="basic-input list-input margin-r10" name="name" value="${criteria.name }" />
			</div>
			<div class="zform-group">
				<label  class="zform-label">通用名称</label>
				<input type="text" class="basic-input list-input margin-r10" name="alias" value="${criteria.commonName }" />
			</div>
			<button type="submit" class="operation-small-btn margin-l10">查询</button>
			<a class="operation-small-btn margin-l10 tab" href="admin/special_position/special_position_add" title="添加地点" target="special_position_add" >添加</a>
		</form>
	</div>
	<div class="list-area">
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
				<c:forEach items="${list }" var="specialPositionEntity" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td><a class="tab" href="admin/special_position/special_position_detail/${specialPositionEntity.specialPosition.id }" target="special_position_detail_${specialPositionEntity.specialPosition.name }" title="详情-${specialPositionEntity.specialPosition.name }" >${specialPositionEntity.specialPosition.name }</a></td>
						<td>${specialPositionEntity.specialPosition.commonName }</td>
						<td>${specialPositionEntity.belongPositionName }</td>
						<td>${specialPositionEntity.specialPosition.levelName }</td>	
						<td>
							<a class="tab" href="admin/special_position/special_position_edit/${specialPositionEntity.specialPosition.id }" target="special_position_edit_${specialPositionEntity.specialPosition.name }" title="修改-${specialPositionEntity.specialPosition.name }">修改</a>
							<a href="admin/special_position/special_position_delete/${specialPositionEntity.specialPosition.id }" confirm="确认删除？">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="cpf-paginator margin-t40 margin-b40" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#position-list');
	});
</script>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="position-list" class="zpage">
	<h1 class="zpage-title">行政区域</h1>
	<div class="operation-bar">
		<form class="form-inline" action="admin/position/position_list">
			<div class="zform-group">
				<span class="zform-label">名称</span>
				<input type="text" class="basic-input list-input margin-r10" name="name" value="${criteria.name }" />
			</div>
			<div class="zform-group">
				<span class="zform-label">别名</span>
				<input type="text" class="basic-input list-input margin-r10" name="alias" value="${criteria.alias }" />
			</div>
			<button type="submit" class="operation-small-btn margin-l10">查询</button>
			<a class="operation-small-btn margin-l10 tab" href="admin/position/position_add" title="添加地点" target="position_add" >添加</a>
		</form>
	</div>
	<div class="list-area">
		<table class="table">
			<thead>
				<tr>
					<th class="index">序号</th>
					<th class="short td-tleft">名称</th>
					<th class="short td-tleft">别名</th>
					<!-- <th>状态</th> -->
					<th class="short-st td-tleft">级别</th>
					<th class="short-st">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="position" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td class="td-tleft"><a class="tab" href="admin/position/position_detail/${position.code }" target="position_detail_${position.name }" title="详情-${position.name }" >${position.name }</a></td>
						<td class="td-tleft">${position.alias }</td>
						<%-- <td>${position.status }</td> --%>
						<td class="td-tleft">${position.levelName }</td>
						<td>
							<a class="tab" href="admin/position/position_edit/${position.code }" target="position_edit_${position.name }" title="修改-${position.name }">修改</a>
							<a href="admin/position/position_delete/${position.code }" confirm="确认删除？">删除</a>
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
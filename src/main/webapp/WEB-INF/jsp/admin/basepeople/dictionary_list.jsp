<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="dictionary-list">
	<nav>

		<form class="form-inline" action="admin/peopleDictionary/list">
			<a class="btn btn-primary tab" href="admin/peopleDictionary/add" title="创建字段" target="dictionary_add" >新增</a>
		</form>

	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>id</th>
					<th>name</th>
					<th>english</th>
					<th>类型</th>
					<th>规则</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="item" varStatus="i">
					<tr>
						<td>${item.cId}</td>
						<td><a class="tab" href="admin/peopleDictionary/update/${item.cId }" target="people_detail_${item.cId }" title="详情">${item.cCnName }</a></td>
						<td>${item.cCnEnglish }</td>
						<td>${item.type}</td>
						<td>${item.check_rule}</td>
						<td>
							<a href="admin/people/itemlist/${item.cCnEnglish }" class="tab" target="item_list_${item.cId }" title="查看">查看枚举值</a>
							<a href="admin/peopleDictionary/do_delete/${item.cId }" confirm="确认删除？">删除</a>
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
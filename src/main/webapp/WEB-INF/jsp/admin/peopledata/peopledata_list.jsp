<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="peopledata-list">
	<nav>
		<form class="form-inline" action="admin/peopledata/list">
			<div class="form-group">
				<label for="name">姓名</label>
				<input type="text" class="form-control" name="name" value="${criteria.name }" />
			</div>
			<div class="form-group">
				<label class="form-control-title" for="address">地址</label>
				<input type="text" class="form-control" name="address" value="${criteria.address }"/>
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a class="btn btn-primary tab" href="admin/peopledata/add" title="创建人口" target="people_add" >创建</a>
			<a class="btn btn-primary tab" href="admin/peopledata/import" title="导入人口" target="people_import">导入</a>
			<a class="btn btn-primary tab" href="admin/peopledata/output" title="导出人口" target="people_output">导出</a>
			<a class="btn tab" href="admin/peopledata/viewtmpl/list" title="模板配置" target="viewtmpl_list">模板配置</a>
			<a class="btn tab" href="admin/peopledata/model" title="字段管理模版" target="people_model">字段管理模版</a>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>姓名</th>
					<th>身份证号</th>
					<th>地址</th>
					<th>联系号码</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="item" varStatus="i">
					<tr>
						<td>${i.index + 1 }</td>
						<td><a class="tab" href="admin/peopledata/detail_tmpl/${item.peopleCode }" target="people_detail_${item.peopleCode }" title="详情-${item.name }">${item.name }</a></td>
						<td>${item.idcode }</td>
						<td>${item.address }</td>
						<td>${item.contact }</td>
						<td>
							<a href="admin/peopledata/update_tmpl/${item.peopleCode }" class="tab" target="people_update_${item.peopleCode }" title="修改-${item.name }">修改</a>
							<%-- <a href="admin/peopledata/smart/${item.peopleCode }" class="tab" target="people_update_${item.peopleCode }" title="修改-${item.name }">修改</a> --%>
							<a href="admin/peopledata/do_delete/${item.peopleCode }" confirm="确认删除？">删除</a>
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
		var $page = $('#peopledata-list');
		
	});
</script>
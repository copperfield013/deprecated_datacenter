<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<style>
input[type=checkbox]{
opacity:1;
position:initial;
}
</style>
<div id="peopledata-output">
	<nav>
		<form class="form-inline" action="admin/peopledata/output">
			<div class="form-group">
				<label for="name">模板名</label>
				<input type="text" class="form-control" name="modelName" value="${criteria.modelName }" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
			<a class="btn btn-primary tab" href="admin/peopledata/outputAdd/0" title="创建模板" target="people_outputAdd" >创建</a>
			<input class="btn btn-primary" type="button" value="导出" onclick="download()"/>
		</form>
	</nav>
	<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>模板名</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="item" varStatus="i">
					<tr>
						<td><input name="model" id="${i.index + 1 }" value="${item.id }" style="opacity: 1; position: static; height: 13px; border: medium none; background-color: rgb(255, 255, 255); cursor: auto;" type="radio">
							${i.index + 1 }</td>
						<td><a class="tab" href="admin/peopledata/output_detail/${item.id }" target="people_detail_${item.id }" title="详情-${item.modelName }">${item.modelName }</a></td>
						<td>
							<a href="admin/peopledata/outputAdd/${item.id }" class="tab" target="people_update_${item.id }" title="修改-${item.modelName }">修改</a>
							<a href="admin/peopledata/output_delete/${item.id }" confirm="确认删除？">删除</a>
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
		var $page = $('#peopledata-output');
		download = function (){
			var a = $("input[type='radio']:checked").val();
			if(a==null){
				alert("请选择一个模板");
				return;
			}
		    var url="admin/peopledata/do_download/"+a;
		    window.open(url);
		};
	});
</script>
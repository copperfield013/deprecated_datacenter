<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="entity-selection">
	<nav>
		<form class="form-inline" action="admin/cascadedict/cascadedictBasicItem/list">
			<div class="form-group">
				<label for="name">名称</label>
				<input type="text" class="form-control" name="name" value="" />
			</div>
			<button type="submit" class="btn btn-default">查询</button>
		</form>
	</nav>
	<div class="row list-area">
		<input type="hidden" id="mappingName" value="${mappingName }">
		<input type="hidden" id="relationName" value="${relationName }">
		<table class="table">
			<thead>
				<tr>
					<th>#</th>
					<c:forEach items="${attrNameList }" var="attr">
						<th>${attr.title }</th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${reulstMap }" var="entityMap" varStatus="i">
					<tr data-id="${entityMap.key }">
						<td>${i.index + 1 }</td>
						<c:forEach items="${entityMap.value }" var="map">
                                <td>${map.value}</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div> 
	</div>
</div>
<div class="modal-footer">
	<div class="row">
		<div class="col-lg-3 col-lg-offset-4">
			<input id="btn-conf" class="btn btn-primary btn-block" type="button" value="确定" /> 
		</div>
	</div>
</div>

<style>
#entity-selection .list-area tbody>tr.selected {
    background-color: #87CEEB;
    color: #fff;
}
</style>

<script>
	seajs.use(['dialog','utils'], function(Dialog, Utils){
		var $page = $('#entity-selection');
		$(function () {
		    //除了表头（第一行）以外所有的行添加click事件.
		    $("tr").slice(1).click(function () {
	            $(this).addClass("selected");
	            $(this).siblings().removeClass("selected");
		    });
		});
		
		$("#btn-conf").on('click', function(){
			var $pag = $("#tree_view_panel");
			var mappingName = $("#entity-selection .list-area").find("#mappingName").val();
			var relationName = $("#entity-selection .list-area").find("#relationName").val();
			
			var code = $("#entity-selection .list-area tbody>tr.selected").attr("data-id");
			$pag.data("selectEntity")(code+ " " + mappingName + " " +relationName);
		});
		
		
	});
</script>
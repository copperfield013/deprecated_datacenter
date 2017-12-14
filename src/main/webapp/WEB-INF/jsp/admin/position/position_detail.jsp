<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail" id="position-detail">
<div class="page-header">
	<div class="header-title">
			<h1>地点-${position.name }-详情</h1>
		</div>
	</div>

	<div class="page-body">
		<%-- <div class="row">
			<label class="col-lg-2">编码</label>
			<div class="col-lg-4">${position.code }</div>
		</div> --%>
		<div class="row">
			<label class="col-lg-2">名称</label>
			<div class="col-lg-4">${position.name }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">别名</label>
			<div class="col-lg-4">${position.alias }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">级别</label>
			<div class="col-lg-4">${position.levelName }</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#address-detail');
	});
</script>
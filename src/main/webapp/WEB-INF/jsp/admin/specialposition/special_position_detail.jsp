<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail" id="special-position-detail">
<div class="page-header">
	<div class="header-title">
			<h1>特殊地名-${specialPosition.name }-详情</h1>
		</div>
	</div>

	<div class="page-body">
		<div class="row">
			<label class="col-lg-2">编码</label>
			<div class="col-lg-4">${specialPosition.id }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">名称</label>
			<div class="col-lg-4">${specialPosition.name }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">通用名称</label>
			<div class="col-lg-4">${specialPosition.commonName }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">所属行政区域</label>
			<div class="col-lg-4">${specialPosition.belongPosition }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">级别</label>
			<div class="col-lg-4">${specialPosition.levelName }</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#special-position-detail');
	});
</script>
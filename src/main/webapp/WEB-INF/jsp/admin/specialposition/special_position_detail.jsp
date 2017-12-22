<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="zpage" id="special-position-detail">
	<h1 class="zpage-title">特殊地名-${specialPosition.name }-详情</h1>


		<%-- <div class="row">
			<label class="col-lg-2">编码</label>
			<div class="col-lg-4">${specialPosition.id }</div>
		</div> --%>
		<div class="zform-group margin-t15">
			<span class="zform-label">名称</span>
			<div class="form-group zform-item">${specialPosition.name }</div>
		</div>
		<div class="zform-group">
			<span class="zform-label">通用名称</span>
			<div class="form-group zform-item">${specialPosition.commonName }</div>
		</div>
		<div class="zform-group">
			<span class="zform-label">所属行政区域</span>
			<div class="form-group zform-item">${belongPositionName }</div>
		</div>
		<div class="zform-group">
			<span class="zform-label">级别</span>
			<div class="form-group zform-item">${specialPosition.levelName }</div>
		</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#special-position-detail');
	});
</script>
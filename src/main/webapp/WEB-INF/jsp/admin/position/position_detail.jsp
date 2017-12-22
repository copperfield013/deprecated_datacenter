<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="zpage" id="position-detail">
	<h1 class="zpage-title">地点-${position.name }-详情</h1>


		<%-- <div class="row">
			<label class="col-lg-2">编码</label>
			<div class="col-lg-4">${position.code }</div>
		</div> --%>
		<div class="zform-group margin-t15">
			<span class="zform-label">名称</span>
			<div class="zform-item">${position.name }</div>
		</div>
		<div class="zform-group">
			<span class="zform-label">别名</span>
			<div class="zform-item">${position.alias }</div>
		</div>
		<div class="zform-group">
			<span class="zform-label">级别</span>
			<div class="zform-item">${position.levelName }</div>
		</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#address-detail');
	});
</script>
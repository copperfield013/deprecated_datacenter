<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-category-list">
	<div class="page-header">
		<div class="header-title">
			<h1>相同地址</h1>
		</div>
	</div>
	<div id="address-same-list" style="width:48%;float:left;">
		<jsp:include page="address_same_list_left.jsp"></jsp:include>
	</div>
	<div id="address-all-list" style="width:48%;float:right;">
		<jsp:include page="address_same_list_right.jsp"></jsp:include>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#address-category-list');
	});
</script>
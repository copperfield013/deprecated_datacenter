<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<style>
.page-body {
	border-top: 1px solid #999999;
	padding-right: 0;
}

.form-inline {
	display:inline-block;
}
.aaa  
    {float:left; width:150px; height:35px; margin:10px;padding:10px;border:1px solid #aaaaaa;
    text-align:center;}
</style>

<div id="peopledata_output_detail${id}">
	<div class="page-header">
		<div class="header-title">
			<h1>模板查看</h1>
		</div>
	</div>
	<nav>
		
		<div class="form-inline" >
			<input id='modelId' type='hidden' name='id' value='${model.id}' />
			<div style="padding:2px">
				<label for="search">模板名:${model.modelName}</label> 
			</div>

		</div>
		
	</nav>
	
	<div class="page-body">
		<c:forEach items="${list }" var="item" varStatus="i">
					<div class='aaa'>${item.cCnName }</div>
		</c:forEach>
	
</div>
<script>
	seajs.use(['ajax', 'dialog','utils'], function(Ajax, Dialog,Utils){
		var $page = $('#peopledata_output_detail${id}');		 
	});
</script>

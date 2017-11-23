<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail" id="address-detail">
	<div class="page-header">
		<div class="header-title">
			<h1>地址-${addressStr }-详情</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<label class="col-lg-2">地址名称</label>
			<div class="col-lg-4">${addressStr }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">地点编码</label>
			<div class="col-lg-4">${splitedAddressEntity.positionCode }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">地点名/楼栋名</label>
			<div class="col-lg-8">${splitedAddressEntity.keyPoint }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">后址</label>
			<div class="col-lg-8">${splitedAddressEntity.laterPart }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">分词地址</label>
			<div class="col-lg-8">
					<%-- <c:forEach items="${splitedAddressEntity.elements }" var="addressElement">
						${addressElement.content }[${addressElement.level },${addressElement.termWord }]&nbsp;&nbsp;
					</c:forEach> --%>
					${splitedAddressEntity.splitNameToShow }
			</div>
		</div>
		<div class="row">
			<label class="col-lg-2">人工分词地址</label>
			<div class="col-lg-8">
				<c:if test="${not empty splitedAddressEntity.artificialElements }">
					<%-- <c:forEach items="${splitedAddressEntity.artificialElements }" var="artificialElements">
						${artificialElements.content }[${artificialElements.level },${artificialElements.termWord }]&nbsp;&nbsp;
					</c:forEach> --%>
					${splitedAddressEntity.artificialSplitNameToShow }
				</c:if>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#address-detail');
	});
</script>
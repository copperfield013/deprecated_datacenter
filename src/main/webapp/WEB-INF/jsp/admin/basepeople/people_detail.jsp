<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail" id="people-${people.id }">
	<div class="page-header">
		<div class="header-title">
			<h1>人物-${people.name }-详情</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<label class="col-lg-2">姓名</label>
			<div class="col-lg-5">${people.name }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">身份证号</label>
			<div class="col-lg-5">${people.idcode }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">性别</label>
			<div class="col-lg-5">
				<c:if test="${people.gender==1 }"> 男 </c:if>
				<c:if test="${people.gender==0 }"> 女 </c:if>
			</div>
		</div>
		<div class="row">
			<label class="col-lg-2">生日</label>
			<div class="col-lg-5">${people.birthday }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">居住地址</label>
			<div class="col-lg-5">${people.address }</div>
		</div>
		<div class="row">
			<label class="col-lg-2">联系号码</label>
			<div class="col-lg-5">${people.contact }</div>
		</div>
	</div>
</div>
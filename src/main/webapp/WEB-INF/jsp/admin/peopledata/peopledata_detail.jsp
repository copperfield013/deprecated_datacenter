<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail" id="people-${people.peopleCode }">
	<div class="page-header">
		<div class="header-title">
			<h1>${people.name }-详情</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<label class="col-lg-1">姓名</label>
			<div class="col-lg-4">${people.name }</div>
			<label class="col-lg-1">身份证号</label>
			<div class="col-lg-4">${people.idcode }</div>
		</div>
		<div class="row">
			<label class="col-lg-1">性别</label>
			<div class="col-lg-4">
				${people.gender }
			</div>
			<label class="col-lg-1">生日</label>
			<div class="col-lg-4">${people.birthday }</div>
		</div>
		<div class="row">
			<label class="col-lg-1">居住地址</label>
			<div class="col-lg-4">${people.address }</div>
			<label class="col-lg-1">联系号码</label>
			<div class="col-lg-4">${people.contact }</div>
		</div>
		<div class="row">
			<label class="col-lg-1">籍贯</label>
			<div class="col-lg-4">${people.nativePlace }</div>
			<label class="col-lg-1">户籍地址</label>
			<div class="col-lg-4">${people.householdPlace }</div>
		</div>
		<div class="row">
			<label class="col-lg-1">民族</label>
			<div class="col-lg-4">${people.nation }</div>
			<label class="col-lg-1">政治面貌</label>
			<div class="col-lg-4">${people.politicalStatus }</div>
		</div>
		<div class="row">
			<label class="col-lg-1">婚姻状况</label>
			<div class="col-lg-4">${people.maritalStatus }</div>
			<label class="col-lg-1">宗教信仰</label>
			<div class="col-lg-4">${people.religion }</div>
		</div>
		<div class="row">
			<label class="col-lg-1">健康状况</label>
			<div class="col-lg-4">${people.healthCondition }</div>
			<label class="col-lg-1">人口类型</label>
			<div class="col-lg-4">${people.peopleType }</div>
		</div>
	</div>
</div>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail" id="people-${peopleCode }">
	<div class="page-header">
		<div class="header-title">
			<h1>${people.name }-详情</h1>
		</div>
		<div class="header-buttons">
			<div>
				<label>数据时间：</label>
				<input class="form-control" css-width="200px" style="margin-top:3px;display: inline;margin-right: 1em;" type="text" id="datetime" placeholder="选择时间" value="${datetime }" />
			</div>
		</div>
	</div>
	<div class="page-body">
		<c:if test="${people != null }">
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
				<div class="col-lg-4">
					<fmt:formatDate value="${people.birthday }" pattern="yyyy年MM月dd日" />
				</div>
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
			<div class="row">
				<label class="col-lg-1">家庭医生</label>
				<div class="col-lg-4">${people.familyDoctor.name }</div>
			</div>
			<div class="row">
				<label class="col-lg-1">残疾证号</label>
				<div class="col-lg-4">${people.handicappedCode }</div>
				<label class="col-lg-1">残疾类型</label>
				<div class="col-lg-4">${people.handicappedType }</div>
			</div>
			<div class="row">
				<label class="col-lg-1">残疾等级</label>
				<div class="col-lg-4">${people.handicappedLevel }</div>
			</div>
			<div class="row">
				<label class="col-lg-1">低保证号</label>
				<div class="col-lg-4">${people.lowIncomeInsuredCode }</div>
				<label class="col-lg-1">低保人员类别</label>
				<div class="col-lg-4">${people.lowIncomeInsuredType }</div>
			</div>
		</c:if>
	</div>
</div>
<script>
	seajs.use(['dialog'], function(Dialog){
		var $page = $('#people-${peopleCode }');
		var hasRecord = '${people != null}';
		if(hasRecord != 'true'){
			Dialog.notice('数据不存在', 'warning');
		}
		
		$('#datetime', $page).datetimepicker({
			language	: 'zh-CN',
			format		: 'yyyy-mm-dd hh:ii:ss',
			minuteStep	: 1,
			autoclose	: true,
			startView	: 'day'
		}).on('changeDate', function(e){
			$page.getLocatePage().loadContent('admin/peopledata/detail/${peopleCode }', undefined, {
				datetime	: $(this).val()
			});
		});
	});
</script>
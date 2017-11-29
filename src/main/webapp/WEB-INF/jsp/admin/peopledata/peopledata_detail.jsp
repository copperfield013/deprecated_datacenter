<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div class="detail" id="people-${peopleCode }">
<style>
	#people-${peopleCode } .toggle-history{
	    position: absolute;
	    right: 2em;
	    display: inline-block;
	    top: 0;
	    vertical-align: middle;
	    height: 40px;
	    line-height: 40px;
	}
	#people-${peopleCode } div#errors {
	    width: 300px;
	    height: 200px;
	    position: absolute;
	    border: 3px solid #ccc;
	    top: 30px;
	    left: 100px;
	    background: #fff;
	    padding: 20px 10px;
	    overflow-x: hidden;
	    overflow-y: auto;
	}
</style>
	<div class="page-header">
		<div class="header-title">
			<h1>${people.name }-详情</h1>
			<c:if test="${people.errors != null && fn:length(people.errors) > 0 }">
			    <h1 id="showErrors" class="fa fa-info-circle" style="cursor: pointer;color: #CD5C5C;"></h1>
			</c:if>
		</div>
		<c:if test="${datetime == null}">
			<div class="toggle-history">
				<a href="#">查看历史</a>
			</div>
		</c:if>
		<div class="header-buttons" style="${datetime == null?'display:none':''}">
			<div>
				<label>数据时间：</label>
				<input class="form-control" css-width="200px" style="margin-top:3px;display: inline;margin-right: 1em;" type="text" id="datetime" placeholder="选择时间" value="${datetime }" />
			</div>
		</div>
	</div>
	<div class="page-body">
		<c:if test="${people != null }">
			<div class="row">
				<label class="col-lg-2">姓名</label>
				<div class="col-lg-4">${people.name }</div>
				<label class="col-lg-2">身份证号</label>
				<div class="col-lg-4">${people.idcode }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">性别</label>
				<div class="col-lg-4">
					${people.gender }
				</div>
				<label class="col-lg-2">生日</label>
				<div class="col-lg-4">
					<fmt:formatDate value="${people.birthday }" pattern="yyyy年MM月dd日" />
				</div>
			</div>
			<div class="row">
				<label class="col-lg-2">居住地址</label>
				<div class="col-lg-4">${people.address }</div>
				<label class="col-lg-2">联系号码</label>
				<div class="col-lg-4">${people.contact }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">籍贯</label>
				<div class="col-lg-4">${people.nativePlace }</div>
				<label class="col-lg-2">户籍地址</label>
				<div class="col-lg-4">${people.householdPlace }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">民族</label>
				<div class="col-lg-4">${people.nation }</div>
				<label class="col-lg-2">人口类型</label>
				<div class="col-lg-4">${people.peopleType }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">婚姻状况</label>
				<div class="col-lg-4">${people.maritalStatus }</div>
				<label class="col-lg-2">宗教信仰</label>
				<div class="col-lg-4">${people.religion }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">健康状况</label>
				<div class="col-lg-4">${people.healthCondition }</div>
				<%-- <label class="col-lg-2">家庭医生</label>
				<div class="col-lg-4">${people.familyDoctor.name }</div> --%>
			</div>
			<div class="row">
				<h4>残疾人信息</h4>
			</div>
			<div class="row">
				<label class="col-lg-2">残疾证号</label>
				<div class="col-lg-4">${people.handicappedCode }</div>
				<label class="col-lg-2">残疾类型</label>
				<div class="col-lg-4">${people.handicappedType }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">残疾级别</label>
				<div class="col-lg-4">${people.handicappedLevel }</div>
				<label class="col-lg-2">残疾原因</label>
				<div class="col-lg-4">${people.handicappedReason }</div>
			</div>
			<div class="row">
				<h4>低保信息</h4>
			</div>
			<div class="row">
				<label class="col-lg-2">低保证号</label>
				<div class="col-lg-4">${people.lowIncomeInsuredCode }</div>
				<label class="col-lg-2">低保人员类别</label>
				<div class="col-lg-4">${people.lowIncomeInsuredType }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">低保原因</label>
				<div class="col-lg-4">${people.lowIncomeInsuredReason }</div>
				<label class="col-lg-2">享受低保标识</label>
				<div class="col-lg-4">${people.lowIncomeInsuredId }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">享受低保金额</label>
				<div class="col-lg-4">${people.lowIncomeInsuredAmount }</div>
				<label class="col-lg-2">享受开始日期</label>
				<div class="col-lg-4"><fmt:formatDate value="${people.lowIncomeInsuredStart }" pattern="yyyy年MM月dd日" /></div>
			</div>
			<div class="row">
				<label class="col-lg-2">享受结束日期</label>
				<div class="col-lg-4"><fmt:formatDate value="${people.lowIncomeInsuredEnd }" pattern="yyyy年MM月dd日" /></div>
			</div>
			
			<div class="row">
				<h4>失业信息</h4>
			</div>
			
			<div class="row">
				<label class="col-lg-2">就失业日期</label>
				<div class="col-lg-4"><fmt:formatDate value="${people.unemployeeDate }" pattern="yyyy年MM月dd日" /></div>
				<label class="col-lg-2">就失业证号</label>
				<div class="col-lg-4">${people.unemployeeCode }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">就失业状态</label>
				<div class="col-lg-4">${people.unemployeeStatus }</div>
				<label class="col-lg-2">就业标识</label>
				<div class="col-lg-4">${people.employeeId }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">就业困难人员类型</label>
				<div class="col-lg-4">${people.hardToEmployeeType }</div>
				<label class="col-lg-2">就业类型</label>
				<div class="col-lg-4">${people.employeeType }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">就业能力</label>
				<div class="col-lg-4">${people.employeeCapacity }</div>
				<label class="col-lg-2">就业情况</label>
				<div class="col-lg-4">${people.employeeSituation }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">就业去向</label>
				<div class="col-lg-4">${people.employeeDestination }</div>
				<label class="col-lg-2">就业途径</label>
				<div class="col-lg-4">${people.employeeWay }</div>
			</div>
	
			<div class="row">
				<h4>党员信息</h4>
			</div>


			<!-- 党员信息 -->
			<div class="row">
				<label class="col-lg-2">政治面貌</label>
				<div class="col-lg-4">${people.politicalStatus }</div>
				<label class="col-lg-2">入党日期</label>
				<div class="col-lg-4"><fmt:formatDate value="${people.partyDate }" pattern="yyyy年MM月dd日" /></div>
			</div>
			<div class="row">
				<label class="col-lg-2">党内职务</label>
				<div class="col-lg-4">${people.partyPost }</div>
				<label class="col-lg-2">所在党组织</label>
				<div class="col-lg-4">${people.partyOrganization }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">党组织隶属</label>
				<div class="col-lg-4">${people.partySuperior }</div>
				<label class="col-lg-2">党组织联系电话</label>
				<div class="col-lg-4">${people.partyOrgContact }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">共青团组织</label>
				<div class="col-lg-4">${people.CYOrganization }</div>
			</div>

			<div class="row">
				<h4>工作信息</h4>
			</div>

			<!-- 工作信息 -->
			<div class="row">
				<label class="col-lg-2">公司名${people.workExperiences.size()}</label>
				<div class="col-lg-4">${people.workExperiences[0].companyName }</div>
				<label class="col-lg-2">工作单位</label>
				<div class="col-lg-4">${people.workExperiences[0].workUnit }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">工作地址</label>
				<div class="col-lg-4">${people.workExperiences[0].workAddress }</div>
				<label class="col-lg-2">单位电话</label>
				<div class="col-lg-4">${people.workExperiences[0].unitContact }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">当前部门</label>
				<div class="col-lg-4">${people.workExperiences[0].workDepartment }</div>
				<label class="col-lg-2">单位性质</label>
				<div class="col-lg-4">${people.workExperiences[0].unitNature }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">工资（元）</label>
				<div class="col-lg-4">${people.workExperiences[0].salary }</div>
				<label class="col-lg-2">工作内容</label>
				<div class="col-lg-4">${people.workExperiences[0].workContent }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">工作职责</label>
				<div class="col-lg-4">${people.workExperiences[0].workDuty }</div>
				<label class="col-lg-2">工作主题</label>
				<div class="col-lg-4">${people.workExperiences[0].workSubject }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">曾从事职业</label>
				<div class="col-lg-4">${people.workExperiences[0].workedOccupation }</div>
			</div>
			
			<div class="row">
				<h4>家庭信息</h4>
			</div>


			<!-- 家庭信息 -->
			<div class="row">
				<label class="col-lg-2">家庭住址</label>
				<div class="col-lg-4">${people.familyInfo.familyAddress }</div>
				<label class="col-lg-2">家庭成员人数</label>
				<div class="col-lg-4">${people.familyInfo.familyCount }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">家庭电话</label>
				<div class="col-lg-4">${people.familyInfo.familyContact }</div>
				<label class="col-lg-2">家庭经济状况</label>
				<div class="col-lg-4">${people.familyInfo.familyFinancialSituation }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">家庭类别</label>
				<div class="col-lg-4">${people.familyInfo.familyType }</div>
				<label class="col-lg-2">家庭年收入（元）</label>
				<div class="col-lg-4">${people.familyInfo.familyYearIncome }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">家庭情况</label>
				<div class="col-lg-4">${people.familyInfo.familySituation }</div>
				<label class="col-lg-2">家庭人均月收入（元）</label>
				<div class="col-lg-4">${people.familyInfo.familyAvgMonthIncome }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">家庭失业人数</label>
				<div class="col-lg-4">${people.familyInfo.familyUnemployeeCount }</div>
			</div>
			
			<div class="row">
				<h4>计生信息</h4>
			</div>

			<!-- 计生信息 -->
			<div class="row">
				<label class="col-lg-2">子女数</label>
				<div class="col-lg-4">${people.childrenCount }</div>
				<label class="col-lg-2">节育措施</label>
				<div class="col-lg-4">${people.contraceptionMeasure }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">现孕周</label>
				<div class="col-lg-4">${people.pregnancyWeeks }</div>
				<label class="col-lg-2">计划生育证编号</label>
				<div class="col-lg-4">${people.familyPlanningCode }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">计划生育证类型</label>
				<div class="col-lg-4">${people.familyPlanningType }</div>
			</div>

		</c:if>
	</div>
	<div id="errors" style="display: none;">
		<ul>
			<c:forEach items="${people.errors }" var="error">
				<li>${error.error_str }</li>
			</c:forEach>
		</ul>
   </div>
</div>
<script>
	seajs.use(['dialog'], function(Dialog){
		var $page = $('#people-${peopleCode }');
		var hasRecord = '${people != null}';
		if(hasRecord != 'true'){
			Dialog.notice('数据不存在', 'warning');
			$('.header-title h1').text('数据不存在');
		}
		$('.toggle-history a', $page).click(function(){
			$(this).closest('.toggle-history').hide();
			$('.header-buttons', $page).show();
		});
		
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
		var $errors = $('#errors', $page);
		$('#showErrors', $page).mouseenter(function(e){
			$errors.show();
		});
		$page.click(function(e){
			var $target = $(e.target);
			if(!$target.is('#showErrors') && $target.closest('#errors').length == 0){
				$errors.hide();
			}
		});
	});
</script>
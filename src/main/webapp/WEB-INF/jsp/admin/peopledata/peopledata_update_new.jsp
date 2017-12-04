<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link rel="stylesheet"
	href="media/admin/bigautocomplete/css/jquery.bigautocomplete.css"
	type="text/css" />
<link rel="stylesheet" href="media/admin/peopleupdate/swinput.css"
	type="text/css" />
<link rel="stylesheet" href="media/admin/peopleupdate/cover.css?v=1.0"
	type="text/css" />
<link rel="stylesheet" href="media/admin/peopleupdate/selection.css"
	type="text/css" />
<script src="media/admin/peopleupdate/swinput.js"></script>
<script src="media/admin/peopleupdate/selection.js"></script>

<div class="inforpage-wrap" id="peopledata-update${people.peopleCode }">
	<h1 class="zpage-title">人口信息</h1>
	<div class="search-part">

		<nav class="smartForm search">
			<div class="zform-group">
				<span class="infor-search" for="search">信息查询</span> 
				<span class="colon">:</span>
				<div class="search-inforbox">
					<input type="text" class="search-input zinput" id="search" name="search" />
					<ul class="search-helpbox">
						
					</ul>
				</div>
				<span type="button" class="search-button" id="smartSubmit">查询</span>
			</div>
			<div class="smartForm-button zclear">
				<span id="editSmartForm">编辑</span> <span id="saveSmartForm">保存</span>
			</div>
			<form id="cloneForm" class=""
				action="admin/peopledata/do_smart_update"  autocomplete="off">
				<div class="cloneBox zclear">
					
				</div>
				<div class="clone-btn">
					<input type="hidden" name="peopleCode"
						value="${people.peopleCode }" />
				</div>
			</form>
		</nav>

	</div>

	<div class="zpage-body zclear search">
		<span class="dragline"></span>
		<div class="integratedForm-button zclear">
			<span id="editIntegratedForm">编辑</span>
			<span id="saveIntegratedForm">保存</span>
		</div>
		<div class="menu-wrap zclear">
			<ul class="zpage-menuview">
				<li class="active" data-group="basic">基本信息</li>
				<li data-group="handicapped">残疾人信息</li>
				<li data-group="lowIncomeInsured">低保信息</li>
				<li data-group="unemployee">失业信息</li>
				<li data-group="political">党员信息</li>
				<li data-group="familyPlanning">计生信息</li>
			</ul>
			<div class="zpage-menubody">
				<form id="integratedForm" class="search"
					action="admin/peopledata/do_update"  autocomplete="off">
					<input type="hidden" name="peopleCode"
						value="${people.peopleCode }">

					<div class="zitem-group zclear" data-group="basic">
						<div class="zitem-list">
							<label class="zlabel list-label" for="name">姓名</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" name="name"
									value="${people.name }" />
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label" for="idcode">身份证号</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" name="idcode"
									id="code" value="${people.idcode }" />
							</div>
						</div>
						
						<div class="zitem-list">
							<label class="zlabel list-label">性别</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="gender"
									data-value="${people.gender}">
									<option value="">-- 请选择 --</option>
									<option value="男性">男性</option>
									<option value="女性">女性</option>
									<option value="不祥">不祥</option>
								</select>
							</div>
						</div>
						
						<div class="zitem-list">
							<label class="zlabel list-label" for="birthday">生日</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" id="date"
									name="birthday" readonly="readonly" css-cursor="text"
									value='<fmt:formatDate value="${people.birthday }" pattern="yyyy-MM-dd" />' />
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label" for="address">居住地址</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" name="address"
									value="${people.address }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="contact">联系号码</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" name="contact"
									id="contact" value="${people.contact }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label">籍贯</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" name="nativePlace"
									value="${people.nativePlace }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="householdPlace">户籍地址</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="householdPlace" value="${people.householdPlace }" />
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label">民族</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="nation"
									data-value="${people.nation }">
									<option value="">-- 请选择 --</option>
									<option value="汉族">汉族</option>
									<option value="蒙古族">蒙古族</option>
									<option value="回族">回族</option>
									<option value="藏族">藏族</option>
									<option value="维吾尔族">维吾尔族</option>
									<option value="苗族">苗族</option>
									<option value="彝族">彝族</option>
									<option value="壮族">壮族</option>
									<option value="布依族">布依族</option>
									<option value="朝鲜族">朝鲜族</option>
									<option value="满族">满族</option>
									<option value="侗族">侗族</option>
									<option value="瑶族">瑶族</option>
									<option value="白族">白族</option>
									<option value="土家族">土家族</option>
									<option value="哈尼族">哈尼族</option>
									<option value="哈萨克族">哈萨克族</option>
									<option value="傣族">傣族</option>
									<option value="黎族">黎族</option>
									<option value="傈僳族">傈僳族</option>
									<option value="佤族">佤族</option>
									<option value="畲族">畲族</option>
									<option value="高山族">高山族</option>
									<option value="拉祜族">拉祜族</option>
									<option value="水族">水族</option>
									<option value="东乡族">东乡族</option>
									<option value="纳西族">纳西族</option>
									<option value="景颇族">景颇族</option>
									<option value="柯尔克孜族">柯尔克孜族</option>
									<option value="土族">土族</option>
									<option value="达斡尔族">达斡尔族</option>
									<option value="仫佬族">仫佬族</option>
									<option value="羌族">羌族</option>
									<option value="布朗族">布朗族</option>
									<option value="撒拉族">撒拉族</option>
									<option value="毛难族">毛难族</option>
									<option value="仡佬族">仡佬族</option>
									<option value="锡伯族">锡伯族</option>
									<option value="阿昌族">阿昌族</option>
									<option value="普米族">普米族</option>
									<option value="塔吉克族">塔吉克族</option>
									<option value="怒族">怒族</option>
									<option value="乌孜别克族">乌孜别克族</option>
									<option value="俄罗斯族">俄罗斯族</option>
									<option value="鄂温克族">鄂温克族</option>
									<option value="德昂族">德昂族</option>
									<option value="保安族">保安族</option>
									<option value="裕固族">裕固族</option>
									<option value="京族">京族</option>
									<option value="塔塔尔族">塔塔尔族</option>
									<option value="独龙族">独龙族</option>
									<option value="鄂伦春族">鄂伦春族</option>
									<option value="赫哲族">赫哲族</option>
									<option value="门巴族">门巴族</option>
									<option value="珞巴族">珞巴族</option>
									<option value="基诺族">基诺族</option>
								</select>
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label">婚姻状况</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="maritalStatus"
									data-value="${people.maritalStatus }">
									<option value="">-- 请选择 --</option>
									<option value="未婚">未婚</option>
									<option value="已婚">已婚</option>
									<option value="初婚">初婚</option>
									<option value="再婚">再婚</option>
									<option value="复婚">复婚</option>
									<option value="丧偶">丧偶</option>
									<option value="离异">离异</option>
									<option value="不详">不详</option>
								</select>
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label">宗教信仰</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="religion"
									data-value="${people.religion }">
									<option value="">-- 请选择 --</option>
									<option value="无宗教信仰">无宗教信仰</option>
									<option value="佛教">佛教</option>
									<option value="喇嘛教">喇嘛教</option>
									<option value="道教">道教</option>
									<option value="天主教">天主教</option>
									<option value="基督教">基督教</option>
									<option value="东正教">东正教</option>
									<option value="伊斯兰教">伊斯兰教</option>
									<option value="其他">其他</option>
								</select>
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label">健康状况</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="healthCondition"
									data-value="${people.healthCondition }">
									<option value="">-- 请选择 --</option>
									<option value="良好">良好</option>
									<option value="较好">较好</option>
									<option value="一般">一般</option>
									<option value="较差">较差</option>
									<option value="很差">很差</option>
								</select>
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label">人口类型</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="peopleType"
									data-value="${people.peopleType }">
									<option value="">-- 请选择 --</option>
									<option value="户籍人口">户籍人口</option>
									<option value="流动人口">流动人口</option>
								</select>
							</div>
						</div>
						<i class="zitem-group-line"></i>
					</div>


					<div class="zitem-group zclear" data-group="handicapped">
						<div class="zitem-list">
							<label class="zlabel list-label" for="handicappedCode">残疾证号</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="handicappedCode" value="${people.handicappedCode }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="handicappedType">残疾类型</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="handicappedType" value="${people.handicappedType }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="handicappedLevel">残疾级别</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="handicappedLevel" value="${people.handicappedLevel }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="handicappedReason">残疾原因</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="handicappedReason" value="${people.handicappedReason }" />
							</div>
						</div>
						<i class="zitem-group-line"></i>
					</div>


					<div class="zitem-group zclear" data-group="lowIncomeInsured">

						<div class="zitem-list">
							<label class="zlabel list-label" for="lowIncomeInsuredCode">低保证号</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="lowIncomeInsuredCode"
									value="${people.lowIncomeInsuredCode }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="lowIncomeInsuredType">低保人员类别</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="lowIncomeInsuredType"
									data-value="${people.lowIncomeInsuredType }">
									<option value="">-- 请选择 --</option>
									<option value="在职职工">在职职工</option>
									<option value="灵活就业人员">灵活就业人员</option>
									<option value="登记失业人员">登记失业人员</option>
									<option value="非登记失业人员">非登记失业人员</option>
									<option value="离退休人员">离退休人员</option>
									<option value="务工人员">务工人员</option>
									<option value="务农人员">务农人员</option>
									<option value="在校生">在校生</option>
									<option value="残疾人">残疾人</option>
									<option value="三无人员">三无人员</option>
									<option value="农垦企业人员">农垦企业人员</option>
									<option value="森工企业人员">森工企业人员</option>
									<option value="两劳释放人员">两劳释放人员</option>
									<option value="归侨侨眷">归侨侨眷</option>
									<option value="非农水库移民">非农水库移民</option>
									<option value="高校毕业生">高校毕业生</option>
									<option value="优抚对象">优抚对象</option>
									<option value="退役军人">退役军人</option>
									<option value="60年代精简退职人员">60年代精简退职人员</option>
									<option value="其它">其它</option>
								</select>
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label" for="lowIncomeInsuredReason">低保原因</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="lowIncomeInsuredReason"
									value="${people.lowIncomeInsuredReason }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="lowIncomeInsuredId">享受低保标识</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="lowIncomeInsuredId" value="${people.lowIncomeInsuredId }" />
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label" for="lowIncomeInsuredAmount">享受低保金额（元）</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="lowIncomeInsuredAmount"
									value="${people.lowIncomeInsuredAmount }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="lowIncomeInsuredStart">享受开始日期</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									id="lowIncomeInsuredStart" name="lowIncomeInsuredStart"
									readonly="readonly" css-cursor="text"
									value='<fmt:formatDate value="${people.lowIncomeInsuredStart }" pattern="yyyy-MM-dd"/>' />
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label" for="lowIncomeInsuredEnd">享受结束日期</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									id="lowIncomeInsuredEnd" name="lowIncomeInsuredEnd"
									readonly="readonly" css-cursor="text"
									value='<fmt:formatDate value="${people.lowIncomeInsuredEnd }" pattern="yyyy-MM-dd"/>' />
							</div>
						</div>
						<i class="zitem-group-line"></i>
					</div>


					<div class="zitem-group zclear" data-group="unemployee">

						<div class="zitem-list">
							<label class="zlabel list-label" for="unemployeeDate">就失业日期</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									id="unemployeeDate" name="unemployeeDate" readonly="readonly"
									css-cursor="text"
									value='<fmt:formatDate value="${people.unemployeeDate }" pattern="yyyy-MM-dd"/>' />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="unemployeeCode">就失业证号</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="unemployeeCode" value="${people.unemployeeCode }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="unemployeeStatus">就失业状态</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="unemployeeStatus" value="${people.unemployeeStatus }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="employeeId">就业标识</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" name="employeeId"
									value="${people.employeeId }" />
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label" for="hardToEmployeeType">就业困难人员类型</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="hardToEmployeeType"
									data-value="${people.hardToEmployeeType }">
									<option value="">-- 请选择 --</option>
									<option value="双失业">双失业</option>
									<option value="单亲有子女上学">单亲有子女上学</option>
									<option value="低保">低保</option>
									<option value="4045大龄人员">4045大龄人员</option>
									<option value="纯农户">纯农户</option>
									<option value="失地农民">失地农民</option>
									<option value="其它">其它</option>
								</select>
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="employeeType">就业类型</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="employeeType" value="${people.employeeType }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="employeeCapacity">就业能力</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="employeeCapacity" value="${people.employeeCapacity }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="employeeSituation">就业情况</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="employeeSituation"
									data-value="${people.employeeSituation }">
									<option value="">-- 请选择 --</option>
									<option value="未填">未填</option>
									<option value="在职">在职</option>
									<option value="学生">学生</option>
									<option value="失业">失业</option>
									<option value="无业">无业</option>
									<option value="离休">离休</option>
									<option value="下岗">下岗</option>
									<option value="其他">其他</option>
									<option value="内退">内退</option>
									<option value="其他">其他</option>
								</select>
							</div>
						</div>

						<div class="zitem-list">
							<label class="zlabel list-label" for="employeeDestination">就业去向</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="employeeDestination"
									value="${people.employeeDestination }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="employeeWay">就业途径</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="employeeWay"
									data-value="${people.employeeWay }">
									<option value="">-- 请选择 --</option>
									<option value="未填">未填</option>
									<option value="工职介绍">工职介绍</option>
									<option value="劳务中介">劳务中介</option>
									<option value="亲友推荐">亲友推荐</option>
									<option value="企业招工">企业招工</option>
									<option value="国家分配">国家分配</option>
									<option value="部队转业">部队转业</option>
									<option value="自由职业">自由职业</option>
									<option value="残疾人">残疾人</option>
									<option value="其他">其他</option>
								</select>
							</div>
						</div>
						<i class="zitem-group-line"></i>
					</div>


					<div class="zitem-group zclear" data-group="political">

						<div class="zitem-list">
							<label class="zlabel list-label" for="politicalStatus">政治面貌</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="politicalStatus"
									data-value="${people.politicalStatus }">
									<option value="">-- 请选择 --</option>
									<option value="群众">群众</option>
									<option value="共青团员">共青团员</option>
									<option value="中共预备党员">中共预备党员</option>
									<option value="中共党员">中共党员</option>
								</select>
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="partyDate">入党日期</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" id="partyDate"
									name="partyDate" readonly="readonly" css-cursor="text"
									value='<fmt:formatDate value="${people.partyDate }" pattern="yyyy-MM-dd"/>' />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="partyPost">党内职务</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input" name="partyPost"
									value="${people.partyPost }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="partyOrganization">所在党组织</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="partyOrganization" value="${people.partyOrganization }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="partySuperior">党组织隶属</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="partySuperior"
									data-value="${people.partySuperior }">
									<option value="">-- 请选择 --</option>
									<option value="社区">社区</option>
									<option value="街道">街道</option>
									<option value="本系统内">本系统内</option>
									<option value="无">无</option>
								</select>
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="partyOrgContact">党组织联系电话</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="partyOrgContact" id="partyOrgContact"
									value="${people.partyOrgContact }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="CYOrganization">共青团组织</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="CYOrganization" id="CYOrganization"
									value="${people.CYOrganization }" />
							</div>
						</div>
						<i class="zitem-group-line"></i>
					</div>

					<div class="zitem-group zclear" data-group = "familyPlanning">

						<div class="zitem-list">
							<label class="zlabel list-label" for="childrenCount">子女数</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="number" class="zinput  list-input"
									name="childrenCount" value="${people.childrenCount }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="contraceptionMeasure">节育措施</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<select class="select-replace" name="contraceptionMeasure"
									data-value="${people.contraceptionMeasure }">
									<option value="">-- 请选择 --</option>
									<option value="上环">上环</option>
									<option value="取环">取环</option>
									<option value="结扎">结扎</option>
									<option value="无">无</option>
								</select>
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="pregnancyWeeks">现孕周</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="number" class="zinput  list-input"
									name="pregnancyWeeks" value="${people.pregnancyWeeks }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="familyPlanningCode">计划生育证编号</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="familyPlanningCode" value="${people.familyPlanningCode }" />
							</div>
						</div>
						<div class="zitem-list">
							<label class="zlabel list-label" for="familyPlanningType">计划生育证类型</label>
							<span class="colon">:</span>
							<div class="zinfor-input-wrap">
								<input type="text" class="zinput  list-input"
									name="familyPlanningType" value="${people.familyPlanningType }" />
							</div>
						</div>
					</div>


				</form>
			</div>
		</div>
	</div>

</div>

<script>
	seajs
			.use(
					[ 'utils', 'ajax' ],
					function(Utils, Ajax) {
						var $page = $('#peopledata-update${people.peopleCode }');
						Utils.datepicker($('#date', $page),$(".zpage-menubody"));
						Utils.datepicker($('#lowIncomeInsuredStart', $page),$(".zpage-menubody"));
						Utils.datepicker($('#lowIncomeInsuredEnd', $page),$(".zpage-menubody"));
						Utils.datepicker($('#unemployeeDate', $page),$(".zpage-menubody"));
						Utils.datepicker($('#partyDate', $page),$(".zpage-menubody"));
						//完整表单处 select 替换 对应的select选中值加上selected属性值,同时加上展示html
						var selectList=$('.zpage-body .select-replace'); 
						for(var i=0; i<selectList.length; i++){							
							var value = $(selectList[i]).attr('data-value');
							var optionList = $(selectList[i]).find('option');
							var name = "";														
							for(var j= 0; j<optionList.length; j++){
								if($(optionList[j]).attr('value') == value){								
									$(optionList[j]).attr("selected","selected")
									name = $(optionList[j]).text();
								}
							}
							var listValueHtml = "<span class='list-value'>"+name+"</span>";
							$(selectList[i]).closest('.zinfor-input-wrap').append(listValueHtml);
						}
						$('.select-replace').Selection({	
							
						});
						
						//完整表单处input加上展示html
						var inputList=$('.zpage-body .list-input'); 
						for(var i=0; i<inputList.length; i++){							
							var value = $(inputList[i]).val();													
							var listValueHtml = "<span class='list-value'>"+value+"</span>";
							$(inputList[i]).closest('.zinfor-input-wrap').append(listValueHtml);
						}
						
						
						var dragline = $('.dragline')[0]; //拖动的线
						var tObj = $('.search-part')[0];  //上半部分界面div
						var bObj = $('.zpage-body')[0];   //下半部分界面div
						
						//定义drag(拖动底线改变div高度) 方法
						var dragFn = function(dragline, tObj, bObj) {
							dragline.onmousedown = function(ev) {
								var oEv = ev || event;
								//记录下原始数据
								var oldY = oEv.clientY;
								var oldHeight = tObj.offsetHeight;
								var limitTop = 100;
								var limitBottom = document.body.clientHeight - 100;
								document.onmousemove = function(ev) {
									var oEv = ev || event;
									if (oEv.clientY > limitTop
											&& oEv.clientY < limitBottom) {
										tObj.style.height = oldHeight
												+ (oEv.clientY - oldY) + 'px';
										bObj.style.top = oldHeight
												+ (oEv.clientY - oldY)+ 56 + 'px';  //56为标题部分
									}
								};

								document.onmouseup = function() {
									document.onmousemove = null;
								};
								//阻止事件冒泡
								oEv.cancelBubble = true;
								return false;
							};
						}
						//调用拖动方法
						dragFn(dragline, tObj, bObj);
						
						//完整表单部分右侧菜单栏点击
						$('.zpage-menuview').on('click','li',function(ev){
							var oEv = ev || event;
							var target = oEv.target;
							var cid = $(target).attr('data-group');
							var groupItem = $('.zitem-group[data-group='+cid+']')[0];
							var scrollD = groupItem.offsetTop-5;
							console.log(scrollD);
							$(target).addClass('active').siblings('li').removeClass('active');
							$('.zpage-menubody').scrollTop(scrollD);
						})
						
						//查询表单部分初始化成查看 方法
						var searchSmartInit = function() {
							var inputList = $('.smartForm.search .cloneBox', $page).find('.zitem-list.input');
							var selectList = $('.smartForm.search .cloneBox', $page).find('.zitem-list.select');
							var value = "";	
							var selected = null;
							for(var i = 0; i< inputList.length; i++){					
								value = $(inputList[i]).find('.zinput.list-input').val();
								$(inputList[i]).find('.list-value').text(value);
							}
							for(var i = 0; i< selectList.length; i++){
								selected = $(selectList[i]).find('select').val();
								//判断是否是数组，数组则是多选
								if(!$.isArray(value)){
									$(selectList[i]).find('.list-value').text(selected);
								}else {
									console.log("多选类型");
								}
							}
							
						}
						
						//查询表单部分初始化成 编辑 方法
						var editSmartInit = function() {
							var inputList = $('.smartForm.edit .cloneBox', $page).find('.zitem-list.input');
							var selectList = $('.smartForm.edit .cloneBox', $page).find('.zitem-list.select');					
							for(var i = 0; i< selectList.length; i++){
								if( $(selectList[i]).find('.selection-container').length === 0 ){ // select没有被初始化,则初始化
									$(selectList[i]).find('.smartForm-select').Selection();
								}	
							}
								
						}
	
						//完整表单部分初始化成查看方法
						var searchInit = function() {
							var inputList = $('.zpage-body.search',$page).find('input.list-input');
							var selectList = $('.zpage-body.search', $page).find('select.select-replace');
							var box = null;
							var selected = "";
							var value = "";
							
							for(var i=0; i<inputList.length; i++){
								value = $(inputList[i]).val();
								$(inputList[i]).parent('.zinfor-input-wrap').find('.list-value').text(value);
							}
							
							for(var i=0; i<selectList.length; i++){								
								selected = $(selectList[i]).val();	
								//判断是否是数组，数组则是多选
								if(!$.isArray(value)){
									$(selectList[i]).parent('.zinfor-input-wrap').find('.list-value').text(selected);
								}else {
									console.log("多选类型");
								}								
							}
						}

												
						//完整表单部分编辑保存按钮的点击事件
						$('#editIntegratedForm', $page)
								.on(
										'click',
										function() {
											//需要进行判断，判断smartForm处是否处于查看状态，否 ，则提醒用户保存smartForm表单（“是否保存已修改过的信息”）
											var smartFormStatus = $(
													'.smartForm', $page)
													.hasClass('edit'); //true 则 smartForm为查看状态
											if (smartFormStatus) {
												var userDirective = confirm("是否保存已修改的信息");
												if (!userDirective) {
													return;
												}
												$('#saveSmartForm', $page)
														.click();
											}
											$('.zpage-body.search', $page)
													.removeClass("search")
													.addClass("edit");
										})

						$('#saveIntegratedForm', $page).on(
								'click',
								function() {
									$('#integratedForm').submit();
									$('.zpage-body.edit', $page).removeClass(
											"edit").addClass("search");
									searchInit();
								})

						//smartForm部分编辑保存按钮的点击事件
						$('#editSmartForm', $page)
								.on(
										'click',
										function() {
											//需要进行判断，判断完整表单处是否处于查看状态，否 ，则提醒用户保存完整表单（“是否保存已修改过的信息”）
											var integratedFormStatus = $(
													'.zpage-body', $page)
													.hasClass('search'); //true 则 smartForm为查看状态
											if (!integratedFormStatus) {
												var userDirective = confirm("是否保存已修改的信息");
												if (!userDirective) {
													return;
												}
												$('#saveIntegratedForm', $page)
														.click();
											}
											$('.smartForm.search', $page)
													.removeClass("search")
													.addClass("edit");
											editSmartInit();
										})

						$('#saveSmartForm', $page).on(
								'click',
								function() {
									$('#cloneForm').submit();
									$('.smartForm.edit', $page).removeClass(
											"edit").addClass("search");
									searchSmartInit();
								})
						
					  //身份验证
						function IdentityCodeValid(code) {
							var city = {
								11 : "北京",
								12 : "天津",
								13 : "河北",
								14 : "山西",
								15 : "内蒙古",
								21 : "辽宁",
								22 : "吉林",
								23 : "黑龙江 ",
								31 : "上海",
								32 : "江苏",
								33 : "浙江",
								34 : "安徽",
								35 : "福建",
								36 : "江西",
								37 : "山东",
								41 : "河南",
								42 : "湖北 ",
								43 : "湖南",
								44 : "广东",
								45 : "广西",
								46 : "海南",
								50 : "重庆",
								51 : "四川",
								52 : "贵州",
								53 : "云南",
								54 : "西藏 ",
								61 : "陕西",
								62 : "甘肃",
								63 : "青海",
								64 : "宁夏",
								65 : "新疆",
								71 : "台湾",
								81 : "香港",
								82 : "澳门",
								91 : "国外 "
							};
							var tip = "";
							var pass = true;

							if (!code
									|| !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i
											.test(code)) {
								tip = "身份证号格式错误";
								pass = false;
							}

							else if (!city[code.substr(0, 2)]) {
								tip = "地址编码错误";
								pass = false;
							} else {
								//18位身份证需要验证最后一位校验位
								if (code.length == 18) {
									code = code.split('');
									//∑(ai×Wi)(mod 11)
									//加权因子
									var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6,
											3, 7, 9, 10, 5, 8, 4, 2 ];
									//校验位
									var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4,
											3, 2 ];
									var sum = 0;
									var ai = 0;
									var wi = 0;
									for (var i = 0; i < 17; i++) {
										ai = code[i];
										wi = factor[i];
										sum += ai * wi;
									}
									var last = parity[sum % 11];
									if (parity[sum % 11] != code[17]) {
										tip = "校验位错误";
										pass = false;
									}
								}
							}
							if (!pass)
								$("#txt").val(tip);
							else
								$("#txt").val("√")
							return pass;
						}

						$("#code").blur(function() {
							var code = $("#code").val();
							IdentityCodeValid(code);
						});
						
						
						//手机验证
						$("#contact").blur(function() {
							var contact = $("#contact").val();
							if (!(/^1[34578]\d{9}$/.test(contact))) {//手机验证
								alert('联系电话有误，请重填');
								return false;
							}
						});
						

						/**
						 *  根据传回的type生成相应的输入框
						 *  1.基础文本输入框  input
						 *  3.单选
						 *  4.多选
						 *  6.date时间控件
						 *  
						 * *
						 * **/

						function addSmartSerarch(json, SearchWord,
								SearchWordEnglish, peopleCode, $page, type,
								check_rule) {
							var type = type;
							var check_rule = check_rule;
							var fieldList = json.fieldList;
							var jsondata = json.data;
							var conmponentHtml = "";
							var status = $('.smartForm').hasClass("search");  //是否处于查看状态
							switch (type) {
							case "1":     //文本信息
								if(typeof(jsondata[SearchWordEnglish]) == "undefined" ){
									jsondata[SearchWordEnglish] = "系统错误";
								}
								conmponentHtml = "<div class='zitem-list input'>"
											    +"<label class='zlabel list-label' data-name='"+SearchWordEnglish+"'>"+SearchWord+"</label>" 
											    +"<span class='colon'>:</span>"
												+"<div class='zinfor-input-wrap'>"
												+"<input type='text' class='zinput  list-input' name='"+SearchWordEnglish+"' value='"+jsondata[SearchWordEnglish]+"'/>"
												+"<span class='list-value'>"+jsondata[SearchWordEnglish]+"</span>"
												+"</div>"
											+"</div>"
								$('.cloneBox', $page).prepend(conmponentHtml);
								break;

							case "3":   //单选
								var selected = "未选择";    //选中项文本
								var selectHtml = "";  //selecthtml
								var optionList = "";  //选项html
								if (typeof (jsondata[SearchWordEnglish]) !== "undefined") {
									selected = jsondata[SearchWordEnglish];
								}
								
								for (var i = 0; i < json.fieldList.length; i++) {
									if( json.fieldList[i]. c_enum_value == selected ){
										optionList += "<option selected='selected' value='"+json.fieldList[i].c_enum_value+"'>"+json.fieldList[i].c_enum_cn_name+"</option>"
									}else{
										optionList += "<option value='"+json.fieldList[i].c_enum_value+"'>"+json.fieldList[i].c_enum_cn_name+"</option>"	
									}
									
								}
								selectHtml = "<select class='smartForm-select' name='"+SearchWordEnglish+"' data-value='"+selected+"' style='display: none'>"
										                + "<option value=''>-- 请选择 --</option>"
										                + optionList
										     +"</select>"  
								conmponentHtml = "<div class='zitem-list select'>"
												    +"<label class='zlabel list-label'  data-name='"+SearchWordEnglish+"'>"+SearchWord+"</label>" 
												    +"<span class='colon'>:</span>"
													+"<div class='zinfor-input-wrap'>"
													+"<span class='list-value'>"+selected+"</span>"
													+selectHtml
												+"</div>"
												
								$(".cloneBox", $page).prepend(conmponentHtml);
								break;
							case "4": //多选
								var selected = "未选择";    //选中项文本
								var selectHtml = "";  //selecthtml
								var optionList = "";  //选项html
								if (typeof (jsondata[SearchWordEnglish]) !== "undefined") {
									selected = jsondata[SearchWordEnglish];
								}
								
								for (var i = 0; i < json.fieldList.length; i++) {
									if( json.fieldList[i]. c_enum_value == selected ){
										optionList += "<option selected='selected' value='"+json.fieldList[i].c_enum_value+"'>"+json.fieldList[i].c_enum_cn_name+"</option>"
									}else{
										optionList += "<option value='"+json.fieldList[i].c_enum_value+"'>"+json.fieldList[i].c_enum_cn_name+"</option>"	
									}
								}
								selectHtml = "<select multiple class='smartForm-select' name='"+SearchWordEnglish+"' data-value='"+selected+"' style='display: none'>"
										                + "<option value=''>-- 请选择 --</option>"
										                + optionList
										     +"</select>"  
								conmponentHtml = "<div class='zitem-list select'>"
												    +"<label class='zlabel list-label'  data-name='"+SearchWordEnglish+"'>"+SearchWord+"</label>" 
												    +"<span class='colon'>:</span>"
													+"<div class='zinfor-input-wrap'>"
													+"<span class='list-value'>"+selected+"</span>"
													+selectHtml
													+"</div>"
													
												+"</div>"
												
								$(".cloneBox", $page).prepend(conmponentHtml);
								break;
							case "6":
								var dateTime = null;
								if (typeof (jsondata[SearchWordEnglish]) !== "undefined") {
									dataTime = new Date(
											jsondata[SearchWordEnglish])
											.toLocaleString();
								} else {
									dataTime = '';
								}
								
								conmponentHtml = "<div class='zitem-list input'>"
								    +"<label class='zlabel list-label' data-name='"+SearchWordEnglish+"'>"+SearchWord+"</label>" 
								    +"<span class='colon'>:</span>"
									+"<div class='zinfor-input-wrap'>"
									+"<input type='text' id='smartdate' class='data-picker zinput  list-input' name='"+SearchWordEnglish+"' readonly='readonly' css-cursor='text' value='"+dataTime+"'/>"
									+"<span class='list-value'>"+dataTime+"</span>"
									+"</div>"
								+"</div>"
								$('.cloneBox', $page).prepend(conmponentHtml);
								$($('#smartdate', $page)).datepicker(
										{
											container : '#tab_2',
											format : 'yyyy-mm-dd',
											weekStart : 1,
											daysOfWeek : [ '日', '一', '二', '三',
													'四', '五', '六' ],
											monthNames : [ '一月', '二月', '三月',
													'四月', '五月', '六月', '七月',
													'八月', '九月', '十月', '十一月',
													'十二月' ]
										});
								break 
							default:
								alert("请输入正确的字段");
							}
							if (status) {
								searchSmartInit();
							} else {
								editSmartInit();
							}
						}

						
						var alreadyName = [];  //已经查询过的字段数组
						var fieldArray = [];   //最后一次从ES中获取到的数据						
						//查询  如果已经查询过则不再查询						
						$("#smartSubmit", $page)
								.on(
										"click",
										function() {
											var peopleCode = '${people.peopleCode }';  //页面标识（人的标识）
											var keyWord = $('.search-input',		   //搜索框内文本信息
													$page).val();
											var keyArray = fieldArray;					
											var SearchWord = "";
											var SearchWordEnglish = "";		
											var type = "";								//表单类型		
											var check_rule = "";						//验证类型
											for (var i = 0; i < keyArray.length; i++) {
												if (keyWord === keyArray[i].cCnName) {
													SearchWord = keyArray[i].cCnName;
													type = keyArray[i].type;
													SearchWordEnglish = keyArray[i].cCnEnglish;
													check_rule = keyArray[i].check_rule;
												}
											}
											if ($.inArray(SearchWordEnglish,
													alreadyName) !== -1) {
												return;
											}
											
											alreadyName.push(SearchWordEnglish);
											Ajax
													.ajax(
															'admin/peopledata/smart_search',
															{
																peopleCode : peopleCode,
																type : type,
																field : SearchWordEnglish														
															},
															function(json) {
																addSmartSerarch(
																		json,
																		SearchWord,
																		SearchWordEnglish,
																		peopleCode,
																		$page,
																		type,
																		check_rule);
															});
										});
						/* $("#clone").on('click', "[id='check']", function() {
							$('#cloneForm').submit();
							$(".cloneBox").html("");
							alreadyName = [];
						});

						$("#clone").on('click', "[id='remove']", function() {
							alreadyName = [];
							$(".cloneBox").html("");
							alreadyName = [];
						}); */

						/**
						 * 字段查询
						 * *
						 * **/
						var k = null;
						$('.search-input', $page)
								.bind("keyup click",
										function(event) {
											if(event.keyCode == 40 || event.keyCode == 38)return;										
											if (
													 $(this).val() != ""
													&& $(this).val() != null) {
												Ajax
														.ajax(
																'admin/peopledata/titleSearch',
																{
																	txt : $(
																			this)
																			.val()
																},
																function(json) {
																	fieldArray = json.data;   //模糊查询结果存入fileArray数组
																	bigAutocomplete(json.data);
																});

												k = $(this).val();
											}
											if ($(this).val() == ""
													|| $(this).val() == null)
												$('.search-helpbox',$page).html("");  //清空
										});
						
						$('.search-input', $page)
								.keydown(
										function(event) {
											switch (event.keyCode) {
											case 40://向下键
												if ($(".search-helpbox")
														.html() == "")
													return;
												var $nextSiblingLi = $(
														".search-helpbox")
														.find("li.ct");
												if ($nextSiblingLi.length <= 0) {//没有选中行时，选中第一行
													$nextSiblingLi = $(
															".search-helpbox")
															.find("li:first");
												} else {
													$nextSiblingLi = $nextSiblingLi
															.next();
												}
												$(".search-helpbox")
														.find("li")
														.removeClass("ct");
												if ($nextSiblingLi.length > 0) {//有下一行时（不是最后一行）
													$nextSiblingLi
															.addClass("ct");//选中的行加背景
													$(this)
															.val($nextSiblingLi.text());//选中行内容设置到输入框中
													$(this)
															.attr(
																	'id',
																	$nextSiblingLi.attr("id"));
												} else {
													$(this).val(k);//输入框显示用户原始输入的值
													$(this).attr('id', "0");//id为0时，添加无效
												}
												break;
											case 38://向上键
												if ($(".search-helpbox")
														.html() == "")
													return;
												var $previousSiblingLi = $(
														".search-helpbox")
														.find("li.ct");
												if ($previousSiblingLi.length <= 0) {//没有选中行时，选中最后一行行
													$previousSiblingLi = $(
															".search-helpbox")
															.find("li:last");
												} else {
													$previousSiblingLi = $previousSiblingLi
															.prev();
												}
												$(".search-helpbox")
														.find("li")
														.removeClass("ct");
												if ($previousSiblingLi.length > 0) {//有上一行时（不是第一行）
													$previousSiblingLi
															.addClass("ct");//选中的行加背景
													$(this)
															.val(
																	$previousSiblingLi.text());//选中行内容设置到输入框中
													$(this)
															.attr(
																	'id',
																	$previousSiblingLi.attr("id"));
													//div滚动到选中的行,jquery-1.6.1 $$previousSiblingTr.offset().top 有bug，数值有问题
													/* $("#bigAutocompleteContent")
															.scrollTop(
																	$previousSiblingTr[0].offsetTop
																			- $(
																					"#bigAutocompleteContent")
																					.height()
																			+ $previousSiblingTr
																					.height()); */
												} else {
													$(this).val(k);//输入框显示用户原始输入的值
													$(this).attr('id', "0");//id为0时，添加无效
												}

												break;
											case 13://回车键隐藏下拉框,同时查询
												$("#smartSubmit", $page)
														.click();
												$('.search-helpbox').html("");
												$('.search-input',$page).val("");
												break;
											case 27://ESC键隐藏下拉框
												$('.search-helpbox').html("");
												break;
											}
										});
						/* function hideContent() {
							if ($("#bigAutocompleteContent").css("display") != "none") {
								$("#bigAutocompleteContent").find("tr")
										.removeClass("ct");
								$("#bigAutocompleteContent").hide();
							}
						} */
						
						//页面查询补全方法
						var bigAutocomplete = function(data){
							if ((data != null || data != "") && $.isArray(data)){  //数据判断是否有效
								
								if (data == null || data.length <= 0) {
									$('.search-helpbox').html(optionHtml);   //隐藏下拉框
									return;
								}
							
								var optionHtml = "";  //选项下拉框html

								for (var i = 0; i < data.length; i++) {
									optionHtml += "<li id="+data[i].cCnEnglish+">"
											+ data[i].cCnName
											+ "</li>"
								}

								$('.search-helpbox').html(optionHtml);
							}
														
						}
						
						//查询补全部分事件绑定
						$(".search-helpbox",$page).on("click",function() {
							var text = $(this).find('.ct').text();
							$('.search-input',$page).val(text);
							$('.search-input').html("");     //点击后置空补全选项
						});
						$(".search-helpbox",$page).on("mouseover","li",
								function() {
									$(".search-helpbox tr")
											.removeClass("ct");
									$(this).addClass("ct");
								})
						$(".search-helpbox",$page).on("mouseout","li",
								function() {
									$(this).removeClass("ct");
								})
						$page.on('click',function(){
							$('.search-helpbox').html("");
						})
								
/* var bigAutocomplete = new function() {
							this.holdText = null;//输入框中原始输入的内容
							//初始化插入自动补全div，并在document注册mousedown，点击非div区域隐藏div
							this.init = function() {
								$("body")
										.append(
												"<div id='bigAutocompleteContent' class='bigautocomplete-layout'></div>");
								$(document)
										.bind(
												'mousedown',
												function(event) {
													var $target = $(event.target);
													if ((!($target.parents()
															.andSelf()
															.is('#bigAutocompleteContent')))
															&& (!$target
																	.is(sFocus))) {
														hideContent();
													}
												})

								//鼠标悬停时选中当前行
								$("#bigAutocompleteContent").delegate(
										"tr",
										"mouseover",
										function() {
											$("#bigAutocompleteContent tr")
													.removeClass("ct");
											$(this).addClass("ct");
										}).delegate(
										"tr",
										"mouseout",
										function() {
											$("#bigAutocompleteContent tr")
													.removeClass("ct");
										});

								//单击选中行后，选中行内容设置到输入框中，并执行callback函数
								$("#bigAutocompleteContent").delegate(
										"tr",
										"click",
										function() {
											console.log(sFocus);
											sFocus.val($(this).find("div:last")
													.html());
											hideContent();
										});
							}

							this.autocomplete = function(param) {
								console.log(1);
								if ($("body").length > 0
										&& $("#bigAutocompleteContent").length <= 0) {
									bigAutocomplete.init();//初始化信息
								}

								var $this = $(this);//为绑定自动补全功能的输入框jquery对象
								var $bigAutocompleteContent = $("#bigAutocompleteContent");

								this.config = {
									//width:下拉框的宽度，默认使用输入框宽度
									width : $this.outerWidth() - 2,
									//url：格式url:""用来ajax后台获取数据，返回的数据格式为data参数一样
									url : null,
									//data：格式{data:[{title:null,result:{}},{title:null,result:{}}]}
									url和data参数只有一个生效，data优先
									data : null,
									//callback：选中行后按回车或单击时回调的函数
									callback : null
								};
								$.extend(this.config, param);

								$this.data("config", this.config);

								var config = $this.data("config");

								var offset = $this.offset();
								$bigAutocompleteContent.width(config.width);
								var h = $this.outerHeight() - 1;
								$bigAutocompleteContent.css({
									"top" : offset.top + h,
									"left" : offset.left
								});

								var data = config.data;
								var url = config.url;
								var keyword_ = $.trim($this.val());
								if (keyword_ == null || keyword_ == "") {
									hideContent();
									return;
								}

								if ((data != null || data != "")
										&& $.isArray(data)) {
									makeContAndShow(data);
								}

								bigAutocomplete.holdText = $this.val();

								//组装下拉框html内容并显示
								function makeContAndShow(data_) {
									if (data_ == null || data_.length <= 0) {
										hideContent();
										return;
									}
									var cont = "<table><tbody>";

									for (var i = 0; i < data_.length; i++) {
										cont += "<tr><td><div id="+data_[i].cCnEnglish+">"
												+ data_[i].cCnName
												+ "</div></td></tr>"
									}

									cont += "</tbody></table>";
									$bigAutocompleteContent.html(cont);
									$bigAutocompleteContent.show();
									//每行tr绑定数据，返回给回调函数
									$bigAutocompleteContent.find("tr").each(
											function(index) {
												$(this).data("jsonData",
														data_[index]);
											})
								}
							}
						};

						$.fn.bigAutocomplete = bigAutocomplete.autocomplete; */
					});
</script>
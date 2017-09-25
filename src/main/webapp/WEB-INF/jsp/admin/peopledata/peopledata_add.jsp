<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="people-add">
	<div class="page-header">
		<div class="header-title">
			<h1>创建人口</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/peopledata/do_add">
					<div class="form-group">
						<label class="col-lg-1 control-label" for="name">姓名</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="name" />
						</div>
						<label class="col-lg-1 control-label" for="idcode">身份证号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="idcode" id="code"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="gender">性别</label>
						<div class="col-lg-4">
							<input name="gender" id="1" type="radio" value="男性" style="opacity:1;position: static;height:13px;" checked/> 
							<label for="1">男性</label> 
							<input name="gender" id="0" type="radio" value="女性" style="opacity:1;position: static;height:13px;"/> 
							<label for="0">女性</label>
						</div>
						<label class="col-lg-1 control-label" for="birthday">生日</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="date" name="birthday" readonly="readonly" css-cursor="text"  />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="address">居住地址</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="address" />
						</div>
						<label class="col-lg-1 control-label" for="contact">联系号码</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="contact1" id="contact"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label">籍贯</label>
						<div class="col-lg-4">
							<input type="text" name="nativePlace" class="form-control" />
						</div>
						<label class="col-lg-1 control-label" for="householdPlace">户籍地址</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="householdPlace" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label">民族</label>
						<div class="col-lg-4">
							<select name="nation" class="form-control">
								<option value=""> -- 请选择 -- </option>
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
						<label class="col-lg-1 control-label" for="politicalStatus">政治面貌</label>
						<div class="col-lg-4">
							<select class="form-control" name="politicalStatus">
								<option value=""> -- 请选择 -- </option>
								<option value="群众">群众</option>
								<option value="共青团员">共青团员</option>
								<option value="中共预备党员">中共预备党员</option>
								<option value="中共党员">中共党员</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label">婚姻状况</label>
						<div class="col-lg-4">
							<select class="form-control" name="maritalStatus">
								<option value=""> -- 请选择 -- </option>
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
						<label class="col-lg-1 control-label">宗教信仰</label>
						<div class="col-lg-4">
							<select class="form-control" name="religion">
								<option value=""> -- 请选择 -- </option>
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
					<div class="form-group">
						<label class="col-lg-1 control-label">健康状况</label>
						<div class="col-lg-4">
							<select class="form-control" name="healthCondition">
								<option value=""> -- 请选择 -- </option>
								<option value="良好">良好</option>
								<option value="较好">较好</option>
								<option value="一般">一般</option>
								<option value="较差">较差</option>
								<option value="很差">很差</option>
							</select>
						</div>
						<label class="col-lg-1 control-label">人口类型</label>
						<div class="col-lg-4">
							<select class="form-control" name="peopleType">
								<option value=""> -- 请选择 -- </option>
								<option value="户籍人口">户籍人口</option>
								<option value="流动人口">流动人口</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<h4>残疾人信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="handicappedCode">残疾证号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="handicappedCode" />
						</div>
						<label class="col-lg-1 control-label" for="handicappedType">残疾类型</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="handicappedType"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="handicappedLevel">残疾级别</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="handicappedLevel" />
						</div>
						<label class="col-lg-1 control-label" for="handicappedReason">残疾原因</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="handicappedReason"/>
						</div>
					</div>
					
					<div class="form-group">
						<h4>低保信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="lowIncomeInsuredCode">低保证号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="lowIncomeInsuredCode"/>
						</div>
						<label class="col-lg-1 control-label" for="lowIncomeInsuredCode">低保人员类别</label>
						<div class="col-lg-4">
							<select class="form-control" name="lowIncomeInsuredCode">
								<option value=""> -- 请选择 -- </option>
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
					<div class="form-group">
						<label class="col-lg-1 control-label" for="lowIncomeInsuredReason">低保原因</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="lowIncomeInsuredReason"/>
						</div>
						<label class="col-lg-1 control-label" for="lowIncomeInsuredId">享受低保标识</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="lowIncomeInsuredId"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="lowIncomeInsuredAmount">享受低保金额</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="lowIncomeInsuredAmount"/>
						</div>
						<label class="col-lg-1 control-label" for="lowIncomeInsuredStart">享受开始日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="lowIncomeInsuredStart" name="lowIncomeInsuredStart" readonly="readonly" css-cursor="text"  />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="lowIncomeInsuredEnd">享受结束日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="lowIncomeInsuredEnd" name="lowIncomeInsuredEnd" readonly="readonly" css-cursor="text"/>
						</div>
					</div>
					
					<div class="form-group">
						<h4>失业信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="unemployeeDate">就失业日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="unemployeeDate" name="unemployeeDate" readonly="readonly" css-cursor="text"/>
						</div>
						<label class="col-lg-1 control-label" for="unemployeeCode">就失业证号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="unemployeeDate"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="unemployeeStatus">就失业状态</label>
						<div class="col-lg-4">
							<input type="text" class="form-control"name="unemployeeStatus"/>
						</div>
						<label class="col-lg-1 control-label" for="employeeId">就业标识</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="employeeId"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="hardToEmployeeType">就业困难人员类型</label>
						<div class="col-lg-4">
							<select class="form-control" name="hardToEmployeeType">
								<option value=""> -- 请选择 -- </option>
								<option value="双失业">双失业</option>
								<option value="单亲有子女上学">单亲有子女上学</option>
								<option value="低保">低保</option>
								<option value="4045大龄人员">4045大龄人员</option>
								<option value="纯农户">纯农户</option>
								<option value="失地农民">失地农民</option>
								<option value="其它">其它</option>
							</select>
						</div>
						<label class="col-lg-1 control-label" for="employeeType">就业类型</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="employeeType"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="employeeCapacity">就业能力</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="employeeCapacity"/>
						</div>
						<label class="col-lg-1 control-label" for="employeeSituation">就业情况</label>
						<div class="col-lg-4">
							<select class="form-control" name="employeeSituation">
								<option value=""> -- 请选择 -- </option>
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
					<div class="form-group">
						<label class="col-lg-1 control-label" for="employeeDestination">就业去向</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="employeeDestination"/>
						</div>
						<label class="col-lg-1 control-label" for="employeeWay">就业途径</label>
						<div class="col-lg-4">
							<select class="form-control" name="employeeWay">
								<option value=""> -- 请选择 -- </option>
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
					
					<div class="form-group">
						<h4>党员信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="politicalStatus">政治面貌</label>
						<div class="col-lg-4">
							<select class="form-control" name="politicalStatus">
								<option value=""> -- 请选择 --</option>
								<option value="群众">群众</option>
								<option value="共青团员">共青团员</option>
								<option value="中共预备党员">中共预备党员</option>
								<option value="中共党员">中共党员</option>
							</select>
						</div>
						<label class="col-lg-1 control-label" for="partyDate">入党日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="partyDate" name="partyDate" readonly="readonly" css-cursor="text"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="partyPost">党内职务</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="partyPost"/>
						</div>
						<label class="col-lg-1 control-label" for="partyOrganization">所在党组织</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="partyOrganization"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="partySuperior">党组织隶属</label>
						<div class="col-lg-4">
							<select class="form-control" name="partySuperior">
								<option value=""> -- 请选择 -- </option>
								<option value="社区">社区</option>
								<option value="街道">街道</option>
								<option value="本系统内">本系统内</option>
								<option value="无">无</option>
							</select>
						</div>
						<label class="col-lg-1 control-label" for="partyOrgContact">党组织联系电话</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="partyOrgContact" id="partyOrgContact"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="CYOrganization">共青团组织</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="CYOrganization"/>
						</div>
					</div>
					
					<div class="form-group">
						<h4>工作信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="companyName">公司名称</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="companyName"/>
						</div>
						<label class="col-lg-1 control-label" for="workUnit">工作单位</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="workUnit"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="workAddress">工作地址</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="workAddress"/>
						</div>
						<label class="col-lg-1 control-label" for="unitContact">单位电话</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="unitContact" id="unitContact"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="workDepartment">当前部门</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="workDepartment"/>
						</div>
						<label class="col-lg-1 control-label" for="unitNature">单位性质</label>
						<div class="col-lg-4">
							<select class="form-control" name="unitNature">
								<option value=""> -- 请选择 --</option>
								<option value="未填">未填</option>
								<option value="机关">机关</option>
								<option value="社会团体">社会团体</option>
								<option value="事业单位">事业单位</option>
								<option value="企业">企业</option>
								<option value="民办非企业单位">民办非企业单位</option>
								<option value="部队">部队</option>
								<option value="其他">其他</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="salary">工资</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="salary"/>
						</div>
						<label class="col-lg-1 control-label" for="workContent">工作内容</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="workContent" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="workDuty">工作职责</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="workDuty"/>
						</div>
						<label class="col-lg-1 control-label" for="workSubject">工作主题</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="workSubject" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="workedOccupation">曾从事职业</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="workedOccupation"/>
						</div>
					</div>
					
					<div class="form-group">
						<h4>家庭信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="familyAddress">家庭住址</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyAddress"/>
						</div>
						<label class="col-lg-1 control-label" for="familyCount">家庭成员人数</label>
						<div class="col-lg-4">
							<input type="number" class="form-control" name="familyCount"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="familyContact">家庭电话</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyContact" />
						</div>
						<label class="col-lg-1 control-label" for="familyFinancialSituation">家庭经济状况</label>
						<div class="col-lg-4">
							<select class="form-control" name="familyFinancialSituation">
								<option value=""> -- 请选择 -- </option>
								<option value="无固定经济来源">无固定经济来源</option>
								<option value="有固定经济来源">有固定经济来源</option>
								<option value="低收入家庭">低收入家庭</option>
								<option value="贫困">贫困</option>
								<option value="其他">其他</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="familyType">家庭类别</label>
						<div class="col-lg-4">
							<select class="form-control" name="familyType">
								<option value=""> -- 请选择 --</option>
								<option value="低保家庭">低保家庭</option>
								<option value="纯老家庭">纯老家庭</option>
								<option value="低收入家庭">低收入家庭</option>
								<option value="五好文明家庭">五好文明家庭</option>
								<option value="平安家庭">平安家庭</option>
								<option value="文化特色家庭">文化特色家庭</option>
								<option value="节水家庭">节水家庭</option>
								<option value="幸福家庭">幸福家庭</option>
								<option value="和谐家庭">和谐家庭</option>
								<option value="其它">其它</option>
							</select>
						</div>
						<label class="col-lg-1 control-label" for="familyYearIncome">家庭年收入</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyYearIncome"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="familySituation">家庭情况</label>
						<div class="col-lg-4">
							<select class="form-control" name="familySituation">
								<option value=""> -- 请选择 --</option>
								<option value="低收入家庭">低收入家庭</option>
								<option value="单亲家庭">单亲家庭</option>
								<option value="流动家庭">流动家庭</option>
								<option value="抚养人受教育水平相对较低">抚养人受教育水平相对较低</option>
								<option value="与父母关系不融洽">与父母关系不融洽</option>
								<option value="家庭成员的不良行为多">家庭成员的不良行为多</option>
								<option value="其他">其他</option>
							</select>
						</div>
						<label class="col-lg-1 control-label" for="familyAvgMonthIncome">家庭人均月收入</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyAvgMonthIncome"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="familyUnemployeeCount">家庭失业人数</label>
						<div class="col-lg-4">
							<input type="number" class="form-control" name="familyUnemployeeCount"/>
						</div>
					</div>
					
					<div class="form-group">
						<h4>计生信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="childrenCount">子女数</label>
						<div class="col-lg-4">
							<input type="number" class="form-control" name="childrenCount"/>
						</div>
						<label class="col-lg-1 control-label" for="contraceptionMeasure">节育措施</label>
						<div class="col-lg-4">
							<select class="form-control" name="contraceptionMeasure">
								<option value=""> -- 请选择 -- </option>
								<option value="上环">上环</option>
								<option value="取环">取环</option>
								<option value="结扎">结扎</option>
								<option value="无">无</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="pregnancyWeeks">现孕周</label>
						<div class="col-lg-4">
							<input type="number" class="form-control" name="childrenCount"/>
						</div>
						<label class="col-lg-1 control-label" for="familyPlanningCode">计划生育证编号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyPlanningCode"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="familyPlanningType">计划生育证类型</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyPlanningType"/>
						</div>
					</div>
					<div class="form-group">
			        	<div class="col-lg-offset-4 col-lg-3">
			        		<input class="btn btn-block btn-primary" type="submit" value="提交"  />
				        </div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#people-add');
		Utils.datepicker($('#date', $page));
		Utils.datepicker($('#lowIncomeInsuredStart', $page));	//低保享受开始日期
		Utils.datepicker($('#lowIncomeInsuredEnd', $page));	//低保享受结束日期
		Utils.datepicker($('#unemployeeDate', $page));	//就失业日期
		Utils.datepicker($('#partyDate', $page));	//入党日期
		function IdentityCodeValid(code) { 
	        var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
	        var tip = "";
	        var pass= true;
	        
	        if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
	            tip = "身份证号格式错误";
	            pass = false;
	        }
	        
	       else if(!city[code.substr(0,2)]){
	            tip = "地址编码错误";
	            pass = false;
	        }
	        else{
	            //18位身份证需要验证最后一位校验位
	            if(code.length == 18){
	                code = code.split('');
	                //∑(ai×Wi)(mod 11)
	                //加权因子
	                var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
	                //校验位
	                var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
	                var sum = 0;
	                var ai = 0;
	                var wi = 0;
	                for (var i = 0; i < 17; i++)
	                {
	                    ai = code[i];
	                    wi = factor[i];
	                    sum += ai * wi;
	                }
	                var last = parity[sum % 11];
	                if(parity[sum % 11] != code[17]){
	                    tip = "校验位错误";
	                    pass =false;
	                }
	            }
	        }
	        if(!pass)  $("#txt").val(tip);
	        else $("#txt").val("√")
	        return pass;
	    }
		
		$("#code").blur(function(){
			var code = $("#code").val();
			IdentityCodeValid(code);
		});
		
		$("#contact").blur(function(){
			var contact = $("#contact").val();
			/* if(!(/^1[34578]\d{9}$/.test(contact)) ){//手机验证
				alert('联系电话有误，请重填');
				return false;
				} */
			if(!testContact(contact)){
				return false;
			}
		});
		//党员信息--党组织联系电话
		$("#partyOrgContact").blur(function(){
			var contact = $("#partyOrgContact").val();
			if(!testContact(contact)){
				return false;
			}
		});
		
		//工作信息--工作单位电话
		$("#unitContact").blur(function(){
			var contact = $("#unitContact").val();
			if(!testContact(contact)){
				return false;
			}
		});
		
		//联系方式验证
		function testContact(contact){
			if(!(/^1[34578]\d{9}$/.test(contact)) ){ //手机验证
				alert('联系电话有误，请重填');
				return false;
			}
		}
	});
	
	
</script>
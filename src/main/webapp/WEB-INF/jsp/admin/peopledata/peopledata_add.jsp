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
			if(!(/^1[34578]\d{9}$/.test(contact)) ){//手机验证
				alert('联系电话有误，请重填');
				return false;
				}
		});
	});
	
	
</script>
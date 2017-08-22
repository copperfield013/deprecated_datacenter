<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="people-update">
	<div class="page-header">
		<div class="header-title">
			<h1>修改人口</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/people/do_update">
					<input type="hidden" name="id" value="${people.id }" />
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name" >姓名</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="name" value="${people.name }" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="idcode">身份证号</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="idcode" id="code" value="${people.idcode }"/>
						</div>
						<div class="col-lg-3">
							<input id="txt" readonly="readonly" style="border:0px"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="gender">性别</label>
						<div class="col-lg-5">
							<input name="gender" id="1" type="radio" value="1" style="opacity:1;position: static;height:13px;" <c:if test="${people.gender==1 }"> checked </c:if>/> 
							<label for="1">男</label> 
							<input name="gender" id="0" type="radio" value="0" style="opacity:1;position: static;height:13px;" <c:if test="${people.gender==0 }"> checked </c:if>/> 
							<label for="0">女</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="birthday" >生日</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" id="date" name="birthday" readonly="readonly" css-cursor="text"  value="${people.birthday }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="address" >居住地址</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="address" value="${people.address }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="contact">联系号码</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="contact" id="contact" value="${people.contact }"/>
						</div>
					</div>
					<div class="form-group">
			        	<div class="col-lg-offset-3 col-lg-3">
			        		<input class="btn btn-block btn-darkorange" type="submit" value="提交"  />
				        </div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#people-update');
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
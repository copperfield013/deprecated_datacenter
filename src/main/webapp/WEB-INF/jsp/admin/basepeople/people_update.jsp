<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link rel="stylesheet" href="media/admin/bigautocomplete/css/jquery.bigautocomplete.css" type="text/css" />
<script type="text/javascript" src="media/admin/bigautocomplete/js/jquery.bigautocomplete.js"></script>


<div id="people-update">
	<div class="page-header">
		<div class="header-title">
			<h1>修改人口</h1>
		</div>
	</div>

	<!--  这里加入一个搜索框 下面加入个区间选择克隆对象？-->
	<nav>
		<div class="form-inline" >
			<div class="form-group">
				<label for="search">填报字段</label>
				<input type="text" class="form-control search" id="0" name="search"  />
			</div>
			<button type="button" class="btn btn-default" id="smartSubmit">查询</button>
		</div>
	</nav>


  <div id="clone">

  </div>

	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/people/do_update">
					<input type="hidden" name="id" value="${people.id }" />
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name" >姓名</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="name" value="${people.name }"  id="姓名"/>
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

		$("#smartSubmit").click(function(){
		    //simple demo clone one option;
			SearchWord = "姓名";
            $("#"+SearchWord).parent().parent().clone(true).appendTo($("#clone"));
            $("#clone").append("<button class=\"btn btn-labeled btn-palegreen\" id='check'>\n" +
                "<i class=\"btn-label glyphicon glyphicon-ok\"></i>确认</button>" +
				"<button class=\"btn btn-labeled btn-darkorange\" id='remove'>  \n" +
                " <i class=\"btn-label glyphicon glyphicon-remove\"></i>取消</button>");
        });
		$("#clone").on('click',"[id='check']",function(){
			var result =$(this).parent().find(".form-control").val();
			$("input[name='name']").last().val(result);
            $("#clone").html("");
		});

        $("#clone").on('click',"[id='remove']",function(){
   			$("#clone").html("");
        });
        /**
		 * clone function
         */
	});
	
	
	 var k=null;
	 //监听查询内容
	    $(".search").keyup(function(event){
	    	if(event.which>"40"||event.which=="32"||event.which=="8"
	    			&&$(this).val()!=""&&$(this).val()!=null){
	    		$.ajax({  
	    	        url: 'admin/people/titleSearch',
	    	        data: {
	    	        	txt:$('.search').val()
	    	        },
	    	        dataType: "html",  
	    	        type: "POST",  
	    	        //async: false,
	    	        //traditional: true,  
	    	        success: function (data) {  
	    	            // your logic  
	    	            var a=JSON.parse(data);
	    	            $('.search').bigAutocomplete({
	    	        		width:190,
	    	        		data:a
	    	        		
	    	        	});
	    	            
	    	           
	    	        },
	    	        error: function(XMLHttpRequest, textStatus, errorThrown) {
	                    alert(XMLHttpRequest.status+","+XMLHttpRequest.readyState+","+textStatus);
	                }
	    	      
	    	    });
	        	k=$(this).val();
	    	}
	    	if($(this).val()==""||$(this).val()==null)
				hideContent();
	    });
	    
	    $(".search").keydown(function(event) {
			switch (event.keyCode) {
			case 40://向下键
				if($("#bigAutocompleteContent").css("display") == "none")return;
				
				var $nextSiblingTr = $("#bigAutocompleteContent").find(".ct");
				if($nextSiblingTr.length <= 0){//没有选中行时，选中第一行
					$nextSiblingTr = $("#bigAutocompleteContent").find("tr:first");
				}else{
					$nextSiblingTr = $nextSiblingTr.next();
				}
				$("#bigAutocompleteContent").find("tr").removeClass("ct");
				
				if($nextSiblingTr.length > 0){//有下一行时（不是最后一行）
					$nextSiblingTr.addClass("ct");//选中的行加背景
					$(this).val($nextSiblingTr.find("div:last").html());//选中行内容设置到输入框中
					$(this).attr('id',$nextSiblingTr.find("div:last").attr("id"));
					//div滚动到选中的行,jquery-1.6.1 $nextSiblingTr.offset().top 有bug，数值有问题
					$("#bigAutocompleteContent").scrollTop($nextSiblingTr[0].offsetTop - $("#bigAutocompleteContent").height() + $nextSiblingTr.height() );
					
				}else{
					$(this).val(k);//输入框显示用户原始输入的值
					$(this).attr('id',"0");//id为0时，添加无效
				}
				
				
				break;
			case 38://向上键
				if($("#bigAutocompleteContent").css("display") == "none")return;
				
				var $previousSiblingTr = $("#bigAutocompleteContent").find(".ct");
				if($previousSiblingTr.length <= 0){//没有选中行时，选中最后一行行
					$previousSiblingTr = $("#bigAutocompleteContent").find("tr:last");
				}else{
					$previousSiblingTr = $previousSiblingTr.prev();
				}
				$("#bigAutocompleteContent").find("tr").removeClass("ct");
				
				if($previousSiblingTr.length > 0){//有上一行时（不是第一行）
					$previousSiblingTr.addClass("ct");//选中的行加背景
					$(this).val($previousSiblingTr.find("div:last").html());//选中行内容设置到输入框中
					$(this).attr('id',$nextSiblingTr.find("div:last").attr("id"));
					//div滚动到选中的行,jquery-1.6.1 $$previousSiblingTr.offset().top 有bug，数值有问题
					$("#bigAutocompleteContent").scrollTop($previousSiblingTr[0].offsetTop - $("#bigAutocompleteContent").height() + $previousSiblingTr.height());
				}else{
					$(this).val(k);//输入框显示用户原始输入的值
					$(this).attr('id',"0");//id为0时，添加无效
				}
				
				break;
			case 13://回车键隐藏下拉框
				hideContent();
				break;
			case 27://ESC键隐藏下拉框
				hideContent();
				break;
			}
		});		
	   function hideContent(){
		   if($("#bigAutocompleteContent").css("display") != "none"){
				$("#bigAutocompleteContent").find("tr").removeClass("ct");
				$("#bigAutocompleteContent").hide();
			}
	   }
	
	
</script>
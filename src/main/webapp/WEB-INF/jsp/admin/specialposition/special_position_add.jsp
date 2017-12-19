<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link href="media/admin/selectiontest/selectiontest.css" rel="stylesheet" type="text/css" />
<script src="media/admin/selectiontest/selectiontest.js"></script>
<script src="media/admin/addressdata/area_pcc.js"></script>
<style>
	#special-position-add {
		padding: 0 20px;
	}
	#special-position-add .special-position-submit {
		margin-left: 80px;
	}
	.hide {
		display: none;
	}
	.margin-t10 {
		margin-top: 10px;
	}
	.margin-t15 {
		margin-top: 15px;
	}
	.margin-t20 {
		margin-top: 20px;
	}
	.margin-t30 {
		margin-top: 30px;
	}
	.margin-r20 {
		margin-right: 20px;
	}
	.inline-middle {
		display: inline-block;
		vertical-align: middle;
	}
	.label-w4 {
		width: 4em;
		text-align: right;
	}
	.small-input {
		height: 30px;
		width: 400px;
	}
	.special-position-button {
		marign
	}
	.basic-button {
		width: 100px;
	    text-align: center;
	    height: 30px;
	    line-height: 30px;
	    font-size: 14px;
	    border: 1px solid #d9d9d9;
	    color: #656565;
	    display: inline-block;
	    cursor: pointer;
	    background-color: #ffffff;
	}
	.primary-button {
		width: 100px;
	    text-align: center;
	    height: 30px;
	    line-height: 30px;
	    font-size: 14px;
	    border: 1px solid #d9d9d9;
	    display: inline-block;
	    cursor: pointer;
		color: #ffffff;
    	background-color: #126def;
	}
</style>
<div id="special-position-add">
	<h1 class="zpage-title">添加地点</h1>
	<div class="zpage-body">
				<form id="specialPositionAddForm"  action="admin/special_position/doAdd">
					<!-- <div class="form-group">
						<label class="col-lg-2 control-label" for="code">编码</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="id" />
						</div>
					</div> -->
						<div class="margin-t15">
							<span class="margin-r20 inline-middle label-w4 " for="name">名称</span>
							<input type="text" name="name" class="basic-input small-input" 
								data-bv-notempty="true"
								data-bv-notempty-message="名称不能为空"/>
						</div>
						
						<div class="margin-t15">
							<span class="margin-r20 inline-middle label-w4" for="commonName">通用名称</span>
							<input type="text" name="commonName" class="basic-input small-input"/>
						</div>
						
						<div class="margin-t15">
							<span class="margin-r20 inline-middle label-w4">行政区域级别</span>
							<select class="choose-position">   <!-- 不加name属性，不提交 -->
								<option>省级</option>
								<option>市级</option>
								<option>区级</option>
								<option>街道</option>
							</select>							
						</div>
						<div class="margin-t15">
							<span class="margin-r20 inline-middle label-w4"> 所属行政区域</span>
								<select class="choose-position-province">
									<option id="choosePPro" value="-1">省级</option>
								</select>
								<select class="choose-position-city">
									<option id="choosePCity" value="-1">市级</option>
								</select>
								<select class="choose-position-county">
									<option id="choosePCou" value="-1">区/县级</option>
								</select>
								<select class="choose-position-street">
									<option id="choosePStr" value="-1">街道级</option>
								</select>
							</div>
						<!-- <label class="col-lg-2 control-label" for="belongPosition">所属行政区域</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="belongPosition" />
						</div> -->                         
						
						<div class="margin-t15">
							<span  for="level" class="margin-r20 inline-middle label-w4">选择级别</span>
							<select id="level" name="level">
								<c:forEach items="${levelNameMap }" var="levelName">
									<option value="${levelName.key }">${levelName.value }</option>
								</c:forEach>
							</select>
						</div>
			        	
			        	<!-- <input class="btn btn-block btn-darkorange" type="submit" value="提交" /> -->
			        	<input type="hidden"  name="belongPosition" />
			        	<div class=" margin-t30">
			        		<span class="special-position-submit primary-button">提交</span>
			        	</div>

				</form>			
	</div>
</div>
<script>
	$(function(){
		var $page = $('#special-position-add');
		
		//街道数据对象
	    var streetJson = {
	        codeFirst: [],
	        codeSecond: [],
	        codeThird: [],
	        codeFourth: [],
	        codeFifth: [],
	        codesixth: [],
	    }
		
	    function setStreetValue(data, index) {
	        streetJson[index] = data;
	    }

	    function getStreetAjax(url, index) {
	        var index = index;
	        $.ajax({
	            url: url,
	            type: "GET",
	            success: function (data) {
	                data = JSON.parse(data);
	                setStreetValue(data, index);
	            },
	            error: function (XMLHttpRequest, textStatus, errorThrown) {
	                console.error(textStatus);
	                alert("服务器出错!");
	            }
	        })
	    }
		
	   //省 初始化
	    var sb = "";

	    for (var i = 0; i < 31; i++) {   //有31个省份 
	        var val = cityJson[i];
	        if (val.c.substr(2, 7) == '0000000') {
	            sb += ("<option value='" + val.c + "'>" + val.n + "</option>");
	        }
	    }
	    $("#choosePPro",$page).after(sb);
	    
	    // 省值变化时 处理市
	    function doProvAndCityRelation() {
	        var city = $(".choose-position-city",$page);
	        var county = $(".choose-position-county",$page);
	        var street = $(".choose-position-street",$page);
	        if (city.children().length > 1) {
	            city.empty();
	        }
	        if (county.children().length > 1) {
	            county.empty();
	        }
	        if (street.children().length > 1) {
	            street.empty();
	        }
	        if ($("#choosePCity",$page).length == 0) {
	            city.append("<option id='choosePCity' value='-1'>市级</option>");
	        }
	        if ($("#choosePCounty",$page).length == 0) {
	            county.append("<option id='choosePCou' value='-1'>区/县级</option>");
	        }
	        if ($("#choosePStreet",$page).length == 0) {
	            street.append("<option id='choosePStr' value='-1'>街道</option>");
	        }
	        var sb = "";
	        
	        for (var i = 31; i < cityJson.length; i++) {
	            var val = cityJson[i];
	            if (val.c.substr(0, 2) == $(".choose-position-province",$page).val().substr(0, 2) && val.c.substr(2, 7) != '0000000' && val.c.substr(4, 5) == '00000') {
	            	sb += ("<option value='" + val.c + "'>" + val.n + "</option>");
	            }
	        }
	        $("#choosePCity",$page).after(sb);
	    }
	    
	 	// 市值变化时 处理区/县  异步加载相应的街道数据用变量存储,如果变量有值则不加载
	    function doCityAndCountyRelation() {
	        var cityVal = $(".choose-position-city",$page).val();
	        var county = $(".choose-position-county",$page);
	        var street = $(".choose-position-street",$page);
	        var codeKey = parseFloat(cityVal.substr(0, 1));   //根据市code 的第一位数据 加载相应的街道数据文件
	        if (county.children().length > 1) {
	            county.empty();
	        }
	        if (street.children().length > 1) {
	            street.empty();
	        }
	        if ($("#choosePCou",$page).length == 0) {
	            county.append("<option id='choosePCou' value='-1'>区/县级</option>");
	        }
	        if ($("#choosePStreet",$page).length == 0) {
	            street.append("<option id='choosePStr' value='-1'>街道</option>");
	        }
	        var sb = "";
	        for (var i = 0; i < cityJson.length; i++) {
	            var val = cityJson[i];
	            if (val.c.substr(0, 4) == cityVal.substr(0, 4) && val.c.substr(4, 5) != '00000' && val.c.substr(6, 3) == '000') {
	                sb += ("<option value='" + val.c + "'>" + val.n + "</option>");

	            }
	        }
	        $("#choosePCou",$page).after(sb);
	        
	        	switch (codeKey) {
	            case 1:
	                if (streetJson.codeFirst.length === 0) {
	                    getStreetAjax("media/admin/addressdata/code_1.js", "codeFirst");
	                }
	                break;
	            case 2:
	                if (streetJson.codeSecond.length === 0) {
	                    getStreetAjax("media/admin/addressdata/code_2.js", "codeSecond");
	                }
	                break;
	            case 3:
	                if (streetJson.codeThird.length === 0) {
	                    getStreetAjax("media/admin/addressdata/code_3.js", "codeThird");
	                }
	                break;
	            case 4:
	                if (streetJson.codeFourth.length === 0) {
	                    getStreetAjax("media/admin/addressdata/code_4.js", "codeFourth");
	                }
	                break;
	            case 5:
	                if (streetJson.codeFifth.length === 0) {
	                    getStreetAjax("media/admin/addressdata/code_5.js", "codeFifth");
	                }
	                break;
	            case 6:
	                if (streetJson.codesixth.length === 0) {
	                    getStreetAjax("media/admin/addressdata/code_6.js", "codesixth");
	                }
	                break;
	            default:
	                break;
	        	}
	        
	    }
	 	
		 // 区/县变化时，处理街道级
	    function doCountyAndStreetRelation() {
	        var CountyVal = $(".choose-position-county",$page).val();
	        var street = $(".choose-position-street",$page);
	        var codeKey = parseFloat(CountyVal.substr(0, 1));
	        var usefulJson = [];
	        var count = 0;
	        if (street.children().length > 1) {
	            street.empty();
	        }
	        if ($("#choosePStr",$page).length == 0) {
	            street.append("<option id='choosePStr' value='-1'>请选择您所在街道</option>");
	        }

	        switch (codeKey) {
	            case 1:
	                usefulJson = streetJson.codeFirst
	                break;
	            case 2:
	                usefulJson = streetJson.codeSecond
	                break;
	            case 3:
	                usefulJson = streetJson.codeThird
	                break;
	            case 4:
	                usefulJson = streetJson.codeFourth
	                break;
	            case 5:
	                usefulJson = streetJson.codeFifth
	                break;
	            case 6:
	                usefulJson = streetJson.codesixth
	                break;
	            default:
	                break;
	        }
	        var sb = "";
	        if (usefulJson.length === 0) {
	            if( $('.choose-position-province',$page).val() == -1 || $('.choose-position-city',$page).val() == -1 || $('.choose-position-street',$page).val() == -1 ){
	                return;
	            }else{
	                alert('网络延迟严重，请重新选择区选项');
	            }
	        }
	        for (var i = 0; i < usefulJson.length; i++) {
	            var val = usefulJson[i];
	            if (val.c.substr(0, 6) == CountyVal.substr(0, 6) && val.c.substr(6, 3) != '000') {
	                sb += ("<option value='" + val.c + "'>" + val.n + "</option>");
	            }
	        }
	        $("#choosePStr",$page).after(sb);
	    }
	    
	    
		//下拉框初始化
		var choosePosition = $('.choose-position',$page).Selection({
	        width: "140px",
	        right: "10px",	
	        clickCallBack: function () {
	        	var pVal = $('.choose-position',$page).val();
	        	switch (pVal) {
	            case "省级":
	            	chooseCity.hidden();
	        		chooseCounty.hidden();
	        		chooseStreet.hidden();
	                break;
	            case "市级":
	            	chooseCity.show();
	        		chooseCounty.hidden();
	        		chooseStreet.hidden();
	                break;
	            case "区级":
	            	chooseCity.show();
	        		chooseCounty.show();
	        		chooseStreet.hidden();
	                break;
	            case "街道":
	            	chooseCity.show();
	        		chooseCounty.show();
	        		chooseStreet.show();
	                break;	       
	            default:
	                break;
	        }
	        	
	        }
	    });
		var chooseProvince = $('.choose-position-province',$page).Selection({
	        width: "140px",
	        right: "10px",	
	        clickCallBack: function () {       //选项点击回调函数    	        	
	        	doProvAndCityRelation();
	            chooseCity.reload();
	            chooseCounty.reload();
	            chooseStreet.reload(); 
	        },
	    });
				
		var chooseCity = $('.choose-position-city',$page).Selection({
	        width: "140px",
	        right: "10px",	
	        clickCallBack: function () {       //选项点击回调函数    	        	
	        	doCityAndCountyRelation()
                chooseCounty.reload();
        		chooseStreet.reload();
	        },
	    });
		var chooseCounty = $('.choose-position-county',$page).Selection({
	        width: "140px",
	        right: "10px",	
	        clickCallBack: function () {       //选项点击回调函数    	        	
	        	doCountyAndStreetRelation()
        		chooseStreet.reload();
	        },
	    });
		var chooseStreet = $('.choose-position-street',$page).Selection({
	        width: "140px",
	        right: "10px",	        
	    });
		var chooseLevel= $('#level',$page).Selection({
	        width: "140px",
	        right: "10px",	        
	    });
		
		//市区街道首先隐藏
		chooseCity.hidden();
		chooseCounty.hidden();
		chooseStreet.hidden();
		
		//提交		
		$(".special-position-submit",$page).on("click",function(){
			console.log($('.choose-position',$page).val());
			var pVal = $('.choose-position',$page).val(),
				admDivision;
			switch (pVal) {
	            case "省级":
	            	admDivision = $('.choose-position-province',$page).val();
	                break;
	            case "市级":
	            	admDivision = $('.choose-position-city',$page).val();
	                break;
	            case "区级":
	            	admDivision = $('.choose-position-county',$page).val();
	                break;
	            case "街道":
	            	admDivision = $('.choose-position-street',$page).val();
	                break;	       
	            default:
	                break;
	        }
			admDivision = admDivision + "000";
			$('input[name="belongPosition"]').val(admDivision);		
			$('#specialPositionAddForm').submit();
		})
	})
</script>
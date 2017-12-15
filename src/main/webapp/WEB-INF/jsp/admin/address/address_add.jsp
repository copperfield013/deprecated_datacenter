<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link href="media/admin/selectiontest/selectiontest.css" rel="stylesheet" type="text/css" />
<script src="media/admin/selectiontest/selectiontest.js"></script>
<script src="media/admin/addressdata/area_pcc.js"></script>
<style>
	#address-add {
		padding-left: 20px;
	}
	#address-add .address-add-label {
		margin-top: 10px;
	}
	#address-add .address-add-input {
		background-color: #ffffff;
		height: 36px;
		padding-left: 1em;
		border: 1px solid #d9d9d9;
		width: 375px;
	}
	#address-add .submit-btn {
		display: block;
		width: 80px;
		text-align:center;
		height: 30px;
		line-height:30px;
		background-color: #126def;
		margin: 20px 0;
		color: #ffffff;
		cursor: pointer;
	}
	#address-add .submit-btn:hover {
		background-color: #4b90f3;
	}
</style>
  <div id="address-add">
        <h1 class="zpage-title">添加地址</h1>
        <div class="address-add-body">
            <form id="address-add-form" action="admin/address/doAdd" autocomplete="off">
            	<input type="hidden" id="name"name="name"/>
                <p class="address-add-label">请选择行政区域</p>
                <select id="province" name="province">
                	<option id="choosePro" value="-1">请选择您所在省份</option>
                </select>
                <select id="citys" name="city">
                	<option id='chooseCity' value='-1'>请选择您所在城市</option>
                </select>
                <select id="county" name="county">
                	<option id='chooseCounty' value='-1'>请选择您所在区/县</option>
                </select>
                <select id="street" name="street">
                	<option id='chooseStreet' value='-1'>请选择您所在街道</option>
                </select>
                <p class="address-add-label">请填写详细地址</p>
                <input type="text" class="address-add-input" id="detailed-address-name" name="detailed-address-name" />
                <span class="submit-btn">提交</span>
            </form>
        </div>
    </div>

<script>


$(function () {
	var $page = $("#address-add");
	
	$('.submit-btn',$page).on("click",function(){
		var name = '';
		var province = $("#province", $page).find("option:selected").text();
		var citys = $("#citys", $page).find("option:selected").text();
		var county = $("#county", $page).find("option:selected").text();
		var street = $("#street", $page).find("option:selected").text();
		var detialedAddressName = $("#detailed-address-name", $page).val();
		$("#name", $page).val(province + citys + county + street + detialedAddressName);
		$('#address-add-form').submit();
	})
    //街道数据对象
    var streetJson = {
        codeFirst: [],
        codeSecond: [],
        codeThird: [],
        codeFourth: [],
        codeFifth: [],
        codesixth: [],
    }

    //省 初始化
    var sb = "";

    for (var i = 0; i < 31; i++) {   //有31个省份 
        var val = cityJson[i];
        if (val.c.substr(2, 7) == '0000000') {
            sb += ("<option value='" + val.c + "'>" + val.n + "</option>");
        }
    }
    $("#choosePro",$page).after(sb);

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

    // 省值变化时 处理市
    function doProvAndCityRelation() {
        var city = $("#citys",$page);
        var county = $("#county",$page);
        var street = $("#street",$page);
        if (city.children().length > 1) {
            city.empty();
        }
        if (county.children().length > 1) {
            county.empty();
        }
        if (street.children().length > 1) {
            street.empty();
        }
        if ($("#chooseCity",$page).length == 0) {
            city.append("<option id='chooseCity' value='-1'>请选择您所在城市</option>");
        }
        if ($("#chooseCounty",$page).length == 0) {
            county.append("<option id='chooseCounty' value='-1'>请选择您所在区/县</option>");
        }
        if ($("#chooseStreet",$page).length == 0) {
            street.append("<option id='chooseStreet' value='-1'>请选择您所在街道</option>");
        }
        var sb = "";
        for (var i = 31; i < cityJson.length; i++) {
            var val = cityJson[i];
            if (val.c.substr(0, 2) == $("#province",$page).val().substr(0, 2) && val.c.substr(2, 7) != '0000000' && val.c.substr(4, 5) == '00000') {
                sb += ("<option value='" + val.c + "'>" + val.n + "</option>");
            }
        }
        $("#chooseCity",$page).after(sb);
    }

    // 市值变化时 处理区/县  同时异步加载相应的街道数据用变量存储,如果变量有值则不加载
    function doCityAndCountyRelation() {
        var cityVal = $("#citys",$page).val();
        var county = $("#county",$page);
        var street = $("#street",$page);
        var codeKey = parseFloat(cityVal.substr(0, 1));   //根据市code 的第一位数据 加载相应的街道数据文件
        if (county.children().length > 1) {
            county.empty();
        }
        if (street.children().length > 1) {
            street.empty();
        }
        if ($("#chooseCounty",$page).length == 0) {
            county.append("<option id='chooseCounty' value='-1'>请选择您所在区/县</option>");
        }
        if ($("#chooseStreet",$page).length == 0) {
            street.append("<option id='chooseStreet' value='-1'>请选择您所在街道</option>");
        }
        var sb = "";
        for (var i = 0; i < cityJson.length; i++) {
            var val = cityJson[i];
            if (val.c.substr(0, 4) == cityVal.substr(0, 4) && val.c.substr(4, 5) != '00000' && val.c.substr(6, 3) == '000') {
                sb += ("<option value='" + val.c + "'>" + val.n + "</option>");

            }
        }
        $("#chooseCounty",$page).after(sb);
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
        var CountyVal = $("#county",$page).val();
        var street = $("#street",$page);
        var codeKey = parseFloat(CountyVal.substr(0, 1));
        var usefulJson = [];
        var count = 0;
        if (street.children().length > 1) {
            street.empty();
        }
        if ($("#chooseStreet",$page).length == 0) {
            street.append("<option id='chooseStreet' value='-1'>请选择您所在街道</option>");
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
            if( $('#province',$page).val() == -1 || $('#citys',$page).val() == -1 || $('#street',$page).val() == -1 ){
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
        $("#chooseStreet",$page).after(sb);
    }

    var Province = $('#province',$page).Selection({
        width: "180px",
        right: "10px",
        clickCallBack: function () {       //选项点击回调函数            
            doProvAndCityRelation();
            Citys.reload();
            County.reload();
            Street.reload();
        },
    });
    var Citys = $('#citys',$page).Selection({
        width: "180px",
        right: "10px",
        clickCallBack: function () {       //选项点击回调函数            
            doCityAndCountyRelation()
            County.reload();
            Street.reload();
        },
    });
    var County = $('#county',$page).Selection({
        width: "180px",
        right: "10px",
        clickCallBack: function () {       //选项点击回调函数            
            doCountyAndStreetRelation()
            Street.reload();
        },
    });
    var Street = $('#street',$page).Selection({
        width: "180px",
        right: "10px",
    });
});



</script>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp" %>
<link rel="stylesheet" href="media/admin/bigautocomplete/css/jquery.bigautocomplete.css" type="text/css"/>
<link rel="stylesheet" href="media/admin/peopleupdate/swinput.css" type="text/css"/>
<link rel="stylesheet" href="media/admin/peopleupdate/cover.css" type="text/css"/>
<link rel="stylesheet" href="media/admin/peopleupdate/selection.css" type="text/css"/>
<script src="media/admin/peopleupdate/swinput.js"></script>
<script src="media/admin/peopleupdate/selection.js"></script>
<style>
    .integratedForm-button {
        border-top: none;
    }

    .zpage-body {
        top: 0;
    }
    div.tab-content>.tab-pane {
    	background-color: #ffffff;
    }
    .zpage-menubody {
    	position: static;
    }
    .zpage-body {
    	position: static;
    }
    .integratedForm-button {
    	position: static;
    }
    .menu-wrap {
    	position: static;
    }
</style>
<div id="people-update${peopleMap["peopleCode"]}">
    <div class="page-header">
        <div class="header-title">
            <h1>动态字段修改人口</h1>
        </div>
    </div>

    <div class="zpage-body zclear search">
        <div class="integratedForm-button zclear">
            <span id="editIntegratedForm">编辑</span>
            <span id="saveIntegratedForm">保存</span>
        </div>
        <div class="menu-wrap zclear">
            <div class="zpage-menubody">
                <form id="integratedForm" class="search"
                      action="admin/peopledata/do_update" autocomplete="off">
                    <input type="hidden" name="peopleCode"
                           value=" ${peopleMap["peopleCode"]}">
                    <div class="zitem-group zclear" data-group="basic">
                        <c:forEach items="${dic }" var="item" varStatus="i">
                            <c:set var="keys" value="${item.cCnEnglish}"></c:set>
                        <c:choose>
                        <c:when test="${item.type <3}">
                        <div class="zitem-list">
                            <label class="zlabel list-label" for=" ${keys}"> ${item.cCnName}</label>
                            <span class="colon">:</span>
                            <div class="zinfor-input-wrap">
                                <input type="text" class="zinput  list-input" name="${item.cCnEnglish}"
                                       value="${peopleMap[keys]}"/>
                            </div>
                        </div>
                        </c:when>
                        <c:when test="${item.type >2 and item.type<5}">
                            <c:set var="enum" value="${item.cId}"></c:set>
                        <div class="zitem-list">
                            <label class="zlabel list-label">${item.cCnName}</label>
                            <span class="colon">:</span>
                            <div class="zinfor-input-wrap">
                                <select class="select-replace" name="${item.cCnEnglish}"
                                        data-value="${peopleMap[keys]}">
                                    <option value="">-- 请选择 --</option>
                                    <c:forEach items="${itemList }" var="item1" varStatus="j">
                                        <c:if test="${item.cId==item1.cDictionaryId}">
                                            <option value="${item1.cEnumValue }">${item1.cEnumCnName}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        </c:when>
                            <c:when test="${item.type >5}">
                            <div class="zitem-list">
                                <label class="zlabel list-label" for="${item.cCnEnglish}">${item.cCnName}</label>
                                <span class="colon">:</span>
                                <div class="zinfor-input-wrap">
                                    <input type="text" class="zinput  list-input" id="date"
                                           name="${item.cCnEnglish}" readonly="readonly" css-cursor="text"
                                           value='<fmt:formatDate value="${peopleMap[keys]}" pattern="yyyy-MM-dd" />' />
                                </div>
                            </div>
                        </c:when>
                        </c:choose>

                            <%--${keys}--%>
                            <%--${peopleMap[keys]}--%>
                            <%--${item.type}--%>
                            <%--${item.check_rule}--%>
                        </c:forEach>

                </form>
            </div>
        </div>
    </div>

    <script>
        seajs
            .use(
                ['utils', 'ajax'],
                function (Utils, Ajax) {
                    var $page = $('#people-update${peopleMap["peopleCode"]}');
                    console.log($page);
                    console.log( $('#editIntegratedForm', $page) );
                    Utils.datepicker($('#date', $page));
                    Utils.datepicker($('#lowIncomeInsuredStart', $page));
                    Utils.datepicker($('#lowIncomeInsuredEnd', $page));
                    Utils.datepicker($('#unemployeeDate', $page));
                    Utils.datepicker($('#partyDate', $page));
                    //完整表单处 select 替换 对应的select选中值加上selected属性值,同时加上展示html
                    var selectList = $('.zpage-body .select-replace');
                    for (var i = 0; i < selectList.length; i++) {
                        var value = $(selectList[i]).attr('data-value');
                        var optionList = $(selectList[i]).find('option');
                        var name = "";
                        for (var j = 0; j < optionList.length; j++) {
                            if ($(optionList[j]).attr('value') == value) {
                                $(optionList[j]).attr("selected", "selected")
                                name = $(optionList[j]).text();
                            }
                        }
                        var listValueHtml = "<span class='list-value'>" + name + "</span>";
                        $(selectList[i]).closest('.zinfor-input-wrap').append(listValueHtml);
                    }
                    $('.select-replace').Selection({});

                    //完整表单处input加上展示html
                    var inputList = $('.zpage-body .list-input');
                    for (var i = 0; i < inputList.length; i++) {
                        var value = $(inputList[i]).val();
                        var listValueHtml = "<span class='list-value'>" + value + "</span>";
                        $(inputList[i]).closest('.zinfor-input-wrap').append(listValueHtml);
                    }                         

                    //完整表单部分初始化成查看方法
                    var searchInit = function () {
                        var inputList = $('.zpage-body.search', $page).find('input.list-input');
                        var selectList = $('.zpage-body.search', $page).find('select.select-replace');
                        var box = null;
                        var selected = "";
                        var value = "";

                        for (var i = 0; i < inputList.length; i++) {
                            value = $(inputList[i]).val();
                            $(inputList[i]).parent('.zinfor-input-wrap').find('.list-value').text(value);
                        }

                        for (var i = 0; i < selectList.length; i++) {
                            selected = $(selectList[i]).val();
                            //判断是否是数组，数组则是多选
                            if (!$.isArray(value)) {
                                $(selectList[i]).parent('.zinfor-input-wrap').find('.list-value').text(selected);
                            } else {
                                console.log("多选类型");
                            }
                        }
                    }


                    //完整表单部分编辑保存按钮的点击事件
                    $('#editIntegratedForm', $page)
                        .on(
                            'click',
                            function () { 
                            	console.log(1);
                                $('.zpage-body.search', $page)
                                    .removeClass("search")
                                    .addClass("edit");
                            })
                    $('#saveIntegratedForm', $page).on(
                        'click',
                        function () {
                            $('#integratedForm').submit();
                            $('.zpage-body.edit', $page).removeClass(
                                "edit").addClass("search");
                            searchInit();
                        })
                  
                    //身份验证
                    function IdentityCodeValid(code) {
                        var city = {
                            11: "北京",
                            12: "天津",
                            13: "河北",
                            14: "山西",
                            15: "内蒙古",
                            21: "辽宁",
                            22: "吉林",
                            23: "黑龙江 ",
                            31: "上海",
                            32: "江苏",
                            33: "浙江",
                            34: "安徽",
                            35: "福建",
                            36: "江西",
                            37: "山东",
                            41: "河南",
                            42: "湖北 ",
                            43: "湖南",
                            44: "广东",
                            45: "广西",
                            46: "海南",
                            50: "重庆",
                            51: "四川",
                            52: "贵州",
                            53: "云南",
                            54: "西藏 ",
                            61: "陕西",
                            62: "甘肃",
                            63: "青海",
                            64: "宁夏",
                            65: "新疆",
                            71: "台湾",
                            81: "香港",
                            82: "澳门",
                            91: "国外 "
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
                                var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6,
                                    3, 7, 9, 10, 5, 8, 4, 2];
                                //校验位
                                var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4,
                                    3, 2];
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

                    $("#code").blur(function () {
                        var code = $("#code").val();
                        IdentityCodeValid(code);
                    });


                    //手机验证
                    $("#contact").blur(function () {
                        var contact = $("#contact").val();
                        if (!(/^1[34578]\d{9}$/.test(contact))) {//手机验证
                            alert('联系电话有误，请重填');
                            return false;
                        }
                    });
                   
                });
    </script>
</div>
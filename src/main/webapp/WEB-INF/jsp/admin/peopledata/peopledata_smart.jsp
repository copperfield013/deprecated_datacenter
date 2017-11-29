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
        top: 121px;
    }
</style>
<div id="people-update">
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
                    var $page = $('#peopledata-update${people.peopleCode }');
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


                    var dragline = $('.dragline')[0]; //拖动的线
                    var tObj = $('.search-part')[0];  //上半部分界面div
                    var bObj = $('.zpage-body')[0];   //下半部分界面div

                    //定义drag(拖动底线改变div高度) 方法
                    var dragFn = function (dragline, tObj, bObj) {
                        dragline.onmousedown = function (ev) {
                            var oEv = ev || event;
                            //记录下原始数据
                            var oldY = oEv.clientY;
                            var oldHeight = tObj.offsetHeight;
                            var limitTop = 100;
                            var limitBottom = document.body.clientHeight - 100;
                            document.onmousemove = function (ev) {
                                var oEv = ev || event;
                                if (oEv.clientY > limitTop
                                    && oEv.clientY < limitBottom) {
                                    tObj.style.height = oldHeight
                                        + (oEv.clientY - oldY) + 'px';
                                    bObj.style.top = oldHeight
                                        + (oEv.clientY - oldY) + 56 + 'px';  //56为标题部分
                                }
                            };

                            document.onmouseup = function () {
                                document.onmousemove = null;
                            };
                            //阻止事件冒泡
                            oEv.cancelBubble = true;
                            return false;
                        };
                    }
                    //调用拖动方法
//                    dragFn(dragline, tObj, bObj);

                    //完整表单部分右侧菜单栏点击
                    $('.zpage-menuview').on('click', 'li', function (ev) {
                        var oEv = ev || event;
                        var target = oEv.target;
                        var cid = $(target).attr('data-group');
                        var groupItem = $('.zitem-group[data-group=' + cid + ']')[0];
                        var scrollD = groupItem.offsetTop - 5;
                        console.log(scrollD);
                        $(target).addClass('active').siblings('li').removeClass('active');
                        $('.zpage-menubody').scrollTop(scrollD);
                    })

                    //查询表单部分初始化成查看 方法
                    var searchSmartInit = function () {
                        var inputList = $('.smartForm.search .cloneBox', $page).find('.zitem-list.input');
                        var selectList = $('.smartForm.search .cloneBox', $page).find('.zitem-list.select');
                        var value = "";
                        var selected = null;
                        for (var i = 0; i < inputList.length; i++) {
                            value = $(inputList[i]).find('.zinput.list-input').val();
                            $(inputList[i]).find('.list-value').text(value);
                        }
                        for (var i = 0; i < selectList.length; i++) {
                            selected = $(selectList[i]).find('select').val();
                            //判断是否是数组，数组则是多选
                            if (!$.isArray(value)) {
                                $(selectList[i]).find('.list-value').text(selected);
                            } else {
                                console.log("多选类型");
                            }
                        }

                    }

                    //查询表单部分初始化成 编辑 方法
                    var editSmartInit = function () {
                        var inputList = $('.smartForm.edit .cloneBox', $page).find('.zitem-list.input');
                        var selectList = $('.smartForm.edit .cloneBox', $page).find('.zitem-list.select');
                        for (var i = 0; i < selectList.length; i++) {
                            if ($(selectList[i]).find('.selection-container').length === 0) { // select没有被初始化,则初始化
                                $(selectList[i]).find('.smartForm-select').Selection();
                            }
                        }

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
                        function () {
                            $('#integratedForm').submit();
                            $('.zpage-body.edit', $page).removeClass(
                                "edit").addClass("search");
                            searchInit();
                        })

                    //smartForm部分编辑保存按钮的点击事件
                    $('#editSmartForm', $page)
                        .on(
                            'click',
                            function () {
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
                        function () {
                            $('#cloneForm').submit();
                            $('.smartForm.edit', $page).removeClass(
                                "edit").addClass("search");
                            searchSmartInit();
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
                                if (typeof(jsondata[SearchWordEnglish]) == "undefined") {
                                    jsondata[SearchWordEnglish] = "系统错误";
                                }
                                conmponentHtml = "<div class='zitem-list input'>"
                                    + "<label class='zlabel list-label' data-name='" + SearchWordEnglish + "'>" + SearchWord + "</label>"
                                    + "<span class='colon'>:</span>"
                                    + "<div class='zinfor-input-wrap'>"
                                    + "<input type='text' class='zinput  list-input' name='" + SearchWordEnglish + "' value='" + jsondata[SearchWordEnglish] + "'/>"
                                    + "<span class='list-value'>" + jsondata[SearchWordEnglish] + "</span>"
                                    + "</div>"
                                    + "</div>"
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
                                    if (json.fieldList[i].c_enum_value == selected) {
                                        optionList += "<option selected='selected' value='" + json.fieldList[i].c_enum_value + "'>" + json.fieldList[i].c_enum_cn_name + "</option>"
                                    } else {
                                        optionList += "<option value='" + json.fieldList[i].c_enum_value + "'>" + json.fieldList[i].c_enum_cn_name + "</option>"
                                    }

                                }
                                selectHtml = "<select class='smartForm-select' name='" + SearchWordEnglish + "' data-value='" + selected + "' style='display: none'>"
                                    + "<option value=''>-- 请选择 --</option>"
                                    + optionList
                                    + "</select>"
                                conmponentHtml = "<div class='zitem-list select'>"
                                    + "<label class='zlabel list-label'  data-name='" + SearchWordEnglish + "'>" + SearchWord + "</label>"
                                    + "<span class='colon'>:</span>"
                                    + "<div class='zinfor-input-wrap'>"
                                    + "<span class='list-value'>" + selected + "</span>"
                                    + selectHtml
                                    + "</div>"

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
                                    if (json.fieldList[i].c_enum_value == selected) {
                                        optionList += "<option selected='selected' value='" + json.fieldList[i].c_enum_value + "'>" + json.fieldList[i].c_enum_cn_name + "</option>"
                                    } else {
                                        optionList += "<option value='" + json.fieldList[i].c_enum_value + "'>" + json.fieldList[i].c_enum_cn_name + "</option>"
                                    }
                                }
                                selectHtml = "<select multiple class='smartForm-select' name='" + SearchWordEnglish + "' data-value='" + selected + "' style='display: none'>"
                                    + "<option value=''>-- 请选择 --</option>"
                                    + optionList
                                    + "</select>"
                                conmponentHtml = "<div class='zitem-list select'>"
                                    + "<label class='zlabel list-label'  data-name='" + SearchWordEnglish + "'>" + SearchWord + "</label>"
                                    + "<span class='colon'>:</span>"
                                    + "<div class='zinfor-input-wrap'>"
                                    + "<span class='list-value'>" + selected + "</span>"
                                    + selectHtml
                                    + "</div>"

                                    + "</div>"

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
                                    + "<label class='zlabel list-label' data-name='" + SearchWordEnglish + "'>" + SearchWord + "</label>"
                                    + "<span class='colon'>:</span>"
                                    + "<div class='zinfor-input-wrap'>"
                                    + "<input type='text' id='smartdate' class='data-picker zinput  list-input' name='" + SearchWordEnglish + "' readonly='readonly' css-cursor='text' value='" + dataTime + "'/>"
                                    + "<span class='list-value'>" + dataTime + "</span>"
                                    + "</div>"
                                    + "</div>"
                                $('.cloneBox', $page).prepend(conmponentHtml);
                                $($('#smartdate', $page)).datepicker(
                                    {
                                        container: '#tab_2',
                                        format: 'yyyy-mm-dd',
                                        weekStart: 1,
                                        daysOfWeek: ['日', '一', '二', '三',
                                            '四', '五', '六'],
                                        monthNames: ['一月', '二月', '三月',
                                            '四月', '五月', '六月', '七月',
                                            '八月', '九月', '十月', '十一月',
                                            '十二月']
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
                            function () {
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
                                            peopleCode: peopleCode,
                                            type: type,
                                            field: SearchWordEnglish
                                        },
                                        function (json) {
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
                            function (event) {
                                if (
                                    $(this).val() != ""
                                    && $(this).val() != null) {
                                    Ajax
                                        .ajax(
                                            'admin/peopledata/titleSearch',
                                            {
                                                txt: $(
                                                    this)
                                                    .val()
                                            },
                                            function (json) {
                                                fieldArray = json.data;   //模糊查询结果存入fileArray数组
                                                bigAutocomplete(json.data);
                                            });

                                    k = $(this).val();
                                }
                                if ($(this).val() == ""
                                    || $(this).val() == null)
                                    $('.search-helpbox', $page).html("");  //清空
                            });

                    $('.search-input', $page)
                        .keydown(
                            function (event) {
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
                                                    $nextSiblingLi.attr("id"));
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
                                        $('.search-input', $page).val("");
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
                    var bigAutocomplete = function (data) {
                        if ((data != null || data != "") && $.isArray(data)) {  //数据判断是否有效

                            if (data == null || data.length <= 0) {
                                $('.search-helpbox').html(optionHtml);   //隐藏下拉框
                                return;
                            }

                            var optionHtml = "";  //选项下拉框html

                            for (var i = 0; i < data.length; i++) {
                                optionHtml += "<li id=" + data[i].cCnEnglish + ">"
                                    + data[i].cCnName
                                    + "</li>"
                            }

                            $('.search-helpbox').html(optionHtml);
                        }

                    }

                    //查询补全部分事件绑定
                    $(".search-helpbox", $page).on("click", function () {
                        var text = $(this).find('.ct').text();
                        $('.search-input', $page).val(text);
                        $('.search-input').html("");     //点击后置空补全选项
                    });
                    $(".search-helpbox", $page).on("mouseover", "li",
                        function () {
                            $(".search-helpbox tr")
                                .removeClass("ct");
                            $(this).addClass("ct");
                        })
                    $(".search-helpbox", $page).on("mouseout", "li",
                        function () {
                            $(this).removeClass("ct");
                        })
                    $page.on('click', function () {
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
</div>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link rel="stylesheet" href="media/admin/bigautocomplete/css/jquery.bigautocomplete.css" type="text/css" />

<style>
div.tab-content>.tab-pane{
	position:relative;
}
.tab-content {
	background-color: #ffffff;
}
.clone-btn {
	padding-right: 40px;
	padding-bottom: 20px;
	text-align: right;
	display: none;
	border-bottom: 2px solid #999999;

}
.btn.btn-palegreen {
	margin-right: 25px;
}
.cloneBox {
	padding:0 20px;
}
.cloneBox .col-lg-2 .control-label{
	margin-left: 0;
}
.datacenter-gird-box {
	border: 1px solid transparent;
}
.datacenter-menu-box {
	padding: 10px 0;
}
.datacenter-gird-box span,
.datacenter-gird-box label {
	display: block;
	color: #9e9e9e;
	margin-bottom: 15px;
}
</style>
<style>
input:-webkit-autofill,
    textarea:-webkit-autofill,
    select:-webkit-autofill {
        background-color: transparent;
    }

    .datacenter-input {
        width: 350px;
        margin-top: 16px;
        margin-bottom: 15px;
        padding: 0;
        box-sizing: border-box;
        position: relative;
    }
    .datacenter-form-control {
    	width: 215px;
    }
    .datacenter-form-control > label {
    	
    }
    

    .datacenter-input>.basic-slide {
        outline: none;
        border: none;
        width: 100%;
        height: 36px;
        line-height: 36px;
        border-bottom: 1px solid #9e9e9e;
        background-color: transparent;
        font-size: 14px;
        -webkit-box-shadow: none;
        box-shadow: none;
        -webkit-box-sizing: content-box;
        box-sizing: content-box;
        -webkit-transition: all 0.3s;
        transition: all 0.3s;
        margin: 0 0 20px 0;
        padding: 0;
        color: #444444;
        font-family: "Roboto", sans-serif;
    }

    .datacenter-input>textarea.basic-slide {
        padding: 0.8em 0 1.6em 0.5em;
        overflow: auto;
        overflow-y: hidden;
        resize: none;
        min-height: 4.5em;
        line-height: 1.5em;
        box-sizing: content-box;
        font-size: 14px
    }

    .datacenter-input>.basic-slide.vaild,
    .datacenter-input>.basic-slide.vaild:focus {
        border-bottom: 1px solid #4CAF50;
        -webkit-box-shadow: 0 1px 0 0 #4CAF50;
        box-shadow: 0 1px 0 0 #4CAF50;
    }

    .datacenter-input>.basic-slide.invaild,
    .datacenter-input>.basic-slide.invaild:focus {
        border-bottom: 1px solid #F44336;
        -webkit-box-shadow: 0 1px 0 0 #F44336;
        box-shadow: 0 1px 0 0 #F44336;
    }

    .datacenter-input>.basic-slide:focus {
        border-bottom: 1px solid #26a69a;
        -webkit-box-shadow: 0 1px 0 0 #26a69a;
        box-shadow: 0 1px 0 0 #26a69a;
    }

    .datacenter-input>.basic-slide:focus+label {
        color: #26a69a;
    }

    .datacenter-input>label {
        position: absolute;
        left: 0;
        top: 0;
        font-size: 14px;
        color: #9e9e9e;
        height: 100%;
        width: 100%;
        cursor: text;
        -webkit-transform: translateY(12px);
        transform: translateY(12px);
        -webkit-transition: -webkit-transform .2s ease-out;
        transition: -webkit-transform .2s ease-out;
        transition: transform .2s ease-out;
        transition: transform .2s ease-out, -webkit-transform .2s ease-out;
        -webkit-transform-origin: 0% 100%;
        transform-origin: 0% 100%;
        pointer-events: none;
    }

    .datacenter-input>label.active {
        -webkit-transform: translateY(-14px);
        transform: translateY(-14px);
        -webkit-transform-origin: 0 0;
        transform-origin: 0 0;
    }

    .datacenter-input>label.active:after {
        content: "";
        display: none;
        font-size: 12px;
        position: absolute;
        top: 100%;
        width: auto;
        -webkit-transform: translateY(9px);
        transform: translateY(9px);
    }

    .datacenter-input>.basic-slide.vaild+label.active:after {
        display: block;
        color: #4CAF50;
        content: "格式正确"
    }

    .datacenter-input>.basic-slide.invaild+label.active:after {
        display: block;
        color: #F44336;
        content: "格式错误"
    }
    /* textarea自适应高度所需div */

    .hiddendiv {
        padding: 0.8em 0 1.6em 0.5em;
        height: auto;
        line-height: 1.5em;
        box-sizing: content-box;
        font-size: 14px;
        white-space: pre-wrap;
        word-wrap: break-word;
        overflow-wrap: break-word;
        visibility: hidden;
        position: absolute;
        top: -999999999px;
    }

    .datacenter-radio,
    .datacenter-checkbox {
        pointer-events: none;
        opacity: 0;
        padding: 0;
        opacity: 0;
	    position: absolute;
	    left: -9999px;
	    z-index: 12;
	    width: 0;
	    height: 0;
	    cursor: pointer;
    }

    .datacenter-radio+label,
    .datacenter-checkbox+label {
        position: relative;
        font-family: "Roboto", sans-serif;
        font-size: 14px;
        color: #9e9e9e;
        padding-left: 30px;
        margin-right: 15px;
        cursor: pointer;
        display: inline-block;
        height: 25px;
        line-height: 25px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        box-sizing: border-box;
    }

    .datacenter-radio+label:before {
        position: absolute;
        content: " ";
        border: 2px solid #5a5a5a;
        border-radius: 50%;
        top: 0;
        left: 0;
        width: 16px;
        height: 16px;
        -webkit-transition: .28s ease;
        transition: .28s ease;
        box-sizing: border-box;
        margin: 4px;
    }
    .datacenter-radio:checked+label:before {
        border-color: #26a69a;
    }
    .datacenter-radio+label:after {
        position: absolute;
        content: '';
        left: 0;
        top: 0;
        width: 16px;
        height: 16px;
        -webkit-transition: .28s ease;
        transition: .28s ease;
        background-color: #26a69a;
        border-radius: 50%;
        -webkit-transform: scale(0);
        transform: scale(0);
        box-sizing: border-box;
        margin: 4px;
    }
    .datacenter-radio:checked+label:after {
        -webkit-transform: scale(0.5);
        transform: scale(0.5);
    }
    .datacenter-checkbox+label:before,
    .datacenter-checkbox+label:after  {
        margin-top: 4px;
        box-sizing: border-box;
        content: '';
        position: absolute;
        -webkit-transition: border .25s, background-color .25s, width .20s .1s, height .20s .1s, top .20s .1s, left .20s .1s;
        transition: border .25s, background-color .25s, width .20s .1s, height .20s .1s, top .20s .1s, left .20s .1s;
    }
    .datacenter-checkbox+label:after {
        height: 20px;
        width: 20px;
        background-color: transparent;
        border: 2px solid #5a5a5a;
        top: 0px;
        border-radius: 2px;
        top: 0;
        left: 0;
    }
    .datacenter-checkbox:checked+label:before {
        top: 2px;
        left: 1px;
        width: 8px;
        height: 13px;
        border-top: 2px solid transparent;
        border-left: 2px solid transparent;
        border-right: 2px solid #fff;
        border-bottom: 2px solid #fff;
        -webkit-transform: rotateZ(37deg);
        transform: rotateZ(37deg);
        -webkit-transform-origin: 100% 100%;
        transform-origin: 100% 100%;
        z-index: 1;
    }
    .datacenter-checkbox:checked+label:after {
        width: 20px;
        height: 20px;
        border: 2px solid #26a69a;
        background-color: #26a69a;
        z-index: 0;
    }

    /*select*/
    .datacenter-select {
        position: relative;
        color: #9e9e9e;
    }
    .datacenter-select > .down-icon {
        position: absolute;
        right: 0;
        top: 0;
        bottom: 0;
        height: 10px;
        margin: auto 0;
        font-size: 10px;
        line-height: 10px;
    }
    .datacenter-select > .select-dropdown {
        position: relative;
        cursor: pointer;
        background-color: transparent;
        border: none;
        border-bottom: 1px solid #9e9e9e;
        outline: none;
        height: 42px;
        line-height: 42px;
        width: 100%;
        font-size: 14px;
        margin: 0 0 20px 0;
        padding: 0;
        display: block;
        color: #333333;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }
    .datacenter-select > ul.dropdown-content {
        background-color: #fff;
        margin: 0;
        width: 100%;
        min-width: 100px;
        max-height: 350px;
        overflow-y: auto;
        display: none;
        position: absolute;
        z-index: 999;
        padding: 0;
        top: 0;
        left: 0;
        -webkit-box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);
        box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14), 0 1px 5px 0 rgba(0,0,0,0.12), 0 3px 1px -2px rgba(0,0,0,0.2);
        -webkit-transition: all 0.3s ease-out;
        transition: all 0.3s ease-out;
    }
    .datacenter-select > ul.dropdown-content.active {
        display: block;
    }
    .datacenter-select > ul.dropdown-content > li {
        clear: both;
        color: rgba(0,0,0,0.87);
        cursor: pointer;
        width: 100%;
        text-align: left;
        text-transform: none;
        list-style: none;
        font-size: 14px;
        color: #26a69a;
        display: block;
        padding: 14px 16px;
        box-sizing: border-box;
    }
    .datacenter-select > ul.dropdown-content > li:hover,
    .datacenter-select > ul.dropdown-content > li.disabled {
        background-color: rgba(0,0,0,0.06);
        
    }
    .datacenter-select > ul.dropdown-content > li.disabled {
        color: rgba(0,0,0,0.3);
    }
    .datacenter-select > ul.dropdown-content > li.active {
       background-color: rgba(0,0,0,0.03);
    }
    .datacenter-select > select {
        display: none;
    }
</style>
<script>
'use strict';

;(function (window, document) {

    var SWinput;
    var VERSION = '1.0.0';
    var swinputMap = {}; //保存所有SWinput对象

    SWinput = function (pt, options) {
        //pt  容器元素
        var me = this;

        me.wrapper = typeof pt === 'string' ? document.querySelector(pt) : pt;

        this._init(me.wrapper, options);
    };

    SWinput.version = VERSION;

    SWinput.prototype = {
        //初始化
        _init: function _init(wrapper, options) {

            var me = this;

            //实例化的包裹层
            me.warpper = wrapper;

            //创建ID
            me.id = options && options.id || Number(Math.random().toString().substr(2, 8) + Date.now()).toString(36);

            //默认选项
            me.options = {
                type: "1",
                title_en: "text",
                title: "defaultBox"

            };

            //参数和并
            for (var i in options) {
                me.options[i] = options[i];
            }

            //document事件监听
            //类型筛选
            switch (me.options.type) {
                case "1":
                    me.inputCallback(me.wrapper, me.options, me.id);
                    break;
                case "2":
                	me.radioCallback(me.wrapper, me.options, me.id);
                    break;
                case "3":
                	me.selectCallback(me.wrapper, me.options, me.id);                   
                    break;
                case "5":
                    me.checkBoxCallback(me.wrapper, me.options, me.id);
                    break;
                case "7":
                	me.textareaCallback(me.wrapper, me.options, me.id);
                    break;
                default:
                    break;
            }
        },

        //input类型render 并且添加事件监听
        inputCallback: function inputCallback(wrapper, options, id) {
            var me = this;
            var regular = options.check_rule;
            var title = options.title;
            var SWid = id;
            var _thisELE = null; //当前元素
            var _thisINPUT = null; //
            var value = options.a_value ? options.a_value : "";
            var name = options.title_en;
            var html = '<div class="datacenter-swinput datacenter-input" sw_id = ' + SWid + '>\n                            <input class="basic-slide" type="text" data-type="' + regular + '" autocomplete="off" name="'+name+'">\n                            <label>' + title + '</label>\n                        </div>';

            var checkInput = function checkInput(target) {
                var target = target;
                var hasValue = target.value !== ""; //当前input是否有值
                var value = ""; //input的值
                var vaild = false; //值是否合法有效

                if (!hasValue) {
                    target.nextElementSibling.classList.remove("active");
                    target.classList.remove("vaild");
                    target.classList.remove("invaild");
                } else {
                    target.nextElementSibling.classList.add("active");
                    value = target.value;
                    vaild = me.regular(regular, value);
                    if (vaild) {
                        target.classList.remove("invaild");
                        target.classList.add("vaild");
                    } else {
                        target.classList.remove("vaild");
                        target.classList.add("invaild");
                    }
                }
            };

            wrapper.insertAdjacentHTML('beforeend', html); //后期加入错误处理机制

            _thisELE = document.querySelector('div[sw_id=\'' + SWid + '\']');
            _thisINPUT = document.querySelector('div[sw_id=\'' + SWid + '\']>input');

            _thisINPUT.setAttribute("value", value); //初始化放值
            checkInput(_thisINPUT);

            //添加事件监听
            _thisINPUT.addEventListener('focus', function (evt) {
                var target = evt.target;
                target.nextElementSibling.classList.add("active");
            }, false);

            _thisINPUT.addEventListener('blur', function (evt) {
                var target = evt.target;
                checkInput(target);
            }, false);

            //保存SWinput对象；
            me.swinput = _thisELE;
            swinputMap[me.id] = me;
        },

        //正则验证
        regular: function regular(_regular, value) {

            function checkEmail(email) {
                // 邮箱验证方法
                console.log('reg email');
                var reg = /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/;
                var re = new RegExp(reg);
                var result = false;
                re.test(email) ? result = true : result = false;
                return result;
            }

            function checkIdentity(identity) {
                //  身份证号码验证
                console.log('reg identity');
                // 15位数身份证正则表达式
                var reg1 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
                // 18位数身份证正则表达式
                var reg2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z])$/;
                if (identity.match(reg1) == null && identity.match(reg2) == null) {
                    return false;
                } else {
                    return true;
                }
            }

            function checkMobiles(mobiles) {
                //  手机验证
                console.log('reg mobiles');
                var reg = /^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
                var re = new RegExp(reg);
                var result = false;
                re.test(mobiles) ? result = true : result = false;
                return result;
            }

            switch (_regular) {//验证规则选定
                case "1":
                    return checkEmail(value);
                case "2":
                    return checkIdentity(value);
                case "3":
                    return checkMobiles(value);
                default:
                    return true;
            }
        },

        //textarea类型render 同时添加事件监听 没有正则验证  支持 高度自适应内容
        textareaCallback: function textareaCallback(wrapper, options, id) {
            var me = this;
            var title = options.title;
            var SWid = id;
            var _thisELE = null; //当前元素
            var _thisTEXTAREA = null; //当前插入的元素中的textarea元素
            var hiddenDiv = null; //对应的div
            var html = '<div class="datacenter-swinput datacenter-input" sw_id = ' + SWid + '>\n                            <textarea class="basic-slide"></textarea>\n                            <label>' + title + '</label>\n                        </div>';
            var hiddenHtml = '<div class="hiddendiv" sw_id = ' + SWid + ' ></div>';

            wrapper.insertAdjacentHTML('beforeend', html); //后期需要加入错误处理机制
            document.body.insertAdjacentHTML('beforeend', hiddenHtml);

            _thisELE = document.querySelector('div[sw_id=\'' + SWid + '\']');
            _thisTEXTAREA = document.querySelector('div[sw_id=\'' + SWid + '\']>textarea');

            //设置hiddenDIV
            hiddenDiv = document.querySelector('div.hiddendiv[sw_id=\'' + SWid + '\']');
            hiddenDiv.style.width = window.getComputedStyle(_thisELE.querySelector('textarea')).width;

            //添加事件监听
            _thisTEXTAREA.addEventListener('focus', function (evt) {
                var target = evt.target;
                target.nextElementSibling.classList.add("active");
            }, false);

            _thisTEXTAREA.addEventListener('blur', function (evt) {
                var target = evt.target;
                var hasValue = target.value !== ""; //当前input是否有值
                var value = ""; //input的值

                if (!hasValue) {
                    target.nextElementSibling.classList.remove("active");
                }
            }, false);

            _thisTEXTAREA.addEventListener('input', function (evt) {
                var target = evt.target;
                var height = parseFloat(window.getComputedStyle(target).height); //textarea现有高度  
                var minHeight = parseFloat(window.getComputedStyle(target).minHeight); //textarea最小高度
                var targetValue = target.value;
                //内容放置到对应的div
                hiddenDiv.textContent = targetValue;
                var hiddenHeight = parseFloat(window.getComputedStyle(hiddenDiv).height);

                if (hiddenHeight > height || hiddenHeight < height && height > minHeight) {
                    target.style.height = hiddenHeight + "px";
                }
            }, false);

            //保存SWinput对象；
            me.swinput = _thisELE;
            swinputMap[me.id] = me;
        },

        //select类型render 添加相应的事件监听 无正则验证 只提供单选  多选请用checkbox
        selectCallback: function selectCallback(wrapper, options, id) {
            var me = this;
            var option = options.fileList;
            var SWid = id;
            var _thisELE = null;
            var _thisINPUT = null;
            var _thisSelect = null;
            var _thisList = null;
            var optionHtml = "";
            var selectHtml = "";
            var title = "";
            var value = "";
            var name = options.name;
            var checked = options.checked;
            for (var i = 0; i < option.length; i++) {
                title = option[i].title;
                value = option[i].value;
                optionHtml += '<li class="" data-index = ' + (i + 1) + ' data-value= "'+value+'">' + title + '</li>';

                selectHtml += '<option value="' + value + '">' + title + '</option>';
            }
            var html = '<div class="datacenter-select" sw_id="' + SWid + '">\n                            <span class="down-icon">▼</span>\n                            <input type="text" class="select-dropdown" readonly="true" value="选择一个选项">\n                            <ul class="dropdown-content">\n                                <li class="disabled active">选择一个选项</li>  \n                                ' + optionHtml + '                              \n                            </ul>\n                            <select class="initialized" name="'+name+'">\n                                <option value="" disabled selected>选择一个选项</option>\n                                ' + selectHtml + '\n                            </select>\n                        </div>';

            wrapper.insertAdjacentHTML('beforeend', html); //后期需要加入错误处理机制

            _thisELE = document.querySelector('div[sw_id=\'' + SWid + '\']');
            _thisINPUT = document.querySelector('div[sw_id=\'' + SWid + '\']>input');
            _thisList = document.querySelector('div[sw_id=\'' + SWid + '\']>ul');
            _thisSelect = document.querySelector('div[sw_id=\'' + SWid + '\']>select');

            //事件监听
            _thisINPUT.addEventListener("click", function (evt) {
                //展开，同时判断向上展开还是向下展开
                var target = evt.target;
                // var topDistance = $(this).parent('.selection-container').offset().top;  //元素距离顶部的距离
                // var scrollTop = $(window).scrollTop();  //网页被卷起高度
                // var viewHeight = $(window).height(); //可视区域高度
                // var selectionHeight = $(this).height();
                // var selectHeight = $(this).parent('.selection-container').height();   //selection 整体高度
                // var viewTop = topDistance - scrollTop;  //元素距离可视区域顶部距离
                // var viewBottom = viewHeight - viewTop - selectHeight;    // selection距离可视区域底部距离
                // var dropHeight = $(this).parent('.selection-container').find('.selection-drop').height();
                _thisList.classList.add("active");
            }, false);

            _thisList.addEventListener("click", function (evt) {
                var target = evt.target;
                var allOption = target.parentNode.children;
                var index = 1;
                var text = "";
                if (target.classList.contains('disabled')) {
                    _thisList.classList.remove("active");
                    return;
                }
                for (var i = 0; i < allOption.length; i++) {
                    allOption[i].classList.remove("active");
                }
                _thisList.classList.remove("active");
                target.classList.add('active');

                //设置inputvalue
                text = target.textContent;
                _thisINPUT.setAttribute('value', text);

                //设置select对应选项
                index = target.getAttribute('data-index');
                _thisSelect.children[index].selected = true;
            }, false);
            //如果有值
            if(checked !== ""){
            	var index = 0;
                var listLength = _thisList.children.length;
            	for( var i=0; i < listLength; i++){
            		if(_thisList.children[i].getAttribute('data-value') === checked){            			
						index = _thisList.children[i].getAttribute('data-index');
						console.log(index);
            		}
            	}
            	_thisINPUT.setAttribute('value', checked);
            	_thisSelect.children[index].selected = true;
            }
        },

        //radio类型render
        radioCallback: function radioCallback(wrapper, options, id) {
            var me = this;
            var title = options.title;
            var name = options.name;
            var SWid = id;
            var value = options.value;
            var checked = "";
            options.check ? checked = "checked" : "";
            var _thisELE = null; //当前元素 radio
            var html = '<input ' + checked + ' class="datacenter-radio" id="' + SWid + '" name="' + name + '" type="radio" value="' + value + '">\n                        <label for="' + SWid + '">' + title + '</label>';

            wrapper.insertAdjacentHTML('beforeend', html); //后期加入错误处理机制
            _thisELE = document.querySelector('div[id=\'' + SWid + '\']');

            //保存SWinput对象；
            me.swinput = _thisELE;
            swinputMap[me.id] = me;
        },

        //checkbox类型render
        checkBoxCallback: function checkBoxCallback(wrapper, options, id) {
            var me = this;
            var title = options.title;
            var name = options.name;
            var SWid = id;
            var value = options.value;
            var _thisELE = null; //当前元素 radio
            var checked = "";
            options.check ? checked = "checked" : "";
            var html = '<input ' + checked + ' class="datacenter-checkbox" id="' + SWid + '" name="' + name + '" type="checkbox" value="' + value + '">\n                        <label for="' + SWid + '">' + title + '</label>';

            wrapper.insertAdjacentHTML('beforeend', html); //后期加入错误处理机制
            _thisELE = document.querySelector('input[id=\'' + SWid + '\']');

            //保存SWinput对象；
            me.swinput = _thisELE;
            swinputMap[me.id] = me;
        }

    };

    window.SWinput = SWinput;
})(window, document);
</script>
<div id="people-update${people.id }">
	<div class="page-header">
		<div class="header-title">
			<h1>修改人口</h1>
		</div>
	</div>

	<!--  这里加入一个搜索框 下面加入个区间选择克隆对象？-->
	<nav>
		<div class="form-inline">
			<div class="form-group">
				<label for="search">填报字段</label> <input type="text"
					class="form-control search${people.id }" id="0" name="search" />
			</div>
			<button type="button" class="btn btn-default" id="smartSubmit">查询</button>
		</div>
	</nav>


	<div id="clone">
		<form id="cloneForm" class="" action="http://6.zhukaifeng.applinzi.com/tianyamingyuedao/content.php">
			<div class="cloneBox">
				
			</div>
	        <div class="clone-btn">
	        	<input type="hidden" name="id" value="${people.id} }" />
	        	<!-- <button class="btn  btn-palegreen" id='check' type = "submit">
					<i class=" glyphicon glyphicon-ok"></i>确认
				</button>
	        	<button class="btn  btn-darkorange" id='remove' type="button">
					<i class=" glyphicon glyphicon-remove"></i>取消
				</button> -->
				<input type = "button" value = "确认" id="check">
				<input type = "button" value = "取消" id="remove">
	        </div>
		</form>
	</div>

	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form"
					action="admin/people/do_update">
					<div class="form-group">
						<h4>基本信息</h4>
					</div>
					<input type="hidden" name="id" value="${people.id }" />
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">姓名</label>
							<div class="col-lg-4">
								<input type="text" class="form-control" name="name" value="${people.name }" />
							</div>
						</div>
						<div >
							<label class="col-lg-1 control-label" for="idcode">身份证号</label>
							<div class="col-lg-4">
								<input type="text" class="form-control" name="idcode" id="code" value="${people.idcode }" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="gender">性别</label>
						<div class="col-lg-4">
							<input name="gender" id="1" type="radio" value="1" style="opacity:1;position: static;height:13px;" ${people.gender=='1'? 'checked':'' }/>
							<label for="1">男性</label>
							<input name="gender" id="2" type="radio" value="2" style="opacity:1;position: static;height:13px;" ${people.gender=='2'? 'checked':'' }/>
							<label for="2">女性</label>
						</div>
						<label class="col-lg-1 control-label" for="birthday">生日</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="date" name="birthday" readonly="readonly" css-cursor="text" value='<fmt:formatDate value="${people.birthday }" pattern="yyyy-MM-dd" />'  />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="address">居住地址</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="address" value="${people.address }" />
						</div>
						<label class="col-lg-1 control-label" for="contact">联系号码</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="contact" id="contact" value="${people.contact }" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label">籍贯</label>
						<div class="col-lg-4">
							<input type="text" name="nativePlace" class="form-control" value="${people.nativePlace }"   />
						</div>
						<label class="col-lg-1 control-label" for="householdPlace">户籍地址</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="householdPlace" value="${people.householdPlace }"  />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label">民族</label>
						<div class="col-lg-4">
							<select name="nation" class="form-control" data-value="${people.nation }">
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
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label">婚姻状况</label>
						<div class="col-lg-4">
							<select class="form-control" name="maritalStatus"  data-value="${people.maritalStatus }">
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
							<select class="form-control" name="religion" data-value="${people.religion }">
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
							<select class="form-control" name="healthCondition" data-value="${people.healthCondition }">
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
							<select class="form-control" name="peopleType" data-value="${people.peopleType }">
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
							<input type="text" class="form-control" name="handicappedCode" value="${people.handicappedCode }"/>
						</div>
						<label class="col-lg-1 control-label" for="handicappedType">残疾类型</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="handicappedType" value="${people.handicappedType }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="handicappedLevel">残疾级别</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="handicappedLevel" value="${people.handicappedLevel }"/>
						</div>
						<label class="col-lg-1 control-label" for="handicappedReason">残疾原因</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="handicappedReason" value="${people.handicappedReason }"/>
						</div>
					</div>

					<div class="form-group">
						<h4>低保信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="lowIncomeInsuredCode">低保证号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="lowIncomeInsuredCode" value="${people.lowIncomeInsuredCode }"/>
						</div>
						<label class="col-lg-1 control-label" for="lowIncomeInsuredType">低保人员类别</label>
						<div class="col-lg-4">
							<select class="form-control" name="lowIncomeInsuredType" data-value="${people.lowIncomeInsuredType }">
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
							<input type="text" class="form-control" name="lowIncomeInsuredReason" value="${people.lowIncomeInsuredReason }"/>
						</div>
						<label class="col-lg-1 control-label" for="lowIncomeInsuredId">享受低保标识</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="lowIncomeInsuredId" value="${people.lowIncomeInsuredId }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="lowIncomeInsuredAmount">享受低保金额（元）</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="lowIncomeInsuredAmount" value="${people.lowIncomeInsuredAmount }"/>
						</div>
						<label class="col-lg-1 control-label" for="lowIncomeInsuredStart">享受开始日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="lowIncomeInsuredStart" name="lowIncomeInsuredStart" readonly="readonly" css-cursor="text" value='<fmt:formatDate value="${people.lowIncomeInsuredStart }" pattern="yyyy-MM-dd"/>' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="lowIncomeInsuredEnd">享受结束日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="lowIncomeInsuredEnd" name="lowIncomeInsuredEnd" readonly="readonly" css-cursor="text" value='<fmt:formatDate value="${people.lowIncomeInsuredEnd }" pattern="yyyy-MM-dd"/>'/>
						</div>
					</div>

					<div class="form-group">
						<h4>失业信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="unemployeeDate">就失业日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="unemployeeDate" name="unemployeeDate" readonly="readonly" css-cursor="text" value='<fmt:formatDate value="${people.unemployeeDate }" pattern="yyyy-MM-dd"/>'/>
						</div>
						<label class="col-lg-1 control-label" for="unemployeeCode">就失业证号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="unemployeeCode" value="${people.unemployeeCode }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="unemployeeStatus">就失业状态</label>
						<div class="col-lg-4">
							<input type="text" class="form-control"name="unemployeeStatus" value="${people.unemployeeStatus }"/>
						</div>
						<label class="col-lg-1 control-label" for="employeeId">就业标识</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="employeeId" value="${people.employeeId }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="hardToEmployeeType">就业困难人员类型</label>
						<div class="col-lg-4">
							<select class="form-control" name="hardToEmployeeType" data-value="${people.hardToEmployeeType }">
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
							<input type="text" class="form-control" name="employeeType" value="${people.employeeType }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="employeeCapacity">就业能力</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="employeeCapacity" value="${people.employeeCapacity }"/>
						</div>
						<label class="col-lg-1 control-label" for="employeeSituation">就业情况</label>
						<div class="col-lg-4">
							<select class="form-control" name="employeeSituation" data-value="${people.employeeSituation }">
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
							<input type="text" class="form-control" name="employeeDestination" value="${people.employeeDestination }"/>
						</div>
						<label class="col-lg-1 control-label" for="employeeWay">就业途径</label>
						<div class="col-lg-4">
							<select class="form-control" name="employeeWay" data-value="${people.employeeWay }">
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
							<select class="form-control" name="politicalStatus" data-value="${people.politicalStatus }">
								<option value=""> -- 请选择 --</option>
								<option value="群众">群众</option>
								<option value="共青团员">共青团员</option>
								<option value="中共预备党员">中共预备党员</option>
								<option value="中共党员">中共党员</option>
							</select>
						</div>
						<label class="col-lg-1 control-label" for="partyDate">入党日期</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" id="partyDate" name="partyDate" readonly="readonly" css-cursor="text" value='<fmt:formatDate value="${people.partyDate }" pattern="yyyy-MM-dd"/>'/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="partyPost">党内职务</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="partyPost" value="${people.partyPost }"/>
						</div>
						<label class="col-lg-1 control-label" for="partyOrganization">所在党组织</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="partyOrganization" value="${people.partyOrganization }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="partySuperior">党组织隶属</label>
						<div class="col-lg-4">
							<select class="form-control" name="partySuperior" data-value="${people.partySuperior }">
								<option value=""> -- 请选择 -- </option>
								<option value="社区">社区</option>
								<option value="街道">街道</option>
								<option value="本系统内">本系统内</option>
								<option value="无">无</option>
							</select>
						</div>
						<label class="col-lg-1 control-label" for="partyOrgContact">党组织联系电话</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="partyOrgContact" id="partyOrgContact" value="${people.partyOrgContact }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="CYOrganization">共青团组织</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="CYOrganization" id="CYOrganization" value="${people.CYOrganization }"/>
						</div>
					</div>
					<div class="form-group">
						<h4>计生信息</h4>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="childrenCount">子女数</label>
						<div class="col-lg-4">
							<input type="number" class="form-control" name="childrenCount" value="${people.childrenCount }"/>
						</div>
						<label class="col-lg-1 control-label" for="contraceptionMeasure">节育措施</label>
						<div class="col-lg-4">
							<select class="form-control" name="contraceptionMeasure" data-value="${people.contraceptionMeasure }">
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
							<input type="number" class="form-control" name="pregnancyWeeks" value="${people.pregnancyWeeks }"/>
						</div>
						<label class="col-lg-1 control-label" for="familyPlanningCode">计划生育证编号</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyPlanningCode" value="${people.familyPlanningCode }"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-1 control-label" for="familyPlanningType">计划生育证类型</label>
						<div class="col-lg-4">
							<input type="text" class="form-control" name="familyPlanningType" value="${people.familyPlanningType }"/>
						</div>
					</div>
					<div class="form-group">
						<div class="col-lg-offset-3 col-lg-3">
							<input class="btn btn-block btn-darkorange" type="submit"
								value="提交" />
						</div>
					</div>
				</form>
			</div>

		</div>
	</div>
</div>

<script>
seajs.use(['utils','ajax'], function(Utils,Ajax){
	var $page = $('#people-update${people.id }');
	Utils.datepicker($('#date', $page));
	var sFocus = Utils.focus($('.search${people.id }', $page));
	var fieldArray = [];    //最后一次从ES中获取到的数据
	
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
	
	

/**
 *  根据传回的type生成相应的输入框
 *  1.基础文本输入框  input
 *  2.选择按钮
 *  3.下拉选择框
 *  4.date时间控件
 *  5.other
 * *
 * **/

	function addSmartSerarch(json,SearchWord,SearchWordEnglish,peopleid,$page,type,check_rule){
	var type = type;
	var check_rule = check_rule;
	var fieldList = json.fieldList;
	var jsondata = json.data;
	var boxLength = $('.datacenter-gird-box').length;
	var boxHtml = '<div class="datacenter-gird-box datacenter-menu-box" data-uid="'+boxLength+'"><span class="radio-box-title">'+SearchWord+'</span></div>';
	var inputHtml = '<div class="datacenter-gird-box" data-uid="'+boxLength+'"></div>';
	console.log(json);
	console.log(SearchWordEnglish);
	$('.clone-btn').css("display","block");
	switch(type){
		case  "1":
			$('.cloneBox').prepend(inputHtml);
			 new SWinput('div[data-uid="'+boxLength+'"]', {
	            type: "1",
	            title: SearchWord,
	            title_en: SearchWordEnglish,
	            check_rule: check_rule,
	            a_value: jsondata[SearchWordEnglish]
	        });
				break;

        case  "7": 
        	$('.cloneBox').prepend(inputHtml);
        	new SWinput('div[data-uid="'+boxLength+'"]', {
                type: '7',
                title: SearchWord,
                title_en: SearchWordEnglish
            });
        	break;
        case  "2": 
        	$(".cloneBox").prepend(boxHtml);
        	var checked = false;
        	if( typeof(jsondata[SearchWordEnglish]) !== "undefined"){
        		checked = jsondata[SearchWordEnglish];	
        	}       
        	console.log(checked);
        	for(var i = 0; i<json.fieldList.length; i++){
        		console.log(json.fieldList[i].c_enum_value);
        		if(json.fieldList[i].c_enum_value === checked){
        			checked = true;
        		}else{
        			checked = false;
        		}
        		new SWinput('div[data-uid="'+boxLength+'"]', {
    	            type: "2",
    	            title: fieldList[i].c_enum_cn_name,
    	            name: SearchWordEnglish,
    	            value: fieldList[i].c_enum_value,
    	            check: checked
    	        }); 
        	}
        	break;
        case  "5": 
        	$(".cloneBox").prepend(boxHtml);
        	for(var i = 0; i<json.fieldList.length; i++){
        		new SWinput('div[data-uid="'+boxLength+'"]', {
    	            type: "5",
    	            title: fieldList[i].c_enum_cn_name,
    	            name: SearchWordEnglish,
    	            value: fieldList[i].c_enum_value
    	        }); 
        	}
        	break;
        case  "3": 
        	$(".cloneBox").prepend(boxHtml);
        	var fileList = [];
        	var checked = "";
        	var name = SearchWordEnglish;
        	if( typeof(jsondata[SearchWordEnglish]) !== "undefined"){
        		checked = jsondata[SearchWordEnglish];	
        	}       
        	for(var i = 0; i<fieldList.length; i++){
        		fileList[i] = {};
        		fileList[i].title = fieldList[i].c_enum_cn_name;
        		fileList[i].value = fieldList[i].c_enum_value;
        	}
        	new SWinput('div[data-uid="'+boxLength+'"]',{
                type: '3',
                fileList: fileList,
                checked:  checked,
                name: name
            }) 
        	break;
        case  "6":
        	var dateTime = null;
        	if( typeof(jsondata[SearchWordEnglish]) !== "undefined"){
        		dataTime = new Date(jsondata[SearchWordEnglish]).toLocaleString();
        	}else {
        		dataTime = '';
        	}
        	console.log(dataTime);
            $(".cloneBox").prepend(
        '<div class="datacenter-menu-box datacenter-gird-box">' +
                '<label class="" for="'+SearchWordEnglish+'">'+SearchWord+'</label>' +
                '<input type="text" class="form-control data-picker datacenter-form-control" id="smartdate" name="'+SearchWord+'"' +
                'readonly="readonly" css-cursor="text"' +
                'value='+dataTime+'>' +
                '</div>');
            /*Utils.datepicker($('#smartdate', $page)); */
            $($('#smartdate', $page)).datepicker({
            	container   : '#tab_2',
				format		: 'yyyy-mm-dd',
				weekStart	: 1,
				daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],  
                monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',  
                        '七月', '八月', '九月', '十月', '十一月', '十二月' ]
			});
        break;
		default: alert("请输入正确的字段");
	}
    }	
    
    
    
	//如果已经查询过则不再查询
		var alreadyName = [];
		$("#smartSubmit").on("click",function(){
			console.log("查询点击");
			var peopleid=${people.id };
			var keyWord = $('.search${people.id }').val();
			var keyArray = fieldArray;
			var SearchWord = "";
			var SearchWordEnglish="";
			var type = "";
			var check_rule = "";	
			
			for(var i=0; i<keyArray.length; i++){
				if(keyWord === keyArray[i].title){
					SearchWord = keyArray[i].title;
					type = keyArray[i].type;
					SearchWordEnglish = keyArray[i].title_en;
					check_rule = keyArray[i].check_rule;
				}
			}
			if($.inArray(SearchWordEnglish,alreadyName) !== -1){
				return;
			} 
			console.log(fieldArray);
			alreadyName.push(SearchWordEnglish);
            Ajax.ajax('admin/people/smartSearch',{
                peopleid : peopleid,
                type:type,
                field:SearchWordEnglish                     //name  后期改为SearchWordEnglish
            },function(json) {
                addSmartSerarch(json,SearchWord,SearchWordEnglish,peopleid,$page,type,check_rule);
            });
        });
		$("#clone").on('click',"[id='check']",function(){			
			$('#cloneForm').submit();
			$(".cloneBox").html("");			
            $('.clone-btn').css("display","none");
            alreadyName = [];
		});

	    $("#clone").on('click',"[id='remove']",function(){
	    		alreadyName = [];
				$(".cloneBox").html("");
	    		$('.clone-btn').css("display","none");
	    		alreadyName = [];
	    });
    
    
    /**
	 * 字段查询
	 * *
	 * **/	
	var k = null;
	$('.search${people.id }').keyup(function(event) {
		if (event.keyCode > "40" || event.keyCode == "32"|| event.keyCode == "8"
			&& $(this).val() != ""&& $(this).val() != null) {
			Ajax.ajax('admin/people/titleSearch',
					{txt : $(this).val()},
					function(json) {
						fieldArray = json;
						$('.search${people.id }').bigAutocomplete({
							width : 190,
							data : json
							});
					});
		
		k = $(this).val();
		}
	if ($(this).val() == "" || $(this).val() == null)
		hideContent();
	});
	$('.search${people.id }').keydown(function(event) {
		switch (event.keyCode) {
		case 40://向下键
			if ($("#bigAutocompleteContent").css("display") == "none") return;
			var $nextSiblingTr = $("#bigAutocompleteContent").find(".ct");
			if ($nextSiblingTr.length <= 0) {//没有选中行时，选中第一行
				$nextSiblingTr = $("#bigAutocompleteContent").find("tr:first");
			} else {
				$nextSiblingTr = $nextSiblingTr.next();
			}
			$("#bigAutocompleteContent").find("tr").removeClass("ct");
			if ($nextSiblingTr.length > 0) {//有下一行时（不是最后一行）
				$nextSiblingTr.addClass("ct");//选中的行加背景
				$(this).val($nextSiblingTr.find("div:last").html());//选中行内容设置到输入框中
				$(this).attr('id',$nextSiblingTr.find("div:last").attr("id"));
				//div滚动到选中的行,jquery-1.6.1 $nextSiblingTr.offset().top 有bug，数值有问题
				$("#bigAutocompleteContent").scrollTop($nextSiblingTr[0].offsetTop
						- $("#bigAutocompleteContent").height()
						+ $nextSiblingTr.height());
			} else {
				console.log("else");
				$(this).val(k);//输入框显示用户原始输入的值
				$(this).attr('id', "0");//id为0时，添加无效
				}
			break;
		case 38://向上键
			if ($("#bigAutocompleteContent").css("display") == "none") return;
			var $previousSiblingTr = $("#bigAutocompleteContent").find(".ct");
			if ($previousSiblingTr.length <= 0) {//没有选中行时，选中最后一行行
				$previousSiblingTr = $("#bigAutocompleteContent").find("tr:last");
			} else {
				$previousSiblingTr = $previousSiblingTr.prev();
			}
			$("#bigAutocompleteContent").find("tr").removeClass("ct");
			if ($previousSiblingTr.length > 0) {//有上一行时（不是第一行）
				$previousSiblingTr.addClass("ct");//选中的行加背景
				$(this).val($previousSiblingTr.find("div:last").html());//选中行内容设置到输入框中
				$(this).attr('id',$nextSiblingTr.find("div:last").attr("id"));
				//div滚动到选中的行,jquery-1.6.1 $$previousSiblingTr.offset().top 有bug，数值有问题
				$("#bigAutocompleteContent").scrollTop($previousSiblingTr[0].offsetTop
						- $("#bigAutocompleteContent").height()
						+ $previousSiblingTr.height());
			} else {
				$(this).val(k);//输入框显示用户原始输入的值
				$(this).attr('id', "0");//id为0时，添加无效
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
	function hideContent() {
		if ($("#bigAutocompleteContent").css("display") != "none") {
			$("#bigAutocompleteContent").find("tr").removeClass("ct");
			$("#bigAutocompleteContent").hide();
		}
	}
	var bigAutocomplete = new function() {
		this.holdText = null;//输入框中原始输入的内容

		//初始化插入自动补全div，并在document注册mousedown，点击非div区域隐藏div
		this.init = function() {
			$("body").append("<div id='bigAutocompleteContent' class='bigautocomplete-layout'></div>");
			$(document).bind('mousedown',function(event) {
				var $target = $(event.target);
				if ((!($target.parents().andSelf().is('#bigAutocompleteContent')))
						&& (!$target.is(sFocus))) {
					hideContent();
					}
				})

			//鼠标悬停时选中当前行
			$("#bigAutocompleteContent").delegate("tr", "mouseover",function() {
				$("#bigAutocompleteContent tr").removeClass("ct");
				$(this).addClass("ct");
				}).delegate("tr", "mouseout", function() {
					$("#bigAutocompleteContent tr").removeClass("ct");
			});

			//单击选中行后，选中行内容设置到输入框中，并执行callback函数
			$("#bigAutocompleteContent").delegate("tr","click",function() {
				sFocus.val($(this).find("div:last").html());
				hideContent();
				});
		}

		this.autocomplete = function(param) {
			if ($("body").length > 0&& $("#bigAutocompleteContent").length <= 0) {
				bigAutocomplete.init();//初始化信息
			}

			var $this = $(this);//为绑定自动补全功能的输入框jquery对象
			var $bigAutocompleteContent = $("#bigAutocompleteContent");

			this.config = {
				//width:下拉框的宽度，默认使用输入框宽度
				width : $this.outerWidth() - 2,
				//url：格式url:""用来ajax后台获取数据，返回的数据格式为data参数一样
				url : null,
				/*data：格式{data:[{title:null,result:{}},{title:null,result:{}}]}
				url和data参数只有一个生效，data优先*/
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

			if ((data != null || data != "") && $.isArray(data)) {
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
					cont += "<tr><td><div id="+data_[i].title_en+">"
							+ data_[i].title + "</div></td></tr>"
				}

				cont += "</tbody></table>";
				$bigAutocompleteContent.html(cont);
				$bigAutocompleteContent.show();
				//每行tr绑定数据，返回给回调函数
				$bigAutocompleteContent.find("tr").each(function(index) {
					$(this).data("jsonData", data_[index]);
				})
			}
		}
	};

	$.fn.bigAutocomplete = bigAutocomplete.autocomplete;
});
	

</script>
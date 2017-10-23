<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link rel="stylesheet" href="media/admin/bigautocomplete/css/jquery.bigautocomplete.css" type="text/css" />


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


	<div id="clone"></div>

	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form"
					action="admin/people/do_update">
					<input type="hidden" name="id" value="${people.id }" />
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name">姓名</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="name"
								value="${people.name }" id="姓名" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="idcode">身份证号</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="idcode" id="code"
								value="${people.idcode }" />
						</div>
						<div class="col-lg-3">
							<input id="txt" readonly="readonly" style="border: 0px" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="gender">性别</label>
						<div class="col-lg-5">
							<input name="gender" id="1" type="radio" value="1"
								style="opacity: 1; position: static; height: 13px;"
								<c:if test="${people.gender==1 }"> checked </c:if> /> <label
								for="1">男</label> <input name="gender" id="0" type="radio"
								value="0" style="opacity: 1; position: static; height: 13px;"
								<c:if test="${people.gender==0 }"> checked </c:if> /> <label
								for="0">女</label>
						</div>
					</div>

					<div class="form-group">
						<label class="col-lg-2 control-label" for="address">居住地址</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="address"
								value="${people.address }" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="contact">联系号码</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="contact"
								id="contact" value="${people.contact }" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-lg-2 control-label">民族</label>
						<div class="col-lg-5">
							<select name="nation" class="form-control" data-value="${people.national }">
								<option value=""> -- 请选择 -- </option>
								<option value="1">汉族</option>
								<option value="2">蒙古族</option>
							</select>
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

	function addSmartSerarch(json,SearchWord,SearchWordEnglish,peopleid,$page){
	debugger;
	var basehtml ='<form class="bv-form form-horizontal validate-form" action="admin/people/do_update">' +
        '<input type="hidden" name="id" value="'+peopleid+'" />';
	switch(json.type){
		case  "1":
		    $("#clone").append(basehtml +
                '<div class="form-group">' +
                '<label class="col-lg-2 control-label" for="'+SearchWordEnglish+'" >'+SearchWord+'</label>' +
                '<div class="col-lg-5">' +
                '<input type="text" class="form-control" name="'+SearchWordEnglish+
				'" value="'+json.data[SearchWordEnglish]+'"  />' +
                '</div>' +
                '</div>'+
				"<button class=\"btn btn-labeled btn-palegreen\" id='check'>\n" +
				"<i class=\"btn-label glyphicon glyphicon-ok\"></i>确认</button>" +
                "<button class=\"btn btn-labeled btn-darkorange\" id='remove'>  \n" +
				" <i class=\"btn-label glyphicon glyphicon-remove\"></i>取消</button></form>");
				break;

        case  "2": alert("check");break;
        case  "3": alert("check radio");break;
        case  "4": alert("dateutil");
            $("#clone").append(basehtml +
        '<div class="form-group">' +
                '<label class="col-lg-2 control-label" for="'+SearchWordEnglish+'">'+SearchWord+'</label>' +
                '<div class="col-lg-5">' +
                '<input type="text" class="form-control" id="smartdate" name="'+SearchWord+'"' +
                'readonly="readonly" css-cursor="text"' +
                'value='+new Date(json.data[SearchWordEnglish]).toLocaleString()+' />' +
                '</div>' +
                '</div>');
            Utils.datepicker($('#smartdate', $page));
        break;
		default: alert("2");
	}
    }

		$("#smartSubmit").click(function(){

		    var peopleid=1;
            SearchWord = "日期";
            var SearchWordEnglish="idcode";
            Ajax.ajax('admin/people/smartSearch',{
                peopleid : peopleid
            },function(json) {
                addSmartSerarch(json,SearchWord,SearchWordEnglish,peopleid,$page);
//
//                $("#clone").append('<form class="bv-form form-horizontal validate-form" action="admin/people/do_update">' +
//                '<input type="hidden" name="id" value="'+peopleid+'" />' +
//                '<div class="form-group">' +
//                '<label class="col-lg-2 control-label" for="name" >'+SearchWord+'</label>' +
//                '<div class="col-lg-5">' +
//                '<input type="text" class="form-control" name="name" value="'+json.status+'"  id="姓名"/>' +
//                '</div>' +
//                '</div>'+
//				"<button class=\"btn btn-labeled btn-palegreen\" id='check'>\n" + "<i class=\"btn-label glyphicon glyphicon-ok\"></i>确认</button>" +
//                "<button class=\"btn btn-labeled btn-darkorange\" id='remove'>  \n" + " <i class=\"btn-label glyphicon glyphicon-remove\"></i>取消</button></form>");
            });

//			SearchWord = "姓名";
//            $("#"+SearchWord).parent().parent().clone(true).appendTo($("#clone"));
//            $("#clone").append("<button class=\"btn btn-labeled btn-palegreen\" id='check'>\n" +
//                "<i class=\"btn-label glyphicon glyphicon-ok\"></i>确认</button>" +
//				"<button class=\"btn btn-labeled btn-darkorange\" id='remove'>  \n" +
//                " <i class=\"btn-label glyphicon glyphicon-remove\"></i>取消</button>");
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
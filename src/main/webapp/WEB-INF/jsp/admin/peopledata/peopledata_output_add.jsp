<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link rel="stylesheet" href="media/admin/bigautocomplete/css/jquery.bigautocomplete.css" type="text/css" />
<style>
input[type=checkbox]{
opacity:1;
position:initial;
}
.page-body {
	border-top: 1px solid #999999;
	padding-right: 0;
}
.infor-search {
	margin: 0 5px 0 15px;
}

.form-inline {
	display:inline-block;
}
.cloneBox {
	padding:20px;
	float:left;
}
.aaa  
    {float:left; width:100px; height:35px; margin:10px;padding:10px;border:1px solid #aaaaaa;
    text-align:center;}
</style>
<script>
var sFocus;
	seajs.use(['ajax', 'dialog','utils'], function(Ajax, Dialog,Utils){
		var $page = $('#peopledata_output_add${id}');
		
		$(".smartSearch${id}").focus(function(){
			sFocus = $(this);
		});
		
		function getId(id){
			return document.getElementById(id);
		}
		//获取鼠标位置
		function getMousePos(e){
			return {
				x : e.pageX || e.clientX + document.body.scrollLeft,
				y : e.pageY || e.clientY + document.body.scrollTop
			}
		}
		//获取元素位置
		function getElementPos(el){
			return {
				x : el.getBoundingClientRect().left,
				y : el.getBoundingClientRect().top
			}
		}
		//获取元素尺寸
		function getElementSize(el){
			return {
				width : el.offsetWidth,
				height : el.offsetHeight
			}
		}
		//禁止选择
		document.onselectstart = function(){
			return false;
		}
		//判断是否有挪动
		var isMove = false;

		//就是创建的标杆
		var div = document.createElement('span');

		var outer_wrap = getId('cloneBox_${id}');
		outer_wrap.onmousedown = function(event){
			//获取列表顺序
			var lis = outer_wrap.getElementsByTagName('div');
			for(var i = 0; i < lis.length; i++){
				lis[i]['pos'] = getElementPos(lis[i]);
				lis[i]['size'] = getElementSize(lis[i]);
			}
			event = event || window.event;
			var t = event.target || event.srcElement;
			if(t.tagName.toLowerCase() == 'div'){
				var p = getMousePos(event);
				var el = t.cloneNode(true);
				el.style.position = 'absolute';
				el.style.left = t.pos.x + 'px';
				el.style.top = t.pos.y + 'px';
				el.style.width = t.size.width + 'px';
				el.style.height = t.size.height + 'px';
				el.style.border = '1px solid #d4d4d4';
				el.style.background = '#d4d4d4';
				el.style.opacity = '0.7';
				document.body.appendChild(el);
				
				document.onmousemove = function(event){
					event = event || window.event;
					var current = getMousePos(event);
					el.style.left =t.pos.x + current.x - p.x + 'px';
					el.style.top =t.pos.y + current.y - p.y+ 'px';
					document.body.style.cursor = 'move';
					var a;
					for(var i = 0; i < lis.length; i++){
						if(t == lis[i]){
							a=i;
						}
					}
					//判断插入点
					for(var i = 0; i < lis.length; i++){
						if((current.x > lis[i]['pos']['x'] && current.x < lis[i]['pos']['x'] + lis[i]['size']['width']) 
						&& (current.y > lis[i]['pos']['y'] && current.y < lis[i]['pos']['y'] + lis[i]['size']['height'])){
							if(a>i){
								isMove = true;
								outer_wrap.insertBefore(div,lis[i]);
							}
							if(a<i){
								isMove = true;
								outer_wrap.insertBefore(div,lis[i].nextSibling);
							}

						}
					}
				}
				//移除事件
				document.onmouseup = function(event){
					event = event || window.event;
					document.onmousemove = null;
					if(isMove){
						outer_wrap.replaceChild(t,div);
						isMove = false;
					}
				document.body.removeChild(el);
				el = null;
				document.body.style.cursor = 'normal';
				document.onmouseup = null;
				}
			}
		}
		
		selectAll = function (x){
		    var isCheck=$("input[value='"+x+"']").is(':checked');  //获得全选复选框是否选中
		    $("input[name='"+x+"']").each(function() {
		    	var beChecked = this.checked;
		        this.checked = isCheck;       //循环赋值给每个复选框是否选中
		        if(beChecked != isCheck)
		    		this.onchange();
		        
		    });
		}
		selectOne = function(enname,pageId,cId){
			//var enname = dic.cCnEnglish;
			//var cnname = dic.cCnName;
			//var cId = dic.cId
			var idName = enname+"_"+pageId;
			var id = $("#"+idName);
			var name = id.prop("name");
			var val = id.val().split("_")[0];//cnname+"_"+pageId;
			var cbox = $("input[name='"+name+"']");
			var isCheck = id.is(':checked');
			if(isCheck==true){
				var html = "<div id='checked_"+idName+"_"+cId+"' class='aaa'>"
						+"<input id='list_"+idName+"_"+cId+"' type='hidden' name='list' value='"+cId+"' />"+val
						+"<a href='#' onclick='deleteDiv("+idName+")' style='float:right;text-decoration:none;font-family:Marlett'>r</a>"
						+"</div>";
				$("#cloneBox_"+pageId).append(html);
				
			}
			else{
				$("#checked_"+idName+"_"+cId).remove();
				$("#list_"+idName+"_"+cId).remove();
			}
			for(i=0;i<cbox.length;i++){
				var isCheck2 = cbox[i].checked;
				if(isCheck2==false){
					$("input[value='"+name+"']").each(function() {
						this.checked=false;
					});
					break;
				}
				if(i==(cbox.length-1))
					$("input[value='"+name+"']").each(function() {
						this.checked=true;
					});
			}
		}
		
		saveModel = function(id){
			if($("input[type='checkbox']").is(':checked')==false){
				alert("请至少选择一个字段");
				return;
			}
			$("#cloneForm_"+id).submit();			
		}
		

		smartSubmit = function(x){
			var cnname = $('.smartSearch'+x).val();
			$("input[value='"+cnname+"_"+x+"']").each(function() {  
				if(this.checked==true)
					alert("字段已添加");
				else{
					this.checked = true;
			        this.onchange();
				}
		    });
		};
		
		deleteDiv = function(idName){
			$(idName).each(function() {
				this.checked=false;
				this.onchange();
			});
		}
		/**
		 * 字段查询
		 * *
		 * **/	
		var k = null;
		$('.smartSearch${id}').keyup(function(event) {
			if (event.keyCode > "40" || event.keyCode == "32"|| event.keyCode == "8"
				&& $(this).val() != ""&& $(this).val() != null) {
				Ajax.ajax('admin/peopledata/titleSearch',
						{txt : $(this).val()},
						function(json) {
							$('.smartSearch${id}').bigAutocomplete({
								width : 190,
								data : json.data
								});
						});
			
			k = $(this).val();
			}
		if ($(this).val() == "" || $(this).val() == null)
			hideContent();
		});
		$('.smartSearch${id}').keydown(function(event) {
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
					$(this).attr('id',$previousSiblingTr.find("div:last").attr("id"));
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
					sFocus.attr('id',$(this).find("div:last").attr("id"));
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
						cont += "<tr><td><div id="+data_[i].cCnEnglish+">"
								+ data_[i].cCnName + "</div></td></tr>"
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
<div id="peopledata_output_add${id}">
	<div class="page-header">
		<div class="header-title">
			<h1>模板生成</h1>
		</div>
	</div>
	<nav>
		
		<form id="cloneForm_${id}" class="form-inline" action="admin/peopledata/do_outputAdd" >
			<input id='modelId' type='hidden' name='id' value='${model.id}' />
			<div style="padding:2px">
				<label for="search">填模板名</label> 
				<input type="text" class="form-control" name="modelName" value='${model.modelName}'/>
			</div>
			<div style="padding:2px">
				<label for="search">填写表名</label> 
				<input type="text" class="form-control" name="titleName" value='${model.titleName}'/>
			</div>
			<div class="form-group" style="padding:2px">
				<label for="search">填报字段</label> 
				<input type="text" class="form-control smartSearch${id}" id="0" />
			</div>
			<button type="button" class="btn btn-default" id="smartSubmit_${id}" onclick="smartSubmit(${id})">添加</button>
			<div class="bv-form form-horizontal">
				<div id="cloneBox_${id}" class="cloneBox">
				<c:forEach items="${list }" var="item" varStatus="i">
					<script>
					$("#${item.cCnEnglish}_${id}").each(function() {
						this.checked = true;
			        	this.onchange();
			        });
					</script>
				</c:forEach>
				</div>
			</div>
		</form>
		
	</nav>
	
	<div class="page-body">
		<div class="row">
				<h4><input name="information" type="checkbox" value="people_${id}" onchange="selectAll('people_${id}')" />基本信息</h4>
			</div>
			<div class="row">
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('name',${id},'14')" id="name_${id}" value="姓名_${id}"  />姓名</label>
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('idcode',${id},'15')" id="idcode_${id}" value="身份证号_${id}" />身份证号</label>
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('gender',${id},'2')" id="gender_${id}" value="性别_${id}" />性别</label>
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('birthday',${id},'17')" id="birthday_${id}" value="生日_${id}" />生日</label>
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('nation',${id},'1')" id="nation_${id}" value="民族_${id}" />民族</label>
			</div>
			<div class="row">
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('peopleType',${id},'6')" id="peopleType_${id}" value="人口类型_${id}" />人口类型</label>	
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('maritalStatus',${id},'3')" id="maritalStatus_${id}" value="婚姻状况_${id}" />婚姻状况</label>
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('religion',${id},'4')" id="religion_${id}" value="宗教信仰_${id}" />宗教信仰</label>
				<label class="col-lg-2"><input name="people_${id}" class="con" type="checkbox" onchange="selectOne('healthCondition',${id},'5')" id="healthCondition_${id}" value="健康状况_${id}" />健康状况</label>
				<%-- <label class="col-lg-2">家庭医生</label>--%>
			</div>
			<div class="row">
				<h4><input name="information" type="checkbox" value="handicapped" onchange="selectAll('handicapped')" />残疾人信息</h4>
			</div>
			<div class="row">
				<label class="col-lg-2"><input name="handicapped" class="con" type="checkbox" onchange="selectOne('handicapped')" value="残疾证号" />残疾证号</label>
				<label class="col-lg-2"><input name="handicapped" class="con" type="checkbox" onchange="selectOne('handicapped')" value="残疾类型" />残疾类型</label>
				<label class="col-lg-2"><input name="handicapped" class="con" type="checkbox" onchange="selectOne('handicapped')" value="残疾级别" />残疾级别</label>
				<label class="col-lg-2"><input name="handicapped" class="con" type="checkbox" onchange="selectOne('handicapped')" value="残疾原因" />残疾原因</label>
			</div>
			<div class="row">
				<h4><input name="information" type="checkbox" value="lowIncomeInsured" onchange="selectAll('lowIncomeInsured')" />低保信息</h4>
			</div>
			<div class="row">
				<label class="col-lg-2"><input name="lowIncomeInsured" class="con" type="checkbox" onchange="selectOne('lowIncomeInsured')" value="低保证号" />低保证号</label>
				<label class="col-lg-2"><input name="lowIncomeInsured" class="con" type="checkbox" onchange="selectOne('lowIncomeInsured')" value="低保人员类别" />低保人员类别</label>
				<label class="col-lg-2"><input name="lowIncomeInsured" class="con" type="checkbox" onchange="selectOne('lowIncomeInsured')" value="低保原因" />低保原因</label>
				<label class="col-lg-2"><input name="lowIncomeInsured" class="con" type="checkbox" onchange="selectOne('lowIncomeInsured')" value="享受低保标识" />享受低保标识</label>
				<label class="col-lg-2"><input name="lowIncomeInsured" class="con" type="checkbox" onchange="selectOne('lowIncomeInsured')" value="享受低保金额" />享受低保金额</label>
			</div>
			<div class="row">
				<label class="col-lg-2"><input name="lowIncomeInsured" class="con" type="checkbox" onchange="selectOne('lowIncomeInsured')" value="享受开始日期" />享受开始日期</label>
				<label class="col-lg-2"><input name="lowIncomeInsured" class="con" type="checkbox" onchange="selectOne('lowIncomeInsured')" value="享受结束日期" />享受结束日期</label>
			</div>
			
			<div class="row">
				<h4><input name="" type="checkbox" value="unemployee" onchange="selectAll('unemployee')" />失业信息</h4>
			</div>
			
			<div class="row">
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就失业日期" />就失业日期</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就失业证号" />就失业证号</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就失业状态" />就失业状态</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就业标识" />就业标识</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就业困难人员类型" />就业困难人员类型</label>
			</div>
			<div class="row">
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就业类型" />就业类型</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就业能力" />就业能力</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就业情况" />就业情况</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就业去向" />就业去向</label>
				<label class="col-lg-2"><input name="unemployee" class="con" type="checkbox" onchange="selectOne('unemployee')" value="就业途径" />就业途径</label>
			</div>
	
			<div class="row">
				<h4><input name="information" type="checkbox" value="political" onchange="selectAll('political')" />党员信息</h4>
			</div>


			<!-- 党员信息 -->
			<div class="row">
				<label class="col-lg-2"><input name="political" class="con" type="checkbox" onchange="selectOne('political')" value="政治面貌" />政治面貌</label>
				<label class="col-lg-2"><input name="political" class="con" type="checkbox" onchange="selectOne('political')" value="入党日期" />入党日期</label>
				<label class="col-lg-2"><input name="political" class="con" type="checkbox" onchange="selectOne('political')" value="党内职务" />党内职务</label>
				<label class="col-lg-2"><input name="political" class="con" type="checkbox" onchange="selectOne('political')" value="所在党组织" />所在党组织</label>
				<label class="col-lg-2"><input name="political" class="con" type="checkbox" onchange="selectOne('political')" value="党组织隶属" />党组织隶属</label>
			</div>
			<div class="row">
				<label class="col-lg-2"><input name="political" class="con" type="checkbox" onchange="selectOne('political')" value="党组织联系电话" />党组织联系电话</label>
				<label class="col-lg-2"><input name="political" class="con" type="checkbox" onchange="selectOne('political')" value="共青团组织" />共青团组织</label>
			</div>

			<div class="row">
				<h4><input name="information" type="checkbox" value="familyPlanning" onchange="selectAll('familyPlanning')" />计生信息</h4>
			</div>

			<!-- 计生信息 -->
			<div class="row">
				<label class="col-lg-2"><input name="familyPlanning" class="con" type="checkbox" onchange="selectOne('familyPlanning')" value="子女数" />子女数</label>
				<label class="col-lg-2"><input name="familyPlanning" class="con" type="checkbox" onchange="selectOne('familyPlanning')" value="节育措施" />节育措施</label>
				<label class="col-lg-2"><input name="familyPlanning" class="con" type="checkbox" onchange="selectOne('familyPlanning')" value="现孕周" />现孕周</label>
				<label class="col-lg-2"><input name="familyPlanning" class="con" type="checkbox" onchange="selectOne('familyPlanning')" value="计划生育证编号" />计划生育证编号</label>
				<label class="col-lg-2"><input name="familyPlanning" class="con" type="checkbox" onchange="selectOne('familyPlanning')" value="计划生育证类型" />计划生育证类型</label>
			</div>
			<div class="form-group">
				<div class="col-lg-offset-3 col-lg-3">
				<c:if test="${id == 0}">
					<input class="btn btn-block btn-darkorange"  onclick="saveModel(${id})" value="生成模板" />
				</c:if>
				<c:if test="${id != 0}">
					<input class="btn btn-block btn-darkorange"  onclick="saveModel(${id})" value="修改模板" />
				</c:if>
					
				</div>
			</div>
	</div>
	<c:forEach items="${list }" var="item" varStatus="i">
	
	</c:forEach>
</div>


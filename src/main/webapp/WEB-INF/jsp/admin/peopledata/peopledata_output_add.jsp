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
    {float:left; width:150px; height:35px; margin:10px;padding:10px;border:1px solid #aaaaaa;
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
			var idName = enname+"_"+pageId;
			var id = $("#"+idName);//字段id
			var name = id.prop("name");//字段name值
			var val = id.val().split("_")[0];//字段value
			var cbox = $("input[name='"+name+"']");//同name值字段
			var isCheck = id.is(':checked');
			if(isCheck==true){//添加div块
				var html = "<div id='checked_"+idName+"_"+cId+"' class='aaa'>"
						+"<input id='list_"+idName+"_"+cId+"' type='hidden' name='list' value='"+cId+"' />"+val
						+"<a href='#' onclick='deleteDiv("+idName+")' style='float:right;text-decoration:none;font-family:Marlett'>r</a>"
						+"</div>";
				$("#cloneBox_"+pageId).append(html);
				
			}
			else{//删除div块
				$("#checked_"+idName+"_"+cId).remove();
				$("#list_"+idName+"_"+cId).remove();
			}
			for(i=0;i<cbox.length;i++){
				var isCheck2 = cbox[i].checked;
				if(isCheck2==false){//如果有未选中子项目 则父项目未选中
					$("input[value='"+name+"']").each(function() {
						this.checked=false;
					});
					break;
				}
				if(i==(cbox.length-1))//如果子项目全选中则父项目选中
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
		<c:forEach items="${infolist }" var="info" varStatus="i">
			<div class="row">
				<h4><input name="information" type="checkbox" value="${info.enName}_${id}" onchange="selectAll('${info.enName}_${id}')" />${info.cnName}</h4>
			</div>
			<div class="row">
			<c:forEach items="${info.dicList }" var="dic" varStatus="i">
					<label class="col-lg-2">
						<input name="${info.enName}_${id}" class="con" type="checkbox" 
							onchange="selectOne('${dic.cCnEnglish}',${id},${dic.cId})" 
							id="${dic.cCnEnglish}_${id}" value="${dic.cCnName}_${id}"  />${dic.cCnName}</label>				
			</c:forEach>
			</div>
		</c:forEach>
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


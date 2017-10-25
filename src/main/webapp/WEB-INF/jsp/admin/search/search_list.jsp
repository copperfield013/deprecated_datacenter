<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>


<div id="search_list">
	<div class="page-header">
		<div class="header-title">
			<h1>人口查询</h1>
		</div>
	</div>

	<!--  这里加入一个搜索框 下面加入个区间选择克隆对象？-->
	<nav>
		<div class="form-inline">
			<div class="form-group">
				<label for="search">姓名：</label> <input type="text"
					class="form-control search" id="name" name="name" />
			</div>
			<div class="form-group">
				<label for="search">身份证号：</label> <input type="text"
					class="form-control search" id="idCode" name="idCode" />
			</div>
			<div class="form-group">
				<label for="search">地址：</label> <input type="text"
					class="form-control search" id="address" name="address" />
			</div>
			<div class="form-group">
				<label for="search">内容：</label> <input type="text"
					class="form-control search" id="content" name="content" />
			</div>
			<button type="button" class="btn btn-default" id="smartSubmit">查询</button>
		</div>
	</nav>



	<div class="page-body">
		<div class="row list-area">
		<table class="table">
			<thead>
				<tr>
					<th>序号</th>
					<th>姓名</th>
					<th>身份证号</th>
					<th>地址</th>
					<th>内容</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="aaa">
				
			</tbody>
		</table>
	</div>
	</div>
</div>

<script>
seajs.use(['utils','ajax'], function(Utils,Ajax){
	var $page = $('#search_list');
	//var sFocus = Utils.focus($('.search', $page));
	 /**
	 * 人口查询
	 * *
	 * **/	
	$('.search').keyup(function(event) {
		if (event.keyCode > "40" || event.keyCode == "32"|| event.keyCode == "8") {
			Ajax.ajax('admin/search/peopleSearch',
					{name : $("#name").val(),
					 idCode:$("#idCode").val(),
					 address:$("#address").val(),
					 content:$("#content").val()
					},
					function(json) {	
						var cont ="";
						console.log(json)
						for(i=0;i<json.length;i++){
							cont += "<tr><td>" + (i+1) + "</td>"
										+"<td>" + json[i].name + "</td>"
										+"<td>" + json[i].idCode + "</td>"
										+"<td>" + json[i].address + "</td>"
										+"<td>" + json[i].content + "</td>"
										+"<td><a href='admin/people/list' class='tab' target='' title='修改'>修改</a>&nbsp;"
											+"<a  href='admin/people/list' confirm='确认删除？'>删除</a></td></tr>";
						}
						$("#aaa").html(cont);
					});
		}
	});

	
});
	

</script>
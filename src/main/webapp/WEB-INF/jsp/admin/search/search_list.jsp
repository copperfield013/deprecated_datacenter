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
		<a class='tab' href='admin/search/detail/1' target='search_detail_1' title='详情'>xxx</a>
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
		<div id="page">
		</div>
	</div>
	</div>
</div>

<script>
seajs.use(['utils','ajax','paging'], function(Utils,Ajax,Paging){
	var $page = $('#search_list');
	//var sFocus = Utils.focus($('.search', $page));
	 /**
	 * 人口查询
	 * *
	 * **/
	 var goPageNum = 1;
	$('.search').keyup(function(event) {
		if (event.keyCode > "40" || event.keyCode == "32"|| event.keyCode == "8") {
			search(1,0);
		}
	});
	 
	search = function (pageNo,index){
		Ajax.ajax('admin/search/peopleSearch',
				{name : $("#name").val(),
				 idCode:$("#idCode").val(),
				 address:$("#address").val(),
				 content:$("#content").val(),
				 pageNo:pageNo
				},
				function(json) {	
					var cont ="";
					console.log(json)
					var people = json.people
					var pageInfo = json.page
					for(i=0;i<people.length;i++){
						cont += "<tr><td>" + (index+i+1) + "</td>"
									+"<td><a class='tab' href='admin/search/detail/"+people[i].id+"' target='search_detail_"+people[i].id+"' title='详情'>" + people[i].name + "</a></td>"
									+"<td>" + people[i].idCode + "</td>"
									+"<td>" + people[i].address + "</td>"
									+"<td>" + people[i].content.substr(0,50) + "</td>"
									+"<td><a href='admin/search/detail/"+people[i].id+"' class='tab' target='' title='修改'>修改</a>&nbsp;"
										+"<a  href='admin/search/do_delete/"+people[i].id+"' confirm='确认删除？'>删除</a></td></tr>";
					}
					$("#aaa").html(cont);
					goPage(pageInfo);
				});
	}
	 
	goPage = function(pageInfo){
	    var count = pageInfo.count;//总条数
	    var totalPage = 0;//总页数
	    var pageSize = pageInfo.pageSize;//每页显示行数
	    //总共分几页 
	    if(count/pageSize > parseInt(count/pageSize)){   
	            totalPage=parseInt(count/pageSize)+1;   
	       }else{   
	          totalPage=parseInt(count/pageSize);   
	       }   
	    var currentPage = pageInfo.pageNo;//当前页数
	    var firstIndex = pageInfo.firstIndex;
	    var endIndex = pageInfo.endIndex;
	    var tempStr = "共"+count+"条记录 分"+totalPage+"页 当前第"+currentPage+"页";
	    console.log(currentPage)
	    var beforePage = currentPage-1;
	    var nextPage = currentPage+1;
	    var beforeIndex = firstIndex-10;
	    var nextIndex = endIndex+1;
	    if(currentPage==totalPage){
	    	nextPage = totalPage;
	    	nextIndex = firstIndex;
	    }
	    	
	   
	    
	    if(currentPage>1){
	        tempStr += "<button type='button' class='btn btn-default' onClick='search(1,0)'>首页</button>";
	        tempStr += "<button type='button' class='btn btn-default' onClick='search("+beforePage+","+(firstIndex-10)+")'>上一页</button>"
	    }else{
	        tempStr += "<button type='button' class='btn btn-default' onClick=''>首页</button>";
	        tempStr += "<button type='button' class='btn btn-default' onClick=''>上一页</button>";    
	    }

	    if(currentPage<totalPage){
	        tempStr += "<button type='button' class='btn btn-default' onClick='search("+nextPage+","+(endIndex+1)+")'>下一页</button>";
	        tempStr += "<button type='button' class='btn btn-default' onClick='search("+totalPage+","+((totalPage-1)*pageSize)+")'>尾页</button>";
	    }else{
	        tempStr += "<button type='button' class='btn btn-default' onClick=''>下一页</button>";
	        tempStr += "<button type='button' class='btn btn-default' onClick=''>尾页</button>";    
	    }
	    tempStr += "<input type='text' class='cpf-paginator-jump-text' id='go' value='"+goPageNum+"'>";
	    tempStr +="<button type='button' class='cpf-paginator-jump-button btn-default btn' onClick='go("+pageSize+")'>go</button>"
	    $("#page").html(tempStr);
	    
	}
	
	go = function(pageSize){
		goPageNum = $("#go").val();
		search($("#go").val(),($("#go").val()-1)*pageSize)
	}
	
	
});

</script>
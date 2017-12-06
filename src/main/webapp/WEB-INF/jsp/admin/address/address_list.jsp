<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<style>
	#address-list {
		padding-right: 20px;
	}
	#address-list a.opera {
		color: #126def;
	}
	#address-list .pagination>li>a, .pager>li>a {
		color: #656565;
	}
	#address-list .pagination>li.active>a, .pager>li.active>a {
		color: #fff;
	}
	#address-list a:hover {
		color: #4b90f3;
	}
	#address-list a:active {
		color: #0f5acf;
	}
	#address-list .address_detail_hover {
		color: #656565;
	}
	#address-list .address_detail_hover:hover {
		color: #656565;
		cursor: pointer;
	}
	#address-list .table tbody > tr:hover .address_detail_hover{
		color: #126def;
	}
	#address-list .cpf-paginator {
		margin: 40px 0 50px 0;		
	}
</style>
<div id="address-list">
	<h1 class="zpage-title">地址信息</h1>
	<div class="operation-bar">
		<form action="admin/address/list">
			<div class="operation-search">
				<label class="search-label" for="addressStr">地址名称</label>
				<span class="colon">:</span>
				<div class="search-warp">
					<input type="text" class="search-input" name="addressStr" value="${addressStr }" placeholder="请输入地址"/>
					<span class="search-button">查询</span>
				</div>
			</div>
			
			<div class="margin-left-20">
				<a class="operation-btn" href="admin/address/add" title="添加地址" target="address_add" >添加</a>
				<a class="operation-btn" href="admin/address/import" title="导入地址" target="address_import" >导入</a>
			</div>
		</form>
	</div>
	<div class="list-area">
		<table class="table">
			<thead>
				<tr>
					<th class="index">序号</th>
					<th class="normal">地址名称</th>
					<!-- <th>地点名/楼栋名</th>
					<th>后址</th> -->
					<!-- <th>地址分词地址</th>
					<th>人工分词</th> -->
					<th class="long">地址解析</th>
					<th class="short">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="address" varStatus="i">
					<tr data-name="${address.name }">
						<td>${i.index + 1 }</td>
						<td><span class="address_detail_hover" href="javascript:;" target="address_detail_${address.name }">${address.name }</span></td>
						<%-- <td><a class="tab" href="admin/address/detail?addressStr=${address.name}" target="address_detail_${address.name }" title="详情-${address.name }" >${address.name }</a></td> --%>
						<%-- <td>${address.keyPoint }</td>
						<td>${address.laterPart }</td> --%>
						<%-- <td>${address.splitName }</td>
						<td>${address.artificialSplitName }</td> --%>
						<c:choose>
							<c:when test="${ not empty address.artificialSplitName }">
								<td>${address.artificialSplitNameToShow }</td>
							</c:when>
							<c:otherwise>
								<td>${address.splitNameToShow }</td>
							</c:otherwise>
						</c:choose>
						<td>
							<a class="tab opera" href="admin/address/edit/?addressStr=${address.name }" target="address_edit_${address.name }" title="修改-${address.name }">修改</a>
							<a class="tab opera" href="admin/address/categoryList?addressStr=${address.name }" target="address_category_list" title="同类-${address.name }">查看相同</a>
							<a class="opera" href="admin/address/delete?addressStr=${address.name }" confirm="确认删除？">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
	</div>
<script type="text/x-jquery-tmpl" id="address-detail">
	<div style="z-index:999">
		<div class="page-header">
			<div class="header-title">
				<h1>地址详情</h1>
			</div>
		</div>
		<div class="page-body">
			<div class="row">
				<label class="col-lg-2">地址名称</label>
				<div class="col-lg-6">\${addressStr }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">行政区域</label>
				<div class="col-lg-6">\${position }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">地点名/楼栋名</label>
				<div class="col-lg-6">\${keyPoint }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">后址</label>
				<div class="col-lg-6">\${laterPart }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">系统分词</label>
				<div class="col-lg-6">\${splitName }</div>
			</div>
			<div class="row">
				<label class="col-lg-2">人工分词</label>
				<div class="col-lg-6">\${artificialSplitName }</div>
			</div>
		</div>
	</div>
</script>
</div>
<script>
	seajs.use(['ajax','utils'], function(Ajax,Utils){
		var $page = $('#address-list');
		
		$(".address_detail_hover").click(function(){
			var addressName = $(this).closest('tr[data-name]').attr('data-name');
			console.log(addressName);
			var $this = $(this);
			var page = $(this).getLocatePage();
			console.log(page);
			Ajax.ajax("admin/address/detail", {
				addressStr : addressName
			},function(data){
				console.log("succsess data:" + data);
				$("#address-detail").tmpl(data).appendTo($this.closest('td'));
			});
		});
	});
</script>
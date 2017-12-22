<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>

<div id="address-list" class="zpage">
	<h1 class="zpage-title">地址信息</h1>
	<div class="operation-bar">
		<form id="address-search-form" action="admin/address/list">
			<div class="operation-search">
				<label class="search-label" for="addressStr">地址名称</label>
				<span class="colon">:</span>
				<div class="search-wrap">
					<input type="text" class="search-input" name="addressStr" value="${addressStr }" placeholder="请输入地址"/>
					<span id="search-button" class="search-button">查询</span>
				</div>
			</div>			
			<a class="operation-btn tab margin-l15" href="admin/address/add" title="添加地址" target="address_add" >添加</a>
			<a class="operation-btn tab margin-l15" href="admin/address/import" title="导入地址" target="address_import">导入</a>
		</form>
	</div>
	<div class="list-area">
		<table class="table">
			<thead>
				<tr>
					<th class="index">序号</th>
					<th class="normal td-tleft">地址名称</th>
					<!-- <th>地点名/楼栋名</th>
					<th>后址</th> -->
					<!-- <th>地址分词地址</th>
					<th>人工分词</th> -->
					<th class="long td-tleft">地址解析</th>
					<th class="short">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="address" varStatus="i">
					<tr data-name="${address.name }">
						<td>${i.index + 1 }</td>
						<td class="td-tleft"><span class="td-list-hover">${address.name }</span></td>
						<%-- <td><a class="tab" href="admin/address/detail?addressStr=${address.name}" target="address_detail_${address.name }" title="详情-${address.name }" >${address.name }</a></td> --%>
						<%-- <td>${address.keyPoint }</td>
						<td>${address.laterPart }</td> --%>
						<%-- <td>${address.splitName }</td>
						<td>${address.artificialSplitName }</td> --%>
						<c:choose>
							<c:when test="${ not empty address.artificialSplitName }">
								<td class="td-tleft">${address.artificialSplitNameToShow }</td>
							</c:when>
							<c:otherwise>
								<td class="td-tleft">${address.splitNameToShow }</td>
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
		<div class="cpf-paginator margin-t40 margin-b40" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
	</div>
	
<script type="text/x-jquery-tmpl" class="address-detail">
	<div class="pane-card">
		<span class="pane-close">×</span>
		<h1 class="pane-title">地址详情</h1>
		<div class="pane-item">
			<span class="pane-item-label">地址名称</span>
			<span class="pane-item-colon">:</span>
			<span class="pane-item-text">\${addressStr }</span>
		</div>
		<div class="pane-item">
			<span class="pane-item-label">行政区域</span>
			<span class="pane-item-colon">:</span>
			<span class="pane-item-text">\${position }</span>
		</div>
		<div class="pane-item">
			<span class="pane-item-label">地点名/楼栋名</span>
			<span class="pane-item-colon">:</span>
			<span class="pane-item-text">\${keyPoint }</span>
		</div>
		<div class="pane-item">
			<span class="pane-item-label">后址</span>
			<span class="pane-item-colon">:</span>
			<span class="pane-item-text">\${laterPart }</span>
		</div>
		<div class="pane-item">
			<span class="pane-item-label">系统分词</span>
			<span class="pane-item-colon">:</span>
			<span class="pane-item-text">\${splitName }</span>
		</div>
		<div class="pane-item">
			<span class="pane-item-label">人工分词</span>
			<span class="pane-item-colon">:</span>
			<span class="pane-item-text">\${artificialSplitName }</span>
		</div>
	</div>
</script>
</div>
<script>
	seajs.use(['ajax','utils'], function(Ajax,Utils){
		var $page = $('#address-list');		
		
		$("#search-button", $page).click(function(){
			$("#address-search-form", $page).submit();
		});
		
		$(".td-list-hover",$page).click(function(){
			var addressName = $(this).closest('tr[data-name]').attr('data-name');
			var $this = $(this);
			var page = $(this).getLocatePage();
			Ajax.ajax("admin/address/detail", {
				addressStr : addressName
			},function(data){
				var trHeight = $this.closest('tr').height();
				var pageOffset = $page.offset();
				var trOffset = $this.closest('tr').offset();
				var trPosition = $this.closest('tr').position();
				var thisOffset = $this.offset();
				var THISELE = $(".address-detail").tmpl(data).appendTo($this.closest('#address-list'));
				var ELEHEIGHT = THISELE.outerHeight();
				var docHeight= $('body').height();
				var top;
				
				if( docHeight - thisOffset.top >= ELEHEIGHT || trPosition.top < ELEHEIGHT ){
					top = trOffset.top - pageOffset.top + trHeight -5;
					$('.pane-card').addClass('bottom');
				}else {
					top = trOffset.top - pageOffset.top - ELEHEIGHT + 5 ;
					$('.pane-card').addClass('top');
				}
				THISELE.css({
					top: top ,
					left: thisOffset.left - pageOffset.left + 20
				});
			});
		});
		
		$page.on('click','.pane-close',function(){
			$(this).closest('.pane-card').remove();
		})
		$page.on('click',function(){
			$('.pane-card').remove();
		})
	});
</script>
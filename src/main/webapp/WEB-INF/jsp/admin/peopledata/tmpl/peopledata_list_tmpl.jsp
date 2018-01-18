<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="peopledata-list-tmpl" class="detail">
	<div class="page-header">
		<div class="header-title">
			<h1>人口列表</h1>
		</div>
		<div class="header-buttons">
			<a title="切换模板" class="btn-toggle" href="page:#tmpl-list.toggle">
				<i class="iconfont icon-template"></i>
			</a>
			<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
				<i class="glyphicon glyphicon-refresh"></i>
			</a>
		</div>
	</div>
	<div class="page-body">
		<form action="admin/peopledata/tmpl/list">
			<input type="hidden" id="tmplId" name="tmplId" value="${ltmpl.id }" />
		</form>
		<%-- <nav>
			<form class="form-inline" action="admin/peopledata/tmpl/list">
				<input type="hidden" name="tmplId" value="${ltmpl.id }" />
				<button type="submit" class="btn btn-default">查询</button>
				<a class="btn btn-primary tab" href="admin/peopledata/add" title="创建人口" target="people_add" >创建</a>
				<a class="btn btn-primary tab" href="admin/peopledata/import" title="导入人口" target="people_import">导入</a>
				<a class="btn btn-primary tab" href="admin/peopledata/output" title="导出人口" target="people_output">导出</a>
				<a class="btn tab" href="admin/peopledata/model" title="字段管理模版" target="people_model">字段管理模版</a>
			</form>
		</nav> --%>
		<div class="row list-area">
			<table class="table">
				<thead>
					<tr>
						<c:forEach items="${ltmpl.columns }" var="column">
							<th>${column.title }</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${parserList }" var="parser" varStatus="i">
						<tr>
							<c:forEach items="${ltmpl.columns }" var="column" varStatus="j" >
								<td>
									<c:choose >
										<c:when test="${column.specialField == 'number' }">
											${i.index + 1 }
										</c:when>
										<c:when test="${column.specialField == 'operate-u' }">
											<a target="people_update_${parser['peopleCode'] }" 
												title="修改-${parser['name'] }" 
												href="admin/peopledata/update_tmpl/${parser['peopleCode'] }" 
												class="tab btn btn-info btn-xs edit">
												<i class="fa fa-edit"></i>修改
											</a>
										</c:when>
										<c:when test="${column.specialField == 'operate-r' }">
											<a confirm="确认删除？"
												href="admin/peopledata/do_delete/${parser['peopleCode'] }" 
												class="btn btn-danger btn-xs delete">
												<i class="fa fa-trash-o"></i>删除
											</a>
										</c:when>
										<c:when test="${column.specialField == 'operate-u-r' }">
											<a target="people_update_${parser['peopleCode'] }" 
												title="修改-${parser['name'] }" 
												href="admin/peopledata/update_tmpl/${parser['peopleCode'] }" 
												class="tab btn btn-info btn-xs edit">
												<i class="fa fa-edit"></i>修改
											</a>
											<a confirm="确认删除？"
												href="admin/peopledata/do_delete/${parser['peopleCode'] }" 
												class="btn btn-danger btn-xs delete">
												<i class="fa fa-trash-o"></i>删除
											</a>
										</c:when>
										<c:otherwise>
											${parser[column.fieldKey] }
										</c:otherwise>
									</c:choose>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
		</div>
	</div>
	<div id="tmpl-list" class="detail-toggle-sublist blur-hidden" style="display: none;">
		<div class="detail-toggle-sublist-wrapper">
			<c:forEach items="${ltmplList }" var="ltmplItem">
				<a data-id="${ltmplItem.id }" class="${ltmplItem.id == ltmpl.id? 'active': '' }">
					<span class="detail-toggle-sublist-icon"><i class="fa fa-lightbulb-o"></i></span>
					<span class="detail-toggle-sublist-item-body">
						<span class="detail-toggle-sublist-item-name">${ltmplItem.title }</span>
						<span class="detail-toggle-sublist-item-date"><fmt:formatDate value="${ltmplItem.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </span>
					</span>
				</a>
			</c:forEach>
		</div>
		<div class="detail-toggle-sublist-operate">
			<a class="tab" title="配置模板" target="ltmpl_list" href="admin/tmpl/ltmpl/list">
				<i class="icon glyphicon glyphicon-cog"></i>
			</a>
		</div>
	</div>
</div>
<script>
	seajs.use(['utils'], function(Utils){
		var $page = $('#peopledata-list-tmpl');
		$('#tmpl-list a[data-id]:not(.active)').click(function(){
			var $this = $(this);
			$('#tmplId', $page).val($this.attr('data-id'));
			$('form', $page).submit();
		});
	});
</script>
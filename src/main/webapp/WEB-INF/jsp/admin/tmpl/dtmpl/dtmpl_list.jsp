<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<title>模板列表</title>
<div id="dtmpl-list" class="detail">
	<div>
		<form action="admin/tmpl/dtmpl/list">
			
		</form>
	</div>
	<div class="page-header">
		<div class="header-title">
			<h1>模板列表</h1>
		</div>
		<div class="header-buttons">
			<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
				<i class="glyphicon glyphicon-refresh"></i>
			</a>
			<a class="tab"  href="admin/tmpl/dtmpl/to_create/${module }" title="创建模板" target="create_dtmpl">
				<i class="fa fa-plus"></i>
			</a>
		</div>
	</div>
	<div class="page-body" style="font-size: 15px;">
		<div class="col-lg-offset-1 col-lg-10">
			<table class="table">
				<thead>
					<tr>
						<th>#</th>
						<th>模板名</th>
						<th>更新时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${tmplList }" var="tmpl" varStatus="i" >
						<tr>
							<td>${i.index + 1 }</td>
							<td>${tmpl.title }</td>
							<td><fmt:formatDate value="${tmpl.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
							<td>
								<a target="dtmpl_update_${tmpl.id }" href="admin/tmpl/dtmpl/update/${tmpl.id }" class="tab btn btn-info btn-xs edit">
									<i class="fa fa-edit"></i>
									修改
								</a>
								<a confirm="确认删除模板(${tmpl.title })?" href="admin/tmpl/dtmpl/remove/${tmpl.id }" class="btn btn-danger btn-xs delete">
									<i class="fa fa-trash-o"></i>
									删除
								</a>
								<c:choose>
									<c:when test="${tmpl.id != defaultTemplate.id }">
										<a href="admin/tmpl/dtmpl/as_default/${tmpl.id }" class="btn btn-warning btn-xs">
											<i class="glyphicon glyphicon-star"></i>
											设为默认
										</a>
									</c:when>
									<c:otherwise>
										<a disabled="disabled" class="btn btn-xs">
											<i class="glyphicon glyphicon-star"></i>
											默认模板
										</a>
										
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
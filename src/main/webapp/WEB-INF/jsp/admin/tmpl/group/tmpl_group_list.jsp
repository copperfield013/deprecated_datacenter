<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<title>模板组合管理（${module.title }）</title>
<div id="tmplgroup-${module.key }" class="detail">
	<div>
		<form action="admin/tmpl/group/list">
			
		</form>
	</div>
	<div class="page-header">
		<div class="header-title">
			<h1>模板组合管理（${module.title }）</h1>
		</div>
		<div class="header-buttons">
			<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
				<i class="glyphicon glyphicon-refresh"></i>
			</a>
			<a class="tab"  href="admin/tmpl/group/to_create/${module.key }" title="创建模板" target="create_tmpl_group">
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
						<th>组合名称</th>
						<th>列表模板</th>
						<th>详情模板</th>
						<th>Key</th>
						<th>最近更新时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${tmplGroups }" var="group" varStatus="i" >
						<tr>
							<td>${i.index + 1 }</td>
							<td>${group.title }</td>
							<td><a class="dialog" target="dialog_ltmpl_update_${group.listTemplateId }" href="admin/tmpl/ltmpl/update/${group.listTemplateId }">${group.listTemplateTitle }</a></td>
							<td><a class="dialog" target="dialog_dtmpl_update_${group.detailTemplateId }" href="admin/tmpl/dtmpl/update/${group.detailTemplateId }">${group.detailTemplateTitle }</a></td>
							<td>${group.key }</td>
							<td><fmt:formatDate value="${group.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
							<td>
								<a target="tmpl_group_update_${group.id }" href="admin/tmpl/group/update/${group.id }" class="tab btn btn-info btn-xs edit">
									<i class="fa fa-edit"></i>
									修改
								</a>
								<a confirm="确认删除模板组合(${group.title })?" href="admin/tmpl/group/remove/${group.id }" class="btn btn-danger btn-xs delete">
									<i class="fa fa-trash-o"></i>
									删除
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
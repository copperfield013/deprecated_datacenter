<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<title>列表模板（${module.title }）</title>
<div id="tmpl-ltmpl-list-${module.name }" class="detail">
	<div>
		<form action="admin/tmpl/ltmpl/list/${module.name }">
			
		</form>
	</div>
	<div class="page-header">
		<div class="header-title">
			<h1>列表模板（${module.title }）</h1>
		</div>
		<div class="header-buttons">
			<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
				<i class="glyphicon glyphicon-refresh"></i>
			</a>
			<a class="tab"  href="admin/tmpl/ltmpl/add/${module.name }" title="创建模板" target="add_ltmpl_${module.name }">
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
					<c:forEach items="${ltmplList }" var="ltmpl" varStatus="i" >
						<tr>
							<td>${i.index + 1 }</td>
							<td>${ltmpl.title }</td>
							<td><fmt:formatDate value="${ltmpl.updateTime }" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
							<td>
								<a target="viewtmpl_update_${ltmpl.id }" href="admin/tmpl/ltmpl/update/${ltmpl.id }" class="tab btn btn-info btn-xs edit">
									<i class="fa fa-edit"></i>
									修改
								</a>
								<c:choose>
									<c:when test="${empty relatedGroupsMap[ltmpl.id] }">
										<a confirm="确认删除模板(${ltmpl.title })?" href="admin/tmpl/ltmpl/remove/${ltmpl.id }" class="btn btn-danger btn-xs delete">
											<i class="fa fa-trash-o"></i>
											删除
										</a>
									</c:when>
									<c:otherwise>
										<a title="查看绑定的所有模板组合" href="admin/tmpl/ltmpl/group_list/${ltmpl.id }"
											target="dtmpl_group_list_${tmpl.id }" 
											class="tab btn btn-success btn-xs">
											<i class="fa fa-th-list"></i>
											模板组合
										</a>
										<a href="javascript:STATICS.TMPL.switchTemplateGroup('ltmpl', '${module.name }', ${ltmpl.id });" 
											class="btn btn-warning btn-xs "
											title="为所有已经绑定到当前列表模板的组合重新指定一个列表模板">
											<i class="fa fa-exchange"></i>
											解绑
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
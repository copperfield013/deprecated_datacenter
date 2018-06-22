<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-desktop"></i>
		<span class="menu-text">系统配置</span>
	</a>
	<ul class="submenu">
		<li>
			<a class="tab" href="admin/config/sidemenu" target="sidemenu" title="功能列表管理">
	   			<span class="menu-text">功能列表管理</span>
	   		</a>
		</li>
	</ul>
</li>
<c:forEach var="menu" items="${menus }">
	<li>
		<a href="#" class="menu-dropdown">
		    <i class="menu-icon fa fa-bookmark"></i>
		    <span class="menu-text">${menu.title }</span>
		</a>
		<ul class="submenu">
			<c:forEach var="level2" items="${menu.level2s }">
				<li>
					<c:choose>
						<c:when test="${level2.isDefault == 1 }">
					   		<a class="tab" href="admin/modules/curd/list/${level2.templateModule }" target="${level2.templateModule }_list_tmpl" title="${group.title }">
					   			<span class="menu-text">${level2.title }</span>
					   		</a>
						</c:when>
						<c:otherwise>
					   		<a class="tab" href="admin/modules/curd/list/${level2.templateModule }?tg=${level2.templateGroupKey}" 
					   			target="${moduleName }_list_tmpl_${level2.templateGroupKey}" title="${level2.title }">
					   			<span class="menu-text">${level2.title }</span>
					   		</a>
						</c:otherwise>
					</c:choose>
			   	</li>
			</c:forEach>
		</ul>
	</li>
</c:forEach>

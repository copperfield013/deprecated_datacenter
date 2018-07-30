<%@page import="cn.sowell.copframe.spring.properties.PropertyPlaceholder"%>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<sec:authorize access="hasAuthority('${configAuth }')">
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
</sec:authorize>

<c:forEach var="menu" items="${menus }">
	<li>
		<a href="#" class="menu-dropdown">
		    <i class="menu-icon fa fa-bookmark"></i>
		    <span class="menu-text">${menu.title }</span>
		</a>
		<ul class="submenu">
			<c:forEach var="level2" items="${menu.level2s }">
				<li>
					<a class="tab" href="admin/modules/curd/list/${level2.id }" 
			   			target="entity_list_${level2.id }" title="${level2.title }">
			   			<span class="menu-text">${level2.title }</span>
			   		</a>
			   	</li>
			</c:forEach>
		</ul>
	</li>
</c:forEach>
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-bookmark"></i>
		<span class="menu-text">demo</span>
		<i></i>
	</a>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/treeview/treeView?id=2adba5c2ab424be69cedd0f52a776631&mappingName=角色权限" target="treeview" title="树形控件">
	   			<span class="menu-text">树形控件</span>
	   		</a>
	   	</li>
	</ul>
</li>

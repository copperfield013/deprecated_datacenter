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
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-cogs"></i>
		<span class="menu-text">模板管理</span>
		<i></i>
	</a>
	<ul class="submenu">
		<c:forEach var="module" items="${modules }">
			<c:set var="moduleName" value="${module.configModule.name }" />
		   	<li>
		   		<a class="tab" href="admin/tmpl/dtmpl/list/${moduleName }" target="${moduleName }_dtmpl_list" title="人口详情模板">
		   			<span class="menu-text">${module.configModule.title }详情模板</span>
		   		</a>
		   	</li>
		   	<li>
		   		<a class="tab" href="admin/tmpl/ltmpl/list/${moduleName }" target="${moduleName }_ltmpl_list" title="${module.configModule.title }列表模板">
		   			<span class="menu-text">${module.configModule.title }列表模板</span>
		   		</a>
		   	</li>
		   	<li>
		   		<a class="tab" href="admin/tmpl/group/list/${moduleName }" target="${moduleName }_tmpl_group_list" title="${module.configModule.title }模板组合">
		   			<span class="menu-text">${module.configModule.title }模板组合</span>
		   		</a>
		   	</li>
		</c:forEach>
	</ul>
</li>
<c:forEach var="module" items="${modules }">
	<c:set var="moduleName" value="${module.configModule.name }" />
	<c:set var="moduleTitle" value="${empty module.title ? module.configModule.title: module.title }" />
	<li>
		<a href="#" class="menu-dropdown">
		    <i class="menu-icon fa fa-bookmark"></i>
		    <span class="menu-text">${moduleTitle }</span>
		</a>
		<ul class="submenu">
			<c:forEach var="group" items="${module.groups }">
				<li>
					<c:choose>
						<c:when test="${group.isDefault == 1 }">
					   		<a class="tab" href="admin/modules/curd/list/${moduleName }" target="${moduleName }_list_tmpl" title="${group.title }">
					   			<span class="menu-text">${group.title }</span>
					   		</a>
						</c:when>
						<c:otherwise>
					   		<a class="tab" href="admin/modules/curd/list/${moduleName }?tg=${group.templateGroup.key}" 
					   			target="${moduleName }_list_tmpl_${group.templateGroup.key}" title="${group.title }">
					   			<span class="menu-text">${group.title }</span>
					   		</a>
						</c:otherwise>
					</c:choose>
			   	</li>
			</c:forEach>
		</ul>
	</li>
</c:forEach>

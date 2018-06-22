<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link type="text/css" rel="stylesheet" href="media/admin/config/css/sidemenu-main.css" />
<div id="${RES_STAMP }" class="sidemenu-main detail">
	<div class="page-header">
		<div class="header-title">
			<h1>功能列表管理</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="col-lg-7">
			<div class="widget radius-bordered modules-container">
				<div class="widget-header bordered-left bordered-blueberry separated">
					<span class="widget-caption">功能列表</span>
					<div class="widget-buttons">
						<a href="#" title="保存" style="display: none"><i id="save" class="fa fa-check-square"></i></a>
						<a href="#" title="添加一级菜单"><i id="add-level1" class="fa fa-plus-square"></i></a>
					</div>
				</div>
				<div class="widget-body menu-container">
					<ol id="level1-list" class="dd-list">
						<c:forEach items="${menus }" var="menu">
							<li class="dd-item" data-id="${menu.id }">
								<div class="dd-handle">
									<span class="level1-title">${menu.title }</span>
									<span class="level-operate">
										<a href="#"><i class="del-level fa fa-trash-o"></i></a>
									</span>
								</div>
								<ol class="dd-list">
									<c:if test="${!empty menu.level2s }">
										<c:forEach items="${menu.level2s }" var="level2">
											<li class="dd-item" data-id="${level2.id }" group-id="${level2.isDefault == 1? 0: level2.templateGroupId }">
												<div class="dd-handle">
													<span class="level2-title">${level2.title }</span>
													<span class="level-operate">
														<a href="#"><i class="del-level fa fa-trash-o"></i></a>
													</span>
													<span class="tip-level-title">${level2.templateModuleTitle}-${level2.isDefault == 1? '默认': level2.templateGroupTitle }</span>
												</div>
											</li>
										</c:forEach>
									</c:if>
								</ol>
							</li>
						</c:forEach>
					</ol>
				</div>
			</div>
		</div>
		<div class="col-lg-5">
			<div class="widget radius-bordered mds-container">
				<div class="widget-header bordered-left bordered-palegreen separated">
					<span class="widget-caption">模块列表</span>
				</div>
				<div class="widget-body">
					<ol class="dd-list">
						<c:forEach items="${modules }" var="module">
							<li class="dd-item module-wrapper" module-name="${module.name }">
								<div class="dd-handle">
									<span class="module-title">${module.title }</span>
									<span class="level-operate">
										<a title="列表模板" href="admin/tmpl/ltmpl/list/${module.name }" class="tab" target="${module.name }_ltmpl_list"><i class="iconfont icon-tools"></i></a>
										<a title="详情模板" href="admin/tmpl/dtmpl/list/${module.name }" class="tab" target="${module.name }_dtmpl_list"><i class="iconfont icon-detail"></i></a>
										<a title="模板组合" href="admin/tmpl/group/list/${module.name }" class="tab" target="${module.name }_tmpl_group_list"><i class="iconfont icon-group"></i></a>
									</span>
								</div>
								<ol class="dd-list">
									<c:forEach items="${tmplGroupsMap[module.name] }" var="group">
										<li>
											<div class="dd-handle md-group-title" group-id="${group.id }">
												<span>${group.title }</span>
											</div>
										</li>
									</c:forEach>
								</ol>
							</li>
						</c:forEach>
					</ol>
				</div>
		</div>
	</div>
	<script type="jquery/tmpl" id="level1-item-tmpl">
		<li class="dd-item">
			<div class="dd-handle">
				<span class="level1-title"><input type="text" value="" /></span>
				<span class="level-operate">
					<a href="#"><i class="del-level fa fa-trash-o"></i></a>
				</span>
				<span class="tip-level-title"></span>
			</div>
			<ol class="dd-list"></ol>
		</li>
	</script>
	<script type="jquery/tmpl" id="level2-item-tmpl">
		<li class="dd-item">
			<div class="dd-handle">
				<span class="level2-title"><input type="text" value="\${level2Title}" /></span>
				<span class="level-operate">
					<a href="#"><i class="del-level fa fa-trash-o"></i></a>
				</span>	
				<span class="tip-level-title">\${moduleTitle}-\${tmplGroupTitle}</span>
			</div>
		</li>
	</script>
</div>
<script type="text/javascript">
	seajs.use(['config/js/sidemenu-main'], function(S){
		var $page = $('#${RES_STAMP }.sidemenu-main');
		try{
			var config = $.parseJSON('${config}');
			S.init($page, config);
		}catch(e){}
	});
</script>
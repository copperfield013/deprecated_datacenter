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
		<div class="widget radius-bordered modules-container">
			<div class="widget-header bg-blue">
				<span class="widget-caption">功能列表</span>
				<div class="widget-buttons">
					<a href="#" title="保存" style="display: none"><i id="save" class="fa fa-check-square"></i></a>
					<a href="#" title="添加一级菜单"><i id="add-level1" class="fa fa-plus-square"></i></a>
				</div>
			</div>
			<div class="widget-body">
				<ol id="level1-list" class="dd-list">
					<c:forEach items="${modules }" var="module">
						<li class="dd-item" data-id="${module.id }" module-name="${module.configModule.name }">
							<div class="dd-handle">
								<span class="level1-title">${module.title }</span>
								<span class="level-operate">
									<a href="#"><i class="del-level fa fa-trash-o"></i></a>
									<a href="#"><i class="add-level2 fa fa-plus-square-o"></i></a>
								</span>
								<span class="tip-level-title">${module.configModule.title }</span>
							</div>
							<ol class="dd-list">
								<c:if test="${!empty module.groups }">
									<c:forEach items="${module.groups }" var="group">
										<li class="dd-item" data-id="${group.id }" group-id="${group.isDefault == 1? 0: group.templateGroupId }">
											<div class="dd-handle">
												<span class="level2-title">${group.title }</span>
												<span class="level-operate">
													<a href="#"><i class="del-level fa fa-trash-o"></i></a>
												</span>
												<span class="tip-level-title">${group.isDefault == 1? '默认': group.templateGroup.title }</span>
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
	<script type="jquery/tmpl" id="level1-item-tmpl">
		<li class="dd-item">
			<div class="dd-handle">
				<span class="level1-title"><input type="text" value="人口" /></span>
				<span class="level-operate">
					<a href="#"><i class="del-level fa fa-trash-o"></i></a>
					<a href="#"><i class="save-level1 fa fa-check"></i></a>
				</span>
				<span class="tip-level-title"></span>
			</div>
			<ol class="dd-list"></ol>
		</li>
	</script>
	<script type="jquery/tmpl" id="level2-item-tmpl">
		<li class="dd-item">
			<div class="dd-handle">
				<span class="level2-title"><input type="text" /></span>
				<span class="level-operate">
					<a href="#"><i class="del-level fa fa-trash-o"></i></a>
					<a href="#"><i class="save-level2 fa fa-check"></i></a>
				</span>	
				<span class="tip-level-title"></span>
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
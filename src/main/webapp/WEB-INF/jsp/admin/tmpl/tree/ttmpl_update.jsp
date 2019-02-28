<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<c:set var="title">
	<c:choose>
		<c:when test="${ttmpl != null }">修改${module.title }树形模板-${ttmpl.title }</c:when>
		<c:otherwise>创建${module.title }树形模板</c:otherwise>
	</c:choose>
</c:set>
<title>${title }</title>
<div class="ttmpl-update" id="ttmpl-update-${RES_STAMP }">
	<script type="text/json" id="config-structure-json">${configStructureJson}</script>
	<div class="float-operate-area">
		<div class="operate-area-cover"></div>
		<a id="save" class="btn-save" title="保存"><i class="fa fa-check-square"></i></a>
	</div>
	<div class="detail">
		<div class="page-header">
			<div class="header-title">
				<h1>${title }</h1>
			</div>
			<div class="header-buttons">
				<a class="refresh" title="刷新" id="refresh-toggler" href="page:refresh">
					<i class="glyphicon glyphicon-refresh"></i>
				</a>
			</div>
		</div>
		<div class="page-body">
			<div class="row header-row">
				<div class="col-lg-10 col-lg-offset-1">
					<input type="text" class="form-control" id="tmplTitle" placeholder="请输入模板名称" value="${tmpl.title }">
				</div>
			</div>
			<div class="row">
				<div class="col-lg-10 col-lg-offset-1">
					<div class="widget">
						<div class="widget-header bordered-bottom bordered-blue">
							<span class="widget-caption">全局配置</span>
						</div>
						<!--Widget Header-->
						<div class="widget-body">
							<div class="form-group">
								<label>默认节点颜色</label>
								<div>
									<input type="text" id="wheel-demo" class="form-control colorpicker" data-control="wheel" value="#5db2ff">
								</div>
							</div>

						</div>
					</div>
					<div class="widget">
						<div class="widget-header bordered-bottom bordered-blue">
							<span class="widget-caption">节点配置</span>
							<div class="widget-buttons">
								<span class="input-icon">
									<select class="form-control input-xs">
										<option>人口</option>
										<option>家庭</option>
									</select>
								</span>
							</div>
						</div>
						<div class="widget-body">
							<div class="widget">
								<div class="widget-header bordered-bottom bordered-blue">
									<span class="widget-caption">人口</span>
								</div>
								<div class="widget-body">
									<div class="form-group">
										<label>选择器</label>
										<div>
											<input type="text" class="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label>节点文本</label>
										<div>
											<div class="node-text"></div>
										</div>
									</div>
									<div class="form-group">
										<label>可选关系</label>
										<div>
											<div class="seletable-relations">
												<div>关系1</div>
												<div>关系2</div>
											</div>
											<div class="relation-filter">
												<div class="relation-fieldsearch"></div>
												<div class="relation-criterias">
													<div>关系字段1</div>
													<div>关系字段2</div>
												</div>
												<div class="relation-criteria-detail">
													<div id="relation-label-row">
														<label>关系名</label>
														<div id="relation-label-value-wrap">
														</div>
													</div>
													<div id="criteria-detail-field-input-type-row">
														<label>控件</label>
														<div>
															<select id="field-input-type">
																<option value="text">文本框</option>
																<option value="select">单选下拉框</option>
															</select>
														</div>
													</div>
													<div>
														<label>关系</label>
														<div>
															<select id="criteria-detail-comparator">
																<option value="s1">包含</option>
																<option value="s2">开头为</option>
																<option value="s3">结尾为</option>
																<option value="s4">等于</option>
															</select>
														</div>
													</div>
													<div>
														<label id="default-value-label">值</label>
														<div id="criteria-default-value-container">
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
		
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['tmpl/js/ttmpl-update.js', 'utils'], function(TreeTmplUpdate, Utils){
		try{
			var $page = $('#ttmpl-update-${RES_STAMP }');
			console.log($page);
			TreeTmplUpdate.init({
				$page	: $page
			});
		}catch(e){
			console.error(e);
		}
	});
</script>
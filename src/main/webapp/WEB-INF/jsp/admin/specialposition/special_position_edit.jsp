<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link href="media/admin/selectiontest/selectiontest.css" rel="stylesheet" type="text/css" />
<script src="media/admin/selectiontest/selectiontest.js"></script>
<div id="special-position-edit" class="zpage">

	<h1 class="zpage-title">修改特殊地名信息</h1>
				<form class="bv-form  validate-form margin-t15" action="admin/special_position/doEdit">
					<input type="hidden" name="id" value="${specialPosition.id }"/>
					<%-- <div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="id">编码</label>
							<div class="col-lg-4">
								<input type="text" name="id" class="form-control" value="${specialPosition.id }" readonly="readonly"/>
							</div>
						</div>
					</div> --%>
					<div class="zform-group">
							<span class="zform-label">名称</span>
							<div class="form-group zform-item">
								<input type="text" name="name" class="form-control basic-input item-input" value="${specialPosition.name }"
									data-bv-notempty="true"
									data-bv-notempty-message="名称不能为空"/>
							</div>
					</div>
					<div class="zform-group">
							<span class="zform-label">通用名称</span>
							<div class="form-group zform-item">
								<input type="text" name="commonName" class="form-control basic-input item-input" value="${specialPosition.commonName }"/>
							</div>
					</div>
					<div class="zform-group">
						<div>
							<span class="zform-label">所属行政区域</span>
							<div class="form-group zform-item">
								<input type="text" name="belongPosition" class="form-control basic-input item-input" value="${specialPosition.belongPosition }"/>
							</div>
						</div>
					</div>
					<div class="zform-group">
							<span class="zform-label">级别</span>
							<div class="form-group zform-item">
								<select id="level" name="level" data-value="${specialPosition.level }">
									<c:forEach items="${levelNameMap }" var="levelName">
										<option value="${levelName.key }">${levelName.value }</option>
									</c:forEach>
								</select>
							</div>
					</div>
				

			        <input id="submit-btn" class="form-primary-button margin-l80 margin-t30" type="submit" value="提交" />

				</form>
</div>

<script>
	seajs.use(['ajax','utils'], function(Ajax, Utils){
		var $page = $('#special-position-edit');
		$("#level",$page).Selection({
			width: "180px"
		})
	});
	
	
</script>
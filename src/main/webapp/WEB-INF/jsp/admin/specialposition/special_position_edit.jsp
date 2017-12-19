<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>

<div id="special-position-edit">
	<div class="page-header">
		<div class="header-title">
			<h1>修改特殊地名信息</h1>
		</div>
	</div>


	<div class="page-body" id="clone">
		<div id="cloneInput"></div>
	</div >
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/special_position/doEdit">
					<input type="hidden" name="id" value="${specialPosition.id }"/>
					<%-- <div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="id">编码</label>
							<div class="col-lg-4">
								<input type="text" name="id" class="form-control" value="${specialPosition.id }" readonly="readonly"/>
							</div>
						</div>
					</div> --%>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">名称</label>
							<div class="col-lg-4">
								<input type="text" name="name" class="form-control" value="${specialPosition.name }"
									data-bv-notempty="true"
									data-bv-notempty-message="名称不能为空"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">通用名称</label>
							<div class="col-lg-4">
								<input type="text" name="commonName" class="form-control" value="${specialPosition.commonName }"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">所属行政区域</label>
							<div class="col-lg-4">
								<input type="text" name="belongPosition" class="form-control" value="${specialPosition.belongPosition }"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="level">级别</label>
							<div class="col-lg-4">
								<select id="level" name="level" data-value="${specialPosition.level }">
									<c:forEach items="${levelNameMap }" var="levelName">
										<option value="${levelName.key }">${levelName.value }</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				
					<div class="form-group">
			        	<div class="col-lg-offset-3 col-lg-3">
			        		<input id="submit-btn" class="btn btn-block btn-darkorange" type="submit" value="提交" />
				        </div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>
	seajs.use(['ajax','utils'], function(Ajax, Utils){
		var $page = $('#special-position-edit');
		
	});
	
	
</script>
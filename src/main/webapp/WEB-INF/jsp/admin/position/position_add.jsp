<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link href="media/admin/selectiontest/selectiontest.css" rel="stylesheet" type="text/css" />
<script src="media/admin/selectiontest/selectiontest.js"></script>

<div id="position-add" class="zpage">
	<h1 class="zpage-title">添加地点</h1>
				<form class="bv-form  validate-form margin-t15" action="admin/position/doAdd">
					<!-- <div class="form-group">
						<label class="col-lg-2 control-label" for="code">编码</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="code" />
						</div>
					</div> -->
					<div class="zform-group">
						<span class="zform-label">名称</span>
						<div class="form-group zform-item">
							<input type="text" class="form-control basic-input item-input" name="name" 
								data-bv-notempty="true"
								data-bv-notempty-message="名称不能为空"/>
						</div>
					</div>
					<div class="zform-group">
						<span class="zform-label" >别名</span>
						<div class="form-group zform-item">
							<input type="text" class="form-control basic-input item-input" name="alias" />
						</div>
					</div>
					<!-- <div class="form-group">
						<label class="col-lg-2 control-label" for="level">级别</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="level" />
						</div>
					</div> -->
					<div class="zform-group">
						<span class="zform-label" >级别</span>
						<div class="form-group zform-item">
							<select id="level" name="level">
								<c:forEach items="${levelNameMap }" var="levelName">
									<option value="${levelName.key }">${levelName.value }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					
			        <input class="form-primary-button margin-l80 margin-t30" type="submit" value="提交" />
				       
				</form>
</div>

<script type="text/javascript">
seajs.use(['dialog', 'utils'], function(Dialog, Utils){
	var $page = $("#position-add");
	
	$('#level',$page).Selection({
		width: "180px"
	})
})
</script>

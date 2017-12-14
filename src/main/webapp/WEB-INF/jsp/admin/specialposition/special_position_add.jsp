<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="special-position-add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加地点</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/special_position/doAdd">
					<!-- <div class="form-group">
						<label class="col-lg-2 control-label" for="code">编码</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="id" />
						</div>
					</div> -->
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name">名称</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="name" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="commonName">通用名称</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="commonName" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="belongPosition">所属行政区域</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="belongPosition" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="level">级别</label>
						<div class="col-lg-5">
							<select id="level" name="level">
								<c:forEach items="${levelNameMap }" var="levelName">
									<option value="${levelName.key }">${levelName.value }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
			        	<div class="col-lg-offset-3 col-lg-3">
			        		<input class="btn btn-block btn-darkorange" type="submit" value="提交" />
				        </div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
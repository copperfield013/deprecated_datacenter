<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加地址</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/address/doAdd">
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name">地址名称</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="name" 
								data-bv-notempty="true"
								data-bv-notempty-message="名称不能为空"/>
						</div>
					</div>
					<div class="form-group">
			        	<div class="col-lg-offset-3 col-lg-3">
			        		<input class="btn btn-block btn-darkorange" type="submit" value="提交"  />
				        </div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/x-jquery-tmpl" id="province"></script>
</div>
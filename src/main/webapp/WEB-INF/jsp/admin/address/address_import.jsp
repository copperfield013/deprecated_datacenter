<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-import">
	<div class="page-header">
		<div class="header-title">
			<h1>地址导入</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/address/doImport" enctype="multipart/form-data">
					<div class="form-group">
						<label class="col-lg-2 control-label" for="name">选择文件：</label>
						<div class="col-lg-5">
							<input type="file" name="file" />
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
</div>
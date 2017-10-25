<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="field-add">
	<div class="page-header">
		<div class="header-title">
			<h1>添加Demo</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/field/do_add">
					<div class="form-group">
						<label class="col-lg-2 control-label" for="id">id</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="id" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="title">字段中文名</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="title" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="title_en">字段英文名</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="title_en" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="type">类型</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="type" />
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
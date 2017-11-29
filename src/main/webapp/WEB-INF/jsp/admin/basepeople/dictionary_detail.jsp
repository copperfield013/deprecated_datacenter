<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="dictionary-add">
	<div class="page-header">
		<div class="header-title">
			<h1>编辑Demo</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">

			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/peopleDictionary/save">
					<div class="form-group">
						<label class="col-lg-2 control-label" for="cId"></label>
						<div class="col-lg-5">
							<input class="form-control"  type ="hidden" name="cId" value="${dictionaryDataDto.cId}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="cCnName">字段中文名</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="cCnName" value="${dictionaryDataDto.cCnName}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="cCnEnglish">字段英文名</label>
						<div class="col-lg-5">
							<input type="text" class="form-control" name="cCnEnglish"  value="${dictionaryDataDto.cCnEnglish}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="type">类型</label>
						<div class="col-lg-5">

							<select class="form-control" name="type"  data-value="${dictionaryDataDto.type}">
								<option value=""> -- 请选择 -- </option>
								<option value="1">文本输入框</option>
								<option value="2">单选框</option>
								<option value="3">单选下拉框</option>
								<option value="4">多选下拉框</option>
								<option value="5">暂无</option>
								<option value="6">时间控件</option>

							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="check_rule">字段校验规则</label>
						<div class="col-lg-5">
							<select class="form-control" name="check_rule"  data-value="${dictionaryDataDto.check_rule}">
								<option value="0">不设置 </option>
								<option value="1">email</option>
								<option value="2">idcode</option>
								<option value="3">phone</option>

							</select>
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
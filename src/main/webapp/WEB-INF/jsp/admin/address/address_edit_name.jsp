<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-edit-name" class="zpage">

	<h1 class="zpage-title">修改地址</h1>
				<form class="bv-form  validate-form" action="admin/address/doEditName">
					<input type="hidden" name="oldName" value="${addressStr }"/>
					<div class="zform-group">
						<span class="zform-label">地址名称</span>
						<div class="form-group zform-item">
							<input type="text" class="form-control basic-input item-input" name="name" value="${addressStr }"
								data-bv-notempty="true"
								data-bv-notempty-message="名称不能为空"/>
						</div>
					</div>
					<div class="margin-t30">
			        	<input class="form-primary-button margin-l80" type="submit" value="提交"  />
					</div>
				</form>
</div>
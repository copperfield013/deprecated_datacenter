<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="special-position-add">
	<div class="page-header">
		<div class="header-title">
			<h1>重新分组</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/address/updateCategory">
					<input type="hidden" id="name" name="name" value="${splitedAddressEntity.name }"/>
					<input type="hidden" id="splitName" name="splitName" value="${splitedAddressEntity.splitName }"/>
					<input type="hidden" id="positionCode" name="positionCode" value="${splitedAddressEntity.positionCode }"/>
					<input type="hidden" id="keyPoint" name="keyPoint" value="${splitedAddressEntity.keyPoint }"/>
					<input type="hidden" id="laterPart" name="laterPart" value="${splitedAddressEntity.laterPart }"/>
					<div class="form-group">
						<label class="col-lg-2 control-label" for="code">编码</label>
						<div class="col-lg-5">
							<select id="code" name="code" data-value="${splitedAddressEntity.code }">
								<c:forEach items="${list }" var="addressCode">
									<option value="${addressCode.code }">${addressCode.code }</option>
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
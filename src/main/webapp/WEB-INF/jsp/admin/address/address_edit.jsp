<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>

<div id="address-edit">
	<div class="page-header">
		<div class="header-title">
			<h1>修改地址分词</h1>
		</div>
	</div>


	<div class="page-body" id="clone">
		<div id="cloneInput"></div>
	</div >
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form id="address-edit-form" class="bv-form form-horizontal validate-form" action="admin/address/doEdit">
					<input type="hidden" id="code" name="splitedAddressEntity.code" value="${splitedAddressEntity.code }"/>
					<input type="hidden" id="splitName" name="splitedAddressEntity.splitName" value="${splitedAddressEntity.splitName }"/>
					<input type="hidden" id="positionCode" name="splitedAddressEntity.positionCode" value="${splitedAddressEntity.positionCode }"/>
					<input type="hidden" id="keyPoint" name="splitedAddressEntity.keyPoint" value="${splitedAddressEntity.keyPoint }"/>
					<input type="hidden" id="laterPart" name="splitedAddressEntity.laterPart" value="${splitedAddressEntity.laterPart }"/>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">地址名称</label>
							<div class="col-lg-4">
								<input type="text" class="form-control" name="splitedAddressEntity.name" style="border:0;width:85%; display:inline-block" value="${splitedAddressEntity.name }" readonly="readonly"/>
								<a href="admin/address/edit_name?addressStr=${splitedAddressEntity.name }">修改</a>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">分词地址</label>
							<div class="col-lg-8">
								<c:forEach items="${list }" var="addressElement" varStatus="i">
									<div class="col-lg-3" style="display:inline-block;">
										<select id="level" name="elements[${i.index }].level" data-value="${addressElement.level }">
											<c:forEach items="${levelNameMap }" var="levelName">
												<option value="${levelName.key }">${levelName.value }</option>
											</c:forEach>
										</select>
									</div>
									<input type="text" class="form-control content" name="elements[${i.index }].content" value="${addressElement.content }" style="display:inline-block; width: 40%;"/>
									<%-- <input type="hidden" name="elements[${i.index }].level" value="${addressElement.level }"/> --%>
									<input type="text" class="form-control" name="elements[${i.index }].termWord" value="${addressElement.termWord }" style="display:inline-block; width: 10%;"/>
									<input type="hidden" name="elements[${i.index }].positionCode" value="${addressElement.positionCode }"/>
								</c:forEach>
							</div>
						</div>
					</div>
				
					<div class="form-group">
			        	<div class="col-lg-offset-3 col-lg-3">
			        		<input id="submit-btn" class="btn btn-block btn-darkorange" type="button" value="提交" />
				        </div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>
	seajs.use(['dialog', 'utils'], function(Dialog, Utils){
		var $page = $('#address-edit');
		
		$("#submit-btn", $page).on("click", function(){
			var list = '${list }';
			//var length = ${fn:length(list)};
			var addressStr = '${splitedAddressEntity.name}';
			var artificialSplitName= "";
			$(".content").each(function(){
				artificialSplitName += $(this).val();
			});
			if(addressStr == artificialSplitName){
				$("#address-edit-form").submit();
			}else{
				Dialog.notice("修改后的分词与地址不符！", "warning");
			}
		});
	});
	
	
</script>
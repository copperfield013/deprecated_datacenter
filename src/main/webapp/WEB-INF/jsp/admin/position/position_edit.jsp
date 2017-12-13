<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>

<div id="position-edit">
	<div class="page-header">
		<div class="header-title">
			<h1>修改地点信息</h1>
		</div>
	</div>


	<div class="page-body" id="clone">
		<div id="cloneInput"></div>
	</div >
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/position/doEdit">
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">编码</label>
							<div class="col-lg-4">
							<input type="text" name="code" class="form-control" value="${position.code }" readonly="readonly"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">名称</label>
							<div class="col-lg-4">
							<input type="text" name="name" class="form-control" value="${position.name }"/>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">别名</label>
							<div class="col-lg-4">
							<input type="text" name="alias" class="form-control" value="${position.alias }"/>
							</div>
						</div>
					</div>
					<%-- <div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="level">级别</label>
							<div class="col-lg-4">
							<input type="text" name="level" class="form-control" value="${position.level }"/>
							</div>
						</div>
					</div> --%>
				
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
		var $page = $('#position-edit');
		
		/* $("#submit-btn", $page).on("click", function(){
			var list = '${list}';
			var length = ${fn:length(list)};
			var $ABC0924 = "";
			for(var i=0; i<length; i++){
				if($("#ABP0003-" + i).val() != null && $("#ABP0003-" + i).val() != ""){
					$ABC0924 += $("#ABP0003-" + i).val() + ",";
				}
			}
			$ABC0924 = $ABC0924.substring(0, $ABC0924.length -1);
			console.log($ABC0924);
			Ajax.ajax("admin/address/doEdit",{
				ABP0003 : $("#ABP0003-old").val(),
				ABC0924 : $ABC0924
			},{
				page : $page.getLocatePage()
			});
		}); */
	});
	
	
</script>
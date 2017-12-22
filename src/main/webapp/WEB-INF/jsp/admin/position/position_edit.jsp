<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link href="media/admin/selectiontest/selectiontest.css" rel="stylesheet" type="text/css" />
<script src="media/admin/selectiontest/selectiontest.js"></script>
<div id="position-edit" class="zpage">
	<h1 class="zpage-title">修改地点信息</h1>
		<form class="bv-form  validate-form margin-t15" action="admin/position/doEdit">
					<%-- <div class="form-group">
						<div>
							<label class="col-lg-1 control-label" for="name">编码</label>
							<div class="col-lg-4">
							<input type="text" name="code" class="form-control" value="${position.code }" readonly="readonly"/>
							</div>
						</div>
					</div> --%>
					<div class="zform-group">
							<span class="zform-label" >名称</span>
							<div class="form-group zform-item">
							<input type="text" name="name" class="form-control basic-input item-input" value="${position.name }"
								data-bv-notempty="true"
								data-bv-notempty-message="名称不能为空"/>
							</div>
					</div>

					<div class="zform-group">
							<span class="zform-label" >别名</span>
							<div class="form-group zform-item">
							<input type="text" name="alias" class="form-control basic-input item-input" value="${position.alias }"/>
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
					<div class="zform-group">
							<span class="zform-label" >级别</span>
							<div class="form-group zform-item">
								<select id="level" name="level" data-value="${position.level }">
									<c:forEach items="${levelNameMap }" var="levelName">
										<option value="${levelName.key }">${levelName.value }</option>
									</c:forEach>
								</select>
							</div>
					</div>
				
			       <input id="submit-btn" class="form-primary-button margin-l80 margin-t30" type="submit" value="提交" />
		</form>		
</div>

<script>
	seajs.use(['ajax','utils'], function(Ajax, Utils){
		var $page = $('#position-edit');
		
		$('#level',$page).Selection({
			width: "180px"
		})
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
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="address-import" class="zpage">
	<h1 class="zpage-title">地址导入</h1>
	<div class="">
		<form class="bv-form  validate-form" action="admin/address/doImport" enctype="multipart/form-data">
			<div class="zform-group">
				<span class="zform-label" >选择文件</span>
				<div class="form-group zform-item" >
					<input type="file" name="file" class="form-control" data-show-caption="true"/>
				</div>
			</div>	
			<span class="form-primary-button margin-t30 submit">提交</span>
		</form>
	</div>
</div>
<script>
seajs.use(['dialog'], function(Dialog){
	var $page = $('#address-import');
	$('span.submit',$page).on('click',function(){
		var fileVal = $('input[type="file"]',$page).val();
		console.log(fileVal);
		if( fileVal === ""){
			Dialog.notice("请选择文件","warning");
			return;
		}
		$('form',$page).submit();
	})
})
</script>

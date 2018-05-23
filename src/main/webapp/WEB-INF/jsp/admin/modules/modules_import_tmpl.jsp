<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<title>${module.title }导入</title>
<div id="modules-import-${module.name }">
	<div class="page-header">
		<div class="header-title">
			<h1>${module.title }导入</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" 
					start-url="admin/modules/import/do/${module.name }"
					status-url="admin/modules/import/status"
					break-url="admin/modules/import/break">
					<div class="form-group">
						<label class="col-lg-2 control-label">上传文件</label>
						<div class="col-lg-6">
							<input id="file" type="file" name="file" class="form-control" accept=".xls,.xlsx" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">表格名</label>
						<div class="col-lg-6">
							<select id="sheetName" name="sheetName" class="form-control"></select>
							<!-- <input id="sheetNames" type="text" name="sheetName" class="form-control" /> -->
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">导入条线</label>
						<div class="col-lg-6">
							<select id="composite" name="dataType">
								<c:forEach var="composite" items="${composites }">
									<option value="${composite.name }">${composite.title }</option>
								</c:forEach>
							</select>
							<a href="javascript:;" id="link-import-tmpl">模板</a>
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">进度</label>
						<div class="col-lg-6">
							<div id="progress" class="progress progress-striped active">
                                <div class="progress-bar progress-bar-success" 
                                	role="progressbar" 
                                	aria-valuenow="0" 
                                	aria-valuemin="0" 
                                	aria-valuemax="100" 
                                	style="width: 0">
                                    <span>
                                        0
                                    </span>
                                </div>
                            </div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-lg-offset-3 col-lg-2">
			        		<a class="btn btn-block btn-primary" id="submit">开始导入</a>
				        </div>
						<div class="col-lg-2">
			        		<a class="btn btn-block btn-defualt" id="break" css-display="none">停止导入</a>
				        </div>
					</div>
					<div class="form-group">
						<div class="col-lg-6 col-lg-offset-2" id="feedback-msg">
							
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['ajax', 'dialog', '$CPF'], function(Ajax, Dialog, $CPF){
		var $page = $('#modules-import-${module.name }');
		console.log($page);
		var $feedback = $('#feedback-msg', $page);
		var uuid = null;
		var $form = $('form', $page);
		var fileUUID = null;
		$('#file').change(function(){
			var formData = new FormData();
			if(this.files && this.files.length > 0){
				formData.append('file', this.files[0]);
				$CPF.showLoading();
				Ajax.ajax('admin/modules/import/resolve_file', formData, function(data){
					if(data.status === 'suc'){
						var $sheetName = $('#sheetName', $page).empty();
						for(var i in data.names){
							$sheetName.append('<option value="' + data.names[i] + '">' + data.names[i] + '</option>');	
						}
						fileUUID = data.fileName;
					}else{
						Dialog.notice('文件错误', 'error');
					}
				}, {
					afterLoad	: function(){
						$CPF.closeLoading();
					}
				});
			}
		});
		
		
		$('#submit', $page).click(function(){
			Dialog.confirm('确认导入？', function(yes){
				if(yes){
					var formData = new FormData($form[0]);
					formData['delete']('file');
					formData.append('fileName', fileUUID)
					$CPF.showLoading();
					Ajax.ajax($form.attr('start-url'), formData, function(data){
						if(data.status === 'suc' && data.uuid){
							$('#break', $page).show();
							$('#submit', $page).attr('disabled', 'disabled');
							var timer = setInterval(function(){
								uuid = data.uuid;
								Ajax.ajax($form.attr('status-url'),{uuid: data.uuid}, function(data){
									if(data.status === 'suc'){
										if(typeof data.current === 'number' && typeof data.totalCount === 'number'){
											var progress = data.current/data.totalCount;
											var percent = parseFloat(progress * 100).toFixed(0);
											$('#progress', $page)
												.find('.progress-bar')
												.attr('aria-valuenow', percent)
												.css('width', percent + '%')
												.find('span')
													.text(percent + '%');
											var remain = data.totalCount - data.current;
											
											if(progress >= 1){
												Dialog.notice('导入完成', 'success');
												$('#progress span', $page).text('导入完成');
												clearInterval(timer);
												$feedback.text('');
												$('#submit', $page).removeAttr('disabled').text('重新导入');
												uuid = null;
											}else{
												var msg = data.message + ',' 
															+ '剩余' + remain + '条';
												if(data.lastInterval && data.lastInterval > 0){
													msg += '，当前速率' + parseFloat(1000 / data.lastInterval).toFixed(2) + '条/秒，'
													+ '预计剩余时间' + parseFloat(remain * data.lastInterval / 1000).toFixed(0) + '秒';
												}
												$feedback.text(msg);
											}
										}
									}else{
										clearInterval(timer);
										$('#submit', $page).removeAttr('disabled').text('重新导入');
										uuid = null;
									}
								});
							}, 1000);
						}else{
							if(data.status === 'error' && data.msg){
								Dialog.notice(data.msg, 'error');
							}
						}
					}, {
						afterLoad	: function(){
							$CPF.closeLoading();
						}
					});
				}
			});
		});
		$('#break', $page).click(function(){
			if(uuid){
				Dialog.confirm('确认停止当前的导入任务？', function(){
						Ajax.ajax($form.attr('break-url'), {uuid: uuid}, function(data){
							if(data.status === 'suc'){
								$('#break', $page).hide();
							}
						});
				});
			}
		});
		$('#link-import-tmpl', $page).click(function(){
			var composite = $('#composite', $page).val();
			Dialog.openDialog('admin/modules/import/tmpl/${module.name}/' + composite, '字段', undefined, {
				width	: 1000,
				height	: 500
			});
		});
	});
</script>
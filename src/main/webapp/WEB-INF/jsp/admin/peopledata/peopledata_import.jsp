<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="peopledata-import">
	<div class="page-header">
		<div class="header-title">
			<h1>人口导入</h1>
		</div>
	</div>
	<div class="page-body">
		<div class="row">
			<div class="col-lg-12">
				<form class="bv-form form-horizontal validate-form" action="admin/peopledata/do_import">
					<div class="form-group">
						<label class="col-lg-2 control-label">上传文件</label>
						<div class="col-lg-6">
							<input type="file" name="file" class="form-control" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">表格名</label>
						<div class="col-lg-6">
							<input type="text" name="sheetName" class="form-control" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">进度</label>
						<div class="col-lg-6">
							<div id="progress" class="progress progress-striped active">
                                <div class="progress-bar progress-bar-orange" 
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
	seajs.use(['ajax', 'dialog'], function(Ajax, Dialog){
		var $page = $('#peopledata-import');
		var $feedback = $('#feedback-msg', $page);
		$('#submit', $page).click(function(){
			Dialog.confirm('确认导入？', function(yes){
				if(yes){
					var formData = new FormData($('form', $page)[0]);
					Ajax.ajax('admin/peopledata/do_import', formData, function(){
						$('#break', $page).show();
						var timer = setInterval(function(){
							Ajax.ajax('admin/peopledata/status_of_import',{}, function(data){
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
											Dialog.notice('导入完成');
											clearInterval(timer);
											$feedback.text('');
										}else{
											var msg = data.message + ',' 
														+ '剩余' + remain + '条';
											if(data.lastInterval && data.lastInterval > 0){
												msg += '，当前速率' + parseFloat(1000/data.lastInterval).toFixed(2) + '条/秒，'
												+ '预计剩余时间' + parseFloat(remain * data.lastInterval / 1000).toFixed(0) + '秒'
											}
											$feedback.text(msg);
										}
									}
								}else{
									clearInterval(timer);
								}
							});
						}, 1000);
					});
				}
			});
		});
		$('#break', $page).click(function(){
			Dialog.confirm('确认停止当前的导入任务？', function(){
				Ajax.ajax('admin/peopledata/break_import', {}, function(data){
					if(data.status === 'suc'){
						$('#break', $page).hide();
					}
				});
			});
		});
	});
</script>
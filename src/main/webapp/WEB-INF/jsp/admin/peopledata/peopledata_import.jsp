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
						<div class="col-lg-offset-3 col-lg-3">
			        		<input class="btn btn-block btn-primary" id="submit" type="button" value="提交"  />
				        </div>
					</div>
					
				</form>
			</div>
		</div>
	</div>
</div>
<script>
	seajs.use(['ajax'], function(Ajax){
		var $page = $('#peopledata-import');
		$('#submit', $page).click(function(){
			var formData = new FormData($('form', $page)[0]);
			Ajax.ajax('admin/peopledata/do_import', formData, function(){
				var timer = setInterval(function(){
					Ajax.ajax('admin/peopledata/status_of_import',{}, function(data){
						if(data.status === 'no found import progress'){
							clearInterval(timer);
						}else{
							if(typeof data.current === 'number' && typeof data.totalCount === 'number'){
								var progress = data.current/data.totalCount;
								var percent = parseFloat(progress * 100).toFixed(0);
								$('#progress', $page)
									.find('.progress-bar')
									.attr('aria-valuenow', percent)
									.css('width', percent + '%')
									.find('span')
										.text(percent + '%');
							}
							console.log(data);
						}
					});
				}, 1000);
			});
		});
	});
</script>
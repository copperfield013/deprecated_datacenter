<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<title>易+数据融合工具</title>
		<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
		<jsp:include page="/WEB-INF/jsp/admin/common/admin-include.jsp"></jsp:include>
	    <!--Basic Scripts-->
	    <script src="media/admin/plugins/beyond/js/jquery-ui-1.10.4.custom.js"></script>
	    <!--Skin Script: Place this script in head to load scripts for skins and rtl support-->
	    <script src="media/admin/plugins/beyond/js/skins.min.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/bootstrap-datepicker.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/bootstrap-timepicker.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/moment.js"></script>
	    <script src="media/admin/plugins/beyond/js/datetime/daterangepicker.js"></script>
	    <script src="media/admin/plugins/bootstrapt-treeview/dist/bootstrap-treeview.min.js"></script>
	    <script src="media/admin/plugins/beyond/js/validation/bootstrapValidator.js"></script>
	    <script src="media/admin/plugins/beyond/js/tagsinput/bootstrap-tagsinput.js"></script>
	    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=YhhQe0yOr3L0f1mHLZcjHIZyvQixl4Zq"></script>
	</head>
	<body>
		<div class="navbar">
	        <div class="navbar-inner">
	            <div class="navbar-container">
	                <div class="navbar-header pull-left">
	                    <a href="admin/logout" class="navbar-brand">
	                        <small>
	                            <img src="media/admin/main/image/datacenter-logo.png"/>
	                        </small>
	                    </a>
	                </div>
	                <div class="sidebar-collapse" id="sidebar-collapse">
	                    <i class="collapse-icon fa fa-bars"></i>
	                </div>
	                <div class="cmd-right">
	                	<a class="jump" href="admin/logout">退出登录</a>
	                </div>
				</div>
			</div>
		</div>
	    <div class="main-container container-fluid">
	        <div class="page-container">
	            <div class="page-sidebar" id="sidebar">
	                <ul class="nav sidebar-menu">
	                	<li class="open">
	                		<a href="#">
	                			<i class="menu-icon glyphicon glyphicon-home"></i>
	                			<span class="menu-text">主页面</span>
	                		</a>
	                	</li>
	            		<jsp:include page="sidebar_menu.jsp" />
					</ul>
				</div>
				<div class="page-content">
					<div class="tabbable">	
						<div class="tab-warp">
							<a href="javascript:;" class="move left">◀</a>	
								<div>	
									<ul class="nav nav-tabs" id="main-tab-title-container">
										<li class="active main-tab-title">
										    <a data-toggle="tab" href="#cpf-home-tab">
												主页
										    </a>
										</li>
									</ul>
								</div>
							<a href="javascript:;" class="move right">▶</a>
						</div>				
						<div class="tab-content" id="main-tab-content-container">
							<div id="cpf-home-tab" class="tab-pane active main-tab-content">
								<jsp:include page="home.jsp"></jsp:include>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	    <script src="media/admin/plugins/beyond/js/bootstrap.js"></script>
	    <script src="media/admin/plugins/beyond/js/toastr/toastr.js"></script>
	    <script src="media/admin/plugins/beyond/js/beyond.min.js"></script>
	    
	    <script type="text/javascript">
	    	$(function(){
	    		seajs.config({
				base	: '${basePath}media/admin/',
				paths	: {
					  COMMON	: '${basePath}media/common/',
					  MAIN		: '${basePath}media/admin/main/js/'
				},
				alias	: {
					'$CPF'		: 'COMMON/cpf/cpf-core.js',
					'utils'		: 'COMMON/cpf/cpf-utils.js',
					'page'		: 'COMMON/cpf/cpf-page.js',
					'dialog'	: 'COMMON/cpf/cpf-dialog.js',
					'paging'	: 'COMMON/cpf/cpf-paging.js',
					'tree'		: 'COMMON/cpf/cpf-tree.js',
					'form'		: 'COMMON/cpf/cpf-form.js',
					'tab' 		: 'COMMON/cpf/cpf-tab.js',
					'ajax'		: 'COMMON/cpf/cpf-ajax.js',
					'css'		: 'COMMON/cpf/cpf-css.js',
					'timer'		: 'COMMON/cpf/cpf-timer.js',
					'console'	: 'COMMON/cpf/cpf-console.js',
					'control'	: 'COMMON/cpf/cpf-control.js'
				}
	    		});
	    		seajs.use('COMMON/cpf/cpf-main.js');
	    	});
	    </script>
	</body>
</html>
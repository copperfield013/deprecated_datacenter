define(function(require){
	/**
	 * 插件初始化顺序
	 * 1.tab
	 * 
	 * 
	 * 
	 * 页面初始化顺序
	 * 1.core
	 * 2.paging
	 * 3.dialog
	 * 4.form
	 * 5.page
	 * 6.css
	 * 7.
	 * 8.
	 * 9.
	 * 10.tab
	 * 11.control
	 * 12.staticview
	 * 
	 */
	
	var $CPF = require('$CPF');
	require('ajax');
	var Page = require('page');
	require('form');
	require('paging');
	require('dialog');
	require('tree');
	require('tab');
	require('innerpage');
	require('css');
	require('control');
	$CPF.init({
		loadingImg			: 'media/admin/cpf/image/loading.gif',
		innerPageLoadingImg	: 'media/admin/cpf/image/innerpage-loading.gif',
		maxPageCount		: 8,
		sessionInvalidURL	: 'admin/login',
		staticViewArea		: $('#cpf-staticview-area')
	});
	$CPF.showLoading();
	//初始化当前页面
    $CPF.initPage(document);
    $CPF.closeLoading();
});

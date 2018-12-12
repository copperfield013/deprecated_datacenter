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
	require('checkbox');
	$CPF.putPageInitSequeue(12, function($page){
		$(':text.dtrangepicker', $page).each(function(){
			require('utils').daterangepicker($(this));
		});
		$('table.row-selectable', $page).each(function(){
			var $table = $(this);
			var $rows = $('>tbody>tr', $table);
			$table.data('checkedRowGetter', function(){
				return $rows.filter(function(){
					return $(this).find('.row-selectable-checkbox').prop('checked');
				});
			});
			$table.find('.row-selectable-checkbox').change(function(){
				var $checkedRows = $table.data('checkedRowGetter')();
				if($checkedRows.length === 0){
					$selectAll.prop('checked', false);
				}else if($checkedRows.length === $rows.length){
					$selectAll.removeClass('partly').prop('checked', true);
				}else{
					$selectAll.addClass('partly').prop('checked', true);
				}
				$table.trigger('row-selected-change', [$checkedRows]);
			});
			var $selectAll = $table.find(':checkbox.select-all').change(function(){
				$selectAll.removeClass('partly');
				var checkAll = $selectAll.prop('checked');
				$table.children('tbody').find('.row-selectable-checkbox').prop('checked', checkAll);
				$table.trigger('row-selected-change', [checkAll? $rows: $()]);
			});
		});
	});
	$CPF.init({
		loadingImg			: 'media/admin/cpf/image/loading.gif',
		innerPageLoadingImg	: 'media/admin/cpf/image/innerpage-loading.gif',
		maxPageCount		: 8,
		sessionInvalidURL	: 'admin/login',
		tabLinkPrefix		: 'admin/',
		//加载层最多持续多久后出现关闭加载按钮
		loadingTimeout		: 7000
	});
	$CPF.showLoading();
	//初始化当前页面
    $CPF.initPage(document);
    $(document).on('click', function(e){
		var $this = $(e.target);
		var $blurHidden = $this.closest('.blur-hidden'); 
		if($blurHidden.length === 0){
			if(!$this.is('.btn-toggle') && $this.closest('.btn-toggle').length == 0){
				$('.blur-hidden').hide();
			}
		}
	});
    $('.account-view').click(function(e){
    	e.stopPropagation();
    	var $accountArea = $(this).parent();
    	$accountArea.toggleClass('open');
    	$(document).one('click', function(){
    		$accountArea.removeClass('open');
    	});
    });
    
    try{
    	require('main/js/statis-func.js');
    }catch(e){}
    $CPF.closeLoading();
});

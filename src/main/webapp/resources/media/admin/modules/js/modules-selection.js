define(function(require, exports, module){
	"use strict";
	var Form = require('form'),
		Ajax = require('ajax'),
		Utils = require('utils')
	
	exports.init = function($page, moduleName, menuId, 
			pageInfo, multiple, stmplId){
			
		
		var initParam = {};
		Utils.botByDom($page.getLocatePage().getContent(), 'cpf-page-inited', function(){
			var $form = $('form', $page),
				formData = new FormData($form[0]);
			Form.formatFormData($form, formData)
			initParam = Utils.converteFormdata(formData);
			$.extend(initParam, pageInfo);
		});
		
		multiple = multiple == '1';
		
		var $trs = $('.list-area tbody>tr', $page);
		$trs.click(function(){
			if(!multiple){
				$trs.removeClass('selected');
			}
			$(this).closest('tr').toggleClass('selected');
			
		});
		var page = $page.getLocatePage(); 
		page.bind('footer-submit', function(data){
			
			var codes = [];
			$trs.filter('.selected').each(function(){
				var code = $(this).attr('data-code');
				if(code){
					codes.push(code);
				}
			});
			console.log(codes);
			var entitiesLoader = function(fields){
				var deferred = $.Deferred();
				if($.isArray(fields) && fields.length > 0){
					Ajax.ajax('admin/modules/curd/load_entities/' + menuId + '/' + stmplId, {
						codes	: codes.join(),
						fields	: fields.join()
					}, function(data){
						if(data.status === 'suc'){
							deferred.resolve(data.entities);
						}else{
							$.error('获取数据错误');
						}
					});
				}
				return deferred.promise();
			};
			entitiesLoader.codes = codes;
			return entitiesLoader;
		});
	}
});
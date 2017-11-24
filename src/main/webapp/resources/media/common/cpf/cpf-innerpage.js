/**
 * 
 */
define(function(require, exports, module){
	var $CPF = require('$CPF'),
		Utils = require('utils')
		;
	
	$CPF.addDefaultParam({
		innerPageClass			: 'cpf-inner-page',
		innerPageIdAttrName		: 'inner-page-id',
		innerPageURLAttrName	: 'url'
	});
	
	$CPF.putPageInitSequeue(12, function($page){
		//在tab和dialog加载时，页面中的所有嵌入页面
		$('.' + $CPF.getParam('innerPageClass'), $page).each(function(){
			var $this = $(this);
			var pageId = $this.attr($CPF.getParam('innerPageIdAttrName'));
			new InnerPage({
				id			: pageId,
				parent		: $page.getLocatePage(),
				$content	: $this,
				onPageLoad	: function(){
					
				}
			});
		});
		
	});
	
	function InnerPage(_param){
		var defaultParam = {
			id			: Utils.uuid(5, 32),
			parent		: null,
			$content	: undefined,
			onPageLoad	: $.noop
		};
		var param = $.extend({}, defaultParam, _param);
		var _this = this,
			formData, url;
		var pageType = 'innerPage';
		
		var Page = require('page');
		
		var page = new Page({
			type		: pageType,
			id			: param.id,
			pageObj		: _this,
			$content	: param.$content,
			$container	: param.$content
		});
		
		this.getId = function(){
			return param.id;
		}
		
		this.getPage = function(){
			return page;
		}
		
		this.getContent = function(){
			return param.$content;
		}
		
		this.getType = function(){
			return pageType;
		};
		
		this.refresh = function(){
			var url = this.getContent().attr($CPF.getParam('innerPageURLAttrName'));
			if(url){
				this.loadContent(url);
			}
		};
		
		this.loadContent = function(content, _title, _formData){
			if(typeof content === 'string'){
				var dUrl = content;
				var dFormData = _formData;
				$CPF.showLoading();
				require('ajax').ajax(dUrl, dFormData, {
					page		: page,
					whenSuc		: function(data, dataType){
						if(dataType === 'html'){
							url = dUrl;
							formData = dFormData;
							_this.loadContent($('<div>').html(data));
						}
					},
					afterLoad	: function(){
						$CPF.closeLoading();
					}
				});
			}else if(content instanceof $){
				$CPF.showLoading();
				this.getContent().html(content.html());
				$CPF.initPage(_this.getContent());
				param.onPageLoad.apply(this);
				$CPF.closeLoading();
			}
		};
		
		this.close = function(){
			
		}
		
	}
	
	$.extend(InnerPage, {
		/**
		 * 
		 */
		bindInnerPage	: function(_param){
			
		}
	});
	
	
	module.exports = InnerPage;
	
});

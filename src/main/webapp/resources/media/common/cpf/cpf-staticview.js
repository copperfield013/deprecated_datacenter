/**
 * 用于静态显示区域的一些动作和初始化
 */
define(function(require, exports, module){
	var $CPF = require('$CPF');
	
	$CPF.addDefaultParam({
		staticViewClass		: 'cpf-static-view',
		staticViewDataKey	: 'cpf-static-view-data',
		staticViewArea 		: null
	});
	
	function getArea(){
		return $($CPF.getParam('staticViewArea'));
	}
	
	function appendToStaticViewArea($ele){
		var $staticViewArea = getArea();
		var id = $ele.attr('id');
		if(!id){
			console.error('要求存放到静态显示区域的元素都要有id属性');
		}else{
			if($staticViewArea.find('#' + id).length > 0){
				console.error('存放到静态显示区域的元素的id属性不能重复');
			}else{
				$staticViewArea.append($ele);
				return;
			}
		}
		console.log($ele);
	}
	
	$CPF.putPageInitSequeue(12, function($page){
		$page = $($page);
		var $staticViewArea = getArea();
		if($staticViewArea.length > 0){
			var staticViewClass = $CPF.getParam('staticViewClass');
			var $staticView = $page.find('.' + staticViewClass);
			var toMove = [];
			$staticView.each(function(){
				var $this = $(this);
				if($this.closest('.' + staticViewClass).length === 1){
					toMove.push($this);
				}
			});
			for(var i in toMove){
				var staticView = new StaticView({
					$dom	: toMove[i]
				});
				toMove[i].data($CPF.getParam('staicViewDataKey'), staticView);
				$staticViewArea.append(toMove[i]);
			}
		}
	});
	
	function StaticView(_param){
		var defaultParam = {
			$dom				: $(),
			//所在页面关闭时，是否移除该元素
			closeCascade		: true,
			//当取消页面的激活状态时，是否同时隐藏该元素
			activateCascade	: true
			
		};
		var param = $.extend({}, defaultParam, _param);
		var _this = this;
		
		this.init = function init(__param){
			$.extend(param, __param);
			var page = param.$dom.getLocatePage();
			if(page instanceof require('page')){
				if(param.closeCascade){
					page.getEventCallbacks('afterClose').remove(_this.remove).add(_this.remove);
				}
				if(param.activateCascade){
					page.getEventCallbacks('afterInactivate').remove(_this.hide).add(_this.hide);
					page.getEventCallbacks('afterActivate').remove(_this.show).add(_this.show);
				}
			}
		}
		
		this.getDom = function(){
			return param.$dom;
		};
		
		this.remove = function(){
			param.$dom.remove();
		};
		this.show = function(){
			param.$dom.show();
		}
		this.hide = function(){
			param.$dom.hide();
		};
		
		this.init();
		
	}
	
	$.extend(StaticView, {
		getElement		: function(id){
			var $area = getArea();
			return $ele = $('#' + id, $area);
		},
		getStaticView	: function(id){
			return getElement().data($CPF.getParam('staticViewDataKey'));
		}
	});
	
	module.exports = StaticView;
});
/**
 * 
 */
define(function(require, exports, module){
	var Utils = require('utils');
	function EntityTreeTemplate(_param){
		Utils.assert($.fn.treeview, '没有加载Bootstrap Treeview');
		var defParam = {
			container	: null,
			treeData	: null
		};
		var param = $.extend({}, defParam, _param);
		
		this.render = function(treeData){
			if(treeData){
				param.treeData = treeData;
				return this.render();
			}
			this.getContainer().treeview({
				showCheckbox: true,
				data		: param.treeData
			});
		}
		
		this.getContainer = function(){
			return $(param.container);
		}
		
	}
	
	EntityTreeTemplate.init = function(_param){
		var defParam = {
			$page		: null,
			moduleName	: null,
			ttmplData	: null
		}
		var param = $.extend({}, defParam, _param);
		
		var $page = param.$page;
		
		var treeData = [{
			text				: '1',
			selectable			: false,
			hierarchicalCheck	: true,
			nodes	: [
				{
					text	: '1-1',
					selectable	: false,
				},
				{
					text	: '1-2',
					selectable	: false,
				}
			]
		},
		{
			text	: '2',
			selectable	: false,
			nodes	: [
				{
					text	: '2-1',
					selectable	: false,
				},
				{
					text	: '2-2',
					selectable	: false,
				}
			]
		}];
		
		var ttmpl = new EntityTreeTemplate({
			container	: $('#entity-tree-tmpl', param.$page),
			treeData	: treeData
		});
		
		//ttmpl.render();
		
		
		$('.cpf-tree li>a>label', $page).click(function(){
			$(this).closest('a').toggleClass('node-checked');
		});
		$('.cpf-tree li>a>i', $page).click(function(){
			$(this).closest('li').toggleClass('node-expanded');
		});
		
		var configStructure = Utils.parseJSON($('#config-structure-json', $page).html());
		console.log(configStructure);
		
		$('.colorpicker', $page).each(function(){
			$(this).minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                change: function (hex, opacity) {
                    if (!hex) return;
                    if (opacity) hex += ', ' + opacity;
                    try {
                        console.log(hex);
                    } catch (e) { }
                },
                theme: 'bootstrap'
            });
		});
	}
	

	
	
	
	
	module.exports = EntityTreeTemplate;
});
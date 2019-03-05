/**
 * 
 */
define(function(require, exports, module){
	var Utils = require('utils');
	var FieldSearch = require('field/js/field-search.js');
	var FieldInput = require('field/js/field-input.js');
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
		
		var $selectableRels = $('div.seletable-relations', $page);
		var $criterias = $('div.relation-criterias', $page);
		
		
		var $nodeConfigTmpl = $('#node-config-tmpl', $page);
		var $selectableRelTmpl = $('#selectable-rel-tmpl', $page);
		
		var $nodeAttrTmpl = $('#node-attr-tmpl', $page);
		
		var $criteriaFielSearchTmpl = $('#criteria-field-search-tmpl', $page);
		
		
		$selectableRels.on('click', '>*', function(){
			var $this = $(this);
			if(!$this.is('.selected')){
				$('>*', $selectableRels).removeClass('selected');
				$this.addClass('selected');
				var mappingId = $this.attr("mapping-id");
				showRelConfig($this);
			}else{
				$this.removeClass('selected');
			}
		});
		$criterias.on('click', '>*', function(){
			var $this = $(this);
			if(!$this.is('.selected')){
				$('>*', $criterias).removeClass('selected');
				$this.addClass('selected');
			}else{
				$this.removeClass('selected');
			}
		});
		
		
		
		var $addNodeType = $('#add-node-type', $page).empty();
		
		var $nodeConfigContainer = $('#node-configs-container', $page);

		var configStructure = Utils.parseJSON($('#config-structure-json', $page).html());
		console.log(configStructure);
		
		var abcNodes = configStructure.abcNodes;
		var abcNodeMap = {};
		for(var i in abcNodes){
			var abcNode = abcNodes[i];
			abcNodeMap[abcNode.mappingId] = abcNode;
			$addNodeType.append('<option value="' + abcNode.mappingId + '">' + abcNode.moduleTitle + '</option>');
		}
		
		
		var thisNode = abcNodeMap[configStructure.rootNodeMappingId];
		
		
		$selectableRels.empty();
		if(thisNode.rels && thisNode.rels.length > 0){
			for(var i in thisNode.rels){
				var $relItem = $selectableRelTmpl.tmpl(thisNode.rels[i]);
				$selectableRels.append($relItem);
			}
		}
		
		function showRelConfig(){
			
		}
		
		//添加节点
		$('#btn-add-node', $page).click(function(){
			var nodeMappingId = $('#add-node-type', $page).val();
			if(nodeMappingId && abcNodeMap[nodeMappingId]){
				var abcNode = abcNodeMap[nodeMappingId];
				var tmplData = {
					nodeName	: abcNode.moduleTitle,
					selector	: '',
					nodeText	: '',
					rels		: [],
					selectableRelations	: []
				};
				for(var i in abcNode.rels){
					var rel = abcNode.rels[i];
					tmplData.rels.push({
						mappingId	: rel.mappingId,
						name		: rel.name,
						relIndex	: i
					});
				}

				var $nodeConfig = $nodeConfigTmpl.tmpl(tmplData);
				var $popup = $('.node-relation-popup', $nodeConfig);
				$('.node-relation-selector>i', $nodeConfig).click(function(){
					var $icon = $(this);
					if($popup.is('.actived')){
						hideRelationPopup();
					}else{
						var offsetTop = $icon[0].offsetTop,
							offsetLeft = $icon[0].offsetLeft;
						$popup.css({
							left	: offsetTop + 'px',
							botttom	: offsetTop + 'px'
						})
						$popup.appendTo($nodeConfig.find('.node-relations')).addClass('actived');
					}

				});
				$('.node-relation-popup>div', $nodeConfig).click(function(){
					//TODO: 添加关系，激活关系
					var relIndex = $(this).attr('rel-index');
						
					var rel = abcNode.rels[relIndex];
					if(rel){
						appendSelectableRel(rel, $nodeConfig);
						hideRelationPopup();
					}
				});
				$nodeConfigContainer.append($nodeConfig);
				
				function hideRelationPopup(){
					$popup.removeClass('actived').insertAfter($('.node-relation-selector>i', $nodeConfig));
				}
				
			}

		});
		
		
		function appendSelectableRel(rel, $nodeConfig){
			var mappingId = rel.mappingId;
			var $selectableRelations = $('.seletable-relations', $nodeConfig);
			var $rel = $('<div>').text(rel.name).attr('mapping-id', rel.mappingId);
			$rel.click(function(){
				if(!$rel.is('selected')){
					$selectableRelations.children('.selected').removeClass('selected');
					$rel.addClass('selected');
					enableRelation(rel, $nodeConfig);
				}
			});
			$selectableRelations.append($rel);
		}
		
		var $criteriaItemTmpl = $('#criteria-item-tmpl', $page);
		
		function enableRelation(rel, $nodeConfig){
			console.log(rel);
			if(rel.rabcNodeMappingId){
				var relationNode = abcNodeMap[rel.rabcNodeMappingId];
				console.log(relationNode);
				var LtmplUpdate = require('tmpl/js/ltmpl-update.js');
				
				var $criteriaContainer = $('.relation-criterias', $nodeConfig);
				$criteriaContainer.empty();

				var $detailArea = $('.relation-criteria-detail', $nodeConfig);
				var $criteriaFieldSearch = 
						$criteriaFielSearchTmpl.tmpl()
								.prependTo($detailArea.children('.criteria-field-search').remove().end());
				
				var criteriaHandler = LtmplUpdate.initCriteria({
					$page					: $page,	
					$criteriaFieldSearch	: $criteriaFieldSearch,
					$fieldPickerContainer	: $('#configs-area', $page),
					moduleName				: relationNode.moduleName,
					$detailArea				,
					$criteriaContainer		: $criteriaContainer,
					$criteriaItemTmpl		: $criteriaItemTmpl
				});

				$('.criteria-opr-area>i', $nodeConfig).off('click').click(function(){
					criteriaHandler.addCriteria();
				});
			}
		}
		
		
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
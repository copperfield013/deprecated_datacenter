/**
 * 
 */
define(function(require, exports, module){
	var Utils = require('utils');
	var FieldSearch = require('field/js/field-search.js');
	var FieldInput = require('field/js/field-input.js');
	
	exports.init = function(_param){
		var defParam = {
			$page			: null,
			moduleName		: null,
			ttmplData		: null,
			configStructure	: null
		}
		var param = $.extend({}, defParam, _param);
		
		var $page = param.$page;
		
		//模块结构元数据
		var configStructure = param.configStructure;
		console.log(configStructure);
		
		//模板初始数据
		var ttmplData = param.ttmplData;
		console.log(ttmplData);
		
		
		//Node模板
		var $nodeConfigTmpl = $('#node-config-tmpl', $page);
		//node的模块配置中所有RABC关系模板
		var $moduleRelTmpl = $('#module-rel-tmpl', $page);
		//已添加的关系模板
		var $selectableRelationTmpl = $('#selectable-relation-tmpl', $page);
		//关系条件字段选择器模板
		var $criteriaFieldSearchTmpl = $('#criteria-field-search-tmpl', $page);
		//条件对象模板
		var $criteriaItemTmpl = $('#criteria-item-tmpl', $page);

		//Node容器
		var $nodeConfigContainer = $('#node-configs-container', $page);
		//添加Node的模块选择框
		var $addNodeType = $('#add-node-type', $page).empty();
		

		
		
		//根据模块结构元数据，初始化配置控件
		var abcNodes = configStructure.abcNodes;
		var abcNodeMap = {};
		var moduleNodeMap = {};
		for(var i in abcNodes){
			var abcNode = abcNodes[i];
			abcNodeMap[abcNode.mappingId] = abcNode;
			moduleNodeMap[abcNode.moduleName] = abcNode;
			$addNodeType.append('<option value="' + abcNode.mappingId + '">' + abcNode.moduleTitle + '</option>');
		}
		
		var thisNode = abcNodeMap[configStructure.rootNodeMappingId];
		
		/**
		 * 构造用于NodeConfig模板的数据对象
		 */
		function buildNodeConfigTmplData(nodeConfig){
			var abcNode = nodeConfig.abcNode;
			var tmplData = {
				nodeId		: nodeConfig.id || '',
				nodeName	: abcNode.moduleTitle,
				nodeModule	: abcNode.moduleName,
				selector	: nodeConfig.selector || '',
				nodeText	: nodeConfig.nodeText || '',
				nodeColor	: nodeConfig.nodeColor,
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
			var relationMap = {};
			for(var i in nodeConfig.relations){
				var relConfig = nodeConfig.relations[i];
				relationMap['id_' + relConfig.id] = relConfig;
				var rel = getNodeRelation(abcNode, relConfig.relationName);
				tmplData.selectableRelations[i] = {
					id			: relConfig.id,
					name		: relConfig.relationName,
					mappingId	: rel.rabcNodeMappingId,
					title		: relConfig.title
				};
			}
			tmplData.relationMap = relationMap;
			return tmplData;
		}
		function getNodeRelation(abcNode, relationName){
			if(abcNode.rels){
				for(var i in abcNode.rels){
					if(abcNode.rels[i].name === relationName){
						return abcNode.rels[i];
					}
				}
			}
		}
		
		/**
		 * 绑定按钮，弹出框选择关系
		 */
		function bindNodeConfigAddRelationEvent($nodeConfig, abcNode){
			var $popup = $('.node-relation-popup', $nodeConfig);
			$('.node-relation-selector>i', $nodeConfig).click(function(){
				var $icon = $(this);
				if($popup.is('.actived')){
					hideRelationPopup();
				}else{
					$popup.appendTo($nodeConfig.find('.node-relations')).addClass('actived');
					
					var $popupOffsetParent = $popup[0].offsetParent;
					var $n = $icon[0];
					//计算当前按钮位置，并在该位置弹出选择框
					var offsetTop = 0,
						offsetLeft = 0;
					while($n && $n.offsetParent !== $popupOffsetParent){
						offsetTop += $n.offsetTop;
						offsetLeft += $n.offsetLeft;
						$n = $n.offsetParent;
					}
					if($n){
						offsetTop += $n.offsetTop - 100;
						offsetLeft += $n.offsetLeft - 2;
						$popup.css({
							left	: offsetLeft + 'px',
							top		: offsetTop + 'px'
						})
					}else{
						$.error('icon不在popup的定位容器内');
					}
					
				}
			});
			//选择关系后添加到selectableRelations里
			$('.node-relation-popup>div', $nodeConfig).click(function(){
				//TODO: 添加关系，激活关系
				var relIndex = $(this).attr('rel-index');
				var rel = abcNode.rels[relIndex];
				if(rel){
					appendSelectableRel(rel, $nodeConfig);
					hideRelationPopup();
				}
			});
			//隐藏弹出框
			function hideRelationPopup(){
				$popup.removeClass('actived').insertAfter($('.node-relation-selector>i', $nodeConfig));
			}
		}
		
		//添加Node到当前页面的容器中
		function addNode(nodeConfig){
			//构造用于NodeConfig模板的数据对象
			var tmplData = buildNodeConfigTmplData(nodeConfig);
			//根据数据构造NodeConfig的DOM
			var $nodeConfig = $nodeConfigTmpl.tmpl(tmplData);
			//为NodeConfig的DOM绑定数据和事件
			bindColorpicker($('.node-color', $nodeConfig));
			bindNodeConfigAddRelationEvent($nodeConfig, nodeConfig.abcNode);
			//绑定NodeConfig的初始Relation的数据
			$('.selectable-relations>[data-id]', $nodeConfig).each(function(){
				var $selectableRel = $(this);
				var relId = $(this).attr('data-id');
				if(relId){
					var relConfig = tmplData.relationMap['id_' + relId];
					var rel = getNodeRelation(nodeConfig.abcNode, relConfig.relationName);
					if(rel){
						$selectableRel.data('init-rel-config', relConfig);
						$selectableRel.data('data-rel',rel);
					}
				}
			});
			//绑定NodeConfig内所有Relation的代理点击事件
			$nodeConfig.on('click', '.selectable-relations>div', function(){
				var $rel = $(this);
				if(!$rel.is('.selected')){
					enableRelation.apply($rel, [$nodeConfig]);
				}
			})
			//关系名可编辑
			.on('dblclick', '.selectable-relations>div>.selectable-relation-title', function(){require('utils').toEditContent(this)});
			//将NodeConfig的DOM放到容器中
			$nodeConfigContainer.append($nodeConfig);
		}
		
		
		//添加Node
		$('#btn-add-node', $page).click(function(){
			var nodeMappingId = $('#add-node-type', $page).val();
			if(nodeMappingId && abcNodeMap[nodeMappingId]){
				var abcNode = abcNodeMap[nodeMappingId];
				addNode({abcNode});
			}
		});
		
		//当前已选的关系
		var $selectedRel = null;

		/**
		 * 在Node中添加关系
		 */
		function appendSelectableRel(rel, $nodeConfig){
			var mappingId = rel.mappingId;
			var tmplData = {
				mappingId	: rel.rabcNodeMappingId,
				name		: rel.name,
				id			: rel.id || '',
				title		: rel.name
			}
			var $rel = $selectableRelationTmpl.tmpl(tmplData);
			$rel.data('data-rel', rel);
			$('.selectable-relations', $nodeConfig).append($rel);
		}
		
		 
		/**
		 * 激活某个selectableRelation
		 * 方法的this指向关系的DOM对象
		 */
		function enableRelation($nodeConfig){
			var rel = this.data('data-rel');
			if(rel.rabcNodeMappingId){
				var relationNode = abcNodeMap[rel.rabcNodeMappingId];
				if(relationNode){
					var LtmplUpdate = require('tmpl/js/ltmpl-update.js');

					if($selectedRel){
						var selectedCriteriaHandler = $selectedRel.data('criteriaHandler');
						if(selectedCriteriaHandler){
							$selectedRel.data('criteriaData', selectedCriteriaHandler.getCriteriaData());
						}
					}

					var $criteriaContainer = $('.relation-criterias', $nodeConfig);
					$criteriaContainer.empty();

					var $detailArea = $('.relation-criteria-detail', $nodeConfig);
					var $criteriaFieldSearch = 
							$criteriaFieldSearchTmpl.tmpl()
									.prependTo($detailArea.children('.criteria-field-search').remove().end());
					var criteriaData = this.data('criteriaData');
					var initRelConfig = this.data('init-rel-config');
					if(initRelConfig){
						if(!criteriaData){
							criteriaData = initRelConfig.criterias;
						}
					}
					var criteriaHandler = LtmplUpdate.initCriteria({
						$page					: $page,	
						$criteriaFieldSearch	: $criteriaFieldSearch,
						$fieldPickerContainer	: $('#configs-area', $page),
						moduleName				: relationNode.moduleName,
						$detailArea				,
						$criteriaContainer		: $criteriaContainer,
						$criteriaItemTmpl		: $criteriaItemTmpl,
						criteriaData			: criteriaData
					});

					this.data('criteriaHandler', criteriaHandler);

					$('.criteria-opr-area>i', $nodeConfig).off('click').click(function(){
						criteriaHandler.addCriteria();
					});
					$selectedRel = this;
					$('.selectable-relations', $nodeConfig).children('.selected').removeClass('selected');
					this.addClass('selected');
				}
			}
		}
		
		
		$('#save', $page).click(function(){
			var Dialog = require('dialog');
			var saveData = checkSaveData();
			if(saveData){
				require('ajax').postJson('admin/tmpl/tree/save/' + param.moduleName, saveData, function(data){
					if(data.status === 'suc'){
						Dialog.notice('保存成功', 'success');
						$page.getLocatePage().close();
						var tpage = require('page').getPage(param.moduleName + '_tmpl_tree_list');
						if(tpage){
							tpage.refresh();
						}
					}else{
						Dialog.notice('保存失败', 'error');
					}
				});
			}
		});
		
		function checkSaveData(){
			var saveData = {};
			var Dialog = require('dialog');
			var title = $('#tmplTitle', $page).val();
			if(!title){
				Dialog.notice('请输入模板名称', 'error');
				return null;
			}
			var ttmpl = {
				title,
				module			: param.moduleName,
				defaultNodeColor: $('#def-node-color', $page).val(),
				maxDeep			: $('#max-deep', $page).val(),
				nodes		: []
			};
			saveData.ttmpl = ttmpl;
			
			if(ttmplData){
				ttmpl.id = ttmplData.id || '';
			}
			var $nodeConfigs = $nodeConfigContainer.children('.node-config');
			
			$nodeConfigs.each(function(){
				var $nodeConfig = $(this);
				var node = {
					id				: $nodeConfig.attr('data-id'),
					moduleName		: $('.nodeModule', $nodeConfig).val(),
					nodeColor		: $('.node-color', $nodeConfig).val(),
					selector		: $('.node-selector', $nodeConfig).val(),
					text			: $('.node-text', $nodeConfig).text(),
					relations		: []
				};
				$('.node-relations>.node-relation-list>.selectable-relations>div[rel-name]', $nodeConfig).each(function(){
					var $rel = $(this);
					var rel = {
						id			: $rel.attr('data-id'),
						relationName: $rel.attr('rel-name'),
						title		: $rel.find('.selectable-relation-title').text(),
						criterias	: []
					};
					var criteriaData = $rel.data('criteriaData');
					if(criteriaData){
						rel.criterias = criteriaData;
					}else{
						var initRelConfig = $rel.data('init-rel-config');
						if(initRelConfig){
							$.extend(rel, convertToSaveData(initRelConfig));
						}
					}
					
					node.relations.push(rel);
				});
				ttmpl.nodes.push(node);
			});
			return saveData;
		}
		
		function convertToSaveData(initRelConfig){
			var saveData = {
				criterias	: initRelConfig.criterias
			};
			
			return saveData;
		}
		
		bindColorpicker($('#def-node-color', $page));
		
		
		//根据模板初始数据，初始化页面
		if(ttmplData && ttmplData.nodes){
			for(var i in ttmplData.nodes){
				var node = ttmplData.nodes[i];
				var abcNode = moduleNodeMap[node.moduleName];
				//构造方法参数
				var theNode = {
					abcNode		: abcNode,
					id			: node.id,
					selector	: node.selector,
					nodeText	: node.text,
					relations	: node.relations,
					nodeColor	: node.nodeColor
				}
				//添加Node
				addNode(theNode);
			}
		}
		
	}
	
	function bindColorpicker($texts){
		$texts.each(function(){
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
                },
                theme: 'bootstrap'
            });
		});
	}
});
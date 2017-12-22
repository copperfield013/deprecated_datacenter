seajs.use(['utils', 'dialog', 'bloodhound', 'dialog', 'ajax'], function(Utils, Dialog, Bloodhound, Dialog, Ajax){
	var $page = $('#viewtempl-create');
	var $groupContainer = $('.group-container', $page);
	var $tmplFieldGroup = $('#tmpl-field-group', $page),
		$tmplField = $('#tmpl-field', $page);
	var events = {
		afterRemoveGroup	: $.noop
	}
	var tmplData = {
		groups	: [
		    {
		    	id		: 1,
		    	title	: '基本信息',
		    	fields	: [
		    	    {
		    	    	id		: 1,
		    	    	fieldId	: 14,
		    	    	name	: '姓名',
		    	    	title	: '名字',
		    	    	dv		: 'XXX'
		    	    },
		    	    {
		    	    	id		: 2,
		    	    	fieldId	: 17,
		    	    	name	: '生日',
		    	    	title	: '生日',
		    	    	dv		: 'XXXX-XX-XX'
		    	    },
		    	    {
		    	    	id		: 3,
		    	    	fieldId	: 2,
		    	    	name	: '性别',
		    	    	title	: '性别',
		    	    	dv		: 'XXXX-XX-XX'
		    	    }
		    	]
		    },
		    {
		    	id		: 2,
		    	title	: '工作信息',
		    	fields	: [
		    	    {
		    	    	id		: 4,
		    	    	title	: '职位',
		    	    	dv		: 'XX'
		    	    },
		    	    {
		    	    	id		: 5,
		    	    	title	: '工作地址工作地址工作地址工作地址',
		    	    	dv		: 'XXXXX'
		    	    }
		    	]
		    }
		]
	}
	/**
	 * 初始化某个字段组内的字段容器的拖拽事件
	 */
	function bindGroupFieldsDraggable($fieldContainer){
		$fieldContainer.sortable({
			helper 		: "clone",
			cursor 		: "move",// 移动时候鼠标样式
			opacity		: 0.5, // 拖拽过程中透明度
			placeholder	: function(curr){
				return curr.is('.dbcol')? 
						'field-item-placeholder dbcol'
						: 'field-item-placeholder'  
			},
			tolerance 	: 'pointer'
		});
	}
	
	/**
	 * 切换字段的显示长度
	 */
	function toggleFieldExpand($field, toExpand){
		var $i = $('.toggle-expand-field i', $field);
		Utils.switchClass($i, 'fa-expand', 'fa-compress', toExpand, function(compressed){
			$field.toggleClass('dbcol', !compressed);
		});
	}
	
	
	/**
	 * 动态调整字段标题的行高，使其垂直居中
	 */
	function adjustFieldTitle($titleLabel){
		var $titleSpan = $('<span class="field-title-d">').text($titleLabel.text());
		$titleLabel.empty().append($titleSpan);
		Utils.removeStyle($titleLabel, 'font-size');
		var thisWidth = $titleSpan.width(),
			thisHeight = $titleSpan.height(),
			parentWidth = $titleLabel.width(),
			parentHeight = $titleLabel.height(),
			parentFontsize = parseFloat($titleLabel.css('font-size'));
			;
		var row = Math.ceil(thisWidth / parentWidth);
		var parentLineheight = parentHeight / row;
		if(parentFontsize >= parentLineheight){
			$titleLabel.css('font-size', (parentLineheight - 2) + 'px');
		}
		$titleLabel.css('line-height', (parentLineheight - 1) + 'px');
		
		$titleLabel.text($titleSpan.text());
	}
	
	/**
	 * 绑定双击时，编辑该文本的事件
	 */
	function bindDblClickEdit(selector, inputClass){
		$page.on('dblclick', selector, function(e){
			var $this = $(e.target);
			var title = $this.text();
			var $input = 
				$('<input type="text" />')
				.addClass(inputClass)
				.val(title)
				.keypress(function(e){
					if(e.keyCode === 13){
						confirmTitle();
					}
				})
				.blur(confirmTitle);
			$input.appendTo($this.empty()).select();
			function confirmTitle(){
				var newTitle = $input.val();
				var blankExp = /^\s*$/;
				if(blankExp.test(newTitle)){
					newTitle = title;
				}
				$input.remove();
				$this.text(newTitle);
				if($this.is('.field-title')){
					adjustFieldTitle($this);
				}else if($this.is('.group-title')){
					//焦点放到字段搜索框中
					getLocateGroup($this).find('.search-text-input').select();
					
				}
			}
		});
	}
	
	var __fieldData = null, __compositeData = null, waitFnArray = null;
	/**
	 * 初始化字段数据后执行callback方法
	 */
	function afterLoadFieldData(callback){
		if(!__compositeData){
			if(waitFnArray){
				waitFnArray.push(callback);
				return;
			}else{
				waitFnArray = [callback];
			}
			Ajax.ajax('admin/peopledata/viewtmpl/field_json', {
				
			}, function(data){
				if(data){
					__compositeData = data;
					__fieldData = transferInfoToFields(__compositeData);
					for(var i in waitFnArray){
						(waitFnArray[i] || $.noop)(__fieldData, __compositeData);
					}
					waitFnArray = null;
				}
			});
		}else{
			(callback || $.noop)(__fieldData, __compositeData);
		}
	}
	
	function transferInfoToFields(infoData){
		var fieldData = [];
		for(var i in infoData){
			var thisInfo = infoData[i];
			for(var j in thisInfo.fields){
				var thisField = thisInfo.fields[j];
				fieldData.push({
					id		: thisField.id,
					name	: thisField.name,
					cname	: thisField.cname,
					type	: thisField.type,
					c_id	: thisInfo.id,
					c_name	: thisInfo.name,
					c_cname	: thisInfo.cname
				});
			}
		}
		return fieldData;
	}
	
	/**
	 * 初始化某个字段组的自动完成功能
	 */
	function initGroupFieldSearchAutocomplete($group){
		afterLoadFieldData(function(fieldData){
			//数据源
			var bloodhound = new Bloodhound({
				datumTokenizer 	: Bloodhound.tokenizers.obj.whitespace(['cname', 'c_cname']),
				queryTokenizer 	: Bloodhound.tokenizers.whitespace,
				local			: fieldData
			});
			var $search = $('.glyphicon-search-input', $group); 
			$search.typeahead({
			}, {
				display		: 'cname',
				source 		: bloodhound.ttAdapter(),
				templates	: {
					suggestion	: Handlebars.compile('<p><strong>{{cname}}</strong>-<i>{{c_cname}}</i></p>')
				}
			}).bind('typeahead:select', function(e, suggestion){
				//判断字段在页面中是否存在
				var $existsField = $page.find('.field-item[field-id="' + suggestion.id + '"]');
				if($existsField.length > 0){
					Dialog.notice('该字段已存在，不能重复添加', 'error');
				}else{
					//构造新字段的内容
					var fieldData = {
							title	: suggestion.cname,
							name	: suggestion.cname,
							fieldId	: suggestion.id,
							dv		: 'XXXXX'
					}
					//将字段插入到字段组中
					appendFieldToGroup(fieldData, $group);
					//搜索后将输入框值置空
					$search.typeahead('val', '');
				}
			});
		});
	}
	//初始化字段组容器的拖拽事件
	$groupContainer.sortable({
		helper		: 'clone',
		cursor		: 'move',
		opacity		: 0.5,
		placeholder	: 'group-item-placeholder',
		handle		: '.group-title',
		tolerance 	: 'pointer'
	});
	
	function appendFieldToGroup(fieldData, $group){
		var $fieldContainer = getFieldContainer($group);
		var $field = $tmplField.tmpl(fieldData);
		$field.data('field-data', fieldData).appendTo($fieldContainer);
		adjustFieldTitle($field.find('.field-title'));
		
	}
	/**
	 * 将字段选择器放到搜索框下
	 */
	function appendFieldPicker($group){
		var $fieldSearch = $group.find('.field-search');
		fieldpickerHandler(function($fieldPicker){$fieldSearch.append($fieldPicker.show())});
	}
	
	var __$fieldPicker = null;
	/**
	 * 传入一个函数，获得字段选择器的dom，并对其进行处理
	 */
	function fieldpickerHandler(callback){
		if(!__$fieldPicker){
			afterLoadFieldData(function(fieldData, compositeData){
				__$fieldPicker = $('#tmpl-fieldpicker', $page).tmpl({
					composites	: compositeData
				});
				(callback || $.noop)(__$fieldPicker);
			});
		}else{
			(callback || $.noop)(__$fieldPicker);
		}
	}
	
	
	
	//初始化默认数据
	if(tmplData && tmplData.groups){
		for(var i in tmplData.groups){
			var $group = $tmplFieldGroup.tmpl(tmplData.groups[i]);
			var $fieldContainer = getFieldContainer($group);
			bindGroupFieldsDraggable($fieldContainer);
			for(var j in tmplData.groups[i].fields){
				var field = tmplData.groups[i].fields[j];
				appendFieldToGroup(field, $group);
			}
			$group.data('group-data', tmplData.groups[i]).appendTo($groupContainer);
			initGroupFieldSearchAutocomplete($group);
		}
	}
	/**
	 * 绑定全局事件
	 */
	function bindPageEvent(event, selector, callback){
		$page.bind(event, selector, function(e){
			var $target = $(e.target);
			console.log($target);
			if($target.is(selector)){
				try{
					callback.apply($target, [e]);
				}catch(e){}
				return false;
			}
		});
	}
	
	
	
	/**
	 * 获得某个元素所在的字段最顶层dom
	 */
	function getLocateField($dom){
		return $($dom).closest('.field-item');
	}
	
	/**
	 * 获得某个元素所在的字段组最顶层dom
	 */
	function getLocateGroup($dom){
		return $($dom).closest('.field-group');
	}
	/**
	 * 获得字段组的字段容器dom
	 */
	function getFieldContainer($group){
		return $('.field-container', $group);
	}
	
	//绑定点击添加字段组按钮的事件
	$('#add-group', $page).click(function(){
		var $group = $tmplFieldGroup.tmpl({
					title	: '新字段组'
				}).appendTo($groupContainer);
		//绑定字段组内字段的拖动动作
		bindGroupFieldsDraggable($group)
		//初始化字段组的字段搜索自动完成功能
		initGroupFieldSearchAutocomplete($group);
		//页面滚动到底部
		Utils.scrollTo($page.closest('.cpf-page-content'));
		//触发字段组的标题修改功能
		$group.find('.group-title').trigger('dblclick');
	});
	
	//切换字段的显示长度
	bindPageEvent('click', '.toggle-expand-field i', function(e){
		var $field = getLocateField(this);
		toggleFieldExpand($field);
	});
	
	//删除字段
	bindPageEvent('click', '.remove-field i', function(e){
		var $field = getLocateField(e.target),
			$group = getLocateGroup(e.target),
			fieldTitle = $field.find('.field-title').text(),
			groupName = $group.find('.group-title').text();
		Dialog.confirm('确认在字段组[' + groupName + ']中删除字段[' + fieldTitle + ']？', function(yes){
			if(yes){
				$field.remove();
			}
		});
	});
	//恢复字段名称
	bindPageEvent('click', '.recover-field i', function(e){
		var $field = getLocateField(e.target),
			$fieldTitle = $field.find('.field-title'),
			fieldTitle = $fieldTitle.text(),
			fieldData = $field.data('field-data'),
			fieldName = fieldTitle;
		if(fieldData && fieldData.name){
			fieldName = fieldData.name;
		}
		Dialog.confirm('确认恢复字段[' + fieldTitle + ']为原始名称[' + fieldName + ']？', function(yes){
			if(yes){
				$fieldTitle.text(fieldName);
				adjustFieldTitle($fieldTitle);
			}
		});
	});
	
	//删除字段组
	bindPageEvent('click', '.remove-group i', function(e){
		var $group = getLocateGroup(e.target);
		var groupTitle = $group.find('.group-title').text();
		Dialog.confirm('是否删除字段组[' + groupTitle + ']', function(yes){
			if(yes){
				//移除
				$group.remove();
				events.afterRemoveGroup([$group, $group.attr('data-id'), groupTitle]);
			}
		});
	});
	
	bindPageEvent('click', 'i.field-picker-button', function(e){
		appendFieldPicker(getLocateGroup(e.target));
	});
	
	//双击编辑字段组标题
	bindDblClickEdit('span.group-title', 'group-title');
	//双击编辑字段标题
	bindDblClickEdit('label.field-title', 'field-title');
	bindDblClickEdit('span.field-view', 'field-view');
	//字段的标题初始化，需要延迟，等到页面加载完之后执行
	setTimeout(function(){
		$('.field-title', $page).each(function(){adjustFieldTitle($(this))});
	}, 50);

});
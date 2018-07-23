/**
 * 人口编辑的编辑功能中，用于匹配生成适用于对应字段的表单
 */
define(function(require, exports, module){
	"use strict";
	
	function FieldInput(_param){
		var defaultParam = {
			//字段类型
			//text			： 普通文本
			//textarea		： 长文本
			//select		： 单选下拉框
			//multiselect	: 多选下拉框
			//checkbox		: 多选
			//radio			: 单选
			//date			: 日期选择
			//time			: 时间选择
			//datetime		: 日期时间选择
			//daterange		: 日期范围选择
			//datetimerange	: 日期时间范围选择
			//autocomplete	: 自动完成框
			//label			: 多选标签
			//image			: 图片文件
			//file			: 文件
			type		: 0,
			//表单的name（提交的字段名）
			name		: null,
			//表单的id
			id			: null,
			//默认显示的值
			value		: null,
			//要添加的class
			styleClass	: '',
			readonly	: false,
			//当类型是select、checkbox、radio时，可用的选项
			//数组的元素有三个属性（view/value/attrs)
			options		: null,
			//当没有传入options，但是传入optionKey时，
			//会自动去FieldInput.GLOBAL_OPTIONS中根据该key去获取options
			optionsKey	: null,
			//当没有传入options和optionsKey，并且optionsSet不为空时，会将其转换成options
			optionsSet	: '',
			//用于标记字段，只在类型是label时有效
			fieldKey	: null,
			//已经生成的表单元素，如果传入了该值，那么就不会根据其他参数再生成表单元素
			$dom		: null,
			//检测表单的函数，如果错误，返回错误信息(string)，否则检测成功
			validator	: $.noop,
			$page		: null
		};
		
		var param = $.extend({}, defaultParam, _param);
		var _this = this;
		if(!param.options){
			if(param.optionsKey != undefined){
				param.options = FieldInput.GLOBAL_OPTIONS[param.optionsKey];
			}else{
				if(param.optionsSet){
					param.options = resolveOptionsSet(param.optionsSet);
				}
			}
		}
		function resolveOptionsSet(arg){
			if(typeof arg === 'string'){
				arg = arg.trim();
				try{
					var arr = $.parseJSON(arg);
					if($.isArray(arr)){
						return arr;
					}else{
						$.error();
					}
				}catch(e){
					var matcher = arg.match(/^\[(([^\,]+\,?)*)\]$/);
					if(matcher != null){
						var mainSnippet = matcher[1];
						var reg = /([^\,]+)\,?/g;
						var snippet;
						var arr = [];
						do{
							snippet = reg.exec(mainSnippet);
							if(snippet){
								arr.push(snippet[1].trim());
							}
						}while(snippet);
						for(var i in arr){
							arr[i] = {
								view	: arr[i],
								value	: arr[i]
							}
						}
						return arr;
					}
				}
			}
		}
		/**
		 * 检查构造表单元素的参数是否正常
		 */
		function checkBuildParam(){
		}
		
		function setNormalAttrs($dom){
			if(param.name){
				$dom.attr('name', param.name);
			}
			if(param.id){
				$dom.attr('id', param.id);
			}
			if(param.readonly){
				$dom.attr('readonly', 'readonly');
			}
			$dom.change(function(){
				_this.__triggerValueChanged();
			});
		};
		
		
		var domBuilder = {
			//普通文本框
			'text'			: function(){
				var $text = $('<input type="text" />');
				setNormalAttrs($text);
				if(param.value){
					$text.val(param.value);
				}
				return $text;
			},
			//长文本输入
			'textarea'		: function(){
				var $ta = $('<textarea></textarea>');
				setNormalAttrs($ta);
				if(param.value){
					$ta.val(param.value);
				}
				return $ta;
			},
			//下拉选择框
			'select'		: function(){
				var $select = $('<select>');
				setNormalAttrs($select);
				var $defOption = $('<option value="">--请选择---</option>');
				$select.append($defOption);
				$select.val('');
				if($.isArray(param.options)){
					for(var i in param.options){
						var option = param.options[i];
						if(option.view){
							//构造选项dom
							var $option = $('<option>');
							//显示内容
							$option.text(option.view);
							//设置属性
							if(typeof option.attrs === 'object'){
								$option.attr(option.attrs);
							}
							//设置选项值
							if(option.value){
								$option.attr('value', option.value);
							}
							$select.append($option);
						}
					}
					if(param.value !== undefined && param.value !== ''){
						if($select.find('option[value="' + param.value + '"]').length > 0){
							$select.val(param.value);
						}
					}
				}
				return $select;
			},
			'date'			: function(){
				var $text = this['text']();
				var value = param.value;
				var Utils = require('utils');
				if(typeof value === 'number'){
					value = Utils.formatDate(value, 'yyyy-mm-dd');
				}
				if(typeof value === 'string'){
					$text.val(value);
				}
				$text.attr('readonly', 'readonly');
				var scrollEle = null;
				if(param.$page){
					var page = param.$page.getLocatePage();
					if(page instanceof require('page')){
						scrollEle = page.getContent();
					}
				}
				Utils.datepicker($text, scrollEle);
				return $text;
			},
			'checkbox'		: function(){
				return this['multiselect']('options');
			},
			'label'			: function(){
				return this['multiselect']('labels');
			},
			'multiselect'	: function(source){
				var options = null;
				if(!source){
					if($.isArray(param.options)){
						source = 'options';
					}else if(param.fieldKey){
						source = 'labels';
					}
				}
				
				if(source === 'options'){
					options = param.options;
				}else if(source === 'labels'){
					options = FieldInput.GLOBAL_LABELS[param.fieldKey];
				}
				
				if(options){
					var $div = $('<div>');
					$div.append('<span class="cpf-select-sign cpf-select-sign-and"></span>')
					var $select = $('<select multiple="multiple" >').appendTo($div);
					setNormalAttrs($select);
					if(param.styleClass){
						$select.addClass(param.styleClass);
					}
					var S2 = require('select2');
					if($.fn.select2){
						$select.select2({
							theme	: "bootstrap",
							width	: null,
							data	: options
							
						});
					}
					$div.val = function(value){
						if(value === undefined){
							var v = $select.val();
							if(typeof v === 'string'){
								return v;
							}else if($.isArray(v)){
								return v.join();
							}
						}else if(typeof value === 'string'){
							return $div.val(value.split(','));
						}else if($.isArray(value)){
							$select.val(value).trigger('change');
							return $div;
						}
					}
					if(param.value){
						$div.val(param.value);
					}
					return $div;
				}
			},
			'daterange'		: function(){
				var $start = $('<input type="text" />'),
					$end = $('<input type="text" />'),
					$div = $('<span class="field-input-date-range">');
				$div.append($start);
				$div.append('~');
				$div.append($end);
				require('utils').datepicker($start).on('changeDate', function(){
					var start = $(this).val() || '0-0-0';
					$end.datetimepicker('setStartDate', start);
					_this.__triggerValueChanged();
				});
				require('utils').datepicker($end).on('changeDate', function(){
					var end = $(this).val() || '9999-12-31';
					$start.datetimepicker('setEndDate', end);
					_this.__triggerValueChanged();
				});
				$div.val = function(value){
					if(!value){
						var start = $start.val(),
							end = $end.val();
						return start + '~' + end;
					}else{
						if(typeof value === 'string'){
							var split = value.split('~');
							$start.val(split[0]).trigger('changeDate');
							$end.val(split[1]).trigger('changeDate');
						}
					}
				};
				if(param.value){
					$div.val(param.value);
				}
				return $div;
			},
			'image'			: function(){
				return this['file']({
					accept		: 'image/*'
				});
			},
			'file'			: function(_param){
				var fileParam = $.extend({
					//multiple	: false,	//是否多选
					maxSize		: 2048		//文件不得大于2M
				}, _param);
				
				var $container = $('<span class="cpf-file-input-container cpf-field-input">');
				
				var $thumb = $('<span class="cpf-file-input-thumb">');
				$container.append($thumb);
				
				var originFileURL = null;
				var fileChanged = null;
				var inputFile;
				var $fileInput = $('<input type="file" />').change(function(e){
					var files = e.currentTarget.files;
					if(files && files.length == 1){
						var file = files.item(0);
						setFile({
							file	: file
						});
					}
					$(this).val('');
				});
				var $operates = 
						$('<span class="cpf-file-input-operates">')
						.append($('<i class="fa fa-times">').click(function(){
							require('dialog').confirm('是否移除该文件？', function(yes){
								if(yes){
									showFileChooser();
									fileChanged = true;
								}
							});
						}))
						.append($('<i class="fa fa-download">').click(function(){
							if(originFileURL){
								location.href = originFileURL;
								//require('ajax').download(originFileURL);
							}
						}));
								
				
				function setFile(fileData){
					if(fileData.file){
						//通过浏览器选择的文件
						if(fileData.file.size > fileParam.maxSize * 1024){
							//大小限制
							return showError('文件大小不得超过' + fileParam.maxSize + 'KB');
						}
						if (/^image\/.+$/.test(fileData.file.type)) {
							//图片文件，进行预览
							var reader = new FileReader();
							reader.onload = function(e1) {
								setFile({
									src			: e1.target.result, 
									fileName	: fileData.fileName,
									isLocated	: true
								});
								$operates.find('.fa-download').hide();
							};
							reader.readAsDataURL(fileData.file);
						}else{
							//其他类型文件，展示内置图标
							showUnpicFile(fileData);
						}
						inputFile = fileData.file;
					}else{
						$operates.detach();
						$thumb.empty()
							.append($('<img>')
									.attr('src', fileData.src)
									.attr('alt', fileData.fileName)
									.addClass(fileData.isLocated? 'cpf-file-located': ''))
							.append($operates);
						originFileURL = fileData.src;
						fileChanged = true;
					}
				}
				
				function showUnpicFile(fileData){
					
				}
				
				function showError(msg){
					require('dialog').notice(msg, 'error');
				}
				
				function showFileChooser(){
					originFileURL = null;
					$operates.detach();
					$thumb.empty().append($('<i>').click(function(){
						$fileInput.trigger('click')
					}));
				}
				
				var fileSrc = param.value;
				if(fileSrc){
					setFile({
						src		: fileSrc
					});
				}else{
					showFileChooser();
				}
				fileChanged = false;
				$container.val = function(value){
					if(!value){
						if(inputFile){
							//如果是选择文件后要上传的，则返回文件对象
							return inputFile;
						}else{
							//其他情况下是传入文件的url，
							//那么返回当前控件的文件是否有被修改过的标记
							return {
								src		: originFileURL,
								changed	: fileChanged
							};
						}
					}else{
						//设置值
						if(value instanceof File){
							setFile({
								file	: file
							});
						}else if(typeof value === 'object'){
							setFile(file);
						}else if(typeof value === 'string'){
							setFile({
								url	: value
							});
						}
					}
				};
				$container.funcMap = {
					setDisabled	: function(toDisabled){
						if(toDisabled){
							
						}else{
							
						}
					},
					setReadonly	: function(toReadonly){
						if(toReadonly){
							
						}else{
							
						}
					},
					getSubmitData	: function(){
						return $container.val();
					}
				};
				$container.data('fieldInputObject', this);
				return $container;
				
			}
		};
		
		this.__buildDom = function(){
			checkBuildParam();
			return (param.type && domBuilder[param.type] || function(){return $('<input type="hidden" />')}).apply(domBuilder);
		};
		
		this.getName = function(){
			return param.name;
		}
		
		this.getType = function(){
			return param.type;
		}
		/**
		 * 根据参数生成表单元素（只生成一次）
		 */
		this.getDom = function(){
			if(param.$dom){
				return param.$dom;
			}else{
				return param.$dom = this.__buildDom();
			}
		};
		/**
		 * 获得当前表单的值
		 */
		this.getValue = function(){
			switch(param.type){
				case 'label':
					/*var labels = [];
					this.getDom().find(':checkbox:checked').each(function(){
						labels.push($(this).val());
					});
					return labels;
					break;*/
				case 'select':
				default:
					return this.getDom().val();
					
			}
		};
		
		/**
		 * 手动设置当前表单的值
		 */
		this.setValue = function(val, ignoreTrigger){
			switch(param.type){
				case 'select':
				default: 
					this.getDom().val(val);
			}
			if(!ignoreTrigger){
				this.__triggerValueChanged();
			}
		}
		
		function onSelectFocus(){this.defaultIndex=this.selectedIndex}
		function onSelectChange(){this.selectedIndex=this.defaultIndex}
		function onSelectMousedown(){return false}
		
		this.setDisabled = function(toDisabled){
			var $dom = this.getDom();
			if($dom.funcMap && typeof $dom.funcMap['setDisabled'] === 'function'){
				$dom.func['setDisabled'](toDisabled);
			}else{
				var $inputs = $dom.filter(':input').add($dom.find(':input'));
				if(toDisabled != false){
					$inputs.each(function(){
						var $input = $(this);
						$input
							.attr('disabled', 'disabled')
							.data('disabled-setted', true)
							;
					});
				}else{
					$inputs.each(function(){
						var $input = $(this);
						if($input.data('disabled-setted')){
							$input
								.removeData('disabled-setted')
								.removeAttr('disabled')
								;
						}
					});
				}
			}
		}
		
		this.setReadonly = function(toReadonly){
			var $dom = this.getDom();
			if($dom.funcMap && typeof $dom.funcMap['setReadonly'] === 'function'){
				$dom.func['setReadonly'](toReadonly);
			}else{
				var $inputs = $dom.filter(':input').add($dom.find(':input'));
				if(toReadonly != false){
					$inputs.each(function(){
						var $input = $(this);
						if($input.is('select')){
							$input.on('focus', onSelectFocus)
							.on('change', onSelectChange)
							.on('mousedown', onSelectMousedown)
							.data('readonly-setted', true);
						}else{
							$input
							.attr('readonly', 'readonly')
							.data('readonly-setted', true)
							;
						}
					})
				}else{
					$inputs.each(function(){
						var $input = $(this);
						if($input.data('readonly-setted')){
							if($input.is('select')){
								$input.off('focus', onSelectFocus)
								.off('change', onSelectChange)
								.off('mousedown', onSelectMousedown)
								.removeAttr('readonly-setted');
							}else{
								$input
								.removeData('readonly-setted')
								.removeAttr('readonly')
								;
							}
						}
					});
				}
			}
		}
		var relatedInputs = new Set();
		/**
		 * 关联当前控件和另一个控件，使得两个控件的值同步
		 */
		this.relateInput = function(otherInput){
			if(!relatedInputs.has(otherInput) && otherInput instanceof FieldInput){
				this.getDom().on('field-input-changed', function(e, thisInput){
					otherInput.setValue(thisInput.getValue(), true);
				});
				relatedInputs.add(otherInput);
				otherInput.relateInput(this);
			}
			return this;
		}
		
		this.removeFormName = function(){
			var $dom = this.getDom();
			var $inputs = $dom.filter(':input').add($dom.find(':input')).each(function(){
				var $this = $(this);
				$this.removeAttr('name');
			});
			return this;
		}
		
		/**
		 * 重置表单的值
		 */
		this.resetValue = function(ignoreTrigger){
			this.setValue(param.value);
			if(!ignoreTrigger){
				this.__triggerValueChanged();
			}
		}
		
		this.__triggerValueChanged = function(){
			this.getDom().trigger('field-input-changed', [this]);
		}
		
		/**
		 * 检测当前表单的合法性
		 */
		this.validate = function(){
			param.validator.apply(this, [this.getValue()]);
		}
		
		/**
		 * 
		 */
		this.getComparatorMap = function(callback){
			return FieldInput.getGlobalComparators(param.type, callback);
		}
		
		this.getSubmitData = function(){
			var $dom = this.getDom();
			if($dom && $dom.funcMap && $dom.funcMap.getSubmitData){
				return $dom.funcMap.getSubmitData();
			}
		}
	}
	
	$.extend(FieldInput, {
		globalOptionsCacheTimeLineMap	: {},
		globalOptionsCacheMap			: {},
		/**
		 * 加载全局的选项map
		 * loadGlobalOptions(url, reqParam)：从后台加载所有选项数据
		 */
		loadGlobalOptions	: function(url, reqParam){
			var deferred = $.Deferred();
			var originOptions = FieldInput.GLOBAL_OPTIONS;
			var TIMELINE = 30000;
			if(typeof url === 'string'){
				var timeline = this.globalOptionsCacheTimeLineMap[url];
				var now = (new Date()).getTime();
				if(!timeline || now - timeline > TIMELINE){
					require('ajax').ajax(url, reqParam, function(data){
						FieldInput.loadGlobalOptions(data).done(function(){
							FieldInput.globalOptionsCacheMap[url] = data;
							FieldInput.globalOptionsCacheTimeLineMap[url] = now;
							deferred.resolve([data, originOptions]);
						});
					});
				}else{
					deferred.resolve([FieldInput.globalOptionsCacheMap[url], originOptions]);
				}
			}else if(typeof url === 'object'){
				FieldInput.GLOBAL_OPTIONS = url;
				FieldInput.GLOBAL_LABELS = url.LABELS_MAP;
				FieldInput.globalOptionsLoaded = true;
				deferred.resolve([url, originOptions]);
			}
			return deferred.promise();
		},
		globalOptionsLoaded		: false,
		//全局选项，
		GLOBAL_OPTIONS			: {},
		GLOBAL_LABELS			: {},
		GLOBAL_COMPARATOR_MAP	: null,
		getGlobalComparators	: function(inputType, callback){
			function _callback(){
				(callback || $.noop)(FieldInput.GLOBAL_COMPARATOR_MAP[inputType].comparators);
			}
			if(FieldInput.GLOBAL_COMPARATOR_MAP == null){
				FieldInput.GLOBAL_COMPARATOR_MAP = $.Callbacks();
				FieldInput.GLOBAL_COMPARATOR_MAP.add(_callback);
				require('ajax')
					.loadResource('media/admin/field/json/comparator-map.json')
					.done(function(data){
						var callbacks = FieldInput.GLOBAL_COMPARATOR_MAP;
						FieldInput.GLOBAL_COMPARATOR_MAP = data;
						callbacks.fire();
					});
			}else if(typeof FieldInput.GLOBAL_COMPARATOR_MAP.fire === 'function'){
				FieldInput.GLOBAL_COMPARATOR_MAP.add(_callback);
			}else{
				_callback();
			}
		},
		bindSubmitData			: function(form, formData){
			$(form).find('.cpf-field-input').each(function(){
				var fieldInputObject = $(this).data('fieldInputObject');
				if(fieldInputObject instanceof FieldInput){
					var formName = fieldInputObject.getName();
					if(formName && !formData.has(formName)){
						var submitData = fieldInputObject.getSubmitData(); 
						if(submitData){
							formData.append(formName, submitData);
						}
					}
				}
			});
		}
	});
	
	function getFileIconSrc(file){
		var dotIndex = file.name.indexOf('.');
		if(dotIndex >= 0){
			var suffix = file.name.substring(dotIndex + 1, file.name.length);
			return 
		}
		return 
	}
	
	module.exports = FieldInput;
});
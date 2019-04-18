define(function(require, exports, module){
	
	function requireTemplateMap(){
		return require('tmpl').load('media/jv/entity/tmpl/entity-detail-input.tmpl');
	}
	function DetailInput(_param){
		this.param = $.extend({
			type				: null,
			options				: null,
			fieldId				: null,
			optionsKey			: null,
			$page				: null,
			labelKey			: null,
			$container			: null,
			fieldOptionsFetcher	: null
		}, _param);
		this.dom = null
		this.name = null;
		var _this = this;
		this.ui = {
			tipError	: function($dom, message){
				var $dom = $($dom);
				$dom.tooltip({
					title		: message,
					trigger		: 'manual',
					container	: _this.param.$container,
					template	: '<div class="tooltip field-input-error-tip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
				}).tooltip('show');
				$dom.addClass('field-input-error');
			},
			removeError	: function($dom){
				$dom.removeClass('field-input-error');
				$dom.tooltip('destroy');
			}
		};
		
	}
	
	
	$.extend(DetailInput.prototype, {
		setName		: function(name){
			this.name = name;
		},
		getValue	: function(){
			if(this.valueGetter){
				return this.valueGetter.apply(this, [this.dom]);
			}
		},
		setValue	: function(val){
			if(this.valueSetter){
				return this.valueSetter.apply(this, [this.dom, val]);
			}
		},
		renderDOM	: function(){
			var defer = $.Deferred();
			var _this = this;
			if(!this.dom){
				requireTemplateMap()
					.done(function(tmplMap){
						_this._getTemplateParameter().done(function(tmplParam){
							_this.tmplMap = tmplMap;
							_this.dom = tmplMap[tmplParam.tmplKey].tmpl($.extend({
								fieldType	: _this.param.type,
							}, tmplParam.data), tmplParam.events);
							_this.valueGetter = tmplParam.valueGetter;
							_this.valueSetter = tmplParam.valueSetter;
							tmplParam.afterRender.apply(_this, [_this.dom]);
							defer.resolveWith(_this, [_this.dom]);
						});
						defer.notify('prepared');
					});
			}else{
				defer.resolveWith(_this, [_this.dom]);
			}
			return defer.promise();
		},
		appendToFormData	: function(formData){
			
		},
		_getTemplateParameter	: function(){
			var defer = $.Deferred();
			function prepare(tmplParamFileName, args){
				var tmplParameter = null;
				if(typeof tmplParamFileName === 'object'){
					tmplParameter = tmplParamFileName;
				}else{
					var TemplateParamter = null;
					if(typeof tmplParamFileName === 'function'){
						TemplateParamter = tmplParamFileName;
					}
					tmplParameter = new TemplateParamter(args);
				}
				tmplParameter.prepare(function(){defer.resolve(tmplParameter)}, defer);
			}
			switch(this.param.type){
				case 'text':
					prepare(require('entity/js/tmpl_param/NormalInputTemplateParameter'));
					break
				case 'int':
					prepare(require('entity/js/tmpl_param/IntInputTemplateParameter'));
					break
				case 'decimal':
					prepare(require('entity/js/tmpl_param/DecimalInputTemplateParameter'));
					break;
				case 'date':
					prepare(require('entity/js/tmpl_param/DateInputTemplateParameter'), {
						$container	: this.param.$container
					});
					break;
				case 'select':
					prepare(require('entity/js/tmpl_param/SelectInputTemplateParameter'), {
						optionsKey			: this.param.fieldId,
						options				: this.param.options,
						fieldOptionsFetcher	: this.param.fieldOptionsFetcher
					});
					break;
				case 'preselect':
					prepare(require('entity/js/tmpl_param/SelectInputTemplateParameter'), {
						optionsKey			: this.param.fieldId,
						options				: this.param.options,
						fieldOptionsFetcher	: this.param.fieldOptionsFetcher,
						withoutEmpty		: false,
						tags				: true
					});
					break;
				case 'caselect':
					prepare(require('entity/js/tmpl_param/CaselectInputTemplateParameter'), {
						optionsKey			: this.param.optionsKey,
						$container			: this.param.$container
					});
					break;
				case 'label':
					prepare(require('entity/js/tmpl_param/MultiSelectInputTemplateParameter'), {
						optionsKey			: this.param.fieldId,
						options				: this.param.options,
						fieldOptionsFetcher	: this.param.fieldOptionsFetcher
					});
					break;
				case 'file':
					prepare(require('entity/js/tmpl_param/FileInputTemplateParameter'))
					break;
				default	:
					prepare(new AbstractTemplateParameter());
			}
			return defer.promise();
		}
	});
	
	
	
	function AbstractTemplateParameter(){
		this.tmplKey = 'input-unknown';
		this.data = {};
		this.events = {};
	}
	AbstractTemplateParameter.prototype.prepare = function(resolve){resolve()};
	AbstractTemplateParameter.prototype.valueGetter = function($dom){};
	AbstractTemplateParameter.prototype.valueSetter = function($dom){};
	AbstractTemplateParameter.prototype.afterRender = function($dom){};
	AbstractTemplateParameter.prototype.setReadonly = function($dom){};
	AbstractTemplateParameter.prototype.setDisabled = function($dom){};
	AbstractTemplateParameter.prototype.isValueChanged = function($dom){return true};
	AbstractTemplateParameter.prototype.validate = function($span, ui){};
	
	DetailInput.AbstractTemplateParameter = AbstractTemplateParameter;
	
	module.exports = DetailInput;
});
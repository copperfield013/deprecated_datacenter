define(function(require, exports, modules){
	
	/**
	 * 
	 */
	function CriteriaRenderer(_param){
		var defParam = {
			$tmpl		: null,
			tCriteria	: null,
			$formGrouTmpl	: null,
			options		: null
		};
		var param = $.extend({}, defParam, _param);
		var criteriaName = 'criteria_' + param.tCriteria.id;
		
		this.render = function(renderParam){
			var $input = param.$tmpl.tmpl({
				criteria	: param.tCriteria,
				name		: criteriaName,
				options		: param.options
			});
			var $formGroup = param.$formGrouTmpl.tmpl({
				criteria	: param.tCriteria
			});
			$formGroup.append($input);
			
			bindInputValFunction($formGroup, $input);
			switch(typeof renderParam){
				case 'string':
					$formGroup.data(FORM_GROUP_VALUE_FUNC_KEY)(renderParam);
					break;
				default:
			}
			
			return $formGroup;
		}

		var FORM_GROUP_VALUE_FUNC_KEY = 'input-value-func';
		function bindInputValFunction($formGroup, $input){
			switch(param.tCriteria.inputType){
				case 'text':
				case 'select':
				case 'multiselect':
				default:
					$formGroup.data(FORM_GROUP_VALUE_FUNC_KEY, $input.val.bind($input));
					
			}
		}
	}
	
	exports.getRenderer = function(tCriteria, options){
		var defer = $.Deferred();
		require('tmpl').load('media/jv/entity/tmpl/criteria-renderer-factory.tmpl').done(function(tmplMap){
			var $tmpl = tmplMap[tCriteria.inputType];
			if(!$tmpl){
				$tmpl = tmplMap['unknown'];
			}
			var renderer = new CriteriaRenderer({
				$tmpl		: $tmpl,
				tCriteria	: tCriteria,
				$formGrouTmpl	: tmplMap['form-group'],
				options		: options
			});
			defer.resolve(renderer);
		});
		return defer.promise();
	}
});

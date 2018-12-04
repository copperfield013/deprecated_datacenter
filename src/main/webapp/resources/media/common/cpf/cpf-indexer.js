/**
 * 
 */
define(function(require, exports, module){
	function Indexer(_param){
		var defParam = {
			//用于设置scrollTop位置的窗口dom对象
			scrollTarget: null,
			//要生成索引的元素数组，元素的对象类型不限。
			//如果元素对象有titleGetter方法或者offsetGetter方法，那么会覆盖通用方法
			elements	: [],
			//从elements中的元素获得索引标题的通用方法
			titleGetter	: $.noop,
			//从elements中获得offsetTop的通用方法
			offsetGetter: $.noop,
			//是否显示控制按钮
			button		: true,
			//按钮行为，可选"hide"和"close"
			buttonAction: 'hide',
			//窗口滚动方法
			scrollTo	: function(scrollTarget, offset){scrollTarget.scrollTop = offset},
			//elements的offset是否是有序的，此处关系到根据位置查找元素的算法，尽量传入有序的elements
			sorted		: true
		};
		
		var param = $.extend({}, defParam, _param);
		
		
		var $container = null;
		var _this = this;
		this.get = function(index){
			return param.elements[index];
		}
		this.getElementOffset = function(index){
			var ele = this.get(index);
			if(ele){
				return getElementOffset(ele);
			}
		}
		
		function getElementOffset(ele){
			if(ele){
				return ele.offsetGetter.apply(ele.element, [ele.element, ele, param.elements]);
			}
		}
		
		this.go = function(index){
			var data = index;
			if(typeof index === 'number'){
				data = this.get(index);
			}
			if(data.offsetGetter){
				var viewHeight = param.scrollTarget.offsetHeight;
				var scrollHeight = param.scrollTarget.scrollHeight;
				var maxScroll = scrollHeight - viewHeight;
				var offset = getElementOffset(data);
				if(offset > maxScroll){
					//超过最大位置
					var offsetElements = getMaxOffsetElements();
					for(var i = 0; i < offsetElements.length; i++){
						if(data === offsetElements[i].ele){
							offset = offsetElements[i].maxOffset;
							break;
						}
					}
				}
				param.scrollTo(param.scrollTarget, offset);
				
			}else{
				$.error('不能解析元素[参数index=' + index + ']');
			}
			
		}
		
		function showActive(element){
			if(element){
				$container.find('.index-title-list>p.current').removeClass('current');
				$(element.dom).addClass('current');
			}
		}
		
		this.getContainer = function(){
			if($container == null){
				$container = this.createContainer();
			}
			return $container
		}
		
		
		this.createContainer = function(){
			var $container = $('<div class="index-area">');
			if(param.button){
				var $btn = null;
				switch(param.buttonAction){
				case 'hide':
					$btn = $('<span class="btn-hide">')
					break;
				case 'close':
					$btn = $('<span class="btn-close">')
					break;
				}
				$container.append($btn);
			}
			var $wrapper = $('<div class="index-title-wrapper">');
			var $list = $('<div class="index-title-list">');
			syncFromElements(param.elements, $list);
			bindScrollEvent();
			return $container.append($wrapper.append($list));
		}
		
		this.triggerScroll = function(){
			$(param.scrollTarget).trigger('scroll');
		}
		
		function syncFromElements(elements, $list){
			$list.empty();
			var datas = [];
			for(var i = 0; i < elements.length; i++){
				var ele = elements[i];
				if(ele != null){
					var data = {element: ele};
					data.titleGetter = 
						typeof ele.titleGetter === 'function'? 
								ele.titleGetter
								: param.titleGetter;
					data.offsetGetter = 
						typeof ele.offsetGetter === 'function'? 
								ele.offsetGetter
								: param.offsetGetter;
					datas.push(data);
				}
			}
			
			for(var i in datas){
				var data = datas[i];
				var $p = $('<p>');
				if(typeof data.titleGetter === 'function'){
					$p.text(data.titleGetter.apply(data.element, [data.element, data, datas]));
				}else{
					$p.text('');
				}
				data.dom = $p;
				$p.data('indexer-data', data);
				$p.click(bindElementClickEvent);
				$list.append($p);
			}
			param.elements = datas;
		}
		function bindElementClickEvent(){
			_this.go($(this).data('indexer-data'));
		}
		function bindScrollEvent(){
			$(param.scrollTarget).on('scroll', function(e){
				var scrollTop = e.target.scrollTop;
				var element = findElementByOffset(scrollTop);
				showActive(element);
			});
		}
		
		
		function getMaxOffsetElements(offset){
			//计算最后n个无法占满页面的元素的高度
			var viewHeight = param.scrollTarget.offsetHeight;
			var scrollHeight = param.scrollTarget.scrollHeight;
			var maxScroll = scrollHeight - viewHeight;
			var invertElements = [];
			var hasOffset = typeof offset === 'number';
			for(var i = param.elements.length - 1; i > 0; i--){
				var ele = param.elements[i];
				var eleOffset = getElementOffset(ele);
				var eleHeight = 0;
				if(invertElements.length === 0){
					eleHeight = scrollHeight - eleOffset;
				}else{
					eleHeight = invertElements[invertElements.length - 1].offset - eleOffset;
				}
				invertElements.push({ele:ele, offset:eleOffset, height: eleHeight});
				if(eleOffset < maxScroll){
					break;
				}
			}
			var keyElement = invertElements[invertElements.length - 1];
			if(!hasOffset || offset > keyElement.offset){
				//只有当前的位置已经超过最后一个可以越出窗口的元素位置时，才进行分配
				if(invertElements.length > 1){
					var lastRetainHeight = maxScroll - keyElement.offset;
					//将最后一个能越出窗口的元素最多能越出窗口的高度进行分配，根据各个元素的高度比例进行分配
					var totleHeight = scrollHeight - invertElements[invertElements.length - 2].offset;
					
					var sumHeight = 0;
					for(var i = invertElements.length - 2; i >= 0; i--){
						var ele = invertElements[i];
						sumHeight += ele.height;
						var maxOffset = keyElement.offset + Math.ceil(lastRetainHeight * sumHeight / totleHeight);
						ele.maxOffset = maxOffset;
						if(hasOffset && offset <= maxOffset){
							return ele.ele;
						}
					}
					
				}
			}
			if(!hasOffset){
				return invertElements.reverse();
			}
		}
		
		function findElementByOffset(offset){
			if(param.sorted === true){
				if(param.elements.length > 0){
					var lastElement = getMaxOffsetElements(offset);
					if(lastElement){
						return lastElement;
					}
					for(var i = 0; i < param.elements.length; i++){
						var ele = _this.get(i);
						var thisElementOffset = getElementOffset(ele);
						if(offset <= thisElementOffset){
							return ele;
						}else if(i < param.elements.length - 1){
							var nextElementOffset = getElementOffset(_this.get(i + 1));
							if(nextElementOffset > offset + param.scrollTarget.offsetHeight){
								return ele;
							}
						}
					}
				}
				
				
			}
			
			
		}
		
	}
	
	module.exports = Indexer;
	
		
});
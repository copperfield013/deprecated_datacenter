/**
 * 
 */
define(function(require, exports, module){
	var $CPF = require('$CPF'),
		utils = require('utils')
		;
	
	$CPF.addDefaultParam({
		//是否在ajax请求时检测返回session状态
		ajaxSessionValid	: true,
		//当session无效的时候，ajax请求返回后需要跳转的地址
		sessionInvalidURL	: ''
	});
	
	/**
	 * 返回json数据时，将其转换成AjaxPageResponse类对象
	 */
	function AjaxPageResponse(_data){
		var Page = require('page');
		var defaultResponseData = {
			//当前页面的处理方式(close:关闭;refresh:重新加载;redirect:url：跳转)
			localPageAction	: '',
			localPageRedirectURL	: '',
			//要处理响应数据的页面id
			targetPageId	: '',
			//处理方式(refresh/redirect,默认refresh，传入url时则为redirect）
			targetPageAction: '',
			//打开的页面标题
			targetPageTitle	: '',
			targetPageRedirectURL	: '',
			//如果没有找到targetPageId对应的Page，将以什么方式打开页面(redirect下)（dialog/tab,默认dialog）
			targetPageType	: '',
			//响应状态
			status			: '',
			//响应的提示语
			notice			: '',
			//响应提示语的类型
			noticeType		: ''
			
		};
		var data = $.extend({}, defaultResponseData, _data);
		data.localPageAction = data.localPageAction && data.localPageAction;
		data.targetPageAction = data.targetPageAction && data.targetPageAction;
		data.targetPageType = data.targetPageType && data.targetPageType;
		data.noticeType = data.noticeType && data.noticeType;
		this.getLocalPageAction = function(){
			return data.localPageAction;
		};
		this.getLocalPageRedirectURL = function(){
			return data.localPageRedirectURL;
		};
		
		this.getTargetPageId = function(){
			return data.targetPageId;
		};
		
		this.getTargetPageAction = function(){
			return data.targetPageAction;
		};
		this.getTargetPageRedirectURL = function(){
			return data.targetPageRedirectURL;
		};
		
		this.getTargetPageTitle = function(){
			data.targetPageTitle;
		};
		
		this.getTargetPageType = function(){
			return data.targetPageType
		};
		this.getNotice = function(){
			return data.notice;
		};
		this.getNoticeType = function(){
			return data.noticeType;
		};
		
		this.doAction = function(page){
			if(page instanceof $){
				page = page.getLocatePage();
			}
			var localPageAction = this.getLocalPageAction();
			if(localPageAction && page instanceof Page){
				//处理当前页面
				_doAction(localPageAction, page, null, this.getLocalPageRedirectURL());
			}
			var tPageId = this.getTargetPageId(),
				tPageAction = this.getTargetPageAction()
				;
			if(tPageId){
				var tPage = Page.getPage(tPageId);
				if(tPage instanceof Page){
					_doAction(tPageAction, tPage, this.getTargetPageTitle(), this.getLocalPageRedirectURL());
					if(localPageAction === 'close' && tPage.getType() === 'tab'){
						tPage.getPageObj().activate();
					}
				}
			}
			var notice = this.getNotice(),
				noticeType = this.getNoticeType()
				;
			if(notice && noticeType){
				var Dialog = require('dialog');
				if(Dialog){
					Dialog.notice(notice, noticeType);
				}
			}
		}
		
	}
	var REDIRECT_KEY = 'redirect';
	function _doAction(action, page, title, url){
		if(action === 'refresh'){
			page.refresh();
		}else if(action === REDIRECT_KEY){
			page.loadContent(url, title);
		}else if(action === 'close'){
			page.close();
		}
	}
	
	
	
	function ajax(url, formData, whenSuc, _param){
		var defaultParam = {
			//提交类型
			method		: 'POST',
			//当提交请求成功
			whenSuc		: $.noop,
			//当提交请求失败
			whenErr		: $.noop,
			//提交请求无论成功或者失败都会执行
			afterLoad	: $.noop,
			//当前页面
			page		: undefined,
			//
			interval	: 0
		};
		if(typeof formData === 'function'){
			if($.isPlainObject(whenSuc)){
				_param = whenSuc;
			}else{
				_param = {};
			}
			whenSuc = formData;
			formData = _param.whenSuc;
		}else if($.isPlainObject(whenSuc)){
			_param = whenSuc;
			whenSuc = _param.whenSuc;
		}else{
			_param = {} || _param;
		}
		if($.isPlainObject(_param)){
			_param.whenSuc = whenSuc;
		}
		//继承获得参数
		var param = $.extend({}, defaultParam, _param);
		
		var fData = new FormData();
		if($.isPlainObject(formData)){
			for(var key in formData){
				var name = key;
				if($.isArray(formData[key])){
					for(var i in formData[key]){
						fData.append(name, formData[key][i]);
					}
				}else{
					fData.append(name, formData[key]);
				}
			}
		}else if(formData instanceof FormData){
			fData = formData;
		}
		
		return $.ajax({
		    url: 		url,
		    type: 		param.method,
		    cache: 		false,
		    data: 		fData,
		    processData: false,
		    contentType: false,
		    beforeSend	: function(){
		    	console.log(arguments);
		    },
		    headers		: {
		    	'request-category'	: 'cpf-ajax'
		    },
		    success		: function(data, status, jqXHR){
		    	commonHandleSucAjax(data, status, jqXHR);
		    	var resContentType = utils.trim(jqXHR.getResponseHeader("Content-Type"));
		    	if(/^.+\/json;.+$/.test(resContentType)){
		    		//返回的数据是Json格式的数据
		    		try{
		    			var json = data;
		    			if(typeof json === 'string'){
		    				json = $.parseJSON(json)
		    			}
		    			if(json && json['ajax_page_response'] === 'cpf'){
		    				var jRes = new AjaxPageResponse(json);
		    				var result = param.whenSuc(jRes, 'json');
		    				if(result !== false){
		    					jRes.doAction(param.page);
		    				}
		    			}else{
		    				param.whenSuc(json, 'json');
		    			}
		    		}catch(e){
		    			console.error(e);
		    		}
		    	}else if(/^.+\/html;.+$/.test(resContentType)){
		    		//返回的数据是html
		    		param.whenSuc(data, 'html');
		    	}else{
		    		console.log(resContentType);
		    		param.whenSuc(data, jqXHR);
		    	}
		    },
		    error		: function(jqXHR, textStatus, errorThrown){
		    	console.error(textStatus);
		    	var errResult = param.whenErr();
		    	var Dialog = require('dialog');
		    	if(errResult !== false){
		    		if(Dialog){
		    			Dialog.notice('请求时发生错误', 'error');
		    		}else{
		    			console.error('请求时发生错误');
		    		}
		    	}
		    }
		}).always(function(res) {
			param.afterLoad(res);
		});
	}
	
	/**
	 * 以post的方式将obj转换成json字符串，并发送到后台
	 * 后台的控制器需要支持application/json;charset=utf-8的头信息
	 * @param url {String} 请求地址
	 * @param obj {PlainObject} 请求的数据对象，会被转换成json，因此必须是纯粹对象
	 * @param done {Function} 请求成功后的回调函数，有一个参数，是已经对象化的json对象
	 */
	function postJson(url, obj, done){
		$.ajax({
			//提交的地址
			url		: url,
			method	: 'POST',
			dataType: 'json',
			headers	: {
				'content-type'	: 'application/json;charset=utf-8'
			},
			data	: JSON.stringify(obj)
		}).done(function(data, textStatus, jqXHR){
			commonHandleSucAjax(data, textStatus, jqXHR);
			var json = data;
			if(typeof json === 'string'){
				try{
					json = $.parseJSON(json);
				}catch(e){}
			}
			done.apply(this, [json, 'done', arguments]);
		}).fail(function(jqXHR, textStatus, errorThrown){
			done.apply(this, [null, 'fail', arguments]);
		});
	}
	
	/**
	 * 用get方法ajax获取资源
	 */
	function loadResource(url, reqParam, ajaxParam){
		var deferred = $.Deferred();
		ajax(url, reqParam, $.extend({}, {
			method	: 'get',
			whenSuc	: function(data){
				try{
					data = $.parseJSON(data);
				}catch(e){}
				deferred.resolve(data);
			}
		}, ajaxParam));
		return deferred.promise();
	}
	
	/**
	 * 轮询查询当前进度
	 */
	function poll(_param){
		var defaultParam = {
			startupURL				: '',
			progressURL				: '',
			startupReqParameters	: {},
			uuidResponseName		: 'uuid',
			uuidRequestName			: 'uuid',
			//提交进度获取时的方法
			progressReqParameters	: function(startupRes, uuid){},
			//进度值的获取方法
			progressGetter			: function(res){
				return res.current/res.totalCount;
			},
			//进度值的最大值
			progressMax				: 1,
			//进度完成时调用
			whenComplete			: function(res){},
			//当
			whenUnsuccess			: function(res){}
		};
		var param = $.extend({}, defaultParam , _param);
		Ajax.ajax(param.startupURL, param.startupParameters, function(data){
			var uuid = data[uuidName];
			if(uuid){
				var statusTimer = setInterval(function(){
					var parameters = {};
					if(typeof param.progressReqParameters === 'function'){
						var p = param.progressReqParameters.apply(param, [data, uuid]);
						
					}
					Ajax.ajax(param.progressURL, parameters, function(res){
						if(res.status === 'suc'){
							var progress = param.progressGetter.apply(param, [res]);
							progress = progress > param.progressMax? param.progressMax: progress;
							try{
								param.propgressHandler.apply(param, [progress, res]);
							}catch(e){}
							if(progress == param.progressMax){
								clearInterval(statusTimer);
								param.whenComplete.apply(param, [res]);
							}
						}else{
							try{
								if(param.param.whenUnsuccess.apply(param, [res]) !== false){
									clearInterval(statusTimer);
								}
							}catch(e){clearInterval(statusTimer)}
						}
					});
				}, 1000);
			}
		});
		
	}
	
	function commonHandleSucAjax(data, textStatus, jqXHR){
		//处理session超时
		if($CPF.getParam('ajaxSessionValid')){
			var sessionStatus = jqXHR.getResponseHeader('cpf-session-status');
			if(sessionStatus === 'invalid'){
				location.href = $CPF.getParam('sessionInvalidURL');
			}
		}
	}
	
	function download(url){
		var $downloadFrame = $('#cpf-download-frame');
		if($downloadFrame.length === 0){
			$downloadFrame = $('<iframe>');
			$downloadFrame.attr('width', 0).attr('height', 0).css('display', 'none');
			$downloadFrame.attr('src', url);
			$downloadFrame.appendTo(document.body);
		}else{
			$downloadFrame[0].contentWindow.location.href = url;
		}
		
	}
	
	exports.ajax = ajax;
	exports.postJson = postJson;
	exports.AjaxPageResponse = AjaxPageResponse;
	exports.download = download;
	exports.loadResource = loadResource;
});
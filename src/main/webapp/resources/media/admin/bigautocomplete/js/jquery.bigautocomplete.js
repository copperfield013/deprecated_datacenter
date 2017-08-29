(function($){
	var bigAutocomplete = new function(){
		this.currentInputText = $(".search");//目前获得光标的输入框（解决一个页面多个输入框绑定自动补全功能）
		this.functionalKeyArray = [9,20,13,16,17,18,91,92,93,45,36,33,34,35,37,39,112,113,114,115,116,117,118,119,120,121,122,123,144,19,145,40,38,27];//键盘上功能键键值数组
		this.holdText = null;//输入框中原始输入的内容
		
		//初始化插入自动补全div，并在document注册mousedown，点击非div区域隐藏div
		this.init = function(){
			$("body").append("<div id='bigAutocompleteContent' class='bigautocomplete-layout'></div>");
			$(document).bind('mousedown',function(event){
				var $target = $(event.target);
				if((!($target.parents().andSelf().is('#bigAutocompleteContent'))) && (!$target.is(bigAutocomplete.currentInputText))){
					bigAutocomplete.hideAutocomplete();
				}
			})
			
			//鼠标悬停时选中当前行
			$("#bigAutocompleteContent").delegate("tr", "mouseover", function() {
				$("#bigAutocompleteContent tr").removeClass("ct");
				$(this).addClass("ct");
			}).delegate("tr", "mouseout", function() {
				$("#bigAutocompleteContent tr").removeClass("ct");
			});		
			
			
			//单击选中行后，选中行内容设置到输入框中，并执行callback函数
			$("#bigAutocompleteContent").delegate("tr", "click", function() {
				bigAutocomplete.currentInputText.val( $(this).find("div:last").html());			
				bigAutocomplete.hideAutocomplete();
			})			
			
		}
		
		this.autocomplete = function(param){
			
			if($("body").length > 0 && $("#bigAutocompleteContent").length <= 0){
				bigAutocomplete.init();//初始化信息
			}			
			
			var $this = $(this);//为绑定自动补全功能的输入框jquery对象
			
			var $bigAutocompleteContent = $("#bigAutocompleteContent");
			
			this.config = {
			               //width:下拉框的宽度，默认使用输入框宽度
			               width:$this.outerWidth() - 2,
			               //url：格式url:""用来ajax后台获取数据，返回的数据格式为data参数一样
			               url:null,
			               /*data：格式{data:[{title:null,result:{}},{title:null,result:{}}]}
			               url和data参数只有一个生效，data优先*/
			               data:null,
			               //callback：选中行后按回车或单击时回调的函数
			               callback:null};
			$.extend(this.config,param);
			
			$this.data("config",this.config);
			
					var config = $this.data("config");
					
					var offset = $this.offset();
					$bigAutocompleteContent.width(config.width);
					var h = $this.outerHeight() - 1;
					$bigAutocompleteContent.css({"top":offset.top + h,"left":offset.left});
					
					var data = config.data;
					var url = config.url;
					var keyword_ = $.trim($this.val());
					if(keyword_ == null || keyword_ == ""){
						bigAutocomplete.hideAutocomplete();
						return;
					}	
					
					if((data != null||data!="" )&& $.isArray(data) ){
						makeContAndShow(data);
					}
					
					bigAutocomplete.holdText = $this.val();

			
					
			//组装下拉框html内容并显示
			function makeContAndShow(data_){
				if(data_ == null || data_.length <=0 ){
					bigAutocomplete.hideAutocomplete();
					return;
				}
				var cont = "<table><tbody>";
				
					for(var i=0;i<data_.length;i++){
						cont += "<tr><td><div id="+data_[i].title_en+">" + data_[i].title + "</div></td></tr>"
					}
				
				
				cont += "</tbody></table>";
				$bigAutocompleteContent.html(cont);
				$bigAutocompleteContent.show();
				//每行tr绑定数据，返回给回调函数
				$bigAutocompleteContent.find("tr").each(function(index){
					$(this).data("jsonData",data_[index]);
				})
			}			
					
			
			//输入框focus事件
			$this.focus(function(){
				console.log($this)
				bigAutocomplete.currentInputText = $this;
			});
			
		}
		//隐藏下拉框
		this.hideAutocomplete = function(){
			var $bigAutocompleteContent = $("#bigAutocompleteContent");
			if($bigAutocompleteContent.css("display") != "none"){
				$bigAutocompleteContent.find("tr").removeClass("ct");
				$bigAutocompleteContent.hide();
			}			
		}
		
	};
	
	
	$.fn.bigAutocomplete = bigAutocomplete.autocomplete;
	
})(jQuery)
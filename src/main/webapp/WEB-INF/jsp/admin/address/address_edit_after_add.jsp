<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link href="media/admin/selectiontest/selectiontest.css" rel="stylesheet" type="text/css" />
<script src="media/admin/selectiontest/selectiontest.js"></script>
<style>
	#address-edit-after-add {
		padding-left: 20px;
	}
	#address-edit-after-add .address-add-level,
	#address-edit-after-add .address-remove-level {
		display: inline-block;
		width: 24px;
		height: 24px;
		text-align: center;
		line-height: 24px;
		border: 1px solid #d9d9d9;
		color:#656565;
		font-size: 14px;
		margin-right: 10px;
		border-radius: 2px;
		vertical-align: middle;
		cursor: pointer;
	}
	#address-edit-after-add .address-add-level {
		margin-left: 20px;
	}
	#address-edit-after-add .address-add-level:hover,
	#address-edit-after-add .address-remove-level:hover {
		border: 1px solid #126def;
		color: #126def;
	}
	#address-edit-after-add .address-participle-item {
		height: 30px;
		margin-bottom: 10px;
	}
	#address-edit-after-add .selection-button {
		height: 30px;
		line-height:30px;
	}
	#address-edit-after-add .address-participle-inblock {
		height: 30px;
		display:inline-block;
		vertical-align: middle;
		margin-right: 10px;
	}
	#address-edit-after-add .address-participle-participle{
		width: 150px;
	}
	#address-edit-after-add .address-participle-end {
		width: 80px;
	}
	#address-edit-after-add .address-line {
		margin-top: 10px;
		font-size: 0;
	}
	#address-edit-after-add .address-line-label {
		font-size: 14px;
		color: #656565;
		margin-right: 20px;
	}
	#address-edit-after-add .basic-input {
		font-size: 14px;
		color: #656565;
	}
	#address-edit-after-add .address-line-content {
		font-size: 14px;
		color: #656565;
		background-color: #ffffff;
	}
	#address-edit-after-add .edit-participle {
		margin-top: 30px;
	}
	#address-edit-after-add .edit-participle:after {
		content: "";
		display: block;
		clear: both;
	}
	#address-edit-after-add .edit-participle-label {
		float:left;
	}
	#address-edit-after-add .edit-participle-content {
		float: left;
	}
	#address-edit-after-add .address-button {
		margin: 50px 0 0 78px;
		font-size: 0;
	}
	#address-edit-after-add .address-button>span {
		width: 100px;
		text-align: center;
		height: 30px;
		line-height: 30px;
		font-size: 14px;
		border: 1px solid #d9d9d9;
		color: #656565;
		display: inline-block;
		cursor: pointer;
	}
	#address-edit-after-add .address-button>span.address-button-submit {
		color: #ffffff;
		background-color: #126def;
		margin-left: 30px;
	}
	#address-edit-after-add .address-button>span.address-button-submit:hover {
		background-color: #4b90f3;
	}
</style>
<div id="address-edit-after-add">		
	<h1 class="zpage-title">修改地址分词</h1>
	<div class="zpage-body">
				<form id="address-edit-after-add-form" action="admin/address/doEdit">
					<input type="hidden" id="code" name="splitedAddressEntity.code" value="${splitedAddressEntity.code }"/>
					<input type="hidden" id="splitName" name="splitedAddressEntity.splitName" value="${splitedAddressEntity.splitName }"/>
					<input type="hidden" id="positionCode" name="splitedAddressEntity.positionCode" value="${splitedAddressEntity.positionCode }"/>
					<input type="hidden" id="keyPoint" name="splitedAddressEntity.keyPoint" value="${splitedAddressEntity.keyPoint }"/>
					<input type="hidden" id="laterPart" name="splitedAddressEntity.laterPart" value="${splitedAddressEntity.laterPart }"/>
						<div class="address-name address-line">
							<span class="address-name-label address-line-label" disabled="true">地址名称：</span>
							<input type="text" class="address-line-content" name="splitedAddressEntity.name" style="border:0;width:85%; display:inline-block" value="${splitedAddressEntity.name }" readonly="readonly"/>
						</div>						
						<div class="symbol-participle address-line">
							<span class="symbol-participle-label address-line-label">系统分词：</span>
							<c:forEach items="${list }" var="addressElement" varStatus="i">							
								<span class="symbol-participle-content address-line-content">${addressElement.content }(${addressElement.termWord })；</span>
							</c:forEach>
							
						</div>
						<div class="edit-participle address-line">
							<span class="edit-participle-label address-line-label">编辑分词</span>
							<div class="edit-participle-content">
								<c:forEach items="${list }" var="addressElement" varStatus="i">
									<div class="address-participle-item">
										<div class="address-participle-inblock">
											<select class="level" name="elements[${i.index }].level">
												<c:forEach items="${levelNameMap }" var="levelName">											
													<option value="${levelName.key }" ${addressElement.level==levelName.key?'selected':''}>${levelName.value }</option>											
												</c:forEach>
											</select>
										</div>
										<input type="text" class="basic-input content address-participle-inblock address-participle-input" name="elements[${i.index }].content" value="${addressElement.content }"/>
										<%-- <input type="hidden" name="elements[${i.index }].level" value="${addressElement.level }"/> --%>
										<input type="text" class="basic-input address-participle-inblock address-participle-end" name="elements[${i.index }].termWord" value="${addressElement.termWord }"/>
										<c:if test="${addressElement.level != 0 && addressElement.level != 1 && addressElement.level != 2 && addressElement.level != 3 && addressElement.level != 4}">
											<span class="address-add-level">+</span>
											<span class="address-remove-level">-</span>
										</c:if>									
										<input type="hidden" name="elements[${i.index }].positionCode" value="${addressElement.positionCode }"/>
									</div>
								</c:forEach>
							</div>
						</div>								
			        	<div class="address-button">
			        		<span class="address-button-cancel">取消</span>
			        		<span class="address-button-submit">提交</span>
			        		<!-- <input id="address-edit-after-add-submit-btn" class="btn btn-block btn-darkorange" type="button" value="提交" /> -->
				        </div>
				</form>
	</div>
</div>

<script>
	seajs.use(['dialog','utils'], function(Dialog, Utils){
		var $page = $('#address-edit-after-add');
		
		$('.level',$page).Selection({
	        width: "180px",
	    });
		
		$('.address-add-level').on('click',function(){
			
			var THISITEM = $(this).closest('.address-participle-item');	
			var SELECTED = THISITEM.find('select').attr("data-value");				  //选中项
			var ADDSELECTNAME = THISITEM.find('select').attr("name");
			var ADDSELECTLENGTH = ADDSELECTNAME.length;
			var ADDOPTIONS = THISITEM.find('select').html();						  //该select 下的选项html
	 		var WORDNAME = THISITEM.find('input.content').attr("name");               //分词input name属性
	 		var WORDLENGTH = WORDNAME.length;	
	 		var ENDNAME = THISITEM.find('input.address-participle-end').attr("name"); //结束词name属性
	 		var ENDLENGTH = ENDNAME.length;
	 		var HIDDENNAME = THISITEM.find('input[type=hidden]').attr("name");
	 		var HIDDENLENGTH = HIDDENNAME.length;
			var ADDSELECTNAME = THISITEM.find('select').attr("name")
			var COMINDEX = parseFloat(WORDNAME.substr(9,1)) + 1;                      //name中的数字 ，通用

			
			ADDSELECTNAME = ADDSELECTNAME.substr(0,9) + COMINDEX + ADDSELECTNAME.substring(10,ADDSELECTLENGTH);
			WORDNAME = WORDNAME.substr(0,9) + COMINDEX + WORDNAME.substring(10,WORDLENGTH);
			ENDNAME = ENDNAME.substr(0,9) + COMINDEX + ENDNAME.substring(10,ENDLENGTH);
			HIDDENNAME = HIDDENNAME.substr(0,9) + COMINDEX + HIDDENNAME.substring(10,HIDDENLENGTH);
			
			
			
			var NEXTELELIST = $(this).closest('.address-participle-item').nextAll();
			
			for(var i = 0; i<NEXTELELIST.length;i++){   //顺次修改之后的select的name属性
				var SELECTNAME = $(NEXTELELIST[i]).find('select').attr("name");
				var wordName = $(NEXTELELIST[i]).find('input.content').attr("name");
				var endName = $(NEXTELELIST[i]).find('input.address-participle-end').attr("name");
				var hiddenName = $(NEXTELELIST[i]).find('input[type=hidden]').attr("name");
				
				var INDEX = parseFloat(SELECTNAME.substr(9,1)) + 1;
				var SELECTLENGTH = SELECTNAME.length;
				var wordLength = wordName.length;
				var endLength = endName.length;
				var hiddenLength = hiddenName.length;
				
				SELECTNAME = SELECTNAME.substr(0,9) + INDEX + SELECTNAME.substring(10,SELECTLENGTH);
				wordName = wordName.substr(0,9) + INDEX + wordName.substring(10,wordLength);
				endName = endName.substr(0,9) + INDEX + endName.substring(10,endLength);
				hiddenName = hiddenName.substr(0,9) + INDEX + hiddenName.substring(10,hiddenLength);
				
				$(NEXTELELIST[i]).find('select').attr("name",SELECTNAME);
				$(NEXTELELIST[i]).find('input.content').attr("name",wordName);
				$(NEXTELELIST[i]).find('input.address-participle-end').attr("name",endName);
				$(NEXTELELIST[i]).find('input[type=hidden]').attr("name",hiddenName);
			}
			
			var TEMPLELATE = "<div class='address-participle-item'>"
								+"<div class='address-participle-inblock'>"
								+"<select class='level' name='"+ADDSELECTNAME+"'>"
									+ADDOPTIONS
								+"</select>"
							+"</div>"
							+"<input type='text' class='basic-input content address-participle-inblock address-participle-input' name='"+WORDNAME+"' value=''/>	"					
							+"<input type='text' class='basic-input address-participle-inblock address-participle-end' name='"+ENDNAME+"' value=''/>"								
								+"<span class='address-add-level'>+</span>"
								+"<span class='address-remove-level'>-</span>"																
							+"<input type='hidden' name='"+HIDDENNAME+"' value=''/>"
						+"</div>";
						
				var ADDELE = $(TEMPLELATE).insertAfter(THISITEM);
				
				ADDELE.find('select').Selection({
				width: "180px",
				})
		})
		
		$("#address-edit-after-add-submit-btn", $page).on("click", function(){
			var list = '${list }';
			//var length = ${fn:length(list)};
			var addressStr = '${splitedAddressEntity.name}';
			var artificialSplitName= "";
			$(".content").each(function(){
				artificialSplitName += $(this).val();
			});
			if(addressStr == artificialSplitName){
				$("#address-edit-after-add-form").submit();
			}else{
				Dialog.notice("修改后的分词与地址不符！", "warning");
			}
		});
		
	});
	
	
</script>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<link href="media/admin/selectiontest/selectiontest.css" rel="stylesheet" type="text/css" />
<script src="media/admin/selectiontest/selectiontest.js"></script>
<div id="address-edit-after-add" class="zpage">		
				<h1 class="zpage-title">添加地址</h1>
				<form id="address-edit-after-add-form" action="admin/address/doEdit" >
					<input type="hidden" id="code" name="splitedAddressEntity.code" value="${splitedAddressEntity.code }"/>
					<input type="hidden" id="splitName" name="splitedAddressEntity.splitName" value="${splitedAddressEntity.splitName }"/>
					<input type="hidden" id="positionCode" name="splitedAddressEntity.positionCode" value="${splitedAddressEntity.positionCode }"/>
					<input type="hidden" id="keyPoint" name="splitedAddressEntity.keyPoint" value="${splitedAddressEntity.keyPoint }"/>
					<input type="hidden" id="laterPart" name="splitedAddressEntity.laterPart" value="${splitedAddressEntity.laterPart }"/>
						<div class="margin-t10">
							<span class=" zform-label" disabled="true">地址名称</span>							
							<span class="zform-item">${splitedAddressEntity.name }</span>
						</div>						
						<div class=" margin-t10">
							<span class=" zform-label">系统分词</span>
							<c:forEach items="${list }" var="addressElement" varStatus="i">							
								<span class="zform-item">${addressElement.content }(${addressElement.termWord })；</span>
							</c:forEach>
							
						</div>
						
						<div class="margin-t30 clear-fix">
							<span class="float-l zform-label">编辑分词</span>
							<div class="float-l">
								<c:forEach items="${list }" var="addressElement" varStatus="i">
									<div class="address-participle-item margin-b10">
										<div class="inline-middle margin-r10">
											<select class="level" name="elements[${i.index }].level">
												<c:forEach items="${levelNameMap }" var="levelName">											
													<option value="${levelName.key }" ${addressElement.level==levelName.key?'selected':''}>${levelName.value }</option>											
												</c:forEach>
											</select>
										</div>
										<input type="text" class="basic-input list-input content inline-middle margin-r10 " name="elements[${i.index }].content" value="${addressElement.content }"/>
										<%-- <input type="hidden" name="elements[${i.index }].level" value="${addressElement.level }"/> --%>
										<input type="text" class="basic-input short-input inline-middle margin-r10 address-participle-end" name="elements[${i.index }].termWord" value="${addressElement.termWord }"/>
										<c:if test="${addressElement.level != 0 && addressElement.level != 1 && addressElement.level != 2 && addressElement.level != 3 && addressElement.level != 4}">
											<span class="address-add-level margin-l20">+</span>
											<span class="address-remove-level">-</span>
										</c:if>									
										<input type="hidden" name="elements[${i.index }].positionCode" value="${addressElement.positionCode }"/>
									</div>
								</c:forEach>
							</div>
						</div>								
			        	<div class=" margin-t40 margin-l80 margin-b40">
			        		<span class="address-button-cancel form-button">取消</span>
			        		<span class="address-button-submit form-primary-button margin-l20">提交</span>
			        		<!-- <input id="address-edit-after-add-submit-btn" class="btn btn-block btn-darkorange" type="button" value="提交" /> -->
				        </div>
				</form>
</div>

<script>
	seajs.use(['dialog','utils'], function(Dialog, Utils){
		var $page = $('#address-edit-after-add');
		
		$('.level',$page).Selection({
	        width: "180px",
	    });
		
		//增加一级  依次修改之后的name属性
		$('#address-edit-after-add-form',$page).on('click','.address-add-level',function(){
			
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
			
			var TEMPLELATE = "<div class='address-participle-item margin-b10'>"
								+"<div class='inline-middle margin-r10'>"
								+"<select class='level' name='"+ADDSELECTNAME+"'>"
									+ADDOPTIONS
								+"</select>"
							+"</div>"
							+"<input type='text' class='basic-input list-input content inline-middle margin-r10' name='"+WORDNAME+"' value=''/>	"					
							+"<input type='text' class='basic-input short-input inline-middle margin-r10 address-participle-end' name='"+ENDNAME+"' value=''/>"								
								+"<span class='address-add-level margin-l20'>+</span>"
								+"<span class='address-remove-level'>-</span>"																
							+"<input type='hidden' name='"+HIDDENNAME+"' value=''/>"
						+"</div>";
						
				var ADDELE = $(TEMPLELATE).insertAfter(THISITEM);
				ADDELE.find('option[selected]').prop("selected",false).attr("selected",false)
					.next("option").prop("selected",true).attr("selected",true);
				ADDELE.find('select').Selection({
				width: "180px",
				})
		})
		
		//去除当前行 依次修改之后的name属性
		$('#address-edit-after-add-form').on('click','.address-remove-level',function(){
			var NEXTELELIST = $(this).closest('.address-participle-item').nextAll();
			
			for(var i = 0; i<NEXTELELIST.length;i++){   //顺次修改之后的select的name属性
				var SELECTNAME = $(NEXTELELIST[i]).find('select').attr("name");
				var wordName = $(NEXTELELIST[i]).find('input.content').attr("name");
				var endName = $(NEXTELELIST[i]).find('input.address-participle-end').attr("name");
				var hiddenName = $(NEXTELELIST[i]).find('input[type=hidden]').attr("name");
				
				var INDEX = parseFloat(SELECTNAME.substr(9,1)) - 1;
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
			
			$(this).closest('.address-participle-item').remove();
			
		})
		
		$(".address-button-submit", $page).on("click", function(){
			var list = '${list }';
			//var length = ${fn:length(list)};
			var addressStr = '${splitedAddressEntity.name}';
			var artificialSplitName= "";
			$(".content",$page).each(function(){
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
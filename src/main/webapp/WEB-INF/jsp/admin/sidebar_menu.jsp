<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<li>
	<a href="#" class="menu-dropdown">
	    <i class="menu-icon fa fa-bookmark"></i>
	    <span class="menu-text">Demo</span>
	</a>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/demo/list" target="demo_list" title="列表">
	   			<span class="menu-text">列表</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/people/list" target="people_list" title="列表">
	   			<span class="menu-text">列表2</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/search/list" target="search_list" title="列表">
	   			<span class="menu-text">人口查询</span>
	   		</a>
	   	</li>   	
		<li><a class="tab" href="admin/field/list" target="field_list"
			title="字段列表"> <span class="menu-text">字段列表</span>
		</a></li>
		<li><a class="tab" href="admin/peopleDictionary/list"
			target="dictionary_list" title="字典列表"> <span class="menu-text">字典列表</span>
		</a></li>
	</ul>
</li>
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-bookmark"></i>
		<span class="menu-text">ABC人口</span>
		<i></i>
	</a>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/modules/curd/list/people" target="people_list_tmpl_" title="人口列表">
	   			<span class="menu-text">人口列表</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/modules/curd/list/people?tg=old" target="people_list_tmpl_old" title="老人信息">
	   			<span class="menu-text">老人信息</span>
	   		</a>
	   	</li>
	</ul>
</li>
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-bookmark"></i>
		<span class="menu-text">模板管理</span>
		<i></i>
	</a>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/tmpl/dtmpl/list/people" target="people_dtmpl_list" title="人口详情模板">
	   			<span class="menu-text">人口详情模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/ltmpl/list/people" target="ltmpl_list" title="人口列表模板">
	   			<span class="menu-text">人口列表模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/group/list/people" target="tmpl_group_list_people" title="人口模板组合">
	   			<span class="menu-text">人口模板组合</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/dtmpl/list/address" target="address_dtmpl_list" title="地址列表模板">
	   			<span class="menu-text">地址详情模板</span>
	   		</a>
	   	</li>
	   	
	   	<!-- 
	   	<li>
	   		<a class="tab" href="admin/tmpl/ltmpl/list/address" target="address_ltmpl_list" title="地址列表模板">
	   			<span class="menu-text">学生列表模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/dtmpl/list/student" target="student_dtmpl_list" title="学生详情模板">
	   			<span class="menu-text">学生详情模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/ltmpl/list/student" target="student_ltmpl_list" title="学生列表模板">
	   			<span class="menu-text">学生列表模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/group/list/student" target="tmpl_group_list_student" title="学生模板组合">
	   			<span class="menu-text">学生模板组合</span>
	   		</a>
	   	</li>
	   	 -->
	   		<li>
	   		<a class="tab" href="admin/tmpl/dtmpl/list/disabledpeople" target="disabledpeople_dtmpl_list" title="残助详情模板">
	   			<span class="menu-text">残助详情模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/ltmpl/list/disabledpeople" target="disabledpeople_ltmpl_list" title="残助列表模板">
	   			<span class="menu-text">残助列表模板</span>
	   		</a>
	   	</li>
	   		<li>
	   		<a class="tab" href="admin/tmpl/dtmpl/list/disablederror" target="disablederror_dtmpl_list" title="残助错误详情模板">
	   			<span class="menu-text">残助错误详情模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/ltmpl/list/disablederror" target="disablederror_ltmpl_list" title="残助错误列表模板">
	   			<span class="menu-text">残助错误列表模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/dtmpl/list/hspeople" target="hspeople_dtmpl_list" title="党员详情模板">
	   			<span class="menu-text">党员详情模板</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/tmpl/ltmpl/list/hspeople" target="hspeople_ltmpl_list" title="党员列表模板">
	   			<span class="menu-text">党员列表模板</span>
	   		</a>
	   	</li>
	</ul>
</li>
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-bookmark"></i>
		<span class="menu-text">地址管理</span>
		<i></i>
	</a>
	<ul class="submenu">
		<li>
	   		<a class="tab" href="admin/modules/curd/list/address" target="address_list_tmpl" title="地址列表">
	   			<span class="menu-text">地址列表</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/address/list" target="address_list" title="地址信息">
	   			<span class="menu-text">地址信息</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/position/position_list" target="position_list" title="行政区域">
	   			<span class="menu-text">行政区域</span>
	   		</a>
	   	</li>
	   	<li>
	   		<a class="tab" href="admin/special_position/special_position_list" target="special_position_list" title="特殊地名">
	   			<span class="menu-text">特殊地名</span>
	   		</a>
	   	</li>
	</ul>
</li>
<!--  
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-bookmark"></i>
		<span class="menu-text">学生模块</span>
		<i></i>
	</a>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/modules/curd/list/student" target="student_list_tmpl" title="学生列表">
	   			<span class="menu-text">学生列表</span>
	   		</a>
	   	</li>
	   		<li>
	   		<a class="tab" href="admin/modules/curd/list/student?tg=studentparty" target="studentparty_list_tmpl" title="学生列表">
	   			<span class="menu-text">党员列表</span>
	   		</a>
	   	</li>
	</ul>
</li>
-->
<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-bookmark"></i>
		<span class="menu-text">党员模块</span>
		<i></i>
	</a>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/modules/curd/list/hspeople" target="hspeople_list_tmpl" title="党员列表">
	   			<span class="menu-text">党员列表</span>
	   		</a>
	   	</li>
	</ul>
</li>

<li>
	<a href="#" class="menu-dropdown">
		<i class="menu-icon fa fa-bookmark"></i>
		<span class="menu-text">残助模块</span>
		<i></i>
	</a>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/modules/curd/list/disabledpeople" target="disabledpeople_list_tmpl" title="残助列表">
	   			<span class="menu-text">残助列表</span>
	   		</a>
	   	</li>
	</ul>
	<ul class="submenu">
	   	<li>
	   		<a class="tab" href="admin/modules/curd/list/disablederror" target="disablederror_list_tmpl" title="错误信息">
	   			<span class="menu-text">错误信息</span>
	   		</a>
	   	</li>
	</ul>
</li>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="field-list">
    <a class="btn btn-primary tab" href="admin/field/add" title="新增字段" target="field_add" >新增字段</a>
    <div class="row list-area">
        <table class="table">
            <thead>
            <tr>
                <th>序号</th>
                <th>字段中文</th>
                <th>字段英文</th>
                <th>控件类型</th>
                <th>字段校验规则</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list }" var="item" varStatus="i">
                <tr>
                    <%--<td>${i.index + 1 }</td>--%>
                    <td>${item.id }</td>
                        <td><a class="tab" href="admin/field/detail/${item.id }" target="field_detail_${item.id }" title="编辑">${item.title }</a></td>
                    <td>${item.title_en }</td>
                        <td>
                        <c:if test="${item.type==1 }"> 文本输入框 </c:if>
                        <c:if test="${item.type==2 }"> 单选框 </c:if>
                            <c:if test="${item.type==3 }"> 单选下拉框 </c:if>
                            <c:if test="${item.type==4 }"> 多选下拉框 </c:if>
                            <c:if test="${item.type==5 }"> 暂无 </c:if>
                            <c:if test="${item.type==6 }"> 时间控件 </c:if>

                        </td>
                        <td>
                            <c:if test="${item.check_rule=='0' }"> 未设置 </c:if>
                            <c:if test="${item.check_rule!='0' }"> 已设置 </c:if>
                        </td>
                    <%--<td>--%>
                        <%--<a href="admin/people/update/${item.id }" class="tab" target="people_update_${item.id }" title="修改">修改</a>--%>
                        <%--<a href="admin/people/do_delete/${item.id }" confirm="确认删除？">删除</a>--%>
                    <%--</td>--%>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="cpf-paginator" pageNo="${pageInfo.pageNo }" pageSize="${pageInfo.pageSize }" count="${pageInfo.count }"></div>
    </div>
</div>

<script>
    seajs.use(['utils'], function(Utils){
        var $page = $('#field-list');
        //Utils.datepicker($('#date', $page));
    });
</script>
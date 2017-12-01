<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/base_empty.jsp"%>
<div id="enum-add">
    <div class="page-header">
        <div class="header-title">
            <h1>编辑枚举值</h1>
        </div>
    </div>
    <div class="page-body">
        <div class="row">

            <div class="col-lg-12">
                <form class="bv-form form-horizontal validate-form" action="admin/peopleDictionary/SaveEnum">
                    <div class="form-group">
                        <label class="col-lg-2 control-label" for="cId"></label>
                        <div class="col-lg-5">
                            <input class="form-control"  type="hidden" name="cId" value="${ItemDto.cId}" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 control-label" for="cDictionaryId"></label>
                        <div class="col-lg-5">
                            <input 。 class="form-control"  type="hidden" name="cDictionaryId" value="${ItemDto.cDictionaryId}" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 control-label" for="cEnumCnName">枚举名</label>
                        <div class="col-lg-5">
                            <input type="text" class="form-control" name="cEnumCnName"  value="${ItemDto.cEnumCnName}" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-lg-2 control-label" for="cEnumValue">枚举值</label>
                        <div class="col-lg-5">
                            <input type="text" class="form-control" name="cEnumValue"  value="${ItemDto.cEnumValue}" />
                        </div>
                    </div>



                    <div class="form-group">
                        <div class="col-lg-offset-3 col-lg-3">
                            <input class="btn btn-block btn-darkorange" type="submit" value="提交"  />
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
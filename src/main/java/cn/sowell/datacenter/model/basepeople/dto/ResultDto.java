package cn.sowell.datacenter.model.basepeople.dto;

public class ResultDto {
        private Object Data ;
        private Object FieldList;
        private String type ;
        private String Status;

    public Object getFieldList() {
        return FieldList;
    }

    public void setFieldList(Object fieldList) {
        FieldList = fieldList;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String staues) {
        Status = staues;
    }
}

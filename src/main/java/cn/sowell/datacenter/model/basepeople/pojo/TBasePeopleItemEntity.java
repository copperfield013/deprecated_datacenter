package cn.sowell.datacenter.model.basepeople.pojo;


public class TBasePeopleItemEntity {
    private int c_id;
    private String c_dictionary_id;
    private String c_enum_cn_name;
    private String c_enum_value;

    public TBasePeopleItemEntity(int c_id, String c_dictionary_code, String c_enum_cn_name, String c_enum_value) {
        this.c_id = c_id;
        this.c_dictionary_id = c_dictionary_code;
        this.c_enum_cn_name = c_enum_cn_name;
        this.c_enum_value = c_enum_value;

    }

    public TBasePeopleItemEntity() {
    }


    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getC_dictionary_id() {
        return c_dictionary_id;
    }

    public void setC_dictionary_id(String c_dictionary_code) {
        this.c_dictionary_id = c_dictionary_code;
    }

    public String getC_enum_cn_name() {
        return c_enum_cn_name;
    }

    public void setC_enum_cn_name(String c_enum_cn_name) {
        this.c_enum_cn_name = c_enum_cn_name;
    }

    public String getC_enum_value() {
        return c_enum_value;
    }

    public void setC_enum_value(String c_enum_value) {
        this.c_enum_value = c_enum_value;
    }
}


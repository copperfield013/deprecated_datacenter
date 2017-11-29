package cn.sowell.datacenter.model.basepeople.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class CityEntiy {
    /**
     * 省份 城市
     */

    @JSONField(name="c")
    String city;

    @JSONField(name="n")
    String name;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

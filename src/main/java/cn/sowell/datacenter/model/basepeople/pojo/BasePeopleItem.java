package cn.sowell.datacenter.model.basepeople.pojo;

import javax.persistence.*;

@Entity
@Table(name = "t_base_people_item")
public class BasePeopleItem {
    private Long cId;
    private String cDictionaryId;
    private String cEnumCnName;
    private String cEnumValue;

    @Id
    @Column(name = "c_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    @Basic
    @Column(name = "c_dictionaryid")
    public String getcDictionaryId() {
        return cDictionaryId;
    }

    public void setcDictionaryId(String cDictionaryId) {
        this.cDictionaryId = cDictionaryId;
    }

    @Basic
    @Column(name = "c_enum_cn_name")
    public String getcEnumCnName() {
        return cEnumCnName;
    }

    public void setcEnumCnName(String cEnumCnName) {
        this.cEnumCnName = cEnumCnName;
    }

    @Basic
    @Column(name = "c_enum_value")
    public String getcEnumValue() {
        return cEnumValue;
    }

    public void setcEnumValue(String cEnumValue) {
        this.cEnumValue = cEnumValue;
    }




}

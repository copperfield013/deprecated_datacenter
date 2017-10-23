package cn.sowell.datacenter.model.basepeople.pojo;

import javax.persistence.*;

@Entity
@Table(name = "t_base_people_item", schema = "datacenter_temp", catalog = "")
public class TBasePeopleItemEntity {
    private int cId;
    private String cDictionarycode;
    private String cEnumCnName;
    private String cEnumValue;

    @Id
    @Column(name = "c_id")
    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    @Basic
    @Column(name = "c_dictionarycode")
    public String getcDictionarycode() {
        return cDictionarycode;
    }

    public void setcDictionarycode(String cDictionarycode) {
        this.cDictionarycode = cDictionarycode;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TBasePeopleItemEntity that = (TBasePeopleItemEntity) o;

        if (cId != that.cId) return false;
        if (cDictionarycode != null ? !cDictionarycode.equals(that.cDictionarycode) : that.cDictionarycode != null)
            return false;
        if (cEnumCnName != null ? !cEnumCnName.equals(that.cEnumCnName) : that.cEnumCnName != null) return false;
        if (cEnumValue != null ? !cEnumValue.equals(that.cEnumValue) : that.cEnumValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cId;
        result = 31 * result + (cDictionarycode != null ? cDictionarycode.hashCode() : 0);
        result = 31 * result + (cEnumCnName != null ? cEnumCnName.hashCode() : 0);
        result = 31 * result + (cEnumValue != null ? cEnumValue.hashCode() : 0);
        return result;
    }
}

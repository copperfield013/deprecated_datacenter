package cn.sowell.datacenter.model.basepeople.pojo;

import javax.persistence.*;

@Entity
@Table(name = "t_base_people_dictionary", schema = "datacenter_temp", catalog = "")
public class TBasePeopleDictionaryEntity {
    private int cId;
    private String cCnName;
    private String cDictionarycode;

    @Id
    @Column(name = "c_id")
    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    @Basic
    @Column(name = "c_cn_name")
    public String getcCnName() {
        return cCnName;
    }

    public void setcCnName(String cCnName) {
        this.cCnName = cCnName;
    }

    @Basic
    @Column(name = "c_dictionarycode")
    public String getcDictionarycode() {
        return cDictionarycode;
    }

    public void setcDictionarycode(String cDictionarycode) {
        this.cDictionarycode = cDictionarycode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TBasePeopleDictionaryEntity that = (TBasePeopleDictionaryEntity) o;

        if (cId != that.cId) return false;
        if (cCnName != null ? !cCnName.equals(that.cCnName) : that.cCnName != null) return false;
        if (cDictionarycode != null ? !cDictionarycode.equals(that.cDictionarycode) : that.cDictionarycode != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cId;
        result = 31 * result + (cCnName != null ? cCnName.hashCode() : 0);
        result = 31 * result + (cDictionarycode != null ? cDictionarycode.hashCode() : 0);
        return result;
    }
}

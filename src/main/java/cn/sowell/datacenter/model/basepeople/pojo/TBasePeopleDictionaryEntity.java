package cn.sowell.datacenter.model.basepeople.pojo;

import javax.persistence.*;

@Entity
@Table(name = "t_base_people_dictionary")
public class TBasePeopleDictionaryEntity {
    private int cId;
    private String cCnName;
    private String cDictionarycode;
    private String cCnEnglish;

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

    @Basic
    @Column(name = "c_cn_english")
    public String getcCnEnglish() {
        return cCnEnglish;
    }

    public void setcCnEnglish(String cCnEnglish) {
        this.cCnEnglish = cCnEnglish;
    }


}

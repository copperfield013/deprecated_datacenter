package cn.sowell.datacenter.model.basepeople.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "t_base_people_dictionary")
public class TBasePeopleDictionaryEntity {
    private int cId;
    private  String cCnName;
    private  String cCnEnglish;
    private  String  type;
    private  String  check_rule;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheck_rule() {
        return check_rule;
    }

    public void setCheck_rule(String check_rule) {
        this.check_rule = check_rule;
    }

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
    @Column(name = "c_cn_english")
    public String getcCnEnglish() {
        return cCnEnglish;
    }

    public void setcCnEnglish(String cCnEnglish) {
        this.cCnEnglish = cCnEnglish;
    }


}

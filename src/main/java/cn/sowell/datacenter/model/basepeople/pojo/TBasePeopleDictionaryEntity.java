package cn.sowell.datacenter.model.basepeople.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "t_base_people_dictionary")
public class TBasePeopleDictionaryEntity {
	@Id
    @Column(name = "c_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  Long    cId;
    @Basic
    @Column(name = "c_cn_name")
    private  String cCnName;
    @Basic
    @Column(name = "c_cn_english")
    private  String cCnEnglish;
    private  String type;
    private  String check_rule;
    @Basic
    @Column(name = "c_info_id")
    private  Long    cInfoId;
    

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

    
    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    
    public String getcCnName() {
        return cCnName;
    }

    public void setcCnName(String cCnName) {
        this.cCnName = cCnName;
    }


   
    public String getcCnEnglish() {
        return cCnEnglish;
    }

    public void setcCnEnglish(String cCnEnglish) {
        this.cCnEnglish = cCnEnglish;
    }

	public Long getcInfoId() {
		return cInfoId;
	}

	public void setcInfoId(Long cInfoId) {
		this.cInfoId = cInfoId;
	}

//    public String getValueName(){
//        String changeValue[]  ="1,2,3,4,5"
//    }
}

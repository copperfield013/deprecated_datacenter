package cn.sowell.datacenter.model.basepeople.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_base_people_information")
public class TBasePeopleInformationEntity {
	@Id
    @Column(name = "t_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private  Long    id;
    @Basic
    @Column(name = "t_info_cnname")
    private  String cnName;
    @Basic
    @Column(name = "t_info_enname")
    private  String enName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
    
	
    
}

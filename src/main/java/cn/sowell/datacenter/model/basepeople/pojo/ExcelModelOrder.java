package cn.sowell.datacenter.model.basepeople.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_excel_model_order")
public class ExcelModelOrder {
	
	@Id
	@Column(name = "c_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "c_modelid")
	private Long model_id;
	
	@Column(name = "c_dictionaryid")
	private Long dictionary_id;
	
	@Column(name = "c_orderid")
	private Long order_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getModel_id() {
		return model_id;
	}

	public void setModel_id(Long model_id) {
		this.model_id = model_id;
	}

	public Long getDictionary_id() {
		return dictionary_id;
	}

	public void setDictionary_id(Long dictionary_id) {
		this.dictionary_id = dictionary_id;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	

}

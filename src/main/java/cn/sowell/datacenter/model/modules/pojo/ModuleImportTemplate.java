package cn.sowell.datacenter.model.modules.pojo;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.sowell.datacenter.model.tmpl.pojo.AbstractTemplate;

@Entity
@Table(name="t_import_tmpl")
public class ModuleImportTemplate extends AbstractTemplate{
	
	@Transient
	private Set<ModuleImportTemplateField> fields;
	
	public Set<ModuleImportTemplateField> getFields() {
		return fields;
	}
	public void setFields(Set<ModuleImportTemplateField> fields) {
		this.fields = fields;
	}
}

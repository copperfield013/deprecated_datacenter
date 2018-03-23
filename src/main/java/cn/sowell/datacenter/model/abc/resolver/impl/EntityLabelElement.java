package cn.sowell.datacenter.model.abc.resolver.impl;

import java.util.Set;

import cn.sowell.datacenter.model.abc.resolver.EntityElement;

public class EntityLabelElement extends EntityElement{
	Set<String> subdomain;

	public Set<String> getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(Set<String> subdomain) {
		this.subdomain = subdomain;
	}
	
}

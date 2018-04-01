package cn.sowell.datacenter.model.abc.resolver.impl;


import java.util.Set;

import cn.sowell.copframe.utils.Assert;
import cn.sowell.copframe.utils.CollectionUtils;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;
import cn.sowell.datacenter.model.abc.resolver.EntityBindContext;
import cn.sowell.datacenter.model.abc.resolver.FieldParserDescription;
import cn.sowell.datacenter.model.abc.resolver.FusionContextConfig;
import cn.sowell.datacenter.model.abc.resolver.PropertyNamePartitions;
import cn.sowell.datacenter.model.abc.resolver.format.PropertyFormat;
import cn.sowell.datacenter.model.abc.resolver.format.PropertyFormatFactory;

class CommonEntityPropertyParser extends AbstractEntityPropertyParser{

	private FusionContextConfig config;
	
	private EntityBindContext context;

	private final PropertyFormatFactory formatFactory;
	
	
	CommonEntityPropertyParser(FusionContextConfig config, EntityBindContext context, Set<FieldParserDescription> fieldSet) {
		super(CollectionUtils.toMap(fieldSet, field->field.getFullKey()));
		Assert.notNull(config);
		Assert.notNull(context);
		Assert.notNull(context.getEntity());
		this.context = context;
		this.config = config;
		formatFactory = new PropertyFormatFactory();
	}
	
	@Override
	public String getCode() {
		return this.getId();
	}
	
	@Override
	public String getId() {
		return (String) getProperty(config.getCodeAttributeName());
	}
	
	@Override
	public String getTitle() {
		return (String) getProperty(config.getTitleAttributeName());
	}
	
	
	@Override
	public Object getProperty(String propertyName, String propType) {
		String[] names = TextUtils.splitToArray(propertyName, "\\.");
		
		EntityBindContext thisContext = this.context;
		for (int i = 0; i < names.length - 1; i++) {
			PropertyNamePartitions namePartitions = new PropertyNamePartitions(names[i]);
			thisContext = thisContext.getElement(namePartitions);
		}
		if(propType == null) {
			PropertyNamePartitions namePartitions = new PropertyNamePartitions(propertyName);
			FieldParserDescription field = fieldMap.get(namePartitions.getMainPartition());
			if(field != null) {
				propType = field.getAbcType();
			}else {
				propType = "string";
			}
		}
		return thisContext.getValue(names[names.length - 1], propType);
	}

	@Override
	public String getFormatedProperty(String propertyName, String propType, String format) {
		Object value = getProperty(propertyName, propType);
		String result = null;
		try {
			PropertyFormat pFormat = formatFactory.getFormat(value, propType, format);
			result = pFormat.format(value);
		}catch(Exception e) {
			result = FormatUtils.toString(value);
		}
		return result;
	}





}

package cn.sowell.datacenter.model.peopledata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EntityRecord {
	Class<?> elementClass() default Object.class;

	String entityName() default "";

	String domainName() default "";
}

package cn.sowell.datacenter.model.peopledata;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
public @interface ABCAttribute {
	String value() default "";
	boolean ignored() default false;
	String entityName() default "";
}

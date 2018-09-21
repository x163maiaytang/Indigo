package com.fire.core.bhns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author 
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServicePortalInfo
{
	int portalId();
	int timeout() default 500;
	boolean asyn() default false;
	
	Class<?> registClass() default ServicePortalInfo.class;
	int[] depends() default {2, 7};
}

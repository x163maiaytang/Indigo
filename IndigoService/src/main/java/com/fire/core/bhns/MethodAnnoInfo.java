package com.fire.core.bhns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodAnnoInfo {

	int timeout() default 500;
	// 是否异步
	boolean asyn() default false;
}

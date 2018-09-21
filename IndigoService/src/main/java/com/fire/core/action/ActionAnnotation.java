package com.fire.core.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import game.net.message.AbstractMessage;

/**
 * action注解类
 * @author
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionAnnotation {
	Class<? extends AbstractMessage> message();
	int serviceId();
	int validInterval() default 200;
	boolean needAuth() default true;
	int discardTime() default 10000;
}

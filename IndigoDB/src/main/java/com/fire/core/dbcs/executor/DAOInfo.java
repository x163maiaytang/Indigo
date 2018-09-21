package com.fire.core.dbcs.executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fire.core.dbcs.dbrule.IDBZoneRule;
import com.fire.core.dbcs.resulthandler.IResultHandler;
/**
 * DAO接口函数注解
 * 包含参数列表、数据库映射规则信息
 * 
 * @author 
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DAOInfo
{
	String Params();

	Class<? extends IDBZoneRule> dbRule();
	
	Class<? extends IResultHandler> resultHandler();
	
	boolean batch() default false;
}

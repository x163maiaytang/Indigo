package com.fire.core.dbcs.executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fire.core.dbcs.interceptor.IDbOprateIntereptor;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DAOClass {

	Class<? extends IDbOprateIntereptor> intereptor();
}

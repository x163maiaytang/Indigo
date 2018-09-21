package com.fire.datastore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fire.gamedataobject.AbstractGameDataObjectManager;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBlockAnnotationInfo
{
	Class<? extends AbstractGameDataObjectManager>[] depandences();
}

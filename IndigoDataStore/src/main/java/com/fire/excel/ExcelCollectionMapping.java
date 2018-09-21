package com.fire.excel;

import java.lang.annotation.Annotation;

public interface ExcelCollectionMapping extends Annotation
{

	public abstract Class clazz();

	public abstract String collectionNumber();
}

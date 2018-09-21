package com.fire.excel;

import java.lang.annotation.Annotation;

public interface FixUpByCellRange extends Annotation
{

	public abstract int start();

	public abstract int len();
}

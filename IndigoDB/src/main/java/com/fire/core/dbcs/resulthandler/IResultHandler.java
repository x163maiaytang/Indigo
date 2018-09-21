package com.fire.core.dbcs.resulthandler;


public interface IResultHandler<T>
{
	/**
	 * 
	 * @param t
	 * @return 是否处理完成，可以中断
	 */
	public boolean handleResult(T t);
	
	public T getResult();
}

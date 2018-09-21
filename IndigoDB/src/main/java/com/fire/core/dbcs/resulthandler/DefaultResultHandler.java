package com.fire.core.dbcs.resulthandler;

public class DefaultResultHandler implements IResultHandler<Object>
{
	private Object result;

	@Override
	public boolean handleResult(Object t)
	{
		result = t;
		return false;
	}

	@Override
	public Object getResult()
	{
		return result;
	}
}

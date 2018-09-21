package com.fire.core.bhns.data;

import java.io.Serializable;
import java.util.Iterator;

//TODO：暂未实现
public class ServiceDataIterator implements Iterator<Serializable>,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean hasNext()
	{
		return false;
	}

	@Override
	public Serializable next()
	{
		return null;
	}

	@Override
	public void remove()
	{
	}

}

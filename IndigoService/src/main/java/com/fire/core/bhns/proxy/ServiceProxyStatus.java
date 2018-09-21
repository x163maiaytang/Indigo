package com.fire.core.bhns.proxy;

import com.fire.core.bhns.IServiceable;

public class ServiceProxyStatus<T extends IServiceable>
{
	public final static byte INACTIVE_STATUS = 0;
	public final static byte ACTIVE_STATUS = 1;
	public final static byte UNAVAILABLE_STATUS = -1;

	private byte status;
	private T entrance;

	public ServiceProxyStatus()
	{
		status = UNAVAILABLE_STATUS;
		entrance = null;
	}

	public void bindEntrance(T entrance)
	{
		this.entrance = entrance;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte v)
	{
		this.status = v;
	}

	public T getEntrance()
	{
		return this.entrance;
	}
}

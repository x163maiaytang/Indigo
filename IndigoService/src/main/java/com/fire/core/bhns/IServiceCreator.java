package com.fire.core.bhns;

import com.fire.core.bhns.data.ServiceData;

public interface IServiceCreator
{
	public void configInstance(long serviceId, int... params);
	
	public void resetServiceData(ISimpleService c, ServiceData newServiceData);
}

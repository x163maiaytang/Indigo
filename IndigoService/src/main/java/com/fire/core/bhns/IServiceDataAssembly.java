package com.fire.core.bhns;

import com.fire.core.bhns.data.ServiceData;

public interface IServiceDataAssembly
{
	public void addServiceData(int portalId, ServiceData data);

	public void removeServiceData(int portalId);
	
	public ServiceData getServiceData(int portalId);
}

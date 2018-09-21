package com.fire.core.bhns;


public interface IServiceAssembly
{
	public ISimpleService getService(int portalId);
	
	public void addService(int portalId,ISimpleService service);
	
	public void removeService(int portalId);
}

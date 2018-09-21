package com.fire.core.bhns.source;

import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.data.ServiceData;

public interface IEndpointSource
{
	boolean isInited();
	
	void setInited(boolean inited);
	
	public void initialize() ;

	public int getPortalId();

	public void destroyService(long svrid) ;

	public <T extends ISimpleService> T findService(long svrid,boolean forceReal);

	public <T extends ISimpleService> T activeService(long svrid, int... params);

	public IServicePortal<? extends ISimpleService> getPortal();

	public ServiceData getServiceData(long serviceId);
	
	public boolean isLocalImplemention();
	
	public int getEndpointId();
	
	public void onSourceDestory(long svrid);

	public void update() ;

}

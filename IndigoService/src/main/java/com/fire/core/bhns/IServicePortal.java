package com.fire.core.bhns;

import java.util.Collection;

/**
 * 
 * @author
 * 
 *         此接口是规范portal内部结构，也就是从服务自身管理角度(不考虑调用其他远程服务)来看，服务应该提供的基本组织形态
 */

public interface IServicePortal<T extends ISimpleService> extends
		IServiceable
{
	public int getPortalId(); // 此服务提供的服务标识，目前设计每个服务对外只提供一种行为

	public T findService(long svrid,boolean forceReal); 

	public T activeService(long svrid, int... params);

	public void destroyService(long svrid);
	
	public Collection<Long> getActiveServiceIds();
	
	public Collection<T> getActiveServices();
	
	public int getEndpointId();
	
	public void setEndpointId(int endpointId);
}

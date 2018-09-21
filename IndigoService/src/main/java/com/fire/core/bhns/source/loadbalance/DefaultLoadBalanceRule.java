package com.fire.core.bhns.source.loadbalance;

import java.util.List;

import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ServiceManager;

/**
 * 
 *简单粗暴省性能
 */
public class DefaultLoadBalanceRule implements ILoadBalanceRule
{
	@Override
	public int getEndPoint(long serviceId,int portalId) 
	{
		List<IServicePortal<?>> list = ServiceManager.findAllPortal(portalId);
		int index = (int) (serviceId % list.size());
		
		return list.get(index).getEndpointId();
	}
	
}

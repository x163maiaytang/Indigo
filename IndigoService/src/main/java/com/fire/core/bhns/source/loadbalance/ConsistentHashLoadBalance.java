package com.fire.core.bhns.source.loadbalance;

public class ConsistentHashLoadBalance implements ILoadBalanceRule{

	@Override
	public int getEndPoint(long serviceId, int portalId)  {
		return 0;
	}

}

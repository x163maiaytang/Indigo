package com.fire.core.bhns.source.loadbalance;

public interface ILoadBalanceRule
{
	public int getEndPoint(long serviceId,int portalId) ;
}

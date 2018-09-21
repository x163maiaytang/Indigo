package com.fire.core.bhns.source;

import com.fire.core.bhns.AbstractSimpleService;
import com.fire.core.bhns.IMethodCollection;
import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.proxy.ServiceRemoteProxy;
import com.fire.core.bhns.source.options.ServiceEndpointInfo;

/**
 * 服务的配置
 */
public interface IServiceOption
{
	public final byte CONFIG_TYPE_NORMAL = 0;
	public final byte CONFIG_TYPE_CLUSTER = 1;
	public final byte CONFIG_TYPE_APPERTAIN = 2;

	public int portalId();

	public byte configType();
	
	public Class<? extends IServicePortal> portalClass();
	
	public Class<? extends ISimpleService> serviceClass();
	
	public Class<? extends ISimpleService> serviceActorProxyClass();
	
	public IMethodCollection getServiceMethodMap();

	public IMethodCollection getPortalMethodMap();
	
	public ServiceEndpointInfo getEndpointInfo(int endpointId);
	
	public AbstractSimpleService createSvrImpInstance();
	
	public AbstractSimpleService createSvrActorProxyInstance(long serviceId);
	
	public AbstractSimpleService createSvrRemoteProxyInstance(long serviceId,ServiceRemoteProxy<?,?> proxy);
	
	public IServicePortal createPortalRemoteProxyInstance(ServiceRemoteProxy<?,?> proxy, IServiceOption serviceOption);
}


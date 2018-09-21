package com.fire.core.bhns.source.source.cluster;

import com.fire.core.bhns.INetSupport;
import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.data.ServiceData;
import com.fire.core.bhns.net.ServiceClientFactory;
import com.fire.core.bhns.proxy.ServiceRemoteProxy;
import com.fire.core.bhns.source.AbstractEndpointSource;
import com.fire.core.bhns.source.IServiceOption;
import com.fire.core.bhns.source.options.ServiceEndpointInfo;

import game.net.server.SimpleClient;

public class RemoteEndpointSource extends AbstractEndpointSource implements INetSupport
{
	private IServiceOption serviceOption;
	private int endpointId;
	
	private ServiceRemoteProxy remoteProxy;
	private IServicePortal proxy;
//	private ConnectPool netChannel;
	
	private SimpleClient netChannel;

	public RemoteEndpointSource(IServiceOption serviceOption, int endpointId)
	{
		this.serviceOption = serviceOption;
		this.endpointId = endpointId;
		remoteProxy = new ServiceRemoteProxy(serviceOption.portalId(),this.endpointId, serviceOption, this);
	}

	@Override
	public void initialize() 
	{
		if(inited){
			return;
		}
		try
		{
			this.proxy = serviceOption.createPortalRemoteProxyInstance(this.remoteProxy, serviceOption);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			
			inited = true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T activeService(long svrid, int... params) 
	{
		return (T) remoteProxy.activeServiceProxy(svrid);
	}
	
	@Override
	public void destroyService(long svrid) 
	{
		remoteProxy.destroyServiceProxy(svrid);
	}

	@Override
	public int getEndpointId()
	{
		return endpointId;
	}

	@Override
	public int getPortalId()
	{
		return this.serviceOption.portalId();
	}

	@Override
	public boolean isLocalImplemention()
	{
		return false;
	}

	@Override
	public SimpleClient getNetChannel()
	{
		if (this.netChannel == null)
		{
			ServiceEndpointInfo tmpServiceUnitInfo = serviceOption.getEndpointInfo(this.endpointId);
			if (tmpServiceUnitInfo != null){
				this.netChannel = ServiceClientFactory.getClient(tmpServiceUnitInfo.getEndpointIP(), tmpServiceUnitInfo.getEndpointPort());
			}
		}
		
		return netChannel;
	}

//	@Override
//	public AsynchronismClientManager relocateNetChannel()
//	{
//		if(this.netChannel != null)
//		{
//			ServiceEndpointInfo tmpServiceUnitInfo = serviceOption.getEndpointInfo(this.endpointId);
//			if(tmpServiceUnitInfo != null)
//			{
//				ConnectionInfo info = new ConnectionInfo(tmpServiceUnitInfo.getEndpointIP(),
//						tmpServiceUnitInfo.getEndpointPort(), 10000, 5000);
//				this.netChannel.relocate(info);
//			}
//		}
//		return this.netChannel;
//	}

	@Override
	public ServiceData getServiceData(long serviceId)
	{
		return null;
	}

	@Override
	public <T extends ISimpleService> T findService(long svrid,
			boolean forceReal)
	{
		return (T) remoteProxy.getServiceProxy(svrid);
	}

	@Override
	public IServicePortal<? extends ISimpleService> getPortal()
	{
		return proxy;
	}

	@Override
	public void onSourceDestory(long svrid)
	{
		this.remoteProxy.clearRemoteProxy(svrid);
		if(netChannel != null){
			netChannel = null;
		}
	}

	@Override
	public void update() {
		
	}
}

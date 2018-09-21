package com.fire.core.bhns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.core.bhns.source.IEndpointSource;
import com.fire.core.bhns.source.IServiceOption;
import com.fire.core.bhns.source.IServiceSource;

/**
 * 服务基础配置信息
 * 
 * @author
 * 
 */
public class ServiceInfo
{
	protected static final Logger logger = Logger.getLogger(ServiceInfo.class);

	private IServiceOption serviceOption;// 服务自身的配置信息
	private IServiceSource serviceSource;// 服务的源码

	public ServiceInfo(IServiceOption serviceOption,IServiceSource serviceSource)
	{
		this.serviceOption = serviceOption;
		this.serviceSource = serviceSource;
//		switch (option().configType())
//		{
//		case IServiceOption.CONFIG_TYPE_NORMAL:
//			serviceSource = new NormalSource((NormalServiceOption) serviceOption);
//			break;
//		case IServiceOption.CONFIG_TYPE_CLUSTER:
//			serviceSource = new ClusterSource((ClusterServiceOption) serviceOption);
//			break;
//		}
	}

	public int getPortalId()
	{
		return serviceOption.portalId();
	}

	public IServiceOption option()
	{
		return serviceOption;
	}

	public IServiceSource source()
	{
		return serviceSource;
	}

	public void initialSource() 
	{
		if (serviceSource != null)
		{
			this.serviceSource.initialize();
		} else
		{
			logger.error("initialSource error:portalId=" + this.getPortalId());
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends ISimpleService> T activeService(long svrid, int endpoint, int... params) 
	{
		return (T) serviceSource.activeService(svrid, endpoint, params);
	}

	public void destoryService(long svrid) 
	{
		serviceSource.destroyService(svrid);
	}

	@SuppressWarnings("unchecked")
	public <T extends ISimpleService> T findService(long svrid,boolean forceReal)
	{
		return (T) serviceSource.findService(svrid,forceReal);
	}
	
	public <T extends IServicePortal> List<T> findAllPortal()
	{
		List<T> list = new ArrayList<T>();
		for(Map.Entry<Integer, IEndpointSource> entry : serviceSource.getEndpointMap().entrySet()){
			list.add((T) entry.getValue().getPortal());
		}
		
		return list;
	}
	
	public <T extends IServicePortal> T findDefaultPortal()
	{
		return (T) serviceSource.getDefaultEndpointSource().getPortal();
	}
 
	public <T extends IServicePortal> T findPortal(int endpointId) {
		IEndpointSource endpoint = serviceSource.getEndpoint(endpointId);
		
		if(endpoint != null){
			return (T) endpoint.getPortal();
		}
		
		return null;
	}

	public void update() {
		if(serviceSource != null){
			
			serviceSource.update();
		}
	}
	
}

package com.fire.core.bhns.source.source.cluster;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.log4j.Logger;

import com.fire.constant.CoreServiceConstants;
import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.ServiceManager;
import com.fire.core.bhns.data.ServiceData;
import com.fire.core.bhns.location.ISvrLocationPortal;
import com.fire.core.bhns.portal.AbstractLocalPortal;
import com.fire.core.bhns.portal.MethodInfo;
import com.fire.core.bhns.source.AbstractEndpointSource;
import com.fire.core.bhns.source.IServiceOption;
import com.fire.core.bhns.source.options.ClusterServiceOption;

public class EndpointSource extends AbstractEndpointSource
{
	private ClusterServiceOption serviceOption;

	private AbstractLocalPortal<?> portalmpl;

	private int endpointId;
	
	private static final Logger logger = Logger.getLogger(EndpointSource.class);
	
	public EndpointSource(ClusterServiceOption serviceOption, int endpointId)
	{
		this.serviceOption = serviceOption;
		this.endpointId = endpointId;
	}

	@Override
	public void initialize() 
	{
		if(inited){
			return;
		}
		try
		{
			if(serviceOption.serviceClass() != null)
				serviceOption.serviceClass().getConstructor(null)
				.setAccessible(true);
			Constructor<?> tmpConstructor = serviceOption.portalClass()
					.getConstructor(IServiceOption.class);
			
			this.portalmpl = (AbstractLocalPortal<?>) tmpConstructor.newInstance(serviceOption);
			this.portalmpl.setEndpointId(endpointId);
			if(this.portalmpl.initPortal()) {
				
				inited = true;
			}
		} catch (Exception e){
			logger.error("init fail portalId = " + getPortalId() + " endpointId = " + endpointId + "......");
		}
	}

	@Override
	public int getPortalId()
	{
		return serviceOption.portalId();
	}

	@Override
	public IServicePortal<? extends ISimpleService> getPortal()
	{
		return portalmpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fire.core.bhns.source.IEndpointSource#activeService(long, boolean)
	 */
	@Override
	public <T extends ISimpleService> T activeService(long svrid, int... params) 
	{ // 激活服务的结点服务器.处理逻辑.
		T tmpT = (T) portalmpl.activeService(svrid, params);
		if (tmpT != null)
		{
			List<ISvrLocationPortal> allPortal = ServiceManager.findAllPortal(CoreServiceConstants.BO_LOCATION);
			for (ISvrLocationPortal portal : allPortal)
			{
				portal.changeLocationOfserviceId(serviceOption.portalId(),
						svrid, this.endpointId);
			}
			
//			ISvrLocationPortal localSvrPortal = ServiceManager.findLocalPortal(CoreServiceConstants.BO_LOCATION);
//			localSvrPortal.saveServiceInfo(serviceOption.portalId(), svrid, endpointId, true);
			
		}
		return tmpT;
	}

	@Override
	public void destroyService(long svrid) 
	{
		if(this.getPortalId() == CoreServiceConstants.BO_LOCATION)
			return;
//		if(this.getPortalId() != CoreServiceConstants.BO_GATE)
//		{
//			IGateService gateService = BOServiceManager.findService(CoreServiceConstants.BO_GATE, svrid);
//			//玩家在线时候不能销毁服务
//			if(gateService != null)
//				return;
//		}
		
		List<ISvrLocationPortal> allPortal = ServiceManager.findAllPortal(CoreServiceConstants.BO_LOCATION);
		
		for (ISvrLocationPortal portal : allPortal)
			portal.changeLocationOfserviceId(
					serviceOption.portalId(), svrid, (byte) -1);
		
//		try {
//			ISvrLocationPortal localSvrPortal = ServiceManager.findLocalPortal(CoreServiceConstants.BO_LOCATION);
//			localSvrPortal.removeServiceInfo(serviceOption.portalId(), svrid, endpointId, true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		this.portalmpl.destroyService(svrid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T findService(long svrid,
			boolean forceReal) 
	{
		return (T) this.portalmpl.findService(svrid, forceReal);
	}

	@Override
	public ServiceData getServiceData(long serviceId)
	{
		return this.portalmpl.getServiceData(serviceId);
	}

	public Object invokeServiceMethod(
			@SuppressWarnings("rawtypes") ISimpleService service,
			String methodSignature, Object[] args)
	{
		if (serviceOption.getServiceMethodMap() != null)
		{
			MethodInfo tmpMethod = serviceOption.getServiceMethodMap()
					.getMeshodOfMethodSignature(methodSignature);
			try
			{
				return tmpMethod.getMethod().invoke(service, args);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public Object invokePortalMethod(String methodSignature, Object[] args)
	{
		if (serviceOption.getPortalMethodMap() != null)
		{
			MethodInfo tmpMethod = serviceOption.getPortalMethodMap()
					.getMeshodOfMethodSignature(methodSignature);
			try
			{
				return tmpMethod.getMethod().invoke(this.portalmpl, args);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isLocalImplemention()
	{
		return true;
	}

	@Override
	public int getEndpointId()
	{
		return endpointId;
	}

	@Override
	public void onSourceDestory(long svrid)
	{
		
	}

	@Override
	public void update() {
		if(portalmpl != null){
			portalmpl.update();
		}
	}
}

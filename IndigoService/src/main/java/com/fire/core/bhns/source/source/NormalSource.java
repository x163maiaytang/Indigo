package com.fire.core.bhns.source.source;

import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.portal.AbstractLocalPortal;
import com.fire.core.bhns.portal.MethodInfo;
import com.fire.core.bhns.source.IEndpointSource;
import com.fire.core.bhns.source.IServiceSource;
import com.fire.core.bhns.source.options.NormalServiceOption;
import com.fire.core.bhns.source.source.cluster.EndpointSource;

@Deprecated
public class NormalSource implements IServiceSource
{
	private static final Logger logger = Logger.getLogger(NormalSource.class);

	private NormalServiceOption serviceOption;
	private IEndpointSource endpoint;
	private byte[] endpointIdList;

	public NormalSource(NormalServiceOption serviceOption)
	{
		this.serviceOption = serviceOption;
		this.endpoint = null;
		endpointIdList = new byte[]
		{ 0 };
	}

	public void bindSource()
	{
		this.endpoint = new EndpointSource(serviceOption, 0);
	}

	@Override
	public void initialize() 
	{
		this.endpoint.initialize();
	}

	@Override
	public IEndpointSource getEndpoint(int endpointId)
	{
		return this.endpoint;
	}

	@Override
	public void destroyService(long svrid) 
	{
		endpoint.destroyService(svrid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T findService(long svrid,
			boolean forceReal) 
	{
		return (T) endpoint.findService(svrid, forceReal);
	}

	public <T extends AbstractLocalPortal> T getPortalImpl()
	{
		return (T) endpoint.getPortal();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T activeService(long svrid,
			int endpointId, int... params) 
	{
		T t = (T) endpoint.activeService(svrid);
//		if (isOrderActive)
//			ActiveServiceHolder.activeNextService(
//					this.serviceOption.portalId(), svrid);
		return t;
	}

	public Object invokeServiceMethod(
			@SuppressWarnings("rawtypes") long serviceId,
			String methodSignature, Object[] args) 
	{
		ISimpleService service = this.findService(serviceId, false);
		if (service != null && serviceOption.getServiceMethodMap() != null)
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

	public Object invokePortalMethod(int endpointId, String methodSignature,
			Object[] args) 
	{
		if (serviceOption.getPortalMethodMap() != null)
		{
			MethodInfo tmpMethod = serviceOption.getPortalMethodMap()
					.getMeshodOfMethodSignature(methodSignature);
			try
			{
				return tmpMethod.getMethod().invoke(endpoint.getPortal(), args);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isLocalImplemention(int endpointId)
	{
		return true;
	}

//	@Override
//	public byte[] endpointIdList()
//	{
//		return endpointIdList;
//	}

	@Override
	public int getEndPointSize()
	{
		return 1;
	}

	@Override
	public IEndpointSource getLocalEndPoint()
	{
		return endpoint;
	}

	@Override
	public void onSourceDestory(long svrid, int endpointId)
	{
	}

	@Override
	public Map<Integer, IEndpointSource> getEndpointMap() {
		return null;
	}

	@Override
	public IEndpointSource getDefaultEndpointSource() {
		return null;
	}

	@Override
	public void update() {
		
	}
}

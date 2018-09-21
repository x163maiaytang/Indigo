package com.fire.core.bhns.source;

import java.util.Map;

import com.fire.core.bhns.ISimpleService;

/**
 * 服务的源码
 */
public interface IServiceSource
{
	public void initialize();

	public void destroyService(long svrid);

	public boolean isLocalImplemention(int endpointId);
	
	public <T extends ISimpleService> T activeService(long svrid, int endpointId, int... params);

	public <T extends ISimpleService> T findService(long svrid,boolean forceReal);

//	public byte[] endpointIdList();
	
	public int getEndPointSize();
	
	public IEndpointSource getEndpoint(int endpointId);

	public IEndpointSource getLocalEndPoint();
	
	public Object invokeServiceMethod(long svrId, String methodSignature, Object[] args);
	
	public Object invokePortalMethod(int endpointId, String methodSignature, Object[] args);
	
	public void onSourceDestory(long svrid, int endpointId);

	public Map<Integer, IEndpointSource> getEndpointMap();
	
	public IEndpointSource getDefaultEndpointSource();

	public void update();
	
}

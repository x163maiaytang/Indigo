package com.fire.core.bhns.source.source;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fire.constant.CoreServiceConstants;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.ServiceManager;
import com.fire.core.bhns.location.ISvrLocationPortal;
import com.fire.core.bhns.portal.MethodInfo;
import com.fire.core.bhns.source.IEndpointSource;
import com.fire.core.bhns.source.IServiceSource;
import com.fire.core.bhns.source.options.ClusterServiceOption;
import com.fire.core.bhns.source.options.ServiceEndpointInfo;
import com.fire.core.bhns.source.source.cluster.EndpointSource;
import com.fire.core.bhns.source.source.cluster.RemoteEndpointSource;

public class ClusterSource implements IServiceSource
{
	private static final Logger logger = Logger.getLogger(ClusterSource.class);

	private ClusterServiceOption serviceOption;
	private ConcurrentHashMap<Integer, IEndpointSource> endpointMap;
//	private byte[] endpointIdList;
	private IEndpointSource localEndpointSource;
	private IEndpointSource defaultEndpointSource;
	
	public ClusterSource(ClusterServiceOption serviceOption)
	{
		this.serviceOption = serviceOption;
//		this.endpointList = new IEndpointSource[serviceOption
//				.getMaxEndpointId() + 1];
//		this.endpointIdList = null;
		this.endpointMap = new ConcurrentHashMap<Integer, IEndpointSource>();
	}

	public boolean regEndpoint(int endpointId, boolean local)
	{
//		if (endpointId >= 0 && endpointId < this.endpointMap.length)
//		{
		IEndpointSource tmpClusterEndpoint = null;
		if(local){
			
			tmpClusterEndpoint = new EndpointSource(serviceOption, endpointId);
			if (this.getLocalEndPoint() != null)
			{
//				logger.error("error reg two local endpoint in clusterSource",
//						new UnsupportedOperationException("two local endpoint"));
				return false;
			}
		}else{
			
			tmpClusterEndpoint = new RemoteEndpointSource(serviceOption, endpointId);
		}
		
			endpointMap.put(endpointId, tmpClusterEndpoint);
			
//		} else
//		{
//			logger.error("register endpoint error:unitId=" + endpointId);
//		}
			
			if(localEndpointSource == null){
				if(tmpClusterEndpoint.isLocalImplemention()){
					localEndpointSource = tmpClusterEndpoint;
				}
			}
			
			if(defaultEndpointSource == null){
				defaultEndpointSource = tmpClusterEndpoint;
			}
			
			return true;
	}
	
	public void unRegEndpoint(int endpointId){
		IEndpointSource remove = endpointMap.remove(endpointId);
		
		if(remove != null){
			if(remove == localEndpointSource){
				localEndpointSource = null;
			}
			
			if(defaultEndpointSource == remove){
				defaultEndpointSource = null;
				
				for(IEndpointSource source : endpointMap.values()){
					defaultEndpointSource = source;
					break;
				}
			}
		}
	}

	@Override
	public IEndpointSource getEndpoint(int endpointId)
	{
//		if (endpointId >= 0 && endpointId < this.endpointMap.length)
			return this.endpointMap.get(endpointId);
//		else
//			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.doteyplay.core.bhns.source.IServiceSource#endpointIdOfserviceId(int)
	 */
	@Override
	public void initialize() 
	{
//		byte[] tmpIdList = new byte[endpointMap.length];
//		int cursor = 0;

		ServiceEndpointInfo tmpEndpointInfo = null;
		for (byte i = 0; i < serviceOption.getEndpointSize(); i++)
		{
			tmpEndpointInfo = serviceOption.getEndpointInfoByIndex(i);
			if (tmpEndpointInfo != null)
			{
//				tmpIdList[cursor++] = (byte) i;
				if (endpointMap.get(tmpEndpointInfo.getEndpointId()) != null)
				{
					endpointMap.get(tmpEndpointInfo.getEndpointId()).initialize();
				}else{
					logger.error("error endpointId = " + tmpEndpointInfo.getEndpointId() + " not source !!!!!!!!");
				}
			}
			
		}
//		endpointIdList = new byte[cursor];
//		if (cursor > 0)
//			System.arraycopy(tmpIdList, 0, endpointIdList, 0, cursor);
	}

	@Override
	public boolean isLocalImplemention(int endpointId)
	{
//		if (endpointId >= 0 && endpointId < this.endpointMap.length
//				&& this.endpointMap[endpointId] != null)
//			return this.endpointMap[endpointId].isLocalImplemention();
//		else
//			return false;
		
//		IEndpointSource endpointSource = this.endpointMap.get(endpointId);
//		if(endpointSource != null){
//			return endpointSource.isLocalImplemention();
//		}
		if(localEndpointSource != null){
			return localEndpointSource.getEndpointId() == endpointId;
		}
		return false;
	}

	public IEndpointSource getLocalEndPoint()
	{
//		for (IEndpointSource es : endpointList)
//			if (es != null && es.isLocalImplemention())
//				return es;
		return localEndpointSource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T activeService(long svrid,
			int endpointId, int... params) 
	{
		T t = null;

		ISvrLocationPortal locationPortal = ServiceManager.findLocalPortal(CoreServiceConstants.BO_LOCATION);
		Integer tmpLocation = locationPortal.getEndpointIdOfserviceId(this.serviceOption.portalId(), svrid);
		
		int tmpOldLocation = (tmpLocation != null) ? tmpLocation.intValue(): -1;

		if (tmpOldLocation != -1){
			t = (T) this.endpointMap.get(tmpOldLocation).findService(svrid, false);
		}

		if (t == null)
		{
//			if (endpointId >= 0 && endpointId < this.endpointMap.length
//					&& this.endpointMap[endpointId] != null)
//				t = (T) this.endpointMap[endpointId].activeService(svrid);
			
		}else{
			this.endpointMap.get(tmpOldLocation).destroyService(svrid);
			t.release();
//			System.out.println("不能重复激活！！！！！！！！！！");
		}
		IEndpointSource endpointSource = endpointMap.get(endpointId);
		if(endpointSource != null){
			t = endpointSource.activeService(svrid, params);
		}
//		if (isOrderActive)
//			ActiveServiceHolder.activeNextService(
//					this.serviceOption.portalId(), svrid);
		return t;
	}

	@Override
	public void destroyService(long svrid) 
	{
		if (this.serviceOption.portalId() == CoreServiceConstants.BO_LOCATION)
			return;
//		if (this.serviceOption.portalId() != CoreServiceConstants.BO_GATE)
//		{
//			IGateService gateService = BOServiceManager.findService(
//					CoreServiceConstants.BO_GATE, svrid);
//			// 玩家在线时候不能销毁服务
//			if (gateService != null)
//				return;
//		}

		ISvrLocationPortal locationPortal = ServiceManager.findLocalPortal(CoreServiceConstants.BO_LOCATION);
		Integer tmpCurLocation = null;
		int tmpLocation = -1;
		tmpCurLocation = locationPortal.getEndpointIdOfserviceId(this.serviceOption.portalId(), svrid);
		if (tmpCurLocation != null)
		{
			tmpLocation = tmpCurLocation.intValue();
			
//			if (tmpLocation >= 0 && tmpLocation < this.endpointMap.length
//					&& this.endpointMap[tmpLocation] != null)
//				this.endpointMap[tmpLocation].destroyService(svrid);
			if(endpointMap.get(tmpLocation) != null){
				endpointMap.get(tmpLocation).destroyService(svrid);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISimpleService> T findService(long svrid,
			boolean forceReal) 
	{
		T r = null;
		int tmpLocation = -1;
		ISvrLocationPortal locationPortal = ServiceManager.findLocalPortal(CoreServiceConstants.BO_LOCATION);
		Integer tmpCurLocation = null;
		tmpCurLocation = locationPortal.getEndpointIdOfserviceId(this.serviceOption.portalId(), svrid);
		if (tmpCurLocation != null)
		{
			tmpLocation = tmpCurLocation.intValue();
			
			IEndpointSource endpointSource = this.endpointMap.get(tmpLocation);
			if (/*tmpLocation >= 0 && tmpLocation < this.endpointMap.length
					&&*/ endpointSource != null)
			{
				if (forceReal && !endpointSource.isLocalImplemention())
				{
					logger.error("非本地服务不能强制使用真实源码调用");
					forceReal = false;
				}

				r = (T)endpointSource.findService(svrid, forceReal);
			}
		}
		return r;
	}


	public Map<Integer, IEndpointSource> getEndpointMap() {
		return endpointMap;
	}

	@Override
	public int getEndPointSize()
	{
//		int size = 0;
//		for (IEndpointSource source : endpointMap)
//			if (source != null)
//				size++;
		return endpointMap.size();
	}

	@Override
	public Object invokeServiceMethod(long svrId, String methodSignature,
			Object[] args) 
	{
		ISimpleService service = this.getLocalEndPoint().findService(svrId, false);
		if (service != null && serviceOption.getServiceMethodMap() != null)
		{
			MethodInfo tmpMethod = serviceOption.getServiceMethodMap()
					.getMeshodOfMethodSignature(methodSignature);
			try
			{
//				if(CoreServiceConstants.SERVER_FIBER){
					
//					return Task.invoke(tmpMethod.getMethod(), service, args);
//				}else{
					
					return tmpMethod.getMethod().invoke(service, args);
//				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Object invokePortalMethod(int endpointId, String methodSignature,
			Object[] args) 
	{
		if (serviceOption.getPortalMethodMap() != null)
		{
			MethodInfo tmpMethod = serviceOption.getPortalMethodMap()
					.getMeshodOfMethodSignature(methodSignature);
			try
			{
				IEndpointSource endpointSource = endpointMap.get(endpointId);
//				if (endpointId >= 0 && endpointId < this.endpointMap.length
//						&& this.endpointMap[endpointId] != null)
//				{
//					return Task.invoke(tmpMethod,
//							this.endpointMap[endpointId].getPortal(), args);
//				}
				
				if(endpointSource != null && endpointSource.isInited()){
					
//					if(CoreServiceConstants.SERVER_FIBER){
//						
//						return Task.invoke(tmpMethod.getMethod(), endpointSource.getPortal(), args);
//					}else{
						return tmpMethod.getMethod().invoke(endpointSource.getPortal(), args);
//					}
				}
			} catch (Exception e)
			{
				logger.error("Method = " + tmpMethod.getMethod().getName(), e);
			}
		}
		return null;
	}

	@Override
	public void onSourceDestory(long svrid, int endpointId)
	{
		if(this.getEndpoint(endpointId) != null){
			this.getEndpoint(endpointId).onSourceDestory(svrid);
		}

	}

	@Override
	public IEndpointSource getDefaultEndpointSource() {
		if(localEndpointSource != null){
			return localEndpointSource;
		}
		return defaultEndpointSource;
	}

	@Override
	public void update() {
		if(localEndpointSource != null && localEndpointSource.isInited()){
			localEndpointSource.update();
		}
	}

}

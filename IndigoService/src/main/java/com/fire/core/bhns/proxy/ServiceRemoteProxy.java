package com.fire.core.bhns.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fire.constant.CoreServiceConstants;
import com.fire.core.bhns.INetSupport;
import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.ServiceManager;
import com.fire.core.bhns.actor.IActor;
import com.fire.core.bhns.location.ISvrLocationPortal;
import com.fire.core.bhns.net.action.DefaultClientAction;
import com.fire.core.bhns.net.message.CS_ServiceDefaultMessage;
import com.fire.core.bhns.portal.MethodInfo;
import com.fire.core.bhns.source.IServiceOption;

import game.net.server.SimpleClient;

public class ServiceRemoteProxy<T extends ISimpleService, P extends IServicePortal>
{
	private int portalId;
	private int endpointId;
	private INetSupport netSupport;
	private IServiceOption option;
	
	private Map<Long, ServiceProxyStatus<T>> serviceProxyCache;

	public ServiceRemoteProxy(int portalid, int endpointId,
			IServiceOption option, INetSupport netSupport)
	{
		this.netSupport = netSupport;
		this.endpointId = endpointId;
		this.portalId = portalid;
		this.option = option;
		serviceProxyCache = new ConcurrentHashMap<Long, ServiceProxyStatus<T>>();
	}

	private ServiceProxyStatus<T> createProxyStatus(long serviceId)
	{
		ServiceProxyStatus<T> tmpServiceProxyStatus = new ServiceProxyStatus<T>();
		try
		{
			@SuppressWarnings("unchecked")
			T tmpEntrance = (T) option.createSvrRemoteProxyInstance(serviceId, this);
			tmpServiceProxyStatus.bindEntrance(tmpEntrance);
			tmpServiceProxyStatus.setStatus(ServiceProxyStatus.INACTIVE_STATUS);
			return tmpServiceProxyStatus;
		} catch (Exception e)
		{
			tmpServiceProxyStatus.setStatus(ServiceProxyStatus.UNAVAILABLE_STATUS);
			e.printStackTrace();
		}
		return null;
	}

	private Object invokePortalMethodProxy(String methodname,
			MethodInfo methodInfo, byte intervalflag, long serviceId, Object[] paramters)
			
	{
		SimpleClient tmpNet = netSupport.getNetChannel();
		if (tmpNet != null)
		{
			ProxyBean tmpPB = new ProxyBean();
			tmpPB.setInternalFlag(intervalflag);
			tmpPB.setMethodSignature(methodname);
			tmpPB.setPortalId(this.portalId);
			tmpPB.setEndpointId(this.endpointId);
			tmpPB.setParameter(paramters);
			tmpPB.setServiceId(serviceId);
			CS_ServiceDefaultMessage message = new CS_ServiceDefaultMessage();
			message.setBean(tmpPB);

			IActor caller = null;
//			if (WorkerThread.class.isAssignableFrom(Thread.currentThread().getClass()))
//			if(CoreServiceConstants.SERVER_FIBER)
//			{
//				caller = Task.getCurrentTask();
//				Task task = Task.getCurrentTask();
//				if (task != null && IActor.class.isAssignableFrom(task.getClass()))
//					caller = (IActor) task;
//			} else {
//				caller = CallerTransmiter.getInstance().remove();
//			}else{
				
				caller = new FakeActor();
//			}

			
			if(methodInfo.isAsyn()){
				tmpNet.sendMessage(message);
				return methodInfo.getReturn(null);
			}
			
			message.setSynKey(caller.id());
			DefaultClientAction.getInstance().addWaitActor(caller, methodInfo);
//			if (CoreServiceConstants.SERVER_FIBER)
//			{
//				StopWatch st = new Log4JStopWatch();
//				try{
//					
//					if(tmpNet.sendMessage(message)){
//						return methodInfo.getReturn(caller.waitForRes(methodInfo.getTimeout()));
//					}
//				}finally{
//					
//					st.stop(methodname + "methodInfo.getTimeout() = " + methodInfo.getTimeout());
//				}
//			}else{
				
				FakeActor fakeCaller = (FakeActor) caller; 
				try
				{
					fakeCaller.getSyncLock().lock();
					if(tmpNet.sendMessage(message)){
						
						caller.waitForRes(methodInfo.getTimeout());
						
						return methodInfo.getReturn(caller.waitForRes());
					}
				}finally{
					fakeCaller.getSyncLock().unlock();
				}
//			}

		}
		return methodInfo.getReturn(null);
	}

	public void clearRemoteProxy(long serviceId)
	{
		serviceProxyCache.remove(serviceId);
	}

	/**
	 * 走到这一步说明服务已经激活过了
	 * 
	 * @param serviceId
	 * @return
	 */
	public T getServiceProxy(long serviceId)
	{
		if (serviceId <= 0)
			return null;

		try
		{
			ServiceProxyStatus<T> tmpServiceProxyStatus = serviceProxyCache.get(serviceId);
			if (tmpServiceProxyStatus == null)
			{
				tmpServiceProxyStatus = createProxyStatus(serviceId);
				tmpServiceProxyStatus.setStatus(ServiceProxyStatus.ACTIVE_STATUS);
				serviceProxyCache.put(serviceId, tmpServiceProxyStatus);
			}

			if (tmpServiceProxyStatus != null
					&& tmpServiceProxyStatus.getStatus() == ServiceProxyStatus.ACTIVE_STATUS)
				return tmpServiceProxyStatus.getEntrance();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public T activeServiceProxy(long serviceId)
			
	{
		if (serviceId <= 0)
			return null;

		invokePortalMethodProxy("active", new MethodInfo(null, 500, false), ProxyConst.FLAG_SERVICE_ACTIVE, serviceId,null);

		// 本地先知道这个服务已经激活
		ISvrLocationPortal localPortal = ServiceManager.findLocalPortal(CoreServiceConstants.BO_LOCATION);
		
		localPortal.changeLocationOfserviceId(getPortalId(), serviceId,endpointId);
//		localPortal.saveServiceInfo(portalId, serviceId, endpointId, false);

		return this.getServiceProxy(serviceId);
	}

	public void destroyServiceProxy(long serviceId) 
	{
		if (serviceId <= 0)
			return;
		try
		{
			invokePortalMethodProxy("destory", new MethodInfo(null, 500, false), ProxyConst.FLAG_SERVICE_DESTROY,
					serviceId, null);

//			// 本地先知道销毁了
//			ISvrLocationPortal localPortal = BOServiceManager
//					.findLocalPortal(Constants.BO_LOCATION);
//			localPortal.changeLocationOfserviceId(getPortalId(), serviceId,
//					(byte) -1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Object doService(long serviceId, String method, Object[] args)
			
	{
		if (this.netSupport != null && method != null){
			MethodInfo methodInfo = option.getServiceMethodMap().getMeshodOfMethodSignature(method);
			if(methodInfo != null){
				
				return invokePortalMethodProxy(method, methodInfo,
						ProxyConst.FLAG_SERVICE_INVOKE, serviceId, args);
			}
		}
		return null;
	}

	public Object doPortal(String method, Object[] args)
			
	{
		if (this.netSupport != null && method != null){
			
			MethodInfo methodInfo = option.getPortalMethodMap().getMeshodOfMethodSignature(method);
			if(methodInfo != null){
				
				return invokePortalMethodProxy(method, methodInfo,
						ProxyConst.FLAG_PORTAL_INVOKE, 0, args);
			}
		}
			
		return null;
	}

	public int getPortalId()
	{
		return portalId;
	}

	public int getEndpointId() {
		return endpointId;
	}
	
}

package com.fire.core.bhns.portal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fire.constant.CoreServiceConstants;
import com.fire.core.bhns.IServiceCreator;
import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.data.ServiceData;
import com.fire.core.bhns.source.IServiceOption;
import com.fire.core.messageagent.ITransferAgent;

/**
 * 本地的服务入口
 * 
 * @author
 * 
 */
public abstract class AbstractLocalPortal<T extends ISimpleService> implements
		IServicePortal<T>, IServiceCreator, ITransferAgent
{

	private static final Logger logger = Logger.getLogger(AbstractLocalPortal.class);

	protected IServiceOption option;

	protected Map<Long, T> activeServiceMap = new ConcurrentHashMap<Long, T>();
	
	protected int endpointId;
//	private ConcurrentHashMap<Long, T> serviceActorProxyMap = new ConcurrentHashMap();

//	private ConcurrentHashMap<Long, ServiceData> dataMap = new ConcurrentHashMap<Long, ServiceData>();

	public AbstractLocalPortal(IServiceOption option)
	{
		this.option = option;
	}
	
	/**
	 * 想要被动态代理必须提供一个默认构造方法
	 */
	public AbstractLocalPortal(){
		
	}

	public abstract boolean initPortal() ;// 初始化
	
	@Override
	public int getPortalId()
	{
		return option.portalId();
	}

	@Override
	public int getEndpointId() {
		return endpointId;
	}
	
	public void setEndpointId(int endpointId) {
		this.endpointId = endpointId;
	}

	@Override
	public T findService(long serviceId, boolean forceReal) 
	{
		if(!false)
			forceReal = true;
		
		T tmpImpl = activeServiceMap.get(serviceId);
		if (forceReal)
			return tmpImpl;

		// close playerActor
//		if (tmpImpl != null)
//		{
//			if (WorkerThread.class.isAssignableFrom(Thread.currentThread()
//					.getClass()))
//			{
//				IActor actor = (IActor) Task.getCurrentTask();
//				PlayerActor targetActor = PlayerActorManager.getInstnace().get(serviceId);
//				if (!actor.isNeedProxy(targetActor))
//					return tmpImpl;
//			}
//
//			T proxy = serviceActorProxyMap.get(serviceId);
//			return proxy;
//		}
		return tmpImpl;
	}

	public void resetServiceData(ISimpleService c, ServiceData newServiceData)
	{
//		if (c == null || newServiceData == null)
//			return;
//		long serviceId = c.getServiceId();
//		dataMap.put(serviceId, newServiceData);
	}

	public ServiceData getServiceData(long serviceId)
	{
//		ServiceData serviceData = this.dataMap.get(serviceId);
//		if (serviceData != null)
//			return serviceData;
		return null;
	}

	@Override
	public T activeService(long serviceId, int... params) 
	{
		T tmpImpl = this.findService(serviceId, false);
		// 如果为空重新创建
		if (tmpImpl == null)
			tmpImpl = safeNewInstance(serviceId, params);
		return (T) this.findService(serviceId, false);
	}

	@Override
	public void destroyService(long serviceId) 
	{
		T tmpImpl = this.activeServiceMap.remove(serviceId);
		if (tmpImpl != null)
		{
			// 销毁时必须强制使用代理类来销毁，防止之前别的actor调用的消息被忽略
//			T proxy = this.serviceActorProxyMap.remove(serviceId);
//			if (proxy == null)
				tmpImpl.release();
//			else
//				proxy.release();
			
//			activeServiceMap.remove(serviceId);
//			this.dataMap.remove(serviceId);
			tmpImpl = null;
		}
	}

	private T safeNewInstance(long serviceId, int... params) 
	{
		T c = (T) this.option.createSvrImpInstance();
		if (c == null)
			return null;
		activeServiceMap.put(serviceId, c);
		// close playerActor
//		if(!CoreServiceConstants.IS_SINGLETON_MODE)
//		{
//			T proxy = (T) option.createSvrActorProxyInstance(serviceId);
//			this.serviceActorProxyMap.put(serviceId, proxy);
//		}
		configInstance(serviceId, params);
		return c;
	}

	public void configInstance(long serviceId, int... params) 
	{
		//直接源码初始化
		T service = this.findService(serviceId, true);
		if (service != null)
		{
			service.init(this, this, serviceId);
//			ServiceData tmpServiceData = dataMap.get(serviceId);
//			if (tmpServiceData == null)
//			{
//				tmpServiceData = new ServiceData();
//				dataMap.put(serviceId, tmpServiceData);
//			}
//			service.bindServiceData(tmpServiceData);
			service.initialize(params);
			service.finishInit();
		}
	}

	public Collection<T> getActiveServices()
	{
		return activeServiceMap.values();
	}

	public Collection<Long> getActiveServiceIds()
	{
		return activeServiceMap.keySet();
	}
	

	public void update() {
		
	}

}

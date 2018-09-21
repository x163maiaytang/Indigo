package com.fire.core.bhns.source.options;

import org.apache.log4j.Logger;

import com.fire.core.bhns.AbstractSimpleService;
import com.fire.core.bhns.IMethodCollection;
import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.portal.SimpleMethodCollection;
import com.fire.core.bhns.proxy.ServiceRemoteProxy;
import com.fire.core.bhns.source.IServiceOption;

public abstract class BaseServiceOption implements IServiceOption
{
	protected static final Logger logger = Logger.getLogger(BaseServiceOption.class);

	private final static String POT_PROXY_PACKAGE = "com.fire.game.service.bo.proxy.remoteportal.";
	private final static String SVR_REMOTE_PROXY_PACKAGE = "com.fire.game.service.bo.proxy.remoteservice.";
	private final static String SVR_ACTOR_PROXY_PACKAGE = "com.fire.game.service.bo.proxy.actor.";

	private int _portalId; // 服务标识
	private Class<? extends ISimpleService> svrClass;// 服务接口
	private Class<? extends IServicePortal> ptoClass;// portal接口

	private Class<? extends ISimpleService> svrAcotrProxyClass;
	private Class<? extends ISimpleService> svrRemoteProxyClass;
	private Class<? extends IServicePortal> portalRemoteProxyClass;

	protected IMethodCollection srvMethodMap;// 函数快捷入口
	protected IMethodCollection ptoMethodMap;

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public BaseServiceOption(int ptid,
			Class<? extends ISimpleService> svrclass,
			Class<? extends IServicePortal> ptoClass)
	{
		this._portalId = ptid;
		this.svrClass = svrclass;
		this.ptoClass = ptoClass;

		try
		{
			if (svrclass != null)
			{
//				svrAcotrProxyClass = (Class<? extends ISimpleService>) Class
//						.forName(SVR_ACTOR_PROXY_PACKAGE
//								+ svrclass.getSimpleName() + "ActorProxy");
				svrRemoteProxyClass = (Class<? extends ISimpleService>) Class
						.forName(SVR_REMOTE_PROXY_PACKAGE + svrClass.getSimpleName()
								+ "RemoteProxy");
			}

			portalRemoteProxyClass = (Class<? extends IServicePortal>) Class
					.forName(POT_PROXY_PACKAGE + ptoClass.getSimpleName()
							+ "RemoteProxy");

			srvMethodMap = new SimpleMethodCollection(this.svrClass);
			ptoMethodMap = new SimpleMethodCollection(this.ptoClass);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public int portalId()
	{
		return _portalId;
	}

	public IMethodCollection getServiceMethodMap()
	{
		return srvMethodMap;
	}

	public IMethodCollection getPortalMethodMap()
	{
		return ptoMethodMap;
	}

	public Class<? extends ISimpleService> serviceClass()
	{
		return svrClass;
	}

	public Class<? extends IServicePortal> portalClass()
	{
		return ptoClass;
	}

	@Override
	public Class<? extends ISimpleService> serviceActorProxyClass()
	{
		return svrAcotrProxyClass;
	}

	@Override
	public AbstractSimpleService createSvrImpInstance()
	{
		if (this.serviceClass() != null)
		{
			try
			{
				return (AbstractSimpleService<?>) this.serviceClass()
						.newInstance();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public AbstractSimpleService createSvrActorProxyInstance(long serviceId)
	{
		try
		{
			return (AbstractSimpleService) this.svrAcotrProxyClass
					.getConstructor(long.class, int.class).newInstance(
							serviceId, this._portalId);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public AbstractSimpleService createSvrRemoteProxyInstance(long serviceId,
			ServiceRemoteProxy<?, ?> proxy)
	{
		try
		{
			return (AbstractSimpleService) this.svrRemoteProxyClass
					.getConstructor(long.class, ServiceRemoteProxy.class)
					.newInstance(serviceId, proxy);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IServicePortal createPortalRemoteProxyInstance(ServiceRemoteProxy<?,?> proxy, IServiceOption serviceOption)
	{
		try
		{
			return (IServicePortal) this.portalRemoteProxyClass.getConstructor(
					ServiceRemoteProxy.class, IServiceOption.class).newInstance(proxy, serviceOption);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

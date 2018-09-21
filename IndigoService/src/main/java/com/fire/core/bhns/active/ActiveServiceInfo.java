package com.fire.core.bhns.active;

import org.apache.log4j.Logger;

import com.fire.core.bhns.ServiceAnnotationInfo;
import com.fire.core.bhns.source.loadbalance.ILoadBalanceRule;
import com.fire.core.util.dependence.IDependence;

public class ActiveServiceInfo implements IDependence
{
	private static final Logger logger = Logger
			.getLogger(ActiveServiceInfo.class);

	private int portalId;
	private String[] dependences;
	private Class<? extends ILoadBalanceRule> ruleClass;
	private ILoadBalanceRule rule;

	public ActiveServiceInfo(ServiceAnnotationInfo info)
	{
		this.portalId = info.portalId();
		this.dependences = new String[info.depandence().length];
		
		int i = 0;
		for (int d : info.depandence())
		{
			this.dependences[i] = String.valueOf(d);
			i++;
		}
		
		if(info.rule() != null && ILoadBalanceRule.class.isAssignableFrom(info.rule()))
		{
			try
			{
				this.ruleClass = info.rule();
				this.rule = ruleClass.newInstance();

			} catch (InstantiationException e)
			{
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	public int getPortalId()
	{
		return portalId;
	}

	@Override
	public String[] getDependence()
	{
		return dependences;
	}
	
	public ILoadBalanceRule getRule()
	{
		return rule;
	}
}

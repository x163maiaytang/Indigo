package com.fire.core.bhns.active;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.core.bhns.ServiceAnnotationInfo;
import com.fire.core.bhns.ServiceManager;
import com.fire.core.util.dependence.DepandenceSorter;
import com.fire.core.util.dependence.DependenceScaner;
import com.fire.core.util.dependence.IDependence;
import com.fire.core.util.dependence.IDependenceAssembly;

/**
 * 服务的激活管理 改为通过annotation的方式激活
 * 
 * @author
 * 
 */
@Deprecated
public class ActiveServiceHolder implements IDependenceAssembly
{
	private static final Logger logger = Logger
			.getLogger(ActiveServiceHolder.class);
	/**
	 * 存贮service.<Key是服务器的Id,Value是服务的简单信息封装>
	 */
	private Map<Integer, ActiveServiceInfo> activeIdInfoMap = new HashMap<Integer, ActiveServiceInfo>();
	/**
	 * 排序后的List
	 */
	private List<String> serivceSortedList = new ArrayList<String>();

	public void init(List<ServiceAnnotationInfo> annotationInfos)
	{
		if (annotationInfos == null || annotationInfos.size() <= 0)
			return;

		for (ServiceAnnotationInfo annotationInfo : annotationInfos)
		{
			if (annotationInfo.portalId() <= 0)
			{
				logger.error("Invalid active setting,portalId="
						+ annotationInfo.portalId());
				return;
			}
			// 构造入口的信息存贮类.
			ActiveServiceInfo activeInfo = new ActiveServiceInfo(annotationInfo);
			this.activeIdInfoMap.put(annotationInfo.portalId(), activeInfo);
		}

		boolean check = this.checkDependRelation();
		if (!check)
			throw new RuntimeException("activeService depaendRelation error");

		DepandenceSorter sorter = new DepandenceSorter();
		for (ActiveServiceInfo info : activeIdInfoMap.values())
			sorter.addElement(String.valueOf(info.getPortalId()),
					info.getDependence());

		serivceSortedList = sorter.sort();
	}

	public void active_all_service(long serviceId) 
	{
		if (serviceId <= 0)
			return;

		if (serivceSortedList != null && serivceSortedList.size() > 0)
		{
			int portalId = Integer.parseInt(serivceSortedList.get(0));
			ActiveServiceInfo info = activeIdInfoMap.get(portalId);
			ServiceManager.activeService(info.getPortalId(), serviceId, info.getRule());
		}
	}

	public void active_next_service(int portalId, long serviceId) 
	{
		int curIndex = serivceSortedList.indexOf(String.valueOf(portalId));
		int nextIndex = 0;
		if (curIndex < serivceSortedList.size() - 1)
		{
			nextIndex = curIndex + 1;

			int nextPortalId = Integer.parseInt(serivceSortedList
					.get(nextIndex));
			ActiveServiceInfo info = activeIdInfoMap.get(nextPortalId);
			ServiceManager.activeService(info.getPortalId(), serviceId, info.getRule());
		} else
			return;
	}

	public void active_service(int portalId, long serviceId) 
	{
		ActiveServiceInfo info = activeIdInfoMap.get(portalId);
		ServiceManager.activeService(info.getPortalId(), serviceId, info.getRule());
	}

	public boolean checkDependRelation()
	{
		Collection<ActiveServiceInfo> tmpBlockInfos = activeIdInfoMap.values();
		if (tmpBlockInfos != null)
		{
			for (ActiveServiceInfo tmpInfo : tmpBlockInfos)
			{
				if (!DependenceScaner.checkValidation(
						String.valueOf(tmpInfo.getPortalId()), this))
				{
					logger.error("dependence of " + tmpInfo.getPortalId()
							+ " is invalid");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public IDependence getItem(String name)
	{
		if (name != null)
			return activeIdInfoMap.get(Integer.parseInt(name));
		else
			return null;
	}

	// **********************************************************************
	private static ActiveServiceHolder instance;

	private synchronized static ActiveServiceHolder getInstance()
	{
		if (instance == null)
		{
			instance = new ActiveServiceHolder();
		}
		return instance;

	}

	public static void initialize(List<ServiceAnnotationInfo> annotationInfos)
	{
		ActiveServiceHolder tmpDataStore = getInstance();
		if (tmpDataStore != null)
		{
			tmpDataStore.init(annotationInfos);
		}
	}

	public static void activeAllService(long serviceId)
	{
		ActiveServiceHolder tmpDataStore = getInstance();
		if (tmpDataStore != null)
		{
			tmpDataStore.active_all_service(serviceId);
		}
	}

	public static void activeNextService(int curPortalId, long serviceId)
	{
		ActiveServiceHolder tmpDataStore = getInstance();
		if (tmpDataStore != null)
		{
			tmpDataStore.active_next_service(curPortalId, serviceId);
		}
	}
}

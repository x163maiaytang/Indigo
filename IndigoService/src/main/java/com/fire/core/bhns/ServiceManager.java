package com.fire.core.bhns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.fire.constant.CoreServiceConstants;
import com.fire.core.bhns.source.IEndpointSource;
import com.fire.core.bhns.source.loadbalance.ILoadBalanceRule;
import com.fire.core.bhns.source.options.ClusterServiceOption;
import com.fire.core.bhns.source.source.ClusterSource;
import com.fire.task.taskpool.ServiceLogicTask;

/**
 * 服务管理器
 * 
 * @author
 * portalId > endpointId > serviceId > 
 */
public class ServiceManager implements IServiceInfoAssembly {
	
	private static final Logger logger = Logger.getLogger(ServiceManager.class);

	
//	private static List<ServiceInfo> addServiceList = new ArrayList<>();
//	
//	private static boolean[] initCheck = new boolean[30]; 
//	private static boolean initFlag = false;
	/**
	 * 存贮服务信息的Map
	 */
//	private static Map<Integer, RemoteServiceInfo> remoteServiceMap = new ConcurrentHashMap<Integer, RemoteServiceInfo>();
	private static Map<Integer, ServiceInfo> serviceMap = new HashMap<Integer, ServiceInfo>();
	private static CopyOnWriteArrayList<ServiceInfo> serviceList = new CopyOnWriteArrayList<ServiceInfo>();
	
	

	// public Map<Integer, ServiceInfo> get_serviceInfoMap()
	// {
	// return serviceMap;
	// }

	// public void register_service_info(ServiceInfo svrinfo)
	// {
	// if (svrinfo != null)
	// serviceMap.put(svrinfo.getPortalId(), svrinfo);
	// }
	//
	// public ServiceInfo get_service_info(int portalId)
	// {
	// if (portalId >= 0)
	// return serviceMap.get(portalId);
	// else
	// {
	// logger.error("portalid is invalid");
	// return null;
	// }
	// }

	// @SuppressWarnings("unchecked")
	// private <T extends ISimpleService> T active_service(int portalId,
	// long serviceId,byte endpointId, boolean isOrderActive) 
	// {
	// if (serviceId <= 0)
	// return null;
	//
	// ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
	// if (tmpBHNSInfo != null)
	// return (T) tmpBHNSInfo.activeService(serviceId,
	// endpointId,isOrderActive);
	// else
	// {
	// logger.error("service info is invalid,portalid=:" + portalId);
	// return null;
	// }
	// }

	// public <T extends ISimpleService> T find_service(int portalId,
	// long serviceId)
	// {
	// ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
	// if (tmpBHNSInfo != null)
	// return (T) tmpBHNSInfo.findService(serviceId,false);
	// else
	// {
	// logger.error("find_service:service info is invalid,portalid=:" +
	// portalId);
	// return null;
	// }
	// }

	// public byte find_local_endpointId(int portalId)
	// {
	// ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
	// if (tmpBHNSInfo != null)
	// {
	// IEndpointSource endpointSource = tmpBHNSInfo.source()
	// .getLocalEndPoint();
	// if (endpointSource != null)
	// return endpointSource.getEndpointId();
	// }
	// return -1;
	// }

	// public <P extends IServicePortal> P find_local_portal(int portalId)
	// {
	// ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
	// if(tmpBHNSInfo != null)
	// {
	// IEndpointSource endpointSource = tmpBHNSInfo.source().getLocalEndPoint();
	// if(endpointSource != null)
	// return (P)endpointSource.getPortal();
	// }
	// return null;
	// }

	// public <T extends IServicePortal> Map<Byte, T> find_all_portal(int
	// portalId)
	// {
	// ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
	// if (tmpBHNSInfo != null)
	// return tmpBHNSInfo.findAllPortal();
	// else
	// {
	// logger.error("service portal is invalid,portalid=:" + portalId);
	// return null;
	// }
	// }

	// public <T extends IServicePortal> T find_default_portal(int portalId)
	// {
	// ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
	// if (tmpBHNSInfo != null)
	// return tmpBHNSInfo.findDefaultPortal();
	// else
	// {
	// logger.error("service portal is invalid,portalid=:" + portalId);
	// return null;
	// }
	// }

	// public <T extends ISimpleService> void destroy_service(int portalId,
	// long serviceId) 
	// {
	// if (portalId <= 0)
	// return;
	//
	// ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
	// if (tmpBHNSInfo != null)
	// {
	// tmpBHNSInfo.destoryService(serviceId);
	// } else
	// logger.error("service info is invalid,portalid=:" + portalId);
	// }

	// public void initial_service()
	// {
	// for (ServiceInfo bhns : serviceMap.values())
	// {
	// bhns.initialSource();
	// }
	// }

	// *************************************************
	// private final static BOServiceManager _instance = new BOServiceManager();
	static{
		ServiceLogicTask.getInstance().scheduleAtFixedRate(new BOServiceTaskImpl(), 100, 100);
	}

	public static void registerServiceInfo(ServiceInfo serviceInfo, boolean localService)  {

		if (serviceInfo != null) {
			
//			remoteServiceMap.put(remoteInfo.getEndpointId(), remoteInfo);
			if(!serviceMap.containsKey(serviceInfo.getPortalId())){
				
				serviceList.add(serviceInfo);
				serviceMap.put(serviceInfo.getPortalId(), serviceInfo);
			}
			
			serviceInfo.initialSource();
			

			
//			if(serviceMap.get(2) != null){
//				List<ServiceInfo> tempList = new ArrayList<>();
//				tempList.addAll(serviceList);
//				serviceList.clear();
//				Collections.sort(tempList, new Comparator<ServiceInfo>() {
//					@Override
//					public int compare(ServiceInfo o1, ServiceInfo o2) {
//						if(o1.getPortalId() > o2.getPortalId()){
//							return 1;
//						}else if(o1.getPortalId() < o2.getPortalId()){
//							return -1;
//						}
//						return 0;
//					}
//				});
//				serviceList.addAll(tempList);
//			}else{
//				return;
//			}
//			logger.info("===============================START INIT SERIVICE ================================");
			
			
//			initialService();
		}
	}
	
	
	public static void initialAllService()  {
		
		for (ServiceInfo bhns : serviceMap.values()) {
			bhns.initialSource();
		}
		
	}

//	public static void registerServiceInfo(ServiceInfo svrinfo) {
//		if (svrinfo != null) {
//			serviceMap.put(svrinfo.getPortalId(), svrinfo);
//			serviceList.add(svrinfo);
//		}
//	}

	public static void unRegisterServerInfo(RemoteServiceInfo remoteInfo) {
//		remoteServiceMap.remove(remoteInfo.getEndpointId());
		
		List<Integer> portalIds = CoreServiceConstants.getAllServiceId(remoteInfo.getLocalService());
		byte serverId = (byte) remoteInfo.getServerId();
		
		for (int portalId : portalIds) {
			ServiceInfo serviceInfo = getServiceInfo(portalId);

			if (serviceInfo != null) {
				List<Integer> removeEndpointList = new ArrayList<Integer>();
				boolean flag = ((ClusterServiceOption) serviceInfo.option()).unRegEndpointInfo(serverId, removeEndpointList);

				for(Integer endpointId : removeEndpointList){
					((ClusterSource) serviceInfo.source()).unRegEndpoint(endpointId);
				}

				if (!flag) {
					ServiceInfo remove = serviceMap.remove(serviceInfo.getPortalId());
					if(remove != null){
						serviceList.remove(remove);
					}
				}
			}
		}
	}

	public static <T extends ISimpleService> ServiceInfo getServiceInfo(
			int portalId) {
		if (portalId >= 0)
			return serviceMap.get(portalId);
		else {
			logger.error("portalid is invalid");
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ISimpleService> T activeService(int portalId,
			long serviceId, int endpointId, int... params)
			 {

		if (serviceId <= 0)
			return null;

		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null)
			return (T) tmpBHNSInfo.activeService(serviceId, endpointId, params);
		else {
			logger.error("service info is invalid,portalid=:" + portalId);
			return null;
		}
	}
	

	@SuppressWarnings("unchecked")
	public static <T extends ISimpleService> T activeService(int portalId,
			long serviceId, ILoadBalanceRule rule)
			 {
		if (rule == null){
			int endpointId = findDefaultPortal(portalId).getEndpointId();
			return (T) activeService(portalId, serviceId, endpointId);
		}else{
			return (T) activeService(portalId, serviceId, rule.getEndPoint(serviceId, portalId));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ISimpleService> T findService(int portalId,
			long serviceId)  {

		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null)
			return (T) tmpBHNSInfo.findService(serviceId, false);
		else {
			logger.error("find_service:service info is invalid,portalid=:"
					+ portalId);
			return null;
		}
	}

	public static <T extends ISimpleService> void destroyService(int portalId,
			long serviceId)  {
		if (portalId <= 0)
			return;

		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null) {
			tmpBHNSInfo.destoryService(serviceId);
		} else {

			logger.error("service info is invalid,portalid=:" + portalId);
		}
	}



	public static <T extends IServicePortal> List<T> findAllPortal(
			int portalId) {

		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null)
			return tmpBHNSInfo.findAllPortal();
		else {
			logger.error("service portal is invalid,portalid=:" + portalId);
			return null;
		}
	}

//	public static <T extends IServicePortal> Map<Byte, T> findAllPortal(
//			Class<T> clz) {
//		int portalId = getPortalId(clz);
//		return findAllPortal(portalId);
//	}

	public static boolean havePortal(int portalId) {
		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null) {
			return tmpBHNSInfo.findDefaultPortal() != null;
		}
		
		return false;
	}
	public static <P extends IServicePortal> P findDefaultPortal(int portalId) {
		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null)
			return tmpBHNSInfo.findDefaultPortal();
		else {
			logger.error("service portal is invalid,portalid=:" + portalId);
			return null;
		}
	}

	public static <P extends IServicePortal> P findPortal(int portalId,
			int endpointId) {
		return (P) serviceMap.get(portalId).findPortal(endpointId);
	}

//	private static int getPortalId(Class<? extends IServicePortal> clz) {
//		return ((ServicePortalInfo) clz.getAnnotation(ServicePortalInfo.class)).portalId();
//	}

//	public static void activeAllService(long serviceId)  {
//		ActiveServiceHolder.activeAllService(serviceId);
//	}

//	public static void destoryAllService(long serviceId)  {
//		for (ServiceInfo info : serviceMap.values()){
//			info.source().destroyService(serviceId);
//		}
//	}

	public static int findLocalEndpointId(int portalId) {
		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null) {
			IEndpointSource endpointSource = tmpBHNSInfo.source()
					.getLocalEndPoint();
			if (endpointSource != null)
				return endpointSource.getEndpointId();
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	public static <P extends IServicePortal> P findLocalPortal(int portalId) {
		ServiceInfo tmpBHNSInfo = serviceMap.get(portalId);
		if (tmpBHNSInfo != null) {
			IEndpointSource endpointSource = tmpBHNSInfo.source()
					.getLocalEndPoint();
			if (endpointSource != null)
				return (P) endpointSource.getPortal();
		}
		return null;
	}
	

	public static void update() {
		
		for(ServiceInfo info : serviceList){
//			if(info.getPortalId() == 9) {
//				System.out.println();
//			}
			try {
				info.update();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		
	}
	
}

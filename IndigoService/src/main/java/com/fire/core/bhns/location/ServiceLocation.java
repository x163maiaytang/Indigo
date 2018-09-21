package com.fire.core.bhns.location;

public class ServiceLocation {
//	private Map<Integer, ServiceLocationInfo> portalLocationMap = new ConcurrentHashMap<Integer, ServiceLocationInfo>();

	private static ServiceLocation instance = new ServiceLocation();

	private ServiceLocation() {

	}

	public static ServiceLocation getInstance() {
		return instance;
	}

//	public int getEndpointIdOfserviceId(int portalId, long serviceId) throws Pausable {
//		ServiceLocationInfo tmpServiceLocationInfo = portalLocationMap.get(portalId);
//		if (tmpServiceLocationInfo != null) {
//
//			return tmpServiceLocationInfo.getLocation(serviceId);
//		}
//
//		return -1;
//	}
//
//	public void saveRoleInfo(int portalId, long roleId, int endpointId, boolean local) throws Pausable {
//		if (portalId >= 0 && roleId >= 0) {
//			ServiceLocationInfo tmpServiceLocationInfo = portalLocationMap.get(portalId);
//			if (tmpServiceLocationInfo == null) {
//				tmpServiceLocationInfo = new ServiceLocationInfo(portalId);
//				portalLocationMap.put(portalId, tmpServiceLocationInfo);
//			}
//
//			tmpServiceLocationInfo.changeLocation(roleId, endpointId);
//
//			if (local) {
//
//				SvrInfo info = new SvrInfo(portalId, roleId, endpointId);
//				ZookeeperService.getZkService().registSvrPos(info.toJson(), true);
//			}
//		}
//	}
//
//	public void removeRoleInfo(int portalId, long roleId, int endpointId, boolean local) throws Pausable {
//		ServiceLocationInfo tmpServiceLocationInfo = portalLocationMap.get(portalId);
//		if (tmpServiceLocationInfo != null) {
//			Integer oldEndpointId = tmpServiceLocationInfo.changeLocation(roleId, -1);
//
//			ServiceInfo serviceInfo = ServiceManager.getServiceInfo(portalId);
//			
//			if(serviceInfo != null){
//				if(serviceInfo.source() != null){
//					if(oldEndpointId != null){
//						serviceInfo.source().onSourceDestory(roleId, oldEndpointId);
//					}
//				}
//			}
//
//			if (local) {
//
//				SvrInfo info = new SvrInfo(portalId, roleId, endpointId);
//				ZookeeperService.getZkService().registSvrPos(info.toJson(), false);
//			}
//		}
//	}
}

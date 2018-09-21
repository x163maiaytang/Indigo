package com.fire.core.bhns.location;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLocationInfo
{
	private int portalId;
	private ConcurrentHashMap<Long, Integer> serviceLocationMap;
	
	
//	private Map<Byte, List<Long>> endpointServiceMap; 

	public ServiceLocationInfo(int portalId)
	{
		this.portalId = portalId;
		serviceLocationMap = new ConcurrentHashMap<Long, Integer>();
		
//		endpointServiceMap = new HashMap<Byte, List<Long>>();
	}

	public int getPortalId()
	{
		return portalId;
	}

	public Integer changeLocation(long serviceId, int endpointId)
	{
		
		if(endpointId < 0){
			
			Integer remove = serviceLocationMap.remove(serviceId);
			
			
			return remove;
		}else{
			
			serviceLocationMap.put(serviceId, endpointId);
		}
		
		
		return endpointId;
//		Integer tmpLocation = serviceLocationMap.get(serviceId);
//		if (tmpLocation == null){
//		}else if (tmpLocation.byteValue() != endpointId){
//		}
		
		//FIXME:CLEAR REMOTE PROXY
		
//		if(endpointId < 0){
//			
//			if(tmpLocation != null){
//				
//				List<Long> tempList = endpointServiceMap.get(tmpLocation);
//				if(tempList != null){
//					tempList.remove(serviceId);
//				}
//			}
//		}else{
			
//			List<Long> serviceList = endpointServiceMap.get(endpointId);
//			if(serviceList == null){
//				serviceList = new ArrayList<Long>();
//				endpointServiceMap.put(endpointId, serviceList);
//			}
//			
//			if(!serviceList.contains(serviceId)){
//				serviceList.add(serviceId);
//			}
//		}
		
	}
	

	public ConcurrentHashMap<Long, Integer> getServiceLocationMap() {
		return serviceLocationMap;
	}

	public int getLocation(long serviceId)
	{
		Integer tmpLocation = serviceLocationMap.get(serviceId);
		return (tmpLocation != null) ? tmpLocation.intValue() :  -1;
	}

	public void serverLost(int endpointId) {
		for(Map.Entry<Long, Integer> entry : serviceLocationMap.entrySet()){
			if(entry.getValue() == endpointId){
				serviceLocationMap.put(entry.getKey(), -1);
			}
		}
	}

	public void print() {
		for(Map.Entry<Long, Integer> entry : serviceLocationMap.entrySet()){
			
			System.out.println("serviceId = " + entry.getKey() + " endpointId = " + entry.getValue());
		}
	}


//	public static void main(String[] args) {
//		
//		
//		Map<Integer, String> map = new HashMap<>();
//		
//		map.put(1, "dd");
//		String remove = map.remove(1);
//		
//		System.out.println(remove);
//	}

//	public List<Long> getActiveService(byte endpointId) {
//		return endpointServiceMap.get(endpointId);
//	}
}

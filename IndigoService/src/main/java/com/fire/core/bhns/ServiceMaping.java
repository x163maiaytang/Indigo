package com.fire.core.bhns;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class ServiceMaping {
	
	private static final Logger logger = Logger.getLogger(ServiceMaping.class);

	private static Map<Integer, Integer> SERVICE_MAPING_GROUP = null;
	private static Map<Integer, List<Integer>> GROUP_MAPING_SERVICE = null;
	 
	
	public static void init(){
		
	}
	
	public static int SERVICE_MAPING_GROUP(int serviceId){
		return SERVICE_MAPING_GROUP.get(serviceId);
	}
	
	public static List<Integer> GROUP_MAPING_SERVICE(int groupId){
		return GROUP_MAPING_SERVICE.get(groupId);
	}
	
	public static void set(Map<Integer, Integer> SERVICE_MAPING_GROUP_PARAM, Map<Integer, List<Integer>> GROUP_MAPING_SERVICE_PARAM){
		SERVICE_MAPING_GROUP = SERVICE_MAPING_GROUP_PARAM;
		GROUP_MAPING_SERVICE = GROUP_MAPING_SERVICE_PARAM;
	}
}

package com.fire.core.bhns.location;

import java.util.List;
import java.util.Map;

import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.MethodAnnoInfo;
import com.fire.core.bhns.RemoteServiceInfo;

public interface ISvrLocationPortal extends IServicePortal
{
	public int getEndpointIdOfserviceId(int portalId, long serviceId) ; 

	@MethodAnnoInfo(asyn = true)
	public void changeLocationOfserviceId(int portalId, long serviceId, int endpointId) ;

//	public void saveServiceInfo(int portalId, long svrid, int endpointId, boolean local) ;
//
//	public void removeServiceInfo(int portalId, long svrid, int endpointId, boolean local) ;

	void reloadResource(List<String> successList) ;

	public void serverLost(RemoteServiceInfo remoteInfo) ;

	public Map<Integer, ServiceLocationInfo> getAllEndpointInfo() ;

//	@MethodAnnoInfo(asyn = true)
//	public void syncAllEndpointInfo(Map<Integer, ServiceLocationInfo> allEndpointInfo) ;  

}

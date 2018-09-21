package com.fire.core.bhns.source.options;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;
import com.fire.core.bhns.source.IServiceOption;

public class ClusterServiceOption extends BaseServiceOption implements
		IServiceOption
{
	private static final Logger logger = Logger.getLogger(ClusterServiceOption.class);

//	public final static byte MAX_UNIT_ID = 127;

//	private ServiceEndpointInfo[] endpointList;
	// 多读少些 线程安全
	private CopyOnWriteArrayList<ServiceEndpointInfo> endpointList;
	
	
	private int parentServiceId;
	
	public ClusterServiceOption(int ptid,
			Class<? extends ISimpleService> svrclass,
			Class<? extends IServicePortal> ptoClass, byte maxEndPointId)
	{
		super(ptid, svrclass, ptoClass);
//		if (maxEndPointId < 1 || maxEndPointId > MAX_UNIT_ID)
//			maxEndPointId = MAX_UNIT_ID;
//		endpointList = new ServiceEndpointInfo[maxEndPointId + 1];
		
		endpointList = new CopyOnWriteArrayList<ServiceEndpointInfo>();
	}

	@Override
	public byte configType()
	{
		return IServiceOption.CONFIG_TYPE_CLUSTER;
	}

	public int regEndpointInfo(int areaId, byte serverId, String endpointIP, int endpointPort)
	{
		if (endpointIP != null && endpointIP.length() >= 0 
				&& endpointPort > 0 && endpointPort < 65535)
		{
			int endpointId = areaId * serverId;
			
			endpointList.add(new ServiceEndpointInfo(areaId, serverId, endpointId, endpointIP, endpointPort));
			
			return endpointId;
		} else
		{
			logger.error("register endpointinfo error:portalId="
					+ this.portalId() + ",endpointIP=" + endpointIP + ",endpointPort="
					+ endpointPort);
		}
		
		return -1;
	}
	
	public boolean unRegEndpointInfo(byte serverId, List<Integer> removeEndpointList){
		for(ServiceEndpointInfo info : endpointList){
			if(info.getServerId() == serverId){
				endpointList.remove(info);
				removeEndpointList.add(info.getEndpointId());
			}
		}
		
		return endpointList.size() > 0;
	}


	public ServiceEndpointInfo getEndpointInfo(int endpointId)
	{
		for(ServiceEndpointInfo info : endpointList){
			if(info.getEndpointId() == endpointId){
				return info;
			}
		}
		
		return null;
	}
	
	public int getEndpointSize(){
		return endpointList.size();
	}
	
	public ServiceEndpointInfo getEndpointInfoByIndex(int index){
		
		return endpointList.get(index);
	}

	public int getParentServiceId() {
		return parentServiceId;
	}

	public void setParentServiceId(int parentServiceId) {
		this.parentServiceId = parentServiceId;
	}
	
	
}

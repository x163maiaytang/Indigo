package com.fire.core.bhns;

import java.util.ArrayList;
import java.util.List;

public class RemoteServiceInfo {
	

	private int regionId;
	
	private int endpointId;
	
	private int areaId;
	
	private int serverId;
	
	private String ip;
	
	private int port;
	
	private String nodeName;
	
	private int localService;
	
	private List<Integer> sceneClassIdList;

	private boolean crossServer;
	
	public RemoteServiceInfo() {
		
	}
	
	public RemoteServiceInfo(int localService, String ip, int port, int areaId, int serverId, int regionId, boolean crossServer) {
		this();
		this.regionId = regionId;
		this.areaId = areaId;
		this.serverId = serverId;
		this.localService = localService;
		this.ip = ip;
		this.port = port;
		this.endpointId = areaId * serverId;
		this.nodeName = areaId + "_" + serverId;
		this.crossServer = crossServer;

	}

	public int getServerId() {
		return serverId;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public int getAreaId() {
		return areaId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public int getEndpointId() {
		return endpointId;
	}


	public int getRegionId() {
		return regionId;
	}

	public void addSceneClassId(int sceneClassId){
		if(sceneClassIdList == null){
			sceneClassIdList = new ArrayList<>();
		}
		
		sceneClassIdList.add(sceneClassId);
	}
	
	public List<Integer> getSceneClassIdList() {
		return sceneClassIdList;
	}

 
	@Override
	public String toString() {
		return "RemoteServiceInfo [regionId=" + regionId + ", endpointId=" + endpointId + ", areaId=" + areaId
				+ ", serverId=" + serverId + ", ip=" + ip + ", port=" + port + ", nodeName=" + nodeName
				+ ", localService=" + localService + ", sceneClassIdList=" + sceneClassIdList + ", crossServer="
				+ crossServer + "]";
	}


	public int getLocalService() {
		return localService;
	}

	public boolean isCrossServer() {
		return crossServer;
	}


}

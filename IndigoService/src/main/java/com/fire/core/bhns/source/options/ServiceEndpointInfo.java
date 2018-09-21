package com.fire.core.bhns.source.options;

public class ServiceEndpointInfo
{
	private int areaId;
	private int serverId;
	private int endpointId;
	private String endpointIP;
	private int endpointPort;

	public ServiceEndpointInfo(int areaId, int serverId, int endpointId, String endpointIP,
			int endpointPort)
	{
		super();
		this.areaId = areaId;
		this.serverId = serverId;
		this.endpointId = endpointId; 
		this.endpointIP = endpointIP;
		this.endpointPort = endpointPort;
	}

	public int getEndpointId()
	{
		return endpointId;
	}

	public void setEndpointId(int endpointId)
	{
		this.endpointId = endpointId;
	}

	public String getEndpointIP()
	{
		return endpointIP;
	}

	public void setEndpointIP(String endpointIP)
	{
		this.endpointIP = endpointIP;
	}

	public int getEndpointPort()
	{
		return endpointPort;
	}

	public void setEndpointPort(int endpointPort)
	{
		this.endpointPort = endpointPort;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endpointId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceEndpointInfo other = (ServiceEndpointInfo) obj;
		if (endpointId != other.endpointId)
			return false;
		return true;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	
}

package com.fire.core.bhns;

/**
 * 本地服务需要实现的服务管理功能接口
 * 
 * @author
 * 
 */
public interface ILocalService extends IServiceData
{
	public long getServiceId();

	public byte getEndpointId();


}

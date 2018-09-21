package com.fire.core.bhns;

import com.fire.core.bhns.data.ServiceData;

/**
 * 简单服务接口
 * 
 * @author 
 * 
 */
public interface ISimpleService<P extends IServicePortal> extends IServiceable,IServiceData
{
	public long getServiceId();

	public boolean bindServiceData(ServiceData servicedata);
	
	public void init(P serviceportal,
			IServiceCreator servicecreator, long serviceId);
	
	public void release();
	
	public void initialize(int... params);
	
	public void onLogin();
	
	public void finishInit();

	public boolean isInited();
}

package com.fire.core.bhns.source.options;

import com.fire.core.bhns.IServicePortal;
import com.fire.core.bhns.ISimpleService;

@Deprecated
public class NormalServiceOption extends ClusterServiceOption
{
	public NormalServiceOption(int ptid,
			Class<? extends ISimpleService> svrclass,
			Class<? extends IServicePortal> ptoClass, String endpointIp,
			int endpointPort)
	{
		super(ptid, svrclass, ptoClass, (byte) 1);
		this.regEndpointInfo(0, (byte) 0, endpointIp, endpointPort);
	}

	@Override
	public byte configType()
	{
		return CONFIG_TYPE_NORMAL;
	}

}

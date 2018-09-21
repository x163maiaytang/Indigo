package com.fire.core.bhns;

import com.fire.core.bhns.data.ServiceData;

/**
 * 本地服务实现的基类
 * 
 * @author
 * 
 */
public abstract class AbstractSimpleService<P extends IServicePortal> implements ISimpleService<P>
{
	// 服务对应key
	protected long _serviceId;
	protected int portalId;
//	private byte _endpointId;
//	protected ServiceData _serviceData;
	private IServiceCreator _serviceCreator;
	private P _servicePortal;
	protected volatile boolean inited;
	
	
	@Override
	public void init(P serviceportal,
			IServiceCreator servicecreator, long serviceId)
	{
		this._servicePortal = serviceportal;
		this._serviceCreator = servicecreator;
		this._serviceId = serviceId;
		this.portalId = _servicePortal.getPortalId();
	}

	public boolean bindServiceData(ServiceData servicedata)
	{
		if (servicedata == null)
			return false;
//		if (this._serviceData != null)
//			_serviceCreator.resetServiceData(this, servicedata);
//		this._serviceData = servicedata;
		return true;
	}

	@Override
	public long getServiceId()
	{
		return _serviceId;
	}

	// Data**********************************************
//	public Object getData(String datatag)
//	{
//		return (_serviceData != null) ? _serviceData.getData(datatag) : null;
//	}
//
//	public void putData(String datatag, Object data)
//	{
//		if (_serviceData != null)
//			_serviceData.putData(datatag, data);
//	}
//
//	public void removeData(String datatag)
//	{
//		if (_serviceData != null)
//			_serviceData.removeData(datatag);
//	}
//
//	public void clearData()
//	{
//		if (_serviceData != null)
//			_serviceData.clearData();
//	}
//
//	public Object getData(int datagroup, String datakey)
//	{
//		return (_serviceData != null) ? _serviceData
//				.getData(datagroup, datakey) : null;
//	}
//
//	public void putData(int datagroup, String datakey, Object data)
//	{
//		if (_serviceData != null)
//			_serviceData.putData(datagroup, datakey, data);
//	}
//
//	public void removeData(int datagroup, String datakey)
//	{
//		if (_serviceData != null)
//			_serviceData.removeData(datagroup, datakey);
//	}
//
//	public void clearData(int datagroup)
//	{
//		if (_serviceData != null)
//			_serviceData.clearData(datagroup);
//	}
//
//	public void clearAllData()
//	{
//		if (_serviceData != null)
//			_serviceData.clearAllData();
//	}
//
//	public boolean isDataExist(String datatag)
//	{
//		if (_serviceData != null)
//			return _serviceData.isDataExist(datatag);
//		else
//			return false;
//	}
//
//	public boolean isDataExist(int datagroup)
//	{
//		if (_serviceData != null)
//			return _serviceData.isDataExist(datagroup);
//		else
//			return false;
//	}
//
//	public Iterator getDataIterator()
//	{
//		if (_serviceData != null)
//			return _serviceData.getDataIterator();
//		else
//			return null;
//	}
//
//	public Iterator getDataIterator(int datagroup)
//	{
//		if (_serviceData != null)
//			return _serviceData.getDataIterator(datagroup);
//		else
//			return null;
//	}

	public P getPortal()
	{
		return this._servicePortal;
	}

	@Override
	public void finishInit(){
		inited = true;
	}
	
	
	public boolean isInited() {
		return inited;
	}

	public int getPortalId() {
		return portalId;
	}
	
}

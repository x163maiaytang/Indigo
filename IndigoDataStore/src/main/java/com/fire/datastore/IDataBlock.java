package com.fire.datastore;

import java.util.Collection;
import java.util.Map;

import com.fire.gamedataobject.AbstractGameDataObject;

public interface IDataBlock<T extends AbstractGameDataObject>
{
	public boolean initialize(boolean isReload);
	
	public T getObject(int id);
	
	public T getObject(int id, boolean reloadIfAbsent);
	
	public Map<Integer, T> getObjectMap();

	public Collection<T> getObjectList();

	public T reload(int id);
	
	public void releaseObject(T gameObject);

	public boolean addObject(T gameObject);
	
	public boolean removeObject(T gameObject);
	
	public T removeObject(int id);
	
	public boolean isReloadSupported();

	Class getObjectClz();

	Class getDataClz();
}

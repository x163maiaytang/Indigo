package com.fire.gamedataobject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fire.datastore.IDataBlock;
import com.fire.excel.TemplateObject;
import com.fire.excel.TemplateService;

public abstract class AbstractGameDataObjectManager<D extends TemplateObject, T extends AbstractGameDataObject<D>>
		implements IDataBlock<T>
{
	private static final Logger logger = Logger
			.getLogger(AbstractGameDataObjectManager.class);

	protected Map<Integer, T> objectMap = new HashMap<Integer, T>();

	protected final Class<T> objectClz;
	protected final Class<D> dataClz;
	protected final boolean reloadSupported;

	public AbstractGameDataObjectManager(Class<D> dataClz,Class<T> clz, boolean reloadSupported)
	{
		if (clz == null)
			throw new IllegalArgumentException("dataObject clz is null");

		this.reloadSupported = reloadSupported;
		this.objectClz = clz;
		this.dataClz = dataClz;
	}

	public T getObject(String id)
	{
		return objectMap.get(id);
	}

	public T getObject(int id, boolean reloadIfAbsent)
	{
		T obj = this.getObject(id);
		if (obj == null && reloadIfAbsent && reloadSupported)
			obj = this.reload(id);
		return obj;
	}
	
	@Override
	public Collection<T> getObjectList() {
		return objectMap.values();
	}

	@Override
	public Map<Integer, T> getObjectMap() {
		return objectMap;
	}

	public T removeObject(int id)
	{
		return objectMap.remove(String.valueOf(id));
	}
	
	public T removeObject(String id)
	{
		return objectMap.remove(id);
	}

	public T newObject()
	{
		try
		{
			return objectClz.newInstance();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean initialize(boolean isReload)
	{
		boolean inited = initialize(TemplateService.getInstance().getAll(dataClz),isReload);
		return inited;
	}

	public boolean initialize(Map<Integer,D> gameDataMap,boolean isReload)
	{
		T gameObject = null;
		if(gameDataMap != null && gameDataMap.size() > 0)
		{
			
			Map<Integer, Integer> tempMap = new HashMap<>();
			for(Map.Entry<Integer, T> entry : objectMap.entrySet()){
				tempMap.put(entry.getKey(), entry.getKey());
			}
			
			for (D resourceData : gameDataMap.values())
			{
				
				tempMap.remove(resourceData.getId());
				if(isReload)
					this.reload(resourceData.getId());
				else
				{
					gameObject = newObject();
					if (gameObject == null)
						continue;
					
					gameObject.init(resourceData, isReload);
					objectMap.put(gameObject.getId(), gameObject);
				}
			}
			
			
			if(tempMap.size() > 0){
				
				for(Integer dataId : tempMap.values()){
					
					objectMap.remove(dataId);
				}
			}
			
			tempMap = null;
			
			return true;
		}
		return false;
	}

	public T reload(int dataId)
	{
		if(!isReloadSupported())
			return null;
		
		D data = TemplateService.getInstance().get(dataId, dataClz);
		if (data == null)
			return null;

		boolean isNew = false;
		T gameObject = objectMap.get(dataId);
		if (gameObject == null)
		{
			isNew = true;
			gameObject = newObject();
			if (gameObject == null)
				return null;
		} else
			this.beforeReload(gameObject);

		gameObject.init(data, true);
		objectMap.put(gameObject.getId(), gameObject);
		this.afterReload(gameObject, isNew);
		return gameObject;
	}
	
	public boolean addObject(T gameObject)
	{
		objectMap.put(gameObject.getId(), gameObject);
		return true;
	}

	public boolean removeObject(T gameObject)
	{
		objectMap.remove(gameObject.getObjectId());
		return true;
	}

	public void releaseObject(T gameObject)
	{
	}

	public final boolean isReloadSupported()
	{
		return reloadSupported;
	}

	public T getObject(int id)
	{
		return objectMap.get(id);
	}
	
	protected abstract void beforeReload(T gameObject);

	protected abstract void afterReload(T gameObject, boolean isNew);

	public Class getObjectClz() {
		return objectClz;
	}

	public Class getDataClz() {
		return dataClz;
	}
	
	
}

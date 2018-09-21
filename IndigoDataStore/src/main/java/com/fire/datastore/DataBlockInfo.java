package com.fire.datastore;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.fire.core.util.dependence.IDependence;
import com.fire.gamedataobject.AbstractGameDataObjectManager;

public class DataBlockInfo implements IDependence
{
	private static final Logger logger = Logger.getLogger(DataBlockInfo.class);

	private String[] dependence;
	private Class<? extends IDataBlock> implClass;
	private IDataBlock impl;
	private boolean loaded;
	private boolean valid;

	public DataBlockInfo(Class<? extends AbstractGameDataObjectManager> clz)
	{
		implClass = clz;
		DataBlockAnnotationInfo info = clz.getAnnotation(DataBlockAnnotationInfo.class);
		if(info == null)
			throw new RuntimeException("clz:"+clz.getName()+" must be definded DataBlockAnnotationInfo");
		
		if(info.depandences() != null)
		{
			dependence = new String[info.depandences().length];
			
			int i = 0;
			for(Class c:info.depandences())
			{
				dependence[i] = c.getSimpleName();
				i++;
			}
		}
		this.loaded = false;
		this.valid = true;
	}

	@Override
	public String[] getDependence()
	{
		return dependence;
	}

	public Class<? extends IDataBlock> getImplClass()
	{
		return implClass;
	}

	public boolean isLoaded()
	{
		return this.loaded;
	}

	public IDataBlock createImplementInstance()
	{
		if (!this.loaded)
		{
			try
			{
				this.impl = null;
				this.loaded = true;
				this.valid = false;

				if (!IDataBlock.class.isAssignableFrom(implClass))
				{
					return null;
				}
				Constructor<?> c = implClass.getDeclaredConstructor(null);
				c.setAccessible(true);
				this.impl = (IDataBlock) c.newInstance();
				this.valid = true;
			} catch (Exception e)
			{
				this.impl = null;
				e.printStackTrace();
			}
		}
		if (this.loaded && !this.valid)
		{
			logger.error("datablock " + implClass.getSimpleName() + " is invalid");
		}
		return this.impl;
	}

	public boolean isValid()
	{
		return valid;
	}
}

package com.fire.gamedataobject;

import org.apache.log4j.Logger;

import com.fire.excel.TemplateObject;

public abstract class AbstractGameDataObject<D extends TemplateObject>
{
	private static final Logger logger = Logger
			.getLogger(AbstractGameDataObject.class);
	/**
	 * 游戏对象Id
	 */
	protected int id;

	/**
	 * 游戏对象名称
	 */
	protected String name;
	/**
	 * 开放标识
	 */
	protected boolean openFlag;
	/**
	 * 初始化标识
	 */
	protected boolean initialized;
	/**
	 * 配置数据
	 */
	protected D configData;
	
	public AbstractGameDataObject()
	{
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ": id=" + id + ", name=" + name;
	}

	public boolean init(D configData, boolean reload)
	{
		this.configData = configData;
		this.id = configData.getId();
		return this.initialize(reload);
	}
	
	public abstract boolean initialize(boolean reload);

	public void release()
	{
		name = null;
		initialized = false;
	}

	public int getObjectId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isInitialized()
	{
		return initialized;
	}

	public boolean getOpenFlag()
	{
		return openFlag;
	}

	public int getId()
	{
		return id;
	}
	
	/**
	 * 因为reload之后对象会变成新的对象，所以不要引用或者保存下来这个对象
	 * @return
	 */
	public D getConfigData()
	{
		return configData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((configData == null) ? 0 : configData.hashCode());
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
		AbstractGameDataObject other = (AbstractGameDataObject) obj;
		if (configData == null) {
			if (other.configData != null)
				return false;
		} else if (!configData.equals(other.configData))
			return false;
		return true;
	}
	
	
}
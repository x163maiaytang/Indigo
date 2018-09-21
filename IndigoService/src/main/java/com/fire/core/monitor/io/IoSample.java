package com.fire.core.monitor.io;

import java.io.Serializable;

public class IoSample implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int commandId;
	private volatile int upCount;
	private volatile long totalUpIo;
	private int maxUpIo;
	private int minUpIo;
	private int avaUpIo;

	private volatile int downCount;
	private volatile long totalDownIo;
	private int maxDownIo;
	private int minDownIo;
	private int avaDownIo;

	public void addUp(boolean isUp, int size)
	{
		if (isUp)
		{
			totalUpIo += size;
			if (this.upCount <= 0)
			{
				this.maxUpIo = size;
				this.minUpIo = size;
			} else
			{
				if (size > maxUpIo)
					maxUpIo = size;

				if (size < minUpIo)
					minUpIo = size;
			}
			upCount++;
		} else
		{
			totalDownIo += size;
			if (this.downCount <= 0)
			{
				this.maxDownIo = size;
				this.minDownIo = size;
			} else
			{
				if (size > maxDownIo)
					maxDownIo = size;

				if (size < minDownIo)
					minDownIo = size;
			}
			downCount++;
		}
	}

	/**
	 * @return the avaCost
	 */
	public long getAvaUpIo()
	{
		if (this.upCount > 0)
			this.avaUpIo = (int) (totalUpIo / this.upCount);
		return avaUpIo;
	}

	/**
	 * @return the avaCost
	 */
	public long getAvaDownIo()
	{
		if (this.downCount > 0)
			this.avaDownIo = (int) (totalDownIo / this.downCount);
		return avaDownIo;
	}

	/**
	 * @return the count
	 */
	public int getUpCount()
	{
		return upCount;
	}

	/**
	 * @return the count
	 */
	public int getDownCount()
	{
		return upCount;
	}

	public int getCommandId()
	{
		return commandId;
	}

	public void setCommandId(int commandId)
	{
		this.commandId = commandId;
	}

	public long getTotalUpIo()
	{
		return totalUpIo;
	}

	public void setTotalUpIo(long totalUpIo)
	{
		this.totalUpIo = totalUpIo;
	}

	public int getMaxUpIo()
	{
		return maxUpIo;
	}

	public void setMaxUpIo(int maxUpIo)
	{
		this.maxUpIo = maxUpIo;
	}

	public int getMinUpIo()
	{
		return minUpIo;
	}

	public void setMinUpIo(int minUpIo)
	{
		this.minUpIo = minUpIo;
	}

	public long getTotalDownIo()
	{
		return totalDownIo;
	}

	public void setTotalDownIo(long totalDownIo)
	{
		this.totalDownIo = totalDownIo;
	}

	public int getMaxDownIo()
	{
		return maxDownIo;
	}

	public void setMaxDownIo(int maxDownIo)
	{
		this.maxDownIo = maxDownIo;
	}

	public int getMinDownIo()
	{
		return minDownIo;
	}

	public void setMinDownIo(int minDownIo)
	{
		this.minDownIo = minDownIo;
	}

	public void setUpCount(int upCount)
	{
		this.upCount = upCount;
	}

	public void setAvaUpIo(int avaUpIo)
	{
		this.avaUpIo = avaUpIo;
	}

	public void setDownCount(int downCount)
	{
		this.downCount = downCount;
	}

	public void setAvaDownIo(int avaDownIo)
	{
		this.avaDownIo = avaDownIo;
	}

	@Override
	public String toString()
	{
		return "IoSample [commandId=" + commandId + ", upCount=" + upCount
				+ ", totalUpIo=" + totalUpIo + ", maxUpIo=" + maxUpIo
				+ ", minUpIo=" + minUpIo + ", avaUpIo=" + avaUpIo
				+ ", downCount=" + downCount + ", totalDownIo=" + totalDownIo
				+ ", maxDownIo=" + maxDownIo + ", minDownIo=" + minDownIo
				+ ", avaDownIo=" + avaDownIo + "]";
	}

	public static String csvTitle()
	{
		return "commandId,upCount,totalUpIo,maxUpIo,minUpIo,avaUpIo,downCount,totalDownIo,maxDownIo,minDownIo,avaDownIo";
	}

	public String csv()
	{
		return commandId + "," + upCount + "," + totalUpIo + "," + maxUpIo
				+ "," + minUpIo + "," + avaUpIo + "," + downCount + ","
				+ totalDownIo + "," + maxDownIo + "," + minDownIo + ","
				+ avaDownIo;
	}
}

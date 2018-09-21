package com.fire.core.monitor.command;

import java.io.Serializable;
import java.lang.reflect.Method;


public class CommandSample implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int commandId = 0;
	/**
	 * 采样次数
	 */
	private volatile int count;

	/**
	 * 采样点时间
	 */
	private long curTime = System.currentTimeMillis();

	/**
	 * 样本原子执行总时间
	 */
	private long totalCost = 0;

	/**
	 * 采样最大执行时间
	 */
	private long maxCost = 0;

	/**
	 * 采样最小执行时间
	 */
	private long minCost = 0;
	/**
	 * 平均时间
	 */
	private long avaCost = 0;

	public void addUp(ClipInfo info)
	{
		this.addUpCost(info);
		this.count++;
	}

	private void addUpCost(ClipInfo info)
	{
		long cost = info.getProcessCost();
		totalCost += cost;

		if (this.count <= 0)
		{
			maxCost = cost;
			minCost = cost;
		} else
		{
			if (cost > maxCost)
				maxCost = cost;

			if (cost < minCost)
				minCost = cost;
		}
	}

	/**
	 * @return the avaCost
	 */
	public long getAvaCost()
	{
		if (this.count > 0)
			avaCost = totalCost / count;
		return avaCost;
	}

	/**
	 * @return the count
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count)
	{
		this.count = count;
	}

	/**
	 * @return the curTime
	 */
	public long getCurTime()
	{
		return curTime;
	}

	/**
	 * @param curTime
	 *            the curTime to set
	 */
	public void setCurTime(long curTime)
	{
		this.curTime = curTime;
	}

	public int getCommandId()
	{
		return commandId;
	}

	public void setCommandId(int commandId)
	{
		this.commandId = commandId;
	}

	/**
	 * @return the totalCost
	 */
	public long getTotalCost()
	{
		return totalCost;
	}

	/**
	 * @return the maxCost
	 */
	public long getMaxCost()
	{
		return maxCost;
	}

	/**
	 * @return the minCost
	 */
	public long getMinCost()
	{
		return minCost;
	}

	public void setTotalCost(long totalCost)
	{
		this.totalCost = totalCost;
	}

	public void setMaxCost(long maxCost)
	{
		this.maxCost = maxCost;
	}

	public void setMinCost(long minCost)
	{
		this.minCost = minCost;
	}

	public void setAvaCost(long avaCost)
	{
		this.avaCost = avaCost;
	}

	@Override
	public String toString()
	{
		return "Sample [commandId=" + commandId + ", count=" + count
				+ ", curTime=" + curTime + ", totalCost=" + totalCost
				+ ", maxCost=" + maxCost + ", minCost=" + minCost
				+ ", avaCost=" + avaCost + "]";
	}

	public static String csvTitle()
	{
		return "commandId,count,curTime," + "totalCost,maxCost,"
				+ "minCost,avaCost";
	}

	public String csv()
	{
		return commandId + "," + count + "," + curTime + "," + totalCost + ","
				+ maxCost + "," + minCost + "," + getAvaCost();
	}

	public static void main(String[] args)
	{
		for (Method f : CommandSample.class.getDeclaredMethods())
		{
			if (f.getName().startsWith("get"))
				System.out.println(f.getName());
		}
	}
}

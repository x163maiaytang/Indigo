package com.fire.core.monitor.command;


public class ClipInfo
{
	/**
	 * 指令
	 */
	private int commandId = 0;
	
	/**
	 * 进入方法的开始时间
	 */
	private long startTime = System.currentTimeMillis();
	
	/**
	 * 方法运行的完成时间
	 */
	private long endTime;
	
	
	public long getProcessCost()
	{
		return endTime - startTime;
	}
	
	/**
	 * @return the startTime
	 */
	public long getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime()
	{
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	public int getCommandId()
	{
		return commandId;
	}

	public void setCommandId(int commandId)
	{
		this.commandId = commandId;
	}
}

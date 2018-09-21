package com.fire.core.monitor.io;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import game.net.monitor.IMonitor;

/**
 * 
 * @version
 */
public class IoMonitor implements IMonitor
{

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger("Monitor");

	/**
	 * 开关
	 */
	private AtomicBoolean isOpen = new AtomicBoolean(false);
	

	/**
	 * 开启采集
	 * 
	 * @Title: beginSamlping
	 * @Description:
	 * @param
	 * @return void
	 * @throws
	 */
	public void beginSamlping()
	{
		int tmp = 5000;
		while (tmp > 0)
		{
			try
			{
				Thread.sleep(1);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			tmp--;
		}
		isOpen.set(true);
	}

	/**
	 * 停止采集 开始回收
	 * 
	 * @Title: stopSamlping
	 * @Description:
	 * @param
	 * @return void
	 * @throws
	 */
	public void stopSamlping()
	{
		isOpen.set(false);
		
		IoSampleManager.getInstance().putOutInfo();
		IoSampleManager.getInstance().release();
	}

	/**
	 * 发采样切片
	 * 
	 * @Title: postSimpleInfo
	 * @Description:
	 * @param
	 * @param markWord
	 * @param
	 * @param sampletype
	 * @param
	 * @param timecost
	 * @param
	 * @param extdetail
	 * @return void
	 * @throws
	 */
	public void recordSimpleInfo(int commandId,boolean isUp,int size)
	{
		if (!isOpen.get())
			return;

		IoSampleManager.getInstance().analy(commandId, isUp, size);
	}

	private final static IoMonitor instance = new IoMonitor();
	
	public final static IoMonitor getInstance()
	{
		return instance;
	}
}

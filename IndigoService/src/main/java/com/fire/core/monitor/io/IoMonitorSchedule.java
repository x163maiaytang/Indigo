package com.fire.core.monitor.io;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.fire.constant.DateTimeConstants;

public class IoMonitorSchedule implements Runnable
{

	private static final Logger logger = Logger.getLogger("IoMonitorSchedule");

	private boolean runing;
	private long sampletime;
	/**
	 * 是否还在运行状态，用于刚开启又关闭又开启的阻止操作
	 */
	private static AtomicBoolean isRuning = new AtomicBoolean(false);

	public IoMonitorSchedule()
	{
		runing = false;
	}

	public void forceStop()
	{
		runing = false;
	}

	/**
	 * 开启采样计划
	 */
	public boolean beginSampleSchedule(long sampletime)
	{
		this.sampletime = sampletime;
		
		new Thread(this).start();
		logger.error("command sample start success...");
		return true;
	}

	@Override
	public void run()
	{
		runing = true;
		logger.error("IoMonitorSchedule start...");

//		while (runing)
//		{
			isRuning.set(true);
			try
			{
				IoMonitor.getInstance().beginSamlping();
				logger.error("beginSamlping..." + DateTimeConstants.getTimeString(System.currentTimeMillis()));
				long tmpSampleExpire = System.currentTimeMillis() + sampletime;
				do
				{
					try
					{
						Thread.sleep(1000l);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
//					logger.error("check Samlping...");
				} while (System.currentTimeMillis() < tmpSampleExpire);
				logger.error("stop samlping and start post data..." + DateTimeConstants.getTimeString(System.currentTimeMillis()));
				IoMonitor.getInstance().stopSamlping();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			isRuning.set(false);
//		}
		
		logger.error("EndSampling....");
	}

	public boolean isRuning()
	{
		return runing;
	}

	// *************************************************
	private static IoMonitorSchedule instance;

	public synchronized static IoMonitorSchedule getInstance()
	{
		if (instance == null)
		{
			instance = new IoMonitorSchedule();
		}
		return instance;
	}

	public static boolean startSample(long sampletime)
	{
		IoMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
		{
			if(isRuning.get())
			{
				logger.error("上一次强制关闭操作还没有执行，请稍候执行");
				return false;
			}
			return tmpMonitorSchedule.beginSampleSchedule(sampletime);
		}
		else
		{
			logger.error("init MonitorSchedule fail...");
			return false;
		}
	}

	public static void forceStopSampleSchedule()
	{
		IoMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
		{
			logger.error("force stop command monitor...");
			tmpMonitorSchedule.forceStop();
		}
	}

	public static boolean hasRun()
	{
		IoMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
			return tmpMonitorSchedule.isRuning();
		else
		{
			logger.error("init MonitorSchedule fail...");
			return false;
		}
	}
}

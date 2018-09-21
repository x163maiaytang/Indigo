package com.fire.core.monitor.command;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;


public class CommandMonitorSchedule implements Runnable
{

	private static final Logger logger = Logger.getLogger(CommandMonitorSchedule.class);

	private boolean runing;
	private long sleepInterval;
	private long sampleTime;

	private int sampleCount;
	private int sampleLeftCount;
	
	/**
	 * 是否还在运行状态，用于刚开启又关闭又开启的阻止操作
	 */
	private static AtomicBoolean isRuning = new AtomicBoolean(false);

	public CommandMonitorSchedule()
	{
		runing = false;
	}

	public void forceStop()
	{
		runing = false;
		sampleCount = 0;
		sampleLeftCount = 0;
		CommandSampleManager.getInstance().release();
	}

	public String sampleProcessInfo()
	{
		return "sampleCount=" + sampleCount + ",sampleLeftCount=" + sampleLeftCount;
	}

	/**
	 * 开启采样计划
	 */
	public boolean beginSampleSchedule(long sampletime, long sleepinterval, int samplecount)
	{
		if (sleepinterval < 10000l || sampletime < 20000l || samplecount < 1)
		{
			logger.error("采样指令非法");
			return false;
		}
		CommandSampleManager.getInstance().release();

		if (runing)
		{
			logger.error("上次采样还在进行中，请等待采样结束或直接停止采样");
			return false;
		}

		this.sleepInterval = sleepinterval;
		this.sampleTime = sampletime;
		sampleCount = samplecount;
		new Thread(this).start();
		logger.error("command sample start success...");
		return true;
	}

	@Override
	public void run()
	{
		runing = true;
		sampleLeftCount = sampleCount;
		logger.error("MonitorSchedule start...");

		while (runing && sampleLeftCount > 0)
		{
			isRuning.set(true);
			if (runing)
			{
				try
				{
					Thread.sleep(sleepInterval);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				CommandMonitor.beginSamlping();
				logger.error("beginSamlping...");
				long tmpSampleExpire = System.currentTimeMillis() + this.sampleTime;
				do
				{
					try
					{
						Thread.sleep(5000l);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					logger.error("check Samlping...");
				} while (System.currentTimeMillis() < tmpSampleExpire);
				logger.error("stop samlping and start post data...");
				CommandMonitor.stopSamlping();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(sampleLeftCount>0)
				{
					sampleLeftCount--;
				}
			}
			isRuning.set(false);
		}
		
		logger.error("EndSampling....");
	}

	public long getSleepInterval()
	{
		return sleepInterval;
	}

	public boolean isRuning()
	{
		return runing;
	}

	// *************************************************
	private static CommandMonitorSchedule instance;

	public synchronized static CommandMonitorSchedule getInstance()
	{
		if (instance == null)
		{
			instance = new CommandMonitorSchedule();
		}
		return instance;
	}

	public static boolean startSample(long sampletime, long sleepinterval, int samplecount)
	{
		CommandMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
		{
			if(isRuning.get())
			{
				logger.error("上一次强制关闭操作还没有执行，请稍候执行");
				return false;
			}
			return tmpMonitorSchedule.beginSampleSchedule(sampletime, sleepinterval, samplecount);
		}
		else
		{
			logger.error("init MonitorSchedule fail...");
			return false;
		}
	}

	public static void forceStopSampleSchedule()
	{
		CommandMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
		{
			logger.error("force stop command monitor...");
			tmpMonitorSchedule.forceStop();
		}
	}

	public static String getSampleProcessInfo()
	{
		CommandMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
		{
			return tmpMonitorSchedule.sampleProcessInfo();
		}
		else
		{
			logger.error("init MonitorSchedule fail...");
			return null;
		}
	}

	public static long getIntevalTime()
	{
		CommandMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
			return tmpMonitorSchedule.getSleepInterval();
		else
		{
			logger.error("init MonitorSchedule fail...");
			return 0;
		}
	}

	public static boolean hasRun()
	{
		CommandMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
			return tmpMonitorSchedule.isRuning();
		else
		{
			logger.error("init MonitorSchedule fail...");
			return false;
		}
	}
	
	public static int getCurrentSampleCount()
	{
		CommandMonitorSchedule tmpMonitorSchedule = getInstance();
		if (tmpMonitorSchedule != null)
			return tmpMonitorSchedule.sampleCount - tmpMonitorSchedule.sampleLeftCount;
		return 0;
	}

}

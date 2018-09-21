package com.fire.core.monitor.command;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;


/**
 * 
 * @version
 */
public class CommandMonitor
{

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger("Monitor");

	/**
	 * 开关
	 */
	private static AtomicBoolean isOpen = new AtomicBoolean(false);
	
	/**
	 * 线程对应正在执行信息表
	 */
	private static ConcurrentHashMap<Long, ClipInfo> threadMethodInfoMap = new ConcurrentHashMap<Long, ClipInfo>();

	/**
	 * 开启采集
	 * 
	 * @Title: beginSamlping
	 * @Description:
	 * @param
	 * @return void
	 * @throws
	 */
	public static void beginSamlping()
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
	public static void stopSamlping()
	{
		isOpen.set(false);
		
		CommandSampleManager.getInstance().putOutInfo(CommandMonitorSchedule.getCurrentSampleCount());
		CommandSampleManager.getInstance().release();
	}

	/**
	 * 标识线程对应处理指令
	 * 
	 * @Title: mark
	 * @Description:
	 * @param
	 * @param commandID
	 * @return void
	 * @throws
	 */
	public static void mark(int commandId)
	{
		if (!isOpen.get())
		{
			return;
		}
		long id = Thread.currentThread().getId();
		ClipInfo info = new ClipInfo();
		info.setCommandId(commandId);
		threadMethodInfoMap.put(id, info);
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
	public static void recordSimpleInfo()
	{
		if (!isOpen.get())
		{
			return;
		}		
		long id = Thread.currentThread().getId();
		if(threadMethodInfoMap.containsKey(id)){
			ClipInfo info = threadMethodInfoMap.get(id);
			if(info!=null){
				info.setEndTime(new Date().getTime());
				CommandSampleManager.getInstance().analy(info);
			}
		}
	}

}

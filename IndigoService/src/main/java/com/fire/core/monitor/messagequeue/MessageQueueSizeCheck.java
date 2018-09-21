package com.fire.core.monitor.messagequeue;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import game.net.executor.OrderedMessageExecutor;

/**
 * 统计消息队列未处理的消息
 * 
 */
public class MessageQueueSizeCheck implements Runnable
{
	
	private static final Logger logger = Logger.getLogger("MessageQueueSizeCheck");
	
	private AtomicBoolean isRunning = new AtomicBoolean(false);
	private long intervalTime = 30 * 1000;
	private int sampleCount = 30;
	private int curFinishedCount = 0;
	private OrderedMessageExecutor messageCached;
	private Thread curThread = null;
	
	@Override
	public void run()
	{
		while(isRunning.get() && curFinishedCount < sampleCount)
		{
			printMessageQueue();
			curFinishedCount ++;
			try
			{
				Thread.sleep(intervalTime);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		isRunning.set(false);
	}
	
	public boolean start(long intervaltime, int samplecount,OrderedMessageExecutor messageCached)
	{
		if(intervaltime < 10 || sampleCount < 1 || messageCached == null)
		{
			logger.error("采样时间间隔设置太小，最少10秒以上");
			return false;
		}
		if(this.isRunning.get())
			return false;
		this.isRunning.set(true);
		this.intervalTime = intervaltime * 1000;
		this.sampleCount = samplecount;
		this.messageCached = messageCached;
		
		curThread = new Thread(this);
		curThread.start();
		
		return true;
	}
	
	public void forceStop()
	{
		this.isRunning.set(false);
		curThread = null;
		curFinishedCount = 0;
	}
	
	public boolean isRunning()
	{
		return this.isRunning.get();
	}
	
	//**********************************************
	private static MessageQueueSizeCheck instance = new MessageQueueSizeCheck();
	
	public static MessageQueueSizeCheck getInstance()
	{
		return instance;
	}
	
	public void printMessageQueue()
	{
		logger.error("printMessageQueue is running,left count = "+ (sampleCount - curFinishedCount));
		for(Entry<Integer, Integer> e:messageCached.getWaitQueueSize().entrySet())
			logger.error(e.getValue()+":"+e.getKey());
		
		logger.error("printMessageQueue finished");
	}
	
	public static void main(String[] args)
	{
//		MessageQueueSizeCheck.getInstance().start(11000, 3);
	}
}

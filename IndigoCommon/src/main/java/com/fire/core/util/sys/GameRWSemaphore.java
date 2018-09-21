package com.fire.core.util.sys;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * 读写信号量，用于系统短时间读暂停为了写数据。
 */
public class GameRWSemaphore
{
	private static final Logger logger = Logger
			.getLogger(GameRWSemaphore.class);
	
	private static final long STOP_READ_MAX_TIMEOUT = 5 * 1000l;
			
	private long waitingReadTime = 0L;
	
	private AtomicBoolean waitingWrite = new AtomicBoolean(false);
	private AtomicInteger readRefCount = new AtomicInteger(0);

	
	public void acquire(boolean isWrite)
	{
		if(isWrite)
		{
			if(waitingWrite.get())
			{
				logger.error("",new RuntimeException("write unfinished"));
				return;
			}
			
			waitingWrite.set(true);
			waitingReadTime = System.currentTimeMillis() + STOP_READ_MAX_TIMEOUT;
			
			while(readRefCount.get() > 0)
			{
				try
				{
					Thread.sleep(1);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			while(waitingWrite.get())
			{
				try
				{
					Thread.sleep(1);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
				if(waitingReadTime > 0 && System.currentTimeMillis() > waitingReadTime)
				{
					this.release(true);
					break;
				}
			}
			readRefCount.incrementAndGet();
		}
	}
	
	public void release(boolean isWrite)
	{
		if(isWrite)
		{
			if(!waitingWrite.get())
				logger.warn("unwrite state");
			
			waitingWrite.set(false);
			waitingReadTime = 0l;	
		}
		else
		{
			readRefCount.decrementAndGet();
		}
	}
}

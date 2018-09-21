package com.fire.util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * 读写信号量，用于系统短时间读暂停为了写数据。
 */
public class RWSemaphore
{
	private static final Logger logger = Logger
			.getLogger(RWSemaphore.class);
	
	private static final long WAIT_WRITE_MAX_TIMEOUT = 5 * 1000l;
	private static final long WAIT_READ_MAX_TIMEOUT = 5 * 1000l;
			
	private long waitingReadTime = 0L;
	private long waitingWriteTime = 0L;
	
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
			waitingWriteTime = System.currentTimeMillis() + WAIT_READ_MAX_TIMEOUT;
			
			while(readRefCount.get() > 0)
			{
				try
				{
					Thread.sleep(1);
					if(waitingWriteTime > 0 && System.currentTimeMillis() > waitingWriteTime)
					{
						break;
					}
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			waitingWriteTime = 0L;
			waitingReadTime = System.currentTimeMillis() + WAIT_READ_MAX_TIMEOUT;
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

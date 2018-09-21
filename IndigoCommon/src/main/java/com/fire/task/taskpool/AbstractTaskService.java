package com.fire.task.taskpool;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.fire.task.ITaskIntereptor;
import com.fire.task.Task;
import com.fire.task.TaskCallbackHandler;
import com.fire.task.TaskRunnable;

/**
 * 抽象的计划任务服务器
 */
public abstract class AbstractTaskService
{
	private static final Logger logger = Logger.getLogger(AbstractTaskService.class);
	
	/**
	 * 每秒帧数
	 */
	public static final int FRAMES_PER_SECOND = 10;
	/**
	 * 场景帧调度任务执行时间间隔，单位为毫秒
	 */
	public static final int MILLISECOND_PER_FRAME = 100;
	
	/**
	 * 每秒
	 */
	public static final int MILLISECOND_PER_SCONDED = 1000;
	
	/**
	 * 当前服务包含的所有任务回调的列表
	 */
	protected List<TaskCallbackHandler> handlerList = new CopyOnWriteArrayList<TaskCallbackHandler>();

	protected ITaskIntereptor intereptor;
	
	public AbstractTaskService()
	{
	}
	
	public void init(ITaskIntereptor intereptor)
	{
		this.intereptor = intereptor;		
	}
	
	/**
	 * 添加计划任务
	 * 
	 * @param task
	 * @return
	 */
	public TaskCallbackHandler scheduleAtFixedRate(Task task)
	{
		ScheduledFuture<?> future = getExecutor().scheduleAtFixedRate(
				new TaskRunnable(task,intereptor), getDelay(), getPeriod(),
				TimeUnit.MILLISECONDS);
		TaskCallbackHandler handler = new TaskCallbackHandler(task, future,
				this);
		handlerList.add(handler);
		return handler;
	}

	/**
	 * 添加计划任务
	 * 
	 * @param task
	 * @param delay
	 * @return
	 */
	public TaskCallbackHandler scheduleAtFixedRate(Task task, long delay)
	{
		ScheduledFuture<?> future = getExecutor().scheduleAtFixedRate(
				new TaskRunnable(task,intereptor), delay, getPeriod(),
				TimeUnit.MILLISECONDS);
		TaskCallbackHandler handler = new TaskCallbackHandler(task, future,
				this);
		handlerList.add(handler);
		return handler;
	}

	/**
	 * 添加计划任务
	 * 
	 * @param task
	 * @param delay
	 * @param period
	 * @return
	 */
	public TaskCallbackHandler scheduleAtFixedRate(Task task, long delay,
			long period)
	{
		ScheduledFuture<?> future = getExecutor().scheduleAtFixedRate(
				new TaskRunnable(task,intereptor), delay, period, TimeUnit.MILLISECONDS);
		TaskCallbackHandler handler = new TaskCallbackHandler(task, future,
				this);
		handlerList.add(handler);
		return handler;
	}

	/**
	 * 一次性的计划任务
	 * 
	 * @param task
	 * @param delay
	 * @param period
	 * @return
	 */
	public TaskCallbackHandler scheduleOnceTask(Task task, long delay)
	{
		if(delay < 0){
			delay = 0;
		}
		
		ScheduledFuture<?> future = getExecutor().schedule(
				new TaskRunnable(task, intereptor), delay, TimeUnit.MILLISECONDS);
		
		if(delay > 1000){
			
			TaskCallbackHandler handler = new TaskCallbackHandler(task, future,
					this);
			handlerList.add(handler);
			return handler;
		}
		
		return null;
	}

	/**
	 * 移除指定的Handler
	 * 
	 * @param handler
	 */
	public void removeHandler(TaskCallbackHandler handler)
	{
		handlerList.remove(handler);
	}

	/**
	 * 统一运行计划任务
	 */
	public void info()
	{
//		if (logger.isInfoEnabled())
//			logger.info("【" + getName() + "】执行线程数量,Core="
//					+ getExecutor().getCorePoolSize() + ",active="
//					+ getExecutor().getActiveCount() + ",complateNum="
//					+ getExecutor().getCompletedTaskCount() + ",taskNum="
//					+ handlerList.size());
	}

	/**
	 * 执行停止的方法
	 */
	public void shutdown()
	{
		for (TaskCallbackHandler handler : handlerList)
		{
			handler.cancel();
		}
		logger.error("停止任务服务器");
	}

	/**
	 * 获取系统具体的执行器
	 * 
	 * @return ScheduledExecutorService
	 * 
	 */
	protected abstract ScheduledExecutorService getExecutor();

	/**
	 * 获取执行周期
	 * 
	 * @return
	 */
	protected abstract long getPeriod();

	/**
	 * 获取默认延时周期
	 * 
	 * @return
	 */
	protected abstract long getDelay();

	/**
	 * 返回当前服务的名称
	 * 
	 * @return
	 */
	protected abstract String getName();
	
	
	
}

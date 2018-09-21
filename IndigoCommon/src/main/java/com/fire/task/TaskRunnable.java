package com.fire.task;

import org.apache.log4j.Logger;

/**
 */
public class TaskRunnable implements Runnable
{
	private static final Logger logger = Logger.getLogger(TaskRunnable.class);
	/**
	 * 任务对象
	 */
	private Task task;
	private ITaskIntereptor taskIntereptor;

	public TaskRunnable(Task t, ITaskIntereptor intereptor)
	{
		this.task = t;
		this.taskIntereptor = intereptor;
	}

	@Override
	public void run()
	{
		try
		{
			if(taskIntereptor != null)
				taskIntereptor.befor();
			this.task.run();
		} catch (Exception e)
		{
			logger.error("执行计划任务的时候发生异常", e);
		} catch(Error e){
			logger.error("执行计划任务的时候发生异常", e);
			
		}finally
		{
			if(taskIntereptor != null)
				taskIntereptor.after();
		}
	}
}

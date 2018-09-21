package com.fire.task;

/**
 *在线程池执行任务之前和之后做操作 
 */
public interface ITaskIntereptor
{
	public void befor();
	
	public void after();
}

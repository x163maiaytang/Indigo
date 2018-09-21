package com.fire.task.taskpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.fire.task.DefaultThreadFactory;

/**
 * 
 */
public class ServiceLogicTask extends AbstractTaskService {

	/**
	 * 具体执行的线程池对象
	 */
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(ServiceLogicTask.class.getSimpleName()));
	/**
	 * 第一次执行的延迟时间
	 */
	private static long initialDelay = 20001;

	/**
	 * 单例对象
	 */
	private static ServiceLogicTask instance = new ServiceLogicTask();

	private ServiceLogicTask() {
	}

	public static ServiceLogicTask getInstance() {
		return instance;
	}

	@Override
	protected long getDelay() {
		return initialDelay;
	}

	@Override
	protected ScheduledExecutorService getExecutor() {
		return scheduler;
	}

	@Override
	protected String getName() {
		return "通用任务服务器";
	}

	@Override
	protected long getPeriod() {
		return MILLISECOND_PER_SCONDED;
	}

}

package com.fire.task.taskpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.fire.task.DefaultThreadFactory;

/**
 * 
 */
public class CommonTask extends AbstractTaskService {

	/**
	 * 具体执行的线程池对象
	 */
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(CommonTask.class.getSimpleName()));
	/**
	 * 第一次执行的延迟时间
	 */
	private static long initialDelay = 20001;

	/**
	 * 单例对象
	 */
	private static CommonTask instance = new CommonTask();

	private CommonTask() {
	}

	public static CommonTask getInstance() {
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

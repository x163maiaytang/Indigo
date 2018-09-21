package game.net.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.fire.task.DefaultThreadFactory;

import game.net.task.IOrderMessageTask;
import game.net.task.TaskQueue;

class MessageThreadPoolExector extends ThreadPoolExecutor
{

	public MessageThreadPoolExector(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, String threadGroupName)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				new DefaultThreadFactory(threadGroupName));
		
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t)
	{
		IOrderMessageTask task = (IOrderMessageTask) r;
		TaskQueue<IOrderMessageTask> taskQueue = task.getParentQueue();
		if (taskQueue == null)
		{
			return;
		}
		synchronized (taskQueue)
		{
			if (taskQueue.getSize() == 0)
			{
				taskQueue.setRunning(false);
				return;
			}
			
			IOrderMessageTask next = taskQueue.poll();
			if (next != null)
			{
				System.out.println("cur : " + task.getClass().getName() + " next : " + next.getClass().getName());
				execute(next);
			}
		}
	}
}

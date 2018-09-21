package game.net.executor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import game.net.task.IOrderMessageTask;
import game.net.task.TaskQueue;

public class OrderedMessageExecutor
{

	private MessageThreadPoolExector executor;
	private ConcurrentMap<Object, TaskQueue<IOrderMessageTask>> tasks = new ConcurrentHashMap<Object, TaskQueue<IOrderMessageTask>>();

	public OrderedMessageExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, String threadGroupName)
	{

		executor = new MessageThreadPoolExector(corePoolSize, maximumPoolSize,
				keepAliveTime, unit, new LinkedBlockingDeque<Runnable>(),
				threadGroupName);
	}

	public void execute(IOrderMessageTask task)
	{

		Object key = task.getKey();
		if (key == null)
		{
			executor.execute(task);
			return;
		}

		TaskQueue<IOrderMessageTask> taskQueue = tasks.get(key);
		if (taskQueue == null)
		{
			tasks.put(key, 	taskQueue = new TaskQueue<IOrderMessageTask>(key));
//			if (taskQueue == null)
//			{
//				taskQueue = tasks.get(key);
//			}
		}

		synchronized (taskQueue)
		{
			task.setParentQueue(taskQueue);
			if (!taskQueue.isRunning())
			{
				taskQueue.setRunning(true);
				executor.execute(task);
			} else if (task.isUrgency()){
				
				taskQueue.addFirst(task);
			}
			else{
				taskQueue.offer(task);
			}
		}
	}

	public TreeMap<Integer, Integer> getWaitQueueSize()
	{
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (TaskQueue<IOrderMessageTask> tq : tasks.values())
		{
			for (IOrderMessageTask task : tq.getQueue())
			{
				Integer size = map.get((Integer)task.getKey());
				if (size == null)
					size = 0;
				size++;
				map.put((Integer) task.getKey(), size);
			}
		}

		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(
				new Comparator<Integer>()
				{
					public int compare(Integer o1, Integer o2)
					{
						return o1 < o2 ? -1 : 1;
					};
				});

		for (Entry<Integer, Integer> e : map.entrySet())
			sortedMap.put(e.getValue(), e.getKey());

		return sortedMap;
	}

	// TODO:
	public void clean()
	{
	}


	public void release() {
		tasks.clear();
		tasks = null;
		
		executor.shutdownNow();
		
		executor = null;
	}

	public void remove(Object key) {
		TaskQueue<IOrderMessageTask> removeQueue = tasks.remove(key);
		if(removeQueue != null){
			if(!removeQueue.isRunning()){
				
				LinkedBlockingDeque<IOrderMessageTask> queue = removeQueue.getQueue();
				for(IOrderMessageTask m : queue){
					executor.execute(m);
				}
			}
		}
	}

}

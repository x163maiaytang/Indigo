package game.net.task;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskQueue<E>
{
	Object key;
	LinkedBlockingDeque<E> queue = new LinkedBlockingDeque<E>();
	AtomicBoolean running = new AtomicBoolean();
	
	public TaskQueue(Object key)
	{
		this.key = key;
	}

	/**
	 * if the queue is empty before offer, return true, otherwise true
	 * 
	 * @param e
	 * @return
	 */
	public boolean offer(E e)
	{
		return queue.offer(e);
	}

	public E poll()
	{
		return queue.poll();
	}

	public boolean addFirst(E e)
	{
		queue.offerFirst(e);
		return true;
	}

	public int getSize()
	{
		return queue.size();
	}

	public boolean isRunning()
	{
		return running.get();
	}

	public void setRunning(boolean value)
	{
		running.set(value);
	}
	
	public LinkedBlockingDeque<E> getQueue()
	{
		return queue;
	}
}

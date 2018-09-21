package game.net.task;

public interface IOrderMessageTask extends Runnable{

	Object getKey();

	void setParentQueue(TaskQueue<IOrderMessageTask> taskQueue);

	boolean isUrgency();

	TaskQueue<IOrderMessageTask> getParentQueue();

}

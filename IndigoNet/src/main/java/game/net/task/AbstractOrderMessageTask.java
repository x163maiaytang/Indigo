package game.net.task;

public abstract class AbstractOrderMessageTask implements IOrderMessageTask {

	protected TaskQueue<IOrderMessageTask> parentQueue;
	protected boolean isUrgency;

	public void setParentQueue(TaskQueue<IOrderMessageTask> queue) {
		this.parentQueue = queue;
	}

	public TaskQueue<IOrderMessageTask> getParentQueue() {
		return parentQueue;
	}

	public boolean isUrgency() {
		return isUrgency;
	}

	public void setUrgency(boolean isUrgency) {
		this.isUrgency = isUrgency;
	}

}

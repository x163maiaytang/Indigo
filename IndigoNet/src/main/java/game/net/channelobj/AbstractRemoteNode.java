package game.net.channelobj;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractRemoteNode implements IRemoteNode {


	
	protected static final AtomicLong GENGERT_ID = new AtomicLong(0l); 
	protected long userId;
	protected Object attachement;
	
	protected final long sessionId;
	protected AtomicInteger activeCount = new AtomicInteger(0);
	
	protected AtomicInteger upSequenceId  = new AtomicInteger(0);
	protected AtomicInteger downSequenceId  = new AtomicInteger(0);

	public AbstractRemoteNode()
	{
		this.sessionId = GENGERT_ID.incrementAndGet();
	}
	
	public final long getId()
	{
		return this.sessionId;
	}
	
	public Object getAttachement()
	{
		return attachement;
	}

	public void setAttachement(Object attachement)
	{
		this.attachement = attachement;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long id)
	{
		this.userId = id;
	}
 
	public int getActiveCount() {
		return activeCount.get();
	}

	public void setActiveCount(int activeCount) {
		this.activeCount.set(activeCount);
	}
	
	public void addActiveCount(int num){
		this.activeCount.addAndGet(num);
	}

	public Integer getUpSequenceId() {
		return upSequenceId.getAndAdd(0);
	}
	
	public void setUpSequenceId(int newSeqId){
		upSequenceId.getAndSet(newSeqId);
	}

	public Integer getDownSequenceId() {
		return downSequenceId.addAndGet(1);
	}
	
	public void setDownSequenceId(int newSeqId){
		downSequenceId.getAndSet(newSeqId);
	}

}

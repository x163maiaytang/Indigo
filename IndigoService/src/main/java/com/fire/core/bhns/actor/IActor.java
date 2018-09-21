package com.fire.core.bhns.actor;

public interface IActor<P extends IActorParam>
{
	public boolean sendReqNB(P param);
	
	public Object sendReqB(P param);
	
	public void sendResNB(Object res);
	
	public Object waitForRes();
	
	public Object waitForRes(int timeMilils);
	
	public long id();
	
//	public boolean isNeedProxy(IActor next);
//	
//	public List<Long> getWaitActorIdList();
}
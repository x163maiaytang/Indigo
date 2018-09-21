package com.fire.core.bhns.proxy;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fire.core.bhns.actor.IActor;
import com.fire.core.bhns.actor.IActorParam;

public class FakeActor implements IActor<IActorParam> {
	
	private Lock syncLock = new ReentrantLock(false);
	private Condition condition = syncLock.newCondition();

	private static AtomicInteger id = new AtomicInteger(0);
	
	private Object returnObj;
	
	public FakeActor() {
		id.addAndGet(1);
	}
	@Override
	public boolean sendReqNB(IActorParam param) {
		return false;
	}

	@Override
	public Object sendReqB(IActorParam param)  {
		return null;
	}

	@Override
	public void sendResNB(Object res) {
		this.returnObj = res;
		
		condition.signal();
	}

	@Override
	public Object waitForRes()  {
		return returnObj;
	}

	@Override
	public Object waitForRes(int timeMilils)  {
		try {
			condition.await(timeMilils, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long id() {
		return id.get();
	}
//
//	@Override
//	public boolean isNeedProxy(IActor next) {
//		return false;
//	}
//
//	@Override
//	public List<Long> getWaitActorIdList() {
//		return null;
//	}

	public Lock getSyncLock() {
		return syncLock;
	}

	public Condition getCondition() {
		return condition;
	}
	
	

}

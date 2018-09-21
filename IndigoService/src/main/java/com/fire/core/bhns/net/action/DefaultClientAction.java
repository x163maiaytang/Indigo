package com.fire.core.bhns.net.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fire.constant.CoreServiceConstants;
import com.fire.core.bhns.actor.IActor;
import com.fire.core.bhns.actor.IActorParam;
import com.fire.core.bhns.net.message.SC_ServiceDefaultMessage;
import com.fire.core.bhns.portal.MethodInfo;
import com.fire.core.bhns.proxy.FakeActor;

import game.net.channelobj.IRemoteNode;
import game.net.handler.AbstractSessionAction;

public class DefaultClientAction extends AbstractSessionAction<SC_ServiceDefaultMessage>
{
	private static final Logger logger = Logger.getLogger(DefaultClientAction.class.getName());

	private static DefaultClientAction instance = new DefaultClientAction();
	
	private Map<Long, WaitActor> waitActorMap = new ConcurrentHashMap<Long, WaitActor>();

	public static DefaultClientAction getInstance()
	{
		return instance;
	}

	public void addWaitActor(IActor actor, MethodInfo methodInfo)
	{
		WaitActor waitActor = new WaitActor(actor, methodInfo);
		this.waitActorMap.put(waitActor.actor.id(), waitActor);
	}

	
	static class NullActorParam implements IActorParam{
		
	}
	private static NullActorParam nullReturn = new NullActorParam();
	
	
	
	class WaitActor
	{
		private IActor actor;
		private long waitTime;
		private MethodInfo methodInfo;;
		
		public WaitActor(IActor actor, MethodInfo methodInfo){
			this.actor = actor;
			this.waitTime = System.currentTimeMillis() + methodInfo.getTimeout();
			this.methodInfo = methodInfo;
		}
	}

	@Override
	public void processMessage(SC_ServiceDefaultMessage message, IRemoteNode session)
			
	{
		final long id = message.getSynKey();
		WaitActor waitActor = waitActorMap.remove(id);
//		if (Task.class.isAssignableFrom(actor.getClass()))
//			if (((Task) (actor)).forceStop)
//				return;
		
		if(waitActor != null){
			
			if(false){
				
				if(System.currentTimeMillis() <= waitActor.waitTime){
					Object result = message.getResult();
//					if(result != null){
						
						waitActor.actor.sendResNB(result);
//					}else{
//						
//						waitActor.actor.sendResNB(nullReturn);
//					}
				}else{
					
					logger.error("actor timeout = " + waitActor.methodInfo.getMethod().getName() + "!!!");
				}
			}else{
				FakeActor fakeActor = (FakeActor) waitActor.actor;
				
				try{
					fakeActor.getSyncLock().lock();
					
					if(System.currentTimeMillis() <= waitActor.waitTime){
						waitActor.actor.sendResNB(message.getResult());
//						System.out.println(waitActor.methodname + " = " + ( waitActor.waitTime - System.currentTimeMillis()));
					}else{
						logger.error("actor timeout = " + waitActor.methodInfo.getMethod().getName() + "!!!" + message.getResult() + " id = " + id);
					}
				}finally{
					fakeActor.getSyncLock().unlock();
				}

			}
//		}else{
//			logger.error("actor timeout");
		}
	}
}

package com.fire.core.bhns.net.initializer;

import java.util.concurrent.TimeUnit;

import com.fire.core.bhns.net.action.DefaultClientAction;
import com.fire.core.bhns.net.action.DefaultServerAction;
import com.fire.core.bhns.net.action.ServiceConnectionAction;
import com.fire.core.bhns.net.message.CS_ServiceDefaultMessage;
import com.fire.core.bhns.net.message.SC_ServiceDefaultMessage;
import com.fire.core.monitor.io.IoMonitor;

import game.net.executor.OrderedMessageExecutor;
import game.net.handler.AbstractMessageInitializer;

public class ServiceClientMessageInitalizer extends AbstractMessageInitializer
{
	public ServiceClientMessageInitalizer(String threadGroupName) {
		super(threadGroupName);
	}


//	private AbstractActorPool<MessageProcessorActor> actorPool;

	@Override
	protected void initMessages()
	{
		ServiceConnectionAction connectionHandler = new ServiceConnectionAction();
		setConnectionActiveHandler(connectionHandler);
		setConnectionInactiveHandler(connectionHandler);
		
		setIoIMonitor(IoMonitor.getInstance());
		
//		if(CoreServiceConstants.SERVER_FIBER){
//			actorManager = new MessageProcessorActorPool(100000);
//			actorManager.init();
//		}else{
			setBeforeExecutor(new OrderedMessageExecutor(orderPoolThreadNum, orderPoolThreadNum, 30, TimeUnit.SECONDS, threadGroupName	));
//		}
		
		register();
	}


	private void register()
	{
		addHandler(0, CS_ServiceDefaultMessage.class,
				DefaultServerAction.getInstance());
		addHandler(1, SC_ServiceDefaultMessage.class,
				DefaultClientAction.getInstance());
	}

}

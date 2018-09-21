package game.net.handler;

import game.net.channelobj.IRemoteNode;
import game.net.executor.OrderedMessageExecutor;
import game.net.message.AbstractMessage;
import game.net.message.MessageFactory;
import game.net.monitor.IMonitor;
import game.net.util.SystemUitls;

public abstract class AbstractMessageInitializer
{
	private IAction connectionActiveHandler;
	private IAction connectionInactiveHandler;
	private OrderedMessageExecutor beforeExecutor;
//	protected AbstractActorPool<MessageProcessorActor> actorManager;

	private IMonitor ioIMonitor;
	
	protected int orderPoolThreadNum;
	protected String threadGroupName;

	public AbstractMessageInitializer(String threadGroupName)
	{
		this.orderPoolThreadNum = SystemUitls.CORE_SIZE + 1;
		this.threadGroupName = threadGroupName;
		initMessages();
	}

	protected abstract void initMessages();

	public IAction getConnectionActiveHandler()
	{
		return connectionActiveHandler;
	}

	public void setConnectionActiveHandler(
			IAction connectionActiveHandler)
	{
		this.connectionActiveHandler = connectionActiveHandler;
	}

	public IAction getConnectionInactiveHandler()
	{
		return connectionInactiveHandler;
	}

	public void setConnectionInactiveHandler(
			IAction connectionInactiveHandler)
	{
		this.connectionInactiveHandler = connectionInactiveHandler;
	}

	public OrderedMessageExecutor getBeforeExecutor()
	{
		return beforeExecutor;
	}

	public void setBeforeExecutor(OrderedMessageExecutor beforeExecutor)
	{
		this.beforeExecutor = beforeExecutor;
	}

	public IMonitor getIoIMonitor()
	{
		return ioIMonitor;
	}

	public void setIoIMonitor(IMonitor ioIMonitor)
	{
		this.ioIMonitor = ioIMonitor;
	}

//	public void setActorManager(AbstractActorPool manager)
//	{
//		this.actorManager = manager;
//	}
	
//	public AbstractActorPool<MessageProcessorActor> getActorManager()
//	{
//		return actorManager;
//	}

	public void addHandler(int commandId,
			Class<? extends AbstractMessage> messageClass,
			IAction<?> messageHandler)
	{
		MessageFactory.getInstance().addMessageAndAction(commandId,
				messageClass, messageHandler);
	}
	
	public void release(){
		
		if(beforeExecutor != null){
			beforeExecutor.release();
			beforeExecutor = null;
		}
	
//		if(actorManager != null){
//			actorManager.release();
//			actorManager = null;
//		}
		
		connectionActiveHandler = null;
		connectionInactiveHandler = null;
	}

	public void remove(IRemoteNode remoteNode) {


//		if(beforeExecutor != null){
//			
//			beforeExecutor.remove(remoteNode);
//		}
	}
}

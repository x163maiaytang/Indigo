package game.net.task;


import org.apache.log4j.Logger;

import game.net.channelobj.IRemoteNode;
import game.net.handler.IAction;
import game.net.message.AbstractMessage;

public class MessageTask extends AbstractOrderMessageTask
{
	
	private static final Logger logger = Logger.getLogger(MessageTask.class);
	
	private AbstractMessage message;
	private IAction handler;
	private IRemoteNode session;

	public MessageTask(AbstractMessage message, IAction<?> handler,
			IRemoteNode session)
	{
		this.message = message;
		this.handler = handler;
		this.session = session;
	}

	public Object getKey()
	{
		return handler.getMessageKey(session, message);
	}


	public int getcommandId()
	{
		return message.getCommandId();
	}

	public AbstractMessage getMessage()
	{
		return message;
	}

	public void setMessage(AbstractMessage message)
	{
		this.message = message;
	}

	public IAction getHandler()
	{
		return handler;
	}

	public void setHandler(IAction handler)
	{
		this.handler = handler;
	}

	public IRemoteNode getSession()
	{
		return session;
	}

	public void setSession(IRemoteNode session)
	{
		this.session = session;
	}


	public void run()
	{
		try {
			handler.handleMessage(session, message);
		} catch (Exception e){
			logger.error("", e);
		} 
	}
}

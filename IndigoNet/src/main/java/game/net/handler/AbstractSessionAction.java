package game.net.handler;

import game.net.channelobj.IRemoteNode;
import game.net.message.AbstractMessage;
import game.net.message.ISubTypeAction;

public abstract class AbstractSessionAction<T extends AbstractMessage> implements
		ISubTypeAction<T, IRemoteNode>
{
	public void handleMessage(IRemoteNode session, T message)
	{
		this.processMessage(message, session);
	}
	
	public Object getMessageKey(IRemoteNode remoteNode,T message)
	{
		return null;
	}
}

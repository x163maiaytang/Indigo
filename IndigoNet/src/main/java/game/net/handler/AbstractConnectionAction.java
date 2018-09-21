package game.net.handler;

import game.net.channelobj.IRemoteNode;
import game.net.message.AbstractMessage;

public abstract class AbstractConnectionAction
		implements IAction
{
	public void handleMessage(IRemoteNode session, AbstractMessage message)
	{
		if(session.isConnected())
			this.handleConnectionActive(session);
		else
			this.handleConnectionInactive(session);
	}
	
	public Object getMessageKey(IRemoteNode session, AbstractMessage message)
	{
		return null;
	}

	public abstract void handleConnectionActive(IRemoteNode session);
	
	public abstract void handleConnectionInactive(IRemoteNode session);
}
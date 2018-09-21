package game.net.handler;

import game.net.channelobj.IRemoteNode;
import game.net.message.AbstractMessage;

public interface IAction<T extends AbstractMessage>
{
	public void handleMessage(IRemoteNode session, T message); 

	public Object getMessageKey(IRemoteNode remoteNode, T message);
}

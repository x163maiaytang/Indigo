package com.fire.core.action;

import com.fire.core.bhns.IServiceable;

import game.net.channelobj.IRemoteNode;
import game.net.message.AbstractMessage;
import game.net.message.ISubTypeAction;

public abstract class AbstractServiceableAction<T extends AbstractMessage, P extends IServiceable>
		implements ISubTypeAction<T, P>
{
	
	protected long nextVisitTimestamp;
	
	public Object getMessageKey(IRemoteNode remoteNode,T message)
	{
		return null;
	}
}

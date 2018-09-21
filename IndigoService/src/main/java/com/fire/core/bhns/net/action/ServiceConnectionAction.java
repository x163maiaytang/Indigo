package com.fire.core.bhns.net.action;

import org.apache.log4j.Logger;

import game.net.channelobj.IRemoteNode;
import game.net.handler.AbstractConnectionAction;
import game.net.message.AbstractMessage;

public class ServiceConnectionAction extends AbstractConnectionAction
{
	private static final Logger logger = org.apache.log4j.Logger
			.getLogger(ServiceConnectionAction.class);

	@Override
	public void handleConnectionActive(IRemoteNode remoteNode) 
	{
	};

	@Override
	public void handleConnectionInactive(IRemoteNode remoteNode) 
	{
	}

	public Object getMessageKey(IRemoteNode session, AbstractMessage message)
	{
		return session;
	}
}

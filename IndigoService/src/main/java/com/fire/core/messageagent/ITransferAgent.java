package com.fire.core.messageagent;

import game.net.message.AbstractMessage;

/**
 * 传递消息接口 
 * 
 */
public interface ITransferAgent
{

	public void sendMessage(long roleId, AbstractMessage message) ;
	
	public void sendMessageBySessionId(long sessionId, AbstractMessage message) ;

}

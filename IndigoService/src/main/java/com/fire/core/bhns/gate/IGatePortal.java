package com.fire.core.bhns.gate;

import com.fire.core.bhns.IServicePortal;

import game.net.message.AbstractMessage;

public interface IGatePortal<T extends IGateService> extends IServicePortal<T>
{
	public void kick(long sessionId,boolean isNow) ;
	
	public void kickAll() ;
	
	public void sendMessage(byte[] messageBytes,int messageId,long sessionId) ;

	void broadcastMessage(AbstractMessage message) ;

	void broadcastForCamp(AbstractMessage message, int camp) ;
}

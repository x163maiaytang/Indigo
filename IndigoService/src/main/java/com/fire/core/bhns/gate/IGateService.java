package com.fire.core.bhns.gate;

import com.fire.core.bhns.ISimpleService;

public interface IGateService<P extends IGatePortal> extends ISimpleService<P>
{
	public void sendMessage(int messageId,byte[] messageBytes);
	
	public void loginFinish(long userId, long sessionId, int camp);
}

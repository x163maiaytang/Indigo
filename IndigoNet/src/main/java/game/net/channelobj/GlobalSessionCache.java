package game.net.channelobj;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import game.net.message.AbstractMessage;
import io.netty.channel.ChannelFuture;

public class GlobalSessionCache
{
	private static Map<Integer, ConcurrentHashMap<Long, IRemoteNode>> serverSessionsMap = new HashMap<Integer, ConcurrentHashMap<Long, IRemoteNode>>();

	public boolean registSession(int serverType, long sessionId,
			IRemoteNode session)
	{
		findSessionMap(serverType).put(sessionId, session);
		return true;
	}

	private ConcurrentHashMap<Long, IRemoteNode> findSessionMap(int serverType)
	{
		ConcurrentHashMap<Long, IRemoteNode> sessionMap = serverSessionsMap
				.get(serverType);
		if (sessionMap == null)
		{
			sessionMap = new ConcurrentHashMap<Long, IRemoteNode>();
			serverSessionsMap.put(serverType, sessionMap);
		}
		return sessionMap;
	}

	public int getSessionCount(int serverType)
	{
		return findSessionMap(serverType).size();
	}

	public IRemoteNode getSessionById(int serverType, long sessionId)
	{
		IRemoteNode remoteNode = findSessionMap(serverType).get(sessionId);
		
		return remoteNode;
	}

	public IRemoteNode removeSession(int serverType, long sessionId)
	{
		return findSessionMap(serverType).remove(sessionId);
	}

	public boolean sendMessage(int serverType, long sessionId,
			AbstractMessage message, ISendMessageInterceptor interceptor)
	{
		IRemoteNode session = findSessionMap(serverType).get(sessionId);
		if (interceptor != null
				&& !interceptor.before(serverType, message, session))
			return false;
		if (session == null || !session.isConnected())
			return false;

		try
		{
			ChannelFuture writeFuture = session.writeAndFlush(message);
			if (interceptor != null)
				interceptor.after(serverType, message, session, writeFuture);
		} catch (Exception e)
		{
			e.printStackTrace();
			if (interceptor != null)
				interceptor.onException(serverType, message, session, e);
		}
		return true;
	}

	public void broadcastMessage(int serverType, AbstractMessage message)
	{
		for (IRemoteNode session : findSessionMap(serverType).values())
		{
			if (session.isConnected())
				session.writeAndFlush(message);
		}
	}

	// *********************************************//
	private final static GlobalSessionCache _instance = new GlobalSessionCache();

	public final static GlobalSessionCache getInstance()
	{
		return _instance;
	}
}

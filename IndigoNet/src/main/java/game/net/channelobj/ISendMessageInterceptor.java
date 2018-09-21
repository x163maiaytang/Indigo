package game.net.channelobj;

import game.net.message.AbstractMessage;
import io.netty.channel.ChannelFuture;

public interface ISendMessageInterceptor
{
	public boolean before(int serverType, AbstractMessage message,
			IRemoteNode session);

	public void after(int serverType, AbstractMessage message,
			IRemoteNode session, ChannelFuture writeFuture);

	public void onException(int serverType, AbstractMessage message,
			IRemoteNode session, Exception e);
}

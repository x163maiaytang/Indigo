package game.net.server;

import java.net.SocketAddress;

import org.apache.log4j.Logger;

import game.net.channelobj.RemoteNode;
import game.net.handler.AbstractMessageInitializer;
import game.net.message.AbstractMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SimpleClient
{
	private static final Logger logger = Logger.getLogger(SimpleClient.class);
	
	private Bootstrap clientBootstrap;
	private Channel channel;

	private AbstractMessageInitializer messageInitializer;
	
	public void initialize(ChannelInitializer<Channel> channelInitializer,
			AbstractMessageInitializer messageInitializer) throws Exception {
		
		this.messageInitializer = messageInitializer;
		
		EventLoopGroup workerGroupClient = new NioEventLoopGroup();
		clientBootstrap = new Bootstrap();
		
		clientBootstrap.group(workerGroupClient).channel(NioSocketChannel.class).handler(channelInitializer);
		clientBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		clientBootstrap.option(ChannelOption.TCP_NODELAY, true);
		clientBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000); // 3s
	};


	public boolean connectTo(SocketAddress remoteAddress, int retryTimes)
	{
		ChannelFuture future = null;
		boolean result = false;

		for (int connectNum = 0; connectNum <= retryTimes; connectNum ++)
		{
			try
			{
				future = clientBootstrap.connect(remoteAddress).sync();
			} catch (Exception e1)
			{
				logger.error("Failed to connect to {" + remoteAddress + "} {" + e1 + "}");
				continue;
			}

			if (future.isSuccess())
			{
				result = true;
				channel = future.channel();
				logger.info("Connect to " + remoteAddress.toString() + " success");
				break;
			}
		}

		if (!result){
			
			logger.error("After retry " + retryTimes + " times, still can't connect to {" + remoteAddress.toString() + "}!!!");
		}else{
			try
			{
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e)
			{
				logger.error("exception:", e);
			}
		}

		return result;
	}

	public long getSessionId(){
		if(isConnected()){
			return channel.attr(RemoteNode.REMOTENODE).get().getId();
		}
		
		return 0;
	}
	public boolean sendMessage(AbstractMessage msg)
	{
		if (channel == null)
		{
			logger.error("client channel not init");
			return false;
		}

		if (!channel.isActive())
		{
			logger.error("client channel is unconnected");
			return false;
		}

		this.channel.writeAndFlush(msg);
		return true;
	}

	public boolean isConnected()
	{
		if (channel == null)
			return false;
		return channel.isActive();
	}


	public void close() {
		if(isConnected()){
			channel.close();
		}
		
		if(messageInitializer != null){
			messageInitializer.release();
			
			messageInitializer = null;
		}
	}
}

package game.net.channelobj;

import java.net.SocketAddress;

import org.apache.log4j.Logger;

import game.net.message.AbstractMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;

public class RemoteNode extends AbstractRemoteNode
{
	private static final Logger logger = Logger.getLogger(RemoteNode.class);
	public static final AttributeKey<RemoteNode> REMOTENODE = AttributeKey.valueOf("remoteNode");
	public static final AttributeKey<Integer> CHANNELID = AttributeKey.valueOf("channelId");
	
	private SocketAddress address;
	private Channel channel;
 

	public RemoteNode(Channel channel)
	{
		super();
		this.channel = channel;
	}
 
	
	public SocketAddress getAddress()
	{
		return address;
	}

	public void setAddress(SocketAddress address)
	{
		this.address = address;
	}

	public Channel getChannel()
	{
		return channel;
	}

	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}


	public void write(AbstractMessage msg)
	{
		msg.addRefNum();
		channel.write(msg);
	}

	public void flush()
	{
		channel.flush();
	}

	public ChannelFuture writeAndFlush(final AbstractMessage msg)
	{
		final long now = System.currentTimeMillis();
		if(msg == null)
			throw new NullPointerException();
		
		msg.setTimeStamp(now);
		msg.addRefNum();
		
//		if(!channel.isWritable()){
//			logger.error("WriteBufferHighWater  error error = " +  channel.config().getWriteBufferHighWaterMark());
//		}
		return channel.writeAndFlush(msg);
		
	}

	public void writeAndClose(AbstractMessage msg)
	{
		if(msg== null)
			throw new NullPointerException();
		
		logger.debug("send message:"+msg.getCommandId()+",messageClz："+msg.getClass().getSimpleName());
		msg.addRefNum();
		ChannelFuture f = channel.writeAndFlush(msg);
		f.addListener(futureListner);
	}
		
	private static ChannelFutureListener futureListner = new ChannelFutureListener()
	{
		public void operationComplete(ChannelFuture future) 
		{
			Channel ch = future.channel();
			ch.close().addListener(ChannelFutureListener.CLOSE);;
		}
    };
	
	public boolean isConnected()
	{
		return this.channel.isActive();
	}
	
	public void close()
	{
		this.channel.close();
	}


	
}

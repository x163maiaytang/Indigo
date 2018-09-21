package game.net.channelobj;

import java.net.SocketAddress;

import org.apache.log4j.Logger;

import game.net.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;

public class WSNettyRemoteNode extends AbstractRemoteNode
{
	private static final Logger logger = Logger.getLogger(RemoteNode.class);
	public static final AttributeKey<WSNettyRemoteNode> REMOTENODE = AttributeKey.valueOf("remoteNode");
	public static final AttributeKey<Integer> CHANNELID = AttributeKey.valueOf("channelId");
	
	private SocketAddress address;
	private Channel channel;
 

	public WSNettyRemoteNode(Channel channel)
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
		
		
		byte[] encode = msg.encode();
		
		int commandId = msg.getCommandId();
		ByteBuf outBuffer = ByteBufAllocator.DEFAULT.ioBuffer(256);
		
		outBuffer.writeInt(encode.length + 8);
		outBuffer.writeInt(commandId);
		outBuffer.writeBytes(encode);
		
//		byte[] bytes = new byte[outBuffer.readableBytes()];
//		outBuffer.readBytes(bytes);
//		if(!channel.isWritable()){
//			logger.error("WriteBufferHighWater  error error = " +  channel.config().getWriteBufferHighWaterMark());
//		}
		
//		try {
			return channel.writeAndFlush(new BinaryWebSocketFrame(outBuffer));
//		} catch (Exception e) {
//			logger.error("", e);
//		}finally {
//			if (outBuffer != null && outBuffer.refCnt() > 0){
//				
//				ReferenceCountUtil.release(outBuffer);
//			}
//			
//		}
//		return null;
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

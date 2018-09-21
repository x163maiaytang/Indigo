package game.net.channelobj;

import java.io.IOException;
import java.net.SocketAddress;

import org.apache.log4j.Logger;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import game.net.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.util.ReferenceCountUtil;

public class WSRemoteNode extends AbstractRemoteNode
{
	private static final Logger logger = Logger.getLogger(WSRemoteNode.class);
	
 
	
	private SocketAddress address;


	private WebSocketSession session;
 

	public WSRemoteNode(WebSocketSession session)
	{
		super();
		this.session = session;
	}
 
	
	public SocketAddress getAddress()
	{
		return address;
	}

	public void setAddress(SocketAddress address)
	{
		this.address = address;
	}

	public Object getChannel()
	{
		return session;
	}

 
	public void write(AbstractMessage msg)
	{
		msg.addRefNum();
//		channel.write(msg);
	}

	public void flush()
	{
//		session.flush();
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
		
		byte[] bytes = new byte[outBuffer.readableBytes()];
		outBuffer.readBytes(bytes);
		
		try {
			session.sendMessage(new BinaryMessage(bytes));
		} catch (IOException e) {
			logger.error("", e);
		}finally {
			if (outBuffer != null && outBuffer.refCnt() > 0){
				
				ReferenceCountUtil.release(outBuffer);
			}
			
			bytes = null;
		}
		
		return null;
		
	}

	@Deprecated
	public void writeAndClose(AbstractMessage msg)
	{
		if(msg== null)
			throw new NullPointerException();
		
		logger.debug("send message:"+msg.getCommandId()+",messageClz："+msg.getClass().getSimpleName());
		msg.addRefNum();
		
		
	}
		
 
	
	public boolean isConnected()
	{
		return this.session.isOpen();
	}
	
	public void close()
	{
		try {
			this.session.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}


	
}

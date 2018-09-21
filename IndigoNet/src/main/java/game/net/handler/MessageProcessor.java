package game.net.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import game.net.channelobj.IRemoteNode;
import game.net.channelobj.RemoteNode;
import game.net.executor.OrderedMessageExecutor;
import game.net.message.AbstractMessage;
import game.net.message.MessageFactory;
import game.net.task.MessageTask;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class MessageProcessor extends ChannelDuplexHandler
{
	private static final Logger logger = Logger.getLogger(MessageProcessor.class);

	public static final int MESSAGE_SENDBUFFER_DEFAULTSIZE = 256;
	protected AbstractMessageInitializer messageInitializer;

	public MessageProcessor(AbstractMessageInitializer messageInitializer)
	{
		this.messageInitializer = messageInitializer;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = null;
		AbstractMessage message = null;
		try {
			IRemoteNode remoteNode = ctx.channel().attr(RemoteNode.REMOTENODE).get();
			
			buf = (ByteBuf) msg;
			final int length = buf.readableBytes();
			int commandId = buf.readInt();
			

			if (messageInitializer.getIoIMonitor() != null)
				messageInitializer.getIoIMonitor().recordSimpleInfo(commandId, true, length);

			message = MessageFactory.getInstance().getMessage(commandId);
			if (message != null) {
				
				byte[] bufdata = new byte[buf.readableBytes()];
				buf.readBytes(bufdata);
				
				message.decode(bufdata);
				message.setCommandId(commandId);
			}

			IAction<? extends AbstractMessage> handler = MessageFactory.getInstance().getMessageHandler(commandId);
			readMessage(handler, message, remoteNode);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
//			System.out.println(buf.refCnt());
			if(buf != null && buf.refCnt() > 0){
				
				ReferenceCountUtil.release(buf);
			}
//			System.out.println(buf.refCnt());
			
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		ByteBuf outBuffer = null;
		AbstractMessage message = null;
		int commandId = 0;
		
		try {
			message = (AbstractMessage) msg;
			
			commandId = message.getCommandId();
			outBuffer = ByteBufAllocator.DEFAULT.ioBuffer(MESSAGE_SENDBUFFER_DEFAULTSIZE);
			outBuffer.writeInt(commandId);
			outBuffer.writeBytes(message.encode());
			
//			MessageFactory.freeMessage(message);
			if (messageInitializer.getIoIMonitor() != null){
				messageInitializer.getIoIMonitor().recordSimpleInfo(commandId,
						false, outBuffer.writerIndex());
			}

			ctx.write(outBuffer, promise);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outBuffer != null && outBuffer.refCnt() > 0){
				
				ReferenceCountUtil.release(outBuffer);
			}
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		if (!NioDatagramChannel.class
				.isAssignableFrom(ctx.channel().getClass()))
		{
//			System.out.println("建立连接啦~~~~~~~~~~~~~~~~~~" + ctx.channel().remoteAddress().toString());
			// ctx.fireChannelActive();
			RemoteNode remoteNode = new RemoteNode(ctx.channel());
			remoteNode.setAddress(ctx.channel().remoteAddress());

			ctx.channel().attr(RemoteNode.REMOTENODE).set(remoteNode);
			
		    onActive(remoteNode);
		}
	}

	
	public void onActive(IRemoteNode remoteNode)
	{
		
	}
	  
	/**
	 * Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward to
	 * the next {@link ChannelInboundHandler} in the {@link ChannelPipeline}.
	 * 
	 * Sub-classes may override this method to change behavior.
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		if (!NioDatagramChannel.class
				.isAssignableFrom(ctx.channel().getClass()))
		{
			// ctx.fireChannelInactive();
			IRemoteNode remoteNode = ctx.channel().attr(RemoteNode.REMOTENODE).get();
		    onInactive(remoteNode);
		}
	}

	public void onInactive(IRemoteNode remoteNode)
	{
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		if (IOException.class.isAssignableFrom(cause.getClass()))
		{
			// 猜想是远程连接关
//			logger.error("Remote Peer:{} {}" + ctx.channel().remoteAddress());
		} else
		{
			logger.error("Exeption:", cause);
		}
	}


	public void readMessage(final IAction handler,
			final AbstractMessage message, final IRemoteNode session)
	{
		if (handler != null)
		{
			if(message != null){
				message.setTimeStamp(System.currentTimeMillis());
			}
			final OrderedMessageExecutor beforeExecutor = messageInitializer.getBeforeExecutor();
			beforeExecutor.execute(new MessageTask(message, handler, session)
			{
				public void run()
				{
					try {
						handler.handleMessage(session, message);
					} catch (Exception e){
						logger.error("", e);
					} 
				}
			});
		}
	}
}

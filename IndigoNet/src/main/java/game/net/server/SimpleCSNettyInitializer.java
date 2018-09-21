package game.net.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import game.net.handler.AbstractMessageInitializer;
import game.net.handler.MessageProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class SimpleCSNettyInitializer extends ChannelInitializer<Channel>
{
	protected static AtomicInteger handlerId = new AtomicInteger(0);
	protected MessageProcessor messageProcessor;

	public SimpleCSNettyInitializer(AbstractMessageInitializer messageInitializer, MessageProcessor processor){
		
		if (processor != null){
			
			this.messageProcessor = processor;
		}else{
			// default
			this.messageProcessor = new MessageProcessor(messageInitializer);
		}
	}

	@Override
	protected void initChannel(Channel ch) throws Exception
	{
		String nameId = "-" + handlerId.incrementAndGet();
		ChannelPipeline p = ch.pipeline();
		p.addLast("idleStateHandler", new IdleStateHandler(10, 15, 0, TimeUnit.SECONDS)); 
		p.addLast("Decoder" + nameId, new LengthFieldBasedFrameDecoder(204800, 0, 4, 0, 4));
		p.addLast("Encoder" + nameId, new LengthFieldPrepender(4, false));
		p.addLast("MessageProcessor" + nameId, messageProcessor);
	}

	public MessageProcessor getMessageProcessor()
	{
		return messageProcessor;
	}
}

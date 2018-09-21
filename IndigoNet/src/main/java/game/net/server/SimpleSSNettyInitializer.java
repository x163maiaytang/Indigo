package game.net.server;


import java.util.concurrent.atomic.AtomicInteger;

import game.net.handler.AbstractMessageInitializer;
import game.net.handler.MessageProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;


public class SimpleSSNettyInitializer extends ChannelInitializer<Channel>
{
	static AtomicInteger handlerId=new AtomicInteger(0);
	private MessageProcessor messageProcessor;

	public SimpleSSNettyInitializer(AbstractMessageInitializer messageInitializer, MessageProcessor processor)
	{
	    if (processor != null){
	    	this.messageProcessor = processor;
	    }else{
	    	// default
	    	this.messageProcessor = new MessageProcessor(messageInitializer);
	    }
	}


	@Override
	protected void initChannel(Channel ch)
		throws Exception{
		
		String nameId="-"+ handlerId.incrementAndGet();

		ChannelPipeline p = ch.pipeline();
		
		p.addLast("Decoder" + nameId, new LengthFieldBasedFrameDecoder(100 * 1024 * 1024, 0, 4, 0, 4));
		p.addLast("Encoder" + nameId, new LengthFieldPrepender(4,false));
		
		p.addLast("MessageProcessor" + nameId, messageProcessor);
	}
}

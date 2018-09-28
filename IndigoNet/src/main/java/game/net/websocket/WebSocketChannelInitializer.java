package game.net.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class WebSocketChannelInitializer extends ChannelInitializer {

	private SimpleChannelInboundHandler<Object> handler;

	public WebSocketChannelInitializer(SimpleChannelInboundHandler<Object> handler) {
		this.handler = handler;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {

		ChannelPipeline p = ch.pipeline();
		p.addLast(new ChannelHandler[] { new HttpClientCodec(), new HttpObjectAggregator(1024 * 1024 * 10) });
		p.addLast("hookedHandler", handler);

	}

}

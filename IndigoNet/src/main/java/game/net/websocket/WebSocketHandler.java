package game.net.websocket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;

public abstract class WebSocketHandler<T extends Object> extends SimpleChannelInboundHandler<Object>{

	public abstract void setShakeHandler(WebSocketClientHandshaker handshaker) ;

	public abstract ChannelFuture handshakeFuture() ;

}

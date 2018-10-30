package game.net.websocket;

import java.net.URI;

import org.apache.log4j.Logger;

import game.net.channelobj.RemoteNode;
import game.net.message.AbstractMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class WebSocketSimpleClient {
	private static final Logger logger = Logger.getLogger(WebSocketSimpleClient.class);

	private Bootstrap boot;
	private Channel channel;

	private WebSocketHandler<Object> handler;

	public void initialize(WebSocketHandler<Object> handler) throws Exception {

		this.handler = handler;

		WebSocketChannelInitializer initializer = new WebSocketChannelInitializer(handler);

		EventLoopGroup group = new NioEventLoopGroup();
		boot = new Bootstrap();
		boot.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 3s
//			.option(ChannelOption.SO_BACKLOG, 1024 * 1024 * 10)
			.group(group)
			.handler(new LoggingHandler(LogLevel.INFO)).channel(NioSocketChannel.class).handler(initializer);

	};

	public boolean connectTo(String url, int retryTimes) throws Exception {
		boolean result = false;
		ChannelFuture future = null;
		
		
		URI websocketURI = new URI(url);
		
		
		HttpHeaders httpHeaders = new DefaultHttpHeaders();
		WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String) null, true, httpHeaders);
		for (int connectNum = 0; connectNum <= retryTimes; connectNum++) {
			try {
				future = boot.connect(websocketURI.getHost(), websocketURI.getPort()).sync();
			} catch (Exception e) {
				logger.error("Failed to connect to {" + url + "} {" + e + "}");
				continue;
			}

			if (future.isSuccess()) {
				
				this.channel = future.channel();

				handler.setShakeHandler(handshaker);
				handshaker.handshake(channel);

				// 阻塞等待是否握手成功
				handler.handshakeFuture().sync();
				result = true;
				logger.info("Connect to " + url + " success");
				break;
			}
		}

		if (!result) {

			logger.error("After retry " + retryTimes + " times, still can't connect to {" +url+ "}!!!");
		} else {
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
				logger.error("exception:", e);
			}
		}

		return result;
	}

	public long getSessionId() {
		if (isConnected()) {
			return channel.attr(RemoteNode.REMOTENODE).get().getId();
		}

		return 0;
	}

	public boolean sendMessage(ByteBuf bf) {
		if (channel == null) {
			logger.error("client channel not init");
			return false;
		}
		
		if (!channel.isActive()) {
			logger.error("client channel is unconnected");
			return false;
		}
		
		
		BinaryWebSocketFrame binaryWebSocketFrame=new BinaryWebSocketFrame(bf);
		this.channel.writeAndFlush(binaryWebSocketFrame);
		return true;
	}
	
	public boolean sendMessage(AbstractMessage msg) {
		if (channel == null) {
			logger.error("client channel not init");
			return false;
		}

		if (!channel.isActive()) {
			logger.error("client channel is unconnected");
			return false;
		}

		byte[] encode = msg.encode();
		
		int commandId = msg.getCommandId();
		ByteBuf bf = ByteBufAllocator.DEFAULT.ioBuffer(256);
		
		bf.writeInt(encode.length + 8);
		bf.writeInt(commandId);
		bf.writeBytes(encode);
		
		
		return sendMessage(bf);
	}

	public boolean isConnected() {
		if (channel == null)
			return false;
		return channel.isActive();
	}

	public void close() {
		if (isConnected()) {
			channel.close();
		}
	}
}

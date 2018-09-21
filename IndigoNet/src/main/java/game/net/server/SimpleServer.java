package game.net.server;

import java.net.SocketAddress;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SimpleServer
{
	private static final Logger logger = Logger.getLogger(SimpleServer.class);
	
	private ServerBootstrap serverBootstrap;
	private EventLoopGroup bossGroup = 	null;
	private EventLoopGroup workerGroup = null;
	
	public void initialize(ChannelInitializer<Channel> channelInitializer) throws Exception{
		
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
		
		serverBootstrap.childHandler(channelInitializer);
		
		serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
		serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000); // 3s
		
//		serverBootstrap.option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024);
//		serverBootstrap.option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024);
	};


	public boolean openPort(SocketAddress localAddress){
		
		if (serverBootstrap == null){
			throw new NullPointerException("serverBootstap");
		}

		
		try{
			ChannelFuture future = serverBootstrap.bind(localAddress).sync();
			
			
//			future.channel().closeFuture().sync();
			
		} catch (InterruptedException e){
//			result = false;
			logger.error("Failed to bind local address:" + localAddress, e);
			
		}finally{
//			 bossGroup.shutdownGracefully();
//	         workerGroup.shutdownGracefully();
	         bossGroup = null;
	         workerGroup = null;
		}

		return true;
	}
}

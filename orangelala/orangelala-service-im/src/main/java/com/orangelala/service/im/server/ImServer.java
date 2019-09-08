package com.orangelala.service.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 基于websocket的通讯系统server
 * 
 * @author chrilwe
 *
 */
public class ImServer {

	public void startServer() {
		// boossGroups处理连接事件
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// workGroup处理已连接的channelHandler事件
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class) // 用的是nio的方式来处理连接
					.childHandler(new ImChannelInitializer()).option(ChannelOption.SO_BACKLOG, 20)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			// 绑定端口
			ChannelFuture future = serverBootstrap.bind(52013).sync();
			// 事件是异步处理的，要想知道事件完成,监听事件返回future判断是否事件处理成功
			Channel channel = future.channel();
			// 等待关闭
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();

		}
	}
	
	public static void main(String[] args) {
		ImServer imServer = new ImServer();
		imServer.startServer();
	}
}

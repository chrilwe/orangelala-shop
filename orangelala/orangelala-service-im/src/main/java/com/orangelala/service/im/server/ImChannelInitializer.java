package com.orangelala.service.im.server;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ImChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		// HttpServerCodec将请求和应答消息编码或解码为HTTP消息
		// 通常接收到的http是一个片段，如果想要完整接受一次请求所有数据，我们需要绑定HttpObjectAggregator
		// 然后就可以收到一个FullHttpRequest完整的请求信息了
		// ChunkedWriteHandler 向客户端发送HTML5文件，主要用于支持浏览器和服务器进行WebSocket通信
		/*SSLContext sslContext = SslContextUtil.sslContext("123456", "jks", "d:/ssl.jks");
		pipeline.addLast("ssl",new SslHandler(engine))*/
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
		pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(536));
		// WebSocketServerProtocolHandler中实现了对websocket的封装，不用我们去考虑握手的问题
		pipeline.addLast("webSocketServerProtocolHandler", (ChannelHandler) new WebSocketServerProtocolHandler("/ws"));
		pipeline.addLast("handler",new ImHandler());
	}

}

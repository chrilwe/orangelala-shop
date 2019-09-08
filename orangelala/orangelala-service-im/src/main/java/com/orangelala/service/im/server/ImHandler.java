package com.orangelala.service.im.server;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.base.Code;
import com.orangelala.framework.model.im.ConnectionModel;
import com.orangelala.framework.model.im.MessageModel;
import com.orangelala.service.im.group.ImChannelGroup;
import com.orangelala.service.im.server.auth.ImAuth;
import com.orangelala.service.im.utils.SpringBeanGetter;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ImHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext context, Object message) throws Exception {
		String msg = ((TextWebSocketFrame) message).text();
		System.out.println(msg);
		//身份校验
		SpringBeanGetter beanGetter = new SpringBeanGetter();
		ImAuth imAuth = beanGetter.getBean(ImAuth.class);
		BaseResponse response = imAuth.login(msg);
		if(response.getCode() != Code.SUCCESS) {
			//校验失败，返回401状态码,并断开与客户端的连接
			Object code = "401";
			context.channel().writeAndFlush(new TextWebSocketFrame(((TextWebSocketFrame) code).text()));
			context.channel().close();
			return;
		}
		//身份校验成功，保存连接信息
		
	}
	
	//客户端连上服务端
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		
	}
	
}

package com.orangelala.service.im.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.alibaba.fastjson.JSON;
import com.orangelala.framework.model.im.ConnectionModel;
import com.orangelala.framework.model.im.type.AuthType;

/**
 * IM通讯客户端
 * 
 * @author chrilwe
 *
 */
public class ImClient {

	private static WebSocketClient client;

	public static void main(String[] args) throws URISyntaxException, NotYetConnectedException, UnsupportedEncodingException {
		client = new WebSocketClient(new URI("ws://localhost:52013/ws"), new Draft_6455()) {
			
			@Override
			public void onOpen(ServerHandshake handshakedata) {
				System.out.println("client connect to server,status="+handshakedata.getHttpStatus());
			}
			
			@Override
			public void onMessage(String message) {
				System.out.println(message);
			}
			
			@Override
			public void onError(Exception ex) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose(int code, String reason, boolean remote) {
				System.out.println("close client");
			}
		};
		client.connect();
		while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
			System.out.println("connecting.....");
		}
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setAuthType(AuthType.token);
		client.send(JSON.toJSONString(connectionModel));
	}
}

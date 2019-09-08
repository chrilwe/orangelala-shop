package com.orangelala.framework.model.im;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClientDetails implements Serializable {
	//客户端连接到 服务端的IP地址
	private String connectServerIp;
	//客户端ID
	private String clientId;
	//channel ID
	private String channelId;
}

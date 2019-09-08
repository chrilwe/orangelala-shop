package com.orangelala.service.im.server.auth;
/**
 * IM通讯认证方式(自定义认证实现当前接口，并且子类加上companent注解)
 * @author chrilwe
 *
 */

import com.orangelala.framework.common.base.BaseResponse;
import com.orangelala.framework.common.im.response.ImAuthResponse;

public interface ImAuth {
	public ImAuthResponse login(String message);
}

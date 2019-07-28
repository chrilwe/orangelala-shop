package com.orangelala.framework.common.base;

import lombok.Data;
import lombok.ToString;
/**
 * 基本响应信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class BaseResponse {
	private int code;//响应码
	private String msg;//响应内容
	private boolean success;//响应是否成功
	
	public BaseResponse(int code,String msg) {
		this.code = code;
		this.msg = msg;
		if(code == Code.SUCCESS) {
			this.success = true;
		} else {
			this.success = false;
		}
	}
}

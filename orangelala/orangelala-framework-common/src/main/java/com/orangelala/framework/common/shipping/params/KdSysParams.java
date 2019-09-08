package com.orangelala.framework.common.shipping.params;

import lombok.Data;
import lombok.ToString;

/**
 * 快递鸟系统级别参数
 * @author chrilwe
 *
 */
@Data
@ToString
public class KdSysParams {
	private String EBusinessID;
	private String RequestData;
	private String RequestType;
	private String DataSign;
	private String DataType;
}

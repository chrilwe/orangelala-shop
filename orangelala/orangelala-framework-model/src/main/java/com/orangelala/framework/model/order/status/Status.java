package com.orangelala.framework.model.order.status;
/**
 * 物流状态
 * @author chrilwe
 *
 */
public interface Status {
	public static final String NORMAL = "normal";//正常
	public static final String CANCEL = "cancel";//取消
	public static final String NOPAY = "noPay";//未支付
	public static final String PAY_SUCCESS = "pay_success";//已支付 
	public static final String PAYING = "paying";//支付中
}

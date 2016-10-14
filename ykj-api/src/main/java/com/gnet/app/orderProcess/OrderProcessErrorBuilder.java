package com.gnet.app.orderProcess;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class OrderProcessErrorBuilder extends BaseErrorBuilder {
	
	/** 订单进程状态不能为空 **/
	public static final Integer ERROR_ORDERPROCESS_NULL = 13200;
	
	public OrderProcessErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

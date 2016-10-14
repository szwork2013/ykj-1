package com.gnet.app.order;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class OrderErrorBuilder extends BaseErrorBuilder {
	
	/**
	 * 订单编号为空
	 */
	public static final Integer ERROR_ID_NULL = 13000;
	
	/**
	 * 编号无法找到对应的订单信息
	 */
	public static final Integer ERROR_ORDER_NULL = 13001;
	
	
	
	
	/**
	 * 订单来源为空错误
	 */
	public static final Integer ERROR_ORDERSOURCE_NULL = 13033;
	
	/**
	 * 送货地址为空错误
	 */
	static final Integer ERROR_ADDRESS_NULL = 13034;
	
	/**
	 * 客户编号为空错误
	 */
	static final Integer ERROR_CUSTOMERID_NULL = 13032;
	
	/**
	 * 跟单人编号为空错误
	 */
	static final Integer ERROR_ORDERRESPONSIBLEID_NULL = 13031;
	
	/**
	 * 订单下单日期为空错误
	 */
	public static final Integer ERROR_ORDERDATE_NULL = 13030;
	
	
	public static final Integer ERROR_ORDERNO_TOOLONG = 13002;
	
	public static final Integer ERROR_CUSTOMERREMARK_TOOLONG = 13003;
	
	public static final Integer ERROR_ADDRESS_TOOLONG = 13004;
	
	public static final Integer ERROR_ORDERCREATORID_TOOLONG = 13005;
	
	public static final Integer ERROR_PHONESEC_TOOLONG = 13006;
	
	public static final Integer ERROR_PRIVATEREMARK_TOOLONG = 13007;
	
	public static final Integer ERROR_ORDERRESPONSIBLEID_TOOLONG = 13008;
	
	public static final Integer ERROR_ID_TOOLONG = 13009;
	
	public static final Integer ERROR_CUSTOMERID_TOOLONG = 13010;
	
	public static final Integer ERROR_BUSINESSID_TOOLONG = 13011;
	
	
	
	public OrderErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

package com.gnet.app.orderGood;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class OrderGoodErrorBuilder extends BaseErrorBuilder {
	
	
	public static final Integer ERROR_INITPOSITION_TOOLONG = 13100;
	
	/** 库存商品不能为空 **/
	public static final Integer ERROR_STORAGEGOOD_NULL = 13101;
	
	/** 订单商品数量 **/
	public static final Integer ERROR_GOODSNUM_INVALID = 13102;
	
	/** 折扣率不能为空 **/ 
	public static final Integer ERROR_DISCOUNTRATE_NULL = 13103;
	
	/** 折后单价  **/
	public static final Integer ERROR_UNITPRICE_NULL = 13104;
	
	/** 订单商品不能为空 **/
	public static final Integer ERROR_ORDERGOOD_NULL = 13105;
	
	/** 订单商品编号为空  **/
	public static final Integer ERROR_ORDERGOODID_NULL = 13106;
	
	/** 订单预留商品为空 **/
	public static final Integer ERROR_RESERVEDGOODS_NULL = 13107;
	
	public OrderGoodErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

package com.gnet.app.orderDeliverGoods;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class OrderDeliverGoodsErrorBuilder extends BaseErrorBuilder {
	
	
	/** 送货数量为空 **/
	public static Integer ERROR_DELIVENUM_NULL = 16000;
	
	public static Integer ERROR_DELIVENUM_TOOLONG = 16001;
	
	public static Integer ERROR_POSTION_TOOLONG = 16002;
	
	/** 订单商品编号为空 **/
	public static Integer ERROR_ORDERGOODSID_NULL = 16003;
	
	/** 订单送货服务记录编号 **/
	public static Integer ERROR_ORDERDELIVERSERVICEID_NULL = 16004;
	
	/** 送货数量为空 **/
	public static Integer ERROR_DELIVERNUM_NULL = 16005;
	
	/** 订单送货商品为空 **/
	public static final Integer ERROR_ORDERDELIVERGOODS_NULL = 16006;
	
	/** 更新的送货商品数量和订单商品数量不同 **/
	public static final Integer ERROR_DELIVERGOODS_LACK = 16007;

	/** 订单商品送货编号为空 **/
	public static final Integer ERROR_DELIVER_GOODS_NULL = 16008;
	
	public OrderDeliverGoodsErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

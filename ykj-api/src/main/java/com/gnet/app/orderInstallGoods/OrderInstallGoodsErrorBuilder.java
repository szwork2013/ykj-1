package com.gnet.app.orderInstallGoods;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class OrderInstallGoodsErrorBuilder extends BaseErrorBuilder {
	
	/**
	 * 订单商品安装编号为空
	 */
	public static final Integer ERROR_ID_NULL = 17000;
	
	/**
	 * 编号无法找到对应的订单商品安装信息
	 */
    public static final Integer ERROR_ORDERINSTALLGOODS_NULL = 17001;
	
    /** 安装数量为空 **/
    public static final Integer ERROR_INSTALLNUM_NULL = 17002;
	
    public static final Integer ERROR_INSTALLNUM_TOOLONG = 17003;
    
    /** 订单商品安装信息为空 **/
    public static final Integer ERROR_ORDERINSTALLGOODG_NULL = 17004;
    
    /** 保存或更新的订单送货商品数量有问题 **/
    public static final Integer ERROR_INSTALLGOODS_LACK = 17005;
	
    
	public OrderInstallGoodsErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

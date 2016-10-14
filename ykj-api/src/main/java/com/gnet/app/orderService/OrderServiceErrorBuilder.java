package com.gnet.app.orderService;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class OrderServiceErrorBuilder extends BaseErrorBuilder {
	
	/**
	 * 费用为空错误
	 */
	public static final Integer ERROR_COST_NULL = 14007;
	
	/**
	 * 订单编号为空错误
	 */
	public static final Integer ERROR_ORDERID_NULL = 14002;
	
	/**
	 * 单号为空错误
	 */
	public static final Integer ERROR_SERVICECODE_NULL = 14003;
	
	/**
	 * 名称为空错误
	 */
	public static final Integer ERROR_NAME_NULL = 14004;
	
	/**
	 * 人员编号为空错误
	 */
	public static final Integer ERROR_CLERKID_NULL = 14005;
	
	/**
	 * 要求时间为空错误
	 */
	public static final Integer ERROR_NEEDTIME_NULL = 14006;
	
	/**
	 * 是否已经完成为空错误
	 */
	public static final Integer ERROR_ISFINISH_NULL = 14008;
	
	/**
	 * 类型为空错误
	 */
	public static final Integer ERROR_TYPE_NULL = 14001;
	
	/**
	 * 是否已经结算为空错误
	 */
	public static final Integer ERROR_ISCLEAR_NULL = 14009;
	
	
	public static final Integer ERROR_CUSTOMERREMARK_TOOLONG = 14030;
	
	public static final Integer ERROR_CLERKID_TOOLONG = 14031;
	
	public static final Integer ERROR_SERVICEPOSITION_TOOLONG = 14032;
	
	public static final Integer ERROR_PRIVATEREMARK_TOOLONG = 14033;
	
	public static final Integer ERROR_NAME_TOOLONG = 14034;
	
	public static final Integer ERROR_ATTACHMENTID_TOOLONG = 14035;
	
	public static final Integer ERROR_ID_TOOLONG = 14036;
	
	public static final Integer ERROR_FINANCEEXPENDID_TOOLONG = 14037;
	
	public static final Integer ERROR_ORDERID_TOOLONG = 14038;
	
	/** 服务信息为空 **/
	public static final Integer ERROR_SERVICE_NULL = 14039;
	
	/** 服务编号为空 **/
	public static final Integer ERROR_ID_NULL = 14040;
	
	public static final Integer ERROR_SERVICECODE_TOOLONG = 14041;
	
	/** 同一商家下的单号重复 **/
	public static final Integer ERROR_SERVICECODE_REPEAT = 14042;
	
	/** 未获取上传文件 **/
	public static final Integer ERROR_UPLOAD = 12043;
	
	/** 下载失败 **/
	public static final Integer ERROR_DOWNLOAD = 12044;
	
	/** 已结算的无法再结算 **/
	public static final Integer ERROR_STATE = 12045;
	
	/** 还未结算的无法取消结算 **/
	public static final Integer ERROR_CANCELSTATE = 12046;
	
	/** 订单商品送货数量为空 **/
	public static final Integer ERROR_DELIVER_NUM_NULL = 12047;
	
	/** 订单商品送货数量大于未送货数 **/
	public static final Integer ERROR_DELIVER_NUM = 12048;
	
	/** 订单还未付清不能进行送货服务 **/
	public static final Integer ERROR_DELIVER = 12049;
	
	/** 订单还未付清不能进行安装服务 **/
	public static final Integer ERROR_INSTALLATION = 12050;
	
	/** 订单商品安装数量为空 **/
	public static final Integer ERROR_INSTALL_NUM_NULL = 12051;
	
	/** 订单商品未安装的数量大于未安装数 **/
	public static final Integer ERROR_INSTALL_NUM = 12052;
	
	public OrderServiceErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

package com.gnet.app.customer;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class CustomerErrorBuilder extends  BaseErrorBuilder{


	/**
	 * 客户的编号为空
	 */
	public static final Integer ERROR_ID_NULL = 10501;
	
	/**
	 * 客户信息为空
	 */
	public static final Integer ERROR_CUSTOMER_NULL = 10502;

	/**
	 * 客户负责人编号为空
	 * 
	 */
	public static final Integer ERROR_CUSTOMER_RESPONSIBLE_NULL = 10503;
	
	/**
	 * 客户姓名为空
	 */
	public static final Integer ERROR_NAME_NULL = 10504;
	
	/**
	 * 客户性别为空
	 */
	public static final Integer ERROR_SEX_NULL = 10505;
	
	/**
	 * 手机号码为空
	 */
	public static final Integer ERROR_PHONE_NULL = 10506;
	
	/**
	 * 购物需求为空
	 */
	public static final Integer ERROR_NEED_TIME_NULL = 10507;
	
	/**
	 * 联系地址为空
	 */
	public static final Integer ERROR_ADRESS_NULL = 10508;
	
	/**
	 * 该客户已存在
	 */
	public static final Integer ERROR_REPEAT = 10509;
	
	/**
	 * 新增错误
	 */
	public static final Integer ERROR_CREATED_ERROR = 10510;
	
	/**
	 * 更新错误
	 */
	public static final Integer ERROR_UPDATED_ERROR = 10511;
	
	/**
	 * 删除错误
	 */
	public static final Integer ERROR_DELETED_ERROR = 10512;
	
	/** 排序字段不符合要求  **/
	public static final Integer ERROR_SORT_PROPERTY_NOTFOUND = 10513;
	
	/** 排序方向不符合要求 **/
	public static final Integer ERROR_SORT_DIRECTION_NOTFOUND = 10514;
	/**
	 * 客户负责人信息为空
	 * 
	 */
	public static final Integer ERROR_CUSTOMER_RESPONSIBLE_INFO_NULL = 10515;
	
	/** 客户来源为空 **/
	public static final Integer ERROR_ORIGIN_TYPE_NULL = 10516;
	
	/**
	 * 商家编号为空
	 */
	public static final Integer ERROR_BUSINESS_ID_NULL = 10517;
	
	/**
	 * 客户已是有效无须再置为有效
	 */
	public static final Integer ERROR_IS_EFFECTIVITY = 10518;
	
	/**
	 * 客户已是无效无须再置为无效
	 */
	public static final Integer ERROR_NOT_EFFECTIVITY = 10519;
	
	/**
	 * 标签重复
	 */
	public static final Integer ERROR_TAGS_REPEAT = 10520;
	
	/**
	 * 客户标签信息为空
	 */
	public static final Integer ERROR_CUSTOMER_TAG_NULL = 10521;
	
	public CustomerErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

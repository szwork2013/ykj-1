package com.gnet.app.customerHouseProperty;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class CustomerHousePropertyErrorBuilder extends  BaseErrorBuilder{


	/**
	 * 客户房产信息的编号为空
	 */
	public static final Integer ERROR_ID_NULL = 10701;
	
	/**
	 * 客户房产信息为空
	 */
	public static final Integer ERROR_CUSTOMER_HOUSE_PROPERTY_NULL = 10702;

	/** 楼盘名称为空 **/
	public static final Integer ERROR_BUILDING_NAME_NULL = 10703;
	
	/** 风格为空 **/
    public static final Integer ERROR_ROOM_STYLE_NULL = 10704;
    
    /** 装修进度为空 **/
    public static final Integer ERROR_DECORATE_PROCESS_NULL = 10705;
    
    /** 装修类型为空 **/
	public static final Integer ERROR_DECORATE_TYPE_NULL = 10706;
	
	/** 户型为空 **/
	public static final Integer ERROR_ROOM_MODEL_NULL = 10707;
	
	/** 没有这种装修风格 **/
    public static final Integer ERROR_ROOM_STYLE = 10708;
    
    /** 没有这种装修进度 **/
    public static final Integer ERROR_DECORATE_PROCESS = 10709;
    
    /** 没有这种装修类型 **/
	public static final Integer ERROR_DECORATE_TYPE = 10710;
	
	/** 没有这种户型 **/
	public static final Integer ERROR_ROOM_MODEL = 10711;
    
	public CustomerHousePropertyErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

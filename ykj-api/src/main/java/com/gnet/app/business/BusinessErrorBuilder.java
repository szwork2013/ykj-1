package com.gnet.app.business;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class BusinessErrorBuilder extends BaseErrorBuilder {
	
	
	/**
	 * 供货商名称为空错误
	 */
	public static final Integer ERROR_NAME_NULL = 11001;
	
	/**
	 * 联系电话为空错误
	 */
	public static final Integer ERROR_CONTACTNUMBER_NULL = 11003;
	
	/**
	 * 联系人为空错误
	 */
	public static final Integer ERROR_CONTACTPERSON_NULL = 11002;
	
	/**
	 * 联系地址为空错误
	 */
	public static final Integer ERROR_ADDRESS_NULL = 11004;
	
	/** 找不到该商家 **/
	public static final Integer ERROR_BUSINESS_NULL = 11005;
	
	/** 商家编号为空 **/
	public static final Integer ERROR_ID_NULL = 11006;
	
	public static final Integer ERROR_SALEBRANDS_TOOLONG = 11007;
	
	public static final Integer ERROR_ADDRESS_TOOLONG = 11008;
	
	public static final Integer ERROR_CONTACTPERSON_TOOLONG = 11009;
	
	public static final Integer ERROR_SERVICECALL_TOOLONG = 11010;
	
	public static final Integer ERROR_POSTCODE_TOOLONG = 11011;
	
	public static final Integer ERROR_CONTACTNUMBER_TOOLONG = 11012;
	
	public static final Integer ERROR_NAME_TOOLONG = 11013;
	
	public static final Integer ERROR_LOCATION_TOOLONG = 11014;
	
	public static final Integer ERROR_ID_TOOLONG = 11015;
	
	public static final Integer ERROR_LOGO_TOOLONG = 11016;
	
	/** 商家名称重复 **/
	public static final Integer ERROR_NAME_REPEAT = 11017;
	
	/** 商家下存在部门不允许删除 **/
	public static final Integer ERROR_EXIST_RELATION = 11018;
	
	
	public BusinessErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

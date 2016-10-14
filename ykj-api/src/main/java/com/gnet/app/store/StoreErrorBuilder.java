package com.gnet.app.store;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class StoreErrorBuilder extends BaseErrorBuilder {
	
	
	/**
	 * 门店名称为空错误
	 */
	public static final Integer ERROR_NAME_NULL = 12001;
	
	/**
	 * 联系人为空错误
	 */
	public static final Integer ERROR_CONTACTPERSON_NULL = 12002;
	
	/**
	 * 联系电话为空错误
	 */
	public static final Integer ERROR_CONTACTNUMBER_NULL = 12003;
	
	/**
	 * 商家编号为空错误
	 */
	public static final Integer ERROR_BUSINESSID_NULL = 12004;
	
	/** 门店编号为空 **/
	public static final Integer ERROR_ID_NULL = 12005;
	
	public static final Integer ERROR_ADDRESS_TOOLONG = 12006;
	
	public static final Integer ERROR_CONTACTPERSON_TOOLONG = 12007;
	
	public static final Integer ERROR_NAME_TOOLONG = 12008;
	
	public static final Integer ERROR_ID_TOOLONG = 12009;
	
	public static final Integer ERROR_BUSINESSID_TOOLONG = 12010;
	
	public static final Integer ERROR_CONTACTNUMBER_TOOLONG = 12011;
	
	/** 同一商家下的门店名称重复 **/
	public static final Integer ERROR_NAME_REPEAT = 12012;
	
	/** 门店信息为空 **/
	public static final Integer ERROR_STORE_NULL = 12013;

	/** 门店下有部门 **/
	public static final Integer ERROR_EXIST_RELATION = 12014;
	
	public StoreErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

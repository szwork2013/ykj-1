package com.gnet.app.supplier;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class SupplierErrorBuilder extends BaseErrorBuilder {
	
	
	/**
	 * 供货商名称为空错误
	 */
	public static final Integer ERROR_SUPPLIERNAME_NULL = 10901;
	
	/**
	 * 电子邮箱为空错误
	 */
	public static final Integer ERROR_CONTACTEMAIL_NULL = 10904;
	
	/**
	 * 联系人为空错误
	 */
	public static final Integer ERROR_CONTACTNAME_NULL = 10902;
	
	/**
	 * 商家编号为空错误
	 */
	public static final Integer ERROR_BUSINESSID_NULL = 10905;
	
	/**
	 * 联系电话为空错误
	 */
	public static final Integer ERROR_CONTACTPHONE_NULL = 10903;
	
	/** 找不到该品牌供货商 **/
	public static final Integer ERROR_SUPPLIER_NULL =  10904;
	
	/** 品牌供货商的编号为空 **/
	public static final Integer ERROR_ID_NULL = 10905;
	
	public static final Integer ERROR_CONTACTNAME_TOOLONG = 10906;
	
	public static final Integer ERROR_CONTACTPHONE_TOOLONG = 10907;
	
	public static final Integer ERROR_ID_TOOLONG = 10908;
	
	public static final Integer ERROR_SUPPLIERNAME_TOOLONG = 10909;
	
	public static final Integer ERROR_CONTACTADDRESS_TOOLONG = 10910;
	
	public static final Integer ERROR_BUSINESSID_TOOLONG = 10911;
	
	public static final Integer ERROR_CONTACTEMAIL_TOOLONG = 10912;
	
	/** 品牌供货商名称重复 **/
	public static final Integer ERROR_SUPPLIERNAME_REPEAT = 10913;
	
	public SupplierErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

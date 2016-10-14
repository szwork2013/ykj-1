package com.gnet.app.good;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class GoodErrorBuilder extends BaseErrorBuilder {
	
	/**
	 * 商品编号为空
	 */
	public static final Integer ERROR_ID_NULL = 16000;
	
	/**
	 * 编号无法找到对应的商品信息
	 */
	public static final Integer ERROR_GOOD_NULL = 16001;
	
	/**
	 * 商品类型重复
	 */
	public static final Integer ERROR_MODEL_REPEAT = 16002;
	
	/**
	 * 商品价格不能为空
	 */
	public static final Integer ERROR_PRICE_NULL = 16003;
	
	/**
	 * 商品最小单位不能为空
	 */
	public static final Integer ERROR_ATOMUNIT_NULL = 16004;
	
	/**
	 * 供货商编号为空错误
	 */
	public static final Integer ERROR_SUPPLIERID_NULL = 16005;
	
	/**
	 * 商品名称为空错误
	 */
	public static final Integer ERROR_NAME_NULL = 16003;
	
	/**
	 * 商品型号为空错误
	 */
	public static final Integer ERROR_MODEL_NULL = 16004;
	
	/**
	 * 商品处于使用中状态
	 */
	public static final Integer ERROR_GOOD_INUSE = 16005;
	
	public static final Integer ERROR_COLOR_TOOLONG = 16020;
	
	public static final Integer ERROR_SPECIFICATION_TOOLONG = 16021;
	
	public static final Integer ERROR_TYPE_TOOLONG = 16022;
	
	public static final Integer ERROR_NAME_TOOLONG = 16023;
	
	public static final Integer ERROR_MODEL_TOOLONG = 16024;
	
	public static final Integer ERROR_ID_TOOLONG = 16025;
	
	public static final Integer ERROR_SUPPLIERID_TOOLONG = 16026;
	
	public static final Integer ERROR_BUSINESSID_TOOLONG = 16027;
	
	
	public GoodErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

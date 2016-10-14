package com.gnet.app.codeword;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class CodewordErrorBuilder extends BaseErrorBuilder  {
	
	/**
	 * 数据字典项不存在
	 */
	public static final Integer ERROR_CODEWORD_NULL = 10201;
	
	/**
	 * 数据字典项类型不能为空
	 */
	public static final Integer ERROR_TYPEID_NULL = 10202;
	
	/**
	 * 数据字典项编码不能为空
	 */
	public static final Integer ERROR_CODE_NULL = 10203;
	
	/**
	 * 数据字典项值不能为空
	 */
	public static final Integer ERROR_VALUE_NULL = 10204;
	
	/**
	 * 数据字典项是否为系统内置
	 */
	public static final Integer ERROR_IS_SYSTEM = 10205;
	
	/**
	 * 数据字典项类型不存在
	 */
	public static final Integer ERROR_TYPE_NULL = 10206;

	public CodewordErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}
}

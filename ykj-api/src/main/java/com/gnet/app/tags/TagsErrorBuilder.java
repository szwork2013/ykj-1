package com.gnet.app.tags;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class TagsErrorBuilder extends BaseErrorBuilder{

	/** 标签编号为空 **/
	public static final Integer ERROR_ID_NULL = 10601;
	
	/** 标签名称为空 **/
	public static final Integer ERROR_NAME_NULL = 10602;
	
	/** 标签详细信息为空 **/
	public static final Integer ERROR_TAGS_NULL = 10603;
	
	/** 客户标签关联编号为空 **/
	public static final Integer ERROR_CUSTOMER_TAGS_NULL = 10604;
	
	
	public TagsErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

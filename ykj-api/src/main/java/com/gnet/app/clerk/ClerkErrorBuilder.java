package com.gnet.app.clerk;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class ClerkErrorBuilder extends BaseErrorBuilder {
	
	/**
	 * 公司人员不存在
	 */
	public static final Integer ERROR_CLERK_NULL = 10400;
	
	/**
	 * 公司人员编号不能为空
	 */
	public static final Integer ERROR_ID_NULL = 10401;
	
	/**
	 * 姓名为空错误
	 */
	public static final Integer ERROR_NAME_NULL = 10407;
	
	/**
	 * 性别为空过滤
	 */
	public static final Integer ERROR_SEX_NULL = 10408;
	
	/**
	 * 电话为空过滤
	 */
	public static final Integer ERROR_PHONE_NULL = 10409;
	
	/**
	 * 直属部门为空过滤
	 */
	public static final Integer ERROR_OFFICE_NULL = 10410;
	
	/**
	 * 角色身份为空过滤
	 */
	public static final Integer ERROR_ROLETYPE_NULL = 10411;
	
	public static final Integer ERROR_QQ_TOOLONG = 10421;
	
	public static final Integer ERROR_WECHAT_TOOLONG = 10422;
	
	public static final Integer ERROR_PHOTO_TOOLONG = 10423;
	
	public static final Integer ERROR_PHONESEC_TOOLONG = 10424;
	
	public static final Integer ERROR_PHONE_TOOLONG = 10426;
	
	public static final Integer ERROR_NAME_TOOLONG = 10427;
	
	public static final Integer ERROR_EMAIL_TOOLONG = 10430;
	
	
	public ClerkErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

package com.gnet.authentication.user;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class UserErrorBuilder extends BaseErrorBuilder {
	
	/**
	 * 创建失败
	 */
	public static final Integer ERROR_CREATED_ERROR = 10101;
	
	/**
	 * 更新失败
	 */
	public static final Integer ERROR_EDITED_ERROR = 10102;
	
	/**
	 * 用户名已经存在
	 */
	public static final Integer ERROR_USERNAME_EXISTS = 10103;

	/**
	 * 姓名为空
	 */
	public static final Integer ERROR_NAME_NULL = 10104;

	/**
	 * 密码为空
	 */
	public static final Integer ERROR_PASSWORD_NULL = 10105;

	/**
	 * 用户名为空
	 */
	public static final Integer ERROR_USERNAME_NULL = 10106;
	
	/**
	 * 角色为空
	 */
	public static final Integer ERROR_ROLE_NULL = 10107;
	
	/**
	 * 角色类型为空
	 */
	public static final Integer ERROR_ROLE_TYPE_NULL = 10108;
	
	/**
	 * 用户不存在
	 */
	public static final Integer ERROR_USER_NULL=10110;

	/**
	 * 两次输入的密码不一致
	 */
	public static final Integer ERROR_PASSWORD_DIFFERENCE = 10118;
	
	/**
	 * 手机号为空
	 */
	public static final Integer ERROR_TELEPHONE_NULL = 10119;
	
	/**
	 * 删除失败
	 */
	public static final Integer ERROR_DELETE_ERROR = 10120;
	

	
	public UserErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}

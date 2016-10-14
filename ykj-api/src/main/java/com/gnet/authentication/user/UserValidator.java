package com.gnet.authentication.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.app.authentication.user.UserMapper;
import com.gnet.security.user.CustomUser;
import com.gnet.utils.spring.SpringContextHolder;

public class UserValidator {
	
	private UserValidator(){}
	
	/**
	 * @param manager
	 * @param map
	 * @param customUser  
	 * @return
	 */
	static Map<String, Object> validatorBeforeUpdateUser(SysUser user, Map<String, Object> map, CustomUser customUser){
		Map<String, Object> errorcode = new HashMap<>();
		
		// 验证密码是否通过
		// 密码加密
		PasswordEncoder encode = SysUser.PASSWORD_ENCODER;
		String newDecodePassword = String.valueOf(map.get("password"));
		String newDecodeRepassword = String.valueOf(map.get("repassword"));
		if(newDecodePassword == null || newDecodeRepassword == null) {
			errorcode.put("msg", "密码不得为空");
			errorcode.put("code", UserErrorBuilder.ERROR_PASSWORD_NULL);
			return errorcode;
		} else if(!newDecodePassword.endsWith(newDecodeRepassword)) {
			errorcode.put("msg", "两次输入的密码不一致");
			errorcode.put("code", UserErrorBuilder.ERROR_PASSWORD_DIFFERENCE);
			return errorcode;
		}
		
		user.setPassword(encode.encode(newDecodePassword));
						
		return null;
	}
	
	/**
	 * @param manager
	 * @param map
	 * @param customUser  
	 * @return
	 */
	static Map<String, Object> validatorBeforeUpdateUser(SysUser user){
		UserService userService = SpringContextHolder.getBean(UserService.class);
		UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
		
		Map<String, Object> errorcode = new HashMap<>();
		
		//非空验证
		if (StringUtils.isBlank(user.getName())) {
			errorcode.put("msg", "姓名不能为空");
			errorcode.put("code", UserErrorBuilder.ERROR_NAME_NULL);
			return errorcode;
		}
		if (StringUtils.isBlank(user.getUsername())) {
			errorcode.put("msg", "用户名不能为空");
			errorcode.put("code", UserErrorBuilder.ERROR_USERNAME_NULL);
			return errorcode;
		}
		
		SysUser originUser = userMapper.selectByPrimaryKey(user.getId());
		if (null == originUser) {
			errorcode.put("msg", "用户不能为空");
			errorcode.put("code", UserErrorBuilder.ERROR_USER_NULL);
			return errorcode;
		}
						
		//用户名唯一性验证
		if (userService.isExists(user.getUsername(), originUser.getUsername())) {
			errorcode.put("msg", "用户名已经存在");
			errorcode.put("code", UserErrorBuilder.ERROR_USERNAME_EXISTS);
			return errorcode;
		}
		return null;
	}
	
}

package com.gnet.app.clerk;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.authentication.user.UserErrorBuilder;
import com.gnet.authentication.user.UserService;
import com.gnet.utils.spring.SpringContextHolder;

public class ClerkValidator {
	
	private ClerkValidator(){}
	
	static Map<String, Object> validateBeforeCreateClerk(Clerk clerk, Map<String, Object> extraMap) {
		Map<String, Object> map = new HashMap<>();
		UserService userService = SpringContextHolder.getBean(UserService.class);
		
		// 姓名不能为空过滤
		if (StringUtils.isBlank(clerk.getName())) {
			map.put("code", ClerkErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "姓名不能为空");
			return map;
		}
		
		// 性别不能为空过滤
		if (clerk.getSex() == null) {
			map.put("code", ClerkErrorBuilder.ERROR_SEX_NULL);
			map.put("msg", "性别不能为空");
			return map;
		}
		
		// 联系电话不能为空
		if (StringUtils.isBlank(clerk.getPhone())) {
			map.put("code", ClerkErrorBuilder.ERROR_PHONE_NULL);
			map.put("msg", "手机不能为空");
			return map;
		}
		
		// 部门不能为空
		if (StringUtils.isBlank(clerk.getOfficeId())) {
			map.put("code", ClerkErrorBuilder.ERROR_OFFICE_NULL);
			map.put("msg", "直属部门不能为空");
			return map;
		}
		
		// 角色身份不能为空
		if (clerk.getRoleType() == null) {
			map.put("code", ClerkErrorBuilder.ERROR_ROLETYPE_NULL);
			map.put("msg", "角色身份不能为空");
			return map;
		}
		
		// 检验长度
		Map<String, Object> lengthErrorCode = validateLength(clerk);
		if (lengthErrorCode != null) {
			return lengthErrorCode;
		}
		
		// 系统用户相关数据检验
		// 用户名不能为空
		if (extraMap.get("username") == null) {
			map.put("code", UserErrorBuilder.ERROR_USERNAME_NULL);
			map.put("msg", "用户名不能为空");
			return map;
		}
		
		SysUser originUser = userService.findByUsername(extraMap.get("username").toString());
						
		//用户名唯一性验证
		if (originUser != null && userService.isExists(extraMap.get("username").toString(), originUser.getUsername())) {
			map.put("msg", "用户名已经存在");
			map.put("code", UserErrorBuilder.ERROR_USERNAME_EXISTS);
			return map;
		}
		
		// 验证密码是否通过
		// 密码加密
		String newDecodePassword = String.valueOf(extraMap.get("password"));
		String newDecodeRepassword = String.valueOf(extraMap.get("repassword"));
		if (newDecodePassword == null || newDecodeRepassword == null) {
			map.put("msg", "密码不得为空");
			map.put("code", UserErrorBuilder.ERROR_PASSWORD_NULL);
			return map;
		}
		if (!newDecodePassword.endsWith(newDecodeRepassword)) {
			map.put("msg", "两次输入的密码不一致");
			map.put("code", UserErrorBuilder.ERROR_PASSWORD_DIFFERENCE);
			return map;
		}
		
		return null;
	}
	
	static Map<String, Object> validateBeforeUpdateClerk(Clerk clerk) {
		Map<String, Object> map = new HashMap<>();
		
		if (clerk.getId() == null) {
			map.put("code", ClerkErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "公司人员编号为空");
			return map;
		}
		
		// 姓名不能为空过滤
		if (StringUtils.isBlank(clerk.getName())) {
			map.put("code", ClerkErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "姓名不能为空");
			return map;
		}
		
		// 性别不能为空过滤
		if (clerk.getSex() == null) {
			map.put("code", ClerkErrorBuilder.ERROR_SEX_NULL);
			map.put("msg", "性别不能为空");
			return map;
		}
		
		// 联系电话不能为空
		if (StringUtils.isBlank(clerk.getPhone())) {
			map.put("code", ClerkErrorBuilder.ERROR_PHONE_NULL);
			map.put("msg", "手机不能为空");
			return map;
		}
		
		// 部门不能为空
		if (StringUtils.isBlank(clerk.getOfficeId())) {
			map.put("code", ClerkErrorBuilder.ERROR_OFFICE_NULL);
			map.put("msg", "直属部门不能为空");
			return map;
		}
		
		// 角色身份不能为空
		if (clerk.getRoleType() == null) {
			map.put("code", ClerkErrorBuilder.ERROR_ROLETYPE_NULL);
			map.put("msg", "角色身份不能为空");
			return map;
		}
		
		// 检验长度
		Map<String, Object> lengthErrorCode = validateLength(clerk);
		if (lengthErrorCode != null) {
			return lengthErrorCode;
		}

		return null;
	}
	
	static Map<String, Object> validateLength(Clerk clerk) {
		Map<String, Object> map = new HashMap<>();
		
		if (StringUtils.isNotBlank(clerk.getQQ()) && clerk.getQQ().length() > 20 ) {
			map.put("code", ClerkErrorBuilder.ERROR_QQ_TOOLONG);
			map.put("msg", "QQ长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(clerk.getWechat()) && clerk.getWechat().length() > 50 ) {
			map.put("code", ClerkErrorBuilder.ERROR_WECHAT_TOOLONG);
			map.put("msg", "微信长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(clerk.getPhoto()) && clerk.getPhoto().length() > 255 ) {
			map.put("code", ClerkErrorBuilder.ERROR_PHOTO_TOOLONG);
			map.put("msg", "照片路径长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(clerk.getPhoneSec()) && clerk.getPhoneSec().length() > 20 ) {
			map.put("code", ClerkErrorBuilder.ERROR_PHONESEC_TOOLONG);
			map.put("msg", "备用号码长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(clerk.getPhone()) && clerk.getPhone().length() > 20 ) {
			map.put("code", ClerkErrorBuilder.ERROR_PHONE_TOOLONG);
			map.put("msg", "号码长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(clerk.getName()) && clerk.getName().length() > 50 ) {
			map.put("code", ClerkErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "姓名长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(clerk.getEmail()) && clerk.getEmail().length() > 50 ) {
			map.put("code", ClerkErrorBuilder.ERROR_EMAIL_TOOLONG);
			map.put("msg", "邮箱长度不能超过50");
			return map;
		}
		
		return null;
	}

}
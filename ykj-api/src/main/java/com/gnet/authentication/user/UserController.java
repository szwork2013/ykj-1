package com.gnet.authentication.user;


import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.security.user.CustomUser;

@RepositoryRestController
@ExposesResourceFor(SysUser.class)
@RequestMapping("/api/user")
public class UserController {

	// TODO 更新当前用户customUser的方法
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<UserResource> user(Authentication authentication) {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		UserResourceAssembler userResourceAssembler = new UserResourceAssembler();
		UserResource userResource = userResourceAssembler.toResource(customUser);
		
		return ResponseEntity.ok(userResource);
	}
	
	// 更新
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateRole(
		Authentication authentication,
		@RequestBody SysUser user
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Date date = new Date();
		user.setId(customUser.getId());
		user.setModifyDate(date);
		
		//非空、唯一性验证
		Map<String, Object> validatorCode = UserValidator.validatorBeforeUpdateUser(user);
		
		if (null != validatorCode) {
			//验证不通过
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserErrorBuilder(Integer.valueOf(validatorCode.get("code").toString()), validatorCode.get("msg").toString()).build());
		}
		
		Boolean result =userService.update(user);
		if (result) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserErrorBuilder(UserErrorBuilder.ERROR_EDITED_ERROR, "更新错误").build());
	}
	
	// 更新密码
	@RequestMapping(method = RequestMethod.PATCH)
	public ResponseEntity<?> updateRole(
			Authentication authentication,
			// 需要包含：password，repassword
			@RequestBody Map<String, Object> map
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Date date = new Date();
		SysUser user = new SysUser();
		user.setId(customUser.getId());
		user.setModifyDate(date);
		
		//非空、唯一性验证
		Map<String, Object> validatorCode = UserValidator.validatorBeforeUpdateUser(user, map, customUser);
		
		if (null != validatorCode) {
			//验证不通过
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserErrorBuilder(Integer.valueOf(validatorCode.get("code").toString()), validatorCode.get("msg").toString()).build());
		}
		
		Boolean result = userService.update(user);
		if (result) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserErrorBuilder(UserErrorBuilder.ERROR_EDITED_ERROR, "更新错误").build());
	}
	
	// TODO 头像上传 还没做
	// 更新密码
	@RequestMapping(value="/upload", method = RequestMethod.PATCH)
	public ResponseEntity<?> uploadRole(
			Authentication authentication,
			// 需要包含：password，repassword,oldPassword
			@RequestBody Map<String, Object> map
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Date date = new Date();
		SysUser user = new SysUser();
		user.setId(customUser.getId());
		user.setModifyDate(date);
		
		//非空、唯一性验证
		Map<String, Object> validatorCode = UserValidator.validatorBeforeUpdateUser(user, map, customUser);
		
		if (null != validatorCode) {
			//验证不通过
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserErrorBuilder(Integer.valueOf(validatorCode.get("code").toString()), validatorCode.get("msg").toString()).build());
		}
		
		Boolean result = userService.update(user);
		if (result) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserErrorBuilder(UserErrorBuilder.ERROR_EDITED_ERROR, "更新错误").build());
	}
}


package com.gnet.authentication.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.core.Authentication;

import com.gnet.security.user.CustomUser;

public class UserResourceAssembler implements ResourceAssembler<CustomUser, UserResource> {

	@Autowired
	private Authentication authentication;
	
	@Override
	public UserResource toResource(CustomUser entity) {
		UserResource userResource = new UserResource(entity);
		userResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).user(authentication)).withSelfRel());
		return userResource;
	}

}

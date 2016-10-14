package com.gnet.app.business;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class BusinessResourceAssembler implements ResourceAssembler<Business, BusinessResource> {

	@Override
	public BusinessResource toResource(Business entity) {
		BusinessResource resource = new BusinessResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BusinessController.class).getBusiness(entity.getId())).withSelfRel());
		return resource;
	}
	
}
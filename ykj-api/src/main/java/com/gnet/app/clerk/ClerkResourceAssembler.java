package com.gnet.app.clerk;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class ClerkResourceAssembler implements ResourceAssembler<Clerk, ClerkResource> {

	@Override
	public ClerkResource toResource(Clerk entity) {
		ClerkResource resource = new ClerkResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ClerkController.class).getClerk(entity.getId())).withSelfRel());
		return resource;
	}
	
}
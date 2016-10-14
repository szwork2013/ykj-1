package com.gnet.app.good;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class GoodResourceAssembler implements ResourceAssembler<Good, GoodResource> {

	@Override
	public GoodResource toResource(Good entity) {
		GoodResource resource = new GoodResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GoodController.class).getGood(entity.getId())).withSelfRel());
		return resource;
	}
	
}
package com.gnet.app.store;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class StoreResourceAssembler implements ResourceAssembler<Store, StoreResource> {

	@Override
	public StoreResource toResource(Store entity) {
		StoreResource resource = new StoreResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(StoreController.class).getStore(entity.getId())).withSelfRel());
		return resource;
	}
	
}
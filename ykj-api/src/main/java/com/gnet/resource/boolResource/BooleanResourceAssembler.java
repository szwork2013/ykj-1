package com.gnet.resource.boolResource;

import org.springframework.hateoas.ResourceAssembler;

public class BooleanResourceAssembler implements ResourceAssembler<Boolean, BooleanResource> {

	@Override
	public BooleanResource toResource(Boolean entity) {
		return new BooleanResource(entity);
	}

}

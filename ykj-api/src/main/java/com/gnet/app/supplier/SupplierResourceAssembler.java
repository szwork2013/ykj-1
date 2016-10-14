package com.gnet.app.supplier;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class SupplierResourceAssembler implements ResourceAssembler<Supplier, SupplierResource> {

	@Override
	public SupplierResource toResource(Supplier entity) {
		SupplierResource resource = new SupplierResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SupplierController.class).getSupplier(entity.getId())).withSelfRel());
		return resource;
	}
	
}
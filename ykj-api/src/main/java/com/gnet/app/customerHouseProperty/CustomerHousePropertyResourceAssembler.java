package com.gnet.app.customerHouseProperty;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;


public class CustomerHousePropertyResourceAssembler implements ResourceAssembler<CustomerHouseProperty, CustomerHousePropertyResource>{
	
	@Override
	public CustomerHousePropertyResource toResource(CustomerHouseProperty entity) {
		CustomerHousePropertyResource resource = new CustomerHousePropertyResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerHousePropertyController.class).getCustomerHouseProperty(entity.getId())).withSelfRel());
		return resource;
	}


}

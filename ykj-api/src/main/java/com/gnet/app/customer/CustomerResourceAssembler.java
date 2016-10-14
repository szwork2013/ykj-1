package com.gnet.app.customer;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class CustomerResourceAssembler implements ResourceAssembler<Customer, CustomerResource>{

	@Override
	public CustomerResource toResource(Customer entity) {
		CustomerResource customerResource = new CustomerResource(entity);
		customerResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).getCustomer(entity.getId())).withSelfRel());
		return customerResource;
	}

}

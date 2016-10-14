package com.gnet.app.customerTrack;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class CustomerTrackResourceAssembler implements ResourceAssembler<CustomerTrack, CustomerTrackResource>{

	@Override
	public CustomerTrackResource toResource(CustomerTrack entity) {
		CustomerTrackResource customerTrackResource = new CustomerTrackResource(entity);
		customerTrackResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerTrackController.class).getCustomerTrack(entity.getId())).withSelfRel());
		return customerTrackResource;
	}

}

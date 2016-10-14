package com.gnet.app.order;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class OrderResourceAssembler implements ResourceAssembler<Order, OrderResource> {

	@Override
	public OrderResource toResource(Order entity) {
		OrderResource resource = new OrderResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderController.class).getOrder(entity.getId())).withSelfRel());
		return resource;
	}
	
}
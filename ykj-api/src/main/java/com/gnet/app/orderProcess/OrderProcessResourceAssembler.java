package com.gnet.app.orderProcess;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class OrderProcessResourceAssembler implements ResourceAssembler<OrderProcess, OrderProcessResource> {

	@Override
	public OrderProcessResource toResource(OrderProcess entity) {
		OrderProcessResource resource = new OrderProcessResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderProcessController.class).getOrderProcess(entity.getId())).withSelfRel());
		return resource;
	}
	
}
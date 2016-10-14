package com.gnet.app.orderGood;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class OrderGoodResourceAssembler implements ResourceAssembler<OrderGood, OrderGoodResource> {

	@Override
	public OrderGoodResource toResource(OrderGood entity) {
		OrderGoodResource resource = new OrderGoodResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderGoodController.class).getOrderGood(entity.getId())).withSelfRel());
		return resource;
	}
	
}
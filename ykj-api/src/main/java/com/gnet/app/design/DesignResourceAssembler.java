package com.gnet.app.design;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceResource;

public class DesignResourceAssembler implements ResourceAssembler<OrderSer, OrderServiceResource> {

	@Override
	public OrderServiceResource toResource(OrderSer entity) {
		OrderServiceResource resource = new OrderServiceResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(DesignController.class).getDesign(entity.getId())).withSelfRel());
		return resource;
	}
	
}
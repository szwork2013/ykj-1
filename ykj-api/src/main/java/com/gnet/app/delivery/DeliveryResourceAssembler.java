package com.gnet.app.delivery;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceResource;

public class DeliveryResourceAssembler implements ResourceAssembler<OrderSer, OrderServiceResource> {

	@Override
	public OrderServiceResource toResource(OrderSer entity) {
		OrderServiceResource resource = new OrderServiceResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(DeliveryController.class).getDelivery(entity.getId())).withSelfRel());
		return resource;
	}
	
}